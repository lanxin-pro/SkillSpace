<template>
  <div class="app-container">
    <!--  搜索相关  -->
    <div class="pug-search">
      <div>
        <el-button icon="Plus" size="small" type="primary" @click="handleOpenDialog">
          新增分类
        </el-button>
        <el-button icon="CloseBold" size="small" type="danger" @click="handleDeleteBatch">批量删除</el-button>
      </div>

      <!-- 这里的源码，里面尽然还包裹了一层div -->
      <div class="flex items-center">
        <el-input
            v-model.trim="keyword"
            class="w-50 m-2"
            placeholder="请输入搜索的名字"
            prefix-icon="Search"
            size="small"
        >
        </el-input>
        <el-button size="small" type="primary" @click="loadData">搜索</el-button>
      </div>
    </div>
    <!--  表格相关  -->
    <div>
      <el-table
          :border="parentBorder"
          :data="tableData"
          :default-expand-all="false"
          style="width: 100%"
          @selection-change="selectTableAll">

        <!--    子项      -->
        <el-table-column type="expand">
          <template #default="props">
            <div class="ml-12">
              <el-table
                  :cell-style="{'background':'#fafafa'}"
                  :data="props.row.children"
                  :show-header="false"
                  @selection-change="selectTableAll">
                <el-table-column prop="id" type="selection"/>
                <el-table-column label="主键" prop="id" />
                <el-table-column label="名字">
                  <template #default="scope">
                    <div v-if="scope.row.edit">
                      <el-input :value="scope.row.title" size="small"></el-input>
                    </div>
                    <div v-else>
                      <span style="color: #3655b3">{{scope.row.title}}</span>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="排序" prop="sorted" />
                <el-table-column label="状态" prop="status">
                  <template #default="scope">
                    <el-tag v-if="scope.row.status==1" type="success">已发布</el-tag>
                    <el-tag v-else type="info">未发布</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="删除状态" prop="isdelete">
                  <template #default="scope">
                    <el-tag v-if="scope.row.isdelete==1" type="danger">已删除</el-tag>
                    <el-tag  v-else type="warning">未删除</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="300">
                  <template #default="scope">
                    <el-button size="small" type="info" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    <el-button
                        size="small"
                        type="danger"
                        @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-table-column>

        <!-- 父项 -->
        <el-table-column prop="id" type="selection"/>
        <el-table-column label="主键" prop="id" />
        <el-table-column label="名字">
          <template #default="scope">
            <div v-if="scope.row.edit">
              <el-input   v-model.trim="scope.row.title" size="small" style="width: 80%"></el-input>
            </div>
            <div v-else>
              <span style="color: #3655b3">{{scope.row.title}}</span>
              <span class="font-bold ml-2" style="color: #ffc577;"> ({{scope.row.children.length}})</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="排序">
          <template #default="scope">
            <div v-if="scope.row.edit">
              <el-input-number
                  v-model="scope.row.sorted"
                  :max="30"
                  :min="1"
                  :step="1"
                  size="small"
                  style="width: 50%"></el-input-number>
            </div>
            <div v-else>
              <span>{{scope.row.sorted}}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status">
          <template #default="scope">
            <div v-if="scope.row.edit">
              <el-switch
                  v-model="scope.row.status"
                  :active-value="1"
                  :inactive-value="0"
                  :inline-prompt="false"
                  active-color="rgb(174, 199, 58)"
                  active-text="发布"
                  inactive-color=""
                  inactive-text="不发布"
                  size="small"
              />
            </div>
            <div v-else>
              <el-tag v-if="scope.row.status==1" type="success">已发布</el-tag>
              <el-tag v-else type="info">未发布</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="删除状态" prop="isdelete">
          <template #default="scope">
            <el-tag v-if="scope.row.isdelete==1" type="danger">已删除</el-tag>
            <el-tag v-else type="warning">未删除</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300">
          <template #default="scope">
            <el-button v-if="!scope.row.edit" size="small" type="success" @click="handleAddChild(scope.$index, scope.row)">添加子项</el-button>
            <el-button v-if="!scope.row.edit" size="small" type="info" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button v-if="!scope.row.edit" size="small" type="info" @click="handleEdit2(scope.$index, scope.row)">快速编辑</el-button>
            <el-button v-if="scope.row.edit" size="small" type="info" @click="saveEdit(scope.$index, scope.row)">保存编辑</el-button>
            <el-button v-if="scope.row.edit" size="small" type="info" @click="cancleEdit(scope.$index, scope.row)">取消保存</el-button>
            <el-button
                v-if="!scope.row.edit"
                size="small"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <Dialog
        v-model="dialogVisible"
        :dialogEnableStatus="true"
        :lockScroll="true"
        :title="dialogTitle"
        maxHeight="700px"
        width="70%">
      <el-container class="w-screen-xl">
        <el-form
            ref="categoryFormRef"
            :model="formData.category"
            :rules="rules"
            style="width: 100%">
          <el-col :span="24">
            <!--  emitPath一次只返回一次    checkStrictly是否严格的遵守父子节点不互相关联（等于有个小原点）          -->
            <el-form-item label="分类父级：" prop="pid" style="width: 100%">
              <el-cascader
                  v-model="formData.category.pid"
                  :clearable="true"
                  :disabled="formData.category.disabled"
                  :options="categoryDataList"
                  :props="{label:'title',value:'id',children:'children',checkStrictly:true,emitPath:false}"
                  filterable
                  placeholder="请选择父级"
                  @change="handleCategoryPid"
              />
            </el-form-item>
          </el-col>

          <el-row :gutter="20" style="width: inherit">
            <el-col :span="24">
              <el-form-item label="分类名称：" prop="name" style="width: 55%">
                <el-input
                    v-model="formData.category.title"
                    maxlength="22"
                    placeholder="请输入分类名称"></el-input>
              </el-form-item>
            </el-col>



            <el-col :span="24">
              <el-form-item label="分类状态：" prop="status" style="width: 100%">
                <el-radio-group
                    v-model="formData.category.status"
                    fill="#FF358E"
                    text-color="#FF358E">
                  <el-radio
                      v-for="item in [{name:'发布',value:1},{name:'未发布',value:0}]"
                      :key="item.key"
                      :label="item.value">{{item.name}}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="删除状态：" prop="status" style="width: 100%">
                <el-radio-group
                    v-model="formData.category.isdelete"
                    fill="#FF358E"
                    text-color="#FF358E">
                  <el-radio
                      v-for="item in [{name:'删除',value:1},{name:'未删除',value:0}]"
                      :key="item.key"
                      :label="item.value">{{item.name}}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="分类排序：" prop="sorted" style="width: 100%">
                <el-input-number
                    v-model="formData.category.sorted"
                    :max="30"
                    :min="1"
                    :step="1"
                    placeholder="请输入课程有效天数"
                    style="width: 30%"></el-input-number>
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="分类描述：" prop="status" style="width: 100%">
                <el-input
                    v-model="formData.category.description"
                    :rows="3"
                    placeholder="请输入分类名称"
                    type="textarea">

                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

      </el-container>

      <template #footer>
        <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </template>
    </Dialog>

  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { findCategoryTree, saveUpdateCategory, deleteCategoryById, deleteCategoryBatch } from '@/api/course/category/index.js'
import Dialog from '@/components/Dialog/index.vue'
import ELComponent from '@/plugins/modal.js'

// 弹窗的是否展示
let dialogVisible = ref(false)
// 弹窗的标题
const dialogTitle = ref('')
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
// 表单的类型：create - 新增；update - 修改
const formType = ref('')
/** 添加/修改操作 */
const formCreateRef = ref()
const createOrUpdateOpenForm = (type, id)=>{
  formCreateRef.value.open(type, id)
}

// 表格相关
const pageNo = ref(1)
const pageSize = ref(10)
const tableData = ref([])
const total = ref(0)
const keyword = ref("")

// 加载默认数据 因为没有分页的信息，所以放在一起也是可以的
const loadData = async()=>{
  const serverResponse = await findCategoryTree(keyword.value)
  // tableData.value = serverResponse.data
  // 如果不给后端增加字段，就需要在前端这样加
  tableData.value = handleBussort(serverResponse)
}
// 递归增加数据
const handleBussort = (arr)=>{
  const handleCallback = (itemArr)=>{
    itemArr.forEach(item=>{
      // 递归给所有的子元素增加属性
      item.auth2 = false
      if(item.children.length>0){
        handleCallback(item.children)
      }
    })
  }
  handleCallback(arr)
  return arr
}

// 生命周期初始化
onMounted(()=>{
  loadData()
})







// 表单相关
// 获取dialog的弹窗对象
const categoryDialogRef = ref(null)
// 用来装载父级分类的数据信息
const categoryDataList = ref([])
const categoryFormRef = ref(null)
const batchIdsRef = ref([])
const formData = reactive({
  category:{
    id: "",
    title: "",
    description: 1,
    sorted: 1,
    pid: 0,
    status: 1,
    isdelete: 0,
    disabled: false
  }
})
// 重置方法
const reset = ()=>{
  formData.category = {
    id: "",
    title: "",
    description: 1,
    sorted: 1,
    pid: 0,
    status: 1,
    isdelete: 0,
    disabled: false
  }
}
// 级联清空为null的情况
const handleCategoryPid = (item)=>{
  if(!item){
    formData.category.pid = 0
  }
}

// 校验
const rules = reactive({

})

// 提交
const submitForm = ()=>{
  categoryFormRef.value.validate(async (valid)=>{
    if(!valid){
      return ELComponent.msgError("输入不合法")
    }
    try {
      const serverResponse = await saveUpdateCategory(formData.category)
      ELComponent.msg(formData.category.id ? "修改成功" : "添加成功")
      // 关闭弹窗
      dialogVisible.value = false
      // 重新加载数据
      loadData()
    }catch (err){
      console.log(err)
      ELComponent.msgError(err.msg)
    }
  })
}
// 添加子项分类
const handleAddChild = ($index,row)=>{
  // 渲染父级的数据
  categoryDataList.value = tableData.value
  // 打开弹窗
  dialogVisible.value = true
  // 排序，方便累加
  formData.category.sorted = tableData.value[$index].children.length + 1
  // 父Id的一个赋值
  formData.category.pid = row.id
  // 无法选中父类
  formData.category.disabled = true
}
// 编辑
const handleEdit = ($index,row)=>{
  // 渲染父级的数据
  categoryDataList.value = tableData.value
  // 打开弹窗
  dialogVisible.value = true
  // 数据回传
  formData.category = {...row}
  if(row.pid=="0"){
    formData.category.disabled = true
  }
}
// 快速编辑的数据备份
const rowBaseRef = ref({})
// 快速编辑
const handleEdit2 = ($index,row)=>{
  rowBaseRef.value = {...row}
  row.edit = true
}
// 保存编辑
const saveEdit = async ($index,row)=>{
  const serverResponse = await categoryService.saveUpdateCategory(row)
  toast("编辑成功")
  // 还原编辑
  row.edit = false
  loadData()
}
// 取消编辑
const cancleEdit = ($index,row)=>{
  row.edit = false
  row.title = rowBaseRef.value.title
  row.sorted = rowBaseRef.value.sorted
  row.status = rowBaseRef.value.status
}
// 单个删除
const handleDelete = async ($index,row)=>{
  if(row.children.length>0){
    await confirm("您确定要删除该父类下面所有子集吗？")
  }
  await confirm("您确定要删除吗？")
  try{
    const serverResponse = categoryService.deleteCategoryById(row.id)
    toast("删除成功")
    row.isdelete = 1
  }catch (err){
    toastError(err.msg)
  }
}
// 获取批量删除的元素
const selectTableAll = (items)=>{
  batchIdsRef.value = items.map((item)=>{
    return item.id
  })
}
// 批量删除
const handleDeleteBatch = async ()=>{
  if(batchIdsRef.value.length==0){
    toastError("请选中删除元素")
    return
  }
  await confirm("您确定要进行批量删除吗？")
  try{
    const batchIds = batchIdsRef.value.join(",")
    const serverResponse = await categoryService.deleteCategoryBatch(batchIds)
    toast("删除成功")
    loadData()
  }catch (err){
    toastError(err.msg)
  }
}
// 开打分类保存
const handleOpenDialog = ()=>{
  reset()
  // 分类父级的查询
  categoryDataList.value = tableData.value
  // 排序
  formData.category.sorted = tableData.value.length + 1
  dialogVisible.value = true
}
</script>

<style scoped>
.pug-table-box{
}
.pug-table-box .pug-search{
  @apply shadow shadow-light-800
  padding: 4px 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pug-table-box .pug-search button{
  padding: 6px;
}
.pug-table-box .pug-page-box{
  display: flex;
  justify-content: center;
  margin-top: 10px;
}
/*表单的 hover */
.pug-table .el-table{
  --el-table-row-hover-bg-color: #f89b82
}
</style>
