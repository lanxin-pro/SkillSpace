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
      <el-form-item label="任务名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入任务名称"
            clearable
            @keyup.enter="handleQuery"
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
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_JOB_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="处理器的名字" prop="handlerName">
        <el-input
            v-model="queryParams.handlerName"
            placeholder="请输入处理器的名字"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button
            @click="handleQuery"
            icon="Search"
            type="success"
        >
          搜索
        </el-button>
        <el-button
            @click="resetQuery"
            icon="Refresh"
            typoe="info"
        >
          重置
        </el-button>
        <el-button
            type="primary"
            plain
            @click="openForm('create')"
            v-hasPermi="['infra:job:create']"
            icon="Plus"
        >
          新增
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
        <el-button
            type="info"
            plain
            @click="handleJobLog"
            v-hasPermi="['infra:job:query']"
            icon="zoom-in"
        >
          执行日志
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <el-table class="mt10" v-loading="loading" :data="list">
      <el-table-column label="任务编号" align="center" prop="id" />
      <el-table-column label="任务名称" align="center" prop="name" />
      <el-table-column label="任务状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_JOB_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="处理器的名字" align="center" prop="handlerName" />
      <el-table-column label="处理器的参数" align="center" prop="handlerParam" />
      <el-table-column label="CRON 表达式" align="center" prop="cronExpression" />
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button
              type="primary"
              link
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['infra:job:update']"
              size="small"
              icon="Edit"
          >
            修改
          </el-button>
          <el-button
              type="primary"
              link
              @click="handleChangeStatus(scope.row)"
              v-hasPermi="['infra:job:update']"
              size="small"
              :icon="scope.row.status === InfraJobStatusEnum.STOP ? 'Unlock' : 'Lock'"
          >
            {{ scope.row.status === InfraJobStatusEnum.STOP ? '开启' : '暂停' }}
          </el-button>
          <el-button
              type="danger"
              link
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['infra:job:delete']"
              icon="delete"
              size="small"
          >
            删除
          </el-button>
          <el-dropdown
              class="pt5"
              @command="(command) => handleCommand(command, scope.row)"
              v-hasPermi="['infra:job:trigger', 'infra:job:query']"
          >
            <el-button type="primary"  size="small" icon="d-arrow-right" link>更多</el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                    command="handleRun"
                    v-if="checkPermi(['infra:job:trigger'])"
                >
                  执行一次
                </el-dropdown-item>
                <el-dropdown-item
                    command="openDetail"
                    v-if="checkPermi(['infra:job:query'])"
                >
                  任务详细
                </el-dropdown-item>
                <el-dropdown-item
                    command="handleJobLog"
                    v-if="checkPermi(['infra:job:query'])"
                >
                  调度日志
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
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

    <!-- 表单弹窗：添加/修改 -->
    <JobForm ref="formRef" @success="getList" />
    <!-- 表单弹窗：查看 -->
    <JobDetail ref="detailRef" />

  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import JobDetail from './JobDetail.vue'
import JobForm from './JobForm.vue'
import { parseTime } from '@/utils/ruoyi.js'
import { getJobPage } from '@/api/infra/job/index.js'
import DictTag from '@/components/DictTag/index.vue'
import download from '@/utils/download.js'
import { formatDate } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'
import { InfraJobStatusEnum } from '@/utils/constants.js'
import { checkPermi } from '@/utils/permission.js'
import { useRouter } from 'vue-router'

// 路由
const { push } = useRouter()
// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: undefined,
  status: undefined,
  handlerName: undefined
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
    const response = await getJobPage(queryParams)
    console.log(response.data)
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

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await ELComponent.confirm('您确定要导出吗？')
    // 发起导出
    exportLoading.value = true
    const data = await exportJob(queryParams)
    download.excel(data, '定时任务.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type, id) => {
  formRef.value.open(type, id)
}

/** 修改状态操作 */
const handleChangeStatus = async (row) => {
  try {
    // 修改状态的二次确认
    const text = row.status === InfraJobStatusEnum.STOP ? '开启' : '关闭'
    await message.confirm(
        '确认要' + text + '定时任务编号为"' + row.id + '"的数据项?',
        '温馨提示'
    )
    const status =
        row.status === InfraJobStatusEnum.STOP ? InfraJobStatusEnum.NORMAL : InfraJobStatusEnum.STOP
    await updateJobStatus(row.id, status)
    message.success(text + '成功')
    // 刷新列表
    await getList()
  } catch {
    // 取消后，进行恢复按钮
    row.status =
        row.status === InfraJobStatusEnum.NORMAL ? InfraJobStatusEnum.STOP : InfraJobStatusEnum.NORMAL
  }
}

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteJob(id)
    ELComponent.success('删除成功！')
    // 刷新列表
    await getList()
  } catch {}
}

/** '更多'操作按钮 */
const handleCommand = (command, row) => {
  switch (command) {
    case 'handleRun':
      handleRun(row)
      break
    case 'openDetail':
      openDetail(row.id)
      break
    case 'handleJobLog':
      handleJobLog(row?.id)
      break
    default:
      break
  }
}

/** 执行一次 */
const handleRun = async (row) => {
  try {
    // 二次确认
    await ELComponent.confirm('确认要立即执行一次' + row.name + '?', '温馨提示')
    // 提交执行
    await runJob(row.id)
    message.success('执行成功')
    // 刷新列表
    await getList()
  } catch {}
}

/** 查看操作 */
const detailRef = ref()
const openDetail = (id) => {
  detailRef.value.open(id)
}

/** 跳转执行日志 */
const handleJobLog = (id) => {
  if (id > 0) {
    push('/job/job-log?id=' + id)
  } else {
    push('/job/job-log')
  }
}

</script>

<style scoped>
/*去除操作的换行*/
.el-table .cell {
  padding: 0;
}
</style>
