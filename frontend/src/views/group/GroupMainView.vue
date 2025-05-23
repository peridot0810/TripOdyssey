<template>
  <v-container fluid class="pa-0 group-main-container">
    <v-row no-gutters class="fill-height">
      <!-- 1. Group Info Section -->
      <v-col cols="12" lg="3" md="6" class="group-info-panel">
        <div class="panel-content">
          <GroupBox />
        </div>
      </v-col>

      <!-- 2. Official Schedule Container -->
      <v-col cols="12" lg="3" md="6" class="schedule-panel">
        <div class="panel-content">
          <GroupSchedule />
        </div>
      </v-col>

      <!-- 3. Transportation & Accommodation Section -->
      <v-col cols="12" lg="3" md="6" class="transport-accommodation-panel">
        <div class="panel-content">
          <div class="section-placeholder">
            <div class="placeholder-header">
              <v-icon size="large" color="primary">mdi-car-multiple</v-icon>
              <h3 class="text-h6 font-weight-bold mt-2">교통 & 숙박</h3>
            </div>
            <div class="placeholder-content">
              <div class="sub-section mb-4">
                <h4 class="text-subtitle-1 font-weight-medium mb-2">
                  <v-icon size="small" class="mr-1">mdi-airplane</v-icon>
                  교통수단
                </h4>
                <p class="text-body-2 text-grey-darken-1">교통 정보 컴포넌트</p>
              </div>
              <div class="sub-section">
                <h4 class="text-subtitle-1 font-weight-medium mb-2">
                  <v-icon size="small" class="mr-1">mdi-bed</v-icon>
                  숙박시설
                </h4>
                <p class="text-body-2 text-grey-darken-1">숙박 정보 컴포넌트</p>
              </div>
            </div>
          </div>
        </div>
      </v-col>

      <!-- 4. Finance Section -->
      <v-col cols="12" lg="3" md="6" class="finance-panel">
        <div class="panel-content">
          <div class="section-placeholder finance-placeholder">
            <div class="placeholder-header">
              <v-icon size="large" color="primary">mdi-calculator</v-icon>
              <h3 class="text-h6 font-weight-bold mt-2">예산 관리</h3>
            </div>
            <div class="placeholder-content">
              <div class="finance-summary">
                <div class="finance-item mb-3">
                  <div class="finance-label">총 예산</div>
                  <div class="finance-value">₩ 0</div>
                </div>
                <div class="finance-item mb-3">
                  <div class="finance-label">사용 금액</div>
                  <div class="finance-value">₩ 0</div>
                </div>
                <div class="finance-item">
                  <div class="finance-label">잔여 예산</div>
                  <div class="finance-value text-primary">₩ 0</div>
                </div>
              </div>
              <p class="text-caption text-grey-darken-1 mt-3">예산 관리 컴포넌트</p>
            </div>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import GroupSchedule from '@/components/group/GroupSchedule.vue'
import GroupBox from '@/components/group/GroupBox.vue'
</script>

<style scoped>
.group-main-container {
  height: calc(100vh - 64px);
  overflow: hidden;
}

.group-info-panel,
.schedule-panel,
.transport-accommodation-panel,
.finance-panel {
  height: 100%;
  border-right: 1px solid #e0e0e0;
  overflow-y: auto;
}

.finance-panel {
  border-right: none;
}

.panel-content {
  padding: 20px;
  height: 100%;
}

.section-placeholder {
  height: 100%;
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  border: 1px solid #e9ecef;
}

.placeholder-header {
  text-align: center;
  margin-bottom: 20px;
  flex-shrink: 0;
}

.placeholder-content {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.sub-section {
  padding: 12px;
  background-color: white;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.finance-placeholder {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  min-height: 400px;
}

.finance-summary {
  background-color: white;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.finance-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }
}

.finance-label {
  font-size: 0.875rem;
  color: #666;
  font-weight: 500;
}

.finance-value {
  font-size: 1rem;
  font-weight: 600;
  color: #333;
}

.group-info-panel::-webkit-scrollbar,
.schedule-panel::-webkit-scrollbar,
.transport-accommodation-panel::-webkit-scrollbar,
.finance-panel::-webkit-scrollbar {
  width: 6px;
}

.group-info-panel::-webkit-scrollbar-thumb,
.schedule-panel::-webkit-scrollbar-thumb,
.transport-accommodation-panel::-webkit-scrollbar-thumb,
.finance-panel::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 3px;
}

.group-info-panel::-webkit-scrollbar-track,
.schedule-panel::-webkit-scrollbar-track,
.transport-accommodation-panel::-webkit-scrollbar-track,
.finance-panel::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

@media (max-width: 1264px) {
  .group-main-container {
    height: auto;
    overflow: visible;
  }

  .group-info-panel,
  .schedule-panel,
  .transport-accommodation-panel,
  .finance-panel {
    height: auto;
    overflow-y: visible;
    border-right: none;
    border-bottom: 1px solid #e0e0e0;
    margin-bottom: 16px;
    min-height: 400px;
  }

  .finance-panel {
    border-bottom: none;
    margin-bottom: 0;
  }

  .section-placeholder {
    min-height: 300px;
  }

  .finance-placeholder {
    min-height: 300px;
  }
}

@media (max-width: 960px) {
  .panel-content {
    padding: 16px;
  }

  .section-placeholder {
    padding: 16px;
  }

  .finance-summary {
    padding: 12px;
  }
}

@media (max-width: 600px) {
  .panel-content {
    padding: 12px;
  }

  .section-placeholder {
    padding: 12px;
  }
}
</style>
