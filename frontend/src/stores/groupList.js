import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useGroupListStore = defineStore('group', () => {
  // State
  const groups = ref([])
  const selectedGroup = ref(null)

  // Computed
  const groupCount = computed(() => groups.value.length)

  const groupsByStatus = computed(() => {
    return {
      planning: groups.value.filter((group) => group.status === 'planning'),
      in_progress: groups.value.filter((group) => group.status === 'in_progress'),
      completed: groups.value.filter((group) => group.status === 'completed'),
    }
  })

  // Actions - Pure state management only
  const setGroups = (groupList) => {
    groups.value = [...groupList]
  }

  const getGroupById = (groupId) => {
    return groups.value.find((group) => group.groupId === groupId) || null
  }

  const selectGroup = (groupId) => {
    selectedGroup.value = getGroupById(groupId)
    return selectedGroup.value
  }

  const addGroup = (newGroup) => {
    groups.value.unshift(newGroup)
  }

  const updateGroup = (groupId, updatedData) => {
    const index = groups.value.findIndex((group) => group.groupId === groupId)
    if (index !== -1) {
      groups.value[index] = { ...groups.value[index], ...updatedData }

      // Update selected group if it's the same one
      if (selectedGroup.value?.groupId === groupId) {
        selectedGroup.value = groups.value[index]
      }
    }
  }

  const removeGroup = (groupId) => {
    const index = groups.value.findIndex((group) => group.groupId === groupId)
    if (index !== -1) {
      groups.value.splice(index, 1)

      // Clear selected group if it was removed
      if (selectedGroup.value?.groupId === groupId) {
        selectedGroup.value = null
      }
    }
  }

  const clearSelectedGroup = () => {
    selectedGroup.value = null
  }

  const resetGroups = () => {
    groups.value = []
    selectedGroup.value = null
  }

  // Helper function to format date
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    })
  }

  // Helper function to get status label
  const getStatusLabel = (status) => {
    const statusMap = {
      planning: '계획 중',
      in_progress: '진행 중',
      completed: '완료됨',
    }
    return statusMap[status] || status
  }

  // Helper function to get group image URL
  const getGroupImageUrl = (group) => {
    if (!group?.imageUrl) return null

    // If it's already a full URL, return as is
    if (group.imageUrl.startsWith('http')) {
      return group.imageUrl
    }

    // If it's a relative path, construct full URL
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
    return `${API_BASE_URL}${group.imageUrl}`
  }

  // Helper function to check if group has image
  const hasGroupImage = (group) => {
    return !!group?.imageUrl
  }

  return {
    // State
    groups,
    selectedGroup,

    // Computed
    groupCount,
    groupsByStatus,

    // Actions
    setGroups,
    getGroupById,
    selectGroup,
    addGroup,
    updateGroup,
    removeGroup,
    clearSelectedGroup,
    resetGroups,

    // Helpers
    formatDate,
    getStatusLabel,
    getGroupImageUrl,
    hasGroupImage,
  }
})
