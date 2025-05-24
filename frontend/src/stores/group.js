import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { apiClient } from '@/utils/apiClient'

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

  const isLoading = ref(false)
  const error = ref(null)

  // Computed
  const hasGroup = computed(() => myGroup.value.groupId !== null)

  // Get group info by ID
  const getGroupInfo = async (id) => {
    if (!id) {
      error.value = 'Group ID is required'
      return { success: false, error: error.value }
    }

    isLoading.value = true
    error.value = null

    try {
      const response = await apiClient.get(`/group/${id}`)

      if (response.data.success) {
        const groupData = response.data.data
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
        return { success: true, data: myGroup.value }
      } else {
        error.value = response.data.message || '그룹 정보를 불러올 수 없습니다.'
        return { success: false, error: error.value }
      }

    } catch (err) {
      console.error('Error fetching group info:', err)

      if (err.response?.status === 401) {
        error.value = '로그인이 필요합니다.'
      } else if (err.response?.status === 404) {
        error.value = '그룹을 찾을 수 없습니다.'
      } else {
        error.value = '그룹 정보를 불러오는 중 오류가 발생했습니다.'
      }

      return { success: false, error: error.value }
    } finally {
      isLoading.value = false
    }
  }

  const clearError = () => {
    error.value = null
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
    error.value = null
  }

  console.log('Store created with createGroup:', typeof createGroup) // Debug

  return {
    // State
    myGroup,
    isLoading,
    error,

    // Computed
    hasGroup,

    // Actions
    getGroupInfo,
    clearError,
    clearGroup
  }
})
