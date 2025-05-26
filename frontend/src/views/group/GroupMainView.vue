<template>
  <v-container fluid class="pa-0 group-main-container">
    <v-row no-gutters class="fill-height">
      <!-- 1. Group Info Section -->
      <v-col cols="12" lg="3" md="6" class="group-info-panel">
        <div class="panel-content">
          <GroupBox />
        </div>
      </v-col>

      <!-- 2. Schedule & Proposal Container -->
      <v-col cols="12" lg="3" md="6" class="schedule-panel">
        <div class="panel-content">
          <div class="schedule-proposal-container">
            <!-- Toggle Header -->
            <div class="toggle-header">
              <v-btn-toggle
                v-model="scheduleActiveTab"
                color="primary"
                variant="outlined"
                divided
                mandatory
                class="toggle-buttons"
              >
                <v-btn value="schedule" class="toggle-btn">
                  <v-icon size="small" class="mr-1">mdi-calendar-check</v-icon>
                  일정
                </v-btn>
                <v-btn value="proposal" class="toggle-btn">
                  <v-icon size="small" class="mr-1">mdi-lightbulb-outline</v-icon>
                  제안
                </v-btn>
              </v-btn-toggle>
            </div>

            <!-- Content Area -->
            <div class="content-area">
              <div v-if="scheduleActiveTab === 'schedule'" class="tab-content">
                <GroupSchedule />
              </div>
              <div v-if="scheduleActiveTab === 'proposal'" class="tab-content">
                <ProposalList />
              </div>
            </div>
          </div>
        </div>
      </v-col>

      <!-- 3. Transportation & Accommodation Section -->
      <v-col cols="12" lg="3" md="6" class="transport-accommodation-panel">
        <div class="panel-content">
          <div class="transport-accommodation-container">
            <!-- Toggle Header -->
            <div class="toggle-header">
              <v-btn-toggle
                v-model="activeTab"
                color="primary"
                variant="outlined"
                divided
                mandatory
                class="toggle-buttons"
              >
                <v-btn value="transportation" class="toggle-btn">
                  <v-icon size="small" class="mr-1">mdi-airplane</v-icon>
                  교통수단
                </v-btn>
                <v-btn value="accommodation" class="toggle-btn">
                  <v-icon size="small" class="mr-1">mdi-bed</v-icon>
                  숙박시설
                </v-btn>
              </v-btn-toggle>
            </div>

            <!-- Content Area -->
            <div class="content-area">
              <div v-if="activeTab === 'transportation'" class="tab-content">
                <TransportationList />
              </div>
              <div v-if="activeTab === 'accommodation'" class="tab-content">
                <AccommodationList />
              </div>
            </div>
          </div>
        </div>
      </v-col>

      <!-- 4. Finance Section -->
      <v-col cols="12" lg="3" md="6" class="finance-panel">
        <div class="panel-content finance-content">
          <FinanceCardList />
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import GroupSchedule from '@/components/group/GroupSchedule.vue'
import GroupBox from '@/components/group/GroupBox.vue'
import TransportationList from '@/components/transportation/TransportationList.vue'
import AccommodationList from '@/components/accommodation/AccommodationList.vue'
import FinanceCardList from '@/components/finance/FinanceCardList.vue'
import ProposalList from '@/components/schedule/ProposalList.vue'

// Active tab states
const activeTab = ref('transportation')
const scheduleActiveTab = ref('schedule')
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

.transport-accommodation-container,
.schedule-proposal-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #f8f9fa;
  border-radius: 12px;
  border: 1px solid #e9ecef;
  overflow: hidden;
}

.toggle-header {
  padding: 16px 16px 0 16px;
  flex-shrink: 0;
  background-color: white;
  border-bottom: 1px solid #e0e0e0;
}

.toggle-buttons {
  width: 100%;
}

.toggle-btn {
  flex: 1;
  text-transform: none;
  font-weight: 500;
}

.content-area {
  flex: 1;
  overflow-y: auto;
  background-color: white;
}

.tab-content {
  height: 100%;
  padding: 16px;
}

.finance-content {
  padding: 0 !important;
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

  .transport-accommodation-container,
  .schedule-proposal-container {
    min-height: 350px;
  }

  .section-placeholder {
    min-height: 300px;
  }

  .finance-content {
    padding: 16px !important;
  }
}

@media (max-width: 960px) {
  .panel-content {
    padding: 16px;
  }

  .toggle-header {
    padding: 12px 12px 0 12px;
  }

  .tab-content {
    padding: 12px;
  }

  .section-placeholder {
    padding: 16px;
  }

  .finance-content {
    padding: 12px !important;
  }
}

@media (max-width: 600px) {
  .panel-content {
    padding: 12px;
  }

  .toggle-header {
    padding: 8px 8px 0 8px;
  }

  .tab-content {
    padding: 8px;
  }

  .section-placeholder {
    padding: 12px;
  }

  .toggle-btn {
    font-size: 0.75rem;
    padding: 8px 12px;
  }
}
</style>
