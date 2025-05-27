<template>
  <div class="role-selection">
    <h1>역할 선택</h1>
    <p>여행에서 당신의 역할을 선택하세요</p>

    <div class="roles-container">
      <RoleCard
        v-for="role in roles"
        :key="role.id"
        :role="role"
        :isSelected="selectedRole === role.id"
        :hasSelection="!!selectedRole"
        @select="selectRole"
      />
    </div>

    <div class="action-buttons">
      <v-btn color="primary" size="large" :disabled="!selectedRole" @click="confirmSelection">
        선택 완료
      </v-btn>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import RoleCard from '@/components/role/RoleCard.vue'

const roles = [
  {
    id: 'schedule',
    name: '일정 관리자',
    subtitle: '여행 일정을 계획하고 관리합니다',
    image: '/img/schedule.PNG'
  },
  {
    id: 'finance',
    name: '재정 담당자',
    subtitle: '여행 경비를 관리합니다',
    image: '/img/finance.PNG'
  },
  {
    id: 'logistics',
    name: '교통 담당자',
    subtitle: '이동과 교통을 담당합니다',
    image: '/img/logistics.PNG'
  },
  {
    id: 'normal',
    name: '일반 참가자',
    subtitle: '여행에 참여합니다',
    image: '/img/normal.PNG'
  }
]

const selectedRole = ref(null)

const selectRole = (roleId) => {
  selectedRole.value = roleId
}

const confirmSelection = () => {
  // 선택된 역할 정보 저장 및 다음 화면으로 이동
  console.log('선택된 역할:', selectedRole.value)
  // 여기에 역할 저장 및 라우팅 로직 추가
}
</script>

<style scoped>
.role-selection {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  font-size: 2.5rem;
  margin-bottom: 16px;
  color: #333;
}

p {
  font-size: 1.2rem;
  margin-bottom: 40px;
  color: #666;
}

.roles-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 40px;
  margin-bottom: 60px;
}

.action-buttons {
  margin-top: 20px;
}

@media (max-width: 768px) {
  h1 {
    font-size: 2rem;
  }

  p {
    font-size: 1rem;
  }

  .roles-container {
    gap: 20px;
  }
}
</style>
