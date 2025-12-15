<template>
  <!-- ẨN NAVBAR KHI Ở TRANG ADMIN -->
  <header
    v-if="!isAdminPage"
    class="navbar-header"
    :class="{ scrolled: isScrolled }"
  >
    <div class="navbar-container">
      <!-- Logo -->
      <router-link to="/" class="navbar-logo" @click="scrollToTop">
        <img
          src="../assets/images/taco-logo.png"
          alt="PDQ Logo"
          class="logo-image"
        />
        <span class="logo-text">PDQ</span>
      </router-link>

      <!-- Desktop Navigation -->
      <nav class="navbar-menu">
        <router-link to="/" class="nav-link" @click="scrollToTop">
          <i class="fas fa-home"></i>
        </router-link>
        <router-link to="/about" class="nav-link" @click="scrollToTop">
          <i class="fas fa-info-circle"></i>
        </router-link>
        <router-link to="/promotions" class="nav-link" @click="scrollToTop">
          <i class="fas fa-tags"></i>
        </router-link>
        <router-link to="/menu" class="nav-link" @click="scrollToTop">
          <i class="fas fa-utensils"></i>
        </router-link>
        <router-link to="/table" class="nav-link" @click="scrollToTop">
          <i class="fas fa-chair"></i>
        </router-link>
      </nav>

      <!-- Actions -->
      <div class="navbar-actions">
        <!-- Cart -->
        <router-link
          to="/cart"
          class="action-btn cart-btn"
          @click="scrollToTop"
        >
          <i class="fas fa-shopping-cart"></i>
          <span
            class="badge"
            v-if="cartCount > 0"
            :class="{ 'badge-bounce': isBadgeBouncing }"
          >
            {{ cartCount }}
          </span>
        </router-link>

        <!-- NOT LOGGED IN -->
        <div class="account-dropdown" v-if="!user">
          <button
            class="action-btn account-btn"
            @click.stop="toggleAccountDropdown"
          >
            <i class="fas fa-user"></i>
          </button>

          <div class="dropdown-menu" :class="{ show: isAccountDropdownOpen }">
            <router-link
              to="/login"
              class="dropdown-item"
              @click="closeAccountDropdown"
            >
              <i class="fas fa-sign-in-alt"></i>
              Đăng nhập
            </router-link>
            <router-link
              to="/register"
              class="dropdown-item"
              @click="closeAccountDropdown"
            >
              <i class="fas fa-user-plus"></i>
              Đăng ký
            </router-link>
          </div>
        </div>

        <!-- LOGGED IN -->
        <div class="account-dropdown" v-else>
          <button
            class="action-btn account-btn logged-in"
            @click.stop="toggleAccountDropdown"
          >
            <i class="fas fa-user-circle"></i>
          </button>

          <div class="dropdown-menu" :class="{ show: isAccountDropdownOpen }">
            <div class="dropdown-header">
              <i class="fas fa-user-circle"></i>
              <span>{{ getUserDisplayName }}</span>
            </div>

            <router-link
              to="/profile"
              class="dropdown-item"
              @click="closeAccountDropdown"
            >
              <i class="fas fa-user-cog"></i>
              Thông tin tài khoản
            </router-link>

            <router-link
              to="/my-order"
              class="dropdown-item"
              @click="closeAccountDropdown"
            >
              <i class="fas fa-clipboard-list"></i>
              Đơn hàng của tôi
            </router-link>

            <div class="dropdown-divider"></div>

            <router-link
              to="/"
              class="dropdown-item logout"
              @click="handleLogout"
            >
              <i class="fas fa-sign-out-alt"></i>
              Đăng xuất
            </router-link>
          </div>
        </div>

        <!-- Mobile button -->
        <button class="mobile-menu-btn" @click="toggleMobileMenu">
          <span class="bar"></span>
          <span class="bar"></span>
          <span class="bar"></span>
        </button>
      </div>
    </div>

    <!-- Mobile Navigation -->
    <div class="mobile-menu" :class="{ active: isMobileMenuOpen }">
      <div class="mobile-menu-content">
        <router-link to="/" class="mobile-nav-link" @click="closeMobileMenu">
          <i class="fas fa-home"></i> <span>Trang chủ</span>
        </router-link>

        <router-link
          to="/about"
          class="mobile-nav-link"
          @click="closeMobileMenu"
        >
          <i class="fas fa-info-circle"></i> <span>Về chúng tôi</span>
        </router-link>

        <router-link
          to="/promotions"
          class="mobile-nav-link"
          @click="closeMobileMenu"
        >
          <i class="fas fa-tags"></i> <span>Khuyến mãi</span>
        </router-link>

        <router-link
          to="/menu"
          class="mobile-nav-link"
          @click="closeMobileMenu"
        >
          <i class="fas fa-utensils"></i> <span>Thực đơn</span>
        </router-link>

        <router-link
          to="/table"
          class="mobile-nav-link"
          @click="closeMobileMenu"
        >
          <i class="fas fa-chair"></i> <span>Đặt bàn</span>
        </router-link>

        <div class="mobile-menu-divider"></div>

        <!-- MOBILE USER -->
        <div v-if="!user">
          <router-link
            to="/login"
            class="mobile-nav-link"
            @click="closeMobileMenu"
          >
            <i class="fas fa-sign-in-alt"></i> <span>Đăng nhập</span>
          </router-link>

          <router-link
            to="/register"
            class="mobile-nav-link"
            @click="closeMobileMenu"
          >
            <i class="fas fa-user-plus"></i> <span>Đăng ký</span>
          </router-link>
        </div>

        <div v-else>
          <div class="mobile-user-info">
            <i class="fas fa-user-circle"></i>
            <span>{{ getUserDisplayName }}</span>
          </div>

          <router-link
            to="/profile"
            class="mobile-nav-link"
            @click="closeMobileMenu"
          >
            <i class="fas fa-user-cog"></i> <span>Thông tin tài khoản</span>
          </router-link>

          <router-link
            to="/my-order"
            class="mobile-nav-link"
            @click="closeMobileMenu"
          >
            <i class="fas fa-clipboard-list"></i> <span>Đơn hàng của tôi</span>
          </router-link>

          <router-link
            to="/"
            class="mobile-nav-link logout"
            @click="handleLogout"
          >
            <i class="fas fa-sign-out-alt"></i> <span>Đăng xuất</span>
          </router-link>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { mapState, mapMutations } from "vuex";
import api from "@/axios";
import storage from "@/utils/storage";

export default {
  name: "NavBar",

  data() {
    return {
      isScrolled: false,
      isMobileMenuOpen: false,
      isAccountDropdownOpen: false,
      cartCount: 0,
      isBadgeBouncing: false, // ✅ THÊM ĐỂ TẠO HIỆU ỨNG
    };
  },

  computed: {
    ...mapState(["user"]),

    isCustomer() {
      return (this.user?.role || "").toUpperCase().includes("CUSTOMER");
    },

    getUserDisplayName() {
      if (!this.user) return "User";
      return (
        this.user.fullName ||
        this.user.user_name ||
        this.user.username ||
        "User"
      );
    },

    isAdminPage() {
      return this.$route.path.startsWith("/admin");
    },
  },

  watch: {
    user: {
      immediate: true,
      handler(newUser) {
        if (newUser) {
          this.loadCartCount();
        } else {
          this.cartCount = 0;
        }
      },
    },
  },

  mounted() {
    window.addEventListener("scroll", this.handleScroll);
    document.addEventListener("click", this.closeAccountDropdownOutside);

    // ✅ LẮNG NGHE EVENT TỪ QUICKVIEW KHI THÊM VÀO GIỎ HÀNG
    window.addEventListener("cart-updated", this.handleCartUpdate);

    if (this.user) {
      this.loadCartCount();
    }
  },

  beforeUnmount() {
    window.removeEventListener("scroll", this.handleScroll);
    document.removeEventListener("click", this.closeAccountDropdownOutside);
    window.removeEventListener("cart-updated", this.handleCartUpdate);
  },

  methods: {
    ...mapMutations(["setUser"]),

    // ✅ XỬ LÝ KHI NHẬN ĐƯỢC EVENT CẬP NHẬT GIỎ HÀNG
    async handleCartUpdate() {
      console.log("🔔 Nhận được thông báo cập nhật giỏ hàng!");

      // Load lại số lượng giỏ hàng
      await this.loadCartCount();

      // ✅ TẠO HIỆU ỨNG BOUNCE CHO BADGE
      this.isBadgeBouncing = true;
      setTimeout(() => {
        this.isBadgeBouncing = false;
      }, 600);
    },

    async loadCartCount() {
      if (!this.user || !this.isCustomer) {
        this.cartCount = 0;
        return;
      }

      try {
        const token = storage.getToken();
        if (!token) {
          this.cartCount = 0;
          return;
        }

        console.log("🔄 Đang tải số lượng giỏ hàng...");

        const res = await api.get("/cart", {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (res.data.success && res.data.data) {
          this.cartCount = res.data.data.totalItems || 0;
          console.log("✅ Cập nhật số lượng giỏ hàng:", this.cartCount);
        }
      } catch (err) {
        console.error("❌ Lỗi khi tải số lượng giỏ hàng:", err);
        this.cartCount = 0;
      }
    },

    scrollToTop() {
      window.scrollTo({ top: 0, behavior: "smooth" });
    },

    handleScroll() {
      this.isScrolled = window.scrollY > 50;
    },

    toggleAccountDropdown(event) {
      event.stopPropagation();
      this.isAccountDropdownOpen = !this.isAccountDropdownOpen;
    },

    closeAccountDropdown() {
      this.isAccountDropdownOpen = false;
    },

    closeAccountDropdownOutside(event) {
      if (!this.$el.contains(event.target)) {
        this.isAccountDropdownOpen = false;
      }
    },

    toggleMobileMenu() {
      this.isMobileMenuOpen = !this.isMobileMenuOpen;
      document.body.style.overflow = this.isMobileMenuOpen ? "hidden" : "";
    },

    closeMobileMenu() {
      this.isMobileMenuOpen = false;
      document.body.style.overflow = "";
      this.scrollToTop();
    },

    handleLogout() {
      storage.clearAuth();
      this.setUser(null);
      this.cartCount = 0;
      this.closeMobileMenu();
      this.closeAccountDropdown();
      this.$router.push("/");
      setTimeout(() => window.location.reload(), 200);
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

.navbar-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  background: white;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.navbar-header.scrolled {
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.navbar-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 16px 5%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.navbar-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  transition: transform 0.3s ease;
}

.navbar-logo:hover {
  transform: scale(1.05);
}

.logo-image {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.logo-text {
  font-size: 28px;
  font-weight: 900;
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.navbar-menu {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border-radius: 12px;
  text-decoration: none;
  color: #666;
  font-size: 15px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.nav-link i {
  font-size: 16px;
}

.nav-link:hover {
  color: #00b067;
  background: rgba(0, 176, 103, 0.1);
}

.nav-link.router-link-exact-active {
  color: white;
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  box-shadow: 0 4px 12px rgba(0, 176, 103, 0.3);
}

.navbar-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.action-btn {
  position: relative;
  width: 44px;
  height: 44px;
  border-radius: 12px;
  border: none;
  background: #f7f7f7;
  color: #333;
  font-size: 18px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  text-decoration: none;
}

.action-btn:hover {
  background: #00b067;
  color: white;
  transform: translateY(-2px);
}

.cart-btn.router-link-exact-active {
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(0, 176, 103, 0.3);
}

.account-btn.logged-in {
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  color: white;
}

/* ✅ BADGE VỚI ANIMATION */
.badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background: #e74c3c;
  color: white;
  font-size: 11px;
  font-weight: 700;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
  transition: all 0.3s ease;
}

/* ✅ HIỆU ỨNG BOUNCE KHI SỐ LƯỢNG THAY ĐỔI */
.badge-bounce {
  animation: badgeBounce 0.6s cubic-bezier(0.34, 1.56, 0.64, 1);
}

@keyframes badgeBounce {
  0% {
    transform: scale(1);
  }
  25% {
    transform: scale(1.4);
  }
  50% {
    transform: scale(0.9);
  }
  75% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

.account-dropdown {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 12px);
  right: 0;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  min-width: 220px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 1001;
}

.dropdown-menu.show {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.dropdown-header {
  padding: 16px;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 700;
  color: #00b067;
}

.dropdown-header i {
  font-size: 24px;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  color: #333;
  text-decoration: none;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.dropdown-item i {
  font-size: 16px;
  width: 20px;
  color: #666;
}

.dropdown-item:hover {
  background: rgba(0, 176, 103, 0.1);
  color: #00b067;
}

.dropdown-item:hover i {
  color: #00b067;
}

.dropdown-item.logout {
  color: #e74c3c;
}

.dropdown-item.logout:hover {
  background: rgba(231, 76, 60, 0.1);
}

.dropdown-divider {
  height: 1px;
  background: #e9ecef;
  margin: 8px 0;
}

.mobile-menu-btn {
  display: none;
  flex-direction: column;
  gap: 5px;
  width: 44px;
  height: 44px;
  background: #f7f7f7;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.mobile-menu-btn .bar {
  width: 24px;
  height: 3px;
  background: #333;
  border-radius: 2px;
  transition: all 0.3s ease;
}

.mobile-menu-btn:hover {
  background: #00b067;
}

.mobile-menu-btn:hover .bar {
  background: white;
}

.mobile-menu {
  position: fixed;
  top: 72px;
  left: 0;
  right: 0;
  bottom: 0;
  background: white;
  transform: translateX(-100%);
  transition: transform 0.3s ease;
  overflow-y: auto;
  z-index: 999;
}

.mobile-menu.active {
  transform: translateX(0);
}

.mobile-menu-content {
  padding: 20px;
}

.mobile-nav-link {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  border-radius: 12px;
  text-decoration: none;
  color: #333;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
  transition: all 0.3s ease;
}

.mobile-nav-link i {
  font-size: 20px;
  width: 24px;
  color: #666;
}

.mobile-nav-link:hover,
.mobile-nav-link.router-link-exact-active {
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
  color: #00b067;
}

.mobile-nav-link:hover i,
.mobile-nav-link.router-link-exact-active i {
  color: #00b067;
}

.mobile-nav-link.logout {
  color: #e74c3c;
}

.mobile-nav-link.logout:hover {
  background: rgba(231, 76, 60, 0.1);
}

.mobile-menu-divider {
  height: 1px;
  background: #e9ecef;
  margin: 16px 0;
}

.mobile-user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
  border-radius: 12px;
  margin-bottom: 16px;
  font-weight: 700;
  color: #00b067;
}

.mobile-user-info i {
  font-size: 28px;
}

@media (max-width: 1024px) {
  .navbar-menu {
    display: none;
  }

  .mobile-menu-btn {
    display: flex;
  }
}

@media (max-width: 480px) {
  .navbar-container {
    padding: 12px 4%;
  }

  .logo-text {
    font-size: 24px;
  }

  .logo-image {
    width: 36px;
    height: 36px;
  }

  .action-btn {
    width: 40px;
    height: 40px;
    font-size: 16px;
  }

  .mobile-menu {
    top: 64px;
  }
}
</style>
