<template>
  <div class="app-container">

    <el-card class="box-card" style="margin:10px 0;">
      <template #header>
        <span style="font-weight: bolder">数据库文档</span>
      </template>

      <!-- 操作工作栏 -->
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="plus" size="small" @click="handleExportHtml">导出 HTML</el-button>
          <el-button type="primary" icon="plus" size="small" @click="handleExportWord">导出 Word</el-button>
          <el-button type="primary" icon="plus" size="small" @click="handleExportMarkdown">导出 Markdown</el-button>
        </el-col>
      </el-row>

      <!-- 展示文档 -->
      <IFrame v-if="!loading" v-loading="loading" :src="src" />

    </el-card>

  </div>
</template>

<script setup>
import { ref,onMounted } from 'vue'
import IFrame from '@/components/IFrame/index.vue'
import * as DbDocApi from '@/api/infra/dbDoc'
import ElComponent from '@/plugins/modal.js'

// 是否加载中
const loading = ref(true)
// HTML 的地址
const src = ref('')

/**
 * 初始化
 */
onMounted(async () => {
  await init()
  ElComponent.msgSuccess("温馨提示：由于页面数据过长，在使用该页面的时候有可能会发生卡顿")
})

const init = async ()=>{
  try {
    const response = await DbDocApi.exportHtml()
    console.log("文档结果",response)
    const blob = new Blob([response], { type: 'text/html' })
    src.value = window.URL.createObjectURL(blob)
  } finally {
    loading.value = false
  }
}

</script>

<style scoped>

</style>
