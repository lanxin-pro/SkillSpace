<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="公众号" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
      <el-form-item label="消息类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择消息类型" class="!w-240px">
          <el-option
              v-for="dict in getStrDictOptions(DICT_TYPE.MP_MESSAGE_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户标识" prop="openid">
        <el-input
            v-model="queryParams.openid"
            placeholder="请输入用户标识"
            clearable
            :v-on="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
            v-model="queryParams.createTime"
            style="width: 240px"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" type="primary" icon="Search">
          搜索
        </el-button>
        <el-button @click="resetQuery" icon="Refresh">
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <MessageTable
        class="mt10"
        :list="list"
        :loading="loading"
        @send="handleSend"
    />

    <Pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 发送消息的弹窗 -->
    <el-dialog
        title="粉丝消息列表"
        v-model="messageBox.show"
        @click="messageBox.show = true"
        width="50%"
        destroy-on-close
    >

      <WxMsg
          :user-id="messageBox.userId"
      />
    </el-dialog>

  </div>
</template>

<script setup>
import { getMessagePage,sendMessage } from '@/api/mp/message/index.js'
import Pagination from '@/components/Pagination/index.vue'
import { dateFormatter,formatDate } from '@/utils/formatTime'
import { fileSizeFormatter } from '@/utils'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted } from 'vue'
import MessageTable from './MessageTable.vue'
import WxMsg from '@/views/mp/components/wx-msg/index.vue'
import WxAccountSelect from '@/views/mp/components/wx-account-select/index.vue'
import { DICT_TYPE, getStrDictOptions } from '@/utils/dict'
import { MsgType } from '@/views/mp/components/wx-msg/types.js'


const loading = ref(false)
// 数据的总页数
const total = ref(0)
// 当前页的列表数据
const list = ref([])

// 搜索参数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  openid: '',
  accountId: -1,
  type: MsgType.Text,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()

// 消息对话框
const messageBox = reactive({
  show: false,
  userId: 0
})

/** 侦听accountId */
const onAccountChanged = (id) => {
  queryParams.accountId = id
  queryParams.pageNo = 1
  handleQuery()
}

/** 查询列表 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

const getList = async () => {
  try {
    loading.value = true
    const response = await getMessagePage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 重置按钮操作 */
const resetQuery = async () => {
  // 暂存 accountId，并在 reset 后恢复
  const accountId = queryParams.accountId
  queryFormRef.value?.resetFields()
  queryParams.accountId = accountId
  handleQuery()
}

/** 打开消息发送窗口 */
const handleSend = async (userId) => {
  messageBox.userId = userId
  messageBox.show = true
}
</script>

<style scoped>

</style>
