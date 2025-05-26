<template>
  <v-dialog v-model="isOpen" max-width="600" persistent>
    <v-card class="finance-dialog-card">
      <!-- Header -->
      <v-card-title class="finance-dialog-header">
        <v-icon color="warning" class="mr-2">mdi-calculator</v-icon>
        재무 AI 도우미
        <v-spacer></v-spacer>
        <v-btn icon variant="text" @click="closeDialog">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <!-- Content -->
      <v-card-text class="dialog-content">
        <!-- AI Greeting -->
        <div class="greeting-section">
          <v-avatar size="48" color="warning" class="mb-3">
            <v-icon color="white" size="24">mdi-calculator</v-icon>
          </v-avatar>
          <h3 class="text-h6 mb-2">재무 AI 도우미</h3>
          <p class="text-body-1 mb-4">재무 관련 질문을 입력하시면 분석해드립니다.</p>
        </div>

        <!-- Examples -->
        <v-alert type="info" variant="tonal" density="comfortable" class="mb-4">
          <div class="examples-text">
            <strong>예시:</strong><br />
            • "이번 달 지출 요약해줘"<br />
            • "예산 대비 지출 분석해줘"<br />
            • "절약할 수 있는 방법 알려줘"
          </div>
        </v-alert>

        <!-- Input -->
        <v-text-field
          v-model="userInput"
          label="재무 관련 질문을 입력하세요"
          variant="outlined"
          density="comfortable"
          :disabled="isProcessing"
          @keyup.enter="submitMessage"
        >
          <template #append-inner>
            <v-btn
              icon
              variant="text"
              color="primary"
              :disabled="!userInput.trim() || isProcessing"
              @click="submitMessage"
            >
              <v-icon>mdi-send</v-icon>
            </v-btn>
          </template>
        </v-text-field>

        <!-- Loading -->
        <div v-if="isProcessing" class="loading-section text-center py-4">
          <v-progress-circular indeterminate color="warning" size="48"></v-progress-circular>
          <p class="text-body-1 mt-3">재무 분석 중입니다...</p>
        </div>
      </v-card-text>

      <!-- Actions -->
      <v-card-actions class="close-section">
        <v-spacer></v-spacer>
        <v-btn
          variant="outlined"
          color="grey"
          @click="closeDialog"
          :disabled="isProcessing"
        >
          닫기
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- Response Display Component -->
  <FinanceResponseDisplay
    v-model="showResponse"
    :response-text="responseText"
  />
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { apiClient } from '@/utils/apiClient'
import FinanceResponseDisplay from './FinanceResponseDisplay.vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:modelValue'])

const route = useRoute()

const userInput = ref('')
const isProcessing = ref(false)
const showResponse = ref(false)
const responseText = ref('')

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const submitMessage = async () => {
  if (!userInput.value.trim() || isProcessing.value) return

  const messageText = userInput.value.trim()
  const groupId = route.params.groupId

  isProcessing.value = true
  console.log(messageText)
  try {
    const response = await apiClient.get(`/financial/expense-tracker/${groupId}/summary`, {
      params: {
        userInput: messageText,
      },
    })

    console.log('Finance AI Response:', response.data)

    if (response.data && response.data.success) {
      let cleanResponse = response.data.data || '분석 결과를 받을 수 없었습니다.'

      // Clean up markdown code block syntax if present
      cleanResponse = cleanResponse.replace(/^```html\s*\n?/i, '').replace(/\n?\s*```$/i, '')

      responseText.value = cleanResponse

      // Close this dialog and show response
      isOpen.value = false
      setTimeout(() => {
        showResponse.value = true
      }, 300)
    }

  } catch (error) {
    console.error('Finance AI Error:', error)
    responseText.value = '재무 분석 중 오류가 발생했습니다.'

    // Still show response even on error
    isOpen.value = false
    setTimeout(() => {
      showResponse.value = true
    }, 300)
  } finally {
    isProcessing.value = false
  }
}

const closeDialog = () => {
  if (isProcessing.value) return

  isOpen.value = false

  // Reset state
  setTimeout(() => {
    userInput.value = ''
    responseText.value = ''
    isProcessing.value = false
  }, 300)
}
</script>

<style scoped>
.finance-dialog-card {
  max-height: 80vh;
  display: flex;
  flex-direction: column;
}

.finance-dialog-header {
  background-color: #fff8e1;
  font-weight: bold;
  padding: 16px 20px;
}

.dialog-content {
  padding: 24px;
  text-align: center;
}

.greeting-section {
  margin-bottom: 24px;
}

.examples-text {
  font-size: 14px;
  line-height: 1.5;
  text-align: left;
}

.loading-section {
  margin: 20px 0;
}

.close-section {
  padding: 16px 20px;
  background-color: #f8f9fa;
}
</style>
