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
          <v-btn variant="text" @click="confirmDialog = false" :disabled="isSubmitting">
            취소
          </v-btn>
          <v-btn
            color="primary"
            variant="elevated"
            @click="finalConfirm"
            class="px-6"
            :loading="isSubmitting"
            :disabled="isSubmitting"
          >
            확정
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import RoleCard from '@/components/role/RoleCard.vue'
import RoleDetails from '@/components/role/RoleDetails.vue'
import { roleBackgrounds, rolesData } from '@/data/roles.js'
import { apiClient } from '@/utils/apiClient.js'

const router = useRouter()
const route = useRoute()

// State
const roles = ref(rolesData)
const selectedRoleId = ref(5) // Default to normal (id: 5)
const showDetails = ref(false)
const confirmDialog = ref(false)
const currentBackground = ref(roleBackgrounds.normal)
const isSubmitting = ref(false)

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

async function finalConfirm() {
  if (isSubmitting.value) return

  isSubmitting.value = true
  const groupId = route.params.groupId

  try {
    const response = await apiClient.post(`/group/${groupId}/member/role-request`, {
      roleId: selectedRoleId.value,
    })

    console.log('Role selection successful:', response.data)

    confirmDialog.value = false

    // Show success message and navigate back
    // You can add a toast notification here
    router.push({ name: 'group-main', params: { groupId } })
  } catch (error) {
    console.error('Role selection failed:', error)

    // Handle error - you can show an error toast here
    if (error.response?.status === 401) {
      console.error('Unauthorized - please login again')
    } else {
      console.error('Failed to select role. Please try again.')
    }
  } finally {
    isSubmitting.value = false
  }
}

// Watch for role changes to update background
watch(
  selectedRoleId,
  (newRoleId) => {
    const selectedRoleData = roles.value.find((role) => role.id === newRoleId)
    if (selectedRoleData) {
      currentBackground.value = roleBackgrounds[selectedRoleData.key]
    }
  },
  { immediate: true },
)

// Initialize
onMounted(() => {
  // Set initial background for default selection (normal)
  currentBackground.value = roleBackgrounds.normal

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
