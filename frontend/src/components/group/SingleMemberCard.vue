<template>
  <div class="member-card">
    <!-- Left: Avatar Icon -->
    <div class="user-icon">
      <v-icon size="20" color="white">mdi-account</v-icon>
    </div>

    <!-- Center: Nickname + ID -->
    <div class="user-info">
      <div class="nickname">{{ member.nickName }}</div>
      <div class="user-id">@{{ member.userId }}</div>
    </div>

    <!-- Right: Role Badge -->
    <div :class="['role-badge', roleBadgeClass]">
      {{ displayRole }}
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useMemberListStore } from '@/stores/memberList'

const props = defineProps({
  member: {
    type: Object,
    required: true,
  },
})

const store = useMemberListStore()
const roleKey = computed(() => store.getMemberRole(props.member))
const displayRole = computed(() => store.getRoleDisplayName(roleKey.value))

const roleBadgeClass = computed(() => {
  return {
    MASTER: 'role-badge--master',
    FINANCE: 'role-badge--finance',
    SCHEDULE: 'role-badge--schedule',
    LOGISTICS: 'role-badge--logistics',
    MEMBER: 'role-badge--member',
  }[roleKey.value]
})
</script>

<style scoped>
.member-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-md) var(--space-lg);
  border-radius: var(--radius-large);
  background: var(--card-bg);
  border: 2px solid var(--border-light);
  font-family: 'Nunito', sans-serif;
  width: 92%;
  max-width: 600px;
  margin: 0 auto var(--space-sm);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
  transition: var(--transition-medium);
}

.user-icon {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-xl);
  background: var(--joy-con-purple);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  margin-right: var(--space-md);
  flex-shrink: 0;
}

.user-info {
  flex: 1;
}

.nickname {
  font-size: 1rem;
  font-weight: 700;
  color: var(--text-primary);
}

.user-id {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin-top: 2px;
}

/* Role badge (Nintendo role badge system) */
.role-badge {
  padding: 0.4rem 0.75rem;
  border-radius: var(--radius-medium);
  font-size: 0.8rem;
  font-weight: 600;
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
  white-space: nowrap;
  min-width: 70px;
  text-align: center;
}

/* Role-specific badge variants */
.role-badge--master {
  background: var(--joy-con-red-gradient);
  box-shadow: 0 1px 6px rgba(255, 107, 107, 0.3);
}

.role-badge--finance {
  background: var(--joy-con-teal-gradient);
  box-shadow: 0 1px 6px rgba(78, 205, 196, 0.3);
}

.role-badge--schedule {
  background: var(--joy-con-blue-gradient);
  box-shadow: 0 1px 6px rgba(69, 183, 209, 0.3);
}

.role-badge--logistics {
  background: var(--joy-con-yellow-gradient);
  box-shadow: 0 1px 6px rgba(249, 202, 36, 0.3);
}

.role-badge--member {
  background: var(--joy-con-purple-gradient);
  box-shadow: 0 1px 6px rgba(102, 126, 234, 0.3);
}
</style>
