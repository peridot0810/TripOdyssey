<template>
  <div class="role-request-list">
    <!-- Header with request count and refresh -->
    <div class="list-header">
      <div class="request-count">
        <span class="count-label">신청 현황:</span>
        <span class="count-number">{{ totalRequestCount }}건</span>
      </div>
      <div class="header-actions">
        <button class="refresh-btn" @click="fetchRoleRequests" :disabled="isLoading">
          {{ isLoading ? '🔄' : '↻' }} 새로고침
        </button>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="isLoading" class="loading-container">
      <div class="loading-spinner">🔄</div>
      <span>역할 신청 목록을 불러오는 중...</span>
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="error-container">
      <div class="error-message">❌ {{ error }}</div>
      <button class="retry-btn" @click="fetchRoleRequests">다시 시도</button>
    </div>

    <!-- Role requests grouped by role -->
    <div
      v-else-if="groupedRequests && Object.keys(groupedRequests).length > 0"
      class="request-groups"
    >
      <div v-for="(requests, roleId) in groupedRequests" :key="roleId" class="role-group">
        <h4 class="role-group-title" :class="`role-title--${roleId}`">
          {{ getRoleIcon(roleId) }} {{ getRoleName(roleId) }} 역할 신청 ({{ requests.length }})
        </h4>
        <div class="role-group-requests">
          <div
            v-for="request in requests"
            :key="`${request.userId}-${request.roleId}`"
            class="request-item"
            :class="getRequestItemClass(request.status)"
          >
            <div class="request-info">
              <span class="user-name">{{ request.userId }}</span>
              <span class="request-date">{{ formatDate(request.requestedAt) }}</span>
              <span class="request-status" :class="getStatusClass(request.status)">
                {{ getStatusText(request.status) }}
              </span>
            </div>
            <div v-if="request.status === 'PENDING'" class="request-actions">
              <button
                class="action-btn approve-btn"
                :disabled="isRequestProcessing(request)"
                @click="approveRequest(request)"
              >
                {{ isRequestProcessing(request) ? '⏳' : '✓' }}
              </button>
              <button
                class="action-btn reject-btn"
                :disabled="isRequestProcessing(request)"
                @click="rejectRequest(request)"
              >
                {{ isRequestProcessing(request) ? '⏳' : '❌' }}
              </button>
            </div>
            <div v-else class="status-indicator">
              <span v-if="request.status === 'ACCEPTED'" class="status-icon accepted">✅</span>
              <span v-else-if="request.status === 'REJECTED'" class="status-icon rejected">❌</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty state -->
    <div v-else class="empty-container">
      <div class="empty-icon">📝</div>
      <div class="empty-message">현재 역할 신청이 없습니다</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { apiClient } from '@/utils/apiClient'

const route = useRoute()

// State
const isLoading = ref(false)
const error = ref(null)
const roleRequests = ref([])
const processingRequests = ref(new Set()) // Track which requests are being processed

// Role mapping
const roleMapping = {
  1: { name: '마스터', icon: '' },
  2: { name: '회계', icon: '' },
  3: { name: '일정', icon: '' },
  4: { name: '숙박', icon: '' },
  5: { name: '일반', icon: '' },
}

// Computed
const groupId = computed(() => route.params.groupId)

const totalRequestCount = computed(() => roleRequests.value.length)

const groupedRequests = computed(() => {
  const grouped = {}

  roleRequests.value.forEach((request) => {
    const roleId = request.roleId
    if (!grouped[roleId]) {
      grouped[roleId] = []
    }
    grouped[roleId].push(request)
  })

  // Sort requests within each group by requestedAt (newest first)
  Object.keys(grouped).forEach((roleId) => {
    grouped[roleId].sort((a, b) => new Date(b.requestedAt) - new Date(a.requestedAt))
  })

  return grouped
})

// Methods
const fetchRoleRequests = async () => {
  if (!groupId.value) {
    error.value = '그룹 ID를 찾을 수 없습니다'
    return
  }

  isLoading.value = true
  error.value = null

  try {
    const response = await apiClient.get(`/group/${groupId.value}/member/role-request`)

    if (response.data.success) {
      roleRequests.value = response.data.data || []
      console.log('Role requests fetched successfully:', response.data.data)
    } else {
      throw new Error(response.data.message || '역할 신청 목록을 불러오는데 실패했습니다')
    }
  } catch (err) {
    console.error('Error fetching role requests:', err)
    const errorMessage =
      err.response?.data?.message || err.message || '네트워크 오류가 발생했습니다'
    error.value = errorMessage
  } finally {
    isLoading.value = false
  }
}

const getRoleName = (roleId) => {
  return roleMapping[roleId]?.name || `역할 ${roleId}`
}

const getRoleIcon = (roleId) => {
  return roleMapping[roleId]?.icon || ''
}

const formatDate = (dateString) => {
  if (!dateString) return '날짜 없음'

  try {
    const date = new Date(dateString)
    return date.toLocaleDateString('ko-KR', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  } catch (error) {
    console.log(error)
    return dateString
  }
}

const approveRequest = async (request) => {
  const requestKey = `${request.userId}-${request.roleId}`
  processingRequests.value.add(requestKey)

  try {
    const response = await apiClient.post(
      `/group/${groupId.value}/member/${request.userId}/role`,
      {}, // Empty body
      {
        params: {
          roleId: request.roleId,
          accept: true,
        },
      },
    )

    console.log('Approve request response:', response.data)

    if (response.data.success) {
      alert(`${request.userId}님의 ${getRoleName(request.roleId)} 역할 신청이 승인되었습니다.`)
      // Refresh both role requests and member list
      await fetchRoleRequests()
      // Emit event or call parent method to refresh member list if needed
    } else {
      throw new Error(response.data.message || '승인에 실패했습니다.')
    }
  } catch (error) {
    console.error('Approve request error:', error)
    const errorMessage =
      error.response?.data?.message || error.message || '승인 처리에 실패했습니다.'
    alert(errorMessage)
  } finally {
    processingRequests.value.delete(requestKey)
  }
}

const rejectRequest = async (request) => {
  const requestKey = `${request.userId}-${request.roleId}`
  processingRequests.value.add(requestKey)

  try {
    const response = await apiClient.post(
      `/group/${groupId.value}/member/${request.userId}/role`,
      {}, // Empty body
      {
        params: {
          roleId: request.roleId,
          accept: false,
        },
      },
    )

    console.log('Reject request response:', response.data)

    if (response.data.success) {
      alert(`${request.userId}님의 ${getRoleName(request.roleId)} 역할 신청이 거절되었습니다.`)
      // Refresh the role requests list
      await fetchRoleRequests()
    } else {
      throw new Error(response.data.message || '거절에 실패했습니다.')
    }
  } catch (error) {
    console.error('Reject request error:', error)
    const errorMessage =
      error.response?.data?.message || error.message || '거절 처리에 실패했습니다.'
    alert(errorMessage)
  } finally {
    processingRequests.value.delete(requestKey)
  }
}

const getStatusText = (status) => {
  const statusMap = {
    PENDING: '대기중',
    ACCEPTED: '승인됨',
    REJECTED: '거절됨',
  }
  return statusMap[status] || status
}

const getStatusClass = (status) => {
  return `status-${status.toLowerCase()}`
}

const getRequestItemClass = (status) => {
  return status !== 'PENDING' ? 'processed' : ''
}

const isRequestProcessing = (request) => {
  const requestKey = `${request.userId}-${request.roleId}`
  return processingRequests.value.has(requestKey)
}

// Lifecycle
onMounted(() => {
  fetchRoleRequests()
})
</script>

<style scoped>
.role-request-list {
  display: flex;
  flex-direction: column;
  height: 100%;
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

.request-count {
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
  background: #fff3cd;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.9rem;
  border: 1px solid #ffeaa7;
}

.header-actions {
  display: flex;
  gap: 0.5rem;
}

.refresh-btn {
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

/* Request groups */
.request-groups {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.role-group-title {
  margin: 0 0 0.5rem 0;
  padding: 0.5rem 0.75rem;
  background: #a5ffe1;
  border-left: 3px solid #77dfff;
  font-size: 0.95rem;
  font-weight: 600;
  color: #00455a;
}

.role-group-requests {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

/* Request item */
.request-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  background: white;
  transition: box-shadow 0.2s ease;
}

.request-item:hover {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.request-item.processed {
  background: #f8f9fa;
  opacity: 0.8;
}

.request-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex: 1;
}

.user-icon {
  font-size: 1.1rem;
}

.user-name {
  font-weight: 600;
  color: #333;
}

.request-date {
  color: #666;
  font-size: 0.8rem;
}

.request-status {
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 500;
  margin-left: auto;
  margin-right: 1rem;
}

.status-pending {
  background: #fff3cd;
  color: #856404;
  border: 1px solid #ffeaa7;
}

.status-accepted {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.status-rejected {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.request-actions {
  display: flex;
  gap: 0.5rem;
}

.status-indicator {
  display: flex;
  align-items: center;
}

.status-icon {
  font-size: 1.2rem;
}

.status-icon.accepted {
  color: #28a745;
}

.status-icon.rejected {
  color: #dc3545;
}

.action-btn {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.approve-btn {
  background: #28a745;
  color: white;
}

.approve-btn:hover {
  background: #218838;
}

.reject-btn {
  background: #dc3545;
  color: white;
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
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
}

/* Responsive */
@media (max-width: 768px) {
  .request-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }

  .request-info {
    width: 100%;
  }

  .request-date {
    margin-left: 0;
    margin-right: 0;
  }

  .request-actions {
    align-self: flex-end;
  }
}

/* Role-specific header colors (Nintendo-style Joy-Con inspired) */

.role-title--1 {
  /* MASTER */
  background: linear-gradient(135deg, #6c5ce7, #a29bfe);
  border-left-color: #6c5ce7;
  color: white;
}

.role-title--2 {
  /* FINANCE */
  background: linear-gradient(135deg, #f9ca24, #f0932b);
  border-left-color: #4ecdc4;
  color: white;
}

.role-title--3 {
  /* SCHEDULE */
  background: linear-gradient(135deg, #e95385, #ff2075);
  border-left-color: #45b7d1;
  color: white;
}

.role-title--4 {
  /* LOGISTICS */
  background: linear-gradient(135deg, #45b7d1, #007bff);
  border-left-color: #f9ca24;
  color: #333;
}
</style>
