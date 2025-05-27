<template>
  <div class="helper-space">
    <!-- Schedule AI Helper - Only show on schedule pages -->
    <AiHelperSchedule v-if="isSchedulePage" />

    <!-- Transportation AI Helper - Only show on logistics pages -->
    <TransportationAiHelper v-if="isLogisticsPage" />

    <!-- Finance AI Helper - Only show on finance pages -->
    <FinanceAiHelper v-if="isFinancePage" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AiHelperSchedule from './AiHelperSchedule.vue'
import TransportationAiHelper from './TransportationAiHelper.vue'
import FinanceAiHelper from './FinanceAiHelper.vue'

const route = useRoute()

// Check current page types
const isSchedulePage = computed(() => {
  return route.path.includes('/schedule')
})

const isLogisticsPage = computed(() => {
  return route.path.includes('/logistics')
})

const isFinancePage = computed(() => {
  return route.path.includes('/finance')
})
</script>

<style scoped>
.helper-space {
  position: fixed;
  bottom: 60px;
  left: 62px;
  z-index: 1000;
  pointer-events: none;
}

/* Allow pointer events on child components */
.helper-space > * {
  pointer-events: auto;
}
</style>
