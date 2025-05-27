<template>
  <v-card class="transportation-card" elevation="2">
    <v-card-item class="pa-4">
      <!-- Top Row: Transportation Type and Delete Button -->
      <div class="d-flex justify-space-between align-start mb-3">
        <div class="transportation-type">
          <v-chip
            size="large"
            class="text-white font-weight-bold"
            style="
              background-color: #003c78;
              display: flex;
              align-items: center;
              justify-content: center;
              font-size: 14px;
              padding: 8px 16px;
            "
          >
            {{ getTransportationType(transportation.typeId) }}
          </v-chip>
        </div>
        <v-btn
          v-if="isLogisticsPage"
          icon
          size="small"
          variant="flat"
          class="delete-btn"
          @click="handleDelete"
          :loading="isDeleting"
        >
          <SvgIcon type="mdi" :path="deleteIcon" size="20" color="#d32f2f" />
        </v-btn>
      </div>

      <!-- Departure and Arrival Labels -->
      <div class="time-labels d-flex justify-space-between mb-1">
        <span class="text-caption text-grey-darken-1">출발</span>
        <span class="text-caption text-grey-darken-1">도착</span>
      </div>

      <!-- Departure and Arrival Times with Arrow -->
      <div class="time-section d-flex justify-space-between align-center mb-2">
        <span class="time-text">{{ transportation.departure.split(' ')[1].slice(0, 5) }}</span>
        <div class="arrow-container">
          <img src="/img/barcode.jpg" alt="barcode" style="height: 80px" />
        </div>
        <span class="time-text">{{ transportation.arrival.split(' ')[1].slice(0, 5) }}</span>
      </div>

      <!-- Dates row -->
      <div
        class="date-section d-flex justify-space-between text-caption text-grey-darken-1 mb-2 px-1"
      >
        <span class="date-text">{{ transportation.departure.split(' ')[0] }}</span>
        <span class="date-text">{{ transportation.arrival.split(' ')[0] }}</span>
      </div>

      <!-- Departure and Arrival Locations -->
      <div class="location-section d-flex justify-space-between mb-3">
        <span class="location-text">{{ transportation.from }}</span>
        <span class="location-text">{{ transportation.to }}</span>
      </div>

      <!-- Divider -->
      <v-divider class="mb-3"></v-divider>

      <!-- Description and Edit Button Row -->
      <div class="description-section d-flex justify-space-between align-end">
        <div class="description-content flex-grow-1">
          <p class="text-body-2 text-grey-darken-1 mb-0">
            {{ transportation.description || '상세 정보 없음' }}
          </p>
        </div>
        <v-btn
          v-if="isLogisticsPage"
          size="small"
          variant="outlined"
          color="primary"
          class="edit-btn ml-3"
          @click="handleEdit"
        >
          <SvgIcon type="mdi" :path="pencilIcon" size="16" class="mr-1" />
          편집
        </v-btn>
      </div>
    </v-card-item>

    <!-- Delete Confirmation Dialog -->
    <v-dialog v-model="showDeleteDialog" max-width="400px">
      <v-card>
        <v-card-title class="text-h6">
          <SvgIcon type="mdi" :path="alertIcon" size="20" color="error" class="mr-2" />
          교통편 삭제
        </v-card-title>
        <v-card-text>
          <p class="mb-2">이 교통편을 삭제하시겠습니까?</p>
          <div class="delete-preview pa-3 bg-grey-lighten-4 rounded">
            <div class="d-flex align-center mb-1">
              <v-chip size="x-small" color="secondary" variant="flat" class="mr-2">
                {{ getTransportationType(transportation.typeId) }}
              </v-chip>
              <span class="text-body-2 font-weight-medium">
                {{ transportation.departure }} → {{ transportation.arrival }}
              </span>
            </div>
            <div class="text-caption text-grey-darken-1">
              {{ transportation.from }} → {{ transportation.to }}
            </div>
          </div>
          <p class="text-caption text-error mt-2 mb-0">⚠️ 이 작업은 되돌릴 수 없습니다.</p>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="text" @click="showDeleteDialog = false">취소</v-btn>
          <v-btn color="error" variant="elevated" @click="confirmDelete" :loading="isDeleting">
            삭제
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Edit Transportation Modal -->
    <EditTransportationModal
      v-model="showEditModal"
      :transportation="transportation"
      @transportation-updated="handleTransportationUpdated"
    />
  </v-card>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useTransportationStore } from '@/stores/transportation'
import { apiClient } from '@/utils/apiClient'
import EditTransportationModal from '@/components/transportation/EditTransportationModal.vue'

import SvgIcon from '@jamescoyle/vue-icon'
import { mdiDelete, mdiPencil, mdiAlertCircle } from '@mdi/js'

const deleteIcon = mdiDelete
const pencilIcon = mdiPencil
const alertIcon = mdiAlertCircle

// Props
const props = defineProps({
  transportation: {
    type: Object,
    required: true,
  },
})

console.log(props)
// Store and Route
const route = useRoute()
const transportationStore = useTransportationStore()

// State
const showDeleteDialog = ref(false)
const showEditModal = ref(false)
const isDeleting = ref(false)

// Check if current route is logistics page
const isLogisticsPage = computed(() => {
  return route.path.includes('/logistics')
})

// Helper function to get transportation type name
const getTransportationType = (typeId) => {
  const types = {
    1: '항공편',
    2: '기차',
    3: '버스',
    4: '지하철',
    5: '택시',
    6: '렌터카',
    7: '도보',
    8: '기타',
  }
  return types[typeId] || '교통편'
}

// Methods
const handleDelete = () => {
  showDeleteDialog.value = true
}

const confirmDelete = async () => {
  isDeleting.value = true

  try {
    // Get group ID from route
    const groupId = route.params.groupId
    if (!groupId) {
      throw new Error('그룹 ID를 찾을 수 없습니다.')
    }

    // Call API to delete transportation
    const response = await apiClient.delete(`/transportation/${groupId}`, {
      params: {
        transportationId: props.transportation.transportationId,
      },
    })

    if (response.data.success) {
      // Remove from store
      const result = transportationStore.removeTransportation(props.transportation.transportationId)

      if (result.success) {
        showDeleteDialog.value = false
        console.log('교통편 삭제 성공:', response.data.message)
      } else {
        // This shouldn't happen if API succeeded, but handle gracefully
        console.warn('Store에서 삭제 실패:', result.error)
        showDeleteDialog.value = false
        // Force a page refresh or refetch might be needed here
      }
    } else {
      throw new Error(response.data.message || '교통편 삭제에 실패했습니다.')
    }
  } catch (err) {
    console.error('교통편 삭제 오류:', err)

    let errorMessage = '교통편 삭제 중 오류가 발생했습니다.'

    if (err.response) {
      const status = err.response.status
      const message = err.response.data?.message || err.message

      if (status === 400) {
        errorMessage = `잘못된 요청: ${message}`
      } else if (status === 401) {
        errorMessage = '로그인이 필요합니다.'
      } else if (status === 403) {
        errorMessage = '교통편 삭제 권한이 없습니다.'
      } else if (status === 404) {
        errorMessage = '해당 교통편을 찾을 수 없습니다.'
      } else {
        errorMessage = `서버 오류 (${status}): ${message}`
      }
    } else if (err.code === 'ECONNABORTED') {
      errorMessage = '요청 시간이 초과되었습니다. 다시 시도해주세요.'
    } else {
      errorMessage = err.message || errorMessage
    }

    // Set error in store for display
    transportationStore.error = errorMessage
    showDeleteDialog.value = false
  } finally {
    isDeleting.value = false
  }
}

const handleEdit = () => {
  showEditModal.value = true
}

const handleTransportationUpdated = (updatedTransportation) => {
  showEditModal.value = false
  // Optional: Show success message
  console.log('교통편이 수정되었습니다:', updatedTransportation)
}
</script>

<style scoped>
.transportation-card {
  width: 100%;
  border-radius: 12px;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  border: 1px solid #e0e0e0;
  position: relative;
  background-color: rgb(248, 248, 248);
}

.transportation-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.12) !important;
}

.transportation-type {
  display: flex;
  justify-content: flex-start;
}

.delete-btn {
  opacity: 1; /* Make more visible */
  transition: all 0.2s;
}

.delete-btn:hover {
  border-color: rgba(244, 67, 54, 0.5);
  transform: scale(1.05);
}

.time-labels {
  font-weight: 500;
}

.time-section {
  position: relative;
}

.arrow-container {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  padding: 0 8px;
}

.location-section {
  font-weight: 500;
}

.description-section {
  min-height: 40px;
}

.description-content {
  min-height: 20px;
  display: flex;
  align-items: center;
}

.edit-btn {
  opacity: 0.8;
  transition: opacity 0.2s;
  flex-shrink: 0;
}

.edit-btn:hover {
  opacity: 1 !important;
}

/* .delete-preview {
  border: 1px solid #e0e0e0;
} */

/* Responsive adjustments */
@media (max-width: 600px) {
  .time-section {
    flex-direction: column;
    gap: 8px;
  }

  .arrow-container {
    position: static;
    transform: none;
    align-self: center;
  }

  .location-section {
    flex-direction: column;
    gap: 4px;
    text-align: center;
  }

  .time-labels {
    flex-direction: column;
    gap: 4px;
    text-align: center;
  }

  .description-section {
    flex-direction: column;
    gap: 12px;
    align-items: stretch !important;
  }

  .edit-btn {
    align-self: flex-end;
  }
}

.time-text {
  font-size: 1.25rem; /* or use 20px */
  font-weight: 700;
}

.location-text {
  font-size: 1rem; /* or 16px */
  font-weight: 600;
}
</style>
