package com.pdq.dto;

import java.math.BigDecimal;

public class ProductSalesDTO {

    private Long productId;
    private String productName;
    private Long quantitySold;
    private BigDecimal unitPrice;
    private BigDecimal totalRevenue;

    public ProductSalesDTO() {}

    public ProductSalesDTO(Long productId, String productName, Long quantitySold, BigDecimal unitPrice,
            BigDecimal totalRevenue) {
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.totalRevenue = totalRevenue;
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

    public Long getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Long quantitySold) {
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
}
