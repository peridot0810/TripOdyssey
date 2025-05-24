<template>
  <div class="schedule-edit-list">
    <h2 class="text-h6 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-calendar-edit</v-icon>
      일정 편집
    </h2>

    <!-- Error Alert -->
    <v-alert
      v-if="scheduleStore.error"
      type="error"
      class="mb-4"
      closable
      @click:close="scheduleStore.clearError()"
    >
      {{ scheduleStore.error }}
    </v-alert>

    <!-- Loading State -->
    <div v-if="scheduleStore.isLoading" class="loading-state text-center py-8">
      <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
      <p class="text-body-1 mt-3">일정을 불러오는 중...</p>
    </div>

    <!-- Empty State -->
    <div v-else-if="scheduleCandidates.length === 0" class="empty-state text-center py-8">
      <v-icon size="x-large" color="grey">mdi-calendar-blank</v-icon>
      <p class="text-body-1 text-grey-darken-1 mt-3">등록된 일정이 없습니다</p>
    </div>

    <!-- Schedule Cards with Plus Buttons -->
    <div v-else class="schedule-cards">
      <div
        v-for="schedule in scheduleCandidates"
        :key="schedule.contentId"
        class="schedule-card-container"
      >
        <!-- Plus Button to Make Official -->
        <v-btn
          icon
          size="small"
          color="success"
          variant="elevated"
          class="make-official-button"
          @click="handleMakeOfficial(schedule)"
          :title="'확정 일정으로 추가'"
        >
          <v-icon>mdi-plus</v-icon>
        </v-btn>

        <!-- Schedule Card -->
        <ScheduleCard
          :schedule="schedule"
          class="schedule-card-item"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import ScheduleCard from './ScheduleCard.vue'
import { useScheduleStore } from '@/stores/schedule'

const scheduleStore = useScheduleStore()

// Get only schedule candidates (non-official schedules)
const scheduleCandidates = computed(() => {
  const candidates = scheduleStore.getAllScheduleCandidates()
  console.log('Schedule candidates computed:', candidates)
  console.log('Total schedules in store:', scheduleStore.schedules.length)
  return candidates
})

// Handle making a schedule official
const handleMakeOfficial = (schedule) => {
  console.log('Making schedule official:', schedule.name)
  console.log('Current selected day:', scheduleStore.selectedDay)

  const success = scheduleStore.makeScheduleOfficial(schedule.contentId)

  if (success) {
    console.log(`Schedule "${schedule.name}" successfully made official!`)
  } else {
    console.error('Failed to make schedule official')
  }
}
</script>

<style scoped>
.schedule-edit-list {
  padding: 16px;
}

.empty-state,
.loading-state {
  border-radius: 8px;
  background-color: #f5f5f5;
}

.schedule-cards {
  max-width: 100%;
}

.schedule-card-container {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.make-official-button {
  width: 48px !important;
  height: 48px !important;
  border-radius: 50% !important;
  min-width: auto !important;
  transition: transform 0.2s, box-shadow 0.2s;
  flex-shrink: 0;
}

.make-official-button:hover {
  transform: translateY(-1px) scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2) !important;
}

.schedule-card-item {
  flex: 1;
}
</style>
