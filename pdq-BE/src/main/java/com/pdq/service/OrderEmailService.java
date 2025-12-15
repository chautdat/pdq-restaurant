package com.pdq.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pdq.entity.Order;
import com.pdq.entity.OrderItem;
import com.pdq.entity.PaymentMethod;

@Service
public class OrderEmailService {

    private static final Logger log = LoggerFactory.getLogger(OrderEmailService.class);

    private final EmailService emailService;

    @Value("${app.email.admin-email:}")
    private String adminEmail;

    public OrderEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Send order placed success email.
     */
    public void sendOrderPlacedSuccess(Order order) {
        try {
            log.info("🎉 Sending order placed email for: {}", order.getOrderId());

            Map<String, Object> vars = new HashMap<>();
            vars.put("customerName", order.getRecipientName());
            vars.put("orderNumber", order.getOrderId() != null ? order.getOrderId().toString() : "N/A");
            vars.put("orderDate", formatDateTime(order.getCreatedAt()));
            vars.put("paymentMethod", formatPaymentMethod(order.getPaymentMethod()));
            vars.put("deliveryAddress", order.getDeliveryAddress());
            vars.put("phone", order.getPhone());
            vars.put("orderItems", mapOrderItems(order.getItems()));
            vars.put("totalAmount", formatCurrency(order.getFinalAmount()));

            emailService.sendHtmlEmail(
                    order.getUser().getEmail(),
                    "🎉 Đặt hàng thành công - #" + vars.get("orderNumber"),
                    "order-placed-success",
                    vars
            );

        } catch (Exception e) {
            log.error("❌ Failed to send order placed email", e);
        }
    }

    /**
     * Send delivery completed email with voucher.
     */
    public void sendDeliveryCompleted(Order order) {
        try {
            log.info("🎊 Sending delivery completed email for: {}", order.getOrderId());

            String voucherCode = generateVoucherCode();
            LocalDateTime expiry = LocalDateTime.now().plusDays(30);

            Map<String, Object> vars = new HashMap<>();
            vars.put("customerName", order.getRecipientName());
            vars.put("orderNumber", order.getOrderId() != null ? order.getOrderId().toString() : "N/A");
            vars.put("orderDate", formatDateTime(order.getCreatedAt()));
            vars.put("deliveryDate", formatDate(LocalDateTime.now()));
            vars.put("deliveryTime", formatTime(LocalDateTime.now()));
            vars.put("deliveryAddress", order.getDeliveryAddress());
            vars.put("recipientName", order.getRecipientName());
            vars.put("phone", order.getPhone());
            vars.put("paymentMethod", formatPaymentMethod(order.getPaymentMethod()));
            vars.put("orderItems", mapOrderItems(order.getItems()));
            vars.put("totalAmount", formatCurrency(order.getFinalAmount()));
            vars.put("discountCode", voucherCode);
            vars.put("discountExpiry", formatDate(expiry));

            emailService.sendHtmlEmail(
                    order.getUser().getEmail(),
                    "🎉 Giao hàng thành công - #" + vars.get("orderNumber"),
                    "delivery-completed",
                    vars
            );

            log.info("✅ Delivery email sent with voucher: {}", voucherCode);

        } catch (Exception e) {
            log.error("❌ Failed to send delivery email", e);
        }
    }

    /**
     * Send admin alert for new order.
     */
    public void sendNewOrderAlert(Order order) {
        if (adminEmail == null || adminEmail.isBlank()) {
            return;
        }
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("orderNumber", order.getOrderNumber());
            vars.put("customerName", order.getRecipientName());
            vars.put("phone", order.getPhone());
            vars.put("deliveryAddress", order.getDeliveryAddress());
            vars.put("totalAmount", formatCurrency(order.getFinalAmount()));
            vars.put("paymentMethod", formatPaymentMethod(order.getPaymentMethod()));

            emailService.sendHtmlEmail(
                    adminEmail,
                    "🔔 Đơn hàng mới #" + order.getOrderNumber(),
                    "admin-new-order",
                    vars
            );
        } catch (Exception e) {
            log.error("❌ Failed to send admin alert", e);
        }
    }

    // ========== HELPERS ==========

    private List<Map<String, Object>> mapOrderItems(List<OrderItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", item.getProductName());
            map.put("quantity", item.getQuantity());
            map.put("price", formatCurrency(
                    item.getSubtotal() != null
                            ? item.getSubtotal()
                            : item.getPrice() != null && item.getQuantity() != null
                                ? item.getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity()))
                                : java.math.BigDecimal.ZERO
            ));
            return map;
        }).collect(Collectors.toList());
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
    }

    private String formatDate(LocalDateTime dt) {
        return dt != null ? dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";
    }

    private String formatTime(LocalDateTime dt) {
        return dt != null ? dt.format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A";
    }

    private String formatCurrency(java.math.BigDecimal amount) {
        return amount != null ? String.format("%,d₫", amount.longValue()) : "0₫";
    }

    private String formatPaymentMethod(PaymentMethod method) {
        if (method == null) return "N/A";
        return switch (method) {
            case vnpay -> "VNPay";
            case cash -> "Tiền mặt";
            default -> method.name();
        };
    }

    private String generateVoucherCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder("NEXT");
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return code.toString();
    }
}
