<template>
  <v-card class="location-info-card" elevation="2">
    <!-- 1. Location Image -->
    <div class="image-container">
      <v-img
        v-if="locationData.attractionInfo.firstImage1"
        :src="locationData.attractionInfo.firstImage1"
        :alt="locationData.attractionInfo.title"
        height="200"
        cover
        class="location-image"
      >
        <template v-slot:placeholder>
          <div class="d-flex align-center justify-center fill-height">
            <v-progress-circular indeterminate color="primary"></v-progress-circular>
          </div>
        </template>
      </v-img>
      <div v-else class="no-image-placeholder">
        <v-icon color="grey-lighten-1" size="large">mdi-image-off</v-icon>
        <span class="text-caption text-grey-lighten-1 mt-2">이미지 없음</span>
      </div>
    </div>

    <!-- Location Information -->
    <v-card-item class="location-details">
      <!-- 2. Location Name -->
      <v-card-title class="location-title text-h6 font-weight-bold pa-0 mb-2">
        {{ locationData.attractionInfo.title }}
      </v-card-title>

      <!-- 3. Location Type -->
      <div class="location-type mb-3">
        <v-chip size="small" color="primary" variant="tonal" prepend-icon="mdi-tag">
          {{ locationData.attractionInfo.contentTypeName }}
        </v-chip>
      </div>

      <!-- 4. Address -->
      <div class="location-address mb-3">
        <div class="info-row">
          <v-icon size="small" color="grey-darken-1" class="info-icon"> mdi-map-marker </v-icon>
          <div class="info-content">
            <div class="info-label">주소</div>
            <div class="info-value">
              {{ locationData.attractionInfo.addr1 }}
            </div>
          </div>
        </div>
      </div>

      <!-- 5. Phone Number -->
      <div class="location-phone mb-3">
        <div class="info-row">
          <v-icon size="small" color="grey-darken-1" class="info-icon"> mdi-phone </v-icon>
          <div class="info-content">
            <div class="info-label">전화번호</div>
            <div class="info-value">
              <span v-if="locationData.attractionInfo.tel">
                {{ locationData.attractionInfo.tel }}
              </span>
              <span v-else class="text-grey">정보 없음</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Optional: Overview/Description -->
      <div v-if="locationData.attractionInfo.overview" class="location-overview mb-4">
        <v-divider class="mb-3"></v-divider>
        <div class="info-label mb-2">상세 정보</div>

        <!-- Tooltip for full overview on hover -->
        <v-tooltip
          v-if="isOverviewTruncated"
          :text="locationData.attractionInfo.overview"
          location="bottom"
          max-width="400"
        >
          <template v-slot:activator="{ props }">
            <p
              v-bind="props"
              class="text-body-2 text-grey-darken-1 overview-text cursor-pointer"
            >
              {{ truncatedOverview }}...
            </p>
          </template>
        </v-tooltip>

        <!-- Show full overview if not truncated -->
        <p
          v-else
          class="text-body-2 text-grey-darken-1 overview-text"
        >
          {{ locationData.attractionInfo.overview }}
        </p>
      </div>

      <!-- Recommend Button -->
      <div class="recommend-section">
<v-btn
  variant="text"
  color="pink"
  size="large"
  :loading="isRecommending"
  :disabled="isRecommending"
  @click="handleRecommend"
  style="width: fit-content; max-width: 200px; min-width: 140px; padding: 0 20px; font-weight: 600; letter-spacing: 0.5px; text-transform: none; transition: background-color 0.2s ease; border-radius: 9999px; border: 2px solid #d81b60;"
>
  <template #prepend>
    <SvgIcon type="mdi" :path="thumbUpIcon" size="20" />
  </template>
  {{ isRecommending ? '추천 중...' : '추천하기' }}
</v-btn>




      </div>
    </v-card-item>

    <!-- Success Notification Toast -->
    <v-snackbar
      v-model="showSuccessNotification"
      location="bottom right"
      color="success"
      timeout="3000"
      class="success-toast"
    >
      <div class="d-flex align-center">
        <v-icon class="mr-2">mdi-check-circle</v-icon>
        <div>
          <div class="font-weight-medium">추천 완료!</div>
          <div class="text-caption">제안 ID: {{ proposalId }}</div>
        </div>
      </div>
    </v-snackbar>
  </v-card>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { apiClient } from '@/utils/apiClient'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiThumbUp } from '@mdi/js'

const thumbUpIcon = mdiThumbUp


const props = defineProps({
  locationData: {
    type: Object,
    required: true,
  },
})

// Get current route to extract groupId from path params
const route = useRoute()

// Loading state for recommend button
const isRecommending = ref(false)

// Success notification state
const showSuccessNotification = ref(false)
const proposalId = ref(null)

// Calculate truncated overview (50 words max)
const truncatedOverview = computed(() => {
  if (!props.locationData.attractionInfo.overview) return ''

  const words = props.locationData.attractionInfo.overview.split(' ')
  return words.slice(0, 50).join(' ')
})

// Check if overview is truncated
const isOverviewTruncated = computed(() => {
  if (!props.locationData.attractionInfo.overview) return false

  const words = props.locationData.attractionInfo.overview.split(' ')
  return words.length > 50
})

// Handle recommend button click
const handleRecommend = async () => {
  try {
    isRecommending.value = true

    // Get groupId from route params
    const groupId = route.params.groupId
    if (!groupId) {
      console.error('No groupId found in route params')
      return
    }

    console.log('Recommending attraction:', props.locationData.attractionInfo.no, 'for group:', groupId)

    // Make API call to recommend the attraction
    const response = await apiClient.post(`/schedule/${groupId}/recommend`,
      props.locationData.attractionInfo.no, // Send attraction no directly as request body
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )

    if (response.data.success) {
      proposalId.value = response.data.data.proposalId
      showSuccessNotification.value = true

      console.log('Recommendation successful! Proposal ID:', proposalId.value)

      // Hide notification after 3 seconds
      setTimeout(() => {
        showSuccessNotification.value = false
        proposalId.value = null
      }, 3000)
    } else {
      console.error('Recommendation failed:', response.data.message)
    }

  } catch (error) {
    console.error('Error recommending attraction:', error)

    // You could show an error notification here too
    // For now, just log the error

  } finally {
    isRecommending.value = false
  }
}
</script>

<style scoped>
.location-info-card {
  width: 100%;
  overflow: hidden;
}

.image-container {
  position: relative;
  width: 100%;
}

.location-image {
  width: 100%;
}

.no-image-placeholder {
  height: 200px;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #e0e0e0;
}

.location-details {
  padding: 16px;
}

.location-title {
  color: #1976d2;
  line-height: 1.3;
  word-break: keep-all;
}

.location-type {
  display: flex;
  align-items: center;
}

.info-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.info-icon {
  margin-top: 2px;
  flex-shrink: 0;
}

.info-content {
  flex: 1;
  min-width: 0;
}

.info-label {
  font-size: 12px;
  color: #666;
  font-weight: 500;
  margin-bottom: 4px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-value {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
  word-break: keep-all;
}

.location-overview {
  border-top: 1px solid #f0f0f0;
  padding-top: 16px;
}

.overview-text {
  margin-bottom: 0;
  line-height: 1.5;
}

.overview-text.cursor-pointer {
  cursor: pointer;
  transition: color 0.2s ease;
}

.recommend-section {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.recommend-btn {
  font-weight: 600;
  letter-spacing: 0.5px;
  text-transform: none;
  transition: background-color 0.2s ease;
}

.recommend-btn:hover {
  background-color: rgba(25, 118, 210, 0.08); /* subtle hover */
}


.success-toast {
  z-index: 9999;
}

.success-toast .v-snackbar__wrapper {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .location-details {
    padding: 12px;
  }

  .location-title {
    font-size: 1.1rem;
  }

  .info-value {
    font-size: 13px;
  }

  .no-image-placeholder {
    height: 150px;
  }

  .location-image {
    height: 150px;
  }

  .recommend-btn {
    font-size: 14px;
  }
}
</style>
