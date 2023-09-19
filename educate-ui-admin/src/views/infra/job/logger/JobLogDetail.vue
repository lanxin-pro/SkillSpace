<template>
  <Dialog v-model="dialogVisible" title="任务详细" width="700px">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="日志编号" min-width="60">
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item label="任务编号">
        {{ detailData.jobId }}
      </el-descriptions-item>
      <el-descriptions-item label="处理器的名字">
        {{ detailData.handlerName }}
      </el-descriptions-item>
      <el-descriptions-item label="处理器的参数">
        {{ detailData.handlerParam }}
      </el-descriptions-item>
      <el-descriptions-item label="第几次执行">
        {{ detailData.executeIndex }}
      </el-descriptions-item>
      <el-descriptions-item label="执行时间">
        {{ formatDate(detailData.beginTime) + ' ~ ' + formatDate(detailData.endTime) }}
      </el-descriptions-item>
      <el-descriptions-item label="执行时长">
        {{ detailData.duration + ' 毫秒' }}
      </el-descriptions-item>
      <el-descriptions-item label="任务状态">
        <DictTag :type="DICT_TYPE.INFRA_JOB_LOG_STATUS" :value="detailData.status" />
      </el-descriptions-item>
      <el-descriptions-item label="执行结果">
        {{ detailData.duration + ' result' }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>

<script setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { formatDate } from '@/utils/formatTime.js'
import { getJobLog } from '@/api/infra/jobLog/index.js'
import DictTag from '@/components/DictTag/index.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中
const detailLoading = ref(false)
// 详情数据
const detailData = ref({})

/** 打开弹窗 */
const open = async (id) => {
  dialogVisible.value = true
  // 查看，设置数据
  if (id) {
    detailLoading.value = true
    try {
      const response = await getJobLog(id)
      detailData.value = response.data
    } finally {
      detailLoading.value = false
    }
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })
</script>

<style scoped>

</style>
