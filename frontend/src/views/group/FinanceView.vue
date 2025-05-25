<template>
  <v-container fluid class="pa-0 finance-container">
    <v-row no-gutters class="fill-height">
      <!-- Left Column: Group Schedule List (3/10 width) -->
      <v-col cols="12" md="3" class="left-panel">
        <div class="panel-content">
          <GroupSchedule />
        </div>
      </v-col>

      <!-- Center Column: Finance Management Component (5/10 width) -->
      <v-col cols="12" md="5" class="center-panel">
        <div class="panel-content">
          <div class="finance-list-container">
            <FinanceCardList />
          </div>
        </div>
      </v-col>

      <!-- Right Column: Member Expense List (2/10 width) -->
      <v-col cols="12" md="4" class="right-panel">
        <div class="panel-content">
          <MemberExpenseList />
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import GroupSchedule from '@/components/group/GroupSchedule.vue'
import FinanceCardList from '@/components/finance/FinanceCardList.vue'
import MemberExpenseList from '@/components/finance/MemberExpenseList.vue';
</script>

<style scoped>
.finance-container {
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

.finance-list-container {
  margin-top: 16px;
  height: calc(100% - 80px);
  overflow-y: auto;
}

/* Make scrollbars prettier */
.left-panel::-webkit-scrollbar,
.center-panel::-webkit-scrollbar,
.right-panel::-webkit-scrollbar,
.finance-list-container::-webkit-scrollbar {
  width: 6px;
}

.left-panel::-webkit-scrollbar-thumb,
.center-panel::-webkit-scrollbar-thumb,
.right-panel::-webkit-scrollbar-thumb,
.finance-list-container::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 3px;
}

.left-panel::-webkit-scrollbar-track,
.center-panel::-webkit-scrollbar-track,
.right-panel::-webkit-scrollbar-track,
.finance-list-container::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* Responsive adjustments */
@media (max-width: 960px) {
  .finance-container {
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

  .finance-list-container {
    height: auto;
    overflow-y: visible;
  }
}
</style>
