import { defineStore } from 'pinia'
import { ref } from 'vue'
import { apiClient } from '@/utils/apiClient'

export const useScheduleStore = defineStore('schedule', () => {
  // State
  const schedules = ref([])
  const isLoading = ref(false)
  const error = ref(null)
  const selectedDay = ref(1) // Add selectedDay state

  // Batch update tracking
  const newOfficialSchedules = ref([])
  const modifiedOfficialSchedules = ref([])
  const removedOfficialSchedules = ref([])

  // Actions
  const addSchedule = (scheduleData) => {
    // Create new schedule object with proper format
    const newSchedule = {
      contentId: null,
      name: scheduleData.name,
      order: null,
      day: null,
      description: scheduleData.description,
      isOfficial: false,
      attractionsNo: scheduleData.selectedLocation.no,
      attractionInfo: {
        no: scheduleData.selectedLocation.no,
        title: scheduleData.selectedLocation.title,
        contentTypeName: scheduleData.selectedLocation.contentTypeName,
        firstImage1: scheduleData.selectedLocation.firstImage1 || '',
        firstImage2: scheduleData.selectedLocation.firstImage2 || '',
        latitude: scheduleData.selectedLocation.latitude,
        longitude: scheduleData.selectedLocation.longitude,
        tel: scheduleData.selectedLocation.tel || '',
        addr1: scheduleData.selectedLocation.addr1 || '',
        addr2: scheduleData.selectedLocation.addr2 || '',
        homepage: scheduleData.selectedLocation.homepage || '',
        overview: scheduleData.selectedLocation.overview || ''
      }
    }

    schedules.value.push(newSchedule)
    return newSchedule
  }

const deleteSchedule = (contentId) => {
  const schedule = schedules.value.find(s => s.contentId === contentId)

  if (!schedule) {
    return false
  }

  // If it was an official schedule, track it as removed
  if (schedule.isOfficial) {
    removedOfficialSchedules.value.push({ contentId })

    // Remove from other tracking arrays if it exists
    const newIndex = newOfficialSchedules.value.findIndex(s => s.contentId === contentId)
    if (newIndex !== -1) {
      newOfficialSchedules.value.splice(newIndex, 1)
    }

    const modifiedIndex = modifiedOfficialSchedules.value.findIndex(s => s.contentId === contentId)
    if (modifiedIndex !== -1) {
      modifiedOfficialSchedules.value.splice(modifiedIndex, 1)
    }
  }

  // Remove from schedules array
  const index = schedules.value.findIndex(schedule => schedule.contentId === contentId)
  if (index !== -1) {
    schedules.value.splice(index, 1)
    console.log('Removed official schedules tracking:', removedOfficialSchedules.value)
    return true
  }
  return false
}

  const getSchedulesByDay = (day) => {
    return schedules.value.filter(schedule => schedule.day === day)
  }

  const getAllScheduleCandidates = () => {
    return schedules.value.filter(schedule => !schedule.isOfficial)
  }

  const getOfficialSchedules = () => {
    return schedules.value.filter(schedule => schedule.isOfficial)
  }

  const fetchSchedules = async (groupId) => {
  isLoading.value = true
  error.value = null

  try {
    console.log('Fetching schedules for group:', groupId)
    const response = await apiClient.get(`/schedule/${groupId}/all`)

    if (response.data.success) {
      schedules.value = response.data.data || []
      console.log('Schedules fetched successfully:', schedules.value)
    } else {
      throw new Error(response.data.message || 'Failed to fetch schedules')
    }
  } catch (err) {
    console.error('Error fetching schedules:', err)
    error.value = err.response?.data?.message || err.message || 'Failed to fetch schedules'
    schedules.value = []
  } finally {
    isLoading.value = false
  }
}

  const saveScheduleToServer = async (groupId, scheduleData) => {
    try {
      const response = await apiClient.post(`/schedule/${groupId}/content`, {
        name: scheduleData.name,
        description: scheduleData.description,
        attractionNo: scheduleData.attractionsNo
      })

      if (response.data.success) {
        // Update the local schedule with server response if needed
        return response.data
      }
    } catch (err) {
      console.error('Error saving schedule to server:', err)
      error.value = err.response?.data?.message || err.message || 'Failed to save schedule'
      throw err
    }
  }

  const clearError = () => {
    error.value = null
  }

  const clearSchedules = () => {
    schedules.value = []
  }

  // Selected day methods
  const setSelectedDay = (day) => {
    selectedDay.value = day
  }

  const getSelectedDay = () => {
    return selectedDay.value
  }

  // Helper method to get next order for a specific day
  const getNextOrderForDay = (day) => {
    const schedulesForDay = schedules.value.filter(
      schedule => schedule.isOfficial && schedule.day === day
    )

    if (schedulesForDay.length === 0) {
      return 1
    }

    const maxOrder = Math.max(...schedulesForDay.map(s => s.order || 0))
    return maxOrder + 1
  }

  // Make a schedule official
const makeScheduleOfficial = (contentId) => {
  const schedule = schedules.value.find(s => s.contentId === contentId)

  if (!schedule) {
    console.error('Schedule not found:', contentId)
    return false
  }

  if (schedule.isOfficial) {
    console.warn('Schedule is already official:', contentId)
    return false
  }

  // Calculate the next order for the selected day
  const nextOrder = getNextOrderForDay(selectedDay.value)

  // Update the schedule
  schedule.isOfficial = true
  schedule.day = selectedDay.value
  schedule.order = nextOrder

  // Add to newOfficialSchedules tracking
  const trackingData = {
    contentId: schedule.contentId,
    day: schedule.day,
    order: schedule.order
  }

  // Remove from tracking if already exists (shouldn't happen, but safety check)
  const existingIndex = newOfficialSchedules.value.findIndex(s => s.contentId === contentId)
  if (existingIndex !== -1) {
    newOfficialSchedules.value.splice(existingIndex, 1)
  }

  newOfficialSchedules.value.push(trackingData)

  console.log(`Schedule "${schedule.name}" made official:`, {
    day: schedule.day,
    order: schedule.order
  })
  console.log('New official schedules tracking:', newOfficialSchedules.value)

  return true
}


// Track order modifications for drag and drop
const trackOrderModification = (contentId, newDay, newOrder) => {
  const schedule = schedules.value.find(s => s.contentId === contentId)

  if (!schedule || !schedule.isOfficial) {
    return false
  }

  // Don't track if this is a newly made official schedule
  const isNewOfficial = newOfficialSchedules.value.some(s => s.contentId === contentId)
  if (isNewOfficial) {
    // Update the newOfficialSchedules tracking instead
    const newOfficialIndex = newOfficialSchedules.value.findIndex(s => s.contentId === contentId)
    if (newOfficialIndex !== -1) {
      newOfficialSchedules.value[newOfficialIndex].day = newDay
      newOfficialSchedules.value[newOfficialIndex].order = newOrder
    }
    return true
  }

  // Track as modified official
  const trackingData = {
    contentId,
    day: newDay,
    order: newOrder
  }

  // Remove existing tracking for this schedule
  const existingIndex = modifiedOfficialSchedules.value.findIndex(s => s.contentId === contentId)
  if (existingIndex !== -1) {
    modifiedOfficialSchedules.value.splice(existingIndex, 1)
  }

  modifiedOfficialSchedules.value.push(trackingData)
  console.log('Modified official schedules tracking:', modifiedOfficialSchedules.value)

  return true
}

// Clear all tracking arrays
const clearTracking = () => {
  newOfficialSchedules.value = []
  modifiedOfficialSchedules.value = []
  removedOfficialSchedules.value = []
  console.log('Tracking arrays cleared')
}

// Get current tracking state for debugging
const getTrackingState = () => {
  return {
    newOfficial: newOfficialSchedules.value,
    modifiedOfficial: modifiedOfficialSchedules.value,
    removedOfficial: removedOfficialSchedules.value
  }
}



return {
  // State
  schedules,
  isLoading,
  error,
  selectedDay,
  newOfficialSchedules,
  modifiedOfficialSchedules,
  removedOfficialSchedules,

  // Actions
  addSchedule,
  deleteSchedule,
  getSchedulesByDay,
  getAllScheduleCandidates,
  getOfficialSchedules,
  fetchSchedules,
  saveScheduleToServer,
  clearError,
  clearSchedules,
  setSelectedDay,
  getSelectedDay,
  getNextOrderForDay,
  makeScheduleOfficial,
  trackOrderModification,
  clearTracking,
  getTrackingState
}
})
