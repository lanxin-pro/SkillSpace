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
      <el-form-item label="字典名称" prop="name">
        <el-input
            v-model="queryParams.name"
            class="!w-240px"
            clearable
            placeholder="请输入字典名称"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="字典类型" prop="type">
        <el-input
            v-model="queryParams.type"
            class="!w-240px"
            clearable
            placeholder="请输入字典类型"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
            v-model="queryParams.status"
            class="!w-240px"
            clearable
            placeholder="请选择字典状态"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
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
        <el-button type="primary" icon="Search" @click="handleQuery()">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>

      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm('create')"
                   v-hasPermi="['system:dict:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="Download" size="small" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:dict:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column align="center" label="字典编号" prop="id" />
      <el-table-column align="center" label="字典名称" prop="name" show-overflow-tooltip />
      <el-table-column align="center" label="字典类型" prop="type" width="300" />
      <el-table-column align="center" label="状态" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column
          :formatter="dateFormatter"
          align="center"
          label="创建时间"
          prop="createTime"
          width="180"
      />
      <el-table-column align="center" label="操作">
        <template #default="scope">
          <el-button size="small" type="text" icon="Edit" @click="openForm('update', scope.row.id)"
                     v-hasPermi="['system:dict:update']">修改</el-button>
          <el-button size="small" type="text" icon="Delete" @click="handleDelete(scope.row.id)"
                     v-hasPermi="['system:dict:delete']">删除</el-button>
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
    <DictTypeForm ref="formRef" @success="getList" />
  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import { parseTime } from '@/utils/ruoyi.js'
import { getDictTypePage,deleteDictType } from '@/api/system/dict/dict/index.js'
import DictTag from '@/components/DictTag/index.vue'
import DictTypeForm from './DictTypeForm.vue'
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
  name: '',
  type: '',
  status: undefined,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()
// 导出的加载中
const exportLoading = ref(false)

/** 初始化 */
onMounted(()=>{
  getList()
})
/** 查询全部数据 */
const getList = async ()=>{
  loading.value = true
  try {
    const response = await getDictTypePage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}
/** 添加/修改操作 */
const formRef = ref()
const openForm = (type, id) => {
  formRef.value.open(type, id)
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

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteDictType(id)
    ELComponent.msgSuccess("删除成功！")
    // 刷新列表
    await getList()
  } catch {}
}

</script>

<style>
.el-table .cell {
  padding: 0;
}
</style>
