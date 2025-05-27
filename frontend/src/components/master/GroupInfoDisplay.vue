<template>
  <div class="group-info-display">
    <div v-if="groupStore.hasGroup" class="content">
      <!-- Left Side - Image and Upload -->
      <div class="image-section">
        <div class="image-container">
          <img v-if="groupImage" :src="groupImage" alt="Group Image" class="group-image" />
          <div v-else class="image-placeholder">
            <div class="placeholder-icon">📷</div>
            <div class="placeholder-text">그룹 이미지</div>
          </div>

          <!-- Loading overlay -->
          <div v-if="isUploading" class="upload-overlay">
            <div class="upload-spinner">⟳</div>
            <div class="upload-text">업로드 중...</div>
          </div>
        </div>

        <!-- Upload Button -->
        <button class="upload-btn" :disabled="isUploading" @click="triggerFileInput">
          {{ groupImage ? '이미지 변경' : '이미지 업로드' }}
        </button>

        <!-- Hidden File Input -->
        <input
          ref="fileInput"
          type="file"
          accept="image/*"
          style="display: none"
          @change="handleFileSelect"
        />
      </div>

      <!-- Right Side - Group Info -->
      <div class="info-section">
        <!-- Group Name -->
        <div class="info-item">
          <h3 class="group-name">{{ groupStore.myGroup.name || '그룹명 없음' }}</h3>
        </div>

        <!-- Description -->
        <div class="info-item">
          <div class="info-value">
            {{ groupStore.myGroup.description || '설명 없음' }}
          </div>
        </div>

        <!-- Date Range -->
        <div class="info-item">
          <label>여행 기간</label>
          <div class="info-value date-range">
            {{ formatDate(groupStore.myGroup.startDate) }} ~
            {{ formatDate(groupStore.myGroup.endDate) }}
          </div>
        </div>

        <!-- Role Limits -->
        <div class="info-item horizontal-align">
          <label>역할 제한</label>
          <div class="role-chips">
            <div
              v-for="(limit, roleKey) in groupStore.myGroup.roleLimits"
              :key="roleKey"
              class="role-chip"
            >
              {{ getRoleText(roleKey) }} {{ limit }}명
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- No Group Data -->
    <div v-else class="no-data">
      <p>그룹 정보가 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useGroupStore } from '@/stores/group'
import { apiClient } from '@/utils/apiClient'

const groupStore = useGroupStore()
const route = useRoute()

// State
const fileInput = ref(null)
const isUploading = ref(false)
const groupImage = ref(groupStore.getGroupImageUrl) // Initialize from store

// Computed
const groupId = computed(() => route.params.groupId)

// Watch for changes in store image URL
watch(
  () => groupStore.getGroupImageUrl,
  (newImageUrl) => {
    groupImage.value = newImageUrl
  },
  { immediate: true },
)

// Methods
const formatDate = (dateString) => {
  return dateString ? new Date(dateString).toLocaleDateString('ko-KR') : '날짜 없음'
}

const getRoleText = (roleKey) => {
  const map = {
    finance: '재정',
    schedule: '일정',
    logistics: '물류',
  }
  return map[roleKey] || roleKey
}

const triggerFileInput = () => {
  if (fileInput.value) {
    fileInput.value.click()
  }
}

const handleFileSelect = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  // Validate file type
  if (!file.type.startsWith('image/')) {
    alert('이미지 파일만 업로드 가능합니다.')
    return
  }

  // Validate file size (5MB limit)
  if (file.size > 5 * 1024 * 1024) {
    alert('파일 크기는 5MB 이하여야 합니다.')
    return
  }

  await uploadImage(file)
}

const uploadImage = async (file) => {
  if (!groupId.value) {
    alert('그룹 ID를 찾을 수 없습니다.')
    return
  }

  isUploading.value = true

  try {
    // Create FormData for multipart upload
    const formData = new FormData()
    formData.append('file', file)

    const response = await apiClient.post(`/group/${groupId.value}/upload-group-image`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    console.log('Image upload response:', response.data)

    // If upload successful, update the store
    if (response.data.success) {
      // Update the store with the new image URL
      const updatedGroupData = {
        ...groupStore.myGroup,
        imageUrl: response.data.data,
      }
      groupStore.setGroupData(updatedGroupData)

      console.log('Image uploaded successfully:', response.data.data)
    } else {
      throw new Error(response.data.message || '업로드에 실패했습니다.')
    }
  } catch (error) {
    console.error('Image upload error:', error)
    const errorMessage =
      error.response?.data?.message || error.message || '이미지 업로드에 실패했습니다.'
    alert(errorMessage)
  } finally {
    isUploading.value = false
    // Clear the file input
    if (fileInput.value) {
      fileInput.value.value = ''
    }
  }
}
</script>

<style scoped>
.group-info-display {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content {
  height: 100%;
  display: flex;
  gap: 1.5rem;
  overflow-y: auto;
}

.no-data {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
}

/* Left Side - Image Section */
.image-section {
  flex: 0 0 160px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.image-container {
  position: relative;
  width: 370px; /* was 160px */
  height: 250px; /* was 160px */
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid #e0e0e0;
}

.group-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
  border: 2px dashed #dee2e6;
  color: #6c757d;
}

.placeholder-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.placeholder-text {
  font-size: 0.8rem;
  text-align: center;
}

.upload-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
}

.upload-spinner {
  font-size: 1.5rem;
  animation: spin 1s linear infinite;
  margin-bottom: 0.5rem;
}

.upload-text {
  font-size: 0.9rem;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.upload-btn {
  width: 100%;
  padding: 0.75rem;
  background: #3ef3d5;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s ease;
  display: inline-block;
  width: 50%;
  margin: auto;
}

.upload-btn:hover:not(:disabled) {
  background: #0056b3;
}

.upload-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

/* Right Side - Info Section */
.info-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.group-name {
  font-size: 1.2rem;
  font-weight: 600;
  margin: 0;
  color: #333;
}

.info-item label {
  font-size: 0.8rem;
  font-weight: 600;
  color: #666;
  text-transform: uppercase;
}

.info-value {
  font-size: 0.95rem;
  color: #333;
  line-height: 1.4;
}

.date-range {
  color: #495057;
}

.role-chips {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.role-chip {
  background: #e9ecef;
  color: #495057;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
}

/* Responsive */
@media (max-width: 768px) {
  .content {
    flex-direction: column;
    gap: 1rem;
  }

  .image-section {
    flex: 0 0 220px; /* was 160px */
    display: flex;
    flex-direction: column;
    gap: 1rem;
    align-items: center;
  }

  .image-container {
    width: 120px;
    height: 120px;
  }

  .upload-btn {
    max-width: 160px;
  }
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.info-item {
  background: var(--card-bg, #ffffff);
  border-left: 6px solid var(--joy-teal, #4ecdc4);
  border-radius: 16px;
  padding: 1rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);
  transition: transform 0.2s ease;
}

.info-item:hover {
  transform: translateY(-2px);
}

.group-name {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--text-primary, #2d3748);
  font-family: 'Nunito', sans-serif;
}

.info-item label {
  font-size: 0.75rem;
  font-weight: 700;
  color: var(--text-muted, #718096);
  text-transform: uppercase;
  margin-bottom: 0.25rem;
  letter-spacing: 0.5px;
}

.info-value {
  font-size: 1rem;
  font-weight: 500;
  color: var(--text-primary, #2d3748);
  font-family: 'Nunito', sans-serif;
  line-height: 1.5;
}

.date-range {
  color: var(--text-secondary, #495057);
}

.role-chips {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.role-chip {
  background: var(--joy-purple, #6c5ce7);
  color: white;
  padding: 0.35rem 0.75rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
  box-shadow: 0 2px 6px rgba(102, 126, 234, 0.3);
  font-family: 'Nunito', sans-serif;
  transition: transform 0.2s ease;
}

.role-chip:hover {
  transform: scale(1.05);
}

.info-item.horizontal-align {
  flex-direction: row;
  align-items: center;
  gap: 1rem;
}

.info-item.horizontal-align label {
  min-width: 80px;
  margin-bottom: 0; /* remove vertical gap */
}
</style>
