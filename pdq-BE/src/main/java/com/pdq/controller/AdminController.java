package com.pdq.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdq.dto.admin.StatisticsResponse;
import com.pdq.dto.common.ApiResponse;
import com.pdq.dto.common.PageResponse;
import com.pdq.entity.User;
import com.pdq.entity.UserRole;
import com.pdq.repository.UserRepository;
import com.pdq.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;

    public AdminController(AdminService adminService, UserRepository userRepository) {
        this.adminService = adminService;
        this.userRepository = userRepository;
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<StatisticsResponse>> getStatistics() {
        StatisticsResponse stats = adminService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success("Statistics retrieved successfully", stats));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<UserInfo>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserInfo> users = userPage.getContent().stream()
                .map(this::mapToUserInfo)
                .collect(Collectors.toList());

        PageResponse<UserInfo> response = new PageResponse<>(
                users,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );

        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<ApiResponse<UserInfo>> updateUserRole(
            @PathVariable Long userId,
            @RequestParam String role) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            UserRole newRole = UserRole.valueOf(role.toLowerCase());
            user.setRole(newRole);
            userRepository.save(user);

            return ResponseEntity.ok(ApiResponse.success(
                    "User role updated successfully", 
                    mapToUserInfo(user)));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role");
        }
    }

    private UserInfo mapToUserInfo(User user) {
        UserInfo info = new UserInfo();
        info.setUserId(user.getUserId());
        info.setUsername(user.getUsername());
        info.setEmail(user.getEmail());
        info.setFullName(user.getFullName());
        info.setPhone(user.getPhone());
        info.setRole(user.getRole().name());
        info.setStatus(user.getStatus().name());
        info.setCreatedAt(user.getCreatedAt());
        return info;
    }

    // Inner DTO class
    public static class UserInfo {
        private Long userId;
        private String username;
        private String email;
        private String fullName;
        private String phone;
        private String role;
        private String status;
        private java.time.LocalDateTime createdAt;

        public UserInfo() {}

        // Getters and Setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public java.time.LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(java.time.LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}