<template>
  <Dialog v-model="dialogVisible" :max-height="500" :scroll="true" title="消息详情">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="发送人">
        {{ detailData.templateNickname }}
      </el-descriptions-item>
      <el-descriptions-item label="发送时间">
        {{ formatDate(detailData.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item label="消息类型">
        <DictTag :type="DICT_TYPE.SYSTEM_NOTIFY_TEMPLATE_TYPE" :value="detailData.templateType" />
      </el-descriptions-item>
      <el-descriptions-item label="是否已读">
        <DictTag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="detailData.readStatus" />
      </el-descriptions-item>
      <el-descriptions-item v-if="detailData.readStatus" label="阅读时间">
        {{ formatDate(detailData.readTime) }}
      </el-descriptions-item>
      <el-descriptions-item label="内容">
        {{ detailData.templateContent }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import { formatDate } from '@/utils/formatTime.js'
import DictTag from '@/components/DictTag/index.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中
const detailLoading = ref(false)
// 详情数据
const detailData = ref()

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
