package com.pdq.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "promo_codes")
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_code_id")
    private Long promoCodeId;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType; // PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    // Nếu discount type là PERCENTAGE, discountValue = 50 (50%)
    // Nếu discount type là FIXED_AMOUNT, discountValue = 50000 (50,000 VND)
    // Nếu discount type là FREE_SHIPPING, discountValue = 1 (luôn miễn phí)

    @Column(name = "minimum_order_amount", precision = 10, scale = 2)
    private BigDecimal minimumOrderAmount = BigDecimal.ZERO; // Đơn hàng tối thiểu

    @Column(name = "max_discount_amount", precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount; // Giảm tối đa (dùng cho PERCENTAGE)

    @Column(nullable = false)
    private Boolean active = true; // Kích hoạt hay không

    @Column(name = "usage_limit")
    private Integer usageLimit; // Giới hạn số lần sử dụng (null = không giới hạn)

    @Column(name = "usage_count")
    private Integer usageCount = 0; // Số lần đã sử dụng

    @Column(name = "used_by_users", columnDefinition = "TEXT")
    private String usedByUsers = ""; // Danh sách userId đã dùng (separated by comma)

    @Column(name = "start_date")
    private LocalDateTime startDate; // Ngày bắt đầu hoạt động

    @Column(name = "end_date")
    private LocalDateTime endDate; // Ngày kết thúc hoạt động

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy; // Admin tạo

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (usageCount == null) {
            usageCount = 0;
        }
        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public boolean isValid() {
        if (!active) return false;
        
        LocalDateTime now = LocalDateTime.now();
        if (startDate != null && now.isBefore(startDate)) return false;
        if (endDate != null && now.isAfter(endDate)) return false;
        
        if (usageLimit != null && usageCount >= usageLimit) return false;
        
        return true;
    }

    // Getters and Setters
    public Long getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(Long promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public String getUsedByUsers() {
        return usedByUsers;
    }

    public void setUsedByUsers(String usedByUsers) {
        this.usedByUsers = usedByUsers;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public enum DiscountType {
        PERCENTAGE,      // % giảm (e.g., 50%)
        FIXED_AMOUNT,    // Số tiền cố định (e.g., 50,000 VND)
        FREE_SHIPPING    // Miễn phí vận chuyển
    }
}
