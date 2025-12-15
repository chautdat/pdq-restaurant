<template>
  <div class="category-page">
    <!-- Error Banner -->
    <div v-if="errorMsg" class="error-banner">
      <strong>⚠️ Lỗi:</strong> {{ errorMsg }}
      <button @click="errorMsg = ''" class="close-error">✕</button>
    </div>

    <!-- Success Banner -->
    <div v-if="successMsg" class="success-banner">
      <strong>✅ Thành công:</strong> {{ successMsg }}
      <button @click="successMsg = ''" class="close-success">✕</button>
    </div>

    <!-- Header Section -->
    <div class="header-section">
      <div class="header-content">
        <h2 class="page-title">🏷️ Category Management</h2>
        <p class="page-subtitle">Manage product categories</p>
      </div>
      <button class="btn-add" @click="openAddModal">
        <i class="fas fa-plus"></i>
        Add Category
      </button>
    </div>

    <!-- Categories Grid -->
    <div class="categories-grid">
      <div v-if="loading" class="loading-state">
        <i class="fas fa-spinner fa-spin"></i>
        <p>Đang tải danh mục...</p>
      </div>

      <div v-else-if="!categories.length" class="empty-state">
        <i class="fas fa-folder-open"></i>
        <p>Chưa có danh mục nào</p>
        <button class="btn-add" @click="openAddModal">
          <i class="fas fa-plus"></i>
          Tạo danh mục đầu tiên
        </button>
      </div>

      <div
        v-else
        v-for="category in categories"
        :key="category.categoryId"
        class="category-card"
      >
        <div class="category-icon">
          <span class="list-icon" aria-hidden="true"></span>
        </div>
        <div class="category-info">
          <h3>{{ category.categoryName }}</h3>
          <p class="category-desc" v-if="category.description">
            {{ category.description }}
          </p>
          <div class="category-meta">
            <span class="item-count">
              <i class="fas fa-utensils"></i>
              {{ category.itemCount || 0 }} món
            </span>
          </div>
        </div>
        <div class="category-actions">
          <button
            class="btn-action btn-edit"
            @click="openEditModal(category)"
            title="Edit"
          >
            <i class="fas fa-edit"></i>
          </button>
          <button
            class="btn-action btn-delete"
            @click="deleteCategory(category)"
            title="Delete"
          >
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- Add/Edit Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>
            {{ isEditMode ? "✏️ Edit Category" : "➕ Add Category" }}
          </h3>
          <button class="modal-close" @click="closeModal">✕</button>
        </div>

        <div class="modal-body">
          <div class="form-group">
            <label for="categoryName">Category Name *</label>
            <input
              id="categoryName"
              type="text"
              v-model="formData.categoryName"
              placeholder="e.g. Món Chính, Tráng Miệng..."
              maxlength="100"
            />
          </div>

          <div class="form-group">
            <label for="description">Description</label>
            <textarea
              id="description"
              v-model="formData.description"
              placeholder="Mô tả ngắn về danh mục..."
              rows="3"
              maxlength="500"
            ></textarea>
          </div>

          <div class="form-group">
            <label>Icon Preview</label>
            <div class="icon-preview">
              <span class="list-icon" aria-hidden="true"></span>
              <span>{{ formData.categoryName || "Category Name" }}</span>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button class="btn btn-cancel" @click="closeModal">Cancel</button>
          <button
            class="btn btn-save"
            @click="saveCategory"
            :disabled="!formData.categoryName"
          >
            {{ isEditMode ? "Update" : "Create" }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import storage from "@/utils/storage";
import api from "@/axios";
import Swal from "sweetalert2";

export default {
  name: "CategoryManagement",

  data() {
    return {
      categories: [],
      loading: false,
      errorMsg: "",
      successMsg: "",
      showModal: false,
      isEditMode: false,
      formData: {
        categoryId: null,
        categoryName: "",
        description: "",
      },
    };
  },

  mounted() {
    this.fetchCategories();
  },

  methods: {
    // ================== FETCH CATEGORIES ==================
    async fetchCategories() {
      try {
        this.loading = true;
        this.errorMsg = "";

        const token = storage.getToken();
        if (!token) {
          this.$router.push("/login");
          return;
        }

        console.log("📡 Fetching categories...");

        const res = await api.get("/categories", {
          headers: { Authorization: `Bearer ${token}` },
        });

        console.log("✅ Categories response:", res.data);

        // Handle different response structures
        if (res.data?.success && res.data.data) {
          this.categories = res.data.data;
        } else if (Array.isArray(res.data)) {
          this.categories = res.data;
        } else if (res.data?.data && Array.isArray(res.data.data)) {
          this.categories = res.data.data;
        } else {
          this.categories = [];
        }

        console.log(`✅ Loaded ${this.categories.length} categories`);
      } catch (error) {
        console.error("❌ Error fetching categories:", error);
        this.errorMsg =
          error.response?.data?.message || "Không thể tải danh sách danh mục";

        if (error.response?.status === 401 || error.response?.status === 403) {
          storage.clearAll();
          this.$router.push("/login");
        }
      } finally {
        this.loading = false;
      }
    },

    // ================== OPEN MODALS ==================
    openAddModal() {
      this.isEditMode = false;
      this.formData = {
        categoryId: null,
        categoryName: "",
        description: "",
      };
      this.showModal = true;
    },

    openEditModal(category) {
      this.isEditMode = true;
      this.formData = {
        categoryId: category.categoryId,
        categoryName: category.categoryName,
        description: category.description || "",
      };
      this.showModal = true;
    },

    closeModal() {
      this.showModal = false;
      this.formData = {
        categoryId: null,
        categoryName: "",
        description: "",
      };
    },

    // ================== SAVE CATEGORY ==================
    async saveCategory() {
      if (!this.formData.categoryName.trim()) {
        this.errorMsg = "Tên danh mục không được để trống!";
        return;
      }

      try {
        const token = storage.getToken();
        if (!token) {
          this.$router.push("/login");
          return;
        }

        const payload = {
          categoryName: this.formData.categoryName.trim(),
          description: this.formData.description.trim() || null,
        };

        console.log("💾 Saving category:", payload);

        if (this.isEditMode) {
          // UPDATE
          await api.put(`/categories/${this.formData.categoryId}`, payload, {
            headers: { Authorization: `Bearer ${token}` },
          });
          this.successMsg = "✅ Cập nhật danh mục thành công!";
        } else {
          // CREATE
          await api.post("/categories", payload, {
            headers: { Authorization: `Bearer ${token}` },
          });
          this.successMsg = "✅ Tạo danh mục thành công!";
        }

        this.closeModal();
        this.fetchCategories();

        // Auto-hide success message after 3s
        setTimeout(() => {
          this.successMsg = "";
        }, 3000);
      } catch (error) {
        console.error("❌ Error saving category:", error);
        this.errorMsg =
          error.response?.data?.message || "Không thể lưu danh mục";
      }
    },

    // ================== DELETE CATEGORY ==================
    async deleteCategory(category) {
      const result = await Swal.fire({
        title: "Xóa danh mục?",
        text: `Xác nhận xóa danh mục "${category.categoryName}"?\nLưu ý: Các món trong danh mục này sẽ không bị xóa.`,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#ef4444",
        cancelButtonColor: "#6b7280",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
      });
      if (!result.isConfirmed) return;

      try {
        const token = storage.getToken();
        if (!token) {
          this.$router.push("/login");
          return;
        }

        console.log("🗑️ Deleting category:", category.categoryId);

        await api.delete(`/categories/${category.categoryId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        this.successMsg = "✅ Xóa danh mục thành công!";
        this.fetchCategories();

        setTimeout(() => {
          this.successMsg = "";
        }, 3000);
      } catch (error) {
        console.error("❌ Error deleting category:", error);
        this.errorMsg =
          error.response?.data?.message || "Không thể xóa danh mục";
      }
    },

    // ================== GET CATEGORY ICON ==================
    getCategoryIcon(categoryName) {
      if (!categoryName) return "fas fa-folder";

      const name = categoryName.toLowerCase();

      if (name.includes("món chính") || name.includes("main")) {
        return "fas fa-drumstick-bite";
      } else if (name.includes("món phụ") || name.includes("side")) {
        return "fas fa-egg";
      } else if (name.includes("tráng miệng") || name.includes("dessert")) {
        return "fas fa-ice-cream";
      } else if (name.includes("đồ uống") || name.includes("drink")) {
        return "fas fa-coffee";
      } else if (name.includes("món chay") || name.includes("vegetarian")) {
        return "fas fa-leaf";
      } else if (name.includes("topping")) {
        return "fas fa-plus-circle";
      } else {
        return "fas fa-utensils";
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

.category-page {
  padding: 30px;
  background: linear-gradient(135deg, #f4f7fb 0%, #e1e9f7 100%);
  min-height: 100vh;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

/* Banners */
.error-banner,
.success-banner {
  padding: 16px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  animation: slideDown 0.3s ease;
}

.error-banner {
  background: #fee;
  color: #c33;
  border: 2px solid #fcc;
}

.success-banner {
  background: #efe;
  color: #2a7;
  border: 2px solid #cfc;
}

.close-error,
.close-success {
  background: transparent;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: inherit;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.close-error:hover {
  background: rgba(204, 51, 51, 0.1);
}

.close-success:hover {
  background: rgba(34, 170, 119, 0.1);
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

/* Header */
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-title {
  font-size: 36px;
  font-weight: 700;
  color: #2563eb;
  margin-bottom: 8px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.page-subtitle {
  font-size: 16px;
  color: #3b82f6;
  font-weight: 500;
}

.btn-add {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.25);
}

.btn-add:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(37, 99, 235, 0.3);
}

/* Categories Grid */
.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.loading-state,
.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.loading-state i {
  font-size: 48px;
  color: #2563eb;
  margin-bottom: 16px;
}

.empty-state i {
  font-size: 64px;
  color: #cbd5e1;
  margin-bottom: 16px;
}

.empty-state p {
  font-size: 18px;
  color: #64748b;
  margin-bottom: 24px;
}

/* Category Card */
.category-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.category-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.25);
}

.category-info h3 {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 8px;
}

.category-desc {
  font-size: 14px;
  color: #64748b;
  line-height: 1.5;
  margin-bottom: 12px;
}

.category-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #64748b;
}

.item-count {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: #e8f0ff;
  border-radius: 8px;
  font-weight: 600;
  color: #2563eb;
}

.category-actions {
  display: flex;
  gap: 8px;
  margin-top: auto;
}

.btn-action {
  flex: 1;
  padding: 10px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.btn-edit {
  background: #e0f2fe;
  color: #2563eb;
}

.btn-edit:hover {
  background: #dbeafe;
  transform: scale(1.05);
}

.btn-delete {
  background: #e6eefc;
  color: #dc2626;
}

.btn-delete:hover {
  background: #e6eefc;
  transform: scale(1.05);
}

/* Modal */
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
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal-content {
  background: white;
  border-radius: 16px;
  width: 500px;
  max-width: 90%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    transform: translateY(50px);
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
  border-bottom: 2px solid #f0f0f0;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
  border-radius: 16px 16px 0 0;
}

.modal-header h3 {
  font-size: 22px;
  font-weight: 700;
}

.modal-close {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  font-size: 24px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.modal-close:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: rotate(90deg);
}

.modal-body {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #334155;
  margin-bottom: 8px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
  transition: all 0.2s ease;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
}

.icon-preview {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: linear-gradient(135deg, #f4f7fb 0%, #e1e9f7 100%);
  border-radius: 12px;
  border: 2px dashed #2563eb;
}

.icon-preview i {
  font-size: 32px;
  color: #2563eb;
}

.icon-preview span {
  font-size: 16px;
  font-weight: 600;
  color: #2563eb;
}

.modal-footer {
  padding: 20px 24px;
  border-top: 2px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.btn {
  padding: 10px 24px;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
}

.btn-cancel {
  background: #f1f5f9;
  color: #64748b;
}

.btn-cancel:hover {
  background: #e2e8f0;
}

.btn-save {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: white;
}

.btn-save:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.3);
}

.btn-save:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Responsive */
@media (max-width: 768px) {
  .category-page {
    padding: 20px;
  }

  .page-title {
    font-size: 28px;
  }

  .header-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .categories-grid {
    grid-template-columns: 1fr;
  }
}
</style>
