<template>
  <!-- Loading State -->
  <div v-if="groupStore.isLoading" class="loading-container">
    <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
    <p class="text-h6 mt-4">그룹 정보를 불러오는 중...</p>
  </div>

  <!-- Error State -->
  <div v-else-if="groupStore.error" class="error-container">
    <v-alert
      type="error"
      variant="tonal"
      class="mb-4"
      :text="groupStore.error"
    >
      <template #append>
        <v-btn
          variant="text"
          size="small"
          @click="loadGroupInfo"
        >
          다시 시도
        </v-btn>
      </template>
    </v-alert>

    <v-btn
      to="/"
      variant="outlined"
      prepend-icon="mdi-arrow-left"
    >
      홈으로 돌아가기
    </v-btn>
  </div>

  <!-- Main Content -->
  <div v-else-if="groupStore.hasGroup" class="group-layout">
    <!-- Navigation Drawer -->
    <v-navigation-drawer app width="220" permanent class="pa-3">
      <!-- Navigation Links -->
      <v-list density="comfortable" nav>
        <v-list-item
          v-for="item in links"
          :key="item.label"
          :to="`/group/${groupId}/${item.path}`"
          :title="item.label"
          :prepend-icon="item.icon"
          exact
          router
        />
      </v-list>
    </v-navigation-drawer>

    <!-- Main Content Area -->
    <v-main class="pa-4">
      <RouterView />
    </v-main>
  </div>
</template>

<script setup>
import { onMounted, watch, onUnmounted } from 'vue'
import { useRoute, useRouter, RouterView } from 'vue-router'
import { useGroupStore } from '@/stores/group'

const route = useRoute()
const router = useRouter()
const groupStore = useGroupStore()

const groupId = route.params.groupId

const links = [
  { label: '그룹 홈', path: '', icon: 'mdi-home' },
  { label: '일정 조율', path: 'meeting', icon: 'mdi-account-group' },
  { label: '장소 찾아보기', path: 'map', icon: 'mdi-map' },
  { label: '일정 계획', path: 'schedule', icon: 'mdi-calendar' },
  { label: '교통/숙소', path: 'logistics', icon: 'mdi-truck' },
  { label: '재무', path: 'finance', icon: 'mdi-cash' },
  { label: '채팅', path: 'chat', icon: 'mdi-chat' },
  { label: '역할', path: 'role', icon: 'mdi-role' },
]

// Load group information and store in pinia
const loadGroupInfo = async () => {
  const result = await groupStore.getGroupInfo(groupId)

  if (!result.success) {
    console.error('Failed to load group info:', result.error)
    // Error is already set in the store, will be displayed in template
  }
}

// Watch for route changes to different groups
watch(
  () => route.params.groupId,
  (newGroupId, oldGroupId) => {
    if (newGroupId && newGroupId !== oldGroupId) {
      loadGroupInfo()
    }
  }
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
  min-height: 100vh;
  padding: 32px;
}

.group-layout {
  min-height: 100vh;
}

/* Navigation active state styling */
.v-list-item--active {
  background-color: rgba(var(--v-theme-primary), 0.1);
  color: rgb(var(--v-theme-primary));
}
</style>
