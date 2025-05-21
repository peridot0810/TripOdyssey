<template>
  <v-card class="tourism-card pa-5 rounded-lg" elevation="3">
    <div class="d-flex align-center mb-4">
      <v-icon size="large" color="primary" class="mr-3">mdi-map-search</v-icon>
      <h2 class="text-h5 font-weight-bold mb-0 primary--text">관광지 정보 검색</h2>
    </div>

    <v-divider class="mb-4"></v-divider>

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
        />

        <v-select
          v-model="selectedGugun"
          :items="filteredGuguns"
          item-title="name"
          item-value="code"
          label="시군구 선택"
          :disabled="!selectedSido"
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
          class="rounded-lg search-field mb-4"
          prepend-inner-icon="mdi-tag-multiple"
          hide-details
        />

        <v-btn
          color="primary"
          @click="handleSearch"
          block
          class="search-button rounded-lg py-3"
          elevation="2"
          :disabled="!selectedSido || !selectedContentType"
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
        <p class="text-caption text-grey-darken-1 mb-0">시도와 관광타입을 선택하여 원하는 관광지를 찾아보세요. 더 정확한 결과를 위해 시군구도 함께 선택하시면 좋습니다.</p>
      </div>
    </v-expand-transition>
  </v-card>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import {
  contentTypes,
  sidoList,
  gugunList,
} from '@/data/map/searchOptions'

const selectedSido = ref('')
const selectedGugun = ref('')
const selectedContentType = ref('')
const showTips = ref(true)

const filteredGuguns = computed(() =>
  gugunList.filter((g) => g.sidoCode === selectedSido.value)
)

// Reset gugun when sido changes
watch(selectedSido, () => {
  selectedGugun.value = ''
})

const handleSearch = () => {
  console.log('시도:', selectedSido.value)
  console.log('시군구:', selectedGugun.value)
  console.log('관광타입:', selectedContentType.value)
  showTips.value = false
}
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
