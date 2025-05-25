<template>
  <div class="group-info-display">
    <div v-if="groupStore.hasGroup" class="content">
      <!-- Group Name -->
      <div class="form-group">
        <label>그룹명</label>
        <div class="form-display">
          {{ groupStore.myGroup.name || '그룹명 없음' }}
        </div>
      </div>

      <!-- Description -->
      <div class="form-group">
        <label>설명</label>
        <div class="form-display">
          {{ groupStore.myGroup.description || '설명 없음' }}
        </div>
      </div>

      <!-- Date Range -->
      <div class="date-group">
        <div class="form-group">
          <label>시작일</label>
          <div class="form-display">
            {{ formatDate(groupStore.myGroup.startDate) }}
          </div>
        </div>

        <div class="form-group">
          <label>종료일</label>
          <div class="form-display">
            {{ formatDate(groupStore.myGroup.endDate) }}
          </div>
        </div>
      </div>

      <!-- Role Limits -->
      <div class="form-group">
        <label>역할 제한</label>
        <div class="role-limits-display">
          <div v-for="(limit, roleKey) in groupStore.myGroup.roleLimits" :key="roleKey" class="role-limit-display">
            <span class="role-name">{{ getRoleText(roleKey) }}</span>
            <span class="role-count">{{ limit }}명</span>
          </div>
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
import { useGroupStore } from '@/stores/group'

const groupStore = useGroupStore()

const formatDate = (dateString) => {
  return dateString ? new Date(dateString).toLocaleDateString('ko-KR') : '날짜 없음'
}

const getRoleText = (roleKey) => {
  const map = { finance: '재정 관리자', schedule: '일정 관리자', logistics: '물류 관리자' }
  return map[roleKey] || roleKey
}
</script>

<style scoped>
.group-info-display {
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

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  font-weight: 600;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.form-display {
  padding: 0.75rem 0;
  font-size: 0.9rem;
  min-height: 1.5rem;
}

.date-group {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.role-limits-display {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.role-limit-display {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 4px;
  min-width: 100px;
  flex: 1;
}

.role-name {
  font-size: 0.8rem;
  color: #666;
  margin-bottom: 0.25rem;
}

.role-count {
  font-weight: 600;
  color: #007bff;
  font-size: 1rem;
}

@media (max-width: 768px) {
  .date-group {
    grid-template-columns: 1fr;
  }
}
</style>
