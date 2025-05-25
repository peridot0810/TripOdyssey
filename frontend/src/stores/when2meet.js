import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useWhen2MeetStore = defineStore('when2meet', () => {
  // State
  const availabilityData = ref([])
  const currentGroupId = ref(null)
  const isLoading = ref(false)
  const error = ref(null)

  // User's personal availability (derived from availabilityData)
  const userAvailability = ref([])

  // Computed - Create availability map for calendar visualization
  const availabilityMap = computed(() => {
    const map = new Map()

    availabilityData.value.forEach((entry) => {
      const startDate = new Date(entry.startDate)
      const endDate = new Date(entry.endDate)

      // Iterate through each date in the range
      const currentDate = new Date(startDate)
      while (currentDate <= endDate) {
        const dateStr = currentDate.toISOString().split('T')[0]
        const current = map.get(dateStr) || { count: 0, users: [] }
        current.count += 1
        current.users.push(entry.userId)
        map.set(dateStr, current)
        currentDate.setDate(currentDate.getDate() + 1)
      }
    })

    return map
  })

  // Computed - Best meeting recommendations
  const meetingRecommendations = computed(() => {
    if (!availabilityMap.value.size) {
      return {
        bestDates: [],
        maxAvailable: 0,
        totalResponses: 0
      }
    }

    // Find dates with highest availability
    let maxCount = 0
    const bestDatesMap = new Map()

    for (const [dateStr, data] of availabilityMap.value) {
      if (data.count > maxCount) {
        maxCount = data.count
        bestDatesMap.clear()
        bestDatesMap.set(dateStr, data)
      } else if (data.count === maxCount) {
        bestDatesMap.set(dateStr, data)
      }
    }

    // Convert to sorted array of best dates
    const bestDates = Array.from(bestDatesMap.keys())
      .sort()
      .slice(0, 5) // Top 5 best dates

    // Get unique users count (total responses)
    const uniqueUsers = new Set()
    availabilityData.value.forEach(entry => uniqueUsers.add(entry.userId))

    return {
      bestDates,
      maxAvailable: maxCount,
      totalResponses: uniqueUsers.size
    }
  })

  // Actions
  const setCurrentGroup = (groupId) => {
    currentGroupId.value = groupId
  }

  const setAvailabilityData = (data) => {
    availabilityData.value = data || []
  }

  const setUserAvailability = (data) => {
    userAvailability.value = data || []
  }

  const clearError = () => {
    error.value = null
  }

  const clearData = () => {
    availabilityData.value = []
    userAvailability.value = []
    currentGroupId.value = null
    clearError()
  }

  // Helper function to format date for API
  const formatDateForAPI = (date) => {
    if (date instanceof Date) {
      return date.toISOString().split('T')[0] // YYYY-MM-DD format
    }
    return date // Assume it's already in correct format
  }

  // Helper function to get availability count for a specific date
  const getAvailabilityCountForDate = (dateStr) => {
    return availabilityMap.value.get(dateStr)?.count || 0
  }

  // Helper function to get users available for a specific date
  const getUsersAvailableForDate = (dateStr) => {
    return availabilityMap.value.get(dateStr)?.users || []
  }

  return {
    // State
    availabilityData,
    userAvailability,
    currentGroupId,
    isLoading,
    error,

    // Computed
    availabilityMap,
    meetingRecommendations,

    // Actions
    setCurrentGroup,
    setAvailabilityData,
    setUserAvailability,
    clearError,
    clearData,

    // Helpers
    getAvailabilityCountForDate,
    getUsersAvailableForDate,
    formatDateForAPI
  }
})
