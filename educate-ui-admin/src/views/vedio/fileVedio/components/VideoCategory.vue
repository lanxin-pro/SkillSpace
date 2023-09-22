<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="800">

    <div class='wrap'>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="grid-content mt10">
            <el-table
                v-loading="formLoading"
                :data="categoryList"
                row-key="id"
                border
                ref="categoryTale"
                default-expand-all
                :tree-props="{children: 'childrenList', hasChildren: 'hasChildren'}"
                style="width: 100%;border:1px solid #eee;">
              <el-table-column label="分类名称" align="center">
                <template v-slot="scope">
                  <span style="margin-left: 10px" class="fw">{{ scope.row.categoryName }}</span>
                  <span v-if="scope.row.childrenList && scope.row.childrenList.length > 0" class="fw">
                    ({{scope.row.childrenList.length}})
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template v-slot="scope">
                  <el-button
                      size="small"
                      type="primary"
                      icon="Plus"
                      v-if="scope.row.parentId > 0 && !scope.row.selected"
                      @click="handleSelect(scope.$index, scope.row)">选择
                  </el-button>
                  <el-button
                      size="small"
                      type="danger"
                      icon="Close"
                      @click="handleUnSelect(scope.$index, scope.row)"
                      v-if="scope.row.parentId > 0 && scope.row.selected">删除选择
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
      </el-row>
    </div>

  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict.js'
import { getSimpleMenusList,getMenu,createMenu,updateMenu } from '@/api/system/menu/index.js'
import { loadDataTree } from '@/api/video/category/index.js'
import { CommonStatusEnum, SystemMenuTypeEnum } from '@/utils/constants.js'
import ElComponent from '@/plugins/modal.js'
import Editor from '@/components/Editor/index.vue'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref('')
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
const categoryList = ref([])
const expandedRowKeys = ref([])
const selectParentList = ref([])


const formRules = reactive({
  title: [{ required: true, message: '公告标题不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '公告类型不能为空', trigger: 'change' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }],
  content: [{ required: true, message: '公告内容不能为空', trigger: 'blur' }]
})
// 表单 Ref
const formRef = ref()

/** 打开弹窗 */
const open = async (type) => {
  dialogVisible.value = true
  dialogTitle.value = type
  formLoading.value = true
  try {
    const response = await loadDataTree()

    let categoryListLet = response.data
    if (categoryListLet && categoryListLet.length > 0) {
      categoryList.value = categoryListLet.map(c => {
        c.selected = false
        c.childrenList.map(cc => {
          cc.selected = false
          return cc
        })
        return c
      })
    }
  } finally {
    formLoading.value = false
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
// 定义 success 事件，用于操作成功后的回调
const emit = defineEmits(['select'])

// 选中的元素
const selectChildrenList = ref([])
/** 选择 */
const handleSelect = (index, row) => {
  // 获取当前自己的ID
  const childrenId = row.id
  const childrenCategoryName = row.categoryName
  // 获取父亲ID
  const parentId = row.parentId
  // 父亲分类的名称
  const parentCategoryName = categoryList.value.find(category => {
    return category.id === parentId
  }).categoryName

  // 查询是否已经添加了
  const cindex = selectChildrenList.value.findIndex(children => {
    return children.id === childrenId
  })
  // selectChildrenList没有就是 -1 那么就开始添加
  if (cindex === -1 && !row.selected) {
    row.selected = true
    selectChildrenList.value.push({
      id: childrenId,
      name: childrenCategoryName,
      pid: parentId,
      pname: parentCategoryName
    })
  }
  // 判断父是不是已经添加过了
  const pindex = selectParentList.value.findIndex(c => {
    return c.id === parentId
  })
  if (pindex === -1) {
    selectParentList.value.push({ id: parentId, name: parentCategoryName })
  }


  ElComponent.msgSuccess("选择分类成功！")
  // 发送操作成功的事件
  emit('select',[selectChildrenList.value, selectParentList.value])
}

/** 反选 */
const handleUnSelect = (index , row) => {
  // 获取当前自己的ID
  const childrenId = row.id
  // 获取父亲ID
  const parentId = row.parentId

  const cindex = selectChildrenList.value.findIndex(c => {
    return c.id === childrenId
  })
  if (cindex !== -1 && row.selected) {
    row.selected = false
    selectChildrenList.value.splice(cindex, 1)
  }

  // 判断父是不是已经添加过了
  const pindex = selectParentList.value.findIndex(c => {
    return c.id === parentId
  })
  const ccindex = selectChildrenList.value.findIndex(cc => {
    return cc.pid === parentId
  })
  if (pindex !== -1 && ccindex === -1) {
    selectParentList.value.splice(pindex, 1)
  }

  ElComponent.msgSuccess("取消分类！")
  // 发送操作成功的事件
  emit('select',[selectChildrenList.value, selectParentList.value])
}
</script>

<style scoped>

</style>
