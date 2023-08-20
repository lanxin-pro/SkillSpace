<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        ref="queryFormRef"
        :inline="true"
        :model="queryParams"
        class="-mb-15px"
        label-width="68px"
        size="small"
    >
      <el-form-item label="敏感词" prop="name">
        <el-input
            v-model="queryParams.name"
            class="!w-240px"
            clearable
            placeholder="请输入敏感词"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签" prop="tag">
        <el-select
            v-model="queryParams.tag"
            class="!w-240px"
            clearable
            placeholder="请选择标签"
            @keyup.enter="handleQuery"
        >
          <el-option v-for="tag in tagList" :key="tag" :label="tag" :value="tag" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择启用状态">
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
              class="!w-240px"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
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
      <el-form-item>
        <el-button
            type="primary"
            icon="Search"
            @click="handleQuery"
        >搜索</el-button>
        <el-button
            icon="Refresh"
            @click="resetQuery"
        >重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm('create')"
                   v-hasPermi="['system:sensitive-word:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" size="small" @click="handleExport"
                   :loading="exportLoading" v-hasPermi="['system:sensitive-word:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Checked" size="small" @click="openTestForm">测试</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column align="center" label="编号" prop="id" />
      <el-table-column align="center" label="敏感词" prop="name" />
      <el-table-column align="center" label="状态" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="描述" prop="description" />
      <el-table-column align="center" label="标签" prop="tags">
        <template #default="scope">
          <el-tag
              v-for="tag in scope.row.tags"
              :key="tag"
              :disable-transitions="true"
              class="mr-5px"
          >
            {{ tag }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          :formatter="dateFormatter"
          align="center"
          label="创建时间"
          prop="createTime"
          width="180"
      />
      <el-table-column align="center" label="操作">
        <template #default="scope">
          <el-button
              size="small"
              type="text"
              icon="Edit"
              @click="openForm('update',scope.row.id)"
              v-hasPermi="['system:sensitive-word:update']">修改
          </el-button>
          <el-button
              size="small"
              type="text"
              icon="Delete"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['system:sensitive-word:delete']">删除
          </el-button>
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
    <SensitiveWordForm ref="formRef" @success="getList" />

    <!-- 表单弹窗：测试敏感词 -->
    <SensitiveWordTestForm ref="testFormRef" />

  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import { parseTime } from '@/utils/ruoyi.js'
import { getSensitiveWordPage,deleteSensitiveWord,getSensitiveWordTagList,exportSensitiveWordExcel } from '@/api/system/sensitiveWord/index.js'
import DictTag from '@/components/DictTag/index.vue'
import SensitiveWordForm from './SensitiveWordForm.vue'
import SensitiveWordTestForm from './SensitiveWordTestForm.vue'
import { dateFormatter } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'
import download from '@/utils/download.js'

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 20,
  name: undefined,
  tag: undefined,
  status: undefined,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()
// 导出的加载中
const exportLoading = ref(false)
// 标签数组
const tagList = ref([])

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getSensitiveWordPage(queryParams)
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

/** 测试敏感词按钮操作 */
const testFormRef = ref()
const openTestForm = () => {
  testFormRef.value.open()
}

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteSensitiveWord(id)
    ELComponent.msgSuccess('删除成功！')
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
    const response = await exportSensitiveWord(queryParams)
    download.excel(response.data, '敏感词.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 获得 Tag 标签列表
  const response = await getSensitiveWordTagList()
  tagList.value = response.data
})
</script>

<style scoped>
.el-table .cell {
  padding: 0;
}
.el-table .cell .el-button--small {
  padding: 2px;
}
</style>
