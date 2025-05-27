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
  <div v-else-if="groupStore.hasGroup" class="group-layout">
    <!-- Navigation Drawer -->
    <div class="navigator-container" :class="currentPageClass">
      <!-- Radial spread overlay -->
      <div class="radial-overlay" :class="radialOverlayClass"></div>

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
    </div>

    <!-- Main Content Area -->
    <v-main class="pa-4">
      <RouterView />
    </v-main>
    <HelperSpace />
  </div>
</template>

<script setup>
import { ref, onMounted, watch, onUnmounted, computed } from 'vue'
import { useRoute, useRouter, RouterView } from 'vue-router'
import { useGroupStore } from '@/stores/group'
import { useMemberListStore } from '@/stores/memberList'
import { apiClient } from '@/utils/apiClient'
import HelperSpace from '@/components/ai/HelperSpace.vue'

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
  { label: '역할', path: 'role', icon: 'role.png' },
]

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

// 현재 페이지에 따른 클래스 계산
const currentPageClass = computed(() => {
  const path = route.path
  if (path.includes('/master')) return 'page-master'
  if (path.includes('/logistics')) return 'page-logistics'
  if (path.includes('/finance')) return 'page-finance'
  if (path.includes('/schedule')) return 'page-schedule'
  return ''
})

// 라디얼 오버레이 클래스 계산
const radialOverlayClass = computed(() => {
  const path = route.path
  if (path.includes('/master')) return 'radial-master'
  if (path.includes('/logistics')) return 'radial-logistics'
  if (path.includes('/finance')) return 'radial-finance'
  if (path.includes('/schedule')) return 'radial-schedule'
  return ''
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

/* 전체 레이아웃 스타일 */
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
  height: calc(100vh);
  position: fixed;
  left: 0;
  top: 70px;
  z-index: 100;
  transition: background-color 0.5s ease;
  overflow: hidden;
  border-top-right-radius: 80px;
}

/* 라디얼 오버레이 */
.radial-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  opacity: 0;
  transition: all 1.2s cubic-bezier(0.4, 0, 0.2, 1);
  transform: scale(0);
  border-radius: 50%;
  z-index: 1;
}

/* 라디얼 스프레드 애니메이션 클래스들 */
.radial-master {
  background: radial-gradient(
    circle,
    rgba(46, 199, 197, 0.15) 0%,
    rgba(46, 199, 197, 0.08) 50%,
    rgba(46, 199, 197, 0.03) 100%
  );
  opacity: 1;
  transform: scale(3);
  animation: radialSpread 1.2s cubic-bezier(0.4, 0, 0.2, 1) forwards;
}

.radial-logistics {
  background: radial-gradient(
    circle,
    rgba(59, 130, 246, 0.15) 0%,
    rgba(59, 130, 246, 0.08) 50%,
    rgba(59, 130, 246, 0.03) 100%
  );
  opacity: 1;
  transform: scale(3);
  animation: radialSpread 1.2s cubic-bezier(0.4, 0, 0.2, 1) forwards;
}

.radial-finance {
  background: radial-gradient(
    circle,
    rgba(249, 115, 22, 0.15) 0%,
    rgba(249, 115, 22, 0.08) 50%,
    rgba(249, 115, 22, 0.03) 100%
  );
  opacity: 1;
  transform: scale(3);
  animation: radialSpread 1.2s cubic-bezier(0.4, 0, 0.2, 1) forwards;
}

.radial-schedule {
  background: radial-gradient(
    circle,
    rgba(236, 72, 153, 0.15) 0%,
    rgba(236, 72, 153, 0.08) 50%,
    rgba(236, 72, 153, 0.03) 100%
  );
  opacity: 1;
  transform: scale(3);
  animation: radialSpread 1.2s cubic-bezier(0.4, 0, 0.2, 1) forwards;
}

/* 라디얼 스프레드 키프레임 애니메이션 */
@keyframes radialSpread {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  50% {
    opacity: 0.8;
  }
  100% {
    transform: scale(3);
    opacity: 1;
  }
}

/* 페이지별 배경색 변경 */
.page-master {
  background: linear-gradient(135deg, #54ebca 0%, rgba(38, 110, 138, 0.05) 100%);
}

.page-logistics {
  background-color: #08549b;
}

.page-finance {
  background-color: #ff9900;
}

.page-schedule {
  background-color: #eb3b67;
}

/* background: linear-gradient(135deg, #ff9900 0%, rgba(249, 115, 22, 0.05) 100%); */

/* 네비게이터 스타일 */
.navigator {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 18.48px;
  width: 100%;
  margin-top: 30px;
  position: relative;
  z-index: 2;
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
  position: relative;
  z-index: 3;
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
  border: 2px solid #666666;
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
