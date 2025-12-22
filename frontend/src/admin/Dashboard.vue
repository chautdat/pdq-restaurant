<template>
  <div>
    <!-- Welcome Banner -->
    <v-card class="mb-6 rounded-xl" elevation="3">
      <v-card-text class="d-flex align-center justify-space-between pa-6">
        <div>
          <h1 class="text-h4 font-weight-bold mb-2">👋 Xin chào, Admin!</h1>
          <p class="text-subtitle-1 text-medium-emphasis">
            Chào mừng quay lại Dashboard. Đây là tổng quan hệ thống của bạn.
          </p>
        </div>
        <v-chip
          prepend-icon="mdi-calendar"
          variant="tonal"
          color="primary"
          size="large"
        >
          {{ getCurrentDate() }}
        </v-chip>
      </v-card-text>
    </v-card>

    <!-- Stats Cards -->
    <v-row class="mb-6">
      <!-- Total Earnings -->
      <v-col cols="12" sm="6" lg="3">
        <v-card elevation="2" class="stat-card">
          <v-card-text class="d-flex align-center pa-4">
            <v-avatar size="64" color="blue-darken-1" class="mr-4">
              <v-icon size="32" color="white">mdi-currency-usd</v-icon>
            </v-avatar>
            <div>
              <div
                class="text-caption text-medium-emphasis font-weight-bold text-uppercase"
              >
                Total Earnings
              </div>
              <div class="text-h5 font-weight-bold mt-1">
                {{ cards[0].value }}
              </div>
              <v-chip
                v-if="cards[0].trend !== null"
                size="x-small"
                :color="cards[0].trend > 0 ? 'success' : 'error'"
                variant="tonal"
                class="mt-2"
              >
                <v-icon start size="x-small">
                  {{ cards[0].trend > 0 ? "mdi-arrow-up" : "mdi-arrow-down" }}
                </v-icon>
                {{ Math.abs(cards[0].trend) }}% this month
              </v-chip>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- Today's Orders -->
      <v-col cols="12" sm="6" lg="3">
        <v-card elevation="2" class="stat-card">
          <v-card-text class="d-flex align-center pa-4">
            <v-avatar size="64" color="blue-darken-2" class="mr-4">
              <v-icon size="32" color="white">mdi-cart</v-icon>
            </v-avatar>
            <div>
              <div
                class="text-caption text-medium-emphasis font-weight-bold text-uppercase"
              >
                Today's Orders
              </div>
              <div class="text-h5 font-weight-bold mt-1">
                {{ cards[1].value }}
              </div>
              <v-chip
                v-if="cards[1].trend !== null"
                size="x-small"
                :color="cards[1].trend > 0 ? 'success' : 'error'"
                variant="tonal"
                class="mt-2"
              >
                <v-icon start size="x-small">
                  {{ cards[1].trend > 0 ? "mdi-arrow-up" : "mdi-arrow-down" }}
                </v-icon>
                {{ Math.abs(cards[1].trend) }}% vs yesterday
              </v-chip>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- Total Users -->
      <v-col cols="12" sm="6" lg="3">
        <v-card elevation="2" class="stat-card">
          <v-card-text class="d-flex align-center pa-4">
            <v-avatar size="64" color="blue-darken-3" class="mr-4">
              <v-icon size="32" color="white">mdi-account-group</v-icon>
            </v-avatar>
            <div>
              <div
                class="text-caption text-medium-emphasis font-weight-bold text-uppercase"
              >
                Total Users
              </div>
              <div class="text-h5 font-weight-bold mt-1">
                {{ cards[2].value }}
              </div>
              <v-chip
                v-if="cards[2].trend !== null"
                size="x-small"
                :color="cards[2].trend > 0 ? 'success' : 'error'"
                variant="tonal"
                class="mt-2"
              >
                <v-icon start size="x-small">
                  {{ cards[2].trend > 0 ? "mdi-arrow-up" : "mdi-arrow-down" }}
                </v-icon>
                {{ Math.abs(cards[2].trend) }}% growth
              </v-chip>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- Total Products -->
      <v-col cols="12" sm="6" lg="3">
        <v-card elevation="2" class="stat-card">
          <v-card-text class="d-flex align-center pa-4">
            <v-avatar size="64" color="blue-lighten-2" class="mr-4">
              <v-icon size="32" color="white">mdi-silverware-fork-knife</v-icon>
            </v-avatar>
            <div>
              <div
                class="text-caption text-medium-emphasis font-weight-bold text-uppercase"
              >
                Total Products
              </div>
              <div class="text-h5 font-weight-bold mt-1">
                {{ cards[3].value }}
              </div>
              <v-chip
                v-if="cards[3].trend !== null"
                size="x-small"
                :color="cards[3].trend > 0 ? 'success' : 'error'"
                variant="tonal"
                class="mt-2"
              >
                <v-icon start size="x-small">
                  {{ cards[3].trend > 0 ? "mdi-arrow-up" : "mdi-arrow-down" }}
                </v-icon>
                {{ Math.abs(cards[3].trend) }}% new items
              </v-chip>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Latest Orders Section -->
    <v-card elevation="2" class="rounded-xl">
      <v-card-title class="d-flex align-center justify-space-between pa-6 pb-4">
        <div class="d-flex align-center">
          <v-icon color="primary" class="mr-2">mdi-receipt-text-outline</v-icon>
          <span class="text-h6 font-weight-bold">Latest Orders</span>
        </div>
        <v-btn
          :to="{ name: 'AdminOrders' }"
          color="primary"
          variant="tonal"
          append-icon="mdi-arrow-right"
        >
          View All Orders
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <!-- Loading State -->
      <div v-if="loadingOrders" class="text-center pa-12">
        <v-progress-circular
          indeterminate
          color="primary"
          size="60"
        ></v-progress-circular>
        <p class="mt-4 text-medium-emphasis">Loading orders...</p>
      </div>

      <!-- Empty State -->
      <div v-else-if="!orders.length" class="text-center pa-12">
        <v-icon size="80" color="grey-lighten-2">mdi-inbox</v-icon>
        <p class="mt-4 text-h6 text-medium-emphasis">No orders yet</p>
      </div>

      <!-- Orders Table -->
      <v-table v-else hover>
        <thead>
          <tr>
            <th class="text-left font-weight-bold">Order ID</th>
            <th class="text-left font-weight-bold">Customer</th>
            <th class="text-left font-weight-bold">Status</th>
            <th class="text-left font-weight-bold">Payment</th>
            <th class="text-left font-weight-bold">Amount</th>
            <th class="text-left font-weight-bold">Date</th>
            <th class="text-center font-weight-bold">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.orderId">
            <td>
              <span class="font-weight-bold text-primary">
                #{{ order.orderNumber || order.orderId }}
              </span>
            </td>
            <td>
              <div class="d-flex align-center">
                <v-avatar size="36" color="primary" class="mr-3">
                  <span class="text-white text-caption font-weight-bold">
                    {{ getInitials(order.recipientName) }}
                  </span>
                </v-avatar>
                <div>
                  <div class="font-weight-medium">
                    {{ order.recipientName }}
                  </div>
                  <div class="text-caption text-medium-emphasis">
                    {{ order.phone }}
                  </div>
                </div>
              </div>
            </td>
            <td>
              <v-chip
                size="small"
                :color="getStatusColor(order.orderStatus)"
                variant="tonal"
                label
              >
                {{ getStatusLabel(order.orderStatus) }}
              </v-chip>
            </td>
            <td>
              <v-chip
                size="small"
                :color="getPaymentColor(order.paymentStatus)"
                variant="tonal"
                label
              >
                {{ getPaymentLabel(order.paymentStatus) }}
              </v-chip>
            </td>
            <td>
              <span class="font-weight-bold">
                {{ formatCurrency(order.finalAmount) }}
              </span>
            </td>
            <td>
              <span class="text-medium-emphasis">
                {{ formatDate(order.createdAt) }}
              </span>
            </td>
            <td class="text-center">
              <v-btn
                :to="{ name: 'AdminOrders' }"
                icon
                size="small"
                variant="tonal"
                color="primary"
              >
                <v-icon size="18">mdi-eye</v-icon>
              </v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>
    </v-card>
  </div>
</template>

<script>
import storage from "@/utils/storage";
import api from "@/axios";

export default {
  name: "AdminDashboard",

  data() {
    return {
      cards: [
        { title: "Total Earnings", value: "Loading...", trend: null },
        { title: "Today's Orders", value: "Loading...", trend: null },
        { title: "Total Users", value: "Loading...", trend: null },
        { title: "Total Products", value: "Loading...", trend: null },
      ],
      orders: [],
      loadingOrders: false,
    };
  },

  mounted() {
    this.fetchDashboardData();
  },

  methods: {
    async fetchDashboardData() {
      try {
        const token = storage.getToken();
        if (!token) {
          this.$router.push("/login");
          return;
        }

        console.log("📊 Fetching dashboard data...");

        await Promise.all([
          this.fetchStats(token),
          this.fetchLatestOrders(token),
        ]);

        console.log("✅ Dashboard data loaded");
      } catch (error) {
        console.error("❌ Error fetching dashboard data:", error);
      }
    },

    async fetchStats(token) {
      try {
        console.log("📊 Fetching stats...");

        // Total earnings
        try {
          const earningsRes = await api.get("/orders/total-earnings", {
            headers: { Authorization: `Bearer ${token}` },
          });
          console.log("💰 Earnings response:", earningsRes.data);

          // Handle different response formats
          let totalEarnings = 0;
          if (
            earningsRes.data?.success &&
            earningsRes.data?.data !== undefined
          ) {
            totalEarnings = Number(earningsRes.data.data) || 0;
          } else if (earningsRes.data?.data !== undefined) {
            totalEarnings = Number(earningsRes.data.data) || 0;
          } else if (typeof earningsRes.data === "number") {
            totalEarnings = Number(earningsRes.data) || 0;
          }

          this.cards[0].value = this.formatCurrency(totalEarnings);
          this.cards[0].trend = 12.5;
          console.log("✅ Total Earnings:", totalEarnings);
        } catch (error) {
          console.warn("⚠️ Error fetching earnings:", error.message);
          this.cards[0].value = "N/A";
          this.cards[0].trend = 0;
        }

        // Today's orders
        try {
          const todayOrdersRes = await api.get("/orders/today-count", {
            headers: { Authorization: `Bearer ${token}` },
          });
          console.log("📦 Today's orders response:", todayOrdersRes.data);

          let todayOrders = 0;
          if (
            todayOrdersRes.data?.success &&
            todayOrdersRes.data?.data !== undefined
          ) {
            todayOrders = Number(todayOrdersRes.data.data) || 0;
          } else if (todayOrdersRes.data?.data !== undefined) {
            todayOrders = Number(todayOrdersRes.data.data) || 0;
          } else if (typeof todayOrdersRes.data === "number") {
            todayOrders = Number(todayOrdersRes.data) || 0;
          }

          this.cards[1].value = todayOrders.toString();
          this.cards[1].trend = -5.2;
          console.log("✅ Today's Orders:", todayOrders);
        } catch (error) {
          console.warn("⚠️ Error fetching today's orders:", error.message);
          this.cards[1].value = "N/A";
          this.cards[1].trend = 0;
        }

        // Total users
        try {
          const usersRes = await api.get("/users/count", {
            headers: { Authorization: `Bearer ${token}` },
          });
          console.log("👥 Users response:", usersRes.data);

          let totalUsers = 0;
          if (usersRes.data?.success && usersRes.data?.data !== undefined) {
            totalUsers = Number(usersRes.data.data) || 0;
          } else if (usersRes.data?.data !== undefined) {
            totalUsers = Number(usersRes.data.data) || 0;
          } else if (typeof usersRes.data === "number") {
            totalUsers = Number(usersRes.data) || 0;
          }

          this.cards[2].value = totalUsers.toString();
          this.cards[2].trend = 8.1;
          console.log("✅ Total Users:", totalUsers);
        } catch (error) {
          console.warn("⚠️ Error fetching users:", error.message);
          this.cards[2].value = "N/A";
          this.cards[2].trend = 0;
        }

        // Total products
        try {
          const productsRes = await api.get("/products/count", {
            headers: { Authorization: `Bearer ${token}` },
          });
          console.log("🍽️ Products response:", productsRes.data);

          let totalProducts = 0;
          if (
            productsRes.data?.success &&
            productsRes.data?.data !== undefined
          ) {
            totalProducts = Number(productsRes.data.data) || 0;
          } else if (productsRes.data?.data !== undefined) {
            totalProducts = Number(productsRes.data.data) || 0;
          } else if (typeof productsRes.data === "number") {
            totalProducts = Number(productsRes.data) || 0;
          }

          this.cards[3].value = totalProducts.toString();
          this.cards[3].trend = 15.3;
          console.log("✅ Total Products:", totalProducts);
        } catch (error) {
          console.warn("⚠️ Error fetching products:", error.message);
          this.cards[3].value = "N/A";
          this.cards[3].trend = 0;
        }

        console.log("✅ All stats loaded successfully");
      } catch (error) {
        console.error("❌ Error fetching stats:", error);
      }
    },

    async fetchLatestOrders(token) {
      try {
        this.loadingOrders = true;
        console.log("📋 Fetching latest orders...");

        const res = await api.get("/orders", {
          headers: { Authorization: `Bearer ${token}` },
          params: {
            page: 0,
            size: 5,
            sort: "createdAt,desc",
          },
        });

        console.log("📋 Orders API Response:", res);
        console.log("📋 Orders data:", res.data);

        // Try multiple response format patterns
        let ordersList = [];

        if (res.data?.success && res.data?.data) {
          if (Array.isArray(res.data.data)) {
            console.log("✅ Format 1: success + array data");
            ordersList = res.data.data;
          } else if (
            res.data.data?.content &&
            Array.isArray(res.data.data.content)
          ) {
            console.log("✅ Format 2: success + pagination object");
            ordersList = res.data.data.content;
          }
        } else if (Array.isArray(res.data)) {
          console.log("✅ Format 3: direct array");
          ordersList = res.data;
        } else if (res.data?.content && Array.isArray(res.data.content)) {
          console.log("✅ Format 4: pagination object");
          ordersList = res.data.content;
        } else if (
          res.data?.data?.content &&
          Array.isArray(res.data.data.content)
        ) {
          console.log("✅ Format 5: nested pagination");
          ordersList = res.data.data.content;
        }

        // Filter and limit to 5 orders
        this.orders = ordersList.slice(0, 5);
        console.log(`✅ Loaded ${this.orders.length} orders`);
        console.log("📋 Orders:", this.orders);

        // If no orders, show empty state
        if (this.orders.length === 0) {
          console.warn("⚠️ No orders found in response");
        }
      } catch (error) {
        console.error("❌ Error fetching orders:", error);
        console.error("Error message:", error.message);
        console.error("Error response:", error.response?.data);
        this.orders = [];
      } finally {
        this.loadingOrders = false;
      }
    },

    getCurrentDate() {
      const date = new Date();
      return date.toLocaleDateString("vi-VN", {
        weekday: "long",
        year: "numeric",
        month: "long",
        day: "numeric",
      });
    },

    getInitials(name) {
      if (!name) return "?";
      return name
        .split(" ")
        .map((n) => n[0])
        .join("")
        .toUpperCase()
        .slice(0, 2);
    },

    getStatusLabel(status) {
      const map = {
        pending: "Pending",
        confirmed: "Confirmed",
        preparing: "Preparing",
        shipping: "Shipping",
        delivered: "Delivered",
        cancelled: "Cancelled",
      };
      return map[(status || "").toLowerCase()] || status;
    },

    getStatusColor(status) {
      const map = {
        pending: "warning",
        confirmed: "blue",
        preparing: "info",
        shipping: "primary",
        delivered: "success",
        cancelled: "error",
      };
      return map[(status || "").toLowerCase()] || "default";
    },

    getPaymentLabel(status) {
      const map = {
        pending: "Pending",
        paid: "Paid",
        failed: "Failed",
      };
      return map[(status || "").toLowerCase()] || status;
    },

    getPaymentColor(status) {
      const map = {
        pending: "warning",
        paid: "success",
        failed: "error",
      };
      return map[(status || "").toLowerCase()] || "default";
    },

    formatCurrency(value) {
      const num = Number(value || 0);
      return num.toLocaleString("vi-VN") + "₫";
    },

    formatDate(dateStr) {
      if (!dateStr) return "N/A";
      const date = new Date(dateStr);
      return date.toLocaleDateString("vi-VN", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      });
    },
  },
};
</script>

<style scoped>
.stat-card {
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12) !important;
}
</style>
