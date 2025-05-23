<template>
  <div class="role-select-container">
    <!-- Dynamic Background -->
    <div class="background-image" :style="{ backgroundImage: `url(${currentBackground})` }"></div>

    <!-- Overlay for better text readability -->
    <div class="background-overlay"></div>

    <!-- Main Content -->
    <div class="content-wrapper">
      <!-- Header Section -->
      <div class="header-section">
        <h1 class="main-title">역할 선택</h1>
        <p class="main-subtitle">여행 그룹에서 맡을 역할을 선택해주세요</p>
      </div>

      <!-- Role Cards Grid -->
      <div class="roles-grid">
        <RoleCard
          v-for="role in roles"
          :key="role.id"
          :role="role"
          :isSelected="selectedRoleId === role.id"
          :hasSelection="selectedRoleId !== null"
          @select="selectRole"
        />
      </div>

      <!-- Bottom Navigation -->
      <div class="bottom-navigation">
        <v-btn variant="text" color="white" @click="$router.go(-1)" class="back-btn">
          <v-icon icon="mdi-arrow-left" class="mr-2" />
          뒤로가기
        </v-btn>
      </div>
    </div>

    <!-- Role Details Panel -->
    <RoleDetails
      v-if="selectedRole"
      :selectedRole="selectedRole"
      :show="showDetails"
      @confirm="confirmRole"
    />

    <!-- Confirmation Dialog -->
    <v-dialog v-model="confirmDialog" max-width="500">
      <v-card class="text-center pa-4">
        <v-card-title class="text-h5 mb-2"> 역할 확정 </v-card-title>
        <v-card-text>
          <v-icon :icon="selectedRole?.icon" size="64" color="primary" class="mb-4" />
          <p class="text-body-1">
            <strong>{{ selectedRole?.name }}</strong> 역할을 선택하시겠습니까?
          </p>
          <p class="text-caption text-grey">선택한 후에도 설정에서 역할을 변경할 수 있습니다.</p>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn variant="text" @click="confirmDialog = false"> 취소 </v-btn>
          <v-btn color="primary" variant="elevated" @click="finalConfirm" class="px-6">
            확정
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import RoleCard from '@/components/role/RoleCard.vue'
import RoleDetails from '@/components/role/RoleDetails.vue'

const router = useRouter()

// Background images for each role
const backgrounds = {
  normal:
    'https://fastly.picsum.photos/id/201/5000/3333.jpg?hmac=NE8fOMa8u9PBfkq4AVwEoJdRqoPTNwUsyKvKWuXyNCk',
  scheduler:
    'https://fastly.picsum.photos/id/219/5000/3333.jpg?hmac=nQIYAD6CmzCXEG3xtEckzbu3iauIE3DpHiqnQy3gdG4',
  finance:
    'https://fastly.picsum.photos/id/237/3500/2095.jpg?hmac=y2n_cflHFKpQwLOL1SSCtVDqL8NmOnBzEW7LYKZ-z_o',
  logistics:
    'https://fastly.picsum.photos/id/244/4288/2848.jpg?hmac=R6j9PBP4aBk2vcEIoOPU4R_nuknizryn2Vq8GGtWTrM',
}

// Role definitions
const roles = ref([
  {
    id: 'scheduler',
    name: '일정 담당',
    subtitle: 'Schedule Manager',
    icon: 'mdi-calendar-clock',
    responsibilities: [
      '여행 일정 계획 및 관리',
      '관광지 및 액티비티 예약',
      '시간표 작성 및 공유',
      '일정 변경사항 공지',
      '효율적인 동선 계획',
    ],
    skills: ['계획성', '시간관리', '정보수집', '의사소통'],
    difficulty: 'medium',
    difficultyDescription: '다양한 정보를 종합하여 최적의 일정을 짜는 능력이 필요합니다.',
  },
  {
    id: 'finance',
    name: '재무 담당',
    subtitle: 'Finance Manager',
    icon: 'mdi-currency-usd',
    responsibilities: [
      '여행 예산 계획 및 관리',
      '공동 경비 정산',
      '영수증 관리 및 기록',
      '비용 절약 방안 제안',
      '재무 현황 보고',
    ],
    skills: ['계산능력', '꼼꼼함', '절약정신', '투명성'],
    difficulty: 'easy',
    difficultyDescription: '기본적인 계산 능력과 꼼꼼함만 있으면 충분합니다.',
  },
  {
    id: 'logistics',
    name: '교통/숙소 담당',
    subtitle: 'Logistics Coordinator',
    icon: 'mdi-car-multiple',
    responsibilities: [
      '교통수단 예약 및 관리',
      '숙박시설 예약',
      '이동 경로 최적화',
      '짐 보관 및 운송 관리',
      '교통비 및 숙박비 관리',
    ],
    skills: ['정보수집', '협상력', '문제해결', '체력'],
    difficulty: 'hard',
    difficultyDescription: '다양한 교통수단과 숙소 정보를 비교 분석하는 능력이 필요합니다.',
  },
  {
    id: 'normal',
    name: '일반 멤버',
    subtitle: 'General Member',
    icon: 'mdi-account-group',
    responsibilities: [
      '여행 계획에 적극적으로 참여',
      '의견 제시 및 피드백 제공',
      '정해진 일정과 규칙 준수',
      '다른 멤버들과의 협력',
      '여행 분위기 메이커 역할',
    ],
    skills: ['소통능력', '협력정신', '융통성', '긍정성'],
    difficulty: 'easy',
    difficultyDescription: '특별한 책임 없이 여행을 즐기며 팀에 기여할 수 있습니다.',
  },
])

// State
const selectedRoleId = ref('normal') // Default to normal
const showDetails = ref(false)
const confirmDialog = ref(false)
const currentBackground = ref(backgrounds.normal)

// Computed
const selectedRole = computed(() => {
  return roles.value.find((role) => role.id === selectedRoleId.value)
})

// Methods
function selectRole(roleId) {
  selectedRoleId.value = roleId
  showDetails.value = false

  // Small delay for smooth transition
  setTimeout(() => {
    showDetails.value = true
  }, 100)
}

function confirmRole() {
  confirmDialog.value = true
}

function finalConfirm() {
  confirmDialog.value = false

  // Here you would typically save the role selection to your backend
  console.log('Selected role:', selectedRole.value)

  // Show success message and navigate back
  // You can add a toast notification here
  router.push({ name: 'group-main', params: { groupId: router.currentRoute.value.params.groupId } })
}

// Watch for role changes to update background
watch(
  selectedRoleId,
  (newRoleId) => {
    currentBackground.value = backgrounds[newRoleId]
  },
  { immediate: true },
)

// Initialize
onMounted(() => {
  // Set initial background
  currentBackground.value = backgrounds.normal

  // Show details for default selection after a short delay
  setTimeout(() => {
    showDetails.value = true
  }, 500)
})
</script>

<style scoped>
.role-select-container {
  position: relative;
  min-height: 100vh;
  width: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  transition: background-image 0.8s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1;
}

.background-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    rgba(0, 0, 0, 0.4) 0%,
    rgba(0, 0, 0, 0.2) 50%,
    rgba(0, 0, 0, 0.6) 100%
  );
  z-index: 2;
}

.content-wrapper {
  position: relative;
  z-index: 3;
  padding: 40px 24px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 100vh;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.header-section {
  text-align: center;
  margin-bottom: 48px;
}

.main-title {
  font-size: 3rem;
  font-weight: bold;
  color: white;
  margin-bottom: 16px;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.5);
  letter-spacing: 2px;
}

.main-subtitle {
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.roles-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 48px;
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
}

.bottom-navigation {
  display: flex;
  justify-content: center;
}

.back-btn {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  font-weight: 500;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

/* Mobile responsiveness */
@media (max-width: 1024px) {
  .roles-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}

@media (max-width: 768px) {
  .content-wrapper {
    padding: 24px 16px;
  }

  .main-title {
    font-size: 2.2rem;
  }

  .main-subtitle {
    font-size: 1rem;
  }

  .roles-grid {
    grid-template-columns: 1fr;
    gap: 16px;
    margin-bottom: 32px;
  }

  .header-section {
    margin-bottom: 32px;
  }
}

@media (max-width: 480px) {
  .main-title {
    font-size: 1.8rem;
  }

  .content-wrapper {
    padding: 20px 12px;
  }
}
</style>
