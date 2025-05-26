<template>
  <div class="signup-page">
    <!-- Background Image -->
    <div class="background-image"></div>

    <!-- Signup Form Container -->
    <div class="signup-container">
      <SignupForm/>
    </div>
  </div>
</template>

<script setup>
import SignupForm from '@/components/main/SignupForm.vue'
</script>

<style scoped>
.signup-page {
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

.signup-container {
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
  .signup-container {
    width: 35%;
  }
}

@media (max-width: 768px) {
  .signup-container {
    width: 100%;
    padding: 1rem;
    background: rgba(255, 255, 255, 0.9);
  }
}

@media (max-width: 480px) {
  .signup-container {
    padding: 0.5rem;
  }
}
</style>
