<template>
  <v-container fluid class="pa-0 transport-accommodation-container">
    <v-row no-gutters class="fill-height">
      <!-- Left Side: Transportation List -->
      <v-col cols="12" md="6" class="left-panel">
        <div class="panel-content">
          <TransportationList />
        </div>
      </v-col>

      <!-- Right Side: Accommodation List (Placeholder) -->
      <v-col cols="12" md="6" class="right-panel">
        <div class="panel-content">
          <AccommodationList />
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import TransportationList from '@/components/transportation/TransportationList.vue'
import AccommodationList from '@/components/accommodation/AccommodationList.vue'
</script>

<style scoped>
.transport-accommodation-container {
  height: calc(100vh - 64px); /* Adjust based on your app's header height */
  overflow: hidden;
}

.left-panel,
.right-panel {
  height: 100%;
  overflow-y: auto;
}

.left-panel {
  border-right: 1px solid #e0e0e0;
}

.panel-content {
  padding: 20px;
  height: 100%;
}

.placeholder-content {
  height: 400px;
  background-color: #f5f5f5;
  border-radius: 8px;
  margin-top: 16px;
  border: 2px dashed #bdbdbd;
}

/* Make scrollbars prettier */
.left-panel::-webkit-scrollbar,
.right-panel::-webkit-scrollbar {
  width: 6px;
}

.left-panel::-webkit-scrollbar-thumb,
.right-panel::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 3px;
}

.left-panel::-webkit-scrollbar-track,
.right-panel::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* Responsive adjustments */
@media (max-width: 960px) {
  .transport-accommodation-container {
    height: auto;
    overflow: visible;
  }

  .left-panel,
  .right-panel {
    height: auto;
    overflow-y: visible;
    border-right: none;
    border-bottom: 1px solid #e0e0e0;
    margin-bottom: 16px;
  }

  .right-panel {
    border-bottom: none;
  }
}
</style>
