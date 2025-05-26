<template>
  <v-dialog v-model="dialog" max-width="600px" persistent>
    <v-card>
      <!-- Removed card title header -->
      <v-card-text class="pa-0">
        <!-- Schedule Info Section -->
        <div v-if="schedule" class="schedule-info-section">
          <!-- Schedule Image -->
          <div class="schedule-image-container">
            <v-img
              v-if="schedule.attractionInfo?.firstImage1"
              :src="schedule.attractionInfo.firstImage1"
              :alt="schedule.attractionInfo.title"
              height="200"
              cover
              class="schedule-image"
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

          <!-- Schedule Details -->
          <div class="schedule-details pa-4">
            <div class="d-flex align-center justify-between mb-2">
              <h3 class="text-h6 font-weight-bold">{{ schedule.attractionInfo?.title || schedule.name }}</h3>
              <v-btn icon variant="text" @click="closeModal">
                <SvgIcon type="mdi" :path="closeIcon" size="20" />
              </v-btn>
            </div>
            <div class="d-flex align-center mb-2">
              <SvgIcon type="mdi" :path="mapMarkerIcon" size="16" color="grey-darken-1" class="mr-2" />
              <span class="text-body-2 text-grey-darken-1">
                {{ schedule.attractionInfo?.addr1 || '주소 정보 없음' }}
              </span>
            </div>
            <div class="d-flex align-center">
              <SvgIcon type="mdi" :path="cashPlusIcon" size="16" color="primary" class="mr-2" />
              <span class="text-body-2 text-primary font-weight-medium">경비 추가</span>
            </div>
          </div>
        </div>

        <!-- No Schedule State -->
        <div v-else class="no-schedule-header pa-4 d-flex align-center justify-between">
          <div class="d-flex align-center">
            <SvgIcon type="mdi" :path="cashPlusIcon" size="24" color="primary" class="mr-3" />
            <span class="text-h5 font-weight-bold">경비 추가</span>
          </div>
          <v-btn icon variant="text" @click="closeModal">
            <SvgIcon type="mdi" :path="closeIcon" size="20" />
          </v-btn>
        </div>

        <v-divider></v-divider>

        <!-- Form Section -->
        <div class="form-section pa-6">
          <v-form ref="form" v-model="valid" @submit.prevent="submitForm">
            <!-- Amount and Category Row -->
            <v-row class="mb-2">
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="formData.amount"
                  label="금액"
                  type="number"
                  variant="outlined"
                  density="comfortable"
                  suffix="원"
                  :rules="amountRules"
                  required
                >
                  <template v-slot:prepend-inner>
                    <SvgIcon type="mdi" :path="currencyKrwIcon" size="20" color="grey" />
                  </template>
                </v-text-field>
              </v-col>

              <v-col cols="12" sm="6">
                <v-select
                  v-model="formData.categoryId"
                  :items="categoryOptions"
                  label="카테고리"
                  variant="outlined"
                  density="comfortable"
                  :rules="categoryRules"
                  required
                >
                  <template v-slot:prepend-inner>
                    <SvgIcon type="mdi" :path="tagIcon" size="20" color="grey" />
                  </template>
                </v-select>
              </v-col>
            </v-row>

            <!-- Description -->
            <v-textarea
              v-model="formData.description"
              label="설명"
              variant="outlined"
              density="comfortable"
              rows="3"
              :rules="descriptionRules"
              class="mb-4"
              required
            >
              <template v-slot:prepend-inner>
                <SvgIcon type="mdi" :path="textIcon" size="20" color="grey" />
              </template>
            </v-textarea>

            <!-- Date and Time Section -->
            <div class="datetime-section mb-4">
              <h4 class="text-subtitle-1 font-weight-medium mb-3 d-flex align-center">
                <SvgIcon type="mdi" :path="calendarClockIcon" size="20" color="primary" class="mr-2" />
                경비 발생 날짜/시간
              </h4>
              <v-row>
                <v-col cols="7">
                  <v-text-field
                    v-model="formData.date"
                    label="날짜"
                    type="date"
                    variant="outlined"
                    density="comfortable"
                    :rules="dateRules"
                    required
                  />
                </v-col>
                <v-col cols="5">
                  <v-text-field
                    v-model="formData.time"
                    label="시간"
                    type="time"
                    variant="outlined"
                    density="comfortable"
                    :rules="timeRules"
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
          :disabled="loading"
        >
          취소
        </v-btn>
        <v-btn
          color="primary"
          variant="elevated"
          @click="submitForm"
          :disabled="!valid"
          :loading="loading"
        >
          <SvgIcon type="mdi" :path="checkIcon" size="20" class="mr-2" />
          경비 추가
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useFinanceStore } from '@/stores/finance'
import { apiClient } from '@/utils/apiClient'
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiCashPlus,
  mdiClose,
  mdiMapMarker,
  mdiImageOff,
  mdiCurrencyKrw,
  mdiTag,
  mdiText,
  mdiCalendarClock,
  mdiCheck
} from '@mdi/js'

const cashPlusIcon = mdiCashPlus
const closeIcon = mdiClose
const mapMarkerIcon = mdiMapMarker
const imageOffIcon = mdiImageOff
const currencyKrwIcon = mdiCurrencyKrw
const tagIcon = mdiTag
const textIcon = mdiText
const calendarClockIcon = mdiCalendarClock
const checkIcon = mdiCheck

// Props (removed modelValue)
const props = defineProps({
  schedule: {
    type: Object,
    default: null
  }
})

// Stores
const financeStore = useFinanceStore()
const route = useRoute()

// Internal dialog state (no emit needed)
const dialog = ref(false)

// Reactive data
const form = ref(null)
const valid = ref(false)
const loading = ref(false)
const error = ref(null)

// Form data
const formData = ref({
  amount: '',
  categoryId: null,
  description: '',
  date: '',
  time: ''
})

// Get groupId from route
const groupId = computed(() => {
  return parseInt(route.params.groupId)
})

// Category options based on the finance store categories
const categoryOptions = [
  { title: '교통비', value: 1 },
  { title: '숙박비', value: 2 },
  { title: '식비', value: 3 },
  { title: '레저', value: 4 },
  { title: '공용품', value: 5 },
  { title: '기타', value: 6 }
]

// Validation rules
const amountRules = [
  v => !!v || '금액을 입력해주세요',
  v => v > 0 || '금액은 0보다 커야 합니다',
  v => !isNaN(Number(v)) || '올바른 숫자를 입력해주세요'
]

const categoryRules = [
  v => !!v || '카테고리를 선택해주세요'
]

const descriptionRules = [
  v => !!v || '설명을 입력해주세요',
  v => v.length >= 2 || '설명은 최소 2글자 이상이어야 합니다',
  v => v.length <= 200 || '설명은 200글자를 초과할 수 없습니다'
]

const dateRules = [
  v => !!v || '날짜를 선택해주세요'
]

const timeRules = [
  v => !!v || '시간을 선택해주세요'
]

// Methods
const openDialog = () => {
  dialog.value = true
  resetForm()
  setDefaultValues()
}

const closeModal = () => {
  dialog.value = false
  resetForm()
}

const resetForm = () => {
  formData.value = {
    amount: '',
    categoryId: null,
    description: '',
    date: '',
    time: ''
  }
  error.value = null

  if (form.value) {
    form.value.resetValidation()
  }
}

const setDefaultValues = () => {
  // Set current date and time as defaults
  const now = new Date()
  formData.value.date = now.toISOString().split('T')[0]
  formData.value.time = now.toTimeString().split(' ')[0].substring(0, 5)

  // Set description hint based on schedule
  if (props.schedule) {
    const scheduleName = props.schedule.attractionInfo?.title || props.schedule.name
    formData.value.description = `${scheduleName} 관련 비용`
  }
}

const clearError = () => {
  error.value = null
}

const submitForm = async () => {
  if (!form.value) return

  const isValid = await form.value.validate()
  if (!isValid.valid) return

  loading.value = true
  error.value = null

  try {
    // Combine date and time into ISO datetime format
    const datetime = `${formData.value.date}T${formData.value.time}:00`

    const requestData = {
      datetime: datetime,
      description: formData.value.description.trim(),
      amount: Number(formData.value.amount),
      categoryId: Number(formData.value.categoryId),
      contentId: props.schedule?.contentId || null,
      groupId: groupId.value
    }

    console.log('Sending finance request:', requestData)

    // Make API request
    const response = await apiClient.post(`/financial/expense-tracker/${groupId.value}`, requestData)

    console.log('Finance API response:', response.data)

    if (response.data.success) {
      // Create expense object for Pinia store
      const newExpense = {
        expenseId: response.data.data.expenseId,
        datetime: datetime.replace('T', ' '), // Convert to "YYYY-MM-DD HH:mm:ss" format
        description: requestData.description,
        amount: requestData.amount,
        categoryId: requestData.categoryId,
        contentName: props.schedule?.attractionInfo?.title || props.schedule?.name || null,
        contentId: requestData.contentId
      }

      // Add to Pinia store
      financeStore.addExpense(newExpense)

      console.log('경비가 성공적으로 추가되었습니다!')

      // Ensure modal closes after successful submission
      setTimeout(() => {
        closeModal()
      }, 100)
    } else {
      throw new Error(response.data.message || '경비 추가에 실패했습니다.')
    }

  } catch (err) {
    console.error('Error submitting finance data:', err)

    if (err.response) {
      const status = err.response.status
      const message = err.response.data?.message || err.message

      if (status === 400) {
        error.value = `입력 데이터 오류: ${message}`
      } else if (status === 401) {
        error.value = '로그인이 필요합니다.'
      } else if (status === 403) {
        error.value = '경비 추가 권한이 없습니다.'
      } else if (status === 404) {
        error.value = '해당 그룹을 찾을 수 없습니다.'
      } else {
        error.value = `서버 오류 (${status}): ${message}`
      }
    } else if (err.code === 'ECONNABORTED') {
      error.value = '요청 시간이 초과되었습니다. 다시 시도해주세요.'
    } else {
      error.value = err.message || '경비 추가 중 오류가 발생했습니다.'
    }

  } finally {
    loading.value = false
  }
}

// Expose methods to parent
defineExpose({
  openDialog,
  closeModal
})

// Watch for schedule changes to update description when dialog is open
watch(() => props.schedule, (newSchedule) => {
  if (dialog.value && newSchedule) {
    const scheduleName = newSchedule.attractionInfo?.title || newSchedule.name
    formData.value.description = `${scheduleName} 관련 비용`
  }
})
</script>

<style scoped>
.schedule-info-section {
  background-color: #ffffff;
}

.schedule-image-container {
  height: 200px;
  overflow: hidden;
}

.schedule-image {
  width: 100%;
}

.no-image-placeholder {
  height: 200px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}

.schedule-details {
  background-color: #fafafa;
  border-bottom: 1px solid #e0e0e0;
}

.no-schedule-header {
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

.v-card-actions {
  background-color: #fafafa;
  border-top: 1px solid #e0e0e0;
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

  .schedule-details {
    padding: 16px !important;
  }

  .form-section {
    padding: 16px !important;
  }

  .v-card-actions {
    flex-direction: column;
    gap: 8px;
  }

  .v-card-actions .v-btn {
    width: 100%;
  }
}
</style>
