import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { apiClient } from '@/utils/apiClient'
import { groupData } from '@/data/group/groupData'

export const useGroupListStore = defineStore('group', () => {
  // State
  const groups = ref([])
  const selectedGroup = ref(null)
  const isLoading = ref(false)
  const error = ref(null)

  // Computed
  const groupCount = computed(() => groups.value.length)

  const groupsByStatus = computed(() => {
    return {
      planning: groups.value.filter(group => group.status === 'planning'),
      in_progress: groups.value.filter(group => group.status === 'in_progress'),
      completed: groups.value.filter(group => group.status === 'completed')
    }
  })

  // Actions
  const getUserGroupList = async () => {
    isLoading.value = true
    error.value = null

    try {
      // TODO: Replace with actual API call when server is ready
      // const response = await authenticatedRequest({
      //   method: 'GET',
      //   url: '/groups/user'
      // })
      // groups.value = response.data.data

      // Temporary: Use local data
      await new Promise(resolve => setTimeout(resolve, 500)) // Simulate API delay
      groups.value = [...groupData]

      return { success: true, data: groups.value }

    } catch (err) {
      console.error('Error fetching user groups:', err)
      error.value = '그룹 목록을 불러오는 중 오류가 발생했습니다.'
      return { success: false, error: error.value }
    } finally {
      isLoading.value = false
    }
  }

  // Future API call version (commented out for now)
  const getUserGroupListFromAPI = async (userStore) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await userStore.authenticatedRequest({
        method: 'GET',
        url: '/groups/user'
      })

      if (response.data.success) {
        groups.value = response.data.data
        return { success: true, data: groups.value }
      } else {
        error.value = response.data.message || '그룹 목록을 불러올 수 없습니다.'
        return { success: false, error: error.value }
      }

    } catch (err) {
      console.error('Error fetching user groups:', err)
      if (err.response?.status === 401) {
        error.value = '로그인이 필요합니다.'
      } else if (err.response?.status === 404) {
        error.value = '그룹을 찾을 수 없습니다.'
      } else {
        error.value = '그룹 목록을 불러오는 중 오류가 발생했습니다.'
      }
      return { success: false, error: error.value }
    } finally {
      isLoading.value = false
    }
  }

  const getGroupById = (groupId) => {
    return groups.value.find(group => group.groupId === groupId) || null
  }

  const selectGroup = (groupId) => {
    selectedGroup.value = getGroupById(groupId)
    return selectedGroup.value
  }

  const addGroup = (newGroup) => {
    groups.value.unshift(newGroup)
  }

  const updateGroup = (groupId, updatedData) => {
    const index = groups.value.findIndex(group => group.groupId === groupId)
    if (index !== -1) {
      groups.value[index] = { ...groups.value[index], ...updatedData }

      // Update selected group if it's the same one
      if (selectedGroup.value?.groupId === groupId) {
        selectedGroup.value = groups.value[index]
      }
    }
  }

  const removeGroup = (groupId) => {
    const index = groups.value.findIndex(group => group.groupId === groupId)
    if (index !== -1) {
      groups.value.splice(index, 1)

      // Clear selected group if it was removed
      if (selectedGroup.value?.groupId === groupId) {
        selectedGroup.value = null
      }
    }
  }

  const clearError = () => {
    error.value = null
  }

  // Create new group (moved from single group store)
  const createGroup = async (groupData) => {
    isLoading.value = true
    error.value = null

    try {
      const requestBody = {
        name: groupData.name,
        description: groupData.description || null,
        status: 'planning',
        startDate: groupData.startDate || null,
        endDate: groupData.endDate || null,
        roleLimits: groupData.roleLimits
      }

      const response = await apiClient.post('/group', requestBody)

      if (response.data.success) {
        return {
          success: true,
          data: {
            groupId: response.data.data.groupId,
            status: response.data.data.status,
            createdAt: response.data.data.createdAt
          }
        }
      } else {
        error.value = response.data.message || '그룹 생성에 실패했습니다.'
        return { success: false, error: error.value }
      }

    } catch (err) {
      console.error('Error creating group:', err)

      if (err.response?.status === 401) {
        error.value = '로그인이 필요합니다.'
      } else if (err.response?.status === 400) {
        error.value = '입력 정보를 확인해주세요.'
      } else {
        error.value = '그룹 생성 중 오류가 발생했습니다.'
      }

      return { success: false, error: error.value }
    } finally {
      isLoading.value = false
    }
  }

  const resetGroups = () => {
    groups.value = []
    selectedGroup.value = null
    error.value = null
  }

  // Helper function to format date
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  // Helper function to get status label
  const getStatusLabel = (status) => {
    const statusMap = {
      'planning': '계획 중',
      'in_progress': '진행 중',
      'completed': '완료됨'
    }
    return statusMap[status] || status
  }

  return {
    // State
    groups,
    selectedGroup,
    isLoading,
    error,

    // Computed
    groupCount,
    groupsByStatus,

    // Actions
    getUserGroupList,
    getUserGroupListFromAPI,
    getGroupById,
    selectGroup,
    addGroup,
    updateGroup,
    removeGroup,
    clearError,
    resetGroups,
    createGroup,

    // Helpers
    formatDate,
    getStatusLabel
  }
})
