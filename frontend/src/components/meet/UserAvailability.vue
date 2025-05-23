<template>
  <v-card class="h-100" elevation="2">
    <v-card-title class="bg-primary text-white pa-3">
      <v-icon left class="mr-2">mdi-calendar-range</v-icon>
      <span class="text-subtitle-1">Your Availability</span>
    </v-card-title>

    <v-card-text class="pa-3 d-flex flex-column align-center">
      <div class="date-picker-wrapper">
        <DatePicker />
      </div>

      <v-btn
        color="primary"
        block
        class="mt-3"
        prepend-icon="mdi-check"
        variant="flat"
        size="small"
      >
        Save Availability
      </v-btn>
    </v-card-text>
  </v-card>
</template>

<script setup>
import DatePicker from '@/components/meet/DatePicker.vue'
</script>

<style scoped>
.date-picker-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}
</style>
