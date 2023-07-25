<template>
  <Dialog
      v-model="dialogVisible"
      align-center
      class="app-infra-codegen-preview-container"
      title="代码预览"
      width="80%"
  >

  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 加载中的状态
const loading = ref(false)

/** 打开弹窗 */
const open = async (id) => {
  dialogVisible.value = true
  try {
    loading.value = true

  } finally {
    loading.value = false
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗



</script>

<style scoped>

</style>
