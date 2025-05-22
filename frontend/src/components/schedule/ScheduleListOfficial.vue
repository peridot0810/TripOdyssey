<template>
  <div class="schedule-list-official">
    <h2 class="text-h6 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-calendar-check</v-icon>
      확정된 일정
    </h2>

    <!-- Day Selection Tabs -->
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
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ScheduleCard from './ScheduleCard.vue'
import { dummyScheduleData } from '@/data/schedule/dummyScheduleData.js'

const schedules = ref([...dummyScheduleData]) // Create a copy for manipulation
const selectedDay = ref(1)
const draggedIndex = ref(null)
const dragOverIndex = ref(null)

// Get all available days from the schedule data
const availableDays = computed(() => {
  const days = [...new Set(schedules.value.map((schedule) => schedule.day))]
  return days.sort((a, b) => a - b)
})

// Filter schedules by selected day and sort by order
const filteredSchedules = computed(() => {
  return schedules.value
    .filter((schedule) => schedule.day === selectedDay.value)
    .sort((a, b) => a.order - b.order)
})

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

  // Update the original schedules array
  const draggedOriginalIndex = schedules.value.findIndex(
    (s) => s.contentId === draggedSchedule.contentId,
  )
  const droppedOriginalIndex = schedules.value.findIndex(
    (s) => s.contentId === droppedOnSchedule.contentId,
  )

  schedules.value[draggedOriginalIndex].order = draggedSchedule.order
  schedules.value[droppedOriginalIndex].order = droppedOnSchedule.order

  dragOverIndex.value = null
}

const onDragEnd = () => {
  draggedIndex.value = null
  dragOverIndex.value = null
}
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

.empty-state {
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

.schedule-card-with-order {
  position: relative;
  margin-bottom: 16px;
}

.order-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 5;
}

.draggable-schedule-card {
  position: relative;
  margin-bottom: 16px;
  cursor: grab;
  transition: all 0.3s ease;
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

.drag-info-hint {
  margin-bottom: 12px;
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
