<template>
  <div class="groups-card nintendo-card">
    <!-- Header -->
    <div class="card-header">
      <svg-icon type="mdi" :path="accountGroupPath" size="24" color="#4ecdc4" class="header-icon" />
      <h3 class="header-title">그룹 정보</h3>
    </div>

    <!-- Tabs -->
    <div class="tabs-container">
      <div class="tab-buttons">
        <button
          class="tab-button"
          :class="{ active: activeTab === 'joined' }"
          @click="activeTab = 'joined'"
        >
          <svg-icon type="mdi" :path="accountCheckPath" size="18" color="currentColor" />
          <span>참여 그룹 ({{ joinedGroups.length }})</span>
        </button>
        <button
          class="tab-button"
          :class="{ active: activeTab === 'invited' }"
          @click="activeTab = 'invited'"
        >
          <svg-icon type="mdi" :path="emailPath" size="18" color="currentColor" />
          <span>초대받은 그룹 ({{ invitedGroups.length }})</span>
        </button>
      </div>
    </div>

    <!-- Content -->
    <div class="tab-content">
      <!-- Joined Groups Tab -->
      <div v-if="activeTab === 'joined'" class="tab-panel">
        <div v-if="joinedGroups.length === 0" class="empty-state">
          <svg-icon type="mdi" :path="accountGroupOutlinePath" size="48" color="#cbd5e1" />
          <p class="empty-text">참여 중인 그룹이 없습니다</p>
        </div>

        <div v-else class="groups-list">
          <div
            v-for="group in sortedJoinedGroups"
            :key="group.id"
            class="group-item nintendo-info-card"
          >
            <div class="group-main">
              <div class="group-header">
                <h4 class="group-name">{{ group.name }}</h4>
                <div class="group-roles">
                  <span
                    v-for="role in group.myRole"
                    :key="role"
                    class="role-chip"
                    :class="`role-chip--${role}`"
                  >
                    {{ getRoleText(role) }}
                  </span>
                </div>
              </div>
              <div class="group-details">
                <span class="group-dates">{{ group.startDate }} ~ {{ group.endDate }}</span>
                <div class="group-members">
                  <svg-icon type="mdi" :path="accountMultiplePath" size="16" color="#64748b" />
                  <span>{{ group.memberCount }}명</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Invited Groups Tab -->
      <div v-if="activeTab === 'invited'" class="tab-panel">
        <div v-if="invitedGroups.length === 0" class="empty-state">
          <svg-icon type="mdi" :path="emailOutlinePath" size="48" color="#cbd5e1" />
          <p class="empty-text">새로운 초대가 없습니다</p>
        </div>

        <div v-else class="groups-list">
          <div
            v-for="invitation in sortedInvitedGroups"
            :key="invitation.groupId"
            class="invitation-item nintendo-info-card"
            :class="{ 'invitation-pending': invitation.status === 'PENDING' }"
          >
            <div class="invitation-main">
              <div class="invitation-header">
                <h4 class="group-name">{{ invitation.groupInfo.name }}</h4>
                <span
                  class="status-chip"
                  :class="`status-chip--${invitation.status.toLowerCase()}`"
                >
                  {{ getStatusText(invitation.status) }}
                </span>
              </div>
              <p class="group-description">{{ invitation.groupInfo.description }}</p>
              <div class="invitation-details">
                <span class="group-dates"
                  >{{ invitation.groupInfo.startDate }} ~ {{ invitation.groupInfo.endDate }}</span
                >
                <div class="group-members">
                  <svg-icon type="mdi" :path="accountMultiplePath" size="16" color="#64748b" />
                  <span>{{ invitation.memberCount }}명</span>
                </div>
              </div>
            </div>

            <div v-if="invitation.status === 'PENDING'" class="invitation-actions">
              <button
                class="action-btn action-btn--accept"
                @click="acceptInvitation(invitation.senderId, invitation.groupId)"
              >
                ✓ 수락
              </button>
              <button
                class="action-btn action-btn--decline"
                @click="declineInvitation(invitation.senderId, invitation.groupId)"
              >
                ✕ 거절
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiAccountGroup,
  mdiAccountCheck,
  mdiEmail,
  mdiAccountGroupOutline,
  mdiAccountMultiple,
  mdiEmailOutline,
} from '@mdi/js'
import { apiClient } from '@/stores/apiClient'

const accountGroupPath = mdiAccountGroup
const accountCheckPath = mdiAccountCheck
const emailPath = mdiEmail
const accountGroupOutlinePath = mdiAccountGroupOutline
const accountMultiplePath = mdiAccountMultiple
const emailOutlinePath = mdiEmailOutline

const props = defineProps({
  joinedGroups: {
    type: Array,
    required: true,
  },
  invitedGroups: {
    type: Array,
    required: true,
  },
})

// 참여 그룹: 여행 시작일 내림차순(최신 → 과거)
const sortedJoinedGroups = computed(() => {
  return [...props.joinedGroups].sort((a, b) => {
    return new Date(b.startDate) - new Date(a.startDate)
  })
})

// 초대 그룹: 초대 생성일 내림차순(최근 초대 → 과거 초대)
const sortedInvitedGroups = computed(() => {
  return [...props.invitedGroups].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
})

const emit = defineEmits(['accept-invitation'])

const activeTab = ref('joined')

function getRoleText(role) {
  const roleMap = {
    master: '마스터',
    schedule: '일정',
    finance: '재무',
    logistics: '물류',
    normal: '일반',
  }
  return roleMap[role] || role
}

function getStatusText(status) {
  const statusMap = {
    PENDING: '대기중',
    ACCEPTED: '수락됨',
    REJECTED: '거절됨',
  }
  return statusMap[status] || status
}

async function acceptInvitation(senderId, groupId) {
  const groupIndex = props.invitedGroups.findIndex((g) => g.groupId === groupId)
  if (groupIndex !== -1) {
    const acceptedInvitation = props.invitedGroups[groupIndex]
    await apiClient.put('/user/invited', {
      senderId,
      groupId,
      accept: true,
    })
    acceptedInvitation.status = 'ACCEPTED'
    acceptedInvitation.memberCount += 1
    emit('accept-invitation', {
      newGroup: {
        groupId,
        memberCount: acceptedInvitation.memberCount,
        myRole: ['normal'],
        name: acceptedInvitation.groupInfo.name,
        progress: acceptedInvitation.groupInfo.progress,
        startDate: acceptedInvitation.groupInfo.startDate,
        endDate: acceptedInvitation.groupInfo.endDate,
        status: acceptedInvitation.groupInfo.status,
      },
    })
  }
}

async function declineInvitation(senderId, groupId) {
  const groupIndex = props.invitedGroups.findIndex((g) => g.groupId === groupId)
  if (groupIndex !== -1) {
    const declinedInvitation = props.invitedGroups[groupIndex]
    await apiClient.put('/user/invited', {
      senderId: senderId,
      groupId: groupId,
      accept: false,
    })
    declinedInvitation.status = 'REJECTED'
    console.log(props.invitedGroups[groupIndex])
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

.nintendo-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  border: 3px solid #e2e8f0;
  border-radius: 20px;
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.3) inset;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-family: 'Nunito', sans-serif;
  max-height: 500px; /* Limit height */
  display: flex;
  flex-direction: column;
}

.card-header {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  padding: 1rem 1.5rem;
  border-bottom: 2px solid #e2e8f0;
  border-radius: 16px 16px 0 0;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex-shrink: 0;
}

.header-icon {
  background: linear-gradient(135deg, #4ecdc4, #44a08d);
  padding: 6px;
  border-radius: 10px;
  box-shadow: 0 3px 8px rgba(78, 205, 196, 0.3);
}

.header-title {
  font-size: 1.2rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

/* Tabs */
.tabs-container {
  border-bottom: 2px solid #e2e8f0;
  flex-shrink: 0;
}

.tab-buttons {
  display: flex;
}

.tab-button {
  flex: 1;
  padding: 0.75rem 1rem;
  background: none;
  border: none;
  font-family: 'Nunito', sans-serif;
  font-weight: 600;
  font-size: 0.9rem;
  color: #64748b;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  border-bottom: 3px solid transparent;
}

.tab-button:hover {
  background: rgba(78, 205, 196, 0.1);
  color: #4ecdc4;
}

.tab-button.active {
  color: #4ecdc4;
  border-bottom-color: #4ecdc4;
  background: rgba(78, 205, 196, 0.05);
}

/* Content */
.tab-content {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.tab-panel {
  padding: 1rem;
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.groups-list {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  max-height: 300px; /* Limit list height */
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  background: rgba(248, 250, 252, 0.5);
  border-radius: 12px;
  border: 2px dashed #e2e8f0;
}

.empty-text {
  color: #64748b;
  font-size: 0.9rem;
  font-weight: 500;
  margin: 0.5rem 0 0 0;
}

/* Group Items */
.nintendo-info-card {
  background: rgba(255, 255, 255, 0.8);
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  padding: 1rem;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.nintendo-info-card:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  border-color: #4ecdc4;
}

.group-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.group-main {
  flex: 1;
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
}

.group-name {
  font-size: 1rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
  line-height: 1.2;
}

.group-roles {
  display: flex;
  gap: 0.25rem;
  flex-wrap: wrap;
}

.role-chip {
  font-size: 0.7rem;
  font-weight: 600;
  padding: 0.25rem 0.5rem;
  border-radius: 8px;
  border: 1px solid;
}

.role-chip--master {
  background: rgba(139, 69, 19, 0.1);
  color: #8b4513;
  border-color: #8b4513;
}

.role-chip--schedule {
  background: rgba(236, 72, 153, 0.1);
  color: #ec4899;
  border-color: #ec4899;
}

.role-chip--finance {
  background: rgba(249, 115, 22, 0.1);
  color: #f97316;
  border-color: #f97316;
}

.role-chip--logistics {
  background: rgba(108, 92, 231, 0.1);
  color: #6c5ce7;
  border-color: #6c5ce7;
}

.role-chip--normal {
  background: rgba(100, 116, 139, 0.1);
  color: #64748b;
  border-color: #64748b;
}

.group-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.8rem;
  color: #64748b;
}

.group-dates {
  font-weight: 500;
}

.group-members {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

/* Invitation Items */
.invitation-item {
  border-left: 4px solid #e2e8f0;
}

.invitation-pending {
  border-left-color: #f97316;
  background: rgba(249, 115, 22, 0.02);
}

.invitation-main {
  margin-bottom: 0.75rem;
}

.invitation-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
}

.status-chip {
  font-size: 0.7rem;
  font-weight: 600;
  padding: 0.25rem 0.5rem;
  border-radius: 8px;
  border: 1px solid;
}

.status-chip--pending {
  background: rgba(249, 115, 22, 0.1);
  color: #f97316;
  border-color: #f97316;
}

.status-chip--accepted {
  background: rgba(34, 197, 94, 0.1);
  color: #22c55e;
  border-color: #22c55e;
}

.status-chip--rejected {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
  border-color: #ef4444;
}

.group-description {
  font-size: 0.85rem;
  color: #475569;
  margin: 0 0 0.5rem 0;
  line-height: 1.4;
}

.invitation-details {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.8rem;
  color: #64748b;
}

.invitation-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.75rem;
}

.action-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  font-family: 'Nunito', sans-serif;
  font-size: 0.8rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn--accept {
  background: linear-gradient(135deg, #22c55e, #16a34a);
  color: white;
  box-shadow: 0 2px 8px rgba(34, 197, 94, 0.3);
}

.action-btn--accept:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(34, 197, 94, 0.4);
}

.action-btn--decline {
  background: white;
  color: #ef4444;
  border: 2px solid #ef4444;
}

.action-btn--decline:hover {
  background: #ef4444;
  color: white;
  transform: translateY(-1px);
}

/* Responsive */
@media (max-width: 768px) {
  .nintendo-card {
    max-height: 400px;
  }

  .groups-list {
    max-height: 250px;
  }

  .tab-button {
    font-size: 0.8rem;
    padding: 0.6rem 0.5rem;
  }

  .group-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }

  .invitation-actions {
    flex-direction: column;
  }
}

/* Scrollbar styling */
.groups-list::-webkit-scrollbar {
  width: 4px;
}

.groups-list::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 2px;
}

.groups-list::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 2px;
}

.groups-list::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>
