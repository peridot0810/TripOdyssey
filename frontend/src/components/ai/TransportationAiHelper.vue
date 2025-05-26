<template>
  <div class="transportation-ai-helper">
    <v-btn
      icon
      size="large"
      color="info"
      variant="elevated"
      class="ai-helper-button"
      @click="showDialog = true"
    >
      <v-icon size="large">mdi-train</v-icon>
    </v-btn>

    <!-- Transportation AI Dialog -->
    <TransportationAiDialog v-model="showDialog" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import TransportationAiDialog from './TransportationAiDialog.vue'

const showDialog = ref(false)
</script>

<style scoped>
.transportation-ai-helper {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 16px;
}

.ai-helper-button {
  width: 56px !important;
  height: 56px !important;
  border-radius: 50% !important;
  min-width: auto !important;
  transition: transform 0.2s, box-shadow 0.2s;
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
