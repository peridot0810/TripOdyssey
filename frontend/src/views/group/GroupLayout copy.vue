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
      <div class="navigator-container" :class="sidebarColorClass" :key="sidebarColorClass">
        <!-- =========================== RADIAL ANIMATION OVERLAY =========================== -->
        <!-- This creates the color spreading animation when switching between pages -->
        <div class="radial-spread-overlay" :class="sidebarColorClass"></div>
        <!-- ============================================================================== -->

        <!-- Navigation Links -->
        <div class="navigator">
          <!-- =========================== EXPANDABLE NAV ITEMS =========================== -->
          <!-- Each nav item can expand horizontally when active, showing the label -->
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
              <!-- This label slides in when the nav item becomes active -->
              <span class="nav-label">{{ item.label }}</span>
            </div>
          </div>
          <!-- ========================================================================== -->
        </div>

        <!-- AI Helper - Only show on schedule page -->
        <div v-if="isSchedulePage" class="ai-helper-container">
          <ScheduleAiHelper />
        </div>
      </div>

      <!-- Main Content Area -->
      <v-main class="main-content">
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

// Navigation menu items
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

// Check current page for AI helpers
const isSchedulePage = computed(() => route.path.includes('/schedule'))
const isLogisticsPage = computed(() => route.path.includes('/logistics'))
const isFinancePage = computed(() => route.path.includes('/finance'))

// =========================== SIDEBAR COLOR ANIMATION LOGIC ===========================
// This computed property determines which color animation to trigger based on the current route
const sidebarColorClass = computed(() => {
  const path = route.path

  if (path.includes('/logistics')) return 'sidebar-blue'
  else if (path.includes('/schedule')) return 'sidebar-pink'
  else if (path.includes('/finance')) return 'sidebar-orange'
  else if (path.includes('/master')) return 'sidebar-green'
  else if (path.includes('/meeting')) return 'sidebar-purple'
  else if (path.includes('/map')) return 'sidebar-teal'
  else if (path.includes('/chat')) return 'sidebar-yellow'
  else if (path.includes('/role')) return 'sidebar-red'
  else return 'sidebar-default'
})
// ==================================================================================

// Icon path helper
const getIconPath = (iconName) => {
  return new URL(`../../data/group/gruopNavigator/${iconName}`, import.meta.url).href
}

// Active route checker
const isActiveRoute = (itemPath) => {
  if (itemPath === '') {
    return route.path === `/group/${groupId}` || route.path === `/group/${groupId}/`
  }
  return route.path === `/group/${groupId}/${itemPath}`
}

// Load group information
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

// Watch for route changes
watch(
  () => route.params.groupId,
  (newGroupId, oldGroupId) => {
    if (newGroupId && newGroupId !== oldGroupId) {
      loadGroupInfo()
    }
  },
)

onMounted(() => {
  if (groupId) {
    loadGroupInfo()
  } else {
    router.push('/')
  }

  // Load Inter font
  const link = document.createElement('link')
  link.rel = 'stylesheet'
  link.href = 'https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap'
  document.head.appendChild(link)
})

onUnmounted(() => {
  groupStore.clearGroup()
})
</script>

<style scoped>
/* Loading and Error containers */
.loading-container,
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 32px;
}

/* Layout containers */
.group-layout-container {
  height: 100%;
  width: 100%;
  overflow: hidden;
}

.group-layout {
  min-height: 100vh;
  position: relative;
}

/* =========================== SIDEBAR CONTAINER =========================== */
.navigator-container {
  width: 258px;
  min-width: 258px;
  max-width: 258px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 50px 13px 50px 13px;
  position: fixed;
  left: 0;
  top: 20px;
  z-index: 100;
  border-top-right-radius: 70px;
  border-bottom-right-radius: 70px;
  height: auto;
  min-height: fit-content;
  max-height: calc(100vh - 150px);
  transition: background-color 0.3s ease;
  overflow: hidden;
  flex-shrink: 0;
  background-color: #ffffff; /* Default background */
}

/* =========================== RADIAL SPREAD ANIMATION =========================== */
/* This overlay creates the expanding color animation when switching between pages */
.radial-spread-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-top-right-radius: 70px;
  border-bottom-right-radius: 70px;
  z-index: 1;
  pointer-events: none;
  transform: scale(0);
  transform-origin: 50% 50%;
  animation: radialSpread 0.6s ease-out forwards;
}

/* The animation that creates the expanding circle effect */
@keyframes radialSpread {
  0% {
    transform: scale(0);
    border-radius: 50%;
  }
  70% {
    border-radius: 30px;
  }
  100% {
    transform: scale(1.5);
    border-radius: 0px;
    border-top-right-radius: 70px;
    border-bottom-right-radius: 70px;
  }
}

/* Color variations for different pages */
.radial-spread-overlay.sidebar-default { background-color: #ffffff; }
.radial-spread-overlay.sidebar-blue { background-color: #1780ca; }
.radial-spread-overlay.sidebar-pink { background-color: #ffb073; }
.radial-spread-overlay.sidebar-orange { background-color: #ffb759; }
.radial-spread-overlay.sidebar-green { background-color: #9b83ff; }
.radial-spread-overlay.sidebar-purple { background-color: #ffffff; }
.radial-spread-overlay.sidebar-teal { background-color: #ffffff; }
.radial-spread-overlay.sidebar-yellow { background-color: #ffffff; }
.radial-spread-overlay.sidebar-red { background-color: #ffffff; }
/* ============================================================================ */

/* Navigation container */
.navigator {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 18.48px;
  width: 100%;
  position: relative;
  z-index: 2;
}

/* =========================== HORIZONTAL EXPANSION ANIMATION =========================== */
/* Base nav item - starts as a circle */
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
  padding: 0;
  overflow: hidden;
  position: relative;
  z-index: 2;

  /* ANIMATION: Smooth transitions for width changes */
  transition: width 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55),
              transform 0.3s ease,
              border 0.3s ease,
              padding 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

/* Content wrapper inside nav item */
.nav-item-content {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;

  /* ANIMATION: Smooth content repositioning */
  transition: justify-content 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

/* Icon container */
.nav-icon-container {
  width: 70px;
  height: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
}

/* Icon styling */
.nav-icon {
  width: 55px;
  height: 55px;
  object-fit: contain;
}

.nav-icon-large {
  width: 71.5px;
  height: 71.5px;
}

/* Label - hidden by default */
.nav-label {
  white-space: nowrap;
  color: #2ec7c5;
  font-size: 19.2px;
  font-weight: 500;
  margin-left: 10px;

  /* ANIMATION: Label slides in from the right */
  opacity: 0;
  transform: translateX(20px);
  transition: opacity 0.3s ease 0.1s,
              transform 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55) 0.1s;
}

/* ACTIVE STATE: Expands horizontally to show label */
.nav-item-active {
  width: 220px; /* Expands from 100px to 220px */
  border: 5px solid #55f6d7;
  transform: scale(1.05);
  padding-left: 15px; /* Adds padding to align content properly */
}

/* Active content positioning */
.nav-item-active .nav-item-content {
  justify-content: flex-start; /* Aligns content to the left */
}

/* Active label: slides in and becomes visible */
.nav-item-active .nav-label {
  opacity: 1;
  transform: translateX(0); /* Slides in from translateX(20px) to 0 */
}

/* Hover effect */
.nav-item:hover:not(.nav-item-active) {
  transform: scale(1.1);
}
/* ==================================================================================== */

/* AI Helper container */
.ai-helper-container {
  margin-top: 20px;
  width: 100%;
  border-top: 1px solid #dedede;
  background-color: rgba(240, 240, 240, 0.8);
  padding: 10px;
  max-height: 200px;
  overflow-y: auto;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;
  position: relative;
  z-index: 2;
}

/* Main content area */
.main-content {
  margin-left: 258px;
  width: calc(100% - 258px);
  padding: 16px;
  min-height: 100vh;
}
</style>
