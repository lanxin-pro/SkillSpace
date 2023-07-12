<template>
  <div class="change-avatar">

    <CropperAvatar
        ref="cropperRef"
        :showBtn="false"
        :value="avatar"
        width="120px"
        @change="handelUpload"
    />

  </div>
</template>

<script setup>
import { computed,ref } from 'vue'
import CropperAvatar from '@/components/Cropper/CropperAvatar.vue'
import { propTypes } from '@/utils/propTypes'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
const { wsCache } = useCache()

const props = defineProps({
  img: propTypes.string.def('')
})
const avatar = computed(()=>{
  return props.img
})
const cropperRef = ref()
const handelUpload = ()=>{
  console.log("提交上传avatar")
}

</script>

<style scoped lang="scss">
.change-avatar {
  img {
    display: block;
    margin-bottom: 15px;
    border-radius: 50%;
  }
}
</style>
