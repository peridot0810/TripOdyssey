<template>
  <v-card class="groups-card" elevation="3">
    <v-card-title class="text-h6 font-weight-bold d-flex align-center">
      <svg-icon type="mdi" :path="accountGroupPath" size="22" color="#1976d2" class="mr-2" style="vertical-align: middle;" />
      그룹 정보
    </v-card-title>
    <v-divider></v-divider>

    <v-card-text class="pa-0">
      <v-tabs v-model="activeTab" color="primary" fixed-tabs>
        <v-tab value="joined">
          <svg-icon type="mdi" :path="accountCheckPath" size="18" class="mr-1" style="vertical-align: middle;" />
          참여 그룹 ({{ joinedGroups.length }})
        </v-tab>
        <v-tab value="invited">
          <svg-icon type="mdi" :path="emailPath" size="18" class="mr-1" style="vertical-align: middle;" />
          초대 받은 그룹 ({{ invitedGroups.length }})
        </v-tab>
      </v-tabs>

      <v-tabs-window v-model="activeTab">
        <!-- Joined Groups Tab -->
        <v-tabs-window-item value="joined">
          <div class="pa-4">
            <div v-if="joinedGroups.length === 0" class="empty-state text-center py-8">
              <svg-icon type="mdi" :path="accountGroupOutlinePath" size="64" color="#BDBDBD" />
              <p class="text-body-1 text-grey mt-3">참여 중인 그룹이 없습니다</p>
            </div>

            <div v-else>
              <v-card
                v-for="group in sortedJoinedGroups"
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
                    <svg-icon type="mdi" :path="accountMultiplePath" size="16" color="#757575" class="mr-1" style="vertical-align: middle;" />
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
              <svg-icon type="mdi" :path="emailOutlinePath" size="64" color="#BDBDBD" />
              <p class="text-body-1 text-grey mt-3">새로운 초대가 없습니다</p>
            </div>

            <div v-else>
              <v-card
                v-for="invitation in sortedInvitedGroups"
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
                      <svg-icon type="mdi" :path="accountMultiplePath" size="16" color="#757575" class="mr-1" style="vertical-align: middle;" />
                      <span class="text-caption text-grey">
                        {{invitation.memberCount}}/(최대 멤버 수)명
                      </span>
                    </div>

                    <v-chip :color="getStatusColor(invitation.status)" size="small" variant="flat">
                      {{ invitation.status }}
                    </v-chip>
                    {{ invitation.createdAt }}
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
import { ref, computed } from 'vue'
import SvgIcon from '@jamescoyle/vue-icon'
import {
  mdiAccountGroup,
  mdiAccountCheck,
  mdiEmail,
  mdiAccountGroupOutline,
  mdiAccountMultiple,
  mdiEmailOutline
} from '@mdi/js'
import { apiClient } from '@/stores/apiClient'

const accountGroupPath = mdiAccountGroup
const accountCheckPath = mdiAccountCheck
const emailPath = mdiEmail
const accountGroupOutlinePath = mdiAccountGroupOutline
const accountMultiplePath = mdiAccountMultiple
const emailOutlinePath = mdiEmailOutline

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

// 참여 그룹: 여행 시작일 내림차순(최신 → 과거)
const sortedJoinedGroups = computed(() => {
  // 날짜가 최신(큰 값) → 과거(작은 값) 순서로 내림차순 정렬
  return [...props.joinedGroups].sort((a, b) => {
    return new Date(b.startDate) - new Date(a.startDate)
  })
})

// 초대 그룹: 초대 생성일 내림차순(최근 초대 → 과거 초대)
const sortedInvitedGroups = computed(() => {
  return [...props.invitedGroups].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
})

const emit = defineEmits(['accept-invitation'])

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
  const groupIndex = props.invitedGroups.findIndex((g) => g.groupId === groupId)
  if (groupIndex !== -1) {
    const acceptedInvitation = props.invitedGroups[groupIndex]
    await apiClient.put("/user/invited", {
      senderId,
      groupId,
      accept: true
    })
    acceptedInvitation.status = "ACCEPTED"
    acceptedInvitation.memberCount+=1;
    emit('accept-invitation', {
      newGroup: {
        groupId,
        memberCount: acceptedInvitation.memberCount,
        myRole: ['normal'],
        name: acceptedInvitation.groupInfo.name,
        progress: acceptedInvitation.groupInfo.progress,
        startDate: acceptedInvitation.groupInfo.startDate,
        endDate: acceptedInvitation.groupInfo.endDate,
        status: acceptedInvitation.groupInfo.status
      }
    })
  }
}


async function declineInvitation(senderId, groupId) {
  const groupIndex = props.invitedGroups.findIndex((g) => g.groupId === groupId)
  if (groupIndex !== -1) {
    // const declinedGroup = props.invitedGroups.splice(groupIndex, 1)[0]

    const declinedInvitation = props.invitedGroups[groupIndex]
    await apiClient.put("/user/invited", {
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
