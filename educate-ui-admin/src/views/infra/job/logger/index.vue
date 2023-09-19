<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="120px"
        size="small"
    >
      <el-form-item label="处理器的名字" prop="handlerName">
        <el-input
            v-model="queryParams.handlerName"
            placeholder="请输入处理器的名字"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="开始执行时间" prop="beginTime">
        <el-date-picker
            v-model="queryParams.beginTime"
            type="date"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择开始执行时间"
            clearable
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="结束执行时间" prop="endTime">
        <el-date-picker
            v-model="queryParams.endTime"
            type="date"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择结束执行时间"
            clearable
            :default-time="new Date('1 23:59:59')"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select
            v-model="queryParams.status"
            placeholder="请选择任务状态"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_JOB_LOG_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
            @click="handleQuery"
            type="primary"
            icon="Search"
        >
          搜索
        </el-button>
        <el-button
            @click="resetQuery"
            icon="Refresh"
            type="info"
        >
          重置
        </el-button>
        <el-button
            type="success"
            plain
            @click="handleExport"
            :loading="exportLoading"
            v-hasPermi="['infra:job:export']"
            icon="Download"
        >
          导出
        </el-button>

      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 -mb-15px">
      <el-col :span="1.5">
        <el-button
            @click="back()"
            icon="back"
            type="warning"
            size="small"
        >
          返回
        </el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list" class="mt10">
      <el-table-column label="日志编号" align="center" prop="id" />
      <el-table-column label="任务编号" align="center" prop="jobId" />
      <el-table-column label="处理器的名字" align="center" prop="handlerName" />
      <el-table-column label="处理器的参数" align="center" prop="handlerParam" />
      <el-table-column label="第几次执行" align="center" prop="executeIndex" />
      <el-table-column label="执行时间" align="center" width="170s">
        <template #default="scope">
          <span>{{ formatDate(scope.row.beginTime) + ' ~ ' + formatDate(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="执行时长" align="center" prop="duration">
        <template #default="scope">
          <span>{{ scope.row.duration + ' 毫秒' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_JOB_LOG_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
              type="primary"
              link
              @click="openDetail(scope.row.id)"
              v-hasPermi="['infra:job:query']"
          >
            详细
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

    <!-- 表单弹窗：查看 -->
    <JobLogDetail ref="detailRef" />

  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import JobLogDetail from './JobLogDetail.vue'
import { parseTime } from '@/utils/ruoyi.js'
import { getJobLogPage,exportJobLogExcel } from '@/api/infra/jobLog/index.js'
import DictTag from '@/components/DictTag/index.vue'
import download from '@/utils/download.js'
import { formatDate } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'
import { InfraJobStatusEnum } from '@/utils/constants.js'
import { checkPermi } from '@/utils/permission.js'
import { useRoute,useRouter } from 'vue-router'

// 查询参数
const { query } = useRoute()
const { push,back } = useRouter()
// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  jobId: query.id,
  handlerName: undefined,
  beginTime: undefined,
  endTime: undefined,
  status: undefined
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
    const response = await getJobLogPage(queryParams)
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

/** 查看操作 */
const detailRef = ref()
const openDetail = (rowId) => {
  detailRef.value.open(rowId)
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await ELComponent.confirm('您确定要导出数据吗？')
    // 发起导出
    exportLoading.value = true
    const data = await exportJobLogExcel(queryParams)
    download.excel(data, '定时任务执行日志.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

</script>

<style scoped>

</style>
