<template>
  <div class="location-detail-info">
    <div class="detail-header">
      <h3 class="text-h6 font-weight-bold">
        <v-icon size="large" color="primary" class="mr-2">mdi-map-marker-outline</v-icon>
        장소 정보
      </h3>

      <!-- Clear selection button when attraction is selected -->
      <v-btn
        v-if="attractionStore.hasSelectedAttraction"
        icon
        size="small"
        variant="text"
        @click="attractionStore.clearSelectedAttraction()"
        class="ml-2"
      >
        <v-icon size="small">mdi-close</v-icon>
      </v-btn>
    </div>

    <!-- Show selected location -->
    <div v-if="attractionStore.hasSelectedAttraction" class="location-content">
      <LocationInfoCard :locationData="formattedLocationData" />
    </div>

    <!-- Show search results summary when no selection but has results -->
    <div v-else-if="attractionStore.hasAttractions && !attractionStore.isLoading" class="search-results-state">
      <div class="results-summary">
        <v-icon size="48" color="primary">mdi-map-search</v-icon>
        <h4 class="text-h6 mt-3 mb-2">검색 결과</h4>
        <p class="text-body-2 text-grey-darken-1 mb-3">
          {{ attractionStore.attractions.length }}개의 관광지를 찾았습니다
        </p>
        <p class="text-caption text-grey">
          지도에서 마커를 클릭하면 상세 정보가 표시됩니다
        </p>
      </div>
    </div>

    <!-- Show loading state -->
    <div v-else-if="attractionStore.isLoading" class="loading-state">
      <div class="loading-content">
        <v-progress-circular
          indeterminate
          color="primary"
          size="48"
          class="mb-3"
        ></v-progress-circular>
        <p class="text-body-2 text-grey-darken-1">관광지 정보를 불러오는 중...</p>
      </div>
    </div>

    <!-- Show error state -->
    <div v-else-if="attractionStore.error" class="error-state">
      <div class="error-content">
        <v-icon size="48" color="error" class="mb-3">mdi-alert-circle</v-icon>
        <p class="text-body-2 text-error mb-3">{{ attractionStore.error }}</p>
        <v-btn
          color="primary"
          variant="outlined"
          size="small"
          @click="attractionStore.clearError()"
        >
          닫기
        </v-btn>
      </div>
    </div>

    <!-- Show default empty state -->
    <div v-else class="empty-state">
      <div class="empty-state-content">
        <v-icon size="64" color="grey-lighten-1">mdi-map-search-outline</v-icon>
        <p class="text-body-1 text-grey mt-3">관광지를 검색해주세요</p>
        <p class="text-caption text-grey-darken-1">
          위의 검색 조건을 설정하고 검색 버튼을 클릭하세요
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAttractionStore } from '@/stores/attraction'
import LocationInfoCard from './LocationInfoCard.vue'

// Store
const attractionStore = useAttractionStore()

// Format the selected attraction data to match LocationInfoCard expected format
const formattedLocationData = computed(() => {
  if (!attractionStore.selectedAttraction) return null

  return {
    attractionInfo: {
      ...attractionStore.selectedAttraction
    }
  }
})

// Props (kept for backward compatibility, but now using store primarily)
const props = defineProps({
  location: {
    type: Object,
    default: null,
  },
})

// If location is passed via props, use it (for compatibility)
// But store data takes precedence
if (props.location && !attractionStore.selectedAttraction) {
  attractionStore.setSelectedAttraction(props.location.attractionInfo)
}
</script>

<style scoped>
.location-detail-info {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fafafa;
}

.detail-header {
  padding: 16px;
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.location-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.empty-state,
.loading-state,
.error-state,
.search-results-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 16px;
}

.empty-state-content,
.loading-content,
.error-content,
.results-summary {
  text-align: center;
  max-width: 250px;
}

.results-summary {
  background-color: white;
  padding: 24px;
  border-radius: 12px;
  border: 1px solid #e3f2fd;
}

.loading-content {
  background-color: white;
  padding: 24px;
  border-radius: 12px;
  border: 1px solid #f0f0f0;
}

.error-content {
  background-color: white;
  padding: 24px;
  border-radius: 12px;
  border: 1px solid #ffebee;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .detail-header {
    padding: 12px;
  }

  .location-content {
    padding: 12px;
  }

  .empty-state,
  .loading-state,
  .error-state,
  .search-results-state {
    padding: 24px 12px;
  }

  .results-summary,
  .loading-content,
  .error-content {
    padding: 20px;
  }
}
</style>
