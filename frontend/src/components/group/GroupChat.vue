<template>
  <v-card class="group-chat-card" elevation="1">
    <v-card-item class="pa-0">
      <!-- Chat Header -->
      <div class="chat-header">
        <div class="chat-title">
          <v-icon size="small" class="mr-2">mdi-chat</v-icon>
          <span class="font-weight-bold">그룹 채팅</span>
        </div>
        <v-chip size="small" color="success" variant="dot"> {{ onlineCount }}명 온라인 </v-chip>
      </div>

      <!-- Chat Messages -->
      <div class="chat-content" ref="chatContent">
        <div class="chat-messages">
          <div
            v-for="message in messages"
            :key="message.id"
            class="message-wrapper"
            :class="{ 'own-message': message.isOwn }"
          >
            <div class="message-container">
              <div v-if="!message.isOwn" class="message-avatar">
                <v-avatar size="24" :color="getUserColor(message.userId)">
                  <span class="text-caption font-weight-bold text-white">
                    {{ message.nickname.charAt(0) }}
                  </span>
                </v-avatar>
              </div>

              <div class="message-content">
                <div v-if="!message.isOwn" class="message-author">
                  {{ message.nickname }}
                </div>
                <div
                  class="message-bubble"
                  :class="{ 'own-bubble': message.isOwn, 'other-bubble': !message.isOwn }"
                >
                  {{ message.content }}
                </div>
                <div class="message-time">
                  {{ formatTime(message.timestamp) }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Chat Input -->
      <div class="chat-input-section">
        <v-text-field
          v-model="newMessage"
          placeholder="메시지를 입력하세요..."
          variant="outlined"
          density="compact"
          hide-details
          @keyup.enter="sendMessage"
          class="chat-input"
        >
          <template v-slot:append-inner>
            <v-btn
              icon
              size="small"
              color="primary"
              :disabled="!newMessage.trim()"
              @click="sendMessage"
            >
              <v-icon>mdi-send</v-icon>
            </v-btn>
          </template>
        </v-text-field>
      </div>
    </v-card-item>
  </v-card>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'

// Mock data
const messages = ref([
  {
    id: 1,
    userId: 'jihun',
    nickname: '지훈',
    content: '안녕하세요! 여행 계획 어떻게 진행되고 있나요?',
    timestamp: new Date(Date.now() - 3600000),
    isOwn: false,
  },
  {
    id: 2,
    userId: 'current',
    nickname: '나',
    content: '일정표 거의 완성했어요! 내일 확인해주세요.',
    timestamp: new Date(Date.now() - 1800000),
    isOwn: true,
  },
  {
    id: 3,
    userId: 'junheok',
    nickname: '준혁',
    content: '숙소 예약도 완료했습니다 👍',
    timestamp: new Date(Date.now() - 900000),
    isOwn: false,
  },
  {
    id: 4,
    userId: 'hyeonjin',
    nickname: '현진',
    content: '예산 정리해서 공유드릴게요~',
    timestamp: new Date(Date.now() - 300000),
    isOwn: false,
  },
])

const newMessage = ref('')
const onlineCount = ref(5)
const chatContent = ref(null)

// User color mapping
const getUserColor = (userId) => {
  const colors = ['blue', 'green', 'orange', 'purple', 'pink', 'teal', 'indigo']
  const index = userId.charCodeAt(0) % colors.length
  return colors[index]
}

// Format timestamp
const formatTime = (timestamp) => {
  const now = new Date()
  const diff = now - timestamp
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '방금'
  if (minutes < 60) return `${minutes}분 전`
  if (hours < 24) return `${hours}시간 전`
  if (days < 7) return `${days}일 전`

  return timestamp.toLocaleDateString('ko-KR', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

// Send message
const sendMessage = () => {
  if (!newMessage.value.trim()) return

  messages.value.push({
    id: Date.now(),
    userId: 'current',
    nickname: '나',
    content: newMessage.value.trim(),
    timestamp: new Date(),
    isOwn: true,
  })

  newMessage.value = ''

  // Scroll to bottom
  nextTick(() => {
    if (chatContent.value) {
      chatContent.value.scrollTop = chatContent.value.scrollHeight
    }
  })
}

// Auto scroll to bottom on mount
onMounted(() => {
  nextTick(() => {
    if (chatContent.value) {
      chatContent.value.scrollTop = chatContent.value.scrollHeight
    }
  })
})
</script>

<style scoped>
.group-chat-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #e0e0e0;
  background-color: #fafafa;
  flex-shrink: 0;
}

.chat-title {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.chat-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  min-height: 0;
}

.chat-messages {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-wrapper {
  display: flex;
  justify-content: flex-start;
}

.message-wrapper.own-message {
  justify-content: flex-end;
}

.message-container {
  display: flex;
  max-width: 85%;
  gap: 8px;
}

.own-message .message-container {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
  align-self: flex-end;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.own-message .message-content {
  align-items: flex-end;
}

.message-author {
  font-size: 11px;
  color: #666;
  margin-bottom: 2px;
  font-weight: 500;
}

.message-bubble {
  padding: 8px 12px;
  border-radius: 16px;
  font-size: 13px;
  line-height: 1.4;
  word-wrap: break-word;
  max-width: 100%;
}

.other-bubble {
  background-color: #f5f5f5;
  color: #333;
  border-bottom-left-radius: 4px;
}

.own-bubble {
  background-color: #1976d2;
  color: white;
  border-bottom-right-radius: 4px;
}

.message-time {
  font-size: 10px;
  color: #999;
  margin-top: 2px;
}

.chat-input-section {
  padding: 12px 16px;
  border-top: 1px solid #e0e0e0;
  background-color: #fafafa;
  flex-shrink: 0;
}

.chat-input {
  font-size: 14px;
}

/* Custom scrollbar */
.chat-content::-webkit-scrollbar {
  width: 4px;
}

.chat-content::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 2px;
}

.chat-content::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .chat-header {
    padding: 8px 12px;
  }

  .chat-content {
    padding: 12px;
  }

  .message-container {
    max-width: 90%;
  }

  .message-bubble {
    padding: 6px 10px;
    font-size: 12px;
  }

  .chat-input-section {
    padding: 8px 12px;
  }
}

/* Animation for new messages */
.message-wrapper {
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
