<template>
  <div class="search-and-detail-panel">
    <!-- Top: Search Condition Component -->
    <div class="search-section">
      <SearchCondition @search="handleSearch" />
    </div>

    <!-- Bottom: Location Container Component -->
    <div class="location-section">
      <LocationContainer :location="selectedLocation" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import SearchCondition from '@/components/map/SearchCondition.vue'
import LocationContainer from '@/components/map/LocationContainer.vue'

// State for selected location
const selectedLocation = ref(null)

// Handle search from SearchCondition component
const handleSearch = (searchParams) => {
  console.log('Search parameters:', searchParams)

  // Here you would typically:
  // 1. Make API call with search parameters
  // 2. Update map with search results
  // 3. Reset selected location
  selectedLocation.value = null

  // Emit search event to parent (map page) if needed
  emit('search', searchParams)
}

// Define emits
const emit = defineEmits(['search', 'locationSelected'])

// Methods to be called from parent components
const setSelectedLocation = (location) => {
  selectedLocation.value = location
  emit('locationSelected', location)
}

// Expose methods for parent components
defineExpose({
  setSelectedLocation,
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
