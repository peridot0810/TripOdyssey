<template>
  <div class="nintendo-group-overview">
    <div class="trip-progress nintendo-card">
      <h3 class="group-title">{{ groupData.name }}</h3>

      <div class="progress-flow">
        <div
          v-for="(step, index) in progressSteps"
          :key="index"
          class="stage-item"
          :class="[getStageClass(index), `stage-color-${index}`]"
        >
          <div class="stage-indicator">
            <span v-if="step.completed" class="completed">✓</span>
            <span v-else-if="step.current" class="current">●</span>
            <span v-else class="pending">○</span>
          </div>
          <span class="stage-name">{{ step.label }}</span>
          <div v-if="index < progressSteps.length - 1" class="stage-arrow">→</div>
        </div>
      </div>
    </div>

    <!-- Group Info Grid -->
    <div class="info-grid">
      <div class="info-item nintendo-card">
        <label>여행 기간</label>
        <div class="info-value date-range">📅 {{ formattedTravelDates }}</div>
      </div>

      <!-- Role Limits -->
      <div class="info-item nintendo-card horizontal-align">
        <label>역할 제한</label>
        <div class="role-chips">
          <div class="role-chip finance">재무 {{ groupData.roleLimits.finance }}명</div>
          <div class="role-chip schedule">일정 {{ groupData.roleLimits.schedule }}명</div>
          <div class="role-chip logistics">물류 {{ groupData.roleLimits.logistics }}명</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useGroupStore } from '@/stores/group'

const groupStore = useGroupStore()
const groupData = computed(() => groupStore.myGroup)

// Progress steps configuration based on progress flags from Pinia store
const progressSteps = computed(() => {
  const steps = [
    { key: 'scheduleCoordination', label: '조율', completed: false, current: false },
    { key: 'roleAssignment', label: '역할', completed: false, current: false },
    { key: 'tripPlanning', label: '계획', completed: false, current: false },
    { key: 'onTrip', label: '여행', completed: false, current: false },
    { key: 'settlement', label: '정산', completed: false, current: false },
    { key: 'finished', label: '완료', completed: false, current: false },
  ]

  if (!groupStore.hasGroup) {
    // Default state when no group data
    steps[0].current = true
    return steps
  }

  const progress = groupData.value.progress

  // Find the last completed step
  let lastCompletedIndex = -1

  if (progress.scheduleCoordination) lastCompletedIndex = 0
  if (progress.roleAssignment) lastCompletedIndex = 1
  if (progress.tripPlanning) lastCompletedIndex = 2
  if (progress.onTrip) lastCompletedIndex = 3
  if (progress.settlement) lastCompletedIndex = 4
  if (progress.finished) lastCompletedIndex = 5

  // Set completed/current states
  steps.forEach((step, index) => {
    if (index <= lastCompletedIndex) {
      step.completed = true
    } else if (index === lastCompletedIndex + 1 && lastCompletedIndex < steps.length - 1) {
      step.current = true
    }
  })

  // If all steps are completed, mark the last one as current (finished state)
  if (lastCompletedIndex === steps.length - 1) {
    steps[steps.length - 1].current = true
    steps[steps.length - 1].completed = false
  }

  return steps
})

const getStageClass = (index) => {
  const step = progressSteps.value[index]
  if (step.completed) return 'completed'
  if (step.current) return 'current'
  return 'pending'
}

// Formatted travel dates with duration
const formattedTravelDates = computed(() => {
  if (!groupData.value.startDate || !groupData.value.endDate) {
    return '날짜 미정'
  }

  const startDate = new Date(groupData.value.startDate)
  const endDate = new Date(groupData.value.endDate)

  const formatDate = (date) => {
    return date
      .toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
      })
      .replace(/\./g, '.')
      .replace(/\s/g, '')
  }

  const timeDiff = endDate.getTime() - startDate.getTime()
  const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1

  return `${formatDate(startDate)} ~ ${formatDate(endDate)} (${dayDiff}일간)`
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

.nintendo-group-overview {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  padding: 1.5rem;
  max-width: 800px;
  margin: 0 auto;
  font-family: 'Nunito', sans-serif;
}

.nintendo-card {
  background: var(--card-bg, #ffffff);
  border: 2px solid #e2e8f0;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.nintendo-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  border-color: var(--joy-teal, #4ecdc4);
}

/* Group Header */
.group-header {
  padding: 1.5rem;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-left: 6px solid var(--joy-blue, #45b7d1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.group-title {
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0 0 1rem 0;
  color: #1e293b;
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-align: center;
}

.status-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-radius: 20px;
  font-weight: 600;
  border: 2px solid;
}

.status-badge.status-planning {
  background: linear-gradient(135deg, rgba(249, 202, 36, 0.2), rgba(249, 202, 36, 0.1));
  border-color: #f9ca24;
  color: #d4a000;
}

.status-badge.status-in_progress {
  background: linear-gradient(135deg, rgba(78, 205, 196, 0.2), rgba(78, 205, 196, 0.1));
  border-color: #4ecdc4;
  color: #2d7a6f;
}

.status-badge.status-completed {
  background: linear-gradient(135deg, rgba(46, 213, 115, 0.2), rgba(46, 213, 115, 0.1));
  border-color: #2ed573;
  color: #1d8348;
}

/* Trip Progress */
.trip-progress {
  padding: 1.5rem;
  border-left: 6px solid var(--joy-teal, #4ecdc4);
}

.section-title {
  font-size: 1.2rem;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 1.5rem 0;
  text-align: center;
}

.progress-flow {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
  padding: 1rem 0;
  overflow-x: auto;
}

.stage-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  flex: 1;
  min-width: 60px;
  position: relative;
}

.stage-indicator {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  font-weight: bold;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.stage-item.completed .stage-indicator {
  background: var(--stage-color, #4ecdc4);
  color: white;
}

.stage-item.current .stage-indicator {
  background: var(--stage-color, #45b7d1);
  color: white;
  animation: pulse-glow 1.5s ease-in-out infinite;
}

.stage-item.pending .stage-indicator {
  background: #e9ecef;
  color: #6c757d;
  border: 2px solid #dee2e6;
}

.stage-name {
  font-size: 0.8rem;
  text-align: center;
  font-weight: 600;
  color: #718096;
}

.stage-item.completed .stage-name,
.stage-item.current .stage-name {
  color: var(--stage-color, #4ecdc4);
  font-weight: 700;
}

.stage-arrow {
  position: absolute;
  right: -0.75rem;
  top: 14px;
  color: #dee2e6;
  font-size: 0.9rem;
  z-index: 1;
}

.progress-info {
  background: #edf2f7;
  padding: 1rem;
  border-radius: 12px;
  border-left: 6px solid var(--joy-blue, #45b7d1);
  margin-top: 1rem;
}

.current-stage-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.stage-label {
  font-weight: 700;
  color: #333;
}

.stage-count {
  background: var(--joy-blue, #45b7d1);
  color: white;
  padding: 0.125rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

.stage-description {
  font-size: 0.9rem;
  color: #555;
  line-height: 1.5;
}

/* Stage colors */
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

/* Info Grid */
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
}

.info-item {
  padding: 1.2rem;
  border-left: 6px solid var(--joy-teal, #4ecdc4);
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.info-item.horizontal-align {
  flex-direction: row;
  align-items: center;
  gap: 1rem;
}

.info-item label {
  font-size: 0.8rem;
  font-weight: 700;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-value {
  font-size: 1rem;
  color: #334155;
  font-weight: 500;
  line-height: 1.5;
}

.date-range {
  background: linear-gradient(135deg, #fef3c7, #fde68a);
  padding: 0.75rem;
  border-radius: 12px;
  border-left: 4px solid #f59e0b;
  color: #92400e;
  font-weight: 600;
}

.role-chips {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.role-chip {
  padding: 0.5rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  transition: all 0.2s ease;
  border: 2px solid;
}

.role-chip.finance {
  background: linear-gradient(135deg, rgba(249, 115, 22, 0.2), rgba(249, 115, 22, 0.1));
  border-color: #f97316;
  color: #c2410c;
}

.role-chip.schedule {
  background: linear-gradient(135deg, rgba(236, 72, 153, 0.2), rgba(236, 72, 153, 0.1));
  border-color: #ec4899;
  color: #be185d;
}

.role-chip.logistics {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(59, 130, 246, 0.1));
  border-color: #3b82f6;
  color: #1d4ed8;
}

.role-chip:hover {
  transform: scale(1.05);
}

/* D-Day Container */
.d-day-container {
  padding: 1.5rem;
  text-align: center;
}

.d-day-content {
  display: inline-flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 2rem;
  border-radius: 50px;
  color: white;
  font-weight: 700;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.d-day-content:hover {
  transform: translateY(-4px) scale(1.05);
}

.d-day-primary {
  background: linear-gradient(135deg, #3b82f6, #60a5fa);
}

.d-day-warning {
  background: linear-gradient(135deg, #f9ca24, #fbbf24);
}

.d-day-danger {
  background: linear-gradient(135deg, #ff6b6b, #f87171);
}

.d-day-success {
  background: linear-gradient(135deg, #4ecdc4, #6ee7b7);
}

.d-day-unknown {
  background: linear-gradient(135deg, #6c5ce7, #a78bfa);
}

.d-day-icon {
  font-size: 2rem;
}

.d-day-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.d-day-label {
  font-size: 0.9rem;
  opacity: 0.9;
}

.d-day-value {
  font-size: 1.5rem;
  margin: 0;
}

/* Animations */
@keyframes pulse-glow {
  0%,
  100% {
    transform: scale(1);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  50% {
    transform: scale(1.1);
    box-shadow: 0 4px 16px rgba(69, 183, 209, 0.4);
  }
}

/* Responsive Design */
@media (max-width: 768px) {
  .nintendo-group-overview {
    padding: 1rem;
    gap: 1rem;
  }

  .group-header {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }

  .group-title {
    font-size: 1.5rem;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .info-item.horizontal-align {
    flex-direction: column;
    align-items: flex-start;
  }

  .stage-name {
    font-size: 0.7rem;
  }

  .d-day-content {
    padding: 0.75rem 1.5rem;
  }

  .d-day-value {
    font-size: 1.2rem;
  }
}
</style>
