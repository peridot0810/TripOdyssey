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
import { useAttractionStore } from '@/stores/attraction'
import SearchAndDetailPanel from '@/components/map/SearchAndDetailPanel.vue'
import MapCard from '@/components/map/MapCard.vue'

// Store
const attractionStore = useAttractionStore()

// Refs
const searchDetailPanel = ref(null)

// Handle search from SearchAndDetailPanel
const handleSearch = (searchParams) => {
  console.log('Map page received search:', searchParams)

  // The search is already handled by the store via SearchCondition component
  // Here we can add any additional logic needed when search is performed

  // For example, you might want to:
  // 1. Clear any selected location (already done in store)
  // 2. Update map view to show all results
  // 3. Reset map zoom/position

  console.log(`Search completed. Found ${attractionStore.attractions.length} attractions`)
}

// Handle location selection from SearchAndDetailPanel
const handleLocationSelected = (location) => {
  console.log('Location selected in map view:', location)

  // Additional logic when location is selected
  // The store is already updated by the SearchAndDetailPanel

  // You might want to:
  // 1. Center map on selected location
  // 2. Zoom to appropriate level
  // 3. Highlight the selected marker
}

// Handle location click from map (when user clicks a marker)
const handleMapLocationClick = (location) => {
  console.log('Map location clicked:', location)

  // Update the store with selected location
  attractionStore.setSelectedAttraction(location)

  // The SearchAndDetailPanel will automatically update via store reactivity
  // But we can also call the exposed method for any additional coordination
  if (searchDetailPanel.value) {
    // This is now redundant since we're using store, but kept for compatibility
    searchDetailPanel.value.setSelectedLocation(location)
  }
}

// Utility methods that parent components might need
const clearSelection = () => {
  attractionStore.clearSelectedAttraction()
}

const getSearchResults = () => {
  return attractionStore.attractions
}

const getCurrentSelection = () => {
  return attractionStore.selectedAttraction
}

// Expose methods for potential parent components
defineExpose({
  clearSelection,
  getSearchResults,
  getCurrentSelection,
  handleMapLocationClick
})
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
}
</style>
