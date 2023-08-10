<template>
  <div class="app-container">

    <!-- 搜索工作栏 -->
    <el-form
        ref="queryFormRef"
        :inline="true"
        :model="queryParams"
        label-width="68px"
        size="small"
    >
      <el-form-item label="角色名称" prop="name">
        <el-input
            v-model="queryParams.name"
            class="!w-240px"
            clearable
            placeholder="请输入角色名称"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="角色标识" prop="code">
        <el-input
            v-model="queryParams.code"
            class="!w-240px"
            clearable
            placeholder="请输入角色标识"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" class="!w-240px" clearable placeholder="请选择状态">
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
        <el-button type="primary" icon="Search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm('create')"
                   v-hasPermi="['system:role:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="Download" size="small" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:role:export']">导出</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column align="center" label="角色编号" prop="id" />
      <el-table-column align="center" label="角色名称" prop="name" />
      <el-table-column align="center" label="角色类型" prop="type">
        <template v-slot="scope">
          <DictTag :type="DICT_TYPE.SYSTEM_ROLE_TYPE" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="角色标识" prop="code" :show-overflow-tooltip="true" />
      <el-table-column align="center" label="显示顺序" prop="sort" />
      <el-table-column align="center" label="备注" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column align="center" label="状态" prop="status">
        <template #default="scope">
          <DictTag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
          :formatter="dateFormatter"
          align="center"
          label="创建时间"
          prop="createTime"
          width="180"
      />
      <el-table-column :width="300" align="center" label="操作">
        <template #default="scope">
          <el-button size="small" type="text" icon="Edit" @click="openForm('update', scope.row.id)"
                     v-hasPermi="['system:role:update']">修改</el-button>
<!--    TODO j-sentinel 我后端写的是如果是超级管理员的身份，就会获取全部权限，前端如果能修改超级管理员权限就不合理      -->
          <el-button size="small" type="text" icon="Check" @click="openAssignMenuForm(scope.row)"
                     :disabled="scope.row.id === 1"
                     v-hasPermi="['system:permission:assign-role-menu']">菜单权限</el-button>
          <el-button size="small" type="text" icon="Check" @click="openDataPermissionForm(scope.row)"
                     v-hasPermi="['system:permission:assign-role-data-scope']">数据权限</el-button>
          <el-button size="small" type="text" icon="Delete" @click="handleDelete(scope.row.id)"
                     v-hasPermi="['system:role:delete']">删除</el-button>
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
    <RoleForm ref="formRef" @success="getList" />
    <!-- 表单弹窗：菜单权限 -->
    <RoleAssignMenuForm ref="assignMenuFormRef" @success="getList" />
    <!-- 表单弹窗：数据权限 -->
    <RoleDataPermissionForm ref="dataPermissionFormRef" @success="getList" />

  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import RoleForm from './RoleForm.vue'
import RoleAssignMenuForm from './RoleAssignMenuForm.vue'
import RoleDataPermissionForm from './RoleDataPermissionForm.vue'
import { getRolePage,deleteRole } from '@/api/system/role/index.js'
import DictTag from '@/components/DictTag/index.vue'
import Pagination from '@/components/Pagination/index.vue'
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
  code: '',
  name: '',
  status: undefined,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()
// 导出的加载中
const exportLoading = ref(false)

/** 初始化 **/
onMounted(() => {
  getList()
})
/** 查询角色列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getRolePage(queryParams)
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

/** 数据权限操作 */
const dataPermissionFormRef = ref()
const openDataPermissionForm = async (row) => {
  dataPermissionFormRef.value.open(row)
}

/** 菜单权限操作 */
const assignMenuFormRef = ref()
const openAssignMenuForm = async (row) => {
  assignMenuFormRef.value.open(row)
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
    await deleteRole(id)
    ELComponent.msgSuccess('删除成功！')
    // 刷新列表
    await getList()
  } catch {}
}
</script>

<style scoped>
/* TODO j-sentinel 前端的操作以后都这样写可以去除前后padding */
.el-table .cell .el-button--small {
  padding: 0;
}
</style>
