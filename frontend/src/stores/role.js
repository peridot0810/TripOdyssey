import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useRoleStore = defineStore('role', () => {
  // State - user roles in the current group
  const userRoles = ref({
    master: true,
    finance: true,
    schedule: true,
    logistics: true,
    normal: true
  })

  // Getters
  const hasRole = (roleName) => {
    return userRoles.value[roleName] || false
  }

  const hasScheduleRole = () => {
    return userRoles.value.schedule || false
  }

  const hasMasterRole = () => {
    return userRoles.value.master || false
  }

  const hasFinanceRole = () => {
    return userRoles.value.finance || false
  }

  const hasLogisticsRole = () => {
    return userRoles.value.logistics || false
  }

  return {
    // State
    userRoles,

    // Getters
    hasRole,
    hasScheduleRole,
    hasMasterRole,
    hasFinanceRole,
    hasLogisticsRole
  }
})
