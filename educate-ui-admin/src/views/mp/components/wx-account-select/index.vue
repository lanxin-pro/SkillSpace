<template>
  <el-select
      v-model="account.id"
      placeholder="请选择公众号"
      class="!w-240px"
      @change="onChanged"
  >
    <el-option
        v-for="item in accountList"
        :key="item.id"
        :label="item.name"
        :value="item.id"
    />
  </el-select>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { getSimpleAccountList } from '@/api/mp/account/index.js'

const account = reactive({
  id: -1,
  name: ''
})

const accountList = ref([])

const emit = defineEmits(["change"])

/** 初始化 */
onMounted(() => {
  handleQuery()
})

/** 初始化加载的方法 */
const handleQuery = async () => {
  // 获取精简列表
  const response = await getSimpleAccountList()
  accountList.value = response.data
  // 默认选中第一个
  if (accountList.value.length > 0) {
    account.id = accountList.value[0].id
    if (account.id) {
      account.name = accountList.value[0].name
      // emit传递
      emit('change', account.id, account.name)
    }
  }
}

const onChanged = (id) => {
  // 账号集合
  const found = accountList.value.find((list) => {
    return list.id === id
  })
  if (account.id) {
    // found找到名字了，就传递name了
    account.name = found ? found.name : ''
    emit('change', account.id, account.name)
  }
}



</script>

<style scoped>

</style>
