<template>
  <Dialog v-model="dialogVisible" title="分配角色">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" label-width="80px">
      <el-form-item label="用户名称">
        <el-input v-model="formData.username" :disabled="true" />
      </el-form-item>
      <el-form-item label="用户昵称">
        <el-input v-model="formData.nickname" :disabled="true" />
      </el-form-item>
      <el-form-item label="角色">
        <el-select v-model="formData.roleIds" multiple placeholder="请选择角色">
          <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm()">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>

  </Dialog>
</template>

<script setup>
import { ref,reactive } from 'vue'
import { CommonStatusEnum } from '@/utils/constants'
import Dialog from '@/components/Dialog/index.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { getSimpleRoleList } from '@/api/system/role/index.js'
import { getUserRoleList,assignUserRole } from '@/api/system/permission/index.js'
import ELComponent from '@/plugins/modal.js'

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
const formData = ref({
  id: undefined,
  nickname: '',
  username: '',
  roleIds: []
})
// 表单 Ref
const formRef = ref()
// 角色的列表
const roleList = ref([])

/** 打开弹窗 */
const open = async (row) => {
  dialogVisible.value = true
  resetForm()
  // 设置数据
  formData.value.id = row.id
  formData.value.username = row.username
  formData.value.nickname = row.nickname
  // 获得角色拥有的菜单集合
  formLoading.value = true
  try {
    const responseUserRoleList = await getUserRoleList(row.id)
    formData.value.roleIds = responseUserRoleList.data
  } finally {
    formLoading.value = false
  }
  // 获得角色列表
  const responseRoleList = await getSimpleRoleList()
  roleList.value = responseRoleList.data
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

/** 提交表单 */
// 定义 success 事件，用于操作成功后的回调
const emit = defineEmits(['success'])
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    await assignUserRole({
      userId: formData.value.id,
      roleIds: formData.value.roleIds
    })
    ELComponent.msgSuccess("修改角色成功！")
  } finally {
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success', true)
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    nickname: '',
    username: '',
    roleIds: []
  }
  formRef.value?.resetFields()
}
</script>

<style scoped>

</style>
