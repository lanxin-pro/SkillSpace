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
            prefix-icon="el-icon-search"
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
      用户数据
    </el-col>
  </el-row>
</div>
</template>

<script setup>
import { ref,reactive } from 'vue'

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
</script>

<style scoped>

</style>
