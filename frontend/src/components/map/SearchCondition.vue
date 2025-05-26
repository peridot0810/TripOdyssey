<template>
  <v-card class="tourism-card pa-4 rounded-lg" elevation="2" style="border: 2px solid #3d9fff;">
    <div class="d-flex align-center mb-3">
      <svg-icon type="mdi" :path="mapSearchPath" size="24" class="mr-2" style="color: #1976d2;"></svg-icon>
      <h3 class="text-h6 font-weight-bold mb-0 primary--text">관광지 정보 검색</h3>
    </div>

    <!-- Error Alert -->
    <v-alert
      v-if="attractionStore.error"
      type="error"
      class="mb-3"
      closable
      @click:close="attractionStore.clearError()"
    >
      {{ attractionStore.error }}
    </v-alert>

    <!-- Row 1: Three Dropdowns -->
    <v-row class="mb-2">
      <v-col cols="12" sm="4">
        <v-select
          v-model="selectedSido"
          :items="sidoList"
          item-title="name"
          item-value="code"
          label="시도 선택"
          variant="outlined"
          density="compact"
          hide-details
          :disabled="attractionStore.isLoading"
          rounded="lg"
          style="border-radius: 12px;"
        >
          <template v-slot:prepend-inner>
            <svg-icon type="mdi" :path="cityPath" size="18" style="color: #666;"></svg-icon>
          </template>
        </v-select>
      </v-col>

      <v-col cols="12" sm="4">
        <v-select
          v-model="selectedGugun"
          :items="filteredGuguns"
          item-title="name"
          item-value="code"
          label="시군구 선택"
          :disabled="!selectedSido || attractionStore.isLoading"
          variant="outlined"
          density="compact"
          hide-details
          rounded="lg"
          style="border-radius: 12px;"
        >
          <template v-slot:prepend-inner>
            <svg-icon type="mdi" :path="mapMarkerPath" size="18" style="color: #666;"></svg-icon>
          </template>
        </v-select>
      </v-col>

      <v-col cols="12" sm="4">
        <v-select
          v-model="selectedContentType"
          :items="contentTypes"
          item-title="name"
          item-value="code"
          label="관광타입 선택"
          variant="outlined"
          density="compact"
          hide-details
          :disabled="attractionStore.isLoading"
          rounded="lg"
          style="border-radius: 12px;"
        >
          <template v-slot:prepend-inner>
            <svg-icon type="mdi" :path="tagMultiplePath" size="18" style="color: #666;"></svg-icon>
          </template>
        </v-select>
      </v-col>
    </v-row>

    <!-- Row 2: Search Input + Button -->
    <v-row class="align-center">
      <v-col cols="12" sm="9">
        <v-text-field
          v-model="keyword"
          label="검색 키워드 (선택사항)"
          variant="outlined"
          density="compact"
          hide-details
          :disabled="attractionStore.isLoading"
          placeholder="예: 허브, 공원, 박물관..."
          @keyup.enter="handleSearch"
          rounded="lg"
          style="border-radius: 12px;"
        >
          <template v-slot:prepend-inner>
            <svg-icon type="mdi" :path="magnifyPath" size="18" style="color: #666;"></svg-icon>
          </template>
        </v-text-field>
      </v-col>

      <v-col cols="12" sm="3">
        <v-btn
  color="primary"
  @click="handleSearch"
  block
  elevation="3"
  :loading="attractionStore.isLoading"
  size="large"
  rounded="lg"
  class="font-weight-bold text-none"
  style="
    height: 48px;
    border-radius: 12px;
    letter-spacing: 0.5px;
    box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3);
    transition: all 0.2s ease;
    display: flex !important;
    align-items: center !important;
    justify-content: center !important;
    text-align: center !important;
  "
>
  <svg-icon type="mdi" :path="magnifyPath" size="20" class="mr-2" style="color: white;"></svg-icon>
  검색
</v-btn>
      </v-col>
    </v-row>

    <!-- Results Summary -->
    <v-expand-transition>
      <div v-if="attractionStore.hasAttractions && !attractionStore.isLoading" class="mt-3 pt-2">
        <v-divider class="mb-2"></v-divider>
        <div class="d-flex align-center">
          <svg-icon type="mdi" :path="checkCirclePath" size="16" class="mr-2" style="color: #4caf50;"></svg-icon>
          <span class="text-caption text-success font-weight-medium">
            {{ attractionStore.attractions.length }}개의 관광지를 찾았습니다
          </span>
        </div>
      </div>
    </v-expand-transition>
  </v-card>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useAttractionStore } from '@/stores/attraction'
import {
  contentTypes,
  sidoList,
  gugunList,
} from '@/data/map/searchOptions'

// Icon imports
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiMapSearch,
  mdiCity,
  mdiMapMarker,
  mdiTagMultiple,
  mdiMagnify,
  mdiCheckCircle
} from '@mdi/js'

// Store
const attractionStore = useAttractionStore()

// Icon paths
const mapSearchPath = mdiMapSearch
const cityPath = mdiCity
const mapMarkerPath = mdiMapMarker
const tagMultiplePath = mdiTagMultiple
const magnifyPath = mdiMagnify
const checkCirclePath = mdiCheckCircle

// Form data
const selectedSido = ref('')
const selectedGugun = ref('')
const selectedContentType = ref('')
const keyword = ref('')

// Computed
const filteredGuguns = computed(() =>
  gugunList.filter((g) => g.sidoCode === selectedSido.value)
)

// Watch for sido changes to reset gugun
watch(selectedSido, () => {
  selectedGugun.value = ''
})

// Methods
const handleSearch = async () => {
  if (!selectedSido.value || !selectedContentType.value) {
    return
  }

  const searchData = {
    selectedSido: selectedSido.value,
    selectedGugun: selectedGugun.value,
    selectedContentType: selectedContentType.value,
    keyword: keyword.value?.trim() || undefined
  }

  console.log('Initiating search with:', searchData)

  // Call store action to search attractions
  await attractionStore.searchAttractions(searchData)

  // Emit search event to parent if needed
  emit('search', searchData)
}

// Emits
const emit = defineEmits(['search'])
</script>

<style scoped>
.tourism-card {
  border-radius: 16px;
  transition: all 0.3s ease;
  border: 1px solid #e0e0e0;
}

.v-btn:active:not(:disabled) {
  transform: translateY(0) scale(0.98) !important;
}

/* Input field styling */
:deep(.v-field--variant-outlined .v-field__outline) {
  border-radius: 12px;
}

:deep(.v-field--variant-outlined .v-field__outline__start) {
  border-radius: 12px 0 0 12px;
}

:deep(.v-field--variant-outlined .v-field__outline__end) {
  border-radius: 0 12px 12px 0;
}

/* Mobile responsiveness */
@media (max-width: 599px) {
  .v-btn {
    margin-top: 8px;
  }

  .tourism-card {
    border-radius: 12px;
  }
}
</style>
