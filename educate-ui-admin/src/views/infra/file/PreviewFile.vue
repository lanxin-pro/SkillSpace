<template>
  <Dialog v-model="dialogVisible" title="文件预览" width="600">
    <el-card
        v-loading="loading"
        :gutter="24"
        element-loading-text="生成文件中..."
        shadow="hover"
    >
      <img v-if="fileType==='image/png' " :src="fileImg" class="user-avatar">
    </el-card>
  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import ELComponent from '@/plugins/modal.js'
import { getFilePage } from "@/api/infra/file/index.js"

// 弹窗的是否展示
const dialogVisible = ref(false)

const fileImg = ref()
const loading = ref(true)
// 文件类型
const fileType = ref()
/** 打开弹窗 */
const open = async (type,url) => {
  loading.value = true
  dialogVisible.value = true
  fileType.value = type
  if(type === 'image/png'){
    fileImg.value = url
  }
  loading.value = false
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

</script>

<style scoped>

</style>
