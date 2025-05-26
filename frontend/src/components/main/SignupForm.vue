<template>
  <v-card class="signup-form" elevation="8">
    <v-card-title class="text-center pa-6">
      <h2 class="text-h4 font-weight-bold text-primary">회원 가입</h2>
    </v-card-title>

    <v-card-text class="pa-6">
      <v-form @submit.prevent="handleSignup">

        <!-- ID Input -->
        <v-text-field
          v-model="id"
          label="아이디"
          type="text"
          variant="outlined"
          density="comfortable"
          class="mb-4"
          prepend-inner-icon="mdi-account"
          required
        >
          <template #append-inner>
            <v-btn
              size="small"
              color="primary"
              variant="tonal"
              @click="checkIdDuplicate"
              :loading="isCheckingId"
            >
              중복 확인
            </v-btn>
          </template>
        </v-text-field>
        <div
          v-if="idCheckMessage"
          :class="{'text-success': idCheckAvailable, 'text-error': !idCheckAvailable, 'field-message': true}"
        >
          {{ idCheckMessage }}
        </div>

        <!-- Email Input -->
        <v-text-field
          v-model="email"
          label="이메일"
          type="email"
          variant="outlined"
          density="comfortable"
          class="mb-4"
          prepend-inner-icon="mdi-email"
          required
        >
          <template #append-inner>
            <v-btn
              size="small"
              color="primary"
              variant="tonal"
              @click="checkEmailDuplicate"
              :loading="isCheckingEmail"
              :disabled="!isEmailFormatValid"
            >
              중복 확인
            </v-btn>
          </template>
        </v-text-field>
        <div
          v-if="emailCheckMessage"
          :class="{'text-success': emailCheckAvailable, 'text-error': !emailCheckAvailable, 'field-message': true}"
        >
          {{ emailCheckMessage }}
        </div>

        <!-- Password Input -->
        <v-text-field
          v-model="password"
          label="비밀번호"
          type="password"
          variant="outlined"
          density="comfortable"
          class="mb-4"
          prepend-inner-icon="mdi-lock"
          required
        ></v-text-field>

        <!-- Nickname Input -->
        <v-text-field
          v-model="nickname"
          label="닉네임"
          type="text"
          variant="outlined"
          density="comfortable"
          class="mb-4"
          prepend-inner-icon="mdi-account-circle"
          required
        ></v-text-field>

        <!-- Gender Input (Select) -->
        <v-select
          v-model="gender"
          :items="genderOptions"
          label="성별"
          variant="outlined"
          density="comfortable"
          class="mb-4"
          prepend-inner-icon="mdi-gender-male-female"
          required
        ></v-select>

        <!-- Age Input (Number) -->
        <v-text-field
          v-model="age"
          label="나이"
          type="number"
          variant="outlined"
          density="comfortable"
          class="mb-4"
          prepend-inner-icon="mdi-cake-variant"
          min="0"
          required
        ></v-text-field>

        <!-- Error Message -->
        <v-alert
          v-if="userStore.signupError"
          type="error"
          class="mb-4"
          closable
          @click:close="userStore.clearError()"
        >
          {{ userStore.signupError }}
        </v-alert>
        <v-alert
          v-if="signupCheckError"
          type="error"
          class="mb-4"
          closable
          @click:close="signupCheckError = ''"
        >
          {{ signupCheckError }}
        </v-alert>

        <!-- Signup Button -->
        <v-btn
          type="submit"
          color="primary"
          size="large"
          block
          class="mb-4"
          :loading="userStore.isLoading"
          :disabled="!isFormValid || !idCheckAvailable || !emailCheckAvailable"
        >
          회원가입
        </v-btn>

        <!-- Move To Login Page -->
        <v-btn
          variant="text"
          color="primary"
          block
          class="mb-2"
          @click="handleAlreadyHaveAccount"
        >
          이미 계정이 있으신가요?
        </v-btn>
        
      </v-form>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { apiClient } from "@/stores/apiClient"

const router = useRouter()
const userStore = useUserStore()

// Form data
const id = ref('')
const email = ref('')
const password = ref('')
const nickname = ref('')
const gender = ref('')
const age = ref('')

// Duplicate Check Message
const isCheckingId = ref(false)
const idCheckMessage = ref('')
const idCheckAvailable = ref(false)

const isCheckingEmail = ref(false)
const emailCheckMessage = ref('')
const emailCheckAvailable = ref(false)

// 회원가입 체크 에러 메시지
const signupCheckError = ref('')

const genderOptions = [
  { title: '남성', value: 'male' },
  { title: '여성', value: 'female' },
]

// 이메일 형식 체크 (정규표현식)
const isEmailFormatValid = computed(() => {
  // 간단한 이메일 정규식 (더 엄격하게도 가능)
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)
})

// 모든 값이 입력되었는지 체크
const isFormValid = computed(() =>
  id.value.trim() &&
  email.value.trim() &&
  password.value.trim() &&
  nickname.value.trim() &&
  gender.value &&
  age.value
)

// 이메일 입력값이 바뀔 때마다 형식 체크 및 메시지 처리
watch(email, (newVal) => {
  emailCheckAvailable.value = false
  if (!newVal) {
    emailCheckMessage.value = ''
  } else if (!isEmailFormatValid.value) {
    emailCheckMessage.value = '이메일 형식이 올바르지 않습니다.'
  } else {
    emailCheckMessage.value = ''
  }
})

// Handle signup submission
const handleSignup = async () => {
  // 중복체크가 안되어 있거나, 결과가 불가일 때 회원가입 요청 보내지 않음
  if (!idCheckAvailable.value || !emailCheckAvailable.value) {
    signupCheckError.value = '아이디/이메일 중복체크가 필요합니다'
    return
  }
  // 모든 값이 입력되어 있는지 추가로 체크 (혹시나)
  if (!isFormValid.value) {
    signupCheckError.value = '모든 값을 입력해 주세요'
    return
  }
  signupCheckError.value = '' // 기존 에러 메시지 제거

  try {
    const res = await apiClient.post("/auth/signup", {
      id: id.value,
      email: email.value,
      pw: password.value,
      nickname: nickname.value,
      gender: gender.value,
      age: age.value
    })

    router.push("/login")
  } catch (error) {
    console.log("회원가입 실패 : " + error)
  }
}

// Check Duplicate
const checkIdDuplicate = async () => {
  if(!id.value){  
    idCheckMessage.value="아이디를 입력하세요";
    idCheckAvailable.value=false;
    return;
  }

  isCheckingId.value=true;
  idCheckMessage.value='';
  idCheckAvailable.value=false;

  try {
    const res = await apiClient.get("/user/check/id", {
      params: { 
        id: id.value 
      }
    })  

    idCheckMessage.value="사용 가능한 아이디입니다.";
    idCheckAvailable.value=true;
  } catch (error) {
    idCheckMessage.value='이미 사용 중인 아이디입니다.';
    idCheckAvailable.value=false;
  } finally{
    isCheckingId.value=false;
  }
}

const checkEmailDuplicate = async () => {
  if(!email.value){  
    emailCheckMessage.value="이메일을 입력하세요";
    emailCheckAvailable.value=false;
    return;
  }
  if(!isEmailFormatValid.value){
    emailCheckMessage.value="이메일 형식이 올바르지 않습니다.";
    emailCheckAvailable.value=false;
    return;
  }

  isCheckingEmail.value=true;
  emailCheckMessage.value='';
  emailCheckAvailable.value=false;

  try {
    const res = await apiClient.get("/user/check/email", {
      params: { 
        email: email.value
      }
    })  

    emailCheckMessage.value="사용 가능한 이메일입니다.";
    emailCheckAvailable.value=true;
  } catch (error) {
    emailCheckMessage.value='이미 사용 중인 이메일입니다.';
    emailCheckAvailable.value=false;
  } finally{
    isCheckingEmail.value=false;
  }
}

const handleAlreadyHaveAccount = () => {
  router.push("/login")
}

</script>

<style scoped>
.signup-form {
  width: 100%;
  height: 67vh;
  max-width: 400px;
  border-radius: 16px;
  background: #ffffff;
}

.field-message {
  margin-top: -30px;   /* 입력창과 메시지 사이를 붙임 */
  margin-bottom: 20px;  /* 아래는 살짝 띄우기 */
  font-size: 0.95em;
}

/* Responsive design */
@media (max-width: 768px) {
  .signup-form {
    height: auto;
    min-height: 67vh;
  }
}
</style>
