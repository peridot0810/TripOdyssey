<template>
  <div class="member-card">
    <div class="member-info">
      <!-- <span class="user-icon">👤</span> -->
      <span class="nickname">{{ member.nickName }}</span>
      <span class="user-id">({{ member.userId }})</span>
    </div>

    <div class="member-roles">
      <span
        v-for="roleDisplay in memberRoleDisplays"
        :key="roleDisplay.key"
        class="role-badge"
        :class="`role-badge--${roleDisplay.key.toLowerCase()}`"
      >
        {{ roleDisplay.name }}
      </span>
      <span v-if="memberRoleDisplays.length === 0" class="role-badge role-badge--member">멤버</span>
    </div>

    <div class="action-buttons">
      <button class="action-btn role-btn" @click="openRoleModal" title="역할 관리">⚙️</button>
      <button class="action-btn kick-btn" @click="kickMember" :disabled="isRemoving" title="강퇴">
        {{ isRemoving ? '⏳' : '❌' }}
      </button>
    </div>

    <!-- Role Management Modal -->
    <div v-if="showRoleModal" class="modal-overlay" @click="closeRoleModal">
      <div class="modal-content" @click.stop>
        <h3>역할 관리 - {{ member.nickName }}</h3>
        <div class="role-checkboxes">
          <label v-for="(role, index) in roleOptions" :key="index" class="role-checkbox">
            <input
              type="checkbox"
              v-model="editingRoles[index]"
              :disabled="role.key === 'MASTER' && !canEditMaster"
            />
            <span>{{ role.name }}</span>
          </label>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="closeRoleModal">취소</button>
          <button class="btn-save" @click="saveRoles">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useMemberListStore } from '@/stores/memberList'
import { apiClient } from '@/utils/apiClient'

// Props
const props = defineProps({
  member: {
    type: Object,
    required: true,
  },
})

// Store and route
const memberListStore = useMemberListStore()
const route = useRoute()

// State
const showRoleModal = ref(false)
const editingRoles = ref([])
const isRemoving = ref(false)

// Computed
const groupId = computed(() => route.params.groupId)

// Role configuration
const roleOptions = [
  { key: 'MASTER', name: '방장', icon: '' },
  { key: 'FINANCE', name: '재무', icon: '' },
  { key: 'SCHEDULE', name: '일정', icon: '' },
  { key: 'LOGISTICS', name: '교통/숙박', icon: '' },
  { key: 'MEMBER', name: '멤버', icon: '' },
]

const memberRoleDisplays = computed(() => {
  const displays = []
  props.member.roles.forEach((hasRole, index) => {
    if (hasRole && roleOptions[index]) {
      displays.push({
        key: roleOptions[index].key,
        name: roleOptions[index].name,
        icon: roleOptions[index].icon,
      })
    }
  })
  return displays
})

const canEditMaster = computed(() => {
  // TODO: Check if current user is master or has permission
  return true
})

// Methods
const openRoleModal = () => {
  // Initialize editing roles with current member roles
  editingRoles.value = [...props.member.roles]
  showRoleModal.value = true
}

const closeRoleModal = () => {
  showRoleModal.value = false
  editingRoles.value = []
}

const saveRoles = () => {
  // TODO: Implement API call to update member roles
  console.log('Saving roles for', props.member.userId, editingRoles.value)

  // Update store using existing function
  memberListStore.updateMemberRole(props.member.userId, editingRoles.value)

  closeRoleModal()
}

const kickMember = async () => {
  // Show confirmation dialog
  const confirmed = confirm(`${props.member.nickName}님을 강퇴하시겠습니까?`)
  if (!confirmed) return

  if (!groupId.value) {
    alert('그룹 ID를 찾을 수 없습니다.')
    return
  }

  isRemoving.value = true

  try {
    const response = await apiClient.delete(
      `/group/${groupId.value}/member/${props.member.userId}`,
      {
        params: {
          userId: props.member.userId,
        },
      },
    )

    console.log('Remove member response:', response.data)

    if (response.data.success) {
      // Remove member from pinia store using existing function
      memberListStore.removeMember(props.member.userId)

      alert(`${props.member.nickName}님이 그룹에서 제거되었습니다.`)
      console.log('Member removed successfully:', props.member.userId)
    } else {
      throw new Error(response.data.message || '멤버 제거에 실패했습니다.')
    }
  } catch (error) {
    console.error('Remove member error:', error)
    const errorMessage =
      error.response?.data?.message || error.message || '멤버 제거 중 오류가 발생했습니다.'
    alert(errorMessage)
  } finally {
    isRemoving.value = false
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

.member-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.25rem;
  border-radius: 20px;
  background: #ffffff;
  border: 2px solid #e2e8f0;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.05);
  font-family: 'Nunito', sans-serif;
  margin-bottom: 0.75rem;
  transition: box-shadow 0.2s ease;
}

.member-card:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.08);
}

/* Remove rainbow top bar */
.member-card::before {
  display: none;
}

.member-card:hover {
  box-shadow:
    0 8px 20px rgba(0, 123, 255, 0.15),
    0 4px 8px rgba(0, 0, 0, 0.1);
  border-color: #4ecdc4;
}

.member-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex: 1;
}

.user-icon {
  font-size: 1.5rem;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.nickname {
  font-weight: 700;
  color: #2d3748;
  font-size: 1.1rem;
  letter-spacing: -0.025em;
}

.user-id {
  color: #718096;
  font-size: 0.9rem;
  font-weight: 600;
  background: #edf2f7;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
}

.member-roles {
  display: flex;
  gap: 0.5rem;
  flex: 2;
  justify-content: center;
  flex-wrap: wrap;
}

.role-badge {
  color: white;
  padding: 0.5rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
  transition: all 0.2s ease;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

/* Role-specific colors */
.role-badge--master {
  background: #6c5ce7;
}

.role-badge--finance {
  background: #ffa217;
}

.role-badge--logistics {
  background: #0984e3;
}

.role-badge--schedule {
  background: #ff5a7e;
  color: #2d3436;
}

.role-badge--member {
  background: #dfe6e9;
  color: #2d3436;
}

.action-buttons {
  display: flex;
  gap: 0.75rem;
}

.action-btn {
  background: white;
  border: 3px solid #e2e8f0;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 0.75rem;
  border-radius: 50%;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

.role-btn {
  border-color: #4ecdc4;
  color: #4ecdc4;
}

.role-btn:hover {
  border-color: #4ecdc4;
  background: #4ecdc4;
  color: white;
  box-shadow: 0 4px 12px rgba(78, 205, 196, 0.4);
}

.kick-btn {
  border-color: #ff6b6b;
  color: #ff6b6b;
}

.kick-btn:hover:not(:disabled) {
  border-color: #ff6b6b;
  background: #ff6b6b;
  color: white;
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.4);
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.action-btn:active:not(:disabled) {
  transform: translateY(0) scale(0.95);
}

/* Modal Styles - Nintendo Switch Style */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(45, 55, 72, 0.8);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal-content {
  background: linear-gradient(135deg, #ffffff 0%, #f7fafc 100%);
  padding: 2rem;
  border-radius: 24px;
  min-width: 350px;
  max-width: 450px;
  border: 3px solid #e2e8f0;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.modal-content::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 6px;
  background: linear-gradient(90deg, #ff6b6b 0%, #4ecdc4 50%, #45b7d1 100%);
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modal-content h3 {
  margin: 0 0 1.5rem 0;
  color: #2d3748;
  font-family: 'Nunito', sans-serif;
  font-weight: 700;
  font-size: 1.3rem;
  text-align: center;
}

.role-checkboxes {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

.role-checkbox {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  cursor: pointer;
  padding: 0.75rem;
  border-radius: 16px;
  transition: all 0.2s ease;
  border: 2px solid transparent;
}

.role-checkbox:hover {
  background: #f7fafc;
  border-color: #e2e8f0;
}

.role-checkbox input[type='checkbox'] {
  margin: 0;
  width: 20px;
  height: 20px;
  accent-color: #4ecdc4;
  cursor: pointer;
}

.role-checkbox input[type='checkbox']:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.role-checkbox span {
  font-weight: 600;
  color: #4a5568;
  font-family: 'Nunito', sans-serif;
}

.modal-actions {
  display: flex;
  gap: 0.75rem;
  justify-content: center;
}

.btn-cancel,
.btn-save {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 700;
  font-family: 'Nunito', sans-serif;
  font-size: 1rem;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  min-width: 100px;
}

.btn-cancel {
  background: linear-gradient(135deg, #a0aec0 0%, #718096 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(160, 174, 192, 0.3);
}

.btn-cancel:hover {
  box-shadow: 0 6px 16px rgba(160, 174, 192, 0.4);
}

.btn-save {
  background: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(78, 205, 196, 0.3);
}

.btn-save:hover {
  box-shadow: 0 6px 16px rgba(78, 205, 196, 0.4);
}

.btn-cancel:active,
.btn-save:active {
  transform: translateY(0) scale(0.95);
}

/* Responsive */
@media (max-width: 768px) {
  .member-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
    padding: 1.25rem;
  }

  .member-roles {
    justify-content: flex-start;
    width: 100%;
  }

  .action-buttons {
    align-self: flex-end;
  }

  .modal-content {
    margin: 1rem;
    min-width: auto;
    max-width: none;
  }
}
</style>
