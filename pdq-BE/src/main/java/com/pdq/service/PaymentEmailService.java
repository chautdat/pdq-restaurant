package com.pdq.service;

import com.pdq.entity.Order;
import com.pdq.entity.PaymentMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentEmailService {

    private static final Logger log = LoggerFactory.getLogger(PaymentEmailService.class);

    private final EmailService emailService;

    @Value("${app.email.admin-email:}")
    private String adminEmail;

    public PaymentEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendPaymentSuccessToCustomer(Order order, PaymentMethod method) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("customerName", order.getRecipientName());
            vars.put("orderNumber", order.getOrderNumber());
            vars.put("amount", formatCurrency(order));
            vars.put("paymentMethod", method != null ? method.name().toUpperCase() : "N/A");
            vars.put("deliveryAddress", order.getDeliveryAddress());

            emailService.sendHtmlEmail(
                    order.getUser().getEmail(),
                    "Thanh toán thành công #" + order.getOrderNumber(),
                    "payment-success",
                    vars
            );
        } catch (Exception e) {
            log.error("❌ sendPaymentSuccessToCustomer failed for order {}", order.getOrderNumber(), e);
        }
    }

    public void sendPaymentSuccessToAdmin(Order order, PaymentMethod method) {
        if (adminEmail == null || adminEmail.isBlank()) {
            return;
        }
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("orderNumber", order.getOrderNumber());
            vars.put("customerName", order.getRecipientName());
            vars.put("phone", order.getPhone());
            vars.put("amount", formatCurrency(order));
            vars.put("paymentMethod", method != null ? method.name().toUpperCase() : "N/A");

            emailService.sendHtmlEmail(
                    adminEmail,
                    "Thanh toán online thành công #" + order.getOrderNumber(),
                    "admin-payment-success",
                    vars
            );
        } catch (Exception e) {
            log.error("❌ sendPaymentSuccessToAdmin failed for order {}", order.getOrderNumber(), e);
        }
    }

    private String formatCurrency(Order order) {
        if (order.getFinalAmount() == null) return "0₫";
        return String.format("%,d₫", order.getFinalAmount().longValue());
    }
}
