<template>
  <div class="dual-calendar-wrapper">
    <!-- Navigation Header -->
    <div class="calendar-navigation">
      <button
        class="nav-btn nav-btn-blue"
        @click="previousMonth"
        :disabled="isNavigationDisabled('prev')"
      >
        ◀
      </button>

      <div class="navigation-title">
        Navigate Months
      </div>

      <button
        class="nav-btn nav-btn-blue"
        @click="nextMonth"
        :disabled="isNavigationDisabled('next')"
      >
        ▶
      </button>
    </div>

    <!-- Vertical Calendar Layout -->
    <div class="vertical-calendar-grid">
      <!-- Top Month Title -->
      <div class="month-header top-header">
        <h3 class="month-title">{{ getMonthLabel(leftMonth, leftYear) }}</h3>
      </div>

      <!-- First Calendar -->
      <div class="calendar-container">
        <div class="calendar-grid">
          <div class="weekday" v-for="(day, i) in weekDays" :key="'top-' + i">{{ day }}</div>

          <div
            v-for="(day, i) in getBlankDays(leftMonth, leftYear)"
            :key="'top-blank-' + i"
            class="date-cell blank"
          />

          <div
            v-for="date in getDatesInMonth(leftMonth, leftYear)"
            :key="'top-' + date"
            class="date-cell"
            :class="getDateClass(leftMonth, leftYear, date)"
            @click="selectDate(new Date(leftYear, leftMonth, date))"
          >
            {{ date }}
          </div>
        </div>
      </div>

      <!-- Second Calendar -->
      <div class="calendar-container">
        <div class="calendar-grid">
          <div class="weekday" v-for="(day, i) in weekDays" :key="'bottom-' + i">{{ day }}</div>

          <div
            v-for="(day, i) in getBlankDays(rightMonth, rightYear)"
            :key="'bottom-blank-' + i"
            class="date-cell blank"
          />

          <div
            v-for="date in getDatesInMonth(rightMonth, rightYear)"
            :key="'bottom-' + date"
            class="date-cell"
            :class="getDateClass(rightMonth, rightYear, date)"
            @click="selectDate(new Date(rightYear, rightMonth, date))"
          >
            {{ date }}
          </div>
        </div>
      </div>

      <!-- Bottom Month Title -->
      <div class="month-header bottom-header">
        <h3 class="month-title">{{ getMonthLabel(rightMonth, rightYear) }}</h3>
      </div>
    </div>

    <!-- Selection Info -->
    <div v-if="selectedStart || selectedEnd" class="selection-info">
      <div class="selection-text">
        <span v-if="selectedStart && !selectedEnd">
          Start: {{ formatDate(selectedStart) }} (Select end date)
        </span>
        <span v-else-if="selectedStart && selectedEnd">
          Range: {{ formatDate(selectedStart) }} → {{ formatDate(selectedEnd) }}
        </span>
      </div>
      <button class="clear-btn" @click="clearSelection">
        Clear
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// Props (if needed for external control)
const props = defineProps({
  initialMonth: {
    type: Number,
    default: null
  },
  initialYear: {
    type: Number,
    default: null
  }
})

// Emits for parent communication
const emit = defineEmits(['dateRangeSelected', 'selectionCleared'])

// State
const selectedStart = ref(null)
const selectedEnd = ref(null)

// Current date for reference
const today = new Date()
const currentYear = today.getFullYear()
const currentMonth = today.getMonth()

// Calendar state (0-indexed months)
const leftMonth = ref(props.initialMonth ?? currentMonth)
const leftYear = ref(props.initialYear ?? currentYear)

// Right calendar is always next month from left
const rightMonth = computed(() => {
  return leftMonth.value === 11 ? 0 : leftMonth.value + 1
})

const rightYear = computed(() => {
  return leftMonth.value === 11 ? leftYear.value + 1 : leftYear.value
})

const weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
const monthNames = [
  'January', 'February', 'March', 'April', 'May', 'June',
  'July', 'August', 'September', 'October', 'November', 'December'
]

// Methods
function getMonthLabel(month, year) {
  return `${monthNames[month]} ${year}`
}

function getBlankDays(month, year) {
  const firstDay = new Date(year, month, 1)
  return firstDay.getDay()
}

function getDatesInMonth(month, year) {
  const lastDay = new Date(year, month + 1, 0)
  return Array.from({ length: lastDay.getDate() }, (_, i) => i + 1)
}

function selectDate(date) {
  if (!selectedStart.value || (selectedStart.value && selectedEnd.value)) {
    // Start new selection
    selectedStart.value = date
    selectedEnd.value = null
    emit('selectionCleared')
  } else if (selectedStart.value && !selectedEnd.value) {
    // Complete the range
    if (date >= selectedStart.value) {
      selectedEnd.value = date
      emit('dateRangeSelected', {
        startDate: selectedStart.value,
        endDate: selectedEnd.value
      })
    } else {
      // User selected earlier date, swap
      selectedEnd.value = selectedStart.value
      selectedStart.value = date
      emit('dateRangeSelected', {
        startDate: selectedStart.value,
        endDate: selectedEnd.value
      })
    }
  }
}

function clearSelection() {
  selectedStart.value = null
  selectedEnd.value = null
  emit('selectionCleared')
}

function getDateClass(month, year, date) {
  const currentDate = new Date(year, month, date)
  const dateStr = currentDate.toDateString()

  const classes = []

  // Check if this date is selected
  if (selectedStart.value && dateStr === selectedStart.value.toDateString()) {
    classes.push('selected-start')
  }
  if (selectedEnd.value && dateStr === selectedEnd.value.toDateString()) {
    classes.push('selected-end')
  }

  // Check if this date is in range
  if (isInRange(currentDate)) {
    classes.push('in-range')
  }

  // Check if this date is in the past
  if (isPastDate(currentDate)) {
    classes.push('past-date')
  }

  return classes
}

function isInRange(date) {
  if (!selectedStart.value || !selectedEnd.value) return false
  return date > selectedStart.value && date < selectedEnd.value
}

function isPastDate(date) {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return date < today
}

function previousMonth() {
  if (leftMonth.value === 0) {
    leftMonth.value = 11
    leftYear.value--
  } else {
    leftMonth.value--
  }
}

function nextMonth() {
  if (leftMonth.value === 11) {
    leftMonth.value = 0
    leftYear.value++
  } else {
    leftMonth.value++
  }
}

function isNavigationDisabled(direction) {
  const minDate = new Date(currentYear - 1, 0, 1) // 1 year ago
  const maxDate = new Date(currentYear + 2, 11, 31) // 2 years ahead

  if (direction === 'prev') {
    const targetDate = new Date(leftYear.value, leftMonth.value - 1, 1)
    return targetDate < minDate
  } else {
    const targetDate = new Date(rightYear.value, rightMonth.value + 1, 1)
    return targetDate > maxDate
  }
}

function formatDate(date) {
  if (!date) return ''
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

// Expose methods for parent component
defineExpose({
  clearSelection,
  getSelectedRange: () => ({
    startDate: selectedStart.value,
    endDate: selectedEnd.value
  })
})

onMounted(() => {
  // Initialize with current month
  console.log('DatePicker initialized with:', getMonthLabel(leftMonth.value, leftYear.value))
})
</script>

<style scoped>
.dual-calendar-wrapper {
  width: 100%;
  max-width: 400px;
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  font-family: 'Segoe UI', sans-serif;
}

.calendar-navigation {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.nav-btn {
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  transition: all 0.2s ease;
}

.nav-btn-blue {
  background-color: #1976d2;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-btn-blue:hover:not(:disabled) {
  background-color: #1565c0;
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(25, 118, 210, 0.3);
}

.nav-btn:disabled {
  background-color: #bdbdbd;
  color: #ffffff;
  cursor: not-allowed;
  opacity: 0.6;
  transform: none;
}

.navigation-title {
  font-size: 1rem;
  font-weight: 600;
  color: #666;
  text-align: center;
  min-width: 140px;
}

.vertical-calendar-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.calendar-container {
  width: 100%;
}

.month-header {
  padding: 8px 0;
  text-align: center;
  background-color: #f5f5f5;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
}

.top-header {
  margin-bottom: 8px;
}

.bottom-header {
  margin-top: 8px;
}

.month-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #1976d2;
  margin: 0;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.weekday {
  text-align: center;
  font-weight: 500;
  font-size: 0.85rem;
  color: #666;
  padding: 8px 4px;
  border-bottom: 1px solid #eee;
}

.date-cell {
  text-align: center;
  padding: 10px 8px;
  border-radius: 6px;
  cursor: pointer;
  user-select: none;
  transition: all 0.2s ease;
  font-size: 0.9rem;
  min-height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.date-cell:hover:not(.blank):not(.past-date) {
  background-color: #e3f2fd;
  transform: scale(1.05);
}

.date-cell.blank {
  pointer-events: none;
  visibility: hidden;
}

.date-cell.past-date {
  color: #bbb;
  cursor: not-allowed;
  opacity: 0.5;
}

.date-cell.selected-start,
.date-cell.selected-end {
  background-color: #1976d2;
  color: white;
  font-weight: bold;
  transform: scale(1.1);
}

.date-cell.selected-start {
  border-radius: 6px 3px 3px 6px;
}

.date-cell.selected-end {
  border-radius: 3px 6px 6px 3px;
}

.date-cell.in-range {
  background-color: #bbdefb;
  color: #1565c0;
  border-radius: 0;
}

.selection-info {
  margin-top: 16px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-left: 4px solid #1976d2;
}

.selection-text {
  font-size: 0.9rem;
  color: #333;
  font-weight: 500;
}

.clear-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  transition: background-color 0.2s ease;
}

.clear-btn:hover {
  background-color: #c82333;
}

/* Responsive design */
@media (max-width: 768px) {
  .dual-calendar-wrapper {
    padding: 12px;
    max-width: 100%;
  }

  .navigation-title {
    font-size: 0.9rem;
    min-width: 120px;
  }

  .vertical-calendar-grid {
    gap: 12px;
  }
}

@media (max-width: 480px) {
  .dual-calendar-wrapper {
    padding: 8px;
  }

  .date-cell {
    padding: 8px 4px;
    font-size: 0.8rem;
    min-height: 32px;
  }

  .weekday {
    font-size: 0.75rem;
    padding: 6px 2px;
  }

  .month-title {
    font-size: 1rem;
  }

  .nav-btn {
    width: 36px;
    height: 36px;
    font-size: 14px;
  }
}
</style>
