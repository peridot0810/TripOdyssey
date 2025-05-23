<template>
  <div class="group-member-list">
    <!-- 1. Role Limits Container -->
    <v-card class="role-limits-card mb-4" elevation="1">
      <v-card-item class="pa-3">
        <v-card-title class="text-subtitle-1 font-weight-bold pa-0 mb-2">
          <v-icon size="small" class="mr-1">mdi-account-supervisor</v-icon>
          역할 제한
        </v-card-title>

        <div class="role-limits-grid">
          <div class="role-limit-item">
            <div class="role-limit-label">일정</div>
            <div class="role-limit-value">{{ groupInfo.roleLimits.schedule }}명</div>
          </div>
          <div class="role-limit-item">
            <div class="role-limit-label">교통/숙소</div>
            <div class="role-limit-value">{{ groupInfo.roleLimits.logistics }}명</div>
          </div>
          <div class="role-limit-item">
            <div class="role-limit-label">재무</div>
            <div class="role-limit-value">{{ groupInfo.roleLimits.finance }}명</div>
          </div>
        </div>
      </v-card-item>
    </v-card>

    <!-- 2. Member List Container -->
    <v-card class="member-list-card" elevation="1">
      <v-card-item class="pa-3">
        <v-card-title class="text-subtitle-1 font-weight-bold pa-0 mb-3">
          <v-icon size="small" class="mr-1">mdi-account-group</v-icon>
          그룹 멤버 ({{ sortedMembers.length }}명 / 총 {{ members.length }}명)
        </v-card-title>

        <v-list class="member-list pa-0">
          <v-list-item
            v-for="member in sortedMembers"
            :key="member.userId"
            class="member-item px-2 py-2"
          >
            <template v-slot:prepend>
              <v-avatar :color="getAvatarColor(member.roleName)" size="36">
                <v-icon color="white" size="20">
                  {{ getRoleIcon(member.roleName) }}
                </v-icon>
              </v-avatar>
            </template>

            <v-list-item-title class="member-name font-weight-medium">
              {{ member.nickName }}
            </v-list-item-title>

            <v-list-item-subtitle class="member-id text-caption">
              @{{ member.userId }}
            </v-list-item-subtitle>

            <template v-slot:append>
              <v-chip
                :color="getRoleColor(member.roleName)"
                :variant="member.roleName === 'normal' ? 'tonal' : 'elevated'"
                size="small"
                class="role-chip"
              >
                <v-icon start size="x-small">
                  {{ getRoleIcon(member.roleName) }}
                </v-icon>
                {{ getRoleName(member.roleName) }}
              </v-chip>
            </template>
          </v-list-item>
        </v-list>
      </v-card-item>
    </v-card>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  members: {
    type: Array,
    required: true,
  },
  groupInfo: {
    type: Object,
    required: true,
  },
})

// Sort members: master first, then roles, then normal
const sortedMembers = computed(() => {
  console.log('Original members:', props.members) // Debug log

  const roleOrder = {
    master: 1,
    schedule: 2,
    finance: 3,
    logistics: 4,
    normal: 5,
  }

  const sorted = [...props.members].sort((a, b) => {
    const aOrder = roleOrder[a.roleName] || 999
    const bOrder = roleOrder[b.roleName] || 999

    if (aOrder !== bOrder) {
      return aOrder - bOrder
    }

    // If same role, sort by nickname
    return a.nickName.localeCompare(b.nickName)
  })

  console.log('Sorted members:', sorted) // Debug log
  return sorted
})

// Role color mapping
const getRoleColor = (role) => {
  const colors = {
    master: 'deep-purple',
    schedule: 'blue',
    finance: 'green',
    logistics: 'orange',
    normal: 'grey',
  }
  return colors[role] || 'grey'
}

// Avatar color mapping
const getAvatarColor = (role) => {
  const colors = {
    master: 'deep-purple-darken-1',
    schedule: 'blue-darken-1',
    finance: 'green-darken-1',
    logistics: 'orange-darken-1',
    normal: 'grey-darken-1',
  }
  return colors[role] || 'grey-darken-1'
}

// Role icon mapping
const getRoleIcon = (role) => {
  const icons = {
    master: 'mdi-crown',
    schedule: 'mdi-calendar',
    finance: 'mdi-calculator',
    logistics: 'mdi-truck',
    normal: 'mdi-account',
  }
  return icons[role] || 'mdi-account'
}

// Role name mapping
const getRoleName = (role) => {
  const names = {
    master: '마스터',
    schedule: '일정',
    finance: '재무',
    logistics: '교통/숙소',
    normal: '일반',
  }
  return names[role] || '일반'
}
</script>

<style scoped>
.group-member-list {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.role-limits-card {
  flex-shrink: 0;
}

.role-limits-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.role-limit-item {
  text-align: center;
  padding: 8px;
  background-color: #f5f5f5;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.role-limit-label {
  font-size: 11px;
  color: #666;
  font-weight: 500;
  margin-bottom: 4px;
}

.role-limit-value {
  font-size: 14px;
  font-weight: bold;
  color: #1976d2;
}

.member-list-card {
  flex: 1;
  min-height: 0;
}

.member-list {
  max-height: calc(100% - 60px);
  overflow-y: auto;
}

.member-item {
  border-radius: 8px;
  margin-bottom: 8px;
  transition: all 0.2s ease;
  border: 1px solid transparent;
}

.member-item:hover {
  background-color: #f5f5f5;
  border-color: #e0e0e0;
  transform: translateX(2px);
}

.member-name {
  font-size: 14px;
  line-height: 1.3;
}

.member-id {
  font-size: 11px;
  opacity: 0.7;
}

.role-chip {
  font-weight: 500;
  font-size: 10px;
}

/* Custom scrollbar for member list */
.member-list::-webkit-scrollbar {
  width: 4px;
}

.member-list::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 2px;
}

.member-list::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .role-limits-grid {
    gap: 8px;
  }

  .role-limit-item {
    padding: 6px;
  }

  .role-limit-label {
    font-size: 10px;
  }

  .role-limit-value {
    font-size: 12px;
  }

  .member-name {
    font-size: 13px;
  }

  .member-id {
    font-size: 10px;
  }
}
</style>
