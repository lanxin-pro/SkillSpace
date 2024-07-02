<template>
  <ElDrawer
      :before-close="handleClose"
      :with-header="withHeader"
      :close-on-click-modal="true"
      :direction="direction"
      :lock-scroll="lockScroll"
      :modal="modal"
      :size="size"
      destroy-on-close
      v-bind="getBindValue"
  >
    <template v-if="title !== ''" #header>
      <div class="flex justify-between">
        <slot name="title">
          {{ title }}
        </slot>
      </div>
    </template>

    <!-- 情况一：如果 scroll 为 true，说明开启滚动条 -->
    <ElScrollbar v-if="scroll">
      <slot></slot>
    </ElScrollbar>
    <!-- 情况二：如果 scroll 为 false，说明关闭滚动条滚动条 -->
    <slot v-else></slot>

    <template v-if="slots.footer" #footer>
      <slot name="footer"></slot>
    </template>
  </ElDrawer>

</template>

<script setup>
import { propTypes } from '@/utils/propTypes.js'
import { useSlots,computed,useAttrs } from 'vue'
import { ElMessageBox } from 'element-plus'

const slots = useSlots()

const props = defineProps({
  modelValue: propTypes.bool.def(false),
  title: propTypes.string.def(''),
  scroll: propTypes.bool.def(false), // 是否开启滚动条。如果是的话，按照 maxHeight 设置最大高度
  lockScroll: propTypes.bool.def(false),
  dialogEnableStatus: propTypes.bool.def(false),
  modal: propTypes.bool.def(true),
  // ltr rtl ttb btt
  direction: propTypes.string.def("ltr"),
  size: propTypes.string.def("70%"),
  withHeader: propTypes.bool.def(true)
})

/** 关闭提醒 */
const handleClose = (done) => {
  if(props.dialogEnableStatus){
    ElMessageBox.confirm("您确定要退出吗？您的数据将全部丢失！","系统提示", {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: "error",
    }).then(() => {
      done()
    }).catch(() => {
      // catch error
    })
  } else {
    // ESC 和 点击弹窗外的关闭事件
    done()
  }
}

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


</script>


<style lang="scss">

</style>
