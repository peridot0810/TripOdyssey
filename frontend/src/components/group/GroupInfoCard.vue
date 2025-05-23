<template>
  <v-card class="group-card" hover @click="goToGroupPage">
    <div class="card-content">
      <!-- Group Image (Perfect Square) -->
      <div class="group-image">
        <v-img :src="groupImageUrl" :alt="group.name" cover class="image">
          <template v-slot:placeholder>
            <div class="d-flex align-center justify-center fill-height">
              <v-progress-circular indeterminate color="primary"></v-progress-circular>
            </div>
          </template>
        </v-img>
      </div>

      <!-- Group Info -->
      <div class="group-info">
        <div class="group-header">
          <h3 class="group-title">{{ group.name }}</h3>
          <span class="status-badge" :class="statusClass">{{ statusText }}</span>
        </div>

        <p class="group-description">{{ group.description }}</p>

        <div class="date-section">
          <v-chip size="small" color="primary" variant="outlined" class="date-chip">
            <v-icon start size="small">mdi-calendar</v-icon>
            {{ group.startDate }} ~ {{ group.endDate }}
          </v-chip>
        </div>

        <div class="role-limits">
          <div class="limit-header">Role Limits</div>
          <div class="limit-grid">
            <div class="limit-item">
              <span class="limit-label">재무</span>
              <span class="limit-value">{{ group.roleLimits.finance }}</span>
            </div>
            <div class="limit-item">
              <span class="limit-label">일정</span>
              <span class="limit-value">{{ group.roleLimits.schedule }}</span>
            </div>
            <div class="limit-item">
              <span class="limit-label">물류</span>
              <span class="limit-value">{{ group.roleLimits.logistics }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  group: {
    type: Object,
    required: true,
  },
})

const router = useRouter()
const groupImageUrl =
  'https://fastly.picsum.photos/id/20/3670/2462.jpg?hmac=CmQ0ln-k5ZqkdtLvVO23LjVAEabZQx2wOaT4pyeG10I'

const statusText = computed(() => {
  const statusMap = {
    planning: '계획 중',
    in_progress: '진행 중',
    completed: '완료',
  }
  return statusMap[props.group.status] || props.group.status
})

const statusClass = computed(() => {
  return `status-${props.group.status}`
})

const goToGroupPage = () => {
  router.push(`/group/${props.group.groupId}`)
}
</script>

<style scoped>
.group-card {
  width: 100%;
  height: 280px;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #e0e0e0;
}

.group-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.card-content {
  display: flex;
  height: 100%;
}

.group-image {
  width: 120px;
  height: 100%;
  flex-shrink: 0;
}

.image {
  width: 100%;
  height: 100%;
}

.group-info {
  flex: 1;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 8px;
}

.group-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
  line-height: 1.3;
  flex: 1;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 500;
  white-space: nowrap;
}

.status-planning {
  background-color: #e3f2fd;
  color: #1976d2;
}

.status-in_progress {
  background-color: #e8f5e8;
  color: #2e7d32;
}

.status-completed {
  background-color: #f3e5f5;
  color: #7b1fa2;
}

.group-description {
  font-size: 0.85rem;
  color: #666;
  line-height: 1.4;
  margin: 0;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.date-section {
  margin: 4px 0;
}

.date-chip {
  font-size: 0.75rem;
  height: 24px;
}

.role-limits {
  margin-top: auto;
}

.limit-header {
  font-size: 0.8rem;
  font-weight: 500;
  color: #666;
  margin-bottom: 6px;
}

.limit-grid {
  display: flex;
  gap: 12px;
}

.limit-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.limit-label {
  font-size: 0.7rem;
  color: #888;
}

.limit-value {
  font-size: 0.85rem;
  font-weight: 600;
  color: #333;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .group-card {
    height: 260px;
  }

  .group-image {
    width: 100px;
  }

  .group-info {
    padding: 12px;
    gap: 10px;
  }

  .group-title {
    font-size: 1rem;
  }

  .group-description {
    font-size: 0.8rem;
  }
}
</style>
