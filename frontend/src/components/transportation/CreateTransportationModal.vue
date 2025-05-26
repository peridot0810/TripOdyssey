<template>
  <v-dialog v-model="dialog" max-width="700px" persistent>
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

            <!-- Departure Date and Time -->
            <v-col cols="12">
              <v-card variant="outlined" class="datetime-section">
                <v-card-subtitle class="text-primary font-weight-medium pa-3">
                  <v-icon class="mr-2">mdi-airplane-takeoff</v-icon>출발 일시
                </v-card-subtitle>
                <v-card-text class="pt-0">
                  <v-row>
                    <v-col cols="6">
                      <v-text-field
                        v-model="formData.departureDate"
                        label="출발 날짜"
                        variant="outlined"
                        density="comfortable"
                        prepend-inner-icon="mdi-calendar"
                        :rules="[rules.required]"
                        readonly
                        required
                        @click="openDatePicker('departure')"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="6">
                      <v-text-field
                        v-model="formData.departureTime"
                        label="출발 시간"
                        variant="outlined"
                        density="comfortable"
                        prepend-inner-icon="mdi-clock-outline"
                        :rules="[rules.required]"
                        readonly
                        required
                        @click="openTimePicker('departure')"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-card-text>
              </v-card>
            </v-col>

            <!-- Arrival Date and Time -->
            <v-col cols="12">
              <v-card variant="outlined" class="datetime-section">
                <v-card-subtitle class="text-primary font-weight-medium pa-3">
                  <v-icon class="mr-2">mdi-airplane-landing</v-icon>도착 일시
                </v-card-subtitle>
                <v-card-text class="pt-0">
                  <v-row>
                    <v-col cols="6">
                      <v-text-field
                        v-model="formData.arrivalDate"
                        label="도착 날짜"
                        variant="outlined"
                        density="comfortable"
                        prepend-inner-icon="mdi-calendar"
                        :rules="[rules.required]"
                        readonly
                        required
                        @click="openDatePicker('arrival')"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="6">
                      <v-text-field
                        v-model="formData.arrivalTime"
                        label="도착 시간"
                        variant="outlined"
                        density="comfortable"
                        prepend-inner-icon="mdi-clock-outline"
                        :rules="[rules.required]"
                        readonly
                        required
                        @click="openTimePicker('arrival')"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-card-text>
              </v-card>
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
          <v-alert v-if="error" type="error" class="mb-4" closable @click:close="clearError">
            {{ error }}
          </v-alert>
        </v-form>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions class="pa-6">
        <v-spacer></v-spacer>
        <v-btn variant="outlined" @click="closeModal" :disabled="isLoading"> 취소 </v-btn>
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

    <!-- Date Picker Dialog -->
    <v-dialog v-model="datePickerDialog" max-width="400px">
      <v-card>
        <v-card-title class="text-h6 font-weight-bold">
          {{ currentPickerType === 'departure' ? '출발' : '도착' }} 날짜 선택
        </v-card-title>
        <v-card-text>
          <v-date-picker
            v-model="tempDate"
            color="primary"
            :min="today"
            show-adjacent-months
            elevation="0"
            @update:model-value="selectDate"
          ></v-date-picker>
        </v-card-text>
      </v-card>
    </v-dialog>

    <!-- Time Picker Dialog -->
    <v-dialog v-model="timePickerDialog" max-width="400px">
      <v-card>
        <v-card-title class="text-h6 font-weight-bold">
          {{ currentPickerType === 'departure' ? '출발' : '도착' }} 시간 선택
        </v-card-title>
        <v-card-text>
          <v-row>
            <v-col cols="6">
              <v-select
                v-model="selectedHour"
                :items="hours"
                label="시"
                variant="outlined"
                density="comfortable"
              ></v-select>
            </v-col>
            <v-col cols="6">
              <v-select
                v-model="selectedMinute"
                :items="minutes"
                label="분"
                variant="outlined"
                density="comfortable"
              ></v-select>
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="text" @click="timePickerDialog = false">취소</v-btn>
          <v-btn color="primary" variant="elevated" @click="selectTime">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
    default: false,
  },
})

const emit = defineEmits(['update:modelValue', 'transportation-created'])

// Store and Route
const route = useRoute()
const transportationStore = useTransportationStore()

// Form data
const formRef = ref(null)
const formData = ref({
  typeId: '',
  departureDate: '',
  departureTime: '',
  arrivalDate: '',
  arrivalTime: '',
  from: '',
  to: '',
  description: '',
})

const isLoading = ref(false)
const error = ref(null)

// Date/Time picker related
const datePickerDialog = ref(false)
const timePickerDialog = ref(false)
const currentPickerType = ref('') // 'departure' or 'arrival'
const tempDate = ref(null)
const selectedHour = ref(null)
const selectedMinute = ref(null)

// Generate hour and minute options
const hours = Array.from({ length: 24 }, (_, i) => {
  const hour = String(i).padStart(2, '0')
  return { title: `${hour}시`, value: hour }
})

const minutes = Array.from({ length: 60 }, (_, i) => {
  const minute = String(i).padStart(2, '0')
  return { title: `${minute}분`, value: minute }
})

// Get today's date for minimum date selection
const today = computed(() => {
  const now = new Date()
  return now.toISOString().split('T')[0]
})

// Dialog model
const dialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
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
  { value: '8', label: '기타' },
]

// Validation rules
const rules = {
  required: (value) => !!value || '필수 입력 항목입니다.',
}

// Form validation
const isFormValid = computed(() => {
  return (
    formData.value.typeId &&
    formData.value.departureDate &&
    formData.value.departureTime &&
    formData.value.arrivalDate &&
    formData.value.arrivalTime &&
    formData.value.from &&
    formData.value.to
  )
})

// Date/Time picker methods
const openDatePicker = (type) => {
  currentPickerType.value = type
  const currentDate =
    type === 'departure' ? formData.value.departureDate : formData.value.arrivalDate
  tempDate.value = currentDate || today.value
  datePickerDialog.value = true
}

const openTimePicker = (type) => {
  currentPickerType.value = type
  const currentTime =
    type === 'departure' ? formData.value.departureTime : formData.value.arrivalTime

  if (currentTime) {
    // Parse existing time "HH:MM"
    const [hours, minutes] = currentTime.split(':')
    selectedHour.value = hours
    selectedMinute.value = minutes
  } else {
    // Default to current time
    const now = new Date()
    selectedHour.value = String(now.getHours()).padStart(2, '0')
    selectedMinute.value = String(now.getMinutes()).padStart(2, '0')
  }
  timePickerDialog.value = true
}

const selectDate = () => {
  if (tempDate.value) {
    if (currentPickerType.value === 'departure') {
      formData.value.departureDate = formatDate(tempDate.value)
    } else {
      formData.value.arrivalDate = formatDate(tempDate.value)
    }
  }
  datePickerDialog.value = false
}

const selectTime = () => {
  if (selectedHour.value !== null && selectedMinute.value !== null) {
    const timeString = `${selectedHour.value}:${selectedMinute.value}`

    if (currentPickerType.value === 'departure') {
      formData.value.departureTime = timeString
    } else {
      formData.value.arrivalTime = timeString
    }
  }
  timePickerDialog.value = false
}

// Helper functions
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date
    .toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    })
    .replace(/\./g, '-')
    .replace(/ /g, '')
    .slice(0, -1)
}

const formatDateTime = (date, time) => {
  if (!date || !time) return ''

  // Convert Korean date format to ISO date
  const dateParts = date.split('-')
  const isoDate = `${dateParts[0]}-${dateParts[1]}-${dateParts[2]}`

  return `${isoDate}T${time}:00`
}

// Methods
const handleSubmit = async () => {
  // Validate form
  const { valid } = await formRef.value.validate()
  if (!valid) return

  isLoading.value = true
  error.value = null

  try {
    const groupId = route.params.groupId

    // Format data for API request
    const requestData = {
      typeId: parseInt(formData.value.typeId),
      groupId: parseInt(groupId),
      departure: formatDateTime(formData.value.departureDate, formData.value.departureTime),
      arrival: formatDateTime(formData.value.arrivalDate, formData.value.arrivalTime),
      from: formData.value.from,
      to: formData.value.to,
      description: formData.value.description || '',
    }

    // Call API to create transportation
    const response = await apiClient.post(`/transportation/${groupId}`, requestData)

    if (response.data.success) {
      // Create the new transportation object with the returned ID
      const newTransportation = {
        transportationId: response.data.data.transportationId.toString(),
        typeId: formData.value.typeId,
        groupId: parseInt(groupId),
        departure: formData.value.departureTime,
        arrival: formData.value.arrivalTime,
        from: formData.value.from,
        to: formData.value.to,
        description: formData.value.description || '',
      }

      // Add to store
      transportationStore.transportationList.push(newTransportation)

      // Emit success event
      emit('transportation-created', newTransportation)
      closeModal()

      console.log('교통편 추가 성공:', response.data.message)
    } else {
      error.value = '교통편 추가에 실패했습니다.'
    }
  } catch (err) {
    console.error('교통편 추가 오류:', err)
    error.value = err.response?.data?.message || '교통편 추가 중 오류가 발생했습니다.'
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
    typeId: '',
    departureDate: '',
    departureTime: '',
    arrivalDate: '',
    arrivalTime: '',
    from: '',
    to: '',
    description: '',
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

.datetime-section {
  margin-bottom: 16px;
}

.datetime-section .v-card-subtitle {
  background-color: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
}

/* Form styling */
.v-text-field,
.v-select,
.v-textarea {
  margin-bottom: 8px;
}

/* Make readonly fields clickable */
.v-text-field--readonly {
  cursor: pointer;
}

.v-text-field--readonly input {
  cursor: pointer;
}

/* Button hover effects */
.v-btn:not(.v-btn--disabled):hover {
  transform: translateY(-1px);
  transition: transform 0.2s;
}

/* Date/Time picker dialogs */
.v-date-picker,
.v-time-picker {
  width: 100%;
}
</style>
