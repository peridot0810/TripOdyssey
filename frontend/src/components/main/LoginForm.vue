<template>
  <v-card class="login-form" elevation="8">
    <v-card-title class="text-center pa-6">
      <h2 class="text-h4 font-weight-bold text-primary">로그인</h2>
    </v-card-title>

    <v-card-text class="pa-6">
      <v-form @submit.prevent="handleLogin">
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
        ></v-text-field>

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

        <!-- Remember Me Checkbox -->
        <v-checkbox
          v-model="rememberMe"
          label="로그인 상태 유지"
          class="mb-4"
        ></v-checkbox>

        <!-- Error Message -->
        <v-alert
          v-if="userStore.loginError"
          type="error"
          class="mb-4"
          closable
          @click:close="userStore.clearError()"
        >
          {{ userStore.loginError }}
        </v-alert>

        <!-- Login Button -->
        <v-btn
          type="submit"
          color="primary"
          size="large"
          block
          class="mb-4"
          :loading="userStore.isLoading"
        >
          로그인
        </v-btn>

        <!-- Forgot Password Button -->
        <v-btn
          variant="text"
          color="primary"
          block
          class="mb-2"
          @click="handleForgotPassword"
        >
          비밀번호를 잊으셨나요?
        </v-btn>

        <!-- Sign Up Button -->
        <v-btn
          variant="outlined"
          color="primary"
          size="large"
          block
          @click="handleSignUp"
        >
          회원가입
        </v-btn>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// Form data
const email = ref('')
const password = ref('')
const rememberMe = ref(false)

// Handle login submission
const handleLogin = async () => {
  if (!email.value || !password.value) {
    return
  }

  // Call the login function from the store
  const result = await userStore.login(email.value, password.value)

  if (result.success) {
    // Redirect to home page on successful login
    router.push('/')
  }
  // Error handling is done in the store, error message will be shown in the template
}

// Handle forgot password
const handleForgotPassword = () => {
  // TODO: Implement forgot password logic
  console.log('Forgot password clicked')
  // You might want to navigate to a forgot password page or show a dialog
}

// Handle sign up
const handleSignUp = () => {
  // TODO: Navigate to sign up page
  console.log('Sign up clicked')
  // router.push('/signup')
}
</script>

<style scoped>
.login-form {
  width: 100%;
  height: 67vh;
  max-width: 400px;
  border-radius: 16px;
  background: #ffffff;
}

/* Responsive design */
@media (max-width: 768px) {
  .login-form {
    height: auto;
    min-height: 67vh;
  }
}
</style>
