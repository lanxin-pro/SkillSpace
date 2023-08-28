<template>
  <div v-if="cdesc">
    <div style="font-size: 13px" v-if="cdesc.length <= 40">{{ cdesc }}</div>
    <div v-else style="font-size: 13px">
      <span>{{ cdesc.substring(0, 80) }}</span>
      <span v-if="cshow">{{ cdesc.substring(80) }}</span>
      <span @click="showMore" v-if="cshow" class="kkmore">&lt;&lt;收起查看</span>
      <span @click="showMore" v-else class="kkmore">&gt;&gt;点击查看</span>
    </div>
  </div>
  <div style="font-size: 13px" v-else>暂无描述</div>
</template>

<script setup>
import { propTypes } from '@/utils/propTypes.js'
import { ref } from 'vue'

defineProps({
  clen: propTypes.number.def(80),
  cdesc: propTypes.string
})

const cshow = ref(false)

const showMore = ()=> {
  cshow.value = !cshow.value
}
</script>

<style scoped>
.kkmore{
  color: #409eff;
  font-size: 12px;
  padding-left: 4px;
  cursor: pointer;
}
</style>
