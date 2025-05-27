<template>
  <v-card class="finance-card" elevation="2">
    <div class="card-content">
      <!-- Content Block (Image + Location or Icon) -->
      <div class="content-block">
        <div v-if="expense.contentId && contentImage && contentName" class="content-with-image">
          <div class="image-container">
            <v-img
              :src="contentImage"
              :alt="contentName"
              height="80"
              width="80"
              cover
              class="rounded"
            >
              <template v-slot:placeholder>
                <div class="d-flex align-center justify-center fill-height">
                  <v-progress-circular
                    indeterminate
                    color="primary"
                    size="20"
                  ></v-progress-circular>
                </div>
              </template>
            </v-img>
          </div>
          <div class="location-info">
            <div class="location-title text-caption font-weight-bold text-truncate">
              {{ contentName }}
            </div>
            <div class="location-address text-caption text-grey text-truncate">
              {{ contentAddress || '주소 정보 없음' }}
            </div>
          </div>
        </div>

        <div v-else class="content-without-image d-flex flex-column align-center justify-center">
          <SvgIcon type="mdi" :path="receiptIcon" size="28" color="grey-lighten-1" />
          <span class="text-caption text-grey-lighten-1 mt-1">일반 지출</span>
        </div>
      </div>

      <v-divider vertical class="divider"></v-divider>

      <!-- Category Block -->
      <div class="category-block d-flex align-center justify-center">
        <v-chip size="small" color="orange" variant="flat" class="text-caption">
          {{ getCategoryDisplayName(expense.categoryId) }}
        </v-chip>
      </div>

      <v-divider vertical class="divider"></v-divider>

      <!-- Description Block -->
      <div class="description-block d-flex align-center">
        <div class="description-text">
          <p class="text-body-2 ma-0">{{ expense.description }}</p>
        </div>
      </div>

      <v-divider vertical class="divider"></v-divider>

      <!-- DateTime Block -->
      <div class="datetime-block d-flex align-center justify-center">
        <div class="datetime-info text-center">
          <div class="date-text text-body-2 font-weight-medium">
            {{ formatDate(expense.datetime) }}
          </div>
          <div class="time-text text-caption text-grey">
            {{ formatTime(expense.datetime) }}
          </div>
        </div>
      </div>

      <v-divider vertical class="divider"></v-divider>

      <!-- Amount Block -->
      <div class="amount-block d-flex align-center justify-center">
        <div class="amount-info text-center">
          <div class="amount-text text-h6 font-weight-bold text-primary">
            {{ formatAmount(expense.amount) }}
          </div>
          <div class="currency-text text-caption text-grey">원</div>
        </div>
      </div>

      <!-- Delete Button Block (only show on finance page) -->
      <template v-if="isFinancePage">
        <v-divider vertical class="divider"></v-divider>

        <div class="delete-block d-flex align-center justify-center">
          <v-btn
            icon
            size="small"
            color="error"
            variant="text"
            class="delete-btn"
            @click="handleDelete"
            :loading="deleteLoading"
            :disabled="deleteLoading"
          >
            <SvgIcon type="mdi" :path="deleteIcon" size="18" />
          </v-btn>
        </div>
      </template>
    </div>

    <!-- Delete Confirmation Dialog -->
    <v-dialog v-model="showDeleteDialog" max-width="400px" persistent>
      <v-card>
        <v-card-title class="text-h6">
          <SvgIcon type="mdi" :path="alertCircleIcon" size="20" color="error" class="mr-2" />
          경비 삭제
        </v-card-title>

        <v-card-text>
          <p class="text-body-1 mb-2">이 경비 항목을 삭제하시겠습니까?</p>
          <div class="delete-preview pa-3 bg-grey-lighten-4 rounded">
            <div class="text-body-2 font-weight-medium">{{ expense.description }}</div>
            <div class="text-body-2 text-primary font-weight-bold">
              {{ formatAmount(expense.amount) }}원
            </div>
            <div class="text-caption text-grey">
              {{ formatDate(expense.datetime) }} {{ formatTime(expense.datetime) }}
            </div>
          </div>
          <p class="text-caption text-error mt-2 mb-0">이 작업은 되돌릴 수 없습니다.</p>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="grey"
            variant="outlined"
            @click="showDeleteDialog = false"
            :disabled="deleteLoading"
          >
            취소
          </v-btn>
          <v-btn color="error" variant="elevated" @click="confirmDelete" :loading="deleteLoading">
            삭제
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useFinanceStore } from '@/stores/finance'
import { apiClient } from '@/utils/apiClient'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiReceipt, mdiDelete, mdiAlertCircle } from '@mdi/js'

const receiptIcon = mdiReceipt
const deleteIcon = mdiDelete
const alertCircleIcon = mdiAlertCircle

const props = defineProps({
  expense: {
    type: Object,
    required: true,
  },
  contentImage: {
    type: String,
    default: null,
  },
  contentName: {
    type: String,
    default: null,
  },
  contentAddress: {
    type: String,
    default: null,
  },
})

// Composables
const route = useRoute()
const financeStore = useFinanceStore()

// Reactive data
const showDeleteDialog = ref(false)
const deleteLoading = ref(false)

// Check if current route is finance page
const isFinancePage = computed(() => {
  return route.path.includes('/finance')
})

// Get groupId from route
const groupId = route.params.groupId

// Category display mapping (Korean names)
const categoryDisplayMap = {
  1: '교통비',
  2: '숙박비',
  3: '식비',
  4: '레저',
  5: '공용품',
  6: '기타',
}

// Get Korean category name
const getCategoryDisplayName = (categoryId) => {
  return categoryDisplayMap[categoryId] || '기타'
}

// Format date to Korean format
const formatDate = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleDateString('ko-KR', {
    month: 'short',
    day: 'numeric',
  })
}

// Format time
const formatTime = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  })
}

// Format amount with Korean number formatting
const formatAmount = (amount) => {
  return new Intl.NumberFormat('ko-KR').format(amount)
}

// Handle delete button click
const handleDelete = () => {
  showDeleteDialog.value = true
}

// Confirm deletion
const confirmDelete = async () => {
  deleteLoading.value = true

  try {
    console.log('Deleting expense:', props.expense.expenseId, 'for group:', groupId)

    // Make API call to delete expense
    const response = await apiClient.delete(`/financial/expense-tracker/${groupId}`, {
      params: {
        expenseId: props.expense.expenseId,
      },
    })

    console.log('Delete API response:', response.data)

    if (response.data.success) {
      // Remove from Pinia store
      financeStore.removeExpense(props.expense.expenseId)

      console.log('경비가 성공적으로 삭제되었습니다!')

      // Close dialog
      showDeleteDialog.value = false
    } else {
      throw new Error(response.data.message || '경비 삭제에 실패했습니다.')
    }
  } catch (error) {
    console.error('Error deleting expense:', error)

    let errorMessage = '경비 삭제 중 오류가 발생했습니다.'

    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.message

      if (status === 400) {
        errorMessage = `입력 데이터 오류: ${message}`
      } else if (status === 401) {
        errorMessage = '로그인이 필요합니다.'
      } else if (status === 403) {
        errorMessage = '이 경비를 삭제할 권한이 없습니다.'
      } else if (status === 404) {
        errorMessage = '삭제하려는 경비를 찾을 수 없습니다.'
      } else {
        errorMessage = `서버 오류 (${status}): ${message}`
      }
    } else if (error.code === 'ECONNABORTED') {
      errorMessage = '요청 시간이 초과되었습니다. 다시 시도해주세요.'
    } else {
      errorMessage = error.message || errorMessage
    }

    // Set error in store
    financeStore.setError(errorMessage)
  } finally {
    deleteLoading.value = false
  }
}
</script>

<style scoped>
.finance-card {
  width: 100%;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  margin-bottom: 12px;
  position: relative;
}

.finance-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15) !important;
}

.finance-card:hover .delete-btn {
  opacity: 1;
}

.card-content {
  display: flex;
  align-items: stretch;
  min-height: 100px;
  padding: 12px;
}

.content-block {
  width: 120px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.content-with-image {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.image-container {
  width: 80px;
  height: 80px;
  margin: 0 auto 8px auto;
  flex-shrink: 0;
}

.location-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.location-title {
  line-height: 1.2;
  margin-bottom: 2px;
}

.location-address {
  line-height: 1.2;
}

.content-without-image {
  height: 100%;
  min-height: 80px;
}

.category-block {
  width: 100px;
  flex-shrink: 0;
  padding: 0 8px;
}

.description-block {
  flex: 1;
  min-width: 150px;
  padding: 0 12px;
}

.description-text {
  word-break: break-word;
  hyphens: auto;
}

.datetime-block {
  width: 80px;
  flex-shrink: 0;
  padding: 0 8px;
}

.datetime-info {
  width: 100%;
}

.date-text {
  line-height: 1.2;
  margin-bottom: 2px;
}

.time-text {
  line-height: 1.2;
}

.amount-block {
  width: 100px;
  flex-shrink: 0;
  padding: 0 8px;
}

.amount-info {
  width: 100%;
}

.amount-text {
  line-height: 1.2;
  margin-bottom: 2px;
}

.currency-text {
  line-height: 1.2;
}

.delete-block {
  width: 50px;
  flex-shrink: 0;
  padding: 0 8px;
}

.delete-btn {
  opacity: 0.6;
  transition: opacity 0.2s;
}

.delete-btn:hover {
  opacity: 1;
  background-color: rgba(244, 67, 54, 0.1);
}

.divider {
  margin: 0 4px;
  opacity: 0.3;
}

.delete-preview {
  border-left: 4px solid #f44336;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .card-content {
    flex-direction: column;
    min-height: auto;
  }

  .content-block,
  .category-block,
  .datetime-block,
  .amount-block,
  .delete-block {
    width: 100%;
    flex-shrink: 1;
    margin-bottom: 8px;
  }

  .description-block {
    min-width: auto;
    margin-bottom: 8px;
  }

  .divider {
    display: none;
  }

  .content-with-image {
    flex-direction: row;
    align-items: center;
  }

  .image-container {
    margin: 0 12px 0 0;
  }

  .location-info {
    justify-content: flex-start;
  }

  .delete-block {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 0;
  }

  .delete-btn {
    opacity: 1;
  }
}

.content-block {
  width: 160px; /* previously 120px */
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.image-container {
  width: 100%;
  height: 100px; /* optional: slightly taller if you want */
  margin: 0 auto 8px auto;
  flex-shrink: 0;
}

.image-container .v-img {
  width: 100% !important;
  height: 100% !important;
  object-fit: cover;
  border-radius: 8px;
}
</style>
