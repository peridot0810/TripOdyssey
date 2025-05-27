<template>
  <div class="finance-list">
    <!-- Fixed Header -->
    <div class="finance-header">
      <h2 class="text-h6 font-weight-bold d-flex align-center">
        <SvgIcon type="mdi" :path="walletOutlineIcon" size="24" color="primary" class="mr-2" />
        여행 경비 내역
      </h2>
    </div>

    <!-- Fixed Controls Section -->
    <div class="finance-controls">
      <!-- Loading State -->
      <div v-if="loading" class="loading-section text-center py-8">
        <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
        <p class="text-body-1 mt-3">경비 내역을 불러오는 중...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="error-section mb-4">
        <v-alert type="error" variant="tonal">
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

      <!-- Main Controls -->
      <div v-else>
        <!-- Summary Section -->
        <div class="summary-section mb-4">
          <v-card color="orange" variant="flat" class="pa-4 text-white">
            <div class="d-flex justify-space-between align-center">
              <div>
                <div class="text-h5 font-weight-bold">{{ formatAmount(totalAmount) }}원</div>
                <div class="text-body-2 opacity-90">총 {{ financeData.length }}건의 지출</div>
              </div>
              <SvgIcon
                type="mdi"
                :path="cashMultipleIcon"
                size="32"
                color="white"
                class="opacity-70"
              />
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
              >
                <template v-slot:prepend-inner>
                  <SvgIcon type="mdi" :path="filterVariantIcon" size="20" color="grey" />
                </template>
              </v-select>
            </v-col>
            <v-col cols="12" sm="6" class="pl-sm-2">
              <v-select
                v-model="sortBy"
                :items="sortOptions"
                label="정렬 기준"
                variant="outlined"
                density="compact"
              >
                <template v-slot:prepend-inner>
                  <SvgIcon type="mdi" :path="sortIcon" size="20" color="grey" />
                </template>
              </v-select>
            </v-col>
          </v-row>
        </div>
      </div>
    </div>

    <!-- Scrollable Cards Section -->
    <div class="finance-cards-container">
      <div v-if="!loading && !error">
        <!-- Empty State -->
        <div v-if="filteredFinanceData.length === 0" class="empty-state text-center py-8">
          <SvgIcon type="mdi" :path="walletOutlineIcon" size="32" color="grey" />
          <p class="text-body-1 text-grey-darken-1 mt-3">경비 내역이 없습니다</p>
          <p class="text-body-2 text-grey mt-2">첫 번째 경비를 추가해보세요!</p>
        </div>

        <!-- Finance Cards - Only This Part Scrolls -->
        <div v-else class="finance-cards">
          <div
            v-for="expense in filteredFinanceData"
            :key="expense.expenseId"
            class="finance-card-wrapper"
          >
            <FinanceCard
              :expense="{
                ...expense,
                categoryName: financeStore.getCategoryName(expense.categoryId),
              }"
              :content-image="getContentImage(expense.contentId)"
              :content-name="getContentName(expense.contentId)"
              :content-address="getContentAddress(expense.contentId)"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useFinanceStore } from '@/stores/finance'
import { useScheduleStore } from '@/stores/schedule'
import FinanceCard from './FinanceCard.vue'
import { apiClient } from '@/utils/apiClient'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiWalletOutline, mdiCashMultiple, mdiFilterVariant, mdiSort } from '@mdi/js'

const walletOutlineIcon = mdiWalletOutline
const cashMultipleIcon = mdiCashMultiple
const filterVariantIcon = mdiFilterVariant
const sortIcon = mdiSort

// Router
const route = useRoute()

// Stores
const financeStore = useFinanceStore()
const scheduleStore = useScheduleStore()

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

// Create a map for quick schedule lookup by contentId using real schedule data
const scheduleMap = computed(() => {
  const map = new Map()
  scheduleStore.schedules.forEach((schedule) => {
    if (schedule.contentId) {
      map.set(schedule.contentId, schedule)
    }
  })
  return map
})

// Korean category names for filter
const koreanCategoryMap = {
  1: '교통비',
  2: '숙박비',
  3: '식비',
  4: '레저',
  5: '공용품',
  6: '기타',
}

// Category options for filter
const categoryOptions = computed(() => {
  const categoryIds = [...new Set(financeData.value.map((item) => item.categoryId))]
  return categoryIds.map((categoryId) => ({
    title: koreanCategoryMap[categoryId] || '기타',
    value: koreanCategoryMap[categoryId] || '기타',
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
  let filtered = [...financeData.value]

  // Apply category filter
  if (selectedCategory.value) {
    filtered = filtered.filter(
      (expense) => (koreanCategoryMap[expense.categoryId] || '기타') === selectedCategory.value,
    )
  }

  // Apply sorting
  const [sortField, sortOrder] = sortBy.value.split('-')
  filtered = filtered.sort((a, b) => {
    let comparison = 0

    switch (sortField) {
      case 'datetime':
        comparison = new Date(a.datetime) - new Date(b.datetime)
        break
      case 'amount':
        comparison = a.amount - b.amount
        break
      case 'category':
        comparison = (koreanCategoryMap[a.categoryId] || '기타').localeCompare(
          koreanCategoryMap[b.categoryId] || '기타',
        )
        break
      default:
        comparison = a.expenseId - b.expenseId
    }

    return sortOrder === 'asc' ? comparison : -comparison
  })

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

    // Ensure schedule data is loaded first
    if (scheduleStore.schedules.length === 0) {
      await scheduleStore.fetchSchedules(groupId.value)
    }

    const response = await apiClient.get(`/financial/expense-tracker/${groupId.value}`)

    console.log('Finance API response:', response.data)

    if (response.data.success) {
      // Convert API data to store format
      const storeExpenses = response.data.data.map((expense) => ({
        expenseId: expense.expenseId,
        datetime: expense.datetime,
        description: expense.description,
        amount: expense.amount,
        categoryId: expense.categoryId, // Use categoryId directly from API
        contentName: expense.contentName,
        contentId: expense.contentId,
      }))

      // Set expenses in store
      financeStore.setExpenses(storeExpenses)

      console.log('Loaded', storeExpenses.length, 'expenses to store')
      console.log('Schedule map has', scheduleMap.value.size, 'items')
    } else {
      throw new Error(response.data.message || '경비 내역을 불러오는데 실패했습니다.')
    }
  } catch (err) {
    console.error('Error fetching expenses:', err)

    let errorMessage = '경비 내역을 불러오는 중 오류가 발생했습니다.'

    if (err.response) {
      const status = err.response.status
      const message = err.response.data?.message || err.message

      if (status === 401) {
        errorMessage = '로그인이 필요합니다.'
      } else if (status === 403) {
        errorMessage = '경비 내역을 볼 권한이 없습니다.'
      } else if (status === 404) {
        errorMessage = '해당 그룹의 경비 내역을 찾을 수 없습니다.'
      } else {
        errorMessage = `서버 오류 (${status}): ${message}`
      }
    } else if (err.code === 'ECONNABORTED') {
      errorMessage = '요청 시간이 초과되었습니다. 네트워크 연결을 확인해주세요.'
    } else {
      errorMessage = err.message || errorMessage
    }

    error.value = errorMessage
    financeStore.setError(errorMessage)
  } finally {
    loading.value = false
    refreshing.value = false
    financeStore.setLoading(false)
  }
}

// Helper functions to get content info from REAL schedule data
const getContentImage = (contentId) => {
  if (!contentId) return null
  const schedule = scheduleMap.value.get(contentId)
  console.log(`Getting image for contentId ${contentId}:`, schedule?.attractionInfo?.firstImage1)
  return schedule?.attractionInfo?.firstImage1 || null
}

const getContentName = (contentId) => {
  if (!contentId) return null
  const schedule = scheduleMap.value.get(contentId)
  console.log(
    `Getting name for contentId ${contentId}:`,
    schedule?.attractionInfo?.title || schedule?.name,
  )
  return schedule?.attractionInfo?.title || schedule?.name || null
}

const getContentAddress = (contentId) => {
  if (!contentId) return null
  const schedule = scheduleMap.value.get(contentId)
  console.log(`Getting address for contentId ${contentId}:`, schedule?.attractionInfo?.addr1)
  return schedule?.attractionInfo?.addr1 || null
}

// Format amount with Korean number formatting
const formatAmount = (amount) => {
  return new Intl.NumberFormat('ko-KR').format(amount)
}

// Expose methods to parent component
defineExpose({
  refreshExpenses: () => fetchExpenses(true),
})

// Lifecycle
onMounted(() => {
  fetchExpenses()
})
</script>

<style scoped>
.finance-list {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.finance-header {
  flex-shrink: 0;
  padding: 16px 16px 12px 16px;
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  z-index: 10;
}

.finance-controls {
  flex-shrink: 0;
  padding: 16px;
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
}

.finance-cards-container {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background-color: #fafafa;
}

/* Custom scrollbar styling for cards container */
.finance-cards-container::-webkit-scrollbar {
  width: 8px;
}

.finance-cards-container::-webkit-scrollbar-track {
  background-color: #f1f1f1;
  border-radius: 4px;
}

.finance-cards-container::-webkit-scrollbar-thumb {
  background-color: #c1c1c1;
  border-radius: 4px;
}

.finance-cards-container::-webkit-scrollbar-thumb:hover {
  background-color: #a8a8a8;
}

/* Firefox scrollbar styling */
.finance-cards-container {
  scrollbar-width: thin;
  scrollbar-color: #c1c1c1 #f1f1f1;
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

.empty-state {
  border-radius: 8px;
  background-color: white;
  margin: 20px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.finance-cards {
  max-width: 100%;
}

.finance-card-wrapper {
  margin-bottom: 12px;
}

.finance-card-wrapper:last-child {
  margin-bottom: 16px;
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
  .finance-header {
    padding: 12px 12px 8px 12px;
  }

  .finance-controls {
    padding: 12px;
  }

  .finance-cards-container {
    padding: 12px;
  }

  .filter-section .v-col {
    margin-bottom: 8px;
  }

  .finance-cards-container::-webkit-scrollbar {
    width: 6px;
  }
}
</style>
