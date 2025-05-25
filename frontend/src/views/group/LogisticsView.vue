<template>
  <v-container fluid class="pa-0 transport-accommodation-container">
    <v-row no-gutters class="fill-height">
      <!-- Left Side: Transportation List -->
      <v-col cols="12" lg="4" md="12" class="left-panel">
        <div class="panel-content">
          <TransportationList />
        </div>
      </v-col>

      <!-- Center: Proposed Accommodation List -->
      <v-col cols="12" lg="4" md="12" class="center-panel">
        <div class="panel-content">
          <ProposedAccommodationList />
        </div>
      </v-col>

      <!-- Right Side: Accommodation List -->
      <v-col cols="12" lg="4" md="12" class="right-panel">
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
import ProposedAccommodationList from '@/components/accommodation/ProposedAccommodationList.vue'
</script>

<style scoped>
.transport-accommodation-container {
  height: calc(100vh - 64px); /* Adjust based on your app's header height */
  overflow: hidden;
}

.left-panel,
.center-panel,
.right-panel {
  height: 100%;
  overflow-y: auto;
}

.left-panel {
  border-right: 1px solid #e0e0e0;
}

.center-panel {
  border-right: 1px solid #e0e0e0;
  background-color: #fafafa; /* Subtle background to distinguish the proposal panel */
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
.center-panel::-webkit-scrollbar,
.right-panel::-webkit-scrollbar {
  width: 6px;
}

.left-panel::-webkit-scrollbar-thumb,
.center-panel::-webkit-scrollbar-thumb,
.right-panel::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 3px;
}

.left-panel::-webkit-scrollbar-track,
.center-panel::-webkit-scrollbar-track,
.right-panel::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* Large screens: Three column layout */
@media (min-width: 1264px) {
  .left-panel,
  .center-panel,
  .right-panel {
    min-height: calc(100vh - 64px);
  }
}

/* Medium screens: Stack vertically */
@media (max-width: 1263px) {
  .transport-accommodation-container {
    height: auto;
    overflow: visible;
  }

  .left-panel,
  .center-panel,
  .right-panel {
    height: auto;
    overflow-y: visible;
    border-right: none;
    border-bottom: 1px solid #e0e0e0;
    margin-bottom: 16px;
  }

  .right-panel {
    border-bottom: none;
    margin-bottom: 0;
  }

  .center-panel {
    background-color: #f8f9fa; /* Slightly more visible on mobile */
    border-left: 3px solid #2196f3; /* Blue accent border */
    border-radius: 0 8px 8px 0;
  }
}

/* Small screens: Additional spacing adjustments */
@media (max-width: 600px) {
  .panel-content {
    padding: 16px;
  }

  .center-panel {
    margin: 8px 0;
    border-radius: 8px;
    border-left: 4px solid #2196f3;
  }
}

/* Panel specific styling */
.center-panel .panel-content {
  position: relative;
}

/* Add a subtle indicator for the proposal panel */
.center-panel::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #2196f3, #21cbf3);
  border-radius: 0 0 8px 8px;
}

/* Responsive panel order on mobile */
@media (max-width: 1263px) {
  .v-row {
    flex-direction: column;
  }

  /* Order: Transportation -> Proposals -> Accommodations */
  .left-panel {
    order: 1;
  }

  .center-panel {
    order: 2;
  }

  .right-panel {
    order: 3;
  }
}
</style>
