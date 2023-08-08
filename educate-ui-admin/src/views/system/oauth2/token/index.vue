<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="90px"
        size="small"
    >
      <el-form-item label="客户端编号" prop="clientId">
        <el-select
            v-model="queryParams.clientId"
            placeholder="请选择客户端编号"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in clientList"
              :value="dict.clientId"
              :key="dict.value"
              :label="dict.clientId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用户编号" prop="userId">
        <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户编号"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-select
            v-model="queryParams.userType"
            placeholder="请选择用户类型"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.USER_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button size="small" type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button size="small" icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="访问令牌" align="center" prop="accessToken" width="300" />
      <el-table-column label="刷新令牌" align="center" prop="refreshToken" width="300" />
      <el-table-column label="用户编号" align="center" prop="userId">
        <template #default="scope">
          <div>{{scope.row.userId}} ({{scope.row.nickname}})</div>
        </template>
      </el-table-column>
      <el-table-column label="用户类型" align="center" prop="userType">
        <template #default="scope">
          <DictTag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          :formatter="dateFormatter"
          width="180"
      />
      <el-table-column
          label="过期时间"
          align="center"
          prop="expiresTime"
          :formatter="dateFormatter"
          width="180"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
              link
              type="danger"
              @click="handleForceLogout(scope.row.accessToken)"
              v-hasPermi="['system:oauth2-token:delete']"
          >
            强退
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
</template>

<script setup>
import Pagination from '@/components/Pagination/index.vue'
import { ref,reactive,onMounted } from 'vue'
import { getAccessTokenPage,deleteAccessToken } from '@/api/system/oauth2/token/index.js'
import { getClientIdsInterface } from '@/api/system/oauth2/client/index.js'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime.js'
import ELComponent from '@/plugins/modal.js'
import DictTag from '@/components/DictTag/index.vue'

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 20,
  userId: null,
  userType: null,
  clientId: null
})
// 搜索的表单
const queryFormRef = ref()
const clientList = ref([])

/** 初始化 **/
onMounted(() => {
  getList()
  getClientIds()
})

/** 获取客户端的ids */
const getClientIds = async ()=>{
  const response = await getClientIdsInterface()
  clientList.value = response.data
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getAccessTokenPage(queryParams)
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
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 强制退出操作 */
const handleForceLogout = async (accessToken) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('是否要强制退出用户')
    // 发起删除
    await deleteAccessToken(accessToken)
    ELComponent.msgSuccess('已经成功强制退出')
    // 刷新列表
    await getList()
  } catch {}
}

</script>

<style scoped>

</style>
