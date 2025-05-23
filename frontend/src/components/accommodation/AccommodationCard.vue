<template>
  <v-card class="accommodation-card" elevation="2">
    <div class="card-container d-flex">
      <!-- Left Content Section -->
      <div class="content-section flex-grow-1">
        <v-card-item class="pa-4">
          <!-- Accommodation Name/Title -->
          <v-card-title class="text-h6 font-weight-bold pa-0 mb-2">
            {{ accommodation.accommodationInfo.title }}
          </v-card-title>

          <!-- Custom Name (if different from title) -->
          <div
            v-if="accommodation.name !== accommodation.accommodationInfo.title"
            class="custom-name mb-3"
          >
            <v-chip size="small" color="secondary" variant="outlined" class="text-caption">
              {{ accommodation.name }}
            </v-chip>
          </div>

          <!-- Check-in and Check-out Times -->
          <div class="time-section mb-3">
            <div class="time-labels d-flex justify-space-between mb-1">
              <span class="text-caption text-grey-darken-1">체크인</span>
              <span class="text-caption text-grey-darken-1">체크아웃</span>
            </div>
            <div class="time-values d-flex justify-space-between align-center">
              <span class="text-body-1 font-weight-medium">{{
                formatTime(accommodation.checkInTime)
              }}</span>
              <v-icon color="primary" size="small" class="mx-2">mdi-arrow-right</v-icon>
              <span class="text-body-1 font-weight-medium">{{
                formatTime(accommodation.checkOutTime)
              }}</span>
            </div>
          </div>

          <!-- Contact Information -->
          <div v-if="accommodation.accommodationInfo.tel" class="contact-section mb-2">
            <div class="d-flex align-center">
              <v-icon size="small" color="grey-darken-1" class="mr-2">mdi-phone</v-icon>
              <span class="text-body-2">{{ accommodation.accommodationInfo.tel }}</span>
            </div>
          </div>

          <!-- Homepage -->
          <div v-if="accommodation.accommodationInfo.homepage" class="homepage-section mb-2">
            <div class="d-flex align-center">
              <v-icon size="small" color="grey-darken-1" class="mr-2">mdi-web</v-icon>
              <a
                :href="extractUrl(accommodation.accommodationInfo.homepage)"
                target="_blank"
                class="text-body-2 text-primary text-decoration-none"
              >
                홈페이지 방문
              </a>
            </div>
          </div>

          <!-- Address -->
          <div class="address-section mb-3">
            <div class="d-flex align-start">
              <v-icon size="small" color="grey-darken-1" class="mr-2 mt-1">mdi-map-marker</v-icon>
              <div>
                <div class="text-body-2">{{ accommodation.accommodationInfo.addr1 }}</div>
                <div
                  v-if="accommodation.accommodationInfo.addr2"
                  class="text-caption text-grey-darken-1"
                >
                  {{ accommodation.accommodationInfo.addr2 }}
                </div>
              </div>
            </div>
          </div>

          <!-- Divider -->
          <v-divider class="mb-3"></v-divider>

          <!-- Description/Overview -->
          <div v-if="accommodation.accommodationInfo.overview" class="description-section">
            <p class="text-body-2 text-grey-darken-1 mb-0 description-text">
              {{ accommodation.accommodationInfo.overview }}
            </p>
          </div>
          <div v-else class="description-section">
            <p class="text-body-2 text-grey-lighten-1 mb-0 font-italic">
              숙소 정보가 제공되지 않았습니다.
            </p>
          </div>
        </v-card-item>
      </div>

      <!-- Right Image Section -->
      <div class="image-section">
        <v-img
          v-if="accommodation.accommodationInfo.firstImage1"
          :src="accommodation.accommodationInfo.firstImage1"
          :alt="accommodation.accommodationInfo.title"
          height="100%"
          width="160"
          cover
          class="accommodation-image"
        >
          <template v-slot:placeholder>
            <div class="d-flex align-center justify-center fill-height">
              <v-progress-circular indeterminate color="primary"></v-progress-circular>
            </div>
          </template>
        </v-img>
        <div v-else class="no-image-placeholder d-flex flex-column align-center justify-center">
          <v-icon color="grey-lighten-1" size="large">mdi-image-off</v-icon>
          <span class="text-caption text-grey-lighten-1 mt-1">이미지 없음</span>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script setup>
defineProps({
  accommodation: {
    type: Object,
    required: true,
  },
})

// Helper function to format datetime to display format
const formatTime = (dateTimeString) => {
  const date = new Date(dateTimeString)
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${month}/${day} ${hours}:${minutes}`
}

// Helper function to extract URL from HTML anchor tag
const extractUrl = (htmlString) => {
  if (!htmlString) return ''
  const match = htmlString.match(/href="([^"]*)"/)
  return match ? match[1] : htmlString
}
</script>

<style scoped>
.accommodation-card {
  width: 100%;
  border-radius: 12px;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  border: 1px solid #e0e0e0;
  overflow: hidden;
}

.accommodation-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.12) !important;
}

.card-container {
  min-height: 200px;
}

.content-section {
  min-width: 0; /* Allow flex item to shrink */
}

.image-section {
  width: 160px;
  min-width: 160px;
  height: 100%;
}

.accommodation-image {
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
}

.no-image-placeholder {
  width: 160px;
  height: 100%;
  background-color: #f5f5f5;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
}

.time-section {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 12px;
}

.description-text {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .card-container {
    flex-direction: column;
  }

  .image-section {
    width: 100%;
    min-width: 100%;
    height: 180px;
    order: -1;
  }

  .accommodation-image,
  .no-image-placeholder {
    border-radius: 0;
    border-top-left-radius: 12px;
    border-top-right-radius: 12px;
  }

  .time-values {
    flex-direction: column;
    gap: 8px;
    text-align: center;
  }

  .time-labels {
    flex-direction: column;
    gap: 4px;
    text-align: center;
  }
}
</style>
