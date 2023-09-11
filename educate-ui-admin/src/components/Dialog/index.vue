<template>
  <ElDialog
      :close-on-click-modal="true"
      :fullscreen="isFullscreen"
      :width="width"
      destroy-on-close
      :draggable="draggable"
      :lock-scroll="lockScroll"
      v-bind="getBindValue"
      :align-center="true"
  >
    <template #header>
      <div class="flex justify-between">
        <slot name="title">
          {{ title }}
        </slot>
        <font-awesome-icon
            v-if="fullscreen"
            :icon="isFullscreen ? 'zmdi:fullscreen-exit' : 'zmdi:fullscreen'"
            class="mr-22px cursor-pointer is-hover mt-2px z-10"
            color="var(--el-color-info)"
            @click="toggleFull"
            icon="fa-solid fa-expand" />

      </div>
    </template>

    <!-- 情况一：如果 scroll 为 true，说明开启滚动条 -->
    <ElScrollbar v-if="scroll" :style="dialogStyle">
      <slot></slot>
    </ElScrollbar>
    <!-- 情况二：如果 scroll 为 false，说明关闭滚动条滚动条 -->
    <slot v-else></slot>

    <template v-if="slots.footer" #footer>
      <slot name="footer"></slot>
    </template>
  </ElDialog>

</template>

<script setup>
import { propTypes } from '@/utils/propTypes.js'
import { useSlots,computed,useAttrs,ref,unref,watch,nextTick } from 'vue'
import { isNumber } from '@/utils/is.js'

const slots = useSlots()

const props = defineProps({
  modelValue: propTypes.bool.def(false),
  title: propTypes.string.def('Dialog'),
  fullscreen: propTypes.bool.def(true),
  width: propTypes.oneOfType([String, Number]).def('40%'),
  scroll: propTypes.bool.def(false), // 是否开启滚动条。如果是的话，按照 maxHeight 设置最大高度
  maxHeight: propTypes.oneOfType([String, Number]).def('300px'),
  draggable: propTypes.bool.def(true),
  lockScroll: propTypes.bool.def(false)
})

const getBindValue = computed(() => {
  const delArr = ['fullscreen', 'title', 'maxHeight']
  const attrs = useAttrs()
  const obj = { ...attrs, ...props }
  for (const key in obj) {
    if (delArr.indexOf(key) !== -1) {
      delete obj[key]
    }
  }
  return obj
})

const isFullscreen = ref(false)

const toggleFull = () => {
  isFullscreen.value = !unref(isFullscreen)
}

const dialogHeight = ref(isNumber(props.maxHeight) ? `${props.maxHeight}px` : props.maxHeight)

watch(
  () => isFullscreen.value,
  async (val) => {
    // 计算最大高度
    await nextTick()
    if (val) {
      const windowHeight = document.documentElement.offsetHeight
      dialogHeight.value = `${windowHeight - 55 - 60 - (slots.footer ? 63 : 0)}px`
    } else {
      dialogHeight.value = isNumber(props.maxHeight) ? `${props.maxHeight}px` : props.maxHeight
    }
  },
  {
    immediate: true
  }
)

const dialogStyle = computed(() => {
  return {
    height: unref(dialogHeight)
  }
})
</script>


<style lang="scss">
.el-dialog__header {
  margin-right: 0 !important;
  border-bottom: 1px solid var(--tags-view-border-color);
}

.el-dialog__footer {
  border-top: 1px solid var(--tags-view-border-color);
}

.is-hover {
  &:hover {
    color: var(--el-color-primary) !important;
  }
}

.dark {
  .el-dialog__header {
    border-bottom: 1px solid var(--el-border-color);
  }

  .el-dialog__footer {
    border-top: 1px solid var(--el-border-color);
  }
}
</style>
