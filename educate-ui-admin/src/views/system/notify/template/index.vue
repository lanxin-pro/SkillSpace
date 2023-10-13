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
      <el-form-item label="模板名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入模板名称"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="模板编号" prop="code">
        <el-input
            v-model="queryParams.code"
            placeholder="请输入模版编码"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
            v-model="queryParams.status"
            placeholder="请选择开启状态"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
            v-model="queryParams.createTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" type="primary" icon="search">搜索</el-button>
        <el-button @click="resetQuery" icon="refresh">重置</el-button>
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="openForm('create')"
            v-hasPermi="['system:notify-template:create']"
        >
          新增
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column
          label="模板编码"
          align="center"
          prop="code"
          width="120"
          :show-overflow-tooltip="true"
      />
      <el-table-column
          label="模板名称"
          align="center"
          prop="name"
          width="120"
          :show-overflow-tooltip="true"
      />
      <el-table-column label="类型" align="center" prop="type">
        <template #default="scope">
          <DictTag :type="DICT_TYPE.SYSTEM_NOTIFY_TEMPLATE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="发送人名称" align="center" prop="nickname" />
      <el-table-column
          label="模板内容"
          align="center"
          prop="content"
          width="200"
          :show-overflow-tooltip="true"
      />
      <el-table-column label="开启状态" align="center" prop="status" width="80">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center" width="210" fixed="right">
        <template #default="scope">
          <el-button
              link
              type="primary"
              icon="edit"
              size="small"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['system:notify-template:update']"
          >
            修改
          </el-button>
          <el-button
              link
              type="primary"
              size="small"
              icon="position"
              @click="openSendForm(scope.row)"
              v-hasPermi="['system:notify-template:send-notify']"
          >
            测试
          </el-button>
          <el-button
              link
              icon="delete"
              size="small"
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['system:notify-template:delete']"
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
    <NotifyTemplateForm ref="formRef" @success="getList" />
    <!-- 表单弹窗：测试发送 -->
    <NotifyTemplateSendForm ref="sendFormRef" />

  </div>
</template>

<script setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict.js'
import { dateFormatter } from '@/utils/formatTime.js'
import { ref,reactive,onMounted } from 'vue'
import ELComponent from '@/plugins/modal.js'
import Pagination from '@/components/Pagination/index.vue'
import { getNotifyTemplatePage,deleteNotifyTemplate } from '@/api/system/notify/template/index.js'
import NotifyTemplateForm from './NotifyTemplateForm.vue'
import NotifyTemplateSendForm from './NotifyTemplateSendForm.vue'
import DictTag from '@/components/DictTag/index.vue'

// 列表的加载中
const loading = ref(false)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: undefined,
  status: undefined,
  code: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getNotifyTemplatePage(queryParams)
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
    await deleteNotifyTemplate(id)
    ELComponent.msgSuccess('删除成功')
    // 刷新列表
    await getList()
  } catch {}
}

/** 发送站内信按钮 */
    // 表单 Ref
const sendFormRef = ref()
const openSendForm = (row) => {
  sendFormRef.value.open(row.id)
}

/** 初始化 **/
onMounted(() => {
  getList()
})

</script>

<style scoped>

</style>
