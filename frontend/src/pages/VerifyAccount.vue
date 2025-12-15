<template>
  <v-container class="fill-height pa-6">
    <v-row justify="center" align="center">
      <v-col cols="12" sm="8" md="6">
        <v-card elevation="10">
          <v-card-title class="text-center bg-primary pa-6">
            <div class="w-100">
              <v-icon size="64" color="white" class="mb-2">mdi-email-check</v-icon>
              <h2 class="text-h5 text-white">Xác nhận tài khoản</h2>
            </div>
          </v-card-title>

          <v-card-text class="pa-8">
            <!-- Loading -->
            <div v-if="verifying" class="text-center py-8">
              <v-progress-circular
                indeterminate
                color="primary"
                size="64"
              ></v-progress-circular>
              <p class="mt-4 text-subtitle-1">Đang xác nhận...</p>
            </div>

            <!-- Success -->
            <div v-else-if="verified" class="text-center py-8">
              <v-icon size="80" color="success" class="mb-4">
                mdi-check-circle
              </v-icon>
              <h2 class="text-h5 mb-3">Xác nhận thành công! 🎉</h2>
              <p class="text-subtitle-1 mb-6">
                Email đã được xác nhận.<br />
                Bạn có thể đăng nhập ngay.
              </p>
              <v-btn
                color="primary"
                size="large"
                to="/login"
                prepend-icon="mdi-login"
              >
                Đăng nhập
              </v-btn>
            </div>

            <!-- Manual verify -->
            <div v-else>
              <v-alert
                v-if="errorMessage"
                type="error"
                closable
                class="mb-4"
                @click:close="errorMessage = ''"
              >
                {{ errorMessage }}
              </v-alert>

              <div class="mb-6">
                <h3 class="text-h6 mb-2">Xác nhận email</h3>
                <p class="text-body-2 text-medium-emphasis">
                  Nhập mã 6 ký tự từ email
                </p>
              </div>

              <v-text-field
                v-model="verificationCode"
                label="Mã xác nhận"
                placeholder="Nhập 6 ký tự"
                variant="outlined"
                maxlength="6"
                counter
                prepend-inner-icon="mdi-key"
                class="mb-4"
                @keyup.enter="verifyManual"
              ></v-text-field>

              <v-btn
                color="primary"
                block
                size="large"
                :loading="loading"
                :disabled="!verificationCode || verificationCode.length !== 6"
                @click="verifyManual"
                class="mb-3"
              >
                Xác nhận
              </v-btn>

              <div class="text-center">
                <p class="text-body-2 mb-2">Không nhận được email?</p>
                <v-btn
                  variant="text"
                  color="primary"
                  :loading="resending"
                  @click="showResendDialog = true"
                >
                  Gửi lại
                </v-btn>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Resend Dialog -->
    <v-dialog v-model="showResendDialog" max-width="400">
      <v-card>
        <v-card-title>Gửi lại email</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="resendEmail"
            label="Email"
            type="email"
            variant="outlined"
          ></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="showResendDialog = false">Hủy</v-btn>
          <v-btn
            color="primary"
            :loading="resending"
            @click="resendVerification"
          >
            Gửi
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import api from "@/axios";

const route = useRoute();

const verifying = ref(false);
const verified = ref(false);
const errorMessage = ref("");
const verificationCode = ref("");
const loading = ref(false);
const showResendDialog = ref(false);
const resendEmail = ref("");
const resending = ref(false);

onMounted(() => {
  const token = route.query.token;
  if (token) {
    verifyWithToken(token);
  }
});

async function verifyWithToken(token) {
  verifying.value = true;
  errorMessage.value = "";
  try {
    await api.get("/users/verify-account", { params: { token } });
    verified.value = true;
  } catch (error) {
    errorMessage.value =
      error.response?.data?.message || "Xác nhận thất bại";
  } finally {
    verifying.value = false;
  }
}

async function verifyManual() {
  if (!verificationCode.value || verificationCode.value.length !== 6) return;

  loading.value = true;
  errorMessage.value = "";
  try {
    await api.post("/users/verify-code", { code: verificationCode.value });
    verified.value = true;
  } catch (error) {
    errorMessage.value = error.response?.data?.message || "Mã không đúng";
  } finally {
    loading.value = false;
  }
}

async function resendVerification() {
  if (!resendEmail.value) return;
  resending.value = true;
  try {
    await api.post("/users/resend-verification", {
      email: resendEmail.value,
    });
    showResendDialog.value = false;
    alert("Email đã được gửi lại!");
  } catch (error) {
    alert(error.response?.data?.message || "Gửi lại thất bại");
  } finally {
    resending.value = false;
  }
}
</script>

<style scoped>
.fill-height {
  min-height: 100vh;
  background: linear-gradient(135deg, #dff8e8 0%, #c7f2d8 100%);
}
</style>
