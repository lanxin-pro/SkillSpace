<template>
  <div class="app-container">

    <el-row :gutter='20'>
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span class="text-orange-500 text-lg font-bold">🎫 视频相关</span>
            </div>
          </template>
          <div class="text-item" style="display:flex;justify-content:space-between">
            <div>
              <span>视频未发布：</span>
              <CountTo :startVal="1" :endVal="21" />
            </div>
            <div>
              <span>视频发布：</span>
              <CountTo :startVal="1" :endVal="160" />
            </div>
            <div>
              <span>陌陌视频：</span>
              <CountTo :startVal="1" :endVal="2" />
            </div>
            <div>
              <span>后台视频：</span>
              <CountTo :startVal="1" :endVal="5" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span class="text-blue-500 text-lg font-bold">🙎 用户注册</span>
            </div>
          </template>
          <div class="text-item" style="display:flex;justify-content:space-between">
            <div>
              <span>普通用户：</span>
              <CountTo :startVal="1" :endVal="21" />
            </div>
            <div>
              <span>游客用户：</span>
              <CountTo :startVal="1" :endVal="212123" />
            </div>
            <div>
              <span>主播：</span>
              <CountTo :startVal="1" :endVal="121" />
            </div>
            <div>
              <span>家族长：</span>
              <CountTo :startVal="1" :endVal="251" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span class="text-red-500 text-lg font-bold">💰 收入统计</span>
            </div>
          </template>
          <div class="text-item" style="display:flex;justify-content:space-between">
            <div>
              <span>充值总额：</span>
              <CountTo :startVal="1" :endVal="6283151" />
            </div>
            <div>
              <span>充值人数：</span>
              <CountTo :startVal="1" :endVal="9651" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="box-card" style="margin:10px 0;">
      <template #header>
        <span style="font-weight: bolder">后台操作日志</span>
      </template>


      <!-- 搜索工作栏 -->
      <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
      >
        <el-form-item label="系统模块" prop="title">
          <el-input v-model="queryParams.title" placeholder="请输入系统模块" clearable style="width: 240px;"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item label="操作人员" prop="operName">
          <el-input v-model="queryParams.operName" placeholder="请输入操作人员" clearable style="width: 240px;"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
<!--        <el-form-item label="类型" prop="type">
          <el-select v-model="queryParams.type" placeholder="操作类型" clearable style="width: 240px">
            <el-option v-for="dict in this.getDictDatas(DICT_TYPE.SYSTEM_OPERATE_TYPE)" :key="parseInt(dict.value)"
                       :label="dict.label" :value="parseInt(dict.value)"/>
          </el-select>
        </el-form-item>-->
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.success" placeholder="操作状态" clearable style="width: 240px">
            <el-option :key="true" label="成功" :value="true"/>
            <el-option :key="false" label="失败" :value="false"/>
          </el-select>
        </el-form-item>
        <el-form-item label="操作时间" prop="startTime">
          <el-date-picker
              v-model="queryParams.startTime"
              style="width: 240px"
              value-format="YYYY-MM-DD HH:mm:ss"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          />
          <el-button class="ml-5" type="success" size="small" @click="handleChangeDate(0)">今天</el-button>
          <el-button type="success" size="small" @click="handleChangeDate(1)">昨日</el-button>
          <el-button type="success" size="small" @click="handleChangeDate(7)">7天</el-button>
          <el-button type="success" size="small" @click="handleChangeDate(30)">30天</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          <el-button type="warning" icon="Download" size="small" @click="handleExport" :loading="exportLoading"
                     v-hasPermi="['system:operate-log:export']">导出</el-button>
        </el-form-item>
      </el-form>


      <el-table v-loading="loading" :data="list">
        <el-table-column label="日志编号" align="center" prop="id" />
        <el-table-column label="操作模块" align="center" prop="module" />
        <el-table-column label="操作名" align="center" prop="name" width="180" />
  <!--      <el-table-column label="操作类型" align="center" prop="type">
          <template v-slot="scope">
            <DictTag  :value="scope.row.type"/>
          </template>
        </el-table-column>-->
        <el-table-column label="操作人" align="center" prop="userNickname" />
        <el-table-column label="操作IP" align="center" prop="userIp" />
        <el-table-column label="操作结果" align="center" prop="status">
          <template v-slot="scope">
            <el-tag :type="scope.row.resultCode === 0 ? '' : 'error'">
              {{ scope.row.resultCode === 0 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作日期" align="center" prop="startTime" width="180">
          <template v-slot="scope">
            <span>{{ parseTime(scope.row.startTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="执行时长" align="center" prop="startTime">
          <template v-slot="scope">
            <span>{{ scope.row.duration }}  ms</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center"  class-name="small-padding fixed-width">
          <template v-slot="scope">
            <el-button size="small" type="text" icon="View" @click="handleView(scope.row,scope.index)"
                       v-hasPermi="['system:operate-log:query']">详细</el-button>
          </template>
        </el-table-column>
      </el-table>

      <Pagination
          v-model:limit="queryParams.pageSize"
          v-model:page="queryParams.pageNo"
          :total="total"
          @pagination="getList"
      />


      <!-- 操作日志详细 -->
      <OperateLogDetail ref="detailRef" />
    </el-card>
  </div>
</template>

<script setup>
import { listOperateLog, exportApiAccessLog } from "@/api/system/operatelog"
import { ref,reactive,onMounted } from 'vue'
import { parseTime } from '@/utils/ruoyi.js'
import Pagination from '@/components/Pagination/index.vue'
import DictTag from '@/components/DictTag/index.vue'
import OperateLogDetail from '@/views/system/operatelog/OperateLogDetail.vue'
import CountTo from '@/components/CountTo/index.vue'
import { dayDateFormatter } from '@/utils/formatTime.js'
import ELComponent from '@/plugins/modal.js'
import download from '@/utils/download.js'

const detailRef = ref()
const queryForm = ref()
// 遮罩层
const loading = ref(true)
// 导出遮罩层
const exportLoading = ref(false)
// 显示搜索条件
const showSearch = ref(true)
// 总条数
const total = ref(0)
// 表格数据
const list = ref([])
// 是否显示弹出层
const open = ref(false)
// 类型数据字典
const typeOptions = ref([])
// 表单参数
const form = ref({})
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  title: undefined,
  operName: undefined,
  businessType: undefined,
  status: undefined,
  startTime: []
})
onMounted(()=>{
  getList()
})
// 查询登录日志
const getList = async ()=>{
  loading.value = true
  const response = await listOperateLog(queryParams)
  list.value = response.data.list
  total.value = response.data.total
  loading.value = false
}
// 搜索按钮操作
const handleQuery = async ()=>{
  queryParams.pageNo = 1
  await getList()
}
// 重置按钮操作
const resetQuery = async ()=>{
  queryForm.value.resetFields()
  await handleQuery()
}
const handleView = async (row)=>{
  detailRef.value.open(row)
}
// 切换
const handleChangeDate = (num)=>{
  const value1 = dayDateFormatter(num, 'day')
  if (value1.length > 0) {
    queryParams.startTime = value1
  }
  getList()
}
// excel的数据导出
const handleExport = async () => {
  try {
    // 导出的二次确认
    await ELComponent.confirm("是否确认导出数据项")
    // 发起导出
    exportLoading.value = true
    const data = await exportApiAccessLog(queryParams)
    download.excel(data, 'API 访问日志.xlsx')
  } catch {
  } finally {
    exportLoading.value = false
  }
}
</script>

<style scoped>
.text-item{
  font: normal 14px/24px Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;
  -moz-osx-font-smoothing: grayscale;
  -webkit-font-smoothing: antialiased;
  text-rendering: optimizeLegibility;
  color: #0e0000;
  overflow: hidden;
  padding: 0 !important;
}
.card-header{
  padding: 10px 4px
}
</style>
