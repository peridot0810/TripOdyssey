<template>
  <div class="map-container">
    <KakaoMap
      :height="mapHeight"
      :width="mapWidth"
      :lat="lat"
      :lng="lng"
      :level="level"
      :draggable="true"
      @onLoadKakaoMap="onLoadKakaoMap"
    >
      <!-- Markers from attraction search results -->
      <KakaoMapMarker
        v-for="attraction in attractionStore.attractions"
        :key="attraction.no"
        :lat="parseFloat(attraction.latitude)"
        :lng="parseFloat(attraction.longitude)"
        :clickable="true"
        @onClickKakaoMapMarker="() => handleMarkerClick(attraction)"
      />
    </KakaoMap>

    <!-- Debug info -->
    <div class="debug-info">
      <p>Attractions: {{ attractionStore.attractions.length }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps'
import { useAttractionStore } from '@/stores/attraction'

// Store
const attractionStore = useAttractionStore()

// Map settings
const mapHeight = ref('100%')
const mapWidth = ref('100%')
const lat = ref(37.5665)
const lng = ref(126.9780)
const level = ref(8)
const map = ref()
let bounds = null

// Map load handler
const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef
  bounds = new window.kakao.maps.LatLngBounds()

  if (attractionStore.attractions.length > 0) {
    setBoundsForAttractions()
  }
}

// Function to set map bounds based on attractions
const setBoundsForAttractions = () => {
  if (!map.value || !bounds || attractionStore.attractions.length === 0) return

  bounds = new window.kakao.maps.LatLngBounds()
  let validPointsCount = 0

  attractionStore.attractions.forEach((attraction) => {
    const lat = parseFloat(attraction.latitude)
    const lng = parseFloat(attraction.longitude)

    if (!isNaN(lat) && !isNaN(lng) &&
        lat >= 33 && lat <= 39 && lng >= 124 && lng <= 132) {

      const point = new window.kakao.maps.LatLng(lat, lng)
      bounds.extend(point)
      validPointsCount++
    }
  })

  if (validPointsCount === 0) return

  if (validPointsCount === 1) {
    const firstAttraction = attractionStore.attractions.find(a => {
      const lat = parseFloat(a.latitude)
      const lng = parseFloat(a.longitude)
      return !isNaN(lat) && !isNaN(lng) && lat >= 33 && lat <= 39 && lng >= 124 && lng <= 132
    })

    if (firstAttraction) {
      const center = new window.kakao.maps.LatLng(parseFloat(firstAttraction.latitude), parseFloat(firstAttraction.longitude))
      map.value.setCenter(center)
      map.value.setLevel(5)
      return
    }
  }

  map.value.setBounds(bounds)

  setTimeout(() => {
    const currentLevel = map.value.getLevel()
    if (currentLevel > 10) {
      map.value.setLevel(10)
    }
  }, 100)
}

// Watch for changes in attractions
watch(
  () => attractionStore.attractions,
  (newAttractions) => {
    if (newAttractions.length === 0) {
      lat.value = 37.5665
      lng.value = 126.9780
      level.value = 8
    } else {
      setBoundsForAttractions()
    }
  },
  { immediate: true, deep: true }
)

// Handle marker click - update selected attraction in store
const handleMarkerClick = (attraction) => {
  console.log('Clicked attraction:', attraction.no, attraction.title)

  // Update the store with selected attraction
  // This will automatically update the LocationContainer component
  attractionStore.setSelectedAttraction(attraction)
}
</script>

<style scoped>
.map-container {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.map-container :deep(.kakao-map) {
  width: 100% !important;
  height: 100% !important;
}

.debug-info {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(255, 255, 255, 0.9);
  padding: 8px;
  border-radius: 4px;
  font-size: 12px;
  z-index: 1000;
}
</style>
