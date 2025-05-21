<template>
  <v-card class="pa-4 group-card" hover @click="goToGroupPage">
    <v-row>
      <v-col cols="12">
        <h2 class="group-title">{{ group.name }}</h2>
      </v-col>

      <v-col cols="7" class="d-flex flex-column justify-center">
        <p class="group-description">{{ group.description }}</p>
      </v-col>

      <v-col cols="5" class="d-flex align-center justify-center">
        <v-btn variant="outlined" color="primary" class="date-button" disabled>
          {{ group.startDate }} ~ {{ group.endDate }}
        </v-btn>
      </v-col>

      <v-col cols="12" class="role-limits">
        <div class="limit-grid">
          <span>재무</span>
          <span>일정</span>
          <span>물류</span>
          <span>{{ group.roleLimits.finance }}</span>
          <span>{{ group.roleLimits.schedule }}</span>
          <span>{{ group.roleLimits.logistics }}</span>
        </div>
      </v-col>

      <v-col cols="12" class="d-flex justify-end">
        <span class="status-text">{{ group.status }}</span>
      </v-col>
    </v-row>
  </v-card>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  group: {
    type: Object,
    required: true,
  },
})

const router = useRouter()
const goToGroupPage = () => {
  router.push(`/group/${props.group.groupId}`)
}
</script>

<style scoped>
.group-card {
  border-radius: 12px;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
  cursor: pointer;
}

.group-card:hover {
  transform: translateY(-2px);
}

.group-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 8px;
}

.group-description {
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.date-button {
  font-size: 14px;
  pointer-events: none;
}

.limit-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  text-align: center;
  gap: 8px;
  font-size: 14px;
}

.limit-grid span:nth-child(-n + 3) {
  font-weight: 500;
}

.status-text {
  font-size: 14px;
  color: #666;
  font-style: italic;
}
</style>
