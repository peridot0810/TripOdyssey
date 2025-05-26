<template>
  <template v-if="isLoaded">
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
              <UserGroups 
              :joinedGroups="joinedGroups"
              :invitedGroups="invitedGroups"/>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </template>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import UserProfile from '@/components/user/UserProfile.vue'
import UserExperience from '@/components/user/UserExperience.vue'
import UserGroups from '@/components/user/UserGroups.vue'
import { useUserStore } from '@/stores/user'
import { apiClient } from '@/stores/apiClient'

const userStore = useUserStore()

const userInfo = ref(null)
const joinedGroups = ref([])
const invitedGroups = ref([])

onMounted(async () => {
  try {

    // 유저 정보 
    const userInfoRes = await apiClient.get("/user")
    userInfo.value = userInfoRes.data
    console.log("userInfo : ")
    console.log(userInfo.value)

    // 속한 그룹 정보
    const groupsRes = await apiClient.get("/user/groups")
    joinedGroups.value = groupsRes.data
    console.log("joinedGroups : ")
    console.log(joinedGroups.value)

    // 초대받은 그룹 정보
    const invitedRes = await apiClient.get("/user/invited")
    invitedGroups.value = invitedRes.data.data
    console.log("invitedGroups : ")
    console.log(invitedGroups.value)

  } catch (error) {
    console.log(error)
  }
})

const isLoaded = computed(() =>
  userInfo.value !== null &&
  joinedGroups.value !== null &&
  invitedGroups.value !== null
)
</script>

<style scoped>
.v-container {
  max-width: 1200px;
}
</style>
