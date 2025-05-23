<template>
  <v-navigation-drawer app width="220" permanent class="pa-3">
    <v-list density="comfortable" nav>
      <v-list-item
        v-for="item in links"
        :key="item.label"
        :to="`/group/${groupId}/${item.path}`"
        :title="item.label"
        :prepend-icon="item.icon"
        exact
        router
      />
    </v-list>
  </v-navigation-drawer>

  <v-main class="pa-4">
    <RouterView />
  </v-main>
</template>

<script setup>
import { useRoute, RouterView } from 'vue-router'

const groupId = useRoute().params.groupId

const links = [
  { label: '그룹 홈', path: '', icon: 'mdi-home' },
  { label: '일정 조율', path: 'meeting', icon: 'mdi-account-group' },
  { label: '장소 찾아보기', path: 'map', icon: 'mdi-map' },
  { label: '일정 계획', path: 'schedule', icon: 'mdi-calendar' },
  { label: '교통/숙소', path: 'logistics', icon: 'mdi-truck' },
  { label: '재무', path: 'finance', icon: 'mdi-cash' },
  { label: '채팅', path: 'chat', icon: 'mdi-chat' },
  { label: '역할', path: 'role', icon: 'mdi-role' },
]
</script>

<style scoped></style>
