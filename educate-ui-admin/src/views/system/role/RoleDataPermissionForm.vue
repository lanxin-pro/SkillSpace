<template>
  <Dialog v-model="dialogVisible" title="菜单权限" width="800">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" label-width="80px">
      <el-form-item label="角色名称">
        <el-tag>{{ formData.name }}</el-tag>
      </el-form-item>
      <el-form-item label="角色标识">
        <el-tag>{{ formData.code }}</el-tag>
      </el-form-item>
      <el-form-item label="权限范围">
        <el-select v-model="formData.dataScope">
          <el-option
              v-for="item in getIntDictOptions(DICT_TYPE.SYSTEM_DATA_SCOPE)"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <el-form-item
        v-if="formData.dataScope === SystemDataScopeEnum.DEPT_CUSTOM"
        label="权限范围"
        style="display: flex"
    >
      <el-card class="card" shadow="never">
        <template #header>
          全选/全不选:
          <el-switch
              v-model="treeNodeAll"
              active-text="是"
              inactive-text="否"
              inline-prompt
              @change="handleCheckedTreeNodeAll()"
          />
          全部展开/折叠:
          <el-switch
              v-model="deptExpand"
              active-text="展开"
              inactive-text="折叠"
              inline-prompt
              @change="handleCheckedTreeExpand"
          />
          父子联动(选中父节点，自动选择子节点):
          <el-switch v-model="checkStrictly" active-text="是" inactive-text="否" inline-prompt />
        </template>
        <el-tree
            ref="treeRef"
            :check-strictly="!checkStrictly"
            :data="deptOptions"
            :props="defaultProps"
            default-expand-all
            empty-text="加载中，请稍后"
            node-key="id"
            show-checkbox
        />
      </el-card>
    </el-form-item>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import ELComponent from '@/plugins/modal.js'
import { CommonStatusEnum,SystemDataScopeEnum } from '@/utils/constants.js'
import { listSimpleDepts } from '@/api/system/dept.js'
import { handleTree,defaultProps } from '@/utils/tree.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
const formData = reactive({
  id: 0,
  name: '',
  code: '',
  dataScope: undefined,
  dataScopeDeptIds: []
})
// 表单 Ref
const formRef = ref()
// 部门树形结构
const deptOptions = ref([])
// 展开/折叠
const deptExpand = ref(false)
// 菜单树组件 Ref
const treeRef = ref()
// 全选/全不选
const treeNodeAll = ref(false)
// 是否严格模式，即父子不关联
const checkStrictly = ref(true)

/** 打开弹窗 */
const open = async (row) => {
  dialogVisible.value = true
  resetForm()
  // 加载 Dept 列表。注意，必须放在前面，不然下面 setChecked 没数据节点
  const response = await listSimpleDepts()
  deptOptions.value = handleTree(response.data)
  // 设置数据
  formData.id = row.id
  formData.name = row.name
  formData.code = row.code
  formData.dataScope = row.dataScope

}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })


/** 重置表单 */
const resetForm = () => {
  // 重置选项
  treeNodeAll.value = false
  deptExpand.value = false
  checkStrictly.value = true
  // 重置表单
  formData.value = {
    id: 0,
    name: '',
    code: '',
    dataScope: undefined,
    dataScopeDeptIds: []
  }
  treeRef.value?.setCheckedNodes([])
  formRef.value?.resetFields()
}

</script>

<style scoped>

</style>
