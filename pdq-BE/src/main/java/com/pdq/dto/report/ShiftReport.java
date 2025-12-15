package com.pdq.dto.report;

import java.math.BigDecimal;
import java.util.List;

public class ShiftReport {

    private String shiftType;
    private String shiftName;
    private String timeRange;
    private BigDecimal revenue;
    private BigDecimal cashRevenue;
    private BigDecimal vnpayRevenue;
    private Long totalOrders;
    private Long cashOrders;
    private Long vnpayOrders;
    private BigDecimal averageOrderValue;
    private List<ShiftProductSale> productSales;

    public ShiftReport() {}

    public ShiftReport(String shiftType, String shiftName, String timeRange, BigDecimal revenue, BigDecimal cashRevenue,
            BigDecimal vnpayRevenue, Long totalOrders, Long cashOrders, Long vnpayOrders, BigDecimal averageOrderValue,
            List<ShiftProductSale> productSales) {
        this.shiftType = shiftType;
        this.shiftName = shiftName;
        this.timeRange = timeRange;
        this.revenue = revenue;
        this.cashRevenue = cashRevenue;
        this.vnpayRevenue = vnpayRevenue;
        this.totalOrders = totalOrders;
        this.cashOrders = cashOrders;
        this.vnpayOrders = vnpayOrders;
        this.averageOrderValue = averageOrderValue;
        this.productSales = productSales;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
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

    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public List<ShiftProductSale> getProductSales() {
        return productSales;
    }

    public void setProductSales(List<ShiftProductSale> productSales) {
        this.productSales = productSales;
    }
}
