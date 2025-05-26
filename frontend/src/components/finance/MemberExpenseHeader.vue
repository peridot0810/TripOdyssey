<template>
  <div class="member-expense-header">
    <!-- Header Title and Refresh -->
    <div class="d-flex justify-space-between align-center mb-4">
      <h2 class="text-h6 font-weight-bold">그룹 멤버</h2>
      <div class="d-flex align-center gap-3">
        <v-btn
          icon
          size="small"
          variant="text"
          color="orange"
          @click="refreshExpenses"
          :loading="memberExpenseStore.isLoading"
        >
          <SvgIcon type="mdi" :path="refreshIcon" size="20" />
        </v-btn>
        <v-chip size="small" color="orange" variant="flat">
          {{ memberExpenseStore.totalMembersCount }}명
        </v-chip>
      </div>
    </div>

    <v-divider class="mb-4"></v-divider>

    <!-- Summary Cards -->
    <div class="summary-section mb-4">
      <v-row dense>
        <v-col cols="6">
          <v-card elevation="1" class="pa-3 text-center">
            <p class="text-caption text-grey mb-1">예상 총 지출</p>
            <p class="text-h6 font-weight-bold text-orange">
              ₩{{ formatCurrency(memberExpenseStore.totalExpenseToPay) }}
            </p>
          </v-card>
        </v-col>
        <v-col cols="6">
          <v-card elevation="1" class="pa-3 text-center">
            <p class="text-caption text-grey mb-1">실제 수납액</p>
            <p class="text-h6 font-weight-bold text-success">
              ₩{{ formatCurrency(memberExpenseStore.totalPaidAmount) }}
            </p>
          </v-card>
        </v-col>
      </v-row>
    </div>

    <!-- Budget Setting Section -->
    <div class="budget-section mb-4">
      <v-card elevation="1" class="pa-4">
        <div class="d-flex justify-space-between align-center mb-3">
          <h3 class="text-subtitle-1 font-weight-medium">개인별 예상 회비</h3>
          <v-chip
            size="small"
            :color="budgetStatus.color"
            variant="flat"
          >
            {{ budgetStatus.text }}
          </v-chip>
        </div>

        <div class="budget-input-section">
          <v-form @submit.prevent="submitBudget" ref="budgetForm">
            <div class="budget-input-container">
              <v-text-field
                v-model="budgetInput"
                type="number"
                min="0"
                step="1000"
                variant="outlined"
                density="compact"
                placeholder="회비 금액 입력"
                prefix="₩"
                hide-details
                class="budget-input-field"
                :disabled="isSubmittingBudget"
                :rules="budgetRules"
              />

<v-btn
  type="submit"
  icon
  color="orange"
  class="budget-submit-circle"
  :loading="isSubmittingBudget"
  :disabled="!budgetInput || budgetInput <= 0"
>
  <SvgIcon type="mdi" :path="checkIcon" size="20" />
</v-btn>


            </div>
          </v-form>

          <div class="budget-info mt-2">
            <div class="d-flex justify-space-between text-caption text-grey">
              <span>현재 개인별 예상액:</span>
              <span class="font-weight-medium">
                ₩{{ formatCurrency(currentAverageBudget) }}
              </span>
            </div>
            <div v-if="budgetInput && budgetInput > 0" class="d-flex justify-space-between text-caption text-orange mt-1">
              <span>설정 시 총 예상액:</span>
              <span class="font-weight-medium">
                ₩{{ formatCurrency(budgetInput * memberExpenseStore.totalMembersCount) }}
              </span>
            </div>
          </div>
        </div>
      </v-card>
    </div>

    <!-- Outstanding Amount Card -->
    <v-card elevation="1" class="pa-3 text-center mb-4">
      <p class="text-caption text-grey mb-1">미수금</p>
      <p class="text-h6 font-weight-bold" :class="outstandingAmountColor">
        ₩{{ formatCurrency(memberExpenseStore.totalOutstandingAmount) }}
      </p>
    </v-card>

    <!-- Error Alert -->
    <v-alert
      v-if="budgetError"
      type="error"
      class="mb-4"
      closable
      @click:close="budgetError = null"
    >
      {{ budgetError }}
    </v-alert>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useMemberExpenseStore } from '@/stores/memberExpense'
import { apiClient } from '@/utils/apiClient'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiRefresh } from '@mdi/js'
import { mdiCheck } from '@mdi/js'
const checkIcon = mdiCheck


const refreshIcon = mdiRefresh

// Router and Store
const route = useRoute()
const memberExpenseStore = useMemberExpenseStore()

// Refs
const budgetInput = ref('')
const isSubmittingBudget = ref(false)
const budgetError = ref(null)
const budgetForm = ref(null)

// Computed
const groupId = computed(() => {
  return route.params.groupId
})

const currentAverageBudget = computed(() => {
  if (memberExpenseStore.totalMembersCount === 0) return 0
  return Math.round(memberExpenseStore.totalExpenseToPay / memberExpenseStore.totalMembersCount)
})

const outstandingAmountColor = computed(() => {
  const amount = memberExpenseStore.totalOutstandingAmount
  if (amount > 0) return 'text-error'
  if (amount < 0) return 'text-warning'
  return 'text-success'
})

const budgetStatus = computed(() => {
  const totalMembers = memberExpenseStore.totalMembersCount
  const totalExpense = memberExpenseStore.totalExpenseToPay

  if (totalMembers === 0) {
    return { color: 'grey', text: '멤버 없음' }
  }

  if (totalExpense === 0) {
    return { color: 'warning', text: '미설정' }
  }

  return { color: 'success', text: '설정됨' }
})

// Validation rules
const budgetRules = [
  value => {
    if (!value) return true // Allow empty for optional field
    if (value < 0) return '금액은 0 이상이어야 합니다'
    if (value > 10000000) return '금액이 너무 큽니다'
    return true
  }
]

// Methods
const formatCurrency = (amount) => {
  return new Intl.NumberFormat('ko-KR').format(amount || 0)
}

const refreshExpenses = async () => {
  await fetchMemberExpenseList()
}

const fetchMemberExpenseList = async () => {
  if (!groupId.value) {
    memberExpenseStore.setError('그룹 ID를 찾을 수 없습니다.')
    return
  }

  try {
    memberExpenseStore.setLoading(true)
    memberExpenseStore.clearError()

    const response = await apiClient.get(`/financial/${groupId.value}/member-list`)

    if (response.data.success) {
      memberExpenseStore.setMemberExpenseList(response.data.data)
    } else {
      throw new Error(response.data.message || '멤버 목록을 불러오는데 실패했습니다.')
    }
  } catch (error) {
    console.error('Error fetching member expense list:', error)

    let errorMessage = '멤버 목록을 불러오는 중 오류가 발생했습니다.'

    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.message

      if (status === 401) {
        errorMessage = '로그인이 필요합니다.'
      } else if (status === 403) {
        errorMessage = '접근 권한이 없습니다.'
      } else if (status === 404) {
        errorMessage = '해당 그룹을 찾을 수 없습니다.'
      } else {
        errorMessage = `서버 오류 (${status}): ${message}`
      }
    } else if (error.code === 'ECONNABORTED') {
      errorMessage = '요청 시간이 초과되었습니다. 네트워크 연결을 확인해주세요.'
    } else {
      errorMessage = error.message || errorMessage
    }

    memberExpenseStore.setError(errorMessage)
  } finally {
    memberExpenseStore.setLoading(false)
  }
}

const submitBudget = async () => {
  if (!budgetInput.value || budgetInput.value <= 0) {
    budgetError.value = '올바른 금액을 입력해주세요.'
    return
  }

  if (!groupId.value) {
    budgetError.value = '그룹 ID를 찾을 수 없습니다.'
    return
  }

  // Validate form
  const { valid } = await budgetForm.value.validate()
  if (!valid) return

  try {
    isSubmittingBudget.value = true
    budgetError.value = null

    const budgetAmount = parseInt(budgetInput.value)

    const response = await apiClient.post(`/financial/${groupId.value}/budget`, {
      budget: budgetAmount
    })

    if (response.data.success) {
      // Update all members' expenseToPay in the store
      const updatedMembers = memberExpenseStore.memberExpenseList.map(member => ({
        ...member,
        expenseToPay: budgetAmount
      }))

      memberExpenseStore.setMemberExpenseList(updatedMembers)

      // Clear the input
      budgetInput.value = ''

      console.log('Budget updated successfully')

    } else {
      throw new Error(response.data.message || '회비 설정에 실패했습니다.')
    }
  } catch (error) {
    console.error('Error setting budget:', error)

    let errorMessage = '회비 설정 중 오류가 발생했습니다.'

    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.message

      if (status === 400) {
        errorMessage = `입력 데이터 오류: ${message}`
      } else if (status === 401) {
        errorMessage = '로그인이 필요합니다.'
      } else if (status === 403) {
        errorMessage = '회비 설정 권한이 없습니다.'
      } else if (status === 404) {
        errorMessage = '해당 그룹을 찾을 수 없습니다.'
      } else {
        errorMessage = `서버 오류 (${status}): ${message}`
      }
    } else if (error.code === 'ECONNABORTED') {
      errorMessage = '요청 시간이 초과되었습니다. 다시 시도해주세요.'
    } else {
      errorMessage = error.message || errorMessage
    }

    budgetError.value = errorMessage
  } finally {
    isSubmittingBudget.value = false
  }
}
</script>

<style scoped>
.member-expense-header {
  flex-shrink: 0;
}

.summary-section {
  flex-shrink: 0;
}

.budget-section {
  flex-shrink: 0;
}

.budget-input-section {
  margin-top: 8px;
}

.budget-input-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.budget-input-field {
  max-width: 500px;
  flex: 1;
}

.budget-info {
  background-color: #f8f9fa;
  border-radius: 4px;
  padding: 8px 12px;
}

/* Orange color styles */
.text-orange {
  color: #ff9800 !important;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .budget-input-container {
    flex-direction: column;
    gap: 8px;
  }

  .budget-input-field {
    max-width: 100%;
  }

  .budget-input-container .v-btn {
    align-self: stretch;
  }
}

.budget-submit-btn {
  min-width: 140px;
  border-radius: 8px;
}

@media (max-width: 600px) {
  .budget-submit-btn {
    width: 100%;
  }
}


.budget-submit-circle {
  border-radius: 50% !important;
  width: 36px;
  height: 36px;
  padding: 0;
  min-width: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

</style>
