<template>
  <div class="finance-list">
    <h2 class="text-h6 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-wallet-outline</v-icon>
      여행 경비 내역
    </h2>

    <!-- Loading State -->
    <div v-if="loading" class="loading-section text-center py-8">
      <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
      <p class="text-body-1 mt-3">경비 내역을 불러오는 중...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="error-section">
      <v-alert type="error" variant="tonal" class="mb-4">
        <div class="d-flex align-center justify-space-between">
          <div>
            <strong>오류 발생</strong>
            <div class="text-body-2 mt-1">{{ error }}</div>
          </div>
          <v-btn color="error" variant="outlined" size="small" @click="fetchExpenses">
            다시 시도
          </v-btn>
        </div>
      </v-alert>
    </div>

    <!-- Main Content -->
    <div v-else>
      <!-- Summary Section -->
      <div class="summary-section mb-4">
        <v-card color="primary" variant="flat" class="pa-4 text-white">
          <div class="d-flex justify-space-between align-center">
            <div>
              <div class="text-h5 font-weight-bold">{{ formatAmount(totalAmount) }}원</div>
              <div class="text-body-2 opacity-90">총 {{ financeData.length }}건의 지출</div>
            </div>
            <v-icon size="large" class="opacity-70">mdi-cash-multiple</v-icon>
          </div>
        </v-card>
      </div>

      <!-- Filter Section -->
      <div class="filter-section mb-4">
        <v-row no-gutters>
          <v-col cols="12" sm="6" class="pr-sm-2">
            <v-select
              v-model="selectedCategory"
              :items="categoryOptions"
              label="카테고리 필터"
              variant="outlined"
              density="compact"
              clearable
              prepend-inner-icon="mdi-filter-variant"
            ></v-select>
          </v-col>
          <v-col cols="12" sm="6" class="pl-sm-2">
            <v-select
              v-model="sortBy"
              :items="sortOptions"
              label="정렬 기준"
              variant="outlined"
              density="compact"
              prepend-inner-icon="mdi-sort"
            ></v-select>
          </v-col>
        </v-row>
      </div>

      <!-- Refresh Button -->
      <div class="refresh-section mb-3">
        <v-btn
          variant="outlined"
          size="small"
          @click="fetchExpenses"
          :loading="refreshing"
        >
          <v-icon start>mdi-refresh</v-icon>
          새로고침
        </v-btn>
      </div>

      <!-- Empty State -->
      <div v-if="filteredFinanceData.length === 0" class="empty-state text-center py-8">
        <v-icon size="x-large" color="grey">mdi-wallet-outline</v-icon>
        <p class="text-body-1 text-grey-darken-1 mt-3">경비 내역이 없습니다</p>
        <p class="text-body-2 text-grey mt-2">첫 번째 경비를 추가해보세요!</p>
      </div>

      <!-- Finance Cards -->
      <div v-else class="finance-cards">
        <div
          v-for="expense in filteredFinanceData"
          :key="expense.expenseId"
          class="finance-card-wrapper"
        >
          <FinanceCard
            :expense="{ ...expense, categoryName: financeStore.getCategoryName(expense.categoryId) }"
            :content-image="getContentImage(expense.contentId)"
            :content-name="getContentName(expense.contentId)"
            :content-address="getContentAddress(expense.contentId)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useFinanceStore } from '@/stores/finance'
import FinanceCard from './FinanceCard.vue'
import { dummyScheduleData } from '@/data/schedule/dummyScheduleData.js'
import { apiClient } from '@/utils/apiClient'

// Router
const route = useRoute()

// Stores
const financeStore = useFinanceStore()

// Reactive data
const selectedCategory = ref(null)
const sortBy = ref('datetime-desc')
const loading = ref(false)
const refreshing = ref(false)
const error = ref(null)

// Get groupId from route
const groupId = computed(() => {
  return parseInt(route.params.groupId)
})

// Get data from Pinia store
const financeData = computed(() => financeStore.expenses)
const totalAmount = computed(() => financeStore.totalAmount)
// const storeError = computed(() => financeStore.error)
// const storeLoading = computed(() => financeStore.isLoading)

// Create a map for quick schedule lookup by contentId
const scheduleMap = computed(() => {
  const map = new Map()
  dummyScheduleData.forEach((schedule) => {
    if (schedule.contentId) {
      map.set(schedule.contentId, schedule)
    }
  })
  return map
})

// Category options for filter
const categoryOptions = computed(() => {
  const categories = [...new Set(financeData.value.map((item) => financeStore.getCategoryName(item.categoryId)))]
  return categories.map((category) => ({
    title: category,
    value: category,
  }))
})

// Sort options
const sortOptions = [
  { title: '최신 순', value: 'datetime-desc' },
  { title: '오래된 순', value: 'datetime-asc' },
  { title: '금액 높은 순', value: 'amount-desc' },
  { title: '금액 낮은 순', value: 'amount-asc' },
  { title: '카테고리 순', value: 'category' },
]

// Computed properties
const filteredFinanceData = computed(() => {
  let filtered = financeData.value

  // Apply category filter
  if (selectedCategory.value) {
    filtered = filtered.filter((expense) => financeStore.getCategoryName(expense.categoryId) === selectedCategory.value)
  }

  // Apply sorting using store method
  filtered = financeStore.sortExpenses(sortBy.value.split('-')[0], sortBy.value.split('-')[1] || 'desc')

  return filtered
})

// API Methods
const fetchExpenses = async (isRefresh = false) => {
  if (isRefresh) {
    refreshing.value = true
  } else {
    loading.value = true
  }

  error.value = null
  financeStore.setLoading(true)

  try {
    console.log('Fetching expenses for groupId:', groupId.value)

    const response = await apiClient.get(`/financial/expense-tracker/${groupId.value}`)

    console.log('Finance API response:', response.data)

    if (response.data.success) {
      // Convert API data to store format
      const storeExpenses = response.data.data.map(expense => ({
        expenseId: expense.expenseId,
        datetime: expense.datetime,
        description: expense.description,
        amount: expense.amount,
        categoryId: financeStore.getCategoryId(expense.categoryName) || 6, // Default to 'Others' if not found
        contentName: expense.contentName,
        contentId: expense.contentId
      }))

      // Set expenses in store
      financeStore.setExpenses(storeExpenses)

      console.log('Loaded', storeExpenses.length, 'expenses to store')
    } else {
      throw new Error(response.data.message || '경비 내역을 불러오는데 실패했습니다.')
    }

  } catch (err) {
    console.error('Error fetching expenses:', err)

    let errorMessage = '경비 내역을 불러오는 중 오류가 발생했습니다.'

    if (err.response?.status === 404) {
      errorMessage = '해당 그룹의 경비 내역을 찾을 수 없습니다.'
    } else if (err.response?.status === 403) {
      errorMessage = '경비 내역을 볼 권한이 없습니다.'
    } else if (err.response?.data?.message) {
      errorMessage = err.response.data.message
    } else if (err.message) {
      errorMessage = err.message
    }

    error.value = errorMessage
    financeStore.setError(errorMessage)
  } finally {
    loading.value = false
    refreshing.value = false
    financeStore.setLoading(false)
  }
}

// Helper functions to get content info from schedule data
const getContentImage = (contentId) => {
  if (!contentId) return null
  const schedule = scheduleMap.value.get(contentId)
  return schedule?.attractionInfo?.firstImage1 || null
}

const getContentName = (contentId) => {
  if (!contentId) return null
  const schedule = scheduleMap.value.get(contentId)
  return schedule?.attractionInfo?.title || schedule?.name || null
}

const getContentAddress = (contentId) => {
  if (!contentId) return null
  const schedule = scheduleMap.value.get(contentId)
  return schedule?.attractionInfo?.addr1 || null
}

// Format amount with Korean number formatting
const formatAmount = (amount) => {
  return new Intl.NumberFormat('ko-KR').format(amount)
}

// Expose methods to parent component
defineExpose({
  refreshExpenses: () => fetchExpenses(true)
})

// Lifecycle
onMounted(() => {
  fetchExpenses()
})
</script>

<style scoped>
.finance-list {
  padding: 16px;
}

.loading-section {
  background-color: #f8f9fa;
  border-radius: 8px;
  margin: 20px 0;
}

.error-section {
  margin-bottom: 20px;
}

.summary-section {
  margin-bottom: 20px;
}

.filter-section {
  margin-bottom: 20px;
}

.refresh-section {
  text-align: right;
  margin-bottom: 16px;
}

.empty-state {
  border-radius: 8px;
  background-color: #f5f5f5;
  margin-top: 20px;
}

.finance-cards {
  max-width: 100%;
}

.finance-card-wrapper {
  margin-bottom: 12px;
}

/* Animation for finance cards */
.finance-cards {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .filter-section .v-col {
    margin-bottom: 8px;
  }

  .refresh-section {
    text-align: center;
  }
}
</style>
