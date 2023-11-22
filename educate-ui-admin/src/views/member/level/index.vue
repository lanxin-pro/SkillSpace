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
      <el-form-item label="等级名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入等级名称"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable class="!w-240px">
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button @click="handleQuery" size="small" icon="Search">搜索</el-button>
        <el-button @click="resetQuery" size="small" icon="Refresh">重置</el-button>
        <el-button
            type="primary"
            size="small"
            @click="openForm('create')"
            icon="Plus"
            v-hasPermi="['member:level:create']"
        >
          新增
        </el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="编号" align="center" prop="id" min-width="60" />
      <el-table-column label="等级图标" align="center" prop="icon" min-width="80">
        <template #default="scope">
          <el-image
              :src="scope.row.icon"
              class="h-30px w-30px"
              :preview-src-list="[scope.row.icon]"
          />
        </template>
      </el-table-column>
      <el-table-column label="等级背景图" align="center" prop="backgroundUrl" min-width="100">
        <template #default="scope">
          <el-image
              :src="scope.row.backgroundUrl"
              class="h-30px w-30px"
              :preview-src-list="[scope.row.backgroundUrl]"
          />
        </template>
      </el-table-column>
      <el-table-column label="等级名称" align="center" prop="name" min-width="100" />
      <el-table-column label="等级" align="center" prop="level" min-width="60" />
      <el-table-column label="升级经验" align="center" prop="experience" min-width="80" />
      <el-table-column label="享受折扣(%)" align="center" prop="discountPercent" min-width="110" />
      <el-table-column label="状态" align="center" prop="status" min-width="70">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          :formatter="dateFormatter"
          min-width="170"
      />
      <el-table-column label="操作" align="center" min-width="110px" fixed="right">
        <template #default="scope">
          <el-button
              link
              type="primary"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['member:level:update']"
          >
            编辑
          </el-button>
          <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['member:level:delete']"
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
    <LevelForm ref="formRef" @success="getList" />
  </div>
</template>

<script setup>
import { deleteLevel, getLevelPage } from '@/api/member/level/index.js'
import Pagination from '@/components/Pagination/index.vue'
import { dateFormatter,formatDate } from '@/utils/formatTime'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted } from 'vue'
import LevelForm from './LevelForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict.js'
import DictTag from '@/components/DictTag/index.vue'

// 列表的加载中
const loading = ref(true)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  name: null,
  status: null
})
const total = ref(0)
// 搜索的表单
const queryFormRef = ref()

/** 初始化 **/
onMounted(() => {
  getList()
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getLevelPage(queryParams)
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
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type, id) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteLevel(id)
    ELComponent.msgSuccess('删除成功！')
    // 刷新列表
    await getList()
  } catch {}
}

</script>

<style scoped>

</style>
