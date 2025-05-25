<template>
  <div class="master-view">
    <h1 class="page-title">MASTER DASHBOARD</h1>

    <div class="dashboard-grid">
      <!-- TOP LEFT - GROUP INFO -->
      <div class="section group-info">
        <h2 class="section-title">GROUP INFO</h2>
        <div class="section-content">
          <GroupInfoContainer />
        </div>
      </div>

      <!-- TOP RIGHT - MEMBER MANAGEMENT -->
      <div class="section member-management">
        <h2 class="section-title">MEMBER MANAGEMENT</h2>
        <div class="section-content">
          <!-- Member list, invite button, role dropdowns will go here -->
        </div>
      </div>

      <!-- BOTTOM LEFT - ROLE REQUESTS -->
      <div class="section role-requests">
        <h2 class="section-title">ROLE REQUESTS</h2>
        <div class="section-content">
          <!-- Finance, Schedule, Logistics request grids will go here -->
        </div>
      </div>

      <!-- BOTTOM RIGHT - TRIP PROGRESS -->
      <div class="section trip-progress">
        <h2 class="section-title">TRIP PROGRESS</h2>
        <div class="section-content">
          <!-- Progress stages and advance button will go here -->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import GroupInfoContainer from '@/components/master/GroupInfoContainer.vue'
</script>

<style scoped>
.master-view {
  padding: 1.5rem;
  width: 100%;
  margin: 0 auto;
}

.page-title {
  text-align: center;
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 2rem;
  color: #333;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 1.5rem;
  height: calc(100vh - 200px);
  min-height: 600px;
}

.section {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.section-title {
  background: #f8f9fa;
  padding: 1rem;
  margin: 0;
  font-size: 1.1rem;
  font-weight: 600;
  border-bottom: 1px solid #e0e0e0;
  border-radius: 8px 8px 0 0;
  color: #495057;
}

.section-content {
  flex: 1;
  padding: 1.5rem;
  overflow-y: auto;
}

/* Responsive design */
@media (max-width: 768px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
    grid-template-rows: repeat(4, auto);
    height: auto;
  }

  .master-view {
    padding: 1rem;
  }

  .page-title {
    font-size: 1.5rem;
    margin-bottom: 1rem;
  }
}
</style>
