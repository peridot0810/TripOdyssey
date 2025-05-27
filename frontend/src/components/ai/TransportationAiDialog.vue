<template>
  <v-dialog v-model="isOpen" max-width="500" persistent>
    <v-card class="transportation-dialog-card">
      <!-- Header -->
      <v-card-title class="transportation-dialog-header">
        <div class="header-content">
          <v-icon color="white" class="header-icon">mdi-train</v-icon>
          <h3 class="header-title">교통편 티켓 인식</h3>
        </div>
        <v-btn icon variant="text" @click="closeDialog" class="close-button">
          <v-icon color="white">mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <!-- Content Area -->
      <v-card-text class="dialog-content">
        <!-- Image Upload Section -->
        <div class="upload-section">
          <v-file-input
            v-model="selectedImage"
            label="티켓 이미지 선택하면 자동으로 분석해줄게!"
            accept="image/*"
            variant="outlined"
            show-size
            :disabled="isProcessing"
            @update:model-value="handleImageSelect"
            class="upload-input"
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
        <div v-if="isProcessing" class="processing-section">
          <v-progress-circular indeterminate color="info" size="48"></v-progress-circular>
          <p class="processing-text">티켓 정보를 추출하는 중...</p>
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
      imagePreview.value = e.target.result
    }

    reader.onerror = (e) => {
      console.error('❌ FileReader error:', e)
    }

    reader.readAsDataURL(fileToProcess)
    extractedData.value = null
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

    const endpoint = `/transportation/${groupId}/generate`

    const response = await apiClient.post(endpoint, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    console.log('✅ Response data:', response.data)

    if (response.data && response.data.success) {
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

/* Header Styling - Perfect Centering */
.transportation-dialog-header {
  background-color: #378edb;
  font-weight: bold;
  padding: 16px 20px;
  color: white;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.header-icon {
  font-size: 20px;
}

.header-title {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
}

.close-button {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
}

/* Content Area - Perfect Centering */
.dialog-content {
  padding: 14px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Instructions Section - Perfect Centering */
.instructions-section {
  width: 100%;
  margin-bottom: 24px;
  display: flex;
  justify-content: center;
}

.instruction-text {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 16px;
  gap: 8px;
}

.instruction-icon {
  margin-bottom: 8px;
  color: #1976d2;
}

.instruction-line {
  margin: 0;
  line-height: 1.5;
  color: #1976d2;
}

/* Upload Section - Perfect Centering */
.upload-section {
  width: 100%;
  max-width: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.hint {
  color: rgb(43, 97, 177);
  font-size: 1rem;
}

.upload-input {
  width: 100%;
  margin-bottom: 16px;
}

/* Image Preview - Perfect Centering */
.image-preview-container {
  display: flex;
  justify-content: center;
  width: 100%;
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

/* Processing Section - Perfect Centering */
.processing-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 32px 0;
  text-align: center;
}

.processing-text {
  margin-top: 16px;
  margin-bottom: 0;
  color: #666;
}

/* Result Section */
.result-section {
  width: 100%;
  max-width: 400px;
  margin-top: 20px;
}

.result-card {
  border-radius: 8px;
}

.result-content p {
  margin-bottom: 8px;
  line-height: 1.4;
  text-align: left;
}

.result-content p:last-child {
  margin-bottom: 0;
}

/* Action Section */
.action-section {
  padding: 16px 20px;
  background-color: #f8f9fa;
  gap: 12px;
}

/* Scrollbar Styling */
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
