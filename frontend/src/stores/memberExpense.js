import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useMemberExpenseStore = defineStore('memberExpense', () => {
  // State
  const memberExpenseList = ref([])
  const isLoading = ref(false)
  const error = ref(null)

  // Computed
  const totalMembersCount = computed(() => {
    return memberExpenseList.value.length
  })

  const totalExpenseToPay = computed(() => {
    return memberExpenseList.value.reduce((total, member) => {
      return total + member.expenseToPay
    }, 0)
  })

  const totalPaidAmount = computed(() => {
    return memberExpenseList.value.reduce((total, member) => {
      return total + member.paidAmount
    }, 0)
  })

  const totalOutstandingAmount = computed(() => {
    return totalExpenseToPay.value - totalPaidAmount.value
  })

  const membersWithOutstandingBalance = computed(() => {
    return memberExpenseList.value.filter(member => {
      return member.expenseToPay > member.paidAmount
    })
  })

  const membersFullyPaid = computed(() => {
    return memberExpenseList.value.filter(member => {
      return member.expenseToPay === member.paidAmount
    })
  })

  // Actions
  const setMemberExpenseList = (list) => {
    memberExpenseList.value = list || []
    error.value = null
  }

  const updateMemberExpense = (userId, updates) => {
    const memberIndex = memberExpenseList.value.findIndex(member => member.userId === userId)
    if (memberIndex !== -1) {
      memberExpenseList.value[memberIndex] = {
        ...memberExpenseList.value[memberIndex],
        ...updates
      }
    }
  }

  const addMemberExpense = (memberExpense) => {
    const existingIndex = memberExpenseList.value.findIndex(member => member.userId === memberExpense.userId)
    if (existingIndex !== -1) {
      // Update existing member
      memberExpenseList.value[existingIndex] = memberExpense
    } else {
      // Add new member
      memberExpenseList.value.push(memberExpense)
    }
  }

  const removeMemberExpense = (userId) => {
    memberExpenseList.value = memberExpenseList.value.filter(member => member.userId !== userId)
  }

  const getMemberExpenseByUserId = (userId) => {
    return memberExpenseList.value.find(member => member.userId === userId) || null
  }

  const clearMemberExpenseList = () => {
    memberExpenseList.value = []
    error.value = null
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

  return {
    // State
    memberExpenseList,
    isLoading,
    error,

    // Computed
    totalMembersCount,
    totalExpenseToPay,
    totalPaidAmount,
    totalOutstandingAmount,
    membersWithOutstandingBalance,
    membersFullyPaid,

    // Actions
    setMemberExpenseList,
    updateMemberExpense,
    addMemberExpense,
    removeMemberExpense,
    getMemberExpenseByUserId,
    clearMemberExpenseList,
    setLoading,
    setError,
    clearError
  }
})
