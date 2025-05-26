<template>
  <v-dialog v-model="dialog" max-width="600px">
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center pa-4">
        <span class="text-h6">새 게시글 작성</span>
        <v-btn icon variant="text" @click="$emit('close')">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-4">
        <v-form ref="form" @submit.prevent="submitPost">
          <!-- Category Selection -->
          <v-select
            v-model="formData.categoryId"
            :items="categoryOptions"
            item-title="label"
            item-value="id"
            label="카테고리"
            variant="outlined"
            :rules="[(v) => !!v || '카테고리를 선택해주세요']"
            class="mb-4"
          ></v-select>

          <!-- Title Input -->
          <v-text-field
            v-model="formData.title"
            label="제목"
            variant="outlined"
            :rules="[
              (v) => !!v || '제목을 입력해주세요',
              (v) => v.length <= 100 || '제목은 100자 이내로 입력해주세요',
            ]"
            counter="100"
            class="mb-4"
          ></v-text-field>

          <!-- Content Input -->
          <v-textarea
            v-model="formData.content"
            label="내용"
            variant="outlined"
            rows="8"
            :rules="[
              (v) => !!v || '내용을 입력해주세요',
              (v) => v.length <= 1000 || '내용은 1000자 이내로 입력해주세요',
            ]"
            counter="1000"
            class="mb-4"
          ></v-textarea>
        </v-form>
      </v-card-text>

      <v-card-actions class="px-4 pb-4">
        <v-spacer></v-spacer>
        <v-btn color="grey" variant="text" @click="$emit('close')"> 취소 </v-btn>
        <v-btn color="primary" @click="submitPost" :loading="loading"> 작성하기 </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import {useUserStore} from '@/stores/user'
import { categories } from '@/data/communityData.js'
import { apiClient } from '@/stores/apiClient.js'

const userStore = useUserStore();

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['update:modelValue', 'close', 'submit'])

const dialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const form = ref(null)
const loading = ref(false)

const formData = ref({
  categoryId: null,
  title: '',
  content: '',
})

const categoryOptions = categories

// Reset form when dialog closes
watch(dialog, (newValue) => {
  if (!newValue) {
    resetForm()
  }
})

function resetForm() {
  formData.value = {
    categoryId: null,
    title: '',
    content: '',
  }
  if (form.value) {
    form.value.resetValidation()
  }
}

async function submitPost() {
  const { valid } = await form.value.validate()
  if (!valid) return

  loading.value = true

  try {
    // Simulate API call
    const newPost = {
      title: formData.value.title,
      content: formData.value.content,
      categoryId: formData.value.categoryId,
    }

    const res = await apiClient.post('/posts', newPost);
    console.log("게시글 등록 후 응답 : ", res);

    newPost.postId=res.data.data.postId;
    newPost.createdAt = new Date().toISOString();
    newPost.author=userStore.userInfo.id;
    newPost.views=0;
    newPost.likes=0;
    newPost.commentList=[];

    emit('submit', newPost)
    emit('close')
  } catch (error) {
    console.error('Failed to create post:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* Custom styles if needed */
</style>
