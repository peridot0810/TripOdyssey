<template>
  <div class="location-detail-info">
    <div class="detail-header">
      <h3 class="text-h6 font-weight-bold">
        <v-icon size="large" color="primary" class="mr-2">mdi-map-marker-outline</v-icon>
        장소 정보
      </h3>
    </div>

    <!-- Show selected location or empty state -->
    <div v-if="selectedLocation" class="location-content">
      <LocationInfoCard :locationData="selectedLocation" />
    </div>

    <div v-else class="empty-state">
      <div class="empty-state-content">
        <v-icon size="64" color="grey-lighten-1">mdi-map-search-outline</v-icon>
        <p class="text-body-1 text-grey mt-3">장소를 선택해주세요</p>
        <p class="text-caption text-grey-darken-1">
          지도에서 장소를 클릭하면 상세 정보가 표시됩니다
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import LocationInfoCard from './LocationInfoCard.vue'

// Mock selected location - replace with actual state management
const selectedLocation = ref({
  attractionInfo: {
    no: 56649,
    title: '국립국악원',
    contentTypeName: '문화시설',
    firstImage1:
      'https://fastly.picsum.photos/id/13/2500/1667.jpg?hmac=SoX9UoHhN8HyklRA4A3vcCWJMVtiBXUg0W4ljWTor7s',
    firstImage2: '',
    latitude: 37.4782,
    longitude: 127.0089,
    tel: '02-580-3300',
    addr1: '서울특별시 서초구 남부순환로 2364 (서초동)',
    addr2: '',
    homepage: '',
    overview: '한국 전통음악과 무용 공연을 정기적으로 개최하는 국가 대표 국악 전용 공연장입니다.',
  },
})

// You can add props or emit events here to connect with parent components
const props = defineProps({
  location: {
    type: Object,
    default: null,
  },
})

// Use prop data if provided, otherwise use mock data
if (props.location) {
  selectedLocation.value = props.location
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
}

.location-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 16px;
}

.empty-state-content {
  text-align: center;
  max-width: 250px;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .detail-header {
    padding: 12px;
  }

  .location-content {
    padding: 12px;
  }

  .empty-state {
    padding: 24px 12px;
  }
}
</style>
