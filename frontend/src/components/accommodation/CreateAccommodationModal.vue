<template>
  <v-dialog v-model="dialog" max-width="500px" persistent>
    <v-card>
      <v-card-title class="d-flex align-center pa-6">
        <SvgIcon type="mdi" :path="homePlusIcon" size="24" color="primary" class="mr-3" />
        <span class="text-h5 font-weight-bold">숙소 예약</span>
        <v-spacer></v-spacer>
        <v-btn icon variant="text" @click="closeModal">
          <SvgIcon type="mdi" :path="closeIcon" size="20" />
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-0">
        <!-- Location Info Box -->
        <div class="location-info-section">
          <!-- Location Image -->
          <div class="location-image-container">
            <v-img
              v-if="selectedLocation.firstImage1"
              :src="selectedLocation.firstImage1"
              :alt="selectedLocation.title"
              height="200"
              cover
              class="location-image"
            >
              <template v-slot:placeholder>
                <div class="d-flex align-center justify-center fill-height">
                  <v-progress-circular indeterminate color="primary"></v-progress-circular>
                </div>
              </template>
            </v-img>
            <div v-else class="no-image-placeholder d-flex flex-column align-center justify-center">
              <SvgIcon type="mdi" :path="imageOffIcon" size="32" color="grey-lighten-1" />
              <span class="text-caption text-grey-lighten-1 mt-2">이미지 없음</span>
            </div>
          </div>

          <!-- Location Details -->
          <div class="location-details pa-4">
            <h3 class="text-h6 font-weight-bold mb-2">{{ selectedLocation.title }}</h3>
            <div class="d-flex align-center mb-2">
              <SvgIcon type="mdi" :path="mapMarkerIcon" size="16" color="grey-darken-1" class="mr-2" />
              <span class="text-body-2 text-grey-darken-1">
                {{ formatAddress(selectedLocation) }}
              </span>
            </div>
            <div v-if="selectedLocation.tel" class="d-flex align-center">
              <SvgIcon type="mdi" :path="phoneIcon" size="16" color="grey-darken-1" class="mr-2" />
              <span class="text-body-2 text-grey-darken-1">{{ selectedLocation.tel }}</span>
            </div>
          </div>
        </div>

        <v-divider></v-divider>

        <!-- Form Section -->
        <div class="form-section pa-6">
          <v-form ref="formRef" @submit.prevent="handleSubmit">
            <!-- Accommodation Name -->
            <v-text-field
              v-model="formData.name"
              label="숙소 예약명"
              placeholder="예: 우리팀 숙소, 1박 2일 여행 등"
              variant="outlined"
              density="comfortable"
              :rules="[rules.required]"
              class="mb-4"
              required
            >
              <template v-slot:prepend-inner>
                <SvgIcon type="mdi" :path="textIcon" size="20" color="grey" />
              </template>
            </v-text-field>

            <!-- Check-in Date and Time -->
            <div class="datetime-section mb-4">
              <h4 class="text-subtitle-1 font-weight-medium mb-3 d-flex align-center">
                <SvgIcon type="mdi" :path="calendarClockIcon" size="20" color="primary" class="mr-2" />
                체크인 날짜/시간
              </h4>
              <v-row>
                <v-col cols="7">
                  <v-text-field
                    v-model="formData.checkInDate"
                    label="체크인 날짜"
                    type="date"
                    variant="outlined"
                    density="comfortable"
                    :rules="[rules.required]"
                    required
                  />
                </v-col>
                <v-col cols="5">
                  <v-text-field
                    v-model="formData.checkInTime"
                    label="체크인 시간"
                    type="time"
                    variant="outlined"
                    density="comfortable"
                    :rules="[rules.required]"
                    required
                  />
                </v-col>
              </v-row>
            </div>

            <!-- Check-out Date and Time -->
            <div class="datetime-section mb-4">
              <h4 class="text-subtitle-1 font-weight-medium mb-3 d-flex align-center">
                <SvgIcon type="mdi" :path="calendarClockIcon" size="20" color="primary" class="mr-2" />
                체크아웃 날짜/시간
              </h4>
              <v-row>
                <v-col cols="7">
                  <v-text-field
                    v-model="formData.checkOutDate"
                    label="체크아웃 날짜"
                    type="date"
                    variant="outlined"
                    density="comfortable"
                    :rules="[rules.required]"
                    required
                  />
                </v-col>
                <v-col cols="5">
                  <v-text-field
                    v-model="formData.checkOutTime"
                    label="체크아웃 시간"
                    type="time"
                    variant="outlined"
                    density="comfortable"
                    :rules="[rules.required, rules.checkOutAfterCheckIn]"
                    required
                  />
                </v-col>
              </v-row>
            </div>

            <!-- Error Alert -->
            <v-alert
              v-if="error"
              type="error"
              class="mb-4"
              closable
              @click:close="clearError"
            >
              {{ error }}
            </v-alert>
          </v-form>
        </div>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions class="pa-6">
        <v-spacer></v-spacer>
        <v-btn
          variant="outlined"
          @click="closeModal"
          :disabled="isLoading"
        >
          취소
        </v-btn>
        <v-btn
          color="primary"
          variant="elevated"
          @click="handleSubmit"
          :loading="isLoading"
          :disabled="!isFormValid"
        >
          <SvgIcon type="mdi" :path="plusIcon" size="20" class="mr-2" />
          숙소 예약
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useAccommodationStore } from '@/stores/accommodation'
import { apiClient } from '@/utils/apiClient'
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiHomePlus,
  mdiClose,
  mdiMapMarker,
  mdiPhone,
  mdiImageOff,
  mdiText,
  mdiCalendarClock,
  mdiPlus
} from '@mdi/js'

const homePlusIcon = mdiHomePlus
const closeIcon = mdiClose
const mapMarkerIcon = mdiMapMarker
const phoneIcon = mdiPhone
const imageOffIcon = mdiImageOff
const textIcon = mdiText
const calendarClockIcon = mdiCalendarClock
const plusIcon = mdiPlus

// Props (removed modelValue)
const props = defineProps({
  selectedLocation: {
    type: Object,
    required: true
  }
})

// Store and Route
const route = useRoute()
const accommodationStore = useAccommodationStore()

// Internal dialog state (no emit needed)
const dialog = ref(false)

// Form data
const formRef = ref(null)
const formData = ref({
  name: '',
  checkInDate: '',
  checkInTime: '',
  checkOutDate: '',
  checkOutTime: ''
})

const isLoading = ref(false)
const error = ref(null)

// Validation rules
const rules = {
  required: (value) => !!value || '필수 입력 항목입니다.',
  checkOutAfterCheckIn: () => {
    if (!formData.value.checkInDate || !formData.value.checkInTime ||
        !formData.value.checkOutDate || !formData.value.checkOutTime) {
      return true
    }

    const checkInDateTime = new Date(`${formData.value.checkInDate}T${formData.value.checkInTime}:00`)
    const checkOutDateTime = new Date(`${formData.value.checkOutDate}T${formData.value.checkOutTime}:00`)

    return checkOutDateTime > checkInDateTime || '체크아웃 시간은 체크인 시간보다 늦어야 합니다.'
  }
}

// Form validation
const isFormValid = computed(() => {
  return formData.value.name &&
         formData.value.checkInDate &&
         formData.value.checkInTime &&
         formData.value.checkOutDate &&
         formData.value.checkOutTime &&
         rules.checkOutAfterCheckIn() === true
})

// Helper function to format address
const formatAddress = (location) => {
  const addr1 = location.addr1 || ''
  const addr2 = location.addr2 || ''
  return `${addr1} ${addr2}`.trim() || '주소 정보 없음'
}

// Helper function to combine date and time
const combineDateAndTime = (date, time) => {
  if (!date || !time) return ''
  return `${date} ${time}:00`
}

// Methods
const openDialog = () => {
  dialog.value = true
  resetForm()
}

const closeModal = () => {
  dialog.value = false
  resetForm()
}

const handleSubmit = async () => {
  // Validate form
  const { valid } = await formRef.value.validate()
  if (!valid) return

  isLoading.value = true
  error.value = null

  try {
    // Get group ID from route
    const groupId = route.params.groupId
    if (!groupId) {
      throw new Error('그룹 ID를 찾을 수 없습니다.')
    }

    // Format data for API request
    const requestData = {
      name: formData.value.name,
      attractionNo: props.selectedLocation.no || props.selectedLocation.attractionNo,
      checkInTime: combineDateAndTime(formData.value.checkInDate, formData.value.checkInTime),
      checkOutTime: combineDateAndTime(formData.value.checkOutDate, formData.value.checkOutTime)
    }

    // Call API to create accommodation
    const response = await apiClient.post(`/accommodation/${groupId}`, requestData)

    if (response.data.success) {
      // Create the new accommodation object for store
      const newAccommodationData = {
        groupId: parseInt(groupId),
        attractionsNo: requestData.attractionNo,
        name: formData.value.name,
        checkInTime: requestData.checkInTime,
        checkOutTime: requestData.checkOutTime,
        accommodationInfo: {
          no: props.selectedLocation.no || props.selectedLocation.attractionNo,
          title: props.selectedLocation.title,
          contentTypeName: props.selectedLocation.contentTypeName || '숙박',
          firstImage1: props.selectedLocation.firstImage1 || '',
          firstImage2: props.selectedLocation.firstImage2 || '',
          latitude: props.selectedLocation.latitude,
          longitude: props.selectedLocation.longitude,
          tel: props.selectedLocation.tel || '',
          addr1: props.selectedLocation.addr1 || '',
          addr2: props.selectedLocation.addr2 || '',
          homepage: props.selectedLocation.homepage || null,
          overview: props.selectedLocation.overview || null
        }
      }

      // Use store method to add accommodation
      const result = accommodationStore.addAccommodation(newAccommodationData)

      if (result.success) {
        // Update the accommodationId from server response if available
        if (response.data.data?.accommodationId) {
          result.data.accommodationId = response.data.data.accommodationId
        }

        console.log('숙소 예약 성공:', response.data.message)
        closeModal()
      } else {
        throw new Error(result.error || '숙소 정보 저장에 실패했습니다.')
      }
    } else {
      throw new Error(response.data.message || '숙소 예약에 실패했습니다.')
    }
  } catch (err) {
    console.error('숙소 예약 오류:', err)

    if (err.response) {
      // Server responded with error status
      const status = err.response.status
      const message = err.response.data?.message || err.message

      if (status === 400) {
        error.value = `입력 데이터 오류: ${message}`
      } else if (status === 401) {
        error.value = '로그인이 필요합니다.'
      } else if (status === 403) {
        error.value = '숙소 예약 권한이 없습니다.'
      } else if (status === 404) {
        error.value = '해당 그룹을 찾을 수 없습니다.'
      } else {
        error.value = `서버 오류 (${status}): ${message}`
      }
    } else if (err.code === 'ECONNABORTED') {
      error.value = '요청 시간이 초과되었습니다. 다시 시도해주세요.'
    } else {
      error.value = err.message || '숙소 예약 중 오류가 발생했습니다.'
    }
  } finally {
    isLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {
    name: '',
    checkInDate: '',
    checkInTime: '',
    checkOutDate: '',
    checkOutTime: ''
  }
  error.value = null
  if (formRef.value) {
    formRef.value.resetValidation()
  }
}

const clearError = () => {
  error.value = null
}

// Expose methods to parent if needed
defineExpose({
  openDialog,
  closeModal
})

// Watch for selectedLocation changes to update form when dialog is open
watch(() => props.selectedLocation, (newLocation) => {
  if (dialog.value && newLocation) {
    // Pre-fill accommodation name with location title
    formData.value.name = newLocation.title || ''
  }
})
</script>

<style scoped>
.v-card-title {
  background-color: #fafafa;
  border-bottom: 1px solid #e0e0e0;
}

.v-card-actions {
  background-color: #fafafa;
  border-top: 1px solid #e0e0e0;
}

.location-info-section {
  background-color: #ffffff;
}

.location-image-container {
  height: 200px;
  overflow: hidden;
}

.location-image {
  width: 100%;
}

.no-image-placeholder {
  height: 200px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}

.location-details {
  background-color: #fafafa;
  border-bottom: 1px solid #e0e0e0;
}

.form-section {
  background-color: #ffffff;
}

/* DateTime section styling */
.datetime-section {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #e9ecef;
}

.datetime-section h4 {
  margin-bottom: 12px;
}

/* Button hover effects */
.v-btn:not(.v-btn--disabled):hover {
  transform: translateY(-1px);
  transition: transform 0.2s;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .v-dialog {
    margin: 16px;
  }

  .location-details {
    padding: 16px !important;
  }

  .form-section {
    padding: 16px !important;
  }
}
</style>
