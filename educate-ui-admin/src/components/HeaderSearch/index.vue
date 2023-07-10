<template>
  <div :class="{'show': show}" class="header-search">
    <SvgIcon
        class="fa-solid search-icon"
        icon-class="fa-solid fa-magnifying-glass"
        style="color: #284c8a;"
        @click.stop="click()"
    />
    <el-select
        ref="headerSearchSelect"
        v-model="search"
        :remote-method="querySearch"
        filterable
        default-first-option
        remote
        placeholder="Search"
        class="header-search-select"
        @change="change"
    >
      <el-option
          v-for="option in options"
          :key="option.item.path"
          :value="option.item"
          :label="option.item.title.join(' > ')"
      />
    </el-select>
  </div>
</template>

<script setup>
import { ref,reactive,watch } from 'vue'
import SvgIcon from '@/components/SvgIcon/index.vue'


const search = ref('')
const options = ref([])
const searchPool = ref([])
const show = ref(false)
const fuse = ref()
const headerSearchSelect = ref()

const click = ()=>{
  show.value = !show.value
  if(show.value){
    headerSearchSelect.value.focus()
  }
}

// 过滤掉可以显示在侧边栏中的路由，并生成国际化的标题
const generateRoutes = ((routes, basePath = '/', prefixTitle = [])=>{
  let res = []

  for (const router of routes) {
    // skip hidden router
    if (router.hidden) { continue }

    const data = {
      path: !ishttp(router.path) ? path.resolve(basePath, router.path) : router.path,
      title: [...prefixTitle]
    }

    if (router.meta && router.meta.title) {
      data.title = [...data.title, router.meta.title]

      if (router.redirect !== 'noRedirect') {
        // only push the routes with title
        // special case: need to exclude parent router without redirect
        res.push(data)
      }
    }

    // recursive child routes
    if (router.children) {
      const tempRoutes = generateRoutes(router.children, data.path, data.title)
      if (tempRoutes.length >= 1) {
        res = [...res, ...tempRoutes]
      }
    }
  }
  return res
})
const ishttp = ((url)=>{
  return url.indexOf('http://') !== -1 || url.indexOf('https://') !== -1
})
</script>

<style lang="scss" scoped>
.header-search {
  font-size: 0 !important;

  .search-icon {
    cursor: pointer;
    font-size: 18px;
    vertical-align: middle;
  }

  .header-search-select {
    font-size: 18px;
    transition: width 0.2s;
    width: 0;
    overflow: hidden;
    background: transparent;
    border-radius: 0;
    display: inline-block;
    vertical-align: middle;

    :deep(.el-input__inner) {
      border-radius: 0;
      border: 0;
      padding-left: 0;
      padding-right: 0;
      box-shadow: none !important;
      border-bottom: 1px solid #d9d9d9;
      vertical-align: middle;
    }
  }

  &.show {
    .header-search-select {
      width: 210px;
      margin-left: 10px;
    }
  }
}
</style>
