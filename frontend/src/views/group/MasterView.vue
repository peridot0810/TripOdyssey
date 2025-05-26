<template>
  <div class="master-view">
    <h1 class="page-title">MASTER DASHBOARD</h1>

    <div class="dashboard-container">
      <!-- LEFT COLUMN - GROUP INFO & TRIP PROGRESS -->
      <div class="left-column">
        <!-- Group Info Section -->
        <div class="info-section">
          <h2 class="section-title">GROUP INFO</h2>
          <div class="info-content">
            <GroupInfoContainer />
          </div>
        </div>

        <!-- Trip Progress Section -->
        <div class="progress-section">
          <h2 class="section-title">TRIP PROGRESS</h2>
          <div class="progress-content">
            <TripProgress />
          </div>
        </div>
      </div>

      <!-- RIGHT COLUMN - MEMBER MANAGEMENT -->
      <div class="right-column">
        <div class="member-section">
          <h2 class="section-title">MEMBER MANAGEMENT</h2>
          <div class="member-content">
            <GroupMemberList />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import GroupInfoContainer from '@/components/master/GroupInfoContainer.vue'
import GroupMemberList from '@/components/master/GroupMemberList.vue'
import TripProgress from '@/components/master/TripProgress.vue'
</script>

<style scoped>
.master-view {
  padding: 1.5rem;
  height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.page-title {
  text-align: center;
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 2rem;
  color: #333;
  flex-shrink: 0;
}

.dashboard-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
  height: 100%;
  overflow: hidden;
}

.left-column {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  height: 100%;
  overflow: hidden;
}

.right-column {
  height: 100%;
  overflow: hidden;
}

/* Section Styling */
.info-section,
.progress-section,
.member-section {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.info-section {
  flex: 0 0 auto;
  max-height: 45%;
}

.progress-section {
  flex: 1;
  min-height: 0;
}

.member-section {
  height: 100%;
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
  flex-shrink: 0;
}

/* Content Areas */
.info-content {
  padding: 1.5rem;
  overflow-y: auto;
  flex: 1;
}

.progress-content {
  padding: 1.5rem;
  overflow: hidden;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.member-content {
  padding: 1.5rem;
  overflow: hidden;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* Responsive Design */
@media (max-width: 768px) {
  .master-view {
    padding: 1rem;
    height: auto;
    min-height: calc(100vh - 80px);
  }

  .page-title {
    font-size: 1.5rem;
    margin-bottom: 1rem;
  }

  .dashboard-container {
    grid-template-columns: 1fr;
    grid-template-rows: auto auto;
    height: auto;
    overflow: visible;
  }

  .left-column {
    height: auto;
    overflow: visible;
  }

  .right-column {
    height: auto;
    overflow: visible;
  }

  .info-section,
  .progress-section,
  .member-section {
    min-height: 300px;
    overflow: visible;
  }

  .info-content,
  .progress-content,
  .member-content {
    overflow: visible;
  }
}
</style>
