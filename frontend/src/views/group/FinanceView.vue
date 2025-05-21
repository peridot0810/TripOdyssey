<template>
  <div class="container">
    <h1>재무</h1>
    <h2>그룹 아이디: {{ groupId }}</h2>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const groupId = route.params.groupId

onMounted(() => {
  console.log('FinanceView mounted!')
})
</script>

<style scoped></style>
