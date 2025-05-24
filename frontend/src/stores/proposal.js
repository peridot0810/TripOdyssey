import { defineStore } from 'pinia'
import { ref } from 'vue'
import { apiClient } from '@/utils/apiClient'

export const useProposalStore = defineStore('proposal', () => {
  // State
  const proposals = ref([])
  const isLoading = ref(false)
  const error = ref(null)

  // Actions
  const fetchProposals = async (groupId) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await apiClient.get(`/schedule/${groupId}/recommended/list`)

      if (response.data.success) {
        proposals.value = response.data.data || []
      } else {
        throw new Error(response.data.message || 'Failed to fetch proposals')
      }
    } catch (err) {
      console.error('Error fetching proposals:', err)
      error.value = err.response?.data?.message || err.message || 'Failed to fetch proposals'
      proposals.value = []
    } finally {
      isLoading.value = false
    }
  }

  const toggleLike = async (groupId, proposalId) => {
    try {
      // Find the proposal to update
      const proposal = proposals.value.find(p => p.proposalId === proposalId)
      if (!proposal) {
        throw new Error('Proposal not found')
      }

      // Optimistically update the UI
      const wasLiked = proposal.userLiked
      proposal.userLiked = !wasLiked

      // Update likes count
      if (proposal.userLiked) {
        proposal.likes = (proposal.likes || 0) + 1
      } else {
        proposal.likes = Math.max((proposal.likes || 0) - 1, 0)
      }

      // Make API call
      await apiClient.post(`/schedule/${groupId}/recommended/like`, proposalId)

    } catch (err) {
      console.error('Error toggling like:', err)

      // Revert optimistic update on error
      const proposal = proposals.value.find(p => p.proposalId === proposalId)
      if (proposal) {
        proposal.userLiked = !proposal.userLiked

        // Revert likes count
        if (proposal.userLiked) {
          proposal.likes = (proposal.likes || 0) + 1
        } else {
          proposal.likes = Math.max((proposal.likes || 0) - 1, 0)
        }
      }

      // Set error for user feedback
      error.value = err.response?.data?.message || err.message || 'Failed to toggle like'
      throw err
    }
  }

  const clearError = () => {
    error.value = null
  }

  return {
    // State
    proposals,
    isLoading,
    error,

    // Actions
    fetchProposals,
    toggleLike,
    clearError
  }
})
