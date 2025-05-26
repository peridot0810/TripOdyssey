<template>
  <div class="transportation-list">
    <!-- Header with Add Button -->
    <div class="d-flex justify-space-between align-center mb-4">
      <h2 class="text-h5 font-weight-bold d-flex align-center">
        <v-icon size="large" color="primary" class="mr-2">mdi-car-multiple</v-icon>
        교통편 목록
      </h2>
      <v-btn
        v-if="isLogisticsPage"
        color="primary"
        variant="elevated"
        @click="openCreateModal"
        class="add-btn"
        :disabled="isInitialLoading"
      >
        <v-icon class="mr-2">mdi-plus</v-icon>
        교통편 추가
      </v-btn>
    </div>

    <!-- Initial Loading State -->
    <div v-if="isInitialLoading" class="loading-state text-center py-8">
      <v-progress-circular
        indeterminate
        color="primary"
        size="64"
      ></v-progress-circular>
      <p class="text-body-1 text-grey-darken-1 mt-3">교통편 목록을 불러오는 중...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="fetchError" class="error-state text-center py-8">
      <v-icon size="x-large" color="error">mdi-alert-circle</v-icon>
      <p class="text-body-1 text-error mt-3">교통편 목록을 불러오는데 실패했습니다</p>
      <p class="text-caption text-grey mb-4">{{ fetchError }}</p>
      <v-btn
        color="primary"
        variant="outlined"
        @click="fetchTransportationList"
        :loading="isRetrying"
      >
        <v-icon class="mr-2">mdi-refresh</v-icon>
        다시 시도
      </v-btn>
    </div>

    <!-- Content (Empty State or Transportation Cards) -->
    <div v-else>
      <!-- Empty State -->
      <div v-if="transportationStore.isEmpty" class="empty-state text-center py-8">
        <v-icon size="x-large" color="grey">mdi-car-off</v-icon>
        <p class="text-body-1 text-grey-darken-1 mt-3">등록된 교통편이 없습니다</p>
        <p class="text-caption text-grey">교통편을 추가해보세요!</p>
      </div>

      <!-- Transportation Cards -->
      <div v-else class="transportation-cards">
        <div
          v-for="transportation in transportationStore.transportationList"
          :key="transportation.transportationId"
          class="transportation-card-wrapper mb-4"
        >
          <TransportationCard :transportation="transportation" />
        </div>
      </div>
    </div>

    <!-- Store Loading State -->
    <div v-if="transportationStore.isLoading" class="text-center py-4">
      <v-progress-circular indeterminate color="primary"></v-progress-circular>
      <p class="text-caption text-grey mt-2">처리 중...</p>
    </div>

    <!-- Store Error Alert -->
    <v-alert
      v-if="transportationStore.error"
      type="error"
      class="mt-4"
      closable
      @click:close="transportationStore.clearError()"
    >
      {{ transportationStore.error }}
    </v-alert>

    <!-- Create Transportation Modal -->
    <CreateTransportationModal
      v-model="showCreateModal"
      @transportation-created="handleTransportationCreated"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useTransportationStore } from '@/stores/transportation'
import { apiClient } from '@/utils/apiClient'
import TransportationCard from '@/components/transportation/TransportationCard.vue'
import CreateTransportationModal from '@/components/transportation/CreateTransportationModal.vue'

const route = useRoute()
const transportationStore = useTransportationStore()

// Component state
const showCreateModal = ref(false)
const isInitialLoading = ref(false)
const isRetrying = ref(false)
const fetchError = ref(null)

// Check if current route is logistics page
const isLogisticsPage = computed(() => {
  return route.path.includes('/logistics')
})

// Get group ID from route params
const getGroupId = () => {
  return route.params.groupId
}

// Fetch transportation list from API
const fetchTransportationList = async () => {
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
  transportationStore.clearError()

  try {
    const response = await apiClient.get(`/transportation/${groupId}/list`)

    if (response.data.success) {
      // Format the data to match our store structure
      const formattedData = response.data.data.map(item => ({
        transportationId: item.transportationId.toString(),
        typeId: item.typeId.toString(),
        groupId: item.groupId,
        departure: formatDateTime(item.departure),
        arrival: formatDateTime(item.arrival),
        from: item.from,
        to: item.to,
        description: item.description || ''
      }))

      // Set data to store
      transportationStore.setTransportationList(formattedData)

      console.log('교통편 목록 조회 성공:', response.data.message)
    } else {
      throw new Error(response.data.message || '교통편 목록 조회에 실패했습니다.')
    }
  } catch (error) {
    console.error('교통편 목록 조회 오류:', error)

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

// Helper function to format datetime from server response
const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return ''

  try {
    // Assuming server returns "2025-06-01 09:30:00" format
    // Extract time part (HH:MM)
    const timePart = dateTimeString.split(' ')[1]
    if (timePart) {
      return timePart.substring(0, 5) // "09:30"
    }
    return dateTimeString
  } catch (error) {
    console.warn('날짜 형식 변환 오류:', error)
    return dateTimeString
  }
}

// Event handlers
const openCreateModal = () => {
  showCreateModal.value = true
}

const handleTransportationCreated = (newTransportation) => {
  showCreateModal.value = false
  console.log('새 교통편이 추가되었습니다:', newTransportation)
  // Note: For now, the new transportation is added to store locally
  // Later you might want to refetch the list or implement API create
}

// Lifecycle
onMounted(() => {
  fetchTransportationList()
})
</script>

<style scoped>
.transportation-list {
  padding: 16px;
}

.add-btn {
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

.transportation-cards {
  max-width: 100%;
}

.transportation-card-wrapper {
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

  .add-btn {
    align-self: stretch;
  }
}
</style>
