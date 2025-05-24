import axios from 'axios'

// API Base URL from environment variable
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// Create axios instance with default configuration
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  },
  timeout: 10000 // 10 seconds timeout
})

// Request interceptor - can be used to add auth tokens automatically
apiClient.interceptors.request.use(
  (config) => {
    // You can add common request logic here
    // For example, adding auth token to all requests
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor - can be used for common error handling
apiClient.interceptors.response.use(
  (response) => {
    // Any status code that lie within the range of 2xx cause this function to trigger
    return response
  },
  (error) => {
    // Any status codes that falls outside the range of 2xx cause this function to trigger

    // Handle common errors globally
    if (error.response?.status === 401) {
      // Token expired or invalid - redirect to login
      localStorage.removeItem('accessToken')
      localStorage.removeItem('userInfo')
      // You can dispatch a logout action here if needed
      // Or redirect to login page
      window.location.href = '/login'
    }

    return Promise.reject(error)
  }
)

export default apiClient
