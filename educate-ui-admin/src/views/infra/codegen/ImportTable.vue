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
        <el-button type="primary" icon="search" size="small" @click="handleQuery">搜索</el-button>
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

  </Dialog>
</template>

<script setup>
import Dialog from '@/components/Dialog/index.vue'
import { ElTable } from 'element-plus'
import { ref,reactive } from 'vue'
import ElComponent from '@/plugins/modal.js'
import { getSchemaTableList } from '@/api/infra/codegen.js'

const dialogVisible = ref(false) // 弹窗的是否展示
const dbTableLoading = ref(true) // 数据源的加载中
const dbTableList = ref([]) // 表的列表
const queryParams = reactive({
  name: undefined,
  comment: undefined,
  dataSourceConfigId: 0
})
const queryFormRef = ref() // 搜索的表单
const dataSourceConfigList = ref([]) // 数据源列表


/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  await getList()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 查询表数据 */
const getList = async () => {
  dbTableLoading.value = true
  try {
    const response = await getSchemaTableList(queryParams)
    console.log("加载的数据",response)
    dbTableList.value = response.data
  } finally {
    dbTableLoading.value = false
  }
}

</script>

<style scoped>

</style>
