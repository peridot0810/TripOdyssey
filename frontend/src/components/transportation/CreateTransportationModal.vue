<template>
  <v-dialog v-model="dialog" max-width="600px" persistent>
    <v-card>
      <v-card-title class="d-flex align-center pa-6">
        <v-icon size="large" color="primary" class="mr-3">mdi-plus-circle</v-icon>
        <span class="text-h5 font-weight-bold">교통편 추가</span>
        <v-spacer></v-spacer>
        <v-btn icon variant="text" @click="closeModal">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-6">
        <v-form ref="formRef" @submit.prevent="handleSubmit">
          <v-row>
            <!-- Transportation Type -->
            <v-col cols="12">
              <v-select
                v-model="formData.typeId"
                :items="transportationTypes"
                item-title="label"
                item-value="value"
                label="교통편 종류"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-car"
                :rules="[rules.required]"
                required
              ></v-select>
            </v-col>

            <!-- Departure and Arrival Times -->
            <v-col cols="6">
              <v-text-field
                v-model="formData.departure"
                label="출발 시간"
                placeholder="09:30"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-clock-outline"
                :rules="[rules.required, rules.timeFormat]"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="6">
              <v-text-field
                v-model="formData.arrival"
                label="도착 시간"
                placeholder="11:45"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-clock-outline"
                :rules="[rules.required, rules.timeFormat]"
                required
              ></v-text-field>
            </v-col>

            <!-- Departure and Arrival Locations -->
            <v-col cols="6">
              <v-text-field
                v-model="formData.from"
                label="출발지"
                placeholder="인천국제공항"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-map-marker"
                :rules="[rules.required]"
                required
              ></v-text-field>
            </v-col>

            <v-col cols="6">
              <v-text-field
                v-model="formData.to"
                label="도착지"
                placeholder="제주국제공항"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-map-marker-check"
                :rules="[rules.required]"
                required
              ></v-text-field>
            </v-col>

            <!-- Description -->
            <v-col cols="12">
              <v-textarea
                v-model="formData.description"
                label="상세 정보 (선택사항)"
                placeholder="KE1201 · 대한항공 · 이코노미클래스 · 좌석 12A"
                variant="outlined"
                density="comfortable"
                rows="3"
                prepend-inner-icon="mdi-text"
                no-resize
              ></v-textarea>
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
          추가하기
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useTransportationStore } from '@/stores/transportation'
import { apiClient } from '@/utils/apiClient'

// Props and Emits
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'transportation-created'])

// Store and Route
const route = useRoute()
const transportationStore = useTransportationStore()

// Form data
const formRef = ref(null)
const formData = ref({
  typeId: '',
  departure: '',
  arrival: '',
  from: '',
  to: '',
  description: ''
})

const isLoading = ref(false)
const error = ref(null)

// Dialog model
const dialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// Transportation types
const transportationTypes = [
  { value: '1', label: '항공편' },
  { value: '2', label: '기차' },
  { value: '3', label: '버스' },
  { value: '4', label: '지하철' },
  { value: '5', label: '택시' },
  { value: '6', label: '렌터카' },
  { value: '7', label: '도보' },
  { value: '8', label: '기타' }
]

// Validation rules
const rules = {
  required: (value) => !!value || '필수 입력 항목입니다.',
  timeFormat: (value) => {
    if (!value) return true
    const timePattern = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/
    return timePattern.test(value) || '시간 형식이 올바르지 않습니다. (예: 09:30)'
  }
}

// Form validation
const isFormValid = computed(() => {
  return formData.value.typeId &&
         formData.value.departure &&
         formData.value.arrival &&
         formData.value.from &&
         formData.value.to &&
         rules.timeFormat(formData.value.departure) === true &&
         rules.timeFormat(formData.value.arrival) === true
})

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
      typeId: parseInt(formData.value.typeId),
      groupId: parseInt(groupId),
      departure: formatTimeToDateTime(formData.value.departure),
      arrival: formatTimeToDateTime(formData.value.arrival),
      from: formData.value.from,
      to: formData.value.to,
      description: formData.value.description || ''
    }

    // Call API to create transportation
    const response = await apiClient.post(`/transportation/${groupId}`, requestData)

    if (response.data.success) {
      // Create the new transportation object with the returned ID
      const newTransportation = {
        transportationId: response.data.data.transportationId.toString(),
        typeId: formData.value.typeId,
        groupId: parseInt(groupId),
        departure: formData.value.departure,
        arrival: formData.value.arrival,
        from: formData.value.from,
        to: formData.value.to,
        description: formData.value.description || ''
      }

      // Add to store
      transportationStore.transportationList.push(newTransportation)

      // Emit success event
      emit('transportation-created', newTransportation)
      resetForm()

      console.log('교통편 추가 성공:', response.data.message)
    } else {
      throw new Error(response.data.message || '교통편 추가에 실패했습니다.')
    }
  } catch (err) {
    console.error('교통편 추가 오류:', err)

    if (err.response) {
      // Server responded with error status
      const status = err.response.status
      const message = err.response.data?.message || err.message

      if (status === 400) {
        error.value = `입력 데이터 오류: ${message}`
      } else if (status === 401) {
        error.value = '로그인이 필요합니다.'
      } else if (status === 403) {
        error.value = '교통편 추가 권한이 없습니다.'
      } else if (status === 404) {
        error.value = '해당 그룹을 찾을 수 없습니다.'
      } else {
        error.value = `서버 오류 (${status}): ${message}`
      }
    } else if (err.code === 'ECONNABORTED') {
      error.value = '요청 시간이 초과되었습니다. 다시 시도해주세요.'
    } else {
      error.value = err.message || '교통편 추가 중 오류가 발생했습니다.'
    }
  } finally {
    isLoading.value = false
  }
}

// Helper function to format time to datetime string
const formatTimeToDateTime = (timeString) => {
  if (!timeString) return ''

  try {
    // Convert "09:30" to "2025-06-01T09:30:00" format
    // For now, using a default date - you might want to get actual travel date
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')

    return `${year}-${month}-${day}T${timeString}:00`
  } catch (error) {
    console.warn('시간 형식 변환 오류:', error)
    return timeString
  }
}

const closeModal = () => {
  dialog.value = false
  resetForm()
}

const resetForm = () => {
  formData.value = {
    typeId: '',
    departure: '',
    arrival: '',
    from: '',
    to: '',
    description: ''
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

/* Form styling */
.v-text-field, .v-select, .v-textarea {
  margin-bottom: 8px;
}

/* Button hover effects */
.v-btn:not(.v-btn--disabled):hover {
  transform: translateY(-1px);
  transition: transform 0.2s;
}
</style>
