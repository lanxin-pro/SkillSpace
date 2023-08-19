<template>
  <Dialog v-model="dialogVisible" :max-height="500" :scroll="true" title="详情" width="800">
    <el-descriptions :column="1" border>

      <el-descriptions-item label="日志主键" min-width="120">
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item label="用户编号" min-width="120">
        {{ detailData.userId }}
      </el-descriptions-item>
      <el-descriptions-item label="用户类型" min-width="120">
        <DictTag :type="DICT_TYPE.USER_TYPE" :value="detailData.userType"/>
      </el-descriptions-item>
      <el-descriptions-item label="接收邮箱地址" min-width="120">
        {{ detailData.toMail }}
      </el-descriptions-item>
      <el-descriptions-item label="邮箱账号编号" min-width="120">
        {{ detailData.accountId }}
      </el-descriptions-item>
      <el-descriptions-item label="发送邮箱地址" min-width="120">
        {{ detailData.fromMail }}
      </el-descriptions-item>
      <el-descriptions-item label="模板编号" min-width="120">
        {{ detailData.templateId }}
      </el-descriptions-item>
      <el-descriptions-item label="模板编码" min-width="120">
        {{ detailData.templateCode }}
      </el-descriptions-item>
      <el-descriptions-item label="模版发送人名称" min-width="120">
        {{ detailData.templateNickname }}
      </el-descriptions-item>
      <el-descriptions-item label="邮件标题" min-width="120">
        {{ detailData.templateTitle }}
      </el-descriptions-item>
      <el-form-item label="邮件内容：">
        <Editor v-model="detailData.templateContent" :min-height="192" read-only />
      </el-form-item>
      <el-descriptions-item label="邮件参数" min-width="120">
        {{ detailData.templateParams }}
      </el-descriptions-item>
      <el-descriptions-item label="发送状态" min-width="120">
        <DictTag :type="DICT_TYPE.SYSTEM_MAIL_SEND_STATUS" :value="detailData.sendStatus"/>
      </el-descriptions-item>
      <el-descriptions-item label="发送时间" min-width="120">
        {{ parseTime(detailData.sendTime) }}
      </el-descriptions-item>
      <el-descriptions-item label="发送返回的消息编号" min-width="120">
        {{ detailData.sendMessageId }}
      </el-descriptions-item>
      <el-descriptions-item label="发送异常" min-width="120">
        {{ detailData.sendException }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions,getStrDictOptions } from '@/utils/dict.js'
import { parseTime } from '@/utils/ruoyi.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'
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
