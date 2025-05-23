<template>
  <v-container fluid class="pa-4">
    <v-row>
      <!-- Left Column: User Profile -->
      <v-col cols="12" md="4">
        <UserProfile :userInfo="userInfo" />
      </v-col>

      <!-- Right Column: Experience and Groups -->
      <v-col cols="12" md="8">
        <v-row>
          <!-- Upper Right: Experience Stats -->
          <v-col cols="12">
            <UserExperience :experienceData="userInfo" />
          </v-col>

          <!-- Lower Right: Group Info -->
          <v-col cols="12">
            <UserGroups />
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import UserProfile from '@/components/user/UserProfile.vue'
import UserExperience from '@/components/user/UserExperience.vue'
import UserGroups from '@/components/user/UserGroups.vue'

const userInfo = ref({
  email: 'user01@example.com',
  gender: 'male',
  age: 25,
  nickname: '여행러버',
  userId: 'traveler_01',
  leaderExp: 100,
  scheduleExp: 50,
  financeExp: 30,
  logisticsExp: 20,
  memberExp: 80,
})
</script>

<style scoped>
.v-container {
  max-width: 1200px;
}
</style>
