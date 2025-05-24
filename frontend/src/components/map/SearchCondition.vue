<template>
  <v-card class="tourism-card pa-5 rounded-lg" elevation="3">
    <div class="d-flex align-center mb-4">
      <v-icon size="large" color="primary" class="mr-3">mdi-map-search</v-icon>
      <h2 class="text-h5 font-weight-bold mb-0 primary--text">관광지 정보 검색</h2>
    </div>

    <v-divider class="mb-4"></v-divider>

    <!-- Error Alert -->
    <v-alert
      v-if="attractionStore.error"
      type="error"
      class="mb-4"
      closable
      @click:close="attractionStore.clearError()"
    >
      {{ attractionStore.error }}
    </v-alert>

    <v-row>
      <v-col cols="12" md="8" lg="6" class="mx-auto">
        <v-select
          v-model="selectedSido"
          :items="sidoList"
          item-title="name"
          item-value="code"
          label="시도 선택"
          variant="outlined"
          density="comfortable"
          class="rounded-lg search-field mb-3"
          prepend-inner-icon="mdi-city"
          hide-details
          :disabled="attractionStore.isLoading"
        />

        <v-select
          v-model="selectedGugun"
          :items="filteredGuguns"
          item-title="name"
          item-value="code"
          label="시군구 선택"
          :disabled="!selectedSido || attractionStore.isLoading"
          variant="outlined"
          density="comfortable"
          class="rounded-lg search-field mb-3"
          prepend-inner-icon="mdi-map-marker"
          hide-details
        />

        <v-select
          v-model="selectedContentType"
          :items="contentTypes"
          item-title="name"
          item-value="code"
          label="관광타입 선택"
          variant="outlined"
          density="comfortable"
          class="rounded-lg search-field mb-3"
          prepend-inner-icon="mdi-tag-multiple"
          hide-details
          :disabled="attractionStore.isLoading"
        />

        <!-- Optional Keyword Field -->
        <v-text-field
          v-model="keyword"
          label="검색 키워드 (선택사항)"
          variant="outlined"
          density="comfortable"
          class="rounded-lg search-field mb-4"
          prepend-inner-icon="mdi-magnify"
          hide-details
          :disabled="attractionStore.isLoading"
          placeholder="예: 허브, 공원, 박물관..."
        />

        <v-btn
          color="primary"
          @click="handleSearch"
          block
          class="search-button rounded-lg py-3"
          elevation="2"
          :disabled="!selectedSido || !selectedContentType || attractionStore.isLoading"
          :loading="attractionStore.isLoading"
        >
          <v-icon class="mr-1">mdi-magnify</v-icon>
          관광지 조회
        </v-btn>
      </v-col>
    </v-row>

    <v-expand-transition>
      <div v-if="showTips" class="mt-4 pt-3 search-tips">
        <v-divider class="mb-3"></v-divider>
        <div class="d-flex align-center mb-2">
          <v-icon color="grey darken-1" size="small" class="mr-2">mdi-information</v-icon>
          <span class="text-caption text-grey-darken-1 font-weight-medium">검색 도움말</span>
        </div>
        <p class="text-caption text-grey-darken-1 mb-0">
          시도와 관광타입을 선택하여 원하는 관광지를 찾아보세요. 더 정확한 결과를 위해 시군구와 키워드도 함께 입력하시면 좋습니다.
        </p>
      </div>
    </v-expand-transition>

    <!-- Results Summary -->
    <v-expand-transition>
      <div v-if="attractionStore.hasAttractions && !attractionStore.isLoading" class="mt-4 pt-3">
        <v-divider class="mb-3"></v-divider>
        <div class="d-flex align-center">
          <v-icon color="success" size="small" class="mr-2">mdi-check-circle</v-icon>
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

// Store
const attractionStore = useAttractionStore()

// Form data
const selectedSido = ref('')
const selectedGugun = ref('')
const selectedContentType = ref('')
const keyword = ref('')
const showTips = ref(true)

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

  // Hide tips after first search
  showTips.value = false

  // Emit search event to parent if needed
  emit('search', searchData)
}

// Emits
const emit = defineEmits(['search'])
</script>

<style scoped>
.tourism-card {
  max-width: 1000px;
  margin: auto;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.tourism-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1) !important;
}

.search-field {
  transition: all 0.2s ease;
}

.search-field:hover {
  transform: translateY(-2px);
}

.search-button {
  font-weight: 600;
  letter-spacing: 0.5px;
  text-transform: none;
  transition: all 0.3s ease;
}

.search-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15) !important;
}

.search-tips {
  border-radius: 8px;
  transition: all 0.3s ease;
}
</style>
