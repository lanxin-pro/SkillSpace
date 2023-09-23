<template>
  <el-form-item label="标签分类" prop="path">
    <el-cascader
        size="small"
        placeholder="一级分类"
        :emitPath="false"
        style="margin-right: 5px;"
        :options="categoryList"
        v-model="categoryPids"
        @change="handleChangeCategory"
        :props="{ multiple: multiple,label:'categoryName',value:'id'}"
        clearable
    />
    <el-cascader
        v-if="childCategoryList && childCategoryList.length > 0"
        size="small"
        placeholder="二级分类"
        :emitPath="false"
        style="margin-right: 5px;"
        :options="childCategoryList"
        v-model="categoryCids"
        @change="handleChangeCCategory"
        :props="{ multiple: multiple,label:'categoryName',value:'id'}"
        clearable
    />
  </el-form-item>
</template>

<script setup>
import { ref } from 'vue'
import { propTypes } from '@/utils/propTypes.js'

const categoryPids = ref([])
const categoryCids = ref([])
const categoryList = ref([])
const childCategoryList = ref([])

const props = defineProps({
  multiple: propTypes.bool.def(false)
})
</script>

<style scoped>

</style>
