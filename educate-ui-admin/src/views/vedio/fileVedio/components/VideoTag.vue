<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="1100">
   <div class='wrap'>
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="navleft">
          <div style="display:flex">
            <el-input
                placeholder="请输入标签标题或者标签"
                v-model.trim="queryParams.keyword"
                @keyup.enter.native="handleQuery"
                maxlength="20"
                size="small"
                prefix-icon="Search"
                style="margin:0 10px;"
            >
            </el-input>
            <el-button
                type="success"
                size="small"
                @click="getList()"
                icon="Search">
              搜索
            </el-button>
            <el-button
                icon="Refresh"
                size="small"
                @click="resetQuery()"
            >
              重置
            </el-button>
          </div>
          <div class="grid-content mt10">
            <el-table
                v-loading="loading"
                :data="resultList"
                style="width: 100%;border:1px solid #eee;">
              <el-table-column
                  width="160"
                  label="标签名称" align="center">
                <template v-slot="scope">
                  <span style="margin-left: 10px">{{ scope.row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column label="标签列表" align="center">
                <template v-slot="scope">
                  <template v-if="scope.row.tagList">
                    <el-tag
                        class="ml20 mt5"
                        v-for="tag in scope.row.tagList.split(',')"
                        effect="light"
                        style="cursor: pointer;border-radius: 10px"
                        @click="handleSelect(tag)"
                        :closable="selectTagList.findIndex(c=> c===tag) !== -1"
                        :type="selectTagList.findIndex(c=>c === tag) === -1? 'info' : 'primary'"
                        @close="handleUnSelect(tag)"
                        :key="tag"
                    >
                      {{tag}}
                    </el-tag>
                  </template>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <!-- 分页 -->
          <Pagination
              :total="total"
              v-model:page="queryParams.pageNo"
              v-model:limit="queryParams.pageSize"
              @pagination="getList"
          />
        </div>
      </el-col>
    </el-row>
  </div>
  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { loadDataTag } from '@/api/video/tag/index.js'
import Pagination from '@/components/Pagination/index.vue'
import ELComponent from '@/plugins/modal.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref()
// 加载中
const loading = ref(false)
// 列表的总页数
const total = ref(0)
// 存放选择的标签
const selectTagList = ref([])
const queryParams = reactive({
  keyword: '',
  pageNo: 1,
  pageSize: 15,
  type: 4,
  total: 0,
  pages: 0,

})
const resultList = ref([])

// 表单 Ref
const formRef = ref()

/** 查询标签列表 */
const getList = async () => {
  const response = await loadDataTag(queryParams)

  total.value = response.data.total

  resultList.value = response.data.list.map(tag => {
    tag.loading = false
    tag.tagList = tag.tagList.split(',').map(tag => {
      return tag.replace(/_(0|1)$/, '')
    }).join(',')
    return tag
  })
}

/** 打开弹窗 */
const open = async (type) => {
  dialogVisible.value = true
  dialogTitle.value = type
  loading.value = true
  try {
    await getList()
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
  queryParams.pageNo = 1
  queryParams.keyword = ""
  handleQuery()
}

// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
// 定义 success 事件，用于操作成功后的回调
const emit = defineEmits(['select'])

/** 选择开始进行绑定 */
const handleSelect = (tag) => {
  const index = selectTagList.value.findIndex(c => {
    return c === tag
  })
  if (index !== -1) {
    ELComponent.msgWarning("你已经选择了")
    return
  }
  const len = selectTagList.value.length
  if (len >= 10) {
    ELComponent.msgWarning("最多选择10个标签！")
    return
  }
  selectTagList.value.push(tag)
  ELComponent.msgSuccess("添加标签成功！")
  // 发送操作成功的事件
  emit('select',selectTagList.value)
}

/** 去掉选择 */
const handleUnSelect = (tag) => {
  const index = selectTagList.value.findIndex(c => {
    return c === tag
  })
  if (index !== -1) {
    selectTagList.value.splice(index, 1)
  }
  ELComponent.msgWarning("取消标签成功！")
  // 发送操作成功的事件
  emit('select',selectTagList.value)
}


</script>

<style scoped>
.navleft {
  border: 1px solid #eee;
  background: #fafafa;
  padding: 20px;
}
</style>
