import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useFinanceStore = defineStore('finance', () => {
  // State
  const expenses = ref([])
  const isLoading = ref(false)
  const error = ref(null)

  // Category mapping
  const categoryMap = {
    1: 'Transportation',
    2: 'Accommodation',
    3: 'Food',
    4: 'Leisure',
    5: 'Shared Supplies',
    6: 'Others'
  }

  // Computed properties
  const totalAmount = computed(() => {
    return expenses.value.reduce((sum, expense) => sum + expense.amount, 0)
  })

  const totalCount = computed(() => {
    return expenses.value.length
  })

  const expensesByCategory = computed(() => {
    const categoryGroupMap = new Map()

    expenses.value.forEach(expense => {
      const categoryId = expense.categoryId
      const categoryName = getCategoryName(categoryId)

      if (categoryGroupMap.has(categoryId)) {
        const existing = categoryGroupMap.get(categoryId)
        existing.count += 1
        existing.totalAmount += expense.amount
        existing.expenses.push(expense)
      } else {
        categoryGroupMap.set(categoryId, {
          categoryId: categoryId,
          categoryName: categoryName,
          count: 1,
          totalAmount: expense.amount,
          expenses: [expense]
        })
      }
    })

    return Array.from(categoryGroupMap.values())
  })

  const expensesByContentId = computed(() => {
    const contentMap = new Map()

    expenses.value.forEach(expense => {
      const contentId = expense.contentId
      if (contentId) {
        if (contentMap.has(contentId)) {
          const existing = contentMap.get(contentId)
          existing.count += 1
          existing.totalAmount += expense.amount
          existing.expenses.push(expense)
        } else {
          contentMap.set(contentId, {
            contentId: contentId,
            contentName: expense.contentName,
            count: 1,
            totalAmount: expense.amount,
            expenses: [expense]
          })
        }
      }
    })

    return Array.from(contentMap.values())
  })

  const categories = computed(() => {
    const uniqueCategoryIds = [...new Set(expenses.value.map(expense => expense.categoryId))]
    return uniqueCategoryIds.map(id => ({
      categoryId: id,
      categoryName: getCategoryName(id)
    }))
  })

  // Helper functions
  const getCategoryName = (categoryId) => {
    return categoryMap[categoryId] || 'Unknown'
  }

  const getCategoryId = (categoryName) => {
    const entry = Object.entries(categoryMap).find(([id, name]) => name === categoryName)
    return entry ? parseInt(entry[0]) : null
  }

  // Actions
  const setExpenses = (expenseList) => {
    expenses.value = [...expenseList]
    error.value = null
  }

  const addExpense = (expense) => {
    // Validate required fields
    if (!expense.description || !expense.amount || !expense.categoryId || !expense.datetime) {
      throw new Error('Required fields are missing')
    }

    // Validate category ID
    if (!categoryMap[expense.categoryId]) {
      throw new Error('Invalid category ID')
    }

    // Create new expense object with generated ID if not provided
    const newExpense = {
      expenseId: expense.expenseId || Date.now(),
      datetime: expense.datetime,
      description: expense.description,
      amount: Number(expense.amount),
      categoryId: Number(expense.categoryId),
      contentName: expense.contentName || null,
      contentId: expense.contentId || null
    }

    // Add to beginning of array (most recent first)
    expenses.value.unshift(newExpense)
    error.value = null

    return newExpense
  }

  const updateExpense = (expenseId, updatedData) => {
    const index = expenses.value.findIndex(expense => expense.expenseId === expenseId)

    if (index === -1) {
      throw new Error(`Expense with ID ${expenseId} not found`)
    }

    // Validate required fields if they are being updated
    const currentExpense = expenses.value[index]
    const merged = { ...currentExpense, ...updatedData }

    if (!merged.description || !merged.amount || !merged.categoryId || !merged.datetime) {
      throw new Error('Required fields cannot be empty')
    }

    // Validate category ID if being updated
    if (updatedData.categoryId && !categoryMap[updatedData.categoryId]) {
      throw new Error('Invalid category ID')
    }

    // Update the expense
    expenses.value[index] = {
      ...currentExpense,
      ...updatedData,
      amount: Number(updatedData.amount || currentExpense.amount),
      categoryId: Number(updatedData.categoryId || currentExpense.categoryId)
    }

    error.value = null
    return expenses.value[index]
  }

  const removeExpense = (expenseId) => {
    const index = expenses.value.findIndex(expense => expense.expenseId === expenseId)

    if (index === -1) {
      throw new Error(`Expense with ID ${expenseId} not found`)
    }

    const removedExpense = expenses.value.splice(index, 1)[0]
    error.value = null

    return removedExpense
  }

  const getExpenseById = (expenseId) => {
    return expenses.value.find(expense => expense.expenseId === expenseId) || null
  }

  const getExpensesByCategory = (categoryId) => {
    return expenses.value.filter(expense => expense.categoryId === categoryId)
  }

  const getExpensesByContentId = (contentId) => {
    return expenses.value.filter(expense => expense.contentId === contentId)
  }

  const getExpensesByDateRange = (startDate, endDate) => {
    const start = new Date(startDate)
    const end = new Date(endDate)

    return expenses.value.filter(expense => {
      const expenseDate = new Date(expense.datetime)
      return expenseDate >= start && expenseDate <= end
    })
  }

  const sortExpenses = (sortBy = 'datetime', order = 'desc') => {
    const sorted = [...expenses.value].sort((a, b) => {
      let comparison = 0

      switch (sortBy) {
        case 'datetime':
          comparison = new Date(a.datetime) - new Date(b.datetime)
          break
        case 'amount':
          comparison = a.amount - b.amount
          break
        case 'category':
          comparison = getCategoryName(a.categoryId).localeCompare(getCategoryName(b.categoryId))
          break
        case 'description':
          comparison = a.description.localeCompare(b.description)
          break
        default:
          comparison = a.expenseId - b.expenseId
      }

      return order === 'asc' ? comparison : -comparison
    })

    return sorted
  }

  const clearExpenses = () => {
    expenses.value = []
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

  // Filter methods
  const filterExpenses = (filters = {}) => {
    let filtered = [...expenses.value]

    if (filters.categoryId !== undefined) {
      filtered = filtered.filter(expense => expense.categoryId === filters.categoryId)
    }

    if (filters.contentId !== undefined) {
      filtered = filtered.filter(expense => expense.contentId === filters.contentId)
    }

    if (filters.startDate) {
      const startDate = new Date(filters.startDate)
      filtered = filtered.filter(expense => new Date(expense.datetime) >= startDate)
    }

    if (filters.endDate) {
      const endDate = new Date(filters.endDate)
      filtered = filtered.filter(expense => new Date(expense.datetime) <= endDate)
    }

    if (filters.minAmount !== undefined) {
      filtered = filtered.filter(expense => expense.amount >= filters.minAmount)
    }

    if (filters.maxAmount !== undefined) {
      filtered = filtered.filter(expense => expense.amount <= filters.maxAmount)
    }

    if (filters.search) {
      const searchTerm = filters.search.toLowerCase()
      filtered = filtered.filter(expense =>
        expense.description.toLowerCase().includes(searchTerm) ||
        getCategoryName(expense.categoryId).toLowerCase().includes(searchTerm) ||
        (expense.contentName && expense.contentName.toLowerCase().includes(searchTerm))
      )
    }

    return filtered
  }

  return {
    // State
    expenses,
    isLoading,
    error,

    // Computed
    totalAmount,
    totalCount,
    expensesByCategory,
    expensesByContentId,
    categories,

    // Helper functions
    getCategoryName,
    getCategoryId,

    // Actions
    setExpenses,
    addExpense,
    updateExpense,
    removeExpense,
    getExpenseById,
    getExpensesByCategory,
    getExpensesByContentId,
    getExpensesByDateRange,
    sortExpenses,
    clearExpenses,
    setLoading,
    setError,
    clearError,
    filterExpenses
  }
})
