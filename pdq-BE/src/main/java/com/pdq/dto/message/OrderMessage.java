package com.pdq.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private String orderNumber;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;

    // Customer info
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String deliveryAddress;

    // Order details
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private BigDecimal discount;
    private List<OrderItemMessage> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemMessage implements Serializable {
        private static final long serialVersionUID = 1L;

        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
    }
}
