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
import download from '@/utils/download.js'

// 是否加载中
const loading = ref(true)
// HTML 的地址
const src = ref('')

/**
 * 初始化
 */
onMounted(async () => {
  await init()
  ElComponent.msgSuccess("温馨提示：由于页面数据过长，在使用该页面的时候有可能会发生卡顿，强烈建议您导出HTML结构查看")
})

const init = async ()=>{
  try {
    const response = await DbDocApi.exportHtml()
    const blob = new Blob([response], { type: 'text/html' })
    src.value = window.URL.createObjectURL(blob)
  } finally {
    loading.value = false
  }
}

// 导出 html
const handleExportHtml = async () => {
  const res = await DbDocApi.exportHtml()
  download.html(res, '数据库文档.html')
  ElComponent.msgSuccess("成功导出")
}
// 导出 word
const handleExportWord = async () => {
  const res = await DbDocApi.exportWord()
  download.word(res, '数据库文档.doc')
  ElComponent.msgSuccess("成功导出")
}
// 导出 markdown
const handleExportMarkdown = async () => {
  const res = await DbDocApi.exportMarkdown()
  download.markdown(res, '数据库文档.md')
  ElComponent.msgSuccess("成功导出")
}
</script>

<style scoped>

</style>
