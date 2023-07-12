<template>
  <div class="user-info-head" @click="open()">
    <!--  外部的图片的显示缩略图  -->
    <img
        v-if="sourceValue"
        :src="sourceValue"
        alt="avatar"
        class="img-circle img-lg"
    />
    <!--  showBtn  -->
    <el-button v-if="showBtn" :class="`${prefixCls}-upload-btn`" @click="open()">
      {{ btnText ? btnText : "选择图片" }}
    </el-button>

    <CopperModal
        ref="cropperModelRef"
        :srcValue="sourceValue"
        @upload-success="handleUploadSuccess"
    />

  </div>
</template>
<script setup>
import { ref,watchEffect,watch } from 'vue'
import { propTypes } from '@/utils/propTypes'
import CopperModal from './CopperModal.vue'
const props = defineProps({
  width: propTypes.string.def('200px'),
  // 图片的img
  value: propTypes.string.def(''),
  // 很鸡肋的东西，在图片下方显示选择图片上传的按钮
  showBtn: propTypes.bool.def(true),
  // 图片下方按钮的名称
  btnText: propTypes.string.def('')
})

const emit = defineEmits(['update:value', 'change'])
const sourceValue = ref(props.value)

const cropperModelRef = ref()
const prefixCls = 'v-cropper-avatar'
// 自动收集依赖数据，依赖数据更新时重新执行自身
watchEffect(() => {
  sourceValue.value = props.value
})
// 每次触发emit事件，将新的值传递给父组件
watch(
    () => sourceValue.value,
    (v) => {
      emit('update:value', v)
    }
)
const handleUploadSuccess = ({source, data, filename})=>{
  sourceValue.value = source
  emit('change', { source, data, filename })
  console.log("上传成功handleUploadSuccess（）")
}

const open = () => {
  cropperModelRef.value.openModal()
}

const close = () => {
  cropperModelRef.value.closeModal()
  console.log("cropperModelRef.value.closeModal()",cropperModelRef.value)
}
// 导出方法
defineExpose({
  open,
  close
})
</script>
<style lang="scss" scoped>
$prefix-cls: el--cropper-avatar;

.#{$prefix-cls} {
  display: inline-block;
  text-align: center;

  &-image-wrapper {
    overflow: hidden;
    cursor: pointer;
    border: 1px solid;
    border-radius: 50%;

    img {
      width: 100%;
    }
  }

  &-image-mask {
    opacity: 0%;
    position: absolute;
    width: inherit;
    height: inherit;
    border-radius: inherit;
    border: inherit;
    background: rgb(0 0 0 / 40%);
    cursor: pointer;
    transition: opacity 0.4s;

    ::v-deep(svg) {
      margin: auto;
    }
  }

  &-image-mask:hover {
    opacity: 4000%;
  }

  &-upload-btn {
    margin: 10px auto;
  }
}

.user-info-head {
  position: relative;
  display: inline-block;
}

.img-circle {
  border-radius: 50%;
}

.img-lg {
  width: 120px;
  height: 120px;
}

.user-info-head:hover:after {
  content: '+';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  line-height: 110px;
  border-radius: 50%;
}
</style>
