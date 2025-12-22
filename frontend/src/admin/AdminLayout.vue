<template>
  <v-app>
    <!-- ===== SIDEBAR (NAVIGATION DRAWER) ===== -->
    <v-navigation-drawer
      v-model="drawer"
      app
      :rail="rail"
      :permanent="!mobile"
      :temporary="mobile"
      color="surface"
      elevation="1"
      :width="280"
      :rail-width="72"
    >
      <!-- Logo Section -->
      <div class="pa-4 d-flex align-center" :class="{ 'justify-center': rail }">
        <v-avatar :size="rail ? 40 : 48" color="primary" class="elevation-2">
          <v-icon size="24" color="white">mdi-silverware-fork-knife</v-icon>
        </v-avatar>

        <div v-if="!rail" class="ml-3">
          <div class="logo-title">PDQ Restaurant</div>
          <div class="text-caption text-medium-emphasis">Admin Panel</div>
        </div>
      </div>

      <v-divider></v-divider>

      <!-- User Info Card (when not rail) -->
      <v-card v-if="!rail" flat color="primary" class="ma-3 mb-2" rounded="lg">
        <v-card-text class="pa-3">
          <div class="d-flex align-center">
            <v-avatar size="40" color="white">
              <span class="text-primary font-weight-bold">{{
                userInitial
              }}</span>
            </v-avatar>
            <div class="ml-3 flex-grow-1" style="min-width: 0">
              <div
                class="text-subtitle-2 font-weight-bold text-white text-truncate"
              >
                {{ userName }}
              </div>
              <div
                class="text-caption text-white text-truncate"
                style="opacity: 0.9"
              >
                {{ userRole }}
              </div>
            </div>
          </div>
        </v-card-text>
      </v-card>

      <!-- Menu Items -->
      <v-list nav density="default" class="pa-2">
        <!-- Main Section -->
        <v-list-subheader v-if="!rail" class="menu-section-title">
          MAIN MENU
        </v-list-subheader>

        <v-list-item
          :to="{ name: 'AdminDashboard' }"
          prepend-icon="mdi-view-dashboard"
          title="Dashboard"
          value="dashboard"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        ></v-list-item>

        <v-list-item
          :to="{ name: 'AdminOrders' }"
          prepend-icon="mdi-cart"
          title="Orders"
          value="orders"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        >
          <template v-slot:append v-if="!rail && pendingOrdersCount > 0">
            <v-chip
              size="small"
              color="error"
              variant="flat"
              class="font-weight-bold"
            >
              {{ pendingOrdersCount }}
            </v-chip>
          </template>
        </v-list-item>

        <v-list-item
          :to="{ name: 'AdminKitchen' }"
          prepend-icon="mdi-chef-hat"
          title="Kitchen Display"
          value="kitchen"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        ></v-list-item>

        <v-divider class="my-3"></v-divider>

        <!-- Menu Management Section -->
        <v-list-subheader v-if="!rail" class="menu-section-title">
          MENU MANAGEMENT
        </v-list-subheader>

        <v-list-item
          :to="{ name: 'AdminMenu' }"
          prepend-icon="mdi-food"
          title="Products"
          value="menu"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        ></v-list-item>

        <v-list-item
          :to="{ name: 'AdminCategories' }"
          prepend-icon="mdi-shape"
          title="Categories"
          value="categories"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        ></v-list-item>

        <v-divider class="my-3"></v-divider>

        <!-- Reports Section -->
        <v-list-subheader v-if="!rail" class="menu-section-title">
          REPORTS & ANALYTICS
        </v-list-subheader>

        <v-list-item
          :to="{ name: 'AdminSales' }"
          prepend-icon="mdi-chart-line"
          title="Sales Report"
          value="sales"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        ></v-list-item>

        <v-list-item
          :to="{ name: 'AdminEarnings' }"
          prepend-icon="mdi-cash-multiple"
          title="Revenue"
          value="earnings"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        ></v-list-item>

        <v-divider class="my-3"></v-divider>

        <!-- Promotions Section -->
        <v-list-subheader v-if="!rail" class="menu-section-title">
          PROMOTIONS
        </v-list-subheader>

        <v-list-item
          :to="{ name: 'AdminPromoCodes' }"
          prepend-icon="mdi-tag-multiple"
          title="Promo Codes"
          value="promo-codes"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        ></v-list-item>

        <v-divider class="my-3"></v-divider>

        <!-- System Section -->
        <v-list-subheader v-if="!rail" class="menu-section-title">
          SYSTEM
        </v-list-subheader>

        <v-list-item
          :to="{ name: 'AdminUsers' }"
          prepend-icon="mdi-account-group"
          title="Users"
          value="users"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        ></v-list-item>

        <v-list-item
          :to="{ name: 'AdminSettings' }"
          prepend-icon="mdi-cog"
          title="Settings"
          value="settings"
          color="primary"
          rounded="lg"
          class="menu-item mb-1"
        ></v-list-item>
      </v-list>

      <!-- Sidebar Footer -->
      <template v-slot:append>
        <div class="pa-3">
          <!-- Collapse/Expand Button (Desktop only) -->
          <v-btn
            v-if="!mobile"
            block
            variant="tonal"
            color="primary"
            @click.stop="rail = !rail"
            class="mb-2 btn-large"
            :prepend-icon="rail ? 'mdi-chevron-right' : 'mdi-chevron-left'"
            size="default"
          >
            <span v-if="!rail" class="btn-text">Collapse</span>
          </v-btn>

          <!-- Logout Button -->
          <v-btn
            block
            color="error"
            variant="flat"
            prepend-icon="mdi-logout"
            @click="logout"
            class="btn-large"
            size="default"
          >
            <span v-if="!rail" class="btn-text">Logout</span>
          </v-btn>
        </div>
      </template>
    </v-navigation-drawer>

    <!-- ===== TOP APP BAR ===== -->
    <v-app-bar
      elevation="0"
      border="b"
      color="surface"
      height="64"
      class="px-4"
    >
      <!-- Mobile Menu Toggle -->
      <v-app-bar-nav-icon
        v-if="mobile"
        @click="drawer = !drawer"
        class="mr-2"
      ></v-app-bar-nav-icon>

      <!-- Breadcrumbs & Page Title -->
      <div class="d-flex flex-column">
        <v-breadcrumbs
          :items="breadcrumbs"
          density="compact"
          class="pa-0"
          style="min-height: 0"
        >
          <template v-slot:divider>
            <v-icon size="14">mdi-chevron-right</v-icon>
          </template>
        </v-breadcrumbs>
        <div class="page-title">
          {{ pageTitle }}
        </div>
      </div>

      <v-spacer></v-spacer>

      <!-- Search Button (Optional) -->
      <v-btn icon variant="text" class="mr-2" size="large">
        <v-icon>mdi-magnify</v-icon>
      </v-btn>

      <!-- Dark Mode Toggle -->
      <v-btn icon variant="text" @click="toggleTheme" class="mr-2" size="large">
        <v-icon>
          {{
            theme.global.name.value === "dark"
              ? "mdi-white-balance-sunny"
              : "mdi-weather-night"
          }}
        </v-icon>
      </v-btn>

      <!-- Notifications -->
      <v-menu offset-y>
        <template v-slot:activator="{ props }">
          <v-btn icon variant="text" class="mr-2" v-bind="props" size="large">
            <v-badge
              v-if="pendingOrdersCount > 0"
              :content="pendingOrdersCount"
              color="error"
              overlap
            >
              <v-icon>mdi-bell</v-icon>
            </v-badge>
            <v-icon v-else>mdi-bell-outline</v-icon>
          </v-btn>
        </template>

        <v-card width="320" elevation="8">
          <v-card-title class="d-flex align-center pa-4">
            <v-icon class="mr-2">mdi-bell</v-icon>
            <span class="notification-title">Notifications</span>
            <v-spacer></v-spacer>
            <v-chip size="small" color="error" v-if="pendingOrdersCount > 0">
              {{ pendingOrdersCount }}
            </v-chip>
          </v-card-title>
          <v-divider></v-divider>
          <v-list lines="two" density="compact">
            <v-list-item
              v-if="pendingOrdersCount > 0"
              prepend-icon="mdi-cart"
              :title="`${pendingOrdersCount} pending orders`"
              subtitle="Waiting for confirmation"
            >
              <template v-slot:append>
                <v-chip size="small" color="warning">New</v-chip>
              </template>
            </v-list-item>
            <v-list-item v-else>
              <div class="text-center pa-4 text-medium-emphasis">
                <v-icon size="48" class="mb-2">mdi-bell-off-outline</v-icon>
                <div>No new notifications</div>
              </div>
            </v-list-item>
          </v-list>
        </v-card>
      </v-menu>

      <!-- User Menu -->
      <v-menu offset-y>
        <template v-slot:activator="{ props }">
          <v-btn variant="text" v-bind="props" class="px-2">
            <v-avatar size="36" color="primary" class="mr-2">
              <span class="text-white font-weight-bold">{{ userInitial }}</span>
            </v-avatar>
            <div class="text-left d-none d-sm-block">
              <div class="user-name">{{ userName }}</div>
              <div class="user-role">{{ userRole }}</div>
            </div>
            <v-icon class="ml-2">mdi-chevron-down</v-icon>
          </v-btn>
        </template>

        <v-card width="280" elevation="8">
          <v-card-text class="pa-4">
            <div class="d-flex align-center mb-3">
              <v-avatar size="48" color="primary">
                <span class="text-white text-h6 font-weight-bold">{{
                  userInitial
                }}</span>
              </v-avatar>
              <div class="ml-3">
                <div class="user-menu-name">{{ userName }}</div>
                <div class="user-menu-email">{{ userEmail }}</div>
              </div>
            </div>
          </v-card-text>

          <v-divider></v-divider>

          <v-list density="compact">
            <v-list-item
              prepend-icon="mdi-account"
              title="Profile"
              value="profile"
              @click="goToProfile"
            ></v-list-item>
            <v-list-item
              prepend-icon="mdi-cog"
              title="Settings"
              value="settings"
              @click="goToSettings"
            ></v-list-item>
            <v-list-item
              prepend-icon="mdi-help-circle"
              title="Help & Support"
              value="help"
            ></v-list-item>
          </v-list>

          <v-divider></v-divider>

          <v-card-actions class="pa-3">
            <v-btn
              block
              color="error"
              variant="flat"
              prepend-icon="mdi-logout"
              @click="logout"
              class="btn-large"
            >
              <span class="btn-text">Logout</span>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-menu>
    </v-app-bar>

    <!-- ===== MAIN CONTENT ===== -->
    <v-main class="bg-grey-lighten-4">
      <v-container fluid class="pa-6">
        <!-- Page Transition -->
        <router-view v-slot="{ Component }">
          <transition name="page-fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </v-container>
    </v-main>

    <!-- ===== LOADING OVERLAY ===== -->
    <v-overlay
      v-model="isLoading"
      class="align-center justify-center"
      persistent
    >
      <v-progress-circular
        indeterminate
        size="64"
        color="primary"
      ></v-progress-circular>
      <div class="text-h6 mt-4">Loading...</div>
    </v-overlay>
  </v-app>
</template>

<script>
import { ref, computed, onMounted } from "vue";
import { useTheme, useDisplay } from "vuetify";
import { useRouter, useRoute } from "vue-router";
import storage from "@/utils/storage";

export default {
  name: "AdminLayout",

  setup() {
    const theme = useTheme();
    const { mobile } = useDisplay();
    const router = useRouter();
    const route = useRoute();

    // State
    const drawer = ref(true);
    const rail = ref(false);
    const isLoading = ref(false);
    const pendingOrdersCount = ref(0);

    // User Info
    const user = storage.getUser();
    const userName = computed(
      () => user?.fullName || user?.username || "Admin User"
    );
    const userEmail = computed(() => user?.email || "admin@pdq.com");
    const userRole = computed(() => {
      const role = user?.role || "ROLE_ADMIN";
      return role.replace("ROLE_", "");
    });
    const userInitial = computed(() => {
      return userName.value.charAt(0).toUpperCase();
    });

    // Page Title & Breadcrumbs
    const pageTitle = computed(() => {
      const name = route.name;
      const titles = {
        AdminDashboard: "Dashboard",
        AdminOrders: "Orders Management",
        AdminKitchen: "Kitchen Display",
        AdminMenu: "Menu Management",
        AdminCategories: "Categories",
        AdminUsers: "User Management",
        AdminSales: "Sales Report",
        AdminEarnings: "Revenue & Earnings",
        AdminPromoCodes: "Promotional Code Management",
      };
      return titles[name] || "Admin Panel";
    });

    const breadcrumbs = computed(() => {
      const name = route.name;
      const items = [
        { title: "Home", disabled: false, href: "/admin/dashboard" },
      ];

      if (name !== "AdminDashboard") {
        items.push({
          title: pageTitle.value,
          disabled: true,
        });
      }

      return items;
    });

    // Theme Toggle
    const toggleTheme = () => {
      theme.global.name.value = theme.global.current.value.dark
        ? "light"
        : "dark";
      localStorage.setItem("theme", theme.global.name.value);
    };

    // Load saved theme
    const loadTheme = () => {
      const savedTheme = localStorage.getItem("theme");
      if (savedTheme) {
        theme.global.name.value = savedTheme;
      }
    };

    // Navigation
    const goToProfile = () => {
      router.push({ name: "AdminProfile" });
    };

    const goToSettings = () => {
      router.push({ name: "AdminSettings" });
    };

    // Logout
    const logout = () => {
      if (!confirm("Bạn có chắc muốn đăng xuất?")) return;

      isLoading.value = true;
      storage.clearAll();

      setTimeout(() => {
        router.push("/login");
        window.location.reload();
      }, 500);
    };

    // Fetch Pending Orders Count
    const fetchPendingOrdersCount = async () => {
      try {
        // TODO: Call real API
        pendingOrdersCount.value = 0;
      } catch (error) {
        console.error("Error fetching pending orders:", error);
      }
    };

    // Auto-collapse sidebar on mobile
    const handleResize = () => {
      if (mobile.value) {
        drawer.value = false;
      }
    };

    // Lifecycle
    onMounted(() => {
      loadTheme();
      fetchPendingOrdersCount();
      handleResize();

      // Refresh orders count every 30 seconds
      setInterval(fetchPendingOrdersCount, 30000);
    });

    return {
      drawer,
      rail,
      mobile,
      isLoading,
      pendingOrdersCount,
      userName,
      userEmail,
      userRole,
      userInitial,
      pageTitle,
      breadcrumbs,
      theme,
      toggleTheme,
      goToProfile,
      goToSettings,
      logout,
    };
  },
};
</script>

<style scoped>
/* ===== FONT SIZES - CHỮ TO HƠN ===== */
.logo-title {
  font-size: 19px !important;
  font-weight: 700 !important;
  line-height: 1.2;
}

.menu-section-title {
  font-size: 12px !important;
  font-weight: 700 !important;
  letter-spacing: 0.5px !important;
  color: rgba(var(--v-theme-on-surface), 0.6) !important;
  padding-top: 12px !important;
  padding-bottom: 8px !important;
}

.page-title {
  font-size: 20px !important;
  font-weight: 700 !important;
  margin-top: -4px;
}

.notification-title {
  font-size: 16px !important;
  font-weight: 600 !important;
}

.user-name {
  font-size: 14px !important;
  font-weight: 600 !important;
}

.user-role {
  font-size: 12px !important;
  color: rgba(var(--v-theme-on-surface), 0.6);
}

.user-menu-name {
  font-size: 16px !important;
  font-weight: 700 !important;
}

.user-menu-email {
  font-size: 13px !important;
  color: rgba(var(--v-theme-on-surface), 0.6);
}

.btn-text {
  font-size: 15px !important;
  font-weight: 600 !important;
}

.menu-item .v-list-item-title {
  font-size: 16px !important;
  font-weight: 600 !important;
}

/* ===== MENU ITEMS STYLING ===== */
:deep(.menu-item .v-list-item-title) {
  font-size: 15px !important;
  font-weight: 500 !important;
  letter-spacing: 0.2px !important;
}

:deep(.menu-item) {
  min-height: 48px !important;
  padding: 10px 16px !important;
  margin-bottom: 4px !important;
}

:deep(.menu-item .v-icon) {
  font-size: 22px !important;
}

/* ===== BUTTON SIZES ===== */
.btn-large {
  height: 42px !important;
  font-size: 15px !important;
}

:deep(.btn-large .v-btn__content) {
  font-size: 15px !important;
  font-weight: 600 !important;
}

/* ===== PAGE TRANSITION ===== */
.page-fade-enter-active,
.page-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.page-fade-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.page-fade-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

/* ===== ACTIVE MENU ITEM ===== */
:deep(.v-list-item--active) {
  background: linear-gradient(
    90deg,
    rgba(var(--v-theme-primary), 0.12) 0%,
    rgba(var(--v-theme-primary), 0.08) 100%
  ) !important;
  border-left: 3px solid rgb(var(--v-theme-primary));
}

:deep(.v-list-item--active .v-list-item-title) {
  color: rgb(var(--v-theme-primary)) !important;
  font-weight: 700 !important;
}

:deep(.v-list-item--active .v-icon) {
  color: rgb(var(--v-theme-primary)) !important;
}

/* ===== HOVER EFFECTS ===== */
:deep(.v-list-item) {
  transition: all 0.2s ease;
}

:deep(.v-list-item:hover:not(.v-list-item--active)) {
  background: rgba(var(--v-theme-primary), 0.05) !important;
  transform: translateX(4px);
}

/* ===== NAVIGATION RAIL MODE ===== */
:deep(.v-navigation-drawer--rail) {
  .v-list-item__prepend {
    margin-inline-end: 0 !important;
  }

  .v-list-subheader {
    display: none;
  }
}

/* ===== CUSTOM SCROLLBAR ===== */
:deep(.v-navigation-drawer__content) {
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: rgba(var(--v-theme-primary), 0.3) transparent;
}

:deep(.v-navigation-drawer__content::-webkit-scrollbar) {
  width: 6px;
}

:deep(.v-navigation-drawer__content::-webkit-scrollbar-thumb) {
  background: rgba(var(--v-theme-primary), 0.3);
  border-radius: 10px;
}

:deep(.v-navigation-drawer__content::-webkit-scrollbar-thumb:hover) {
  background: rgba(var(--v-theme-primary), 0.5);
}

/* ===== APP BAR SHADOW ===== */
:deep(.v-app-bar) {
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06) !important;
}

/* ===== BREADCRUMBS STYLING ===== */
:deep(.v-breadcrumbs-item--disabled) {
  opacity: 0.6;
}

:deep(.v-breadcrumbs) {
  font-size: 13px !important;
}

/* ===== MAIN CONTENT BACKGROUND ===== */
.v-main {
  background: linear-gradient(
    180deg,
    rgba(var(--v-theme-surface), 1) 0%,
    rgba(var(--v-theme-surface-variant), 0.3) 100%
  );
}

/* ===== DARK MODE ADJUSTMENTS ===== */
.v-theme--dark .v-main {
  background: linear-gradient(180deg, rgb(18, 18, 18) 0%, rgb(24, 24, 24) 100%);
}

/* ===== REMOVE GREEN SCROLLBAR ===== */
:global(*) {
  scrollbar-color: #cbd5e1 transparent !important;
}

:global(*::-webkit-scrollbar-thumb) {
  background: #cbd5e1 !important;
}

:global(*::-webkit-scrollbar-thumb:hover) {
  background: #94a3b8 !important;
}
</style>
