<template>
  <v-container fluid class="py-6 kitchen-page">
    <v-row class="mb-4" align="center" justify="space-between">
      <v-col cols="12" md="6">
        <h2 class="text-h5 font-weight-bold">🍳 Kitchen Display System</h2>
        <p class="text-body-2 text-medium-emphasis">
          Nhận đơn theo thời gian thực
        </p>
      </v-col>
      <v-col cols="12" md="6" class="text-right">
        <v-chip
          :color="isConnected ? 'success' : 'error'"
          text-color="white"
          class="mr-2"
        >
          <v-icon left>
            {{ isConnected ? "mdi-wifi" : "mdi-wifi-off" }}
          </v-icon>
          {{ isConnected ? "🟢 Online" : "🔴 Disconnected" }}
        </v-chip>
        <v-chip color="info" text-color="white">
          <v-icon left>mdi-receipt</v-icon>
          Total: {{ orders.length }} orders
        </v-chip>
      </v-col>
    </v-row>

    <!-- Status Summary -->
    <v-row class="mb-6">
      <v-col cols="12" sm="4">
        <v-card color="warning-lighten-5" class="text-center pa-4">
          <div class="text-h6 font-weight-bold text-warning">⏳ Waiting</div>
          <div class="text-h4 font-weight-bold">{{ pendingOrders.length }}</div>
        </v-card>
      </v-col>
      <v-col cols="12" sm="4">
        <v-card color="primary-lighten-5" class="text-center pa-4">
          <div class="text-h6 font-weight-bold text-primary">🔥 Preparing</div>
          <div class="text-h4 font-weight-bold">
            {{ preparingOrders.length }}
          </div>
        </v-card>
      </v-col>
      <v-col cols="12" sm="4">
        <v-card color="success-lighten-5" class="text-center pa-4">
          <div class="text-h6 font-weight-bold text-success">✅ Ready</div>
          <div class="text-h4 font-weight-bold">{{ readyOrders.length }}</div>
        </v-card>
      </v-col>
    </v-row>

    <!-- Orders Display -->
    <v-row>
      <!-- Pending Orders -->
      <v-col cols="12" lg="4">
        <v-card color="warning-lighten-5" class="sticky-column">
          <v-card-title class="text-warning font-weight-bold">
            ⏳ Chờ xử lý ({{ pendingOrders.length }})
          </v-card-title>
          <v-divider></v-divider>
          <v-card-text class="pa-0">
            <v-list v-if="pendingOrders.length > 0" lines="one">
              <v-list-item
                v-for="order in pendingOrders"
                :key="order.orderId"
                class="mb-2"
              >
                <OrderCard
                  :order="order"
                  status="PENDING"
                  @start-cooking="startCooking(order)"
                  @remove="removeOrder(order)"
                />
              </v-list-item>
            </v-list>
            <div v-else class="text-center pa-6 text-medium-emphasis">
              <v-icon size="48" color="grey-lighten-2">mdi-inbox</v-icon>
              <p class="mt-2">Không có đơn chờ xử lý</p>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- Preparing Orders -->
      <v-col cols="12" lg="4">
        <v-card color="primary-lighten-5" class="sticky-column">
          <v-card-title class="text-primary font-weight-bold">
            🔥 Đang nấu ({{ preparingOrders.length }})
          </v-card-title>
          <v-divider></v-divider>
          <v-card-text class="pa-0">
            <v-list v-if="preparingOrders.length > 0" lines="one">
              <v-list-item
                v-for="order in preparingOrders"
                :key="order.orderId"
                class="mb-2"
              >
                <OrderCard
                  :order="order"
                  status="PREPARING"
                  @complete="completeOrder(order)"
                  @remove="removeOrder(order)"
                />
              </v-list-item>
            </v-list>
            <div v-else class="text-center pa-6 text-medium-emphasis">
              <v-icon size="48" color="grey-lighten-2">mdi-pot-mix</v-icon>
              <p class="mt-2">Không có đơn đang nấu</p>
            </div>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- Ready Orders -->
      <v-col cols="12" lg="4">
        <v-card color="success-lighten-5" class="sticky-column">
          <v-card-title class="text-success font-weight-bold">
            ✅ Sẵn sàng ({{ readyOrders.length }})
          </v-card-title>
          <v-divider></v-divider>
          <v-card-text class="pa-0">
            <v-list v-if="readyOrders.length > 0" lines="one">
              <v-list-item
                v-for="order in readyOrders"
                :key="order.orderId"
                class="mb-2"
              >
                <OrderCard
                  :order="order"
                  status="READY"
                  @remove="removeOrder(order)"
                />
              </v-list-item>
            </v-list>
            <div v-else class="text-center pa-6 text-medium-emphasis">
              <v-icon size="48" color="grey-lighten-2">mdi-check-circle</v-icon>
              <p class="mt-2">Không có đơn sẵn sàng</p>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Audio notification -->
    <audio ref="kitchenSound" src="/notification.mp3"></audio>

    <!-- Debug Panel (Development) -->
    <v-card class="mt-6" color="grey-lighten-5">
      <v-card-title>🔧 Debug Panel</v-card-title>
      <v-card-text>
        <p>
          <strong>WebSocket Status:</strong>
          {{ isConnected ? "✅ Connected" : "❌ Disconnected" }}
        </p>
        <p><strong>Total Orders:</strong> {{ orders.length }}</p>
        <p>
          <strong>Pending:</strong> {{ pendingOrders.length }} |
          <strong>Preparing:</strong> {{ preparingOrders.length }} |
          <strong>Ready:</strong> {{ readyOrders.length }}
        </p>
        <v-btn color="primary" size="small" @click="testOrder" class="mt-2">
          🧪 Add Test Order
        </v-btn>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import storage from "@/utils/storage";
import api from "@/axios";
import OrderCard from "./partials/OrderCard.vue";

export default {
  name: "KitchenDisplay",
  components: {
    OrderCard,
  },

  data() {
    return {
      orders: [],
      isConnected: false,
      ws: null,
      kitchenSound: null,
      reconnectAttempts: 0,
      maxReconnectAttempts: 5,
    };
  },

  computed: {
    pendingOrders() {
      return this.orders.filter(
        (o) =>
          o.status === "PENDING" ||
          o.status === "CONFIRMED" ||
          o.status === "PENDING_CONFIRMATION"
      );
    },

    preparingOrders() {
      return this.orders.filter((o) => o.status === "PREPARING");
    },

    readyOrders() {
      return this.orders.filter(
        (o) => o.status === "READY" || o.status === "PREPARED"
      );
    },
  },

  mounted() {
    console.log("🍳 Kitchen Display mounted");
    this.initWebSocket();
    this.fetchOrdersFromAPI();
  },

  unmounted() {
    console.log("🍳 Kitchen Display unmounted");
    this.closeWebSocket();
  },

  methods: {
    initWebSocket() {
      const token = storage.getToken();
      if (!token) {
        console.error("❌ No token found");
        return;
      }

      const wsUrl = `${
        import.meta.env.VITE_API_URL || "http://localhost:3000"
      }/ws`;
      console.log("🔌 Connecting WebSocket to:", wsUrl);

      try {
        this.ws = new WebSocket(wsUrl);

        this.ws.onopen = () => {
          console.log("✅ WebSocket connected");
          this.isConnected = true;
          this.reconnectAttempts = 0;

          // Subscribe to kitchen topic
          const subscribeMsg = {
            type: "SUBSCRIBE",
            topic: "/topic/kitchen",
            token: token,
          };
          this.ws.send(JSON.stringify(subscribeMsg));
          console.log("📡 Subscribed to /topic/kitchen");
        };

        this.ws.onmessage = (event) => {
          console.log("📨 WebSocket message received:", event.data);
          try {
            const message = JSON.parse(event.data);
            this.handleOrderMessage(message);
          } catch (error) {
            console.error("❌ Error parsing WebSocket message:", error);
          }
        };

        this.ws.onerror = (error) => {
          console.error("❌ WebSocket error:", error);
          this.isConnected = false;
        };

        this.ws.onclose = () => {
          console.warn("⚠️ WebSocket disconnected");
          this.isConnected = false;
          this.attemptReconnect();
        };
      } catch (error) {
        console.error("❌ Error initializing WebSocket:", error);
        this.attemptReconnect();
      }
    },

    attemptReconnect() {
      this.reconnectAttempts++;
      if (this.reconnectAttempts <= this.maxReconnectAttempts) {
        const delay = Math.min(
          1000 * Math.pow(2, this.reconnectAttempts),
          30000
        );
        console.log(
          `⏳ Reconnecting in ${delay}ms (attempt ${this.reconnectAttempts})`
        );
        setTimeout(() => this.initWebSocket(), delay);
      } else {
        console.error("❌ Max reconnect attempts reached");
      }
    },

    closeWebSocket() {
      if (this.ws) {
        this.ws.close();
        this.ws = null;
      }
    },

    handleOrderMessage(message) {
      console.log("📦 Processing order message:", message);

      if (message.type === "ORDER_CREATED" || message.type === "NEW_ORDER") {
        const order = {
          orderId: message.orderId || message.id,
          orderNumber: message.orderNumber,
          recipientName:
            message.recipientName || message.customerName || "Unknown",
          phone: message.phone || "",
          items: message.items || [],
          itemCount: Array.isArray(message.items)
            ? message.items.reduce((sum, item) => sum + (item.quantity || 1), 0)
            : 0,
          status: message.status || "PENDING",
          timestamp: new Date(message.timestamp || Date.now()),
          createdAt: new Date(message.createdAt || Date.now()),
        };

        // Check if order already exists
        if (!this.orders.find((o) => o.orderId === order.orderId)) {
          this.orders.unshift(order);
          console.log(`✅ New order added: #${order.orderNumber}`);
          this.playSound();
        } else {
          console.log(`⚠️ Order already exists: #${order.orderNumber}`);
        }
      }
    },

    async fetchOrdersFromAPI() {
      try {
        const token = storage.getToken();
        if (!token) return;

        console.log("📋 Fetching orders from API...");
        const res = await api.get("/orders", {
          headers: { Authorization: `Bearer ${token}` },
          params: {
            page: 0,
            size: 50,
            status: ["PENDING", "CONFIRMED", "PREPARING"],
          },
        });

        let ordersList = [];
        if (res.data?.data?.content) {
          ordersList = res.data.data.content;
        } else if (res.data?.content) {
          ordersList = res.data.content;
        } else if (Array.isArray(res.data?.data)) {
          ordersList = res.data.data;
        } else if (Array.isArray(res.data)) {
          ordersList = res.data;
        }

        this.orders = ordersList.map((o) => ({
          orderId: o.orderId || o.id,
          orderNumber: o.orderNumber,
          recipientName: o.recipientName || o.customerName || "Unknown",
          phone: o.phone || "",
          items: o.items || [],
          itemCount: Array.isArray(o.items)
            ? o.items.reduce((sum, item) => sum + (item.quantity || 1), 0)
            : 0,
          status: o.orderStatus || o.status || "PENDING",
          timestamp: new Date(o.timestamp || o.createdAt || Date.now()),
          createdAt: new Date(o.createdAt || Date.now()),
        }));

        console.log(`✅ Loaded ${this.orders.length} orders from API`);
      } catch (error) {
        console.error("❌ Error fetching orders:", error);
      }
    },

    startCooking(order) {
      order.status = "PREPARING";
      console.log(`🔥 Started cooking order #${order.orderNumber}`);
      this.playSound();
    },

    completeOrder(order) {
      order.status = "READY";
      console.log(`✅ Order ready #${order.orderNumber}`);
      this.playSound();
    },

    removeOrder(order) {
      this.orders = this.orders.filter((o) => o.orderId !== order.orderId);
      console.log(`🗑️ Removed order #${order.orderNumber}`);
    },

    playSound() {
      const audio = this.$refs.kitchenSound;
      if (audio) {
        audio.play().catch((error) => {
          console.warn("⚠️ Could not play notification sound:", error);
        });
      }
    },

    testOrder() {
      const testOrder = {
        orderId: "TEST-" + Date.now(),
        orderNumber: "TEST-" + Math.floor(Math.random() * 1000),
        recipientName: "Test Customer",
        phone: "0123456789",
        items: [
          { name: "Cơm gà", quantity: 1 },
          { name: "Nước cam", quantity: 1 },
        ],
        itemCount: 2,
        status: "PENDING",
        timestamp: new Date(),
        createdAt: new Date(),
      };
      this.orders.unshift(testOrder);
      console.log("🧪 Test order added:", testOrder);
      this.playSound();
    },
  },
};
</script>

<style scoped>
.kitchen-page {
  background: linear-gradient(135deg, #f5f7fb 0%, #e8f0ff 100%);
  min-height: 100vh;
}

.sticky-column {
  position: sticky;
  top: 20px;
  max-height: calc(100vh - 40px);
  overflow-y: auto;
}

.text-warning {
  color: #ff9800;
}

.text-primary {
  color: #2196f3;
}

.text-success {
  color: #4caf50;
}

.warning-lighten-5 {
  background-color: #fff3e0;
}

.primary-lighten-5 {
  background-color: #e3f2fd;
}

.success-lighten-5 {
  background-color: #e8f5e9;
}
</style>
