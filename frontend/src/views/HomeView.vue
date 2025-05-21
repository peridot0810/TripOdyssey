<template>
  <div>
    <h1>Group List Page</h1>
    <div class="group-list">
      <GroupInfoCard v-for="group in groups" :key="group.groupId" :group="group" />
    </div>
  </div>
</template>

<script setup>
import { groupData } from '@/data/group/groupData' // Adjust the path if needed
import GroupInfoCard from '@/components/group/GroupInfoCard.vue' // Adjust the path if needed

const groups = groupData
</script>

<style scoped>
.group-link {
  display: inline-block;
  margin-top: 12px;
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  text-decoration: none;
  border-radius: 6px;
  font-weight: 500;
  transition: background-color 0.2s ease;
}

.group-link:hover {
  background-color: #0056b3;
}

.group-list {
  margin-top: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
