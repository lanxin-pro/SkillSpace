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
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
      >

        <el-table v-loading="loading" :data="userList">
          <el-table-column label="用户编号" align="center" key="id" prop="id" v-if="columns[0].visible" />
          <el-table-column label="用户名称" align="center" key="username" prop="username" v-if="columns[1].visible" :show-overflow-tooltip="true" />
          <el-table-column label="用户昵称" align="center" key="nickname" prop="nickname" v-if="columns[2].visible" :show-overflow-tooltip="true" />
          <el-table-column label="部门" align="center" key="deptName" prop="dept.name" v-if="columns[3].visible" :show-overflow-tooltip="true" />
          <el-table-column label="手机号码" align="center" key="mobile" prop="mobile" v-if="columns[4].visible" width="120" />
          <el-table-column label="状态" key="status" v-if="columns[5].visible" align="center">
            <template v-slot="scope">
              <el-switch v-model="scope.row.status" :active-value="0" :inactive-value="1" @change="handleStatusChange(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[6].visible" width="160">
            <template v-slot="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
            <template v-slot="scope">
              <el-button size="small" type="text" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
              <el-dropdown  @command="(command) => handleCommand(command, scope.$index, scope.row)">
                <el-button size="small" type="text" icon="DArrowRight">更多</el-button>
                <template #dropdown>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="handleDelete" v-if="scope.row.id !== 1" size="small" type="text" icon="Delete">删除</el-dropdown-item>
                    <el-dropdown-item command="handleResetPwd" size="small" type="text" icon="Key">重置密码</el-dropdown-item>
                    <el-dropdown-item command="handleRole" size="small" type="text" icon="Circle-check">分配角色</el-dropdown-item>
                  </el-dropdown-menu>
                </template>

              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </el-col>
  </el-row>
</div>
</template>

<script setup>
import {
  listUser
} from "@/api/system/user"
import { listSimpleDepts } from "@/api/system/dept"
import { listSimplePosts } from "@/api/system/post"
import ElComponent from '@/plugins/modal.js'
import { ref,reactive,onMounted } from 'vue'
import { handleTree,parseTime } from '@/utils/ruoyi.js'

// 遮罩层
const loading = ref(true)
// 导出遮罩层
const exportLoading = ref(false)
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
    console.log("值",response.data)
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
    const responseListSimpleDepts = await listSimpleDepts()
    // 处理 deptOptions 参数
    deptOptions.value = []
    deptOptions.value.push(...handleTree(responseListSimpleDepts.data,"id"))

    const responseListSimplePosts = await listSimplePosts()
    // 处理 postOptions 参数
    postOptions.value = []
    postOptions.value.push(...responseListSimplePosts.data)
  }catch (error){
    ElComponent.msgError("树形菜单发生错误"+error)
  }

}
</script>

<style scoped>

</style>
