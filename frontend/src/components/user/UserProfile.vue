<template>
  <v-card class="user-profile-card" elevation="3">
    <v-card-text class="text-center pa-6">
      <!-- Profile Image -->
      <div class="profile-image-container mb-4">
        <v-avatar size="120" class="profile-avatar">
          <v-img v-if="profileImageUrl" :src="profileImageUrl" alt="프로필 사진"></v-img>
          <svg-icon v-else type="mdi" :path="accountCirclePath" size="60" color="#BDBDBD" />
        </v-avatar>
      </div>

      <!-- User Greeting -->
      <h2 class="text-h5 font-weight-bold mb-2 greeting-text">
        안녕하세요 {{ userStore.userInfo.nickname }} 님!
      </h2>

      <!-- User Details -->
      <div class="user-details">
      <v-divider class="my-4"></v-divider>

      <div class="detail-item mb-3">
      <svg-icon type="mdi" :path="accountPath" size="16" color="#1976d2" class="mr-2" style="vertical-align: middle;" />
      <span class="text-body-1">{{ userStore.userInfo.id }}</span>
      </div>

      <div class="detail-item mb-3">
      <svg-icon type="mdi" :path="emailPath" size="16" color="#1976d2" class="mr-2" style="vertical-align: middle;" />
      <span class="text-body-1">{{ userStore.userInfo.email }}</span>
      </div>

      <div class="detail-item mb-3">
      <svg-icon type="mdi" :path="cakeVariantPath" size="16" color="#1976d2" class="mr-2" style="vertical-align: middle;" />
      <span class="text-body-1">{{ userStore.userInfo.age }}세</span>
      </div>

      <div class="detail-item">
      <svg-icon type="mdi" :path="genderPath" size="16" color="#1976d2" class="mr-2" style="vertical-align: middle;" />
      <span class="text-body-1">{{ genderText }}</span>
      </div>
      </div>

      <!-- Edit Profile Button -->
      <v-btn
        color="primary"
        variant="outlined"
        class="mt-4"
        @click="editDialog = true"
      >
        <svg-icon type="mdi" :path="pencilPath" size="18" class="mr-2" style="vertical-align: middle;" />
        프로필 수정
      </v-btn>
    </v-card-text>
  </v-card>
  
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
  mdiPencil
} from '@mdi/js'

const userStore = useUserStore();
const props = defineProps({
  userInfo: {
    type: Object,
    required: true,
  },
})

const editDialog = ref(false);

// 프로필 정보 저장(수정)시 실행될 함수 (뼈대)
async function onEditProfile(editedUserInfo) {
  await apiClient.put("/user", {
    "newNickname" : editedUserInfo.nickname,
    "newAge" : editedUserInfo.age
  })

  userStore.updateUserInfo({
    nickname: editedUserInfo.nickname,
    age: editedUserInfo.age
  })
}

const profileImageUrl = computed(() => {
  const prefix = "http://localhost:8080"
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
.user-profile-card {
height: 100%;
border-radius: 16px;
}

.profile-image-container {
position: relative;
}

.profile-avatar {
border: 4px solid #1976d2;
box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.greeting-text {
color: #1976d2;
}

.user-details {
text-align: left;
max-width: 280px;
margin: 0 auto;
}

.detail-item {
display: flex;
align-items: center;
padding: 4px 0;
}

.detail-item span {
color: #424242;
}
</style>