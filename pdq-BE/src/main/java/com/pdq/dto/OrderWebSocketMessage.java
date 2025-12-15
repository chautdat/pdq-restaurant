package com.pdq.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class OrderWebSocketMessage {
    private Long orderId;
    private Long userId;
    private String userName;
    private String status;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private List<Map<String, Object>> items;
    private String message;
    private Long timestamp;

    public OrderWebSocketMessage() {
    }

    public OrderWebSocketMessage(Long orderId, Long userId, String userName, String status,
                                 BigDecimal totalAmount, String paymentMethod,
                                 List<Map<String, Object>> items, String message, Long timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.items = items;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static class Builder {
        private Long orderId;
        private Long userId;
        private String userName;
        private String status;
        private BigDecimal totalAmount;
        private String paymentMethod;
        private List<Map<String, Object>> items;
        private String message;
        private Long timestamp;

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder items(List<Map<String, Object>> items) {
            this.items = items;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public OrderWebSocketMessage build() {
            return new OrderWebSocketMessage(
                    orderId, userId, userName, status, totalAmount,
                    paymentMethod, items, message, timestamp
            );
        }
    }
}
