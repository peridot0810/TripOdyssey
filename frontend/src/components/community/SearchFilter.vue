<template>
  <div class="search-filter">
    <!-- Category Buttons -->
    <div class="category-buttons mb-4">
      <v-btn-toggle
        v-model="selectedCategoryIndex"
        mandatory
        variant="outlined"
        divided
        class="w-100"
        @update:model-value="updateCategory"
      >
        <v-btn
          value="all"
          class="flex-grow-1"
          :class="{ 'selected-category': selectedCategoryIndex === 'all' }"
        >
          <v-icon class="mr-2">mdi-map-marker-multiple</v-icon>
          전체
        </v-btn>
        <v-btn
          value="1"
          class="flex-grow-1"
          :class="{ 'selected-category': selectedCategoryIndex === '1' }"
        >
          <v-icon class="mr-2">mdi-information</v-icon>
          여행정보
        </v-btn>
        <v-btn
          value="2"
          class="flex-grow-1"
          :class="{ 'selected-category': selectedCategoryIndex === '2' }"
        >
          <v-icon class="mr-2">mdi-account-group</v-icon>
          같이가요
        </v-btn>
      </v-btn-toggle>
    </div>

    <!-- Search and Sort Row -->
    <div class="search-sort-row">
      <v-text-field
        v-model="searchKeyword"
        placeholder="제목, 내용, 작성자로 검색..."
        variant="outlined"
        density="compact"
        hide-details
        class="search-field"
        @keyup.enter="performSearch"
      >
        <template v-slot:prepend-inner>
          <v-icon color="grey">mdi-magnify</v-icon>
        </template>
        <template v-slot:append-inner>
          <v-btn v-if="searchKeyword" icon size="small" variant="text" @click="clearSearch">
            <v-icon size="small">mdi-close</v-icon>
          </v-btn>
        </template>
      </v-text-field>

      <v-btn color="primary" variant="flat" class="search-btn" @click="performSearch">
        <v-icon>mdi-magnify</v-icon>
        검색
      </v-btn>

      <v-select
        v-model="sortBy"
        :items="sortOptions"
        item-title="label"
        item-value="value"
        variant="outlined"
        density="compact"
        hide-details
        class="sort-field"
        @update:model-value="updateSort"
      >
        <template v-slot:prepend-inner>
          <v-icon color="grey" size="small">mdi-sort</v-icon>
        </template>
      </v-select>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  modelCategory: {
    type: [Number, String],
    default: null,
  },
  modelSearch: {
    type: String,
    default: '',
  },
  modelSort: {
    type: String,
    default: 'latest',
  },
})

const emit = defineEmits(['update:category', 'update:search', 'update:sort', 'filter'])

// Local state
const selectedCategoryIndex = ref('all')
const searchKeyword = ref('')
const sortBy = ref('latest')

const sortOptions = [
  { value: 'latest', label: '최신순' },
  { value: 'likes', label: '좋아요' },
  { value: 'views', label: '조회수' },
  { value: 'comments', label: '댓글수' },
]

// Initialize values from props
watch(
  () => props.modelCategory,
  (newVal) => {
    if (newVal === null) {
      selectedCategoryIndex.value = 'all'
    } else {
      selectedCategoryIndex.value = String(newVal)
    }
  },
  { immediate: true },
)

watch(
  () => props.modelSearch,
  (newVal) => {
    searchKeyword.value = newVal
  },
  { immediate: true },
)

watch(
  () => props.modelSort,
  (newVal) => {
    sortBy.value = newVal
  },
  { immediate: true },
)

// Methods
function updateCategory() {
  let categoryValue = null
  if (selectedCategoryIndex.value !== 'all') {
    categoryValue = parseInt(selectedCategoryIndex.value)
  }
  emit('update:category', categoryValue)
  emit('filter')
}

function performSearch() {
  emit('update:search', searchKeyword.value)
  emit('filter')
}

function clearSearch() {
  searchKeyword.value = ''
  emit('update:search', '')
  emit('filter')
}

function updateSort() {
  emit('update:sort', sortBy.value)
  emit('filter')
}

// Watch for local changes to emit updates
watch(searchKeyword, (newVal) => {
  emit('update:search', newVal)
  // Auto-search on typing (debounced effect can be added here)
  if (newVal === '') {
    emit('filter')
  }
})
</script>

<style scoped>
.search-filter {
  width: 100%;
}

.category-buttons {
  display: flex;
  width: 100%;
}

.v-btn-toggle {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

.v-btn-toggle .v-btn {
  border-radius: 0 !important;
  text-transform: none;
  font-weight: 500;
}

.v-btn-toggle .v-btn.v-btn--active {
  background-color: #1976d2 !important;
  color: white !important;
}

.search-sort-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-field {
  flex: 1;
  min-width: 0;
}

.search-btn {
  flex-shrink: 0;
  height: 40px;
  min-width: 80px;
}

.sort-field {
  flex-shrink: 0;
  width: 100px;
}

/* Mobile responsive */
@media (max-width: 600px) {
  .search-sort-row {
    flex-direction: column;
    gap: 8px;
  }

  .search-field,
  .search-btn,
  .sort-field {
    width: 100%;
  }

  .category-buttons .v-btn {
    font-size: 0.875rem;
    padding: 8px 12px;
  }
}

/* Custom focus states */
.v-text-field:focus-within .v-field {
  box-shadow: 0 0 0 2px rgba(25, 118, 210, 0.2);
}

.v-select:focus-within .v-field {
  box-shadow: 0 0 0 2px rgba(25, 118, 210, 0.2);
}
</style>
