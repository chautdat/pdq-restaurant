<template>
  <div class="promo-manager">
    <!-- Header with Stats -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1>
            <span class="icon">🎟️</span>
            Quản Lý Mã Giảm Giá
          </h1>
          <p class="subtitle">Tạo và quản lý các mã khuyến mãi cho cửa hàng</p>
        </div>
        <button class="btn-primary" @click="openCreateForm">
          <span class="icon">➕</span>
          Tạo Mã Mới
        </button>
      </div>

      <!-- Stats Cards -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon blue">📊</div>
          <div class="stat-info">
            <div class="stat-value">{{ allPromos.length }}</div>
            <div class="stat-label">Tổng Mã</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon green">✓</div>
          <div class="stat-info">
            <div class="stat-value">{{ activePromos.length }}</div>
            <div class="stat-label">Đang Hoạt Động</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon red">✕</div>
          <div class="stat-info">
            <div class="stat-value">{{ inactivePromos.length }}</div>
            <div class="stat-label">Vô Hiệu Hóa</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon purple">👥</div>
          <div class="stat-info">
            <div class="stat-value">{{ totalUsage }}</div>
            <div class="stat-label">Lượt Sử Dụng</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Toolbar -->
    <div class="toolbar">
      <div class="search-box">
        <span class="search-icon">🔍</span>
        <input
          v-model="searchText"
          type="text"
          placeholder="Tìm kiếm theo mã hoặc mô tả..."
          class="search-input"
        />
        <button v-if="searchText" class="clear-btn" @click="searchText = ''">
          ✕
        </button>
      </div>

      <div class="toolbar-actions">
        <div class="filter-tabs">
          <button
            :class="['tab-btn', { active: statusFilter === 'all' }]"
            @click="statusFilter = 'all'"
          >
            Tất Cả
            <span class="badge">{{ allPromos.length }}</span>
          </button>
          <button
            :class="['tab-btn', { active: statusFilter === 'active' }]"
            @click="statusFilter = 'active'"
          >
            Hoạt Động
            <span class="badge green">{{ activePromos.length }}</span>
          </button>
          <button
            :class="['tab-btn', { active: statusFilter === 'inactive' }]"
            @click="statusFilter = 'inactive'"
          >
            Vô Hiệu
            <span class="badge red">{{ inactivePromos.length }}</span>
          </button>
        </div>

        <div class="divider"></div>

        <button class="btn-icon" @click="loadPromos" title="Làm mới">
          <span :class="{ rotating: loading }">🔄</span>
        </button>
        <button class="btn-icon" @click="exportData" title="Xuất dữ liệu">
          📥
        </button>
      </div>
    </div>

    <!-- Alert Messages -->
    <transition name="slide-down">
      <div v-if="successMsg" class="alert alert-success">
        <span class="alert-icon">✓</span>
        <span>{{ successMsg }}</span>
        <button @click="successMsg = ''" class="alert-close">✕</button>
      </div>
    </transition>

    <transition name="slide-down">
      <div v-if="errorMsg" class="alert alert-error">
        <span class="alert-icon">⚠</span>
        <span>{{ errorMsg }}</span>
        <button @click="errorMsg = ''" class="alert-close">✕</button>
      </div>
    </transition>

    <!-- Loading State -->
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Đang tải dữ liệu...</p>
    </div>

    <!-- Table -->
    <div v-else-if="filteredPromos.length > 0" class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th class="sortable" @click="sortBy('code')">
              <div class="th-content">
                Mã Code
                <span class="sort-icon">{{ getSortIcon("code") }}</span>
              </div>
            </th>
            <th>Loại Giảm</th>
            <th>Giá Trị</th>
            <th class="text-center">Điều Kiện</th>
            <th class="text-center sortable" @click="sortBy('active')">
              <div class="th-content">
                Trạng Thái
                <span class="sort-icon">{{ getSortIcon("active") }}</span>
              </div>
            </th>
            <th class="text-center">Sử Dụng</th>
            <th class="sortable" @click="sortBy('endDate')">
              <div class="th-content">
                Hết Hạn
                <span class="sort-icon">{{ getSortIcon("endDate") }}</span>
              </div>
            </th>
            <th class="text-center">Thao Tác</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="promo in sortedPromos"
            :key="promo.promoCodeId"
            class="data-row"
          >
            <td class="code-cell">
              <div class="code-wrapper">
                <code class="promo-code">{{ promo.code }}</code>
                <button
                  class="copy-btn"
                  @click="copyCode(promo.code)"
                  title="Copy mã"
                >
                  📋
                </button>
              </div>
              <div v-if="promo.description" class="code-desc">
                {{ promo.description }}
              </div>
            </td>

            <td>
              <span :class="['type-tag', `type-${promo.discountType}`]">
                <span class="type-icon">{{
                  getTypeIcon(promo.discountType)
                }}</span>
                {{ getTypeLabel(promo.discountType) }}
              </span>
            </td>

            <td>
              <div class="value-cell">
                <div class="value-main">{{ formatValue(promo) }}</div>
                <div v-if="promo.maxDiscountAmount" class="value-sub">
                  Tối đa: {{ formatNumber(promo.maxDiscountAmount) }}đ
                </div>
              </div>
            </td>

            <td class="text-center">
              <div v-if="promo.minimumOrderAmount" class="condition-badge">
                Đơn tối thiểu: {{ formatNumber(promo.minimumOrderAmount) }}đ
              </div>
              <span v-else class="text-muted">-</span>
            </td>

            <td class="text-center">
              <button
                :class="['status-badge', { active: promo.active }]"
                @click="toggleStatus(promo.promoCodeId)"
                :title="
                  promo.active ? 'Click để vô hiệu hóa' : 'Click để kích hoạt'
                "
              >
                <span class="status-dot"></span>
                {{ promo.active ? "Hoạt Động" : "Vô Hiệu" }}
              </button>
            </td>

            <td class="text-center">
              <div class="usage-info">
                <div class="usage-bar-wrapper">
                  <div
                    class="usage-bar"
                    :style="{
                      width: getUsagePercent(promo) + '%',
                      backgroundColor: getUsageColor(promo),
                    }"
                  ></div>
                </div>
                <div class="usage-text">
                  {{ promo.usageCount || 0
                  }}<span v-if="promo.usageLimit">/{{ promo.usageLimit }}</span
                  ><span v-else>/∞</span>
                </div>
              </div>
            </td>

            <td>
              <div class="date-cell">
                <span
                  v-if="promo.endDate"
                  :class="['date-badge', { expired: isExpired(promo.endDate) }]"
                >
                  {{ formatDate(promo.endDate) }}
                </span>
                <span v-else class="date-badge unlimited">Vô hạn</span>
              </div>
            </td>

            <td class="text-center">
              <div class="action-buttons">
                <button
                  class="action-btn view"
                  @click="viewDetails(promo)"
                  title="Xem chi tiết"
                >
                  👁️
                </button>
                <button
                  class="action-btn edit"
                  @click="editPromo(promo)"
                  title="Chỉnh sửa"
                >
                  ✏️
                </button>
                <button
                  class="action-btn delete"
                  @click="deletePromo(promo.promoCodeId)"
                  title="Xóa"
                >
                  🗑️
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Empty State -->
    <div v-else class="empty-state">
      <div class="empty-icon">🎫</div>
      <h3>Chưa có mã giảm giá nào</h3>
      <p>Tạo mã giảm giá đầu tiên để bắt đầu chương trình khuyến mãi</p>
      <button class="btn-primary" @click="openCreateForm">
        <span class="icon">➕</span>
        Tạo Mã Đầu Tiên
      </button>
    </div>

    <!-- Create/Edit Modal -->
    <transition name="modal">
      <div
        v-if="showCreateForm || showEditForm"
        class="modal-overlay"
        @click.self="closeForm"
      >
        <div class="modal-container">
          <div class="modal-header">
            <h2>
              <span class="icon">{{ showEditForm ? "✏️" : "➕" }}</span>
              {{
                showEditForm ? "Chỉnh Sửa Mã Giảm Giá" : "Tạo Mã Giảm Giá Mới"
              }}
            </h2>
            <button class="modal-close" @click="closeForm">✕</button>
          </div>

          <form @submit.prevent="submitForm" class="modal-body">
            <!-- Code & Description -->
            <div class="form-section">
              <h3 class="section-title">Thông Tin Cơ Bản</h3>

              <div class="form-group">
                <label class="form-label required">Mã Code</label>
                <input
                  v-model="formData.code"
                  type="text"
                  class="form-input"
                  placeholder="VD: SUMMER2024, FREESHIP50"
                  :disabled="showEditForm"
                  required
                  maxlength="50"
                />
                <p class="form-hint">
                  Mã code phải là duy nhất, không có khoảng trắng
                </p>
              </div>

              <div class="form-group">
                <label class="form-label">Mô Tả</label>
                <textarea
                  v-model="formData.description"
                  class="form-textarea"
                  rows="3"
                  placeholder="Mô tả chi tiết về mã giảm giá này..."
                  maxlength="500"
                ></textarea>
                <p class="form-hint">
                  {{ formData.description?.length || 0 }}/500 ký tự
                </p>
              </div>
            </div>

            <!-- Discount Settings -->
            <div class="form-section">
              <h3 class="section-title">Cài Đặt Giảm Giá</h3>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label required">Loại Giảm Giá</label>
                  <select
                    v-model="formData.discountType"
                    class="form-select"
                    required
                  >
                    <option value="">-- Chọn loại giảm giá --</option>
                    <option value="PERCENTAGE">
                      📊 Giảm theo % (Percentage)
                    </option>
                    <option value="FIXED_AMOUNT">
                      💰 Giảm số tiền cố định (Fixed Amount)
                    </option>
                    <option value="FREE_SHIPPING">
                      🚚 Miễn phí vận chuyển (Free Shipping)
                    </option>
                  </select>
                </div>

                <div class="form-group">
                  <label class="form-label required">Giá Trị Giảm</label>
                  <div class="input-with-suffix">
                    <input
                      v-model.number="formData.discountValue"
                      type="number"
                      class="form-input"
                      :placeholder="getPlaceholder()"
                      step="0.01"
                      min="0"
                      required
                    />
                    <span class="input-suffix">{{
                      formData.discountType === "PERCENTAGE" ? "%" : "VND"
                    }}</span>
                  </div>
                </div>
              </div>

              <div
                v-if="formData.discountType === 'PERCENTAGE'"
                class="form-group"
              >
                <label class="form-label">Giảm Giá Tối Đa</label>
                <div class="input-with-suffix">
                  <input
                    v-model.number="formData.maxDiscountAmount"
                    type="number"
                    class="form-input"
                    placeholder="Để trống nếu không giới hạn"
                    min="0"
                  />
                  <span class="input-suffix">VND</span>
                </div>
                <p class="form-hint">
                  Số tiền giảm tối đa khi áp dụng giảm theo %
                </p>
              </div>

              <div class="form-group">
                <label class="form-label">Giá Trị Đơn Hàng Tối Thiểu</label>
                <div class="input-with-suffix">
                  <input
                    v-model.number="formData.minimumOrderAmount"
                    type="number"
                    class="form-input"
                    placeholder="0"
                    min="0"
                  />
                  <span class="input-suffix">VND</span>
                </div>
                <p class="form-hint">
                  Đơn hàng phải đạt giá trị tối thiểu này mới áp dụng được mã
                </p>
              </div>
            </div>

            <!-- Usage & Time Settings -->
            <div class="form-section">
              <h3 class="section-title">Giới Hạn & Thời Gian</h3>

              <div class="form-group">
                <label class="form-label">Giới Hạn Số Lần Sử Dụng</label>
                <input
                  v-model.number="formData.usageLimit"
                  type="number"
                  class="form-input"
                  placeholder="Để trống = không giới hạn"
                  min="1"
                />
                <p class="form-hint">Tổng số lần mã này có thể được sử dụng</p>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label class="form-label">Ngày Bắt Đầu</label>
                  <input
                    v-model="formData.startDate"
                    type="datetime-local"
                    class="form-input"
                  />
                </div>

                <div class="form-group">
                  <label class="form-label">Ngày Kết Thúc</label>
                  <input
                    v-model="formData.endDate"
                    type="datetime-local"
                    class="form-input"
                  />
                </div>
              </div>
            </div>

            <!-- Status -->
            <div class="form-section">
              <div class="form-group-checkbox">
                <label class="checkbox-label">
                  <input
                    v-model="formData.active"
                    type="checkbox"
                    class="checkbox-input"
                  />
                  <span class="checkbox-text">
                    <span class="checkbox-title">Kích Hoạt Ngay</span>
                    <span class="checkbox-desc"
                      >Mã sẽ có thể sử dụng được ngay sau khi tạo</span
                    >
                  </span>
                </label>
              </div>
            </div>

            <div class="modal-footer">
              <button type="button" class="btn-secondary" @click="closeForm">
                Hủy Bỏ
              </button>
              <button type="submit" class="btn-primary" :disabled="submitting">
                <span v-if="submitting" class="spinner-small"></span>
                <span v-else class="icon">💾</span>
                {{ submitting ? "Đang lưu..." : "Lưu Mã Giảm Giá" }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <!-- Details Modal -->
    <transition name="modal">
      <div
        v-if="showDetailsModal && selectedPromo"
        class="modal-overlay"
        @click.self="showDetailsModal = false"
      >
        <div class="modal-container details-modal">
          <div class="modal-header">
            <h2>
              <span class="icon">📋</span>
              Chi Tiết Mã Giảm Giá
            </h2>
            <button class="modal-close" @click="showDetailsModal = false">
              ✕
            </button>
          </div>

          <div class="modal-body details-content">
            <div class="detail-section">
              <div class="detail-header">
                <code class="promo-code large">{{ selectedPromo.code }}</code>
                <span
                  :class="[
                    'status-badge large',
                    { active: selectedPromo.active },
                  ]"
                >
                  <span class="status-dot"></span>
                  {{ selectedPromo.active ? "Hoạt Động" : "Vô Hiệu" }}
                </span>
              </div>

              <p v-if="selectedPromo.description" class="detail-description">
                {{ selectedPromo.description }}
              </p>
            </div>

            <div class="detail-grid">
              <div class="detail-item">
                <div class="detail-label">Loại Giảm Giá</div>
                <div class="detail-value">
                  <span
                    :class="['type-tag', `type-${selectedPromo.discountType}`]"
                  >
                    <span class="type-icon">{{
                      getTypeIcon(selectedPromo.discountType)
                    }}</span>
                    {{ getTypeLabel(selectedPromo.discountType) }}
                  </span>
                </div>
              </div>

              <div class="detail-item">
                <div class="detail-label">Giá Trị Giảm</div>
                <div class="detail-value highlight">
                  {{ formatValue(selectedPromo) }}
                </div>
              </div>

              <div v-if="selectedPromo.maxDiscountAmount" class="detail-item">
                <div class="detail-label">Giảm Tối Đa</div>
                <div class="detail-value">
                  {{ formatNumber(selectedPromo.maxDiscountAmount) }} VND
                </div>
              </div>

              <div v-if="selectedPromo.minimumOrderAmount" class="detail-item">
                <div class="detail-label">Đơn Tối Thiểu</div>
                <div class="detail-value">
                  {{ formatNumber(selectedPromo.minimumOrderAmount) }} VND
                </div>
              </div>

              <div class="detail-item">
                <div class="detail-label">Đã Sử Dụng</div>
                <div class="detail-value">
                  {{ selectedPromo.usageCount || 0
                  }}<span v-if="selectedPromo.usageLimit"
                    >/{{ selectedPromo.usageLimit }} lần</span
                  ><span v-else> lần (Không giới hạn)</span>
                </div>
              </div>

              <div class="detail-item full-width">
                <div class="detail-label">Thời Gian Hiệu Lực</div>
                <div class="detail-value">
                  <div v-if="selectedPromo.startDate || selectedPromo.endDate">
                    📅
                    {{
                      formatDateTime(selectedPromo.startDate) || "Ngay lập tức"
                    }}
                    <br />
                    → {{ formatDateTime(selectedPromo.endDate) || "Vô hạn" }}
                  </div>
                  <div v-else>Vô hạn</div>
                </div>
              </div>

              <div class="detail-item">
                <div class="detail-label">Ngày Tạo</div>
                <div class="detail-value">
                  {{ formatDateTime(selectedPromo.createdAt) }}
                </div>
              </div>

              <div v-if="selectedPromo.updatedAt" class="detail-item">
                <div class="detail-label">Cập Nhật Lần Cuối</div>
                <div class="detail-value">
                  {{ formatDateTime(selectedPromo.updatedAt) }}
                </div>
              </div>
            </div>
          </div>

          <div class="modal-footer">
            <button class="btn-secondary" @click="showDetailsModal = false">
              Đóng
            </button>
            <button
              class="btn-primary"
              @click="
                editPromo(selectedPromo);
                showDetailsModal = false;
              "
            >
              <span class="icon">✏️</span>
              Chỉnh Sửa
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import apiClient from "@/axios";

export default {
  name: "PromoCodeManager",

  data() {
    return {
      allPromos: [],
      loading: false,
      submitting: false,

      searchText: "",
      statusFilter: "all",
      sortField: null,
      sortDirection: "asc",

      showCreateForm: false,
      showEditForm: false,
      showDetailsModal: false,
      selectedPromo: null,

      successMsg: "",
      errorMsg: "",

      formData: this.getEmptyFormData(),
    };
  },

  computed: {
    activePromos() {
      return this.allPromos.filter((p) => p.active);
    },

    inactivePromos() {
      return this.allPromos.filter((p) => !p.active);
    },

    totalUsage() {
      return this.allPromos.reduce((sum, p) => sum + (p.usageCount || 0), 0);
    },

    filteredPromos() {
      let filtered = this.allPromos;

      // Filter by status
      if (this.statusFilter === "active") {
        filtered = filtered.filter((p) => p.active);
      } else if (this.statusFilter === "inactive") {
        filtered = filtered.filter((p) => !p.active);
      }

      // Filter by search
      if (this.searchText) {
        const q = this.searchText.toLowerCase();
        filtered = filtered.filter(
          (p) =>
            p.code.toLowerCase().includes(q) ||
            (p.description && p.description.toLowerCase().includes(q))
        );
      }

      return filtered;
    },

    sortedPromos() {
      if (!this.sortField) return this.filteredPromos;

      return [...this.filteredPromos].sort((a, b) => {
        let aVal = a[this.sortField];
        let bVal = b[this.sortField];

        // Handle null/undefined
        if (aVal == null) return 1;
        if (bVal == null) return -1;

        // String comparison
        if (typeof aVal === "string") {
          aVal = aVal.toLowerCase();
          bVal = bVal.toLowerCase();
        }

        const comparison = aVal > bVal ? 1 : aVal < bVal ? -1 : 0;
        return this.sortDirection === "asc" ? comparison : -comparison;
      });
    },
  },

  methods: {
    getEmptyFormData() {
      return {
        code: "",
        description: "",
        discountType: "",
        discountValue: 0,
        minimumOrderAmount: 0,
        maxDiscountAmount: null,
        usageLimit: null,
        startDate: "",
        endDate: "",
        active: true,
      };
    },

    async loadPromos() {
      this.loading = true;
      this.errorMsg = "";
      try {
        const res = await apiClient.get("/admin/promo-codes");
        const payload = res.data;
        const data = Array.isArray(payload)
          ? payload
          : payload?.data || payload?.content || [];
        this.allPromos = Array.isArray(data?.content) ? data.content : data;
      } catch (error) {
        this.errorMsg = error.response?.data?.message || error.message;
        console.error("Load error:", error);
      } finally {
        this.loading = false;
      }
    },

    async submitForm() {
      this.submitting = true;
      this.errorMsg = "";
      try {
        const payload = {
          ...this.formData,
          discountValue: parseFloat(this.formData.discountValue),
          minimumOrderAmount: parseFloat(this.formData.minimumOrderAmount) || 0,
          maxDiscountAmount: this.formData.maxDiscountAmount
            ? parseFloat(this.formData.maxDiscountAmount)
            : null,
          usageLimit: this.formData.usageLimit
            ? parseInt(this.formData.usageLimit)
            : null,
        };

        if (this.showEditForm) {
          await apiClient.put(
            `/admin/promo-codes/${this.selectedPromo.promoCodeId}`,
            payload
          );
          this.successMsg = "✅ Cập nhật mã giảm giá thành công!";
        } else {
          await apiClient.post("/admin/promo-codes", payload);
          this.successMsg = "✅ Tạo mã giảm giá thành công!";
        }

        this.closeForm();
        await this.loadPromos();
      } catch (error) {
        this.errorMsg =
          error.response?.data?.message || "Có lỗi xảy ra, vui lòng thử lại";
      } finally {
        this.submitting = false;
      }
    },

    async toggleStatus(id) {
      try {
        await apiClient.post(`/admin/promo-codes/${id}/toggle`);
        this.successMsg = "✅ Cập nhật trạng thái thành công!";
        await this.loadPromos();
      } catch (error) {
        this.errorMsg = error.response?.data?.message || error.message;
      }
    },

    async deletePromo(id) {
      if (!confirm("Bạn có chắc chắn muốn xóa mã giảm giá này?")) return;

      try {
        await apiClient.delete(`/admin/promo-codes/${id}`);
        this.successMsg = "✅ Xóa mã giảm giá thành công!";
        await this.loadPromos();
      } catch (error) {
        this.errorMsg = error.response?.data?.message || error.message;
      }
    },

    openCreateForm() {
      this.showCreateForm = true;
      this.formData = this.getEmptyFormData();
    },

    editPromo(promo) {
      this.showEditForm = true;
      this.selectedPromo = promo;
      this.formData = {
        code: promo.code,
        description: promo.description || "",
        discountType: promo.discountType,
        discountValue: promo.discountValue,
        minimumOrderAmount: promo.minimumOrderAmount || 0,
        maxDiscountAmount: promo.maxDiscountAmount,
        usageLimit: promo.usageLimit,
        startDate: promo.startDate ? this.toInputFormat(promo.startDate) : "",
        endDate: promo.endDate ? this.toInputFormat(promo.endDate) : "",
        active: promo.active,
      };
    },

    viewDetails(promo) {
      this.selectedPromo = promo;
      this.showDetailsModal = true;
    },

    closeForm() {
      this.showCreateForm = false;
      this.showEditForm = false;
      this.selectedPromo = null;
      this.formData = this.getEmptyFormData();
    },

    sortBy(field) {
      if (this.sortField === field) {
        this.sortDirection = this.sortDirection === "asc" ? "desc" : "asc";
      } else {
        this.sortField = field;
        this.sortDirection = "asc";
      }
    },

    getSortIcon(field) {
      if (this.sortField !== field) return "⇅";
      return this.sortDirection === "asc" ? "↑" : "↓";
    },

    copyCode(code) {
      navigator.clipboard.writeText(code).then(() => {
        this.successMsg = `✅ Đã copy mã: ${code}`;
        setTimeout(() => (this.successMsg = ""), 2000);
      });
    },

    exportData() {
      const csv = this.generateCSV();
      const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = `promo-codes-${
        new Date().toISOString().split("T")[0]
      }.csv`;
      link.click();
      this.successMsg = "✅ Xuất dữ liệu thành công!";
    },

    generateCSV() {
      const headers = [
        "Mã Code",
        "Loại",
        "Giá Trị",
        "Trạng Thái",
        "Số Lần Dùng",
        "Giới Hạn",
        "Ngày Tạo",
      ];
      const rows = this.allPromos.map((p) => [
        p.code,
        this.getTypeLabel(p.discountType),
        this.formatValue(p),
        p.active ? "Hoạt Động" : "Vô Hiệu",
        p.usageCount || 0,
        p.usageLimit || "Không giới hạn",
        this.formatDateTime(p.createdAt),
      ]);
      return [headers, ...rows].map((row) => row.join(",")).join("\n");
    },

    getTypeLabel(type) {
      const labels = {
        PERCENTAGE: "Giảm %",
        FIXED_AMOUNT: "Giảm Tiền",
        FREE_SHIPPING: "Miễn Ship",
      };
      return labels[type] || type;
    },

    getTypeIcon(type) {
      const icons = {
        PERCENTAGE: "📊",
        FIXED_AMOUNT: "💰",
        FREE_SHIPPING: "🚚",
      };
      return icons[type] || "🎟️";
    },

    formatValue(promo) {
      switch (promo.discountType) {
        case "PERCENTAGE":
          return `${promo.discountValue}%`;
        case "FIXED_AMOUNT":
          return `${this.formatNumber(promo.discountValue)}đ`;
        case "FREE_SHIPPING":
          return "Miễn Phí Ship";
        default:
          return promo.discountValue;
      }
    },

    formatNumber(n) {
      return new Intl.NumberFormat("vi-VN").format(n || 0);
    },

    formatDate(d) {
      if (!d) return "";
      return new Date(d).toLocaleDateString("vi-VN");
    },

    formatDateTime(d) {
      if (!d) return "";
      return new Date(d).toLocaleString("vi-VN");
    },

    toInputFormat(d) {
      if (!d) return "";
      const date = new Date(d);
      const y = date.getFullYear();
      const m = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      const h = String(date.getHours()).padStart(2, "0");
      const min = String(date.getMinutes()).padStart(2, "0");
      return `${y}-${m}-${day}T${h}:${min}`;
    },

    getPlaceholder() {
      switch (this.formData.discountType) {
        case "PERCENTAGE":
          return "VD: 20 (cho 20%)";
        case "FIXED_AMOUNT":
          return "VD: 50000";
        case "FREE_SHIPPING":
          return "1";
        default:
          return "Nhập giá trị";
      }
    },

    getUsagePercent(promo) {
      if (!promo.usageLimit) return 0;
      return Math.min(((promo.usageCount || 0) / promo.usageLimit) * 100, 100);
    },

    getUsageColor(promo) {
      const percent = this.getUsagePercent(promo);
      if (percent >= 90) return "#dc3545";
      if (percent >= 70) return "#ffc107";
      return "#28a745";
    },

    isExpired(endDate) {
      return endDate && new Date(endDate) < new Date();
    },
  },

  mounted() {
    this.loadPromos();

    // Auto-hide alerts after 5 seconds
    this.$watch("successMsg", (val) => {
      if (val) setTimeout(() => (this.successMsg = ""), 5000);
    });
    this.$watch("errorMsg", (val) => {
      if (val) setTimeout(() => (this.errorMsg = ""), 7000);
    });
  },
};
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.promo-manager {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
  padding: 30px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
    "Helvetica Neue", Arial, sans-serif;
}

/* ============ HEADER ============ */
.page-header {
  margin-bottom: 30px;
}

.header-content {
  background: white;
  padding: 30px;
  border-radius: 16px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.07);
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-text h1 {
  margin: 0 0 8px 0;
  font-size: 32px;
  font-weight: 700;
  color: #1a202c;
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-text h1 .icon {
  font-size: 36px;
}

.subtitle {
  margin: 0;
  color: #718096;
  font-size: 15px;
}

/* ============ STATS CARDS ============ */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
}

.stat-card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
}

.stat-icon.blue {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.stat-icon.green {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}
.stat-icon.red {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
}
.stat-icon.purple {
  background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #1a202c;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  color: #718096;
  font-weight: 500;
}

/* ============ TOOLBAR ============ */
.toolbar {
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  margin-bottom: 20px;
  display: flex;
  gap: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.search-box {
  flex: 1;
  min-width: 300px;
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 16px;
  font-size: 18px;
  pointer-events: none;
}

.search-input {
  width: 100%;
  padding: 12px 16px 12px 48px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  transition: all 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.clear-btn {
  position: absolute;
  right: 12px;
  background: #e2e8f0;
  border: none;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  transition: all 0.2s;
}

.clear-btn:hover {
  background: #cbd5e0;
}

.toolbar-actions {
  display: flex;
  gap: 15px;
  align-items: center;
}

.filter-tabs {
  display: flex;
  gap: 8px;
  background: #f7fafc;
  padding: 4px;
  border-radius: 10px;
}

.tab-btn {
  padding: 8px 16px;
  border: none;
  background: transparent;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: #4a5568;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
}

.tab-btn:hover {
  background: #e2e8f0;
}

.tab-btn.active {
  background: white;
  color: #667eea;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.badge {
  background: #e2e8f0;
  color: #4a5568;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.badge.green {
  background: #d4edda;
  color: #155724;
}

.badge.red {
  background: #f8d7da;
  color: #721c24;
}

.tab-btn.active .badge {
  background: #667eea;
  color: white;
}

.divider {
  width: 1px;
  height: 32px;
  background: #e2e8f0;
}

.btn-icon {
  background: #f7fafc;
  border: 2px solid #e2e8f0;
  width: 40px;
  height: 40px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.btn-icon:hover {
  background: #667eea;
  border-color: #667eea;
  transform: translateY(-2px);
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

/* ============ BUTTONS ============ */
.btn-primary,
.btn-secondary {
  padding: 12px 24px;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.btn-secondary {
  background: #f7fafc;
  color: #4a5568;
  border: 2px solid #e2e8f0;
}

.btn-secondary:hover {
  background: #e2e8f0;
}

/* ============ ALERTS ============ */
.alert {
  padding: 16px 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.alert-success {
  background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
  color: #155724;
  border-left: 4px solid #28a745;
}

.alert-error {
  background: linear-gradient(135deg, #f8d7da 0%, #f5c6cb 100%);
  color: #721c24;
  border-left: 4px solid #dc3545;
}

.alert-icon {
  font-size: 20px;
  flex-shrink: 0;
}

.alert-close {
  margin-left: auto;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: inherit;
  opacity: 0.6;
  transition: opacity 0.2s;
}

.alert-close:hover {
  opacity: 1;
}

/* ============ LOADING ============ */
.loading-state {
  background: white;
  padding: 80px 20px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.spinner {
  width: 48px;
  height: 48px;
  border: 4px solid #e2e8f0;
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin: 0 auto 16px;
}

.spinner-small {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* ============ TABLE ============ */
.table-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.data-table thead {
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
}

.data-table th {
  padding: 16px 20px;
  text-align: left;
  font-weight: 600;
  color: #475569;
  border-bottom: 2px solid #e2e8f0;
  white-space: nowrap;
}

.data-table th.sortable {
  cursor: pointer;
  user-select: none;
}

.data-table th.sortable:hover {
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

.data-table td {
  padding: 16px 20px;
  border-bottom: 1px solid #f1f5f9;
  vertical-align: middle;
}

.data-row:hover {
  background: #f8fafc;
}

.text-center {
  text-align: center;
}

/* Table Cells */
.code-cell {
  min-width: 200px;
}

.code-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.promo-code {
  background: linear-gradient(135deg, #e0e7ff 0%, #c7d2fe 100%);
  color: #4338ca;
  padding: 6px 12px;
  border-radius: 6px;
  font-family: "Courier New", monospace;
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.promo-code.large {
  font-size: 18px;
  padding: 10px 16px;
}

.copy-btn {
  background: #e0e7ff;
  border: none;
  width: 28px;
  height: 28px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
  flex-shrink: 0;
}

.copy-btn:hover {
  background: #c7d2fe;
  transform: scale(1.1);
}

.code-desc {
  font-size: 12px;
  color: #64748b;
  line-height: 1.4;
}

.type-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
  white-space: nowrap;
}

.type-PERCENTAGE {
  background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
  color: #92400e;
}

.type-FIXED_AMOUNT {
  background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
  color: #1e40af;
}

.type-FREE_SHIPPING {
  background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
  color: #065f46;
}

.type-icon {
  font-size: 14px;
}

.value-cell {
  font-weight: 600;
  color: #1a202c;
}

.value-main {
  font-size: 15px;
  margin-bottom: 2px;
}

.value-sub {
  font-size: 11px;
  color: #64748b;
  font-weight: 400;
}

.condition-badge {
  background: #f1f5f9;
  color: #475569;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 12px;
  display: inline-block;
}

.text-muted {
  color: #94a3b8;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: none;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.status-badge.active {
  background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
  color: #065f46;
}

.status-badge:not(.active) {
  background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
  color: #991b1b;
}

.status-badge:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.status-badge.large {
  padding: 10px 20px;
  font-size: 14px;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: currentColor;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.usage-info {
  min-width: 100px;
}

.usage-bar-wrapper {
  height: 6px;
  background: #e2e8f0;
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: 4px;
}

.usage-bar {
  height: 100%;
  border-radius: 3px;
  transition: all 0.3s;
}

.usage-text {
  font-size: 12px;
  color: #64748b;
  text-align: center;
}

.date-cell {
  min-width: 110px;
}

.date-badge {
  display: inline-block;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 500;
  background: #f1f5f9;
  color: #475569;
}

.date-badge.expired {
  background: #fee2e2;
  color: #991b1b;
}

.date-badge.unlimited {
  background: #e0e7ff;
  color: #4338ca;
}

.action-buttons {
  display: flex;
  gap: 6px;
  justify-content: center;
}

.action-btn {
  background: #f7fafc;
  border: 1px solid #e2e8f0;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.action-btn.view:hover {
  background: #dbeafe;
  border-color: #93c5fd;
}

.action-btn.edit:hover {
  background: #fef3c7;
  border-color: #fde68a;
}

.action-btn.delete:hover {
  background: #fee2e2;
  border-color: #fecaca;
}

/* ============ EMPTY STATE ============ */
.empty-state {
  background: white;
  padding: 80px 20px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-state h3 {
  margin: 0 0 8px 0;
  color: #1a202c;
  font-size: 20px;
}

.empty-state p {
  margin: 0 0 24px 0;
  color: #718096;
  font-size: 14px;
}

/* ============ MODAL ============ */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(4px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  padding: 20px;
}

.modal-container {
  background: white;
  border-radius: 16px;
  width: 100%;
  max-width: 700px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.details-modal {
  max-width: 600px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 30px;
  border-bottom: 2px solid #f1f5f9;
  background: linear-gradient(180deg, #ffffff 0%, #f8fafc 100%);
}

.modal-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: #1a202c;
  display: flex;
  align-items: center;
  gap: 10px;
}

.modal-close {
  background: #f7fafc;
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  font-size: 20px;
  cursor: pointer;
  color: #64748b;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-close:hover {
  background: #e2e8f0;
  color: #1a202c;
}

.modal-body {
  padding: 30px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px 30px;
  border-top: 2px solid #f1f5f9;
  background: #f8fafc;
}

/* ============ FORM ============ */
.form-section {
  margin-bottom: 32px;
  padding-bottom: 32px;
  border-bottom: 2px solid #f1f5f9;
}

.form-section:last-of-type {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.section-title {
  margin: 0 0 20px 0;
  font-size: 16px;
  font-weight: 600;
  color: #475569;
  display: flex;
  align-items: center;
  gap: 8px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #334155;
}

.form-label.required::after {
  content: " *";
  color: #ef4444;
}

.form-input,
.form-textarea,
.form-select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  font-family: inherit;
  transition: all 0.3s;
  background: white;
}

.form-input:focus,
.form-textarea:focus,
.form-select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-input:disabled {
  background: #f7fafc;
  color: #94a3b8;
  cursor: not-allowed;
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.form-select {
  cursor: pointer;
}

.input-with-suffix {
  position: relative;
  display: flex;
  align-items: center;
}

.input-with-suffix .form-input {
  padding-right: 60px;
}

.input-suffix {
  position: absolute;
  right: 16px;
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
  pointer-events: none;
}

.form-hint {
  margin: 6px 0 0 0;
  font-size: 12px;
  color: #64748b;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-group-checkbox {
  margin: 20px 0;
}

.checkbox-label {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  cursor: pointer;
  padding: 16px;
  background: #f8fafc;
  border-radius: 10px;
  border: 2px solid #e2e8f0;
  transition: all 0.3s;
}

.checkbox-label:hover {
  background: #f1f5f9;
  border-color: #cbd5e0;
}

.checkbox-input {
  width: 20px;
  height: 20px;
  margin: 2px 0 0 0;
  cursor: pointer;
  accent-color: #667eea;
}

.checkbox-text {
  flex: 1;
}

.checkbox-title {
  display: block;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 4px;
}

.checkbox-desc {
  display: block;
  font-size: 13px;
  color: #64748b;
}

/* ============ DETAILS MODAL ============ */
.details-content {
  padding: 0;
}

.detail-section {
  padding: 24px 30px;
  border-bottom: 2px solid #f1f5f9;
}

.detail-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 12px;
}

.detail-description {
  margin: 0;
  color: #64748b;
  font-size: 14px;
  line-height: 1.6;
}

.detail-grid {
  display: grid;
  gap: 1px;
  background: #f1f5f9;
}

.detail-item {
  display: grid;
  grid-template-columns: 180px 1fr;
  gap: 20px;
  padding: 20px 30px;
  background: white;
  align-items: start;
}

.detail-item.full-width {
  grid-template-columns: 1fr;
}

.detail-label {
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
}

.detail-value {
  font-size: 14px;
  color: #1a202c;
  word-break: break-word;
}

.detail-value.highlight {
  font-size: 20px;
  font-weight: 700;
  color: #667eea;
}

/* ============ ANIMATIONS ============ */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.3s ease;
}

.slide-down-enter-from {
  transform: translateY(-20px);
  opacity: 0;
}

.slide-down-leave-to {
  transform: translateY(-20px);
  opacity: 0;
}

.modal-enter-active,
.modal-leave-active {
  transition: all 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  transform: scale(0.9) translateY(20px);
}

/* ============ RESPONSIVE ============ */
@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .detail-item {
    grid-template-columns: 140px 1fr;
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .promo-manager {
    padding: 16px;
  }

  .header-content {
    flex-direction: column;
    gap: 20px;
    align-items: stretch;
  }

  .header-text h1 {
    font-size: 24px;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .search-box {
    min-width: 100%;
  }

  .toolbar-actions {
    flex-wrap: wrap;
  }

  .filter-tabs {
    width: 100%;
  }

  .tab-btn {
    flex: 1;
    justify-content: center;
  }

  .table-container {
    overflow-x: auto;
  }

  .data-table {
    min-width: 1000px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .detail-item {
    grid-template-columns: 1fr;
    gap: 8px;
  }

  .modal-container {
    max-width: 100%;
    margin: 0;
    border-radius: 0;
    max-height: 100vh;
  }

  .modal-header,
  .modal-body,
  .modal-footer {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .header-text h1 {
    font-size: 20px;
  }

  .header-text h1 .icon {
    font-size: 24px;
  }

  .stat-card {
    padding: 16px;
  }

  .stat-icon {
    width: 48px;
    height: 48px;
    font-size: 20px;
  }

  .stat-value {
    font-size: 24px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
  }
}
</style>
