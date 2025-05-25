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
    roleLimits: {
      finance: 0,
      schedule: 0,
      logistics: 0
    }
  })

  // Computed
  const hasGroup = computed(() => myGroup.value.groupId !== null)

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
      roleLimits: { ...groupData.roleLimits }
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
      roleLimits: {
        finance: 0,
        schedule: 0,
        logistics: 0
      }
    }
  }

  return {
    myGroup,
    hasGroup,
    setGroupData,
    clearGroup
  }
})
