<template>
  <div class="member-card">
    <div class="member-info">
      <span class="user-icon">👤</span>
      <span class="nickname">{{ member.nickName }}</span>
      <span class="user-id">({{ member.userId }})</span>
    </div>

    <div class="member-roles">
      <span v-for="roleDisplay in memberRoleDisplays" :key="roleDisplay.key" class="role-badge">
        {{ roleDisplay.icon }}{{ roleDisplay.name }}
      </span>
      <span v-if="memberRoleDisplays.length === 0" class="role-badge"> 👥멤버 </span>
    </div>

    <div class="action-buttons">
      <button class="action-btn role-btn" @click="openRoleModal" title="역할 관리">⚙️</button>
      <button class="action-btn detail-btn" @click="viewDetail" title="상세 보기">👁️</button>
      <button class="action-btn kick-btn" @click="kickMember" title="강퇴">❌</button>
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
            <span>{{ role.icon }} {{ role.name }}</span>
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
import { useMemberListStore } from '@/stores/memberList'

// Props
const props = defineProps({
  member: {
    type: Object,
    required: true,
  },
})

// Store
const memberListStore = useMemberListStore()

// State
const showRoleModal = ref(false)
const editingRoles = ref([])

// Role configuration
const roleOptions = [
  { key: 'MASTER', name: '마스터', icon: '🔑' },
  { key: 'FINANCE', name: '회계', icon: '💰' },
  { key: 'SCHEDULE', name: '일정', icon: '📅' },
  { key: 'LOGISTICS', name: '숙박', icon: '🏨' },
  { key: 'MEMBER', name: '멤버', icon: '👥' },
]

// Computed
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

// For now, assuming anyone can edit roles except master role
// This logic can be enhanced based on current user's permissions
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

  // Update store (this will be replaced with API call + store update)
  memberListStore.updateMemberRole(props.member.userId, editingRoles.value)

  closeRoleModal()
}

const viewDetail = () => {
  // TODO: Navigate to member detail page
  console.log('View detail for', props.member.userId)
}

const kickMember = () => {
  // TODO: Show confirmation dialog and implement kick functionality
  const confirmed = confirm(`${props.member.nickName}님을 강퇴하시겠습니까?`)
  if (confirmed) {
    console.log('Kicking member', props.member.userId)
    // TODO: API call to kick member + update store
  }
}
</script>

<style scoped>
.member-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: white;
  margin-bottom: 0.5rem;
  transition: box-shadow 0.2s ease;
}

.member-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.member-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex: 1;
}

.user-icon {
  font-size: 1.2rem;
}

.nickname {
  font-weight: 600;
  color: #333;
}

.user-id {
  color: #666;
  font-size: 0.9rem;
}

.member-roles {
  display: flex;
  gap: 0.25rem;
  flex: 2;
  justify-content: center;
}

.role-badge {
  background: #f8f9fa;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
  color: #495057;
  border: 1px solid #dee2e6;
}

.action-buttons {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  background: none;
  border: none;
  font-size: 1.1rem;
  cursor: pointer;
  padding: 0.25rem;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.action-btn:hover {
  background: #f8f9fa;
}

.kick-btn:hover {
  background: #ffebee;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  min-width: 300px;
  max-width: 400px;
}

.modal-content h3 {
  margin: 0 0 1rem 0;
  color: #333;
}

.role-checkboxes {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.role-checkbox {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.role-checkbox input[type='checkbox'] {
  margin: 0;
}

.role-checkbox input[type='checkbox']:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.modal-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

.btn-cancel,
.btn-save {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.btn-cancel {
  background: #6c757d;
  color: white;
}

.btn-cancel:hover {
  background: #5a6268;
}

.btn-save {
  background: #007bff;
  color: white;
}

.btn-save:hover {
  background: #0056b3;
}

/* Responsive */
@media (max-width: 768px) {
  .member-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }

  .member-roles {
    justify-content: flex-start;
  }

  .action-buttons {
    align-self: flex-end;
  }
}
</style>
