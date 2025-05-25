<template>
  <v-dialog v-model="dialog" max-width="600px" persistent>
    <v-card>
      <v-card-title class="text-h5 pa-6 bg-primary text-white">
        <v-icon class="mr-3">mdi-cash-plus</v-icon>
        경비 추가
        <v-spacer></v-spacer>
        <v-btn icon variant="text" @click="closeModal" class="text-white">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-card-text class="pa-6">
        <!-- Schedule Info (if linked to schedule) -->
        <div v-if="schedule" class="schedule-info mb-4">
          <v-alert type="info" variant="tonal" density="compact">
            <div class="d-flex align-center">
              <v-icon class="mr-2">mdi-map-marker</v-icon>
              <div>
                <strong>{{ schedule.attractionInfo?.title || schedule.name }}</strong>
                <div class="text-caption">{{ schedule.attractionInfo?.addr1 || '주소 정보 없음' }}</div>
              </div>
            </div>
          </v-alert>
        </div>

        <v-form ref="form" v-model="valid" @submit.prevent="submitForm">
          <v-row>
            <!-- Amount -->
            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formData.amount"
                label="금액"
                type="number"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-currency-krw"
                suffix="원"
                :rules="amountRules"
                required
              ></v-text-field>
            </v-col>

            <!-- Category -->
            <v-col cols="12" sm="6">
              <v-select
                v-model="formData.categoryId"
                :items="categoryOptions"
                label="카테고리"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-tag"
                :rules="categoryRules"
                required
              ></v-select>
            </v-col>

            <!-- Description -->
            <v-col cols="12">
              <v-textarea
                v-model="formData.description"
                label="설명"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-text"
                rows="3"
                :rules="descriptionRules"
                required
              ></v-textarea>
            </v-col>

            <!-- Date -->
            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formData.date"
                label="날짜"
                type="date"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-calendar"
                :rules="dateRules"
                required
              ></v-text-field>
            </v-col>

            <!-- Time -->
            <v-col cols="12" sm="6">
              <v-text-field
                v-model="formData.time"
                label="시간"
                type="time"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-clock"
                :rules="timeRules"
                required
              ></v-text-field>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>

      <v-card-actions class="pa-6 pt-0">
        <v-spacer></v-spacer>
        <v-btn
          color="grey"
          variant="outlined"
          @click="closeModal"
          class="mr-3"
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
          <v-icon start>mdi-check</v-icon>
          추가
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

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  schedule: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'close'])

// Stores
const financeStore = useFinanceStore()
const route = useRoute()

// Reactive data
const form = ref(null)
const valid = ref(false)
const loading = ref(false)

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
  { title: 'Transportation', value: 1 },
  { title: 'Accommodation', value: 2 },
  { title: 'Food', value: 3 },
  { title: 'Leisure', value: 4 },
  { title: 'Shared Supplies', value: 5 },
  { title: 'Others', value: 6 }
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

// Computed
const dialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// Watch for dialog changes to reset form
watch(() => props.modelValue, (newValue) => {
  if (newValue) {
    resetForm()
    setDefaultValues()
  }
})

// Methods
const resetForm = () => {
  formData.value = {
    amount: '',
    categoryId: null,
    description: '',
    date: '',
    time: ''
  }

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

const closeModal = () => {
  dialog.value = false
  emit('close')
}

const submitForm = async () => {
  if (!form.value) return

  const isValid = await form.value.validate()
  if (!isValid.valid) return

  loading.value = true

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

      closeModal()
    } else {
      throw new Error(response.data.message || '경비 추가에 실패했습니다.')
    }

  } catch (error) {
    console.error('Error submitting finance data:', error)

    let errorMessage = '경비 추가 중 오류가 발생했습니다.'

    if (error.response?.data?.message) {
      errorMessage = error.response.data.message
    } else if (error.message) {
      errorMessage = error.message
    }

    // Set error in store
    financeStore.setError(errorMessage)

    // Show error to user
    alert(errorMessage)

  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.schedule-info {
  border-radius: 8px;
}

.v-card-title {
  position: sticky;
  top: 0;
  z-index: 1;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .v-dialog {
    margin: 16px;
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
