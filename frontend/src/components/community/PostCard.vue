<template>
  <v-card class="post-card mb-4" elevation="2" @click="$emit('click', post)">
    <v-card-item class="pb-2">
      <div class="d-flex justify-space-between align-center mb-2">
        <v-chip :color="getCategoryColor(post.categoryId)" size="small" variant="flat">
          {{ getCategoryLabel(post.categoryId) }}
        </v-chip>
        <span class="text-caption text-grey">{{ formatDate(post.createdAt) }}</span>
      </div>

      <v-card-title class="pa-0 text-h6 font-weight-bold post-title">
        {{ post.title }}
      </v-card-title>

      <v-card-text class="pa-0 mt-2 text-body-2 post-content">
        {{ truncateContent(post.content) }}
      </v-card-text>
    </v-card-item>

    <v-card-actions class="px-4 pb-3">
      <div class="d-flex align-center text-caption text-grey">
        <v-icon size="small" class="mr-1">mdi-account</v-icon>
        <span class="mr-4">{{ post.author }}</span>

        <v-icon size="small" class="mr-1">mdi-eye</v-icon>
        <span class="mr-4">{{ post.views }}</span>

        <v-icon size="small" class="mr-1" color="red">mdi-heart</v-icon>
        <span class="mr-4">{{ post.likes }}</span>

        <v-icon size="small" class="mr-1">mdi-comment</v-icon>
        <span>{{ post.commentList?.length || 0 }}</span>
      </div>
    </v-card-actions>
  </v-card>
</template>

<script setup>
import { categories } from '@/data/communityData.js'

defineEmits(['click'])

defineProps({
  post: {
    type: Object,
    required: true,
  },
})

function getCategoryColor(categoryId) {
  const category = categories.find((cat) => cat.id === categoryId)
  return category?.color || 'grey'
}

function getCategoryLabel(categoryId) {
  const category = categories.find((cat) => cat.id === categoryId)
  return category?.label || 'Unknown'
}

function formatDate(dateString) {
  const date = new Date(dateString)
  const now = new Date()
  const diffInMinutes = Math.floor((now - date) / (1000 * 60))

  if (diffInMinutes < 60) {
    return `${diffInMinutes}분 전`
  } else if (diffInMinutes < 1440) {
    return `${Math.floor(diffInMinutes / 60)}시간 전`
  } else {
    return `${Math.floor(diffInMinutes / 1440)}일 전`
  }
}

function truncateContent(content) {
  return content.length > 100 ? content.substring(0, 100) + '...' : content
}
</script>

<style scoped>
.post-card {
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}

.post-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.12) !important;
}

.post-title {
  line-height: 1.3;
  word-break: keep-all;
}

.post-content {
  line-height: 1.5;
  color: #666;
}
</style>
