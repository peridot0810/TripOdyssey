<template>
  <div class="transportation-ai-helper">
    <div class="ai-helper-button" @click="showDialog = true">
      <img src="/img/logistics_btn.png" alt="Transportation AI Helper" class="ai-helper-image" />
    </div>

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
  position: relative;
}

.ai-helper-button {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 3px solid #153e5a; /* Pink border */
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
</style>
