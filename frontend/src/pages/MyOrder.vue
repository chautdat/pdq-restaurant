<template>
  <div class="my-order-page">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-title">
          <i class="fas fa-clipboard-list"></i>
          <div>
            <h1>Đơn Hàng Của Tôi</h1>
            <p>Theo dõi trạng thái đơn hàng của bạn</p>
          </div>
        </div>
        <div class="header-stats">
          <div class="stat-card">
            <i class="fas fa-box"></i>
            <div>
              <span class="stat-number">{{ orders.length }}</span>
              <span class="stat-label">Đơn hàng</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="isLoading" class="loading-container">
      <div class="spinner-large"></div>
      <p>Đang tải đơn hàng...</p>
    </div>

    <!-- Orders List -->
    <div v-else class="orders-container">
      <div v-if="orders.length > 0" class="orders-grid">
        <!-- ORDER CARD -->
        <div v-for="order in orders" :key="order.orderId" class="order-card">
          <!-- Header -->
          <div class="order-header">
            <div class="order-id">
              <i class="fas fa-receipt"></i>
              <span>{{ order.orderNumber }}</span>
            </div>
            <div class="header-badges">
              <span
                :class="[
                  'badge',
                  'status-' + normalizeStatus(order.orderStatus),
                ]"
              >
                {{ getOrderStatusText(order.orderStatus) }}
              </span>
              <span
                :class="[
                  'badge',
                  'payment-' + normalizeStatus(order.paymentStatus),
                ]"
              >
                {{ getPaymentStatusText(order.paymentStatus) }}
              </span>
            </div>
          </div>

          <!-- Info -->
          <div class="order-info-grid">
            <div class="info-item">
              <i class="fas fa-calendar-check"></i>
              <div>
                <span class="info-label">Thời gian</span>
                <p class="info-value">{{ formatDate(order.createdAt) }}</p>
              </div>
            </div>

            <div class="info-item">
              <i class="fas fa-credit-card"></i>
              <div>
                <span class="info-label">Thanh toán</span>
                <p class="info-value">
                  {{ getPaymentMethodText(order.paymentMethod) }}
                </p>
              </div>
            </div>

            <div class="info-item">
              <i class="fas fa-map-marker-alt"></i>
              <div>
                <span class="info-label">Địa chỉ</span>
                <p class="info-value">{{ getAddress(order) }}</p>
              </div>
            </div>

            <div class="info-item">
              <i class="fas fa-phone"></i>
              <div>
                <span class="info-label">SĐT</span>
                <p class="info-value">{{ order.phone }}</p>
              </div>
            </div>
          </div>

          <!-- Items -->
          <div v-if="getItems(order).length" class="order-items">
            <h5>📦 Sản phẩm đã đặt:</h5>

            <div
              v-for="item in getItems(order)"
              :key="item.orderItemId || item.id || item.productId"
              class="order-item"
            >
              <img :src="getImageUrl(item.productImage)" class="item-image" />

              <div class="item-details">
                <h4>{{ item.productName }}</h4>
                <p class="item-quantity">Số lượng: {{ item.quantity }}</p>
              </div>

              <div class="item-price">
                <p>{{ formatVND(item.price) }}đ</p>
                <p class="item-subtotal">
                  Tổng: {{ formatVND(item.subtotal) }}đ
                </p>
              </div>
            </div>
          </div>

          <!-- Progress -->
          <div class="progress-container">
            <div class="progress-bar">
              <div
                class="progress-fill"
                :style="{ width: getProgressWidth(order.orderStatus) + '%' }"
              ></div>
            </div>

            <div class="progress-steps">
              <div
                v-for="step in progressSteps"
                :key="step.status"
                class="progress-step"
                :class="{
                  active: isStepActive(order.orderStatus, step.status),
                }"
              >
                <div class="step-dot"><i :class="step.icon"></i></div>
                <span class="step-label">{{ step.label }}</span>
              </div>
            </div>
          </div>

          <!-- Payment retry / COD -->
          <div
            v-if="showPaymentActions(order)"
            class="payment-warning"
          >
            <div class="payment-warning-text">
              ⏳ Thanh toán online chưa hoàn tất
              <span v-if="getRemainingTime(order)" class="countdown">
                ({{ getRemainingTime(order) }})
              </span>
            </div>
            <div class="payment-actions-inline">
              <button
                v-if="canRetryPayment(order)"
                class="btn btn-warning"
                @click="retryPayment(order)"
                :disabled="actionLoading"
              >
                💳 Thanh toán lại
                <span v-if="getRemainingTime(order)" class="countdown">
                  ({{ getRemainingTime(order) }})
                </span>
              </button>
              <button
                v-if="canConvertToCOD(order)"
                class="btn btn-outline"
                @click="convertToCOD(order)"
                :disabled="actionLoading"
              >
                💵 Chuyển sang tiền mặt
              </button>
              <button
                v-if="canUserCancel(order)"
                class="btn btn-ghost-danger"
                @click="cancelOrder(order)"
                :disabled="actionLoading"
              >
                ❌ Hủy đơn
              </button>
            </div>
          </div>

          <!-- Totals -->
          <div class="order-total">
            <div class="total-row">
              <span>Tổng tiền món:</span>
              <span>{{ formatVND(getSubtotal(order)) }}đ</span>
            </div>
            <div class="total-row">
              <span>Phí giao hàng:</span>
              <span>{{ formatVND(getShipping(order)) }}đ</span>
            </div>
            <div class="total-row final">
              <span>Tổng cộng:</span>
              <span class="total-amount"
                >{{ formatVND(getFinalAmount(order)) }}đ</span
              >
            </div>
          </div>
        </div>
      </div>

      <!-- Empty -->
      <div v-else class="empty-state">
        <div class="empty-icon"><i class="fas fa-box-open"></i></div>
        <h2>Bạn chưa có đơn hàng nào</h2>
        <p>Hãy đặt món ngay!</p>
        <router-link to="/menu" class="order-now-btn">
          <i class="fas fa-shopping-bag"></i> Đặt món
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/axios";
import { mapState } from "vuex";
import storage from "@/utils/storage";
import Swal from "sweetalert2";

export default {
  name: "MyOrder",

  data() {
    return {
      orders: [],
      isLoading: false,
      actionLoading: false,
      refreshTimer: null,
      autoRefreshMs: 7000,
      countdownTimer: null,

      progressSteps: [
        { status: "pending", icon: "fas fa-clock", label: "Chờ xác nhận" },
        {
          status: "confirmed",
          icon: "fas fa-check-circle",
          label: "Đã xác nhận",
        },
        {
          status: "preparing",
          icon: "fas fa-fire-burner",
          label: "Đang chuẩn bị",
        },
        {
          status: "shipping",
          icon: "fas fa-shipping-fast",
          label: "Đang giao",
        },
        { status: "delivered", icon: "fas fa-home", label: "Đã giao" },
      ],
    };
  },

  computed: {
    ...mapState(["user"]),
  },

  mounted() {
    this.loadOrders();
    this.startAutoRefresh();
    this.countdownTimer = setInterval(() => {
      this.$forceUpdate();
    }, 1000);
  },

  beforeUnmount() {
    this.stopAutoRefresh();
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer);
      this.countdownTimer = null;
    }
  },

  methods: {
    startAutoRefresh() {
      this.stopAutoRefresh();
      this.refreshTimer = setInterval(() => {
        this.loadOrders();
      }, this.autoRefreshMs);
    },

    stopAutoRefresh() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer);
        this.refreshTimer = null;
      }
    },

    async loadOrders() {
      if (!this.user) return this.$router.push("/login");

      const showSpinner = this.orders.length === 0;
      if (showSpinner) this.isLoading = true;

      try {
        const res = await api.get("/orders/my-orders");

        console.log("🔍 Orders API:", res.data);

        const apiData = res.data;
        if (apiData.success) {
          const data = apiData.data;
          const rawOrders = Array.isArray(data)
            ? data
            : apiData.data?.content || [];
          this.orders = rawOrders.map((o) => this.normalizeOrder(o));
        } else {
          this.orders = [];
        }
      } catch (err) {
        console.error("Lỗi tải đơn:", err);
        if (err.response?.status === 401) {
          storage.clearAll();
          this.$store.commit("setUser", null);
          return this.$router.push("/login");
        }
      } finally {
        if (showSpinner) this.isLoading = false;
      }
    },

    /* UTIL FUNCS */

    getImageUrl(path) {
      if (!path) return "/images/notfound.png";
      const base = "http://localhost:3000";
      if (path.startsWith("http")) return path;
      if (path.startsWith("/uploads")) return base + path;
      return base + "/uploads/" + path;
    },

    formatVND(v) {
      return new Intl.NumberFormat("vi-VN").format(v || 0);
    },

    formatDate(date) {
      if (!date) return "N/A";
      return new Date(date).toLocaleString("vi-VN");
    },

    normalizeOrder(order) {
      if (!order) return order;
      return {
        ...order,
        orderStatus: (order.orderStatus || order.status || "").toString().toLowerCase(),
        paymentStatus: (order.paymentStatus || "").toString().toLowerCase(),
        paymentMethod: (order.paymentMethod || "").toString().toLowerCase(),
      };
    },

    normalizeStatus(s) {
      return (s || "").toString().toLowerCase();
    },

    getOrderStatusText(s) {
      const key = this.normalizeStatus(s);
      const map = {
        pending: "Chờ xác nhận",
        confirmed: "Đã xác nhận",
        preparing: "Đang chuẩn bị",
        shipping: "Đang giao",
        delivering: "Đang giao",
        delivered: "Đã giao",
        cancelled: "Đã huỷ",
      };
      return map[key] || s || "Không xác định";
    },

    getPaymentStatusText(s) {
      const key = this.normalizeStatus(s);
      const map = {
        pending: "Chưa thanh toán",
        unpaid: "Chưa thanh toán",
        paid: "Đã thanh toán",
        failed: "Thất bại",
      };
      return map[key] || s || "Không xác định";
    },

    getPaymentMethodText(m) {
      const key = this.normalizeStatus(m);
      const map = { cash: "Tiền mặt", vnpay: "VNPay", zalopay: "ZaloPay" };
      return map[key] || m || "Khác";
    },

    getItems(order) {
      if (Array.isArray(order?.items) && order.items.length) return order.items;
      if (Array.isArray(order?.orderItems) && order.orderItems.length)
        return order.orderItems;
      return [];
    },

    getProgressWidth(status) {
      const key = this.normalizeStatus(status);
      const steps = {
        pending: 0,
        confirmed: 25,
        preparing: 50,
        shipping: 75,
        delivering: 75,
        delivered: 100,
        cancelled: 0,
      };
      return steps[key] || 0;
    },

    isStepActive(current, step) {
      const orderStatus = this.normalizeStatus(current);
      const target = this.normalizeStatus(step);
      const arr = [
        "pending",
        "confirmed",
        "preparing",
        "shipping",
        "delivering",
        "delivered",
      ];
      return arr.indexOf(orderStatus) >= arr.indexOf(target);
    },

    getAddress(order) {
      return order.deliveryAddress || order.addressLine || "";
    },

    getSubtotal(order) {
      const candidates = [
        order?.subtotal,
        order?.subTotal,
        order?.totalPrice,
        order?.total,
        order?.amount,
        order?.itemsTotal,
        order?.itemsSubtotal,
        order?.productsTotal,
        order?.totalAmount && order?.shippingFee != null
          ? order.totalAmount - (order.shippingFee || 0)
          : null,
      ].map((v) => Number(v));

      const fromFields = candidates.find((v) => !Number.isNaN(v) && v > 0);
      if (fromFields) return fromFields;

      const items = this.getItems(order);
      if (Array.isArray(items)) {
        const sum = items.reduce((acc, it) => {
          const subtotal = Number(it?.subtotal);
          const priceQty = Number(it?.price) * Number(it?.quantity || 0);
          const itemVal =
            !Number.isNaN(subtotal) && subtotal > 0
              ? subtotal
              : !Number.isNaN(priceQty) && priceQty > 0
              ? priceQty
              : 0;
          return acc + itemVal;
        }, 0);
        if (sum > 0) return sum;
      }

      return 0;
    },

    getShipping(order) {
      const candidates = [
        order?.shippingFee,
        order?.deliveryFee,
        order?.shippingCost,
        order?.feeShip,
        order?.feeShipping,
      ].map((v) => Number(v));
      const found = candidates.find((v) => !Number.isNaN(v));
      return found || 0;
    },

    getFinalAmount(order) {
      const shipping = this.getShipping(order);
      const subtotal = this.getSubtotal(order);

      const primaryList = [
        order.finalAmount,
        order.totalAmount,
        order.grandTotal,
        order.totalCost,
        order.total,
      ].map((v) => Number(v));
      const primary = primaryList.find(
        (v) => !Number.isNaN(v) && Number.isFinite(v) && v >= 0
      );

      const subtotalPlusShip = subtotal + shipping;

      // Nếu primary hợp lý (>= subtotalPlusShip * 0.5) thì dùng, ngược lại ưu tiên subtotal + ship
      if (
        primary !== undefined &&
        primary !== null &&
        primary >= Math.max(subtotalPlusShip * 0.5, 0)
      ) {
        return primary;
      }

      if (subtotalPlusShip > 0) return subtotalPlusShip;

      return primary || 0;
    },

    canRetryPayment(order) {
      return (
        this.normalizeStatus(order.paymentStatus) === "pending" &&
        (this.normalizeStatus(order.paymentMethod) === "vnpay" ||
          this.normalizeStatus(order.paymentMethod) === "zalopay") &&
        (order.retryCount || 0) < (order.maxRetries || 0) &&
        this.getRemainingTime(order) !== null
      );
    },

    canConvertToCOD(order) {
      return (
        this.normalizeStatus(order.paymentStatus) === "pending" &&
        (this.normalizeStatus(order.paymentMethod) === "vnpay" ||
          this.normalizeStatus(order.paymentMethod) === "zalopay")
      );
    },

    showPaymentActions(order) {
      const status = this.normalizeStatus(order.orderStatus);
      // Chỉ hiển thị khi đơn chưa bị hủy và đang chờ thanh toán online
      if (status === "cancelled") return false;
      return this.canRetryPayment(order) || this.canConvertToCOD(order);
    },

    getRemainingTime(order) {
      if (!order.paymentExpiresAt) return null;
      const expiresAt = new Date(order.paymentExpiresAt);
      const now = new Date();
      const diff = expiresAt - now;
      if (diff <= 0) return null;
      const minutes = Math.floor(diff / 60000);
      const seconds = Math.floor((diff % 60000) / 1000);
      return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
    },

    canUserCancel(order) {
      const status = this.normalizeStatus(order.orderStatus);
      if (status !== "pending") return false;
      if (!order.createdAt) return false;
      const created = new Date(order.createdAt);
      const now = new Date();
      const diffMs = now - created;
      return diffMs <= 5 * 60 * 1000; // trong 5 phút
    },

    async cancelOrder(order) {
      if (!this.canUserCancel(order)) {
        return Swal.fire({
          icon: "info",
          title: "Không thể hủy",
          text: "Chỉ hủy được đơn trong 5 phút sau khi đặt và khi đang chờ xác nhận.",
          confirmButtonColor: "#00b067",
        });
      }

      const confirm = await Swal.fire({
        title: "Hủy đơn hàng?",
        text: "Bạn có chắc muốn hủy đơn này?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#ef4444",
        cancelButtonColor: "#6b7280",
        confirmButtonText: "Hủy đơn",
        cancelButtonText: "Không",
        input: "textarea",
        inputPlaceholder: "Lý do hủy (không bắt buộc)",
      });

      if (!confirm.isConfirmed) return;

      try {
        this.actionLoading = true;
        const token = storage.getToken();
        await api.post(
          `/orders/${order.orderId}/cancel`,
          { reason: confirm.value || "User cancelled" },
          { headers: { Authorization: `Bearer ${token}` } }
        );

        Swal.fire({
          icon: "success",
          title: "Đã hủy đơn",
          showConfirmButton: false,
          timer: 1500,
        });
        this.loadOrders();
      } catch (err) {
        console.error("Cancel order error:", err);
        Swal.fire(
          "Lỗi",
          err?.response?.data?.message || "Không thể hủy đơn",
          "error"
        );
      } finally {
        this.actionLoading = false;
      }
    },

    async retryPayment(order) {
      try {
        this.actionLoading = true;
        const token = storage.getToken();

        const result = await Swal.fire({
          title: "Chọn phương thức thanh toán",
          width: 520,
          input: "select",
          inputOptions: {
            VNPAY: "VNPay",
            ZALOPAY: "ZaloPay",
          },
          inputValue: this.normalizeStatus(order.paymentMethod).toUpperCase(),
          showCancelButton: true,
          confirmButtonText: "Tiếp tục",
          cancelButtonText: "Hủy",
        });

        if (!result.isConfirmed) return;

        const res = await api.post(
          `/orders/${order.orderId}/retry-payment`,
          { paymentMethod: result.value },
          { headers: { Authorization: `Bearer ${token}` } }
        );

        if (res.data?.data?.paymentUrl) {
          window.location.href = res.data.data.paymentUrl;
        } else {
          Swal.fire("Đã chuyển COD", "", "success");
          this.loadOrders();
        }
      } catch (err) {
        console.error("Retry payment error:", err);
        Swal.fire(
          "Lỗi",
          err?.response?.data?.message || "Không thể thanh toán lại",
          "error"
        );
      } finally {
        this.actionLoading = false;
      }
    },

    async convertToCOD(order) {
      const confirm = await Swal.fire({
        title: "Chuyển sang thanh toán tiền mặt?",
        text: "Đơn sẽ thanh toán khi nhận hàng.",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#00b067",
        cancelButtonColor: "#6b7280",
        confirmButtonText: "Xác nhận",
        cancelButtonText: "Hủy",
      });

      if (!confirm.isConfirmed) return;

      try {
        this.actionLoading = true;
        const token = storage.getToken();
        await api.post(
          `/orders/${order.orderId}/convert-to-cod`,
          {},
          { headers: { Authorization: `Bearer ${token}` } }
        );

        Swal.fire({
          icon: "success",
          title: "Đã chuyển sang COD",
          showConfirmButton: false,
          timer: 1500,
        });
        this.loadOrders();
      } catch (err) {
        console.error("Convert COD error:", err);
        Swal.fire(
          "Lỗi",
          err?.response?.data?.message || "Không thể chuyển sang COD",
          "error"
        );
      } finally {
        this.actionLoading = false;
      }
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

.my-order-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8f5e9 100%);
  padding-bottom: 40px;
}

/* Loading */
.loading-container {
  text-align: center;
  padding: 4rem 0;
}

.spinner-large {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #00b067;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* Page Header */
.page-header {
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  padding: 40px 5%;
  color: white;
  box-shadow: 0 4px 20px rgba(0, 176, 103, 0.2);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-title i {
  font-size: 48px;
  opacity: 0.9;
}

.header-title h1 {
  font-size: 32px;
  font-weight: 800;
  margin: 0 0 4px 0;
}

.header-title p {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.header-stats {
  display: flex;
  gap: 20px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  padding: 16px 24px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-card i {
  font-size: 32px;
}

.stat-number {
  display: block;
  font-size: 28px;
  font-weight: 800;
  line-height: 1;
}

.stat-label {
  display: block;
  font-size: 13px;
  opacity: 0.9;
  margin-top: 4px;
}

/* Orders Container */
.orders-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 5%;
}

.orders-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(500px, 1fr));
  gap: 24px;
}

/* Order Card */
.order-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.order-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

/* Order Header */
.order-header {
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.order-id {
  display: flex;
  align-items: center;
  gap: 10px;
  color: white;
  font-size: 16px;
  font-weight: 700;
}

.order-id i {
  font-size: 20px;
}

.header-badges {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.badge {
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.status-pending {
  background: #fff3cd;
  color: #856404;
}
.status-confirmed {
  background: #d1ecf1;
  color: #0c5460;
}
.status-preparing {
  background: #fff3cd;
  color: #856404;
}
.status-shipping {
  background: #cce5ff;
  color: #004085;
}
.status-delivered {
  background: #d4edda;
  color: #155724;
}
.status-cancelled {
  background: #f8d7da;
  color: #721c24;
}

.payment-pending {
  background: #fff3cd;
  color: #856404;
}
.payment-paid {
  background: #d4edda;
  color: #155724;
}
.payment-failed {
  background: #f8d7da;
  color: #721c24;
}

/* Order Info Grid */
.order-info-grid {
  padding: 24px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.info-item i {
  font-size: 20px;
  color: #00b067;
  margin-top: 2px;
}

.info-label {
  display: block;
  font-size: 12px;
  color: #666;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 4px;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: 600;
  margin: 0;
}

/* Order Items */
.order-items {
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.order-items h5 {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  font-weight: 600;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 12px;
  margin-bottom: 10px;
}

.order-item:last-child {
  margin-bottom: 0;
}

.item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
}

.item-details {
  flex: 1;
}

.item-details h4 {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.item-quantity {
  font-size: 12px;
  color: #666;
}

.item-price {
  text-align: right;
}

.item-price p {
  font-size: 14px;
  color: #00b067;
  font-weight: 600;
  margin: 0;
}

.item-subtotal {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

/* Progress Container */
.progress-container {
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.progress-bar {
  height: 6px;
  background: #e9ecef;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 20px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #00b067 0%, #00d97e 100%);
  transition: width 0.5s ease;
  border-radius: 10px;
}

.progress-steps {
  display: flex;
  justify-content: space-between;
  gap: 8px;
}

.progress-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  opacity: 0.4;
  transition: all 0.3s ease;
}

.progress-step.active {
  opacity: 1;
}

.progress-step.current .step-dot {
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.step-dot {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  transition: all 0.3s ease;
}

.progress-step.active .step-dot {
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(0, 176, 103, 0.3);
}

.step-dot i {
  font-size: 16px;
}

.step-label {
  font-size: 11px;
  font-weight: 600;
  text-align: center;
  color: #666;
}

.progress-step.active .step-label {
  color: #00b067;
}

/* Order Total */
.order-total {
  padding: 20px 24px;
  background: #f8fffe;
}

.total-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 14px;
  color: #666;
}

.total-row.final {
  border-top: 2px solid #e8f6ee;
  margin-top: 8px;
  padding-top: 12px;
  font-size: 16px;
  color: #333;
  font-weight: 700;
}

.total-amount {
  color: #00b067;
  font-size: 20px;
  font-weight: 900;
}

.btn {
  padding: 8px 14px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
}
.btn-outline {
  background: white;
  border: 2px solid #e5e7eb;
  color: #374151;
}
.btn-outline:hover {
  border-color: #00b067;
  color: #00b067;
}
.btn-ghost-danger {
  background: transparent;
  color: #ef4444;
}
.btn-ghost-danger:hover:not(:disabled) {
  background: rgba(248, 113, 113, 0.08);
}
.btn-warning {
  background: #f59e0b;
  color: white;
}
.btn-warning:hover:not(:disabled) {
  background: #d97706;
}
.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.countdown {
  font-size: 0.9rem;
  font-weight: 700;
  color: #ef4444;
  margin-left: 6px;
}
.payment-warning {
  background: #fef3c7;
  border: 1px solid #fbbf24;
  border-radius: 10px;
  padding: 12px;
  margin: 12px 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.payment-actions-inline {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-icon {
  font-size: 120px;
  color: #ddd;
  margin-bottom: 24px;
  opacity: 0.5;
}

.empty-state h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 12px;
}

.empty-state p {
  font-size: 16px;
  color: #666;
  margin-bottom: 32px;
}

.order-now-btn {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  color: white;
  padding: 16px 32px;
  border-radius: 12px;
  text-decoration: none;
  font-weight: 700;
  font-size: 16px;
  box-shadow: 0 4px 16px rgba(0, 176, 103, 0.3);
  transition: all 0.3s ease;
}

.order-now-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 24px rgba(0, 176, 103, 0.4);
}

/* Responsive */
@media (max-width: 1200px) {
  .orders-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .page-header {
    padding: 30px 5%;
  }

  .header-content {
    flex-direction: column;
    align-items: flex-start;
  }

  .header-title i {
    font-size: 36px;
  }

  .header-title h1 {
    font-size: 24px;
  }

  .order-info-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .progress-steps {
    flex-wrap: wrap;
  }

  .step-dot {
    width: 32px;
    height: 32px;
  }

  .step-label {
    font-size: 10px;
  }

  .order-header {
    flex-direction: column;
    align-items: flex-start;
  }
}

@media (max-width: 480px) {
  .orders-container {
    padding: 20px 3%;
  }

  .order-card {
    border-radius: 16px;
  }

  .total-amount {
    font-size: 18px;
  }

  .empty-icon {
    font-size: 80px;
  }

  .empty-state h2 {
    font-size: 22px;
  }
}
</style>
