<template>
  <v-card class="accommodation-card" elevation="2">
    <div class="card-container d-flex">
      <!-- Left Content Section -->
      <div class="content-section flex-grow-1">
        <v-card-item class="pa-4">
          <!-- Accommodation Name/Title -->
          <v-card-title class="text-h6 font-weight-bold pa-0 mb-2">
            {{ accommodation.accommodationInfo.title }}
          </v-card-title>

          <!-- Custom Name (if different from title) -->
          <div
            v-if="accommodation.name !== accommodation.accommodationInfo.title"
            class="custom-name mb-3"
          >
            <v-chip size="small" color="secondary" variant="outlined" class="text-caption">
              {{ accommodation.name }}
            </v-chip>
          </div>

          <!-- Check-in and Check-out Times -->
          <div class="time-section mb-3">
            <div class="time-labels d-flex justify-space-between mb-1">
              <span class="text-caption text-grey-darken-1">체크인</span>
              <span class="text-caption text-grey-darken-1">체크아웃</span>
            </div>
            <div class="time-values d-flex justify-space-between align-center">
              <span class="text-body-1 font-weight-medium">{{
                formatTime(accommodation.checkInTime)
              }}</span>
              <v-icon color="primary" size="small" class="mx-2">mdi-arrow-right</v-icon>
              <span class="text-body-1 font-weight-medium">{{
                formatTime(accommodation.checkOutTime)
              }}</span>
            </div>
          </div>

          <!-- Contact Information -->
          <div v-if="accommodation.accommodationInfo.tel" class="contact-section mb-2">
            <div class="d-flex align-center">
              <v-icon size="small" color="grey-darken-1" class="mr-2">mdi-phone</v-icon>
              <span class="text-body-2">{{ accommodation.accommodationInfo.tel }}</span>
            </div>
          </div>

          <!-- Homepage -->
          <div v-if="accommodation.accommodationInfo.homepage" class="homepage-section mb-2">
            <div class="d-flex align-center">
              <v-icon size="small" color="grey-darken-1" class="mr-2">mdi-web</v-icon>
              <a
                :href="extractUrl(accommodation.accommodationInfo.homepage)"
                target="_blank"
                class="text-body-2 text-primary text-decoration-none"
              >
                홈페이지 방문
              </a>
            </div>
          </div>

          <!-- Address -->
          <div class="address-section mb-3">
            <div class="d-flex align-start">
              <v-icon size="small" color="grey-darken-1" class="mr-2 mt-1">mdi-map-marker</v-icon>
              <div>
                <div class="text-body-2">{{ accommodation.accommodationInfo.addr1 }}</div>
                <div
                  v-if="accommodation.accommodationInfo.addr2"
                  class="text-caption text-grey-darken-1"
                >
                  {{ accommodation.accommodationInfo.addr2 }}
                </div>
              </div>
            </div>
          </div>

          <!-- Divider -->
          <v-divider class="mb-3"></v-divider>

          <!-- Description/Overview and Delete Button -->
          <div class="description-delete-section">
            <div class="description-content">
              <div v-if="accommodation.accommodationInfo.overview" class="description-section">
                <p class="text-body-2 text-grey-darken-1 mb-0 description-text">
                  {{ accommodation.accommodationInfo.overview }}
                </p>
              </div>
              <div v-else class="description-section">
                <p class="text-body-2 text-grey-lighten-1 mb-0 font-italic">
                  숙소 정보가 제공되지 않았습니다.
                </p>
              </div>
            </div>

            <!-- Delete Button -->
            <div class="delete-button-container">
              <v-btn
                color="error"
                variant="flat"
                size="small"
                class="delete-btn"
                @click="handleDelete"
                :loading="isDeleting"
              >
                <v-icon size="small" class="mr-1">mdi-delete</v-icon>
                삭제
              </v-btn>
            </div>
          </div>
        </v-card-item>
      </div>

      <!-- Right Image Section -->
      <div class="image-section">
        <v-img
          v-if="accommodation.accommodationInfo.firstImage1"
          :src="accommodation.accommodationInfo.firstImage1"
          :alt="accommodation.accommodationInfo.title"
          height="100%"
          width="160"
          cover
          class="accommodation-image"
        >
          <template v-slot:placeholder>
            <div class="d-flex align-center justify-center fill-height">
              <v-progress-circular indeterminate color="primary"></v-progress-circular>
            </div>
          </template>
        </v-img>
        <div v-else class="no-image-placeholder d-flex flex-column align-center justify-center">
          <v-icon color="grey-lighten-1" size="large">mdi-image-off</v-icon>
          <span class="text-caption text-grey-lighten-1 mt-1">이미지 없음</span>
        </div>
      </div>
    </div>

    <!-- Delete Confirmation Dialog -->
    <v-dialog v-model="showDeleteDialog" max-width="450px">
      <v-card>
        <v-card-title class="text-h6">
          <v-icon color="error" class="mr-2">mdi-alert-circle</v-icon>
          숙소 예약 취소
        </v-card-title>
        <v-card-text>
          <p class="mb-3">이 숙소 예약을 취소하시겠습니까?</p>
          <div class="delete-preview pa-3 bg-grey-lighten-4 rounded">
            <div class="d-flex align-center mb-2">
              <v-icon size="small" color="primary" class="mr-2">mdi-bed</v-icon>
              <span class="text-body-1 font-weight-medium">
                {{ accommodation.accommodationInfo.title }}
              </span>
            </div>
            <div v-if="accommodation.name !== accommodation.accommodationInfo.title" class="mb-2">
              <v-chip size="x-small" color="secondary" variant="outlined">
                {{ accommodation.name }}
              </v-chip>
            </div>
            <div class="text-caption text-grey-darken-1 mb-1">
              <strong>체크인:</strong> {{ formatTime(accommodation.checkInTime) }}
            </div>
            <div class="text-caption text-grey-darken-1">
              <strong>체크아웃:</strong> {{ formatTime(accommodation.checkOutTime) }}
            </div>
          </div>
          <p class="text-caption text-error mt-3 mb-0">
            ⚠️ 이 작업은 되돌릴 수 없습니다.
          </p>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="text" @click="showDeleteDialog = false">취소</v-btn>
          <v-btn
            color="error"
            variant="elevated"
            @click="confirmDelete"
            :loading="isDeleting"
          >
            예약 취소
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { useAccommodationStore } from '@/stores/accommodation'
import { apiClient } from '@/utils/apiClient'

// Props
const props = defineProps({
  accommodation: {
    type: Object,
    required: true,
  },
})

// Store and Route
const route = useRoute()
const accommodationStore = useAccommodationStore()

// State
const showDeleteDialog = ref(false)
const isDeleting = ref(false)

// Helper function to format datetime to display format
const formatTime = (dateTimeString) => {
  const date = new Date(dateTimeString)
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${month}/${day} ${hours}:${minutes}`
}

// Helper function to extract URL from HTML anchor tag
const extractUrl = (htmlString) => {
  if (!htmlString) return ''
  const match = htmlString.match(/href="([^"]*)"/)
  return match ? match[1] : htmlString
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

    // Call API to delete accommodation
    const response = await apiClient.delete(`/accommodation/${groupId}`, {
      params: {
        accommodationId: props.accommodation.accommodationId
      }
    })

    if (response.data.success) {
      // Remove from store
      const result = accommodationStore.removeAccommodation(props.accommodation.accommodationId)

      if (result.success) {
        showDeleteDialog.value = false
        console.log('숙소 예약 취소 성공:', response.data.message)
      } else {
        // This shouldn't happen if API succeeded, but handle gracefully
        console.warn('Store에서 삭제 실패:', result.error)
        showDeleteDialog.value = false
        // Force a page refresh or refetch might be needed here
      }
    } else {
      throw new Error(response.data.message || '숙소 예약 취소에 실패했습니다.')
    }
  } catch (err) {
    console.error('숙소 예약 취소 오류:', err)

    let errorMessage = '숙소 예약 취소 중 오류가 발생했습니다.'

    if (err.response) {
      const status = err.response.status
      const message = err.response.data?.message || err.message

      if (status === 400) {
        errorMessage = `잘못된 요청: ${message}`
      } else if (status === 401) {
        errorMessage = '로그인이 필요합니다.'
      } else if (status === 403) {
        errorMessage = '숙소 예약 취소 권한이 없습니다.'
      } else if (status === 404) {
        errorMessage = '해당 숙소 예약을 찾을 수 없습니다.'
      } else {
        errorMessage = `서버 오류 (${status}): ${message}`
      }
    } else if (err.code === 'ECONNABORTED') {
      errorMessage = '요청 시간이 초과되었습니다. 다시 시도해주세요.'
    } else {
      errorMessage = err.message || errorMessage
    }

    // Set error in store for display
    accommodationStore.error = errorMessage
    showDeleteDialog.value = false
  } finally {
    isDeleting.value = false
  }
}
</script>

<style scoped>
.accommodation-card {
  width: 100%;
  border-radius: 12px;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  border: 1px solid #e0e0e0;
  overflow: hidden;
}

.accommodation-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.12) !important;
}

.card-container {
  min-height: 200px;
}

.content-section {
  min-width: 0; /* Allow flex item to shrink */
}

.image-section {
  width: 160px;
  min-width: 160px;
  height: 100%;
}

.accommodation-image {
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
}

.no-image-placeholder {
  width: 160px;
  height: 100%;
  background-color: #f5f5f5;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
}

.time-section {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 12px;
}

.description-delete-section {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  gap: 16px;
}

.description-content {
  flex: 1;
  min-width: 0;
}

.description-text {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.delete-button-container {
  flex-shrink: 0;
}

.delete-btn {
  background-color: rgba(244, 67, 54, 0.1) !important;
  border: 1px solid rgba(244, 67, 54, 0.3);
  color: #d32f2f !important;
  font-weight: 500;
  transition: all 0.2s;
}

.delete-btn:hover {
  background-color: rgba(244, 67, 54, 0.15) !important;
  border-color: rgba(244, 67, 54, 0.5);
  transform: translateY(-1px);
}

.delete-preview {
  border: 1px solid #e0e0e0;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .card-container {
    flex-direction: column;
  }

  .image-section {
    width: 100%;
    min-width: 100%;
    height: 180px;
    order: -1;
  }

  .accommodation-image,
  .no-image-placeholder {
    border-radius: 0;
    border-top-left-radius: 12px;
    border-top-right-radius: 12px;
  }

  .time-values {
    flex-direction: column;
    gap: 8px;
    text-align: center;
  }

  .time-labels {
    flex-direction: column;
    gap: 4px;
    text-align: center;
  }

  .description-delete-section {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .delete-btn {
    width: 100%;
  }
}
</style>
