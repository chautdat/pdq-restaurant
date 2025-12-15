<template>
  <v-container fluid class="py-6 kitchen-page">
    <v-row class="mb-4" align="center" justify="space-between">
      <v-col cols="12" md="6">
        <h2 class="text-h5 font-weight-bold">🍳 Kitchen Display</h2>
        <p class="text-body-2 text-medium-emphasis">Nhận đơn theo thời gian thực</p>
      </v-col>
      <v-col cols="12" md="6" class="text-right">
        <v-chip :color="isConnected ? 'success' : 'warning'" text-color="white">
          <v-icon left>{{ isConnected ? 'mdi-wifi' : 'mdi-wifi-off' }}</v-icon>
          {{ isConnected ? 'Online' : 'Đang kết nối lại...' }}
        </v-chip>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" lg="4">
        <SectionColumn
          title="Chờ xử lý"
          color="warning"
          :orders="pendingOrders"
          @move="move"
          @remove="removeOrder"
          :showProgress="false"
          primary-action="BẮT ĐẦU NẤU"
          next-status="PREPARING"
        />
      </v-col>
      <v-col cols="12" lg="4">
        <SectionColumn
          title="Đang nấu"
          color="primary"
          :orders="preparingOrders"
          @move="move"
          @remove="removeOrder"
          :showProgress="true"
          primary-action="HOÀN THÀNH"
          next-status="READY"
        />
      </v-col>
      <v-col cols="12" lg="4">
        <SectionColumn
          title="Sẵn sàng"
          color="success"
          :orders="readyOrders"
          @remove="removeOrder"
          :showProgress="false"
        />
      </v-col>
    </v-row>

    <audio ref="kitchenSound" src="/notification.mp3"></audio>
  </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useWebSocket } from '@/composables/useWebSocket'
import SectionColumn from './partials/SectionColumn.vue'
import storage from '@/utils/storage'

const token = storage.getToken?.() || localStorage.getItem('token')
const { isConnected, subscribe } = useWebSocket('http://localhost:3000/ws', {
  headers: token ? { Authorization: `Bearer ${token}` } : {},
})
const orders = ref([])
const kitchenSound = ref(null)

const pendingOrders = computed(() => orders.value.filter((o) => ['PENDING', 'CONFIRMED'].includes(o.status)))
const preparingOrders = computed(() => orders.value.filter((o) => o.status === 'PREPARING'))
const readyOrders = computed(() => orders.value.filter((o) => o.status === 'READY'))

const addOrder = (msg) => {
  const order = {
    orderId: msg.orderId,
    userName: msg.userName,
    items: msg.items || 0,
    status: msg.status || 'PENDING',
    timestamp: msg.timestamp ? new Date(msg.timestamp) : new Date(),
  }
  orders.value = [order, ...orders.value]
  playSound()
}

const move = (order, status) => {
  order.status = status
}

const removeOrder = (order) => {
  orders.value = orders.value.filter((o) => o !== order)
}

const playSound = () => {
  kitchenSound.value?.play?.().catch(() => {})
}

onMounted(() => {
  subscribe('/topic/kitchen', (msg) => addOrder(msg))
})
</script>

<style scoped>
.kitchen-page {
  background: linear-gradient(135deg, #f5f7fb 0%, #e8f0ff 100%);
  min-height: 100vh;
}
</style>
