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
import { uploadAvatar } from '@/api/system/user/profile'
import ELComponent from '@/plugins/modal.js'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'

const { wsCache } = useCache()
const props = defineProps({
  img: propTypes.string.def('')
})
const avatar = computed(()=>{
  return props.img
})
const cropperRef = ref()
// 文件上传
const handelUpload = async ({ data })=>{
  // FormData对象用于构建HTTP请求的键值对数据，并且可以方便地处理包含文件上传的表单数据。
  let formData = new FormData()
  formData.append("avatarFile", data)
  const response = await uploadAvatar(formData)
  ELComponent.msgSuccess("上传成功")

  // 替换缓存中的值
  const user = wsCache.get(CACHE_KEY.USER)
  user.data.user.avatar = response.data
  wsCache.set(CACHE_KEY.USER,user)
  // 关闭弹窗
  cropperRef.value.close()
}
console.log()

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
