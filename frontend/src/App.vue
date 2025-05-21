<template>
  <v-app>
    <!-- Top Navigation Bar -->
    <v-app-bar app flat color="primary" dark>
      <v-toolbar-title class="font-weight-bold">Trip Odyssey</v-toolbar-title>

      <v-spacer />

      <v-btn to="/" variant="text" class="mx-1" exact>Home</v-btn>
      <v-btn to="/community" variant="text" class="mx-1">Community</v-btn>
      <v-btn to="/user-info" variant="text" class="mx-1">User Page</v-btn>
    </v-app-bar>

    <!-- Main Content (wrapped!) -->
    <v-main>
      <RouterView />
    </v-main>
  </v-app>
</template>

<script setup>
import { RouterView } from 'vue-router'
</script>

<style scoped>
.v-btn.router-link-exact-active {
  background-color: rgba(255, 255, 255, 0.2);
}
</style>
