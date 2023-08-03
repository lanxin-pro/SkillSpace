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
      <el-form-item label="文件路径" prop="path">
        <el-input
            v-model="queryParams.path"
            placeholder="请输入文件路径"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件类型" prop="type" width="80">
        <el-input
            v-model="queryParams.type"
            placeholder="请输入文件类型"
            clearable
            @keyup.enter="handleQuery"
        />
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
        <el-button type="primary" plain icon="Plus" size="small" @click="openForm()">上传文件</el-button>
      </el-col>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="list">
      <el-table-column label="文件名" align="center" prop="name">
        <template #default="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.url"
              fit="contain"
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="文件路径" align="center" prop="path" :show-overflow-tooltip="true" />
      <el-table-column label="URL" align="center" prop="url" :show-overflow-tooltip="true" />
      <el-table-column
          label="文件大小"
          align="center"
          prop="size"
          width="120"
          :formatter="fileSizeFormatter"
      />
      <el-table-column label="文件类型" align="center" prop="type" width="180px" />
      <el-table-column
          label="上传时间"
          align="center"
          prop="createTime"
          width="180"
          :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
              link
              type="primary"
              size="small"
              icon="View"
              @click="handlePreview(scope.row)"
              v-hasPermi="['infra:config:delete']"
          >
            预览
          </el-button>
          <el-button
              link
              type="success"
              size="small"
              icon="Download"
              @click="handleDownload(scope.row)"
              v-hasPermi="['infra:config:delete']"
          >
            下载
          </el-button>
          <el-button
              link
              type="danger"
              size="small"
              icon="Delete"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['infra:config:delete']"
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

    <!-- 上传文件 -->
    <FileForm ref="formRef" @success="getList" />
    <PreviewFile ref="previewRef" />
  </div>
</template>

<script setup>
import { ref,reactive,onMounted} from 'vue'
import FileForm from './FileForm.vue'
import PreviewFile from './PreviewFile.vue'
import { getFilePage,deleteFile } from '@/api/infra/file/index.js'
import Pagination from '@/components/Pagination/index.vue'
import { dateFormatter } from '@/utils/formatTime'
import { fileSizeFormatter } from '@/utils'
import ELComponent from '@/plugins/modal.js'

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
  type: undefined,
  createTime: []
})
// 搜索的表单
const queryFormRef = ref()

/** 初始化 **/
onMounted(() => {
  getList()
})
/** 预览 **/
const previewRef = ref()
const handlePreview = (row)=>{
  previewRef.value.open(row)
}
/** 查询列表 */
const getList = async ()=>{
  loading.value = true
  try {
    const response = await getFilePage(queryParams)
    list.value = response.data.list
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}
/** 上传文件操作 */
const formRef = ref()
const openForm = () => {
  formRef.value.open()
}
/** 下载 */
const handleDownload = (row)=>{
  fetch(row.url)
      .then(response => response.blob())
      .then(blob => {
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = row.path
        link.click()
      })
}
/** 删除按钮操作 */
const handleDelete = async (id) => {
  try {
    // 删除的二次确认
    await ELComponent.confirm('您确定要删除吗？')
    // 发起删除
    await deleteFile(id)
    ELComponent.msgSuccess('删除成功')
    // 刷新列表
    await getList()
  } catch {}
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

</script>

<style scoped>
.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 30px;
}
.el-table .cell {
  padding: 0;
}
</style>
