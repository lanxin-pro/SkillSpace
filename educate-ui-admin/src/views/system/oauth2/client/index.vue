<template>
  <div class="app-container">
    <!-- 搜索 -->
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="应用名" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入应用名"
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
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>

      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8 mt10">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm('create')"
                   v-hasPermi="['system:oauth2-client:create']">新增</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="客户端编号" align="center" prop="clientId" />
      <el-table-column label="客户端密钥" align="center" prop="secret" />
      <el-table-column label="应用名" align="center" prop="name" />
      <el-table-column label="应用图标" align="center" prop="logo">
        <template #default="scope">
          <el-image
              class="image__lazy"
              style="width: 120px; height: 120px"
              :src="scope.row.logo"
              fit="contain"
              lazy
          />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <DictTag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="访问令牌的有效期" align="center" prop="accessTokenValiditySeconds">
        <template #default="scope">{{ scope.row.accessTokenValiditySeconds }} 秒</template>
      </el-table-column>
      <el-table-column label="刷新令牌的有效期" align="center" prop="refreshTokenValiditySeconds">
        <template #default="scope">{{ scope.row.refreshTokenValiditySeconds }} 秒</template>
      </el-table-column>
      <el-table-column label="授权类型" align="center" prop="authorizedGrantTypes">
        <template #default="scope">
          <el-tag
              :disable-transitions="true"
              :key="index"
              v-for="(authorizedGrantType, index) in scope.row.authorizedGrantTypes"
              :index="index"
              class="mr-5px"
          >
            {{ authorizedGrantType }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center" width="150">
        <template #default="scope">
          <el-button size="small" type="text" icon="Edit" @click="openForm('update', scope.row.id)"
                     v-hasPermi="['system:oauth2-client:update']">修改</el-button>
          <el-button size="small" type="text" icon="Delete" @click="handleDelete(scope.row.id)"
                     v-hasPermi="['system:oauth2-client:delete']">删除</el-button>
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
    <ClientForm ref="formRef" @success="getList" />
  </div>
</template>

<script setup>
import Pagination from '@/components/Pagination/index.vue'
import ClientForm from './ClientForm.vue'
import { ref,reactive,onMounted } from 'vue'
import { getOAuth2ClientPage,deleteOAuth2Client } from '@/api/system/oauth2/client/index.js'
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
  pageSize: 10,
  name: null,
  status: null
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
    const response = await getOAuth2ClientPage(queryParams)
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
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteOAuth2Client(id)
    ELComponent.msgSuccess('删除成功！')
    // 刷新列表
    await getList()
  } catch {}
}


</script>

<style>
/*去除操作的换行*/
.el-table .cell {
  padding: 0;
}
.el-table .cell .el-button--small {
  padding: 0;
}

/* 懒加载 */
.image__lazy {
  height: 400px;
  overflow-y: auto;
}
.image__lazy .el-image {
  display: block;
  min-height: 200px;
  margin-bottom: 10px;
}
.image__lazy .el-image:last-child {
  margin-bottom: 0;
}
</style>
