<template>
  <div v-show="ssoVisible" class="form-cont">
    <!-- 应用名 -->
    <LoginFormTitle style="width: 100%" />
    <el-tabs class="form" style="float: none" value="uname">
      <el-tab-pane :label="client.name" name="uname" />
    </el-tabs>

    <div>
      <el-form :model="formData" class="login-form">
        <!-- 授权范围的选择 -->
        此第三方应用请求获得以下权限：
        <el-form-item prop="scopes">
          <el-checkbox-group v-model="formData.scopes">
            <el-checkbox
                v-for="scope in queryParams.scopes"
                :key="scope"
                :label="scope"
                style="display: block; margin-bottom: -10px"
            >
              {{ formatScope(scope) }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <!-- 下方的授权按钮 -->
        <el-form-item class="w-1/1">
          <el-button
              :loading="formLoading"
              class="w-6/10"
              type="primary"
              @click.prevent="handleAuthorize(true)"
          >
            <span v-if="!formLoading">同意授权</span>
            <span v-else>授 权 中...</span>
          </el-button>
          <el-button class="w-3/10" @click.prevent="handleAuthorize(false)">拒绝</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref,reactive,computed,unref,watch } from 'vue'
import { LoginStateEnum, useLoginState } from './useLogin.js'
import { LoginFormTitle } from './index.js'
import * as OAuth2Api from '@/api/login/oauth2'
import { useRouter } from 'vue-router'

const { currentRoute } = useRouter()
const { setLoginState,getLoginState } = useLoginState()

// 客户端信息
const client = ref({
  name: '',
  logo: ''
})
const queryParams = reactive({
  // URL 上的 client_id、scope 等参数
  responseType: '',
  clientId: '',
  redirectUri: '',
  state: '',
  // 优先从 query 参数获取；如果未传递，从后端获取
  scopes: []
})
const formData = reactive({
  // 已选中的 scope 数组
  scopes: []
})
// 表单是否提交中
const formLoading = ref(false)

const ssoVisible = computed(() => {
  return unref(getLoginState) === LoginStateEnum.SSO_ACCREDIT
})

/**
 * 初始化授权信息
 * @returns {Promise<void>}
 */
const init = async () => {
  console.log(currentRoute.value)
  // 解析参数
  // 例如说【自动授权不通过】：client_id=default&redirect_uri=https%3A%2F%2Fwww.iocoder.cn&response_type=code&scope=user.read%20user.write
  // 例如说【自动授权通过】：client_id=default&redirect_uri=https%3A%2F%2Fwww.iocoder.cn&response_type=code&scope=user.read
  queryParams.responseType = currentRoute.value.query.response_type
  queryParams.clientId = currentRoute.value.query.client_id
  queryParams.redirectUri = currentRoute.value.query.redirect_uri
  queryParams.state = currentRoute.value.query.state

  console.log(queryParams.responseType)
  console.log(queryParams.clientId)
  console.log(queryParams.redirectUri)
  console.log(queryParams.state)

  if (currentRoute.value.query.scope) {
    queryParams.scopes = (currentRoute.value.query.scope).split(' ')
    console.log("作用域参数",queryParams.scopes)
  }
  // 如果有 scope 参数，先执行一次自动授权，看看是否之前都授权过了。


  // 获取授权页的基本信息
  const data = await OAuth2Api.getAuthorize(queryParams.clientId)
  console.log("授权的结果",data)
}



/**
 * 监听当前路由为 SSOAccreditLogin 时，进行数据的初始化
 */
watch(
    () => currentRoute.value,
    (route) => {
      if (route.name === 'SSOLogin') {
        init()
        setLoginState(LoginStateEnum.SSO_ACCREDIT)
      }
    },
    { immediate: true }
)
</script>

<style scoped>

</style>
