<template>
  <div class="app-container">

  </div>
</template>

<script setup>
import { ref,reactive,onMounted } from 'vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import ELComponent from '@/plugins/modal.js'
import { parseTime } from '@/utils/ruoyi.js'
import { getMailAccountPage,deleteMailAccount } from '@/api/system/mail/account/index.js'
import DictTag from '@/components/DictTag/index.vue'
import TemplateForm from './TemplateForm.vue'
import { dateFormatter } from '@/utils/formatTime.js'
import Pagination from '@/components/Pagination/index.vue'

</script>

<style scoped>

</style>
