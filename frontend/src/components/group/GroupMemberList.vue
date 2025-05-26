<template>
  <div class="group-member-list">
    <div class="list-title">그룹 멤버 (총 {{ sortedMembers.length }}명)</div>

    <div class="member-list-container">
      <SingleMemberCard v-for="member in sortedMembers" :key="member.userId" :member="member" />
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useMemberListStore } from '@/stores/memberList'
import { apiClient } from '@/utils/apiClient'
import SingleMemberCard from './SingleMemberCard.vue'

const route = useRoute()
const memberListStore = useMemberListStore()

const groupId = computed(() => route.params.groupId)

const fetchMembers = async () => {
  if (!groupId.value) return

  try {
    const res = await apiClient.get(`/group/${groupId.value}/members`)
    if (res.data.success) {
      memberListStore.setMembers(res.data.data)
    } else {
      throw new Error(res.data.message || '멤버 불러오기 실패')
    }
  } catch (err) {
    console.error(err)
    memberListStore.setError('멤버 정보를 가져오는 중 오류 발생')
  }
}

const rolePriority = {
  MASTER: 0,
  FINANCE: 1,
  SCHEDULE: 2,
  LOGISTICS: 3,
  MEMBER: 4,
}

const sortedMembers = computed(() => {
  return [...memberListStore.members].sort((a, b) => {
    const aRole = memberListStore.getMemberRole(a)
    const bRole = memberListStore.getMemberRole(b)
    const aOrder = rolePriority[aRole] ?? 99
    const bOrder = rolePriority[bRole] ?? 99
    if (aOrder !== bOrder) return aOrder - bOrder
    return a.nickName.localeCompare(b.nickName)
  })
})

onMounted(() => {
  fetchMembers()
})
</script>

<style scoped>
.group-member-list {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: var(--space-lg);
  font-family: 'Nunito', sans-serif;
}

.list-title {
  font-size: 1.1rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--space-md);
}

.member-list-container {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
  overflow-y: auto;
}
</style>
