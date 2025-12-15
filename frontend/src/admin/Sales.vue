<template>
  <div class="report-page">
    <!-- Header -->
    <div class="page-header">
      <h1>📊 Revenue & Sales Report</h1>
      <p class="subtitle">Comprehensive analytics and statistics</p>
    </div>

    <!-- Date Range Filter -->
    <div class="filter-section">
      <div class="date-filter">
        <label>📅 From:</label>
        <input type="date" v-model="dateFrom" @change="fetchReportData" />

        <label>📅 To:</label>
        <input type="date" v-model="dateTo" @change="fetchReportData" />

        <button class="btn-refresh" @click="fetchReportData">🔄 Refresh</button>
      </div>

      <div class="quick-filters">
        <button @click="setToday">Today</button>
        <button @click="setThisWeek">This Week</button>
        <button @click="setThisMonth">This Month</button>
        <button @click="setAll">All Time</button>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Loading report data...</p>
    </div>

    <!-- Main Content -->
    <div v-else class="report-content">
      <!-- Revenue Cards -->
      <div class="stats-grid">
        <!-- Total Revenue -->
        <div class="stat-card total">
          <div class="card-icon">💰</div>
          <div class="card-content">
            <h3>Total Revenue</h3>
            <p class="stat-value">{{ formatPrice(totalRevenue) }}</p>
            <span class="stat-label">All payment methods</span>
          </div>
        </div>

        <!-- Cash Revenue -->
        <div class="stat-card cash">
          <div class="card-icon">💵</div>
          <div class="card-content">
            <h3>Cash Revenue</h3>
            <p class="stat-value">{{ formatPrice(cashRevenue) }}</p>
            <span class="stat-label">{{ cashOrders }} orders</span>
          </div>
        </div>

        <!-- VNPay Revenue -->
        <div class="stat-card vnpay">
          <div class="card-icon">💳</div>
          <div class="card-content">
            <h3>VNPay Revenue</h3>
            <p class="stat-value">{{ formatPrice(vnpayRevenue) }}</p>
            <span class="stat-label">{{ vnpayOrders }} orders</span>
          </div>
        </div>

        <!-- Total Orders -->
        <div class="stat-card orders">
          <div class="card-icon">📦</div>
          <div class="card-content">
            <h3>Total Orders</h3>
            <p class="stat-value">{{ totalOrders }}</p>
            <span class="stat-label">Completed orders</span>
          </div>
        </div>
      </div>

      <!-- Visual Charts (CSS-based) -->
      <div class="charts-section">
        <!-- Revenue Distribution -->
        <div class="chart-card">
          <h3>📈 Revenue by Payment Method</h3>
          <div class="revenue-bars">
            <div class="bar-item">
              <div class="bar-label">
                <span class="label-text">💵 Cash</span>
                <span class="label-value">{{ formatPrice(cashRevenue) }}</span>
              </div>
              <div class="bar-wrapper">
                <div
                  class="bar cash-bar"
                  :style="{
                    width: getPercentage(cashRevenue, totalRevenue) + '%',
                  }"
                >
                  <span class="bar-percent"
                    >{{ getPercentage(cashRevenue, totalRevenue) }}%</span
                  >
                </div>
              </div>
            </div>

            <div class="bar-item">
              <div class="bar-label">
                <span class="label-text">💳 VNPay</span>
                <span class="label-value">{{ formatPrice(vnpayRevenue) }}</span>
              </div>
              <div class="bar-wrapper">
                <div
                  class="bar vnpay-bar"
                  :style="{
                    width: getPercentage(vnpayRevenue, totalRevenue) + '%',
                  }"
                >
                  <span class="bar-percent"
                    >{{ getPercentage(vnpayRevenue, totalRevenue) }}%</span
                  >
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Top Products -->
        <div class="chart-card">
          <h3>🍽️ Top 5 Selling Products</h3>
          <div class="product-bars">
            <div
              v-for="(item, index) in topProducts"
              :key="item.productId"
              class="product-bar-item"
            >
              <div class="product-bar-label">
                <span class="rank-mini" :class="getRankClass(index)">{{
                  index + 1
                }}</span>
                <span class="product-name-mini">{{ item.productName }}</span>
                <span class="product-qty">{{ item.quantitySold }}</span>
              </div>
              <div class="product-bar-wrapper">
                <div
                  class="product-bar"
                  :style="{
                    width: getPercentage(item.quantitySold, maxQuantity) + '%',
                  }"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Products Table -->
      <div class="products-section">
        <h2>🍽️ Products Sales Report</h2>

        <div class="table-container">
          <table class="products-table">
            <thead>
              <tr>
                <th>Rank</th>
                <th>Product Name</th>
                <th>Quantity Sold</th>
                <th>Unit Price</th>
                <th>Total Revenue</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!productSales.length">
                <td colspan="5" style="text-align: center; padding: 20px">
                  No data available
                </td>
              </tr>
              <tr
                v-else
                v-for="(item, index) in productSales"
                :key="item.productId"
                :class="{ 'top-product': index < 3 }"
              >
                <td>
                  <span class="rank-badge" :class="getRankClass(index)">
                    {{ index + 1 }}
                  </span>
                </td>
                <td class="product-name">
                  <strong>{{ item.productName }}</strong>
                </td>
                <td class="quantity">{{ item.quantitySold }}</td>
                <td>{{ formatPrice(item.unitPrice) }}</td>
                <td class="revenue">{{ formatPrice(item.totalRevenue) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Summary Section -->
      <div class="summary-section">
        <h2>📋 Summary</h2>
        <div class="summary-grid">
          <div class="summary-item">
            <span class="summary-label">Total Products Sold:</span>
            <span class="summary-value">{{ totalProductsSold }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">Average Order Value:</span>
            <span class="summary-value">{{
              formatPrice(averageOrderValue)
            }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">Best Selling Product:</span>
            <span class="summary-value">{{ bestSellingProduct }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">Total Unique Products:</span>
            <span class="summary-value">{{ productSales.length }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/axios";
import storage from "@/utils/storage";

export default {
  name: "RevenueReport",

  data() {
    return {
      loading: false,
      dateFrom: "",
      dateTo: "",

      // Revenue data
      totalRevenue: 0,
      cashRevenue: 0,
      vnpayRevenue: 0,
      cashOrders: 0,
      vnpayOrders: 0,
      totalOrders: 0,

      // Products data
      productSales: [],
    };
  },

  computed: {
    totalProductsSold() {
      return this.productSales.reduce(
        (sum, item) => sum + item.quantitySold,
        0
      );
    },

    averageOrderValue() {
      return this.totalOrders > 0 ? this.totalRevenue / this.totalOrders : 0;
    },

    bestSellingProduct() {
      if (!this.productSales.length) return "N/A";
      return this.productSales[0]?.productName || "N/A";
    },

    topProducts() {
      return this.productSales.slice(0, 5);
    },

    maxQuantity() {
      if (!this.productSales.length) return 1;
      return Math.max(...this.productSales.map((p) => p.quantitySold));
    },
  },

  mounted() {
    this.setThisMonth(); // Default: this month
    this.fetchReportData();
  },

  methods: {
    // ========== DATE FILTERS ==========
    setToday() {
      const today = new Date().toISOString().split("T")[0];
      this.dateFrom = today;
      this.dateTo = today;
      this.fetchReportData();
    },

    setThisWeek() {
      const today = new Date();
      const firstDay = new Date(
        today.setDate(today.getDate() - today.getDay())
      );
      const lastDay = new Date(
        today.setDate(today.getDate() - today.getDay() + 6)
      );

      this.dateFrom = firstDay.toISOString().split("T")[0];
      this.dateTo = lastDay.toISOString().split("T")[0];
      this.fetchReportData();
    },

    setThisMonth() {
      const today = new Date();
      const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
      const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);

      this.dateFrom = firstDay.toISOString().split("T")[0];
      this.dateTo = lastDay.toISOString().split("T")[0];
      this.fetchReportData();
    },

    setAll() {
      this.dateFrom = "";
      this.dateTo = "";
      this.fetchReportData();
    },

    // ========== FETCH DATA ==========
    async fetchReportData() {
      try {
        this.loading = true;

        const token = storage.getToken();
        if (!token) {
          this.$router.push("/login");
          return;
        }

        console.log("📊 Fetching report data...");
        console.log("Date range:", this.dateFrom, "to", this.dateTo);

        const res = await api.get("/admin/reports/revenue", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            dateFrom: this.dateFrom || undefined,
            dateTo: this.dateTo || undefined,
          },
        });

        console.log("✅ Report data:", res.data);

        const data = res.data.data || res.data;

        this.totalRevenue = data.totalRevenue || 0;
        this.cashRevenue = data.cashRevenue || 0;
        this.vnpayRevenue = data.vnpayRevenue || 0;
        this.cashOrders = data.cashOrders || 0;
        this.vnpayOrders = data.vnpayOrders || 0;
        this.totalOrders = data.totalOrders || 0;
        this.productSales = data.productSales || [];

        // Sort products by quantity sold
        this.productSales.sort((a, b) => b.quantitySold - a.quantitySold);
      } catch (err) {
        console.error("❌ Fetch report error:", err);
        console.error("Error response:", err.response);

        if (err.response?.status === 401 || err.response?.status === 403) {
          storage.clearAuth();
          this.$router.push("/login");
        } else {
          alert("Không thể tải báo cáo! Vui lòng thử lại.");
        }
      } finally {
        this.loading = false;
      }
    },

    // ========== UTILITIES ==========
    formatPrice(price) {
      const value = Number(price || 0);
      return value.toLocaleString("vi-VN") + "₫";
    },

    getPercentage(value, total) {
      if (!total || total === 0) return 0;
      return Math.round((value / total) * 100);
    },

    getRankClass(index) {
      if (index === 0) return "gold";
      if (index === 1) return "silver";
      if (index === 2) return "bronze";
      return "";
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.report-page {
  padding: 30px;
  background: linear-gradient(135deg, #f4f7fb 0%, #e1e9f7 100%);
  min-height: 100vh;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

/* Header */
.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 36px;
  font-weight: 800;
  color: #2563eb;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 16px;
  color: #3b82f6;
  font-weight: 500;
}

/* Filter Section */
.filter-section {
  background: white;
  padding: 20px;
  border-radius: 16px;
  margin-bottom: 30px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
}

.date-filter {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
  margin-bottom: 15px;
}

.date-filter label {
  font-weight: 600;
  color: #2563eb;
}

.date-filter input {
  padding: 10px 15px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: all 0.3s ease;
}

.date-filter input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
}

.btn-refresh {
  padding: 10px 20px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-refresh:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.35);
}

.quick-filters {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.quick-filters button {
  padding: 8px 16px;
  background: #f3f4f6;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.quick-filters button:hover {
  background: #2563eb;
  color: white;
  border-color: #2563eb;
}

/* Loading */
.loading-state {
  padding: 5rem;
  text-align: center;
}

.spinner {
  width: 60px;
  height: 60px;
  border: 5px solid #e5e7eb;
  border-top: 5px solid #2563eb;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin: 0 auto 1.5rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-state p {
  color: #6b7280;
  font-size: 1.1rem;
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 25px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
}

.stat-card.total {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
}

.stat-card.cash {
  background: linear-gradient(135deg, #10b981 0%, #047857 100%);
  color: white;
}

.stat-card.vnpay {
  background: linear-gradient(135deg, #3b82f6 0%, #1e40af 100%);
  color: white;
}

.stat-card.orders {
  background: linear-gradient(135deg, #0ea5e9 0%, #2563eb 100%);
  color: white;
}

.card-icon {
  font-size: 48px;
}

.card-content h3 {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 8px;
  opacity: 0.9;
}

.stat-value {
  font-size: 28px;
  font-weight: 900;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  opacity: 0.8;
}

/* Charts Section */
.charts-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.chart-card {
  background: white;
  padding: 25px;
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
}

.chart-card h3 {
  font-size: 18px;
  font-weight: 700;
  color: #2563eb;
  margin-bottom: 20px;
}

/* Revenue Bars */
.revenue-bars {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.bar-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.bar-label {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  font-weight: 600;
}

.label-text {
  color: #6b7280;
}

.label-value {
  color: #2563eb;
  font-weight: 700;
}

.bar-wrapper {
  background: #f3f4f6;
  border-radius: 8px;
  height: 40px;
  position: relative;
  overflow: hidden;
}

.bar {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 12px;
  border-radius: 8px;
  transition: width 0.6s ease;
  color: white;
  font-weight: 700;
  font-size: 13px;
}

.cash-bar {
  background: linear-gradient(135deg, #10b981 0%, #047857 100%);
}

.vnpay-bar {
  background: linear-gradient(135deg, #3b82f6 0%, #1e40af 100%);
}

/* Product Bars */
.product-bars {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.product-bar-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.product-bar-label {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
}

.rank-mini {
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  border-radius: 50%;
  font-weight: 700;
  font-size: 11px;
  background: #e5e7eb;
  color: #6b7280;
}

.rank-mini.gold {
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
  color: #854d0e;
}

.rank-mini.silver {
  background: linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%);
  color: #52525b;
}

.rank-mini.bronze {
  background: linear-gradient(135deg, #cd7f32 0%, #e6a85c 100%);
  color: #78350f;
}

.product-name-mini {
  flex: 1;
  font-weight: 600;
  color: #374151;
}

.product-qty {
  font-weight: 700;
  color: #2563eb;
}

.product-bar-wrapper {
  background: #f3f4f6;
  border-radius: 6px;
  height: 24px;
  position: relative;
  overflow: hidden;
}

.product-bar {
  height: 100%;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  border-radius: 6px;
  transition: width 0.6s ease;
}

/* Products Table */
.products-section {
  background: white;
  padding: 25px;
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
}

.products-section h2 {
  font-size: 24px;
  font-weight: 700;
  color: #2563eb;
  margin-bottom: 20px;
}

.table-container {
  overflow-x: auto;
}

.products-table {
  width: 100%;
  border-collapse: collapse;
}

.products-table thead {
  background: #f8f9fa;
}

.products-table th {
  padding: 15px 20px;
  text-align: left;
  font-size: 13px;
  font-weight: 700;
  color: #2563eb;
  text-transform: uppercase;
  border-bottom: 2px solid #e0e0e0;
}

.products-table tbody tr {
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s ease;
}

.products-table tbody tr:hover {
  background: #f0f5ff;
}

.products-table tbody tr.top-product {
  background: #eef3ff;
}

.products-table td {
  padding: 15px 20px;
  font-size: 14px;
  color: #333;
}

.rank-badge {
  display: inline-block;
  width: 32px;
  height: 32px;
  line-height: 32px;
  text-align: center;
  border-radius: 50%;
  font-weight: 700;
  background: #e5e7eb;
  color: #6b7280;
}

.rank-badge.gold {
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
  color: #854d0e;
}

.rank-badge.silver {
  background: linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%);
  color: #52525b;
}

.rank-badge.bronze {
  background: linear-gradient(135deg, #cd7f32 0%, #e6a85c 100%);
  color: #78350f;
}

.product-name strong {
  color: #2563eb;
}

.quantity {
  font-weight: 700;
  color: #059669;
}

.revenue {
  font-weight: 700;
  color: #2563eb;
}

/* Summary Section */
.summary-section {
  background: white;
  padding: 25px;
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
}

.summary-section h2 {
  font-size: 24px;
  font-weight: 700;
  color: #2563eb;
  margin-bottom: 20px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #f8f9fa;
  border-radius: 12px;
  border-left: 4px solid #2563eb;
}

.summary-label {
  font-weight: 600;
  color: #6b7280;
  font-size: 14px;
}

.summary-value {
  font-weight: 700;
  color: #2563eb;
  font-size: 16px;
}

/* Responsive */
@media (max-width: 768px) {
  .report-page {
    padding: 20px;
  }

  .page-header h1 {
    font-size: 28px;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .charts-section {
    grid-template-columns: 1fr;
  }

  .date-filter {
    flex-direction: column;
    align-items: flex-start;
  }

  .date-filter input {
    width: 100%;
  }
}
</style>
