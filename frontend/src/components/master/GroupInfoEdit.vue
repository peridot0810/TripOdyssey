<template>
  <div class="group-info-edit">
    <div v-if="groupStore.hasGroup" class="content">
      <!-- Group Information Form -->
      <div class="info-form">
        <!-- Group Name -->
        <div class="form-group">
          <label>그룹명</label>
          <input
            v-model="editForm.name"
            type="text"
            class="form-input"
            placeholder="그룹명을 입력하세요"
          />
        </div>

        <!-- Description -->
        <div class="form-group">
          <label>설명</label>
          <textarea
            v-model="editForm.description"
            class="form-input"
            placeholder="그룹 설명을 입력하세요"
            rows="3"
          />
        </div>

        <!-- Date Range -->
        <div class="date-group">
          <div class="form-group">
            <label>시작일</label>
            <input
              v-model="editForm.startDate"
              type="date"
              class="form-input"
            />
          </div>

          <div class="form-group">
            <label>종료일</label>
            <input
              v-model="editForm.endDate"
              type="date"
              class="form-input"
            />
          </div>
        </div>

        <!-- Role Limits -->
        <div class="form-group">
          <label>역할 제한</label>
          <div class="role-limits-edit">
            <div v-for="(limit, roleKey) in editForm.roleLimits" :key="roleKey" class="role-limit-item">
              <span class="role-label">{{ getRoleText(roleKey) }}</span>
              <input
                v-model.number="editForm.roleLimits[roleKey]"
                type="number"
                min="0"
                max="10"
                class="role-number-input"
              />
            </div>
          </div>
        </div>

        <!-- Action Buttons -->
        <div class="action-buttons">
          <button
            @click="handleSave"
            class="save-btn"
            :disabled="isLoading"
          >
            {{ isLoading ? '저장 중...' : '변경사항 저장' }}
          </button>
        </div>

        <!-- Error Message -->
        <div v-if="error" class="error-message">
          {{ error }}
        </div>
      </div>
    </div>

    <!-- No Group Data -->
    <div v-else class="no-data">
      <p>그룹 정보가 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useGroupStore } from '@/stores/group'
import { apiClient } from '@/utils/apiClient'

const route = useRoute()
const groupStore = useGroupStore()

const isLoading = ref(false)
const error = ref(null)

const editForm = reactive({
  name: '',
  description: '',
  startDate: '',
  endDate: '',
  roleLimits: {
    finance: 0,
    schedule: 0,
    logistics: 0
  }
})

const updateEditForm = () => {
  const data = groupStore.myGroup
  editForm.name = data.name || ''
  editForm.description = data.description || ''
  editForm.startDate = data.startDate || ''
  editForm.endDate = data.endDate || ''
  if (data.roleLimits) {
    editForm.roleLimits = { ...data.roleLimits }
  }
}

const handleSave = async () => {
  const groupId = route.params.groupId
  if (!groupId) {
    error.value = 'Group ID not found'
    return
  }

  isLoading.value = true
  error.value = null

  try {
    const requestBody = {
      groupId: parseInt(groupId),
      name: editForm.name,
      description: editForm.description,
      startDate: editForm.startDate,
      endDate: editForm.endDate,
      roleLimits: {
        finance: editForm.roleLimits.finance,
        schedule: editForm.roleLimits.schedule,
        logistics: editForm.roleLimits.logistics
      }
    }

    const response = await apiClient.put(`/group/${groupId}`, requestBody)

    if (response.data.success) {
      // Update store with new data (optimistic UI update)
      groupStore.setGroupData({
        groupId: parseInt(groupId),
        name: editForm.name,
        description: editForm.description,
        startDate: editForm.startDate,
        endDate: editForm.endDate,
        roleLimits: { ...editForm.roleLimits },
        // Keep existing fields
        status: groupStore.myGroup.status,
        createdAt: groupStore.myGroup.createdAt
      })

      // Close modal by dispatching a click event on the overlay
      const overlay = document.querySelector('.modal-overlay')
      if (overlay) {
        overlay.click()
      }

      error.value = null
      console.log('Group info saved successfully')
    } else {
      error.value = response.data.message || '저장 중 오류가 발생했습니다.'
    }
  } catch (err) {
    console.error('Error saving group info:', err)

    if (err.response?.status === 401) {
      error.value = '로그인이 필요합니다.'
    } else if (err.response?.status === 403) {
      error.value = '수정 권한이 없습니다.'
    } else if (err.response?.status === 404) {
      error.value = '그룹을 찾을 수 없습니다.'
    } else {
      error.value = '저장 중 오류가 발생했습니다.'
    }
  } finally {
    isLoading.value = false
  }
}

const getRoleText = (roleKey) => {
  const map = { finance: '재정 관리자', schedule: '일정 관리자', logistics: '물류 관리자' }
  return map[roleKey] || roleKey
}

// Watch for group data changes to update form
watch(() => groupStore.myGroup, (newGroup) => {
  if (newGroup.groupId) {
    updateEditForm()
  }
}, { deep: true, immediate: true })

// Initialize edit form when component mounts
onMounted(() => {
  if (groupStore.hasGroup) {
    updateEditForm()
  }
})

// Expose updateEditForm method for parent component
defineExpose({
  updateEditForm
})
</script>

<style scoped>
.group-info-edit {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.no-data {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
}

.info-form {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.form-group {
  margin-bottom: 1rem;
  flex-shrink: 0;
}

.form-group label {
  display: block;
  font-weight: 600;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.form-input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.form-input:focus {
  outline: none;
  border-color: #007bff;
}

.date-group {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.role-limits-edit {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.role-limit-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  min-width: 100px;
}

.role-label {
  font-size: 0.8rem;
  color: #666;
  margin-bottom: 0.5rem;
  text-align: center;
}

.role-number-input {
  width: 60px;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  text-align: center;
  font-size: 0.9rem;
}

.action-buttons {
  margin-top: auto;
  padding-top: 1rem;
  border-top: 1px solid #eee;
  flex-shrink: 0;
}

.save-btn {
  width: 100%;
  padding: 0.75rem;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.save-btn:hover:not(:disabled) {
  background: #0056b3;
}

.save-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

.error-message {
  margin-top: 1rem;
  padding: 0.75rem;
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
  border-radius: 4px;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .date-group {
    grid-template-columns: 1fr;
  }
}
</style>
