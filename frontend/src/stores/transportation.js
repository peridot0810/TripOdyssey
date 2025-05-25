import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useTransportationStore = defineStore('transportation', () => {
  // State
  const transportationList = ref([]) // Cleared dummy data
  const isLoading = ref(false)
  const error = ref(null)

  // Computed
  const transportationCount = computed(() => transportationList.value.length)

  const isEmpty = computed(() => transportationList.value.length === 0)

  // Helper function to generate unique ID
  const generateId = () => {
    const maxId = transportationList.value.reduce((max, item) => {
      const currentId = parseInt(item.transportationId)
      return currentId > max ? currentId : max
    }, 0)
    return (maxId + 1).toString()
  }

  // Actions
  const addTransportation = (transportationData) => {
    try {
      error.value = null

      // Validate required fields
      if (!transportationData.typeId || !transportationData.departure ||
          !transportationData.arrival || !transportationData.from ||
          !transportationData.to) {
        throw new Error('필수 필드가 누락되었습니다.')
      }

      const newTransportation = {
        transportationId: generateId(),
        typeId: transportationData.typeId,
        departure: transportationData.departure,
        arrival: transportationData.arrival,
        from: transportationData.from,
        to: transportationData.to,
        description: transportationData.description || '',
      }

      transportationList.value.push(newTransportation)
      return { success: true, data: newTransportation }
    } catch (err) {
      error.value = err.message
      return { success: false, error: err.message }
    }
  }

  const removeTransportation = (transportationId) => {
    try {
      error.value = null

      const index = transportationList.value.findIndex(
        item => item.transportationId === transportationId
      )

      if (index === -1) {
        throw new Error('해당 교통편을 찾을 수 없습니다.')
      }

      const removedItem = transportationList.value.splice(index, 1)[0]
      return { success: true, data: removedItem }
    } catch (err) {
      error.value = err.message
      return { success: false, error: err.message }
    }
  }

  const updateTransportation = (transportationId, updateData) => {
    try {
      error.value = null

      const index = transportationList.value.findIndex(
        item => item.transportationId === transportationId
      )

      if (index === -1) {
        throw new Error('해당 교통편을 찾을 수 없습니다.')
      }

      // Validate required fields if they are being updated
      const currentItem = transportationList.value[index]
      const updatedItem = { ...currentItem, ...updateData }

      if (!updatedItem.typeId || !updatedItem.departure ||
          !updatedItem.arrival || !updatedItem.from ||
          !updatedItem.to) {
        throw new Error('필수 필드가 누락되었습니다.')
      }

      // Update the item
      transportationList.value[index] = updatedItem
      return { success: true, data: updatedItem }
    } catch (err) {
      error.value = err.message
      return { success: false, error: err.message }
    }
  }

  const getTransportationById = (transportationId) => {
    return transportationList.value.find(
      item => item.transportationId === transportationId
    )
  }

  const clearError = () => {
    error.value = null
  }

  // Helper methods for API integration
  const setLoading = (loading) => {
    isLoading.value = loading
  }

  const setTransportationList = (list) => {
    transportationList.value = list
  }

  const clearTransportationList = () => {
    transportationList.value = []
  }

  return {
    // State
    transportationList,
    isLoading,
    error,

    // Computed
    transportationCount,
    isEmpty,

    // Actions
    addTransportation,
    removeTransportation,
    updateTransportation,
    getTransportationById,
    clearError,

    // Helper methods for API integration
    setLoading,
    setTransportationList,
    clearTransportationList,
  }
})
