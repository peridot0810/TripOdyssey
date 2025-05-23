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
                    <v-chip :color="getRoleColor(group.myRole)" size="small" variant="flat">
                      {{ group.myRole }}
                    </v-chip>
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
                v-for="group in invitedGroups"
                :key="group.id"
                class="invitation-card mb-3"
                variant="outlined"
                :class="{ 'invitation-highlight': true }"
              >
                <v-card-text class="pa-3">
                  <div class="mb-3">
                    <h3 class="text-subtitle-1 font-weight-bold mb-1">{{ group.name }}</h3>
                    <p class="text-body-2 text-grey-darken-1 mb-2">{{ group.description }}</p>
                    <p class="text-caption text-grey">
                      {{ group.startDate }} ~ {{ group.endDate }}
                    </p>
                  </div>

                  <div class="d-flex justify-space-between align-center mb-3">
                    <div class="d-flex align-center">
                      <v-icon size="small" color="grey" class="mr-1">mdi-account-multiple</v-icon>
                      <span class="text-caption text-grey">
                        {{ group.currentMembers }}/{{ group.maxMembers }}명
                      </span>
                    </div>

                    <v-chip color="orange" size="small" variant="flat"> 초대 대기중 </v-chip>
                  </div>

                  <div class="d-flex gap-2">
                    <v-btn
                      color="success"
                      variant="flat"
                      size="small"
                      prepend-icon="mdi-check"
                      @click="acceptInvitation(group.id)"
                    >
                      수락
                    </v-btn>
                    <v-btn
                      color="error"
                      variant="outlined"
                      size="small"
                      prepend-icon="mdi-close"
                      @click="declineInvitation(group.id)"
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

const activeTab = ref('joined')

// Dummy data for joined groups
const joinedGroups = ref([
  {
    id: 1,
    name: '제주도 힐링 여행',
    startDate: '2025-06-15',
    endDate: '2025-06-18',
    myRole: '리더',
    memberCount: 4,
  },
  {
    id: 2,
    name: '부산 맛집 탐방',
    startDate: '2025-07-20',
    endDate: '2025-07-22',
    myRole: '멤버',
    memberCount: 6,
  },
  {
    id: 3,
    name: '강원도 캠핑 여행',
    startDate: '2025-08-10',
    endDate: '2025-08-13',
    myRole: '일정관리',
    memberCount: 5,
  },
])

// Dummy data for invited groups
const invitedGroups = ref([
  {
    id: 4,
    name: '경주 역사 탐방',
    description: '경주의 아름다운 역사 유적지들을 함께 둘러보는 문화 여행입니다.',
    startDate: '2025-09-05',
    endDate: '2025-09-07',
    currentMembers: 3,
    maxMembers: 6,
  },
  {
    id: 5,
    name: '전주 한옥마을 투어',
    description: '전주의 전통과 맛을 느낄 수 있는 특별한 여행을 계획하고 있습니다.',
    startDate: '2025-10-12',
    endDate: '2025-10-14',
    currentMembers: 2,
    maxMembers: 5,
  },
])

function getRoleColor(role) {
  const roleColors = {
    리더: 'primary',
    일정관리: 'success',
    재정관리: 'warning',
    물류관리: 'info',
    멤버: 'grey',
  }
  return roleColors[role] || 'grey'
}

function acceptInvitation(groupId) {
  const groupIndex = invitedGroups.value.findIndex((g) => g.id === groupId)
  if (groupIndex !== -1) {
    const acceptedGroup = invitedGroups.value.splice(groupIndex, 1)[0]

    // Add to joined groups (with default member role)
    joinedGroups.value.push({
      id: acceptedGroup.id,
      name: acceptedGroup.name,
      startDate: acceptedGroup.startDate,
      endDate: acceptedGroup.endDate,
      myRole: '멤버',
      memberCount: acceptedGroup.currentMembers + 1,
    })

    console.log(`Accepted invitation for group: ${acceptedGroup.name}`)
  }
}

function declineInvitation(groupId) {
  const groupIndex = invitedGroups.value.findIndex((g) => g.id === groupId)
  if (groupIndex !== -1) {
    const declinedGroup = invitedGroups.value.splice(groupIndex, 1)[0]
    console.log(`Declined invitation for group: ${declinedGroup.name}`)
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
