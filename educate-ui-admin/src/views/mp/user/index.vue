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
      <el-form-item label="用户标识" prop="openid">
        <el-input
            v-model="queryParams.openid"
            placeholder="请输入用户标识"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input
            v-model="queryParams.nickname"
            placeholder="请输入昵称"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button
            @click="handleQuery"
            type="primary"
            icon="Search">
          搜索
        </el-button>
        <el-button
            @click="resetQuery"
            icon="Refresh">
          重置
        </el-button>
        <el-button
            type="success"
            plain
            icon="Refresh"
            @click="handleSync"
            v-hasPermi="['mp:user:sync']"
            :disabled="queryParams.accountId === 0">
          同步
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <el-table class="mt10" v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="用户标识" align="center" prop="openid" width="260" />
      <el-table-column label="昵称" align="center" prop="nickname" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="标签" align="center" prop="tagIds" width="200">
        <template #default="scope">
          <span v-for="(tagId, index) in scope.row.tagIds" :key="index">
            <el-tag>{{ tagList.find((tag) => tag.tagId === tagId)?.name }} </el-tag>&nbsp;
          </span>
        </template>
      </el-table-column>
      <el-table-column label="订阅状态" align="center" prop="subscribeStatus">
        <template #default="scope">
          <el-tag v-if="scope.row.subscribeStatus === 0" type="success">已订阅</el-tag>
          <el-tag v-else type="danger">未订阅</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          label="订阅时间"
          align="center"
          prop="subscribeTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
              type="primary"
              link
              @click="openForm(scope.row.id)"
              v-hasPermi="['mp:user:update']"
          >
            修改
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

    <!-- 表单弹窗：修改 -->
    <UserForm ref="formRef" @success="getList" />

  </div>

</template>

<script setup>
import { getUserPage,syncUser } from '@/api/mp/mpuser/index.js'
import { getSimpleTagList } from '@/api/mp/tag/index.js'
import Pagination from '@/components/Pagination/index.vue'
import { dateFormatter,formatDate } from '@/utils/formatTime'
import WxAccountSelect from '@/views/mp/components/wx-account-select/index.vue'
import { fileSizeFormatter } from '@/utils'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted } from 'vue'
import UserForm from './UserForm.vue'

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])

const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  accountId: -1,
  openid: '',
  nickname: ''
})
// 搜索的表单
const queryFormRef = ref()
// 公众号标签列表
const tagList = ref([])


/** 初始化 */
onMounted(async () => {
  const response = await getSimpleTagList()
  tagList.value = response.data
})

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
    const response = await getUserPage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  const accountId = queryParams.accountId
  queryFormRef.value?.resetFields()
  queryParams.accountId = accountId
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (id) => {
  formRef.value?.open(id)
}

/** 同步标签 */
const handleSync = async () => {
  try {
    await ELComponent.confirm('是否确认同步粉丝？')
    await syncUser(queryParams.accountId)
    ELComponent.msgSuccess('开始从微信公众号同步粉丝信息，同步需要一段时间，建议稍后再查询')
    await getList()
  } catch {}
}

</script>

<style scoped>

</style>
