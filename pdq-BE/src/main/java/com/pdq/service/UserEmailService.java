package com.pdq.service;

import com.pdq.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserEmailService {

    private static final Logger log = LoggerFactory.getLogger(UserEmailService.class);
    private final EmailService emailService;

    public UserEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendVerificationEmail(User user, String verifyLink) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("name", user.getFullName());
            vars.put("verifyLink", verifyLink);

            emailService.sendHtmlEmail(
                    user.getEmail(),
                    "Xác thực tài khoản PDQ",
                    "verify-account",
                    vars
            );
        } catch (Exception e) {
            log.error("❌ sendVerificationEmail failed for {}", user.getEmail(), e);
        }
    }

    public void sendResetPasswordEmail(String email, String resetLink) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("resetLink", resetLink);

            emailService.sendHtmlEmail(
                    email,
                    "Đặt lại mật khẩu PDQ",
                    "reset-password",
                    vars
            );
        } catch (Exception e) {
            log.error("❌ sendResetPasswordEmail failed for {}", email, e);
        }
    }

    public void sendWelcomeEmail(User user) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("name", user.getFullName());

            emailService.sendHtmlEmail(
                    user.getEmail(),
                    "Chào mừng đến PDQ Restaurant",
                    "welcome-email",
                    vars
            );
        } catch (Exception e) {
            log.error("❌ sendWelcomeEmail failed for {}", user.getEmail(), e);
        }
    }
}
