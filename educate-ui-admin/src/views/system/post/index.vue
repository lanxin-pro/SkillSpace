<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="岗位名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入岗位名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="岗位编码" prop="code">
        <el-input
            v-model="queryParams.code"
            placeholder="请输入岗位编码"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm('create')"
                   v-hasPermi="['system:post:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="Download" size="small" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['system:post:export']">导出</el-button>
      </el-col>

    </el-row>

    <!--  列表  -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="岗位编号" align="center" prop="id" />
      <el-table-column label="岗位名称" align="center" prop="name" />
      <el-table-column label="岗位编码" align="center" prop="code" />
      <el-table-column label="岗位顺序" align="center" prop="sort" />
      <el-table-column label="岗位备注" align="center" prop="remark" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <DictTag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
              size="small"
              link
              icon="Edit"
              type="primary"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['system:post:update']"
          >
            编辑
          </el-button>
          <el-button
              size="small"
              icon="Delete"
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['system:post:delete']"
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
  </div>
  <!-- 表单弹窗：添加/修改 -->
  <PostForm ref="formRef" @success="getList" />
</template>
<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import PostForm from './PostForm.vue'
import { parseTime } from '@/utils/ruoyi.js'
import { getPostPage,deletePost } from '@/api/system/post.js'
import { handleTree } from '@/utils/tree.js'
import DictTag from '@/components/DictTag/index.vue'
import Pagination from '@/components/Pagination/index.vue'

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
  status: undefined
})
// 搜索的表单
const queryFormRef = ref()
// 导出的加载中
const exportLoading = ref(false)

onMounted(()=>{
  getList()
})
/** 添加/修改操作 */
const formRef = ref()
const openForm = async (type,id)=>{
  formRef.value.open(type, id)
}
/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deletePost(id)
    ELComponent.msgSuccess('删除成功')
    // 刷新列表
    await getList()
  } catch {}
}
/** 加载列表按钮操作 */
const getList = async ()=>{
  loading.value = true
  try {
    const response = await getPostPage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}


/** 搜索按钮操作 */
const handleQuery = () => {
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryParams.pageNo = 1
  queryFormRef.value.resetFields()
  handleQuery()
}

</script>
