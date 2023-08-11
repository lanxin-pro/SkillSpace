<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="150px"
        size="small"
    >
      <el-form-item label="短信类型" prop="type">
        <el-select
            v-model="queryParams.type"
            placeholder="请选择短信类型"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开启状态" prop="status">
        <el-select
            v-model="queryParams.status"
            placeholder="请选择开启状态"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模板编码" prop="code">
        <el-input
            v-model="queryParams.code"
            placeholder="请输入模板编码"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="短信 API 的模板编号" prop="apiTemplateId">
        <el-input
            v-model="queryParams.apiTemplateId"
            placeholder="请输入短信 API 的模板编号"
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
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
            v-model="queryParams.createTime"
            style="width: 240px"
            type="daterange"
            value-format="YYYY-MM-DD HH:mm:ss"
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
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm('create')"
                   v-hasPermi="['system:sms-template:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" size="small" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:sms-template:export']">导出</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column
          label="模板编码"
          align="center"
          prop="code"
          width="120"
          :show-overflow-tooltip="true"
      />
      <el-table-column
          label="模板名称"
          align="center"
          prop="name"
          width="120"
          :show-overflow-tooltip="true"
      />
      <el-table-column label="短信渠道" align="center" width="120">
        <template #default="scope">
          <div>
            {{ channelList.find((channel) => channel.id === scope.row.channelId)?.signature }}
          </div>
          <DictTag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="scope.row.channelCode" />
        </template>
      </el-table-column>
      <el-table-column
          label="模板内容"
          align="center"
          prop="content"
          width="200"
          :show-overflow-tooltip="true"
      />
      <el-table-column label="短信类型" align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>

      <el-table-column label="状态" align="center" prop="status" width="80">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
          label="短信 API 的模板编号"
          align="center"
          prop="apiTemplateId"
          width="200"
          :show-overflow-tooltip="true"
      />

      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center" width="210">
        <template #default="scope">
          <el-button size="small" type="text" icon="Share" @click="openSendForm(scope.row.id)"
                     v-hasPermi="['system:sms-template:send-sms']">测试</el-button>
          <el-button size="small" type="text" icon="Edit" @click="openForm('update', scope.row.id)"
                     v-hasPermi="['system:sms-template:update']">修改</el-button>
          <el-button size="small" type="text" icon="Delete" @click="handleDelete(scope.row.id)"
                     v-hasPermi="['system:sms-template:delete']">删除</el-button>
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

    <!-- 表单弹窗：添加/修改 -->
    <SmsTemplateForm ref="formRef" @success="getList" />
    <!-- 表单弹窗：测试发送 -->
    <SmsTemplateSendForm ref="sendFormRef" />

  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { getIntDictOptions, DICT_TYPE,getDictLabel } from '@/utils/dict'
import { deleteSmsTemplate,getSmsTemplatePage } from '@/api/system/sms/template/index.js'
import { getSimpleSmsChannelList } from '@/api/system/sms/channel/index.js'
import { handleTree } from '@/utils/ruoyi.js'
import SvgIcon from '@/components/SvgIcon/index.vue'
import { SystemMenuTypeEnum, CommonStatusEnum } from '@/utils/constants'
import SmsTemplateForm from './SmsTemplateForm.vue'
import SmsTemplateSendForm from './SmsTemplateSendForm.vue'
import DictTag from '@/components/DictTag/index.vue'
import ELComponent from '@/plugins/modal.js'
import { dateFormatter } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'

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
  type: null,
  status: null,
  code: '',
  content: '',
  apiTemplateId: '',
  channelId: null,
  createTime: []
})
// 导出的加载中
const exportLoading = ref(false)
// 短信渠道列表
const channelList = ref([])

/** 初始化 **/
onMounted(async () => {
  await getList()
  const response = await getSimpleSmsChannelList()
  // 加载渠道列表
  channelList.value = response.data
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getSmsTemplatePage(queryParams)
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

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type, id) => {
  formRef.value.open(type, id)
}

/** 发送短信按钮 */
const sendFormRef = ref()
const openSendForm = (id) => {
  sendFormRef.value.open(id)
}

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteSmsTemplate(id)
    ELComponent.msgSuccess('删除成功！')
    // 刷新列表
    await getList()
  } catch {}
}

</script>

<style scoped>
/*去除操作的换行*/
.el-table .cell {
  padding: 0;
}

.el-table .cell .el-button--small {
  padding: 0;
}
</style>
