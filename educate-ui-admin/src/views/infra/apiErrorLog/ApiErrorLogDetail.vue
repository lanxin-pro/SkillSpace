<template>
  <Dialog v-model="dialogVisible" :max-height="500" :scroll="true" title="详情" width="800">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="日志主键" min-width="120">
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item label="链路追踪">
        {{ detailData.traceId }}
      </el-descriptions-item>
      <el-descriptions-item label="应用名">
        {{ detailData.applicationName }}
      </el-descriptions-item>
      <el-descriptions-item label="用户编号">
        {{ detailData.userId }}
        <dict-tag :type="DICT_TYPE.USER_TYPE" :value="detailData.userType" />
      </el-descriptions-item>
      <el-descriptions-item label="用户 IP">
        {{ detailData.userIp }}
      </el-descriptions-item>
      <el-descriptions-item label="用户 UA">
        {{ detailData.userAgent }}
      </el-descriptions-item>
      <el-descriptions-item label="请求信息">
        {{ detailData.requestMethod }} {{ detailData.requestUrl }}
      </el-descriptions-item>
      <el-descriptions-item label="请求参数">
        {{ detailData.requestParams }}
      </el-descriptions-item>
      <el-descriptions-item label="异常时间">
        {{ formatDate(detailData.exceptionTime) }}
      </el-descriptions-item>
      <el-descriptions-item label="异常名">
        {{ detailData.exceptionName }}
      </el-descriptions-item>
      <el-descriptions-item v-if="detailData.exceptionStackTrace" label="异常堆栈">
        <el-input
            style="font-size: 10px"
            v-model="detailData.exceptionStackTrace"
            :autosize="{ maxRows: 25 }"
            :readonly="true"
            type="textarea"
        />
      </el-descriptions-item>
      <el-descriptions-item label="处理状态">
        <dict-tag
            :type="DICT_TYPE.INFRA_API_ERROR_LOG_PROCESS_STATUS"
            :value="detailData.processStatus"
        />
      </el-descriptions-item>
      <el-descriptions-item v-if="detailData.processUserId" label="处理人">
        {{ detailData.processUserId }}
      </el-descriptions-item>
      <el-descriptions-item v-if="detailData.processTime" label="处理时间">
        {{ formatDate(detailData.processTime) }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>

<script setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { formatDate } from '@/utils/formatTime.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中
const detailLoading = ref(false)
const detailData = ref() // 详情数据

/** 打开弹窗 */
const open = async (data) => {
  dialogVisible.value = true
  // 设置数据
  detailLoading.value = true
  try {
    detailData.value = data
  } finally {
    detailLoading.value = false
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })
</script>

<style scoped>

</style>
