<template>
  <v-container fluid class="pa-0 meeting-container">
    <div class="page-header">
      <h1 class="text-h4 font-weight-bold text-center mb-2">when2meet</h1>
      <p class="text-subtitle-1 text-center text-grey-darken-1 mb-4">
        Coordinate your team's availability and find the perfect meeting time
      </p>
    </div>

    <v-row class="fill-height ma-0">
      <!-- Left Panel: User Availability (Narrower) -->
      <v-col cols="12" lg="3" md="4" class="left-panel">
        <UserAvailability />
      </v-col>

      <!-- Right Panel: Team Availability Overview (Wider) -->
      <v-col cols="12" lg="9" md="8" class="right-panel">
        <TeamAvailabilityOverview />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import UserAvailability from '@/components/meet/UserAvailability.vue'
import TeamAvailabilityOverview from '@/components/meet/TeamAvailabilityOverview.vue'
</script>

<style scoped>
.meeting-container {
  min-height: calc(100vh - 64px);
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.page-header {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.left-panel,
.right-panel {
  padding: 8px;
}

/* Responsive adjustments */
@media (max-width: 1264px) {
  .meeting-container {
    padding: 16px;
  }

  .left-panel,
  .right-panel {
    margin-bottom: 16px;
  }
}

@media (max-width: 960px) {
  .page-header {
    padding: 16px;
    margin-bottom: 16px;
  }

  .page-header h1 {
    font-size: 2rem;
  }
}

@media (max-width: 600px) {
  .meeting-container {
    padding: 12px;
  }
}
</style>
