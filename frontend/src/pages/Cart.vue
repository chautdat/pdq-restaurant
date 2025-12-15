<template>
  <div class="cart-page">
    <div class="cart-header">
      <div class="cart-header-text">
        <span class="eyebrow">Giỏ hàng</span>
        <h1>Sản phẩm chất lượng, giao hàng nhanh chóng</h1>
      </div>
    </div>

    <div class="cart-layout">
      <!-- LEFT: CART ITEMS -->
      <section class="cart-main">
        <div class="card card-items">
          <header class="card-header card-header-items">
            <h2>
              <span class="chip-count">{{ cart.length }}</span>
              <span class="muted">sản phẩm trong giỏ hàng của bạn</span>
            </h2>
          </header>

          <!-- EMPTY STATE -->
          <div v-if="!cart.length" class="card-body empty-state">
            <div class="empty-left">
              <h2>Bạn chưa có sản phẩm nào trong giỏ hàng</h2>
              <p>Hãy khám phá thực đơn và chọn món yêu thích của bạn!</p>
              <router-link to="/menu" class="btn btn-primary">
                <i class="fa fa-shopping-bag"></i>
                <span>Mua sắm ngay</span>
              </router-link>
            </div>
            <div class="empty-right">
              <img src="/images/notfound.png" alt="Empty cart" />
            </div>
          </div>

          <!-- CART ITEMS -->
          <div v-else class="card-body">
            <div class="cart-items-scroll">
              <transition-group name="cart-item" tag="div">
                <article
                  v-for="item in cart"
                  :key="item.cartItemId"
                  class="cart-item"
                >
                  <div class="cart-item-grid">
                    <!-- IMAGE -->
                    <div class="cart-item-image">
                      <div class="image-frame">
                        <img
                          :src="imagePath(item.productImage)"
                          :alt="item.productName"
                          class="image"
                        />
                      </div>
                    </div>

                    <!-- INFO -->
                    <div class="cart-item-info">
                      <h3 class="item-title">{{ item.productName }}</h3>
                      <p v-if="item.notes" class="item-notes">
                        {{ item.notes }}
                      </p>
                      <button
                        class="btn btn-ghost-danger"
                        @click="removeItem(item)"
                        :disabled="isLoading"
                      >
                        <i class="fa fa-trash"></i>
                        <span>Xóa sản phẩm</span>
                      </button>
                    </div>

                    <!-- PRICE -->
                    <div class="cart-item-price">
                      <span class="label">Đơn giá</span>
                      <div class="price">{{ formatVND(item.price) }}đ</div>
                    </div>

                    <!-- QTY -->
                    <div class="cart-item-qty">
                      <span class="label">Số lượng</span>
                      <div class="qty-control">
                        <button
                          class="qty-button"
                          @click="changeQty(item, -1)"
                          :disabled="item.quantity <= 1 || isLoading"
                        >
                          <i class="fa fa-minus"></i>
                        </button>
                        <input
                          type="number"
                          class="qty-input"
                          :value="item.quantity"
                          min="1"
                          max="99"
                          readonly
                        />
                        <button
                          class="qty-button"
                          @click="changeQty(item, 1)"
                          :disabled="item.quantity >= 99 || isLoading"
                        >
                          <i class="fa fa-plus"></i>
                        </button>
                      </div>
                    </div>

                    <!-- TOTAL -->
                    <div class="cart-item-total">
                      <span class="label">Tổng</span>
                      <div class="total">{{ formatVND(item.subtotal) }}đ</div>
                    </div>
                  </div>
                </article>
              </transition-group>
            </div>
          </div>
        </div>

        <!-- ACTIONS -->
        <div class="card card-actions" v-if="cart.length">
          <div class="card-body actions-row">
            <router-link to="/menu" class="btn btn-outline">
              <i class="fa fa-arrow-left"></i>
              <span>Tiếp tục mua sắm</span>
            </router-link>
            <button
              class="btn btn-primary"
              :disabled="!cart.length || isLoading"
              @click="goCheckout()"
            >
              <i class="fa fa-shopping-cart"></i>
              <span>Thanh toán</span>
            </button>
          </div>
        </div>
      </section>

      <!-- RIGHT: SUMMARY + SUPPORT -->
      <aside class="cart-sidebar">
        <!-- SUMMARY -->
        <div class="card summary-card">
          <header class="card-header">
            <h2>
              <i class="fa fa-receipt"></i>
              <span>Tóm tắt giỏ hàng</span>
            </h2>
          </header>
          <div class="card-body summary-body">
            <div class="summary-row">
              <span>Tổng tiền hàng</span>
              <span class="value">
                {{ formatVND(cartSummary.totalPrice) }}đ
              </span>
            </div>

            <div class="summary-row">
              <span>Số lượng</span>
              <span class="value"> {{ cartSummary.totalItems }} món </span>
            </div>

            <div class="summary-row">
              <span>
                <i class="fa fa-truck"></i>
                Phí giao hàng
              </span>
              <span class="value"> {{ formatVND(deliveryFee) }}đ </span>
            </div>

            <div class="summary-divider"></div>

            <div class="summary-row summary-row-total">
              <span>Tổng cộng</span>
              <span class="value-total">
                {{ formatVND(cartSummary.totalPrice + deliveryFee) }}đ
              </span>
            </div>

            <div class="summary-actions">
              <button
                class="btn btn-primary btn-block"
                :disabled="!cart.length || isLoading"
                @click="goCheckout()"
              >
                <i class="fa fa-shopping-cart"></i>
                <span>Thanh toán</span>
              </button>
              <button
                class="btn btn-soft"
                @click="clearCart()"
                :disabled="!cart.length || isLoading"
              >
                <i class="fa fa-times"></i>
                <span>Hủy giỏ hàng</span>
              </button>
            </div>
          </div>
        </div>

        <!-- SUPPORT -->
        <div class="card support-card">
          <header class="card-header">
            <h2>
              <i class="fa fa-headset"></i>
              <span>Hỗ trợ</span>
            </h2>
          </header>
          <div class="card-body support-body">
            <div class="support-icon">
              <i class="fa fa-phone-volume"></i>
            </div>
            <div class="support-text-block">
              <div class="support-number">+84 123 456 789</div>
              <p class="support-text">
                Nếu bạn có thắc mắc, hãy liên hệ với chúng tôi. Luôn sẵn sàng hỗ
                trợ bạn 24/7.
              </p>
            </div>
          </div>
        </div>

        <!-- DELIVERY INFO -->
        <div class="card delivery-card">
          <div class="delivery-row">
            <i class="fa fa-shipping-fast"></i>
            <span>Giao hàng nhanh</span>
          </div>
          <div class="delivery-row">
            <i class="fa fa-shield-alt"></i>
            <span>Thanh toán an toàn</span>
          </div>
          <div class="delivery-row">
            <i class="fa fa-undo"></i>
            <span>Đổi trả dễ dàng</span>
          </div>
        </div>
      </aside>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import api from "@/axios";
import storage from "@/utils/storage";
import Swal from "sweetalert2";

export default {
  name: "Cart",

  data() {
    return {
      cart: [],
      cartSummary: {
        cartId: null,
        totalItems: 0,
        totalPrice: 0,
      },
      isLoading: false,
    };
  },

  computed: {
    ...mapState(["user"]),

    deliveryFee() {
      if (this.cartSummary.totalPrice > 200000) return 0;
      if (this.cart.length === 0) return 0;
      return 15000;
    },
  },

  mounted() {
    this.loadCart();
  },

  methods: {
    dispatchCartUpdated() {
      try {
        window.dispatchEvent(new Event("cart-updated"));
      } catch (e) {
        console.warn("Không thể phát event cart-updated:", e);
      }
    },

    imagePath(src) {
      if (!src) return "/images/notfound.png";
      const backendUrl = "http://localhost:3000";
      if (src.startsWith("http")) return src;
      if (src.startsWith("/uploads")) return `${backendUrl}${src}`;
      return `${backendUrl}/uploads/${src}`;
    },

    formatVND(price) {
      return new Intl.NumberFormat("vi-VN").format(price || 0);
    },

    async loadCart() {
      console.log("🔄 loadCart() called");

      if (!this.user) {
        console.log("❌ No user");
        this.cart = [];
        this.cartSummary = { cartId: null, totalItems: 0, totalPrice: 0 };
        return;
      }

      try {
        this.isLoading = true;
        const token = storage.getToken();

        if (!token) {
          console.log("❌ No token");
          this.cart = [];
          this.cartSummary = { cartId: null, totalItems: 0, totalPrice: 0 };
          return;
        }

        const res = await api.get("/cart", {
          headers: { Authorization: `Bearer ${token}` },
        });

        console.log("✅ Cart API response:", res.data);

        if (res.data.success && res.data.data) {
          const cartData = res.data.data;

          // ✅ CẬP NHẬT cartSummary
          this.cartSummary = {
            cartId: cartData.cartId,
            totalItems: cartData.totalItems || 0,
            totalPrice: cartData.totalPrice || 0,
          };

          // ✅ CẬP NHẬT cart items
          this.cart = cartData.items || [];

          console.log("✅ Cart updated:");
          console.log("   - Items:", this.cart.length);
          console.log("   - Total:", this.cartSummary.totalPrice);

          // ✅ DISPATCH EVENT
          this.dispatchCartUpdated();
        } else {
          console.log("⚠️ Empty cart response");
          this.cart = [];
          this.cartSummary = { cartId: null, totalItems: 0, totalPrice: 0 };
        }
      } catch (err) {
        console.error("❌ Lỗi load cart:", err);
        this.cart = [];
        this.cartSummary = { cartId: null, totalItems: 0, totalPrice: 0 };

        if (err.response?.status === 401 || err.response?.status === 403) {
          storage.clearAuth();
          this.$store.commit("setUser", null);
          this.$router.push("/login");
        }
      } finally {
        this.isLoading = false;
      }
    },

    async changeQty(item, amount) {
      const newQty = item.quantity + amount;
      if (newQty < 1 || newQty > 99) return;

      try {
        this.isLoading = true;
        const token = storage.getToken();
        // Chuẩn hóa ID (BE trả cartItemId)
        const itemId =
          item.cartItemId ||
          item.id ||
          item.itemId ||
          item?.cartItem?.cartItemId ||
          item?.cartItem?.id;

        if (!itemId) {
          await Swal.fire({
            icon: "error",
            title: "Lỗi!",
            text: "Không tìm thấy ID sản phẩm.",
            confirmButtonColor: "#00b067",
          });
          return;
        }

        console.log("🔄 Updating quantity:", itemId, "=>", newQty);

        await api.put(
          `/cart/items/${itemId}`,
          { quantity: newQty },
          { headers: { Authorization: `Bearer ${token}` } }
        );

        console.log("✅ Quantity updated, reloading cart...");

        // ✅ RELOAD CART
        await this.loadCart();
      } catch (err) {
        console.error("❌ Lỗi cập nhật:", err);
        await Swal.fire({
          icon: "error",
          title: "Lỗi!",
          text: err.response?.data?.message || "Không thể cập nhật số lượng!",
          confirmButtonColor: "#e74c3c",
        });
      } finally {
        this.isLoading = false;
      }
    },

    async removeItem(item) {
      console.log("🗑️ removeItem() called");
      console.log("📦 Item:", item);

      const result = await Swal.fire({
        title: "Xóa sản phẩm?",
        text: `Xóa "${item.productName}" khỏi giỏ hàng?`,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#e74c3c",
        cancelButtonColor: "#6b7280",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
      });

      if (!result.isConfirmed) {
        console.log("❌ User cancelled");
        return;
      }

      try {
        this.isLoading = true;
        const token = storage.getToken();
        const itemId = item.cartItemId || item.id || item.itemId;

        console.log("🆔 itemId:", itemId);

        if (!itemId) {
          console.error("❌ Không có itemId!");
          await Swal.fire({
            icon: "error",
            title: "Lỗi!",
            text: "Không tìm thấy ID sản phẩm.",
            confirmButtonColor: "#e74c3c",
          });
          return;
        }

        console.log("🌐 DELETE /api/cart/items/" + itemId);

        // ✅ GỌI API DELETE
        const res = await api.delete(`/cart/items/${itemId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        console.log("✅ Item deleted from backend");

        // ✅ Ưu tiên dùng dữ liệu trả về từ BE nếu có
        const cartData = res?.data?.data;
        if (cartData) {
          this.cartSummary = {
            cartId: cartData.cartId,
            totalItems: cartData.totalItems || 0,
            totalPrice: cartData.totalPrice || 0,
          };
          this.cart = cartData.items || [];
          this.dispatchCartUpdated();
        } else {
          // ✅ XÓA KHỎI MẢNG NGAY LẬP TỨC (fallback)
          const index = this.cart.findIndex(
            (i) => (i.cartItemId || i.id || i.itemId) === itemId
          );
          if (index !== -1) {
            this.cart.splice(index, 1);
          }
          this.cartSummary.totalItems = this.cart.length;
          this.cartSummary.totalPrice = this.cart.reduce(
            (sum, i) => sum + (i.subtotal || i.price * i.quantity),
            0
          );
          this.dispatchCartUpdated();
          await this.loadCart(); // đồng bộ lại
        }

        // ✅ HIỆN THÔNG BÁO
        await Swal.fire({
          icon: "success",
          title: "Đã xóa!",
          text: "Sản phẩm đã được xóa khỏi giỏ hàng",
          showConfirmButton: false,
          timer: 1500,
        });
      } catch (err) {
        console.error("❌ Lỗi xóa:", err);
        console.error("❌ Response:", err.response?.data);

        await Swal.fire({
          icon: "error",
          title: "Lỗi!",
          text: err.response?.data?.message || "Không thể xóa sản phẩm!",
          confirmButtonColor: "#e74c3c",
        });

        // ✅ RELOAD LẠI NẾU CÓ LỖI
        await this.loadCart();
      } finally {
        this.isLoading = false;
      }
    },

    async clearCart() {
      const result = await Swal.fire({
        title: "Xóa giỏ hàng?",
        text: "Bạn chắc chắn muốn xóa toàn bộ giỏ hàng?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#e74c3c",
        cancelButtonColor: "#6b7280",
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy",
      });

      if (!result.isConfirmed) return;

      try {
        this.isLoading = true;
        const token = storage.getToken();

        await api.delete("/cart", {
          headers: { Authorization: `Bearer ${token}` },
        });

        this.cart = [];
        this.cartSummary = { cartId: null, totalItems: 0, totalPrice: 0 };
        this.dispatchCartUpdated();

        await Swal.fire({
          icon: "success",
          title: "Đã xóa!",
          text: "Giỏ hàng đã được xóa",
          showConfirmButton: false,
          timer: 1500,
        });
      } catch (err) {
        console.error("❌ Lỗi xóa giỏ:", err);
        await Swal.fire({
          icon: "error",
          title: "Lỗi!",
          text: "Không thể xóa giỏ hàng!",
          confirmButtonColor: "#e74c3c",
        });
      } finally {
        this.isLoading = false;
      }
    },

    goCheckout() {
      if (!this.cart.length) {
        Swal.fire({
          icon: "info",
          title: "Giỏ hàng trống",
          text: "Vui lòng thêm sản phẩm trước khi thanh toán",
          confirmButtonColor: "#00b067",
        });
        return;
      }
      this.$router.push("/checkout");
    },
  },
};
</script>

<style scoped>
:root {
  --primary: #00b16a;
  --primary-dark: #009459;
  --primary-soft: #e6f7f0;
  --bg-page: #f5f7fb;
  --bg-card: #ffffff;
  --text-main: #111827;
  --text-muted: #6b7280;
  --border-subtle: #e5e7eb;
}

.cart-page {
  min-height: 100vh;
  padding: 32px 6vw 40px;
  background: var(--bg-page);
  font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", "Inter",
    sans-serif;
  color: var(--text-main);
}

.cart-header {
  margin-bottom: 24px;
}

.cart-header-text {
  max-width: 640px;
}

.eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  color: var(--primary-dark);
}

.eyebrow::before {
  content: "";
  width: 28px;
  height: 2px;
  border-radius: 999px;
  background: var(--primary);
}

.cart-header h1 {
  margin-top: 8px;
  font-size: 28px;
  font-weight: 750;
  color: #0b1220;
}

.cart-layout {
  display: grid;
  grid-template-columns: minmax(0, 2.1fr) minmax(320px, 1fr);
  gap: 24px;
  align-items: flex-start;
}

.cart-main {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.cart-sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
  position: sticky;
  top: 24px;
}

.card {
  background: var(--bg-card);
  border-radius: 18px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
  border: 1px solid rgba(148, 163, 184, 0.28);
  overflow: hidden;
}

.card-header {
  padding: 14px 20px;
  border-bottom: 1px solid rgba(226, 232, 240, 0.9);
  background: linear-gradient(
      90deg,
      rgba(0, 177, 106, 0.04),
      rgba(59, 130, 246, 0.02)
    ),
    #ffffff;
}

.card-header h2 {
  margin: 0;
  font-size: 16px;
  font-weight: 650;
  display: flex;
  align-items: center;
  gap: 10px;
  color: #0b1220;
}

.card-header h2 i {
  color: var(--primary);
}

.card-header-items {
  border-bottom: 1px solid rgba(226, 232, 240, 0.9);
}

.card-header-items h2 {
  font-size: 15px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.card-body {
  padding: 18px 20px 20px;
}

.chip-count {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 34px;
  padding: 4px 10px;
  border-radius: 999px;
  background: rgba(0, 177, 106, 0.08);
  color: var(--primary-dark);
  font-size: 13px;
  font-weight: 650;
}

.muted {
  color: var(--text-muted);
}

.empty-state {
  display: grid;
  grid-template-columns: minmax(0, 1.5fr) minmax(0, 1fr);
  gap: 24px;
  align-items: center;
  padding: 24px 20px;
}

.empty-left h2 {
  font-size: 20px;
  font-weight: 650;
  margin-bottom: 6px;
}

.empty-left p {
  color: var(--text-muted);
  margin-bottom: 16px;
}

.empty-right {
  display: flex;
  justify-content: center;
}

.empty-right img {
  max-width: 220px;
  opacity: 0.95;
}

.cart-items-scroll {
  max-height: 580px;
  overflow-y: auto;
  padding-right: 6px;
}

.cart-items-scroll::-webkit-scrollbar {
  width: 6px;
}

.cart-items-scroll::-webkit-scrollbar-track {
  background: rgba(243, 244, 246, 0.9);
  border-radius: 999px;
}

.cart-items-scroll::-webkit-scrollbar-thumb {
  background: rgba(0, 177, 106, 0.6);
  border-radius: 999px;
}

.cart-item {
  margin-bottom: 10px;
}

.cart-item:last-child {
  margin-bottom: 0;
}

.cart-item-grid {
  display: grid;
  grid-template-columns: 96px minmax(0, 1.9fr) 120px 140px 130px;
  gap: 16px;
  padding: 12px 14px;
  border-radius: 14px;
  background: #f9fafb;
  border: 1px solid rgba(226, 232, 240, 0.95);
  transition: all 0.18s ease;
}

.cart-item:hover .cart-item-grid {
  background: #ecfdf3;
  border-color: rgba(0, 177, 106, 0.45);
  box-shadow: 0 8px 18px rgba(15, 23, 42, 0.08);
}

.cart-item-image .image-frame {
  width: 100%;
  aspect-ratio: 4 / 3;
  border-radius: 12px;
  overflow: hidden;
  background: #ffffff;
  border: 1px solid rgba(226, 232, 240, 0.9);
}

.cart-item-image .image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.25s ease;
}

.cart-item:hover .image {
  transform: scale(1.05);
}

.cart-item-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 6px;
}

.item-title {
  font-size: 16px;
  font-weight: 650;
  margin: 0;
  color: #0b1220;
}

.item-notes {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0;
}

.cart-item-price,
.cart-item-qty,
.cart-item-total {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 6px;
}

.label {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--text-muted);
}

.price {
  font-weight: 600;
  font-size: 15px;
  color: #111827;
}

.total {
  font-weight: 750;
  font-size: 16px;
  color: var(--primary-dark);
}

.qty-control {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  border: 1px solid rgba(209, 213, 219, 0.9);
  background: #ffffff;
  overflow: hidden;
}

.qty-button {
  width: 32px;
  height: 32px;
  border: none;
  background: transparent;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 13px;
  color: #4b5563;
  transition: all 0.15s ease;
}

.qty-button:hover:not(:disabled) {
  background: rgba(0, 177, 106, 0.08);
  color: var(--primary-dark);
}

.qty-button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.qty-input {
  width: 50px;
  height: 32px;
  border: none;
  text-align: center;
  font-size: 14px;
  font-weight: 650;
  background: transparent;
}

.qty-input:focus {
  outline: none;
}

.card-actions .card-body {
  padding-top: 14px;
  padding-bottom: 14px;
}

.actions-row {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

.btn {
  border-radius: 999px;
  font-size: 14px;
  font-weight: 600;
  padding: 9px 18px;
  border: none;
  outline: none;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  transition: all 0.18s ease;
  white-space: nowrap;
}

.btn i {
  font-size: 14px;
}

.btn:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary) 0%, #00d47a 100%);
  color: #ffffff;
  box-shadow: 0 8px 20px rgba(0, 177, 106, 0.35);
}

.btn-primary:hover:not(:disabled) {
  background: linear-gradient(135deg, var(--primary-dark) 0%, #00c16e 100%);
  transform: translateY(-1px);
  box-shadow: 0 10px 26px rgba(0, 177, 106, 0.45);
}

.btn-outline {
  background: #ffffff;
  color: #111827;
  border: 1px solid rgba(148, 163, 184, 0.7);
}

.btn-outline:hover:not(:disabled) {
  background: #ecfdf3;
  border-color: rgba(0, 177, 106, 0.7);
  color: var(--primary-dark);
}

.btn-ghost-danger {
  background: transparent;
  border-radius: 999px;
  color: #ef4444;
  font-size: 13px;
  padding: 6px 10px;
}

.btn-ghost-danger:hover:not(:disabled) {
  background: rgba(248, 113, 113, 0.08);
}

.btn-soft {
  background: #f9fafb;
  color: var(--text-muted);
}

.btn-soft:hover:not(:disabled) {
  background: #e5e7eb;
  color: #111827;
}

.btn-block {
  width: 100%;
  justify-content: center;
}

.summary-card {
  border: 1px solid rgba(0, 177, 106, 0.3);
}

.summary-body {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.summary-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  font-size: 14px;
}

.summary-row span:first-child {
  color: var(--text-muted);
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.summary-row span i {
  color: var(--primary-dark);
  font-size: 13px;
}

.value {
  font-weight: 600;
  color: #111827;
}

.summary-divider {
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(148, 163, 184, 0.65),
    transparent
  );
  margin: 10px 0;
}

.summary-row-total {
  margin-top: 4px;
}

.value-total {
  font-size: 20px;
  font-weight: 750;
  color: var(--primary-dark);
}

.summary-actions {
  margin-top: 14px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.support-card .support-body {
  display: flex;
  align-items: center;
  gap: 14px;
}

.support-icon {
  width: 42px;
  height: 42px;
  border-radius: 999px;
  background: rgba(0, 177, 106, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-dark);
}

.support-icon i {
  font-size: 18px;
}

.support-number {
  font-weight: 700;
  color: #0b1220;
  font-size: 16px;
}

.support-text {
  margin: 2px 0 0;
  font-size: 13px;
  color: var(--text-muted);
}

.delivery-card {
  padding: 12px 18px;
  background: #f9fafb;
}

.delivery-row {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 0;
  font-size: 14px;
  color: var(--text-muted);
  border-bottom: 1px dashed rgba(209, 213, 219, 0.7);
}

.delivery-row:last-child {
  border-bottom: none;
}

.delivery-row i {
  color: var(--primary);
}

.cart-item-enter-active,
.cart-item-leave-active {
  transition: all 0.3s ease;
}

.cart-item-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.cart-item-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

.cart-item-move {
  transition: transform 0.3s ease;
}

@media (max-width: 1024px) {
  .cart-layout {
    grid-template-columns: minmax(0, 1.7fr) minmax(280px, 1fr);
  }

  .cart-item-grid {
    grid-template-columns: 90px minmax(0, 1.6fr) 110px 130px;
    grid-template-rows: auto auto;
  }

  .cart-item-total {
    grid-column: 3 / span 2;
    align-items: flex-end;
  }
}

@media (max-width: 768px) {
  .cart-page {
    padding: 20px 4vw 28px;
  }

  .cart-layout {
    grid-template-columns: minmax(0, 1fr);
  }

  .cart-sidebar {
    position: static;
  }

  .empty-state {
    grid-template-columns: minmax(0, 1fr);
    text-align: left;
  }

  .empty-right {
    justify-content: flex-start;
  }

  .cart-item-grid {
    grid-template-columns: 88px minmax(0, 1.8fr);
    grid-template-rows: auto auto auto;
  }

  .cart-item-price,
  .cart-item-qty,
  .cart-item-total {
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }

  .cart-item-total {
    border-top: 1px dashed rgba(209, 213, 219, 0.7);
    padding-top: 6px;
    margin-top: 6px;
  }

  .actions-row {
    flex-direction: column;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .cart-header h1 {
    font-size: 22px;
  }

  .card-body {
    padding: 16px 14px 18px;
  }

  .cart-item-grid {
    padding: 10px 10px;
  }

  .cart-items-scroll {
    max-height: 520px;
  }
}
</style>
