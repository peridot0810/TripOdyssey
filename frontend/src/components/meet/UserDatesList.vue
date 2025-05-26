<template>
  <div class="user-dates-list">
    <!-- Loading State -->
    <div v-if="isLoading" class="loading-state">
      <v-progress-circular indeterminate size="small" color="primary"></v-progress-circular>
      <span class="ml-2 text-caption">Loading your dates...</span>
    </div>

    <!-- No Dates State -->
    <div v-else-if="userDates.length === 0" class="no-dates-state">
      <v-icon size="48" color="grey-lighten-1" class="mb-2">mdi-calendar-blank</v-icon>
      <h3 class="text-subtitle-1 text-grey mb-2">No Available Dates</h3>
      <p class="text-caption text-grey text-center">
        You haven't added any available dates yet.<br>
        Switch to date picker to add some!
      </p>
    </div>

    <!-- Dates List -->
    <div v-else class="dates-container">
      <div class="list-header mb-3">
        <h3 class="text-subtitle-1 font-weight-medium">
          Your Available Dates ({{ userDates.length }})
        </h3>
      </div>

      <div class="dates-list">
        <v-card
          v-for="dateRange in userDates"
          :key="dateRange.id"
          class="date-item mb-2"
          elevation="1"
          :class="{ 'deleting': deletingIds.has(dateRange.id) }"
        >
          <v-card-text class="pa-3">
            <div class="d-flex justify-space-between align-center">
              <div class="date-info">
                <div class="date-range text-body-2 font-weight-medium">
                  {{ formatDateRange(dateRange.startDate, dateRange.endDate) }}
                </div>
                <div class="date-details text-caption text-grey">
                  {{ formatDateDetails(dateRange.startDate, dateRange.endDate) }}
                </div>
              </div>

              <v-btn
                icon
                size="small"
                color="error"
                variant="text"
                :loading="deletingIds.has(dateRange.id)"
                :disabled="deletingIds.has(dateRange.id)"
                @click="deleteDate(dateRange)"
              >
                <svg-icon type="mdi" :path="deletePath" size="20"></svg-icon>
              </v-btn>
            </div>
          </v-card-text>
        </v-card>
      </div>
    </div>

    <!-- Error Alert -->
    <v-alert
      v-if="errorMessage"
      type="error"
      class="mt-3"
      closable
      @click:close="clearError"
    >
      {{ errorMessage }}
    </v-alert>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useWhen2MeetStore } from '@/stores/when2meet'
import { apiClient } from '@/utils/apiClient'

import SvgIcon from '@jamescoyle/vue-icon'
import { mdiDelete } from '@mdi/js'

const deletePath = mdiDelete

// Stores
const userStore = useUserStore()
const when2MeetStore = useWhen2MeetStore()

// State
const isLoading = ref(false)
const errorMessage = ref('')
const deletingIds = ref(new Set())

// Get user's available dates from store
const userDates = computed(() => {
  if (!when2MeetStore.availabilityData || !userStore.userInfo.id) {
    return []
  }

  // Filter availability data for current user
  return when2MeetStore.availabilityData.filter(
    item => item.userId === userStore.userInfo.id
  ).sort((a, b) => new Date(a.startDate) - new Date(b.startDate)) // Sort by start date
})

// Methods
const deleteDate = async (dateRange) => {
  const dateId = dateRange.id

  deletingIds.value.add(dateId)
  clearMessages()

  try {
    console.log('Deleting availability with ID:', dateId)

    const response = await apiClient.delete(`/meet/schedule/${dateId}`)

    if (response.data.success) {
      // Remove from store
      const updatedData = when2MeetStore.availabilityData.filter(
        item => item.id !== dateId
      )
      when2MeetStore.setAvailabilityData(updatedData)

      console.log('Availability deleted successfully')
    } else {
      errorMessage.value = response.data.message || 'Failed to delete availability'
    }
  } catch (error) {
    console.error('Error deleting availability:', error)

    if (error.response?.status === 401) {
      errorMessage.value = 'Authentication failed. Please login again.'
      userStore.logout()
    } else if (error.response?.status === 403) {
      errorMessage.value = 'Access denied. You can only delete your own availability.'
    } else if (error.response?.status === 404) {
      errorMessage.value = 'Availability not found. It may have been already deleted.'
      // Remove from store anyway
      const updatedData = when2MeetStore.availabilityData.filter(
        item => item.id !== dateId
      )
      when2MeetStore.setAvailabilityData(updatedData)
    } else if (error.response?.status >= 500) {
      errorMessage.value = 'Server error. Please try again later.'
    } else if (error.response) {
      errorMessage.value = error.response.data?.message || 'Failed to delete availability'
    } else {
      errorMessage.value = 'Network error. Please check your connection.'
    }
  } finally {
    deletingIds.value.delete(dateId)
  }
}

const formatDateRange = (startDate, endDate) => {
  if (!startDate || !endDate) return ''

  const start = new Date(startDate)
  const end = new Date(endDate)

  const formatDate = (date) => {
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    })
  }

  if (start.toDateString() === end.toDateString()) {
    return formatDate(start)
  } else {
    return `${formatDate(start)} → ${formatDate(end)}`
  }
}

const formatDateDetails = (startDate, endDate) => {
  if (!startDate || !endDate) return ''

  const start = new Date(startDate)
  const end = new Date(endDate)

  const diffTime = Math.abs(end - start)
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1 // +1 because both dates are inclusive

  if (diffDays === 1) {
    return '1 day'
  } else {
    return `${diffDays} days`
  }
}

const clearMessages = () => {
  errorMessage.value = ''
}

const clearError = () => {
  errorMessage.value = ''
}

// Lifecycle
onMounted(() => {
  console.log('UserDatesList mounted for user:', userStore.userInfo.id)
  console.log('User dates found:', userDates.value.length)
})
</script>

<style scoped>
.user-dates-list {
  width: 100%;
  max-width: 400px;
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  font-family: 'Segoe UI', sans-serif;
}

.loading-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  text-align: center;
}

.no-dates-state {
  text-align: center;
  padding: 40px 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.dates-container {
  max-height: 400px;
  overflow-y: auto;
}

.list-header {
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 8px;
}

.dates-list {
  padding-right: 4px; /* Space for scrollbar */
}

.date-item {
  transition: all 0.2s ease;
  border: 1px solid #e0e0e0;
}

.date-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12) !important;
}

.date-item.deleting {
  opacity: 0.6;
  pointer-events: none;
}

.date-info {
  flex: 1;
  min-width: 0; /* Allow text to truncate */
}

.date-range {
  color: #1976d2;
  margin-bottom: 4px;
}

.date-details {
  font-style: italic;
}

/* Custom scrollbar */
.dates-container::-webkit-scrollbar {
  width: 6px;
}

.dates-container::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 3px;
}

.dates-container::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* Responsive design */
@media (max-width: 480px) {
  .user-dates-list {
    padding: 12px;
  }

  .no-dates-state {
    padding: 30px 15px;
  }

  .loading-state {
    padding: 30px 15px;
  }
}
</style>
