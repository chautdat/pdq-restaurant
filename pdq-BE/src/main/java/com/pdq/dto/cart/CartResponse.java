package com.pdq.dto.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartResponse {
    private Long cartId;
    private Integer totalItems;
    private BigDecimal totalPrice;
    private List<CartItemResponse> items;

    public CartResponse() {
        this.items = new ArrayList<>();
    }

    public CartResponse(Long cartId, Integer totalItems, BigDecimal totalPrice, List<CartItemResponse> items) {
        this.cartId = cartId;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    // Inner class
    public static class CartItemResponse {
        private Long cartItemId;
        private Long productId;
        private String productName;
        private String productImage;
        private BigDecimal price;
        private BigDecimal discountPrice;
        private Integer quantity;
        private BigDecimal subtotal;
        private String notes;

        public CartItemResponse() {}

        // Getters and Setters
        public Long getCartItemId() {
            return cartItemId;
        }

        public void setCartItemId(Long cartItemId) {
            this.cartItemId = cartItemId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(BigDecimal discountPrice) {
            this.discountPrice = discountPrice;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}