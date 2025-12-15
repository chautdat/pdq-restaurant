package com.pdq.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ShiftEarningsResponse {

    private LocalDate date;
    private BigDecimal totalDailyRevenue;
    private Long totalDailyOrders;
    private List<ShiftReport> shifts;

    public ShiftEarningsResponse() {}

    public ShiftEarningsResponse(LocalDate date, BigDecimal totalDailyRevenue, Long totalDailyOrders, List<ShiftReport> shifts) {
        this.date = date;
        this.totalDailyRevenue = totalDailyRevenue;
        this.totalDailyOrders = totalDailyOrders;
        this.shifts = shifts;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotalDailyRevenue() {
        return totalDailyRevenue;
    }

    public void setTotalDailyRevenue(BigDecimal totalDailyRevenue) {
        this.totalDailyRevenue = totalDailyRevenue;
    }

    public Long getTotalDailyOrders() {
        return totalDailyOrders;
    }

    public void setTotalDailyOrders(Long totalDailyOrders) {
        this.totalDailyOrders = totalDailyOrders;
    }

    public List<ShiftReport> getShifts() {
        return shifts;
    }

    public void setShifts(List<ShiftReport> shifts) {
        this.shifts = shifts;
    }
}
