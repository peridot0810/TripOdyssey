<template>
  <v-dialog v-model="dialog" max-width="500px" persistent>
    <v-card>
      <v-card-title class="d-flex align-center pa-6">
        <v-icon size="large" color="primary" class="mr-3">mdi-home-plus</v-icon>
        <span class="text-h5 font-weight-bold">숙소 예약</span>
        <v-spacer></v-spacer>
        <v-btn icon variant="text" @click="closeModal">
          <v-icon>mdi-close</v-icon>
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
              <v-icon color="grey-lighten-1" size="x-large">mdi-image-off</v-icon>
              <span class="text-caption text-grey-lighten-1 mt-2">이미지 없음</span>
            </div>
          </div>

          <!-- Location Details -->
          <div class="location-details pa-4">
            <h3 class="text-h6 font-weight-bold mb-2">{{ selectedLocation.title }}</h3>
            <div class="d-flex align-center mb-2">
              <v-icon size="small" color="grey-darken-1" class="mr-2">mdi-map-marker</v-icon>
              <span class="text-body-2 text-grey-darken-1">
                {{ formatAddress(selectedLocation) }}
              </span>
            </div>
            <div v-if="selectedLocation.tel" class="d-flex align-center">
              <v-icon size="small" color="grey-darken-1" class="mr-2">mdi-phone</v-icon>
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
              prepend-inner-icon="mdi-text"
              :rules="[rules.required]"
              class="mb-4"
              required
            ></v-text-field>

            <!-- Check-in and Check-out Times -->
            <v-row>
              <v-col cols="6">
                <v-text-field
                  v-model="formData.checkInTime"
                  label="체크인 날짜/시간"
                  placeholder="2025-06-01 15:00"
                  variant="outlined"
                  density="comfortable"
                  prepend-inner-icon="mdi-calendar-clock"
                  :rules="[rules.required, rules.datetimeFormat]"
                  hint="형식: YYYY-MM-DD HH:MM"
                  persistent-hint
                  required
                ></v-text-field>
              </v-col>

              <v-col cols="6">
                <v-text-field
                  v-model="formData.checkOutTime"
                  label="체크아웃 날짜/시간"
                  placeholder="2025-06-02 11:00"
                  variant="outlined"
                  density="comfortable"
                  prepend-inner-icon="mdi-calendar-clock"
                  :rules="[rules.required, rules.datetimeFormat, rules.checkOutAfterCheckIn]"
                  hint="형식: YYYY-MM-DD HH:MM"
                  persistent-hint
                  required
                ></v-text-field>
              </v-col>
            </v-row>

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
          <v-icon class="mr-2">mdi-plus</v-icon>
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

// Props and Emits
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  selectedLocation: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'accommodation-created'])

// Store and Route
const route = useRoute()
const accommodationStore = useAccommodationStore()

// Form data
const formRef = ref(null)
const formData = ref({
  name: '',
  checkInTime: '',
  checkOutTime: ''
})

const isLoading = ref(false)
const error = ref(null)

// Dialog model
const dialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// Validation rules
const rules = {
  required: (value) => !!value || '필수 입력 항목입니다.',
  datetimeFormat: (value) => {
    if (!value) return true
    const datetimePattern = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}$/
    return datetimePattern.test(value) || '날짜/시간 형식이 올바르지 않습니다. (예: 2025-06-01 15:00)'
  },
  checkOutAfterCheckIn: (value) => {
    if (!value || !formData.value.checkInTime) return true

    const checkInDateTime = new Date(formData.value.checkInTime + ':00')
    const checkOutDateTime = new Date(value + ':00')

    return checkOutDateTime > checkInDateTime || '체크아웃 시간은 체크인 시간보다 늦어야 합니다.'
  }
}

// Form validation
const isFormValid = computed(() => {
  return formData.value.name &&
         formData.value.checkInTime &&
         formData.value.checkOutTime &&
         rules.datetimeFormat(formData.value.checkInTime) === true &&
         rules.datetimeFormat(formData.value.checkOutTime) === true &&
         rules.checkOutAfterCheckIn(formData.value.checkOutTime) === true
})

// Helper function to format address
const formatAddress = (location) => {
  const addr1 = location.addr1 || ''
  const addr2 = location.addr2 || ''
  return `${addr1} ${addr2}`.trim() || '주소 정보 없음'
}

// Helper function to format datetime to API format
const formatDateTimeToAPI = (datetimeString) => {
  if (!datetimeString) return ''

  try {
    // Convert "2025-06-01 15:00" to "2025-06-01 15:00:00" format
    return `${datetimeString}:00`
  } catch (error) {
    console.warn('날짜/시간 형식 변환 오류:', error)
    return datetimeString
  }
}

// Methods
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
      checkInTime: formatDateTimeToAPI(formData.value.checkInTime),
      checkOutTime: formatDateTimeToAPI(formData.value.checkOutTime)
    }

    // Call API to create accommodation
    const response = await apiClient.post(`/accommodation/${groupId}`, requestData)

    if (response.data.success) {
      // Create the new accommodation object for store
      const newAccommodation = {
        accommodationId: response.data.data.accommodationId,
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

      // Add to store only after successful API call
      accommodationStore.accommodationList.push(newAccommodation)

      // Emit success event
      emit('accommodation-created', newAccommodation)
      resetForm()

      console.log('숙소 예약 성공:', response.data.message)
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

const closeModal = () => {
  dialog.value = false
  resetForm()
}

const resetForm = () => {
  formData.value = {
    name: '',
    checkInTime: '',
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

// Watch dialog changes to reset form when modal opens
watch(dialog, (newValue) => {
  if (newValue) {
    resetForm()
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

/* Form styling */
.v-text-field {
  margin-bottom: 8px;
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
