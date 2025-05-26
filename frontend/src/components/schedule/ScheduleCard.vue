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
      <SvgIcon type="mdi" :path="closeIcon" size="16" />
    </v-btn>

    <div class="card-content">
      <!-- Left side: Location Card (Image + Name only) -->
      <div class="location-section">
        <div v-if="schedule.attractionInfo" class="location-card-wrapper">
          <!-- Location Image -->
          <div class="location-image">
            <v-img
              v-if="schedule.attractionInfo.firstImage1"
              :src="schedule.attractionInfo.firstImage1"
              height="100"
              cover
              class="rounded-t"
            />
            <div v-else class="image-placeholder">
              <SvgIcon type="mdi" :path="imageOffIcon" size="24" color="grey" />
            </div>
          </div>

          <!-- Location Name only -->
          <div class="location-name-section">
            <h4 class="location-title">{{ schedule.attractionInfo.title }}</h4>
          </div>
        </div>

        <div v-else class="no-location-placeholder d-flex flex-column align-center justify-center">
          <SvgIcon type="mdi" :path="mapOffIcon" size="28" color="grey-lighten-1" />
          <span class="text-caption text-grey-lighten-1 mt-1">장소 미정</span>
        </div>
      </div>

      <!-- Right side: Schedule Info + Address at bottom -->
      <div class="schedule-info">
        <div class="schedule-content">
          <h3 class="schedule-name font-weight-bold mb-2">{{ schedule.name }}</h3>
          <p class="schedule-description text-body-2 text-grey-darken-1">
            {{ schedule.description }}
          </p>
        </div>

        <!-- Address at bottom right -->
        <div v-if="schedule.attractionInfo?.addr1" class="address-section">
          <p class="location-address text-caption text-grey d-flex align-center">
            <SvgIcon type="mdi" :path="mapMarkerIcon" size="16" class="mr-1" />
            {{ schedule.attractionInfo.addr1 }}
          </p>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script setup>
import { useScheduleStore } from '@/stores/schedule'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiClose, mdiMapMarkerOff, mdiMapMarker, mdiImageOff } from '@mdi/js'

const closeIcon = mdiClose
const mapOffIcon = mdiMapMarkerOff
const mapMarkerIcon = mdiMapMarker
const imageOffIcon = mdiImageOff

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

.location-card-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.location-image {
  height: 100px;
  width: 100%;
}

.image-placeholder {
  height: 100px;
  width: 100%;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
}

.location-name-section {
  flex: 1;
  padding: 8px 12px;
  display: flex;
  align-items: center;
  border-right: 1px solid #e0e0e0;
}

.location-title {
  font-size: 0.875rem;
  font-weight: 600;
  line-height: 1.2;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
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
  justify-content: space-between;
}

.schedule-content {
  flex: 1;
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

.address-section {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid #f0f0f0;
}

.location-address {
  margin: 0;
  font-size: 0.75rem;
  line-height: 1.2;
}
</style>
