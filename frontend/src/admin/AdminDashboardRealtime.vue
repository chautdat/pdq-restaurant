<template>
  <v-container fluid class="py-6">
    <v-row class="mb-4" align="center" justify="space-between">
      <v-col cols="12" md="6">
        <v-alert :type="isConnected ? 'success' : 'warning'" border="start" variant="tonal">
          <div class="d-flex align-center">
            <v-icon class="mr-2">{{ isConnected ? 'mdi-wifi' : 'mdi-wifi-off' }}</v-icon>
            <span>
              {{ isConnected ? 'Đang online WebSocket' : 'Mất kết nối, tự thử lại mỗi 5s...' }}
            </span>
          </div>
        </v-alert>
      </v-col>
      <v-col cols="12" md="6" class="text-right">
        <v-btn color="primary" variant="tonal" @click="refresh">
          <v-icon left>mdi-refresh</v-icon> Làm mới
        </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" md="4" lg="3" v-for="order in orders" :key="order.orderId">
        <v-card :class="['order-card', order.highlight && 'pulse']" elevation="6">
          <v-card-title class="d-flex justify-space-between align-center">
            <div>
              <div class="text-subtitle-1 font-weight-bold">Đơn #{{ order.orderId }}</div>
              <div class="text-caption text-medium-emphasis">{{ formatTime(order.timestamp) }}</div>
            </div>
            <v-chip :color="statusColor(order.status)" text-color="white" size="small">{{ order.status }}</v-chip>
          </v-card-title>
          <v-card-text>
            <div class="text-body-2 mb-2">👤 {{ order.userName || 'Khách lẻ' }}</div>
            <div class="text-body-2 mb-2">💳 {{ order.paymentMethod || 'N/A' }}</div>
            <div class="text-h6 font-weight-bold">{{ formatVnd(order.totalAmount) }}</div>
          </v-card-text>
          <v-card-actions class="pb-4 px-4">
            <v-btn size="small" color="success" @click="updateStatus(order, 'CONFIRMED')">Xác nhận</v-btn>
            <v-btn size="small" color="error" variant="tonal" @click="updateStatus(order, 'CANCELLED')">Hủy</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <audio ref="notificationSound" src="/notification.mp3"></audio>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useWebSocket } from '@/composables/useWebSocket'
import storage from '@/utils/storage'

const token = storage.getToken?.() || localStorage.getItem('token')
const { isConnected, subscribe, send } = useWebSocket('http://localhost:3000/ws', {
  headers: token ? { Authorization: `Bearer ${token}` } : {},
})
const orders = ref([])
const notificationSound = ref(null)

const addOrder = (payload) => {
  const order = {
    orderId: payload.orderId,
    userName: payload.userName,
    totalAmount: payload.totalAmount,
    paymentMethod: payload.paymentMethod,
    status: payload.status || 'PENDING',
    timestamp: payload.timestamp ? new Date(payload.timestamp) : new Date(),
    highlight: true
  }
  orders.value = [order, ...orders.value]
  playSound()
  showBrowserNotification(order)
  setTimeout(() => (order.highlight = false), 1200)
}

const updateOrderStatus = (payload) => {
  const found = orders.value.find((o) => o.orderId === payload.orderId)
  if (found) {
    found.status = payload.status
    found.timestamp = payload.timestamp ? new Date(payload.timestamp) : new Date()
    found.highlight = true
    setTimeout(() => (found.highlight = false), 1200)
  }
}

const formatVnd = (val) =>
  new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0)

const formatTime = (date) => new Date(date).toLocaleString('vi-VN')

const statusColor = (status) => {
  const map = {
    PENDING: 'warning',
    CONFIRMED: 'primary',
    PREPARING: 'info',
    READY: 'success',
    DELIVERED: 'success',
    CANCELLED: 'error'
  }
  return map[status] || 'grey'
}

const playSound = () => {
  notificationSound.value?.play?.().catch(() => {})
}

const showBrowserNotification = (order) => {
  if (!('Notification' in window)) return
  if (Notification.permission === 'granted') {
    new Notification('Đơn hàng mới', {
      body: `#${order.orderId} - ${formatVnd(order.totalAmount)}`,
    })
  }
}

const refresh = () => {
  orders.value = [...orders.value]
}

const updateStatus = (order, status) => {
  const payload = { orderId: order.orderId, status }
  send('/app/order/status', payload)
}

onMounted(() => {
  // request notification permission
  if ('Notification' in window && Notification.permission === 'default') {
    Notification.requestPermission()
  }

  subscribe('/topic/orders', (msg) => addOrder(msg))
  subscribe('/topic/orders/status', (msg) => updateOrderStatus(msg))
})
</script>

<style scoped>
.order-card {
  border-radius: 14px;
}

.pulse {
  animation: pulse 0.8s ease 3;
}

@keyframes pulse {
  0% { box-shadow: 0 0 0 0 rgba(76, 175, 80, 0.4); }
  70% { box-shadow: 0 0 0 12px rgba(76, 175, 80, 0); }
  100% { box-shadow: 0 0 0 0 rgba(76, 175, 80, 0); }
}
</style>
