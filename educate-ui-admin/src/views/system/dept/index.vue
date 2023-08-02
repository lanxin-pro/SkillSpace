<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="部门名称" prop="title">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入部门名称"
            clearable
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="部门状态" prop="status">
        <el-select
            v-model="queryParams.status"
            placeholder="请选择部门状态"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery()">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">

      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            size="small"
            @click="openForm('create')"
            v-hasPermi="['system:dept:create']">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="Sort"
            size="small"
            @click="toggleExpandAll()">展开/折叠</el-button>
      </el-col>

    </el-row>


    <!-- 列表 -->
    <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="list"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="name" label="部门名称" width="260"></el-table-column>
      <el-table-column prop="leader" label="负责人" :formatter="userNicknameFormat" width="120"/>
      <el-table-column prop="sort" label="排序" width="200"></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template v-slot="scope">
          <DictTag
              :type="DICT_TYPE.COMMON_STATUS"
              :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="200">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="small" type="text" icon="Edit" @click="openForm('update',scope.row.id)"
                     v-hasPermi="['system:dept:update']">修改</el-button>
          <el-button size="small" type="text" icon="Plus" @click="openForm('create',null,scope.row.id)"
                     v-hasPermi="['system:dept:create']">新增</el-button>
          <el-button v-if="scope.row.parentId !== 0" size="small" type="text" icon="Delete"
                     @click="handleDelete(scope.row)" v-hasPermi="['system:dept:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>


    <!-- 表单弹窗：添加/修改 -->
    <DeptForm ref="formRef" @success="getList" />

  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import DeptForm from './DeptForm.vue'
import { parseTime } from '@/utils/ruoyi.js'
import { getSimpleUserList } from '@/api/system/user.js'
import { getDeptPage,deleteDept } from '@/api/system/dept.js'
import { handleTree } from '@/utils/tree.js'
import DictTag from '@/components/DictTag/index.vue'

// 列表的加载中
const loading = ref(true)
// 列表的数据
const list = ref()
const queryParams = reactive({
  title: '',
  name: undefined,
  status: undefined,
  pageNo: 1,
  pageSize: 100
})
// 搜索的表单
const queryFormRef = ref()
// 是否展开，默认全部展开
const isExpandAll = ref(true)
// 重新渲染表格状态
const refreshTable = ref(true)
// 用户列表
const userList = ref([])

onMounted(async ()=>{
  await getList()
  // 获取用户列表
  const response = await getSimpleUserList()
  userList.value = response.data
})

const getList = async ()=>{
  loading.value = true
  try {
    const response = await getDeptPage(queryParams)
    list.value = handleTree(response.data)
  } finally {
    loading.value = false
  }
}
/** 展开/折叠操作 */
const toggleExpandAll = ()=>{
  refreshTable.value = false
  isExpandAll.value = !isExpandAll.value
  nextTick(() => {
    refreshTable.value = true
  })
}
/** 添加/修改操作 */
const formRef = ref()
const openForm = (type,id, parentId)=>{
  formRef.value.open(type, id, parentId)
}
/** 搜索按钮操作 */
const handleQuery = ()=>{
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  // TODO j-sentinel 不知道为什么queryFormRef.value.resetFields()对name查询没有生效
  queryParams.name = undefined
  queryParams.pageNo = 1
  queryFormRef.value.resetFields()
  handleQuery()
}
/** 删除按钮操作 */
const handleDelete = async (row)=>{
  try {
    // 删除的二次确认
    await ELComponent.confirm(`您确定要删除 ${row.name} 吗？`)
    // 发起删除
    await deleteDept(row.id)
    ELComponent.msgSuccess('删除成功')
    // 刷新列表
    await getList()
  } catch {}
}
/** 用户昵称展示 */
const userNicknameFormat = (row)=>{
  if (!row.leaderUserId) {
    return '未设置'
  }
  for (const user of userList.value) {
    // 没有查询到用户id，全部返回未知
    if (row.leaderUserId === user.id) {
      return user.nickname
    }
  }
  return '未知【' + row.leaderUserId + '】'
}

</script>

<style scoped>

</style>
