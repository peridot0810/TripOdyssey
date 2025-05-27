import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useGroupStore = defineStore('singleGroup', () => {
  // State
  const myGroup = ref({
    groupId: null,
    name: '',
    description: '',
    status: '',
    createdAt: '',
    startDate: '',
    endDate: '',
    imageUrl: null,
    roleLimits: {
      finance: 0,
      schedule: 0,
      logistics: 0,
    },
    progress: {
      scheduleCoordination: false,
      roleAssignment: false,
      tripPlanning: false,
      onTrip: false,
      settlement: false,
      finished: false,
    },
  })

  // Computed
  const hasGroup = computed(() => myGroup.value.groupId !== null)

  // Helper function to get group image URL
  const getGroupImageUrl = computed(() => {
    if (!myGroup.value?.imageUrl) return null

    // If it's already a full URL, return as is
    if (myGroup.value.imageUrl.startsWith('http')) {
      return myGroup.value.imageUrl
    }

    // If it's a relative path, construct full URL
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
    return `${API_BASE_URL}${myGroup.value.imageUrl}`
  })

  // Helper function to check if group has image
  const hasGroupImage = computed(() => {
    return !!myGroup.value?.imageUrl
  })

  // Actions - only state management
  const setGroupData = (groupData) => {
    myGroup.value = {
      groupId: groupData.groupId,
      name: groupData.name,
      description: groupData.description || '',
      status: groupData.status,
      createdAt: groupData.createdAt,
      startDate: groupData.startDate || '',
      endDate: groupData.endDate || '',
      imageUrl: groupData.imageUrl || null,
      roleLimits: { ...groupData.roleLimits },
      progress: { ...groupData.progress },
    }
  }

  const clearGroup = () => {
    myGroup.value = {
      groupId: null,
      name: '',
      description: '',
      status: '',
      createdAt: '',
      startDate: '',
      endDate: '',
      imageUrl: null,
      roleLimits: {
        finance: 0,
        schedule: 0,
        logistics: 0,
      },
      progress: {
        scheduleCoordination: false,
        roleAssignment: false,
        tripPlanning: false,
        onTrip: false,
        settlement: false,
        finished: false,
      },
    }
  }

  return {
    myGroup,
    hasGroup,
    getGroupImageUrl,
    hasGroupImage,
    setGroupData,
    clearGroup,
  }
})
