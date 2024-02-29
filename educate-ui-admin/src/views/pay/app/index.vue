<template>
  <div class="app-container">
    <!-- 搜索 -->
    <el-form
        ref="queryFormRef"
        :inline="true"
        :model="queryParams"
        label-width="68px"
        size="small"
    >
      <el-form-item label="应用名" prop="name">
        <el-input
            v-model="queryParams.name"
            class="!w-240px"
            clearable
            placeholder="请输入应用名"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开启状态" prop="status">
        <el-select
            v-model="queryParams.status"
            class="!w-240px"
            clearable
            placeholder="请选择开启状态"
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
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-240px"
            end-placeholder="结束日期"
            start-placeholder="开始日期"
            type="daterange"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button
            icon="Search"
            @click="handleQuery"
        >
          搜索
        </el-button>
        <el-button icon="refresh" @click="resetQuery">重置</el-button>
        <el-button v-hasPermi="['pay:app:create']"
                   icon="plus"
                   plain
                   type="primary"
                   @click="openForm('create')">新增
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column align="center" label="应用编号" prop="id" />
      <el-table-column align="center" label="应用名" prop="name" />
      <el-table-column align="center" label="开启状态" prop="status">
        <template #default="scope">
          <el-switch
              v-model="scope.row.status"
              :active-value="0"
              :inactive-value="1"
              @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="支付宝配置">

        <el-table-column :label="PayChannelEnum.ALIPAY_APP.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.ALIPAY_APP.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_APP.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_APP.code)"
            >
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="PayChannelEnum.ALIPAY_PC.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.ALIPAY_PC.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_PC.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_PC.code)"
            >
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="PayChannelEnum.ALIPAY_WAP.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.ALIPAY_WAP.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_WAP.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_WAP.code)"
            >
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="PayChannelEnum.ALIPAY_QR.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.ALIPAY_QR.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_QR.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_QR.code)"
            >
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="PayChannelEnum.ALIPAY_BAR.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.ALIPAY_BAR.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_BAR.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.ALIPAY_BAR.code)"
            >
            </el-button>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="微信配置">
        <el-table-column :label="PayChannelEnum.WX_LITE.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.WX_LITE.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.WX_LITE.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.WX_LITE.code)"
            >
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="PayChannelEnum.WX_PUB.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.WX_PUB.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.WX_PUB.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.WX_PUB.code)"
            >
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="PayChannelEnum.WX_APP.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.WX_APP.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.WX_APP.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.WX_APP.code)"
            >
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="PayChannelEnum.WX_BAR.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.WX_BAR.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.WX_BAR.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.WX_BAR.code)"
            >
            </el-button>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="钱包支付配置">
        <el-table-column :label="PayChannelEnum.WALLET.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.WALLET.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.WALLET.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.WALLET.code)"
            >
            </el-button>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" label="模拟支付配置">
        <el-table-column :label="PayChannelEnum.MOCK.name" align="center">
          <template #default="scope">
            <el-button
                v-if="isChannelExists(scope.row.channelCodes, PayChannelEnum.MOCK.code)"
                circle
                icon="check"
                type="success"
                @click="openChannelForm(scope.row, PayChannelEnum.MOCK.code)"
            >
            </el-button>
            <el-button
                v-else
                circle
                icon="close"
                type="danger"
                @click="openChannelForm(scope.row, PayChannelEnum.MOCK.code)"
            >
            </el-button>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" min-width="130">
        <template #default="scope">
          <el-button
              v-hasPermi="['pay:app:update']"
              icon="edit"
              link
              size="small"
              type="primary"
              @click="openForm('update', scope.row.id)"
          >
            编辑
          </el-button>
          <el-button
              v-hasPermi="['pay:app:delete']"
              icon="delete"
              link
              size="small"
              type="danger"
              @click="handleDelete(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
        v-model:limit="queryParams.pageSize"
        v-model:page="queryParams.pageNo"
        :total="total"
        @pagination="getList"
    />

    <AlipayChannelForm ref="alipayFormRef" @success="getList" />
  </div>
</template>

<script setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { ref,reactive,onMounted } from 'vue'
import ELComponent from '@/plugins/modal.js'
import * as AppApi from '@/api/pay/app'
import AppForm from './components/AppForm.vue'
import { PayChannelEnum } from '@/utils/constants'
import { CommonStatusEnum } from '@/utils/constants'
import Pagination from '@/components/Pagination/index.vue'
import AlipayChannelForm from './components/channel/AlipayChannelForm.vue'

/** 初始化 **/
onMounted(async () => {
  await getList()
})

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: undefined,
  status: undefined,
  remark: undefined,
  payNotifyUrl: undefined,
  refundNotifyUrl: undefined,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await AppApi.getAppPage(queryParams)
    console.log('返回的接口数据', response)
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

/** 应用状态修改 */
const handleStatusChange = async (row) => {
  let text = row.status === CommonStatusEnum.ENABLE ? '启用' : '停用'
  try {
    await message.confirm('确认要"' + text + '""' + row.name + '"应用吗?')
    await AppApi.changeAppStatus({ id: row.id, status: row.status })
    message.success(text + '成功')
  } catch {
    row.status =
        row.status === CommonStatusEnum.ENABLE ? CommonStatusEnum.DISABLE : CommonStatusEnum.ENABLE
  }
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
    await AppApi.deleteApp(id)
    ELComponent.msgSuccess('删除成功！')
    // 刷新列表
    await getList()
  } catch {}
}

/**
 * 根据渠道编码判断渠道列表中是否存在
 *
 * @param channels 渠道列表
 * @param channelCode 渠道编码
 */
const isChannelExists = (channels, channelCode) => {
  if (!channels) {
    return false
  }
  return channels.indexOf(channelCode) !== -1
}

/**
 * 新增支付渠道信息
 */
const alipayFormRef = ref()
const weixinFormRef = ref()
const mockFormRef = ref()
const walletFormRef = ref()
const channelParam = reactive({
  appId: null, // 应用 ID
  payCode: null // 渠道编码
})
const openChannelForm = async (row, payCode) => {
  channelParam.appId = row.id
  channelParam.payCode = payCode
  if (payCode.indexOf('alipay_') === 0) {
    alipayFormRef.value.open(row.id, payCode)
    return
  }
  if (payCode.indexOf('wx_') === 0) {
    weixinFormRef.value.open(row.id, payCode)
    return
  }
  if (payCode.indexOf('mock') === 0) {
    mockFormRef.value.open(row.id, payCode)
  }
  if (payCode.indexOf('wallet') === 0) {
    mockFormRef.value.open(row.id, payCode)
  }
}

</script>

<style scoped>

</style>
