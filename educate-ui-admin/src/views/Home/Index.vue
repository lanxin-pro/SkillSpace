<template>
  <div class="app-container">



    <el-table v-loading="loading" :data="list">
      <el-table-column label="日志编号" align="center" prop="id" />
      <el-table-column label="操作模块" align="center" prop="module" />
      <el-table-column label="操作名" align="center" prop="name" width="180" />
      <el-table-column label="操作类型" align="center" prop="type">
        <template v-slot="scope">
          <dict-tag  :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="操作人" align="center" prop="userNickname" />
      <el-table-column label="操作结果" align="center" prop="status">
        <template v-slot="scope">
          <span>{{ scope.row.resultCode === 0 ? '成功' : '失败' }}</span>
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row,scope.index)"
                     v-hasPermi="['system:operate-log:query']">详细</el-button>
        </template>
      </el-table-column>
    </el-table>


  </div>
</template>

<script setup>
import { listOperateLog } from "@/api/system/operatelog"
import { ref,reactive,onMounted } from 'vue'
import { parseTime } from '@/utils/ruoyi.js'

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
const getList = async ()=>{
  loading.value = true
  const response = await listOperateLog(queryParams)
  console.log(response.data)
  list.value = response.data.list
  total.value = response.data.total
  loading.value = false

}
</script>

<style scoped>

</style>
