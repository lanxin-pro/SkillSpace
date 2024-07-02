<template>
<div class="app-container">

  <!-- 搜索工作栏 -->
  <el-row :gutter="20">
    <!--部门数据-->
    <el-col :span="4" :xs="24">
      <div class="head-container">
        <el-input
            v-model="deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="Search"
            style="margin-bottom: 20px"/>
      </div>
      <div class="head-container">
        <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"/>
      </div>
    </el-col>
    <!--用户数据-->
    <el-col :span="20" :xs="24">
      <el-form
          :model="queryParams"
          ref="queryFormRef"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
      >
        <el-form-item label="用户名称" prop="username">
          <el-input v-model="queryParams.username" placeholder="请输入用户名称" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item label="手机号码" prop="mobile">
          <el-input v-model="queryParams.mobile" placeholder="请输入手机号码" clearable style="width: 240px"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="用户状态" clearable style="width: 240px">
            <el-option v-for="dict in statusDictDatas"
                       :key="parseInt(dict.value)"
                       :label="dict.label"
                       :value="parseInt(dict.value)"
            />
          </el-select>
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
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
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
              v-hasPermi="['system:user:create']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="warning"
              icon="Upload"
              size="small"
              @click="handleImport"
              v-hasPermi="['system:user:import']">导入</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="success"
              icon="Download"
              size="small"
              @click="handleExport"
              :loading="exportLoading"
              v-hasPermi="['system:user:export']">导出</el-button>
        </el-col>
<!--        <RightToolbar
            :showSearch.sync="showSearch"
            @queryTable="getList"
            :columns="columns"
        />-->
      </el-row>

      <el-table v-loading="loading" :data="userList">
        <el-table-column label="用户编号" align="center" key="id" prop="id" v-if="columns[0].visible" />
        <el-table-column label="用户名称" align="center" key="username" prop="username" v-if="columns[1].visible" :show-overflow-tooltip="true" />
        <el-table-column label="用户昵称" align="center" key="nickname" prop="nickname" v-if="columns[2].visible" :show-overflow-tooltip="true" />
        <el-table-column label="部门" align="center" key="deptName" prop="dept.name" v-if="columns[3].visible" :show-overflow-tooltip="true" />
        <el-table-column label="手机号码" align="center" key="mobile" prop="mobile" v-if="columns[4].visible" width="120" />
        <el-table-column label="状态" key="status" v-if="columns[5].visible" align="center">
          <template v-slot="scope">
            <el-switch
                v-model="scope.row.status"
                :active-value="0"
                :inactive-value="1"
                @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[6].visible" width="160">
          <template v-slot="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
          <template v-slot="scope">
            <el-button
                size="small"
                type="text"
                icon="Edit"
                @click="openForm('update', scope.row.id)">
              修改
            </el-button>
            <el-dropdown
                @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button size="small" type="text" icon="DArrowRight">更多</el-button>
              <template #dropdown>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                      command="handleDelete"
                      v-if="scope.row.id !== 1"
                      size="small" type="text"
                      icon="Delete"
                  >
                    删除
                  </el-dropdown-item>
                  <el-dropdown-item
                      command="handleResetPwd"
                      size="small"
                      type="text"
                      icon="Key"
                  >
                    重置密码
                  </el-dropdown-item>
                  <el-dropdown-item
                      command="handleRole"
                      size="small"
                      type="text"
                      icon="Circle-check"
                  >
                    分配角色
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>

            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <Pagination
          :total="total"
          v-model:page="queryParams.pageNo"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />

    </el-col>
  </el-row>

  <!-- 添加或修改用户对话框 -->
  <UserForm ref="formRef" @success="getList" />
  <!-- 用户导入对话框 -->
  <UserImportForm ref="importFormRef" @success="getList" />
  <!-- 分配角色 -->
  <UserAssignRoleForm ref="assignRoleFormRef" @success="getList" />

</div>
</template>

<script setup>
import {
  listUser, resetUserPwd, updateUserStatus, deleteUser, exportUser
} from "@/api/system/user"
import { listSimpleDepts } from "@/api/system/dept"
import { listSimplePosts } from "@/api/system/post"
import ElComponent from '@/plugins/modal.js'
import { ref,reactive,onMounted } from 'vue'
import { handleTree,parseTime } from '@/utils/ruoyi.js'
import Pagination from '@/components/Pagination/index.vue'
import { useDictStore } from '@/piniastore/modules/dict.js'
import { getIntDictOptions,DICT_TYPE } from '@/utils/dict.js'
import RightToolbar from '@/components/RightToolbar/index.vue'
import UserForm from './UserForm.vue'
import UserImportForm from './UserImportForm.vue'
import UserAssignRoleForm from './UserAssignRoleForm.vue'
import ELComponent from '@/plugins/modal.js'
import { CommonStatusEnum } from '@/utils/constants'
import download from '@/utils/download'

const dictStore = useDictStore()
// 遮罩层
const loading = ref(true)
// 显示搜索条件
const showSearch = ref(true)
// 总条数
const total = ref(0)
// 用户表格数据
const userList = ref(null)
// 弹出层标题
const title = ref('')
// 部门树选项
const deptOptions = ref(undefined)
// 是否显示弹出层
const open = ref(false)
// 部门名称
const deptName = ref(undefined)
// 默认密码
const initPassword = ref(undefined)
// 性别状态字典
const sexOptions = ref([])
// 岗位选项
const postOptions = ref([])
// 角色选项
const roleOptions = ref([])
// 表单参数
const form = ref({})
// tree组件
const defaultProps = reactive({
  children: "children",
  label: "name"
})
// 状态的显示
const statusDictDatas = getIntDictOptions(DICT_TYPE.COMMON_STATUS)
// From的ref
const queryFormRef = ref()
// 用户导入参数
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  // headers: getBaseHeader(),
  // 上传的地址
  // url: process.env.VUE_APP_BASE_API + '/admin-api/system/user/import'
})
// 查询参数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  username: undefined,
  mobile: undefined,
  status: undefined,
  deptId: undefined,
  createTime: []
})
// 列信息
const columns = reactive([
  { key: 0, label: `用户编号`, visible: true },
  { key: 1, label: `用户名称`, visible: true },
  { key: 2, label: `用户昵称`, visible: true },
  { key: 3, label: `部门`, visible: true },
  { key: 4, label: `手机号码`, visible: true },
  { key: 5, label: `状态`, visible: true },
  { key: 6, label: `创建时间`, visible: true }
])
// 表单校验
const rules = reactive({
  username: [
    { required: true, message: "用户名称不能为空", trigger: "blur" }
  ],
  nickname: [
    { required: true, message: "用户昵称不能为空", trigger: "blur" }
  ],
  password: [
    { required: true, message: "用户密码不能为空", trigger: "blur" }
  ],
  email: [
    {
      type: "email",
      message: "'请输入正确的邮箱地址",
      trigger: ["blur", "change"]
    }
  ],
  mobile: [
    {
      pattern: /^(?:(?:\+|00)86)?1(?:3[\d]|4[5-79]|5[0-35-9]|6[5-7]|7[0-8]|8[\d]|9[189])\d{8}$/,
      message: "请输入正确的手机号码",
      trigger: "blur"
    }
  ]
})
// 是否显示弹出层（角色权限）
const openRole = ref(false)


onMounted(()=>{
  getList()
  getTreeSelect()
})
/** 添加/修改操作 */
const formRef = ref()
const openForm = (type, id) => {
  formRef.value.open(type, id)
}

/** 搜索按钮操作 */
const handleQuery = ()=>{
  queryParams.pageNo = 1
  getList()
}
/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

/** 用户导入 */
const importFormRef = ref()
const handleImport = () => {
  importFormRef.value.open()
}
/** 导出按钮操作 */
const exportLoading = ref(false)
const handleExport = async () => {
  try {
    // 导出的二次确认
    await ELComponent.confirm('是否确认导出所有用户数据项?')
    // 发起导出
    exportLoading.value = true
    const data = await exportUser(queryParams)
    download.excel(data, '用户数据.xlsx')
  } catch {
  } finally {
    exportLoading.value = false
  }
}
/** 修改用户状态 */
const handleStatusChange = async (row) => {
  try {
    // 修改状态的二次确认
    const text = row.status === CommonStatusEnum.ENABLE ? '启用' : '停用'
    await ELComponent.confirm('确认要"' + text + '""' + row.username + '"用户吗?')
    // 发起修改状态
    await updateUserStatus(row.id, row.status)
    // 刷新列表
    await getList()
  } catch {
    // 取消后，进行恢复按钮，取反
    row.status =
        row.status === CommonStatusEnum.ENABLE ? CommonStatusEnum.DISABLE : CommonStatusEnum.ENABLE
  }
}
// 筛选节点
const filterNode = (value, data)=> {
  if (!value){
    return true
  }
  return data.name.indexOf(value) !== -1
}

// 节点单击事件
const handleNodeClick = (data)=> {
  queryParams.deptId = data.id
  getList()
}

const getList = async ()=>{
  try {
    loading.value = true
    const response = await listUser(queryParams)
    userList.value = response.data.list
    total.value = response.data.total
  }catch (error){
    ElComponent.msgError("发生错误"+error)
  }finally {
    loading.value = false
  }
}

/**
 * 查询部门下拉树结构 + 岗位下拉
 * @returns {Promise<void>}
 */
const getTreeSelect = async ()=>{
  try{
    // 部门
    const responseListSimpleDepts = await listSimpleDepts()
    // 处理 deptOptions 参数
    deptOptions.value = []
    deptOptions.value.push(...handleTree(responseListSimpleDepts.data,"id"))

    // 岗位
    const responseListSimplePosts = await listSimplePosts()
    // 处理 postOptions 参数
    postOptions.value = []
    postOptions.value.push(...responseListSimplePosts.data)
  }catch (error){
    ElComponent.msgError("树形菜单发生错误"+error)
  }

}


/** 操作分发 */
const handleCommand = (command, row) => {
  console.log(command)
  console.log(row)
  switch (command) {
    case 'handleDelete':
      handleDelete(row.id)
      break
    case 'handleResetPwd':
      handleResetPwd(row)
      break
    case 'handleRole':
      handleRole(row)
      break
    default:
      break
  }
}

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm(`是否确认删除用户编号${id}为的数据项?`)
    // 发起删除
    await deleteUser(id)
    ELComponent.msgSuccess('删除成功')
    // 刷新列表
    await getList()
  } catch {}
}

/** 重置密码 */
const handleResetPwd = async (row) => {
  try {
    // 重置的二次确认
    const result = await ELComponent.prompt(
        '请输入"' + row.username + '"的新密码',
        '温馨提示'
    )
    const password = result.value
    // 发起重置
    await resetUserPwd(row.id, password)
    ELComponent.msgSuccess('修改成功，新密码是：' + password)
  } catch {}
}

/** 分配角色 */
const assignRoleFormRef = ref()
const handleRole = (row) => {
  assignRoleFormRef.value.open(row)
}


</script>

<style scoped>

</style>
