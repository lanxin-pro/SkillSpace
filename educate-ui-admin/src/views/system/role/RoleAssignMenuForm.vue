<template>
  <Dialog v-model="dialogVisible" title="菜单权限">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" label-width="80px">
      <el-form-item label="角色名称">
        <el-tag>{{ formData.name }}</el-tag>
      </el-form-item>
      <el-form-item label="角色标识">
        <el-tag>{{ formData.code }}</el-tag>
      </el-form-item>
      <el-form-item label="菜单权限">
        <el-card class="cardHeight">
          <template #header>
            全选/全不选:
            <el-switch
                v-model="treeNodeAll"
                active-text="是"
                inactive-text="否"
                inline-prompt
                @change="handleCheckedTreeNodeAll"
            />
            全部展开/折叠:
            <el-switch
                v-model="menuExpand"
                active-text="展开"
                inactive-text="折叠"
                inline-prompt
                @change="handleCheckedTreeExpand"
            />
          </template>
          <el-tree
              ref="treeRef"
              :data="menuOptions"
              :props="defaultProps"
              empty-text="加载中，请稍候"
              node-key="id"
              show-checkbox
              :default-expand-all="true"
          />
        </el-card>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm()">确 定</el-button>
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
import { getSimpleMenusList } from '@/api/system/menu/index.js'
import { getRoleMenuList,assignRoleMenu } from '@/api/system/permission/index.js'
import { handleTree,defaultProps } from '@/utils/tree.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
const formData = reactive({
  id: 0,
  name: '',
  code: '',
  menuIds: []
})
// 表单 Ref
const formRef = ref()
// 菜单树形结构
const menuOptions = ref([])
// 展开/折叠
const menuExpand = ref(false)
// 菜单树组件 Ref
const treeRef = ref()
// 全选/全不选
const treeNodeAll = ref(false)

/** 打开弹窗 */
const open = async (row) => {
  dialogVisible.value = true
  resetForm()
  // 加载 Menu 列表。注意，必须放在前面，不然下面 setChecked 没数据节点
  const response = await getSimpleMenusList()
  menuOptions.value = handleTree(response.data)
  // 设置数据
  formData.id = row.id
  formData.name = row.name
  formData.code = row.code
  formLoading.value = true
  try {
    const response = await getRoleMenuList(row.id)
    formData.value.menuIds = response.data
    // 设置选中
    formData.value.menuIds.forEach((menuId) => {
      treeRef.value.setChecked(menuId, true, false)
    })
  } finally {
    formLoading.value = false
  }
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })


/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  if (!formRef){
    return
  }
  const valid = await formRef.value.validate()
  if (!valid){
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    const data = {
      roleId: formData.id,
      menuIds: [
        // 获得当前选中节点
        ...(treeRef.value.getCheckedKeys(false)),
        // 获得半选中的父节点
        ...(treeRef.value.getHalfCheckedKeys())
      ]
    }
    await assignRoleMenu(data)
    ELComponent.msgSuccess("更新成功！")
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}



/** 全选/全不选 */
const handleCheckedTreeNodeAll = () => {
  // true 全选
  // false 全不选
  treeRef.value.setCheckedNodes(treeNodeAll.value ? menuOptions.value : [])
}

/** 展开/折叠全部 */
const handleCheckedTreeExpand = () => {
  const nodes = treeRef.value?.store.nodesMap
  for (let node in nodes) {
    if (nodes[node].expanded === menuExpand.value) {
      continue
    }
    nodes[node].expanded = menuExpand.value
  }
}

/** 重置表单 */
const resetForm = () => {
  // 重置选项
  treeNodeAll.value = false
  menuExpand.value = false
  // 重置表单
  formData.value = {
    id: 0,
    name: '',
    code: '',
    menuIds: []
  }
  treeRef.value?.setCheckedNodes([])
  formRef.value?.resetFields()
}

</script>

<style scoped>

</style>
