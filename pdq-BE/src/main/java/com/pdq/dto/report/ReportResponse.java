package com.pdq.dto.report;

import java.math.BigDecimal;
import java.util.List;

public class ReportResponse {

    private BigDecimal totalRevenue;
    private BigDecimal cashRevenue;
    private BigDecimal vnpayRevenue;
    private Integer cashOrders;
    private Integer vnpayOrders;
    private Integer totalOrders;
    private List<ProductSalesItem> productSales;

    public ReportResponse() {}

    public ReportResponse(BigDecimal totalRevenue, BigDecimal cashRevenue, BigDecimal vnpayRevenue,
            Integer cashOrders, Integer vnpayOrders, Integer totalOrders, List<ProductSalesItem> productSales) {
        this.totalRevenue = totalRevenue;
        this.cashRevenue = cashRevenue;
        this.vnpayRevenue = vnpayRevenue;
        this.cashOrders = cashOrders;
        this.vnpayOrders = vnpayOrders;
        this.totalOrders = totalOrders;
        this.productSales = productSales;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getCashRevenue() {
        return cashRevenue;
    }

    public void setCashRevenue(BigDecimal cashRevenue) {
        this.cashRevenue = cashRevenue;
    }

    public BigDecimal getVnpayRevenue() {
        return vnpayRevenue;
    }

    public void setVnpayRevenue(BigDecimal vnpayRevenue) {
        this.vnpayRevenue = vnpayRevenue;
    }

    public Integer getCashOrders() {
        return cashOrders;
    }

    public void setCashOrders(Integer cashOrders) {
        this.cashOrders = cashOrders;
    }

    public Integer getVnpayOrders() {
        return vnpayOrders;
    }

    public void setVnpayOrders(Integer vnpayOrders) {
        this.vnpayOrders = vnpayOrders;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public List<ProductSalesItem> getProductSales() {
        return productSales;
    }

    public void setProductSales(List<ProductSalesItem> productSales) {
        this.productSales = productSales;
    }
}
