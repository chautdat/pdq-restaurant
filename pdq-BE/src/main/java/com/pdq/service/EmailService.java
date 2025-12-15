package com.pdq.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Safe, non-blocking email sender.
 * - Never lets email errors crash the backend or async thread.
 * - SMTP/auth errors are logged only.
 * - Supports global toggle app.email.enabled.
 * - Handles from-name encoding fallback.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${app.email.from:no-reply@pdq-restaurant.local}")
    private String fromEmail;

    @Value("${app.email.from-name:PDQ Restaurant}")
    private String fromName;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Send HTML email using Thymeleaf template (async + safe).
     */
    @Async
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        if (!emailEnabled) {
            log.info("[SKIP] email disabled. Skip HTML email to={} subject={}", to, subject);
            return;
        }
        if (isBlank(to)) {
            log.warn("Skip HTML email because 'to' is blank. subject={} template={}", subject, templateName);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            try {
                helper.setFrom(fromEmail, fromName);
            } catch (UnsupportedEncodingException e) {
                log.warn("⚠️ from-name encoding invalid, fallback to fromEmail. cause={}", e.getMessage());
                helper.setFrom(fromEmail);
            }

            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);
            String html = templateEngine.process("email/" + templateName, context);
            helper.setText(html, true);

            mailSender.send(message);
            log.info("✅ HTML email sent to={}", to);
        } catch (Exception e) {
            log.error("❌ Failed to send HTML email to={} subject={} cause={}", to, subject, e.getMessage(), e);
        }
    }

    /**
     * Send simple text email (async + safe).
     */
    @Async
    public void sendSimpleEmail(String to, String subject, String text) {
        if (!emailEnabled) {
            log.info("[SKIP] email disabled. Skip simple email to={} subject={}", to, subject);
            return;
        }
        if (isBlank(to)) {
            log.warn("Skip simple email because 'to' is blank. subject={}", subject);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("✅ Simple email sent to={}", to);
        } catch (Exception e) {
            log.error("❌ Failed to send simple email to={} subject={} cause={}", to, subject, e.getMessage(), e);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
