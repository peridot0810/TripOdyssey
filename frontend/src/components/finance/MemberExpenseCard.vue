<template>
  <v-card
    elevation="2"
    class="member-expense-card"
    :class="cardStatusClass"
  >
    <v-card-text class="pa-3">
      <div class="d-flex align-center justify-space-between">
        <!-- Left Section: User Info -->
        <div class="user-info d-flex align-center flex-grow-1">
          <v-avatar
            size="40"
            :color="avatarColor"
            class="mr-3"
          >
            <span class="text-white font-weight-bold">
              {{ getUserInitial(member.userId) }}
            </span>
          </v-avatar>

          <div class="user-details">
            <p class="text-body-1 font-weight-medium mb-1">
              {{ member.userId }}
            </p>
            <div class="d-flex align-center">
              <v-chip
                size="x-small"
                :color="statusChipColor"
                variant="flat"
                class="mr-2"
              >
                {{ paymentStatus }}
              </v-chip>
              <span class="text-caption text-grey">
                {{ formatCurrency(remainingAmount) }} {{ remainingText }}
              </span>
            </div>
          </div>
        </div>

        <!-- Center Section: Amount Info -->
        <div class="amount-info text-center mx-3">
          <div class="mb-1">
            <span class="text-caption text-grey">지출: </span>
            <input
              v-if="isEditing"
              v-model="editedExpense"
              type="number"
              min="0"
              step="1000"
              class="expense-input"
              @keyup.enter="handleSaveOrEdit"
              placeholder="0"
            />
            <span v-else class="text-body-2 font-weight-medium">
              ₩{{ formatCurrency(member.expenseToPay) }}
            </span>
          </div>
          <div>
            <span class="text-caption text-grey">납부: </span>
            <span class="text-body-2 font-weight-medium text-success">
              ₩{{ formatCurrency(member.paidAmount) }}
            </span>
          </div>
        </div>

        <!-- Right Section: Save/Edit Button -->
        <div class="action-section">
          <v-btn
            icon
            size="small"
            variant="flat"
            :color="isEditing ? 'success' : 'orange'"
            @click="handleSaveOrEdit"
            :loading="isSaving"
          >
            <SvgIcon type="mdi" :path="isEditing ? checkIcon : pencilIcon" size="20" />
          </v-btn>
        </div>
      </div>

      <!-- Progress Bar -->
      <div class="mt-3">
        <v-progress-linear
          :model-value="paymentProgress"
          :color="progressColor"
          height="6"
          rounded
          class="mb-1"
        ></v-progress-linear>
        <div class="d-flex justify-space-between text-caption text-grey">
          <span>납부율: {{ Math.round(paymentProgress) }}%</span>
          <span v-if="remainingAmount !== 0" :class="remainingAmountClass">
            {{ remainingAmount > 0 ? '+' : '' }}₩{{ formatCurrency(Math.abs(remainingAmount)) }}
          </span>
        </div>
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useMemberExpenseStore } from '@/stores/memberExpense'
import { apiClient } from '@/utils/apiClient'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiCheck, mdiPencil } from '@mdi/js'

const checkIcon = mdiCheck
const pencilIcon = mdiPencil

// Router and Store
const route = useRoute()
const memberExpenseStore = useMemberExpenseStore()

// Props
const props = defineProps({
  member: {
    type: Object,
    required: true,
    validator: (member) => {
      return member &&
             typeof member.userId === 'string' &&
             typeof member.expenseToPay === 'number' &&
             typeof member.paidAmount === 'number'
    }
  }
})

// Refs
const isEditing = ref(false)
const editedExpense = ref('')
const isSaving = ref(false)

// Computed
const groupId = computed(() => {
  return route.params.groupId
})

const remainingAmount = computed(() => {
  return props.member.expenseToPay - props.member.paidAmount
})

const paymentProgress = computed(() => {
  if (props.member.expenseToPay === 0) return 100
  return Math.min(100, Math.max(0, (props.member.paidAmount / props.member.expenseToPay) * 100))
})

const paymentStatus = computed(() => {
  const remaining = remainingAmount.value
  if (remaining === 0) return '완납'
  if (remaining > 0) return '미납'
  return '초과납부'
})

const statusChipColor = computed(() => {
  const remaining = remainingAmount.value
  if (remaining === 0) return 'success'
  if (remaining > 0) return 'error'
  return 'warning'
})

const progressColor = computed(() => {
  const remaining = remainingAmount.value
  if (remaining === 0) return 'success'
  if (remaining > 0) return 'error'
  return 'warning'
})

const cardStatusClass = computed(() => {
  const remaining = remainingAmount.value
  if (remaining === 0) return 'card-paid'
  if (remaining > 0) return 'card-unpaid'
  return 'card-overpaid'
})

const remainingText = computed(() => {
  const remaining = remainingAmount.value
  if (remaining === 0) return ''
  if (remaining > 0) return '미납'
  return '초과납부'
})

const remainingAmountClass = computed(() => {
  const remaining = remainingAmount.value
  if (remaining > 0) return 'text-error'
  return 'text-warning'
})

const avatarColor = computed(() => {
  // Generate a consistent color based on userId
  const colors = ['orange', 'deep-orange', 'amber', 'orange-darken-2', 'orange-lighten-2', 'deep-orange-darken-2']
  const hash = props.member.userId.split('').reduce((a, b) => {
    a = ((a << 5) - a) + b.charCodeAt(0)
    return a & a
  }, 0)
  return colors[Math.abs(hash) % colors.length]
})

// Methods
const formatCurrency = (amount) => {
  return new Intl.NumberFormat('ko-KR').format(amount || 0)
}

const getUserInitial = (userId) => {
  return userId.charAt(0).toUpperCase()
}

const handleSaveOrEdit = async () => {
  if (!isEditing.value) {
    // Enter edit mode
    isEditing.value = true
    editedExpense.value = props.member.expenseToPay.toString()
    return
  }

  // Save changes
  if (!editedExpense.value || editedExpense.value < 0) {
    console.error('Invalid expense amount')
    return
  }

  if (!groupId.value) {
    console.error('Group ID not found')
    return
  }

  try {
    isSaving.value = true

    const newFee = parseInt(editedExpense.value)

    const response = await apiClient.put(`/financial/${groupId.value}/budget`, [
      {
        userId: props.member.userId,
        groupId: parseInt(groupId.value),
        newFee: newFee
      }
    ])

    if (response.data.success) {
      // Update the specific member in the store
      memberExpenseStore.updateMemberExpense(props.member.userId, {
        expenseToPay: newFee
      })

      // Exit edit mode
      isEditing.value = false
      editedExpense.value = ''

      console.log('Member expense updated successfully')
    } else {
      throw new Error(response.data.message || 'Failed to update member expense')
    }
  } catch (error) {
    console.error('Error updating member expense:', error)

    let errorMessage = '멤버 경비 업데이트 중 오류가 발생했습니다.'

    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.message

      if (status === 400) {
        errorMessage = `입력 데이터 오류: ${message}`
      } else if (status === 401) {
        errorMessage = '로그인이 필요합니다.'
      } else if (status === 403) {
        errorMessage = '멤버 경비를 수정할 권한이 없습니다.'
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

    memberExpenseStore.setError(errorMessage)
  } finally {
    isSaving.value = false
  }
}
</script>

<style scoped>
.member-expense-card {
  transition: all 0.2s ease;
  border-left: 4px solid transparent;
}

.member-expense-card:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}

.card-paid {
  border-left-color: #4caf50;
  background-color: #f8fff8;
}

.card-unpaid {
  border-left-color: #f44336;
  background-color: #fff8f8;
}

.card-overpaid {
  border-left-color: #ff9800;
  background-color: #fff9f0;
}

.user-info {
  min-width: 0; /* Allows text to truncate properly */
}

.user-details {
  min-width: 0;
  flex: 1;
}

.user-details p {
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.amount-info {
  flex-shrink: 0;
  min-width: 100px;
}

.expense-input {
  width: 70px;
  padding: 2px 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 0.75rem;
  text-align: center;
  background: white;
}

.expense-input:focus {
  outline: none;
  border-color: #ff9800;
  box-shadow: 0 0 0 2px rgba(255, 152, 0, 0.2);
}

.action-section {
  flex-shrink: 0;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .amount-info {
    display: none;
  }

  .user-details .text-caption {
    font-size: 0.7rem;
  }
}
</style>
