<template>
  <v-container fluid class="pa-0 map-page-container">
    <v-row no-gutters class="fill-height">
      <!-- Left Section: Search Conditions + Location Detail Info -->
      <v-col cols="12" md="4" lg="3" class="left-panel">
        <SearchAndDetailPanel
          ref="searchDetailPanel"
          @search="handleSearch"
          @locationSelected="handleLocationSelected"
        />
      </v-col>

      <!-- Right Section: Map -->
      <v-col cols="12" md="8" lg="9" class="map-panel">
        <div class="map-content">
          <MapCard @locationClick="handleMapLocationClick" />
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import SearchAndDetailPanel from '@/components/map/SearchAndDetailPanel.vue'
import MapCard from '@/components/map/MapCard.vue'

const searchDetailPanel = ref(null)

// Handle search from SearchAndDetailPanel
const handleSearch = (searchParams) => {
  console.log('Map page received search:', searchParams)

  // Here you would:
  // 1. Update map with search results
  // 2. Clear any selected location
}

// Handle location selection from SearchAndDetailPanel
const handleLocationSelected = (location) => {
  console.log('Location selected:', location)

  // Here you would:
  // 1. Update map to show selected location
  // 2. Maybe zoom to location
}

// Handle location click from map
const handleMapLocationClick = (location) => {
  console.log('Map location clicked:', location)

  // Update the search detail panel with selected location
  if (searchDetailPanel.value) {
    searchDetailPanel.value.setSelectedLocation(location)
  }
}
</script>

<style scoped>
.map-page-container {
  height: calc(100vh - 64px); /* Adjust based on your app's header height */
  overflow: hidden;
}

.left-panel {
  height: 100%;
  border-right: 1px solid #e0e0e0;
  overflow: hidden;
}

.map-panel {
  height: 100%;
  overflow: hidden;
}

.map-content {
  height: 100%;
  width: 100%;
}

/* Responsive adjustments */
@media (max-width: 960px) {
  .map-page-container {
    height: auto;
    overflow: visible;
  }

  .left-panel,
  .map-panel {
    height: auto;
    overflow: visible;
  }

  .left-panel {
    border-right: none;
    border-bottom: 1px solid #e0e0e0;
    margin-bottom: 0;
  }

  .left-panel-content {
    height: auto;
  }

  .search-section {
    border-bottom: 1px solid #e0e0e0;
    margin-bottom: 16px;
  }

  .detail-section {
    overflow-y: visible;
    min-height: 300px;
  }

  .map-content {
    height: 500px; /* Fixed height on mobile */
    min-height: 400px;
  }
}

@media (max-width: 600px) {
  .map-content {
    height: 400px;
    min-height: 300px;
  }

  .detail-section {
    min-height: 250px;
  }
}
</style>
