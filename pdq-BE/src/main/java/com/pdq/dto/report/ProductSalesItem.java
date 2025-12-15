package com.pdq.dto.report;

import java.math.BigDecimal;

public class ProductSalesItem {

    private Long productId;
    private String productName;
    private Integer quantitySold;
    private BigDecimal unitPrice;
    private BigDecimal totalRevenue;

    public ProductSalesItem() {}

    public ProductSalesItem(Long productId, String productName, Integer quantitySold,
            BigDecimal unitPrice, BigDecimal totalRevenue) {
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
}
