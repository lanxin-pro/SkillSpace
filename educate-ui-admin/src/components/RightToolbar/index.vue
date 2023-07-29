<template>
  <div class="top-right-btn" :style="style">
    <el-row>
      <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top" v-if="search">
        <el-button size="small" circle icon="search" @click="toggleSearch()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="刷新" placement="top">
        <el-button size="small" circle icon="refresh" @click="refresh()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="显隐列" placement="top" v-if="columns">
        <el-button size="small" circle icon="menu" @click="showColumn()" />
      </el-tooltip>
    </el-row>
    <el-dialog :title="title" :visible.sync="open" append-to-body>
      <el-transfer
          :titles="['显示', '隐藏']"
          v-model="value"
          :data="columns"
          @change="dataChange"
      ></el-transfer>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref,onMounted,getCurrentInstance } from 'vue'
import { propTypes } from '@/utils/propTypes.js'

// 显隐数据
const value = ref([])
// 弹出层标题
const title = ref("显示/隐藏")
// 是否显示弹出层
const open = ref(false)

const props = defineProps({
  showSearch: propTypes.bool.def(true),
  columns: propTypes.array,
  search: propTypes.bool.def(true),
  gutter: propTypes.number.def(10)
})

onMounted(()=>{
  // 显隐列初始默认隐藏列
  for (let item in props.columns) {
    if (props.columns[item].visible === false) {
      value.value.push(parseInt(item))
    }
  }
})

const emit = defineEmits(['update:showSearch'])
const toggleSearch = ()=>{
 emit('update:showSearch',!props.showSearch)
}

</script>

<style lang="scss" scoped>
:deep(.el-transfer__button) {
  border-radius: 50%;
  padding: 12px;
  display: block;
  margin-left: 0px;
}
:deep(.el-transfer__button:first-child) {
  margin-bottom: 10px;
}
</style>

