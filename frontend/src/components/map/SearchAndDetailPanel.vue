<template>
  <div class="search-and-detail-panel">
    <!-- Top: Search Condition Component -->
    <div class="search-section">
      <SearchCondition @search="handleSearch" />
    </div>

    <!-- Bottom: Location Container Component -->
    <div class="location-section">
      <LocationContainer />
    </div>
  </div>
</template>

<script setup>
import { useAttractionStore } from '@/stores/attraction'
import SearchCondition from '@/components/map/SearchCondition.vue'
import LocationContainer from '@/components/map/LocationContainer.vue'

// Store
const attractionStore = useAttractionStore()

// Handle search from SearchCondition component
const handleSearch = (searchParams) => {
  console.log('Search parameters received in panel:', searchParams)

  // The actual search is now handled by the SearchCondition component
  // which calls the store directly. We just emit to parent for any
  // additional coordination (like map updates)
  emit('search', searchParams)
}

// Define emits
const emit = defineEmits(['search', 'locationSelected'])

// Methods to be called from parent components
const setSelectedLocation = (location) => {
  // Use the store to set selected attraction
  attractionStore.setSelectedAttraction(location)
  emit('locationSelected', location)
}

const clearSelection = () => {
  attractionStore.clearSelectedAttraction()
}

const getAttractions = () => {
  return attractionStore.attractions
}

const getSelectedAttraction = () => {
  return attractionStore.selectedAttraction
}

// Expose methods for parent components
defineExpose({
  setSelectedLocation,
  clearSelection,
  getAttractions,
  getSelectedAttraction
})
</script>

<style scoped>
.search-and-detail-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fafafa;
}

.search-section {
  flex-shrink: 0; /* Don't shrink the search section */
  padding: 16px;
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
}

.location-section {
  flex: 1; /* Take remaining space */
  min-height: 0; /* Important for proper flex behavior */
  overflow: hidden; /* Let LocationContainer handle its own scrolling */
}

/* Responsive adjustments */
@media (max-width: 960px) {
  .search-and-detail-panel {
    height: auto;
  }

  .search-section {
    padding: 12px;
  }

  .location-section {
    flex: none;
    min-height: 300px;
  }
}

@media (max-width: 600px) {
  .search-section {
    padding: 8px;
  }

  .location-section {
    min-height: 250px;
  }
}
</style>
