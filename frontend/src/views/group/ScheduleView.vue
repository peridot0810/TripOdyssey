<template>
  <v-container fluid class="pa-0 schedule-container">
    <v-row no-gutters class="fill-height">
      <!-- Left Column: Proposed Locations List -->
      <v-col cols="12" md="3" class="left-panel">
        <div class="panel-content">
          <h2 class="text-h6 font-weight-bold mb-3">일정 조율</h2>
          <v-divider class="mb-4"></v-divider>
          <ProposalList />
        </div>
      </v-col>

      <!-- Center Column: Slightly wider for main content -->
      <v-col cols="12" md="5" class="center-panel">
        <div class="panel-content">
          <h2 class="text-h6 font-weight-bold mb-3">일정 계획</h2>
          <v-divider class="mb-4"></v-divider>
          <div class="placeholder-content d-flex flex-column align-center justify-center">
            <v-icon size="64" color="grey-lighten-1">mdi-calendar-text</v-icon>
            <p class="text-body-1 text-grey mt-3">일정 계획 영역</p>
            <p class="text-caption text-grey-darken-1">여기에 선택된 장소들의 일정이 표시됩니다</p>
          </div>
        </div>
      </v-col>

      <!-- Right Column: Additional Information/Map -->
      <v-col cols="12" md="4" class="right-panel">
        <div class="panel-content">
          <h2 class="text-h6 font-weight-bold mb-3">지도 보기</h2>
          <v-divider class="mb-4"></v-divider>
          <div class="placeholder-content d-flex flex-column align-center justify-center">
            <v-icon size="64" color="grey-lighten-1">mdi-map</v-icon>
            <p class="text-body-1 text-grey mt-3">지도 영역</p>
            <p class="text-caption text-grey-darken-1">여기에 선택된 장소들의 위치가 표시됩니다</p>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import ProposalList from '@/components/schedule/ProposalList.vue';
</script>

<style scoped>
.schedule-container {
  height: calc(100vh - 64px); /* Adjust based on your app's header height */
  overflow: hidden;
}

.left-panel, .center-panel, .right-panel {
  height: 100%;
  border-right: 1px solid #e0e0e0;
  overflow-y: auto;
}

.right-panel {
  border-right: none;
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

/* Responsive adjustments */
@media (max-width: 960px) {
  .schedule-container {
    height: auto;
    overflow: visible;
  }

  .left-panel, .center-panel, .right-panel {
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
