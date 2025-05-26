<template>
  <v-dialog v-model="showDialog" max-width="600px" persistent>
    <v-card>
      <v-card-title class="d-flex align-center justify-space-between">
        <div class="d-flex align-center">
          <SvgIcon type="mdi" :path="calendarPlusIcon" size="20" color="primary" class="mr-2" />
          <span class="text-h6 font-weight-bold">새 일정 추가</span>
        </div>
        <v-btn icon variant="text" @click="closeDialog">
          <SvgIcon type="mdi" :path="closeIcon" size="20" />
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-6">
        <!-- Error Alert -->
        <v-alert
          v-if="error"
          type="error"
          class="mb-4"
          closable
          @click:close="clearError"
        >
          {{ error }}
        </v-alert>

        <v-form ref="form" @submit.prevent="handleSubmit">
          <!-- Selected Location Info -->
          <div class="selected-location mb-6">
            <h3 class="text-h6 font-weight-bold mb-3">선택된 장소</h3>
            <v-card variant="outlined" class="pa-0 overflow-hidden">
              <v-img
                v-if="selectedLocation?.firstImage1"
                :src="selectedLocation.firstImage1"
                height="200"
                cover
                class="location-img"
              />
              <div v-else class="location-placeholder">
                <SvgIcon type="mdi" :path="imageOffIcon" size="28" color="grey" />
              </div>

              <div class="location-meta pa-4 text-center">
                <h4 class="text-h6 font-weight-bold mb-2">
                  {{ selectedLocation?.title || '장소 미정' }}
                </h4>
                <v-chip
                  color="primary"
                  variant="outlined"
                  size="small"
                  class="mb-2 font-weight-bold"
                >
                  {{ selectedLocation?.contentTypeName || '카테고리 미정' }}
                </v-chip>
                <p v-if="selectedLocation?.addr1" class="text-body-2 text-grey d-flex align-center justify-center mb-0">
                  <SvgIcon type="mdi" :path="mapMarkerIcon" size="16" class="mr-1" />
                  {{ selectedLocation.addr1 }}
                </p>
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
          variant="flat"
          color="pink"
          style="border-radius: 9999px; background-color: #d81b60; color: white; font-weight: 600; padding: 6px 20px;"
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
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useScheduleStore } from '@/stores/schedule'
import { apiClient } from '@/utils/apiClient'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiCalendarPlus, mdiClose, mdiMapMarker, mdiImageOff } from '@mdi/js'

const calendarPlusIcon = mdiCalendarPlus
const closeIcon = mdiClose
const mapMarkerIcon = mdiMapMarker
const imageOffIcon = mdiImageOff

const props = defineProps({
  selectedLocation: {
    type: Object,
    required: true
  }
})

const route = useRoute()
const scheduleStore = useScheduleStore()

// Get groupId from route params
const groupId = route.params.groupId

// Internal dialog state (no emit needed)
const showDialog = ref(false)

// Form data
const scheduleForm = ref({
  name: '',
  description: ''
})

const isSubmitting = ref(false)
const error = ref(null)
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
const openDialog = () => {
  showDialog.value = true
  // Pre-fill schedule name with location title
  scheduleForm.value.name = props.selectedLocation?.title || ''
  clearError()
}

const closeDialog = () => {
  showDialog.value = false
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
  clearError()
}

const clearError = () => {
  error.value = null
}

const handleSubmit = async () => {
  if (!form.value) return

  const { valid } = await form.value.validate()
  if (!valid) return

  isSubmitting.value = true
  error.value = null

  try {
    // 1. First, make API call to create schedule on server
    const response = await apiClient.post(`/schedule/${groupId}/content`, {
      name: scheduleForm.value.name,
      description: scheduleForm.value.description,
      attractionNo: props.selectedLocation.no
    })

    if (!response.data.success) {
      throw new Error(response.data.message || 'Failed to create schedule')
    }

    // 2. Then, add to schedule store with the server response data
    const newScheduleData = {
      name: scheduleForm.value.name,
      description: scheduleForm.value.description,
      selectedLocation: props.selectedLocation
    }

    const newSchedule = scheduleStore.addSchedule(newScheduleData)

    // Update the local schedule with any server-provided data if needed
    if (response.data.data && response.data.data.contentId) {
      // Find the schedule we just added and update it with server data
      const addedSchedule = scheduleStore.schedules.find(s => s === newSchedule)
      if (addedSchedule) {
        addedSchedule.contentId = response.data.data.contentId
      }
    }

    console.log('Schedule created successfully:', newSchedule)
    closeDialog()

  } catch (err) {
    console.error('Error creating schedule:', err)
    error.value = err.response?.data?.message || err.message || 'Failed to create schedule'
  } finally {
    isSubmitting.value = false
  }
}

// Expose methods to parent if needed
defineExpose({
  openDialog,
  closeDialog
})

// Watch for selectedLocation changes to update form when dialog is open
watch(() => props.selectedLocation, (newLocation) => {
  if (showDialog.value && newLocation) {
    scheduleForm.value.name = newLocation.title || ''
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

.location-img {
  width: 100%;
  object-fit: cover;
}
</style>
