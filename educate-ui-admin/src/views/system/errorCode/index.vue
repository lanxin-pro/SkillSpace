<template>
  <div class="app-container">
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="90px"
        size="small"
    >
      <el-form-item label="错误码类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择错误码类型" clearable>
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_ERROR_CODE_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
              class="!w-240px"
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
      <el-form-item label="错误码编码" prop="code">
        <el-input
            v-model="queryParams.code"
            placeholder="请输入错误码编码"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="错误码提示" prop="message">
        <el-input
            v-model="queryParams.message"
            placeholder="请输入错误码提示"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
            v-model="queryParams.createTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" type="primary" icon="Search">搜索</el-button>
        <el-button @click="resetQuery" icon="Refresh"> 重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            size="small"
            @click="openForm('create')"
            v-hasPermi="['system:error-code:create']"
        >
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            size="small" @click="handleExport"
            :loading="exportLoading"
            v-hasPermi="['system:error-code:export']"
        >
          导出
        </el-button>
      </el-col>

    </el-row>


    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="类型" align="center" prop="type" width="80">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_ERROR_CODE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="应用名" align="center" prop="applicationName" width="180" />
      <el-table-column label="错误码编码" align="center" prop="code" width="120" />
      <el-table-column label="错误码提示" align="center" prop="message" width="280" />
      <el-table-column label="备注" align="center" prop="memo" width="180" />
      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
              size="small"
              type="text"
              icon="Edit"
              @click="openForm('update',scope.row.id)"
              v-hasPermi="['system:error-code:update']"
          >
            修改
          </el-button>
          <el-button
              size="small"
              type="text"
              icon="Delete"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['system:error-code:delete']"
          >
            删除
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

    <!-- 表单弹窗：添加/修改 -->
    <ErrorCodeForm ref="formRef" @success="getList" />
  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import { parseTime } from '@/utils/ruoyi.js'
import { getErrorCodePage,deleteErrorCode } from '@/api/system/errorCode/index.js'
import DictTag from '@/components/DictTag/index.vue'
import ErrorCodeForm from './ErrorCodeForm.vue'
import { dateFormatter } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'
import download from '@/utils/download.js'

// 遮罩层
const loading = ref(true)
// 导出遮罩层
const exportLoading = ref(false)
// 总条数
const total = ref(0)
// 错误码列表
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 20,
  type: undefined,
  applicationName: undefined,
  code: undefined,
  message: undefined,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()

/** 初始化 **/
onMounted(() => {
  getList()
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getErrorCodePage(queryParams)
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

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    await deleteErrorCode(id)
    ELComponent.msgSuccess('删除成功！')
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await ELComponent.confirm('您确定要导出全部数据吗？')
    // 发起导出
    exportLoading.value = true
    const data = await excelErrorCode(queryParams)
    download.excel(data, '错误码.xls')
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
</style>
