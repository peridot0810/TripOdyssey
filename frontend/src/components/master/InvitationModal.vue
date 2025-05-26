<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <h3>멤버 초대</h3>

      <div class="invite-form">
        <label for="email-input" class="email-label">이메일 주소</label>
        <input
          id="email-input"
          v-model="email"
          type="email"
          placeholder="초대할 멤버의 이메일을 입력하세요"
          class="email-input"
          :disabled="isInviting"
          @keyup.enter="sendInvitation"
        />

        <div v-if="error" class="error-message">
          {{ error }}
        </div>
      </div>

      <div class="modal-actions">
        <button class="btn-cancel" @click="closeModal" :disabled="isInviting">취소</button>
        <button class="btn-invite" @click="sendInvitation" :disabled="!isValidEmail || isInviting">
          {{ isInviting ? '초대 중...' : '초대' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { apiClient } from '@/utils/apiClient'

// Emits
const emit = defineEmits(['close', 'invitation-sent'])

// Route
const route = useRoute()

// State
const email = ref('')
const isInviting = ref(false)
const error = ref('')

// Computed
const groupId = computed(() => route.params.groupId)

const isValidEmail = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return email.value && emailRegex.test(email.value)
})

// Methods
const closeModal = () => {
  if (!isInviting.value) {
    emit('close')
  }
}

const sendInvitation = async () => {
  if (!isValidEmail.value || isInviting.value) return

  if (!groupId.value) {
    error.value = '그룹 ID를 찾을 수 없습니다.'
    return
  }

  isInviting.value = true
  error.value = ''

  try {
    const response = await apiClient.post(`/group/${groupId.value}/member/invite`, {
      receiverEmail: email.value,
    })

    console.log('Invitation response:', response.data)

    if (response.data.success) {
      alert(`${email.value}님에게 초대 메일을 보냈습니다.`)
      emit('invitation-sent')
      emit('close')
    } else {
      throw new Error(response.data.message || '초대 전송에 실패했습니다.')
    }
  } catch (err) {
    console.error('Invitation error:', err)
    const errorMessage =
      err.response?.data?.message || err.message || '초대 전송 중 오류가 발생했습니다.'
    error.value = errorMessage
  } finally {
    isInviting.value = false
  }
}

// Auto-focus email input when modal opens
//const emailInput = ref(null)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700&display=swap');

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(45, 55, 72, 0.8);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal-content {
  background: linear-gradient(135deg, #ffffff 0%, #f7fafc 100%);
  padding: 2rem;
  border-radius: 24px;
  min-width: 400px;
  max-width: 500px;
  border: 3px solid #e2e8f0;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.modal-content::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 6px;
  background: linear-gradient(90deg, #4ecdc4 0%, #45b7d1 50%, #667eea 100%);
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.modal-content h3 {
  margin: 0 0 1.5rem 0;
  color: #2d3748;
  font-family: 'Nunito', sans-serif;
  font-weight: 700;
  font-size: 1.3rem;
  text-align: center;
}

.invite-form {
  margin-bottom: 2rem;
}

.email-label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #4a5568;
  font-family: 'Nunito', sans-serif;
  font-size: 0.9rem;
}

.email-input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 1rem;
  font-family: 'Nunito', sans-serif;
  transition: all 0.2s ease;
  box-sizing: border-box;
}

.email-input:focus {
  outline: none;
  border-color: #4ecdc4;
  box-shadow: 0 0 0 3px rgba(78, 205, 196, 0.1);
}

.email-input:disabled {
  background: #f7fafc;
  cursor: not-allowed;
}

.email-input::placeholder {
  color: #a0aec0;
}

.error-message {
  margin-top: 0.5rem;
  padding: 0.5rem 0.75rem;
  background: #fed7d7;
  border: 1px solid #feb2b2;
  border-radius: 6px;
  color: #c53030;
  font-size: 0.875rem;
  font-family: 'Nunito', sans-serif;
}

.modal-actions {
  display: flex;
  gap: 0.75rem;
  justify-content: center;
}

.btn-cancel,
.btn-invite {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 700;
  font-family: 'Nunito', sans-serif;
  font-size: 1rem;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  min-width: 100px;
}

.btn-cancel {
  background: linear-gradient(135deg, #a0aec0 0%, #718096 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(160, 174, 192, 0.3);
}

.btn-cancel:hover:not(:disabled) {
  box-shadow: 0 6px 16px rgba(160, 174, 192, 0.4);
}

.btn-invite {
  background: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(78, 205, 196, 0.3);
}

.btn-invite:hover:not(:disabled) {
  box-shadow: 0 6px 16px rgba(78, 205, 196, 0.4);
}

.btn-cancel:disabled,
.btn-invite:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-cancel:active:not(:disabled),
.btn-invite:active:not(:disabled) {
  transform: translateY(0) scale(0.95);
}

/* Responsive */
@media (max-width: 768px) {
  .modal-content {
    margin: 1rem;
    min-width: auto;
    max-width: none;
  }
}
</style>
