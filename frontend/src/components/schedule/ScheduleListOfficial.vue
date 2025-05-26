<template>
  <div class="schedule-list-official">
    <h2 class="text-h6 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-calendar-check</v-icon>
      확정된 일정
    </h2>

    <!-- Error Alert -->
    <v-alert
      v-if="scheduleStore.error || groupStore.error"
      type="error"
      class="mb-4"
      closable
      @click:close="clearErrors"
    >
      {{ scheduleStore.error || groupStore.error }}
    </v-alert>

    <!-- Loading State -->
    <div
      v-if="scheduleStore.isLoading || groupStore.isLoading"
      class="loading-state text-center py-8"
    >
      <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
      <p class="text-body-1 mt-3">일정을 불러오는 중...</p>
    </div>

    <!-- Day Selection Tabs (always show based on trip duration) -->
    <div v-else-if="availableDays.length > 0">
      <div class="day-selector mb-4">
        <v-chip-group v-model="selectedDay" active-class="primary" mandatory class="day-chips">
          <v-chip
            v-for="day in availableDays"
            :key="day"
            :value="day"
            variant="outlined"
            size="large"
            class="day-chip"
          >
            <v-icon start>mdi-calendar-today</v-icon>
            {{ day }}일차
          </v-chip>
        </v-chip-group>
      </div>

      <!-- Schedule Cards for Selected Day -->
      <div v-if="filteredSchedules.length === 0" class="empty-state text-center py-8">
        <v-icon size="x-large" color="grey">mdi-calendar-blank</v-icon>
        <p class="text-body-1 text-grey-darken-1 mt-3">{{ selectedDay }}일차 일정이 없습니다</p>
        <p class="text-caption text-grey mt-2">일정 후보에서 일정을 추가해보세요</p>
      </div>

      <div v-else class="schedule-cards">
        <div class="day-header mb-3">
          <v-divider></v-divider>
          <v-chip color="primary" variant="elevated" class="day-title-chip">
            <v-icon start>mdi-calendar-star</v-icon>
            {{ selectedDay }}일차 일정
          </v-chip>
          <v-divider></v-divider>
        </div>

        <div class="drag-info-hint mb-3">
          <v-alert type="info" variant="tonal" density="compact" class="text-caption">
            <v-icon start>mdi-drag</v-icon>
            카드를 드래그하여 순서를 변경할 수 있습니다
          </v-alert>
        </div>

        <div
          v-for="(schedule, index) in filteredSchedules"
          :key="schedule.contentId"
          class="schedule-card-container"
        >
          <div
            :draggable="true"
            @dragstart="onDragStart($event, index)"
            @dragover="onDragOver($event)"
            @drop="onDrop($event, index)"
            @dragend="onDragEnd"
            class="draggable-schedule-card"
            :class="{ dragging: draggedIndex === index, 'drag-over': dragOverIndex === index }"
          >
            <ScheduleCard :schedule="schedule" class="schedule-card-with-order">
              <template #order>
                <div class="order-badge">
                  <v-chip size="small" color="secondary" variant="elevated">
                    {{ index + 1 }}
                    <v-icon end size="small">mdi-drag-vertical</v-icon>
                  </v-chip>
                </div>
              </template>
            </ScheduleCard>
          </div>

          <!-- Move Back to Edit Button -->
          <v-btn
            icon
            size="small"
            color="primary"
            variant="elevated"
            class="move-back-button"
            @click="handleMoveBackToEdit(schedule)"
            :title="'일정 후보로 이동'"
          >
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>
        </div>
      </div>

      <!-- Save Changes Button -->
      <div class="save-section mt-6">
        <v-divider class="mb-4"></v-divider>
        <div class="d-flex justify-center">
          <v-btn
            color="primary"
            size="large"
            variant="elevated"
            :loading="isSaving"
            :disabled="!hasChangesToSave"
            @click="saveChangesToServer"
            class="save-button"
          >
            <v-icon start>mdi-content-save</v-icon>
            변경사항 저장 ({{ totalChanges }}개)
          </v-btn>
        </div>

        <!-- Changes Summary -->
        <div v-if="hasChangesToSave" class="changes-summary mt-3 text-center">
          <v-chip
            v-if="scheduleStore.newOfficialSchedules.length > 0"
            size="small"
            color="success"
            variant="flat"
            class="ma-1"
          >
            신규 확정: {{ scheduleStore.newOfficialSchedules.length }}개
          </v-chip>
          <v-chip
            v-if="scheduleStore.modifiedOfficialSchedules.length > 0"
            size="small"
            color="warning"
            variant="flat"
            class="ma-1"
          >
            순서 변경: {{ scheduleStore.modifiedOfficialSchedules.length }}개
          </v-chip>
          <v-chip
            v-if="scheduleStore.removedOfficialSchedules.length > 0"
            size="small"
            color="error"
            variant="flat"
            class="ma-1"
          >
            삭제: {{ scheduleStore.removedOfficialSchedules.length }}개
          </v-chip>
        </div>
      </div>
    </div>

    <!-- No Group Info State -->
    <div v-else class="empty-state text-center py-8">
      <v-icon size="x-large" color="grey">mdi-alert-circle</v-icon>
      <p class="text-body-1 text-grey-darken-1 mt-3">그룹 정보를 불러올 수 없습니다</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import ScheduleCard from './ScheduleCard.vue'
import { useScheduleStore } from '@/stores/schedule'
import { useGroupStore } from '@/stores/group'
import { apiClient } from '@/utils/apiClient'

const route = useRoute()
const scheduleStore = useScheduleStore()
const groupStore = useGroupStore()

// Get groupId from route params
const groupId = route.params.groupId

const draggedIndex = ref(null)
const dragOverIndex = ref(null)
const isSaving = ref(false)

// Use selectedDay from store
const selectedDay = computed({
  get: () => scheduleStore.selectedDay,
  set: (value) => scheduleStore.setSelectedDay(value),
})

// Get only official schedules
const officialSchedules = computed(() => {
  return scheduleStore.getOfficialSchedules()
})

// Calculate available days based on group start and end dates
const availableDays = computed(() => {
  if (!groupStore.myGroup.startDate || !groupStore.myGroup.endDate) {
    return [1]
  }

  const startDate = new Date(groupStore.myGroup.startDate)
  const endDate = new Date(groupStore.myGroup.endDate)
  const timeDiff = endDate.getTime() - startDate.getTime()
  const daysDiff = Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1

  return Array.from({ length: Math.max(1, daysDiff) }, (_, i) => i + 1)
})

// Filter schedules by selected day and sort by order
const filteredSchedules = computed(() => {
  return officialSchedules.value
    .filter((schedule) => schedule.day === selectedDay.value)
    .sort((a, b) => (a.order || 0) - (b.order || 0))
})

const handleMoveBackToEdit = (schedule) => {
  scheduleStore.moveScheduleBackToEdit(schedule.contentId)
}

// Drag and drop handlers
const onDragStart = (event, index) => {
  draggedIndex.value = index
  event.dataTransfer.effectAllowed = 'move'
  event.dataTransfer.setData('text/html', event.target)
}

const onDragOver = (event) => {
  event.preventDefault()
  event.dataTransfer.dropEffect = 'move'
}

const onDrop = (event, dropIndex) => {
  event.preventDefault()

  if (draggedIndex.value === null || draggedIndex.value === dropIndex) {
    return
  }

  // Get the schedules being reordered
  const draggedSchedule = filteredSchedules.value[draggedIndex.value]
  const droppedOnSchedule = filteredSchedules.value[dropIndex]

  // Swap the order values
  const tempOrder = draggedSchedule.order
  draggedSchedule.order = droppedOnSchedule.order
  droppedOnSchedule.order = tempOrder

  // Update the original schedules array in store
  const draggedOriginalIndex = scheduleStore.schedules.findIndex(
    (s) => s.contentId === draggedSchedule.contentId,
  )
  const droppedOriginalIndex = scheduleStore.schedules.findIndex(
    (s) => s.contentId === droppedOnSchedule.contentId,
  )

  if (draggedOriginalIndex !== -1) {
    scheduleStore.schedules[draggedOriginalIndex].order = draggedSchedule.order
    scheduleStore.trackOrderModification(
      draggedSchedule.contentId,
      draggedSchedule.day,
      draggedSchedule.order,
    )
  }
  if (droppedOriginalIndex !== -1) {
    scheduleStore.schedules[droppedOriginalIndex].order = droppedOnSchedule.order
    scheduleStore.trackOrderModification(
      droppedOnSchedule.contentId,
      droppedOnSchedule.day,
      droppedOnSchedule.order,
    )
  }

  dragOverIndex.value = null
}

// Check if there are any changes to save
const hasChangesToSave = computed(() => {
  return (
    scheduleStore.newOfficialSchedules.length > 0 ||
    scheduleStore.modifiedOfficialSchedules.length > 0 ||
    scheduleStore.removedOfficialSchedules.length > 0
  )
})

// Calculate total number of changes
const totalChanges = computed(() => {
  return (
    scheduleStore.newOfficialSchedules.length +
    scheduleStore.modifiedOfficialSchedules.length +
    scheduleStore.removedOfficialSchedules.length
  )
})

const saveChangesToServer = async () => {
  if (!hasChangesToSave.value) return

  isSaving.value = true

  try {
    const requestBody = {}

    if (scheduleStore.newOfficialSchedules.length > 0) {
      requestBody.newOfficialSchedules = scheduleStore.newOfficialSchedules
    }
    if (scheduleStore.modifiedOfficialSchedules.length > 0) {
      requestBody.modifiedOfficialSchedules = scheduleStore.modifiedOfficialSchedules
    }
    if (scheduleStore.removedOfficialSchedules.length > 0) {
      requestBody.removedOfficialSchedules = scheduleStore.removedOfficialSchedules
    }

    const response = await apiClient.post(`/schedule/${groupId}/update`, requestBody)

    if (response.data.success) {
      scheduleStore.clearTracking()
    } else {
      throw new Error(response.data.message || 'Failed to save changes')
    }
  } catch (error) {
    scheduleStore.error = error.response?.data?.message || error.message || 'Failed to save changes'
  } finally {
    isSaving.value = false
  }
}

const onDragEnd = () => {
  draggedIndex.value = null
  dragOverIndex.value = null
}

// Clear errors from both stores
const clearErrors = () => {
  scheduleStore.clearError()
  groupStore.clearError()
}

onMounted(async () => {
  if (groupId) {
    if (!groupStore.hasGroup || groupStore.myGroup.groupId !== parseInt(groupId)) {
      await groupStore.getGroupInfo(groupId)
    }
    scheduleStore.fetchSchedules(groupId)
  }
})
</script>

<style scoped>
.schedule-list-official {
  padding: 16px;
}

.day-selector {
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
  padding: 8px 0;
  border-radius: 8px;
}

.day-chips {
  justify-content: center;
}

.day-chip {
  margin: 0 4px;
  transition: all 0.3s ease;
}

.day-chip:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.empty-state,
.loading-state {
  border-radius: 8px;
  background-color: #f5f5f5;
  margin-top: 20px;
}

.schedule-cards {
  max-width: 100%;
}

.day-header {
  display: flex;
  align-items: center;
  margin: 20px 0;
  gap: 16px;
}

.day-title-chip {
  white-space: nowrap;
  font-weight: bold;
}

/* NEW: Schedule card container with button layout */
.schedule-card-container {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.schedule-card-with-order {
  position: relative;
  flex: 1;
}

.order-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 5;
}

.draggable-schedule-card {
  position: relative;
  cursor: grab;
  transition: all 0.3s ease;
  flex: 1;
}

.draggable-schedule-card:active {
  cursor: grabbing;
}

.draggable-schedule-card.dragging {
  opacity: 0.6;
  transform: rotate(2deg) scale(1.02);
  z-index: 1000;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.draggable-schedule-card.drag-over {
  transform: translateY(-4px);
  box-shadow: 0 6px 12px rgba(25, 118, 210, 0.3);
  border: 2px dashed #1976d2;
  border-radius: 8px;
}

/* NEW: Move back to edit button styling */
.move-back-button {
  width: 48px !important;
  height: 48px !important;
  border-radius: 50% !important;
  min-width: auto !important;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  flex-shrink: 0;
}

.move-back-button:hover {
  transform: translateY(-1px) scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2) !important;
}

.drag-info-hint {
  margin-bottom: 12px;
}

.save-section {
  border-top: 1px solid #e0e0e0;
  background-color: #fafafa;
  margin: 0 -20px -20px -20px;
  padding: 20px;
}

.save-button {
  min-width: 200px;
  font-weight: bold;
}

.changes-summary {
  max-width: 600px;
  margin: 0 auto;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .day-chips {
    justify-content: flex-start;
    overflow-x: auto;
    padding-bottom: 8px;
  }

  .day-chip {
    flex-shrink: 0;
  }

  .day-header {
    gap: 8px;
  }

  .schedule-card-container {
    gap: 8px;
  }

  .move-back-button {
    width: 40px !important;
    height: 40px !important;
  }
}

/* Animation for schedule cards */
.schedule-cards {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
