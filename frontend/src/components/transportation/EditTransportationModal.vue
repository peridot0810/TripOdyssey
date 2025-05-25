<template>
  <v-dialog v-model="dialog" max-width="600px" persistent>
    <v-card>
      <v-card-title class="d-flex align-center pa-6">
        <v-icon size="large" color="primary" class="mr-3">mdi-pencil-circle</v-icon>
        <span class="text-h5 font-weight-bold">교통편 수정</span>
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

          <!-- Changes Preview -->
          <v-card v-if="hasChanges" variant="outlined" class="mb-4 bg-blue-lighten-5">
            <v-card-title class="text-subtitle-1 pb-2">
              <v-icon color="info" size="small" class="mr-2">mdi-information</v-icon>
              변경 사항 미리보기
            </v-card-title>
            <v-card-text class="pt-0">
              <div class="changes-preview">
                <div v-for="change in changesList" :key="change.field" class="change-item mb-2">
                  <div class="d-flex align-center">
                    <strong class="field-name mr-2">{{ change.fieldName }}:</strong>
                    <span class="original-value text-decoration-line-through text-grey mr-2">
                      {{ change.originalValue }}
                    </span>
                    <v-icon size="small" class="mx-1">mdi-arrow-right</v-icon>
                    <span class="new-value text-primary font-weight-medium">
                      {{ change.newValue }}
                    </span>
                  </div>
                </div>
              </div>
            </v-card-text>
          </v-card>

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
          :disabled="!isFormValid || !hasChanges"
        >
          <v-icon class="mr-2">mdi-content-save</v-icon>
          수정하기
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
  },
  transportation: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'transportation-updated'])

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

const originalData = ref({})
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

// Field name mapping for Korean labels
const fieldNameMap = {
  typeId: '교통편 종류',
  departure: '출발 시간',
  arrival: '도착 시간',
  from: '출발지',
  to: '도착지',
  description: '상세 정보'
}

// Helper function to get type label
const getTypeLabel = (typeId) => {
  const type = transportationTypes.find(t => t.value === typeId)
  return type ? type.label : typeId
}

// Validation rules
const rules = {
  required: (value) => !!value || '필수 입력 항목입니다.',
  timeFormat: (value) => {
    if (!value) return true
    const timePattern = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/
    return timePattern.test(value) || '시간 형식이 올바르지 않습니다. (예: 09:30)'
  }
}

// Computed properties
const isFormValid = computed(() => {
  return formData.value.typeId &&
         formData.value.departure &&
         formData.value.arrival &&
         formData.value.from &&
         formData.value.to &&
         rules.timeFormat(formData.value.departure) === true &&
         rules.timeFormat(formData.value.arrival) === true
})

const hasChanges = computed(() => {
  return Object.keys(formData.value).some(key => {
    return formData.value[key] !== originalData.value[key]
  })
})

const changesList = computed(() => {
  const changes = []

  Object.keys(formData.value).forEach(key => {
    if (formData.value[key] !== originalData.value[key]) {
      let originalValue = originalData.value[key] || '(없음)'
      let newValue = formData.value[key] || '(없음)'

      // Special handling for typeId to show labels
      if (key === 'typeId') {
        originalValue = getTypeLabel(originalData.value[key])
        newValue = getTypeLabel(formData.value[key])
      }

      changes.push({
        field: key,
        fieldName: fieldNameMap[key],
        originalValue,
        newValue
      })
    }
  })

  return changes
})

// Methods
const loadFormData = () => {
  if (props.transportation) {
    formData.value = {
      typeId: props.transportation.typeId,
      departure: props.transportation.departure,
      arrival: props.transportation.arrival,
      from: props.transportation.from,
      to: props.transportation.to,
      description: props.transportation.description || ''
    }

    // Store original data for comparison
    originalData.value = { ...formData.value }
  }
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
      typeId: parseInt(formData.value.typeId),
      departure: formatTimeToDateTime(formData.value.departure),
      arrival: formatTimeToDateTime(formData.value.arrival),
      from: formData.value.from,
      to: formData.value.to,
      description: formData.value.description || ''
    }

    // Call API to update transportation
    const response = await apiClient.put(`/transportation/${groupId}`, requestData, {
      params: {
        transportationId: props.transportation.transportationId
      }
    })

    if (response.data.success) {
      // Update in store with the new data
      const result = transportationStore.updateTransportation(
        props.transportation.transportationId,
        formData.value
      )

      if (result.success) {
        // Emit success event
        emit('transportation-updated', result.data)
        console.log('교통편 수정 성공:', response.data.message)
      } else {
        // This shouldn't happen if API succeeded, but handle gracefully
        console.warn('Store에서 수정 실패:', result.error)
        emit('transportation-updated', formData.value)
      }
    } else {
      throw new Error(response.data.message || '교통편 수정에 실패했습니다.')
    }
  } catch (err) {
    console.error('교통편 수정 오류:', err)

    if (err.response) {
      // Server responded with error status
      const status = err.response.status
      const message = err.response.data?.message || err.message

      if (status === 400) {
        error.value = `입력 데이터 오류: ${message}`
      } else if (status === 401) {
        error.value = '로그인이 필요합니다.'
      } else if (status === 403) {
        error.value = '교통편 수정 권한이 없습니다.'
      } else if (status === 404) {
        error.value = '해당 교통편을 찾을 수 없습니다.'
      } else {
        error.value = `서버 오류 (${status}): ${message}`
      }
    } else if (err.code === 'ECONNABORTED') {
      error.value = '요청 시간이 초과되었습니다. 다시 시도해주세요.'
    } else {
      error.value = err.message || '교통편 수정 중 오류가 발생했습니다.'
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
  error.value = null
  if (formRef.value) {
    formRef.value.resetValidation()
  }
}

const clearError = () => {
  error.value = null
}

// Watch for prop changes and dialog open/close
watch(() => props.transportation, loadFormData, { immediate: true })
watch(dialog, (newValue) => {
  if (newValue) {
    loadFormData()
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

/* Changes preview styling */
.changes-preview {
  font-size: 0.875rem;
}

.change-item {
  padding: 4px 0;
}

.field-name {
  min-width: 80px;
  font-size: 0.8rem;
}

.original-value {
  font-size: 0.8rem;
}

.new-value {
  font-size: 0.8rem;
}

/* Button hover effects */
.v-btn:not(.v-btn--disabled):hover {
  transform: translateY(-1px);
  transition: transform 0.2s;
}
</style>
