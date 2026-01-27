<template>
  <div class="menu-management">
    <!-- Header Section -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1>
            <span class="icon">🍽️</span>
            Quản Lý Thực Đơn
          </h1>
          <p class="subtitle">Quản lý món ăn và danh mục của nhà hàng</p>
        </div>
        <!-- Quick Stats -->
        <div class="quick-stats">
          <div class="stat-item blue">
            <div class="stat-icon">📊</div>
            <div class="stat-content">
              <div class="stat-value">{{ products.length }}</div>
              <div class="stat-label">Tổng Món</div>
            </div>
          </div>

          <div class="stat-item green">
            <div class="stat-icon">✅</div>
            <div class="stat-content">
              <div class="stat-value">{{ availableCount }}</div>
              <div class="stat-label">Còn Hàng</div>
            </div>
          </div>

          <div class="stat-item orange">
            <div class="stat-icon">❌</div>
            <div class="stat-content">
              <div class="stat-value">{{ unavailableCount }}</div>
              <div class="stat-label">Hết Hàng</div>
            </div>
          </div>

          <div class="stat-item purple">
            <div class="stat-icon">📁</div>
            <div class="stat-content">
              <div class="stat-value">{{ categories.length }}</div>
              <div class="stat-label">Danh Mục</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Alert Messages -->
    <transition name="slide-fade">
      <div v-if="errorMsg" class="alert alert-error">
        <span class="alert-icon">⚠️</span>
        <span class="alert-message">{{ errorMsg }}</span>
        <button class="alert-close" @click="errorMsg = ''">✕</button>
      </div>
    </transition>

    <!-- Main Layout -->
    <div class="main-layout">
      <!-- Left Side - Form -->
      <div class="form-section">
        <div class="form-card">
          <div class="form-header">
            <h2>
              <span class="icon">{{ editing ? "✏️" : "➕" }}</span>
              {{ editing ? "Chỉnh Sửa Món Ăn" : "Thêm Món Ăn Mới" }}
            </h2>
            <span v-if="editing" class="editing-badge">Đang chỉnh sửa</span>
          </div>

          <form @submit.prevent="saveProduct" class="form-body">
            <!-- Product Name -->
            <div class="form-group">
              <label class="form-label">
                <span class="label-text">Tên Món Ăn</span>
                <span class="required">*</span>
              </label>
              <input
                v-model="product.productName"
                type="text"
                class="form-input"
                placeholder="Vd: Phở Bò, Cơm Gà Xối Mỡ..."
                required
              />
            </div>

            <!-- Price & Category -->
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">
                  <span class="label-text">Giá Tiền</span>
                  <span class="required">*</span>
                </label>
                <div class="input-with-addon">
                  <input
                    v-model="product.price"
                    type="number"
                    class="form-input"
                    placeholder="50000"
                    min="0"
                    step="1000"
                    required
                  />
                  <span class="input-addon">VNĐ</span>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">
                  <span class="label-text">Danh Mục</span>
                  <span class="required">*</span>
                </label>
                <select
                  v-model.number="product.categoryId"
                  class="form-select"
                  required
                >
                  <option value="" disabled>Chọn danh mục</option>
                  <option
                    v-for="cat in categories"
                    :key="cat.categoryId"
                    :value="cat.categoryId"
                  >
                    {{ cat.categoryName }}
                  </option>
                </select>
              </div>
            </div>

            <!-- Short Description -->
            <div class="form-group">
              <label class="form-label">
                <span class="label-text">Mô Tả Ngắn</span>
                <span class="optional">(không bắt buộc)</span>
              </label>
              <textarea
                v-model="product.shortDescription"
                class="form-textarea"
                rows="2"
                maxlength="200"
                placeholder="Mô tả ngắn gọn về món ăn..."
              ></textarea>
              <div class="char-counter">
                {{ (product.shortDescription || "").length }}/200
              </div>
            </div>

            <!-- Full Description -->
            <div class="form-group">
              <label class="form-label">
                <span class="label-text">Mô Tả Chi Tiết</span>
                <span class="optional">(không bắt buộc)</span>
              </label>
              <textarea
                v-model="product.description"
                class="form-textarea"
                rows="4"
                placeholder="Mô tả đầy đủ về nguyên liệu, hương vị, cách chế biến..."
              ></textarea>
            </div>

            <!-- Image Upload -->
            <div class="form-group">
              <label class="form-label">
                <span class="label-text">Hình Ảnh</span>
              </label>

              <!-- Preview -->
              <div v-if="previewUrl" class="image-preview">
                <img :src="previewUrl" alt="Preview" class="preview-image" />
                <button
                  type="button"
                  class="btn-remove-image"
                  @click="removeImage"
                >
                  <span class="icon">🗑️</span>
                  Xóa ảnh
                </button>
              </div>

              <!-- Upload -->
              <div v-else class="image-upload">
                <input
                  ref="fileInput"
                  type="file"
                  accept="image/*"
                  @change="handleFileChange"
                  style="display: none"
                />
                <button
                  type="button"
                  class="btn-upload"
                  @click="$refs.fileInput.click()"
                >
                  <span class="icon">📤</span>
                  Tải Lên Hình Ảnh
                </button>
                <p class="upload-hint">PNG, JPG, WEBP (Tối đa 5MB)</p>
              </div>
            </div>

            <!-- Availability Toggle -->
            <div class="form-group">
              <label class="toggle-wrapper">
                <input
                  v-model="product.isAvailable"
                  type="checkbox"
                  class="toggle-input"
                />
                <span class="toggle-slider"></span>
                <span class="toggle-label">
                  {{ product.isAvailable ? "Còn hàng" : "Hết hàng" }}
                </span>
              </label>
            </div>

            <!-- Form Actions -->
            <div class="form-actions">
              <button type="submit" class="btn btn-primary" :disabled="loading">
                <span class="btn-icon">{{ editing ? "💾" : "➕" }}</span>
                <span class="btn-text">
                  {{
                    loading ? "Đang lưu..." : editing ? "Cập Nhật" : "Thêm Món"
                  }}
                </span>
              </button>

              <button
                v-if="editing"
                type="button"
                class="btn btn-secondary"
                @click="cancelEdit"
              >
                <span class="btn-icon">🚫</span>
                <span class="btn-text">Hủy</span>
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- Right Side - Table/Cards -->
      <div class="content-section">
        <!-- Toolbar -->
        <div class="toolbar">
          <!-- Search -->
          <div class="search-box">
            <span class="search-icon">🔍</span>
            <input
              v-model="searchQuery"
              type="text"
              class="search-input"
              placeholder="Tìm kiếm món ăn..."
            />
            <button
              v-if="searchQuery"
              class="clear-search"
              @click="searchQuery = ''"
            >
              ✕
            </button>
          </div>

          <!-- Filters -->
          <div class="filters">
            <!-- Category Filter -->
            <select v-model="categoryFilter" class="filter-select">
              <option value="">Tất cả danh mục</option>
              <option
                v-for="cat in categories"
                :key="cat.categoryId"
                :value="cat.categoryId"
              >
                {{ cat.categoryName }}
              </option>
            </select>

            <!-- Status Filter -->
            <select v-model="statusFilter" class="filter-select">
              <option value="">Tất cả trạng thái</option>
              <option value="available">Còn hàng</option>
              <option value="unavailable">Hết hàng</option>
            </select>
          </div>

          <!-- Actions -->
          <div class="toolbar-actions">
            <!-- View Toggle -->
            <div class="view-toggle">
              <button
                @click="viewMode = 'cards'"
                :class="{ active: viewMode === 'cards' }"
                title="Card View"
              >
                ▦
              </button>
              <button
                @click="viewMode = 'table'"
                :class="{ active: viewMode === 'table' }"
                title="Table View"
              >
                ☰
              </button>
            </div>

            <button class="btn-refresh" @click="loadData" :disabled="loading">
              <span :class="{ rotating: loading }">🔄</span>
            </button>

            <button class="btn-export" @click="exportData">
              📥 Xuất Excel
            </button>
          </div>
        </div>

        <!-- Loading State -->
        <div v-if="loading" class="loading-container">
          <div class="spinner"></div>
          <p>Đang tải dữ liệu...</p>
        </div>

        <!-- Empty State -->
        <div v-else-if="filteredProducts.length === 0" class="empty-state">
          <div class="empty-icon">🍽️</div>
          <h3>Không Tìm Thấy Món Ăn</h3>
          <p>Thử thay đổi bộ lọc hoặc thêm món ăn mới</p>
        </div>

        <!-- Content Display -->
        <div v-else>
          <!-- Cards View -->
          <div v-if="viewMode === 'cards'" class="cards-container">
            <div class="products-grid">
              <div
                v-for="product in paginatedProducts"
                :key="product.productId"
                class="product-card"
              >
                <!-- Image -->
                <div class="card-image">
                  <img
                    :src="getImageUrl(product.imageUrl)"
                    :alt="product.productName"
                  />
                  <div class="image-overlay">
                    <button
                      class="overlay-btn"
                      @click="editProduct(product)"
                      title="Sửa"
                    >
                      ✏️
                    </button>
                    <button
                      class="overlay-btn delete"
                      @click="deleteProduct(product.productId)"
                      title="Xóa"
                    >
                      🗑️
                    </button>
                  </div>
                  <div v-if="!product.isAvailable" class="unavailable-badge">
                    Hết Hàng
                  </div>
                </div>

                <!-- Content -->
                <div class="card-content">
                  <div class="card-header-info">
                    <h3 class="card-title">{{ product.productName }}</h3>
                    <span :class="['category-tag', getCategoryClass(product)]">
                      {{ getCategoryName(product) }}
                    </span>
                  </div>

                  <p v-if="product.shortDescription" class="card-description">
                    {{ truncateText(product.shortDescription, 60) }}
                  </p>

                  <div class="card-footer">
                    <span class="card-price">{{
                      formatPrice(product.price)
                    }}</span>

                    <!-- Availability Toggle -->
                    <label class="mini-toggle" @click.stop>
                      <input
                        v-model="product.isAvailable"
                        type="checkbox"
                        @change="toggleAvailability(product)"
                      />
                      <span class="mini-toggle-slider"></span>
                    </label>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Table View -->
          <div v-else class="table-container">
            <table class="products-table">
              <thead>
                <tr>
                  <th class="sortable" @click="sortTable('productName')">
                    <div class="th-content">
                      Tên Món
                      <span class="sort-icon">{{
                        getSortIcon("productName")
                      }}</span>
                    </div>
                  </th>
                  <th class="sortable text-right" @click="sortTable('price')">
                    <div class="th-content">
                      Giá Tiền
                      <span class="sort-icon">{{ getSortIcon("price") }}</span>
                    </div>
                  </th>
                  <th>Danh Mục</th>
                  <th class="text-center">Trạng Thái</th>
                  <th class="text-center">Thao Tác</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="product in paginatedProducts"
                  :key="product.productId"
                  class="table-row"
                >
                  <td>
                    <div class="product-cell">
                      <img
                        :src="getImageUrl(product.imageUrl)"
                        :alt="product.productName"
                        class="product-thumbnail"
                      />
                      <div class="product-info">
                        <div class="product-name">
                          {{ product.productName }}
                        </div>
                        <div
                          v-if="product.shortDescription"
                          class="product-desc"
                        >
                          {{ truncateText(product.shortDescription, 50) }}
                        </div>
                      </div>
                    </div>
                  </td>

                  <td class="text-right">
                    <span class="price-value">{{
                      formatPrice(product.price)
                    }}</span>
                  </td>

                  <td>
                    <span
                      :class="['category-badge', getCategoryClass(product)]"
                    >
                      {{ getCategoryName(product) }}
                    </span>
                  </td>

                  <td class="text-center">
                    <label class="table-toggle" @click.stop>
                      <input
                        v-model="product.isAvailable"
                        type="checkbox"
                        @change="toggleAvailability(product)"
                      />
                      <span class="table-toggle-slider"></span>
                      <span class="table-toggle-label">
                        {{ product.isAvailable ? "Còn hàng" : "Hết hàng" }}
                      </span>
                    </label>
                  </td>

                  <td class="text-center">
                    <div class="table-actions">
                      <button
                        class="action-btn edit"
                        @click="editProduct(product)"
                        title="Sửa"
                      >
                        ✏️ Sửa
                      </button>
                      <button
                        class="action-btn delete"
                        @click="deleteProduct(product.productId)"
                        title="Xóa"
                      >
                        🗑️ Xóa
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Pagination -->
          <div v-if="totalPages > 1" class="pagination">
            <button
              @click="currentPage--"
              :disabled="currentPage === 0"
              class="page-btn"
            >
              ← Trước
            </button>

            <div class="page-numbers">
              <button
                v-for="page in visiblePages"
                :key="page"
                @click="currentPage = page - 1"
                :class="['page-number', { active: currentPage === page - 1 }]"
              >
                {{ page }}
              </button>
            </div>

            <button
              @click="currentPage++"
              :disabled="currentPage >= totalPages - 1"
              class="page-btn"
            >
              Sau →
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/axios";
import Swal from "sweetalert2";

export default {
  name: "MenuManagement",

  data() {
    return {
      products: [],
      categories: [],
      loading: false,
      errorMsg: "",

      // View & Filters
      viewMode: "cards", // 'cards' or 'table'
      searchQuery: "",
      categoryFilter: "",
      statusFilter: "",

      // Pagination
      currentPage: 0,
      pageSize: 12,

      // Sorting
      sortField: null,
      sortDirection: "asc",

      // Form
      editing: false,
      product: {
        productId: null,
        productName: "",
        price: "",
        categoryId: "",
        imageUrl: "",
        description: "",
        shortDescription: "",
        isAvailable: true,
      },

      // Image
      previewUrl: null,
      selectedFile: null,
    };
  },

  computed: {
    availableCount() {
      return this.products.filter((p) => p.isAvailable !== false).length;
    },

    unavailableCount() {
      return this.products.filter((p) => p.isAvailable === false).length;
    },

    filteredProducts() {
      let filtered = [...this.products];

      // Search
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        filtered = filtered.filter(
          (p) =>
            (p.productName || "").toLowerCase().includes(query) ||
            (p.shortDescription || "").toLowerCase().includes(query) ||
            (p.description || "").toLowerCase().includes(query)
        );
      }

      // Category Filter
      if (this.categoryFilter) {
        filtered = filtered.filter(
          (p) =>
            (p.categoryId || p.category?.categoryId) === this.categoryFilter
        );
      }

      // Status Filter
      if (this.statusFilter === "available") {
        filtered = filtered.filter((p) => p.isAvailable !== false);
      } else if (this.statusFilter === "unavailable") {
        filtered = filtered.filter((p) => p.isAvailable === false);
      }

      // Sort
      if (this.sortField) {
        filtered.sort((a, b) => {
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

      return filtered;
    },

    paginatedProducts() {
      const start = this.currentPage * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredProducts.slice(start, end);
    },

    totalPages() {
      return Math.ceil(this.filteredProducts.length / this.pageSize);
    },

    visiblePages() {
      const pages = [];
      const total = this.totalPages;
      const current = this.currentPage + 1;

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
    this.loadData();
  },

  methods: {
    async loadData() {
      this.loading = true;
      this.errorMsg = "";

      try {
        await Promise.all([this.loadProducts(), this.loadCategories()]);
      } catch (e) {
        console.error("Error loading data:", e);
        this.errorMsg = "Không thể tải dữ liệu!";
      } finally {
        this.loading = false;
      }
    },

    async loadProducts() {
      try {
        const res = await api.get("/products", {
          params: { includeUnavailable: true, page: 0, size: 200 },
        });
        this.products = res.data?.data?.content || res.data?.data || [];
      } catch (e) {
        console.error("Error loading products:", e);
        throw e;
      }
    },

    async loadCategories() {
      try {
        const res = await api.get("/categories");
        this.categories = res.data?.data || [];
      } catch (e) {
        console.error("Error loading categories:", e);
        throw e;
      }
    },

    handleFileChange(event) {
      const file = event.target.files?.[0];
      if (!file) return;

      if (file.size > 5 * 1024 * 1024) {
        Swal.fire({
          icon: "error",
          title: "File quá lớn!",
          text: "Vui lòng chọn ảnh dưới 5MB",
        });
        return;
      }

      if (!file.type.startsWith("image/")) {
        Swal.fire({
          icon: "error",
          title: "File không hợp lệ!",
          text: "Vui lòng chọn file ảnh",
        });
        return;
      }

      this.selectedFile = file;

      const reader = new FileReader();
      reader.onload = (e) => {
        this.previewUrl = e.target?.result || null;
      };
      reader.readAsDataURL(file);
    },

    removeImage() {
      this.previewUrl = null;
      this.selectedFile = null;
      this.product.imageUrl = "";
      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = "";
      }
    },

    async saveProduct() {
      if (!this.product.productName?.trim()) {
        Swal.fire({
          icon: "warning",
          title: "Thiếu thông tin!",
          text: "Vui lòng nhập tên món ăn",
        });
        return;
      }

      if (!this.product.price || this.product.price < 0) {
        Swal.fire({
          icon: "warning",
          title: "Thiếu thông tin!",
          text: "Vui lòng nhập giá hợp lệ",
        });
        return;
      }

      if (!this.product.categoryId) {
        Swal.fire({
          icon: "warning",
          title: "Thiếu thông tin!",
          text: "Vui lòng chọn danh mục",
        });
        return;
      }

      this.loading = true;
      this.errorMsg = "";

      try {
        let imageUrl = this.product.imageUrl;

        if (this.selectedFile) {
          const uploadRes = await this.uploadImage(this.selectedFile);
          imageUrl =
            uploadRes.path ||
            uploadRes.url ||
            uploadRes.filename ||
            uploadRes ||
            "";
        }

        const payload = {
          productName: this.product.productName.trim(),
          price: Number(this.product.price),
          categoryId: Number(this.product.categoryId),
          imageUrl: imageUrl || "",
          description: this.product.description?.trim() || "",
          shortDescription: this.product.shortDescription?.trim() || "",
          isAvailable: this.product.isAvailable !== false,
        };

        if (this.editing && this.product.productId) {
          await api.put(`/products/${this.product.productId}`, payload);
          await Swal.fire({
            icon: "success",
            title: "Thành công!",
            text: "Món ăn đã được cập nhật",
            timer: 1500,
            showConfirmButton: false,
          });
        } else {
          await api.post("/products", payload);
          await Swal.fire({
            icon: "success",
            title: "Thành công!",
            text: "Món ăn đã được thêm vào menu",
            timer: 1500,
            showConfirmButton: false,
          });
        }

        await this.loadProducts();
        this.resetForm();
      } catch (e) {
        console.error("Error saving product:", e);
        this.errorMsg = `Lỗi: ${e.response?.data?.message || e.message}`;
        await Swal.fire({
          icon: "error",
          title: "Lỗi!",
          text: this.errorMsg,
        });
      } finally {
        this.loading = false;
      }
    },

    async uploadImage(file) {
      const formData = new FormData();
      formData.append("file", file);

      try {
        const res = await api.post("/upload", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        return res.data?.data || res.data || {};
      } catch (e) {
        console.error("Error uploading image:", e);
        throw new Error("Không thể upload ảnh!");
      }
    },

    editProduct(product) {
      this.editing = true;

      this.product = {
        productId: product.productId,
        productName: product.productName || "",
        price: product.price || "",
        categoryId: product.categoryId || product.category?.categoryId || "",
        imageUrl: product.imageUrl || "",
        description: product.description || "",
        shortDescription: product.shortDescription || "",
        isAvailable: product.isAvailable !== false,
      };

      if (product.imageUrl) {
        this.previewUrl = this.getImageUrl(product.imageUrl);
      }

      window.scrollTo({ top: 0, behavior: "smooth" });
    },

    async deleteProduct(id) {
      const result = await Swal.fire({
        title: "Xóa món ăn?",
        text: "Thao tác này không thể hoàn tác",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#ef4444",
        cancelButtonColor: "#6b7280",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
      });

      if (!result.isConfirmed) return;

      this.loading = true;

      try {
        await api.delete(`/products/${id}`);
        await this.loadProducts();
        await Swal.fire({
          icon: "success",
          title: "Đã xóa!",
          text: "Món ăn đã được xóa",
          timer: 1500,
          showConfirmButton: false,
        });
      } catch (e) {
        console.error("Error deleting product:", e);
        await Swal.fire({
          icon: "error",
          title: "Lỗi!",
          text: e.response?.data?.message || "Không thể xóa món ăn",
        });
      } finally {
        this.loading = false;
      }
    },

    async toggleAvailability(product) {
      const newStatus = !product.isAvailable;

      try {
        const payload = {
          productName: product.productName,
          price: Number(product.price),
          categoryId: Number(
            product.categoryId || product.category?.categoryId
          ),
          imageUrl: product.imageUrl || "",
          description: product.description || "",
          shortDescription: product.shortDescription || "",
          isAvailable: newStatus,
        };

        await api.put(`/products/${product.productId}`, payload);

        const idx = this.products.findIndex(
          (p) => p.productId === product.productId
        );
        if (idx !== -1) {
          this.products[idx].isAvailable = newStatus;
        }
      } catch (e) {
        console.error("Error toggling availability:", e);
        product.isAvailable = !newStatus; // Revert
        await Swal.fire({
          icon: "error",
          title: "Lỗi!",
          text: "Không thể cập nhật trạng thái",
        });
      }
    },

    cancelEdit() {
      Swal.fire({
        title: "Hủy chỉnh sửa?",
        text: "Các thay đổi chưa lưu sẽ bị bỏ qua",
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Hủy",
        cancelButtonText: "Tiếp tục",
      }).then((res) => {
        if (res.isConfirmed) {
          this.resetForm();
        }
      });
    },

    resetForm() {
      this.editing = false;
      this.previewUrl = null;
      this.selectedFile = null;

      this.product = {
        productId: null,
        productName: "",
        price: "",
        categoryId: "",
        imageUrl: "",
        description: "",
        shortDescription: "",
        isAvailable: true,
      };

      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = "";
      }
    },

    sortTable(field) {
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

    exportData() {
      const csv = this.generateCSV();
      const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = `menu-${new Date().toISOString().split("T")[0]}.csv`;
      link.click();
      Swal.fire({
        icon: "success",
        title: "Đã xuất!",
        text: "File CSV đã được tải xuống",
        timer: 1500,
        showConfirmButton: false,
      });
    },

    generateCSV() {
      const headers = ["Tên Món", "Giá", "Danh Mục", "Trạng Thái"];
      const rows = this.products.map((p) => [
        p.productName,
        p.price,
        this.getCategoryName(p),
        p.isAvailable !== false ? "Còn hàng" : "Hết hàng",
      ]);
      return [headers, ...rows].map((row) => row.join(",")).join("\n");
    },

    getImageUrl(url) {
      if (!url) return "/images/no-image.png";

      const backendUrl = "http://localhost:3000";

      if (url.startsWith("http")) return url;
      if (url.startsWith("/uploads")) return `${backendUrl}${url}`;
      return `${backendUrl}/uploads/${url}`;
    },

    getCategoryName(product) {
      if (product.category?.categoryName) return product.category.categoryName;

      const catId = product.categoryId || product.category?.categoryId;
      const found = this.categories.find((c) => c.categoryId === catId);
      if (found) return found.categoryName;

      const map = {
        1: "Món chính",
        2: "Món phụ",
        3: "Đồ uống",
        4: "Tráng miệng",
        5: "Topping",
      };
      return map[catId] || "Khác";
    },

    getCategoryClass(product) {
      const catId = product.categoryId || product.category?.categoryId;
      return `cat-${catId || 0}`;
    },

    formatPrice(value) {
      return Number(value).toLocaleString("vi-VN") + "₫";
    },

    truncateText(text, maxLength) {
      if (!text) return "";
      if (text.length <= maxLength) return text;
      return text.substring(0, maxLength) + "...";
    },
  },
};
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.menu-management {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e9ecef 100%);
  padding: 30px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
    "Helvetica Neue", Arial, sans-serif;
}

/* ============ HEADER ============ */
.page-header {
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

.quick-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-top: 24px;
}

.stat-item {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.stat-item:hover {
  transform: translateY(-4px);
  background: rgba(255, 255, 255, 0.25);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.stat-icon {
  font-size: 36px;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-value {
  display: block;
  font-size: 28px;
  font-weight: 800;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  display: block;
  font-size: 13px;
  opacity: 0.9;
  font-weight: 500;
}

/* ============ ALERTS ============ */
.alert {
  padding: 16px 24px;
  border-radius: 12px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.alert-error {
  background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
  color: #991b1b;
  border-left: 4px solid #dc2626;
}

.alert-icon {
  font-size: 24px;
  flex-shrink: 0;
}

.alert-message {
  flex: 1;
  font-weight: 600;
}

.alert-close {
  background: #dc2626;
  color: white;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 18px;
  transition: all 0.2s;
}

.alert-close:hover {
  transform: scale(1.1);
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}

.slide-fade-enter-from {
  transform: translateY(-20px);
  opacity: 0;
}

.slide-fade-leave-to {
  transform: translateY(-20px);
  opacity: 0;
}

/* ============ MAIN LAYOUT ============ */
.main-layout {
  display: grid;
  grid-template-columns: 450px 1fr;
  gap: 30px;
  align-items: start;
}

/* ============ FORM SECTION ============ */
.form-section {
  position: sticky;
  top: 30px;
}

.form-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.form-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.form-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 8px;
}

.editing-badge {
  background: rgba(255, 255, 255, 0.25);
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.form-body {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
}

.label-text {
  font-size: 14px;
  font-weight: 700;
  color: #374151;
}

.required {
  color: #ef4444;
  font-weight: 700;
}

.optional {
  font-size: 12px;
  color: #9ca3af;
  font-weight: 500;
}

.form-input,
.form-select,
.form-textarea {
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
.form-select:focus,
.form-textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.input-with-addon {
  position: relative;
  display: flex;
}

.input-with-addon .form-input {
  padding-right: 60px;
}

.input-addon {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.char-counter {
  text-align: right;
  font-size: 11px;
  color: #9ca3af;
  margin-top: 4px;
}

/* Image Upload */
.image-preview {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 12px;
}

.preview-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.btn-remove-image {
  width: 100%;
  padding: 10px;
  background: #fee2e2;
  color: #dc2626;
  border: none;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-remove-image:hover {
  background: #fecaca;
}

.image-upload {
  text-align: center;
  padding: 40px 20px;
  border: 2px dashed #cbd5e1;
  border-radius: 12px;
  background: #f8fafc;
  transition: all 0.3s;
}

.image-upload:hover {
  border-color: #667eea;
  background: #f1f5f9;
}

.btn-upload {
  padding: 12px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn-upload:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.upload-hint {
  margin: 12px 0 0 0;
  font-size: 12px;
  color: #9ca3af;
}

/* Toggle */
.toggle-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  cursor: pointer;
  user-select: none;
  transition: all 0.3s;
}

.toggle-wrapper:hover {
  background: #f1f5f9;
}

.toggle-input {
  display: none;
}

.toggle-slider {
  position: relative;
  width: 52px;
  height: 28px;
  background: #cbd5e1;
  border-radius: 14px;
  transition: all 0.3s;
  flex-shrink: 0;
}

.toggle-slider::after {
  content: "";
  position: absolute;
  top: 4px;
  left: 4px;
  width: 20px;
  height: 20px;
  background: white;
  border-radius: 50%;
  transition: all 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.toggle-input:checked + .toggle-slider {
  background: #10b981;
}

.toggle-input:checked + .toggle-slider::after {
  transform: translateX(24px);
}

.toggle-label {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

/* Form Actions */
.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.btn {
  flex: 1;
  padding: 14px 20px;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-primary {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(16, 185, 129, 0.5);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-secondary {
  background: #f1f5f9;
  color: #475569;
  border: 2px solid #e2e8f0;
}

.btn-secondary:hover {
  background: #e2e8f0;
}

/* ============ CONTENT SECTION ============ */
.content-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Toolbar */
.toolbar {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  align-items: center;
}

.search-box {
  position: relative;
  flex: 1;
  min-width: 250px;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 16px;
  font-size: 18px;
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

.clear-search {
  position: absolute;
  right: 12px;
  background: #e2e8f0;
  border: none;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.clear-search:hover {
  background: #cbd5e1;
}

.filters {
  display: flex;
  gap: 12px;
}

.filter-select {
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  background: white;
  cursor: pointer;
  transition: all 0.3s;
  min-width: 160px;
}

.filter-select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.toolbar-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.view-toggle {
  display: flex;
  background: #f1f5f9;
  border-radius: 10px;
  padding: 4px;
  gap: 4px;
}

.view-toggle button {
  padding: 10px 16px;
  border: none;
  background: transparent;
  border-radius: 8px;
  cursor: pointer;
  font-size: 18px;
  transition: all 0.3s;
  color: #64748b;
}

.view-toggle button.active {
  background: white;
  color: #667eea;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.btn-refresh,
.btn-export {
  padding: 12px 20px;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-refresh {
  background: #667eea;
  color: white;
  font-size: 20px;
  padding: 12px 16px;
}

.btn-refresh:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
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

.btn-export {
  background: #10b981;
  color: white;
}

.btn-export:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
}

/* ============ LOADING & EMPTY ============ */
.loading-container,
.empty-state {
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

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-state h3 {
  margin: 0 0 8px 0;
  color: #1e293b;
  font-size: 20px;
}

.empty-state p {
  margin: 0;
  color: #64748b;
  font-size: 15px;
}

/* ============ CARDS VIEW ============ */
.cards-container {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.product-card {
  background: white;
  border: 2px solid #f1f5f9;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border-color: #e2e8f0;
}

.card-image {
  position: relative;
  height: 200px;
  overflow: hidden;
  background: #f8fafc;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: all 0.3s;
}

.product-card:hover .card-image img {
  transform: scale(1.05);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  opacity: 0;
  transition: all 0.3s;
}

.product-card:hover .image-overlay {
  opacity: 1;
}

.overlay-btn {
  padding: 10px 20px;
  background: white;
  color: #1e293b;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.overlay-btn:hover {
  transform: scale(1.05);
}

.overlay-btn.delete {
  background: #ef4444;
  color: white;
}

.unavailable-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: rgba(239, 68, 68, 0.95);
  color: white;
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 700;
}

.card-content {
  padding: 16px;
}

.card-header-info {
  display: flex;
  justify-content: space-between;
  align-items: start;
  gap: 12px;
  margin-bottom: 12px;
}

.card-title {
  margin: 0;
  font-size: 16px;
  font-weight: 700;
  color: #1e293b;
  line-height: 1.3;
  flex: 1;
}

.category-tag {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: 700;
  white-space: nowrap;
  flex-shrink: 0;
}

.category-tag.cat-1 {
  background: #dbeafe;
  color: #1e40af;
}
.category-tag.cat-2 {
  background: #d1fae5;
  color: #065f46;
}
.category-tag.cat-3 {
  background: #fef3c7;
  color: #92400e;
}
.category-tag.cat-4 {
  background: #fce7f3;
  color: #9f1239;
}
.category-tag.cat-5 {
  background: #e0e7ff;
  color: #3730a3;
}

.card-description {
  margin: 0 0 16px 0;
  font-size: 13px;
  color: #64748b;
  line-height: 1.5;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f1f5f9;
}

.card-price {
  font-size: 18px;
  font-weight: 800;
  color: #10b981;
}

/* Mini Toggle */
.mini-toggle {
  position: relative;
  display: inline-block;
  width: 44px;
  height: 24px;
  cursor: pointer;
}

.mini-toggle input {
  display: none;
}

.mini-toggle-slider {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #cbd5e1;
  border-radius: 12px;
  transition: all 0.3s;
}

.mini-toggle-slider::after {
  content: "";
  position: absolute;
  top: 3px;
  left: 3px;
  width: 18px;
  height: 18px;
  background: white;
  border-radius: 50%;
  transition: all 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.mini-toggle input:checked + .mini-toggle-slider {
  background: #10b981;
}

.mini-toggle input:checked + .mini-toggle-slider::after {
  transform: translateX(20px);
}

/* ============ TABLE VIEW ============ */
.table-container {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
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

.table-row {
  transition: background 0.2s;
}

.table-row:hover {
  background: #f8fafc;
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 16px;
}

.product-thumbnail {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 10px;
  border: 2px solid #f1f5f9;
  flex-shrink: 0;
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 15px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 4px;
}

.product-desc {
  font-size: 12px;
  color: #64748b;
}

.price-value {
  font-size: 16px;
  font-weight: 700;
  color: #10b981;
}

.category-badge {
  display: inline-block;
  padding: 6px 14px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 700;
}

.category-badge.cat-1 {
  background: #dbeafe;
  color: #1e40af;
}
.category-badge.cat-2 {
  background: #d1fae5;
  color: #065f46;
}
.category-badge.cat-3 {
  background: #fef3c7;
  color: #92400e;
}
.category-badge.cat-4 {
  background: #fce7f3;
  color: #9f1239;
}
.category-badge.cat-5 {
  background: #e0e7ff;
  color: #3730a3;
}

/* Table Toggle */
.table-toggle {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  user-select: none;
}

.table-toggle input {
  display: none;
}

.table-toggle-slider {
  position: relative;
  width: 44px;
  height: 24px;
  background: #cbd5e1;
  border-radius: 12px;
  transition: all 0.3s;
  flex-shrink: 0;
}

.table-toggle-slider::after {
  content: "";
  position: absolute;
  top: 3px;
  left: 3px;
  width: 18px;
  height: 18px;
  background: white;
  border-radius: 50%;
  transition: all 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.table-toggle input:checked + .table-toggle-slider {
  background: #10b981;
}

.table-toggle input:checked + .table-toggle-slider::after {
  transform: translateX(20px);
}

.table-toggle-label {
  font-size: 13px;
  font-weight: 600;
  color: #475569;
}

/* Table Actions */
.table-actions {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.action-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
}

.action-btn.edit {
  background: #dbeafe;
  color: #1e40af;
}

.action-btn.edit:hover {
  background: #bfdbfe;
  transform: translateY(-2px);
}

.action-btn.delete {
  background: #fee2e2;
  color: #dc2626;
}

.action-btn.delete:hover {
  background: #fecaca;
  transform: translateY(-2px);
}

/* ============ PAGINATION ============ */
.pagination {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
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
@media (max-width: 1200px) {
  .main-layout {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .form-section {
    position: static;
  }
}

@media (max-width: 768px) {
  .menu-management {
    padding: 16px;
  }

  .title-section h1 {
    font-size: 24px;
  }

  .quick-stats {
    grid-template-columns: repeat(2, 1fr);
  }

  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .search-box {
    width: 100%;
  }

  .filters {
    flex-direction: column;
  }

  .filter-select {
    width: 100%;
  }

  .toolbar-actions {
    flex-direction: column;
  }

  .btn-export {
    width: 100%;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .products-grid {
    grid-template-columns: 1fr;
  }

  .table-container {
    overflow-x: auto;
  }

  .products-table {
    min-width: 800px;
  }
}

@media (max-width: 480px) {
  .quick-stats {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column;
  }
}
</style>
