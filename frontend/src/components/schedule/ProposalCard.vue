<template>
  <v-card class="proposal-card" elevation="2">
    <div class="image-container">
      <v-img
        v-if="location.firstImage1"
        :src="location.firstImage1"
        :alt="location.title"
        height="180"
        cover
        class="rounded-t-lg"
      >
        <template v-slot:placeholder>
          <div class="d-flex align-center justify-center fill-height">
            <v-progress-circular indeterminate color="primary"></v-progress-circular>
          </div>
        </template>
      </v-img>
      <div v-else class="no-image-placeholder rounded-t-lg d-flex flex-column align-center justify-center">
        <v-icon color="grey-lighten-1" size="large">mdi-image-off</v-icon>
        <span class="text-caption text-grey-lighten-1 mt-2">이미지 없음</span>
      </div>
    </div>

    <v-card-item class="pa-3">
      <v-card-title class="text-subtitle-1 font-weight-bold pa-0 text-truncate">
        {{ location.title }}
      </v-card-title>

      <v-card-text class="d-flex justify-space-between align-center pa-0 mt-1">
        <v-chip size="small" color="primary" variant="flat" class="text-caption">
          {{ location.contentTypeName }}
        </v-chip>

        <span class="text-caption text-grey">{{ location.userId }}</span>

        <div class="d-flex align-center">
          <span class="heart-icon mr-1">❤</span>
          <span class="text-caption">{{ location.likes || 0 }}</span>
        </div>
      </v-card-text>
    </v-card-item>
  </v-card>
</template>

<script setup>
defineProps({
  location: {
    type: Object,
    required: true
  }
});
</script>

<style scoped>
.proposal-card {
  width: 100%;
  transition: transform 0.2s, box-shadow 0.2s;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.proposal-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15) !important;
}

.image-container {
  height: 180px;
  overflow: hidden;
}

.heart-icon {
  color: #FF0000;
  font-size: 14px;
}

.no-image-placeholder {
  height: 180px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}
</style>
