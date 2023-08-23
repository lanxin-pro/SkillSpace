<template>
  <div class="app-container">
    a
  </div>
</template>

<script setup>
import { ref,reactive,onMounted,nextTick } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import ApiErrorLogDetail from './ApiErrorLogDetail.vue'
import { parseTime } from '@/utils/ruoyi.js'
import { getSimpleUserList } from '@/api/system/user.js'
import { getDeptPage,deleteDept } from '@/api/system/dept.js'
import { handleTree } from '@/utils/tree.js'
import DictTag from '@/components/DictTag/index.vue'

</script>

<style scoped>

</style>
