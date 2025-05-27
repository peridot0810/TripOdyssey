<template>
  <div class="ai-helper-schedule">
    <div
      ref="aiButton"
      class="ai-helper-button"
      @click="handleAiHelperClick"
      @mouseenter="showTooltip = true"
      @mouseleave="showTooltip = false"
    >
      <img src="/img/schedule.PNG" alt="AI Helper" class="ai-helper-image" />
    </div>

    <!-- Custom Dialog-style Tooltip with Fixed Positioning -->
    <Teleport to="body">
      <div v-if="showTooltip && !showDialog" class="ai-dialog-tooltip" :style="tooltipStyle">
        <div class="dialog-bubble">
          일정 짜기가<br />
          막막하면<br />
          저에게<br />
          물어보세요!
        </div>
        <div class="dialog-pointer"></div>
      </div>
    </Teleport>

    <!-- AI Helper Dialog -->
    <AiHelperDialog v-model="showDialog" class="dialog" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import AiHelperDialog from './AiHelperDialog.vue'

const showTooltip = ref(false)
const showDialog = ref(false)
const aiButton = ref(null)

const tooltipStyle = computed(() => {
  if (!aiButton.value) return {}

  const rect = aiButton.value.getBoundingClientRect()
  return {
    position: 'fixed',
    left: `${rect.right + 10}px`,
    top: `${rect.top - 80}px`,
    zIndex: 9999,
  }
})

const handleAiHelperClick = () => {
  showDialog.value = true
  showTooltip.value = false
}
</script>

<style scoped>
.ai-helper-schedule {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 16px;
  position: relative;
}

.ai-helper-button {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 3px solid #5a152c; /* Pink border */
  overflow: hidden;
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
}

.ai-helper-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.25);
}

.ai-helper-button:active {
  transform: translateY(0) scale(0.98);
}

.ai-helper-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

/* Dialog-style Tooltip with Fixed Positioning */
.ai-dialog-tooltip {
  animation: fadeInScale 0.2s ease-out;
  pointer-events: none;
}

.dialog-bubble {
  background-color: #4caf50;
  color: white;
  padding: 12px 14px;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 500;
  line-height: 1.3;
  text-align: center;
  width: 100px;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
  position: relative;
}

.dialog-pointer {
  position: absolute;
  left: -4px;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-top: 4px solid transparent;
  border-bottom: 4px solid transparent;
  border-right: 4px solid #4caf50;
}

@keyframes fadeInScale {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
