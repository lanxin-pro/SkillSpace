<template>
  <Dialog v-model="dialogVisible" title="文件预览" width="800">
    <div style="margin-top: -20px;margin-bottom: 10px">
      <el-collapse v-model="activeNames" @change="handleChange">
        <el-collapse-item title="图片详细参数" name="1">
          <div>
           {{file}}
          </div>
          <div>
            Consistent within interface: all elements should be consistent, such
            as: design style, icons and texts, position of elements, etc.
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>

    <el-card
        v-loading="loading"
        :gutter="24"
        element-loading-text="生成文件中..."
        shadow="hover"
        style="display: flex"
    >
      <el-image
          class="user-avatar"
          :src="fileImg"
      />
      <div style="display: flex;justify-content: flex-end">
        <el-button type="success" round @click="handleDownload">下载原图</el-button>
      </div>
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
const filePath = ref()
const file = ref()
/** 打开弹窗 */
const open = async (row) => {
  loading.value = true
  dialogVisible.value = true
  file.value = row
  filePath.value = row.path
  fileType.value = row.type
  fileImg.value = row.url
  loading.value = false
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })
/** 下载原图 */
const handleDownload = ()=>{
  fetch(fileImg.value)
      .then(response => response.blob())
      .then(blob => {
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = filePath.value
        link.click()
      })
}
</script>

<style scoped>

</style>
