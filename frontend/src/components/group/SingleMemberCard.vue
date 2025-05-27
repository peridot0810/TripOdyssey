<template>
  <div class="member-card" :class="`border--${roleKey}`">
    <!-- Left: Avatar Icon -->
    <div class="user-icon">
      <img :src="profileImageSrc" alt="profile" class="avatar-img" />
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

const profileImageSrc = computed(() => {
  const role = roleKey.value?.toLowerCase()
  const validRoles = ['master', 'finance', 'schedule', 'logistics']
  return validRoles.includes(role) ? `/img/${role}.PNG` : '/img/normal.PNG'
})

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
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

.member-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.25rem;
  border-radius: 16px;
  background: #ffffff;
  margin-bottom: 0.75rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  font-family: 'Nunito', sans-serif;
  width: 92%;
  max-width: 600px;
  margin: 0 auto 0.75rem;
  border: 2px solid #e2e8f0; /* default border, overridden by role class */
}

.member-card::before {
  /* Removed rainbow top border */
  display: none;
}

.member-card:hover {
  box-shadow:
    0 8px 20px rgba(0, 123, 255, 0.15),
    0 4px 8px rgba(0, 0, 0, 0.1);
  /* Hover maintains the same border color - no change */
}

.user-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #6c5ce7; /* flat Nintendo-style lavender */
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 0.75rem;
  flex-shrink: 0;
}

.user-info {
  flex: 1;
}

.nickname {
  font-weight: 600;
  color: #2d3748;
  font-size: 1rem;
}

.user-id {
  font-size: 0.8rem;
  background: #f1f5f9;
  padding: 0.2rem 0.4rem;
  border-radius: 8px;
  color: #4a5568;
  margin-top: 0.2rem;
  display: inline-block;
}

/* Role badge (Nintendo role badge system) */
.role-badge {
  color: white;
  padding: 0.5rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
  transition: all 0.2s ease;
  border: 2px solid rgba(255, 255, 255, 0.3);
  white-space: nowrap;
  min-width: 70px;
  text-align: center;
}

.role-badge--master {
  background: #6c5ce7;
}

.role-badge--finance {
  background: #ff6b6b;
}

.role-badge--schedule {
  background: #45b7d1;
}

.role-badge--logistics {
  background: #f9ca24;
}

.role-badge--member {
  background: #f1f3f4;
  color: #5f6368;
  border: 2px solid #e8eaed;
  box-shadow: 0 2px 8px rgba(95, 99, 104, 0.2);
}

/* Responsive */
@media (max-width: 768px) {
  .member-card {
    width: 100%;
    padding: 1rem;
    margin-bottom: 0.5rem;
  }

  .user-icon {
    width: 36px;
    height: 36px;
    margin-right: 0.5rem;
  }

  .nickname {
    font-size: 1rem;
  }

  .user-id {
    font-size: 0.8rem;
  }

  .role-badge {
    font-size: 0.8rem;
    padding: 0.4rem 0.6rem;
  }
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}
</style>
