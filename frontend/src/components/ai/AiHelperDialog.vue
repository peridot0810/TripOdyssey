<template>
  <v-dialog v-model="isOpen" max-width="600" persistent>
    <v-card class="ai-dialog-card">
      <!-- Header -->
      <v-card-title class="ai-dialog-header">
        <v-icon color="success" class="mr-2">mdi-robot</v-icon>
        AI 일정 도우미
        <v-spacer></v-spacer>
        <v-btn icon variant="text" @click="closeDialog">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <!-- Chat Messages Area -->
      <v-card-text class="chat-area">
        <div class="messages-container">
          <!-- AI Greeting Message -->
          <div class="message ai-message">
            <v-avatar size="32" color="success" class="message-avatar">
              <v-icon color="white" size="18">mdi-robot</v-icon>
            </v-avatar>
            <div class="message-bubble ai-bubble">
              안녕하세요! 일정 계획에 도움이 필요하시면<br />
              무엇이든 말씀해 주세요.
            </div>
          </div>

          <!-- Example Hints (only show initially) -->
          <div v-if="messages.length === 0" class="examples-container">
            <v-alert type="info" variant="tonal" density="compact" class="examples-alert">
              <div class="examples-text">
                <strong>예시:</strong><br />
                • "3일차에 맛집 추천해줘"<br />
                • "부산 관광지 일정 짜줘"<br />
                • "교통편 정보 알려줘"
              </div>
            </v-alert>
          </div>

          <!-- User and AI Messages -->
          <div
            v-for="(message, index) in messages"
            :key="index"
            class="message"
            :class="message.type + '-message'"
          >
            <v-avatar
              v-if="message.type === 'user'"
              size="32"
              color="primary"
              class="message-avatar user-avatar"
            >
              <v-icon color="white" size="18">mdi-account</v-icon>
            </v-avatar>

            <div class="message-bubble" :class="message.type + '-bubble'">
              {{ message.text }}
            </div>

            <v-avatar
              v-if="message.type === 'ai'"
              size="32"
              color="success"
              class="message-avatar ai-avatar"
            >
              <v-icon color="white" size="18">mdi-robot</v-icon>
            </v-avatar>
          </div>

          <!-- Loading indicator when AI is processing -->
          <div v-if="isProcessing" class="message ai-message">
            <v-avatar size="32" color="success" class="message-avatar">
              <v-icon color="white" size="18">mdi-robot</v-icon>
            </v-avatar>
            <div class="message-bubble ai-bubble">
              <v-progress-circular
                indeterminate
                size="16"
                width="2"
                color="success"
              ></v-progress-circular>
              처리 중입니다...
            </div>
          </div>
        </div>
      </v-card-text>

      <!-- Input Section -->
      <v-divider></v-divider>
      <v-card-actions class="input-section">
        <v-text-field
          v-model="userInput"
          :placeholder="inputPlaceholder"
          variant="outlined"
          density="comfortable"
          hide-details
          class="message-input"
          :disabled="isInputDisabled"
          @keyup.enter="submitMessage"
        >
          <template #append-inner>
            <v-btn
              icon
              variant="text"
              color="primary"
              :disabled="!userInput.trim() || isInputDisabled"
              @click="submitMessage"
            >
              <v-icon>mdi-send</v-icon>
            </v-btn>
          </template>
        </v-text-field>
      </v-card-actions>

      <!-- Close Dialog Button -->
      <v-card-actions class="close-section">
        <v-spacer></v-spacer>
        <v-btn variant="outlined" color="grey" @click="closeDialog" :disabled="isProcessing">
          닫기
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useScheduleStore } from '@/stores/schedule'
import { apiClient } from '@/utils/apiClient'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:modelValue'])

const route = useRoute()
const scheduleStore = useScheduleStore()

const userInput = ref('')
const messages = ref([])
const isProcessing = ref(false)
const hasSubmitted = ref(false)

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const isInputDisabled = computed(() => {
  return hasSubmitted.value || isProcessing.value
})

const inputPlaceholder = computed(() => {
  if (hasSubmitted.value) {
    return '작업이 완료될 때까지 기다려주세요...'
  }
  return messages.value.length === 0
    ? '어떤 도움이 필요하신가요?'
    : '추가 요청사항이 있으시면 말씀해 주세요...'
})

const applyOptimizedSchedules = (optimizedSchedules) => {
  console.log('Applying optimized schedules:', optimizedSchedules)

  // Clear current tracking to start fresh
  scheduleStore.clearTracking()

  // Process each optimized schedule
  optimizedSchedules.forEach((optimizedSchedule) => {
    // Find the corresponding schedule in the store
    const existingSchedule = scheduleStore.schedules.find(
      (schedule) => schedule.contentId === optimizedSchedule.contentId,
    )

    if (existingSchedule) {
      // Check if it was already official
      const wasOfficial = existingSchedule.isOfficial

      // Update the schedule with optimized data
      existingSchedule.isOfficial = true
      existingSchedule.day = optimizedSchedule.day
      existingSchedule.order = optimizedSchedule.order

      // Update description if provided
      if (optimizedSchedule.description) {
        existingSchedule.description = optimizedSchedule.description
      }

      // Track as new official if it wasn't official before
      if (!wasOfficial) {
        scheduleStore.newOfficialSchedules.push({
          contentId: optimizedSchedule.contentId,
          day: optimizedSchedule.day,
          order: optimizedSchedule.order,
        })
      } else {
        // Track as modified if it was already official but changed
        scheduleStore.modifiedOfficialSchedules.push({
          contentId: optimizedSchedule.contentId,
          day: optimizedSchedule.day,
          order: optimizedSchedule.order,
        })
      }
    }
  })

  console.log('Optimized schedules applied successfully!')
  console.log('Current tracking state:', scheduleStore.getTrackingState())
}

const submitMessage = async () => {
  if (!userInput.value.trim() || isInputDisabled.value) return

  const messageText = userInput.value.trim()
  const groupId = route.params.groupId

  // Add user message
  messages.value.push({
    type: 'user',
    text: messageText,
  })

  // Clear input and disable it
  userInput.value = ''
  hasSubmitted.value = true
  isProcessing.value = true

  // Add initial AI response
  messages.value.push({
    type: 'ai',
    text: `네! "${messageText}"을 처리해드릴게요. 잠시만 기다려주세요! ⏳`,
  })

  try {
    // Prepare request body
    const requestBody = {
      message: messageText,
      scheduleList: scheduleStore.schedules.map((schedule) => ({
        contentId: schedule.contentId,
        attractionsNo: schedule.attractionsNo,
        name: schedule.name,
        description: schedule.description,
        contentTypeName: schedule.attractionInfo?.contentTypeName || '',
        latitude: schedule.attractionInfo?.latitude || 0,
        longitude: schedule.attractionInfo?.longitude || 0,
        addr1: schedule.attractionInfo?.addr1 || '',
        day: schedule.day || 0,
        order: schedule.order || 0,
        overview: schedule.attractionInfo?.overview || '',
      })),
    }

    console.log('Sending AI request:', requestBody)

    // Make API call
    const response = await apiClient.post('/ai/optimize-schedule', requestBody)

    console.log('AI Response received:', response.data)

    // Add success response message
    if (response.data.status === 'SUCCESS') {
      const aiSummary = response.data.data.summary || '일정 최적화가 완료되었습니다!'

      messages.value.push({
        type: 'ai',
        text: aiSummary,
      })

      // Apply optimized schedules to the store
      if (
        response.data.data.optimizedSchedules &&
        response.data.data.optimizedSchedules.length > 0
      ) {
        applyOptimizedSchedules(response.data.data.optimizedSchedules)

        // Add additional message about applied changes
        messages.value.push({
          type: 'ai',
          text: `✅ ${response.data.data.optimizedSchedules.length}개의 일정이 확정 일정으로 적용되었습니다!`,
        })
      }
    } else {
      throw new Error('AI 처리 실패')
    }
  } catch (error) {
    console.error('AI Helper API error:', error)

    // Add error message
    messages.value.push({
      type: 'ai',
      text: '죄송합니다. 처리 중 오류가 발생했습니다. 다시 시도해 주세요.',
    })
  } finally {
    isProcessing.value = false
  }
}

const closeDialog = () => {
  if (isProcessing.value) return

  isOpen.value = false

  // Reset state when dialog closes
  setTimeout(() => {
    messages.value = []
    userInput.value = ''
    hasSubmitted.value = false
    isProcessing.value = false
  }, 300)
}
</script>

<style scoped>
.ai-dialog-card {
  max-height: 80vh;
  display: flex;
  flex-direction: column;
}

.ai-dialog-header {
  background-color: #f8f9fa;
  font-weight: bold;
  padding: 16px 20px;
}

.chat-area {
  flex: 1;
  max-height: 400px;
  overflow-y: auto;
  padding: 20px;
}

.messages-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.ai-message {
  justify-content: flex-start;
}

.user-message {
  justify-content: flex-end;
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
  margin-top: 2px;
}

.user-avatar {
  margin-left: 12px;
}

.ai-avatar {
  margin-right: 12px;
}

.message-bubble {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 16px;
  line-height: 1.4;
  word-wrap: break-word;
}

.ai-bubble {
  background-color: #e8f5e8;
  color: #2e7d32;
  border-bottom-left-radius: 4px;
}

.user-bubble {
  background-color: #e3f2fd;
  color: #1565c0;
  border-bottom-right-radius: 4px;
}

.examples-container {
  margin: 8px 0;
}

.examples-alert {
  border-radius: 12px;
}

.examples-text {
  font-size: 14px;
  line-height: 1.5;
}

.input-section {
  padding: 16px 20px;
  background-color: #fafafa;
}

.close-section {
  padding: 12px 20px;
  background-color: #f8f9fa;
}

.message-input {
  flex: 1;
}

/* Custom scrollbar for chat area */
.chat-area::-webkit-scrollbar {
  width: 6px;
}

.chat-area::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 3px;
}

.chat-area::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* Animation for new messages */
.message {
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
