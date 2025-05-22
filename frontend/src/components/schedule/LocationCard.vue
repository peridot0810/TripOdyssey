<template>
  <v-card class="location-card" elevation="0" flat>
    <div class="image-container">
      <v-img
        v-if="location.firstImage1"
        :src="location.firstImage1"
        :alt="location.title"
        height="100%"
        cover
      >
        <template v-slot:placeholder>
          <div class="d-flex align-center justify-center fill-height">
            <v-progress-circular indeterminate color="primary" size="small"></v-progress-circular>
          </div>
        </template>
      </v-img>
      <div v-else class="no-image-placeholder d-flex flex-column align-center justify-center">
        <v-icon color="grey-lighten-1" size="small">mdi-image-off</v-icon>
        <span class="text-caption text-grey-lighten-1 mt-1">이미지 없음</span>
      </div>
    </div>

    <v-card-item class="pa-2">
      <v-card-title class="text-body-2 font-weight-bold pa-0 text-truncate">
        {{ location.title }}
      </v-card-title>

      <v-card-text class="pa-0 mt-1">
        <span class="text-caption text-grey">{{ location.addr1 }}</span>
      </v-card-text>
    </v-card-item>
  </v-card>
</template>

<script setup>
defineProps({
  location: {
    type: Object,
    required: true,
  },
})
</script>

<style scoped>
.location-card {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.image-container {
  height: 80px;
  overflow: hidden;
  flex-shrink: 0;
}

.no-image-placeholder {
  height: 80px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}
</style>
