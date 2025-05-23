<template>
  <div class="finance-list">
    <h2 class="text-h6 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-wallet-outline</v-icon>
      여행 경비 내역
    </h2>

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

    <!-- Empty State -->
    <div v-if="filteredFinanceData.length === 0" class="empty-state text-center py-8">
      <v-icon size="x-large" color="grey">mdi-wallet-outline</v-icon>
      <p class="text-body-1 text-grey-darken-1 mt-3">경비 내역이 없습니다</p>
    </div>

    <!-- Finance Cards -->
    <div v-else class="finance-cards">
      <div
        v-for="expense in filteredFinanceData"
        :key="expense.expenseId"
        class="finance-card-wrapper"
      >
        <FinanceCard
          :expense="expense"
          :content-image="getContentImage(expense.contentId)"
          :content-name="getContentName(expense.contentId)"
          :content-address="getContentAddress(expense.contentId)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import FinanceCard from './FinanceCard.vue'
import { dummyFinanceData } from '@/data/financeData.js'
import { dummyScheduleData } from '@/data/schedule/dummyScheduleData.js'

// Reactive data
const financeData = ref([...dummyFinanceData])
const selectedCategory = ref(null)
const sortBy = ref('datetime-desc')

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
  const categories = [...new Set(financeData.value.map((item) => item.categoryName))]
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
const totalAmount = computed(() => {
  return financeData.value.reduce((sum, expense) => sum + expense.amount, 0)
})

const filteredFinanceData = computed(() => {
  let filtered = financeData.value

  // Apply category filter
  if (selectedCategory.value) {
    filtered = filtered.filter((expense) => expense.categoryName === selectedCategory.value)
  }

  // Apply sorting
  filtered = [...filtered].sort((a, b) => {
    switch (sortBy.value) {
      case 'datetime-desc':
        return new Date(b.datetime) - new Date(a.datetime)
      case 'datetime-asc':
        return new Date(a.datetime) - new Date(b.datetime)
      case 'amount-desc':
        return b.amount - a.amount
      case 'amount-asc':
        return a.amount - b.amount
      case 'category':
        return a.categoryName.localeCompare(b.categoryName)
      default:
        return 0
    }
  })

  return filtered
})

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
</script>

<style scoped>
.finance-list {
  padding: 16px;
}

.summary-section {
  margin-bottom: 20px;
}

.filter-section {
  margin-bottom: 20px;
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
}
</style>
