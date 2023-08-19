<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        :model="queryParams"
        ref="queryFormRef"
        size="small"
        :inline="true"
        v-show="showSearch"
        class="-mb-15px"
        label-width="68px"
    >
      <el-form-item label="邮箱" prop="mail">
        <el-input
            v-model="queryParams.mail"
            placeholder="请输入邮箱"
            class="!w-240px"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input
            v-model="queryParams.username"
            placeholder="请输入用户名"
            class="!w-240px"
            clearable
            @keyup.enter.native="handleQuery"
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
                   v-hasPermi="['system:mail-account:create']">新增</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="邮箱" align="center" prop="mail" />
      <el-table-column label="用户名" align="center" prop="username" />
      <el-table-column label="SMTP 服务器域名" align="center" prop="host" />
      <el-table-column label="SMTP 服务器端口" align="center" prop="port" />
      <el-table-column label="是否开启 SSL" align="center" prop="sslEnable">
        <template v-slot="scope">
          <DictTag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.sslEnable" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="small" type="text" icon="Edit" @click="openForm('update',scope.row.id)"
                     v-hasPermi="['system:mail-account:update']">修改</el-button>
          <el-button size="small" type="text" icon="Delete" @click="handleDelete(scope.row.id)"
                     v-hasPermi="['system:mail-account:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <Pagination
        :total="total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
   />
    <!-- 表单弹窗：添加/修改 -->
    <AccountForm ref="formRef" @success="getList" />

  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import { parseTime } from '@/utils/ruoyi.js'
import { getMailAccountPage,deleteMailAccount } from '@/api/system/mail/account/index.js'
import DictTag from '@/components/DictTag/index.vue'
import AccountForm from './MailAccountForm.vue'
import { dateFormatter } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'

// 遮罩层
const loading = ref(true)
// 显示搜索条件
const showSearch = ref(true)
// 总条数
const total = ref(0)
// 短信渠道列表
const list = ref([])
// 弹出层标题
const title = ref("")
// 是否显示弹出层
const open = ref(false)
// 搜索的表单
const queryFormRef = ref()
// 查询参数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  mail: null,
  username: null
})
// 表单参数
const form = ref({})


onMounted(()=>{
  getList()
})

/** 查询全部数据 */
const getList = async ()=>{
  loading.value = true
  try {
    const response = await getMailAccountPage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type, id) => {
  formRef.value.open(type, id)
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

/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteMailAccount(id)
    ELComponent.msgSuccess("删除成功！")
    // 刷新列表
    await getList()
  } catch {}
}

</script>

<style scoped>
.el-table .cell {
  padding: 0;
}
.el-table .cell .el-button--small {
  padding: 2px;
}
</style>
