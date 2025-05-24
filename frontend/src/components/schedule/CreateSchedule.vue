<template>
  <v-dialog v-model="internalDialog" max-width="600px" persistent>
    <v-card>
      <v-card-title class="d-flex align-center justify-space-between">
        <div class="d-flex align-center">
          <v-icon color="primary" class="mr-2">mdi-calendar-plus</v-icon>
          <span class="text-h6 font-weight-bold">새 일정 추가</span>
        </div>
        <v-btn icon variant="text" @click="closeDialog">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-6">
        <v-form ref="form" @submit.prevent="handleSubmit">
          <!-- Selected Location Info -->
          <div class="selected-location mb-6">
            <h3 class="text-h6 font-weight-bold mb-3">선택된 장소</h3>
            <v-card variant="outlined" class="pa-4">
              <div class="d-flex flex-column align-center text-center">
                <v-img
                  v-if="selectedLocation?.firstImage1"
                  :src="selectedLocation.firstImage1"
                  width="200"
                  height="150"
                  cover
                  class="rounded mb-3"
                ></v-img>
                <div v-else class="location-placeholder mb-3">
                  <v-icon color="grey" size="x-large">mdi-image-off</v-icon>
                </div>
                <div class="location-details">
                  <h4 class="text-h6 font-weight-bold mb-2">{{ selectedLocation?.title || '장소 미정' }}</h4>
                  <v-chip
                    color="primary"
                    variant="flat"
                    size="small"
                    class="mb-2"
                  >
                    {{ selectedLocation?.contentTypeName || '카테고리 미정' }}
                  </v-chip>
                  <p v-if="selectedLocation?.addr1" class="text-body-2 text-grey mb-0">
                    <v-icon size="small" class="mr-1">mdi-map-marker</v-icon>
                    {{ selectedLocation.addr1 }}
                  </p>
                </div>
              </div>
            </v-card>
          </div>

          <!-- Schedule Name -->
          <v-text-field
            v-model="scheduleForm.name"
            label="일정 이름"
            variant="outlined"
            density="comfortable"
            class="mb-4"
            :rules="nameRules"
            required
            hint="이 장소에서 할 활동이나 일정의 이름을 입력하세요"
            persistent-hint
          ></v-text-field>

          <!-- Schedule Description -->
          <v-textarea
            v-model="scheduleForm.description"
            label="일정 설명"
            variant="outlined"
            rows="4"
            class="mb-4"
            :rules="descriptionRules"
            hint="일정에 대한 자세한 설명을 입력하세요"
            persistent-hint
          ></v-textarea>
        </v-form>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions class="pa-4">
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="closeDialog" :disabled="isSubmitting">
          취소
        </v-btn>
        <v-btn
          color="primary"
          variant="elevated"
          @click="handleSubmit"
          :loading="isSubmitting"
        >
          일정 추가
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useScheduleStore } from '@/stores/schedule'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  selectedLocation: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['update:modelValue'])

const route = useRoute()
const scheduleStore = useScheduleStore()

// Get groupId from route params
const groupId = route.params.groupId

// Internal dialog state
const internalDialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// Form data
const scheduleForm = ref({
  name: '',
  description: ''
})

const isSubmitting = ref(false)
const form = ref(null)

// Form validation rules
const nameRules = [
  v => !!v || '일정 이름은 필수입니다',
  v => v.length <= 50 || '일정 이름은 50자 이하여야 합니다'
]

const descriptionRules = [
  v => !v || v.length <= 500 || '설명은 500자 이하여야 합니다'
]

// Methods
const closeDialog = () => {
  internalDialog.value = false
  resetForm()
}

const resetForm = () => {
  scheduleForm.value = {
    name: '',
    description: ''
  }
  if (form.value) {
    form.value.resetValidation()
  }
}

const handleSubmit = async () => {
  if (!form.value) return

  const { valid } = await form.value.validate()
  if (!valid) return

  isSubmitting.value = true

  try {
    // Create schedule data
    const newScheduleData = {
      name: scheduleForm.value.name,
      description: scheduleForm.value.description,
      selectedLocation: props.selectedLocation
    }

    // Add to schedule store directly
    const newSchedule = scheduleStore.addSchedule(newScheduleData)

    // Optionally save to server
    try {
      await scheduleStore.saveScheduleToServer(groupId, newSchedule)
      console.log('Schedule saved to server successfully')
    } catch (serverError) {
      // Even if server save fails, keep the local schedule
      console.warn('Failed to save to server, but schedule added locally:', serverError)
    }

    console.log('New schedule created and added to store:', newSchedule)
    closeDialog()
  } catch (error) {
    console.error('Error creating schedule:', error)
  } finally {
    isSubmitting.value = false
  }
}

// Reset form when dialog opens
watch(() => props.modelValue, (newValue) => {
  if (newValue) {
    // Pre-fill schedule name with location title
    scheduleForm.value.name = props.selectedLocation?.title || ''
  } else {
    resetForm()
  }
})
</script>

<style scoped>
.selected-location {
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 20px;
}

.location-placeholder {
  width: 200px;
  height: 150px;
  background-color: #f5f5f5;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.location-details {
  max-width: 300px;
}

.v-card-title {
  background-color: #f5f5f5;
}
</style>
