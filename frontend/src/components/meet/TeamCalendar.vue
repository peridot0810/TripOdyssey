<template>
  <div class="team-calendar-wrapper">
    <div class="calendar-header mb-4">
      <h2 class="text-h5 font-weight-bold text-center">
        그룹 일정 정보 {{ currentYear }}
      </h2>
      <div v-if="when2MeetStore.isLoading" class="text-center mt-2">
        <v-progress-circular indeterminate size="small" color="primary"></v-progress-circular>
        <span class="ml-2 text-caption">Loading availability data...</span>
      </div>
    </div>

    <!-- No Data Message -->
    <div v-if="!when2MeetStore.isLoading && availabilityData.length === 0" class="no-data-message">
      <v-icon size="48" color="grey-lighten-1" class="mb-2">mdi-calendar-blank</v-icon>
      <h3 class="text-h6 text-grey mb-2">No Availability Data</h3>
      <p class="text-body-2 text-grey">
        Team members haven't submitted their availability yet.
      </p>
    </div>

    <!-- Scrollable Calendar Container -->
    <div v-else class="calendar-scroll-container">
      <div class="year-calendar-grid">
        <div v-for="month in 12" :key="month" class="month-calendar">
          <div class="month-header">
            <h3 class="text-h6 font-weight-medium text-center">
              {{ getMonthName(month - 1) }}
            </h3>
          </div>

          <div class="calendar-grid">
            <div class="weekday" v-for="day in weekDays" :key="day">
              {{ day }}
            </div>

            <!-- Blank days for month start -->
            <div
              v-for="blank in getBlankDays(month - 1)"
              :key="'blank-' + blank"
              class="date-cell blank"
            />

            <!-- Actual dates -->
            <div
              v-for="date in getDatesInMonth(month - 1)"
              :key="date"
              class="date-cell"
              :class="getDateClass(month - 1, date)"
              :title="getDateTooltip(month - 1, date)"
            >
              {{ date }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useWhen2MeetStore } from '@/stores/when2meet'

// Store
const when2MeetStore = useWhen2MeetStore()

// Get current year
const currentYear = new Date().getFullYear()

// Get availability data from store
const availabilityData = computed(() => when2MeetStore.availabilityData || [])

const weekDays = ['S', 'M', 'T', 'W', 'T', 'F', 'S']
const monthNames = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
]

function getMonthName(monthIndex) {
  return monthNames[monthIndex]
}

function getBlankDays(monthIndex) {
  const firstDay = new Date(currentYear, monthIndex, 1)
  return firstDay.getDay()
}

function getDatesInMonth(monthIndex) {
  const lastDay = new Date(currentYear, monthIndex + 1, 0)
  return Array.from({ length: lastDay.getDate() }, (_, i) => i + 1)
}

function getDateClass(monthIndex, date) {
  const dateStr = `${currentYear}-${String(monthIndex + 1).padStart(2, '0')}-${String(date).padStart(2, '0')}`
  const count = when2MeetStore.getAvailabilityCountForDate(dateStr)

  if (count === 0) return ''
  if (count === 1) return 'availability-1'
  if (count <= 3) return 'availability-2'
  if (count <= 5) return 'availability-3'
  return 'availability-4'
}

function getDateTooltip(monthIndex, date) {
  const dateStr = `${currentYear}-${String(monthIndex + 1).padStart(2, '0')}-${String(date).padStart(2, '0')}`
  const count = when2MeetStore.getAvailabilityCountForDate(dateStr)
  const users = when2MeetStore.getUsersAvailableForDate(dateStr)

  if (count === 0) return 'No availability'

  const userList = users.length > 0 ? `\nUsers: ${users.join(', ')}` : ''
  return `${count} team member${count > 1 ? 's' : ''} available${userList}`
}
</script>

<style scoped>
.team-calendar-wrapper {
  padding: 16px;
  max-width: 100%;
  margin: 0 auto;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.calendar-header {
  flex-shrink: 0;
}

.no-data-message {
  text-align: center;
  padding: 40px 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.calendar-scroll-container {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 8px;
  margin-right: -8px;
}

.year-calendar-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(3, 1fr);
  gap: 16px;
  min-height: fit-content;
  padding-bottom: 16px;
}

.month-calendar {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 12px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  min-height: 200px;
  display: flex;
  flex-direction: column;
}

.month-header {
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
  flex-shrink: 0;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 3px;
  flex: 1;
}

.weekday {
  text-align: center;
  font-weight: 500;
  font-size: 0.8rem;
  color: #666;
  padding: 6px 3px;
}

.date-cell {
  text-align: center;
  padding: 6px 3px;
  font-size: 0.8rem;
  border-radius: 4px;
  cursor: default;
  min-height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  transition: all 0.2s ease;
}

.date-cell:hover:not(.blank) {
  transform: scale(1.1);
  z-index: 1;
}

.date-cell.blank {
  visibility: hidden;
}

/* Availability color levels */
.date-cell.availability-1 {
  background-color: #e3f2fd;
  color: #1565c0;
}

.date-cell.availability-2 {
  background-color: #90caf9;
  color: #0d47a1;
}

.date-cell.availability-3 {
  background-color: #42a5f5;
  color: white;
}

.date-cell.availability-4 {
  background-color: #1976d2;
  color: white;
  font-weight: bold;
}

/* Custom scrollbar */
.calendar-scroll-container::-webkit-scrollbar {
  width: 8px;
}

.calendar-scroll-container::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 4px;
}

.calendar-scroll-container::-webkit-scrollbar-thumb:hover {
  background-color: #9e9e9e;
}

.calendar-scroll-container::-webkit-scrollbar-track {
  background-color: #f5f5f5;
  border-radius: 4px;
}

/* Responsive design */
@media (max-width: 1200px) {
  .year-calendar-grid {
    grid-template-columns: repeat(3, 1fr);
    grid-template-rows: repeat(4, 1fr);
  }
}

@media (max-width: 900px) {
  .year-calendar-grid {
    grid-template-columns: repeat(2, 1fr);
    grid-template-rows: repeat(6, 1fr);
    gap: 12px;
  }

  .month-calendar {
    padding: 10px;
    min-height: 180px;
  }

  .team-calendar-wrapper {
    padding: 12px;
  }
}

@media (max-width: 600px) {
  .year-calendar-grid {
    grid-template-columns: 1fr;
    grid-template-rows: repeat(12, 1fr);
    gap: 10px;
  }

  .month-calendar {
    padding: 8px;
    min-height: 160px;
  }

  .date-cell {
    font-size: 0.75rem;
    min-height: 20px;
    padding: 4px 2px;
  }

  .weekday {
    font-size: 0.75rem;
    padding: 4px 2px;
  }

  .no-data-message {
    padding: 30px 15px;
  }
}

@media (max-width: 480px) {
  .team-calendar-wrapper {
    padding: 8px;
  }

  .calendar-scroll-container {
    padding-right: 4px;
    margin-right: -4px;
  }
}
</style>
