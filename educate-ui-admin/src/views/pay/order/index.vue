<template>
  <div class="app-container">
    <!--  表单  -->
    <el-form
        ref="queryFormRef"
        :inline="true"
        :model="queryParams"
        label-width="100px"
        size="small"
    >
      <el-form-item label="应用编号" prop="appId">
        <el-select
            v-model="queryParams.appId"
            class="!w-240px"
            clearable
            placeholder="请选择应用信息"
        >
          <el-option v-for="item in appList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="支付渠道" prop="channelCode">
        <el-select
            v-model="queryParams.channelCode"
            class="!w-240px"
            clearable
            placeholder="请选择支付渠道"
        >
          <el-option
              v-for="dict in getStrDictOptions(DICT_TYPE.PAY_CHANNEL_CODE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="商户单号" prop="merchantOrderId">
        <el-input
            v-model="queryParams.merchantOrderId"
            class="!w-240px"
            clearable
            placeholder="请输入商户单号"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付单号" prop="no">
        <el-input
            v-model="queryParams.no"
            class="!w-240px"
            clearable
            placeholder="请输入支付单号"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="渠道单号" prop="channelOrderNo">
        <el-input
            v-model="queryParams.channelOrderNo"
            class="!w-240px"
            clearable
            placeholder="请输入渠道单号"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付状态" prop="status">
        <el-select
            v-model="queryParams.status"
            class="!w-240px"
            clearable
            placeholder="请选择支付状态"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.PAY_ORDER_STATUS)"
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
        <el-button icon="search" @click="handleQuery">搜索</el-button>
        <el-button icon="refresh" @click="resetQuery">重置</el-button>
        <el-button
            v-hasPermi="['system:tenant:export']"
            :loading="exportLoading"
            icon="download"
            plain
            type="success"
            @click="handleExport"
        >
          导出
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column align="center" label="编号" prop="id" width="80" />
      <el-table-column
          :formatter="dateFormatter"
          align="center"
          label="创建时间"
          prop="createTime"
          width="180"
      />
      <el-table-column align="center" label="支付金额" prop="price" width="100">
        <template #default="scope"> ￥{{ parseFloat(scope.row.price / 100).toFixed(2) }} </template>
      </el-table-column>
      <el-table-column align="center" label="退款金额" prop="refundPrice" width="100">
        <template #default="scope">
          ￥{{ parseFloat(scope.row.refundPrice / 100).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="手续金额" prop="channelFeePrice" width="100">
        <template #default="scope">
          ￥{{ parseFloat(scope.row.channelFeePrice / 100).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column align="left" label="订单号" width="300">
        <template #default="scope">
          <p class="order-font">
            <el-tag size="small"> 商户</el-tag> {{ scope.row.merchantOrderId }}
          </p>
          <p v-if="scope.row.no" class="order-font">
            <el-tag size="small" type="warning">支付</el-tag> {{ scope.row.no }}
          </p>
          <p v-if="scope.row.channelOrderNo" class="order-font">
            <el-tag size="small" type="success">渠道</el-tag> {{ scope.row.channelOrderNo }}
          </p>
        </template>
      </el-table-column>
      <el-table-column align="center" label="支付状态" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_ORDER_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="支付渠道" prop="channelCode" width="140">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="scope.row.channelCode" />
        </template>
      </el-table-column>
      <el-table-column
          :formatter="dateFormatter"
          align="center"
          label="支付时间"
          prop="successTime"
          width="180"
      />
      <el-table-column align="center" label="支付应用" prop="appName" width="100" />
      <el-table-column align="center" label="商品标题" prop="subject" width="180" />
      <el-table-column align="center" fixed="right" label="操作">
        <template #default="scope">
          <el-button
              v-hasPermi="['pay:order:query']"
              link
              type="primary"
              @click="openDetail(scope.row.id)"
          >
            详情
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
import * as OrderApi from '@/api/pay/order'
import Pagination from '@/components/Pagination/index.vue'
import ELComponent from '@/plugins/modal.js'
import download from '@/utils/download'
import { dateFormatter } from '@/utils/formatTime'

// 列表的加载中
const loading = ref(false)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  appId: null,
  channelCode: null,
  merchantOrderId: null,
  channelOrderNo: null,
  no: null,
  status: null,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()
// 导出等待
const exportLoading = ref(false)
// 支付应用列表集合
const appList = ref([])

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await OrderApi.getOrderPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await ELComponent.confirm('您确定要倒出数据吗？')
    // 发起导出
    exportLoading.value = true
    const data = await OrderApi.exportOrder(queryParams)
    download.excel(data, '支付订单.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 预览详情 */
const detailRef = ref()
const openDetail = (id) => {
  detailRef.value.open(id)
}

/** 初始化 **/
onMounted(async () => {
  await getList()
})
</script>

<style scoped>
.order-font {
  padding: 2px 0;
  font-size: 12px;
}
</style>
