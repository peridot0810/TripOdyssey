import {ref, computed} from 'vue'
import {defineStore} from 'pinia'

export const useCommunityStore = defineStore('finance', () => {
  // State
  const posts = ref([]);
  const categoryId = ref(1);
  const page = ref(1);
  const perPage = ref(5);
  const totalCount = ref(0);
  const isLoading = ref(false);
  const error = ref(null);

  // Category mapping
  const categoryMap = {
    1: 'Free',
    2: 'GoTogether',
  }

  // Computed properties
  const queryParams = computed(() => ({
    categoryId: categoryId.value,
    page: page.value,
    perPage: perPage.value,
  }))


  // Helper functions
  const getCategoryName = (categoryId) => {
    return categoryMap[categoryId] || 'Unknown'
  }

  const getCategoryId = (categoryName) => {
    const entry = Object.entries(categoryMap).find(([name]) => name === categoryName)
    return entry ? parseInt(entry[0]) : null
  }

  // Actions
  function setCategory(id) {
    categoryId.value = id
    page.value = 1
  }

  function setPage(p) {
    page.value = p
  }

  function setPerPage(n) {
    perPage.value = n
  }

  function setPosts({ posts: newPosts, totalCount: count }) {
    posts.value = newPosts
    totalCount.value = count
  }

  function setLoading(state) {
    isLoading.value = state
  }

  function setError(msg) {
    error.value = msg
  }


  return {
    // Status
    posts,
    categoryId,
    page,
    perPage,
    totalCount,
    isLoading,
    error,

    // Actions
    setCategory,
    setPage,
    setPerPage,
    setPosts,
    setLoading,
    setError,

    // Computed
    queryParams,

    // Helper
    getCategoryName,
    getCategoryId
  }
})