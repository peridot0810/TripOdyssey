<template>
  <div class="group-schedule">
    <h2 class="text-h6 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-calendar-check</v-icon>
      그룹 일정
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

      <!-- Read-only info hint -->
      <div class="readonly-info-hint mb-3">
        <v-alert type="success" variant="tonal" density="compact" class="text-caption">
          <v-icon start>mdi-eye</v-icon>
          일정을 확인하고 있습니다
        </v-alert>
      </div>

      <!-- Each Schedule Item with Order Badge and Finance Button -->
      <div
        v-for="(schedule, index) in filteredSchedules"
        :key="schedule.contentId"
        class="schedule-item"
      >
        <div class="schedule-content">
          <!-- Order Badge positioned absolutely over the schedule card -->
          <div class="order-badge-overlay">
            <v-chip size="small" color="primary" variant="elevated" class="order-chip">
              {{ index + 1 }}
              <v-icon end size="small">mdi-check-circle</v-icon>
            </v-chip>
          </div>

          <!-- Original Schedule Card (unchanged) -->
          <div class="schedule-card-wrapper">
            <ScheduleCard :schedule="schedule" />
          </div>

          <!-- Finance Button - positioned to the right of the card -->
          <div v-if="isFinancePage" class="finance-button-container">
            <v-btn
              icon
              color="success"
              size="large"
              variant="elevated"
              @click="openFinanceModal(schedule)"
              class="finance-add-btn"
            >
              <v-icon>mdi-plus</v-icon>
            </v-btn>
          </div>
        </div>
      </div>
    </div>

    <!-- Create Finance Modal -->
    <CreateFinanceModal
      v-model="showFinanceModal"
      :schedule="selectedSchedule"
      @close="closeFinanceModal"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import ScheduleCard from '../schedule/ScheduleCard.vue'
import CreateFinanceModal from '../finance/CreateFinanceModal.vue'
import { dummyScheduleData } from '@/data/schedule/dummyScheduleData.js'

// Props
const props = defineProps({
  scheduleData: {
    type: Array,
    default: () => dummyScheduleData,
  },
  title: {
    type: String,
    default: '그룹 일정',
  },
})

// Router
const route = useRoute()

// Reactive data
const schedules = ref([...props.scheduleData])
const selectedDay = ref(1)
const showFinanceModal = ref(false)
const selectedSchedule = ref(null)

// Check if current route is finance page
const isFinancePage = computed(() => {
  return route.path.includes('finance')
})

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

// Methods
const openFinanceModal = (schedule) => {
  selectedSchedule.value = schedule
  showFinanceModal.value = true
}

const closeFinanceModal = () => {
  showFinanceModal.value = false
  selectedSchedule.value = null
}
</script>

<style scoped>
.group-schedule {
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

.readonly-info-hint {
  margin-bottom: 12px;
}

/* Schedule Item Styling */
.schedule-item {
  margin-bottom: 20px;
}

.schedule-content {
  display: flex;
  align-items: center;
  gap: 16px;
  position: relative;
}

.schedule-card-wrapper {
  flex: 1;
  position: relative;
}

.order-badge-overlay {
  position: absolute;
  top: 16px;
  right: 16px;
  z-index: 15;
}

.order-chip {
  user-select: none;
}

/* Finance Button Container */
.finance-button-container {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.finance-add-btn {
  box-shadow: 0 4px 8px rgba(76, 175, 80, 0.3) !important;
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
