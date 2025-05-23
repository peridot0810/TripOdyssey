<template>
  <div class="group-box">
    <!-- 1. Group Info Container -->
    <div class="group-info-section">
      <GroupInfo :groupData="groupInformation" />
    </div>

    <!-- 2. Group Member List -->
    <div class="group-member-section">
      <GroupMemberList :members="groupMembers" :groupInfo="groupInformation.groupInfo" />
    </div>

    <!-- 3. Group Chat -->
    <div class="group-chat-section">
      <GroupChat />
    </div>
  </div>
</template>

<script setup>
import GroupInfo from './GroupInfo.vue'
import GroupMemberList from './GroupMemberList.vue'
import GroupChat from './GroupChat.vue'
import { groupMembers, groupInformation } from '@/data/group/groupData.js'
</script>

<style scoped>
.group-box {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.group-info-section {
  flex-shrink: 0;
}

.group-member-section {
  flex: 1;
  min-height: 0;
  overflow: hidden;
}

.group-chat-section {
  flex: 1;
  min-height: 250px;
  overflow: hidden;
}
</style>
