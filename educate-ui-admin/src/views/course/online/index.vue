<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form
        ref="queryFormRef"
        :inline="true"
        :model="queryParams"
        label-width="68px"
        size="small"
    >
      <el-form-item label="课程信息" prop="info">
        <el-input
            v-model="queryParams.keyword"
            class="!w-240px"
            clearable
            placeholder="请输入课程信息"
        />
      </el-form-item>
      <el-form-item label="课程状态" prop="status">
        <el-select
            v-model="queryParams.status"
            clearable
            placeholder="请选择课程状态"
        >
          <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="课程分类" prop="category">
        <el-select
            v-model="queryParams.categoryId"
            clearable
            filterable
            placeholder="搜索课程分类"
            value-key="value">
          <el-option v-for="item in categoryIdList"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课程类型" prop="categoryId">
        <el-select
            v-model="queryParams.categoryType"
            clearable
            filterable
            placeholder="搜索课程类型"
            value-key="value">
          <el-option v-for="item in getIntDictOptions(DICT_TYPE.COURSE_AUTO_TYPE)"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
            v-model="queryParams.createTime"
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            end-placeholder="结束日期"
            range-separator="-"
            start-placeholder="开始日期"
            style="width: 340px"
            type="daterange"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item>
        <el-button icon="Search" type="primary" @click="handleQuery()">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery()">重置</el-button>
      </el-form-item>

    </el-form>

    <el-row :gutter="10" class="mb8">

      <el-col :span="1.5">
        <el-button
            v-hasPermi="['system:dept:create']"
            icon="Plus"
            plain
            size="small"
            type="primary"
            @click="createOrUpdateOpenForm('create')">新增</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
            icon="Sort"
            plain
            size="small"
            type="info"
            @click="toggleExpandAll()">删除课程</el-button>
      </el-col>

    </el-row>


    <!-- 列表 -->
    <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="list"
        row-key="id"
        @selection-change="handleSelectionChange"
    >
      <el-table-column prop="id" type="selection" width="40" />

      <el-table-column label="标题" prop="name">
        <template #default="{$index, row}">
          <a :href="row.img" :title="row.title">
            <div class="flex items-center">
              <div>
                <el-image :src="row.img" fit="contain" style="width: 100px; height: 100px" />
              </div>
              <div class="flex flex-col ml-3">
                <span class="font-bold">标题：{{ row.title }}</span>
                <span class="text-sm text-gray-400">编号：{{ row.id }}</span>
                <span class="text-sm text-gray-500">
                      <span class="mr-3">分类：{{ row.categoryTitle }}</span>
                      <span class="">
                        课程类型：
                        <span v-if="row.courseType == 1" class="text-green-500">基础课</span>
                        <span v-if="row.courseType == 2" class="text-red-500">进阶课</span>
                        <span v-if="row.courseType == 3" class="text-blue-500">面试课</span>
                        <span v-if="row.courseType == 4" class="text-black-500">实战课程</span>
                      </span>
                  </span>
                <span class="text-sm text-red-600">价格：{{row.price}}￥ / <span style="text-decoration: line-through">{{row.realPrice}}￥</span></span>
              </div>
            </div>
          </a>
        </template>
      </el-table-column>
      <el-table-column label="课程类型" prop="address" show-overflow-tooltip width="120">
        <template #default="scope">
          <el-select
              v-model="scope.row.courseType"
              placeholder="请选中课程分类"
              size="small"
              @change="handleUpdate(scope, 'coursetype')">
            <el-option
                v-for="item in getIntDictOptions(DICT_TYPE.COURSE_AUTO_TYPE)"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>

        </template>
      </el-table-column>


      <el-table-column label="创建时间" width="160">
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="状态" prop="status" width="80">
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              :inline-prompt="true"
              active-color="#FF358E"
              active-text="是"
              inactive-color=""
              inactive-text="否"
              @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>

      <el-table-column label="是否评论" prop="comment" width="80">
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.comment"
              :active-value="0"
              :inactive-value="1"
              :inline-prompt="true"
              active-color="#FF358E"
              active-text="是"
              inactive-color=""
              inactive-text="否"
              @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>

      <el-table-column label="是否热门" prop="isHot" width="80">
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.isHot"
              :active-value="1"
              :inactive-value="0"
              :inline-prompt="true"
              active-color="#FF358E"
              active-text="是"
              inactive-color=""
              inactive-text="否"
              @change="handleStatusChange(scope.row)"
          />
        </template>
      </el-table-column>



      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template v-slot="scope">
          <el-button
              v-hasPermi="['course:dept:update']"
              icon="edit"
              size="small"
              type="success"
              @click="handleChapterLesson(scope.$index, scope.row)">章节管理</el-button>
          <el-button
              color="#626aef"
              icon="edit"
              size="small"
              @click="handleCopy(scope.$index, scope.row)">复制</el-button>
          <el-button
              v-hasPermi="['course:dept:update']"
              icon="Edit"
              size="small"
              type="warning"
                     @click="createOrUpdateOpenForm('update', scope.row.id)">修改</el-button>
          <el-button
              v-hasPermi="['course:dept:delete']"
              icon="Delete"
              size="small"
              type="danger"
              @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <Pagination
        v-model:limit="queryParams.pageSize"
        v-model:page="queryParams.pageNo"
        :total="total"
        @pagination="getList"
    />

    <!--  表单弹窗：新增/修改  -->
    <OnlineFormCreateOrUpdate ref="formCreateRef" @success="getList" />

    <!--  新增节  -->
    <OnlineFormChapterLesson ref="formChapterLessonRef" @success="getList" />


  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import OnlineFormCreateOrUpdate from './OnlineFormCreateOrUpdate.vue'
import OnlineFormChapterLesson from './OnlineFormChapterLesson.vue'
import { parseTime } from '@/utils/ruoyi.js'
import { getCourseOnlineInfo } from '@/api/course/online/index.js'
import Pagination from '@/components/Pagination/index.vue'
import DictTag from '@/components/DictTag/index.vue'

// 列表的加载中
const loading = ref(true)
// 列表的数据
const list = ref()
const queryParams = reactive({
  name: undefined,
  status: undefined,
  categoryId: undefined,
  categoryType: undefined,
  createTime: [],
  pageNo: 1,
  pageSize: 10
})
// 搜索的表单
const queryFormRef = ref()
// 重新渲染表格状态
const refreshTable = ref(true)
// 总条数
const total = ref(0)

onMounted(async ()=>{
  await getList()
})

const getList = async ()=>{
  loading.value = true
  try {
    const response = await getCourseOnlineInfo(queryParams)
    list.value = response.data.list
    console.log('数据中的数据', list.value)
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

/** 添加/修改操作 */
const formCreateRef = ref()
const createOrUpdateOpenForm = (type, id)=>{
  formCreateRef.value.open(type, id)
}
/** 新添节操作 */
const formChapterLessonRef = ref()
const handleChapterLesson = (type,id, parentId)=>{
  formChapterLessonRef.value.open(type, id, parentId)
}
/** 搜索按钮操作 */
const handleQuery = ()=>{
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  // TODO j-sentinel 不知道为什么queryFormRef.value.resetFields()对name查询没有生效
  queryParams.name = undefined
  queryParams.pageNo = 1
  queryFormRef.value.resetFields()
  handleQuery()
}
/** 删除按钮操作 */
const handleDelete = async (row)=>{
  try {
    // 删除的二次确认
    await ELComponent.confirm(`您确定要删除 ${row.name} 吗？`)
    // 发起删除
    await deleteDept(row.id)
    ELComponent.msgSuccess('删除成功')
    // 刷新列表
    await getList()
  } catch {}
}
/** 修改状态 */
const handleStatusChange = (row) => {

}
</script>

<style scoped>

</style>
