<template>
  <v-card class="schedule-card" elevation="2">
    <!-- Remove Button (top-right corner) -->
    <v-btn
      icon
      size="x-small"
      color="error"
      variant="text"
      class="remove-button"
      @click="handleRemove"
      :title="'일정 삭제'"
    >
      <v-icon size="small">mdi-close</v-icon>
    </v-btn>

    <div class="card-content">
      <!-- Left side: Location Card -->
      <div class="location-section">
        <LocationCard v-if="schedule.attractionInfo" :location="schedule.attractionInfo" />
        <div v-else class="no-location-placeholder d-flex flex-column align-center justify-center">
          <v-icon color="grey-lighten-1" size="large">mdi-map-marker-off</v-icon>
          <span class="text-caption text-grey-lighten-1 mt-1">장소 미정</span>
        </div>
      </div>

      <!-- Right side: Schedule Info -->
      <div class="schedule-info">
        <h3 class="schedule-name font-weight-bold mb-2">{{ schedule.name }}</h3>
        <p class="schedule-description text-body-2 text-grey-darken-1">
          {{ schedule.description }}
        </p>
      </div>
    </div>
  </v-card>
</template>

<script setup>
import LocationCard from './LocationCard.vue'
import { useScheduleStore } from '@/stores/schedule'

const props = defineProps({
  schedule: {
    type: Object,
    required: true,
  },
})

const scheduleStore = useScheduleStore()

const handleRemove = () => {
  console.log('Removing schedule:', props.schedule.name)

  const success = scheduleStore.deleteSchedule(props.schedule.contentId)

  if (success) {
    console.log(`Schedule "${props.schedule.name}" successfully removed!`)
  } else {
    console.error('Failed to remove schedule')
  }
}
</script>

<style scoped>
.schedule-card {
  width: 100%;
  margin-bottom: 12px;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  position: relative;
}

.schedule-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.12) !important;
}

.schedule-card:hover .remove-button {
  opacity: 1;
}

.remove-button {
  position: absolute;
  top: 8px;
  right: 8px;
  z-index: 10;
  opacity: 0;
  transition: opacity 0.2s;
  background-color: rgba(255, 255, 255, 0.9);
}

.remove-button:hover {
  background-color: rgba(244, 67, 54, 0.1);
}

.card-content {
  display: flex;
  height: 140px;
}

.location-section {
  width: 200px;
  flex-shrink: 0;
  height: 100%;
}

.no-location-placeholder {
  width: 100%;
  height: 100%;
  background-color: #f8f8f8;
  border-right: 1px solid #e0e0e0;
}

.schedule-info {
  flex: 1;
  padding: 16px 20px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.schedule-name {
  font-size: 1.1rem;
  line-height: 1.3;
  margin-bottom: 8px;
}

.schedule-description {
  line-height: 1.4;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
</style>
