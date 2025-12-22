<template>
  <div class="address-selector">
    <!-- ===== CHỌN ĐỊA CHỈ ĐÃ LƯU ===== -->
    <div v-if="savedAddresses.length > 0" class="saved-addresses-section">
      <div class="form-group">
        <label class="form-label">
          <i class="fas fa-bookmark"></i>
          Địa chỉ đã lưu
        </label>
        <select
          v-model="selectedAddressId"
          @change="onAddressChange"
          class="form-select"
        >
          <option value="">-- Chọn địa chỉ hoặc nhập mới --</option>
          <option
            v-for="addr in savedAddresses"
            :key="addr.id"
            :value="addr.id"
          >
            {{ addr.recipientName }} - {{ addr.phone }} -
            {{ formatSavedAddress(addr) }}
            <span v-if="addr.isDefault"> ⭐ Mặc định</span>
          </option>
        </select>
      </div>

      <!-- Hiển thị địa chỉ đã chọn -->
      <div v-if="selectedSavedAddress" class="selected-address-preview">
        <div class="address-card-preview">
          <div class="address-info">
            <h4>{{ selectedSavedAddress.recipientName }}</h4>
            <p class="phone">📞 {{ selectedSavedAddress.phone }}</p>
            <p class="address">
              📍 {{ formatSavedAddress(selectedSavedAddress) }}
            </p>
          </div>
          <button
            type="button"
            class="btn-change-address"
            @click="clearSelectedAddress"
          >
            Đổi địa chỉ
          </button>
        </div>
      </div>

      <div v-if="!hasSavedSelection" class="divider">
        <span>HOẶC</span>
      </div>
    </div>

    <!-- ===== FORM NHẬP MỚI (Chỉ hiện khi CHƯA chọn địa chỉ đã lưu) ===== -->
    <div v-if="!hasSavedSelection" class="new-address-form">
      <!-- City/Province Selector -->
      <div class="form-group">
        <label class="form-label">Tỉnh/Thành phố</label>
        <div class="select-wrapper" @click="openCityModal">
          <input
            type="text"
            :value="selectedCityName"
            placeholder="Chọn Tỉnh/Thành phố"
            class="form-input"
            readonly
            :class="{ error: errors.city }"
          />
          <span class="select-arrow">▼</span>
        </div>
        <p v-if="errors.city" class="error-message">{{ errors.city }}</p>
      </div>

      <!-- District Selector -->
      <div class="form-group">
        <label class="form-label">Quận/Huyện</label>
        <div class="select-wrapper" @click="openDistrictModal">
          <input
            type="text"
            :value="selectedDistrictName"
            placeholder="Chọn Quận/Huyện"
            class="form-input"
            readonly
            :disabled="!selectedCity"
            :class="{ error: errors.district }"
          />
          <span class="select-arrow">▼</span>
        </div>
        <p v-if="errors.district" class="error-message">
          {{ errors.district }}
        </p>
      </div>

      <!-- Ward Selector -->
      <div class="form-group">
        <label class="form-label">Phường/Xã</label>
        <div class="select-wrapper" @click="openWardModal">
          <input
            type="text"
            :value="selectedWardName"
            placeholder="Chọn Phường/Xã"
            class="form-input"
            readonly
            :disabled="!selectedDistrict"
            :class="{ error: errors.ward }"
          />
          <span class="select-arrow">▼</span>
        </div>
        <p v-if="errors.ward" class="error-message">{{ errors.ward }}</p>
      </div>

      <!-- Address Detail -->
      <div class="form-group">
        <label class="form-label">Địa chỉ chi tiết (Số nhà, tên đường)</label>
        <input
          type="text"
          v-model="addressDetail"
          @input="updateFullAddress"
          placeholder="VD: 123 Nguyễn Văn A"
          class="form-input"
          :class="{ error: errors.detail }"
        />
        <p v-if="errors.detail" class="error-message">{{ errors.detail }}</p>
      </div>
    </div>

    <!-- Modal Overlay -->
    <transition name="modal-fade">
      <div v-if="showModal" class="modal-overlay" @click="closeModal">
        <div class="modal-container" @click.stop>
          <!-- Modal Header -->
          <div class="modal-header">
            <h3>{{ modalTitle }}</h3>
            <button type="button" class="modal-close" @click="closeModal">
              ✕
            </button>
          </div>

          <!-- Search Box -->
          <div class="modal-search">
            <input
              ref="searchInput"
              type="text"
              v-model="searchQuery"
              :placeholder="searchPlaceholder"
              class="search-input"
              @input="filterItems"
            />
            <span class="search-icon">🔍</span>
          </div>

          <!-- Loading -->
          <div v-if="loading" class="modal-loading">
            <div class="spinner"></div>
            <p>Đang tải...</p>
          </div>

          <!-- Items List -->
          <div v-else class="modal-list">
            <div
              v-for="item in filteredItems"
              :key="item.code"
              class="modal-item"
              @click="selectItem(item)"
            >
              <span class="item-name">{{ item.name }}</span>
              <span class="item-check">✓</span>
            </div>

            <div v-if="filteredItems.length === 0" class="modal-empty">
              <span class="empty-icon">🔍</span>
              <p>Không tìm thấy kết quả</p>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import api from "@/axios";
import storage from "@/utils/storage";

export default {
  name: "AddressSelector",

  props: {
    modelValue: {
      type: String,
      default: "",
    },
    errors: {
      type: Object,
      default: () => ({
        city: "",
        district: "",
        ward: "",
        detail: "",
      }),
    },
  },

  emits: [
    "update:modelValue",
    "update:city",
    "update:district",
    "update:ward",
    "address-selected",
  ],

  data() {
    return {
      apiUrl: "https://provinces.open-api.vn/api",
      savedAddresses: [],
      selectedAddressId: "",
      selectedSavedAddress: null,
      selectedCity: null,
      selectedDistrict: null,
      selectedWard: null,
      addressDetail: "",
      showModal: false,
      modalType: "",
      loading: false,
      searchQuery: "",
      cities: [],
      districts: [],
      wards: [],
      filteredItems: [],
      citiesLoaded: false,
      pendingExternalAddress: null,
    };
  },

  computed: {
    hasSavedSelection() {
      return !!(this.selectedSavedAddress || this.selectedAddressId);
    },
    selectedCityName() {
      return this.selectedCity?.name || "";
    },
    selectedDistrictName() {
      return this.selectedDistrict?.name || "";
    },
    selectedWardName() {
      return this.selectedWard?.name || "";
    },
    modalTitle() {
      const titles = {
        city: "Chọn Tỉnh/Thành phố",
        district: "Chọn Quận/Huyện",
        ward: "Chọn Phường/Xã",
      };
      return titles[this.modalType] || "";
    },
    searchPlaceholder() {
      const placeholders = {
        city: "Tìm tỉnh, thành phố...",
        district: "Tìm quận, huyện...",
        ward: "Tìm phường, xã...",
      };
      return placeholders[this.modalType] || "";
    },
  },

  async mounted() {
    console.log("🚀 AddressSelector mounted");
    await this.loadCities();
    if (this.pendingExternalAddress) {
      const tmp = { ...this.pendingExternalAddress };
      this.pendingExternalAddress = null;
      await this.applyExternalAddress(tmp);
    }
    await this.loadSavedAddresses();
  },

  methods: {
    normalize(str = "") {
      return str
        .toString()
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .trim();
    },

    findByName(list = [], name = "") {
      if (!name) return null;
      const target = this.normalize(name);
      return (
        list.find((i) => this.normalize(i.name) === target) ||
        list.find((i) => this.normalize(i.name).includes(target)) ||
        null
      );
    },

    async ensureCitiesLoaded() {
      if (!this.citiesLoaded) {
        await this.loadCities();
      }
      return true;
    },

    // Cho phép component cha đẩy dữ liệu city/district/ward vào selector
    async applyExternalAddress({
      city,
      district,
      ward,
      addressLine,
    } = {}) {
      try {
        // Nếu cities chưa sẵn sàng, lưu pending và sẽ apply sau
        if (!this.citiesLoaded || !this.cities.length) {
          this.pendingExternalAddress = { city, district, ward, addressLine };
          await this.ensureCitiesLoaded();
          if (this.pendingExternalAddress) {
            const tmp = { ...this.pendingExternalAddress };
            this.pendingExternalAddress = null;
            return await this.applyExternalAddress(tmp);
          }
        }

        // City
        if (city) {
          const cityObj = this.findByName(this.cities, city);
          if (cityObj) {
            this.selectedCity = cityObj;
            await this.loadDistricts(cityObj.code);
          } else {
            // Nếu không khớp với dữ liệu API, vẫn set tạm để hiển thị/validate
            this.selectedCity = { name: city, code: null };
          }
        }

        // District
        if (district) {
          if (this.districts.length) {
            const distObj = this.findByName(this.districts, district);
            if (distObj) {
              this.selectedDistrict = distObj;
              await this.loadWards(distObj.code);
            } else {
              this.selectedDistrict = { name: district, code: null };
            }
          } else {
            this.selectedDistrict = { name: district, code: null };
          }
        }

        // Ward
        if (ward) {
          if (this.wards.length) {
            const wardObj = this.findByName(this.wards, ward);
            this.selectedWard = wardObj || { name: ward, code: null };
          } else {
            this.selectedWard = { name: ward, code: null };
          }
        }

        if (addressLine) {
          this.addressDetail = addressLine;
          this.$emit("update:modelValue", addressLine);
        }

        // Emit ra để parent không còn lỗi validation
        this.$emit("update:city", this.selectedCity?.name || "");
        this.$emit("update:district", this.selectedDistrict?.name || "");
        this.$emit("update:ward", this.selectedWard?.name || "");
      } catch (err) {
        console.error("applyExternalAddress error:", err);
      }
    },

    getAddressId(addr) {
      return addr?.id || addr?.addressId || addr?.address_id || addr?._id;
    },

    normalizeAddress(addr = {}) {
      return {
        id: this.getAddressId(addr),
        recipientName:
          addr.recipientName ||
          addr.fullName ||
          addr.name ||
          addr.recipient ||
          addr.recipient_name ||
          "",
        phone:
          addr.phone ||
          addr.phoneNumber ||
          addr.phone_number ||
          addr.recipientPhone ||
          addr.mobile ||
          addr.phoneNo ||
          addr.phone_no ||
          "",
        addressLine:
          addr.addressLine ||
          addr.address ||
          addr.address_line ||
          addr.street ||
          addr.addressDetail ||
          addr.address_detail ||
          addr.detailAddress ||
          addr.detail_address ||
          "",
        ward: addr.ward || addr.wardName || addr.ward_name || "",
        district:
          addr.district || addr.districtName || addr.district_name || "",
        city: addr.city || addr.cityName || addr.city_name || addr.province || "",
        isDefault:
          addr.isDefault ||
          addr.default ||
          addr.defaultAddress ||
          addr.default_address ||
          addr.is_default ||
          false,
        note: addr.note,
      };
    },

    emitSelection(addr) {
      this.$emit("address-selected", {
        recipientName: addr.recipientName || "",
        phone: addr.phone || "",
        addressLine: addr.addressLine || "",
        city: addr.city || "",
        district: addr.district || "",
        ward: addr.ward || "",
      });

      // Cập nhật v-model và các field liên quan để parent không còn lỗi
      const full = this.formatSavedAddress(addr);
      this.$emit("update:modelValue", full || addr.addressLine || "");
      this.$emit("update:city", addr.city || "");
      this.$emit("update:district", addr.district || "");
      this.$emit("update:ward", addr.ward || "");
    },

    async loadSavedAddresses() {
      try {
        const token = storage.getToken();

        if (!token) {
          console.log("⚠️ No token found");
          return;
        }

        console.log("🔍 Loading saved addresses...");

        const res = await api.get("/addresses", {
          headers: { Authorization: `Bearer ${token}` },
        });

        console.log("📦 API Response:", res.data);

        let addressList = [];

        if (res.data?.data) {
          const data = res.data.data;

          if (Array.isArray(data)) {
            addressList = data;
          } else if (data.content && Array.isArray(data.content)) {
            addressList = data.content;
          } else if (typeof data === "object" && data.id) {
            addressList = [data];
          }
        }

        this.savedAddresses = addressList.map((a) => this.normalizeAddress(a));
        console.log("✅ Saved Addresses:", this.savedAddresses.length);

        if (this.savedAddresses.length > 0) {
          const defaultAddr = this.savedAddresses.find((a) => a.isDefault);
          if (defaultAddr) {
            console.log("🌟 Default address:", defaultAddr);
            this.selectedAddressId = String(defaultAddr.id);
            this.onAddressChange();
          }
        }
      } catch (err) {
        console.error("❌ Error loading addresses:", err);
        this.savedAddresses = [];
      }
    },

    onAddressChange() {
      if (!this.selectedAddressId) {
        this.selectedSavedAddress = null;
        return;
      }

      const addr = this.savedAddresses.find(
        (a) => String(a.id) === String(this.selectedAddressId)
      );

      if (addr) {
        this.selectedSavedAddress = this.normalizeAddress(addr);
        console.log("✅ Selected address:", addr);

        // ✅ EMIT ĐẦY ĐỦ THÔNG TIN + cập nhật parent v-model
        this.emitSelection(this.selectedSavedAddress);

        // ✅ CẬP NHẬT NỘI BỘ ĐỂ VALIDATION PASS
        this.selectedCity = this.selectedSavedAddress.city
          ? { name: this.selectedSavedAddress.city }
          : null;
        this.selectedDistrict = this.selectedSavedAddress.district
          ? { name: this.selectedSavedAddress.district }
          : null;
        this.selectedWard = this.selectedSavedAddress.ward
          ? { name: this.selectedSavedAddress.ward }
          : null;
        this.addressDetail = this.selectedSavedAddress.addressLine || "";
      } else {
        // Nếu tìm không ra nhưng có id, vẫn coi là đã chọn để không ép nhập form
        this.selectedSavedAddress = { id: this.selectedAddressId };
      }
    },

    clearSelectedAddress() {
      this.selectedAddressId = "";
      this.selectedSavedAddress = null;
      // reset fields so form hiển thị lại và người dùng nhập mới
      this.selectedCity = null;
      this.selectedDistrict = null;
      this.selectedWard = null;
      this.addressDetail = "";
      this.$emit("update:modelValue", "");
      this.$emit("update:city", "");
      this.$emit("update:district", "");
      this.$emit("update:ward", "");
      this.$emit("address-selected", {
        recipientName: "",
        phone: "",
        addressLine: "",
        city: "",
        district: "",
        ward: "",
      });
    },

    formatSavedAddress(addr) {
      if (!addr) return "";
      const parts = [
        addr.addressLine,
        addr.ward,
        addr.district,
        addr.city,
      ].filter(Boolean);
      return parts.join(", ");
    },

    async loadCities() {
      try {
        this.loading = true;
        const response = await fetch(`${this.apiUrl}/p/`);
        this.cities = await response.json();
        this.citiesLoaded = true;
      } catch (error) {
        console.error("❌ Error loading cities:", error);
        this.cities = [];
        this.citiesLoaded = false;
      } finally {
        this.loading = false;
      }
    },

    async loadDistricts(cityCode) {
      try {
        this.loading = true;
        const response = await fetch(`${this.apiUrl}/p/${cityCode}?depth=2`);
        const data = await response.json();
        this.districts = data.districts || [];
      } catch (error) {
        console.error("❌ Error loading districts:", error);
        this.districts = [];
      } finally {
        this.loading = false;
      }
    },

    async loadWards(districtCode) {
      try {
        this.loading = true;
        const response = await fetch(
          `${this.apiUrl}/d/${districtCode}?depth=2`
        );
        const data = await response.json();
        this.wards = data.wards || [];
      } catch (error) {
        console.error("❌ Error loading wards:", error);
        this.wards = [];
      } finally {
        this.loading = false;
      }
    },

    async openCityModal() {
      this.modalType = "city";
      this.searchQuery = "";
      this.filteredItems = this.cities;
      this.showModal = true;
      await this.$nextTick();
      this.$refs.searchInput?.focus();
    },

    async openDistrictModal() {
      if (!this.selectedCity) return;
      this.modalType = "district";
      this.searchQuery = "";
      this.filteredItems = this.districts;
      this.showModal = true;
      await this.$nextTick();
      this.$refs.searchInput?.focus();
    },

    async openWardModal() {
      if (!this.selectedDistrict) return;
      this.modalType = "ward";
      this.searchQuery = "";
      this.filteredItems = this.wards;
      this.showModal = true;
      await this.$nextTick();
      this.$refs.searchInput?.focus();
    },

    closeModal() {
      this.showModal = false;
      this.searchQuery = "";
    },

    async selectItem(item) {
      if (this.modalType === "city") {
        this.selectedCity = item;
        this.selectedDistrict = null;
        this.selectedWard = null;
        this.districts = [];
        this.wards = [];
        await this.loadDistricts(item.code);
      } else if (this.modalType === "district") {
        this.selectedDistrict = item;
        this.selectedWard = null;
        this.wards = [];
        await this.loadWards(item.code);
      } else if (this.modalType === "ward") {
        this.selectedWard = item;
      }
      this.closeModal();
      this.updateFullAddress();
    },

    filterItems() {
      const query = this.searchQuery.toLowerCase().trim();
      let sourceData = [];
      if (this.modalType === "city") sourceData = this.cities;
      else if (this.modalType === "district") sourceData = this.districts;
      else if (this.modalType === "ward") sourceData = this.wards;

      if (!query) {
        this.filteredItems = sourceData;
        return;
      }

      this.filteredItems = sourceData.filter((item) =>
        item.name.toLowerCase().includes(query)
      );
    },

    updateFullAddress() {
      // Nếu đang dùng địa chỉ đã lưu thì không override form nhập mới
      if (this.selectedSavedAddress) return;
      const parts = [];
      if (this.addressDetail) parts.push(this.addressDetail);
      if (this.selectedWard) parts.push(this.selectedWard.name);
      if (this.selectedDistrict) parts.push(this.selectedDistrict.name);
      if (this.selectedCity) parts.push(this.selectedCity.name);
      const fullAddress = parts.join(", ");
      this.$emit("update:modelValue", fullAddress);
      this.$emit("update:city", this.selectedCity?.name || "");
      this.$emit("update:district", this.selectedDistrict?.name || "");
      this.$emit("update:ward", this.selectedWard?.name || "");
    },

    validate() {
      if (this.hasSavedSelection) {
        return {
          isValid: true,
          errors: { city: "", district: "", ward: "", detail: "" },
        };
      }

      const errors = {
        city: "",
        district: "",
        ward: "",
        detail: "",
      };

      if (!this.selectedCity) errors.city = "Vui lòng chọn Tỉnh/Thành phố";
      if (!this.selectedDistrict) errors.district = "Vui lòng chọn Quận/Huyện";
      if (!this.selectedWard) errors.ward = "Vui lòng chọn Phường/Xã";
      if (!this.addressDetail) errors.detail = "Vui lòng nhập địa chỉ chi tiết";

      return {
        isValid: Object.values(errors).every((e) => !e),
        errors,
      };
    },
  },
};
</script>

<style scoped>
.saved-addresses-section {
  margin-bottom: 2rem;
}

.form-select {
  width: 100%;
  padding: 0.95rem 1.25rem;
  border: 1.5px solid #e0e0e0;
  border-radius: 10px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  background: white;
}

.form-select:focus {
  outline: none;
  border-color: #10b981;
  background: #f9fffe;
}

.selected-address-preview {
  margin-top: 1rem;
}

.address-card-preview {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.25rem;
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
  border: 2px solid #10b981;
  border-radius: 12px;
}

.address-info h4 {
  font-size: 1.1rem;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 0.5rem;
}

.address-info p {
  font-size: 0.95rem;
  color: #666;
  margin: 0.25rem 0;
}

.btn-change-address {
  padding: 0.65rem 1.5rem;
  background: white;
  border: 1.5px solid #10b981;
  border-radius: 8px;
  color: #10b981;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.btn-change-address:hover {
  background: #10b981;
  color: white;
}

.divider {
  position: relative;
  text-align: center;
  margin: 2rem 0;
}

.divider::before {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  width: 100%;
  height: 1px;
  background: #e0e0e0;
}

.divider span {
  position: relative;
  background: white;
  padding: 0 1rem;
  color: #999;
  font-size: 0.9rem;
  font-weight: 600;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  display: block;
  font-weight: 600;
  font-size: 0.95rem;
  color: #333;
  margin-bottom: 0.5rem;
}

.select-wrapper {
  position: relative;
  cursor: pointer;
}

.form-input {
  width: 100%;
  padding: 0.95rem 3rem 0.95rem 1.25rem;
  border: 1.5px solid #e0e0e0;
  border-radius: 10px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  background: white;
}

.form-input:hover:not(:disabled) {
  border-color: #10b981;
  background: #f9fffe;
}

.form-input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
  color: #999;
}

.form-input.error {
  border-color: #ef4444;
}

.select-arrow {
  position: absolute;
  right: 1.25rem;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
  color: #666;
  font-size: 0.75rem;
}

.error-message {
  color: #ef4444;
  font-size: 0.85rem;
  margin-top: 0.5rem;
  margin-bottom: 0;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  padding: 1rem;
}

.modal-container {
  background: white;
  border-radius: 16px;
  width: 100%;
  max-width: 500px;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.5rem 2rem;
  border-bottom: 1px solid #e5e5e5;
}

.modal-header h3 {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0;
}

.modal-close {
  width: 32px;
  height: 32px;
  border: none;
  background: #f5f5f5;
  color: #666;
  border-radius: 50%;
  font-size: 1.25rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-close:hover {
  background: #ef4444;
  color: white;
}

.modal-search {
  position: relative;
  padding: 1.5rem 2rem;
  border-bottom: 1px solid #e5e5e5;
}

.search-input {
  width: 100%;
  padding: 0.95rem 3rem 0.95rem 1.25rem;
  border: 1.5px solid #e0e0e0;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.2s ease;
}

.search-input:focus {
  outline: none;
  border-color: #10b981;
  background: #f9fffe;
}

.search-icon {
  position: absolute;
  right: 3rem;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.25rem;
  pointer-events: none;
}

.modal-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 2rem;
  color: #666;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #e5e5e5;
  border-top-color: #10b981;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.modal-list {
  flex: 1;
  overflow-y: auto;
  padding: 1rem 0;
}

.modal-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.modal-item:hover {
  background: #f9fffe;
}

.item-name {
  font-size: 1rem;
  color: #333;
  font-weight: 500;
}

.item-check {
  color: #10b981;
  font-size: 1.25rem;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.modal-item:hover .item-check {
  opacity: 1;
}

.modal-empty {
  text-align: center;
  padding: 3rem 2rem;
  color: #999;
}

.empty-icon {
  font-size: 3rem;
  display: block;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.modal-list::-webkit-scrollbar {
  width: 6px;
}

.modal-list::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.modal-list::-webkit-scrollbar-thumb {
  background: #10b981;
  border-radius: 10px;
}

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: all 0.3s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

.modal-fade-enter-from .modal-container,
.modal-fade-leave-to .modal-container {
  transform: scale(0.9);
}

@media (max-width: 768px) {
  .modal-container {
    max-height: 90vh;
  }

  .modal-header {
    padding: 1.25rem 1.5rem;
  }

  .modal-search {
    padding: 1.25rem 1.5rem;
  }

  .modal-item {
    padding: 0.85rem 1.5rem;
  }

  .address-card-preview {
    flex-direction: column;
    gap: 1rem;
  }

  .btn-change-address {
    width: 100%;
  }
}
</style>
