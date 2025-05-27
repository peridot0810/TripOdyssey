<template>
  <v-dialog v-model="dialog" max-width="400px" persistent>
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center">
        <span class="text-h6">프로필 수정</span>
        <v-btn icon variant="text" @click="close">
          <svg-icon type="mdi" :path="closePath" size="20" />
        </v-btn>
      </v-card-title>
      <v-divider></v-divider>

      <v-card-text>
				<!-- 프로필 이미지 -->
				<div class="d-flex flex-column align-center mb-4">
					<v-avatar size="90" class="mb-2">
						<v-img v-if="edited.profileImage" :src="profileImageUrl" alt="프로필 사진" />
						<svg-icon v-else type="mdi" :path="accountCirclePath" size="60" color="#BDBDBD" />
					</v-avatar>

					<div v-if="!showImageInput">
						<v-btn size="small" variant="outlined" @click="onChangeImage; showImageInput = true">
							<svg-icon type="mdi" :path="cameraPath" size="16" class="mr-1" />
							프로필 이미지 수정
						</v-btn>
					</div>
					<div v-else class="d-flex flex-column align-center">
						<input
							ref="fileInput"
							type="file"
							accept="image/*"
							style="margin-bottom: 8px;"
							@change="onFileSelected"
						/>
						<v-btn size="small" color="primary" variant="flat" @click="onImageEditComplete">
							수정 완료
						</v-btn>
					</div>
				</div>



        <v-form ref="form" @submit.prevent="submit">
          <v-text-field
            v-model="edited.userId"
            label="아이디"
            variant="outlined"
            readonly
            class="mb-3"
          />
          <v-text-field
            v-model="edited.email"
            label="이메일"
            variant="outlined"
            readonly
            class="mb-3"
          />
          <v-text-field
            v-model="edited.nickname"
            label="닉네임"
            variant="outlined"
            :rules="[
              v => !!v || '닉네임을 입력하세요',
              v => v.length <= 20 || '20자 이내로 입력하세요'
            ]"
            class="mb-3"
          />
          <v-text-field
            v-model="edited.age"
            label="나이"
            variant="outlined"
            type="number"
            :rules="[
              v => !!v || '나이를 입력하세요',
              v => v > 0 || '유효한 나이를 입력하세요'
            ]"
            class="mb-3"
          />
          <v-text-field
            v-model="genderText"
            label="성별"
            variant="outlined"
            readonly
            class="mb-3"
          />
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" color="grey" @click="close">취소</v-btn>
        <v-btn color="primary" @click="submit">저장</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiClose, mdiAccountCircle, mdiCamera } from '@mdi/js'
import apiClient from '@/utils/apiClient'

const userStore = useUserStore()
const showImageInput = ref(false)

const props = defineProps({
  modelValue: Boolean,
  userInfo: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'save'])

const dialog = computed({
  get: () => props.modelValue,
  set: v => emit('update:modelValue', v)
})

const edited = ref({
  userId: '',
  email: '',
  nickname: '',
  age: '',
  gender: '',
  profileImage: ''
})

watch(
  () => props.userInfo,
  (val) => {
    if (val) {
      edited.value = {
        userId: val.userId,
        email: val.email,
        nickname: val.nickname,
        age: val.age,
        gender: val.gender,
        profileImage: val.profileImage ? val.profileImage : ''
      }
    }
  },
  { immediate: true }
)

const genderText = computed(() =>
  edited.value.gender === 'male' ? '남성' : '여성'
)

const imageUrlPrefix = "http://localhost:8080"
const profileImageUrl = computed(() => {
  if (!edited.value.profileImage) return ''
  if (edited.value.profileImage.startsWith('data:')) {
    return edited.value.profileImage
  }
  return imageUrlPrefix + edited.value.profileImage
})


const closePath = mdiClose
const accountCirclePath = mdiAccountCircle
const cameraPath = mdiCamera

function close() {
  emit('update:modelValue', false)
}

function submit() {
  emit('save', { ...edited.value })
  close()
}

// 프로필 이미지 변경
const fileInput = ref(null)

function onChangeImage() {
  fileInput.value && fileInput.value.click()
}

const editedFile = ref(null)
function onFileSelected(e) {
  const file = e.target.files[0]
  if (file) {
		editedFile.value = file
		// 미리보기
    const reader = new FileReader()
    reader.onload = (ev) => {
      edited.value.profileImage = ev.target.result
    }
    reader.readAsDataURL(file)
  }
}

async function onImageEditComplete() {
	
	if (!editedFile.value) {
		alert('이미지를 선택해주세요.')
		return
	}

	showImageInput.value = false
	const formData = new FormData()
  formData.append('file', editedFile.value) 

	try {
		const res = await apiClient.post("/user/upload-profile", formData, {
			headers:{
				'Content-Type': 'multipart/form-data'
			}
		})
		userStore.updateUserInfo({
			profileImage: res.data.data.imageUrl
		})
		alert('프로필 이미지가 성공적으로 업로드되었습니다')
	} catch (error) {
		alert('프로필 이미지 업로드에 실패했습니다.')
		console.error(error)
	}
}

</script>

<style scoped>
.d-none {
  display: none;
}
</style>
