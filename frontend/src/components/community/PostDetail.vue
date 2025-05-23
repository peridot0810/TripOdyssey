<template>
  <v-dialog v-model="dialog" max-width="800px" scrollable>
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center pa-4">
        <span class="text-h6">게시글 상세</span>
        <v-btn icon variant="text" @click="$emit('close')">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-0">
        <div class="pa-4">
          <!-- Post Header -->
          <div class="d-flex justify-space-between align-center mb-3">
            <v-chip :color="getCategoryColor(post.categoryId)" size="small" variant="flat">
              {{ getCategoryLabel(post.categoryId) }}
            </v-chip>
            <span class="text-caption text-grey">{{ formatDate(post.createdAt) }}</span>
          </div>

          <!-- Post Title -->
          <h2 class="text-h5 font-weight-bold mb-3">{{ post.title }}</h2>

          <!-- Post Meta -->
          <div class="d-flex align-center mb-4 text-caption text-grey">
            <v-icon size="small" class="mr-1">mdi-account</v-icon>
            <span class="mr-4">{{ post.author }}</span>

            <v-icon size="small" class="mr-1">mdi-eye</v-icon>
            <span class="mr-4">{{ post.views }}</span>

            <v-btn
              variant="text"
              size="small"
              :color="isLiked ? 'red' : 'grey'"
              @click="toggleLike"
            >
              <v-icon size="small" class="mr-1">mdi-heart</v-icon>
              <span>{{ post.likes }}</span>
            </v-btn>
          </div>

          <!-- Post Content -->
          <div class="post-content mb-4">
            <p class="text-body-1" style="line-height: 1.6">{{ post.content }}</p>
          </div>
        </div>

        <v-divider></v-divider>

        <!-- Comments Section -->
        <div class="pa-4">
          <h3 class="text-h6 mb-3">
            댓글 <span class="text-primary">({{ comments.length }})</span>
          </h3>

          <!-- Comment Form -->
          <v-form @submit.prevent="addComment" class="mb-4">
            <v-textarea
              v-model="newComment"
              placeholder="댓글을 입력하세요..."
              rows="3"
              variant="outlined"
              hide-details
            ></v-textarea>
            <div class="d-flex justify-end mt-2">
              <v-btn type="submit" color="primary" :disabled="!newComment.trim()" size="small">
                댓글 작성
              </v-btn>
            </div>
          </v-form>

          <!-- Comments List -->
          <div v-if="comments.length === 0" class="text-center py-4 text-grey">
            첫 번째 댓글을 작성해보세요!
          </div>

          <div v-else class="comments-list">
            <div
              v-for="comment in comments"
              :key="comment.commentId"
              class="comment-item mb-3 pa-3"
            >
              <div class="d-flex justify-space-between align-center mb-2">
                <span class="font-weight-medium">{{ comment.writer }}</span>
                <div class="d-flex align-center">
                  <span class="text-caption text-grey mr-2">
                    {{ formatDate(comment.createdAt) }}
                  </span>
                  <v-btn
                    v-if="comment.writer === currentUser"
                    icon
                    size="x-small"
                    variant="text"
                    @click="deleteComment(comment.commentId)"
                  >
                    <v-icon size="small">mdi-delete</v-icon>
                  </v-btn>
                </div>
              </div>
              <p class="text-body-2 mb-0">{{ comment.content }}</p>
            </div>
          </div>
        </div>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { categories, currentUser } from '@/data/communityData.js'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  post: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits(['update:modelValue', 'close'])

const dialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const newComment = ref('')
const comments = ref([])
const isLiked = ref(false)

// Initialize comments when post changes
watch(
  () => props.post,
  (newPost) => {
    if (newPost) {
      comments.value = [...newPost.commentList]
    }
  },
  { immediate: true },
)

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

function addComment() {
  if (!newComment.value.trim()) return

  const comment = {
    commentId: Date.now(),
    postId: props.post.postId,
    content: newComment.value.trim(),
    writer: currentUser,
    createdAt: new Date().toISOString(),
  }

  comments.value.push(comment)
  newComment.value = ''
}

function deleteComment(commentId) {
  comments.value = comments.value.filter((c) => c.commentId !== commentId)
}

function toggleLike() {
  isLiked.value = !isLiked.value
  // In real app, this would call API
}
</script>

<style scoped>
.post-content {
  white-space: pre-wrap;
}

.comment-item {
  background-color: #f8f9fa;
  border-radius: 8px;
  border-left: 3px solid #e3f2fd;
}

.comments-list {
  max-height: 400px;
  overflow-y: auto;
}
</style>
