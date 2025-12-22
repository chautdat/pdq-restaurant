<template>
  <div class="revenue-report">
    <!-- Header Section -->
    <div class="report-header">
      <div class="header-content">
        <div class="title-section">
          <h1>
            <span class="icon">📊</span>
            Báo Cáo Doanh Thu & Bán Hàng
          </h1>
          <p class="subtitle">Phân tích chi tiết & thống kê toàn diện</p>
        </div>

        <div class="header-actions">
          <button class="btn-export excel" @click="exportExcel">
            📥 Xuất Excel
          </button>
          <button class="btn-export pdf" @click="exportPDF">📄 Xuất PDF</button>
        </div>
      </div>
    </div>

    <!-- Filters Section -->
    <div class="filters-container">
      <div class="date-filters">
        <div class="date-input-group">
          <label>
            <span class="icon">📅</span>
            Từ Ngày
          </label>
          <input type="date" v-model="dateFrom" @change="fetchReportData" />
        </div>

        <div class="date-input-group">
          <label>
            <span class="icon">📅</span>
            Đến Ngày
          </label>
          <input type="date" v-model="dateTo" @change="fetchReportData" />
        </div>

        <button
          class="btn-refresh"
          @click="fetchReportData"
          :disabled="loading"
        >
          <span :class="{ rotating: loading }">🔄</span>
          Làm Mới
        </button>
      </div>

      <div class="quick-filters">
        <button @click="setToday" :class="{ active: activeFilter === 'today' }">
          Hôm Nay
        </button>
        <button
          @click="setThisWeek"
          :class="{ active: activeFilter === 'week' }"
        >
          Tuần Này
        </button>
        <button
          @click="setThisMonth"
          :class="{ active: activeFilter === 'month' }"
        >
          Tháng Này
        </button>
        <button @click="setAll" :class="{ active: activeFilter === 'all' }">
          Tất Cả
        </button>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>Đang tải dữ liệu báo cáo...</p>
    </div>

    <!-- Main Content -->
    <div v-else class="report-content">
      <!-- Overview Stats -->
      <div class="overview-section">
        <div class="stats-grid">
          <!-- Total Revenue -->
          <div class="stat-card primary">
            <div class="stat-header">
              <div class="stat-icon-wrapper">
                <span class="stat-icon">💰</span>
              </div>
              <div class="stat-trend up">
                <span class="trend-icon">↗</span>
                <span class="trend-value">+12.5%</span>
              </div>
            </div>
            <div class="stat-body">
              <h3 class="stat-title">Tổng Doanh Thu</h3>
              <p class="stat-value">{{ formatPrice(totalRevenue) }}</p>
              <p class="stat-description">Tất cả phương thức thanh toán</p>
            </div>
            <div class="stat-footer">
              <span class="footer-text">{{ totalOrders }} đơn hàng</span>
            </div>
          </div>

          <!-- Cash Revenue -->
          <div class="stat-card success">
            <div class="stat-header">
              <div class="stat-icon-wrapper">
                <span class="stat-icon">💵</span>
              </div>
              <div class="stat-trend up">
                <span class="trend-icon">↗</span>
                <span class="trend-value">+8.3%</span>
              </div>
            </div>
            <div class="stat-body">
              <h3 class="stat-title">Tiền Mặt</h3>
              <p class="stat-value">{{ formatPrice(cashRevenue) }}</p>
              <p class="stat-description">
                {{ getPercentage(cashRevenue, totalRevenue) }}% tổng doanh thu
              </p>
            </div>
            <div class="stat-footer">
              <span class="footer-text">{{ cashOrders }} đơn</span>
            </div>
          </div>

          <!-- VNPay Revenue -->
          <div class="stat-card info">
            <div class="stat-header">
              <div class="stat-icon-wrapper">
                <span class="stat-icon">💳</span>
              </div>
              <div class="stat-trend up">
                <span class="trend-icon">↗</span>
                <span class="trend-value">+15.7%</span>
              </div>
            </div>
            <div class="stat-body">
              <h3 class="stat-title">VNPay</h3>
              <p class="stat-value">{{ formatPrice(vnpayRevenue) }}</p>
              <p class="stat-description">
                {{ getPercentage(vnpayRevenue, totalRevenue) }}% tổng doanh thu
              </p>
            </div>
            <div class="stat-footer">
              <span class="footer-text">{{ vnpayOrders }} đơn</span>
            </div>
          </div>

          <!-- Average Order Value -->
          <div class="stat-card warning">
            <div class="stat-header">
              <div class="stat-icon-wrapper">
                <span class="stat-icon">📈</span>
              </div>
              <div class="stat-trend up">
                <span class="trend-icon">↗</span>
                <span class="trend-value">+5.2%</span>
              </div>
            </div>
            <div class="stat-body">
              <h3 class="stat-title">Giá Trị TB/Đơn</h3>
              <p class="stat-value">{{ formatPrice(averageOrderValue) }}</p>
              <p class="stat-description">Trung bình mỗi đơn hàng</p>
            </div>
            <div class="stat-footer">
              <span class="footer-text">{{ totalOrders }} đơn</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Charts Section -->
      <div class="charts-section">
        <!-- Revenue Distribution Chart -->
        <div class="chart-card">
          <div class="chart-header">
            <h2>
              <span class="icon">📊</span>
              Phân Bổ Doanh Thu Theo Phương Thức
            </h2>
            <div class="chart-legend">
              <div class="legend-item">
                <span class="legend-dot cash"></span>
                <span class="legend-label">Tiền Mặt</span>
              </div>
              <div class="legend-item">
                <span class="legend-dot vnpay"></span>
                <span class="legend-label">VNPay</span>
              </div>
            </div>
          </div>

          <div class="chart-body">
            <!-- Pie Chart Visualization -->
            <div class="pie-chart-container">
              <div class="pie-chart">
                <div
                  class="pie-segment cash-segment"
                  :style="{
                    '--percentage': getPercentage(cashRevenue, totalRevenue),
                    '--rotation': 0,
                  }"
                ></div>
                <div
                  class="pie-segment vnpay-segment"
                  :style="{
                    '--percentage': getPercentage(vnpayRevenue, totalRevenue),
                    '--rotation':
                      getPercentage(cashRevenue, totalRevenue) * 3.6,
                  }"
                ></div>
                <div class="pie-center">
                  <div class="pie-total">{{ formatPrice(totalRevenue) }}</div>
                  <div class="pie-label">Tổng</div>
                </div>
              </div>
            </div>

            <!-- Bar Chart Alternative -->
            <div class="revenue-bars">
              <div class="bar-item">
                <div class="bar-label">
                  <span class="label-text">
                    <span class="icon">💵</span>
                    Tiền Mặt
                  </span>
                  <span class="label-value">{{
                    formatPrice(cashRevenue)
                  }}</span>
                </div>
                <div class="bar-track">
                  <div
                    class="bar-fill cash-bar"
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
                  <span class="label-text">
                    <span class="icon">💳</span>
                    VNPay
                  </span>
                  <span class="label-value">{{
                    formatPrice(vnpayRevenue)
                  }}</span>
                </div>
                <div class="bar-track">
                  <div
                    class="bar-fill vnpay-bar"
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
        </div>

        <!-- Top Products Chart -->
        <div class="chart-card">
          <div class="chart-header">
            <h2>
              <span class="icon">🏆</span>
              Top 5 Sản Phẩm Bán Chạy
            </h2>
          </div>

          <div class="chart-body">
            <div class="top-products-list">
              <div
                v-for="(item, index) in topProducts"
                :key="item.productId"
                class="product-item"
              >
                <div class="product-rank">
                  <span :class="['rank-badge', getRankClass(index)]">
                    {{ index + 1 }}
                  </span>
                </div>

                <div class="product-info">
                  <div class="product-header">
                    <h4 class="product-name">{{ item.productName }}</h4>
                    <span class="product-quantity"
                      >{{ item.quantitySold }} món</span
                    >
                  </div>

                  <div class="product-bar-track">
                    <div
                      class="product-bar-fill"
                      :style="{
                        width:
                          getPercentage(item.quantitySold, maxQuantity) + '%',
                        background: getBarGradient(index),
                      }"
                    ></div>
                  </div>

                  <div class="product-footer">
                    <span class="product-revenue">{{
                      formatPrice(item.totalRevenue)
                    }}</span>
                    <span class="product-price"
                      >{{ formatPrice(item.unitPrice) }}/món</span
                    >
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Summary Cards -->
      <div class="summary-section">
        <div class="summary-cards">
          <div class="summary-card">
            <div class="summary-icon">🎯</div>
            <div class="summary-content">
              <h4>Tổng Món Bán Ra</h4>
              <p class="summary-value">{{ totalProductsSold }}</p>
              <span class="summary-label"
                >{{ productSales.length }} sản phẩm khác nhau</span
              >
            </div>
          </div>

          <div class="summary-card">
            <div class="summary-icon">⭐</div>
            <div class="summary-content">
              <h4>Sản Phẩm Bán Chạy Nhất</h4>
              <p class="summary-value">{{ bestSellingProduct }}</p>
              <span class="summary-label"
                >{{ topProducts[0]?.quantitySold || 0 }} món đã bán</span
              >
            </div>
          </div>

          <div class="summary-card">
            <div class="summary-icon">💎</div>
            <div class="summary-content">
              <h4>Doanh Thu Cao Nhất</h4>
              <p class="summary-value">
                {{ formatPrice(highestRevenueProduct?.totalRevenue || 0) }}
              </p>
              <span class="summary-label">{{
                highestRevenueProduct?.productName || "N/A"
              }}</span>
            </div>
          </div>

          <div class="summary-card">
            <div class="summary-icon">📦</div>
            <div class="summary-content">
              <h4>Tổng Đơn Hàng</h4>
              <p class="summary-value">{{ totalOrders }}</p>
              <span class="summary-label">Trong khoảng thời gian đã chọn</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Products Table -->
      <div class="table-section">
        <div class="table-header">
          <h2>
            <span class="icon">📋</span>
            Chi Tiết Bán Hàng Theo Sản Phẩm
          </h2>
          <div class="table-actions">
            <div class="search-box">
              <span class="search-icon">🔍</span>
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Tìm sản phẩm..."
              />
            </div>
            <button class="btn-export-table" @click="exportTable">
              📊 Xuất Bảng
            </button>
          </div>
        </div>

        <div class="table-container">
          <table class="products-table">
            <thead>
              <tr>
                <th class="sortable" @click="sortTable('rank')">
                  <div class="th-content">
                    Hạng
                    <span class="sort-icon">{{ getSortIcon("rank") }}</span>
                  </div>
                </th>
                <th class="sortable" @click="sortTable('productName')">
                  <div class="th-content">
                    Tên Sản Phẩm
                    <span class="sort-icon">{{
                      getSortIcon("productName")
                    }}</span>
                  </div>
                </th>
                <th
                  class="sortable text-right"
                  @click="sortTable('quantitySold')"
                >
                  <div class="th-content">
                    Số Lượng Bán
                    <span class="sort-icon">{{
                      getSortIcon("quantitySold")
                    }}</span>
                  </div>
                </th>
                <th class="text-right">Đơn Giá</th>
                <th
                  class="sortable text-right"
                  @click="sortTable('totalRevenue')"
                >
                  <div class="th-content">
                    Doanh Thu
                    <span class="sort-icon">{{
                      getSortIcon("totalRevenue")
                    }}</span>
                  </div>
                </th>
                <th class="text-center">% Tổng DT</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!filteredProducts.length">
                <td colspan="6" class="empty-row">
                  <div class="empty-state">
                    <span class="empty-icon">📭</span>
                    <p>Không có dữ liệu</p>
                  </div>
                </td>
              </tr>
              <tr
                v-else
                v-for="(item, index) in paginatedProducts"
                :key="item.productId"
                :class="{ 'highlight-row': index < 3 }"
              >
                <td>
                  <span :class="['rank-badge', getRankClass(index)]">
                    {{ index + 1 }}
                  </span>
                </td>
                <td>
                  <div class="product-cell">
                    <span class="product-name-text">{{
                      item.productName
                    }}</span>
                  </div>
                </td>
                <td class="text-right">
                  <span class="quantity-badge">{{ item.quantitySold }}</span>
                </td>
                <td class="text-right">
                  <span class="price-text">{{
                    formatPrice(item.unitPrice)
                  }}</span>
                </td>
                <td class="text-right">
                  <span class="revenue-text">{{
                    formatPrice(item.totalRevenue)
                  }}</span>
                </td>
                <td class="text-center">
                  <div class="percentage-cell">
                    <span class="percentage-value">
                      {{
                        getPercentage(item.totalRevenue, totalProductRevenue)
                      }}%
                    </span>
                    <div class="mini-bar">
                      <div
                        class="mini-bar-fill"
                        :style="{
                          width:
                            getPercentage(
                              item.totalRevenue,
                              totalProductRevenue
                            ) + '%',
                        }"
                      ></div>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div v-if="totalTablePages > 1" class="table-pagination">
          <button
            @click="currentTablePage--"
            :disabled="currentTablePage === 0"
            class="page-btn"
          >
            ← Trước
          </button>

          <div class="page-numbers">
            <button
              v-for="page in visiblePages"
              :key="page"
              @click="currentTablePage = page - 1"
              :class="[
                'page-number',
                { active: currentTablePage === page - 1 },
              ]"
            >
              {{ page }}
            </button>
          </div>

          <button
            @click="currentTablePage++"
            :disabled="currentTablePage >= totalTablePages - 1"
            class="page-btn"
          >
            Sau →
          </button>
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
      activeFilter: "month",

      // Revenue data
      totalRevenue: 0,
      cashRevenue: 0,
      vnpayRevenue: 0,
      cashOrders: 0,
      vnpayOrders: 0,
      totalOrders: 0,

      // Products data
      productSales: [],

      // Table
      searchQuery: "",
      currentTablePage: 0,
      tablePageSize: 10,
      sortField: null,
      sortDirection: "desc",
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

    highestRevenueProduct() {
      if (!this.productSales.length) return null;
      return [...this.productSales].sort(
        (a, b) => b.totalRevenue - a.totalRevenue
      )[0];
    },

    topProducts() {
      return this.productSales.slice(0, 5);
    },

    maxQuantity() {
      if (!this.productSales.length) return 1;
      return Math.max(...this.productSales.map((p) => p.quantitySold));
    },

    totalProductRevenue() {
      return this.productSales.reduce(
        (sum, item) => sum + item.totalRevenue,
        0
      );
    },

    filteredProducts() {
      let products = [...this.productSales];

      // Search filter
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        products = products.filter((p) =>
          p.productName.toLowerCase().includes(query)
        );
      }

      // Sort
      if (this.sortField) {
        products.sort((a, b) => {
          let aVal = a[this.sortField];
          let bVal = b[this.sortField];

          if (typeof aVal === "string") {
            aVal = aVal.toLowerCase();
            bVal = bVal.toLowerCase();
          }

          const comparison = aVal > bVal ? 1 : aVal < bVal ? -1 : 0;
          return this.sortDirection === "asc" ? comparison : -comparison;
        });
      }

      return products;
    },

    paginatedProducts() {
      const start = this.currentTablePage * this.tablePageSize;
      const end = start + this.tablePageSize;
      return this.filteredProducts.slice(start, end);
    },

    totalTablePages() {
      return Math.ceil(this.filteredProducts.length / this.tablePageSize);
    },

    visiblePages() {
      const pages = [];
      const total = this.totalTablePages;
      const current = this.currentTablePage + 1;

      pages.push(1);

      for (
        let i = Math.max(2, current - 1);
        i <= Math.min(total - 1, current + 1);
        i++
      ) {
        if (!pages.includes(i)) pages.push(i);
      }

      if (total > 1 && !pages.includes(total)) pages.push(total);

      return pages;
    },
  },

  mounted() {
    this.setThisMonth();
  },

  methods: {
    // Date Filters
    setToday() {
      this.activeFilter = "today";
      const today = new Date().toISOString().split("T")[0];
      this.dateFrom = today;
      this.dateTo = today;
      this.fetchReportData();
    },

    setThisWeek() {
      this.activeFilter = "week";
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
      this.activeFilter = "month";
      const today = new Date();
      const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
      const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);

      this.dateFrom = firstDay.toISOString().split("T")[0];
      this.dateTo = lastDay.toISOString().split("T")[0];
      this.fetchReportData();
    },

    setAll() {
      this.activeFilter = "all";
      this.dateFrom = "";
      this.dateTo = "";
      this.fetchReportData();
    },

    // Fetch Data
    async fetchReportData() {
      try {
        this.loading = true;

        const token = storage.getToken();
        if (!token) {
          this.$router.push("/login");
          return;
        }

        const res = await api.get("/admin/reports/revenue", {
          headers: { Authorization: `Bearer ${token}` },
          params: {
            dateFrom: this.dateFrom || undefined,
            dateTo: this.dateTo || undefined,
          },
        });

        const data = res.data.data || res.data;

        this.totalRevenue = data.totalRevenue || 0;
        this.cashRevenue = data.cashRevenue || 0;
        this.vnpayRevenue = data.vnpayRevenue || 0;
        this.cashOrders = data.cashOrders || 0;
        this.vnpayOrders = data.vnpayOrders || 0;
        this.totalOrders = data.totalOrders || 0;
        this.productSales = data.productSales || [];

        this.productSales.sort((a, b) => b.quantitySold - a.quantitySold);
      } catch (err) {
        console.error("Error fetching report:", err);

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

    // Table Sorting
    sortTable(field) {
      if (this.sortField === field) {
        this.sortDirection = this.sortDirection === "asc" ? "desc" : "asc";
      } else {
        this.sortField = field;
        this.sortDirection = "desc";
      }
    },

    getSortIcon(field) {
      if (this.sortField !== field) return "⇅";
      return this.sortDirection === "asc" ? "↑" : "↓";
    },

    // Export Functions
    exportExcel() {
      const csv = this.generateCSV();
      const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = `revenue-report-${
        new Date().toISOString().split("T")[0]
      }.csv`;
      link.click();
      alert("✅ Đã xuất báo cáo Excel!");
    },

    exportPDF() {
      alert("📄 Chức năng xuất PDF đang được phát triển!");
    },

    exportTable() {
      const csv = this.generateTableCSV();
      const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = `products-report-${
        new Date().toISOString().split("T")[0]
      }.csv`;
      link.click();
      alert("✅ Đã xuất bảng sản phẩm!");
    },

    generateCSV() {
      const headers = ["Chỉ Số", "Giá Trị"];
      const data = [
        ["Tổng Doanh Thu", this.totalRevenue],
        ["Doanh Thu Tiền Mặt", this.cashRevenue],
        ["Doanh Thu VNPay", this.vnpayRevenue],
        ["Tổng Đơn Hàng", this.totalOrders],
        ["Đơn Tiền Mặt", this.cashOrders],
        ["Đơn VNPay", this.vnpayOrders],
        ["Giá Trị TB/Đơn", this.averageOrderValue],
        ["Tổng Món Bán", this.totalProductsSold],
      ];
      return [headers, ...data].map((row) => row.join(",")).join("\n");
    },

    generateTableCSV() {
      const headers = ["Hạng", "Tên SP", "Số Lượng", "Đơn Giá", "Doanh Thu"];
      const rows = this.productSales.map((p, i) => [
        i + 1,
        p.productName,
        p.quantitySold,
        p.unitPrice,
        p.totalRevenue,
      ]);
      return [headers, ...rows].map((row) => row.join(",")).join("\n");
    },

    // Utilities
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

    getBarGradient(index) {
      const gradients = [
        "linear-gradient(135deg, #ffd700 0%, #ffed4e 100%)",
        "linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%)",
        "linear-gradient(135deg, #cd7f32 0%, #e6a85c 100%)",
        "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        "linear-gradient(135deg, #f093fb 0%, #f5576c 100%)",
      ];
      return gradients[index] || gradients[3];
    },
  },
};
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.revenue-report {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
  padding: 30px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
    "Helvetica Neue", Arial, sans-serif;
}

/* ============ HEADER ============ */
.report-header {
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
  overflow: hidden;
}

.header-content {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30px;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
}

.title-section h1 {
  margin: 0 0 8px 0;
  font-size: 32px;
  font-weight: 800;
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-section h1 .icon {
  font-size: 36px;
}

.subtitle {
  margin: 0;
  opacity: 0.95;
  font-size: 15px;
  font-weight: 500;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.btn-export {
  padding: 12px 24px;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-export.excel {
  background: #10b981;
  color: white;
}

.btn-export.pdf {
  background: #ef4444;
  color: white;
}

.btn-export:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

/* ============ FILTERS ============ */
.filters-container {
  background: white;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  margin-bottom: 30px;
}

.date-filters {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  flex-wrap: wrap;
  align-items: flex-end;
}

.date-input-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.date-input-group label {
  font-size: 13px;
  font-weight: 700;
  color: #475569;
  display: flex;
  align-items: center;
  gap: 6px;
}

.date-input-group input {
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s;
}

.date-input-group input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.btn-refresh {
  padding: 12px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-refresh:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
}

.btn-refresh:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.rotating {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.quick-filters {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.quick-filters button {
  padding: 10px 20px;
  border: 2px solid #e2e8f0;
  background: white;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  color: #475569;
}

.quick-filters button:hover {
  border-color: #667eea;
  color: #667eea;
}

.quick-filters button.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: #667eea;
}

/* ============ LOADING ============ */
.loading-container {
  background: white;
  border-radius: 16px;
  padding: 80px 40px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.spinner {
  width: 48px;
  height: 48px;
  border: 4px solid #e2e8f0;
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-container p {
  color: #64748b;
  font-size: 16px;
  font-weight: 600;
}

/* ============ OVERVIEW STATS ============ */
.overview-section {
  margin-bottom: 30px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.stat-card.primary::before {
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.stat-card.success::before {
  background: linear-gradient(90deg, #10b981 0%, #059669 100%);
}

.stat-card.info::before {
  background: linear-gradient(90deg, #3b82f6 0%, #2563eb 100%);
}

.stat-card.warning::before {
  background: linear-gradient(90deg, #f59e0b 0%, #d97706 100%);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.stat-icon-wrapper {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon {
  font-size: 28px;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 700;
}

.stat-trend.up {
  background: #d1fae5;
  color: #065f46;
}

.stat-trend.down {
  background: #fee2e2;
  color: #991b1b;
}

.trend-icon {
  font-size: 14px;
}

.stat-body {
  margin-bottom: 12px;
}

.stat-title {
  font-size: 14px;
  font-weight: 600;
  color: #64748b;
  margin: 0 0 8px 0;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.stat-value {
  font-size: 28px;
  font-weight: 800;
  color: #1e293b;
  margin: 0 0 4px 0;
  line-height: 1;
}

.stat-description {
  font-size: 13px;
  color: #64748b;
  margin: 0;
}

.stat-footer {
  padding-top: 12px;
  border-top: 1px solid #f1f5f9;
}

.footer-text {
  font-size: 12px;
  color: #94a3b8;
  font-weight: 500;
}

/* ============ CHARTS ============ */
.charts-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(450px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.chart-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 12px;
}

.chart-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: #1e293b;
  display: flex;
  align-items: center;
  gap: 8px;
}

.chart-legend {
  display: flex;
  gap: 16px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
}

.legend-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.legend-dot.cash {
  background: #10b981;
}

.legend-dot.vnpay {
  background: #3b82f6;
}

.chart-body {
  margin-top: 20px;
}

/* Pie Chart */
.pie-chart-container {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.pie-chart {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  position: relative;
  background: conic-gradient(
    #10b981 0deg calc(var(--cash-percentage, 50) * 3.6deg),
    #3b82f6 calc(var(--cash-percentage, 50) * 3.6deg) 360deg
  );
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.pie-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 120px;
  height: 120px;
  background: white;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.pie-total {
  font-size: 16px;
  font-weight: 800;
  color: #1e293b;
}

.pie-label {
  font-size: 11px;
  color: #64748b;
  font-weight: 600;
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
}

.label-text {
  font-size: 14px;
  font-weight: 600;
  color: #475569;
  display: flex;
  align-items: center;
  gap: 8px;
}

.label-value {
  font-size: 15px;
  font-weight: 700;
  color: #667eea;
}

.bar-track {
  height: 40px;
  background: #f1f5f9;
  border-radius: 10px;
  overflow: hidden;
  position: relative;
}

.bar-fill {
  height: 100%;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 12px;
  transition: width 0.8s cubic-bezier(0.4, 0, 0.2, 1);
  color: white;
  font-weight: 700;
  font-size: 13px;
}

.cash-bar {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.vnpay-bar {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
}

.bar-percent {
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

/* Top Products */
.top-products-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.product-item {
  display: flex;
  gap: 16px;
}

.product-rank {
  flex-shrink: 0;
}

.rank-badge {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 16px;
  background: #f1f5f9;
  color: #64748b;
}

.rank-badge.gold {
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
  color: #854d0e;
  box-shadow: 0 4px 12px rgba(255, 215, 0, 0.3);
}

.rank-badge.silver {
  background: linear-gradient(135deg, #c0c0c0 0%, #e8e8e8 100%);
  color: #52525b;
  box-shadow: 0 4px 12px rgba(192, 192, 192, 0.3);
}

.rank-badge.bronze {
  background: linear-gradient(135deg, #cd7f32 0%, #e6a85c 100%);
  color: #78350f;
  box-shadow: 0 4px 12px rgba(205, 127, 50, 0.3);
}

.product-info {
  flex: 1;
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.product-name {
  margin: 0;
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
}

.product-quantity {
  font-size: 13px;
  font-weight: 700;
  color: #667eea;
  background: #f1f5f9;
  padding: 4px 12px;
  border-radius: 12px;
}

.product-bar-track {
  height: 8px;
  background: #f1f5f9;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}

.product-bar-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.8s cubic-bezier(0.4, 0, 0.2, 1);
}

.product-footer {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}

.product-revenue {
  font-weight: 700;
  color: #10b981;
}

.product-price {
  color: #64748b;
  font-weight: 600;
}

/* ============ SUMMARY SECTION ============ */
.summary-section {
  margin-bottom: 30px;
}

.summary-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.summary-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  gap: 16px;
  transition: all 0.3s;
}

.summary-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.summary-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  flex-shrink: 0;
}

.summary-content {
  flex: 1;
}

.summary-content h4 {
  margin: 0 0 8px 0;
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.summary-value {
  font-size: 22px;
  font-weight: 800;
  color: #1e293b;
  margin: 0 0 4px 0;
  line-height: 1;
}

.summary-label {
  font-size: 12px;
  color: #94a3b8;
}

/* ============ TABLE ============ */
.table-section {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.table-header {
  padding: 24px;
  border-bottom: 2px solid #f1f5f9;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.table-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  display: flex;
  align-items: center;
  gap: 8px;
}

.table-actions {
  display: flex;
  gap: 12px;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 12px;
  font-size: 16px;
}

.search-box input {
  padding: 10px 16px 10px 40px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  width: 250px;
  transition: all 0.3s;
}

.search-box input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.btn-export-table {
  padding: 10px 20px;
  background: #10b981;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-export-table:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
}

.table-container {
  overflow-x: auto;
}

.products-table {
  width: 100%;
  border-collapse: collapse;
}

.products-table thead {
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
}

.products-table th {
  padding: 16px 20px;
  text-align: left;
  font-size: 12px;
  font-weight: 700;
  color: #475569;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border-bottom: 2px solid #e2e8f0;
  white-space: nowrap;
}

.products-table th.sortable {
  cursor: pointer;
  user-select: none;
  transition: background 0.2s;
}

.products-table th.sortable:hover {
  background: #e2e8f0;
}

.th-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.sort-icon {
  color: #94a3b8;
  font-size: 12px;
}

.text-right {
  text-align: right;
}

.text-center {
  text-align: center;
}

.products-table td {
  padding: 16px 20px;
  border-bottom: 1px solid #f1f5f9;
}

.products-table tr {
  transition: background 0.2s;
}

.products-table tbody tr:hover {
  background: #f8fafc;
}

.products-table tr.highlight-row {
  background: linear-gradient(90deg, #fef3c7 0%, transparent 100%);
}

.empty-row {
  padding: 60px 20px !important;
}

.empty-state {
  text-align: center;
}

.empty-icon {
  font-size: 48px;
  display: block;
  margin-bottom: 12px;
  opacity: 0.5;
}

.empty-state p {
  color: #94a3b8;
  font-size: 14px;
  margin: 0;
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-name-text {
  font-weight: 600;
  color: #1e293b;
}

.quantity-badge {
  background: #d1fae5;
  color: #065f46;
  padding: 4px 12px;
  border-radius: 12px;
  font-weight: 700;
  font-size: 13px;
}

.price-text {
  color: #64748b;
  font-weight: 600;
}

.revenue-text {
  color: #667eea;
  font-weight: 700;
  font-size: 15px;
}

.percentage-cell {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.percentage-value {
  font-weight: 700;
  color: #667eea;
  font-size: 13px;
}

.mini-bar {
  width: 60px;
  height: 4px;
  background: #f1f5f9;
  border-radius: 2px;
  overflow: hidden;
}

.mini-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
  transition: width 0.6s ease;
}

/* ============ PAGINATION ============ */
.table-pagination {
  padding: 20px 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  border-top: 1px solid #f1f5f9;
}

.page-btn {
  padding: 10px 20px;
  border: 2px solid #e2e8f0;
  background: white;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  color: #475569;
}

.page-btn:hover:not(:disabled) {
  border-color: #667eea;
  color: #667eea;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 6px;
}

.page-number {
  width: 40px;
  height: 40px;
  border: 2px solid #e2e8f0;
  background: white;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  color: #475569;
}

.page-number:hover {
  border-color: #667eea;
  color: #667eea;
}

.page-number.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: #667eea;
}

/* ============ RESPONSIVE ============ */
@media (max-width: 1024px) {
  .charts-section {
    grid-template-columns: 1fr;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .revenue-report {
    padding: 16px;
  }

  .title-section h1 {
    font-size: 24px;
  }

  .header-content {
    flex-direction: column;
    align-items: stretch;
  }

  .header-actions {
    width: 100%;
  }

  .btn-export {
    flex: 1;
    justify-content: center;
  }

  .date-filters {
    flex-direction: column;
    align-items: stretch;
  }

  .btn-refresh {
    width: 100%;
    justify-content: center;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .table-header {
    flex-direction: column;
    align-items: stretch;
  }

  .table-actions {
    flex-direction: column;
  }

  .search-box input {
    width: 100%;
  }

  .table-container {
    overflow-x: auto;
  }

  .products-table {
    min-width: 800px;
  }
}

@media (max-width: 480px) {
  .summary-cards {
    grid-template-columns: 1fr;
  }

  .top-products-list {
    gap: 16px;
  }

  .product-item {
    flex-direction: column;
  }
}
</style>
