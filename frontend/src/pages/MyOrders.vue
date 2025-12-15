<template>
  <div class="my-orders-page">
    <!-- HEADER -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <span class="eyebrow">Quản lý đơn hàng</span>
          <h1>Đơn hàng của tôi</h1>
          <p class="subtitle">Theo dõi và quản lý tất cả đơn hàng của bạn</p>
        </div>
        <div class="header-stats">
          <div class="stat-card">
            <div class="stat-value">{{ orders.length }}</div>
            <div class="stat-label">Tổng đơn hàng</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">{{ getPendingCount() }}</div>
            <div class="stat-label">Đang xử lý</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">{{ getDeliveredCount() }}</div>
            <div class="stat-label">Đã giao</div>
          </div>
        </div>
      </div>
    </div>

    <!-- FILTERS -->
    <div class="filters-section">
      <div class="filters-tabs">
        <button
          v-for="tab in filterTabs"
          :key="tab.value"
          :class="['filter-tab', { active: activeFilter === tab.value }]"
          @click="activeFilter = tab.value"
        >
          <i :class="tab.icon"></i>
          <span>{{ tab.label }}</span>
          <span class="tab-count">{{ getCountByFilter(tab.value) }}</span>
        </button>
      </div>
    </div>

    <!-- LOADING STATE -->
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Đang tải đơn hàng...</p>
    </div>

    <!-- ERROR STATE -->
    <div v-else-if="error" class="error-state">
      <i class="fa fa-exclamation-circle"></i>
      <h3>Đã có lỗi xảy ra</h3>
      <p>{{ error }}</p>
      <button class="btn btn-primary" @click="fetchOrders()">
        <i class="fa fa-redo"></i>
        Thử lại
      </button>
    </div>

    <!-- EMPTY STATE -->
    <div v-else-if="!filteredOrders.length" class="empty-state">
      <div class="empty-icon">
        <i class="fa fa-box-open"></i>
      </div>
      <h3>Chưa có đơn hàng nào</h3>
      <p>Bạn chưa có đơn hàng nào trong danh mục này</p>
      <router-link to="/menu" class="btn btn-primary">
        <i class="fa fa-shopping-bag"></i>
        Đặt hàng ngay
      </router-link>
    </div>

    <!-- ORDERS LIST -->
    <div v-else class="orders-container">
      <div class="orders-grid">
        <div
          v-for="order in filteredOrders"
          :key="order.orderId"
          class="order-card"
          @click="selectOrder(order)"
        >
          <!-- CARD HEADER -->
          <div class="card-header">
            <div class="order-number">
              <i class="fa fa-hashtag"></i>
              <span>{{ order.orderNumber || order.orderId }}</span>
            </div>
            <div class="order-badges">
              <span
                :class="[
                  'badge',
                  'badge-' + getOrderStatusClass(order.orderStatus),
                ]"
              >
                {{ getOrderStatusText(order.orderStatus) }}
              </span>
            </div>
          </div>

          <!-- ORDER INFO -->
          <div class="order-info">
            <div class="info-row">
              <span class="label">
                <i class="fa fa-calendar"></i>
                Ngày đặt
              </span>
              <span class="value">{{ formatDate(order.createdAt) }}</span>
            </div>

            <div class="info-row">
              <span class="label">
                <i class="fa fa-credit-card"></i>
                Thanh toán
              </span>
              <span
                :class="[
                  'badge',
                  'badge-payment',
                  badgePaymentMethod(order.paymentMethod),
                ]"
              >
                {{ formatPaymentMethod(order.paymentMethod) }}
              </span>
            </div>

            <div class="info-row">
              <span class="label">
                <i class="fa fa-check-circle"></i>
                Trạng thái TT
              </span>
              <span :class="['badge', badgePaymentStatus(order.paymentStatus)]">
                {{ formatPaymentStatus(order.paymentStatus) }}
              </span>
            </div>
          </div>

          <!-- ORDER ITEMS PREVIEW -->
          <div class="items-preview">
            <div class="items-label">
              <i class="fa fa-box"></i>
              <span>{{ order.items?.length || 0 }} sản phẩm</span>
            </div>
            <div class="items-list">
              <div
                v-for="(item, idx) in (order.items || []).slice(0, 3)"
                :key="idx"
                class="item-chip"
              >
                {{ item.productName }}
              </div>
              <div v-if="(order.items || []).length > 3" class="item-chip more">
                +{{ order.items.length - 3 }}
              </div>
            </div>
          </div>

          <!-- ORDER TOTAL -->
          <div class="order-total">
            <span class="total-label">Tổng cộng:</span>
            <span class="total-amount">{{
              formatPrice(order.totalAmount)
            }}</span>
          </div>

          <!-- ACTIONS -->
          <div class="card-actions" @click.stop>
            <!-- CASH -->
            <template v-if="isCash(order)">
              <div class="payment-info">
                <i class="fa fa-money-bill-wave"></i>
                <span>{{
                  order.paymentStatus === "PAID"
                    ? "Đã thanh toán tiền mặt"
                    : "Thanh toán khi nhận hàng"
                }}</span>
              </div>
            </template>

            <!-- VNPAY/ZALOPAY - PENDING -->
            <template v-else-if="order.paymentStatus === 'PENDING'">
              <button
                class="btn btn-payment"
                :disabled="payingId === order.orderId"
                @click="payOnline(order)"
              >
                <i class="fa fa-credit-card"></i>
                <span>{{
                  payingId === order.orderId
                    ? "Đang xử lý..."
                    : "Thanh toán ngay"
                }}</span>
              </button>
            </template>

            <!-- PAID -->
            <template v-else-if="order.paymentStatus === 'PAID'">
              <div class="payment-success">
                <i class="fa fa-check-circle"></i>
                <span>Đã thanh toán</span>
              </div>
            </template>

            <!-- FAILED -->
            <template v-else-if="order.paymentStatus === 'FAILED'">
              <div class="payment-failed">
                <i class="fa fa-exclamation-triangle"></i>
                <span>Thanh toán thất bại</span>
              </div>
              <button
                class="btn btn-retry"
                :disabled="payingId === order.orderId"
                @click="payOnline(order)"
              >
                <i class="fa fa-redo"></i>
                <span>Thử lại</span>
              </button>
            </template>

            <!-- VIEW DETAIL -->
            <button class="btn btn-outline" @click.stop="selectOrder(order)">
              <i class="fa fa-eye"></i>
              <span>Chi tiết</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- ORDER DETAIL MODAL -->
    <transition name="modal">
      <div v-if="selectedOrder" class="modal-overlay" @click="closeModal">
        <div class="modal-content" @click.stop>
          <!-- MODAL HEADER -->
          <div class="modal-header">
            <div class="modal-title">
              <h2>Chi tiết đơn hàng</h2>
              <p>
                Mã đơn: {{ selectedOrder.orderNumber || selectedOrder.orderId }}
              </p>
            </div>
            <button class="modal-close" @click="closeModal">
              <i class="fa fa-times"></i>
            </button>
          </div>

          <!-- MODAL BODY -->
          <div class="modal-body">
            <!-- ORDER STATUS TIMELINE -->
            <div class="timeline-section">
              <h3>
                <i class="fa fa-history"></i>
                Lịch sử giao hàng
              </h3>
              <div class="timeline">
                <div
                  v-for="(step, idx) in getOrderTimeline(selectedOrder)"
                  :key="idx"
                  :class="[
                    'timeline-item',
                    { active: step.active, completed: step.completed },
                  ]"
                >
                  <div class="timeline-marker">
                    <i :class="step.icon"></i>
                  </div>
                  <div class="timeline-content">
                    <div class="timeline-title">{{ step.title }}</div>
                    <div class="timeline-time" v-if="step.time">
                      {{ step.time }}
                    </div>
                    <div class="timeline-description" v-if="step.description">
                      {{ step.description }}
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- CUSTOMER INFO -->
            <div class="detail-section">
              <h3>
                <i class="fa fa-user"></i>
                Thông tin nhận hàng
              </h3>
              <div class="detail-grid">
                <div class="detail-item">
                  <span class="detail-label">Người nhận:</span>
                  <span class="detail-value">{{
                    selectedOrder.recipientName || "N/A"
                  }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Số điện thoại:</span>
                  <span class="detail-value">{{
                    selectedOrder.phone || "N/A"
                  }}</span>
                </div>
                <div class="detail-item full-width">
                  <span class="detail-label">Địa chỉ:</span>
                  <span class="detail-value">{{
                    selectedOrder.addressLine ||
                    selectedOrder.deliveryAddress ||
                    "N/A"
                  }}</span>
                </div>
              </div>
            </div>

            <!-- ORDER ITEMS -->
            <div class="detail-section">
              <h3>
                <i class="fa fa-shopping-bag"></i>
                Sản phẩm đã đặt
              </h3>
              <div class="items-table">
                <div
                  v-for="item in selectedOrder.items"
                  :key="item.orderItemId"
                  class="item-row"
                >
                  <div class="item-image">
                    <img
                      :src="getProductImage(item.productImage)"
                      :alt="item.productName"
                    />
                  </div>
                  <div class="item-info">
                    <div class="item-name">{{ item.productName }}</div>
                    <div class="item-price">
                      {{ formatPrice(item.price) }} × {{ item.quantity }}
                    </div>
                  </div>
                  <div class="item-subtotal">
                    {{ formatPrice(item.subtotal) }}
                  </div>
                </div>
              </div>
            </div>

            <!-- PAYMENT SUMMARY -->
            <div class="detail-section">
              <h3>
                <i class="fa fa-file-invoice-dollar"></i>
                Thanh toán
              </h3>
              <div class="summary-table">
                <div class="summary-row">
                  <span>Tạm tính:</span>
                  <span>{{ formatPrice(selectedOrder.subtotal) }}</span>
                </div>
                <div class="summary-row">
                  <span>Phí vận chuyển:</span>
                  <span>{{ formatPrice(selectedOrder.shippingFee) }}</span>
                </div>
                <div class="summary-row" v-if="selectedOrder.discount">
                  <span>Giảm giá:</span>
                  <span class="discount"
                    >-{{ formatPrice(selectedOrder.discount) }}</span
                  >
                </div>
                <div class="summary-divider"></div>
                <div class="summary-row total">
                  <span>Tổng cộng:</span>
                  <span>{{ formatPrice(selectedOrder.totalAmount) }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- MODAL FOOTER -->
          <div class="modal-footer">
            <button class="btn btn-outline" @click="closeModal">Đóng</button>
            <button
              v-if="canPayOnline(selectedOrder)"
              class="btn btn-primary"
              @click="payOnline(selectedOrder)"
            >
              <i class="fa fa-credit-card"></i>
              Thanh toán
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import api from "@/axios";

export default {
  name: "MyOrders",

  data() {
    return {
      orders: [],
      loading: false,
      error: "",
      payingId: null,
      selectedOrder: null,
      activeFilter: "ALL",
      filterTabs: [
        { value: "ALL", label: "Tất cả", icon: "fa fa-list" },
        { value: "PENDING", label: "Chờ xác nhận", icon: "fa fa-clock" },
        { value: "CONFIRMED", label: "Đã xác nhận", icon: "fa fa-check" },
        { value: "PREPARING", label: "Đang chuẩn bị", icon: "fa fa-utensils" },
        { value: "SHIPPING", label: "Đang giao", icon: "fa fa-truck" },
        { value: "DELIVERED", label: "Đã giao", icon: "fa fa-box-check" },
        { value: "CANCELLED", label: "Đã hủy", icon: "fa fa-times-circle" },
      ],
    };
  },

  computed: {
    filteredOrders() {
      if (this.activeFilter === "ALL") return this.orders;
      return this.orders.filter((o) => o.orderStatus === this.activeFilter);
    },
  },

  mounted() {
    this.fetchOrders();
  },

  methods: {
    async fetchOrders() {
      this.loading = true;
      this.error = "";
      try {
        const res = await api.get("/orders/my");
        this.orders = res.data?.data || [];
      } catch (err) {
        this.error = err.response?.data?.message || "Không thể tải đơn hàng";
      } finally {
        this.loading = false;
      }
    },

    getCountByFilter(filter) {
      if (filter === "ALL") return this.orders.length;
      return this.orders.filter((o) => o.orderStatus === filter).length;
    },

    getPendingCount() {
      return this.orders.filter((o) =>
        ["PENDING", "CONFIRMED", "PREPARING", "SHIPPING"].includes(
          o.orderStatus
        )
      ).length;
    },

    getDeliveredCount() {
      return this.orders.filter((o) => o.orderStatus === "DELIVERED").length;
    },

    isCash(order) {
      return (order.paymentMethod || "").toUpperCase() === "CASH";
    },

    canPayOnline(order) {
      return !this.isCash(order) && order.paymentStatus === "PENDING";
    },

    formatPrice(v) {
      return Number(v || 0).toLocaleString("vi-VN") + "đ";
    },

    formatDate(date) {
      if (!date) return "N/A";
      return new Date(date).toLocaleString("vi-VN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
      });
    },

    formatPaymentMethod(method) {
      const map = {
        CASH: "Tiền mặt",
        VNPAY: "VNPay",
        ZALOPAY: "ZaloPay",
      };
      return map[(method || "").toUpperCase()] || method;
    },

    formatPaymentStatus(status) {
      const map = {
        PENDING: "Chờ thanh toán",
        PAID: "Đã thanh toán",
        FAILED: "Thất bại",
      };
      return map[(status || "").toUpperCase()] || status;
    },

    getOrderStatusText(status) {
      const map = {
        PENDING: "Chờ xác nhận",
        CONFIRMED: "Đã xác nhận",
        PREPARING: "Đang chuẩn bị",
        SHIPPING: "Đang giao hàng",
        DELIVERED: "Đã giao hàng",
        CANCELLED: "Đã hủy",
      };
      return map[(status || "").toUpperCase()] || status;
    },

    getOrderStatusClass(status) {
      const map = {
        PENDING: "warning",
        CONFIRMED: "info",
        PREPARING: "primary",
        SHIPPING: "purple",
        DELIVERED: "success",
        CANCELLED: "danger",
      };
      return map[(status || "").toUpperCase()] || "secondary";
    },

    badgePaymentMethod(method) {
      const m = (method || "").toUpperCase();
      return m === "CASH" ? "badge-cash" : "badge-online";
    },

    badgePaymentStatus(status) {
      const s = (status || "").toUpperCase();
      if (s === "PAID") return "badge-success";
      if (s === "PENDING") return "badge-warning";
      if (s === "FAILED") return "badge-danger";
      return "badge-secondary";
    },

    getProductImage(url) {
      if (!url) return "/images/notfound.png";
      if (url.startsWith("http")) return url;
      return `http://localhost:3000${url.startsWith("/") ? "" : "/"}${url}`;
    },

    getOrderTimeline(order) {
      const timeline = [
        {
          title: "Đơn hàng đã đặt",
          icon: "fa fa-shopping-cart",
          time: this.formatDate(order.createdAt),
          description: "Đơn hàng của bạn đã được tiếp nhận",
          completed: true,
          active: order.orderStatus === "PENDING",
        },
        {
          title: "Xác nhận đơn hàng",
          icon: "fa fa-check-circle",
          time: order.confirmedAt ? this.formatDate(order.confirmedAt) : null,
          description:
            order.orderStatus === "CONFIRMED"
              ? "Đơn hàng đã được xác nhận"
              : "Đang chờ xác nhận",
          completed: [
            "CONFIRMED",
            "PREPARING",
            "SHIPPING",
            "DELIVERED",
          ].includes(order.orderStatus),
          active: order.orderStatus === "CONFIRMED",
        },
        {
          title: "Đang chuẩn bị",
          icon: "fa fa-utensils",
          time: null,
          description:
            order.orderStatus === "PREPARING" ? "Đang chuẩn bị món ăn" : null,
          completed: ["PREPARING", "SHIPPING", "DELIVERED"].includes(
            order.orderStatus
          ),
          active: order.orderStatus === "PREPARING",
        },
        {
          title: "Đang giao hàng",
          icon: "fa fa-truck",
          time: null,
          description:
            order.orderStatus === "SHIPPING"
              ? "Đơn hàng đang được giao đến bạn"
              : null,
          completed: ["SHIPPING", "DELIVERED"].includes(order.orderStatus),
          active: order.orderStatus === "SHIPPING",
        },
        {
          title: "Giao hàng thành công",
          icon: "fa fa-box-check",
          time: order.deliveredAt ? this.formatDate(order.deliveredAt) : null,
          description:
            order.orderStatus === "DELIVERED"
              ? "Đơn hàng đã được giao thành công"
              : null,
          completed: order.orderStatus === "DELIVERED",
          active: order.orderStatus === "DELIVERED",
        },
      ];

      // Nếu đơn bị hủy
      if (order.orderStatus === "CANCELLED") {
        return [
          timeline[0],
          {
            title: "Đơn hàng đã hủy",
            icon: "fa fa-times-circle",
            time: order.cancelledAt ? this.formatDate(order.cancelledAt) : null,
            description: order.cancelledReason || "Đơn hàng đã bị hủy",
            completed: true,
            active: true,
          },
        ];
      }

      return timeline;
    },

    selectOrder(order) {
      this.selectedOrder = order;
    },

    closeModal() {
      this.selectedOrder = null;
    },

    async payOnline(order) {
      try {
        this.payingId = order.orderId;

        const paymentMethod = (order.paymentMethod || "VNPAY").toUpperCase();
        const endpoint =
          paymentMethod === "ZALOPAY"
            ? "/payment/zalopay-create"
            : "/payment/vnpay-create";

        const res = await api.post(endpoint, {
          orderId: order.orderId,
        });

        const url = res.data?.data?.paymentUrl || res.data?.paymentUrl;

        if (!url) throw new Error("Không nhận được URL thanh toán");

        window.location.href = url;
      } catch (err) {
        alert(
          err.response?.data?.message ||
            err.message ||
            "Không thể tạo thanh toán"
        );
      } finally {
        this.payingId = null;
      }
    },
  },
};
</script>

<style scoped>
.my-orders-page {
  min-height: 100vh;
  background: #f8f9fa;
  padding-bottom: 40px;
}

/* HEADER */
.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px 24px;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 32px;
  align-items: center;
}

.eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  opacity: 0.9;
  margin-bottom: 8px;
}

.page-header h1 {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 8px 0;
}

.subtitle {
  opacity: 0.9;
  margin: 0;
}

.header-stats {
  display: flex;
  gap: 16px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 16px 20px;
  text-align: center;
  min-width: 100px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  opacity: 0.9;
}

/* FILTERS */
.filters-section {
  background: white;
  border-bottom: 1px solid #e5e7eb;
  position: sticky;
  top: 0;
  z-index: 100;
}

.filters-tabs {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  gap: 8px;
  overflow-x: auto;
}

.filter-tab {
  flex-shrink: 0;
  padding: 14px 20px;
  border: none;
  background: transparent;
  color: #6b7280;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  border-bottom: 3px solid transparent;
  transition: all 0.2s;
}

.filter-tab i {
  font-size: 16px;
}

.filter-tab:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.05);
}

.filter-tab.active {
  color: #667eea;
  border-bottom-color: #667eea;
}

.tab-count {
  background: rgba(0, 0, 0, 0.05);
  padding: 2px 8px;
  border-radius: 999px;
  font-size: 12px;
  min-width: 24px;
  text-align: center;
}

.filter-tab.active .tab-count {
  background: rgba(102, 126, 234, 0.15);
  color: #667eea;
}

/* STATES */
.loading-state,
.error-state,
.empty-state {
  max-width: 600px;
  margin: 80px auto;
  text-align: center;
  padding: 40px 24px;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #e5e7eb;
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.error-state i {
  font-size: 48px;
  color: #ef4444;
  margin-bottom: 16px;
}

.empty-icon {
  font-size: 64px;
  color: #d1d5db;
  margin-bottom: 24px;
}

.empty-icon i {
  font-size: 64px;
}

/* ORDERS GRID */
.orders-container {
  max-width: 1400px;
  margin: 32px auto;
  padding: 0 24px;
}

.orders-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 24px;
}

.order-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  border: 1px solid #e5e7eb;
  transition: all 0.3s;
  cursor: pointer;
}

.order-card:hover {
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
  border-color: #667eea;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f3f4f6;
}

.order-number {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 700;
  font-size: 16px;
  color: #111827;
}

.order-badges {
  display: flex;
  gap: 8px;
}

.badge {
  padding: 4px 12px;
  border-radius: 999px;
  font-weight: 600;
  font-size: 12px;
  display: inline-block;
}

.badge-warning {
  background: #fef3c7;
  color: #92400e;
}
.badge-info {
  background: #dbeafe;
  color: #1e40af;
}
.badge-primary {
  background: #e0e7ff;
  color: #4338ca;
}
.badge-purple {
  background: #f3e8ff;
  color: #6b21a8;
}
.badge-success {
  background: #d1fae5;
  color: #065f46;
}
.badge-danger {
  background: #fee2e2;
  color: #991b1b;
}
.badge-secondary {
  background: #f3f4f6;
  color: #6b7280;
}

.badge-cash {
  background: #d1fae5;
  color: #065f46;
}
.badge-online {
  background: #dbeafe;
  color: #1e40af;
}
.badge-payment {
  font-size: 11px;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.label {
  color: #6b7280;
  display: flex;
  align-items: center;
  gap: 6px;
}

.label i {
  font-size: 12px;
  width: 16px;
}

.value {
  font-weight: 600;
  color: #111827;
}

.items-preview {
  background: #f9fafb;
  border-radius: 12px;
  padding: 12px;
  margin-bottom: 16px;
}

.items-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 600;
  color: #6b7280;
  margin-bottom: 8px;
}

.items-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.item-chip {
  background: white;
  border: 1px solid #e5e7eb;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 12px;
  color: #374151;
}

.item-chip.more {
  background: #667eea;
  color: white;
  border-color: #667eea;
  font-weight: 600;
}

.order-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-top: 2px dashed #e5e7eb;
  margin-bottom: 16px;
}

.total-label {
  font-weight: 600;
  color: #6b7280;
}

.total-amount {
  font-size: 20px;
  font-weight: 700;
  color: #667eea;
}

.card-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.btn {
  padding: 10px 16px;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s;
  flex: 1;
  justify-content: center;
}

.btn i {
  font-size: 14px;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.btn-payment {
  background: #10b981;
  color: white;
}

.btn-payment:hover:not(:disabled) {
  background: #059669;
}

.btn-retry {
  background: #f59e0b;
  color: white;
}

.btn-retry:hover:not(:disabled) {
  background: #d97706;
}

.btn-outline {
  background: white;
  border: 2px solid #e5e7eb;
  color: #6b7280;
}

.btn-outline:hover {
  border-color: #667eea;
  color: #667eea;
}

.payment-info,
.payment-success,
.payment-failed {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  flex: 1;
}

.payment-info {
  background: #d1fae5;
  color: #065f46;
}

.payment-success {
  background: #d1fae5;
  color: #065f46;
}

.payment-failed {
  background: #fee2e2;
  color: #991b1b;
}

/* MODAL */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  padding: 20px;
  overflow-y: auto;
}

.modal-content {
  background: white;
  border-radius: 20px;
  width: 100%;
  max-width: 900px;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  padding: 24px 28px;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
}

.modal-title h2 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 700;
}

.modal-title p {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
}

.modal-close {
  width: 40px;
  height: 40px;
  border: none;
  background: #f3f4f6;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.modal-close:hover {
  background: #e5e7eb;
}

.modal-body {
  padding: 28px;
  overflow-y: auto;
  flex: 1;
}

.detail-section {
  margin-bottom: 32px;
}

.detail-section h3 {
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
  gap: 10px;
  color: #111827;
}

.detail-section h3 i {
  color: #667eea;
}

/* TIMELINE */
.timeline {
  position: relative;
  padding-left: 40px;
}

.timeline::before {
  content: "";
  position: absolute;
  left: 15px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: #e5e7eb;
}

.timeline-item {
  position: relative;
  padding-bottom: 32px;
}

.timeline-item:last-child {
  padding-bottom: 0;
}

.timeline-marker {
  position: absolute;
  left: -40px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #f3f4f6;
  border: 3px solid white;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
  font-size: 14px;
  z-index: 1;
}

.timeline-item.completed .timeline-marker {
  background: #d1fae5;
  color: #065f46;
}

.timeline-item.active .timeline-marker {
  background: #667eea;
  color: white;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.2);
}

.timeline-content {
  padding-left: 8px;
}

.timeline-title {
  font-weight: 700;
  color: #111827;
  margin-bottom: 4px;
}

.timeline-time {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 4px;
}

.timeline-description {
  font-size: 13px;
  color: #9ca3af;
}

.timeline-item.active .timeline-title {
  color: #667eea;
}

/* DETAIL GRID */
.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-item.full-width {
  grid-column: 1 / -1;
}

.detail-label {
  font-size: 13px;
  color: #6b7280;
  font-weight: 600;
}

.detail-value {
  font-size: 14px;
  color: #111827;
}

/* ITEMS TABLE */
.items-table {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.item-row {
  display: grid;
  grid-template-columns: 60px 1fr auto;
  gap: 12px;
  align-items: center;
  padding: 12px;
  background: #f9fafb;
  border-radius: 12px;
}

.item-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  background: white;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-name {
  font-weight: 600;
  color: #111827;
  margin-bottom: 4px;
}

.item-price {
  font-size: 13px;
  color: #6b7280;
}

.item-subtotal {
  font-weight: 700;
  color: #667eea;
  font-size: 16px;
}

/* SUMMARY TABLE */
.summary-table {
  background: #f9fafb;
  border-radius: 12px;
  padding: 16px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 14px;
}

.summary-row.total {
  font-size: 18px;
  font-weight: 700;
  color: #667eea;
  padding-top: 12px;
}

.discount {
  color: #10b981;
}

.summary-divider {
  height: 1px;
  background: #e5e7eb;
  margin: 12px 0;
}

.modal-footer {
  padding: 20px 28px;
  border-top: 1px solid #e5e7eb;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  flex-shrink: 0;
}

/* ANIMATIONS */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-active .modal-content,
.modal-leave-active .modal-content {
  transition: transform 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-content,
.modal-leave-to .modal-content {
  transform: scale(0.9);
}

/* RESPONSIVE */
@media (max-width: 768px) {
  .header-content {
    grid-template-columns: 1fr;
  }

  .header-stats {
    justify-content: space-between;
  }

  .stat-card {
    flex: 1;
  }

  .orders-grid {
    grid-template-columns: 1fr;
  }

  .detail-grid {
    grid-template-columns: 1fr;
  }

  .modal-body {
    padding: 20px;
  }
}
</style>
