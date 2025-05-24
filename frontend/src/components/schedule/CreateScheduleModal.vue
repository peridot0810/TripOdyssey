<template>
  <v-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    max-width="600px"
    persistent
  >
    <v-card>
      <v-card-title class="text-h5 pa-6 bg-primary text-white">
        <v-icon class="mr-3">mdi-calendar-plus</v-icon>
        새 일정 만들기
      </v-card-title>

      <v-card-text class="pa-6">
        <!-- Selected Proposal Info -->
        <div v-if="proposal" class="selected-proposal mb-6">
          <h3 class="text-h6 mb-3">선택된 장소</h3>
          <v-card variant="outlined" class="pa-4">
            <div class="d-flex align-center">
              <v-img
                v-if="proposal.firstImage1"
                :src="proposal.firstImage1"
                width="80"
                height="60"
                cover
                class="rounded mr-4"
              />
              <div v-else class="no-image-placeholder mr-4">
                <v-icon color="grey">mdi-image-off</v-icon>
              </div>
              <div>
                <h4 class="text-subtitle-1 font-weight-bold">{{ proposal.title }}</h4>
                <p class="text-caption text-grey">{{ proposal.contentTypeName }}</p>
                <p class="text-caption text-grey">{{ proposal.addr1 }}</p>
              </div>
            </div>
          </v-card>
        </div>

        <v-form ref="form" @submit.prevent="handleSubmit">
          <!-- Schedule Name -->
          <v-text-field
            v-model="scheduleForm.name"
            label="일정 이름 *"
            variant="outlined"
            :rules="[rules.required]"
            class="mb-4"
            placeholder="예: 강남 관광, 점심식사 등"
          />

          <!-- Schedule Description -->
          <v-textarea
            v-model="scheduleForm.description"
            label="일정 설명"
            variant="outlined"
            rows="3"
            class="mb-4"
            placeholder="이 일정에 대한 간단한 설명을 입력하세요"
          />

          <!-- Day Selection -->
          <v-select
            v-model="scheduleForm.day"
            label="일차 선택 *"
            variant="outlined"
            :items="dayOptions"
            :rules="[rules.required]"
            class="mb-4"
          />

          <!-- Time -->
          <div class="d-flex gap-4 mb-4">
            <v-text-field
              v-model="scheduleForm.startTime"
              label="시작 시간"
              variant="outlined"
              type="time"
              class="flex-1"
            />
            <v-text-field
              v-model="scheduleForm.endTime"
              label="종료 시간"
              variant="outlined"
              type="time"
              class="flex-1"
            />
          </div>

          <!-- Notes -->
          <v-textarea
            v-model="scheduleForm.notes"
            label="특이사항"
            variant="outlined"
            rows="2"
            placeholder="주의사항, 준비물 등 추가 정보"
          />
        </v-form>
      </v-card-text>

      <v-card-actions class="pa-6 pt-0">
        <v-spacer />
        <v-btn
          variant="text"
          @click="handleCancel"
          :disabled="isLoading"
        >
          취소
        </v-btn>
        <v-btn
          color="primary"
          variant="elevated"
          @click="handleSubmit"
          :loading="isLoading"
        >
          일정 추가
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  proposal: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'schedule-created'])

// Form refs and state
const form = ref(null)
const isLoading = ref(false)

// Schedule form data
const scheduleForm = reactive({
  name: '',
  description: '',
  day: 1,
  startTime: '',
  endTime: '',
  notes: ''
})

// Day options (you might want to get this from trip duration)
const dayOptions = ref([
  { title: '1일차', value: 1 },
  { title: '2일차', value: 2 },
  { title: '3일차', value: 3 },
  { title: '4일차', value: 4 },
  { title: '5일차', value: 5 }
])

// Validation rules
const rules = {
  required: (value) => !!value || '필수 입력 항목입니다.'
}

// Watch for proposal changes to auto-fill name
watch(() => props.proposal, (newProposal) => {
  if (newProposal && props.modelValue) {
    scheduleForm.name = newProposal.title
    scheduleForm.description = `${newProposal.title} 방문`
  }
})

// Reset form when modal closes
watch(() => props.modelValue, (isOpen) => {
  if (!isOpen) {
    resetForm()
  }
})

const resetForm = () => {
  Object.assign(scheduleForm, {
    name: '',
    description: '',
    day: 1,
    startTime: '',
    endTime: '',
    notes: ''
  })

  if (form.value) {
    form.value.resetValidation()
  }
}

const handleCancel = () => {
  emit('update:modelValue', false)
}

const handleSubmit = async () => {
  if (!form.value) return

  const { valid } = await form.value.validate()
  if (!valid) return

  isLoading.value = true

  try {
    // Create schedule data object
    const scheduleData = {
      ...scheduleForm,
      proposalId: props.proposal?.proposalId,
      attractionInfo: props.proposal,
      contentId: Date.now(), // Temporary ID
      order: 1 // Will be set properly when added to official schedule
    }

    // Emit the created schedule
    emit('schedule-created', scheduleData)

  } catch (error) {
    console.error('Error creating schedule:', error)
    // You might want to show an error message here
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.selected-proposal {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
}

.no-image-placeholder {
  width: 80px;
  height: 60px;
  background-color: #f5f5f5;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.v-dialog .v-card {
  border-radius: 12px;
}

.v-card-title {
  border-radius: 12px 12px 0 0;
}
</style>
