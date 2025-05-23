<template>
  <v-card class="role-details-card" :class="{ 'slide-in': show }" elevation="24">
    <div class="role-details-header">
      <v-icon :icon="selectedRole.icon" size="48" color="primary" class="mb-2" />
      <h2 class="role-details-title">{{ selectedRole.name }}</h2>
      <p class="role-details-subtitle">{{ selectedRole.subtitle }}</p>
    </div>

    <v-divider class="my-4" />

    <div class="role-details-content">
      <h3 class="section-title">주요 업무</h3>
      <ul class="responsibility-list">
        <li
          v-for="(responsibility, index) in selectedRole.responsibilities"
          :key="index"
          class="responsibility-item"
        >
          <v-icon icon="mdi-check" color="success" size="16" class="mr-2" />
          {{ responsibility }}
        </li>
      </ul>

      <h3 class="section-title mt-4">필요 스킬</h3>
      <div class="skills-container">
        <v-chip
          v-for="skill in selectedRole.skills"
          :key="skill"
          color="primary"
          variant="tonal"
          size="small"
          class="ma-1"
        >
          {{ skill }}
        </v-chip>
      </div>

      <v-alert
        :type="
          selectedRole.difficulty === 'easy'
            ? 'success'
            : selectedRole.difficulty === 'medium'
              ? 'warning'
              : 'error'
        "
        variant="tonal"
        class="mt-4"
      >
        <template v-slot:prepend>
          <v-icon
            :icon="
              selectedRole.difficulty === 'easy'
                ? 'mdi-leaf'
                : selectedRole.difficulty === 'medium'
                  ? 'mdi-gauge-low'
                  : 'mdi-fire'
            "
          />
        </template>
        <strong>난이도: {{ getDifficultyText(selectedRole.difficulty) }}</strong>
        <p class="mb-0 mt-1">{{ selectedRole.difficultyDescription }}</p>
      </v-alert>
    </div>

    <v-card-actions class="pa-4">
      <v-btn
        block
        color="primary"
        variant="elevated"
        size="large"
        @click="$emit('confirm')"
        class="confirm-btn"
      >
        <v-icon icon="mdi-account-check" class="mr-2" />
        이 역할 선택하기
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
defineProps({
  selectedRole: {
    type: Object,
    required: true,
  },
  show: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['confirm'])

function getDifficultyText(difficulty) {
  switch (difficulty) {
    case 'easy':
      return '쉬움'
    case 'medium':
      return '보통'
    case 'hard':
      return '어려움'
    default:
      return '보통'
  }
}
</script>

<style scoped>
.role-details-card {
  position: fixed;
  top: 50%;
  right: -400px;
  transform: translateY(-50%);
  width: 380px;
  max-height: 80vh;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  transition: right 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1000;
  box-shadow: -8px 0 32px rgba(0, 0, 0, 0.2);
  padding: 24px;
}

.role-details-card.slide-in {
  right: 24px;
}

.role-details-header {
  text-align: center;
  padding-bottom: 8px;
}

.role-details-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1976d2;
  margin-bottom: 4px;
}

.role-details-subtitle {
  color: #666;
  margin: 0;
  font-size: 0.9rem;
}

.section-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.responsibility-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.responsibility-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 8px;
  font-size: 0.9rem;
  line-height: 1.4;
}

.skills-container {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  margin-top: 8px;
}

.confirm-btn {
  background: linear-gradient(45deg, #1976d2, #42a5f5);
  color: white;
  font-weight: bold;
  letter-spacing: 0.5px;
}

.confirm-btn:hover {
  background: linear-gradient(45deg, #1565c0, #2196f3);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(25, 118, 210, 0.4);
}

/* Custom scrollbar */
.role-details-card::-webkit-scrollbar {
  width: 6px;
}

.role-details-card::-webkit-scrollbar-thumb {
  background-color: rgba(25, 118, 210, 0.3);
  border-radius: 3px;
}

.role-details-card::-webkit-scrollbar-track {
  background-color: rgba(0, 0, 0, 0.05);
}

/* Mobile responsiveness */
@media (max-width: 768px) {
  .role-details-card {
    width: calc(100vw - 32px);
    right: -100vw;
    max-height: 70vh;
  }

  .role-details-card.slide-in {
    right: 16px;
  }
}
</style>
