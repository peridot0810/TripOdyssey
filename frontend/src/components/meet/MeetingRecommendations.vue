<template>
  <v-card elevation="1" class="mt-3">
    <v-card-text class="pa-3">
      <!-- Loading State -->
      <div v-if="when2MeetStore.isLoading" class="text-center py-4">
        <v-progress-circular indeterminate size="small" color="primary"></v-progress-circular>
        <div class="text-caption mt-2">추천 날짜 계산 중...</div>
      </div>

      <!-- No Data State -->
      <div v-else-if="!hasAvailabilityData" class="text-center py-4">
        <v-icon size="32" color="grey-lighten-1" class="mb-2">mdi-calendar-question</v-icon>
        <div class="text-body-2 text-grey">아직 가능한 날짜 데이터가 없습니다</div>
      </div>

      <!-- Recommendations Display -->
      <div v-else class="recommendations-container">
        <!-- Best Dates -->
        <div class="stat-item mb-3 text-center">
          <div class="stat-label text-caption text-grey-darken-1 mb-1">최적 날짜</div>
          <div class="stat-number text-h6 text-primary font-weight-bold">
            {{ bestDatesDisplay }}
          </div>
        </div>

        <!-- Max Available -->
        <div class="stat-item mb-3 text-center">
          <div class="stat-label text-caption text-grey-darken-1 mb-1">최대 참석자</div>
          <div class="stat-number text-h6 text-success font-weight-bold">
            {{ maxAvailableDisplay }}
          </div>
        </div>

        <!-- Total Responses -->
        <div class="stat-item mb-3 text-center">
          <div class="stat-label text-caption text-grey-darken-1 mb-1">총 응답</div>
          <div class="stat-number text-h6 text-warning font-weight-bold">
            {{ totalResponsesDisplay }}
          </div>
        </div>

        <!-- Detailed Best Dates (if more than 3) -->
        <div v-if="recommendations.bestDates.length > 3" class="mt-3 pt-3 border-top text-center">
          <div class="stat-label text-caption text-grey-darken-1 mb-2">모든 최적 날짜</div>
          <div class="best-dates-list">
            <v-chip
              v-for="date in recommendations.bestDates.slice(0, 10)"
              :key="date"
              size="small"
              color="primary"
              variant="outlined"
              class="ma-1"
            >
              {{ formatDateChip(date) }}
            </v-chip>
            <span v-if="recommendations.bestDates.length > 10" class="text-caption text-grey ml-2">
              +{{ recommendations.bestDates.length - 10 }}개 더
            </span>
          </div>
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
    return '아직 없음'
  }

  if (bestDates.length === 1) {
    return formatDate(bestDates[0])
  }

  if (bestDates.length <= 3) {
    return bestDates.map(date => formatDate(date)).join(', ')
  }

  // Show first 2 dates + count
  const firstTwo = bestDates.slice(0, 2).map(date => formatDate(date)).join(', ')
  return `${firstTwo} +${bestDates.length - 2}개`
})

// Format max available display
const maxAvailableDisplay = computed(() => {
  const max = recommendations.value.maxAvailable
  if (max === 0) return '없음'
  return `${max}명`
})

// Format total responses display
const totalResponsesDisplay = computed(() => {
  const total = recommendations.value.totalResponses
  if (total === 0) return '없음'
  return `${total}개`
})

// Helper functions
function formatDate(dateString) {
  if (!dateString) return ''

  try {
    const date = new Date(dateString)
    return date.toLocaleDateString('ko-KR', {
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
    return date.toLocaleDateString('ko-KR', {
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
.recommendations-container {
  display: flex;
  flex-direction: column;
}

.stat-item {
  padding: 8px 12px;
  border-radius: 8px;
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  transition: all 0.2s ease;
}

.stat-item:hover {
  background-color: #f0f0f0;
  border-color: #dee2e6;
}

.stat-label {
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-weight: 600;
  display: block;
}

.stat-number {
  line-height: 1.3;
  display: block;
  text-align: center;
}

.border-top {
  border-top: 1px solid #e9ecef !important;
}

.best-dates-list {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  align-items: center;
  justify-content: center;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .stat-item {
    padding: 6px 10px;
    margin-bottom: 8px;
  }

  .stat-number {
    font-size: 1rem !important;
  }

  .best-dates-list {
    justify-content: flex-start;
  }
}

@media (max-width: 480px) {
  .recommendations-container {
    padding: 0;
  }

  .stat-item {
    padding: 8px;
  }
}
</style>
