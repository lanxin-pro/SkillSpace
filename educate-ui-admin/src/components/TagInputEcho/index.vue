<template>
  <div>
    <!--      这里的tags是我重新定义的一个响应式数据          -->
    <el-tag
        v-for="(itemTag, index) in tagsList"
        class="mr-3"
        closable
        size="large"
        type="success"
        @close="handleDelTags(index)">{{ itemTag }}</el-tag>
    <el-input
        v-model.trim="tag"
        clearable
        placeholder="请输入标签"
        style="width: 160px"
        @blur="handleAppendTags($event)"></el-input>
  </div>
</template>

<script setup>
import { propTypes } from '@/utils/propTypes'
import ELComponent from '@/plugins/modal.js'
import { ref } from 'vue'

const props = defineProps({
  tagsList: propTypes.array
})
const emit = defineEmits(['blur'])

const tag = ref()
/** 添加标签 */
const handleAppendTags = (ev)=>{
  if(tag.value){
    const cIndex = props.tagsList.findIndex(item=>{
      return item === tag.value
    })
    if(cIndex === -1){
      props.tagsList.push(tag.value)
      tag.value = ""
      emit('blur', props.tagsList)
    }else{
      return ELComponent.msgError("标签已经存在")
    }
  }
}
/** 删除标签 */
const handleDelTags = (index)=>{
  props.tagsList.splice(index,1)
}
</script>

<style scoped>

</style>
