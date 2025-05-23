<template>
  <v-card class="h-100" elevation="2">
    <v-card-title class="bg-secondary text-white pa-3">
      <v-icon left class="mr-2">mdi-account-group</v-icon>
      <span class="text-subtitle-1">Team Availability Overview</span>
    </v-card-title>

    <v-card-text class="pa-0">
      <TeamCalendar />
      <MeetingRecommendations />
    </v-card-text>
  </v-card>
</template>

<script setup>
import TeamCalendar from '@/components/meet/TeamCalendar.vue'
import MeetingRecommendations from '@/components/meet/MeetingRecommendations.vue'
</script>

<style scoped></style>
