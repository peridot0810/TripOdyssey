<template>
  <v-dialog v-model="isOpen" max-width="500" persistent>
    <v-card class="transportation-dialog-card">
      <!-- Header -->
      <v-card-title class="transportation-dialog-header">
        <v-icon color="info" class="mr-2">mdi-train</v-icon>
        교통편 티켓 인식
        <v-spacer></v-spacer>
        <v-btn icon variant="text" @click="closeDialog">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <!-- Content Area -->
      <v-card-text class="dialog-content">
        <!-- Instructions -->
        <div class="instructions-section">
          <v-alert type="info" variant="tonal" density="comfortable" class="mb-4">
            <div class="instruction-text">
              <v-icon start>mdi-camera</v-icon>
              <strong>티켓 이미지를 업로드하세요</strong><br />
              기차표, 버스표, 항공권 등의 이미지를 업로드하면<br />
              자동으로 교통편 정보를 추출해드립니다.
            </div>
          </v-alert>
        </div>

        <!-- Image Upload Section -->
        <div class="upload-section">
          <v-file-input
            v-model="selectedImage"
            label="티켓 이미지 선택"
            accept="image/*"
            variant="outlined"
            prepend-icon="mdi-camera"
            show-size
            :disabled="isProcessing"
            @update:model-value="handleImageSelect"
            class="mb-4"
          />

          <!-- Image Preview -->
          <div v-if="imagePreview" class="image-preview-container mb-4">
            <v-card variant="outlined" class="image-preview-card">
              <v-img :src="imagePreview" max-height="200" class="image-preview" />
              <v-card-actions class="preview-actions">
                <v-btn
                  variant="text"
                  color="error"
                  size="small"
                  @click="clearImage"
                  :disabled="isProcessing"
                >
                  <v-icon start>mdi-delete</v-icon>
                  삭제
                </v-btn>
              </v-card-actions>
            </v-card>
          </div>
        </div>

        <!-- Processing State -->
        <div v-if="isProcessing" class="processing-section text-center py-4">
          <v-progress-circular indeterminate color="info" size="48"></v-progress-circular>
          <p class="text-body-1 mt-3">티켓 정보를 추출하는 중...</p>
        </div>

        <!-- Result Display -->
        <div v-if="extractedData" class="result-section">
          <!-- Error State -->
          <div v-if="extractedData.error" class="error-result">
            <v-alert type="error" variant="tonal" density="comfortable">
              <v-icon start>mdi-alert-circle</v-icon>
              {{ extractedData.message }}
            </v-alert>
          </div>

          <!-- Success State -->
          <div v-else class="success-result">
            <v-alert type="success" variant="tonal" density="comfortable" class="mb-3">
              <v-icon start>mdi-check-circle</v-icon>
              티켓 정보가 성공적으로 추출되었습니다!
            </v-alert>

            <!-- Save Status Alert -->
            <v-alert
              v-if="extractedData.saved"
              type="info"
              variant="tonal"
              density="comfortable"
              class="mb-3"
            >
              <v-icon start>mdi-content-save</v-icon>
              {{ extractedData.message }}
            </v-alert>

            <v-card variant="outlined" class="result-card">
              <v-card-text>
                <div class="result-content">
                  <p v-if="extractedData.typeId">
                    <strong>교통수단:</strong>
                    {{ getTransportationTypeName(extractedData.typeId) }}
                  </p>
                  <p v-if="extractedData.from"><strong>출발지:</strong> {{ extractedData.from }}</p>
                  <p v-if="extractedData.to"><strong>도착지:</strong> {{ extractedData.to }}</p>
                  <p v-if="extractedData.departure">
                    <strong>출발시간:</strong> {{ formatDateTime(extractedData.departure) }}
                  </p>
                  <p v-if="extractedData.arrival">
                    <strong>도착시간:</strong> {{ formatDateTime(extractedData.arrival) }}
                  </p>
                  <p v-if="extractedData.description">
                    <strong>상세정보:</strong> {{ extractedData.description }}
                  </p>
                </div>
              </v-card-text>
            </v-card>
          </div>
        </div>
      </v-card-text>

      <!-- Action Buttons -->
      <v-divider></v-divider>
      <v-card-actions class="action-section">
        <v-spacer></v-spacer>
        <v-btn variant="outlined" color="grey" @click="closeDialog" :disabled="isProcessing">
          닫기
        </v-btn>
        <v-btn
          color="info"
          variant="elevated"
          :disabled="!selectedImage || isProcessing"
          @click="processTicket"
        >
          <v-icon start>mdi-brain</v-icon>
          {{ isProcessing ? '처리 중...' : '티켓 분석' }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useTransportationStore } from '@/stores/transportation'
import { apiClient } from '@/utils/apiClient'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:modelValue', 'transportation-created'])

const route = useRoute()
const transportationStore = useTransportationStore()

const selectedImage = ref(null)
const imagePreview = ref(null)
const isProcessing = ref(false)
const extractedData = ref(null)

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const handleImageSelect = (files) => {
  console.log('🔵 handleImageSelect called with:', files)
  console.log('🔵 Type of files:', typeof files)
  console.log('🔵 Is array:', Array.isArray(files))

  let fileToProcess = null

  if (files) {
    if (Array.isArray(files) && files.length > 0) {
      fileToProcess = files[0]
      console.log('🔵 Got file from array')
    } else if (!Array.isArray(files)) {
      fileToProcess = files
      console.log('🔵 Got single file (not array)')
    }
  }

  if (fileToProcess) {
    console.log('🔵 File selected:', {
      name: fileToProcess.name,
      size: fileToProcess.size,
      type: fileToProcess.type,
      lastModified: fileToProcess.lastModified,
    })

    const reader = new FileReader()

    reader.onload = (e) => {
      console.log('🔵 FileReader loaded successfully')
      imagePreview.value = e.target.result
      console.log('🔵 Image preview set')
    }

    reader.onerror = (e) => {
      console.error('❌ FileReader error:', e)
    }

    reader.readAsDataURL(fileToProcess)
    extractedData.value = null
    console.log('🔵 Cleared previous results')
  } else {
    console.log('⚠️ No valid file found')
    console.log('selectedImage.value after assignment:', selectedImage.value)
  }
}

const clearImage = () => {
  selectedImage.value = null
  imagePreview.value = null
  extractedData.value = null
}

const processTicket = async () => {
  console.log('🔵 processTicket function called')
  console.log('🔵 selectedImage.value:', selectedImage.value)
  console.log('🔵 Type of selectedImage.value:', typeof selectedImage.value)
  console.log('🔵 Is array:', Array.isArray(selectedImage.value))

  let fileToUpload = null

  if (selectedImage.value) {
    if (Array.isArray(selectedImage.value) && selectedImage.value.length > 0) {
      fileToUpload = selectedImage.value[0]
      console.log('🔵 Got file from array format')
    } else if (!Array.isArray(selectedImage.value) && selectedImage.value.name) {
      fileToUpload = selectedImage.value
      console.log('🔵 Got file from single file format')
    }
  }

  if (!fileToUpload) {
    console.log('❌ No valid file found for upload')
    console.log('selectedImage.value structure:', selectedImage.value)
    return
  }

  const groupId = route.params.groupId
  console.log('🔵 Group ID from route:', groupId)

  if (!groupId) {
    console.error('❌ Group ID not found in route params')
    console.log('Current route:', route)
    return
  }

  console.log('🔵 Starting API request process...')
  isProcessing.value = true

  try {
    const formData = new FormData()
    formData.append('file', fileToUpload)

    console.log('🔵 File details for upload:', {
      name: fileToUpload.name,
      size: fileToUpload.size,
      type: fileToUpload.type,
      lastModified: fileToUpload.lastModified,
    })

    console.log('🔵 FormData created successfully')

    const endpoint = `/transportation/${groupId}/generate`
    console.log('🔵 API endpoint:', endpoint)
    console.log('🔵 Making API call...')

    const response = await apiClient.post(endpoint, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    console.log('✅ API call successful!')
    console.log('✅ Response status:', response.status)
    console.log('✅ Full response object:', response)
    console.log('✅ Response data:', response.data)

    if (response.data && response.data.success) {
      console.log('✅ Server returned success=true')
      extractedData.value = response.data.data
      console.log('✅ Extracted transportation data:', extractedData.value)

      await addTransportationToStore(response.data.data)
    } else {
      console.log('⚠️ Server returned success=false or no success field')
      console.log('Response.data.success:', response.data?.success)
      console.log('Response.data.message:', response.data?.message)
      throw new Error(response.data?.message || 'AI 처리 실패')
    }
  } catch (error) {
    console.error('❌ API call failed!')
    console.error('❌ Error object:', error)
    console.error('❌ Error message:', error.message)
    console.error('❌ Error response:', error.response)
    console.error('❌ Error response data:', error.response?.data)
    console.error('❌ Error response status:', error.response?.status)

    extractedData.value = {
      error: true,
      message:
        error.response?.data?.message || error.message || '티켓 처리 중 오류가 발생했습니다.',
    }

    console.log('❌ Set error state:', extractedData.value)
  } finally {
    console.log('🔵 API request process completed')
    isProcessing.value = false
  }
}

const addTransportationToStore = async (aiData) => {
  console.log('🔵 Adding transportation to store and server:', aiData)

  const groupId = route.params.groupId

  try {
    const requestData = {
      typeId: aiData.typeId,
      groupId: parseInt(groupId),
      departure: aiData.departure,
      arrival: aiData.arrival,
      from: aiData.from,
      to: aiData.to,
      description: aiData.description || '',
    }

    console.log('🔵 Sending transportation data to server:', requestData)

    const response = await apiClient.post(`/transportation/${groupId}`, requestData)

    console.log('✅ Transportation saved to server:', response.data)

    if (response.data.success) {
      const newTransportation = {
        transportationId: response.data.data.transportationId.toString(),
        typeId: aiData.typeId.toString(),
        groupId: parseInt(groupId),
        departure: aiData.departure,
        arrival: aiData.arrival,
        from: aiData.from,
        to: aiData.to,
        description: aiData.description || '',
      }

      console.log('✅ Adding to transportation store:', newTransportation)

      transportationStore.transportationList.push(newTransportation)
      emit('transportation-created', newTransportation)

      extractedData.value = {
        ...aiData,
        saved: true,
        message: '교통편이 성공적으로 추가되었습니다!',
      }

      console.log('✅ Transportation successfully added to store and server!')
    } else {
      throw new Error(response.data.message || '교통편 저장에 실패했습니다.')
    }
  } catch (error) {
    console.error('❌ Failed to save transportation:', error)

    extractedData.value = {
      ...aiData,
      error: true,
      message: `교통편 저장 실패: ${error.response?.data?.message || error.message}`,
    }
  }
}

const closeDialog = () => {
  if (isProcessing.value) return

  isOpen.value = false

  setTimeout(() => {
    clearImage()
    isProcessing.value = false
  }, 300)
}

const getTransportationTypeName = (typeId) => {
  const transportationTypes = {
    1: '항공편',
    2: '기차',
    3: '버스',
    4: '지하철',
    5: '택시',
    6: '렌터카',
    7: '도보',
    8: '기타',
  }
  return transportationTypes[typeId] || '알 수 없음'
}

const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return ''

  try {
    const date = new Date(dateTimeString)
    return date.toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    })
  } catch (error) {
    console.log(error)
    return dateTimeString
  }
}
</script>

<style scoped>
.transportation-dialog-card {
  max-height: 80vh;
  display: flex;
  flex-direction: column;
}

.transportation-dialog-header {
  background-color: #f0f8ff;
  font-weight: bold;
  padding: 16px 20px;
}

.dialog-content {
  padding: 20px;
  flex: 1;
  overflow-y: auto;
}

.instructions-section {
  margin-bottom: 20px;
}

.instruction-text {
  line-height: 1.5;
}

.upload-section {
  margin-bottom: 20px;
}

.image-preview-container {
  display: flex;
  justify-content: center;
}

.image-preview-card {
  max-width: 300px;
  width: 100%;
}

.image-preview {
  border-radius: 8px;
}

.preview-actions {
  padding: 8px 12px;
  justify-content: flex-end;
}

.processing-section {
  margin: 20px 0;
}

.result-section {
  margin-top: 20px;
}

.result-card {
  border-radius: 8px;
}

.result-content p {
  margin-bottom: 8px;
  line-height: 1.4;
}

.result-content p:last-child {
  margin-bottom: 0;
}

.action-section {
  padding: 16px 20px;
  background-color: #f8f9fa;
  gap: 12px;
}

.dialog-content::-webkit-scrollbar {
  width: 6px;
}

.dialog-content::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 3px;
}

.dialog-content::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}
</style>
