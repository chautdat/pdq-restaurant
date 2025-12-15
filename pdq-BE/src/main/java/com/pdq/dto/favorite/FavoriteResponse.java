package com.pdq.dto.favorite;

import java.time.LocalDateTime;

import com.pdq.dto.product.ProductResponse;

public class FavoriteResponse {
    private Long favoriteId;
    private ProductResponse product;
    private LocalDateTime createdAt;

    public FavoriteResponse() {}

    // Getters and Setters
    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}