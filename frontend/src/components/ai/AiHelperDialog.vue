<template>
  <v-dialog v-model="isOpen" max-width="600" persistent class="container">
    <v-card class="ai-dialog-card">
      <!-- Header -->
      <v-card-title class="ai-dialog-header">
        AI 일정 도우미
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
          <p class="text-body-1 mb-4">일정 관련 질문을 입력하시면 도움을 드립니다.</p>
        </div>

        <!-- Examples -->
        <v-alert type="info" variant="tonal" density="comfortable" class="mb-4">
          <div class="examples-text">
            <strong>예시:</strong><br />
            • "3일차에 맛집 추천해줘"<br />
            • "부산 관광지 일정 짜줘"<br />
            • "교통편 정보 알려줘"<br />
            • "일정 최적화해줘"
          </div>
        </v-alert>

        <!-- Input -->
        <v-text-field
          v-model="userInput"
          label="일정 관련 질문을 입력하세요 (Enter로 전송)"
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
          <v-progress-circular indeterminate color="success" size="48"></v-progress-circular>
          <p class="text-body-1 mt-3">일정 최적화 중입니다...</p>
        </div>
      </v-card-text>

      <!-- Actions -->
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
import { useScheduleStore } from '@/stores/schedule'
import { apiClient } from '@/utils/apiClient'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:modelValue'])
const scheduleStore = useScheduleStore()

const userInput = ref('')
const isProcessing = ref(false)
const showResponse = ref(false)
const responseText = ref('')

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
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
  if (!userInput.value.trim() || isProcessing.value) return

  const messageText = userInput.value.trim()

  isProcessing.value = true
  console.log(messageText)

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

    if (response.data.status === 'SUCCESS') {
      const aiSummary = response.data.data.summary || '일정 최적화가 완료되었습니다!'

      // Apply optimized schedules to the store
      if (
        response.data.data.optimizedSchedules &&
        response.data.data.optimizedSchedules.length > 0
      ) {
        applyOptimizedSchedules(response.data.data.optimizedSchedules)

        responseText.value = `${aiSummary}\n\n✅ ${response.data.data.optimizedSchedules.length}개의 일정이 확정 일정으로 적용되었습니다!`
      } else {
        responseText.value = aiSummary
      }

      // Close this dialog and show response
      isOpen.value = false
      setTimeout(() => {
        showResponse.value = true
      }, 300)
    } else {
      throw new Error('AI 처리 실패')
    }
  } catch (error) {
    console.error('AI Helper API error:', error)
    responseText.value = '죄송합니다. 처리 중 오류가 발생했습니다. 다시 시도해 주세요.'

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
.ai-dialog-card {
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  border-radius: 12px;
}

.ai-dialog-header {
  background-color: #f3e5f5;
  font-weight: bold;
  padding: 16px 20px;
  display: flex;
  align-items: center;
}

.header-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.dialog-content {
  padding: 24px;
  text-align: center;
}

.greeting-section {
  margin-bottom: 24px;
}

.avatar-container {
  display: flex;
  justify-content: center;
}

.ai-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #5a152c;
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

.container {
  border-radius: 50px; /* or whatever value you prefer */
}
</style>
