<template>
  <v-dialog v-model="isOpen" max-width="900" scrollable>
    <v-card>
      <!-- Header -->
      <v-card-title class="response-header">
        <v-icon color="warning" class="mr-2">mdi-chart-line</v-icon>
        재무 분석 결과
        <v-spacer></v-spacer>
        <v-btn icon variant="text" @click="closeDialog">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <!-- Response Content -->
      <v-card-text class="response-content">
        <div v-html="responseText" class="html-content"></div>
      </v-card-text>

      <!-- Actions -->
      <v-divider></v-divider>
      <v-card-actions class="action-section">
        <v-spacer></v-spacer>
        <v-btn color="primary" variant="elevated" @click="closeDialog">
          <v-icon start>mdi-check</v-icon>
          확인
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  responseText: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['update:modelValue'])

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const closeDialog = () => {
  isOpen.value = false
}
</script>

<style scoped>
.response-header {
  background-color: #fff8e1;
  font-weight: bold;
  padding: 16px 20px;
}

.response-content {
  padding: 24px;
  min-height: 300px;
  max-height: 70vh;
  overflow-y: auto;
}

.html-content {
  font-family: 'Roboto', sans-serif;
  line-height: 1.6;
  color: #333;
}

/* Global HTML content styling */
.html-content :deep(h1) {
  font-size: 1.8em;
  font-weight: bold;
  margin: 20px 0 12px 0;
  color: #ef6c00;
  border-bottom: 3px solid #ef6c00;
  padding-bottom: 8px;
}

.html-content :deep(h2) {
  font-size: 1.5em;
  font-weight: bold;
  margin: 18px 0 10px 0;
  color: #ef6c00;
  border-bottom: 2px solid #ffcc02;
  padding-bottom: 6px;
}

.html-content :deep(h3) {
  font-size: 1.3em;
  font-weight: bold;
  margin: 16px 0 8px 0;
  color: #ef6c00;
}

.html-content :deep(p) {
  margin: 12px 0;
  line-height: 1.6;
}

.html-content :deep(div) {
  margin: 8px 0;
}

/* Table styling */
.html-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 16px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: hidden;
}

.html-content :deep(th) {
  background-color: #fff3e0;
  color: #ef6c00;
  font-weight: bold;
  padding: 14px 12px;
  border: 1px solid #ffcc02;
  text-align: left;
}

.html-content :deep(td) {
  padding: 12px;
  border: 1px solid #e0e0e0;
  background-color: #ffffff;
}

.html-content :deep(tr:nth-child(even) td) {
  background-color: #fafafa;
}

.html-content :deep(tr:hover td) {
  background-color: #fff8f0;
}

/* List styling */
.html-content :deep(ul) {
  margin: 12px 0;
  padding-left: 24px;
}

.html-content :deep(ol) {
  margin: 12px 0;
  padding-left: 24px;
}

.html-content :deep(li) {
  margin: 6px 0;
  line-height: 1.5;
}

/* Text styling */
.html-content :deep(strong) {
  font-weight: bold;
  color: #ef6c00;
}

.html-content :deep(em) {
  font-style: italic;
  color: #666;
}

/* Background boxes */
.html-content :deep(div[style*='background-color']) {
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Emojis in headings */
.html-content :deep(h1):first-child,
.html-content :deep(h2):first-child,
.html-content :deep(h3):first-child {
  margin-top: 0;
}

.action-section {
  padding: 16px 20px;
  background-color: #f8f9fa;
}

/* Custom scrollbar */
.response-content::-webkit-scrollbar {
  width: 8px;
}

.response-content::-webkit-scrollbar-thumb {
  background-color: #ffcc02;
  border-radius: 4px;
}

.response-content::-webkit-scrollbar-track {
  background-color: #fff8e1;
}
</style>
