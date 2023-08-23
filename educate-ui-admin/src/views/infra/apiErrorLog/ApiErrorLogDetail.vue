<template>

</template>

<script setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { ref,reactive } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import ELComponent from '@/plugins/modal.js'
import { CommonStatusEnum } from '@/utils/constants.js'
import { getSimpleUserList, getSimpleUserSimpleDeptList } from '@/api/system/user.js'
import { getDept } from '@/api/system/dept.js'
import { listSimpleDepts,createDept,updateDept } from '@/api/system/dept.js'
import { handleTree,defaultProps } from '@/utils/tree.js'

</script>

<style scoped>

</style>
