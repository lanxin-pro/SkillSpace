<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        size="small"
    >
      <el-form-item label="名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入名称"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm('create')"
                   v-hasPermi="['mp:account:create']">新增
        </el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="微信号" align="center" prop="account" width="180" />
      <el-table-column label="appId" align="center" prop="appId" width="180" />
      <el-table-column label="服务器地址(URL)" align="center" prop="appId" width="300" :show-overflow-tooltip="true">
        <template #default="scope">
          {{ 'http://服务端地址/mp/open/' + scope.row.appId }}
        </template>
      </el-table-column>
      <el-table-column label="二维码" align="center" prop="qrCodeUrl">
        <template #default="scope">
          <img
              v-if="scope.row.qrCodeUrl"
              :src="scope.row.qrCodeUrl"
              alt="二维码"
              style="display: inline-block; height: 100px"
          />
          <el-button
              v-else
              link
              type="primary"
              @click="handleGenerateQrCode(scope.row)"
              v-hasPermi="['mp:account:qr-code']"
          >
            生成二维码
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="160">
        <template #default="scope">
          <el-button
              size="small"
              type="text"
              icon="Edit"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['mp:account:update']">修改
          </el-button>
          <el-button
              size="small"
              type="text"
              icon="Delete"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['mp:account:delete']">删除
          </el-button>
          <el-button
              size="small"
              type="text"
              icon="Refresh"
              @click="handleGenerateQrCode(scope.row)"
              v-hasPermi="['mp:account:qr-code']">生成二维码
          </el-button>
          <el-button
              size="small"
              type="text"
              icon="Share"
              @click="handleCleanQuota(scope.row)"
              v-hasPermi="['mp:account:clear-quota']">清空 API 配额
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

    <!-- 对话框(添加 / 修改) -->
    <AccountForm ref="formRef" @success="getList" />
  </div>
</template>

<script setup>
import { getAccountPage,generateAccountQrCode,clearAccountQuota,deleteAccount } from '@/api/mp/account/index.js'
import Pagination from '@/components/Pagination/index.vue'
import { dateFormatter,formatDate } from '@/utils/formatTime'
import { fileSizeFormatter } from '@/utils'
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted } from 'vue'
import AccountForm from './AccountForm.vue'

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: null,
  account: null,
  appId: null
})
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
    const response = await getAccountPage(queryParams)
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

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type, id) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm("您确定要删除吗？")
    // 发起删除
    await deleteAccount(id)
    ELComponent.msgSuccess("删除成功！")
    // 刷新列表
    await getList()
  } catch {}
}

/** 生成二维码的按钮操作 */
const handleGenerateQrCode = async (row) => {
  try {
    // 生成二维码的二次确认
    await ELComponent.confirm('是否确认生成公众号账号编号为"' + row.name + '"的二维码?')
    // 发起生成二维码
    await generateAccountQrCode(row.id)
    ELComponent.msgSuccess('生成二维码成功')
    // 刷新列表
    await getList()
  } catch {}
}

/** 清空二维码 API 配额的按钮操作 */
const handleCleanQuota = async (row) => {
  try {
    // 清空 API 配额的二次确认
    await ELComponent.confirm('是否确认清空生成公众号账号编号为"' + row.name + '"的 API 配额?')
    // 发起清空 API 配额
    await clearAccountQuota(row.id)
    ELComponent.msgSuccess('清空 API 配额成功')
  } catch {}
}

</script>

<style scoped>
.el-table .cell {
  padding: 0;
}
.el-table .cell .el-button--small {
  padding: 0;
}
</style>
