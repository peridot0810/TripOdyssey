<template>
  <v-card class="user-profile-card" elevation="3">
    <v-card-text class="text-center pa-6">
      <!-- Profile Image -->
      <div class="profile-image-container mb-4">
        <v-avatar size="120" class="profile-avatar">
          <v-img v-if="profileImageUrl" :src="profileImageUrl" alt="프로필 사진"></v-img>
          <v-icon v-else size="60" color="grey-lighten-1">mdi-account-circle</v-icon>
        </v-avatar>
      </div>

      <!-- User Greeting -->
      <h2 class="text-h5 font-weight-bold mb-2 greeting-text">
        안녕하세요 {{ userInfo.nickname }} 님!
      </h2>

      <!-- User Details -->
      <div class="user-details">
        <v-divider class="my-4"></v-divider>

        <div class="detail-item mb-3">
          <v-icon size="small" color="primary" class="mr-2">mdi-account</v-icon>
          <span class="text-body-1">{{ userInfo.userId }}</span>
        </div>

        <div class="detail-item mb-3">
          <v-icon size="small" color="primary" class="mr-2">mdi-email</v-icon>
          <span class="text-body-1">{{ userInfo.email }}</span>
        </div>

        <div class="detail-item mb-3">
          <v-icon size="small" color="primary" class="mr-2">mdi-cake-variant</v-icon>
          <span class="text-body-1">{{ userInfo.age }}세</span>
        </div>

        <div class="detail-item">
          <v-icon size="small" color="primary" class="mr-2">
            {{ userInfo.gender === 'male' ? 'mdi-gender-male' : 'mdi-gender-female' }}
          </v-icon>
          <span class="text-body-1">{{ genderText }}</span>
        </div>
      </div>

      <!-- Edit Profile Button -->
      <v-btn color="primary" variant="outlined" class="mt-4" prepend-icon="mdi-pencil">
        프로필 수정
      </v-btn>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  userInfo: {
    type: Object,
    required: true,
  },
})

// For now, profile image URL is empty
const imageUrlPrefix="http://localhost:8080"
const profileImageUrl = props.userInfo.profileImage ? imageUrlPrefix + props.userInfo.profileImage : '';

const genderText = computed(() => {
  return props.userInfo.gender === 'male' ? '남성' : '여성'
})
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
