<template>
  <div class="nintendo-group-info">
    <!-- 1. Group Name -->
    <div class="group-name-section">
      <h2 class="group-name nintendo-text--bold">{{ groupData.name }}</h2>
    </div>

    <!-- 2. Progress Bar -->
    <div class="progress-section">
      <div class="progress-container">
        <div
          v-for="(step, index) in progressSteps"
          :key="index"
          class="progress-step"
          :class="{ completed: step.completed, current: step.current }"
        >
          <div class="step-circle">
            <v-icon v-if="step.completed" size="16" color="white">mdi-check</v-icon>
            <span v-else class="step-number">{{ index + 1 }}</span>
          </div>
          <div class="step-label nintendo-text--medium">{{ step.label }}</div>
          <div
            v-if="index < progressSteps.length - 1"
            class="step-connector"
            :class="{ active: step.completed }"
          ></div>
        </div>
      </div>
    </div>

    <!-- 3. Group Details Card - Compact -->
    <div class="details-card">
      <div class="detail-row">
        <div class="detail-icon joy-con-button joy-con-button--teal">
          <v-icon size="14">mdi-text</v-icon>
        </div>
        <span class="detail-text nintendo-text--regular">
          <strong>Description:</strong> {{ groupData.description || '설명 없음' }}
        </span>
      </div>

      <div class="detail-row">
        <div class="detail-icon joy-con-button joy-con-button--blue">
          <v-icon size="14">mdi-calendar-range</v-icon>
        </div>
        <span class="detail-text nintendo-text--regular">
          <strong>Travel:</strong> {{ formattedTravelDates }}
        </span>
      </div>

      <div class="detail-row">
        <div class="detail-icon joy-con-button joy-con-button--purple">
          <v-icon size="14">mdi-calendar-plus</v-icon>
        </div>
        <span class="detail-text nintendo-text--regular">
          <strong>Created:</strong> {{ formattedCreatedDate }}
        </span>
      </div>
    </div>

    <!-- 4. Role Limits Card - Horizontal -->
    <div class="role-limits-card">
      <div class="role-limits-row">
        <div class="role-item">
          <div class="role-icon role-icon--finance">
            <v-icon size="14" color="white">mdi-calculator</v-icon>
          </div>
          <span class="role-text nintendo-text--medium"
            >Finance:{{ groupData.roleLimits.finance }}명</span
          >
        </div>

        <div class="role-item">
          <div class="role-icon role-icon--schedule">
            <v-icon size="14" color="white">mdi-calendar-check</v-icon>
          </div>
          <span class="role-text nintendo-text--medium"
            >Schedule:{{ groupData.roleLimits.schedule }}명</span
          >
        </div>

        <div class="role-item">
          <div class="role-icon role-icon--logistics">
            <v-icon size="14" color="white">mdi-truck</v-icon>
          </div>
          <span class="role-text nintendo-text--medium"
            >Logistics:{{ groupData.roleLimits.logistics }}명</span
          >
        </div>
      </div>
    </div>

    <!-- 5. D-Day Nintendo Switch Style -->
    <div class="d-day-section">
      <div class="d-day-card" :class="`d-day-card--${dDayVariant}`">
        <div class="d-day-icon">
          <v-icon size="24" color="white">mdi-calendar-clock</v-icon>
        </div>
        <div class="d-day-content">
          <div class="d-day-label nintendo-text--medium">여행까지</div>
          <div class="d-day-value nintendo-text--bold">{{ dDayText }}</div>
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

// Progress steps configuration based on group status
const progressSteps = computed(() => {
  const steps = [
    { key: 'scheduleCoordination', label: '조율', completed: false, current: false },
    { key: 'roleAssignment', label: '역할', completed: false, current: false },
    { key: 'tripPlanning', label: '계획', completed: false, current: false },
    { key: 'onTrip', label: '여행', completed: false, current: false },
    { key: 'settlement', label: '정산', completed: false, current: false },
    { key: 'finished', label: '완료', completed: false, current: false },
  ]

  // Map group status to progress steps
  const statusMapping = {
    schedule_coordination: 0,
    role_assignment: 1,
    planning: 2,
    on_trip: 3,
    settlement: 4,
    finished: 5,
  }

  const currentStepIndex = statusMapping[groupData.value.status] ?? 0

  steps.forEach((step, index) => {
    if (index < currentStepIndex) {
      step.completed = true
    } else if (index === currentStepIndex) {
      step.current = true
    }
  })

  return steps
})

// Formatted travel dates with duration - more compact
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

  // Calculate duration
  const timeDiff = endDate.getTime() - startDate.getTime()
  const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1

  return `${formatDate(startDate)}-${formatDate(endDate)} (${dayDiff}일간)`
})

// Formatted created date - more compact
const formattedCreatedDate = computed(() => {
  if (!groupData.value.createdAt) return '정보 없음'

  const createdDate = new Date(groupData.value.createdAt)
  return createdDate
    .toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    })
    .replace(/\./g, '.')
    .replace(/\s/g, '')
})

// D-Day calculation
const dDayText = computed(() => {
  if (!groupData.value.startDate) return 'D-?'

  const startDate = new Date(groupData.value.startDate)
  const today = new Date()
  const timeDiff = startDate.getTime() - today.getTime()
  const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24))

  if (dayDiff > 0) {
    return `D-${dayDiff}`
  } else if (dayDiff === 0) {
    return 'D-Day'
  } else {
    return `D+${Math.abs(dayDiff)}`
  }
})

const dDayVariant = computed(() => {
  if (!groupData.value.startDate) return 'unknown'

  const startDate = new Date(groupData.value.startDate)
  const today = new Date()
  const timeDiff = startDate.getTime() - today.getTime()
  const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24))

  if (dayDiff > 7) return 'primary'
  if (dayDiff > 3) return 'warning'
  if (dayDiff >= 0) return 'danger'
  return 'success'
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

/* CSS Variables */
:root {
  --joy-con-blue: #007bff;
  --joy-con-blue-light: #45b7d1;
  --joy-con-blue-gradient: linear-gradient(135deg, #45b7d1 0%, #96c93d 100%);
  --joy-con-red: #ff6b6b;
  --joy-con-red-gradient: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
  --joy-con-teal: #4ecdc4;
  --joy-con-teal-gradient: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%);
  --joy-con-yellow: #f9ca24;
  --joy-con-yellow-gradient: linear-gradient(135deg, #f9ca24 0%, #f0932b 100%);
  --joy-con-purple: #6c5ce7;
  --joy-con-purple-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  --card-bg: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  --card-bg-secondary: linear-gradient(135deg, #ffffff 0%, #f7fafc 100%);
  --text-primary: #2d3748;
  --text-secondary: #718096;
  --border-light: #e2e8f0;
  --bg-light: #edf2f7;

  --radius-small: 12px;
  --radius-medium: 20px;
  --radius-large: 24px;
  --radius-xl: 50%;
}

.nintendo-text--bold {
  font-family: 'Nunito', sans-serif;
  font-weight: 700;
  letter-spacing: -0.025em;
}

.nintendo-text--medium {
  font-family: 'Nunito', sans-serif;
  font-weight: 600;
}

.nintendo-text--regular {
  font-family: 'Nunito', sans-serif;
  font-weight: 400;
}

.nintendo-group-info {
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 18px;
  background: var(--card-bg);
  border-radius: var(--radius-large);
  border: 3px solid var(--border-light);
  position: relative;
  overflow: hidden;
  box-shadow:
    0 4px 12px rgba(0, 123, 255, 0.1),
    0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Nintendo Switch rainbow border */
.nintendo-group-info::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 6px;
  background: linear-gradient(
    90deg,
    #ff6b6b 0%,
    #4ecdc4 25%,
    #45b7d1 50%,
    #f9ca24 75%,
    #6c5ce7 100%
  );
  border-radius: var(--radius-large) var(--radius-large) 0 0;
}

.nintendo-group-info:hover {
  transform: translateY(-2px);
  box-shadow:
    0 8px 20px rgba(0, 123, 255, 0.15),
    0 4px 8px rgba(0, 0, 0, 0.1);
  border-color: var(--joy-con-teal);
}

/* Group Name Section */
.group-name-section {
  text-align: center;
  margin-top: 8px;
}

.group-name {
  font-size: 1.4rem;
  color: var(--joy-con-blue);
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 123, 255, 0.1);
}

/* Progress Section */
.progress-section {
  padding: 12px 0;
}

.progress-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  position: relative;
  padding: 0 10px;
}

.progress-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  flex: 1;
  max-width: 70px;
}

.step-circle {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-xl);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: bold;
  margin-bottom: 6px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  z-index: 2;
  border: 2px solid transparent;
}

.progress-step.completed .step-circle {
  background: var(--joy-con-teal-gradient);
  color: white;
  box-shadow: 0 3px 8px rgba(78, 205, 196, 0.4);
  border-color: rgba(255, 255, 255, 0.3);
}

.progress-step.current .step-circle {
  background: var(--joy-con-blue-gradient);
  color: white;
  box-shadow: 0 3px 8px rgba(69, 183, 209, 0.4);
  border-color: rgba(255, 255, 255, 0.3);
  animation: nintendo-pulse 2s infinite;
}

.progress-step:not(.completed):not(.current) .step-circle {
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e0 100%);
  color: var(--text-secondary);
  border-color: var(--border-light);
}

.step-label {
  font-size: 9px;
  text-align: center;
  line-height: 1.2;
  color: var(--text-secondary);
}

.progress-step.completed .step-label,
.progress-step.current .step-label {
  color: var(--text-primary);
}

.step-connector {
  position: absolute;
  top: 16px;
  left: 50%;
  width: calc(100% - 16px);
  height: 3px;
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e0 100%);
  z-index: 1;
  transition: all 0.3s ease;
  border-radius: 2px;
}

.step-connector.active {
  background: var(--joy-con-teal-gradient);
  box-shadow: 0 1px 4px rgba(78, 205, 196, 0.3);
}

.progress-step:last-child .step-connector {
  display: none;
}

/* Details Card - Compact */
.details-card {
  background: var(--card-bg-secondary);
  border-radius: var(--radius-medium);
  padding: 14px;
  border: 2px solid var(--border-light);
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.detail-text {
  font-size: 0.8rem;
  color: var(--text-secondary);
  line-height: 1.4;
}

.detail-text strong {
  color: var(--text-primary);
  font-weight: 600;
}

/* Role Limits Card - Horizontal */
.role-limits-card {
  background: var(--card-bg-secondary);
  border-radius: var(--radius-medium);
  padding: 14px;
  border: 2px solid var(--border-light);
}

.role-limits-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
}

.role-item {
  display: flex;
  align-items: center;
  gap: 6px;
  flex: 1;
}

.role-icon {
  width: 24px;
  height: 24px;
  border-radius: var(--radius-xl);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.role-icon--finance {
  background: var(--joy-con-teal-gradient);
  box-shadow: 0 2px 6px rgba(78, 205, 196, 0.3);
}

.role-icon--schedule {
  background: var(--joy-con-blue-gradient);
  box-shadow: 0 2px 6px rgba(69, 183, 209, 0.3);
}

.role-icon--logistics {
  background: var(--joy-con-yellow-gradient);
  box-shadow: 0 2px 6px rgba(249, 202, 36, 0.3);
}

.role-text {
  font-size: 0.75rem;
  color: var(--text-primary);
  white-space: nowrap;
}

/* Joy-Con Button Styles */
.joy-con-button {
  background: white;
  border: 2px solid var(--border-light);
  cursor: pointer;
  border-radius: var(--radius-xl);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.joy-con-button--teal {
  border-color: var(--joy-con-teal);
  color: var(--joy-con-teal);
}

.joy-con-button--teal:hover {
  transform: translateY(-1px) scale(1.05);
  background: var(--joy-con-teal);
  color: white;
  box-shadow: 0 3px 8px rgba(78, 205, 196, 0.4);
}

.joy-con-button--blue {
  border-color: var(--joy-con-blue-light);
  color: var(--joy-con-blue-light);
}

.joy-con-button--blue:hover {
  transform: translateY(-1px) scale(1.05);
  background: var(--joy-con-blue-light);
  color: white;
  box-shadow: 0 3px 8px rgba(69, 183, 209, 0.4);
}

.joy-con-button--purple {
  border-color: var(--joy-con-purple);
  color: var(--joy-con-purple);
}

.joy-con-button--purple:hover {
  transform: translateY(-1px) scale(1.05);
  background: var(--joy-con-purple);
  color: white;
  box-shadow: 0 3px 8px rgba(108, 92, 231, 0.4);
}

/* D-Day Section */
.d-day-section {
  display: flex;
  justify-content: center;
}

.d-day-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 18px;
  border-radius: var(--radius-medium);
  color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 3px solid rgba(255, 255, 255, 0.3);
  min-width: 150px;
}

.d-day-card--primary {
  background: var(--joy-con-blue-gradient);
}

.d-day-card--warning {
  background: var(--joy-con-yellow-gradient);
}

.d-day-card--danger {
  background: var(--joy-con-red-gradient);
}

.d-day-card--success {
  background: var(--joy-con-teal-gradient);
}

.d-day-card--unknown {
  background: var(--joy-con-purple-gradient);
}

.d-day-card:hover {
  transform: translateY(-2px) scale(1.05);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.25);
}

.d-day-icon {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: var(--radius-xl);
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.d-day-content {
  flex: 1;
}

.d-day-label {
  font-size: 0.75rem;
  opacity: 0.9;
  margin-bottom: 2px;
}

.d-day-value {
  font-size: 1.1rem;
  margin: 0;
}

/* Animations */
@keyframes nintendo-pulse {
  0%,
  100% {
    box-shadow: 0 3px 8px rgba(69, 183, 209, 0.4);
    transform: scale(1);
  }
  50% {
    box-shadow: 0 3px 12px rgba(69, 183, 209, 0.6);
    transform: scale(1.05);
  }
}

/* Responsive Design */
@media (max-width: 600px) {
  .nintendo-group-info {
    padding: 14px;
    gap: 12px;
  }

  .group-name {
    font-size: 1.2rem;
  }

  .progress-container {
    padding: 0 5px;
  }

  .step-circle {
    width: 28px;
    height: 28px;
    font-size: 10px;
  }

  .step-label {
    font-size: 8px;
  }

  .details-card,
  .role-limits-card {
    padding: 12px;
  }

  .detail-text {
    font-size: 0.75rem;
  }

  .role-limits-row {
    flex-direction: column;
    gap: 6px;
  }

  .role-item {
    justify-content: flex-start;
  }

  .role-text {
    font-size: 0.7rem;
  }

  .role-icon {
    width: 20px;
    height: 20px;
  }

  .d-day-card {
    padding: 12px 14px;
    min-width: 130px;
  }

  .d-day-icon {
    width: 32px;
    height: 32px;
  }

  .joy-con-button {
    width: 20px;
    height: 20px;
  }
}
</style>
