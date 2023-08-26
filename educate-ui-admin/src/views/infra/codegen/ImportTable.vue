<template>
  <Dialog v-model="dialogVisible" title="导入表" width="850px">
    <!-- 搜索栏 -->
    <el-form
        ref="queryFormRef"
        :inline="true"
        :model="queryParams"
        label-width="68px"
    >
      <el-form-item label="数据源" prop="dataSourceConfigId">
        <el-select
            v-model="queryParams.dataSourceConfigId"
            class="!w-240px"
            placeholder="请选择数据源"
        >
          <el-option
              v-for="config in dataSourceConfigList"
              :key="config.id"
              :label="config.name"
              :value="config.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="表名称" prop="name">
        <el-input
            v-model="queryParams.name"
            class="!w-240px"
            clearable
            placeholder="请输入表名称"
            @keyup.enter="getList"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="comment">
        <el-input
            v-model="queryParams.comment"
            class="!w-240px"
            clearable
            placeholder="请输入表描述"
            @keyup.enter="getList"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="search" size="small" @click="getList()">搜索</el-button>
        <el-button icon="refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 列表 -->
    <el-row>
      <el-table
          ref="tableRef"
          v-loading="dbTableLoading"
          :data="dbTableList"
          height="360px"
          @row-click="handleRowClick"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column :show-overflow-tooltip="true" label="表名称" prop="name" />
        <el-table-column :show-overflow-tooltip="true" label="表描述" prop="comment" />
      </el-table>
    </el-row>
    <template #footer>
      <el-button :disabled="tableList.length === 0" type="primary" @click="handleImportTable">
        导入
      </el-button>
      <el-button @click="close()">关闭</el-button>
    </template>
  </Dialog>
</template>

<script setup>
import Dialog from '@/components/Dialog/index.vue'
import { ElTable } from 'element-plus'
import { ref,reactive } from 'vue'
import ElComponent from '@/plugins/modal.js'
import { getSchemaTableList,createCodegenList } from '@/api/infra/codegen.js'
import { getDataSourceConfigList } from '@/api/infra/dataSourceConfig.js'

// 表格的 Ref
const tableRef = ref()
// 选中的表名
const tableList = ref([])
// 弹窗的是否展示
const dialogVisible = ref(false)
// 数据源的加载中
const dbTableLoading = ref(true)
// 表的列表
const dbTableList = ref([])
const queryParams = reactive({
  name: undefined,
  comment: undefined,
  dataSourceConfigId: 0
})
// 搜索的表单
const queryFormRef = ref()
// 数据源列表
const dataSourceConfigList = ref([])


/** 打开弹窗 */
const open = async () => {
  // 加载数据源的列表
  const response = await getDataSourceConfigList()
  dataSourceConfigList.value = response.data
  queryParams.dataSourceConfigId = dataSourceConfigList.value[0]?.id
  dialogVisible.value = true
  await getList()
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

const handleSelectionChange = (selection)=>{
  tableList.value = selection.map(item => {
    return item.name
  })
}

/** 查询表数据 */
const getList = async () => {
  dbTableLoading.value = true
  try {
    const response = await getSchemaTableList(queryParams)
    dbTableList.value = response.data
  } finally {
    dbTableLoading.value = false
  }
}

/** 重置操作 */
const resetQuery = async () => {
  queryParams.name = undefined
  queryParams.comment = undefined
  queryParams.dataSourceConfigId = dataSourceConfigList.value[0]?.id
  await getList()
}

/** 导入按钮操作 */
const handleImportTable = async ()=>{
  const response = await createCodegenList({
    dataSourceConfigId: queryParams.dataSourceConfigId,
    tableNames: tableList.value
  })
  ElComponent.msgSuccess("导出成功")
  emit('success')
  close()
}
/** 关闭弹窗 */
const close = ()=>{
  dialogVisible.value = false
  tableList.value = []
}

const emit = defineEmits(['success'])
</script>

<style scoped>

</style>
