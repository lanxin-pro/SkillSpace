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
      <el-form-item label="异常时间" prop="exceptionTime">
        <el-date-picker
            v-model="queryParams.exceptionTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="处理状态" prop="processStatus">
        <el-select
            v-model="queryParams.processStatus"
            placeholder="请选择处理状态"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_API_ERROR_LOG_PROCESS_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
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
            @click="handleExport"
            :loading="exportLoading"
            v-hasPermi="['infra:api-error-log:export']">导出</el-button>
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
      <el-table-column label="应用名" align="center" prop="applicationName" width="140" />
      <el-table-column label="请求方法" align="center" prop="requestMethod" width="65" />
      <el-table-column label="请求地址" align="center" prop="requestUrl" width="175" />
      <el-table-column
          label="异常发生时间"
          align="center"
          prop="exceptionTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="异常名" align="center" prop="exceptionName" width="180" />
      <el-table-column label="处理状态" align="center" prop="processStatus">
        <template #default="scope">
          <dict-tag
              :type="DICT_TYPE.INFRA_API_ERROR_LOG_PROCESS_STATUS"
              :value="scope.row.processStatus"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template #default="scope">
          <el-button
              link
              size="small"
              icon="View"
              type="primary"
              @click="openDetail(scope.row)"
              v-hasPermi="['infra:api-error-log:query']"
          >
            详细
          </el-button>
          <el-button
              type="text"
              size="small"
              icon="Check"
              v-if="scope.row.processStatus === InfraApiErrorLogProcessStatusEnum.INIT"
              v-hasPermi="['infra:api-error-log:update-status']"
              @click="handleProcess(scope.row.id, InfraApiErrorLogProcessStatusEnum.DONE)">
            已处理
          </el-button>
          <el-button
              type="text"
              size="small"
              icon="Check"
              v-if="scope.row.processStatus === InfraApiErrorLogProcessStatusEnum.INIT"
              v-hasPermi="['infra:api-error-log:update-status']"
              @click="handleProcess(scope.row.id, InfraApiErrorLogProcessStatusEnum.IGNORE)">
            已忽略
          </el-button>
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
    <ApiErrorLogDetail ref="detailRef" />
  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import ApiErrorLogDetail from './ApiErrorLogDetail.vue'
import { parseTime } from '@/utils/ruoyi.js'
import { getApiErrorLogPage,exportApiErrorLogExcel,updateApiErrorLogProcess } from '@/api/infra/apiErrorLog/index.js'
import { handleTree } from '@/utils/tree.js'
import DictTag from '@/components/DictTag/index.vue'
import Pagination from '@/components/Pagination/index.vue'
import download from '@/utils/download.js'
import { InfraApiErrorLogProcessStatusEnum } from '@/utils/constants.js'
import { dateFormatter } from '@/utils/formatTime.js'

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
  processStatus: null,
  exceptionTime: []
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
    const response = await getApiErrorLogPage(queryParams)
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

/** 处理已处理 / 已忽略的操作 **/
const handleProcess = async (id, processStatus) => {
  try {
    // 操作的二次确认
    const type = processStatus === InfraApiErrorLogProcessStatusEnum.DONE ? '已处理' : '已忽略'
    await ELComponent.confirm('确认标记为' + type + '?')
    // 执行操作
    await updateApiErrorLogProcess(id, processStatus)
    await ELComponent.msgSuccess(type)
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await ELComponent.confirm('您确定要导出吗？')
    // 发起导出
    exportLoading.value = true
    const data = await exportApiErrorLogExcel(queryParams)
    download.excel(data, '异常日志.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

</script>

<style scoped>
.el-table .cell {
  padding: 0;
}
.el-table .cell .el-button--small {
  padding: 2px;
}
.el-table .cell .el-button+.el-button{
  margin-left: 4px;
}

</style>
