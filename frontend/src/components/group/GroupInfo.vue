<template>
  <v-card class="group-info-card" elevation="2">
    <v-card-item class="pb-2">
      <!-- 1. Group Name -->
      <v-card-title class="group-name text-h6 font-weight-bold pa-0">
        {{ groupData.groupInfo.name }}
      </v-card-title>

      <!-- 2. Progress Bar -->
      <div class="progress-section mt-4 mb-4">
        <div class="progress-container">
          <div
            v-for="(step, index) in progressSteps"
            :key="index"
            class="progress-step"
            :class="{ completed: step.completed, current: step.current }"
          >
            <div class="step-circle">
              <v-icon v-if="step.completed" size="small" color="white">mdi-check</v-icon>
              <span v-else class="step-number">{{ index + 1 }}</span>
            </div>
            <div class="step-label">{{ step.label }}</div>
            <div
              v-if="index < progressSteps.length - 1"
              class="step-connector"
              :class="{ active: step.completed }"
            ></div>
          </div>
        </div>
      </div>

      <!-- 3. Group Description -->
      <div class="description-section mb-3">
        <v-chip size="small" color="primary" variant="tonal" class="mb-2">
          <v-icon start size="small">mdi-information-outline</v-icon>
          설명
        </v-chip>
        <p class="text-body-2 text-grey-darken-1">
          {{ groupData.groupInfo.description || '그룹 설명이 없습니다.' }}
        </p>
      </div>

      <!-- 4. D-Day -->
      <div class="d-day-section">
        <v-chip :color="dDayColor" variant="elevated" class="d-day-chip" size="large">
          <v-icon start>mdi-calendar-clock</v-icon>
          {{ dDayText }}
        </v-chip>
      </div>
    </v-card-item>
  </v-card>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  groupData: {
    type: Object,
    required: true,
  },
})

// Progress steps configuration
const progressSteps = computed(() => {
  const steps = [
    { key: 'scheduleCoordination', label: '일정조율', completed: false, current: false },
    { key: 'roleAssignment', label: '역할분담', completed: false, current: false },
    { key: 'tripPlanning', label: '여행계획', completed: false, current: false },
    { key: 'onTrip', label: '여행중', completed: false, current: false },
    { key: 'settlement', label: '정산', completed: false, current: false },
    { key: 'finished', label: '완료', completed: false, current: false },
  ]

  let foundIncomplete = false

  steps.forEach((step) => {
    const progressItem = props.groupData.progress.find((p) => p[step.key] !== undefined)
    if (progressItem && progressItem[step.key]) {
      step.completed = true
    } else if (!foundIncomplete) {
      step.current = true
      foundIncomplete = true
    }
  })

  return steps
})

// D-Day calculation
const dDayText = computed(() => {
  const startDate = new Date(props.groupData.groupInfo.startDate)
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

const dDayColor = computed(() => {
  const startDate = new Date(props.groupData.groupInfo.startDate)
  const today = new Date()
  const timeDiff = startDate.getTime() - today.getTime()
  const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24))

  if (dayDiff > 7) return 'primary'
  if (dayDiff > 3) return 'warning'
  if (dayDiff >= 0) return 'error'
  return 'success'
})
</script>

<style scoped>
.group-info-card {
  width: 100%;
}

.group-name {
  color: #1976d2;
  line-height: 1.2;
}

.progress-section {
  padding: 8px 0;
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
  max-width: 80px;
}

.step-circle {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  margin-bottom: 8px;
  transition: all 0.3s ease;
  position: relative;
  z-index: 2;
}

.progress-step.completed .step-circle {
  background-color: #4caf50;
  color: white;
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
}

.progress-step.current .step-circle {
  background-color: #2196f3;
  color: white;
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
  animation: pulse 2s infinite;
}

.progress-step:not(.completed):not(.current) .step-circle {
  background-color: #e0e0e0;
  color: #666;
}

.step-label {
  font-size: 10px;
  text-align: center;
  line-height: 1.2;
  font-weight: 500;
  color: #666;
}

.progress-step.completed .step-label,
.progress-step.current .step-label {
  color: #333;
  font-weight: 600;
}

.step-connector {
  position: absolute;
  top: 16px;
  left: 50%;
  width: calc(100% - 20px);
  height: 2px;
  background-color: #e0e0e0;
  z-index: 1;
  transition: background-color 0.3s ease;
}

.step-connector.active {
  background-color: #4caf50;
}

.progress-step:last-child .step-connector {
  display: none;
}

.description-section {
  border-left: 3px solid #e3f2fd;
  padding-left: 12px;
  background-color: #fafafa;
  border-radius: 0 8px 8px 0;
  padding: 12px;
  margin: 0 -8px;
}

.d-day-section {
  display: flex;
  justify-content: center;
  margin-top: 8px;
}

.d-day-chip {
  font-weight: bold;
  font-size: 14px;
}

@keyframes pulse {
  0% {
    box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
  }
  50% {
    box-shadow: 0 2px 16px rgba(33, 150, 243, 0.6);
  }
  100% {
    box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
  }
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .progress-container {
    padding: 0 5px;
  }

  .step-circle {
    width: 28px;
    height: 28px;
    font-size: 10px;
  }

  .step-label {
    font-size: 9px;
  }
}
</style>
