<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="100px"
        size="small"
    >
      <el-form-item label="手机号" prop="mobile">
        <el-input
            v-model="queryParams.mobile"
            placeholder="请输入手机号"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="短信渠道" prop="channelId">
        <el-select
            v-model="queryParams.channelId"
            placeholder="请选择短信渠道"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="channel in channelList"
              :key="channel.id"
              :value="channel.id"
              :label="
              channel.signature +
              `【 ${getDictLabel(DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE, channel.code)}】`
            "
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模板编号" prop="templateId">
        <el-input
            v-model="queryParams.templateId"
            placeholder="请输入模板编号"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="发送状态" prop="sendStatus">
        <el-select
            v-model="queryParams.sendStatus"
            placeholder="请选择发送状态"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_SMS_SEND_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="发送时间" prop="sendTime">
        <el-date-picker
            v-model="queryParams.sendTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="接收状态" prop="receiveStatus">
        <el-select
            v-model="queryParams.receiveStatus"
            placeholder="请选择接收状态"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_SMS_RECEIVE_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="接收时间" prop="receiveTime">
        <el-date-picker
            v-model="queryParams.receiveTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" size="small" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:sms-log:export']">导出</el-button>
      </el-col>
    </el-row>


    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="手机号" align="center" prop="mobile" width="120">
        <template #default="scope">
          <div>{{ scope.row.mobile }}</div>
          <div v-if="scope.row.userType && scope.row.userId">
            <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
            {{ '(' + scope.row.userId + ')' }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="短信内容" align="center" prop="templateContent" width="300" />
      <el-table-column label="发送状态" align="center" width="110">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_SEND_STATUS" :value="scope.row.sendStatus" />
          <div>{{ formatDate(scope.row.sendTime) }}</div>
        </template>
      </el-table-column>
      <el-table-column label="接收状态" align="center" width="110">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_RECEIVE_STATUS" :value="scope.row.receiveStatus" />
          <div>{{ formatDate(scope.row.receiveTime) }}</div>
        </template>
      </el-table-column>
      <el-table-column label="短信渠道" align="center" width="110">
        <template #default="scope">
          <div>
            {{ channelList.find((channel) => channel.id === scope.row.channelId)?.signature }}
          </div>
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="scope.row.channelCode" />
        </template>
      </el-table-column>
      <el-table-column label="模板编号" align="center" prop="templateId" />
      <el-table-column label="短信类型" align="center" prop="templateType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE" :value="scope.row.templateType" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="fixed-width">
        <template #default="scope">
          <el-button size="small" type="text" icon="View" @click="openDetail(scope.row)"
                     v-hasPermi="['system:sms-log:query']">详细</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />
    <!-- 表单弹窗：详情 -->
    <SmsLogDetail ref="detailRef" />
  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { getIntDictOptions, DICT_TYPE,getDictLabel } from '@/utils/dict'
import { getSimpleSmsChannelList } from '@/api/system/sms/channel/index.js'
import { getSmsLogPage } from '@/api/system/sms/log/index.js'
import { handleTree } from '@/utils/ruoyi.js'
import SvgIcon from '@/components/SvgIcon/index.vue'
import { SystemMenuTypeEnum, CommonStatusEnum } from '@/utils/constants'
import DictTag from '@/components/DictTag/index.vue'
import ELComponent from '@/plugins/modal.js'
import { dateFormatter } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'
import { formatDate } from '@/utils/formatTime.js'
import SmsLogDetail from './SmsLogDetail.vue'

// 列表的加载中
const loading = ref(false)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
// 搜索的表单
const queryFormRef = ref()
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  channelId: null,
  templateId: null,
  mobile: '',
  sendStatus: null,
  receiveStatus: null,
  sendTime: [],
  receiveTime: []
})
// 导出的加载中
const exportLoading = ref(false)
// 短信渠道列表
const channelList = ref([])


/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载渠道列表
  const response = await getSimpleSmsChannelList()
  channelList.value = response.data
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getSmsLogPage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}


/** 详情操作 */
const detailRef = ref()
const openDetail = (data) => {
  detailRef.value.open(data)
}

</script>

<style scoped>

</style>
