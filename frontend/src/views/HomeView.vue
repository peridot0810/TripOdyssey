<template>
  <div class="home-container">
    <h1 class="page-title">My Groups</h1>

    <!-- Loading State -->
    <div v-if="isLoading" class="loading-container">
      <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
      <p class="text-h6 mt-4">그룹 목록을 불러오는 중...</p>
    </div>

    <!-- Error State -->
    <v-alert v-else-if="error" type="error" class="mb-6" closable @click:close="clearError">
      {{ error }}
      <template #append>
        <v-btn variant="text" size="small" @click="loadGroups"> 다시 시도 </v-btn>
      </template>
    </v-alert>

    <!-- Groups Content -->
    <div v-else>
      <!-- Empty State -->
      <div v-if="groupStore.groupCount === 0" class="empty-state">
        <v-icon size="80" color="grey-lighten-2">mdi-account-group-outline</v-icon>
        <h2 class="text-h5 mt-4 mb-2">아직 참여한 그룹이 없습니다</h2>
        <p class="text-body-1 text-grey mb-6">새로운 그룹을 만들어 여행을 계획해보세요!</p>
      </div>

      <!-- Group Cards Grid Layout -->
      <div v-else class="group-cards-grid">
        <div v-for="group in groupStore.groups" :key="group.groupId" class="group-card-slot">
          <GroupInfoCard :group="group" />
        </div>
      </div>
    </div>

    <!-- Create Group Button -->
    <div class="create-group-section">
      <v-btn class="create-group-btn" @click="openCreateGroupDialog" rounded elevation="0">
        CREATE NEW GROUP
      </v-btn>
    </div>

    <!-- Create Group Dialog -->
    <CreateGroupDialog v-model="showCreateDialog" @group-created="handleGroupCreated" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useGroupListStore } from '@/stores/groupList'
import { apiClient } from '@/utils/apiClient'
import GroupInfoCard from '@/components/group/GroupInfoCard.vue'
import CreateGroupDialog from '@/components/group/CreateGroupDialog.vue'

const groupStore = useGroupListStore()
const showCreateDialog = ref(false)

// Local loading and error states for this component
const isLoading = ref(false)
const error = ref(null)

// Load groups from API
const loadGroups = async () => {
  isLoading.value = true
  error.value = null

  try {
    const response = await apiClient.get('/user/groups')

    // Transform API response to match expected format
    const transformedGroups = response.data.map((group) => ({
      groupId: group.groupId,
      name: group.name,
      description: '', // API doesn't provide description, set empty
      status: group.status,
      startDate: group.startDate,
      endDate: group.endDate,
      memberCount: group.memberCount,
      myRole: group.myRole,
      progress: group.progress,
      roleLimits: {
        finance: 1, // Default values since API doesn't provide these
        schedule: 1,
        logistics: 1,
      },
    }))

    groupStore.setGroups(transformedGroups)
  } catch (err) {
    console.error('Failed to load groups:', err)

    if (err.response?.status === 401) {
      error.value = '로그인이 필요합니다.'
    } else if (err.response?.status === 404) {
      error.value = '그룹을 찾을 수 없습니다.'
    } else if (err.response?.status >= 500) {
      error.value = '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.'
    } else {
      error.value = '그룹 목록을 불러오는 중 오류가 발생했습니다.'
    }
  } finally {
    isLoading.value = false
  }
}

const clearError = () => {
  error.value = null
}

const openCreateGroupDialog = () => {
  showCreateDialog.value = true
}

const handleGroupCreated = (newGroup) => {
  // The group is already added to the store by CreateGroupDialog
  // Just close the dialog here
  console.log('Group created successfully:', newGroup)
  showCreateDialog.value = false
}

onMounted(async () => {
  // Load groups when component mounts
  await loadGroups()
})
</script>

<style scoped>
.home-container {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: calc(100vh - 64px); /* Full height minus app bar */
  display: flex;
  flex-direction: column;
}

.page-title {
  font-size: 2rem;
  font-weight: 600;
  margin-bottom: 48px;
  color: #333;
  text-align: center;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 64px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 64px 32px;
  text-align: center;
}

.group-cards-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
  margin: 0 auto;
  width: 100%;
  max-width: 1300px;
}

.group-card-slot {
  width: 100%;
  min-width: 300px;
  max-width: 415px;
  margin: 0 auto;
}

.create-group-section {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.create-group-btn {
  background-color: #2d2d2d !important;
  color: #ebebeb !important;
  border-radius: 40px !important;
  width: 252px !important;
  height: 68px !important;
  font-family: 'Inter', sans-serif !important;
  font-weight: 700 !important;
  font-size: 20px !important;
  text-transform: none !important;
  letter-spacing: normal !important;
  display: flex !important;
  justify-content: center !important;
  align-items: center !important;
}

/* 반응형 디자인 */
@media (max-width: 1200px) {
  .group-cards-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .home-container {
    padding: 16px;
  }

  .page-title {
    font-size: 1.5rem;
    margin-bottom: 24px;
  }

  .group-cards-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .empty-state {
    padding: 32px 16px;
  }

  .loading-container {
    padding: 32px;
  }
}
</style>
