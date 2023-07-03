<template>
  <ConfigProvider>
      <RouterView />
  </ConfigProvider>
</template>

<script setup>
import { computed } from 'vue'
import { ConfigProvider } from 'ant-design-vue'
import { AppProvider } from '@/components/Application'


</script>

