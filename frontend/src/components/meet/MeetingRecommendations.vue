<template>
  <v-card elevation="1" class="mt-3">
    <v-card-title class="bg-success text-white pa-2">
      <v-icon left size="small" class="mr-1">mdi-chart-line</v-icon>
      <span class="text-subtitle-2">Best Meeting Times</span>
    </v-card-title>

    <v-card-text class="pa-2">
      <v-row dense>
        <v-col cols="4">
          <div class="stat-item text-center">
            <div class="stat-number text-h6 text-primary font-weight-bold">July 14-16</div>
            <div class="stat-label text-caption text-grey-darken-1">Best Days</div>
          </div>
        </v-col>

        <v-col cols="4">
          <div class="stat-item text-center">
            <div class="stat-number text-h6 text-success font-weight-bold">4 people</div>
            <div class="stat-label text-caption text-grey-darken-1">Max Available</div>
          </div>
        </v-col>

        <v-col cols="4">
          <div class="stat-item text-center">
            <div class="stat-number text-h6 text-warning font-weight-bold">6 total</div>
            <div class="stat-label text-caption text-grey-darken-1">Responses</div>
          </div>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script setup></script>

<style scoped>
.stat-item {
  padding: 8px 4px;
}

.stat-number {
  line-height: 1.2;
  margin-bottom: 2px;
}

.stat-label {
  text-transform: uppercase;
  letter-spacing: 0.3px;
  font-weight: 500;
}
</style>
