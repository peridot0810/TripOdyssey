<template>
  <div class="proposal-list">
    <h2 class="text-h5 font-weight-bold mb-4 d-flex align-center">
      <v-icon size="large" color="primary" class="mr-2">mdi-map-marker-multiple</v-icon>
      추천 관광지 목록
    </h2>

    <div v-if="proposedLocations.length === 0" class="empty-state text-center py-8">
      <v-icon size="x-large" color="grey">mdi-map-search</v-icon>
      <p class="text-body-1 text-grey-darken-1 mt-3">추천된 장소가 없습니다</p>
    </div>

    <div v-else class="location-cards">
      <div
        v-for="location in proposedLocations"
        :key="location.no"
        class="location-card-wrapper mb-4"
      >
        <ProposalCard :location="location" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import ProposalCard from '@/components/schedule/ProposalCard.vue';

// Mock data from the server response
const proposedLocations = ref([
  {
    "userId": "user05",
    "proposalId": 1,
    "groupId": 1,
    "no": 56644,
    "title": "가회동성당",
    "likes": 5,
    "contentTypeName": "관광지",
    "firstImage1": "http://tong.visitkorea.or.kr/cms/resource/09/3303909_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/09/3303909_image3_1.jpg",
    "latitude": 37.5820858828,
    "longitude": 126.9846616856,
    "tel": "",
    "addr1": "서울특별시 종로구 북촌로 57 (가회동)",
    "addr2": "",
    "homepage": "<a href=\"https://gahoe.or.kr\" target=\"_blank\" title=\"새창 : 가회동성당 사이트로 이동\">https://gahoe.or.kr</a>",
    "overview": "가회동성당이 위치한 북촌 일대는 최초의 선교사 주문모(周文謨, 야고보) 신부가 조선에 밀입국하여 1795년 4월 5일 부활 대축일에 최인길(崔仁吉, 마티아)의 집에서 조선 땅에서의 '첫 미사'를 집전한 지역이다. 본당 관할구역은 주문모 신부가 강완숙(姜完淑, 골롬바)의 집에 숨어 지내면서 사목활동을 펼쳤던 지역으로서 한국 교회사에서 매우 중요한 의미가 있다.정식으로 본당이 된 것은 1949년이고, 이후 1954년에 성전이 완공되었다. 하지만 성전이 낡아 2011년부터 옛 성전을 허물고 현재의 새 성전을 짓게 되었다. 2013년 11월 21일 준공되었고, 준공 3일 후인 24일(그리스도 왕 대축일)에 입주하여 입주 미사를 봉헌하였다. 현재의 동서양 건축양식이 어우러진 새 성전은 과거의 역사를 되살리고자 2014년 4월 20일 부활 대축일에 서울교구장 염수정 추기경님에 의하여 축성되었다.(출처 : 가회동성당 홈페이지)"
  },
  {
    "userId": "user02",
    "proposalId": 4,
    "groupId": 1,
    "no": 56645,
    "title": "간데메공원",
    "likes": null,
    "contentTypeName": "관광지",
    "firstImage1": "",
    "firstImage2": "",
    "latitude": 37.5728353382,
    "longitude": 127.0490783005,
    "tel": "",
    "addr1": "서울특별시 동대문구 서울시립대로2길 59 (답십리동)",
    "addr2": "",
    "homepage": "",
    "overview": "간데메공원은 서울 동대문구 답십리 천변에 위치한 근린공원이다. 어린이 놀이터와 연못, 팔각정 정자, 장미 넝쿨 터널, 소나무 쉼터, 맨발산책로까지 작지만 아기자기하게 조성된 공원이다. 농구장과 배드민턴장도 있다. 전매청 자리를 공원으로 조성하면서 옛날 답십리 일대 간데메(中山) 자연 부락 마을의 토박이 이름을 따서 간데메공원이라 이름을 정했다고 한다."
  },
  {
    "userId": "user02",
    "proposalId": 5,
    "groupId": 1,
    "no": 56646,
    "title": "갈산근린공원",
    "likes": null,
    "contentTypeName": "관광지",
    "firstImage1": "http://tong.visitkorea.or.kr/cms/resource/90/3467490_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/90/3467490_image3_1.jpg",
    "latitude": 37.5061176314,
    "longitude": 126.8684105358,
    "tel": "",
    "addr1": "서울특별시 양천구 신정동 162-56",
    "addr2": "",
    "homepage": "<a href=\"http://www.yangcheon.go.kr/\" target=\"_blank\" title=\"새창: 양천구 \">http://www.yangcheon.go.kr/</a>",
    "overview": "대체로 산세가 둥근 양천구의 다른 산들과는 달리 갈산은 동쪽 부분이 벼랑처럼 깎인 절경을 뽐낸다. 안양천의 오랜 침식작용으로 산 정상이 칼날처럼 변한 것으로 '칼산'이라는 이름으로 불리다가 '갈산'으로 순화되었다. 갈산 입구에는 어린이 교통공원이 들어서 있고, 중턱에는 중심광장과 산책로 등의 쉼터 그리고 정상에는 전망대가 있어 양천을 한눈에 감상할 수 있다. 갈산 정상에는 '갈산정'과 함께 그 옆으로 우리나라 최초의 '대삼각본점'이 설치되어 있다. 이 삼각점은 모든 측량의 기준이 되는 중요한 국가시설물로 1910년 6월 우리나라 최초로 토지조사 사업을 위해 설치한 측량기준점으로 서울시 미래유산으로 지정된 양천구의 중요한 문화유산이다."
  },
  {
    "userId": "user02",
    "proposalId": 6,
    "groupId": 1,
    "no": 56647,
    "title": "감로암(서울)",
    "likes": null,
    "contentTypeName": "관광지",
    "firstImage1": "http://tong.visitkorea.or.kr/cms/resource/22/3481922_image2_1.jpg",
    "firstImage2": "http://tong.visitkorea.or.kr/cms/resource/22/3481922_image3_1.jpg",
    "latitude": 37.5753815016,
    "longitude": 127.0066515383,
    "tel": "",
    "addr1": "서울특별시 종로구 율곡로23길 16 (충신동)",
    "addr2": "",
    "homepage": "",
    "overview": "동대문에서 대학로로 들어가는 율곡로 대로변에 자그마한 사찰이 있다. 그 이름도 특이하게 목마른 중생에게 구제의 감로를 준다는 '감로암'이다. 도심 속 삭막한 빌딩 숲 사이에 아직 개발되지 않은 주택가에 정말 도심에서 보기 힘든 자그마한 암자가 있다. 감로암의 역사는 벌써 100여 년이 흘렀다. 대로변에서 주택가로 나 있는 골목길을 따라 들어서면 보이는 곳에 여느 주택과 별반 차이 없이 건물을 지어 불상을 봉안하고 예배하고 있다. 하지만 여법하게 산문을 만들고 단청을 하며, 기와를 엮어 고풍스러운 가람의 멋을 느끼게 한다. 예부터 관음영험도량으로 알려진 감로암은 동네 어르신들, 특히 할머니들의 다담방, 기도처로 알려져 있다. 감로암은 사찰의 이름처럼 누군가 구제받을 수 있고, 고통에서 벗어날 수 있는 감로가 있는 곳이다. 지금은 마시지 못하지만 보타전 아래에는 여전히 그 옛날 감로의 맛을 느끼게 했던 감로가 흐르고 있다."
  }
]);
</script>

<style scoped>
.proposal-list {
  padding: 16px;
}

.empty-state {
  border-radius: 8px;
  background-color: #f5f5f5;
}

.location-cards {
  max-width: 100%;
}

.location-card-wrapper {
  max-width: 100%;
}
</style>
