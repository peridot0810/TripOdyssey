<template>
  <!-- Loading State -->
  <div v-if="isLoading" class="loading-container">
    <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
    <p class="text-h6 mt-4">그룹 정보를 불러오는 중...</p>
  </div>

  <!-- Error State -->
  <div v-else-if="error" class="error-container">
    <v-alert type="error" variant="tonal" class="mb-4" :text="error">
      <template #append>
        <v-btn variant="text" size="small" @click="loadGroupInfo"> 다시 시도 </v-btn>
      </template>
    </v-alert>

    <v-btn to="/" variant="outlined" prepend-icon="mdi-arrow-left"> 홈으로 돌아가기 </v-btn>
  </div>

  <!-- Main Content -->
  <div v-else-if="groupStore.hasGroup" class="group-layout-container">
    <div class="group-layout">
      <!-- Left Sidebar Navigation -->
      <div class="sidebar">
        <v-list density="comfortable" nav class="sidebar-nav">
          <v-list-item
            v-for="item in links"
            :key="item.label"
            :to="`/group/${groupId}/${item.path}`"
            :title="item.label"
            :prepend-icon="item.icon"
            exact
            router
            class="nav-item"
          />
        </v-list>

        <!-- AI Helper - Only show on schedule page -->
        <div v-if="isSchedulePage" class="ai-helper-container">
          <ScheduleAiHelper />
        </div>
      </div>

      <!-- Main Content Area -->
      <div class="content-area">
        <RouterView />
      </div>
    </div>

    <!-- Transportation AI Helper - Only show on logistics page -->
    <TransportationAiHelper v-if="isLogisticsPage" />

    <!-- Finance AI Helper - Only show on finance page -->
    <FinanceAiHelper v-if="isFinancePage" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onUnmounted, computed } from 'vue'
import { useRoute, useRouter, RouterView } from 'vue-router'
import { useGroupStore } from '@/stores/group'
import { apiClient } from '@/utils/apiClient'
import ScheduleAiHelper from '@/components/ai/AiHelperSchedule.vue'
import TransportationAiHelper from '@/components/ai/TransportationAiHelper.vue'
import FinanceAiHelper from '@/components/ai/FinanceAiHelper.vue'

const route = useRoute()
const router = useRouter()
const groupStore = useGroupStore()

const isLoading = ref(false)
const error = ref(null)
const groupId = route.params.groupId

const links = [
  { label: '그룹 홈', path: '', icon: 'mdi-home' },
  { label: '그룹 관리', path: 'master', icon: 'mdi' },
  { label: '일정 조율', path: 'meeting', icon: 'mdi-account-group' },
  { label: '장소 찾아보기', path: 'map', icon: 'mdi-map' },
  { label: '일정 계획', path: 'schedule', icon: 'mdi-calendar' },
  { label: '교통/숙소', path: 'logistics', icon: 'mdi-truck' },
  { label: '재무', path: 'finance', icon: 'mdi-cash' },
  { label: '채팅', path: 'chat', icon: 'mdi-chat' },
  { label: '역할', path: 'role', icon: 'mdi-role' },
]

// Check if current page is schedule page
const isSchedulePage = computed(() => {
  return route.path.includes('/schedule')
})

// Check if current page is logistics page
const isLogisticsPage = computed(() => {
  return route.path.includes('/logistics')
})

// Check if current page is finance page
const isFinancePage = computed(() => {
  return route.path.includes('/finance')
})

// Load group information and store in pinia
const loadGroupInfo = async () => {
  const currentGroupId = route.params.groupId
  if (!currentGroupId) return

  isLoading.value = true
  error.value = null

  try {
    const response = await apiClient.get(`/group/${currentGroupId}`)

    if (response.data.success) {
      groupStore.setGroupData(response.data.data)
    } else {
      error.value = response.data.message || '그룹 정보를 불러올 수 없습니다.'
    }
  } catch (err) {
    console.error('Error fetching group info:', err)

    if (err.response?.status === 401) {
      error.value = '로그인이 필요합니다.'
    } else if (err.response?.status === 404) {
      error.value = '그룹을 찾을 수 없습니다.'
    } else {
      error.value = '그룹 정보를 불러오는 중 오류가 발생했습니다.'
    }
  } finally {
    isLoading.value = false
  }
}

// Watch for route changes to different groups
watch(
  () => route.params.groupId,
  (newGroupId, oldGroupId) => {
    if (newGroupId && newGroupId !== oldGroupId) {
      loadGroupInfo()
    }
  },
)

onMounted(() => {
  // Load group info when component mounts
  if (groupId) {
    loadGroupInfo()
  } else {
    // Invalid group ID, redirect to home
    router.push('/')
  }
})

// Clear group data when component unmounts
onUnmounted(() => {
  groupStore.clearGroup()
})
</script>

<style scoped>
.loading-container,
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 32px;
  min-height: 400px;
}

.group-layout-container {
  height: 100%;
  width: 100%;
  overflow: hidden;
}

.group-layout {
  display: flex;
  height: 100%;
  width: 100%;
}

.sidebar {
  width: 220px;
  min-width: 220px;
  background-color: #fafafa;
  border-right: 1px solid #e0e0e0;
  height: 100%;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.sidebar-nav {
  padding: 12px;
  flex: 1;
}

.ai-helper-container {
  margin-top: auto;
  border-top: 1px solid #e0e0e0;
  background-color: #f5f5f5;
}

.content-area {
  flex: 1;
  height: 100%;
  overflow-y: auto;
  background-color: #ffffff;
}

/* Navigation item styling */
.nav-item {
  margin-bottom: 4px;
  border-radius: 8px;
}

/* Active navigation item styling */
:deep(.v-list-item--active) {
  background-color: rgba(25, 118, 210, 0.1) !important;
  color: rgb(25, 118, 210) !important;
}

:deep(.v-list-item--active .v-icon) {
  color: rgb(25, 118, 210) !important;
}

/* Hover effects */
.nav-item:hover {
  background-color: rgba(0, 0, 0, 0.04);
}
</style>
