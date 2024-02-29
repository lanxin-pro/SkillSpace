<template>
  <div id="tags-view-container" class="tags-view-container">
    <div class="flex-1 overflow-hidden">
      <ElScrollbar ref="scrollbarRef" class="h-full" @scroll="scroll">
        <div class="layout-navbars-tagsview-ul h-full flex">
          <ContextMenu
              v-for="item in visitedViews"
              :class="{'is-active': isActive(item)}"
              :key="item.fullPath"
              :tag-item="item"
              :schema="[
                {
                  icon: 'refresh',
                  label: '重新加载',
                  disabled: selectedTag?.fullPath !== item.fullPath,
                  command: () => {
                    refreshSelectedTag(item)
                  }
                },
                {
                  icon: 'close',
                  label: '关闭标签',
                  disabled: !!visitedViews?.length && selectedTag?.meta?.affix,
                  command: () => {
                    closeSelectedTag(item)
                  }
                },
                {
                divided: true,
                icon: 'd-arrow-left',
                label: '关闭左侧标签页',
                disabled:
                  !!visitedViews?.length &&
                  (item.fullPath === visitedViews[0].fullPath ||
                    selectedTag?.fullPath !== item.fullPath),
                command: () => {
                  closeLeftTags()
                }
              },
              {
                icon: 'd-arrow-right',
                label: '关闭右侧标签页',
                disabled:
                  !!visitedViews?.length &&
                  (item.fullPath === visitedViews[visitedViews.length - 1].fullPath ||
                    selectedTag?.fullPath !== item.fullPath),
                command: () => {
                  closeRightTags()
                }
              },
              {
                divided: true,
                icon: 'discount',
                label: '关闭其他标签页',
                disabled: selectedTag?.fullPath !== item.fullPath,
                command: () => {
                  closeOthersTags()
                }
              },
              {
                icon: 'minus',
                label: '关闭全部标签页',
                command: () => {
                  closeAllTags()
                }
              }
              ]"
          >
            <div class="test1">
              <router-link :ref="tagLinksRefs.set" :to="{ ...item }" custom v-slot="{ navigate }">
                <div
                    @click="navigate"
                    class="navigateTags"
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

/** 是否是当前tag */
const isActive = (route) => {
  return route.path === unref(currentRoute).path
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
const closeSelectedTag = (item) => {

}
const closeLeftTags = () => {

}
const closeRightTags = () => {

}
const closeOthersTags = () => {

}
/** 关闭全部标签页 */
const closeAllTags = () => {
  tagsViewStore.delAllViews()
  toLastView()
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
  height: 100%;
  /* flex: 1 1 0%; */
  flex-grow: 1;
  flex-shrink: 1;
  flex-basis: 0;
}
.tags-view-container {
  height: 36px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .12), 0 0 3px 0 rgba(0, 0, 0, .04);
  :deep(.el-scrollbar__view) {
    /* 在这里添加样式，可以覆盖element-plus中的样式 */
    height: 100%;
  }
  .layout-navbars-tagsview-ul {
    .navigateTags {
      height: 100%;
      display: flex;
      align-items: center;
      padding: 0 7px;
    }
    /* TODO j-sentinel 初步完成样式的写法 */
    .is-active {
      color: #0256ff;
      transition: all 0.3s cubic-bezier(0.2, 1, 0.3, 1);
      border-color: transparent;
      background: #aec6f3;

      padding: 0 20px;
      -webkit-mask-image: url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNzAiIGhlaWdodD0iNzAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgZmlsbD0ibm9uZSI+PHBhdGggZD0iTTEwMC4yMzYgOTkuODgzQzQ1LjAwOCAxMDAuMDEyLjEzMiA1NS4zNDQuMDAzLjExN2wuMjMzIDEwMCAxMDAtLjIzNHoiIG9wYWNpdHk9InVuZGVmaW5lZCIgc3Ryb2tlPSJudWxsIiBmaWxsPSIjRjhFQUU3Ii8+PHBhdGggZD0iTS0uNjM4IDcuMzEyYy4xMiAwIC4yMTguMDU4LjQ3Ny4xMi4yMzIuMDU0LjI3My4wMzQuMzU4LjExOS4wODQuMDg0LjM1NyAwIC40NzYgMGguMzU4TTI4LjkyMSA2OS4wNTJ2LjU5Nk0yOC45MjEgNjguNDU3aC4xMTl2MS43ODhoLS4xMTl6TTM2LjY2OSA1MS4yOTNoMTkuMDd2NC44ODdoLTE5LjA3eiIgc3Ryb2tlPSJudWxsIi8+PC9zdmc+"),url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNzAiIGhlaWdodD0iNzAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgZmlsbD0ibm9uZSI+PHBhdGggc3Ryb2tlPSJudWxsIiBmaWxsPSIjRjhFQUU3IiBkPSJNNjkuOTU0LjE0Yy0uMjI5IDU1LjIyOC00NS4xODYgOTkuODE0LTEwMC40MTMgOTkuNTg1bDk5Ljk5OS40MTRMNjkuOTU0LjE0eiIgY2xpcC1ydWxlPSJldmVub2RkIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiLz48cGF0aCBkPSJNLS42MzggNy4zMTJjLjEyIDAgLjIxOC4wNTguNDc3LjEyLjIzMi4wNTQuMjczLjAzNC4zNTguMTE5LjA4NC4wODQuMzU3IDAgLjQ3NiAwaC4zNThNMjguOTIxIDY5LjA1MnYuNTk2TTI4LjkyMSA2OC40NTdoLjExOXYxLjc4OGgtLjExOXpNMzYuNjY5IDUxLjI5M2gxOS4wN3Y0Ljg4N2gtMTkuMDd6IiBzdHJva2U9Im51bGwiLz48L3N2Zz4="),url('data:image/svg+xml;charset=utf-8,<svg xmlns="http://www.w3.org/2000/svg"><rect rx="8" width="100%" height="100%" fill="%23F8EAE7"/></svg>');
      -webkit-mask-position: right bottom, left bottom, center top;
      -webkit-mask-size: 18px 30px, 20px 30px, calc(100% - 35px) calc(100% + 17px);
      -webkit-mask-repeat: no-repeat;

      &::before {
        opacity: 1;
        transform: translate3d(0, 0, 0);
        border-radius: 2px;
      }
    }
  }
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
