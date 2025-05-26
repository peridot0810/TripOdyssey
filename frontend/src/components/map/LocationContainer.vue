<template>
  <div class="location-detail-info">
    <div class="detail-header">
      <h3 class="text-h6 font-weight-bold">
        <svg-icon type="mdi" :path="mapMarkerOutlinePath" size="24" class="mr-2" style="color: #1976d2;"></svg-icon>
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
        style="border-radius: 50%;"
      >
        <svg-icon type="mdi" :path="closePath" size="16" style="color: #666;"></svg-icon>
      </v-btn>
    </div>

    <!-- Show selected location -->
    <div v-if="attractionStore.hasSelectedAttraction" class="location-content">
      <LocationInfoCard :locationData="formattedLocationData" />
    </div>

    <!-- Show search results summary when no selection but has results -->
    <div v-else-if="attractionStore.hasAttractions && !attractionStore.isLoading" class="search-results-state">
      <div class="results-summary">
        <svg-icon type="mdi" :path="mapSearchPath" size="48" class="mb-3" style="color: #1976d2;"></svg-icon>
        <h4 class="text-h6 mt-3 mb-2 font-weight-bold">검색 결과</h4>
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
        <p class="text-body-2 text-grey-darken-1 font-weight-medium">관광지 정보를 불러오는 중...</p>
      </div>
    </div>

    <!-- Show error state -->
    <div v-else-if="attractionStore.error" class="error-state">
      <div class="error-content">
        <svg-icon type="mdi" :path="alertCirclePath" size="48" class="mb-3" style="color: #f44336;"></svg-icon>
        <p class="text-body-2 text-error mb-3 font-weight-medium">{{ attractionStore.error }}</p>
        <v-btn
          color="primary"
          variant="outlined"
          size="small"
          @click="attractionStore.clearError()"
          rounded="lg"
          class="font-weight-bold"
          style="border-radius: 12px;"
        >
          닫기
        </v-btn>
      </div>
    </div>

    <!-- Show default empty state -->
    <div v-else class="empty-state">
      <div class="empty-state-content">
        <svg-icon type="mdi" :path="mapSearchOutlinePath" size="64" class="mb-3" style="color: #bdbdbd;"></svg-icon>
        <p class="text-body-1 text-grey mt-3 font-weight-medium">관광지를 검색해주세요</p>
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

// Icon imports
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiMapMarkerOutline,
  mdiClose,
  mdiMapSearch,
  mdiAlertCircle,
  mdiMapSearchOutline
} from '@mdi/js'

// Store
const attractionStore = useAttractionStore()

// Icon paths
const mapMarkerOutlinePath = mdiMapMarkerOutline
const closePath = mdiClose
const mapSearchPath = mdiMapSearch
const alertCirclePath = mdiAlertCircle
const mapSearchOutlinePath = mdiMapSearchOutline

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
  border-radius: 16px;
  overflow: hidden;
}

.detail-header {
  padding: 16px;
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  border-bottom: 2px solid #e3f2fd;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: 16px 16px 0 0;
}

.location-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background-color: #ffffff;
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
  background-color: #ffffff;
}

.empty-state-content,
.loading-content,
.error-content,
.results-summary {
  text-align: center;
  max-width: 280px;
}

.results-summary {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  padding: 32px 24px;
  border-radius: 20px;
  border: 2px solid #e3f2fd;
  box-shadow: 0 8px 20px rgba(25, 118, 210, 0.1);
  transition: all 0.3s ease;
}

.loading-content {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  padding: 32px 24px;
  border-radius: 20px;
  border: 2px solid #f0f0f0;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.error-content {
  background: linear-gradient(135deg, #ffffff 0%, #fef7f7 100%);
  padding: 32px 24px;
  border-radius: 20px;
  border: 2px solid #ffebee;
  box-shadow: 0 8px 20px rgba(244, 67, 54, 0.1);
}

.empty-state-content {
  background: linear-gradient(135deg, #ffffff 0%, #fafafa 100%);
  padding: 32px 24px;
  border-radius: 20px;
  border: 2px solid #f0f0f0;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.05);
}

:deep(.v-btn:active) {
  transform: translateY(0) scale(0.98);
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .detail-header {
    padding: 12px;
    border-radius: 12px 12px 0 0;
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
  .error-content,
  .empty-state-content {
    padding: 24px 20px;
    border-radius: 16px;
    max-width: 100%;
  }

  .location-detail-info {
    border-radius: 12px;
  }
}

/* Custom scrollbar for location content */
.location-content::-webkit-scrollbar {
  width: 6px;
}

.location-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.location-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}
</style>
