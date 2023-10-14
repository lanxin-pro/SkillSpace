<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
        size="small"
    >
      <el-form-item label="是否已读" prop="readStatus">
        <el-select
            v-model="queryParams.readStatus"
            placeholder="请选择状态"
            clearable
            class="!w-240px"
        >
          <el-option
              v-for="dict in getBoolDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="发送时间" prop="createTime">
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
        <el-button
            @click="handleQuery"
            icon="search"
            type="primary"
        >
          搜索
        </el-button>
        <el-button
            @click="resetQuery"
            icon="refresh"
            type="info"
        >
          重置
        </el-button>
        <el-button
            @click="handleUpdateList"
            icon="Reading"
            type="warning"
        >
          标记已读
        </el-button>
        <el-button
            @click="handleUpdateAll"
            icon="reading"
            type="danger"
        >
          全部已读
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <el-table
        v-loading="loading"
        :data="list"
        ref="tableRef"
        row-key="id"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" :selectable="selectable" :reserve-selection="true" />
      <el-table-column label="发送人" align="center" prop="templateNickname" />
      <el-table-column
          label="发送时间"
          align="center"
          prop="createTime"
          width="200"
          :formatter="dateFormatter"
      />
      <el-table-column label="类型" align="center" prop="templateType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_NOTIFY_TEMPLATE_TYPE" :value="scope.row.templateType" />
        </template>
      </el-table-column>
      <el-table-column
          label="消息内容"
          align="center"
          prop="templateContent"
          show-overflow-tooltip
          width="260"
      />
      <el-table-column label="是否已读" align="center" prop="readStatus" width="160">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.readStatus" />
        </template>
      </el-table-column>
      <el-table-column
          label="阅读时间"
          align="center"
          prop="readTime"
          width="200"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center" width="160">
        <template #default="scope">
          <el-button
              link
              :type="scope.row.readStatus ? 'primary' : 'warning'"
              @click="openDetail(scope.row)"
          >
            {{ scope.row.readStatus ? '详情' : '已读' }}
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

    <!-- 表单弹窗：详情 -->
    <MyNotifyMessageDetail ref="detailRef" />
  </div>
</template>

<script setup>
import { DICT_TYPE, getBoolDictOptions } from '@/utils/dict.js'
import { dateFormatter } from '@/utils/formatTime.js'
import { ref,reactive,onMounted } from 'vue'
import ELComponent from '@/plugins/modal.js'
import Pagination from '@/components/Pagination/index.vue'
import {
  getMyNotifyMessagePage,
  updateNotifyMessageRead,
  updateAllNotifyMessageRead
} from '@/api/system/notify/message/index.js'
import MyNotifyMessageDetail from './MyNotifyMessageDetail.vue'
import DictTag from '@/components/DictTag/index.vue'

// 列表的加载中
const loading = ref(true)
// 列表的总页数
const total = ref(0)
// 列表的数据
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 15,
  readStatus: undefined,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()
// 表格的 Ref
const tableRef = ref()
// 表格的选中 ID 数组
const selectedIds = ref([])

/** 初始化 **/
onMounted(() => {
  getList()
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const response = await getMyNotifyMessagePage(queryParams)
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
  tableRef.value.clearSelection()
  handleQuery()
}

/** 详情操作 */
const detailRef = ref()
const openDetail = (data) => {
  // 判断阅读状态
  if (!data.readStatus) {
    // 已读
    handleReadOne(data.id)
  }else{
    // 打开详情
    detailRef.value.open(data)
  }
}

/** 标记一条站内信已读 */
const handleReadOne = async (id) => {
  await updateNotifyMessageRead(id)
  await getList()
}

/** 标记全部站内信已读 **/
const handleUpdateAll = async () => {
  await updateAllNotifyMessageRead()
  ELComponent.msgSuccess('全部已读成功！')
  tableRef.value.clearSelection()
  await getList()
}

/** 标记一些站内信已读 **/
const handleUpdateList = async () => {
  if (selectedIds.value.length === 0) {
    return
  }
  await updateNotifyMessageRead(selectedIds.value)
  ELComponent.msgSuccess('批量已读成功！')
  tableRef.value.clearSelection()
  await getList()
}

/** 某一行，是否允许选中 */
const selectable = (row) => {
  return !row.readStatus
}

/** 当表格选择项发生变化时会触发该事件  */
const handleSelectionChange = (array) => {
  selectedIds.value = []
  if (!array) {
    return
  }
  array.forEach((row) => selectedIds.value.push(row.id))
}
</script>

<style scoped>

</style>
