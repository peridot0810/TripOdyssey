<template>
  <div class="schedule-edit-list">
    <h2 class="text-h6 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-calendar-edit</v-icon>
      일정 편집
    </h2>

    <div v-if="schedules.length === 0" class="empty-state text-center py-8">
      <v-icon size="x-large" color="grey">mdi-calendar-blank</v-icon>
      <p class="text-body-1 text-grey-darken-1 mt-3">등록된 일정이 없습니다</p>
    </div>

    <div v-else class="schedule-cards">
      <ScheduleCard v-for="schedule in schedules" :key="schedule.contentId" :schedule="schedule" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ScheduleCard from './ScheduleCard.vue'
import { dummyScheduleData } from '@/data/schedule/dummyScheduleData.js'

const schedules = ref(dummyScheduleData)
</script>

<style scoped>
.schedule-edit-list {
  padding: 16px;
}

.empty-state {
  border-radius: 8px;
  background-color: #f5f5f5;
}

.schedule-cards {
  max-width: 100%;
}
</style>
