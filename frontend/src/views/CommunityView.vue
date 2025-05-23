<template>
  <v-container fluid class="pa-0 community-container">
    <v-row no-gutters class="fill-height">
      <!-- Left Side: Post List -->
      <v-col cols="12" md="6" class="left-panel">
        <div class="panel-content">
          <!-- Header Section -->
          <div class="d-flex justify-space-between align-center mb-6">
            <div>
              <h1 class="text-h4 font-weight-bold mb-2">커뮤니티</h1>
              <p class="text-body-1 text-grey">여행 정보를 공유하고 동행을 구해보세요</p>
            </div>
            <v-btn color="primary" @click="showCreateDialog = true">
              <v-icon class="mr-2">mdi-plus</v-icon>
              글쓰기
            </v-btn>
          </div>

          <!-- Filter & Search Section -->
          <v-card elevation="1" class="mb-6">
            <v-card-text class="pa-4">
              <SearchFilter
                :model-category="selectedCategory"
                :model-search="searchKeyword"
                :model-sort="sortBy"
                @update:category="selectedCategory = $event"
                @update:search="searchKeyword = $event"
                @update:sort="sortBy = $event"
                @filter="filterPosts"
              />
            </v-card-text>
          </v-card>

          <!-- Posts List -->
          <div v-if="filteredPosts.length === 0" class="text-center py-8">
            <v-icon size="64" color="grey-lighten-1">mdi-forum-outline</v-icon>
            <p class="text-h6 text-grey mt-3">게시글이 없습니다</p>
            <p class="text-body-2 text-grey">첫 번째 게시글을 작성해보세요!</p>
          </div>

          <div v-else class="posts-container">
            <PostCard
              v-for="post in paginatedPosts"
              :key="post.postId"
              :post="post"
              :class="{ 'selected-post': selectedPost && selectedPost.postId === post.postId }"
              @click="selectPost"
            />
          </div>

          <!-- Pagination -->
          <div v-if="totalPages > 1" class="d-flex justify-center mt-6">
            <v-pagination
              v-model="currentPage"
              :length="totalPages"
              :total-visible="5"
              size="small"
            ></v-pagination>
          </div>
        </div>
      </v-col>

      <!-- Right Side: Post Detail or Welcome Image -->
      <v-col cols="12" md="6" class="right-panel">
        <div class="panel-content">
          <!-- Post Detail Content -->
          <div v-if="selectedPost" class="post-detail-content">
            <!-- Post Header -->
            <div class="d-flex justify-space-between align-center mb-3">
              <v-chip
                :color="getCategoryColor(selectedPost.categoryId)"
                size="small"
                variant="flat"
              >
                {{ getCategoryLabel(selectedPost.categoryId) }}
              </v-chip>
              <span class="text-caption text-grey">{{ formatDate(selectedPost.createdAt) }}</span>
            </div>

            <!-- Post Title -->
            <h2 class="text-h5 font-weight-bold mb-3">{{ selectedPost.title }}</h2>

            <!-- Post Meta -->
            <div class="d-flex align-center mb-4 text-caption text-grey">
              <v-icon size="small" class="mr-1">mdi-account</v-icon>
              <span class="mr-4">{{ selectedPost.author }}</span>

              <v-icon size="small" class="mr-1">mdi-eye</v-icon>
              <span class="mr-4">{{ selectedPost.views }}</span>

              <v-btn
                variant="text"
                size="small"
                :color="isLiked ? 'red' : 'grey'"
                @click="toggleLike"
              >
                <v-icon size="small" class="mr-1">mdi-heart</v-icon>
                <span>{{ selectedPost.likes }}</span>
              </v-btn>
            </div>

            <!-- Post Content -->
            <div class="post-content mb-4">
              <p class="text-body-1" style="line-height: 1.6">{{ selectedPost.content }}</p>
            </div>

            <v-divider class="my-4"></v-divider>

            <!-- Comments Section -->
            <div>
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
          </div>

          <!-- Welcome Background when no post selected -->
          <div v-else class="welcome-content">
            <div class="background-image">
              <div class="overlay-content">
                <v-icon size="80" color="white" class="mb-4">mdi-forum-outline</v-icon>
                <h2 class="text-h4 font-weight-bold text-white mb-3">여행 커뮤니티</h2>
                <p class="text-h6 text-white mb-4">게시글을 선택해서 자세한 내용을 확인하세요</p>
                <v-btn
                  color="white"
                  variant="outlined"
                  size="large"
                  @click="showCreateDialog = true"
                >
                  <v-icon class="mr-2">mdi-plus</v-icon>
                  첫 게시글 작성하기
                </v-btn>
              </div>
            </div>
          </div>
        </div>
      </v-col>
    </v-row>

    <!-- Create Post Dialog -->
    <CreatePost v-model="showCreateDialog" @close="showCreateDialog = false" @submit="addNewPost" />
  </v-container>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { communityPosts, categories, currentUser } from '@/data/communityData.js'
import PostCard from '@/components/community/PostCard.vue'
import CreatePost from '@/components/community/CreatePost.vue'
import SearchFilter from '@/components/community/SearchFilter.vue'

// Data
const posts = ref([])
const selectedCategory = ref(null)
const searchKeyword = ref('')
const sortBy = ref('latest')
const currentPage = ref(1)
const postsPerPage = 5

// Dialog states
const showCreateDialog = ref(false)
const selectedPost = ref(null)

// Comment management
const newComment = ref('')
const comments = ref([])
const isLiked = ref(false)

// Filter and sort options (now handled by SearchFilter component)
// const sortOptions = [
//   { value: 'latest', label: '최신순' },
//   { value: 'likes', label: '좋아요순' },
//   { value: 'views', label: '조회수순' },
//   { value: 'comments', label: '댓글순' },
// ]

// Computed properties
const filteredPosts = computed(() => {
  let result = [...posts.value]

  // Category filter
  if (selectedCategory.value !== null) {
    result = result.filter((post) => post.categoryId === selectedCategory.value)
  }

  // Search filter
  if (searchKeyword.value.trim()) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(
      (post) =>
        post.title.toLowerCase().includes(keyword) ||
        post.content.toLowerCase().includes(keyword) ||
        post.author.toLowerCase().includes(keyword),
    )
  }

  // Sort
  result.sort((a, b) => {
    switch (sortBy.value) {
      case 'likes':
        return b.likes - a.likes
      case 'views':
        return b.views - a.views
      case 'comments':
        return (b.commentList?.length || 0) - (a.commentList?.length || 0)
      case 'latest':
      default:
        return new Date(b.createdAt) - new Date(a.createdAt)
    }
  })

  return result
})

const totalPages = computed(() => {
  return Math.ceil(filteredPosts.value.length / postsPerPage)
})

const paginatedPosts = computed(() => {
  const start = (currentPage.value - 1) * postsPerPage
  const end = start + postsPerPage
  return filteredPosts.value.slice(start, end)
})

// Watch for post selection changes
watch(selectedPost, (newPost) => {
  if (newPost) {
    comments.value = [...newPost.commentList]
    isLiked.value = false
  }
})

// Methods
function filterPosts() {
  currentPage.value = 1
  selectedPost.value = null // Clear selection when filtering
}

function selectPost(post) {
  selectedPost.value = post
  // Simulate view count increment
  post.views++
}

function addNewPost(newPost) {
  posts.value.unshift(newPost)
  filterPosts()
}

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
    postId: selectedPost.value.postId,
    content: newComment.value.trim(),
    writer: currentUser,
    createdAt: new Date().toISOString(),
  }

  comments.value.push(comment)
  selectedPost.value.commentList.push(comment)
  newComment.value = ''
}

function deleteComment(commentId) {
  comments.value = comments.value.filter((c) => c.commentId !== commentId)
  selectedPost.value.commentList = selectedPost.value.commentList.filter(
    (c) => c.commentId !== commentId,
  )
}

function toggleLike() {
  isLiked.value = !isLiked.value
  if (isLiked.value) {
    selectedPost.value.likes++
  } else {
    selectedPost.value.likes--
  }
}

// Lifecycle
onMounted(() => {
  posts.value = [...communityPosts]
})
</script>

<style scoped>
.community-container {
  height: calc(100vh - 64px);
  overflow: hidden;
}

.left-panel,
.right-panel {
  height: 100%;
  overflow-y: auto;
}

.panel-content {
  padding: 20px;
  height: 100%;
}

.left-panel {
  border-right: 1px solid #e0e0e0;
}

.posts-container {
  height: auto;
}

.selected-post {
  background-color: #f3f4f6 !important;
  border-left: 4px solid #1976d2;
}

.post-detail-content {
  height: 100%;
  overflow-y: auto;
}

.welcome-content {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.background-image {
  width: 100%;
  height: 100%;
  background-image: url('https://fastly.picsum.photos/id/20/3670/2462.jpg?hmac=CmQ0ln-k5ZqkdtLvVO23LjVAEabZQx2wOaT4pyeG10I');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.background-image::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}

.overlay-content {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 40px;
}

.comment-item {
  background-color: #f8f9fa;
  border-radius: 8px;
  border-left: 3px solid #e3f2fd;
}

.comments-list {
  max-height: 300px;
  overflow-y: auto;
}

/* Custom scrollbars */
.left-panel::-webkit-scrollbar,
.right-panel::-webkit-scrollbar,
.comments-list::-webkit-scrollbar {
  width: 6px;
}

.left-panel::-webkit-scrollbar-thumb,
.right-panel::-webkit-scrollbar-thumb,
.comments-list::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 3px;
}

.left-panel::-webkit-scrollbar-track,
.right-panel::-webkit-scrollbar-track,
.comments-list::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* Mobile responsive */
@media (max-width: 960px) {
  .community-container {
    height: auto;
    overflow: visible;
  }

  .left-panel,
  .right-panel {
    height: auto;
    overflow-y: visible;
  }

  .left-panel {
    border-right: none;
    border-bottom: 1px solid #e0e0e0;
  }

  .right-panel {
    min-height: 50vh;
  }
}
</style>
