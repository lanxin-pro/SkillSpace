<template>
  <div class="app-container">


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
import { listOperateLog } from "@/api/system/operatelog"
import { ref,reactive,onMounted } from 'vue'
import { parseTime } from '@/utils/ruoyi.js'
import Pagination from '@/components/Pagination/index.vue'
import DictTag from '@/components/DictTag/index.vue'
import OperateLogDetail from '@/views/system/operatelog/OperateLogDetail.vue'

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
</script>

<style scoped>

</style>
