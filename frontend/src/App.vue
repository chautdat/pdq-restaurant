<template>
  <div id="app">
    <!-- ✅ Nếu là layout admin -->
    <div v-if="isAdminLayout">
      <router-view />
    </div>

    <!-- ✅ Nếu là layout người dùng -->
    <div v-else-if="isUserLayout">
      <NavBar />
      <div class="auth-wrapper">
        <div class="auth-inner">
          <router-view />
        </div>
      </div>
      <FooterComponent />
      <Chatbot />
    </div>

    <!-- ✅ Nếu là layout trống (ví dụ: trang login admin) -->
    <div v-else>
      <router-view />
    </div>
  </div>
</template>

<script>
import NavBar from "./components/NavBar.vue";
import FooterComponent from "./components/FooterComponent.vue";
import Chatbot from "@/pages/Chatbot.vue";
import websocketService from "@/services/websocket";
import storage from "@/utils/storage";
import { mapActions, mapState } from "vuex";

export default {
  name: "App",
  components: {
    NavBar,
    FooterComponent,
    Chatbot,
  },

  mounted() {
    this.getFoodsData();
    if (this.user) {
      this.connectWebSocket();
    }
    window.addEventListener("beforeunload", this.disconnectWebSocket);
    if (typeof window !== "undefined" && "Notification" in window) {
      if (Notification.permission === "default") {
        Notification.requestPermission().catch(() => {});
      }
    }
  },

  beforeUnmount() {
    this.disconnectWebSocket();
    window.removeEventListener("beforeunload", this.disconnectWebSocket);
  },

  computed: {
    ...mapState(["admin", "user"]),

    // ✅ Kiểm tra loại layout từ route.meta
    layout() {
      return this.$route.meta.layout || "user";
    },

    isAdminLayout() {
      return this.layout === "admin";
    },

    isUserLayout() {
      return this.layout === "user";
    },
  },

  watch: {
    user(newUser) {
      if (newUser) {
        this.connectWebSocket();
      } else {
        this.disconnectWebSocket();
      }
    },
  },

  methods: {
    ...mapActions(["getFoodsData"]),
    async connectWebSocket() {
      try {
        const token = storage.getToken();
        const email = this.user?.email || this.user?.username;
        if (!email) return;
        await websocketService.connect(token, email);
        console.log("✅ WebSocket connected for:", email);
      } catch (error) {
        console.error("❌ WebSocket connection failed:", error);
      }
    },
    disconnectWebSocket() {
      websocketService.disconnect();
    },
  },
};
</script>

<style>
@import "./assets/css/global_style.css";
</style>
