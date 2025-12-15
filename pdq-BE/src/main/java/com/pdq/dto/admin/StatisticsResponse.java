package com.pdq.dto.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StatisticsResponse {
    private DashboardStats dashboardStats;
    private List<TopProduct> topProducts;
    private List<RevenueByDate> revenueByDate;

    public StatisticsResponse() {
        this.topProducts = new ArrayList<>();
        this.revenueByDate = new ArrayList<>();
    }

    // Getters and Setters
    public DashboardStats getDashboardStats() {
        return dashboardStats;
    }

    public void setDashboardStats(DashboardStats dashboardStats) {
        this.dashboardStats = dashboardStats;
    }

    public List<TopProduct> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(List<TopProduct> topProducts) {
        this.topProducts = topProducts;
    }

    public List<RevenueByDate> getRevenueByDate() {
        return revenueByDate;
    }

    public void setRevenueByDate(List<RevenueByDate> revenueByDate) {
        this.revenueByDate = revenueByDate;
    }

    // Inner classes
    public static class DashboardStats {
        private Long totalOrders;
        private Long pendingOrders;
        private Long completedOrders;
        private Long totalUsers;
        private Long totalProducts;
        private BigDecimal totalRevenue;
        private BigDecimal todayRevenue;

        public DashboardStats() {}

        // Getters and Setters
        public Long getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(Long totalOrders) {
            this.totalOrders = totalOrders;
        }

        public Long getPendingOrders() {
            return pendingOrders;
        }

        public void setPendingOrders(Long pendingOrders) {
            this.pendingOrders = pendingOrders;
        }

        public Long getCompletedOrders() {
            return completedOrders;
        }

        public void setCompletedOrders(Long completedOrders) {
            this.completedOrders = completedOrders;
        }

        public Long getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(Long totalUsers) {
            this.totalUsers = totalUsers;
        }

        public Long getTotalProducts() {
            return totalProducts;
        }

        public void setTotalProducts(Long totalProducts) {
            this.totalProducts = totalProducts;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public BigDecimal getTodayRevenue() {
            return todayRevenue;
        }

        public void setTodayRevenue(BigDecimal todayRevenue) {
            this.todayRevenue = todayRevenue;
        }
    }

    public static class TopProduct {
        private Long productId;
        private String productName;
        private String imageUrl;
        private Integer soldCount;
        private BigDecimal revenue;

        public TopProduct() {}

        public TopProduct(Long productId, String productName, String imageUrl, 
                         Integer soldCount, BigDecimal revenue) {
            this.productId = productId;
            this.productName = productName;
            this.imageUrl = imageUrl;
            this.soldCount = soldCount;
            this.revenue = revenue;
        }

        // Getters and Setters
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

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Integer getSoldCount() {
            return soldCount;
        }

        public void setSoldCount(Integer soldCount) {
            this.soldCount = soldCount;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

    public static class RevenueByDate {
        private String date;
        private BigDecimal revenue;
        private Long orderCount;

        public RevenueByDate() {}

        public RevenueByDate(String date, BigDecimal revenue, Long orderCount) {
            this.date = date;
            this.revenue = revenue;
            this.orderCount = orderCount;
        }

        // Getters and Setters
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }

        public Long getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(Long orderCount) {
            this.orderCount = orderCount;
        }
    }
}