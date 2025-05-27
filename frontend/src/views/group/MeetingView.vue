<template>
  <v-container fluid class="pa-0 meeting-container">
    <!-- Loading Overlay -->
    <div v-if="isInitialLoading" class="loading-overlay">
      <v-progress-circular
        indeterminate
        size="64"
        color="primary"
        class="mb-4"
      ></v-progress-circular>
      <div class="loading-text">Loading team availability...</div>
    </div>

    <!-- Error Alert -->
    <v-alert
      v-if="errorMessage && !isInitialLoading"
      type="error"
      class="ma-4"
      closable
      @click:close="clearError"
    >
      {{ errorMessage }}
    </v-alert>

    <!-- Main Content -->
    <v-row class="fill-height ma-0" v-if="!isInitialLoading">
      <!-- Left Panel: User Availability (Narrower) -->
      <v-col cols="12" lg="3" md="4" class="left-panel">
        <UserAvailability />
      </v-col>

      <!-- Right Panel: Team Availability Overview (Wider) -->
      <v-col cols="12" lg="9" md="8" class="right-panel">
        <TeamAvailabilityOverview />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useWhen2MeetStore } from '@/stores/when2meet'
import { useUserStore } from '@/stores/user'
import { apiClient } from '@/utils/apiClient'
import UserAvailability from '@/components/meet/UserAvailability.vue'
import TeamAvailabilityOverview from '@/components/meet/TeamAvailabilityOverview.vue'

// Stores
const when2MeetStore = useWhen2MeetStore()
const userStore = useUserStore()
const route = useRoute()

// State
const isInitialLoading = ref(true)
const errorMessage = ref('')

// Get group ID from route params
const groupId = route.params.groupId

// Methods
const fetchTeamAvailability = async () => {
  if (!groupId) {
    errorMessage.value = 'Group ID not found in URL'
    return
  }

  try {
    console.log('Fetching team availability for group:', groupId)

    // Use the correct endpoint: /meet/schedule/all/{groupId}
    const response = await apiClient.get(`/meet/schedule/all/${groupId}`)

    if (response.data.success) {
      // Transform the data to match our store format
      const availabilityData = response.data.data.map((item) => ({
        id: item.id,
        userId: item.userId || item.user_id,
        groupId: item.groupId || item.group_id || groupId,
        startDate: item.startDate || item.start_date,
        endDate: item.endDate || item.end_date,
      }))

      console.log('Fetched availability data:', availabilityData)

      // Save to Pinia store
      when2MeetStore.setAvailabilityData(availabilityData)
      when2MeetStore.setCurrentGroup(groupId)

      console.log('Team availability loaded successfully')
    } else {
      throw new Error(response.data.message || 'Failed to fetch team availability')
    }
  } catch (error) {
    console.error('Error fetching team availability:', error)

    if (error.response?.status === 401) {
      errorMessage.value = 'Authentication failed. Please login again.'
      // Optionally redirect to login
      userStore.logout()
    } else if (error.response?.status === 403) {
      errorMessage.value = 'Access denied. You may not have permission to view this group.'
    } else if (error.response?.status === 404) {
      errorMessage.value = 'Group not found or no availability data exists yet.'
      // Don't treat 404 as a hard error - just means no data yet
      when2MeetStore.setAvailabilityData([])
      when2MeetStore.setCurrentGroup(groupId)
    } else if (error.response?.status >= 500) {
      errorMessage.value = 'Server error. Please try again later.'
    } else if (error.response) {
      errorMessage.value = error.response.data?.message || 'Failed to load team availability'
    } else {
      errorMessage.value = 'Network error. Please check your connection.'
    }
  }
}

// const fetchUserAvailability = async () => {
//   if (!groupId || !userStore.isLoggedIn) {
//     return // Skip if no group or user not logged in
//   }

//   try {
//     console.log('Fetching user availability for group:', groupId)

//     // Try the correct endpoint path - adjust this based on your actual API
//     const response = await apiClient.get(`/meet/schedule/${groupId}`)

//     if (response.data.success) {
//       // Transform the data to match our store format
//       const userAvailabilityData = response.data.data.map((item) => ({
//         id: item.id,
//         startDate: item.startDate || item.start_date,
//         endDate: item.endDate || item.end_date,
//       }))

//       console.log('Fetched user availability data:', userAvailabilityData)

//       // Save to Pinia store
//       when2MeetStore.setUserAvailability(userAvailabilityData)

//       console.log('User availability loaded successfully')
//     } else {
//       console.log('No user availability data found')
//       when2MeetStore.setUserAvailability([])
//     }
//   } catch (error) {
//     console.error('Error fetching user availability:', error)
//     // Don't show error for user availability - it's okay if it doesn't exist
//     when2MeetStore.setUserAvailability([])
//   }
// }

const initializeData = async () => {
  isInitialLoading.value = true
  errorMessage.value = ''

  // Initialize user authentication if needed
  if (!userStore.isLoggedIn) {
    userStore.initializeAuth()
  }

  try {
    // Fetch team availability (required)
    await fetchTeamAvailability()

    // Fetch user availability (optional)
    if (userStore.isLoggedIn) {
      //await fetchUserAvailability()
    }
  } catch (error) {
    console.error('Error during data initialization:', error)
  } finally {
    isInitialLoading.value = false
  }
}

const clearError = () => {
  errorMessage.value = ''
}

// Lifecycle
onMounted(async () => {
  console.log('MeetingView mounted for group:', groupId)

  if (!groupId) {
    errorMessage.value = 'Invalid group ID in URL'
    isInitialLoading.value = false
    return
  }

  await initializeData()
})
</script>

<style scoped>
.meeting-container {
  min-height: calc(100vh - 64px);
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
  position: relative;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(5px);
  z-index: 1000;
}

.loading-text {
  font-size: 1.1rem;
  font-weight: 500;
  color: #666;
  text-align: center;
}

.left-panel,
.right-panel {
  padding: 8px;
}

/* Responsive adjustments */
@media (max-width: 1264px) {
  .meeting-container {
    padding: 16px;
  }

  .left-panel,
  .right-panel {
    margin-bottom: 16px;
  }
}

@media (max-width: 960px) {
  .loading-text {
    font-size: 1rem;
  }
}

@media (max-width: 600px) {
  .meeting-container {
    padding: 12px;
  }
}
</style>
