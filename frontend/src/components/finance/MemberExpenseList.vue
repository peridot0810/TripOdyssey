<template>
  <div class="member-expense-list">
    <!-- Header Section -->
    <MemberExpenseHeader />

    <!-- Loading State -->
    <div v-if="memberExpenseStore.isLoading" class="text-center py-8">
      <v-progress-circular
        indeterminate
        color="orange"
        size="48"
      ></v-progress-circular>
      <p class="text-body-2 text-grey mt-3">멤버 정보를 불러오는 중...</p>
    </div>

    <!-- Error State -->
    <v-alert
      v-else-if="memberExpenseStore.error"
      type="error"
      class="mb-4"
      closable
      @click:close="memberExpenseStore.clearError()"
    >
      {{ memberExpenseStore.error }}
    </v-alert>

    <!-- Empty State -->
    <div
      v-else-if="memberExpenseStore.memberExpenseList.length === 0"
      class="empty-state text-center py-8"
    >
      <SvgIcon type="mdi" :path="accountGroupOutlineIcon" size="64" color="grey-lighten-1" />
      <p class="text-h6 text-grey mt-3">멤버가 없습니다</p>
      <p class="text-body-2 text-grey-darken-1">그룹에 멤버를 추가해주세요</p>
    </div>

    <!-- Member List -->
    <div v-else class="member-list">
      <div class="member-cards-container">
        <MemberExpenseCard
          v-for="member in memberExpenseStore.memberExpenseList"
          :key="member.userId"
          :member="member"
          class="mb-3"
        />
      </div>
    </div>

    <!-- Quick Stats -->
    <div v-if="memberExpenseStore.memberExpenseList.length > 0" class="quick-stats mt-4">
      <v-divider class="mb-3"></v-divider>
      <div class="d-flex justify-space-between text-caption text-grey">
        <span>완납: {{ memberExpenseStore.membersFullyPaid.length }}명</span>
        <span>미납: {{ memberExpenseStore.membersWithOutstandingBalance.length }}명</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useMemberExpenseStore } from '@/stores/memberExpense'
import { apiClient } from '@/utils/apiClient'
import MemberExpenseCard from './MemberExpenseCard.vue'
import MemberExpenseHeader from './MemberExpenseHeader.vue'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiAccountGroupOutline } from '@mdi/js'

const accountGroupOutlineIcon = mdiAccountGroupOutline

// Router and Store
const route = useRoute()
const memberExpenseStore = useMemberExpenseStore()

// Computed
const groupId = computed(() => {
  return route.params.groupId
})

// Methods
const fetchMemberExpenseList = async () => {
  if (!groupId.value) {
    memberExpenseStore.setError('그룹 ID를 찾을 수 없습니다.')
    return
  }

  try {
    memberExpenseStore.setLoading(true)
    memberExpenseStore.clearError()

    const response = await apiClient.get(`/financial/${groupId.value}/member-list`)

    if (response.data.success) {
      memberExpenseStore.setMemberExpenseList(response.data.data)
    } else {
      throw new Error(response.data.message || '멤버 목록을 불러오는데 실패했습니다.')
    }
  } catch (error) {
    console.error('Error fetching member expense list:', error)

    let errorMessage = '멤버 목록을 불러오는 중 오류가 발생했습니다.'

    if (error.response) {
      const status = error.response.status
      const message = error.response.data?.message || error.message

      if (status === 401) {
        errorMessage = '로그인이 필요합니다.'
      } else if (status === 403) {
        errorMessage = '접근 권한이 없습니다.'
      } else if (status === 404) {
        errorMessage = '해당 그룹을 찾을 수 없습니다.'
      } else {
        errorMessage = `서버 오류 (${status}): ${message}`
      }
    } else if (error.code === 'ECONNABORTED') {
      errorMessage = '요청 시간이 초과되었습니다. 네트워크 연결을 확인해주세요.'
    } else {
      errorMessage = error.message || errorMessage
    }

    memberExpenseStore.setError(errorMessage)
  } finally {
    memberExpenseStore.setLoading(false)
  }
}

// Lifecycle
onMounted(() => {
  fetchMemberExpenseList()
})
</script>

<style scoped>
.member-expense-list {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.member-list {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.member-cards-container {
  flex: 1;
  overflow-y: auto;
  padding-right: 4px; /* Space for scrollbar */
}

.empty-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #f8f9fa;
  border-radius: 8px;
  min-height: 200px;
}

.quick-stats {
  flex-shrink: 0;
  margin-top: auto;
}

/* Custom scrollbar for member cards container */
.member-cards-container::-webkit-scrollbar {
  width: 4px;
}

.member-cards-container::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 2px;
}

.member-cards-container::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* Responsive adjustments */
@media (max-width: 960px) {
  .member-expense-list {
    height: auto;
  }

  .member-list {
    overflow: visible;
  }

  .member-cards-container {
    overflow-y: visible;
  }
}
</style>
