<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        :model="queryParams"
        ref="queryForm"
        size="small"
        label-width="68px"
        :inline="true"
        v-show="showSearch"
    >
      <el-form-item label="菜单名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入菜单名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择菜单状态" clearable>
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="parseInt(dict.value)"
              :label="dict.label"
              :value="parseInt(dict.value)"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="plus" size="small" @click="openForm('create')"
                   v-hasPermi="['system:menu:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="sort" size="small" @click="toggleExpandAll">展开/折叠</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

<!--    :tree-props="{children: 'children', hasChildren: 'hasChildren'}" 渲染嵌套数据的配置选项 -->
    <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="menuList"
        row-key="id"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="name" label="菜单名称" :show-overflow-tooltip="true" width="250"></el-table-column>
      <el-table-column prop="icon" label="图标" align="center" width="100">
        <template v-slot="scope">
          <SvgIcon icon-color="red" icon-size="lg" :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="60"></el-table-column>
      <el-table-column prop="permission" label="权限标识" :show-overflow-tooltip="true" />
      <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true" />
      <el-table-column prop="componentName" label="组件名称" :show-overflow-tooltip="true" />
      <el-table-column prop="status" label="状态" width="80">
        <template v-slot="scope">
          <DictTag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template v-slot="scope">
          <el-button size="small" style="padding: 0" type="text" icon="edit" @click="openForm('update', scope.row.id)"
                     v-hasPermi="['system:menu:update']">修改</el-button>
          <el-button size="small" style="padding: 0" type="text" icon="plus" @click="openForm('create', undefined, scope.row.id)"
                     v-hasPermi="['system:menu:create']">新增</el-button>
          <el-button size="small" style="padding: 0" type="text" icon="delete" @click="handleDelete(scope.row.id)"
                     v-hasPermi="['system:menu:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>



    <!-- 添加或修改菜单对话框 -->
    <MenuForm ref="formRef" @success="getList" />
  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { listMenu,getMenu,deleteMenu } from '@/api/system/menu'
import { handleTree } from '@/utils/ruoyi.js'
import SvgIcon from '@/components/SvgIcon/index.vue'
import { SystemMenuTypeEnum, CommonStatusEnum } from '@/utils/constants'
import MenuForm from './MenuForm.vue'
import DictTag from '@/components/DictTag/index.vue'
import ELComponent from '@/plugins/modal.js'

// 添加/修改操作
const formRef = ref()
// ref
const queryForm = ref()
// 遮罩层
const loading = ref(true)
// 显示搜索条件
const showSearch = ref(true)
// 菜单表格树数据
const menuList = ref([])
// 菜单树选项
const menuOptions = ref([])
// 弹出层标题
const title = ref("")
// 是否显示弹出层
const open = ref(false)
// 是否展开，默认全部折叠
const isExpandAll = ref(false)
// 重新渲染表格状态
const refreshTable = ref(true)
// 查询参数
const queryParams = ref({})
const name = ref(undefined)
const visible = ref(undefined)
// 表单参数
const form = ref({})

const rules = reactive({
  name: [
    { required: true, message: "菜单名称不能为空", trigger: "blur" }
  ],
  sort: [
    { required: true, message: "菜单顺序不能为空", trigger: "blur" }
  ],
  path: [
    { required: true, message: "路由地址不能为空", trigger: "blur" }
  ],
  status: [
    { required: true, message: "状态不能为空", trigger: "blur" }
  ]
})


onMounted(()=>{
  console.log('菜单')
  getList()
})

/**
 * 查询菜单列表
 *
 * @returns {Promise<void>}
 */
const getList = async ()=>{
  loading.value = true
  try{
    const response = await listMenu(queryParams.value)
    menuList.value = handleTree(response.data)
  }finally {
    loading.value = false
  }
}

/**
 * 搜索按钮操作
 */
const handleQuery = ()=>{
  getList()
}
const resetQuery = ()=>{
  queryForm.value.resetFields()
  handleQuery()
}

/**
 * 新增按钮操作
 * @returns {Promise<void>}
 */
const openForm = async (type, id, parentId) => {
  formRef.value.open(type,id,parentId)
}
/**
 * 删除按钮操作
 * @returns {Promise<void>}
 */
const handleDelete = async (id)=>{
  // 删除的二次确认
  await ELComponent.confirm('您确定要删除吗？')
  // 发起删除
  await deleteMenu(id)
  ELComponent.msgSuccess('删除成功')
  // 刷新列表
  await getList()
}
const getTreeSelect = async ()=>{
  const response = await listMenu()
  menuOptions.value = []
  const menu = { id: 0, name: '主类目', children: [] }
  menu.children = this.handleTree(response.data,"id")
  menuOptions.value.push(menu)
}
const reset = ()=>{
  form.value = {
    id: undefined,
    parentId: 0,
    name: undefined,
    icon: undefined,
    type: SystemMenuTypeEnum.DIR,
    sort: undefined,
    status: CommonStatusEnum.ENABLE,
    visible: true,
    keepAlive: true,
    alwaysShow: true,
  }
  queryForm.value.resetFields()
}
/**
 * 展开/折叠操作
 */
const toggleExpandAll = ()=>{
  refreshTable.value = false
  isExpandAll.value = !isExpandAll.value
  nextTick(()=>{
    refreshTable.value = true
  })
}
</script>

<style scoped>

</style>
