<template>
  <v-card class="h-100" elevation="2">
    <v-card-title class="bg-primary text-white pa-3">
      <v-icon left class="mr-2">mdi-calendar-range</v-icon>
      <span class="text-subtitle-1">Your Availability</span>
    </v-card-title>

    <!-- Navigation Tabs -->
    <div class="navigation-tabs">
      <v-btn
        :variant="currentView === 'picker' ? 'flat' : 'text'"
        :color="currentView === 'picker' ? 'primary' : 'default'"
        size="small"
        class="tab-button"
        @click="currentView = 'picker'"
      >
        <v-icon left size="small">mdi-calendar-plus</v-icon>
        날짜 선택
      </v-btn>

      <v-btn
        :variant="currentView === 'list' ? 'flat' : 'text'"
        :color="currentView === 'list' ? 'primary' : 'default'"
        size="small"
        class="tab-button"
        @click="currentView = 'list'"
      >
        <v-icon left size="small">mdi-format-list-bulleted</v-icon>
        나의 가능한 날짜
      </v-btn>
    </div>

    <v-card-text class="pa-3 d-flex flex-column align-center">
      <!-- Date Picker View -->
      <div v-if="currentView === 'picker'" class="picker-container">
        <div class="date-picker-wrapper">
          <DatePicker
            ref="datePickerRef"
            @date-range-selected="handleDateRangeSelected"
            @selection-cleared="handleSelectionCleared"
          />
        </div>

        <!-- Selected Date Range Display -->
        <div v-if="selectedDateRange" class="selected-range-display mb-3">
          <v-chip color="primary" variant="outlined" class="mb-2">
            <v-icon left size="small">mdi-calendar-check</v-icon>
            Selected Range
          </v-chip>
          <div class="range-text">
            {{ formatDateRange(selectedDateRange.startDate, selectedDateRange.endDate) }}
          </div>
        </div>

        <!-- Save Button -->
        <v-btn
          color="primary"
          block
          class="mt-3"
          prepend-icon="mdi-check"
          variant="flat"
          size="small"
          :disabled="!selectedDateRange || isLoading"
          :loading="isLoading"
          @click="saveAvailability"
        >
          {{ isLoading ? 'Saving...' : 'Save Availability' }}
        </v-btn>

        <!-- Error Display -->
        <v-alert
          v-if="errorMessage"
          type="error"
          class="mt-3"
          closable
          @click:close="clearError"
        >
          {{ errorMessage }}
        </v-alert>

        <!-- Success Display -->
        <v-alert
          v-if="successMessage"
          type="success"
          class="mt-3"
          closable
          @click:close="clearSuccess"
        >
          {{ successMessage }}
        </v-alert>
      </div>

      <!-- User Dates List View -->
      <div v-else-if="currentView === 'list'" class="list-container">
        <UserDatesList />
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useWhen2MeetStore } from '@/stores/when2meet'
import { apiClient } from '@/utils/apiClient'
import DatePicker from '@/components/meet/DatePicker.vue'
import UserDatesList from '@/components/meet/UserDatesList.vue'

// Stores
const userStore = useUserStore()
const when2MeetStore = useWhen2MeetStore()
const route = useRoute()

// Refs
const datePickerRef = ref(null)

// State
const currentView = ref('picker') // 'picker' or 'list'
const selectedDateRange = ref(null)
const isLoading = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

// Get group ID from route params
const groupId = route.params.groupId

// Event handlers
const handleDateRangeSelected = (dateRange) => {
  selectedDateRange.value = dateRange
  clearMessages()
}

const handleSelectionCleared = () => {
  selectedDateRange.value = null
  clearMessages()
}

// API Methods
const saveAvailability = async () => {
  if (!selectedDateRange.value) {
    errorMessage.value = 'Please select a date range first'
    return
  }

  // Check if user is logged in
  if (!userStore.isLoggedIn) {
    errorMessage.value = 'Please login to save availability'
    return
  }

  if (!userStore.userInfo.id) {
    errorMessage.value = 'User information not available'
    return
  }

  if (!groupId) {
    errorMessage.value = 'Group ID not found'
    return
  }

  // Validate session before making request
  if (!userStore.validateSession()) {
    errorMessage.value = 'Session expired. Please login again.'
    return
  }

  isLoading.value = true
  clearMessages()

  try {
    const requestBody = {
      userId: userStore.userInfo.id,
      groupId: groupId,
      startDate: formatDateForAPI(selectedDateRange.value.startDate),
      endDate: formatDateForAPI(selectedDateRange.value.endDate)
    }

    console.log('Sending availability request:', requestBody)

    const response = await apiClient.post('/meet/schedule', requestBody)

    if (response.data.success) {
      successMessage.value = response.data.message || 'Availability saved successfully!'

      // Clear the date selection
      selectedDateRange.value = null
      if (datePickerRef.value) {
        datePickerRef.value.clearSelection()
      }

      // Refresh the when2meet store data to update the team calendar
      // We'll refetch from the main endpoint instead of individual calls
      try {
        const response = await apiClient.get(`/meet/schedule/all/${groupId}`)
        if (response.data.success) {
          const availabilityData = response.data.data.map(item => ({
            id: item.id,
            userId: item.userId || item.user_id,
            groupId: item.groupId || item.group_id || groupId,
            startDate: item.startDate || item.start_date,
            endDate: item.endDate || item.end_date
          }))
          when2MeetStore.setAvailabilityData(availabilityData)
        }
      } catch (refreshError) {
        console.error('Error refreshing data after save:', refreshError)
        // Don't show error to user - save was successful
      }

      console.log('Availability saved successfully:', response.data.data)
    } else {
      errorMessage.value = response.data.message || 'Failed to save availability'
    }
  } catch (error) {
    console.error('Error saving availability:', error)

    if (error.response?.status === 401) {
      errorMessage.value = 'Authentication failed. Please login again.'
      // Optionally redirect to login or logout user
      userStore.logout()
    } else if (error.response?.status === 400) {
      errorMessage.value = error.response.data?.message || 'Invalid request data'
    } else if (error.response?.status === 500) {
      errorMessage.value = 'Server error. Please try again later.'
    } else if (error.response) {
      errorMessage.value = error.response.data?.message || 'Failed to save availability'
    } else {
      errorMessage.value = 'Network error. Please check your connection.'
    }
  } finally {
    isLoading.value = false
  }
}

// Helper methods
const formatDateForAPI = (date) => {
  if (!date) return ''

  // Ensure we have a Date object
  const dateObj = date instanceof Date ? date : new Date(date)

  // Format as YYYY-MM-DD
  const year = dateObj.getFullYear()
  const month = String(dateObj.getMonth() + 1).padStart(2, '0')
  const day = String(dateObj.getDate()).padStart(2, '0')

  return `${year}-${month}-${day}`
}

const formatDateRange = (startDate, endDate) => {
  if (!startDate || !endDate) return ''

  const formatDate = (date) => {
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    })
  }

  if (startDate.toDateString() === endDate.toDateString()) {
    return formatDate(startDate)
  } else {
    return `${formatDate(startDate)} → ${formatDate(endDate)}`
  }
}

const clearMessages = () => {
  errorMessage.value = ''
  successMessage.value = ''
}

const clearError = () => {
  errorMessage.value = ''
}

const clearSuccess = () => {
  successMessage.value = ''
}

// Lifecycle
onMounted(async () => {
  console.log('UserAvailability mounted for group:', groupId)

  // Initialize user authentication if not already done
  if (!userStore.isLoggedIn) {
    userStore.initializeAuth()
  }

  // Check if user is authenticated after initialization
  if (!userStore.isLoggedIn) {
    errorMessage.value = 'Please login to access this feature'
    return
  }

  // Initialize the when2meet store for this group
  if (groupId) {
    when2MeetStore.setCurrentGroup(groupId)

    // Load existing availability data
    try {
      await when2MeetStore.fetchTeamAvailability(groupId)
      await when2MeetStore.fetchUserAvailability(groupId)
    } catch (error) {
      console.error('Error loading initial availability data:', error)
      // Don't show error to user for initial data load failure
    }
  } else {
    errorMessage.value = 'Group not found'
  }
})
</script>

<style scoped>
.navigation-tabs {
  display: flex;
  padding: 8px 12px;
  background-color: #f8f9fa;
  border-bottom: 1px solid #e0e0e0;
  gap: 8px;
}

.tab-button {
  flex: 1;
  font-size: 0.8rem;
  text-transform: none;
}

.picker-container,
.list-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.date-picker-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.selected-range-display {
  text-align: center;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  width: 100%;
}

.range-text {
  font-size: 0.9rem;
  font-weight: 500;
  color: #333;
  margin-top: 4px;
}

.v-alert {
  width: 100%;
}

/* Responsive design */
@media (max-width: 768px) {
  .tab-button {
    font-size: 0.75rem;
    padding: 8px 12px;
  }

  .navigation-tabs {
    padding: 6px 8px;
    gap: 4px;
  }
}
</style>
