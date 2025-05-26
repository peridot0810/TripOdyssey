<template>
  <v-card class="groups-card" elevation="3">
    <v-card-title class="text-h6 font-weight-bold d-flex align-center">
      <v-icon color="primary" class="mr-2">mdi-account-group</v-icon>
      그룹 정보
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text class="pa-0">
      <v-tabs v-model="activeTab" color="primary" fixed-tabs>
        <v-tab value="joined">
          <v-icon class="mr-1">mdi-account-check</v-icon>
          참여 그룹 ({{ joinedGroups.length }})
        </v-tab>
        <v-tab value="invited">
          <v-icon class="mr-1">mdi-email</v-icon>
          초대 받은 그룹 ({{ invitedGroups.length }})
        </v-tab>
      </v-tabs>

      <v-tabs-window v-model="activeTab">
        <!-- Joined Groups Tab -->
        <v-tabs-window-item value="joined">
          <div class="pa-4">
            <div v-if="joinedGroups.length === 0" class="empty-state text-center py-8">
              <v-icon size="64" color="grey-lighten-1">mdi-account-group-outline</v-icon>
              <p class="text-body-1 text-grey mt-3">참여 중인 그룹이 없습니다</p>
            </div>

            <div v-else>
              <v-card
                v-for="group in joinedGroups"
                :key="group.id"
                class="group-card mb-3"
                variant="outlined"
                hover
              >
                <v-card-text class="pa-3">
                  <div class="d-flex justify-space-between align-start mb-2">
                    <div>
                      <h3 class="text-subtitle-1 font-weight-bold">{{ group.name }}</h3>
                      <p class="text-caption text-grey mb-1">
                        {{ group.startDate }} ~ {{ group.endDate }}
                      </p>
                    </div>
                    <template v-for="role in group.myRole" :key="role">
                      <v-chip :color="getRoleColor(role)" size="small" variant="flat">
                        {{ role }}
                      </v-chip>
                    </template>
                    
                  </div>

                  <div class="d-flex align-center">
                    <v-icon size="small" color="grey" class="mr-1">mdi-account-multiple</v-icon>
                    <span class="text-caption text-grey">{{ group.memberCount }}명</span>
                  </div>
                </v-card-text>
              </v-card>
            </div>
          </div>
        </v-tabs-window-item>

        <!-- Invited Groups Tab -->
        <v-tabs-window-item value="invited">
          <div class="pa-4">
            <div v-if="invitedGroups.length === 0" class="empty-state text-center py-8">
              <v-icon size="64" color="grey-lighten-1">mdi-email-outline</v-icon>
              <p class="text-body-1 text-grey mt-3">새로운 초대가 없습니다</p>
            </div>

            <div v-else>
              <v-card
                v-for="invitation in invitedGroups"
                :key="invitation.groupId"
                class="invitation-card mb-3"
                variant="outlined"
                :class="{ 'invitation-highlight': true }"
              >
                <v-card-text class="pa-3">
                  <div class="mb-3">
                    <h3 class="text-subtitle-1 font-weight-bold mb-1">{{ invitation.groupInfo.name }}</h3>
                    <p class="text-body-2 text-grey-darken-1 mb-2">{{ invitation.groupInfo.description }}</p>
                    <p class="text-caption text-grey">
                      {{ invitation.groupInfo.startDate }} ~ {{ invitation.groupInfo.endDate }}
                    </p>
                  </div>

                  <div class="d-flex justify-space-between align-center mb-3">
                    <div class="d-flex align-center">
                      <v-icon size="small" color="grey" class="mr-1">mdi-account-multiple</v-icon>
                      <span class="text-caption text-grey">
                        {{invitation.memberCount}}/(최대 멤버 수)명
                      </span>
                    </div>

                    <v-chip :color="getStatusColor(invitation.status)" size="small" variant="flat">
                        {{ invitation.status }}
                      </v-chip>
                  </div>

                  <div v-if="invitation.status==='PENDING'" class="d-flex gap-2">
                    <v-btn
                      color="success"
                      variant="flat"
                      size="small"
                      prepend-icon="mdi-check"
                      @click="acceptInvitation(invitation.senderId, invitation.groupId)"
                    >
                      수락
                    </v-btn>
                    <v-btn
                      color="error"
                      variant="outlined"
                      size="small"
                      prepend-icon="mdi-close"
                      @click="declineInvitation(invitation.senderId,invitation.groupId)"
                    >
                      거절
                    </v-btn>
                  </div>
                </v-card-text>
              </v-card>
            </div>
          </div>
        </v-tabs-window-item>
      </v-tabs-window>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref } from 'vue'
import { apiClient } from '@/stores/apiClient'

const props = defineProps({
  joinedGroups: {
    type: Array,
    required: true,
  },
  invitedGroups: {
    type: Array,
    required: true
  }
})

const activeTab = ref('joined')

function getRoleColor(role) {
  const roleColors = {
    master: 'primary',
    schedule: 'success',
    finance: 'warning',
    logistics: 'info',
    normal: 'grey',
  }
  return roleColors[role] || 'grey'
}

function getStatusColor(status) {
  const statusColors = {
    PENDING: 'primary',
    REJECTED: 'warning',
    ACCEPTED: 'success'
  }
  return statusColors[status] || 'grey'
}

async function acceptInvitation(senderId, groupId) {
  console.log("props invitedGroups : ")
  console.log(props.invitedGroups)
  const groupIndex = props.invitedGroups.findIndex((g) => g.groupId === groupId)
  console.log("groupIndex : ")
  console.log(groupIndex)
  if (groupIndex !== -1) {

    const acceptedInvitation = props.invitedGroups[groupIndex]
    const res = await apiClient.put("/user/invited", {
      "senderId" : senderId,
      "groupId" : groupId,
      "accept" : true
    })
    acceptedInvitation.status="ACCEPTED"

    // 새로 참여하게 된 그룹 생성 및 추가 
    props.joinedGroups.unshift({
      "groupId" : groupId,
      "memberCount" : acceptedInvitation.memberCount+1,
      "myRole" : ['normal'],
      "name" : acceptedInvitation.groupInfo.name,
      "progress" : acceptedInvitation.groupInfo.progress,
      "startDate" : acceptedInvitation.groupInfo.startDate,
      "endDate" : acceptedInvitation.groupInfo.endDate,
      "status" : acceptedInvitation.groupInfo.status
    })
  }
}

async function declineInvitation(senderId, groupId) {
  const groupIndex = props.invitedGroups.findIndex((g) => g.groupId === groupId)
  if (groupIndex !== -1) {
    // const declinedGroup = props.invitedGroups.splice(groupIndex, 1)[0]

    const declinedInvitation = props.invitedGroups[groupIndex]
    const res = await apiClient.put("/user/invited", {
      "senderId" : senderId,
      "groupId" : groupId,
      "accept" : false
    })

    declinedInvitation.status="REJECTED";
    
    console.log(props.invitedGroups[groupIndex])
  }
}
</script>

<style scoped>
.groups-card {
  border-radius: 16px;
  height: 100%;
}

.group-card {
  border-radius: 12px;
  transition: all 0.2s ease;
}

.group-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.invitation-card {
  border-radius: 12px;
  transition: all 0.2s ease;
}

.invitation-highlight {
  border-left: 4px solid #ff9800;
}

.invitation-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.empty-state {
  background-color: #f5f5f5;
  border-radius: 8px;
}

.v-tabs {
  border-bottom: 1px solid #e0e0e0;
}

.gap-2 {
  gap: 8px;
}
</style>
