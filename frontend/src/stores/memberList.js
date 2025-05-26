import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useMemberListStore = defineStore('memberList', () => {
  // State
  const members = ref([])
  const isLoading = ref(false)
  const error = ref(null)

  // Role mapping for better readability
  const ROLE_INDEX = {
    MASTER: 0,
    FINANCE: 1,
    SCHEDULE: 2,
    LOGISTICS: 3,
    MEMBER: 4,
  }

  const ROLE_NAMES = {
    0: 'MASTER',
    1: 'FINANCE',
    2: 'SCHEDULE',
    3: 'LOGISTICS',
    4: 'MEMBER',
  }

  // Computed
  const memberCount = computed(() => members.value.length)

  const membersByRole = computed(() => {
    const roleGroups = {
      MASTER: [],
      FINANCE: [],
      SCHEDULE: [],
      LOGISTICS: [],
      MEMBER: [],
    }

    members.value.forEach((member) => {
      const roleIndex = member.roles.findIndex((role) => role === true)
      const roleName = ROLE_NAMES[roleIndex] || 'MEMBER'
      roleGroups[roleName].push(member)
    })

    return roleGroups
  })

  // Helper function to get member's role
  const getMemberRole = (member) => {
    const roleIndex = member.roles.findIndex((role) => role === true)
    return ROLE_NAMES[roleIndex] || 'MEMBER'
  }

  // Helper function to get role display name
  const getRoleDisplayName = (roleKey) => {
    const displayNames = {
      MASTER: '마스터',
      FINANCE: '회계',
      SCHEDULE: '일정',
      LOGISTICS: '숙박',
      MEMBER: '멤버',
    }
    return displayNames[roleKey] || '멤버'
  }

  // Actions
  const setMembers = (memberList) => {
    members.value = memberList
    error.value = null
  }

  const addMember = (member) => {
    members.value.push(member)
  }

  const removeMember = (userId) => {
    const index = members.value.findIndex((member) => member.userId === userId)
    if (index !== -1) {
      members.value.splice(index, 1)
    }
  }

  const updateMemberRole = (userId, newRoles) => {
    const member = members.value.find((m) => m.userId === userId)
    if (member) {
      member.roles = newRoles
    }
  }

  const getMemberById = (userId) => {
    return members.value.find((member) => member.userId === userId)
  }

  const setLoading = (loading) => {
    isLoading.value = loading
  }

  const setError = (errorMessage) => {
    error.value = errorMessage
  }

  const clearError = () => {
    error.value = null
  }

  const clearMembers = () => {
    members.value = []
    error.value = null
  }

  // Get members with specific role
  const getMembersByRole = (roleKey) => {
    return membersByRole.value[roleKey] || []
  }

  return {
    // State
    members,
    isLoading,
    error,

    // Constants
    ROLE_INDEX,
    ROLE_NAMES,

    // Computed
    memberCount,
    membersByRole,

    // Actions
    setMembers,
    addMember,
    removeMember,
    updateMemberRole,
    getMemberById,
    setLoading,
    setError,
    clearError,
    clearMembers,
    getMembersByRole,

    // Helper functions
    getMemberRole,
    getRoleDisplayName,
  }
})
