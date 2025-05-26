<template>
  <v-app>
    <!-- Top Navigation Bar -->
    <v-app-bar
      app
      flat
      :style="{
        backgroundColor: '#EBEBEB',
        height: '64px',
        boxShadow: 'none',
        display: 'flex',
        alignItems: 'center',
      }"
      elevation="0"
    >
      <v-toolbar-title
        class="font-weight-bold custom-title"
        style="
          font-family: 'Inter', sans-serif;
          font-size: 30px;
          color: #6e787a;
          font-weight: 800;
          line-height: 1.4;
          padding: 8px 0;
          display: flex;
          align-items: center;
        "
      >
        Trip Odyssey
      </v-toolbar-title>

      <v-btn to="/" class="custom-nav-btn" :class="{ 'active-btn': $route.path === '/' }" exact>
        HOME
      </v-btn>
      <v-btn
        to="/community"
        class="custom-nav-btn"
        :class="{ 'active-btn': $route.path === '/community' }"
      >
        COMMUNITY
      </v-btn>
      <v-btn
        to="/user-info"
        class="custom-nav-btn"
        :class="{ 'active-btn': $route.path === '/user-info' }"
      >
        USER PAGE
      </v-btn>
    </v-app-bar>

    <!-- Main Content -->
    <v-main class="main-content">
      <RouterView />
    </v-main>
  </v-app>
</template>

<script setup>
import { RouterView } from 'vue-router'
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore();

onMounted(() => {
  userStore.loadUserInfoFromStorage();
})

import { useRoute } from 'vue-router'

const $route = useRoute()
</script>

<style scoped>
/* 기본 버튼 스타일 */
.custom-nav-btn {
  background-color: #fff !important;
  border: 2px solid #e1e1e1 !important;
  border-radius: 32px !important;
  padding: 8px 24px !important;
  font-family: 'Inter', sans-serif !important;
  font-size: 22px !important;
  color: #6e787a !important;
  font-weight: 400 !important;
  margin: 0 6px;
  box-shadow: none !important;
  min-width: unset !important;
  min-height: unset !important;
  height: auto !important;
  line-height: 1.21 !important;
  display: flex;
  align-items: center;
  justify-content: center;
  transition:
    transform 0.2s,
    border-color 0.2s,
    border-width 0.2s,
    background-color 0.2s;
}

/* 호버 효과 */
.custom-nav-btn:hover {
  transform: scale(1.1);
  border-width: 3px !important;
  border-color: #54f6de !important;
}

/* 활성 상태 스타일 - 더 높은 우선순위 */
.custom-nav-btn.active-btn {
  background-color: #54f6de !important;
  border-color: #54f6de !important;
  color: #fff !important;
  font-weight: 600 !important;
}

/* Vuetify의 기본 라우터 active 클래스 오버라이드 */
.v-btn.router-link-exact-active.custom-nav-btn {
  background-color: #54f6de !important;
  border-color: #54f6de !important;
  color: #fff !important;
  font-weight: 600 !important;
}

/* 활성 상태에서 호버 시 스타일 */
.custom-nav-btn.active-btn:hover,
.v-btn.router-link-exact-active.custom-nav-btn:hover {
  transform: scale(1.05);
  background-color: #45e6ce !important;
  border-color: #45e6ce !important;
}

.v-app-bar {
  box-shadow: none !important;
}

/* 타이틀 스타일 추가 */
.custom-title {
  line-height: 1.4 !important;
  padding-top: 4px !important;
  padding-bottom: 4px !important;
  display: flex !important;
  align-items: center !important;
  height: auto !important;
}

.v-toolbar__content {
  height: 64px !important;
  font-size: 24px !important;
  font-family: 'Inter', sans-serif !important;
  color: #6e787a !important;
  font-weight: 400 !important;
  align-items: center !important;
  padding: 8px 16px !important;
}

/* Main content styling to prevent scrolling issues */
.main-content {
  height: 100vh;
  overflow-y: auto;
}
</style>
