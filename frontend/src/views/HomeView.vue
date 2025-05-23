<template>
  <div class="home-container">
    <h1 class="page-title">My Groups</h1>

    <!-- Group Cards Carousel -->
    <div class="group-carousel-container">
      <div class="carousel-wrapper">
        <!-- Left Arrow -->
        <v-btn
          icon
          variant="text"
          class="carousel-arrow left-arrow"
          @click="scrollLeft"
          :disabled="currentIndex === 0"
        >
          <v-icon>mdi-chevron-left</v-icon>
        </v-btn>

        <!-- Group Cards Container -->
        <div class="group-cards-container" ref="carouselContainer">
          <div
            class="group-cards-track"
            :style="{ transform: `translateX(-${currentIndex * cardWidth}px)` }"
          >
            <div v-for="group in groups" :key="group.groupId" class="group-card-slot">
              <GroupInfoCard :group="group" />
            </div>
          </div>
        </div>

        <!-- Right Arrow -->
        <v-btn
          icon
          variant="text"
          class="carousel-arrow right-arrow"
          @click="scrollRight"
          :disabled="currentIndex >= maxIndex"
        >
          <v-icon>mdi-chevron-right</v-icon>
        </v-btn>
      </div>
    </div>

    <!-- Create Group Button -->
    <div class="create-group-section">
      <v-btn
        size="large"
        color="primary"
        variant="elevated"
        class="create-group-btn"
        @click="openCreateGroupDialog"
      >
        <v-icon left>mdi-plus</v-icon>
        Create New Group
      </v-btn>
    </div>

    <!-- Create Group Dialog -->
    <CreateGroupDialog v-model="showCreateDialog" @group-created="handleGroupCreated" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { groupData } from '@/data/group/groupData'
import GroupInfoCard from '@/components/group/GroupInfoCard.vue'
import CreateGroupDialog from '@/components/group/CreateGroupDialog.vue'

const groups = ref([...groupData])
const currentIndex = ref(0)
const cardWidth = ref(380) // Width of each card + gap
const carouselContainer = ref(null)
const showCreateDialog = ref(false)

// Calculate maximum index for navigation
const maxIndex = computed(() => {
  const visibleCards = Math.floor(window.innerWidth / cardWidth.value) || 3
  return Math.max(0, groups.value.length - visibleCards)
})

const scrollLeft = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--
  }
}

const scrollRight = () => {
  if (currentIndex.value < maxIndex.value) {
    currentIndex.value++
  }
}

const openCreateGroupDialog = () => {
  showCreateDialog.value = true
}

const handleGroupCreated = (newGroup) => {
  groups.value.push(newGroup)
  showCreateDialog.value = false
}

// Handle window resize
const handleResize = () => {
  // Reset current index if needed when window resizes
  if (currentIndex.value > maxIndex.value) {
    currentIndex.value = maxIndex.value
  }
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.home-container {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: calc(100vh - 64px); /* Full height minus app bar */
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.page-title {
  font-size: 2rem;
  font-weight: 600;
  margin-bottom: 48px;
  color: #333;
  text-align: center;
}

.group-carousel-container {
  margin-bottom: 48px;
  flex: 0 0 auto;
}

.carousel-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  gap: 16px;
}

.carousel-arrow {
  flex-shrink: 0;
  width: 48px;
  height: 48px;
  background-color: white;
  border: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.carousel-arrow:hover {
  background-color: #f5f5f5;
}

.carousel-arrow:disabled {
  opacity: 0.3;
}

.group-cards-container {
  flex: 1;
  overflow: hidden;
  position: relative;
}

.group-cards-track {
  display: flex;
  gap: 20px;
  transition: transform 0.3s ease;
  will-change: transform;
}

.group-card-slot {
  flex-shrink: 0;
  width: 360px;
}

.create-group-section {
  display: flex;
  justify-content: center;
  margin-top: 32px;
  flex: 0 0 auto;
}

.create-group-btn {
  padding: 16px 32px;
  font-size: 1.1rem;
  font-weight: 500;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .home-container {
    padding: 16px;
  }

  .page-title {
    font-size: 1.5rem;
    margin-bottom: 24px;
  }

  .carousel-wrapper {
    gap: 8px;
  }

  .carousel-arrow {
    width: 40px;
    height: 40px;
  }

  .group-card-slot {
    width: 340px;
  }

  .group-cards-track {
    gap: 16px;
  }
}
</style>
