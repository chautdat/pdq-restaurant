package com.pdq.dto;

import java.math.BigDecimal;
import java.util.List;

import com.pdq.dto.ProductSalesDTO;

public class RevenueReportResponse {

    private BigDecimal totalRevenue;
    private BigDecimal cashRevenue;
    private BigDecimal vnpayRevenue;

    private Long totalOrders;
    private Long cashOrders;
    private Long vnpayOrders;

    private List<ProductSalesDTO> productSales;

    public RevenueReportResponse() {}

    public RevenueReportResponse(BigDecimal totalRevenue, BigDecimal cashRevenue, BigDecimal vnpayRevenue,
            Long totalOrders, Long cashOrders, Long vnpayOrders, List<ProductSalesDTO> productSales) {
        this.totalRevenue = totalRevenue;
        this.cashRevenue = cashRevenue;
        this.vnpayRevenue = vnpayRevenue;
        this.totalOrders = totalOrders;
        this.cashOrders = cashOrders;
        this.vnpayOrders = vnpayOrders;
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

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Long getCashOrders() {
        return cashOrders;
    }

    public void setCashOrders(Long cashOrders) {
        this.cashOrders = cashOrders;
    }

    public Long getVnpayOrders() {
        return vnpayOrders;
    }

    public void setVnpayOrders(Long vnpayOrders) {
        this.vnpayOrders = vnpayOrders;
    }

    public List<ProductSalesDTO> getProductSales() {
        return productSales;
    }

    public void setProductSales(List<ProductSalesDTO> productSales) {
        this.productSales = productSales;
    }
}
