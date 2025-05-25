import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAccommodationStore = defineStore('accommodation', () => {
  // State
  const accommodationList = ref([]) // No dummy data
  const isLoading = ref(false)
  const error = ref(null)

  // Computed
  const accommodationCount = computed(() => accommodationList.value.length)

  const isEmpty = computed(() => accommodationList.value.length === 0)

  // Helper function to generate unique ID
  const generateId = () => {
    const maxId = accommodationList.value.reduce((max, item) => {
      const currentId = parseInt(item.accommodationId)
      return currentId > max ? currentId : max
    }, 0)
    return maxId + 1
  }

  // Actions
  const addAccommodation = (accommodationData) => {
    try {
      error.value = null

      // Validate required fields
      if (!accommodationData.attractionsNo || !accommodationData.name ||
          !accommodationData.checkInTime || !accommodationData.checkOutTime) {
        throw new Error('필수 필드가 누락되었습니다.')
      }

      if (!accommodationData.accommodationInfo) {
        throw new Error('숙소 정보가 누락되었습니다.')
      }

      const newAccommodation = {
        accommodationId: generateId(),
        groupId: accommodationData.groupId,
        attractionsNo: accommodationData.attractionsNo,
        name: accommodationData.name,
        checkInTime: accommodationData.checkInTime,
        checkOutTime: accommodationData.checkOutTime,
        accommodationInfo: {
          no: accommodationData.accommodationInfo.no,
          title: accommodationData.accommodationInfo.title,
          contentTypeName: accommodationData.accommodationInfo.contentTypeName || '숙박',
          firstImage1: accommodationData.accommodationInfo.firstImage1 || '',
          firstImage2: accommodationData.accommodationInfo.firstImage2 || '',
          latitude: accommodationData.accommodationInfo.latitude,
          longitude: accommodationData.accommodationInfo.longitude,
          tel: accommodationData.accommodationInfo.tel || '',
          addr1: accommodationData.accommodationInfo.addr1 || '',
          addr2: accommodationData.accommodationInfo.addr2 || '',
          homepage: accommodationData.accommodationInfo.homepage || null,
          overview: accommodationData.accommodationInfo.overview || null
        }
      }

      accommodationList.value.push(newAccommodation)
      return { success: true, data: newAccommodation }
    } catch (err) {
      error.value = err.message
      return { success: false, error: err.message }
    }
  }

  const removeAccommodation = (accommodationId) => {
    try {
      error.value = null

      const index = accommodationList.value.findIndex(
        item => item.accommodationId === accommodationId
      )

      if (index === -1) {
        throw new Error('해당 숙소를 찾을 수 없습니다.')
      }

      const removedItem = accommodationList.value.splice(index, 1)[0]
      return { success: true, data: removedItem }
    } catch (err) {
      error.value = err.message
      return { success: false, error: err.message }
    }
  }

  const updateAccommodation = (accommodationId, updateData) => {
    try {
      error.value = null

      const index = accommodationList.value.findIndex(
        item => item.accommodationId === accommodationId
      )

      if (index === -1) {
        throw new Error('해당 숙소를 찾을 수 없습니다.')
      }

      const currentItem = accommodationList.value[index]

      // Validate required fields if they are being updated
      const updatedItem = {
        ...currentItem,
        ...updateData,
        accommodationInfo: {
          ...currentItem.accommodationInfo,
          ...(updateData.accommodationInfo || {})
        }
      }

      if (!updatedItem.attractionsNo || !updatedItem.name ||
          !updatedItem.checkInTime || !updatedItem.checkOutTime) {
        throw new Error('필수 필드가 누락되었습니다.')
      }

      // Update the item
      accommodationList.value[index] = updatedItem
      return { success: true, data: updatedItem }
    } catch (err) {
      error.value = err.message
      return { success: false, error: err.message }
    }
  }

  const getAccommodationById = (accommodationId) => {
    return accommodationList.value.find(
      item => item.accommodationId === accommodationId
    )
  }

  const getAccommodationsByGroupId = (groupId) => {
    return accommodationList.value.filter(
      item => item.groupId === groupId
    )
  }

  const getAccommodationByAttractionsNo = (attractionsNo) => {
    return accommodationList.value.find(
      item => item.attractionsNo === attractionsNo
    )
  }

  const clearError = () => {
    error.value = null
  }

  // Helper methods for API integration
  const setLoading = (loading) => {
    isLoading.value = loading
  }

  const setAccommodationList = (list) => {
    accommodationList.value = list
  }

  const clearAccommodationList = () => {
    accommodationList.value = []
  }

  // Validation helper
  const validateAccommodationData = (data) => {
    const errors = []

    if (!data.attractionsNo) {
      errors.push('관광지 번호가 필요합니다.')
    }

    if (!data.name || data.name.trim() === '') {
      errors.push('숙소 이름이 필요합니다.')
    }

    if (!data.checkInTime) {
      errors.push('체크인 시간이 필요합니다.')
    }

    if (!data.checkOutTime) {
      errors.push('체크아웃 시간이 필요합니다.')
    }

    if (data.checkInTime && data.checkOutTime) {
      const checkIn = new Date(data.checkInTime)
      const checkOut = new Date(data.checkOutTime)

      if (checkOut <= checkIn) {
        errors.push('체크아웃 시간은 체크인 시간보다 늦어야 합니다.')
      }
    }

    if (!data.accommodationInfo) {
      errors.push('숙소 상세 정보가 필요합니다.')
    } else {
      if (!data.accommodationInfo.title) {
        errors.push('숙소 제목이 필요합니다.')
      }
    }

    return {
      isValid: errors.length === 0,
      errors
    }
  }

  return {
    // State
    accommodationList,
    isLoading,
    error,

    // Computed
    accommodationCount,
    isEmpty,

    // Actions
    addAccommodation,
    removeAccommodation,
    updateAccommodation,
    getAccommodationById,
    getAccommodationsByGroupId,
    getAccommodationByAttractionsNo,
    clearError,

    // Helper methods for API integration
    setLoading,
    setAccommodationList,
    clearAccommodationList,

    // Validation
    validateAccommodationData
  }
})
