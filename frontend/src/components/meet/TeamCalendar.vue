<template>
  <div class="team-calendar-wrapper">
    <div class="calendar-header mb-4">
      <h2 class="text-h5 font-weight-bold text-center">
        Team Availability Calendar {{ currentYear }}
      </h2>
      <p class="text-center text-grey-darken-1">
        Darker colors indicate more team members are available
      </p>
    </div>

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

    <!-- Legend -->
    <div class="legend mt-4">
      <h4 class="text-subtitle-1 font-weight-medium mb-2">Availability Legend:</h4>
      <div class="legend-items">
        <div class="legend-item">
          <div class="legend-color" style="background-color: #e3f2fd"></div>
          <span class="text-caption">1 person</span>
        </div>
        <div class="legend-item">
          <div class="legend-color" style="background-color: #90caf9"></div>
          <span class="text-caption">2-3 people</span>
        </div>
        <div class="legend-item">
          <div class="legend-color" style="background-color: #42a5f5"></div>
          <span class="text-caption">4-5 people</span>
        </div>
        <div class="legend-item">
          <div class="legend-color" style="background-color: #1976d2"></div>
          <span class="text-caption">6+ people</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// Get current year
const currentYear = new Date().getFullYear()

// Dummy data - 6 availability entries
const availabilityData = ref([
  { id: 1, userId: 'user01', groupId: 20, startDate: '2025-07-10', endDate: '2025-07-15' },
  { id: 2, userId: 'user02', groupId: 20, startDate: '2025-07-12', endDate: '2025-07-18' },
  { id: 3, userId: 'user03', groupId: 20, startDate: '2025-08-05', endDate: '2025-08-10' },
  { id: 4, userId: 'user04', groupId: 20, startDate: '2025-07-14', endDate: '2025-07-16' },
  { id: 5, userId: 'user05', groupId: 20, startDate: '2025-09-20', endDate: '2025-09-25' },
  { id: 6, userId: 'user06', groupId: 20, startDate: '2025-07-13', endDate: '2025-07-17' },
])

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

// Create a map of date strings to availability count
const availabilityMap = computed(() => {
  const map = new Map()

  availabilityData.value.forEach((entry) => {
    const startDate = new Date(entry.startDate)
    const endDate = new Date(entry.endDate)

    // Iterate through each date in the range
    const currentDate = new Date(startDate)
    while (currentDate <= endDate) {
      const dateStr = currentDate.toISOString().split('T')[0]
      map.set(dateStr, (map.get(dateStr) || 0) + 1)
      currentDate.setDate(currentDate.getDate() + 1)
    }
  })

  return map
})

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
  const count = availabilityMap.value.get(dateStr) || 0

  if (count === 0) return ''
  if (count === 1) return 'availability-1'
  if (count <= 3) return 'availability-2'
  if (count <= 5) return 'availability-3'
  return 'availability-4'
}

function getDateTooltip(monthIndex, date) {
  const dateStr = `${currentYear}-${String(monthIndex + 1).padStart(2, '0')}-${String(date).padStart(2, '0')}`
  const count = availabilityMap.value.get(dateStr) || 0

  if (count === 0) return 'No availability'
  return `${count} team member${count > 1 ? 's' : ''} available`
}
</script>

<style scoped>
.team-calendar-wrapper {
  padding: 16px;
  max-width: 100%;
  margin: 0 auto;
}

.year-calendar-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 16px;
}

.month-calendar {
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  padding: 8px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.month-header {
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 2px;
}

.weekday {
  text-align: center;
  font-weight: 500;
  font-size: 0.75rem;
  color: #666;
  padding: 4px 2px;
}

.date-cell {
  text-align: center;
  padding: 4px 2px;
  font-size: 0.75rem;
  border-radius: 3px;
  cursor: default;
  min-height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
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

.legend {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.legend-items {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  justify-content: center;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 3px;
  border: 1px solid #ddd;
}

/* Responsive design */
@media (max-width: 1024px) {
  .year-calendar-grid {
    grid-template-columns: repeat(4, 1fr);
    grid-template-rows: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .year-calendar-grid {
    grid-template-columns: repeat(3, 1fr);
    grid-template-rows: repeat(4, 1fr);
    gap: 10px;
  }

  .team-calendar-wrapper {
    padding: 12px;
  }
}

@media (max-width: 480px) {
  .year-calendar-grid {
    grid-template-columns: repeat(2, 1fr);
    grid-template-rows: repeat(6, 1fr);
  }

  .legend-items {
    flex-direction: column;
  }
}
</style>
