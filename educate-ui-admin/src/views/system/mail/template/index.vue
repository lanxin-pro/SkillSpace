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
        label-width="100px"
    >
      <el-form-item label="模板名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入模板名称"
            clearable
            @keyup.enter.native="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="模板编码" prop="code">
        <el-input
            v-model="queryParams.code"
            placeholder="请输入模板编码"
            clearable
            @keyup.enter.native="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="邮箱账号" prop="accountId">
        <el-select
            v-model="queryParams.accountId"
            placeholder="请输入邮箱账号"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="account in accountOptions"
              :key="account.id"
              :value="account.id"
              :label="account.mail"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开启状态" prop="status">
        <el-select
            v-model="queryParams.status"
            placeholder="请选择开启状态"
            clearable size="small"
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
            v-model="queryParams.createTime"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-240px"
            end-placeholder="结束日期"
            start-placeholder="开始日期"
            type="daterange"
            value-format="YYYY-MM-DD HH:mm:ss"
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
      <el-table-column label="模板编码" align="center" prop="code" :show-overflow-tooltip="true" />
      <el-table-column label="模板名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="模板标题" align="center" prop="title" />
      <el-table-column label="模板内容" align="center" prop="content" :show-overflow-tooltip="true" />
      <el-table-column label="邮箱账号" align="center" prop="accountId" width="200">
        <template v-slot="scope">
          {{ accountOptions.find(account => account.id === scope.row.accountId)?.mail }}
        </template>
      </el-table-column>
      <el-table-column label="发送人名称" align="center" prop="nickname" />
      <el-table-column label="开启状态" align="center" prop="status">
        <template v-slot="scope">
          <DictTag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template v-slot="scope">
          <el-button size="small" type="text" icon="Share" @click="openSendForm(scope.row.id)"
                     v-hasPermi="['system:mail-template:send-mail']">测试</el-button>
          <el-button size="small" type="text" icon="Edit" @click="openForm('update',scope.row.id)"
                     v-hasPermi="['system:mail-template:update']">修改</el-button>
          <el-button size="small" type="text" icon="Delete" @click="handleDelete(scope.row.id)"
                     v-hasPermi="['system:mail-template:delete']">删除</el-button>
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
    <MailTemplateForm ref="formRef" @success="getList" />
    <MailTemplateSendForm ref="formSendRef" @success="getList" />

  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import { parseTime } from '@/utils/ruoyi.js'
import { getMailTemplatePage,deleteMailTemplate } from '@/api/system/mail/template/index.js'
import { getSimpleMailAccountList } from '@/api/system/mail/account/index.js'
import DictTag from '@/components/DictTag/index.vue'
import MailTemplateForm from './MailTemplateForm.vue'
import MailTemplateSendForm from './MailTemplateSendForm.vue'
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
  pageSize: 12,
  name: null,
  code: null,
  accountId: null,
  status: null,
  createTime: [],
})
// 表单参数
const form = ref({})
const accountOptions = ref([])

onMounted(async ()=>{
  getList()
  // 获得邮箱账号列表
  const response = await getSimpleMailAccountList()
  accountOptions.value = response.data
})

/** 查询全部数据 */
const getList = async ()=>{
  loading.value = true
  try {
    const response = await getMailTemplatePage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type, id) => {
  formRef.value.open(type, id ,accountOptions.value)
}
/** 发送测试操作 */
const formSendRef = ref()
const openSendForm = (id) => {
  formSendRef.value.open(id)
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
    await deleteMailTemplate(id)
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
