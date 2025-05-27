<template>
  <div class="experience-card nintendo-card">
    <div class="card-header">
      <svg-icon type="mdi" :path="chartDonutPath" size="24" color="#4ecdc4" class="header-icon" />
      <h3 class="header-title">경험치 현황</h3>
    </div>

    <div class="card-content">
      <div class="experience-grid">
        <!-- Total Experience Display -->
        <div class="total-exp-section">
          <div class="total-exp-container">
            <div class="level-badge">Lv.{{ totalLevel }}</div>
            <div class="exp-circle-wrapper">
              <div class="exp-circle total-circle" :style="{ '--progress': totalExpPercentage }">
                <div class="circle-content">
                  <div class="exp-number">{{ totalExperience }}</div>
                  <div class="exp-label">총 경험치</div>
                </div>
              </div>
            </div>
            <div class="level-info">
              <span class="current-exp">{{ currentLevelExp }}</span>
              <span class="exp-separator">/</span>
              <span class="max-exp">100</span>
            </div>
          </div>
        </div>

        <!-- Individual Experience Stats -->
        <div class="individual-exp-section">
          <div class="exp-items-grid">
            <div v-for="exp in experienceStats" :key="exp.key" class="exp-item nintendo-info-card">
              <div class="exp-icon" :class="`exp-icon--${exp.key}`">
                <svg-icon type="mdi" :path="exp.icon" size="20" color="white" />
              </div>
              <div class="exp-details">
                <div class="exp-item-header">
                  <span class="exp-category">{{ exp.label }}</span>
                  <span class="exp-level">Lv.{{ exp.level }}</span>
                </div>
                <div class="exp-progress">
                  <div class="exp-bar">
                    <div
                      class="exp-bar-fill"
                      :style="{
                        width: exp.percentage + '%',
                        backgroundColor: exp.color,
                      }"
                    ></div>
                  </div>
                  <span class="exp-text">{{ exp.currentExp }}/100</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiChartDonut,
  mdiCrown,
  mdiCalendarCheck,
  mdiCurrencyUsd,
  mdiTruck,
  mdiAccountGroup,
} from '@mdi/js'

const chartDonutPath = mdiChartDonut

const props = defineProps({
  experienceData: {
    type: Object,
    required: true,
  },
})

// Helper function to calculate level and current exp
const calculateLevel = (totalExp) => {
  const level = Math.floor(totalExp / 100)
  const currentExp = totalExp % 100
  const percentage = currentExp
  return { level, currentExp, percentage }
}

const experienceStats = computed(() => [
  {
    key: 'leader',
    label: '리더십',
    icon: mdiCrown,
    value: props.experienceData.leaderExp,
    ...calculateLevel(props.experienceData.leaderExp),
    color: '#3b82f6',
  },
  {
    key: 'schedule',
    label: '일정관리',
    icon: mdiCalendarCheck,
    value: props.experienceData.scheduleExp,
    ...calculateLevel(props.experienceData.scheduleExp),
    color: '#ec4899',
  },
  {
    key: 'finance',
    label: '재무관리',
    icon: mdiCurrencyUsd,
    value: props.experienceData.financeExp,
    ...calculateLevel(props.experienceData.financeExp),
    color: '#f97316',
  },
  {
    key: 'logistics',
    label: '교통/숙소',
    icon: mdiTruck,
    value: props.experienceData.logisticsExp,
    ...calculateLevel(props.experienceData.logisticsExp),
    color: '#6c5ce7',
  },
  {
    key: 'member',
    label: '멤버십',
    icon: mdiAccountGroup,
    value: props.experienceData.memberExp,
    ...calculateLevel(props.experienceData.memberExp),
    color: '#4ecdc4',
  },
])

const totalExperience = computed(() => {
  return experienceStats.value.reduce((sum, exp) => sum + exp.value, 0)
})

const totalLevel = computed(() => {
  return Math.floor(totalExperience.value / 100)
})

const currentLevelExp = computed(() => {
  return totalExperience.value % 100
})

const totalExpPercentage = computed(() => {
  return currentLevelExp.value
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

.nintendo-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  border: 3px solid #e2e8f0;
  border-radius: 20px;
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.3) inset;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-family: 'Nunito', sans-serif;
  height: 100%;
}

.card-header {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  padding: 1.5rem;
  border-bottom: 2px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.header-icon {
  background: linear-gradient(135deg, #4ecdc4, #44a08d);
  padding: 8px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(78, 205, 196, 0.3);
}

.header-title {
  font-size: 1.3rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

.card-content {
  padding: 1.5rem;
}

.experience-grid {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 2rem;
  align-items: start;
}

/* Total Experience Section */
.total-exp-section {
  display: flex;
  justify-content: center;
}

.total-exp-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.level-badge {
  background: linear-gradient(135deg, #3b82f6, #60a5fa);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 700;
  font-size: 1rem;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.exp-circle-wrapper {
  position: relative;
}

.exp-circle {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  position: relative;
  background: conic-gradient(
    #4ecdc4 0deg,
    #4ecdc4 calc(var(--progress) * 3.6deg),
    #e2e8f0 calc(var(--progress) * 3.6deg),
    #e2e8f0 360deg
  );
  padding: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.total-circle {
  box-shadow: 0 8px 24px rgba(78, 205, 196, 0.3);
}

.circle-content {
  width: 100%;
  height: 100%;
  background: white;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1) inset;
}

.exp-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1e293b;
}

.exp-label {
  font-size: 0.8rem;
  color: #64748b;
  font-weight: 600;
}

.level-info {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-weight: 600;
}

.current-exp {
  color: #4ecdc4;
  font-size: 1.1rem;
}

.exp-separator {
  color: #64748b;
}

.max-exp {
  color: #64748b;
}

/* Individual Experience Section */
.exp-items-grid {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.nintendo-info-card {
  background: rgba(255, 255, 255, 0.7);
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  padding: 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.nintendo-info-card:hover {
  transform: translateX(4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  border-color: #4ecdc4;
}

.exp-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.exp-icon--leader {
  background: linear-gradient(135deg, #3b82f6, #60a5fa);
}

.exp-icon--schedule {
  background: linear-gradient(135deg, #ec4899, #f472b6);
}

.exp-icon--finance {
  background: linear-gradient(135deg, #f97316, #fb923c);
}

.exp-icon--logistics {
  background: linear-gradient(135deg, #6c5ce7, #a78bfa);
}

.exp-icon--member {
  background: linear-gradient(135deg, #4ecdc4, #44a08d);
}

.exp-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.exp-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.exp-category {
  font-size: 0.9rem;
  font-weight: 600;
  color: #1e293b;
}

.exp-level {
  font-size: 0.8rem;
  font-weight: 700;
  color: #4ecdc4;
  background: rgba(78, 205, 196, 0.1);
  padding: 0.25rem 0.5rem;
  border-radius: 8px;
}

.exp-progress {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.exp-bar {
  flex: 1;
  height: 8px;
  background: #e2e8f0;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1) inset;
}

.exp-bar-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
}

.exp-text {
  font-size: 0.8rem;
  font-weight: 600;
  color: #64748b;
  min-width: 50px;
  text-align: right;
}

/* Responsive Design */
@media (max-width: 768px) {
  .experience-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .card-content {
    padding: 1rem;
  }

  .exp-circle {
    width: 120px;
    height: 120px;
  }

  .exp-number {
    font-size: 1.3rem;
  }

  .nintendo-info-card {
    padding: 0.75rem;
  }

  .exp-icon {
    width: 36px;
    height: 36px;
  }
}

/* CSS Variables for consistency */
:root {
  --joy-teal: #4ecdc4;
  --joy-blue: #3b82f6;
  --joy-pink: #ec4899;
  --joy-orange: #f97316;
  --joy-purple: #6c5ce7;
}
</style>
