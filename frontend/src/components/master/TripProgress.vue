<template>
  <div class="trip-progress nintendo-card">
    <h3 class="progress-title">TRIP PROGRESS</h3>

    <!-- Progress Flow -->
    <div class="progress-flow">
      <div
        v-for="(stage, index) in stages"
        :key="index"
        class="stage-item"
        :class="[getStageClass(index), `stage-color-${index}`]"
      >
        <div class="stage-indicator">
          <span v-if="index < currentStage" class="completed">✓</span>
          <span v-else-if="index === currentStage" class="current">●</span>
          <span v-else class="pending">○</span>
        </div>
        <span class="stage-name">{{ stage.name }}</span>
        <div v-if="index < stages.length - 1" class="stage-arrow">→</div>
      </div>
    </div>

    <!-- Progress Info -->
    <div class="progress-info">
      <div class="current-stage-info">
        <span class="stage-label">{{ currentStageInfo.name }} 단계</span>
        <span class="stage-count">({{ currentStage + 1 }}/{{ stages.length }})</span>
      </div>
      <div class="stage-description">
        {{ currentStageInfo.description }}
      </div>
    </div>

    <!-- Control Buttons -->
    <div class="control-buttons">
      <button
        class="nintendo-btn nintendo-btn--red"
        :disabled="currentStage === 0 || isUpdating"
        @click="rollback"
      >
        <span v-if="isUpdating" class="loading-spinner">⟳</span>
        <span v-else>← 롤백</span>
      </button>
      <button
        class="nintendo-btn nintendo-btn--blue"
        :disabled="currentStage === stages.length - 1 || isUpdating"
        @click="proceed"
      >
        <span v-if="isUpdating" class="loading-spinner">⟳</span>
        <span v-else>진행 →</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useGroupStore } from '@/stores/group'
import { apiClient } from '@/utils/apiClient'

const groupStore = useGroupStore()
const route = useRoute()

// Loading state for API calls
const isUpdating = ref(false)

const stages = ref([
  {
    name: '일정조율',
    key: 'scheduleCoordination',
    description: '그룹 멤버들과 여행 일정을 조율하는 단계입니다.',
  },
  {
    name: '역할배정',
    key: 'roleAssignment',
    description: '모든 멤버의 역할이 정해진 후 다음 단계로 진행할 수 있습니다.',
  },
  { name: '여행계획', key: 'tripPlanning', description: '세부 여행 계획을 수립하는 단계입니다.' },
  { name: '여행중', key: 'onTrip', description: '현재 여행을 진행 중인 단계입니다.' },
  { name: '정산', key: 'settlement', description: '여행 비용을 정산하는 단계입니다.' },
  { name: '완료', key: 'finished', description: '모든 여행 과정이 완료된 단계입니다.' },
])

// Calculate current stage based on progress data from Pinia store
const currentStage = computed(() => {
  if (!groupStore.hasGroup) return 0

  const progress = groupStore.myGroup.progress

  // Find the last completed stage
  let lastCompletedStage = -1

  if (progress.scheduleCoordination) lastCompletedStage = 0
  if (progress.roleAssignment) lastCompletedStage = 1
  if (progress.tripPlanning) lastCompletedStage = 2
  if (progress.onTrip) lastCompletedStage = 3
  if (progress.settlement) lastCompletedStage = 4
  if (progress.finished) lastCompletedStage = 5

  // Current stage is one after the last completed stage (unless all are complete)
  return Math.min(lastCompletedStage + 1, stages.value.length - 1)
})

const currentStageInfo = computed(() => stages.value[currentStage.value] || stages.value[0])

const getStageClass = (index) => {
  if (!groupStore.hasGroup) {
    // Default state when no group data
    if (index === 0) return 'current'
    return 'pending'
  }

  const progress = groupStore.myGroup.progress
  const stageKeys = [
    'scheduleCoordination',
    'roleAssignment',
    'tripPlanning',
    'onTrip',
    'settlement',
    'finished',
  ]

  // Check if this stage is completed
  if (progress[stageKeys[index]]) {
    return 'completed'
  }

  // Check if this is the current stage (first non-completed stage)
  if (index === currentStage.value) {
    return 'current'
  }

  return 'pending'
}

// API function to update progress
const updateProgressAPI = async (progressData) => {
  const groupId = route.params.groupId
  if (!groupId) {
    throw new Error('Group ID not found')
  }

  try {
    console.log('Updating progress with data:', progressData)
    const response = await apiClient.put(`/group/${groupId}/update-progress`, progressData)
    console.log('API response:', response.data)
    return response.data
  } catch (error) {
    console.error('API error:', error)
    throw error
  }
}

// Progress control functions with API integration
const proceed = async () => {
  if (!groupStore.hasGroup || isUpdating.value) return

  isUpdating.value = true

  try {
    const progress = { ...groupStore.myGroup.progress }
    const stageKeys = [
      'scheduleCoordination',
      'roleAssignment',
      'tripPlanning',
      'onTrip',
      'settlement',
      'finished',
    ]

    // Mark the current stage as completed
    if (currentStage.value < stageKeys.length) {
      progress[stageKeys[currentStage.value]] = true
    }

    console.log('Proceeding to next stage with progress:', progress)

    // Call API to update progress
    await updateProgressAPI(progress)

    // Update the store only if API call succeeds
    const updatedGroupData = {
      ...groupStore.myGroup,
      progress,
    }
    groupStore.setGroupData(updatedGroupData)

    console.log('Progress updated successfully')
  } catch (error) {
    console.error('Failed to proceed:', error)

    // Show error message to user
    const errorMessage = error.response?.data?.message || '진행 업데이트에 실패했습니다.'
    alert(errorMessage)
  } finally {
    isUpdating.value = false
  }
}

const rollback = async () => {
  if (!groupStore.hasGroup || isUpdating.value) return

  isUpdating.value = true

  try {
    const progress = { ...groupStore.myGroup.progress }
    const stageKeys = [
      'scheduleCoordination',
      'roleAssignment',
      'tripPlanning',
      'onTrip',
      'settlement',
      'finished',
    ]

    // Mark the previous stage as incomplete
    if (currentStage.value > 0) {
      progress[stageKeys[currentStage.value - 1]] = false
    }

    console.log('Rolling back stage with progress:', progress)

    // Call API to update progress
    await updateProgressAPI(progress)

    // Update the store only if API call succeeds
    const updatedGroupData = {
      ...groupStore.myGroup,
      progress,
    }
    groupStore.setGroupData(updatedGroupData)

    console.log('Progress rolled back successfully')
  } catch (error) {
    console.error('Failed to rollback:', error)

    // Show error message to user
    const errorMessage = error.response?.data?.message || '롤백 업데이트에 실패했습니다.'
    alert(errorMessage)
  } finally {
    isUpdating.value = false
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

.trip-progress {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  padding: 1rem;
  border-radius: 20px;
  background: var(--card-bg, #ffffff);
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.1);
  font-family: 'Nunito', sans-serif;
}

.progress-title {
  font-size: 1.1rem;
  font-weight: 700;
  color: var(--text-primary, #2d3748);
  text-align: center;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #e0e0e0;
}

.progress-flow {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.25rem;
  padding: 1rem 0;
  overflow-x: auto;
}

.stage-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.25rem;
  flex: 1;
  min-width: 60px;
  position: relative;
}

.stage-indicator {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: bold;
}

.stage-item.completed .stage-indicator {
  background: var(--stage-color, var(--joy-con-teal, #4ecdc4));
  color: white;
}

.stage-item.current .stage-indicator {
  background: var(--stage-color, var(--joy-con-blue-light, #45b7d1));
  color: white;
  box-shadow: 0 0 0 3px rgba(69, 183, 209, 0.3);
}

.stage-item.pending .stage-indicator {
  background: #e9ecef;
  color: #6c757d;
  border: 2px solid #dee2e6;
}

.stage-name {
  font-size: 0.75rem;
  text-align: center;
  font-weight: 500;
  color: var(--text-secondary, #718096);
}

.stage-item.completed .stage-name {
  color: var(--stage-color, var(--joy-con-teal, #4ecdc4));
}

.stage-item.current .stage-name {
  color: var(--stage-color, var(--joy-con-blue-light, #45b7d1));
  font-weight: 700;
}

.stage-arrow {
  position: absolute;
  right: -0.75rem;
  top: 12px;
  color: #dee2e6;
  font-size: 0.8rem;
  z-index: 1;
}

.progress-info {
  background: var(--bg-light, #edf2f7);
  padding: 0.75rem;
  border-radius: 12px;
  border-left: 6px solid var(--joy-con-blue-light, #45b7d1);
}

.current-stage-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.stage-label {
  font-weight: 600;
  color: #333;
}

.stage-count {
  background: var(--joy-con-blue-light, #45b7d1);
  color: white;
  padding: 0.125rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
}

.stage-description {
  font-size: 0.875rem;
  color: #555;
  line-height: 1.5;
}

.control-buttons {
  display: flex;
  gap: 0.75rem;
  margin-top: 0.5rem;
  justify-content: center;
}

.nintendo-btn {
  flex: 1;
  padding: 0.75rem 1.25rem;
  border: none;
  border-radius: 20px;
  font-weight: 700;
  font-family: 'Nunito', sans-serif;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

/* Red variant (rollback) */
.nintendo-btn--red {
  background: linear-gradient(135deg, #ff6b6b, #ee5a52);
  color: white;
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.4);
}

.nintendo-btn--red:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 107, 107, 0.6);
}

/* Blue variant (proceed) */
.nintendo-btn--blue {
  background: linear-gradient(135deg, #45b7d1, #007bff);
  color: white;
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.4);
}

.nintendo-btn--blue:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 123, 255, 0.6);
}

.nintendo-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.loading-spinner {
  display: inline-block;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* Stage-specific colors */
.stage-color-0 {
  --stage-color: #f9ca24;
}
.stage-color-1 {
  --stage-color: #4ecdc4;
}
.stage-color-2 {
  --stage-color: #45b7d1;
}
.stage-color-3 {
  --stage-color: #6c5ce7;
}
.stage-color-4 {
  --stage-color: #ff6b6b;
}
.stage-color-5 {
  --stage-color: #2ed573;
}

.stage-item.current .stage-indicator {
  animation: pulse-glow-colored 1.5s ease-in-out infinite;
}

@keyframes pulse-glow-colored {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 var(--stage-color, rgba(0, 0, 0, 0.1));
  }
  70% {
    transform: scale(1.08);
    box-shadow: 0 0 0 10px transparent;
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 transparent;
  }
}
</style>
