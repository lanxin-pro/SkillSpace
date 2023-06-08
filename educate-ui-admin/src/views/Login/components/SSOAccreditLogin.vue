<template>
  <div v-show="ssoVisible" class="form-cont">
    <div>授权登录</div>
  </div>
</template>

<script setup>
import { ref,reactive,computed,unref,watch } from 'vue'
import { LoginStateEnum, useLoginState } from './useLogin.js'
import { useRouter } from 'vue-router'

const { currentRoute } = useRouter()

const { setLoginState,getLoginState } = useLoginState()

const ssoVisible = computed(() => {
  return unref(getLoginState) === LoginStateEnum.SSO_ACCREDIT
})

/**
 * 监听当前路由为 SSOAccreditLogin 时，进行数据的初始化
 */
watch(
    () => currentRoute.value,
    (route) => {
      if (route.name === 'SSOAccreditLogin') {
        setLoginState(LoginStateEnum.SSO_ACCREDIT)
      }
    },
    { immediate: true }
)
</script>

<style scoped>

</style>
