<template>
  <v-card class="accommodation-card" elevation="2">
    <div class="card-container d-flex">
      <!-- Left Content Section -->
      <div class="content-section">
        <v-card-item class="pa-4">
          <!-- Accommodation Name/Title -->
          <v-card-title class="text-h6 font-weight-bold pa-0 mb-2 accommodation-title">
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

          <!-- Check-in and Check-out Times (Vertical Layout) -->
          <div class="time-section mb-3">
            <div class="time-item mb-2">
              <span class="text-caption text-grey-darken-1 time-label">체크인</span>
              <span class="text-body-1 font-weight-medium time-value">{{
                formatTime(accommodation.checkInTime)
              }}</span>
            </div>
            <div class="time-divider">
              <SvgIcon :path="arrowIcon" type="mdi" size="16" color="primary" class="rotate-90" />
            </div>
            <div class="time-item">
              <span class="text-caption text-grey-darken-1 time-label">체크아웃</span>
              <span class="text-body-1 font-weight-medium time-value">{{
                formatTime(accommodation.checkOutTime)
              }}</span>
            </div>
          </div>

          <!-- Contact Information -->
          <div v-if="accommodation.accommodationInfo.tel" class="contact-section mb-2">
            <div class="d-flex align-center">
              <SvgIcon :path="phoneIcon" type="mdi" size="16" color="grey-darken-1" class="mr-2 contact-icon" />
              <span class="text-body-2 contact-text">{{ accommodation.accommodationInfo.tel }}</span>
            </div>
          </div>

          <!-- Homepage -->
          <div v-if="accommodation.accommodationInfo.homepage" class="homepage-section mb-2">
            <div class="d-flex align-center">
              <SvgIcon :path="webIcon" type="mdi" size="16" color="grey-darken-1" class="mr-2 contact-icon" />
              <a
                :href="extractUrl(accommodation.accommodationInfo.homepage)"
                target="_blank"
                class="text-body-2 text-primary text-decoration-none contact-text"
              >
                홈페이지 방문
              </a>
            </div>
          </div>

          <!-- Address -->
          <div class="address-section mb-3">
            <div class="d-flex align-start">
              <SvgIcon :path="mapMarkerIcon" type="mdi" size="16" color="grey-darken-1" class="mr-2 mt-1 contact-icon" />
              <div class="address-text">
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
                variant="flat"
                size="medium"
                class="delete-btn"
                @click="handleDelete"
                :loading="isDeleting"
              >
                <SvgIcon :path="deleteIcon" type="mdi" size="20" class="mr-1" />
              </v-btn>
            </div>
          </div>
        </v-card-item>
      </div>

      <!-- Right Image Section (Background Image) -->
      <div class="image-section">
        <v-img
          v-if="accommodation.accommodationInfo.firstImage1"
          :src="accommodation.accommodationInfo.firstImage1"
          :alt="accommodation.accommodationInfo.title"
          class="accommodation-image fill-height"
          cover
        >
          <template v-slot:placeholder>
            <div class="d-flex align-center justify-center fill-height">
              <v-progress-circular indeterminate color="primary" />
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
          <SvgIcon :path="alertIcon" type="mdi" size="20" color="error" class="mr-2" />
          숙소 예약 취소
        </v-card-title>
        <v-card-text>
          <p class="mb-3">이 숙소 예약을 취소하시겠습니까?</p>
          <div class="delete-preview pa-3 bg-grey-lighten-4 rounded">
            <div class="d-flex align-center mb-2">
              <SvgIcon :path="bedIcon" type="mdi" size="16" color="primary" class="mr-2" />
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
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiPhone,
  mdiWeb,
  mdiMapMarker,
  mdiArrowRight,
  mdiDelete,
  mdiAlertCircle,
  mdiBed
} from '@mdi/js'

const phoneIcon = mdiPhone
const webIcon = mdiWeb
const mapMarkerIcon = mdiMapMarker
const arrowIcon = mdiArrowRight
const deleteIcon = mdiDelete
const alertIcon = mdiAlertCircle
const bedIcon = mdiBed

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
  transition: transform 0.2s, box-shadow 0.2s;
  border: 1px solid #e0e0e0;
  overflow: hidden;
  position: relative;
}

.accommodation-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.12) !important;
}

.card-container {
  height: 400px;
  position: relative;
  display: flex;
  align-items: stretch;
}

.content-section {
  width: 70%;
  min-width: 0;
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(2px);
  z-index: 2;
  transition: width 0.3s ease;
  overflow: hidden;
}

.image-section {
  position: absolute;
  top: 0;
  right: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.accommodation-image {
  height: 100% !important;
  width: 100% !important;
  object-fit: cover;
}

.no-image-placeholder {
  width: 100%;
  height: 100%;
  background-color: #f5f5f5;
}

/* Hover Effect */
.accommodation-card:hover .content-section {
  width: 35%;
}

.accommodation-card:hover .accommodation-title {
  font-size: 1rem !important;
  line-height: 1.2;
}

.accommodation-card:hover .time-section {
  padding: 8px;
}

.accommodation-card:hover .contact-icon {
  display: none;
}

.accommodation-card:hover .contact-text {
  font-size: 0.75rem !important;
}

.accommodation-card:hover .address-text {
  font-size: 0.75rem !important;
}

.accommodation-card:hover .description-text {
  -webkit-line-clamp: 2;
  font-size: 0.75rem !important;
}

/* Time Section - Vertical Layout */
.time-section {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 12px;
  transition: padding 0.3s ease;
}

.time-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.time-label {
  display: block;
  margin-bottom: 4px;
}

.time-value {
  display: block;
}

.time-divider {
  display: flex;
  justify-content: center;
  margin: 8px 0;
}

.rotate-90 {
  transform: rotate(90deg);
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
  transition: font-size 0.3s ease;
}

.delete-button-container {
  flex-shrink: 0;
}

.delete-btn {
  color: #d32f2f !important;
  font-weight: 500;
  transition: all 0.2s;
}

.delete-btn:hover {
  transform: translateY(-1px);
}

.delete-preview {
  border: 1px solid #e0e0e0;
}

.contact-text,
.address-text {
  transition: font-size 0.3s ease;
}

.contact-icon {
  transition: opacity 0.3s ease;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .accommodation-card:hover .content-section {
    width: 50%;
  }

  .card-container {
    height: 180px;
  }

  .time-section {
    padding: 8px;
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
