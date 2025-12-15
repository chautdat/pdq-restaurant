package com.pdq.dto.report;

import java.math.BigDecimal;

public class ShiftProductSale {

    private Long productId;
    private String productName;
    private String categoryName;
    private Integer quantitySold;
    private BigDecimal unitPrice;
    private BigDecimal totalRevenue;
    private BigDecimal percentage;

    public ShiftProductSale() {}

    public ShiftProductSale(Long productId, String productName, String categoryName, Integer quantitySold,
            BigDecimal unitPrice, BigDecimal totalRevenue, BigDecimal percentage) {
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.totalRevenue = totalRevenue;
        this.percentage = percentage;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
