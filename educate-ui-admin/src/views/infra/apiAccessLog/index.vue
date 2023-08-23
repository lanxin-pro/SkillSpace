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
      <el-form-item label="用户编号" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户编号"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select
            v-model="queryParams.userType"
            placeholder="请选择用户类型"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.USER_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="应用名" prop="applicationName">
        <el-input
            v-model="queryParams.applicationName"
            placeholder="请输入应用名"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="请求时间" prop="beginTime">
        <el-date-picker
            v-model="queryParams.beginTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="执行时长" prop="duration">
        <el-input
            v-model="queryParams.duration"
            placeholder="请输入执行时长"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="结果码" prop="resultCode">
        <el-input
            v-model="queryParams.resultCode"
            placeholder="请输入结果码"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
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

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            size="small"
            :loading="exportLoading"
            @click="handleExport"
            v-hasPermi="['infra:api-access-log:export']">导出</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="日志编号" align="center" prop="id" />
      <el-table-column label="用户编号" align="center" prop="userId" />
      <el-table-column label="用户类型" align="center" prop="userType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <el-table-column label="应用名" align="center" prop="applicationName" :show-overflow-tooltip="true" />
      <el-table-column label="请求方法" align="center" prop="requestMethod" width="80" />
      <el-table-column label="请求地址" align="center" prop="requestUrl" width="240" :show-overflow-tooltip="true" />
      <el-table-column label="请求时间" align="center" prop="beginTime" width="160">
        <template #default="scope">
          <span>{{ formatDate(scope.row.beginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="执行时长" align="center" prop="duration" width="90">
        <template #default="scope"> {{ scope.row.duration }} ms </template>
      </el-table-column>
      <el-table-column label="操作结果" align="center" prop="status" :show-overflow-tooltip="true">
        <template #default="scope">
          {{ scope.row.resultCode === 0 ? '成功' : '失败(' + scope.row.resultMsg + ')' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
              size="small"
              type="text"
              icon="View"
              @click="openDetail(scope.row)"
              v-hasPermi="['system:sms-log:query']">详细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <Pagination
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 表单弹窗：详情 -->
    <ApiAccessLogDetail ref="detailRef" />
  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import ApiAccessLogDetail from './ApiAccessLogDetail.vue'
import { parseTime } from '@/utils/ruoyi.js'
import { getApiAccessLogPage,exportApiAccessLogExcel } from '@/api/infra/apiAccessLog/index.js'
import DictTag from '@/components/DictTag/index.vue'
import download from '@/utils/download.js'
import { formatDate } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  userId: null,
  userType: null,
  applicationName: null,
  requestUrl: null,
  duration: null,
  resultCode: null,
  beginTime: []
})
// 搜索的表单
const queryFormRef = ref()
// 导出的加载中
const exportLoading = ref(false)

/** 初始化 **/
onMounted(() => {
  getList()
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getApiAccessLogPage(queryParams)
    console.log("数据",response)
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

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await ELComponent.confirm('您确定要导出吗？')
    // 发起导出
    exportLoading.value = true
    const response = await exportApiAccessLogExcel(queryParams)
    download.excel(response.data, 'API 访问日志.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

</script>

<style scoped>

</style>
