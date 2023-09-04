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
      <el-form-item label="公众号" prop="accountId">
        <WxAccountSelect @change="onAccountChanged" />
      </el-form-item>
      <el-form-item>
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="openForm('create')"
            v-hasPermi="['mp:tag:create']"
            :disabled="queryParams.accountId === 0"
        >
          新增
        </el-button>
        <el-button
            icon="Refresh"
            type="success"
            plain
            @click="handleSync"
            v-hasPermi="['mp:tag:sync']"
            :disabled="queryParams.accountId === 0"
        >
          同步
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <el-table class="mt10" v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="标签名称" align="center" prop="name" />
      <el-table-column label="粉丝数" align="center" prop="count" />
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
              link
              icon="Edit"
              size="small"
              type="primary"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['mp:tag:update']"
          >
            修改
          </el-button>
          <el-button
              link
              size="small"
              icon="Delete"
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['mp:tag:delete']"
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
    <TagForm ref="formRef" @success="getList" />
  </div>
</template>

<script setup>
import { getTagPage,deleteTag,syncTag } from '@/api/mp/tag/index.js'
import Pagination from '@/components/Pagination/index.vue'
import { dateFormatter,formatDate } from '@/utils/formatTime'
import WxAccountSelect from '@/views/mp/components/wx-account-select/index.vue'
import { fileSizeFormatter } from '@/utils'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted } from 'vue'
import TagForm from './TagForm.vue'

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])

const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  accountId: -1
})

const formRef = ref()

/** 侦听公众号变化 **/
const onAccountChanged = (id) => {
  queryParams.accountId = id
  queryParams.pageNo = 1
  getList()
}

/** 查询列表 */
const getList = async () => {
  try {
    loading.value = true
    const response = await getTagPage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 添加/修改操作 */
const openForm = (type, id) => {
  formRef.value?.open(type, queryParams.accountId, id)
}

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm("您确定要删除吗？")
    // 发起删除
    await deleteTag(id)
    ELComponent.confirm("删除成功!")
    // 刷新列表
    await getList()
  } catch {}
}

/** 同步操作 */
const handleSync = async () => {
  try {
    await ELComponent.confirm('是否确认同步标签？')
    await syncTag(queryParams.accountId)
    ELComponent.msgSuccess('同步标签成功')
    await getList()
  } catch {}
}
</script>

<style scoped>

</style>
