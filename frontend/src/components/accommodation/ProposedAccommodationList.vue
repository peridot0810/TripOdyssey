<template>
  <div class="proposed-accommodation-list">
    <!-- Header with Add Button -->
    <div class="d-flex justify-space-between align-center mb-4">
      <h2 class="text-h5 font-weight-bold d-flex align-center">
        <SvgIcon type="mdi" :path="homeSearchIcon" size="24" class="mr-2" />
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
        <SvgIcon type="mdi" :path="refreshIcon" size="20" class="mr-2" />
        새로고침
      </v-btn>
    </div>

    <!-- Error Alert -->
    <v-alert
      v-if="proposalStore.error || fetchError"
      type="error"
      class="mb-4"
      closable
      @click:close="clearErrors"
    >
      {{ proposalStore.error || fetchError }}
    </v-alert>

    <!-- Initial Loading State -->
    <div v-if="isInitialLoading" class="loading-state text-center py-8">
      <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
      <p class="text-body-1 mt-3">추천 숙박 시설을 불러오는 중...</p>
    </div>

    <!-- Empty State -->
    <div v-else-if="accommodationProposals.length === 0" class="empty-state text-center py-8">
      <SvgIcon type="mdi" :path="homeOffIcon" size="28" color="grey" />
      <p class="text-body-1 text-grey-darken-1 mt-3">추천된 숙박 시설이 없습니다</p>
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
            icon
            size="small"
            variant="flat"
            color="success"
            class="select-accommodation-button"
            @click="handleSelectAccommodation(accommodation)"
            :title="'이 숙소 선택하기'"
          >
            <SvgIcon type="mdi" :path="plusIcon" size="20" color="white" />
          </v-btn>
        </div>
      </div>
    </div>

    <!-- Create Accommodation Modal -->
    <CreateAccommodationModal
      ref="createAccommodationModal"
      :selected-location="selectedAccommodation || {}"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useProposalStore } from '@/stores/proposal'
import ProposalCard from '@/components/schedule/ProposalCard.vue'
import CreateAccommodationModal from '@/components/accommodation/CreateAccommodationModal.vue'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiHomeSearch, mdiRefresh, mdiHomeOff, mdiPlus } from '@mdi/js'

const homeSearchIcon = mdiHomeSearch
const refreshIcon = mdiRefresh
const homeOffIcon = mdiHomeOff
const plusIcon = mdiPlus

const route = useRoute()
const proposalStore = useProposalStore()

// Component state
const isInitialLoading = ref(false)
const isRefreshing = ref(false)
const fetchError = ref(null)
const selectedAccommodation = ref(null)
const createAccommodationModal = ref(null)

// Get group ID from route params
const groupId = route.params.groupId

// Filter proposals to show only accommodation types (contentTypeId = 32)
const accommodationProposals = computed(() => {
  return proposalStore.proposals.filter(proposal => {
    // Check if contentTypeId is 32 (숙박) or contentTypeName contains '숙박'
    return proposal.contentTypeId === 32 ||
           proposal.contentTypeName === '숙박' ||
           proposal.contentTypeName?.includes('숙박')
  })
})

// Clear all errors
const clearErrors = () => {
  fetchError.value = null
  proposalStore.clearError()
}

// Fetch accommodation proposals from API
const fetchAccommodationProposals = async () => {
  if (!groupId) {
    fetchError.value = '그룹 ID를 찾을 수 없습니다.'
    return
  }

  isInitialLoading.value = true
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
  try {
    await proposalStore.toggleLike(groupId, proposalId)
  } catch (error) {
    console.error('좋아요 토글 실패:', error)
  }
}

const handleSelectAccommodation = (accommodation) => {
  selectedAccommodation.value = accommodation
  // Use the exposed method to open the dialog
  createAccommodationModal.value?.openDialog()
  console.log('선택된 숙소:', accommodation)
}

// Lifecycle
onMounted(() => {
  if (groupId) {
    fetchAccommodationProposals()
  }
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
.empty-state {
  border-radius: 8px;
  background-color: #f5f5f5;
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
  width: 48px !important;
  height: 48px !important;
  border-radius: 50% !important;
  min-width: auto !important;
  transition: transform 0.2s, box-shadow 0.2s;
  flex-shrink: 0;
  background-color: #213bd0 !important;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.select-accommodation-button:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.25);
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
    gap: 8px;
  }

  .select-accommodation-button {
    width: 40px !important;
    height: 40px !important;
  }
}
</style>
