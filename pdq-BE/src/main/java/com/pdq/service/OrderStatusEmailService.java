package com.pdq.service;

import com.pdq.entity.Order;
import com.pdq.entity.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderStatusEmailService {

    private static final Logger log = LoggerFactory.getLogger(OrderStatusEmailService.class);
    private final EmailService emailService;

    @Value("${app.email.admin-email:}")
    private String adminEmail;

    public OrderStatusEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendStatusUpdateEmail(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("customerName", order.getRecipientName());
            vars.put("orderNumber", order.getOrderNumber());
            vars.put("oldStatus", safeStatus(oldStatus));
            vars.put("newStatus", safeStatus(newStatus));
            vars.put("deliveryAddress", order.getDeliveryAddress());

            emailService.sendHtmlEmail(
                    order.getUser().getEmail(),
                    "Cập nhật trạng thái đơn #" + order.getOrderNumber(),
                    "order-status-update",
                    vars
            );

            if (newStatus == OrderStatus.cancelled && adminEmail != null && !adminEmail.isBlank()) {
                emailService.sendHtmlEmail(
                        adminEmail,
                        "Đơn hàng bị hủy #" + order.getOrderNumber(),
                        "admin-order-cancelled",
                        vars
                );
            }
        } catch (Exception e) {
            log.error("❌ sendStatusUpdateEmail failed for order {}", order.getOrderNumber(), e);
        }
    }

    private String safeStatus(OrderStatus status) {
        return status != null ? status.name().toUpperCase() : "UNKNOWN";
    }
}
