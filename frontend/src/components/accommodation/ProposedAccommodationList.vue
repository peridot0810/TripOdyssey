<template>
  <div class="proposed-accommodation-list">
    <!-- Header with Add Button -->
    <div class="d-flex justify-space-between align-center mb-4">
      <h2 class="text-h5 font-weight-bold d-flex align-center">
        <v-icon size="large" color="primary" class="mr-2">mdi-home-search</v-icon>
        추천 숙박 시설
      </h2>
      <v-btn
        color="secondary"
        variant="outlined"
        @click="refreshProposals"
        class="refresh-btn"
        :loading="isRefreshing"
        :disabled="proposalStore.isLoading"
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
      <p class="text-body-1 text-grey-darken-1 mt-3">추천 숙박 시설을 불러오는 중...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="fetchError" class="error-state text-center py-8">
      <v-icon size="x-large" color="error">mdi-alert-circle</v-icon>
      <p class="text-body-1 text-error mt-3">추천 숙박 시설을 불러오는데 실패했습니다</p>
      <p class="text-caption text-grey mb-4">{{ fetchError }}</p>
      <v-btn
        color="primary"
        variant="outlined"
        @click="fetchAccommodationProposals"
        :loading="isRetrying"
      >
        <v-icon class="mr-2">mdi-refresh</v-icon>
        다시 시도
      </v-btn>
    </div>

    <!-- Content (Empty State or Accommodation Cards) -->
    <div v-else>
      <!-- Empty State -->
      <div v-if="accommodationProposals.length === 0" class="empty-state text-center py-8">
        <v-icon size="x-large" color="grey">mdi-home-off</v-icon>
        <p class="text-body-1 text-grey-darken-1 mt-3">추천된 숙박 시설이 없습니다</p>
        <p class="text-caption text-grey">다른 지역의 숙박 시설을 확인해보세요!</p>
      </div>

      <!-- Accommodation Proposal Cards -->
      <div v-else class="accommodation-cards">
        <div
          v-for="accommodation in accommodationProposals"
          :key="accommodation.proposalId"
          class="accommodation-card-wrapper mb-4"
        >
          <div class="proposal-card-container">
            <ProposalCard
              :location="accommodation"
              @toggle-like="handleToggleLike"
              class="proposal-card-item"
            />

            <!-- Select Accommodation Button -->
            <v-btn
              color="success"
              variant="elevated"
              class="select-accommodation-button"
              @click="handleSelectAccommodation(accommodation)"
              :title="'이 숙소 선택하기'"
            >
              <v-icon class="mr-1">mdi-plus</v-icon>
              <span class="select-text">선택</span>
            </v-btn>
          </div>
        </div>
      </div>
    </div>

    <!-- Store Loading State -->
    <div v-if="proposalStore.isLoading" class="text-center py-4">
      <v-progress-circular indeterminate color="primary"></v-progress-circular>
      <p class="text-caption text-grey mt-2">처리 중...</p>
    </div>

    <!-- Store Error Alert -->
    <v-alert
      v-if="proposalStore.error"
      type="error"
      class="mt-4"
      closable
      @click:close="proposalStore.clearError()"
    >
      {{ proposalStore.error }}
    </v-alert>

    <!-- Create Accommodation Modal (will be added later) -->
    <CreateAccommodationModal
      v-model="showCreateModal"
      :selected-location="selectedAccommodation"
      @accommodation-created="handleAccommodationCreated"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useProposalStore } from '@/stores/proposal'
import ProposalCard from '@/components/schedule/ProposalCard.vue'
import CreateAccommodationModal from '@/components/accommodation/CreateAccommodationModal.vue'

const route = useRoute()
const proposalStore = useProposalStore()

// Component state
const isInitialLoading = ref(false)
const isRefreshing = ref(false)
const isRetrying = ref(false)
const fetchError = ref(null)
const selectedAccommodation = ref(null)
const showCreateModal = ref(false)

// Get group ID from route params
const getGroupId = () => {
  return route.params.groupId
}

// Filter proposals to show only accommodation types (contentTypeId = 32)
const accommodationProposals = computed(() => {
  return proposalStore.proposals.filter(proposal => {
    // Check if contentTypeId is 32 (숙박) or contentTypeName contains '숙박'
    return proposal.contentTypeId === 32 ||
           proposal.contentTypeName === '숙박' ||
           proposal.contentTypeName?.includes('숙박')
  })
})

// Fetch accommodation proposals from API
const fetchAccommodationProposals = async () => {
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
  proposalStore.clearError()

  try {
    // Use the existing proposal store's fetch method
    await proposalStore.fetchProposals(groupId)

    console.log('숙박 제안 목록 조회 성공')
  } catch (error) {
    console.error('숙박 제안 목록 조회 오류:', error)

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

// Refresh proposals
const refreshProposals = async () => {
  isRefreshing.value = true
  try {
    await fetchAccommodationProposals()
  } finally {
    isRefreshing.value = false
  }
}

// Event handlers
const handleToggleLike = async (proposalId) => {
  const groupId = getGroupId()
  try {
    await proposalStore.toggleLike(groupId, proposalId)
  } catch (error) {
    console.error('좋아요 토글 실패:', error)
  }
}

const handleSelectAccommodation = (accommodation) => {
  selectedAccommodation.value = accommodation
  showCreateModal.value = true
  console.log('선택된 숙소:', accommodation)
}

const handleAccommodationCreated = (newAccommodation) => {
  showCreateModal.value = false
  console.log('새 숙소가 추가되었습니다:', newAccommodation)

  // Optional: Show success notification
  // You could add a toast notification here if available
}

// Lifecycle
onMounted(() => {
  fetchAccommodationProposals()
})
</script>

<style scoped>
.proposed-accommodation-list {
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

.proposal-card-container {
  display: flex;
  align-items: center;
  gap: 12px;
}

.proposal-card-item {
  flex: 1;
}

.select-accommodation-button {
  width: 64px !important;
  height: 48px !important;
  border-radius: 8px !important;
  min-width: auto !important;
  transition: transform 0.2s, box-shadow 0.2s;
  flex-shrink: 0;
  font-weight: 500;
}

.select-accommodation-button:hover {
  transform: translateY(-1px) scale(1.02);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2) !important;
}

.select-text {
  font-size: 12px;
  font-weight: bold;
  color: white;
  line-height: 1;
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

  .proposal-card-container {
    flex-direction: column;
    gap: 8px;
  }

  .select-accommodation-button {
    width: 100% !important;
    height: 40px !important;
  }
}
</style>
