<template>
  <v-container class="fill-height d-flex justify-center align-center">
    <v-card class="chat-card" elevation="4">
      <v-card-title class="text-h6 font-weight-bold">AI Chat</v-card-title>

      <v-divider />

      <!-- Chat History -->
      <v-card-text class="chat-history" ref="chatHistoryRef">
        <div
          v-for="(msg, index) in messages"
          :key="index"
          class="d-flex mb-2"
          :class="msg.sender === 'user' ? 'justify-end' : 'justify-start'"
        >
          <v-chip
            :color="msg.sender === 'user' ? 'blue lighten-4' : 'grey lighten-3'"
            class="message-chip"
            label
          >
            {{ msg.text }}
          </v-chip>
        </div>
      </v-card-text>

      <v-divider />

      <!-- Input Field -->
      <v-card-actions>
        <v-text-field
          v-model="input"
          @keyup.enter="sendMessage"
          placeholder="Type a message and press Enter"
          variant="outlined"
          density="comfortable"
          class="flex-grow-1"
          clearable
        />
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import axios from 'axios'

const input = ref('')
const messages = ref([{ sender: 'bot', text: '안녕하세요! 무엇을 도와드릴까요?' }])
const chatHistoryRef = ref(null)

const sendMessage = async () => {
  const text = input.value.trim()
  if (!text) return

  messages.value.push({ sender: 'user', text })
  input.value = ''
  await nextTick()
  scrollToBottom()

  try {
    const response = await axios.post(
      'http://localhost:8080/ai/advised',
      { message: text },
      { headers: { 'Content-Type': 'application/json' } },
    )

    const reply = response?.data?.data?.message || '[No response]'
    messages.value.push({ sender: 'bot', text: reply })
  } catch (err) {
    console.error(err)
    messages.value.push({ sender: 'bot', text: '[오류 발생: 서버 응답 실패]' })
  } finally {
    await nextTick()
    scrollToBottom()
  }
}

const scrollToBottom = () => {
  const el = chatHistoryRef.value
  if (el) el.scrollTop = el.scrollHeight
}
</script>

<style scoped>
.chat-card {
  width: 100%;
  max-width: 600px;
  height: 80vh;
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  overflow: hidden;
}

.chat-history {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background-color: #fafafa;
}

.message-chip {
  max-width: 70%;
  white-space: pre-wrap;
  word-break: break-word;
}
</style>
