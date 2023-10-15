<template>
  <div id="tags-view-container" class="tags-view-container">
    <div class="flex-1 overflow-hidden">
      <ElScrollbar ref="scrollbarRef" class="h-full" @scroll="scroll">
        <div class="h-full flex">
          <ContextMenu
              v-for="item in visitedViews"
              :key="item.fullPath"
              :tag-item="item"
          >
            <div>
              <router-link :ref="tagLinksRefs.set" :to="{ ...item }" custom v-slot="{ navigate }">
                <div
                    @click="navigate"
                    class="h-full flex items-center justify-center whitespace-nowrap pl-15px"
                >
                  <Icon
                      v-if="
                      item?.matched &&
                      item?.matched[1] &&
                      item?.matched[1]?.meta?.icon &&
                      tagsViewIcon
                    "
                      :icon="item?.matched[1]?.meta?.icon"
                      :size="12"
                      class="mr-5px"
                  />
                  {{ item?.meta?.title }}
                  <Icon
                      color="#333"
                      icon="ep:close"
                      :size="12"
                  />
                </div>
              </router-link>
            </div>
          </ContextMenu>
        </div>
      </ElScrollbar>
    </div>
  </div>
</template>

<script setup>
import ScrollPane from './ScrollPane.vue'
import { onMounted, watch, computed, unref, ref, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import store from '@/store'
import { filterAffixTags } from './helper.js'
import { usePermissionStore } from '@/piniastore/modules/permission.js'
import { useTagsViewStore } from '@/piniastore/modules/tagsView.js'
import ContextMenu from '@/frame/components/ContextMenu/index.vue'
import { useTemplateRefsList } from '@vueuse/core'

const visible = ref(false)
const top = ref(0)
const left = ref(0)
const selectedTag = ref({})
const affixTags = ref([])
const affixTagArr = ref([])

const { currentRoute, push, replace } = useRouter()

const permissionStore = usePermissionStore()

const routers = computed(() => permissionStore.getRouters)

const tagsViewStore = useTagsViewStore()

const visitedViews = computed(() => tagsViewStore.getVisitedViews)

onMounted(() => {
  initTags()
  addTags()
})
// 初始化tag
const initTags = () => {
  affixTagArr.value = filterAffixTags(unref(routers))
  for (const tag of unref(affixTagArr)) {
    // 必须有标签名
    if (tag.name) {
      tagsViewStore.addVisitedView(tag)
    }
  }
}
// 新增tag
const addTags = () => {
  const { name } = unref(currentRoute)
  if (name) {
    selectedTag.value = unref(currentRoute)
    tagsViewStore.addView(unref(currentRoute))
  }
  return false
}

watch(
    () => currentRoute.value,
    () => {
      addTags()
    }
)

// 保存滚动位置
const scrollLeftNumber = ref(0)

const scroll = ({ scrollLeft }) => {
  scrollLeftNumber.value = scrollLeft
}




// 重新加载
const refreshSelectedTag = async (view) => {
  console.log("重新加载",view)
}

const tagLinksRefs = useTemplateRefsList()

// 所有右键菜单组件的元素
const itemRefs = useTemplateRefsList()

// 右键菜单装填改变的时候
const visibleChange = (visible, tagItem) => {
  console.log('visible',visible)
  console.log('tagItem',tagItem)
}

// const visitedViews = ()=>{
//   return store.state.tagsView.visitedViews
// }
const routes = ()=>{
  return store.state.tagsView.routes
}
const theme = ()=>{
  return store.state.tagsView.theme
}
const closeMenu = () => {
  visible.value = false
}
const handleScroll = ()=>{
  closeMenu()
}
</script>

<style lang="scss" scoped>
.h-full {
  height: 100%;
}
.flex {
  display: flex;
}
.overflow-hidden {
  overflow: hidden;
}
.flex-1 {
  flex: 1 1 0%;
}
.tags-view-container {
  height: 36px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .12), 0 0 3px 0 rgba(0, 0, 0, .04);
  .tags-view-wrapper {
    .tags-view-item {
      display: inline-block;
      position: relative;
      cursor: pointer;
      height: 28px;
      line-height: 28px;
      border: 1px solid #d8dce5;
      color: #495060;
      background: #fff;
      padding: 0 6px;
      font-size: 12px;
      margin-left: 4px;
      margin-top: 4px;
      border-radius: 3px 3px 3px 3px;
      &:first-of-type {
        margin-left: 15px;
      }
      &:last-of-type {
        margin-right: 15px;
      }
      &.active {
        background-color: #42b983;
        color: #fff;
        border-color: #42b983;
        &::before {
          content: '';
          background: #fff;
          display: inline-block;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          position: relative;
          margin-right: 2px;
        }
      }
    }
  }
  .contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, .3);
    li {
      margin: 0;
      padding: 7px 16px;
      cursor: pointer;
      &:hover {
        background: #eee;
      }
    }
  }
}

//reset element css of el-icon-close
.tags-view-wrapper {
  .tags-view-item {
    .el-icon-close {
      width: 16px;
      height: 16px;
      vertical-align: 2px;
      border-radius: 50%;
      text-align: center;
      transition: all .3s cubic-bezier(.645, .045, .355, 1);
      transform-origin: 100% 50%;
      &:before {
        transform: scale(.6);
        display: inline-block;
        vertical-align: -3px;
      }
      &:hover {
        background-color: #b4bccc;
        color: #fff;
      }
    }
  }
}
</style>
