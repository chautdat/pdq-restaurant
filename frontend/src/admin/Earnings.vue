<template>
  <div class="shift-earnings-page">
    <!-- ERROR BANNER -->
    <div v-if="errorMsg" class="error-banner">
      <strong>⚠️ Lỗi:</strong> {{ errorMsg }}
      <button @click="errorMsg = ''" class="close-error">✕</button>
    </div>

    <!-- HEADER -->
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">💰 Báo Cáo Thu Nhập Theo Ca</h2>
        <p class="page-subtitle">
          Theo dõi doanh thu từng ca làm việc trong ngày
        </p>
      </div>
    </div>

    <!-- DATE PICKER SECTION -->
    <div class="date-picker-section">
      <div class="date-input-group">
        <label for="reportDate">📅 Chọn ngày:</label>
        <input
          type="date"
          id="reportDate"
          v-model="selectedDate"
          class="date-input"
          :max="today"
        />
        <button @click="fetchReport" class="btn-search" :disabled="loading">
          {{ loading ? "⏳" : "📊" }} Xem báo cáo
        </button>

        <!-- 🆕 EXPORT EXCEL BUTTON -->
        <button
          @click="exportToExcel"
          class="btn-export"
          :disabled="!reportData || loading"
          title="Xuất file Excel"
        >
          📥 Xuất Excel
        </button>
      </div>

      <div class="quick-date-buttons">
        <button @click="setToday" class="btn-quick">Hôm nay</button>
        <button @click="setYesterday" class="btn-quick">Hôm qua</button>
      </div>
    </div>

    <!-- LOADING STATE -->
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <span>Đang tải báo cáo...</span>
    </div>

    <!-- EMPTY STATE -->
    <div v-else-if="!reportData && !errorMsg" class="empty-state">
      <div class="empty-icon">📊</div>
      <p>Chọn ngày và nhấn "Xem báo cáo" để hiển thị dữ liệu</p>
    </div>

    <!-- REPORT CONTENT -->
    <div v-else-if="reportData" class="report-content">
      <!-- DAILY SUMMARY -->
      <div class="daily-summary">
        <div class="summary-card total-revenue">
          <div class="card-icon">💰</div>
          <div class="card-info">
            <span class="card-label">Tổng Doanh Thu Ngày</span>
            <span class="card-value">
              {{ formatPrice(reportData.totalDailyRevenue) }}
            </span>
          </div>
        </div>
        <div class="summary-card total-orders">
          <div class="card-icon">📦</div>
          <div class="card-info">
            <span class="card-label">Tổng Đơn Hàng</span>
            <span class="card-value">{{ reportData.totalDailyOrders }}</span>
          </div>
        </div>
      </div>

      <!-- SHIFTS CONTAINER -->
      <div class="shifts-container">
        <!-- MORNING SHIFT -->
        <div class="shift-panel morning-shift" v-if="morningShift">
          <div class="shift-header morning-header">
            <div class="shift-title">
              <span class="shift-icon">🌅</span>
              <div>
                <h3>{{ morningShift.shiftName }}</h3>
                <p class="shift-time">{{ morningShift.timeRange }}</p>
              </div>
            </div>
          </div>

          <div class="shift-body">
            <!-- Stats -->
            <div class="shift-stats">
              <div class="stat-item">
                <div class="stat-icon-small">💰</div>
                <div>
                  <div class="stat-label">Doanh thu</div>
                  <div class="stat-value">
                    {{ formatPrice(morningShift.revenue) }}
                  </div>
                </div>
              </div>
              <div class="stat-item">
                <div class="stat-icon-small">📦</div>
                <div>
                  <div class="stat-label">Đơn hàng</div>
                  <div class="stat-value">{{ morningShift.totalOrders }}</div>
                </div>
              </div>
            </div>

            <!-- Pie Chart -->
            <div class="chart-container">
              <h4>📊 Phương Thức Thanh Toán</h4>
              <Pie :data="getMorningChartData()" :options="chartOptions" />
            </div>

            <!-- Products List -->
            <div class="products-section">
              <h4>🥇 Sản Phẩm Bán Chạy</h4>
              <div
                v-if="
                  morningShift.productSales && morningShift.productSales.length
                "
                class="products-list"
              >
                <div
                  v-for="(product, index) in morningShift.productSales"
                  :key="product.productId"
                  class="product-item"
                >
                  <div class="product-rank">{{ index + 1 }}</div>
                  <div class="product-details">
                    <div class="product-name">{{ product.productName }}</div>
                    <div class="product-category">
                      {{ product.categoryName }}
                    </div>
                  </div>
                  <div class="product-stats">
                    <div class="product-quantity">
                      {{ product.quantitySold }} phần
                    </div>
                    <div class="product-revenue">
                      {{ formatPrice(product.totalRevenue) }}
                    </div>
                    <div class="product-percentage-bar">
                      <div
                        class="percentage-fill morning-fill"
                        :style="{ width: (product.percentage || 0) + '%' }"
                      ></div>
                      <span class="percentage-text">
                        {{ (product.percentage || 0).toFixed(1) }}%
                      </span>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else class="no-products">
                Không có sản phẩm nào được bán
              </div>
            </div>
          </div>
        </div>

        <!-- EVENING SHIFT -->
        <div class="shift-panel evening-shift" v-if="eveningShift">
          <div class="shift-header evening-header">
            <div class="shift-title">
              <span class="shift-icon">🌙</span>
              <div>
                <h3>{{ eveningShift.shiftName }}</h3>
                <p class="shift-time">{{ eveningShift.timeRange }}</p>
              </div>
            </div>
          </div>

          <div class="shift-body">
            <!-- Stats -->
            <div class="shift-stats">
              <div class="stat-item">
                <div class="stat-icon-small">💰</div>
                <div>
                  <div class="stat-label">Doanh thu</div>
                  <div class="stat-value">
                    {{ formatPrice(eveningShift.revenue) }}
                  </div>
                </div>
              </div>
              <div class="stat-item">
                <div class="stat-icon-small">📦</div>
                <div>
                  <div class="stat-label">Đơn hàng</div>
                  <div class="stat-value">{{ eveningShift.totalOrders }}</div>
                </div>
              </div>
            </div>

            <!-- Pie Chart -->
            <div class="chart-container">
              <h4>📊 Phương Thức Thanh Toán</h4>
              <Pie :data="getEveningChartData()" :options="chartOptions" />
            </div>

            <!-- Products List -->
            <div class="products-section">
              <h4>🥇 Sản Phẩm Bán Chạy</h4>
              <div
                v-if="
                  eveningShift.productSales && eveningShift.productSales.length
                "
                class="products-list"
              >
                <div
                  v-for="(product, index) in eveningShift.productSales"
                  :key="product.productId"
                  class="product-item"
                >
                  <div class="product-rank">{{ index + 1 }}</div>
                  <div class="product-details">
                    <div class="product-name">{{ product.productName }}</div>
                    <div class="product-category">
                      {{ product.categoryName }}
                    </div>
                  </div>
                  <div class="product-stats">
                    <div class="product-quantity">
                      {{ product.quantitySold }} phần
                    </div>
                    <div class="product-revenue">
                      {{ formatPrice(product.totalRevenue) }}
                    </div>
                    <div class="product-percentage-bar">
                      <div
                        class="percentage-fill evening-fill"
                        :style="{ width: (product.percentage || 0) + '%' }"
                      ></div>
                      <span class="percentage-text">
                        {{ (product.percentage || 0).toFixed(1) }}%
                      </span>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else class="no-products">
                Không có sản phẩm nào được bán
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Pie } from "vue-chartjs";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import api from "@/axios";
import storage from "@/utils/storage";

ChartJS.register(ArcElement, Tooltip, Legend);

export default {
  name: "ShiftEarnings",

  components: {
    Pie,
  },

  data() {
    return {
      selectedDate: "",
      today: "",
      reportData: null,
      loading: false,
      errorMsg: "",
    };
  },

  computed: {
    morningShift() {
      return this.reportData?.shifts?.find((s) => s.shiftType === "MORNING");
    },
    eveningShift() {
      return this.reportData?.shifts?.find((s) => s.shiftType === "EVENING");
    },
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: {
            position: "bottom",
            labels: {
              padding: 15,
              font: {
                size: 13,
                weight: "600",
              },
            },
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                const label = context.label || "";
                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                const percentage = total
                  ? ((context.parsed / total) * 100).toFixed(1)
                  : "0.0";
                return `${label}: ${this.formatPrice(
                  context.parsed
                )} (${percentage}%)`;
              },
            },
          },
        },
      };
    },
  },

  mounted() {
    const now = new Date();
    this.today = now.toISOString().split("T")[0];
    this.selectedDate = this.today;
  },

  methods: {
    setToday() {
      this.selectedDate = this.today;
      this.fetchReport();
    },

    setYesterday() {
      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);
      this.selectedDate = yesterday.toISOString().split("T")[0];
      this.fetchReport();
    },

    async fetchReport() {
      if (!this.selectedDate) {
        this.errorMsg = "Vui lòng chọn ngày báo cáo";
        return;
      }

      const token = storage.getToken();
      if (!token) {
        this.$router.push("/login");
        return;
      }

      try {
        this.loading = true;
        this.errorMsg = "";

        console.log("📊 Fetching shift earnings for date:", this.selectedDate);

        const response = await api.get("/admin/reports/shift-earnings", {
          headers: { Authorization: `Bearer ${token}` },
          params: { date: this.selectedDate },
        });

        console.log("✅ Response:", response.data);

        if (response.data?.data) {
          this.reportData = response.data.data;
        } else {
          this.reportData = response.data;
        }

        console.log("📈 Report data:", this.reportData);
      } catch (error) {
        console.error("❌ Error fetching shift earnings:", error);
        this.errorMsg =
          error.response?.data?.message ||
          "Không thể tải báo cáo thu nhập theo ca";

        if (error.response?.status === 401 || error.response?.status === 403) {
          storage.clearAuth();
          this.$router.push("/login");
        }
      } finally {
        this.loading = false;
      }
    },

    /**
     * 📥 Export shift earnings report to Excel
     */
    async exportToExcel() {
      if (!this.selectedDate) {
        this.errorMsg = "Vui lòng chọn ngày báo cáo";
        return;
      }

      if (!this.reportData) {
        this.errorMsg = "Không có dữ liệu để xuất. Vui lòng xem báo cáo trước.";
        return;
      }

      const token = storage.getToken();
      if (!token) {
        this.$router.push("/login");
        return;
      }

      try {
        console.log("📥 Exporting shift earnings to Excel...");
        console.log("   Date:", this.selectedDate);

        const response = await api.get("/admin/reports/shift-earnings/export", {
          headers: { Authorization: `Bearer ${token}` },
          params: { date: this.selectedDate },
          responseType: "blob", // ⚠️ CRITICAL: Must be 'blob' for binary data
        });

        console.log("✅ Excel data received");
        console.log("   Size:", response.data.size, "bytes");

        // Create blob from response
        const blob = new Blob([response.data], {
          type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        });

        // Create download URL
        const url = window.URL.createObjectURL(blob);

        // Create temporary link and trigger download
        const link = document.createElement("a");
        link.href = url;

        // Filename: BaoCaoThuNhap_28112024.xlsx
        const dateFormatted = this.selectedDate.replace(/-/g, "");
        link.download = `BaoCaoThuNhap_${dateFormatted}.xlsx`;

        // Trigger download
        document.body.appendChild(link);
        link.click();

        // Cleanup
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);

        console.log("✅ Excel file downloaded successfully");
        console.log("   Filename:", link.download);
      } catch (error) {
        console.error("❌ Error exporting Excel:", error);
        this.errorMsg = "Không thể xuất file Excel. Vui lòng thử lại.";

        if (error.response?.status === 401 || error.response?.status === 403) {
          storage.clearAuth();
          this.$router.push("/login");
        }
      }
    },

    getMorningChartData() {
      if (!this.morningShift) return {};
      return {
        labels: ["💵 Tiền mặt", "💳 VNPay"],
        datasets: [
          {
            data: [
              this.morningShift.cashRevenue || 0,
              this.morningShift.vnpayRevenue || 0,
            ],
            backgroundColor: ["#fbbf24", "#3b82f6"],
            borderColor: ["#f59e0b", "#2563eb"],
            borderWidth: 2,
          },
        ],
      };
    },

    getEveningChartData() {
      if (!this.eveningShift) return {};
      return {
        labels: ["💵 Tiền mặt", "💳 VNPay"],
        datasets: [
          {
            data: [
              this.eveningShift.cashRevenue || 0,
              this.eveningShift.vnpayRevenue || 0,
            ],
            backgroundColor: ["#fbbf24", "#3b82f6"],
            borderColor: ["#f59e0b", "#2563eb"],
            borderWidth: 2,
          },
        ],
      };
    },

    formatPrice(price) {
      const value = Number(price || 0);
      return value.toLocaleString("vi-VN") + "₫";
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

.shift-earnings-page {
  padding: 24px 32px;
  background: linear-gradient(135deg, #f4f7fb 0%, #e1e9f7 100%);
  min-height: 100vh;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

/* ========== ERROR BANNER ========== */
.error-banner {
  background: linear-gradient(135deg, #e6eefc 0%, #e6eefc 100%);
  color: #dc2626;
  padding: 16px 24px;
  border-radius: 12px;
  margin-bottom: 24px;
  border: 1px solid #c6d7f7;
  display: flex;
  justify-content: space-between;
  align-items: center;
  animation: slideDown 0.3s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.close-error {
  background: #dc2626;
  color: white;
  border: none;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
}

/* ========== HEADER ========== */
.page-header {
  margin-bottom: 28px;
}

.page-title {
  font-size: 2.2rem;
  font-weight: 800;
  color: #2563eb;
  margin-bottom: 8px;
}

.page-subtitle {
  font-size: 1rem;
  color: #3b82f6;
  font-weight: 500;
}

/* ========== DATE PICKER ========== */
.date-picker-section {
  background: white;
  padding: 20px 24px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.date-input-group {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
  min-width: 300px;
  flex-wrap: wrap;
}

.date-input-group label {
  font-weight: 600;
  color: #6b7280;
  font-size: 14px;
}

.date-input {
  padding: 10px 16px;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
  min-width: 160px;
}

.date-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.1);
}

.btn-search {
  padding: 10px 24px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.btn-search:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(139, 92, 246, 0.4);
}

.btn-search:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* ========== EXPORT BUTTON ========== */
.btn-export {
  padding: 10px 24px;
  background: linear-gradient(135deg, #22c55e 0%, #16a34a 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  box-shadow: 0 2px 8px rgba(34, 197, 94, 0.2);
}

.btn-export:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(34, 197, 94, 0.4);
}

.btn-export:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background: linear-gradient(135deg, #9ca3af 0%, #6b7280 100%);
}

.btn-export:active:not(:disabled) {
  transform: translateY(0);
}

.quick-date-buttons {
  display: flex;
  gap: 10px;
}

.btn-quick {
  padding: 10px 20px;
  background: rgba(139, 92, 246, 0.1);
  color: #2563eb;
  border: 2px solid #3b82f6;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-quick:hover {
  background: #3b82f6;
  color: white;
}

/* ========== LOADING & EMPTY ========== */
.loading-state,
.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  color: #3b82f6;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 5px solid #e1e9f7;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.empty-state {
  background: white;
  border-radius: 20px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.1);
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 16px;
}

.empty-state p {
  color: #6b7280;
  font-size: 1.1rem;
}

/* ========== DAILY SUMMARY ========== */
.daily-summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 28px;
}

.summary-card {
  background: white;
  padding: 24px 28px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 20px;
  transition: all 0.3s ease;
}

.summary-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.summary-card.total-revenue {
  background: linear-gradient(135deg, #e8f0ff 0%, #dbeafe 100%);
  border-left: 5px solid #f59e0b;
}

.summary-card.total-orders {
  background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
  border-left: 5px solid #3b82f6;
}

.card-icon {
  font-size: 2.5rem;
}

.card-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.card-label {
  font-size: 0.9rem;
  color: #6b7280;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.card-value {
  font-size: 2rem;
  font-weight: 800;
  color: #1f2937;
}

/* ========== SHIFTS CONTAINER ========== */
.shifts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(450px, 1fr));
  gap: 24px;
}

.shift-panel {
  background: white;
  border-radius: 20px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
}

.shift-panel:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 50px rgba(0, 0, 0, 0.15);
}

.shift-header {
  padding: 20px 24px;
  color: white;
}

.morning-header {
  background: linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%);
}

.evening-header {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
}

.shift-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.shift-icon {
  font-size: 2rem;
}

.shift-title h3 {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 4px;
}

.shift-time {
  font-size: 0.95rem;
  opacity: 0.95;
  font-weight: 500;
}

.shift-body {
  padding: 24px;
}

/* ========== SHIFT STATS ========== */
.shift-stats {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-item {
  background: #f9fafb;
  padding: 16px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
  border: 2px solid #f3f4f6;
}

.stat-icon-small {
  font-size: 1.8rem;
}

.stat-label {
  font-size: 0.85rem;
  color: #6b7280;
  font-weight: 600;
  text-transform: uppercase;
}

.stat-value {
  font-size: 1.3rem;
  font-weight: 800;
  color: #1f2937;
}

/* ========== CHART CONTAINER ========== */
.chart-container {
  margin-bottom: 28px;
  background: #fafafa;
  padding: 20px;
  border-radius: 16px;
  border: 2px solid #f3f4f6;
}

.chart-container h4 {
  font-size: 1.1rem;
  color: #2563eb;
  margin-bottom: 16px;
  font-weight: 700;
}

/* ========== PRODUCTS SECTION ========== */
.products-section h4 {
  font-size: 1.1rem;
  color: #2563eb;
  margin-bottom: 16px;
  font-weight: 700;
}

.products-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.product-item {
  background: #fafafa;
  border: 2px solid #f3f4f6;
  border-radius: 12px;
  padding: 16px;
  display: grid;
  grid-template-columns: 40px 1fr 200px;
  gap: 16px;
  align-items: center;
  transition: all 0.3s ease;
}

.product-item:hover {
  background: #f4f7fb;
  border-color: #3b82f6;
}

.product-rank {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 1.1rem;
}

.product-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.product-name {
  font-size: 1rem;
  font-weight: 700;
  color: #1f2937;
}

.product-category {
  font-size: 0.85rem;
  color: #6b7280;
  font-weight: 500;
}

.product-stats {
  display: flex;
  flex-direction: column;
  gap: 6px;
  align-items: flex-end;
}

.product-quantity {
  font-size: 0.9rem;
  color: #6b7280;
  font-weight: 600;
}

.product-revenue {
  font-size: 1.1rem;
  font-weight: 800;
  color: #2563eb;
}

.product-percentage-bar {
  width: 100%;
  height: 24px;
  background: #e5e7eb;
  border-radius: 12px;
  position: relative;
  overflow: hidden;
}

.percentage-fill {
  height: 100%;
  border-radius: 12px;
  transition: width 0.6s ease;
}

.morning-fill {
  background: linear-gradient(90deg, #fbbf24 0%, #f59e0b 100%);
}

.evening-fill {
  background: linear-gradient(90deg, #3b82f6 0%, #2563eb 100%);
}

.percentage-text {
  position: absolute;
  top: 50%;
  right: 8px;
  transform: translateY(-50%);
  font-size: 0.75rem;
  font-weight: 700;
  color: white;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.no-products {
  text-align: center;
  padding: 40px 20px;
  color: #9ca3af;
  font-size: 1rem;
  background: #fafafa;
  border-radius: 12px;
  border: 2px dashed #e5e7eb;
}

/* ========== RESPONSIVE ========== */
@media (max-width: 1200px) {
  .shifts-container {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .shift-earnings-page {
    padding: 16px;
  }

  .page-title {
    font-size: 1.6rem;
  }

  .date-picker-section {
    flex-direction: column;
    align-items: stretch;
  }

  .date-input-group {
    flex-direction: column;
    align-items: stretch;
    min-width: 100%;
  }

  .btn-search,
  .btn-export,
  .date-input {
    width: 100%;
    margin-top: 8px;
  }

  .quick-date-buttons {
    width: 100%;
  }

  .btn-quick {
    flex: 1;
  }

  .daily-summary {
    grid-template-columns: 1fr;
  }

  .shifts-container {
    grid-template-columns: 1fr;
  }

  .product-item {
    grid-template-columns: 40px 1fr;
  }

  .product-stats {
    grid-column: 1 / -1;
    align-items: stretch;
    margin-top: 8px;
  }

  .shift-stats {
    grid-template-columns: 1fr;
  }
}
</style>
