<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        ref="queryFormRef"
        :inline="true"
        :model="queryParams"
        class="-mb-15px"
        label-width="68px"
        size="small"
    >
      <el-form-item>
        <el-button icon="search" @click="handleQuery">搜索</el-button>
        <el-button icon="refresh" @click="resetQuery">重置</el-button>
        <el-button plain type="primary" @click="openForm('create')"
        ><Icon icon="ep:plus" />创建业务转账单
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true">
      <el-table-column align="center" label="订单编号" prop="id" />
      <el-table-column align="center" label="转账类型" prop="type" width="120">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_TRANSFER_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="转账金额" prop="price">
        <template #default="scope">
          <span>￥{{ (scope.row.price / 100.0).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="收款人姓名" prop="userName" width="120" />
      <el-table-column align="center" label="支付宝登录账号" prop="alipayLogonId" width="180" />
      <el-table-column align="center" label="微信 openid" prop="openid" width="120" />
      <el-table-column align="center" label="转账状态" prop="transferStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_TRANSFER_STATUS" :value="scope.row.transferStatus" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="转账单号" prop="payTransferId" />
      <el-table-column align="center" label="支付渠道" prop="payChannelCode" />
      <el-table-column
          :formatter="dateFormatter"
          align="center"
          label="转账时间"
          prop="transferTime"
          width="180px"
      />
      <el-table-column
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
          label="操作"
          width="100"
      >
        <template #default="scope">
          <el-button
              v-if="scope.row.transferStatus === 0"
              v-hasPermi="['pay:transfer:create']"
              link
              type="primary"
              @click="handleTransfer(scope.row)"
          >
            发起转账
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
  </div>
</template>

<script setup>
import { DICT_TYPE, getIntDictOptions, getStrDictOptions } from '@/utils/dict'
import { ref,reactive,onMounted } from 'vue'
import * as DemoTransferApi from '@/api/pay/demo/transfer/index.js'
import Pagination from '@/components/Pagination/index.vue'
import ELComponent from '@/plugins/modal.js'
import download from '@/utils/download'
import { dateFormatter } from '@/utils/formatTime'

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10
})
// 搜索的表单
const queryFormRef = ref()
// 传递给转账订单的数据
let payTransfer = reactive({
  appId: undefined,
  merchantTransferId: undefined,
  type: undefined,
  price: undefined,
  subject: undefined,
  userName: undefined,
  alipayLogonId: undefined,
  openid: undefined
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await DemoTransferApi.getDemoTransferPage(queryParams)
    list.value = data.list
    total.value = data.total
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

/** 创建业务转账单操作 */
const demoFormRef = ref()
const payTransferRef = ref()
const openForm = (type) => {
  demoFormRef.value.open(type)
}

/** 发起转账操作 */
const handleTransfer = (row) => {
  payTransfer = { ...row }
  payTransfer.merchantTransferId = row.id.toString()
  payTransfer.subject = '示例转账'
  payTransferRef.value.showPayTransfer(payTransfer)
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>

<style scoped>

</style>
