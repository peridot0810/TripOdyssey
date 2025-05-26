<template>
  <div class="group-member-list">
    <!-- Header with member count and actions -->
    <div class="list-header">
      <div class="member-count">
        <span class="count-label">총 멤버:</span>
        <span class="count-number">{{ memberListStore.memberCount }}명</span>
      </div>
      <div class="header-actions">
        <button class="refresh-btn" @click="fetchMembers" :disabled="isLoading">
          {{ isLoading ? '🔄' : '↻' }} 새로고침
        </button>
        <button class="invite-btn" @click="openInviteModal">➕ 멤버 초대</button>
      </div>
    </div>

    <!-- View toggle controls - moved to top -->
    <div class="view-controls">
      <button
        class="view-toggle-btn"
        :class="{ active: currentView === 'members' && !showRoleGrouping }"
        @click="setView('members', false)"
      >
        📋 전체 목록
      </button>
      <button
        class="view-toggle-btn"
        :class="{ active: currentView === 'requests' }"
        @click="setView('requests')"
      >
        📝 역할 신청
      </button>
    </div>

    <!-- Loading state -->
    <div v-if="isLoading" class="loading-container">
      <div class="loading-spinner">🔄</div>
      <span>멤버 목록을 불러오는 중...</span>
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="error-container">
      <div class="error-message">❌ {{ error }}</div>
      <button class="retry-btn" @click="fetchMembers">다시 시도</button>
    </div>

    <!-- Content area - shows either member list or role requests -->
    <div v-else class="content-area">
      <!-- Member List View -->
      <div v-if="currentView === 'members'" class="member-content">
        <!-- Member list -->
        <div v-if="memberListStore.members.length > 0" class="member-list-container">
          <!-- Role-based grouping (default) -->
          <div v-if="showRoleGrouping" class="role-groups">
            <div
              v-for="(members, roleKey) in memberListStore.membersByRole"
              :key="roleKey"
              class="role-group"
            >
              <h4 v-if="members.length > 0" class="role-group-title">
                {{ memberListStore.getRoleDisplayName(roleKey) }} ({{ members.length }})
              </h4>
              <div v-if="members.length > 0" class="role-group-members">
                <MemberCard v-for="member in members" :key="member.userId" :member="member" />
              </div>
            </div>
          </div>

          <!-- Simple list view -->
          <div v-else class="simple-list">
            <MemberCard
              v-for="member in memberListStore.members"
              :key="member.userId"
              :member="member"
            />
          </div>
        </div>

        <!-- Empty state for members -->
        <div v-else class="empty-container">
          <div class="empty-icon">👥</div>
          <div class="empty-message">아직 그룹 멤버가 없습니다</div>
          <button class="invite-btn" @click="openInviteModal">첫 번째 멤버 초대하기</button>
        </div>
      </div>

      <!-- Role Requests View -->
      <div v-else-if="currentView === 'requests'" class="request-content">
        <RoleRequestList />
      </div>
    </div>

    <!-- Invitation Modal -->
    <InvitationModal
      v-if="showInviteModal"
      @close="closeInviteModal"
      @invitation-sent="handleInvitationSent"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useMemberListStore } from '@/stores/memberList'
import { apiClient } from '@/utils/apiClient'
import MemberCard from './MemberCard.vue'
import RoleRequestList from './RoleRequestList.vue'
import InvitationModal from './InvitationModal.vue'

// Stores and router
const memberListStore = useMemberListStore()
const route = useRoute()

// State
const isLoading = ref(false)
const error = ref(null)
const showRoleGrouping = ref(true) // Default to role grouping
const currentView = ref('members') // 'members' or 'requests'
const showInviteModal = ref(false)

// Computed
const groupId = computed(() => route.params.groupId)

// Methods
const fetchMembers = async () => {
  if (!groupId.value) {
    error.value = '그룹 ID를 찾을 수 없습니다'
    return
  }

  isLoading.value = true
  error.value = null
  memberListStore.setLoading(true)

  try {
    const response = await apiClient.get(`/group/${groupId.value}/members`)

    if (response.data.success) {
      memberListStore.setMembers(response.data.data)
      console.log('Members fetched successfully:', response.data.data)
    } else {
      throw new Error(response.data.message || '멤버 목록을 불러오는데 실패했습니다')
    }
  } catch (err) {
    console.error('Error fetching members:', err)
    const errorMessage =
      err.response?.data?.message || err.message || '네트워크 오류가 발생했습니다'
    error.value = errorMessage
    memberListStore.setError(errorMessage)
  } finally {
    isLoading.value = false
    memberListStore.setLoading(false)
  }
}

const openInviteModal = () => {
  showInviteModal.value = true
}

const closeInviteModal = () => {
  showInviteModal.value = false
}

const handleInvitationSent = () => {
  // Refresh member list after successful invitation
  fetchMembers()
}

const setView = (view, roleGrouping = true) => {
  currentView.value = view
  if (view === 'members') {
    showRoleGrouping.value = roleGrouping
  }
}

// Lifecycle
onMounted(() => {
  fetchMembers()
})
</script>

<style scoped>
.group-member-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0; /* Allow shrinking */
  flex: 1; /* Take available space in parent */
}

/* Header */
.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid #e0e0e0;
}

.member-count {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.count-label {
  color: #666;
  font-size: 0.9rem;
}

.count-number {
  font-weight: 600;
  color: #333;
  background: #f8f9fa;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.9rem;
}

.header-actions {
  display: flex;
  gap: 0.5rem;
}

.refresh-btn,
.invite-btn {
  padding: 0.5rem 0.75rem;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  background: white;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.2s ease;
}

.refresh-btn:hover {
  background: #f8f9fa;
}

.invite-btn {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

.invite-btn:hover {
  background: #0056b3;
  border-color: #0056b3;
}

.refresh-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Loading state */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  text-align: center;
  color: #666;
}

.loading-spinner {
  font-size: 2rem;
  animation: spin 1s linear infinite;
  margin-bottom: 0.5rem;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* Error state */
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  text-align: center;
}

.error-message {
  color: #dc3545;
  margin-bottom: 1rem;
  padding: 1rem;
  background: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 6px;
}

.retry-btn {
  padding: 0.5rem 1rem;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.retry-btn:hover {
  background: #0056b3;
}

/* Content area */
.content-area {
  flex: 1;
  overflow: hidden;
  min-height: 0; /* Critical for proper flex behavior */
  display: flex;
  flex-direction: column;
}

.member-content,
.request-content {
  height: 100%;
  max-height: 100%; /* Prevent overflow */
  overflow-y: auto;
  min-height: 0; /* Important for flex children */
}

/* Member list container */
.member-list-container {
  height: 100%;
  max-height: 100%; /* Constrain to parent */
  overflow-y: auto;
  min-height: 0; /* Allow shrinking */
}

/* Role grouping */
.role-groups {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.role-group-title {
  margin: 0 0 0.5rem 0;
  padding: 0.5rem 0.75rem;
  background: #f8f9fa;
  border-left: 3px solid #007bff;
  font-size: 0.95rem;
  font-weight: 600;
  color: #495057;
}

.role-group-members {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

/* Simple list */
.simple-list {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

/* Empty state */
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 1rem;
  text-align: center;
  color: #666;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-message {
  font-size: 1.1rem;
  margin-bottom: 1.5rem;
}

/* View controls */
.view-controls {
  display: flex;
  gap: 0.25rem;
  padding: 0.5rem;
  background: #f8f9fa;
  border-radius: 6px;
  margin-bottom: 1rem;
}

.view-toggle-btn {
  flex: 1;
  padding: 0.5rem 0.75rem;
  border: none;
  background: transparent;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.2s ease;
}

.view-toggle-btn:hover {
  background: #e9ecef;
}

.view-toggle-btn.active {
  background: white;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  font-weight: 600;
}

/* Responsive */
@media (max-width: 768px) {
  .list-header {
    flex-direction: column;
    gap: 0.75rem;
    align-items: stretch;
  }

  .header-actions {
    justify-content: space-between;
  }

  .view-controls {
    position: sticky;
    bottom: 0;
    background: white;
    border-top: 1px solid #e0e0e0;
    margin: 0;
    border-radius: 0;
  }
}
</style>
