<template>
  <v-card class="transportation-card" elevation="2">
    <v-card-item class="pa-4">
      <!-- Transportation Type -->
      <div class="transportation-type mb-3">
        <v-chip size="small" color="secondary" variant="flat" class="text-caption">
          {{ getTransportationType(transportation.typeId) }}
        </v-chip>
      </div>

      <!-- Departure and Arrival Labels -->
      <div class="time-labels d-flex justify-space-between mb-1">
        <span class="text-caption text-grey-darken-1">출발</span>
        <span class="text-caption text-grey-darken-1">도착</span>
      </div>

      <!-- Departure and Arrival Times with Arrow -->
      <div class="time-section d-flex justify-space-between align-center mb-2">
        <span class="text-h6 font-weight-bold">{{ transportation.departure }}</span>
        <div class="arrow-container">
          <v-icon color="primary" size="large">mdi-arrow-right</v-icon>
        </div>
        <span class="text-h6 font-weight-bold">{{ transportation.arrival }}</span>
      </div>

      <!-- Departure and Arrival Locations -->
      <div class="location-section d-flex justify-space-between mb-3">
        <span class="text-body-2 text-grey-darken-2">{{ transportation.from }}</span>
        <span class="text-body-2 text-grey-darken-2">{{ transportation.to }}</span>
      </div>

      <!-- Divider -->
      <v-divider class="mb-3"></v-divider>

      <!-- Transportation Description -->
      <div class="description-section">
        <p class="text-body-2 text-grey-darken-1 mb-0">{{ transportation.description }}</p>
      </div>
    </v-card-item>
  </v-card>
</template>

<script setup>
defineProps({
  transportation: {
    type: Object,
    required: true,
  },
})

// Helper function to get transportation type name
const getTransportationType = (typeId) => {
  const types = {
    1: '항공편',
    2: '기차',
    3: '버스',
    4: '지하철',
    5: '택시',
    6: '렌터카',
    7: '도보',
    8: '기타',
  }
  return types[typeId] || '교통편'
}
</script>

<style scoped>
.transportation-card {
  width: 100%;
  border-radius: 12px;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  border: 1px solid #e0e0e0;
}

.transportation-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.12) !important;
}

.transportation-type {
  display: flex;
  justify-content: flex-start;
}

.time-labels {
  font-weight: 500;
}

.time-section {
  position: relative;
}

.arrow-container {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  padding: 0 8px;
}

.location-section {
  font-weight: 500;
}

.description-section {
  min-height: 20px;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .time-section {
    flex-direction: column;
    gap: 8px;
  }

  .arrow-container {
    position: static;
    transform: none;
    align-self: center;
  }

  .location-section {
    flex-direction: column;
    gap: 4px;
    text-align: center;
  }

  .time-labels {
    flex-direction: column;
    gap: 4px;
    text-align: center;
  }
}
</style>
