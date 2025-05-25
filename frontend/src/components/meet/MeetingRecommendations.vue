<template>
  <v-card elevation="1" class="mt-3">
    <v-card-title class="bg-success text-white pa-2">
      <v-icon left size="small" class="mr-1">mdi-chart-line</v-icon>
      <span class="text-subtitle-2">Best Meeting Times</span>
    </v-card-title>

    <v-card-text class="pa-2">
      <!-- Loading State -->
      <div v-if="when2MeetStore.isLoading" class="text-center py-2">
        <v-progress-circular indeterminate size="small" color="primary"></v-progress-circular>
        <div class="text-caption mt-1">Calculating recommendations...</div>
      </div>

      <!-- No Data State -->
      <div v-else-if="!hasAvailabilityData" class="text-center py-3">
        <v-icon size="small" color="grey-lighten-1" class="mb-1">mdi-calendar-question</v-icon>
        <div class="text-caption text-grey">No availability data yet</div>
      </div>

      <!-- Recommendations Display -->
      <v-row v-else dense>
        <v-col cols="4">
          <div class="stat-item text-center">
            <div class="stat-number text-h6 text-primary font-weight-bold">
              {{ bestDatesDisplay }}
            </div>
            <div class="stat-label text-caption text-grey-darken-1">Best Days</div>
          </div>
        </v-col>

        <v-col cols="4">
          <div class="stat-item text-center">
            <div class="stat-number text-h6 text-success font-weight-bold">
              {{ maxAvailableDisplay }}
            </div>
            <div class="stat-label text-caption text-grey-darken-1">Max Available</div>
          </div>
        </v-col>

        <v-col cols="4">
          <div class="stat-item text-center">
            <div class="stat-number text-h6 text-warning font-weight-bold">
              {{ totalResponsesDisplay }}
            </div>
            <div class="stat-label text-caption text-grey-darken-1">Responses</div>
          </div>
        </v-col>
      </v-row>

      <!-- Detailed Best Dates (if more than 3) -->
      <div v-if="hasAvailabilityData && recommendations.bestDates.length > 3" class="mt-2">
        <v-divider class="mb-2"></v-divider>
        <div class="text-caption text-grey mb-1">All Best Dates:</div>
        <div class="best-dates-list">
          <v-chip
            v-for="date in recommendations.bestDates.slice(0, 10)"
            :key="date"
            size="x-small"
            color="primary"
            variant="outlined"
            class="ma-1"
          >
            {{ formatDateChip(date) }}
          </v-chip>
          <span v-if="recommendations.bestDates.length > 10" class="text-caption text-grey">
            +{{ recommendations.bestDates.length - 10 }} more
          </span>
        </div>
      </div>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { computed } from 'vue'
import { useWhen2MeetStore } from '@/stores/when2meet'

// Store
const when2MeetStore = useWhen2MeetStore()

// Get recommendations from store
const recommendations = computed(() => when2MeetStore.meetingRecommendations)

// Check if we have any availability data
const hasAvailabilityData = computed(() => {
  return when2MeetStore.availabilityData && when2MeetStore.availabilityData.length > 0
})

// Format best dates for display
const bestDatesDisplay = computed(() => {
  const bestDates = recommendations.value.bestDates

  if (!bestDates || bestDates.length === 0) {
    return 'None yet'
  }

  if (bestDates.length === 1) {
    return formatDate(bestDates[0])
  }

  if (bestDates.length <= 3) {
    return bestDates.map(date => formatDate(date)).join(', ')
  }

  // Show first 2 dates + count
  const firstTwo = bestDates.slice(0, 2).map(date => formatDate(date)).join(', ')
  return `${firstTwo} +${bestDates.length - 2}`
})

// Format max available display
const maxAvailableDisplay = computed(() => {
  const max = recommendations.value.maxAvailable
  if (max === 0) return 'None'
  return `${max} ${max === 1 ? 'person' : 'people'}`
})

// Format total responses display
const totalResponsesDisplay = computed(() => {
  const total = recommendations.value.totalResponses
  if (total === 0) return 'None'
  return `${total} total`
})

// Helper functions
function formatDate(dateString) {
  if (!dateString) return ''

  try {
    const date = new Date(dateString)
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric'
    })
  } catch (error) {
    console.log(error)
    return dateString
  }
}

function formatDateChip(dateString) {
  if (!dateString) return ''

  try {
    const date = new Date(dateString)
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: '2-digit'
    })
  } catch (error) {
    console.log(error)
    return dateString
  }
}
</script>

<style scoped>
.stat-item {
  padding: 8px 4px;
}

.stat-number {
  line-height: 1.2;
  margin-bottom: 2px;
  min-height: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-label {
  text-transform: uppercase;
  letter-spacing: 0.3px;
  font-weight: 500;
}

.best-dates-list {
  display: flex;
  flex-wrap: wrap;
  gap: 2px;
  justify-content: center;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .stat-number {
    font-size: 0.9rem !important;
  }

  .best-dates-list {
    justify-content: flex-start;
  }
}
</style>
