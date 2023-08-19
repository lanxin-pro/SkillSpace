<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        ref="queryFormRef"
        :inline="true"
        :model="queryParams"
        class="-mb-15px"
        label-width="100px"
        size="small"
    >
      <el-form-item label="接收邮箱" prop="toMail">
        <el-input
            v-model="queryParams.toMail"
            class="!w-240px"
            placeholder="请输入接收邮箱"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱账号" prop="accountId">
        <el-select
            v-model="queryParams.accountId"
            class="!w-240px"
            placeholder="请输入邮箱账号"
            clearable
        >
          <el-option
              v-for="account in accountOptions"
              :key="account.id"
              :value="account.id"
              :label="account.mail"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模板编号" prop="templateId">
        <el-input
            v-model="queryParams.templateId"
            class="!w-240px"
            placeholder="请输入模板编号"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发送状态" prop="sendStatus">
        <el-select
            v-model="queryParams.sendStatus"
            class="!w-240px"
            placeholder="请选择发送状态"
            clearable
            size="small"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_MAIL_SEND_STATUS)"
                     :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="发送时间" prop="sendTime">
        <el-date-picker
            v-model="queryParams.createTime"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-240px"
            end-placeholder="结束日期"
            start-placeholder="开始日期"
            type="daterange"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="用户编号" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户编号"
            class="!w-240px"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select
            v-model="queryParams.userType"
            placeholder="请选择用户类型"
            class="!w-240px"
            clearable
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.USER_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
            type="primary"
            icon="Search"
            @click="handleQuery"
        >
          搜索
        </el-button>
        <el-button
            icon="Refresh"
            @click="resetQuery"
        >
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button type="warning" icon="Download" size="small" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:dict:export']">导出</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.sendTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="接收邮箱" align="center" prop="toMail" width="200">
        <template v-slot="scope">
          <div>{{ scope.row.toMail }}</div>
          <div v-if="scope.row.userType && scope.row.userId">
            <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType"/>{{ '(' + scope.row.userId + ')' }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="邮件标题" align="center" prop="templateTitle" />
      <el-table-column label="发送状态" align="center" prop="sendStatus">
        <template v-slot="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_MAIL_SEND_STATUS" :value="scope.row.sendStatus" />
        </template>
      </el-table-column>
      <el-table-column label="邮箱账号" align="center" prop="fromMail" />
      <el-table-column label="模板编号" align="center" prop="templateId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="small" type="text" icon="el-icon-view" @click="openDetail(scope.row)"
                     v-hasPermi="['system:mail-log:query']">详细</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
        v-model:limit="queryParams.pageSize"
        v-model:page="queryParams.pageNo"
        :total="total"
        @pagination="getList"
    />

    <!-- 表单弹窗：添加/修改 -->
    <MailLogDetails ref="formRef" @success="getList" />
  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import { parseTime } from '@/utils/ruoyi.js'
import { getSimpleMailAccountList } from '@/api/system/mail/account/index.js'
import { getMailLogPage } from '@/api/system/mail/log/index.js'
import DictTag from '@/components/DictTag/index.vue'
import MailLogDetails from './MailLogDetails.vue'
import { dateFormatter } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 字典表格数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 20,
  userId: null,
  userType: null,
  toMail: null,
  accountId: null,
  templateId: null,
  sendStatus: null,
  sendTime: [],
})
// 搜索的表单
const queryFormRef = ref()
// 导出的加载中
const exportLoading = ref(false)
// 邮箱账号
const accountOptions = ref()

/** 初始化 */
onMounted(async ()=>{
  getList()
  // 获得邮箱账号列表
  const response = await getSimpleMailAccountList()
  accountOptions.value = response.data
})
/** 查询全部数据 */
const getList = async ()=>{
  loading.value = true
  try {
    const response = await getMailLogPage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 添加/修改操作 */
const formRef = ref()
const openDetail = (data) => {
  formRef.value.open(data)
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

</script>

<style scoped>
.el-table .cell {
  padding: 0;
}
.el-table .cell .el-button--small {
  padding: 2px;
}
</style>
