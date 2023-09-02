<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="公众号" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            size="small"
            icon="Plus"
            @click="onCreate"
            v-hasPermi="['mp:auto-reply:create']"
            v-if="msgType !== MsgType.Follow || list.length <= 0"
        >
          新增
        </el-button>
      </el-col>
    </el-row>

    <!-- tab 切换 -->
    <el-tabs v-model="msgType" @tab-change="onTabChange">

      <!-- tab 项 -->
      <el-tab-pane :name="MsgType.Follow">
        <template #label>
          <el-row align="middle"><el-icon class="msgIcon"><Star /></el-icon>关注时回复</el-row>
        </template>
      </el-tab-pane>
      <el-tab-pane :name="MsgType.Message">
        <template #label>
          <el-row align="middle"><el-icon class="msgIcon"><ChatDotRound /></el-icon>消息回复</el-row>
        </template>
      </el-tab-pane>
      <el-tab-pane :name="MsgType.Keyword">
        <template #label>
          <el-row align="middle"><el-icon class="msgIcon"><Avatar /></el-icon>关键词回复</el-row>
        </template>
      </el-tab-pane>
    </el-tabs>

    <!-- 列表 -->
    <ReplyTable
        :loading="loading"
        :list="list"
        :msg-type="msgType"
        @on-update="onUpdate"
        @on-delete="onDelete"
    />

    <el-dialog
        :title="isCreating ? '新增自动回复' : '修改自动回复'"
        v-model="showDialog"
        width="800px"
        destroy-on-close
    >
      <ReplyForm
          v-model="replyForm"
          v-model:reply="reply"
          :msg-type="msgType"
          ref="formRef"
      />
      <template #footer>
        <el-button
            @click="cancel"
            type="primary"
            size="default"
        >
          取 消
        </el-button>
        <el-button
            size="default"
            type="primary"
            @click="onSubmit"
        >
          确 定
        </el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { getAutoReplyPage,updateAutoReply,createAutoReply } from '@/api/mp/autoReply/index.js'
import Pagination from '@/components/Pagination/index.vue'
import { dateFormatter,formatDate } from '@/utils/formatTime'
import { fileSizeFormatter } from '@/utils'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted } from 'vue'
import WxAccountSelect from '../components/wx-account-select/index.vue'
import ReplyForm from './component/ReplyForm.vue'
import ReplyTable from './component/ReplyTable.vue'
import { MsgType,ReplyType } from '@/utils/constants.js'

// 公众号ID
const accountId = ref(-1)
// 消息类型
const msgType = ref(MsgType.Keyword)
// 遮罩层
const loading = ref(true)
// 总条数
const total = ref(0)
// 自动回复列表
const list = ref([])
// 表单 ref
const formRef = ref()
// 查询参数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  accountId: accountId
})
// 是否新建（否则编辑）
const isCreating = ref(false)
// 是否显示弹出层
const showDialog = ref(false)
// 表单参数
const replyForm = ref({})
// 回复消息
const reply = reactive({
  type: ReplyType.Text,
  accountId: -1
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getAutoReplyPage({
      ...queryParams,
      type: msgType.value
    })
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 侦听账号变化 */
const onAccountChanged = (id,name) => {
  accountId.value = id
  reply.accountId = id
  queryParams.pageNo = 1
  getList()
}

/** 切换tab */
const onTabChange = (tabName) => {
  msgType.value = tabName
  handleQuery()
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}
/** 新增按钮操作 */
const onCreate = () => {
  reset()
  // 打开表单，并设置初始化
  reply.value = {
    type: ReplyType.Text,
    accountId: queryParams.accountId
  }
  isCreating.value = true
  showDialog.value = true
}
// 取消按钮
const cancel = () => {
  showDialog.value = false
  reset()
}
/** 新增 */
const onSubmit = async () => {
  await formRef.value?.validate()

  // 处理回复消息
  const submitForm = { ...replyForm.value }
  submitForm.responseMessageType = reply.value.type
  submitForm.responseContent = reply.value.content
  submitForm.responseMediaId = reply.value.mediaId
  submitForm.responseMediaUrl = reply.value.url
  submitForm.responseTitle = reply.value.title
  submitForm.responseDescription = reply.value.description
  submitForm.responseThumbMediaId = reply.value.thumbMediaId
  submitForm.responseThumbMediaUrl = reply.value.thumbMediaUrl
  submitForm.responseArticles = reply.value.articles
  submitForm.responseMusicUrl = reply.value.musicUrl
  submitForm.responseHqMusicUrl = reply.value.hqMusicUrl

  if (replyForm.value.id !== undefined) {
    await updateAutoReply(submitForm)
    ELComponent.msgSuccess('修改成功')
  } else {
    await createAutoReply(submitForm)
    ELComponent.msgSuccess('新增成功')
  }

  showDialog.value = false
  await getList()
}


// 表单重置
const reset = () => {
  replyForm.value = {
    id: undefined,
    accountId: queryParams.accountId,
    type: msgType.value,
    requestKeyword: undefined,
    requestMatch: msgType.value === MsgType.Keyword ? 1 : undefined,
    requestMessageType: undefined
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>
.msgIcon{
  font-size: 16px;
  margin-right: 1px;
}
</style>
