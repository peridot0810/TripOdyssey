<template>
  <div class="calendar-wrapper">
    <div class="calendar-header">
      <span class="month-label">May 2025</span>
    </div>

    <div class="calendar-grid">
      <div class="weekday" v-for="(day, i) in weekDays" :key="i">{{ day }}</div>

      <div v-for="(day, i) in blankDays" :key="'blank-' + i" class="date-cell blank" />

      <div
        v-for="date in dates"
        :key="date.toDateString()"
        class="date-cell"
        :class="{
          selected: isSelected(date),
          inRange: isInRange(date),
        }"
        @click="selectDate(date)"
      >
        {{ date.getDate() }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const selectedStart = ref(null)
const selectedEnd = ref(null)

// Month: May 2025 (0-indexed)
const year = 2025
const month = 4
const firstDay = new Date(year, month, 1)
const lastDay = new Date(year, month + 1, 0)

const weekDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
const blankDays = Array.from({ length: firstDay.getDay() }) // leading blanks

const dates = Array.from({ length: lastDay.getDate() }, (_, i) => {
  return new Date(year, month, i + 1)
})

function selectDate(date) {
  if (!selectedStart.value || (selectedStart.value && selectedEnd.value)) {
    selectedStart.value = date
    selectedEnd.value = null
  } else if (selectedStart.value && !selectedEnd.value) {
    if (date >= selectedStart.value) {
      selectedEnd.value = date
      console.log('Selected range:', selectedStart.value.toDateString(), '→', date.toDateString())
    } else {
      selectedStart.value = date
    }
  }
}

function isSelected(date) {
  return (
    (selectedStart.value && date.toDateString() === selectedStart.value.toDateString()) ||
    (selectedEnd.value && date.toDateString() === selectedEnd.value.toDateString())
  )
}

function isInRange(date) {
  if (!selectedStart.value || !selectedEnd.value) return false
  return date > selectedStart.value && date < selectedEnd.value
}
</script>

<style scoped>
.calendar-wrapper {
  width: 340px;
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
  font-family: 'Segoe UI', sans-serif;
}

.calendar-header {
  text-align: center;
  margin-bottom: 12px;
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 6px;
}

.weekday {
  text-align: center;
  font-weight: 500;
  font-size: 0.9rem;
  color: #666;
  padding-bottom: 4px;
  border-bottom: 1px solid #eee;
}

.date-cell {
  text-align: center;
  padding: 10px;
  border-radius: 8px;
  cursor: pointer;
  user-select: none;
  transition: background-color 0.2s;
}

.date-cell:hover {
  background-color: #e0f7fa;
}

.date-cell.selected {
  background-color: #1976d2;
  color: white;
  font-weight: bold;
}

.date-cell.inRange {
  background-color: #bbdefb;
  color: #333;
}

.date-cell.blank {
  pointer-events: none;
  visibility: hidden;
}
</style>
