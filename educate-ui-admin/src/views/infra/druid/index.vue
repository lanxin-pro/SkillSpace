<template>
  <IFrame :src="src" />
</template>

<script setup>
import { onMounted, ref } from 'vue'
import IFrame from '@/components/IFrame/index.vue'
import { getConfigKey } from '@/api/infra/config/index.js'

// 是否加载中
const loading = ref(true)
// Knife4j UI
const src = ref(import.meta.env.VITE_SERVER_BASE_API + '/druid/index.html')
// Swagger UI

</script>

<style scoped>

</style>
