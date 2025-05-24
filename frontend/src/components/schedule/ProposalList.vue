<template>
  <div class="proposal-list">
    <h2 class="text-h5 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-map-marker-multiple</v-icon>
      추천 관광지 목록
    </h2>

    <!-- Error Alert -->
    <v-alert
      v-if="proposalStore.error"
      type="error"
      class="mb-4"
      closable
      @click:close="proposalStore.clearError()"
    >
      {{ proposalStore.error }}
    </v-alert>

    <!-- Loading State -->
    <div v-if="proposalStore.isLoading" class="loading-state text-center py-8">
      <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
      <p class="text-body-1 mt-3">추천 장소를 불러오는 중...</p>
    </div>

    <!-- Empty State -->
    <div v-else-if="proposalStore.proposals.length === 0" class="empty-state text-center py-8">
      <v-icon size="x-large" color="grey">mdi-map-search</v-icon>
      <p class="text-body-1 text-grey-darken-1 mt-3">추천된 장소가 없습니다</p>
    </div>

    <!-- Proposal Cards -->
    <div v-else class="location-cards">
      <div
        v-for="location in proposalStore.proposals"
        :key="location.proposalId"
        class="location-card-wrapper mb-4"
      >
        <div class="proposal-card-container">
          <ProposalCard
            :location="location"
            @toggle-like="handleToggleLike"
            class="proposal-card-item"
          />

          <!-- Add to Schedule Button (only on schedule page) -->
          <v-btn
            v-if="isSchedulePage"
            icon
            size="small"
            color="success"
            variant="elevated"
            class="add-schedule-button"
            @click="handleAddToSchedule(location)"
            :title="'일정 후보에 추가'"
          >
            <span class="add-text">추가</span>
          </v-btn>
        </div>
      </div>
    </div>

    <!-- Create Schedule Modal -->
    <CreateSchedule
      v-if="selectedLocationForSchedule && selectedLocationForSchedule.proposalId"
      v-model="showCreateScheduleDialog"
      :selected-location="selectedLocationForSchedule"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import ProposalCard from '@/components/schedule/ProposalCard.vue'
import CreateSchedule from '@/components/schedule/CreateSchedule.vue'
import { useProposalStore } from '@/stores/proposal'
import { useScheduleStore } from '@/stores/schedule'

const route = useRoute()
const proposalStore = useProposalStore()
const scheduleStore = useScheduleStore()

// Get groupId from route params
const groupId = route.params.groupId

// Check if current page is schedule management page
const isSchedulePage = computed(() => {
  return route.path.endsWith('/schedule')
})

// Create Schedule Modal state
const showCreateScheduleDialog = ref(false)
const selectedLocationForSchedule = ref(null)

const handleToggleLike = async (proposalId) => {
  try {
    await proposalStore.toggleLike(groupId, proposalId)
  } catch (error) {
    // Error is already handled in the store
    console.error('Failed to toggle like:', error)
  }
}

const handleAddToSchedule = (location) => {
  selectedLocationForSchedule.value = location
  showCreateScheduleDialog.value = true
}

// Fetch proposals when component mounts
onMounted(() => {
  if (groupId) {
    proposalStore.fetchProposals(groupId)
    scheduleStore.fetchSchedules(groupId)
  }
})
</script>

<style scoped>
.proposal-list {
  padding: 16px;
}

.empty-state,
.loading-state {
  border-radius: 8px;
  background-color: #f5f5f5;
}

.location-cards {
  max-width: 100%;
}

.location-card-wrapper {
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

.add-schedule-button {
  width: 48px !important;
  height: 48px !important;
  border-radius: 50% !important;
  min-width: auto !important;
  transition: transform 0.2s, box-shadow 0.2s;
  flex-shrink: 0;
}

.add-schedule-button:hover {
  transform: translateY(-1px) scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2) !important;
}

.add-text {
  font-size: 11px;
  font-weight: bold;
  color: white;
  line-height: 1;
}
</style>
