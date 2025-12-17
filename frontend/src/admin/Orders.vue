<template>
  <div class="orders-page">
    <!-- ⚠️ ERROR BANNER -->
    <div v-if="errorMsg" class="error-banner">
      <strong>⚠️ Lỗi:</strong> {{ errorMsg }}
      <button @click="errorMsg = ''" class="close-error">✕</button>
    </div>

    <!-- Header Section -->
    <div class="header-section">
      <div class="header-content">
        <h2 class="page-title">📦 Quản Lý Đơn Hàng</h2>
        <p class="page-subtitle">
          Quản lý và theo dõi tất cả đơn hàng khách hàng
        </p>
      </div>
      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-icon">📊</div>
          <div class="stat-info">
            <span class="stat-number">{{ orders.length }}</span>
            <span class="stat-label">Tổng đơn (trang hiện tại)</span>
          </div>
        </div>
        <div class="stat-card paid">
          <div class="stat-icon">✅</div>
          <div class="stat-info">
            <span class="stat-number">{{ paidCount }}</span>
            <span class="stat-label">Đã thanh toán</span>
          </div>
        </div>
        <div class="stat-card unpaid">
          <div class="stat-icon">⏳</div>
          <div class="stat-info">
            <span class="stat-number">{{ unpaidCount }}</span>
            <span class="stat-label">Chờ thanh toán</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Table Section -->
    <div class="table-section">
      <div class="table-header">
        <div class="table-title-block">
          <h3>📋 Danh Sách Đơn Hàng</h3>
          <p class="table-subtitle">
            Theo dõi nhanh trạng thái & thanh toán từng đơn
          </p>
        </div>

        <div class="header-actions">
          <div class="date-range">
            <div class="date-field">
              <label for="startDate">📅 From:</label>
              <input
                id="startDate"
                type="date"
                v-model="startDate"
                @change="onFilterChange"
              />
            </div>
            <div class="date-field">
              <label for="endDate">📅 To:</label>
              <input
                id="endDate"
                type="date"
                v-model="endDate"
                @change="onFilterChange"
              />
            </div>
            <div class="quick-range">
              <button class="btn-quick" @click="setRange('today')">
                Today
              </button>
              <button class="btn-quick" @click="setRange('week')">
                This Week
              </button>
              <button class="btn-quick" @click="setRange('month')">
                This Month
              </button>
              <button class="btn-quick" @click="setRange('all')">
                All Time
              </button>
            </div>
          </div>

          <!-- Filter theo status -->
          <div class="filter-group">
            <label for="statusFilter">Trạng thái:</label>
            <select
              id="statusFilter"
              class="filter-select"
              v-model="statusFilter"
              @change="onFilterChange"
            >
              <option value="">Tất cả</option>
              <option
                v-for="opt in statusOptions"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
          </div>

          <button class="btn-refresh" @click="fetchOrders" :disabled="loading">
            {{ loading ? "⏳" : "🔄" }} Làm mới
          </button>
        </div>
      </div>

      <div class="table-container cards-container">
        <!-- Loading / Empty -->
        <div v-if="loading" class="loading-cell">
          <div class="loading-spinner"></div>
          <span>Đang tải đơn hàng...</span>
        </div>
        <div v-else-if="!orders.length" class="empty-cell card">
          <div class="empty-icon">📭</div>
          <span>Không có đơn hàng nào.</span>
        </div>

        <!-- Cards -->
        <div v-else class="orders-grid">
          <div v-for="order in orders" :key="order.orderId" class="order-card">
            <div class="order-card__top">
              <div class="order-code">
                <span class="order-short">
                  {{
                    order.shortCode ||
                    (order.orderNumber || "ORD").slice(-6).toUpperCase()
                  }}
                </span>
                <span class="order-id">Mã dài: {{ order.orderNumber }}</span>
              </div>
              <div class="order-total">{{ formatPrice(getTotal(order)) }}</div>
            </div>

            <div class="order-meta">
              <div class="meta-item">
                <span class="meta-label">Người nhận</span>
                <span class="meta-value">{{ order.recipientName }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">SĐT</span>
                <span class="meta-value">{{ order.phone }}</span>
              </div>
              <div class="meta-item full">
                <span class="meta-label">Địa chỉ</span>
                <span class="meta-value" :title="getAddress(order)">
                  {{ getAddress(order) }}
                </span>
              </div>
              <div class="meta-item">
                <span class="meta-label">Ngày đặt</span>
                <span class="meta-value">{{
                  formatDate(order.createdAt)
                }}</span>
              </div>
            </div>

            <div class="order-status-row">
              <div class="pill">
                <span class="pill-label">Thanh toán</span>
                <span
                  class="pill-badge"
                  :class="{
                    paid: getPaymentStatus(order) === 'paid',
                    pending: getPaymentStatus(order) === 'pending',
                    failed: getPaymentStatus(order) === 'failed',
                  }"
                >
                  {{ getPaymentStatus(order).toUpperCase() || "N/A" }}
                </span>
                <span class="pill-method">
                  Phương thức: {{ getPaymentMethod(order)?.toUpperCase() }}
                </span>
              </div>

              <div class="status-select-wrapper">
                <label>Trạng thái đơn</label>
                <select
                  v-model="order.orderStatus"
                  class="status-select"
                  :class="'status-' + normalizeStatus(order.orderStatus)"
                  @change="markAsChanged(order)"
                >
                  <option
                    v-for="opt in statusOptions"
                    :key="opt.value"
                    :value="opt.value"
                  >
                    {{ opt.label }}
                  </option>
                </select>
              </div>
            </div>

            <div class="action-buttons card-actions">
              <button
                class="btn-action btn-confirm"
                v-if="
                  getPaymentMethod(order) === 'cash' &&
                  getPaymentStatus(order) === 'pending'
                "
                @click="confirmCash(order)"
                :disabled="confirmingId === order.orderId"
                title="Xác nhận thanh toán tiền mặt"
              >
                💵
              </button>

              <button
                class="btn-action btn-update"
                @click="updateStatus(order)"
                title="Lưu thay đổi"
                :disabled="!order._changed"
              >
                💾
              </button>

              <button
                class="btn-action btn-details"
                @click="showDetails(order)"
                title="Xem chi tiết"
              >
                👁️
              </button>
              <button
                class="btn-action btn-next"
                @click="advanceStatus(order)"
                :disabled="isTerminal(order.orderStatus)"
                title="Chuyển trạng thái tiếp theo"
              >
                ⏭️
              </button>

              <button
                class="btn-action btn-delete"
                @click="deleteOrder(order)"
                title="Xóa đơn hàng"
              >
                🗑️
              </button>
            </div>

            <div v-if="getNote(order)" class="order-note">
              <span class="note-label">Ghi chú khách:</span>
              <span class="note-text">{{ getNote(order) }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Pagination -->
      <div v-if="totalPages > 1" class="pagination">
        <button :disabled="page === 0" @click="changePage(page - 1)">
          « Trước
        </button>
        <span class="page-info">Trang {{ page + 1 }} / {{ totalPages }}</span>
        <button
          :disabled="page + 1 >= totalPages"
          @click="changePage(page + 1)"
        >
          Sau »
        </button>
      </div>
    </div>

    <!-- Modal chi tiết đơn hàng -->
    <div
      v-if="showModal && selectedOrder"
      class="modal-overlay"
      @click.self="closeDetails"
    >
      <div class="modal-content">
        <div class="modal-header">
          <h3>📋 Chi Tiết Đơn Hàng</h3>
          <button class="modal-close" @click="closeDetails">✕</button>
        </div>

        <div class="modal-body">
          <div class="detail-row">
            <span class="detail-label">Mã đơn:</span>
            <span class="detail-value">#{{ selectedOrder.orderId }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Số đơn hàng:</span>
            <span class="detail-value">{{ selectedOrder.orderNumber }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Người nhận:</span>
            <span class="detail-value">{{ selectedOrder.recipientName }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Điện thoại:</span>
            <span class="detail-value">{{ selectedOrder.phone }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Địa chỉ:</span>
            <span class="detail-value">{{ getAddress(selectedOrder) }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Ngày đặt:</span>
            <span class="detail-value">
              {{ formatDate(selectedOrder.createdAt) }}
            </span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Tạm tính:</span>
            <span class="detail-value">
              {{ formatPrice(getSubtotal(selectedOrder)) }}
            </span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Phí vận chuyển:</span>
            <span class="detail-value">
              {{ formatPrice(getShipping(selectedOrder) || 0) }}
            </span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Tổng cộng:</span>
            <span class="detail-value total">
              {{ formatPrice(getTotal(selectedOrder)) }}
            </span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Thanh toán:</span>
            <span class="detail-value">
              {{ getPaymentStatus(selectedOrder)?.toUpperCase() || "N/A" }}
              ({{ getPaymentMethod(selectedOrder)?.toUpperCase() || "N/A" }})
            </span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Trạng thái:</span>
            <span class="status-badge">
              {{ getStatusLabel(selectedOrder.orderStatus) }}
            </span>
          </div>
          <div
            v-if="getNote(selectedOrder)"
            class="detail-row detail-note-row"
          >
            <span class="detail-label">Ghi chú khách:</span>
            <span class="detail-value note-value">
              {{ getNote(selectedOrder) }}
            </span>
          </div>

          <!-- Items -->
          <div
            v-if="selectedOrder.items && selectedOrder.items.length"
            class="items-section"
          >
            <h4>Sản phẩm:</h4>
            <ul class="items-list">
              <li
                v-for="it in selectedOrder.items"
                :key="it.orderItemId"
                class="item-row"
              >
                <span class="item-name">{{ it.productName }}</span>
                <span class="item-qty">× {{ it.quantity }}</span>
                <span class="item-price">{{ formatPrice(it.subtotal) }}</span>
              </li>
            </ul>
          </div>
        </div>

        <div class="modal-footer">
          <button class="btn btn-close-modal" @click="closeDetails">
            Đóng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/axios";
import storage from "@/utils/storage";
import Swal from "sweetalert2";

export default {
  name: "Orders",

  data() {
    return {
      orders: [],
      loading: false,
      errorMsg: "",
      // phân trang
      page: 0,
      size: 10,
      totalPages: 0,
      totalElements: 0,
      // filter
      statusFilter: "",
      statusOptions: [
        { value: "pending", label: "Chờ xử lý" },
        { value: "confirmed", label: "Đã xác nhận" },
        { value: "preparing", label: "Đang chuẩn bị" },
        { value: "delivering", label: "Đang giao" },
        { value: "delivered", label: "Đã giao" },
        { value: "cancelled", label: "Đã hủy" },
      ],
      startDate: "",
      endDate: "",
      // modal
      showModal: false,
      selectedOrder: null,
      confirmingId: null,
    };
  },

  computed: {
    paidCount() {
      return this.orders.filter(
        (o) => this.normalizeStatus(o.paymentStatus) === "paid"
      ).length;
    },
    unpaidCount() {
      return this.orders.filter(
        (o) => this.normalizeStatus(o.paymentStatus) !== "paid"
      ).length;
    },
  },

  mounted() {
    // Mặc định lọc theo hôm nay để không hiển thị đơn của ngày khác
    this.setRange("today");
  },

  methods: {
    formatPrice(price) {
      const value = Number(price || 0);
      return value.toLocaleString("vi-VN") + "₫";
    },

    normalizeStatus(s) {
      return (s || "").toString().toLowerCase();
    },

    getPaymentStatus(order) {
      return this.normalizeStatus(order.paymentStatus);
    },

    getOrderStatus(order) {
      return this.normalizeStatus(order.orderStatus);
    },

    getPaymentMethod(order) {
      return this.normalizeStatus(order.paymentMethod);
    },

    getAddress(order) {
      return order.addressLine || order.deliveryAddress || "";
    },

    getNote(order) {
      const raw =
        order?.notes ||
        order?.note ||
        order?.customerNote ||
        order?.customerNotes;
      if (!raw) return "";
      const note = raw.toString().trim();
      return note;
    },

    async confirmCash(order) {
      const confirmResult = await Swal.fire({
        icon: "question",
        title: "Xác nhận đã nhận tiền mặt?",
        text: `Đơn #${order.orderId}`,
        showCancelButton: true,
        confirmButtonText: "Đã nhận",
        cancelButtonText: "Hủy",
        confirmButtonColor: "#22c55e",
        cancelButtonColor: "#9ca3af",
      });
      if (!confirmResult.isConfirmed) return;

      const token = storage.getToken();
      if (!token) {
        this.$router.push("/login");
        return;
      }

      try {
        this.confirmingId = order.orderId;
        try {
          await api.put(
            `/admin/orders/${order.orderId}/confirm-cash`,
            {},
            {
              headers: { Authorization: token ? `Bearer ${token}` : undefined },
            }
          );
        } catch (err) {
          // Fallback nếu BE không có endpoint admin
          await api.put(
            `/orders/${order.orderId}/confirm-cash`,
            {},
            {
              headers: { Authorization: token ? `Bearer ${token}` : undefined },
            }
          );
        }
        order.paymentStatus = "paid";
        order._changed = false;
        this.toastSuccess("Thành công!", "Đã xác nhận thanh toán tiền mặt");
      } catch (err) {
        console.error("❌ Lỗi confirm cash:", err);
        this.toastError(
          "Lỗi",
          err.response?.data?.message ||
            err.response?.data?.error ||
            "Không thể xác nhận thanh toán"
        );
      } finally {
        this.confirmingId = null;
      }
    },

    getShipping(order) {
      const candidates = [
        order.shippingFee,
        order.deliveryFee,
        order.shippingCost,
      ].map((v) => Number(v));
      const found = candidates.find((v) => !Number.isNaN(v));
      return found || 0;
    },

    getSubtotal(order) {
      const candidates = [
        order.subtotal,
        order.totalPrice,
        order.total,
        order.amount,
        order.itemsTotal,
        order.itemsSubtotal,
        order.totalAmount && order.shippingFee != null
          ? order.totalAmount - (order.shippingFee || 0)
          : null,
      ].map((v) => Number(v));
      const found = candidates.find((v) => !Number.isNaN(v) && v > 0);
      if (found) return found;

      if (Array.isArray(order.items)) {
        const sum = order.items.reduce((acc, it) => {
          const subtotal = Number(it?.subtotal);
          const priceQty = Number(it?.price) * Number(it?.quantity || 0);
          const val =
            !Number.isNaN(subtotal) && subtotal > 0
              ? subtotal
              : !Number.isNaN(priceQty) && priceQty > 0
              ? priceQty
              : 0;
          return acc + val;
        }, 0);
        if (sum > 0) return sum;
      }

      return 0;
    },

    getTotal(order) {
      const shipping = this.getShipping(order);
      const subtotal = this.getSubtotal(order);
      const primary = [
        order.totalAmount,
        order.finalAmount,
        order.grandTotal,
        order.totalCost,
        order.totalPrice,
        order.amount,
        order.total,
      ].map((v) => Number(v));
      const primaryFound = primary.find((v) => Number.isFinite(v));

      if (!Number.isNaN(subtotal) && subtotal > 0) {
        return subtotal + shipping;
      }

      if (primaryFound !== undefined) {
        return primaryFound + (primaryFound > 0 && shipping ? shipping : 0);
      }

      return subtotal + shipping;
    },

    formatDate(date) {
      if (!date) return "";
      return new Date(date).toLocaleString("vi-VN");
    },

    formatDateOnly(date) {
      if (!date) return "";
      const d = new Date(date);
      if (Number.isNaN(d.getTime())) return "";
      const y = d.getFullYear();
      const m = String(d.getMonth() + 1).padStart(2, "0");
      const day = String(d.getDate()).padStart(2, "0");
      return `${y}-${m}-${day}`;
    },

    getStatusLabel(status) {
      const found = this.statusOptions.find(
        (s) => s.value === this.normalizeStatus(status)
      );
      return found ? found.label : status;
    },

    markAsChanged(order) {
      order._changed = true;
    },

    onFilterChange() {
      this.page = 0;
      this.fetchOrders();
    },

    formatInputDate(dateObj) {
      return dateObj.toISOString().split("T")[0];
    },

    setRange(type) {
      const today = new Date();
      const todayStr = this.formatInputDate(today);
      let start = "";
      let end = todayStr;

      if (type === "today") {
        start = todayStr;
      } else if (type === "week") {
        const s = new Date();
        s.setDate(s.getDate() - 6);
        start = this.formatInputDate(s);
      } else if (type === "month") {
        const s = new Date(today.getFullYear(), today.getMonth(), 1);
        start = this.formatInputDate(s);
      } else if (type === "all") {
        start = "";
        end = "";
      }

      this.startDate = start;
      this.endDate = end;
      this.onFilterChange();
    },

    changePage(newPage) {
      if (newPage < 0 || newPage >= this.totalPages) return;
      this.page = newPage;
      this.fetchOrders();
    },

    // ==============================
    // 🔥 LẤY DANH SÁCH ĐƠN HÀNG (ADMIN)
    // ==============================
    async fetchOrders() {
      const token = storage.getToken();
      if (!token) {
        this.$router.push("/login");
        return;
      }

      const loadOrders = async (url, withParams = true) => {
        const opts = {
          headers: { Authorization: `Bearer ${token}` },
        };
        if (withParams) {
          opts.params = {
            page: this.page,
            size: this.size,
            status: this.statusFilter || undefined,
            startDate: this.startDate || undefined,
            endDate: this.endDate || undefined,
          };
        }
        return api.get(url, opts);
      };

      const normalizeResponse = (apiData) => {
        let ordersData = [];

        if (apiData?.success && apiData.data) {
          const pageData = apiData.data;
          ordersData = pageData.content || [];
        } else if (apiData?.content) {
          ordersData = apiData.content || [];
        } else if (Array.isArray(apiData)) {
          ordersData = apiData;
        } else if (apiData?.data && Array.isArray(apiData.data)) {
          ordersData = apiData.data;
        }

        ordersData.forEach((order) => {
          order._changed = false;
        });

        const inRange = (order) => {
          if (!this.startDate && !this.endDate) return true;
          const created = this.formatDateOnly(order.createdAt);
          if (!created) return false;
          const startOk = this.startDate ? created >= this.startDate : true;
          const endOk = this.endDate ? created <= this.endDate : true;
          return startOk && endOk;
        };

        ordersData = ordersData.filter(inRange);

        this.orders = ordersData;
        // Nếu lọc phía client, cập nhật lại tổng
        this.totalElements = ordersData.length;
        this.totalPages = Math.max(
          1,
          Math.ceil(this.totalElements / Math.max(this.size, 1))
        );
      };

      try {
        this.loading = true;
        this.errorMsg = "";

        // 1) Thử endpoint admin có phân trang
        let res;
        try {
          res = await loadOrders("/admin/orders", true);
        } catch (err) {
          // 2) Thử lại không truyền page/size (một số BE không hỗ trợ)
          try {
            res = await loadOrders("/admin/orders", false);
          } catch (err2) {
            // 3) Fallback sang endpoint /orders (trường hợp BE chưa mở admin)
            res = await loadOrders("/orders", true);
          }
        }

        normalizeResponse(res.data);
      } catch (error) {
        console.error("❌ LỖI KHI TẢI ORDERS:", error);
        this.errorMsg =
          error.response?.data?.message ||
          error.message ||
          "Không thể tải danh sách đơn hàng.";

        if (error.response?.status === 401 || error.response?.status === 403) {
          storage.clearAuth();
          this.$router.push("/login");
        }
      } finally {
        this.loading = false;
      }
    },

    // ==============================
    // 🔥 CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG
    // ==============================
    async updateStatus(order, skipConfirm = false) {
      if (!skipConfirm) {
        const result = await Swal.fire({
          icon: "question",
          title: "Cập nhật trạng thái?",
          html: `Đơn #<b>${order.orderId}</b><br/>Thành: <b>${this.getStatusLabel(
            order.orderStatus
          )}</b>`,
          showCancelButton: true,
          confirmButtonText: "Cập nhật",
          cancelButtonText: "Hủy",
          confirmButtonColor: "#2563eb",
          cancelButtonColor: "#9ca3af",
        });
        if (!result.isConfirmed) return;
      }

      const token = storage.getToken();
      if (!token) {
        this.$router.push("/login");
        return;
      }

      const payload = { status: order.orderStatus };
      const headers = { Authorization: `Bearer ${token}` };

      try {
        try {
          await api.patch(`/admin/orders/${order.orderId}/status`, payload, {
            headers,
          });
        } catch (err) {
          // Fallback BE không có endpoint admin
          await api.patch(`/orders/${order.orderId}/status`, payload, {
            headers,
          });
        }

        this.toastSuccess("Thành công!", "Đã cập nhật trạng thái đơn hàng.");
        order._changed = false;
        this.fetchOrders();
      } catch (error) {
        console.error("❌ Không thể cập nhật trạng thái:", error);
        this.toastError(
          "Lỗi",
          error.response?.data?.message ||
            "Không thể cập nhật trạng thái đơn hàng!"
        );
      }
    },

    showDetails(order) {
      this.selectedOrder = order;
      this.showModal = true;
    },

    async deleteOrder(order) {
      const result = await Swal.fire({
        title: "Xóa đơn hàng?",
        text: `Bạn có chắc muốn xóa đơn #${order.orderId}?`,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#ef4444",
        cancelButtonColor: "#6b7280",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
      });
      if (!result.isConfirmed) return;
      try {
        const id = order.orderId || order.id;
        if (!id) throw new Error("Không tìm thấy ID đơn hàng");

        // Admin có thể xóa bất kỳ đơn hàng nào (hard delete)
        await api.delete(`/admin/orders/${id}`);

        this.toastSuccess("Thành công!", "Đã xóa đơn hàng.");
        this.fetchOrders();
      } catch (error) {
        console.error("❌ Không thể xóa đơn:", error);
        this.toastError(
          "Lỗi",
          error.response?.data?.message || "Không thể xóa đơn hàng!"
        );
      }
    },

    closeDetails() {
      this.showModal = false;
      this.selectedOrder = null;
    },

    toastSuccess(title, text) {
      Swal.fire({
        icon: "success",
        title,
        text,
        confirmButtonColor: "#2563eb",
        confirmButtonText: "OK",
      });
    },

    toastError(title, text) {
      Swal.fire({
        icon: "error",
        title,
        text,
        confirmButtonColor: "#ef4444",
        confirmButtonText: "OK",
      });
    },

    advanceStatus(order) {
      const flow = [
        "pending",
        "confirmed",
        "preparing",
        "delivering",
        "delivered",
      ];
      const current = this.normalizeStatus(order.orderStatus);
      const idx = flow.indexOf(current);
      const next = idx >= 0 && idx < flow.length - 1 ? flow[idx + 1] : null;
      if (!next) {
        this.toastError("Thông báo", "Trạng thái đã ở bước cuối cùng.");
        return;
      }
      order.orderStatus = next;
      order._changed = true;
      // Ghi luôn xuống backend, bỏ confirm
      this.updateStatus(order, true);
    },

    isTerminal(status) {
      const norm = this.normalizeStatus(status);
      return norm === "delivered" || norm === "cancelled";
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

.orders-page {
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

/* ========== HEADER SECTION ========== */
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 28px;
  flex-wrap: wrap;
  gap: 24px;
}

.header-content {
  flex: 1;
  min-width: 280px;
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

/* ========== STATS CARDS ========== */
.stats-cards {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.stat-card {
  background: white;
  padding: 20px 28px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s ease;
  min-width: 180px;
  border-left: 4px solid #60a5fa;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.stat-card.paid {
  background: linear-gradient(135deg, #d8e8ff 0%, #bbf7d0 100%);
  border-left-color: #22c55e;
}

.stat-card.unpaid {
  background: linear-gradient(135deg, #e8f0ff 0%, #dbeafe 100%);
  border-left-color: #f59e0b;
}

.stat-icon {
  font-size: 2rem;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-number {
  font-size: 1.8rem;
  font-weight: 800;
  color: #1f2937;
}

.stat-label {
  font-size: 0.85rem;
  color: #6b7280;
  font-weight: 500;
}

/* ========== TABLE SECTION ========== */
.table-section {
  background: white;
  border-radius: 20px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.table-header {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  padding: 20px 28px;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
}

.table-header h3 {
  font-size: 1.4rem;
  font-weight: 700;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 100%;
}

.filter-group label {
  font-size: 14px;
  font-weight: 600;
}

.filter-select {
  padding: 12px 14px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 10px;
  font-size: 14px;
  background: rgba(255, 255, 255, 0.15);
  color: white;
  cursor: pointer;
  min-width: 140px;
}

.filter-select option {
  color: #333;
  background: white;
}

.date-range {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
  background: rgba(255, 255, 255, 0.14);
  padding: 12px 16px;
  border-radius: 14px;
}

.date-field {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.date-field label {
  font-size: 12px;
  font-weight: 700;
  color: #6b7280;
}

.date-field input {
  padding: 10px 12px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.95);
  min-width: 180px;
  font-weight: 700;
  font-size: 15px;
}

.quick-range {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.btn-quick {
  padding: 12px 16px;
  border-radius: 10px;
  border: 2px solid #e5e7eb;
  background: #f3f4f6;
  cursor: pointer;
  font-weight: 700;
  color: #111827;
  transition: all 0.2s ease;
  min-width: 110px;
  text-align: center;
}

.btn-quick:hover {
  background: #3b82f6;
  color: white;
  border-color: #2563eb;
}

.btn-refresh {
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 10px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-refresh:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.3);
}

.btn-refresh:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* ========== TABLE ========== */
.table-container {
  overflow-x: auto;
}

/* Cards Layout */
.cards-container {
  padding: 20px;
}

.orders-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 16px;
}

.order-card {
  background: white;
  border: 1px solid #f4f7fb;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-card__top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
}

.order-code {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.order-id {
  font-weight: 700;
  color: #6b7280;
  font-size: 12px;
}

.order-short {
  font-family: monospace;
  font-size: 14px;
  color: #1f2937;
  background: #e8f0ff;
  padding: 4px 8px;
  border-radius: 6px;
  max-width: 180px;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.order-total {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  padding: 10px 14px;
  border-radius: 12px;
  font-weight: 800;
  min-width: 110px;
  text-align: right;
  box-shadow: 0 8px 18px rgba(124, 58, 237, 0.2);
}

.order-meta {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.meta-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.meta-item.full {
  grid-column: 1 / -1;
}

.meta-label {
  font-size: 12px;
  color: #9ca3af;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.meta-value {
  font-size: 14px;
  color: #1f2937;
  font-weight: 600;
}

.order-status-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
  align-items: center;
}

.pill {
  background: #f8f5ff;
  border: 1px solid #e0e7ff;
  border-radius: 12px;
  padding: 10px 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.pill-label {
  font-size: 12px;
  font-weight: 700;
  color: #6b7280;
  text-transform: uppercase;
}

.pill-badge {
  display: inline-flex;
  align-items: center;
  width: fit-content;
  gap: 6px;
  padding: 6px 10px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 800;
  text-transform: uppercase;
}

.pill-badge.paid {
  background: #d8e8ff;
  color: #166534;
}
.pill-badge.pending {
  background: #e8f0ff;
  color: #92400e;
}
.pill-badge.failed {
  background: #e6eefc;
  color: #991b1b;
}

.pill-method {
  font-size: 12px;
  color: #6b7280;
  font-weight: 600;
}

.status-select-wrapper {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.status-select-wrapper label {
  font-size: 12px;
  font-weight: 700;
  color: #6b7280;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-select {
  width: 100%;
  padding: 10px 12px;
  border: 2px solid #e5e7eb;
  border-radius: 10px;
  font-size: 13px;
  font-weight: 600;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.status-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.1);
}

.status-pending {
  border-color: #fbbf24;
  background: #fffbeb;
}
.status-confirmed {
  border-color: #60a5fa;
  background: #eff6ff;
}
.status-preparing {
  border-color: #60a5fa;
  background: #e8f0ff;
}
.status-delivering {
  border-color: #34d399;
  background: #ecfdf5;
}
.status-delivered {
  border-color: #22c55e;
  background: #d8e8ff;
}
.status-cancelled {
  border-color: #f87171;
  background: #e9efff;
}

.card-actions {
  justify-content: flex-end;
}

/* Action Buttons */
.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.btn-action {
  width: 38px;
  height: 38px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.btn-action:hover:not(:disabled) {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.btn-action:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-confirm {
  background: linear-gradient(135deg, #22c55e 0%, #16a34a 100%);
  color: white;
}

.btn-update {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
}

.btn-details {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
}

.btn-next {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  color: white;
}

.btn-delete {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
}

.order-note {
  margin-top: 12px;
  padding: 10px 12px;
  border-radius: 12px;
  background: #e8f0ff;
  border: 1px solid #fcd34d;
  display: flex;
  gap: 8px;
}

.note-label {
  font-weight: 700;
  color: #92400e;
  font-size: 13px;
}

.note-text {
  color: #78350f;
  font-size: 13px;
  word-break: break-word;
}

/* Loading & Empty States */
.loading-cell,
.empty-cell {
  text-align: center;
  padding: 60px 20px !important;
}

.loading-cell {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  color: #3b82f6;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e1e9f7;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.empty-cell {
  color: #9ca3af;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 12px;
  display: block;
}

/* Pagination */
.pagination {
  padding: 20px 28px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  border-top: 1px solid #f4f7fb;
}

.pagination button {
  padding: 10px 20px;
  border-radius: 10px;
  border: none;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s;
}

.pagination button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.3);
}

.pagination button:disabled {
  background: #e5e7eb;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

/* ========== MODAL ========== */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(4px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 20px;
}

.modal-content {
  background: white;
  border-radius: 20px;
  width: 100%;
  max-width: 540px;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  box-shadow: 0 25px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    transform: translateY(30px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
}

.modal-header h3 {
  font-size: 1.4rem;
  font-weight: 700;
}

.modal-close {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  font-size: 20px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease;
}

.modal-close:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: rotate(90deg);
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  flex: 1;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f4f7fb;
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-label {
  font-weight: 600;
  color: #6b7280;
  font-size: 14px;
}

.detail-value {
  font-weight: 500;
  color: #1f2937;
  font-size: 14px;
}

.detail-value.total {
  font-size: 18px;
  font-weight: 800;
  color: #2563eb;
}

.detail-note-row {
  align-items: flex-start;
}

.note-value {
  text-align: right;
  color: #92400e;
  white-space: pre-wrap;
}

.status-badge {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.items-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 2px solid #f4f7fb;
}

.items-section h4 {
  font-size: 15px;
  color: #2563eb;
  margin-bottom: 12px;
  font-weight: 700;
}

.items-list {
  list-style: none;
  background: #faf5ff;
  border-radius: 12px;
  overflow: hidden;
}

.item-row {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f4f7fb;
}

.item-row:last-child {
  border-bottom: none;
}

.item-name {
  flex: 1;
  font-weight: 600;
  color: #1f2937;
}

.item-qty {
  width: 60px;
  text-align: center;
  color: #6b7280;
}

.item-price {
  width: 100px;
  text-align: right;
  font-weight: 700;
  color: #2563eb;
}

.modal-footer {
  padding: 20px 24px;
  border-top: 1px solid #f4f7fb;
  display: flex;
  justify-content: flex-end;
}

.btn-close-modal {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border: none;
  padding: 12px 28px;
  border-radius: 12px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-close-modal:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(139, 92, 246, 0.4);
}

/* ========== RESPONSIVE ========== */
@media (max-width: 1024px) {
  .header-section {
    flex-direction: column;
  }

  .stats-cards {
    width: 100%;
  }

  .stat-card {
    flex: 1;
    min-width: 0;
  }
}

@media (max-width: 768px) {
  .orders-page {
    padding: 16px;
  }

  .page-title {
    font-size: 1.6rem;
  }

  .table-header {
    flex-direction: column;
    align-items: stretch;
  }

  .header-actions {
    flex-direction: column;
  }

  .filter-select,
  .btn-refresh {
    width: 100%;
  }

  .stat-card {
    padding: 16px 20px;
  }

  .stat-number {
    font-size: 1.4rem;
  }

  .action-buttons {
    flex-wrap: nowrap;
  }

  .btn-action {
    width: 34px;
    height: 34px;
    font-size: 14px;
  }
}
</style>
