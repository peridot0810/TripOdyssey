<template>
  <div>
    <KakaoMap
      height="90vh"
      width="75vw"
      :lat="coordinate.lat"
      :lng="coordinate.lng"
      :draggable="true"
    >
      <KakaoMapMarker :lat="coordinate.lat" :lng="coordinate.lng"></KakaoMapMarker>
    </KakaoMap>
  </div>
</template>

<script setup>
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps'
const coordinate = {
  lat: 33.450701,
  lng: 126.570667,
}
</script>

<style scoped></style>
