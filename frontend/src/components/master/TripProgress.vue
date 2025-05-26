<template>
  <div class="trip-progress">
    <h3 class="progress-title">TRIP PROGRESS</h3>

    <!-- Progress Flow -->
    <div class="progress-flow">
      <div
        v-for="(stage, index) in stages"
        :key="index"
        class="stage-item"
        :class="getStageClass(index)"
      >
        <div class="stage-indicator">
          <span v-if="index < currentStage" class="completed">✓</span>
          <span v-else-if="index === currentStage" class="current">●</span>
          <span v-else class="pending">○</span>
        </div>
        <span class="stage-name">{{ stage.name }}</span>
        <div v-if="index < stages.length - 1" class="stage-arrow">→</div>
      </div>
    </div>

    <!-- Progress Info -->
    <div class="progress-info">
      <div class="current-stage-info">
        <span class="stage-label">{{ currentStageInfo.name }} 단계</span>
        <span class="stage-count">({{ currentStage + 1 }}/{{ stages.length }})</span>
      </div>
      <div class="stage-description">
        {{ currentStageInfo.description }}
      </div>
    </div>

    <!-- Control Buttons -->
    <div class="control-buttons">
      <button class="control-btn rollback-btn" :disabled="currentStage === 0" @click="rollback">
        ← 롤백
      </button>
      <button
        class="control-btn proceed-btn"
        :disabled="currentStage === stages.length - 1"
        @click="proceed"
      >
        진행 →
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// State
const currentStage = ref(1) // Start at stage 1 (role assignment)

// Dummy stage data
const stages = ref([
  {
    name: '일정조율',
    description: '그룹 멤버들과 여행 일정을 조율하는 단계입니다.',
  },
  {
    name: '역할배정',
    description: '모든 멤버의 역할이 정해지면 다음 단계로 진행할 수 있습니다.',
  },
  {
    name: '여행계획',
    description: '세부 여행 계획을 수립하는 단계입니다.',
  },
  {
    name: '여행중',
    description: '현재 여행을 진행 중인 단계입니다.',
  },
  {
    name: '정산',
    description: '여행 비용을 정산하는 단계입니다.',
  },
  {
    name: '완료',
    description: '모든 여행 과정이 완료된 단계입니다.',
  },
])

// Computed
const currentStageInfo = computed(() => {
  return stages.value[currentStage.value] || stages.value[0]
})

// Methods
const getStageClass = (index) => {
  if (index < currentStage.value) return 'completed'
  if (index === currentStage.value) return 'current'
  return 'pending'
}

const proceed = () => {
  if (currentStage.value < stages.value.length - 1) {
    currentStage.value++
    console.log('Proceeded to stage:', currentStage.value)
  }
}

const rollback = () => {
  if (currentStage.value > 0) {
    currentStage.value--
    console.log('Rolled back to stage:', currentStage.value)
  }
}
</script>

<style scoped>
.trip-progress {
  display: flex;
  flex-direction: column;
  height: 100%;
  gap: 0.75rem;
}

.progress-title {
  margin: 0;
  font-size: 1rem;
  font-weight: 600;
  color: #495057;
  text-align: center;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #e0e0e0;
}

/* Progress Flow */
.progress-flow {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.25rem;
  padding: 1rem 0;
  overflow-x: auto;
}

.stage-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.25rem;
  flex: 1;
  min-width: 60px;
  position: relative;
}

.stage-indicator {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: bold;
  transition: all 0.3s ease;
}

.stage-item.completed .stage-indicator {
  background: #28a745;
  color: white;
}

.stage-item.current .stage-indicator {
  background: #007bff;
  color: white;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
}

.stage-item.pending .stage-indicator {
  background: #e9ecef;
  color: #6c757d;
  border: 2px solid #dee2e6;
}

.stage-name {
  font-size: 0.75rem;
  text-align: center;
  line-height: 1.2;
  transition: color 0.3s ease;
}

.stage-item.completed .stage-name {
  color: #28a745;
  font-weight: 500;
}

.stage-item.current .stage-name {
  color: #007bff;
  font-weight: 600;
}

.stage-item.pending .stage-name {
  color: #6c757d;
}

.stage-arrow {
  position: absolute;
  right: -0.75rem;
  top: 12px;
  color: #dee2e6;
  font-size: 0.8rem;
  z-index: 1;
}

.stage-item.completed .stage-arrow {
  color: #28a745;
}

.stage-item.current .stage-arrow {
  color: #007bff;
}

/* Progress Info */
.progress-info {
  background: #f8f9fa;
  padding: 0.75rem;
  border-radius: 6px;
  border-left: 3px solid #007bff;
}

.current-stage-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.stage-label {
  font-weight: 600;
  color: #333;
}

.stage-count {
  background: #007bff;
  color: white;
  padding: 0.125rem 0.375rem;
  border-radius: 10px;
  font-size: 0.75rem;
}

.stage-description {
  font-size: 0.875rem;
  color: #666;
  line-height: 1.4;
}

/* Control Buttons */
.control-buttons {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.control-btn {
  flex: 1;
  padding: 0.75rem;
  border: none;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.rollback-btn {
  background: #6c757d;
  color: white;
}

.rollback-btn:hover:not(:disabled) {
  background: #5a6268;
}

.proceed-btn {
  background: #007bff;
  color: white;
}

.proceed-btn:hover:not(:disabled) {
  background: #0056b3;
}

.control-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Responsive */
@media (max-width: 768px) {
  .progress-flow {
    gap: 0.125rem;
  }

  .stage-name {
    font-size: 0.7rem;
  }

  .stage-indicator {
    width: 20px;
    height: 20px;
    font-size: 0.7rem;
  }

  .stage-arrow {
    font-size: 0.7rem;
    right: -0.5rem;
  }
}
</style>
