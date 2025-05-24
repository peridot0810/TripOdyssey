<template>
  <div class="login-page">
    <!-- Background Image -->
    <div class="background-image"></div>

    <!-- Login Form Container -->
    <div class="login-container">
      <LoginForm />
    </div>
  </div>
</template>

<script setup>
import LoginForm from '@/components/main/LoginForm.vue'
</script>

<style scoped>
.login-page {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('/img/main2.png');
  background-size: 90%;
  background-position: center;
  background-repeat: no-repeat;
  z-index: 1;
}

.login-container {
  position: absolute;
  top: 0;
  right: 0;
  width: 25%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  z-index: 2;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

/* Responsive design */
@media (max-width: 1200px) {
  .login-container {
    width: 35%;
  }
}

@media (max-width: 768px) {
  .login-container {
    width: 100%;
    padding: 1rem;
    background: rgba(255, 255, 255, 0.9);
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 0.5rem;
  }
}
</style>
