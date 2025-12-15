package com.pdq.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pdq.entity.Category;

public class CategoryResponse {

    public CategoryResponse() {}

    @JsonProperty("categoryId")
    private Long categoryId;

    @JsonProperty("categoryName")
    private String categoryName;

    @JsonProperty("categorySlug")
    private String categorySlug;

    @JsonProperty("description")
    private String description;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("displayOrder")
    private Integer displayOrder;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("itemCount")
    private Long itemCount;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    // Mapping from entity
    public CategoryResponse(Category category, Long itemCount) {
        this.categoryId = category.getCategoryId().longValue();
        this.categoryName = category.getCategoryName();
        this.categorySlug = category.getCategorySlug();
        this.description = category.getDescription();
        this.imageUrl = category.getImageUrl();
        this.displayOrder = category.getDisplayOrder();
        this.isActive = category.getIsActive();
        this.itemCount = itemCount;
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }

    // GETTERS & SETTERS
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getItemCount() {
        return itemCount;
    }

    public void setItemCount(Long itemCount) {
        this.itemCount = itemCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
