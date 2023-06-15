<template>
  <h2 class="mb-3 text-2xl font-bold text-center xl:text-3xl enter-x xl:text-center">
    {{ getFormTitle }}
  </h2>
</template>

<script setup>
import { LoginStateEnum, useLoginState } from './useLogin'
import { ref,reactive,computed,unref } from 'vue'

const { getLoginState } = useLoginState()

const getFormTitle = computed(() => {

  const titleObj = {
    [LoginStateEnum.RESET_PASSWORD]: '重置密码',
    [LoginStateEnum.LOGIN]: '登录',
    [LoginStateEnum.REGISTER]: '注册',
    [LoginStateEnum.MOBILE]: '手机号码',
    [LoginStateEnum.QR_CODE]: '二维码登录',
    [LoginStateEnum.SSO_ACCREDIT]: '三方授权',
    [LoginStateEnum.SOCIAL_ACCREDIT]: '绑定账号',
  }
  // 从 titleObj 对象中获取到对应的标题文字并返回
  return titleObj[unref(getLoginState)]
})


</script>

<style scoped>

</style>
