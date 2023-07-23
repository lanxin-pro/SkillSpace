<template>
  <div class="app-container">
    <!-- 操作工作栏 -->
    <el-form
        :model="queryParams"
        ref="queryFormRef"
        size="small"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
    >
      <el-form-item label="表名称" prop="tableName">
        <el-input v-model="queryParams.tableName" placeholder="请输入表名称" clearable
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input v-model="queryParams.tableComment" placeholder="请输入表描述" clearable
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
            v-model="queryParams.createTime"
            style="width: 240px"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="search" @click="handleQuery">搜索</el-button>
        <el-button icon="refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 操作工作栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="info" plain icon="upload" size="small" @click="openImportTable"
                   v-hasPermi="['infra:codegen:create']">导入</el-button>
      </el-col>
<!--      <RightToolbar :showSearch.sync="showSearch" @queryTable="getList"></RightToolbar>-->
    </el-row>


    <!-- 列表 -->
    <el-table v-loading="loading" :data="tableList">
      <el-table-column label="数据源" align="center" >
        <template #default="scope">
          {{
            dataSourceConfigList.find((config) => config.id === scope.row.dataSourceConfigId)?.name
          }}
        </template>
      </el-table-column>
      <el-table-column label="表名称" align="center" prop="tableName" width="200"/>
      <el-table-column label="表描述" align="center" prop="tableComment" :show-overflow-tooltip="true" width="120"/>
      <el-table-column label="实体" align="center" prop="className" width="200"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="300px">
        <template #default="scope">
          <el-button
              icon="view"
              size="small"
              v-hasPermi="['infra:codegen:preview']"
              link
              type="primary"
              @click="handlePreview(scope.row)"
          >预览</el-button>
          <el-button
              icon="edit"
              size="small"
              v-hasPermi="['infra:codegen:update']"
              link
              type="primary"
              @click="handleUpdate(scope.row.id)"
          >编辑</el-button>
          <el-button
              icon="delete"
              size="small"
              v-hasPermi="['infra:codegen:delete']"
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
          >删除</el-button>
          <el-button
              icon="refresh"
              size="small"
              v-hasPermi="['infra:codegen:update']"
              link
              type="primary"
              @click="handleSyncDB(scope.row)"
          >同步</el-button>
          <el-button
              icon="download"
              size="small"
              v-hasPermi="['infra:codegen:download']"
              link
              type="primary"
              @click="handleGenTable(scope.row)"
          >生成代码</el-button>
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

    <!-- 弹窗：导入表 -->
    <ImportTable ref="importRef" @success="getList" />

  </div>
</template>

<script setup>
import RightToolbar from "@/components/RightToolbar/index.vue"
import { ref,reactive,computed,onMounted } from 'vue'
import { parseTime } from '@/utils/ruoyi.js'
import Pagination from '@/components/Pagination/index.vue'
import { getCodegenTablePage } from '@/api/infra/codegen.js'
import { getDataSourceConfigList } from '@/api/infra/dataSourceConfig.js'
import ImportTable from './ImportTable.vue'

const dataSourceConfigList = ref([]) // 数据源列表
const queryFormRef = ref()
// 遮罩层
const loading = ref(true)
// 唯一标识符
const uniqueId = ref("")
// 选中表数组
const tableNames = ref([])
// 显示搜索条件
const showSearch = ref(true)
// 总条数
const total = ref(0)
// 表数据
const tableList = ref([])
// 日期范围
const dateRange = ref("")
// 查询参数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  tableName: undefined,
  tableComment: undefined,
  createTime: []
})
// 预览参数
const preview = reactive({
  open: false,
  title: "代码预览",
  fileTree: [],
  data: {},
  activeName: "",
})
// 数据源列表
const dataSourceConfigs = ref([])

onMounted(async ()=>{
  getList()
  // 加载数据源列表
  dataSourceConfigList.value = await getDataSourceConfigList()
})

const getList = async ()=>{
  loading.value = true
  try {
    const response = await getCodegenTablePage(queryParams)
    tableList.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/**
 * 导入操作
 */
const importRef = ref()
const openImportTable = () => {
  importRef.value.open()
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

<style>
.el-table .cell {

  padding: 0;
}
</style>
