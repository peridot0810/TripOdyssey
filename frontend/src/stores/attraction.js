import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { apiClient } from '@/utils/apiClient'

export const useAttractionStore = defineStore('attraction', () => {
  // State
  const attractions = ref([])
  const selectedAttraction = ref(null)
  const searchParams = ref({})
  const isLoading = ref(false)
  const error = ref(null)

  // Computed
  const hasAttractions = computed(() => attractions.value.length > 0)
  const hasSelectedAttraction = computed(() => !!selectedAttraction.value)

  // Actions
  const searchAttractions = async (params) => {
    isLoading.value = true
    error.value = null

    try {
      // Store search parameters
      searchParams.value = { ...params }

      // Prepare API parameters with pagination defaults
      const apiParams = {
        sidoCode: params.selectedSido,
        gugunCode: params.selectedGugun || undefined,
        contentTypeId: params.selectedContentType,
        keyword: params.keyword || undefined,
        page: 1,        // Hard-coded as requested
        perPage: 20     // Hard-coded as requested
      }

      // Remove undefined values to clean up query string
      Object.keys(apiParams).forEach(key => {
        if (apiParams[key] === undefined) {
          delete apiParams[key]
        }
      })

      console.log('Searching attractions with params:', apiParams)

      const response = await apiClient.get('/attraction/list', {
        params: apiParams
      })

      if (response.data.success) {
        attractions.value = response.data.data
        console.log('Attractions received:', response.data.data)
        console.log(`Found ${attractions.value.length} attractions`)

        // Clear any previously selected attraction when new search is performed
        selectedAttraction.value = null
      } else {
        throw new Error(response.data.message || 'Failed to fetch attractions')
      }

    } catch (err) {
      console.error('Error searching attractions:', err)
      error.value = err.response?.data?.message || err.message || 'Failed to search attractions'
      attractions.value = []
    } finally {
      isLoading.value = false
    }
  }

  const setSelectedAttraction = (attraction) => {
    selectedAttraction.value = attraction
    console.log('Selected attraction:', attraction)
  }

  const clearSelectedAttraction = () => {
    selectedAttraction.value = null
  }

  const clearAttractions = () => {
    attractions.value = []
    selectedAttraction.value = null
    searchParams.value = {}
    error.value = null
  }

  const clearError = () => {
    error.value = null
  }

  // Get attraction by no (ID) - utility method
  const getAttractionById = (no) => {
    return attractions.value.find(attraction => attraction.no === no)
  }

  return {
    // State
    attractions,
    selectedAttraction,
    searchParams,
    isLoading,
    error,

    // Computed
    hasAttractions,
    hasSelectedAttraction,

    // Actions
    searchAttractions,
    setSelectedAttraction,
    clearSelectedAttraction,
    clearAttractions,
    clearError,
    getAttractionById
  }
})
