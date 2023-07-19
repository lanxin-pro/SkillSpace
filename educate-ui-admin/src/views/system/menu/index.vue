<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        :model="queryParams"
        ref="queryForm"
        size="small"
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
        <el-button type="primary" plain icon="plus" size="small" @click="handleAdd"
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
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template v-slot="scope">
          <el-button size="small" type="text" icon="edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['system:menu:update']">修改</el-button>
          <el-button size="small" type="text" icon="plus" @click="handleAdd(scope.row)"
                     v-hasPermi="['system:menu:create']">新增</el-button>
          <el-button size="small" type="text" icon="delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['system:menu:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>




  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { listMenu } from '@/api/system/menu'
import { handleTree } from '@/utils/ruoyi.js'
import SvgIcon from '@/components/SvgIcon/index.vue'

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
  getList()
})

/**
 * 查询菜单列表
 *
 * @returns {Promise<void>}
 */
const getList = async ()=>{
  loading.value = true
  const response = await listMenu(queryParams.value)
  menuList.value = handleTree(response.data,'id')
  loading.value = false
}

</script>

<style scoped>

</style>
