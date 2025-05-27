<template>
  <div class="user-profile-card nintendo-card">
    <div class="card-content">
      <!-- Profile Image -->
      <div class="profile-image-container">
        <div class="profile-avatar-wrapper">
          <v-avatar size="120" class="profile-avatar">
            <v-img v-if="profileImageUrl" :src="profileImageUrl" alt="프로필 사진"></v-img>
            <svg-icon v-else type="mdi" :path="accountCirclePath" size="60" color="#BDBDBD" />
          </v-avatar>
          <div class="avatar-border"></div>
        </div>
      </div>

      <!-- User Greeting -->
      <div class="greeting-section">
        <h2 class="greeting-text">안녕하세요 {{ userStore.userInfo.nickname }} 님!</h2>
      </div>

      <!-- User Details -->
      <div class="user-details-section">
        <div class="detail-item nintendo-info-card">
          <div class="detail-icon">
            <svg-icon type="mdi" :path="accountPath" size="20" color="white" />
          </div>
          <div class="detail-content">
            <span class="detail-label">사용자 ID</span>
            <span class="detail-value">{{ userStore.userInfo.id }}</span>
          </div>
        </div>

        <div class="detail-item nintendo-info-card">
          <div class="detail-icon email-icon">
            <svg-icon type="mdi" :path="emailPath" size="20" color="white" />
          </div>
          <div class="detail-content">
            <span class="detail-label">이메일</span>
            <span class="detail-value">{{ userStore.userInfo.email }}</span>
          </div>
        </div>

        <div class="detail-item nintendo-info-card">
          <div class="detail-icon age-icon">
            <svg-icon type="mdi" :path="cakeVariantPath" size="20" color="white" />
          </div>
          <div class="detail-content">
            <span class="detail-label">나이</span>
            <span class="detail-value">{{ userStore.userInfo.age }}세</span>
          </div>
        </div>

        <div class="detail-item nintendo-info-card">
          <div class="detail-icon gender-icon">
            <svg-icon type="mdi" :path="genderPath" size="20" color="white" />
          </div>
          <div class="detail-content">
            <span class="detail-label">성별</span>
            <span class="detail-value">{{ genderText }}</span>
          </div>
        </div>
      </div>

      <!-- Edit Profile Button -->
      <div class="action-section">
        <button class="nintendo-btn nintendo-btn--primary" @click="editDialog = true">
          <svg-icon type="mdi" :path="pencilPath" size="20" class="btn-icon" />
          프로필 수정
        </button>
      </div>
    </div>
  </div>

  <!-- 프로필 수정 팝업 -->
  <EditUserInfo
    :model-value="editDialog"
    :user-info="userInfo"
    @update:model-value="editDialog = $event"
    @save="onEditProfile"
  />
</template>

<script setup>
import { ref, computed } from 'vue'
import { apiClient } from '@/stores/apiClient'
import { useUserStore } from '@/stores/user'
import EditUserInfo from '@/components/user/EditUserInfo.vue'
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiAccountCircle,
  mdiAccount,
  mdiEmail,
  mdiCakeVariant,
  mdiGenderMale,
  mdiGenderFemale,
  mdiPencil,
} from '@mdi/js'

const userStore = useUserStore()
const props = defineProps({
  userInfo: {
    type: Object,
    required: true,
  },
})

const editDialog = ref(false)

// 프로필 정보 저장(수정)시 실행될 함수 (뼈대)
async function onEditProfile(editedUserInfo) {
  await apiClient.put('/user', {
    newNickname: editedUserInfo.nickname,
    newAge: editedUserInfo.age,
  })

  userStore.updateUserInfo({
    nickname: editedUserInfo.nickname,
    age: editedUserInfo.age,
  })
}

const profileImageUrl = computed(() => {
  const prefix = 'http://localhost:8080'
  return userStore.userInfo.profileImage ? prefix + userStore.userInfo.profileImage : ''
})

const genderText = computed(() => {
  return props.userInfo.gender === 'male' ? '남성' : '여성'
})

const accountCirclePath = mdiAccountCircle
const accountPath = mdiAccount
const emailPath = mdiEmail
const cakeVariantPath = mdiCakeVariant
const genderPath = computed(() => {
  return props.userInfo.gender === 'male' ? mdiGenderMale : mdiGenderFemale
})
const pencilPath = mdiPencil
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
  position: relative;
  overflow: hidden;
  font-family: 'Nunito', sans-serif;
}

.nintendo-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 6px;
  background: linear-gradient(90deg, #f97316 0%, #ec4899 20%, #3b82f6 40%, #4ecdc4 60%);
  border-radius: 20px 20px 0 0;
}

.user-profile-card {
  height: 100%;
  max-width: 400px;
  margin: 0 auto;
}

.card-content {
  padding: 2rem 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  text-align: center;
}

/* Profile Image Section */
.profile-image-container {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.profile-avatar-wrapper {
  position: relative;
  display: inline-block;
}

.profile-avatar {
  border: 4px solid #09698f;
  box-shadow: 0 8px 24px rgba(78, 205, 196, 0.3);
  transition: all 0.3s ease;
}

.profile-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 12px 32px rgba(78, 205, 196, 0.4);
}

.avatar-border {
  position: absolute;
  top: -6px;
  left: -6px;
  right: -6px;
  bottom: -6px;
  border-radius: 50%;
  background: linear-gradient(45deg, #4ecdc4, #45b7d1, #ffa938, #ec4899);
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.profile-avatar-wrapper:hover .avatar-border {
  opacity: 1;
}

/* Greeting Section */
.greeting-section {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(78, 205, 196, 0.1));
  padding: 1rem;
  border-radius: 16px;
  border-left: 6px solid #ff4a77;
}

.greeting-text {
  font-size: 1.4rem;
  font-weight: 700;
  margin: 0;
  color: #1e293b;
  background: linear-gradient(135deg, #229cff, #1d68d8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* User Details Section */
.user-details-section {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.nintendo-info-card {
  background: rgba(255, 255, 255, 0.7);
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  padding: 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.nintendo-info-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  border-color: #4ecdc4;
}

.detail-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  background: linear-gradient(135deg, #3b82f6, #60a5fa);
}

.detail-icon.email-icon {
  background: linear-gradient(135deg, #4ecdc4, #44a08d);
}

.detail-icon.age-icon {
  background: linear-gradient(135deg, #f9ca24, #f0932b);
}

.detail-icon.gender-icon {
  background: linear-gradient(135deg, #ec4899, #f472b6);
}

.detail-content {
  flex: 1;
  text-align: left;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.detail-label {
  font-size: 0.75rem;
  font-weight: 700;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.detail-value {
  font-size: 1rem;
  font-weight: 600;
  color: #1e293b;
}

/* Action Section */
.action-section {
  margin-top: 0.5rem;
}

.nintendo-btn {
  background: linear-gradient(135deg, #3b82f6, #60a5fa);
  color: white;
  border: none;
  border-radius: 16px;
  padding: 1rem 2rem;
  font-family: 'Nunito', sans-serif;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 16px rgba(59, 130, 246, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  width: 100%;
  position: relative;
  overflow: hidden;
}

.nintendo-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.nintendo-btn:hover::before {
  opacity: 1;
}

.nintendo-btn:hover {
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 8px 24px rgba(59, 130, 246, 0.4);
}

.btn-icon {
  transition: transform 0.3s ease;
}

.nintendo-btn:hover .btn-icon {
  transform: rotate(15deg);
}

/* Responsive Design */
@media (max-width: 768px) {
  .card-content {
    padding: 1.5rem 1rem;
    gap: 1rem;
  }

  .greeting-text {
    font-size: 1.2rem;
  }

  .nintendo-info-card {
    padding: 0.75rem;
  }

  .detail-icon {
    width: 36px;
    height: 36px;
  }

  .nintendo-btn {
    padding: 0.875rem 1.5rem;
    font-size: 0.9rem;
  }
}

/* CSS Variables for consistency */
:root {
  --joy-teal: #4ecdc4;
  --joy-blue: #3b82f6;
  --joy-pink: #ec4899;
  --joy-orange: #f97316;
  --joy-yellow: #f9ca24;
  --joy-purple: #6c5ce7;
}
</style>
