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
      <v-chip
        size="small"
        variant="outlined"
        color="primary"
        class="text-caption font-weight-bold"
      >
        {{ location.contentTypeName }}
      </v-chip>


        <span class="text-caption text-grey">{{ location.userId }}</span>

        <v-btn
          size="small"
          :color="location.userLiked ? 'pink' : 'pink'"
          variant="elevated"
          class="like-button"
          @click="handleLikeClick"
          :loading="isLiking"
        >
          <template #prepend>
            <SvgIcon type="mdi" :path="location.userLiked ? heartPath : heartOutlinePath" size="16" />
          </template>
          <span class="text-caption">{{ location.likes || 0 }}</span>
        </v-btn>
      </v-card-text>
    </v-card-item>
  </v-card>
</template>

<script setup>
import { ref } from 'vue'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiHeart, mdiHeartOutline } from '@mdi/js'

const heartPath = mdiHeart
const heartOutlinePath = mdiHeartOutline

const props = defineProps({
  location: { type: Object, required: true }
})
const emit = defineEmits(['toggle-like'])

const isLiking = ref(false)

const handleLikeClick = async () => {
  if (isLiking.value) return
  isLiking.value = true
  try {
    await emit('toggle-like', props.location.proposalId)
  } finally {
    isLiking.value = false
  }
}
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

.no-image-placeholder {
  height: 180px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}

.like-button {
  min-width: auto !important;
  padding: 4px 12px !important;
  transition: transform 0.2s, box-shadow 0.2s;
}

.like-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2) !important;
}

.like-button .v-btn__content {
  gap: 4px;
}
</style>
