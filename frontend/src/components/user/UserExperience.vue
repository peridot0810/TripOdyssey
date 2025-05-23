<template>
  <v-card class="experience-card" elevation="3">
    <v-card-title class="text-h6 font-weight-bold d-flex align-center">
      <v-icon color="primary" class="mr-2">mdi-chart-donut</v-icon>
      경험치 현황
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text class="pa-4">
      <v-row>
        <!-- Total Experience Display -->
        <v-col cols="12" md="4" class="text-center">
          <div class="total-exp-container">
            <v-progress-circular
              :model-value="totalExpPercentage"
              :size="120"
              :width="12"
              color="primary"
              class="total-exp-circle"
            >
              <div class="total-exp-content">
                <div class="total-exp-number">{{ totalExperience }}</div>
                <div class="total-exp-label">총 경험치</div>
              </div>
            </v-progress-circular>
          </div>
        </v-col>

        <!-- Individual Experience Stats -->
        <v-col cols="12" md="8">
          <v-row>
            <v-col
              cols="6"
              sm="4"
              v-for="exp in experienceStats"
              :key="exp.key"
              class="text-center"
            >
              <div class="exp-item">
                <v-progress-circular
                  :model-value="exp.percentage"
                  :size="80"
                  :width="8"
                  :color="exp.color"
                  class="exp-circle mb-2"
                >
                  <div class="exp-value">{{ exp.value }}</div>
                </v-progress-circular>
                <div class="exp-label text-caption">{{ exp.label }}</div>
              </div>
            </v-col>
          </v-row>
        </v-col>
      </v-row>

      <!-- Experience Bars Alternative View -->
      <v-divider class="my-4"></v-divider>
      <div class="experience-bars">
        <div v-for="exp in experienceStats" :key="exp.key" class="exp-bar-item mb-3">
          <div class="d-flex justify-space-between align-center mb-1">
            <span class="text-body-2 font-weight-medium">{{ exp.label }}</span>
            <span class="text-caption text-grey">{{ exp.value }}/100</span>
          </div>
          <v-progress-linear
            :model-value="exp.percentage"
            :color="exp.color"
            height="8"
            rounded
          ></v-progress-linear>
        </div>
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  experienceData: {
    type: Object,
    required: true,
  },
})

const experienceStats = computed(() => [
  {
    key: 'leader',
    label: '리더십',
    value: props.experienceData.leaderExp,
    percentage: props.experienceData.leaderExp,
    color: '#1976d2',
  },
  {
    key: 'schedule',
    label: '일정관리',
    value: props.experienceData.scheduleExp,
    percentage: props.experienceData.scheduleExp,
    color: '#388e3c',
  },
  {
    key: 'finance',
    label: '재정관리',
    value: props.experienceData.financeExp,
    percentage: props.experienceData.financeExp,
    color: '#f57c00',
  },
  {
    key: 'logistics',
    label: '물류관리',
    value: props.experienceData.logisticsExp,
    percentage: props.experienceData.logisticsExp,
    color: '#7b1fa2',
  },
  {
    key: 'member',
    label: '멤버십',
    value: props.experienceData.memberExp,
    percentage: props.experienceData.memberExp,
    color: '#c62828',
  },
])

const totalExperience = computed(() => {
  return experienceStats.value.reduce((sum, exp) => sum + exp.value, 0)
})

const totalExpPercentage = computed(() => {
  const maxTotal = 500 // 100 * 5 categories
  return Math.min((totalExperience.value / maxTotal) * 100, 100)
})
</script>

<style scoped>
.experience-card {
  border-radius: 16px;
  height: 100%;
}

.total-exp-container {
  margin: 16px 0;
}

.total-exp-circle {
  position: relative;
}

.total-exp-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.total-exp-number {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1976d2;
}

.total-exp-label {
  font-size: 0.75rem;
  color: #666;
  margin-top: 2px;
}

.exp-item {
  margin-bottom: 16px;
}

.exp-circle {
  position: relative;
}

.exp-value {
  font-size: 0.9rem;
  font-weight: bold;
  color: #333;
}

.exp-label {
  color: #666;
  font-weight: medium;
}

.experience-bars {
  margin-top: 8px;
}

.exp-bar-item {
  padding: 0 8px;
}

@media (max-width: 960px) {
  .total-exp-container {
    margin-bottom: 24px;
  }
}
</style>
