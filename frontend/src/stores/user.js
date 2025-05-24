import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'

export const useUserStore = defineStore('user', () => {
  // State
  const userInfo = ref({
    id: null,
    email: null,
    nickname: null,
    gender: null,
    age: null
  })

  const accessToken = ref(null)
  const loginError = ref(null)
  const isLoading = ref(false)

  // API Base URL from environment variable
  const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

  // Create axios instance
  const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json'
    }
  })

  // Computed
  const isLoggedIn = computed(() => {
    return !!accessToken.value && !!userInfo.value.id
  })

  // Actions
  const login = async (email, password) => {
    isLoading.value = true
    loginError.value = null

    try {
      const response = await apiClient.post('/auth/login', {
        email: email,
        pw: password
      })

      // Store the access token
      accessToken.value = response.data.accessToken

      // Store user information
      userInfo.value = {
        id: response.data.user.id,
        email: response.data.user.email,
        nickname: response.data.user.nickname,
        gender: response.data.user.gender,
        age: response.data.user.age
      }

      // Store in localStorage for persistence
      localStorage.setItem('accessToken', response.data.accessToken)
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value))

      return { success: true, user: userInfo.value }

    } catch (error) {
      console.error('Login error:', error)

      if (error.response?.status === 401) {
        loginError.value = '이메일 또는 비밀번호가 올바르지 않습니다.'
      } else if (error.response) {
        loginError.value = '로그인 중 오류가 발생했습니다.'
      } else {
        loginError.value = '네트워크 오류가 발생했습니다.'
      }

      return { success: false, error: loginError.value }
    } finally {
      isLoading.value = false
    }
  }

  const logout = () => {
    // Clear state
    userInfo.value = {
      id: null,
      email: null,
      nickname: null,
      gender: null,
      age: null
    }
    accessToken.value = null
    loginError.value = null

    // Clear localStorage
    localStorage.removeItem('accessToken')
    localStorage.removeItem('userInfo')
  }

  const initializeAuth = () => {
    // Check if user was previously logged in
    const storedToken = localStorage.getItem('accessToken')
    const storedUserInfo = localStorage.getItem('userInfo')

    if (storedToken && storedUserInfo) {
      try {
        accessToken.value = storedToken
        userInfo.value = JSON.parse(storedUserInfo)
      } catch (error) {
        console.error('Error parsing stored user info:', error)
        // Clear corrupted data
        logout()
      }
    }
  }

  const updateUserInfo = (newUserInfo) => {
    userInfo.value = { ...userInfo.value, ...newUserInfo }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  const clearError = () => {
    loginError.value = null
  }

  // Check if token is expired (basic check)
  const isTokenExpired = () => {
    if (!accessToken.value) return true

    try {
      const payload = JSON.parse(atob(accessToken.value.split('.')[1]))
      const currentTime = Math.floor(Date.now() / 1000)
      return payload.exp < currentTime
    } catch (error) {
      console.error('Error checking token expiration:', error)
      return true
    }
  }

  const validateSession = () => {
    if (isTokenExpired()) {
      logout()
      return false
    }
    return true
  }

  // API request helper with auth header (using axios)
  const authenticatedRequest = (config = {}) => {
    if (!validateSession()) {
      throw new Error('Session expired')
    }

    return apiClient({
      ...config,
      headers: {
        'Authorization': `Bearer ${accessToken.value}`,
        ...config.headers
      }
    })
  }

  return {
    // State
    userInfo,
    accessToken,
    loginError,
    isLoading,

    // Computed
    isLoggedIn,

    // Actions
    login,
    logout,
    initializeAuth,
    updateUserInfo,
    clearError,
    isTokenExpired,
    validateSession,
    authenticatedRequest
  }
})
