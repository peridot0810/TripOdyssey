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
      <!-- Navigation Drawer -->
      <div class="navigator-container">
        <!-- Navigation Links -->
        <div class="navigator">
          <div
            v-for="item in links"
            :key="item.label"
            class="nav-item"
            :class="{ 'nav-item-active': isActiveRoute(item.path) }"
            @click="$router.push(`/group/${groupId}/${item.path}`)"
          >
            <div class="nav-item-content">
              <div class="nav-icon-container">
                <img
                  :src="getIconPath(item.icon)"
                  :alt="item.label"
                  :class="[
                    'nav-icon',
                    { 'nav-icon-large': item.path === 'master' || item.path === 'logistics' },
                  ]"
                />
              </div>
              <span class="nav-label">{{ item.label }}</span>
            </div>
          </div>
        </div>

        <!-- AI Helper - Only show on schedule page -->
        <div v-if="isSchedulePage" class="ai-helper-container">
          <ScheduleAiHelper />
        </div>
      </div>

      <!-- Main Content Area -->
      <v-main class="pa-4">
        <RouterView />
      </v-main>
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

// 네비게이션 메뉴 아이템 정의 - 아이콘 경로 매핑
const links = [
  { label: '그룹 홈', path: '', icon: 'home.png' },
  { label: '그룹 관리', path: 'master', icon: 'master.png' },
  { label: '일정 조율', path: 'meeting', icon: 'meeting.png' },
  { label: '장소 찾기', path: 'map', icon: 'map.png' },
  { label: '일정 계획', path: 'schedule', icon: 'schedule.png' },
  { label: '교통/숙소', path: 'logistics', icon: 'logistics.png' },
  { label: '재무', path: 'finance', icon: 'finance.png' },
  { label: '채팅', path: 'chat', icon: 'chat.png' },
  { label: '역할', path: 'role', icon: 'role.png' },
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

// 아이콘 경로 생성 함수
const getIconPath = (iconName) => {
  return new URL(`../../data/group/gruopNavigator/${iconName}`, import.meta.url).href
}

// 현재 경로와 메뉴 경로를 정확히 비교하여 활성화 상태 결정
const isActiveRoute = (itemPath) => {
  // 그룹 홈의 경우 (빈 경로)
  if (itemPath === '') {
    return route.path === `/group/${groupId}` || route.path === `/group/${groupId}/`
  }

  // 다른 메뉴 아이템의 경우
  return route.path === `/group/${groupId}/${itemPath}`
}

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

  // 인터 폰트 로드
  const link = document.createElement('link')
  link.rel = 'stylesheet'
  link.href = 'https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap'
  document.head.appendChild(link)
})

// Clear group data when component unmounts
onUnmounted(() => {
  groupStore.clearGroup()
})
</script>

<style scoped>
/* 로딩 및 에러 컨테이너 스타일 */
.loading-container,
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 32px;
}

/* 전체 레이아웃 컨테이너 */
.group-layout-container {
  height: 100%;
  width: 100%;
  overflow: hidden;
}

/* 메인 레이아웃 스타일 */
.group-layout {
  min-height: 100vh;
  display: flex;
}

/* ==================== 네비게이션 스타일 시작 ==================== */
/* 네비게이터 컨테이너 스타일 */
.navigator-container {
  width: 258px;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 13px;
  height: calc(100vh - 100px);
  position: fixed;
  left: 0;
  top: 60px;
  z-index: 100;
}

/* 네비게이터 스타일 */
.navigator {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 18.48px;
  width: 100%;
  margin-top: 30px;
  flex: 1;
}

/* AI Helper 컨테이너 */
.ai-helper-container {
  margin-top: auto;
  width: 100%;
  border-top: 1px solid #dedede;
  background-color: #f0f0f0;
  padding: 10px;
  max-height: 200px;
  overflow-y: auto;
}

/* 네비게이션 아이템 스타일 */
.nav-item {
  width: 100px;
  height: 100px;
  border-radius: 85px;
  background-color: #fffeff;
  border: 3px solid #dedede;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 0;
  overflow: hidden;
}

/* 네비게이션 아이템 내부 컨텐츠 */
.nav-item-content {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  transition: all 0.3s ease;
}

/* 호버 상태 스타일 */
.nav-item:hover {
  transform: scale(1.1);
}

/* 네비게이션 아이콘 컨테이너 */
.nav-icon-container {
  width: 70px;
  height: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.3s ease;
}

/* 네비게이션 아이콘 */
.nav-icon {
  width: 55px;
  height: 55px;
  object-fit: contain;
}

/* 그룹관리 아이콘 크게 */
.nav-icon-large {
  width: 71.5px;
  height: 71.5px;
}

/* 네비게이션 라벨 */
.nav-label {
  display: none;
  white-space: nowrap;
  color: #2ec7c5;
  font-size: 19.2px;
  font-weight: 500;
  margin-left: 10px;
  transition: opacity 0.3s ease;
  opacity: 0;
}

/* 활성화된 네비게이션 아이템 스타일 */
.nav-item-active {
  width: 220px;
  border-radius: 85px;
  border: 5px solid #55f6d7;
  transform: scale(1.1);
  justify-content: center;
  padding: 0;
}

/* 활성화된 아이템의 컨텐츠 스타일 */
.nav-item-active .nav-item-content {
  justify-content: center;
}

/* 활성화된 아이템의 라벨 스타일 */
.nav-item-active .nav-label {
  display: block;
  opacity: 1;
}

/* 메인 콘텐츠 영역 */
.v-main {
  margin-left: 258px;
  width: calc(100% - 258px);
}
/* ==================== 네비게이션 스타일 끝 ==================== */
</style>
