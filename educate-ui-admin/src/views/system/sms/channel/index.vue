<template>
  <div class="app-container">
    <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="短信签名" prop="signature">
        <el-input
            v-model="queryParams.signature"
            placeholder="请输入短信签名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择启用状态" clearable>
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
                   v-hasPermi="['system:sms-channel:create']">新增</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="短信签名" align="center" prop="signature" />
      <el-table-column label="渠道编码" align="center" prop="code">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="scope.row.code" />
        </template>
      </el-table-column>
      <el-table-column label="启用状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column
          label="短信 API 的账号"
          align="center"
          prop="apiKey"
          :show-overflow-tooltip="true"
          width="180"
      />
      <el-table-column
          label="短信 API 的密钥"
          align="center"
          prop="apiSecret"
          :show-overflow-tooltip="true"
          width="180"
      />
      <el-table-column
          label="短信发送回调 URL"
          align="center"
          prop="callbackUrl"
          :show-overflow-tooltip="true"
          width="180"
      />
      <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center" width="120">
        <template #default="scope">
          <el-button size="small" type="text" icon="Edit" @click="openForm('update', scope.row.id)"
                     v-hasPermi="['system:sms-channel:update']">修改</el-button>
          <el-button size="small" type="text" icon="Delete" @click="handleDelete(scope.row.id)"
                     v-hasPermi="['system:sms-channel:delete']">删除</el-button>
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
    <SmsChannelForm ref="formRef" @success="getList" />
  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { deleteSmsChannel,getSmsChannelPage } from '@/api/system/sms/channel/index.js'
import { handleTree } from '@/utils/ruoyi.js'
import SvgIcon from '@/components/SvgIcon/index.vue'
import { SystemMenuTypeEnum, CommonStatusEnum } from '@/utils/constants'
import SmsChannelForm from './SmsChannelForm.vue'
import DictTag from '@/components/DictTag/index.vue'
import ELComponent from '@/plugins/modal.js'
import { dateFormatter } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'

// 列表的加载中
const loading = ref(false)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
// 搜索的表单
const queryFormRef = ref()
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  signature: undefined,
  status: undefined,
  createTime: []
})

/** 初始化 **/
onMounted(() => {
  getList()
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getSmsChannelPage(queryParams)
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
    await deleteSmsChannel(id)
    ELComponent.msgSuccess('删除成功！')
    // 刷新列表
    await getList()
  } catch {}
}

</script>

<style scoped>
/*去除操作的换行*/
.el-table .cell {
  padding: 0;
}

.el-table .cell .el-button--small {
  padding: 0;
}
</style>
