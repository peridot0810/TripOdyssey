<template>
  <div class="finance-ai-helper">
    <v-btn
      icon
      size="large"
      color="warning"
      variant="elevated"
      class="ai-helper-button"
      @click="showDialog = true"
    >
      <v-icon size="large">mdi-calculator</v-icon>
    </v-btn>

    <!-- Finance AI Dialog -->
    <FinanceAiDialog v-model="showDialog" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import FinanceAiDialog from './FinanceAiDialog.vue'

const showDialog = ref(false)
</script>

<style scoped>
.finance-ai-helper {
  position: fixed;
  bottom: 24px;
  left: 24px;
  z-index: 1000;
}

.ai-helper-button {
  width: 56px !important;
  height: 56px !important;
  border-radius: 50% !important;
  min-width: auto !important;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2) !important;
}

.ai-helper-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.25) !important;
}

.ai-helper-button:active {
  transform: translateY(0) scale(0.98);
}
</style>
