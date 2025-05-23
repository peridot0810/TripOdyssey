<template>
  <div class="accommodation-list">
    <h2 class="text-h5 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-bed</v-icon>
      숙소 목록
    </h2>

    <div v-if="accommodationData.length === 0" class="empty-state text-center py-8">
      <v-icon size="x-large" color="grey">mdi-home-off</v-icon>
      <p class="text-body-1 text-grey-darken-1 mt-3">예약된 숙소가 없습니다</p>
    </div>

    <div v-else class="accommodation-cards">
      <div
        v-for="accommodation in accommodationData"
        :key="accommodation.accommodationId"
        class="accommodation-card-wrapper mb-4"
      >
        <AccommodationCard :accommodation="accommodation" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import AccommodationCard from './AccommodationCard.vue'

// Mock accommodation data
const accommodationData = ref([
  {
    accommodationId: 1,
    groupId: 1,
    attractionsNo: 79900,
    name: '강남 비즈니스 호텔',
    checkInTime: '2025-06-06 15:00:00',
    checkOutTime: '2025-06-07 11:00:00',
    accommodationInfo: {
      no: 79900,
      title: '그랜드 서울 호텔',
      contentTypeName: '숙박',
      firstImage1: '', // You can add image URL here
      firstImage2: '',
      latitude: 37.5329878783,
      longitude: 127.0873259497,
      tel: '02-1234-5678',
      addr1: '서울특별시 강남구 테헤란로 123',
      addr2: '(역삼동)',
      homepage: '<a href="https://grandseoul.co.kr" target="_blank">https://grandseoul.co.kr</a>',
      overview:
        '강남 중심가에 위치한 프리미엄 비즈니스 호텔입니다. 지하철역과 인접하여 교통이 편리하며, 각종 편의시설과 비즈니스 센터를 완비하고 있습니다. 객실은 현대적이고 세련된 인테리어로 꾸며져 있으며, 24시간 룸서비스와 컨시어지 서비스를 제공합니다.',
    },
  },
  {
    accommodationId: 2,
    groupId: 1,
    attractionsNo: 79805,
    name: '홍대 아늑한 게스트하우스',
    checkInTime: '2025-06-08 16:00:00',
    checkOutTime: '2025-06-10 10:00:00',
    accommodationInfo: {
      no: 79805,
      title: '홍대 백패커 게스트하우스',
      contentTypeName: '숙박',
      firstImage1: '', // You can add image URL here
      firstImage2: '',
      latitude: 37.5563,
      longitude: 126.9226,
      tel: '010-9876-5432',
      addr1: '서울특별시 마포구 와우산로 29길 15',
      addr2: '(서교동)',
      homepage: null,
      overview:
        '홍대 클럽가 중심에 위치한 아늑한 게스트하우스입니다. 젊은 여행객들에게 인기가 많으며, 깔끔하고 안전한 숙박 환경을 제공합니다. 공용 라운지와 주방 시설이 있어 다른 여행객들과 소통할 수 있는 공간이 마련되어 있습니다.',
    },
  },
])
</script>

<style scoped>
.accommodation-list {
  padding: 16px;
}

.empty-state {
  border-radius: 8px;
  background-color: #f5f5f5;
}

.accommodation-cards {
  max-width: 100%;
}

.accommodation-card-wrapper {
  max-width: 100%;
}
</style>
