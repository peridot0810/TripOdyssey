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
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

.member-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.25rem;
  border-radius: 24px;
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  margin-bottom: 0.75rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.1), 0 2px 4px rgba(0, 0, 0, 0.05);
  font-family: 'Nunito', sans-serif;
  position: relative;
  overflow: hidden;
  width: 92%;
  max-width: 600px;
  margin: 0 auto 0.75rem;
}

/* Role-specific border colors */
.member-card {
  border: 3px solid #4a5568; /* Default dark gray for member */
}

.member-card:has(.role-badge--master) {
  border: 3px solid #667eea; /* Purple for master */
}

.member-card:has(.role-badge--finance) {
  border: 3px solid #ff8c42; /* Orange for finance */
}

.member-card:has(.role-badge--logistics) {
  border: 3px solid #4da6ff; /* Blue for logistics */
}

.member-card:has(.role-badge--schedule) {
  border: 3px solid #ffb3d9; /* Pink for schedule */
}

.member-card::before {
  /* Removed rainbow top border */
  display: none;
}

.member-card:hover {
  box-shadow: 0 8px 20px rgba(0, 123, 255, 0.15), 0 4px 8px rgba(0, 0, 0, 0.1);
  /* Hover maintains the same border color - no change */
}

.user-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
  margin-right: 0.75rem;
  flex-shrink: 0;
}

.user-info {
  flex: 1;
}

.nickname {
  font-weight: 700;
  color: #2d3748;
  font-size: 1.1rem;
  letter-spacing: -0.025em;
}

.user-id {
  color: #718096;
  font-size: 0.9rem;
  font-weight: 600;
  background: #edf2f7;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  margin-top: 0.25rem;
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

/* Role-specific colors matching the master member card */
.role-badge--master {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.role-badge--finance {
  background: #ff8c42;
  box-shadow: 0 2px 8px rgba(255, 140, 66, 0.3);
}

.role-badge--logistics {
  background: #4da6ff;
  box-shadow: 0 2px 8px rgba(77, 166, 255, 0.3);
}

.role-badge--schedule {
  background: #ffb3d9;
  color: #6b2c5c;
  box-shadow: 0 2px 8px rgba(255, 179, 217, 0.3);
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
</style>
