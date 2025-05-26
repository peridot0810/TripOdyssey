<template>
  <div class="group-schedule">
    <h2 class="text-h6 font-weight-bold mb-4 d-flex align-center">
      <SvgIcon type="mdi" :path="calendarCheckIcon" size="24" color="primary" class="mr-2" />
      그룹 일정
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

    <!-- Main Content -->
    <div v-else-if="availableDays.length > 0">
      <!-- Day Selection Component -->
      <DaySelector
        v-model="selectedDay"
        :available-days="availableDays"
      />

      <!-- Schedule Cards for Selected Day -->
      <div v-if="filteredSchedules.length === 0" class="empty-state text-center py-8">
        <SvgIcon type="mdi" :path="calendarBlankIcon" size="32" color="grey" />
        <p class="text-body-1 text-grey-darken-1 mt-3">{{ selectedDay }}일차 일정이 없습니다</p>
        <p class="text-caption text-grey mt-2">확정된 일정이 없습니다</p>
      </div>

      <div v-else class="schedule-cards">
        <!-- Each Schedule Item with Order Badge and Finance Button -->
        <div
          v-for="(schedule, index) in filteredSchedules"
          :key="schedule.contentId"
          class="schedule-item"
        >
          <div class="schedule-content">

            <!-- Original Schedule Card (unchanged) -->
            <div class="schedule-card-wrapper">
              <ScheduleCard :schedule="schedule" />
            </div>

            <!-- Finance Button - positioned to the right of the card -->
            <div v-if="isFinancePage" class="finance-button-container">
              <v-btn
                icon
                color="orange"
                size="large"
                variant="elevated"
                @click="openFinanceModal(schedule)"
                class="finance-add-btn"
              >
                <SvgIcon type="mdi" :path="plusIcon" size="20" color="white" />
              </v-btn>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- No Group Info State -->
    <div v-else class="empty-state text-center py-8">
      <SvgIcon type="mdi" :path="alertCircleIcon" size="32" color="grey" />
      <p class="text-body-1 text-grey-darken-1 mt-3">그룹 정보를 불러올 수 없습니다</p>
    </div>

    <!-- Updated Create Finance Modal -->
    <CreateFinanceModal
      ref="createFinanceModal"
      :schedule="selectedSchedule"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import ScheduleCard from '../schedule/ScheduleCard.vue'
import DaySelector from '@/components/schedule/DaySelector.vue'
import CreateFinanceModal from '../finance/CreateFinanceModal.vue'
import { useScheduleStore } from '@/stores/schedule'
import { useGroupStore } from '@/stores/group'
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiCalendarCheck,
  mdiCalendarBlank,
  mdiPlus,
  mdiAlertCircle
} from '@mdi/js'

const calendarCheckIcon = mdiCalendarCheck
const calendarBlankIcon = mdiCalendarBlank
const plusIcon = mdiPlus
const alertCircleIcon = mdiAlertCircle

// Props (keeping for backward compatibility, but not using dummy data anymore)
defineProps({
  title: {
    type: String,
    default: '그룹 일정',
  },
})

// Router
const route = useRoute()

// Stores
const scheduleStore = useScheduleStore()
const groupStore = useGroupStore()

// Get groupId from route params
const groupId = route.params.groupId

// Updated modal state (no more v-model)
const createFinanceModal = ref(null)
const selectedSchedule = ref(null)

// Use selectedDay from store
const selectedDay = computed({
  get: () => scheduleStore.selectedDay,
  set: (value) => scheduleStore.setSelectedDay(value),
})

// Check if current route is finance page
const isFinancePage = computed(() => {
  return route.path.includes('finance')
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

// Get only official schedules from store
const officialSchedules = computed(() => {
  return scheduleStore.getOfficialSchedules()
})

// Filter schedules by selected day and sort by order
const filteredSchedules = computed(() => {
  return officialSchedules.value
    .filter((schedule) => schedule.day === selectedDay.value)
    .sort((a, b) => (a.order || 0) - (b.order || 0))
})

// Updated Methods
const openFinanceModal = (schedule) => {
  selectedSchedule.value = schedule
  // Use the exposed method to open the dialog
  createFinanceModal.value?.openDialog()
}

// Removed closeFinanceModal - not needed anymore since modal manages its own state

// Clear errors from both stores
const clearErrors = () => {
  scheduleStore.clearError()
  groupStore.clearError()
}

// Fetch data on component mount
onMounted(async () => {
  if (groupId) {
    // Fetch group info if not already loaded or if different group
    if (!groupStore.hasGroup || groupStore.myGroup.groupId !== parseInt(groupId)) {
      await groupStore.getGroupInfo(groupId)
    }

    // Fetch schedules
    await scheduleStore.fetchSchedules(groupId)
  }
})
</script>

<style scoped>
.group-schedule {
  padding: 16px;
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
  width: 48px !important;
  height: 48px !important;
  border-radius: 50% !important;
  min-width: auto !important;
  transition: transform 0.2s, box-shadow 0.2s;
  box-shadow: 0 4px 8px rgba(255, 152, 0, 0.3) !important;
}

.finance-add-btn:hover {
  transform: translateY(-1px) scale(1.05);
  box-shadow: 0 6px 12px rgba(255, 152, 0, 0.4) !important;
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

/* Responsive adjustments */
@media (max-width: 600px) {
  .day-header {
    gap: 8px;
  }

  .schedule-content {
    gap: 8px;
  }

  .finance-add-btn {
    width: 40px !important;
    height: 40px !important;
  }
}
</style>
