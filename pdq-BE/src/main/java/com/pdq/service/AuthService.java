package com.pdq.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pdq.dto.auth.AuthResponse;
import com.pdq.dto.auth.LoginRequest;
import com.pdq.dto.auth.RegisterRequest;
import com.pdq.entity.User;
import com.pdq.entity.UserRole;
import com.pdq.entity.UserStatus;
import com.pdq.event.PasswordResetRequestedEvent;
import com.pdq.event.UserRegisteredEvent;
import com.pdq.exception.BadRequestException;
import com.pdq.exception.ResourceNotFoundException;
import com.pdq.repository.UserRepository;
import com.pdq.security.JwtService;
import org.springframework.context.ApplicationEventPublisher;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;
    private final PromoCodeService promoCodeService;

    public AuthService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService,
                      AuthenticationManager authenticationManager,
                      ApplicationEventPublisher eventPublisher,
                      PromoCodeService promoCodeService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.eventPublisher = eventPublisher;
        this.promoCodeService = promoCodeService;
    }

    // ✅ ĐĂNG KÝ - ĐÃ FIX
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.error("Email already exists");
        }

        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {
            return AuthResponse.error("Username already exists");
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(UserRole.ROLE_CUSTOMER); // ✅ FIX: Dùng ROLE_CUSTOMER
        user.setStatus(UserStatus.active);
        user.setEmailVerifiedAt(LocalDateTime.now()); // Auto verify

        userRepository.save(user);

        // 🎁 Generate signup promo code (50% shipping discount, single use, 30-day validity)
        try {
            promoCodeService.generateSignupPromoCode(user);
        } catch (Exception e) {
            System.err.println("⚠️ Generate signup promo code failed: " + e.getMessage());
            // Don't fail registration if promo generation fails
        }

        // Generate token
        String token = jwtService.generateToken(user);

        // User info
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setFullName(user.getFullName());
        userInfo.setRole(user.getRole().name()); // ✅ Trả về "ROLE_CUSTOMER"

        // Publish user registered event (email handled async)
        try {
            eventPublisher.publishEvent(new UserRegisteredEvent(user));
        } catch (Exception e) {
            System.err.println("⚠️ Publish UserRegisteredEvent failed: " + e.getMessage());
        }

        return AuthResponse.success("Registration successful", token, userInfo);
    }

    // ✅ ĐĂNG NHẬP - ĐÃ FIX
    public AuthResponse login(LoginRequest request) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🔐 LOGIN ATTEMPT");
        System.out.println("📧 Username/Email: " + request.getUsername());
        
        // Authenticate
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), 
                request.getPassword()
            )
        );
        System.out.println("✅ Authentication successful");

        // ✅ FIX: Tìm user bằng username HOẶC email
        User user = userRepository.findByUsernameOrEmail(
                request.getUsername(), 
                request.getUsername()
            )
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        System.out.println("👤 User found: " + user.getUsername());
        System.out.println("🔐 Role: " + user.getRole());
        System.out.println("📊 Status: " + user.getStatus());

        // Check status
        if (user.getStatus() != UserStatus.active) {
            throw new BadRequestException("Account locked or no access permission");
        }

        // Generate token
        String token = jwtService.generateToken(user);
        System.out.println("🔑 Token generated");

        // User info
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setFullName(user.getFullName());
        userInfo.setRole(user.getRole().name()); // ✅ "ROLE_CUSTOMER"
        
        System.out.println("✅ Login successful for: " + user.getUsername());
        System.out.println("═══════════════════════════════════════════════════════");

        return AuthResponse.success("Login successful", token, userInfo);
    }

    // ✅ GET CURRENT USER
    public AuthResponse.UserInfo getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setFullName(user.getFullName());
        userInfo.setRole(user.getRole().name());

        return userInfo;
    }

    // ✅ VERIFY PASSWORD
    @Transactional
    public boolean verifyOldPassword(String username, String oldPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    // ✅ CHANGE PASSWORD
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        // Validate new password
        if (!isValidPassword(newPassword)) {
            throw new BadRequestException("Password does not meet security standards");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // ✅ VALIDATE PASSWORD
    private boolean isValidPassword(String password) {
        return password.length() >= 8 && 
               password.matches(".*[A-Za-z].*") && 
               password.matches(".*\\d.*");
    }

    /**
     * Optional helper to publish password reset request event.
     */
    public void publishPasswordReset(String email, String resetLink) {
        try {
            eventPublisher.publishEvent(new PasswordResetRequestedEvent(email, resetLink));
        } catch (Exception e) {
            System.err.println("⚠️ Publish PasswordResetRequestedEvent failed: " + e.getMessage());
        }
    }
}
