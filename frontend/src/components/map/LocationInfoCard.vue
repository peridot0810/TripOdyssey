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
      <div class="location-phone">
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
      <div v-if="locationData.attractionInfo.overview" class="location-overview mt-4">
        <v-divider class="mb-3"></v-divider>
        <div class="info-label mb-2">상세 정보</div>
        <p class="text-body-2 text-grey-darken-1">
          {{ locationData.attractionInfo.overview }}
        </p>
      </div>
    </v-card-item>
  </v-card>
</template>

<script setup>
defineProps({
  locationData: {
    type: Object,
    required: true,
  },
})
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
}

/* Hover effects */
.location-info-card {
  transition: all 0.2s ease;
}

.location-info-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1) !important;
}
</style>
