<template>
  <Dialog v-model="dialogVisible" :max-height="500" :scroll="true" title="详情" width="800">
    <el-descriptions :column="1" border>

      <el-descriptions-item label="日志主键" min-width="120">
        {{ detailData.id }}
      </el-descriptions-item>

    </el-descriptions>
  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions,getStrDictOptions } from '@/utils/dict.js'

import {  } from '@/api/system/mail/log/index.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'
import { formatDate } from '@/utils/formatTime.js'

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
