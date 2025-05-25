<template>
  <div class="accommodation-list">
    <!-- Header -->
    <div class="d-flex justify-space-between align-center mb-4">
      <h2 class="text-h5 font-weight-bold d-flex align-center">
        <v-icon size="large" color="primary" class="mr-2">mdi-bed</v-icon>
        숙소 목록
      </h2>
      <v-btn
        color="secondary"
        variant="outlined"
        @click="refreshAccommodations"
        class="refresh-btn"
        :loading="isRefreshing"
        :disabled="isInitialLoading"
      >
        <v-icon class="mr-2">mdi-refresh</v-icon>
        새로고침
      </v-btn>
    </div>

    <!-- Initial Loading State -->
    <div v-if="isInitialLoading" class="loading-state text-center py-8">
      <v-progress-circular
        indeterminate
        color="primary"
        size="64"
      ></v-progress-circular>
      <p class="text-body-1 text-grey-darken-1 mt-3">숙소 목록을 불러오는 중...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="fetchError" class="error-state text-center py-8">
      <v-icon size="x-large" color="error">mdi-alert-circle</v-icon>
      <p class="text-body-1 text-error mt-3">숙소 목록을 불러오는데 실패했습니다</p>
      <p class="text-caption text-grey mb-4">{{ fetchError }}</p>
      <v-btn
        color="primary"
        variant="outlined"
        @click="fetchAccommodationList"
        :loading="isRetrying"
      >
        <v-icon class="mr-2">mdi-refresh</v-icon>
        다시 시도
      </v-btn>
    </div>

    <!-- Content (Empty State or Accommodation Cards) -->
    <div v-else>
      <!-- Empty State -->
      <div v-if="accommodationStore.isEmpty" class="empty-state text-center py-8">
        <v-icon size="x-large" color="grey">mdi-home-off</v-icon>
        <p class="text-body-1 text-grey-darken-1 mt-3">예약된 숙소가 없습니다</p>
        <p class="text-caption text-grey">추천 숙소에서 선택해보세요!</p>
      </div>

      <!-- Accommodation Cards -->
      <div v-else class="accommodation-cards">
        <div
          v-for="accommodation in accommodationStore.accommodationList"
          :key="accommodation.accommodationId"
          class="accommodation-card-wrapper mb-4"
        >
          <AccommodationCard :accommodation="accommodation" />
        </div>
      </div>
    </div>

    <!-- Store Loading State -->
    <div v-if="accommodationStore.isLoading" class="text-center py-4">
      <v-progress-circular indeterminate color="primary"></v-progress-circular>
      <p class="text-caption text-grey mt-2">처리 중...</p>
    </div>

    <!-- Store Error Alert -->
    <v-alert
      v-if="accommodationStore.error"
      type="error"
      class="mt-4"
      closable
      @click:close="accommodationStore.clearError()"
    >
      {{ accommodationStore.error }}
    </v-alert>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAccommodationStore } from '@/stores/accommodation'
import { apiClient } from '@/utils/apiClient'
import AccommodationCard from './AccommodationCard.vue'

const route = useRoute()
const accommodationStore = useAccommodationStore()

// Component state
const isInitialLoading = ref(false)
const isRefreshing = ref(false)
const isRetrying = ref(false)
const fetchError = ref(null)

// Get group ID from route params
const getGroupId = () => {
  return route.params.groupId
}

// Fetch accommodation list from API
const fetchAccommodationList = async () => {
  const groupId = getGroupId()

  if (!groupId) {
    fetchError.value = '그룹 ID를 찾을 수 없습니다.'
    return
  }

  // Set loading state
  if (isRetrying.value) {
    isRetrying.value = true
  } else {
    isInitialLoading.value = true
  }

  fetchError.value = null
  accommodationStore.clearError()

  try {
    const response = await apiClient.get(`/accommodation/${groupId}/list`)

    if (response.data.success) {
      // Format the data to match our store structure (already matches)
      const formattedData = response.data.data.map(item => ({
        accommodationId: item.accommodationId,
        groupId: item.groupId,
        attractionsNo: item.attractionsNo,
        name: item.name,
        checkInTime: item.checkInTime,
        checkOutTime: item.checkOutTime,
        accommodationInfo: {
          no: item.accommodationInfo.no,
          title: item.accommodationInfo.title,
          contentTypeName: item.accommodationInfo.contentTypeName,
          firstImage1: item.accommodationInfo.firstImage1 || '',
          firstImage2: item.accommodationInfo.firstImage2 || '',
          latitude: item.accommodationInfo.latitude,
          longitude: item.accommodationInfo.longitude,
          tel: item.accommodationInfo.tel || '',
          addr1: item.accommodationInfo.addr1 || '',
          addr2: item.accommodationInfo.addr2 || '',
          homepage: item.accommodationInfo.homepage || null,
          overview: item.accommodationInfo.overview || null
        }
      }))

      // Set data to store
      accommodationStore.setAccommodationList(formattedData)

      console.log('숙소 목록 조회 성공:', response.data.message)
    } else {
      throw new Error(response.data.message || '숙소 목록 조회에 실패했습니다.')
    }
  } catch (error) {
    console.error('숙소 목록 조회 오류:', error)

    if (error.response) {
      // Server responded with error status
      const status = error.response.status
      const message = error.response.data?.message || error.message

      if (status === 401) {
        fetchError.value = '로그인이 필요합니다.'
      } else if (status === 403) {
        fetchError.value = '접근 권한이 없습니다.'
      } else if (status === 404) {
        fetchError.value = '해당 그룹을 찾을 수 없습니다.'
      } else {
        fetchError.value = `서버 오류 (${status}): ${message}`
      }
    } else if (error.code === 'ECONNABORTED') {
      fetchError.value = '요청 시간이 초과되었습니다. 네트워크 연결을 확인해주세요.'
    } else {
      fetchError.value = error.message || '네트워크 오류가 발생했습니다.'
    }
  } finally {
    isInitialLoading.value = false
    isRetrying.value = false
  }
}

// Refresh accommodations
const refreshAccommodations = async () => {
  isRefreshing.value = true
  try {
    await fetchAccommodationList()
  } finally {
    isRefreshing.value = false
  }
}

// Lifecycle
onMounted(() => {
  fetchAccommodationList()
})
</script>

<style scoped>
.accommodation-list {
  padding: 16px;
}

.refresh-btn {
  font-weight: 500;
}

.loading-state,
.error-state,
.empty-state {
  border-radius: 8px;
  background-color: #f5f5f5;
  padding: 32px 16px;
}

.error-state {
  background-color: #ffebee;
  border: 1px solid #ffcdd2;
}

.accommodation-cards {
  max-width: 100%;
}

.accommodation-card-wrapper {
  max-width: 100%;
}

/* Loading animation */
.loading-state .v-progress-circular {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.7; }
  100% { opacity: 1; }
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .d-flex.justify-space-between {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start !important;
  }

  .refresh-btn {
    align-self: stretch;
  }
}
</style>
