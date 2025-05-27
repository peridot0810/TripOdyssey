<template>
  <div class="trip-progress nintendo-card">
    <h3 class="progress-title">TRIP PROGRESS</h3>

    <!-- Progress Flow -->
    <div class="progress-flow">
      <div
        v-for="(stage, index) in stages"
        :key="index"
        class="stage-item"
        :class="[getStageClass(index), `stage-color-${index}`]"
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
      <button
        class="nintendo-btn nintendo-btn--red"
        :disabled="currentStage === 0"
        @click="rollback"
      >
        ← 롤백
      </button>
      <button
        class="nintendo-btn nintendo-btn--blue"
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

const currentStage = ref(1)

const stages = ref([
  { name: '일정조율', description: '그룹 멤버들과 여행 일정을 조율하는 단계입니다.' },
  { name: '역할배정', description: '모든 멤버의 역할이 정해진 후 다음 단계로 진행할 수 있습니다.' },
  { name: '여행계획', description: '세부 여행 계획을 수복하는 단계입니다.' },
  { name: '여행중', description: '현재 여행을 진행 중인 단계입니다.' },
  { name: '정산', description: '여행 비용을 정산하는 단계입니다.' },
  { name: '완료', description: '모든 여행 과정이 완료된 단계입니다.' },
])

const currentStageInfo = computed(() => stages.value[currentStage.value] || stages.value[0])

const getStageClass = (index) => {
  if (index < currentStage.value) return 'completed'
  if (index === currentStage.value) return 'current'
  return 'pending'
}

const proceed = () => {
  if (currentStage.value < stages.value.length - 1) currentStage.value++
}

const rollback = () => {
  if (currentStage.value > 0) currentStage.value--
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

.trip-progress {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  padding: 1rem;
  border-radius: 20px;
  background: var(--card-bg, #ffffff);
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.1);
  font-family: 'Nunito', sans-serif;
}

.progress-title {
  font-size: 1.1rem;
  font-weight: 700;
  color: var(--text-primary, #2d3748);
  text-align: center;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #e0e0e0;
}

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
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: bold;
}

.stage-item.completed .stage-indicator {
  background: var(--joy-con-teal, #4ecdc4);
  color: white;
}

.stage-item.current .stage-indicator {
  background: var(--joy-con-blue-light, #45b7d1);
  color: white;
  box-shadow: 0 0 0 3px rgba(69, 183, 209, 0.3);
}

.stage-item.pending .stage-indicator {
  background: #e9ecef;
  color: #6c757d;
  border: 2px solid #dee2e6;
}

.stage-name {
  font-size: 0.75rem;
  text-align: center;
  font-weight: 500;
  color: var(--text-secondary, #718096);
}

.stage-item.completed .stage-name {
  color: var(--joy-con-teal, #4ecdc4);
}

.stage-item.current .stage-name {
  color: var(--joy-con-blue-light, #45b7d1);
  font-weight: 700;
}

.stage-arrow {
  position: absolute;
  right: -0.75rem;
  top: 12px;
  color: #dee2e6;
  font-size: 0.8rem;
  z-index: 1;
}

.progress-info {
  background: var(--bg-light, #edf2f7);
  padding: 0.75rem;
  border-radius: 12px;
  border-left: 6px solid var(--joy-con-blue-light, #45b7d1);
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
  background: var(--joy-con-blue-light);
  color: rgb(0, 0, 0);
  padding: 0.125rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
}

.stage-description {
  font-size: 0.875rem;
  color: #555;
  line-height: 1.5;
}

.control-buttons {
  display: flex;
  gap: 0.75rem;
  margin-top: 0.5rem;
  justify-content: center;
}

.control-btn {
  font-family: 'Nunito', sans-serif;
  font-size: 0.875rem;
  font-weight: 600;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.nintendo-btn {
  flex: 1;
  padding: 0.75rem 1.25rem;
  border: none;
  border-radius: 20px;
  font-weight: 700;
  font-family: 'Nunito', sans-serif;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

/* Red variant (rollback) */
.nintendo-btn--red {
  background: linear-gradient(135deg, #ff6b6b, #ee5a52);
  color: white;
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.4);
}

.nintendo-btn--red:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(255, 107, 107, 0.6);
}

/* Blue variant (proceed) */
.nintendo-btn--blue {
  background: linear-gradient(135deg, #45b7d1, #007bff);
  color: white;
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.4);
}

.nintendo-btn--blue:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 123, 255, 0.6);
}

.nintendo-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

/* Stage-specific colors */
.stage-color-0 .stage-indicator,
.stage-color-0 .stage-name {
  --stage-color: #f9ca24;
}
.stage-color-1 .stage-indicator,
.stage-color-1 .stage-name {
  --stage-color: #4ecdc4;
}
.stage-color-2 .stage-indicator,
.stage-color-2 .stage-name {
  --stage-color: #45b7d1;
}
.stage-color-3 .stage-indicator,
.stage-color-3 .stage-name {
  --stage-color: #6c5ce7;
}
.stage-color-4 .stage-indicator,
.stage-color-4 .stage-name {
  --stage-color: #ff6b6b;
}
.stage-color-5 .stage-indicator,
.stage-color-5 .stage-name {
  --stage-color: #2ed573;
}

/* Apply the color depending on state */
.stage-item.completed .stage-indicator {
  background: var(--stage-color);
  color: white;
}
.stage-item.current .stage-indicator {
  background: var(--stage-color);
  color: white;
  box-shadow: 0 0 0 3px rgba(0, 0, 0, 0.15);
}
.stage-item.completed .stage-name,
.stage-item.current .stage-name {
  color: var(--stage-color);
}

@keyframes pulse-glow {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(0, 123, 255, 0.4);
  }
  70% {
    transform: scale(1.08);
    box-shadow: 0 0 0 10px rgba(0, 123, 255, 0);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(0, 123, 255, 0);
  }
}

.stage-item.current .stage-indicator {
  animation: pulse-glow 1.5s infinite;
}

@keyframes pulse-glow-colored {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 var(--stage-color, rgba(0, 0, 0, 0.1));
  }
  70% {
    transform: scale(1.08);
    box-shadow: 0 0 0 10px transparent;
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 transparent;
  }
}

.stage-item.current .stage-indicator {
  animation: pulse-glow-colored 1.5s ease-in-out infinite;
}
</style>
