<template>
  <div class="app-container">
    <!-- 搜索 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="配置名" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入配置名"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="存储器" prop="storage">
        <el-select
            v-model="queryParams.storage"
            placeholder="请选择存储器"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_FILE_STORAGE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
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
        <el-button type="primary" icon="Search" @click="handleQuery()">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>

      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm('create')"
                   v-hasPermi="['infra:file-config:create']">新增</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="配置名" align="center" prop="name" />
      <el-table-column label="存储器" align="center" prop="storage">
        <template #default="scope">
          <DictTag :type="DICT_TYPE.INFRA_FILE_STORAGE" :value="scope.row.storage" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="主配置" align="center" prop="primary">
        <template #default="scope">
          <DictTag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.master" />
        </template>
      </el-table-column>
      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center" width="240px">
        <template #default="scope">
          <el-button
              link
              size="small"
              icon="Edit"
              type="primary"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['infra:file-config:update']"
          >
            编辑
          </el-button>
          <el-button
              link
              size="small"
              icon="Key"
              type="primary"
              :disabled="scope.row.master"
              @click="handleMaster(scope.row)"
              v-hasPermi="['infra:file-config:update']"
          >
            主配置
          </el-button>
          <el-button
              link
              size="small"
              type="primary"
              icon="Share"
              @click="handleTest(scope.row.id)"
          >
            测试
          </el-button>
          <el-button
              link
              size="small"
              type="danger"
              icon="Delete"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['infra:config:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <Pagination
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 表单弹窗：添加/修改 -->
    <FileConfigForm ref="formRef" @success="getList" />

  </div>
</template>

<script setup>
import FileConfigForm from './FileConfigForm.vue'
import Pagination from '@/components/Pagination/index.vue'
import { ref,reactive,onMounted } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import {
  getFileConfigPage,
  testFileConfig,
  deleteFileConfig,
  updateFileConfigMaster
} from '@/api/infra/fileConfig/index.js'
import DictTag from '@/components/DictTag/index.vue'
import { dateFormatter } from '@/utils/formatTime.js'
import ELComponent from '@/plugins/modal.js'

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
  storage: undefined,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()

const getList = async ()=>{
  loading.value = true
  try {
    const response = await getFileConfigPage(queryParams)
    console.log(response.data)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = async (type,id)=>{
  formRef.value.open(type, id)
}

/** 初始化 */
onMounted(()=>{
  getList()
})

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
/** 主配置按钮操作 */
const handleMaster = async (row) => {
  try {
    await ELComponent.confirm('是否确认修改配置编号为"' + row.name + '"的数据项为主配置?')
    await updateFileConfigMaster(row.id)
    ELComponent.msgSuccess('更新成功')
    await getList()
  } catch {}
}
/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteFileConfig(id)
    ELComponent.msgSuccess('删除成功！')
    // 刷新列表
    await getList()
  } catch {}
}

/** 测试按钮操作 */
const handleTest = async (id) => {
  try {
    const response = await testFileConfig(id)
    ELComponent.alertSuccess('测试通过，上传文件成功！访问地址：' + response.data)
  } catch {}
}

</script>

<style>
/*去除操作的换行*/
.el-table .cell {
  padding: 0;
}
</style>
