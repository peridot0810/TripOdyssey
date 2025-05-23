<template>
  <div class="transportation-list">
    <h2 class="text-h5 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-car-multiple</v-icon>
      교통편 목록
    </h2>

    <div v-if="transportationData.length === 0" class="empty-state text-center py-8">
      <v-icon size="x-large" color="grey">mdi-car-off</v-icon>
      <p class="text-body-1 text-grey-darken-1 mt-3">등록된 교통편이 없습니다</p>
    </div>

    <div v-else class="transportation-cards">
      <div
        v-for="transportation in transportationData"
        :key="transportation.transportationId"
        class="transportation-card-wrapper mb-4"
      >
        <TransportationCard :transportation="transportation" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import TransportationCard from '@/components/transportation/TransportationCard.vue'

// Mock transportation data
const transportationData = ref([
  {
    transportationId: '1',
    typeId: '1',
    departure: '09:30',
    arrival: '11:45',
    from: '인천국제공항',
    to: '제주국제공항',
    description: 'KE1201 · 대한항공 · 이코노미클래스 · 좌석 12A',
  },
  {
    transportationId: '2',
    typeId: '2',
    departure: '14:20',
    arrival: '16:55',
    from: '서울역',
    to: '부산역',
    description: 'KTX-158 · 일반실 · 3호차 15번',
  },
  {
    transportationId: '3',
    typeId: '3',
    departure: '08:00',
    arrival: '12:30',
    from: '강남고속터미널',
    to: '경주고속터미널',
    description: '동양고속 · 우등고속버스 · 좌석 7A',
  },
  {
    transportationId: '4',
    typeId: '6',
    departure: '10:00',
    arrival: '18:00',
    from: '제주렌터카',
    to: '제주렌터카',
    description: '현대 아반떼 · 2024년식 · 연료비 별도 · 보험 포함',
  },
])
</script>

<style scoped>
.transportation-list {
  padding: 16px;
}

.empty-state {
  border-radius: 8px;
  background-color: #f5f5f5;
}

.transportation-cards {
  max-width: 100%;
}

.transportation-card-wrapper {
  max-width: 100%;
}
</style>
