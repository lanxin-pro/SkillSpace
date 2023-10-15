<template>
  <div v-show="ssoVisible" class="form-cont">
    <!-- 应用名 -->
    <LoginFormTitle style="width: 100%" />
    <el-tabs class="form" style="float: none" value="uname">
      <el-tab-pane :label="client.name" name="uname" />
    </el-tabs>

    <div>
      <el-form :model="formData" class="login-form">
        <div style="display: flex;align-items: center;justify-content: center">
          <el-image
              style="width: 100px; height: 100px;margin-bottom: 10px"
              :src="client.logo"
              :zoom-rate="1.2"
              :max-scale="7"
              :min-scale="0.2"
              :initial-index="4"
              fit="cover"
          />
        </div>

        <!-- 授权范围的选择 -->
        此第三方应用请求获得以下权限：
        <el-form-item prop="scopes">
          <!--    绑定的值      -->
          <el-checkbox-group v-model="formData.scopes">
            <!--     展示出所有的列表       -->
            <el-checkbox
                v-for="scope in queryParams.scopes"
                :key="scope"
                :label="scope"
                style="display: flex;"
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
import { useRouter,useRoute } from 'vue-router'

const route = useRoute() // 路由
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
  console.log(route.query)
  // 防止在没有登录的情况下循环弹窗
  if (typeof route.query.client_id === 'undefined') return
  // 解析参数
  // 例如说【自动授权不通过】：client_id=default&redirect_uri=https%3A%2F%2Fwww.iocoder.cn&response_type=code&scope=user.read%20user.write
  // 例如说【自动授权通过】：client_id=default&redirect_uri=https%3A%2F%2Fwww.iocoder.cn&response_type=code&scope=user.read
  queryParams.responseType = route.query.response_type
  queryParams.clientId = route.query.client_id
  queryParams.redirectUri = route.query.redirect_uri
  queryParams.state = route.query.state
  console.log("授权码类型",queryParams.responseType)
  console.log("客户端",queryParams.clientId)
  console.log("回调地址",queryParams.redirectUri)
  console.log("状态",queryParams.state)
  if (route.query.scope) {
    queryParams.scopes = (currentRoute.value.query.scope).split(' ')
    console.log("作用域参数",queryParams.scopes)
  }
  console.log("queryParams.scopes.length > 0",queryParams.scopes.length > 0)
  // 如果有 scope 参数，先执行一次自动授权，看看是否之前都授权过了。
  if (queryParams.scopes.length > 0) {
    const response = await doAuthorize(true, queryParams.scopes, [])
    if (response.data) {
      location.href = response.data
      return
    }
  }

  // 获取授权页的基本信息
  const response = await OAuth2Api.getAuthorize(queryParams.clientId)
  client.value = response.data.client
  // 解析 scope
  let scopes
  // 1.1 如果 params.scope 非空，则过滤下返回的 scopes
  if (queryParams.scopes.length > 0) {
    scopes = []
    for (const scope of response.data.scopes) {
      if (queryParams.scopes.indexOf(scope.key) >= 0) {
        scopes.push(scope)
      }
    }
    // 1.2 如果 params.scope 为空，则使用返回的 scopes 设置它
  } else {
    scopes = response.data.scopes
    for (const scope of scopes) {
      queryParams.scopes.push(scope.key)
    }
  }
  // 生成已选中的 checkedScopes
  for (const scope of scopes) {
    // 存在权限的，就给formData赋值
    if (scope.value) {
      formData.scopes.push(scope.key)
    }
  }
}

/** 处理授权的提交 */
const handleAuthorize = async (approved)=>{
  // 计算 checkedScopes + uncheckedScopes
  let checkedScopes
  let uncheckedScopes
  if (approved) {
    // 同意授权，按照用户的选择
    checkedScopes = formData.scopes
    uncheckedScopes = queryParams.scopes.filter((item) => checkedScopes.indexOf(item) === -1)
  } else {
    // 拒绝，则都是取消
    checkedScopes = []
    uncheckedScopes = queryParams.scopes
  }
  // 提交授权的请求
  formLoading.value = true
  try {
    const response = await doAuthorize(false, checkedScopes, uncheckedScopes)
    if (!response.data) {
      return
    }
    location.href = response.data
  } finally {
    formLoading.value = false
  }
}

/** 调用授权 API 接口 */
const doAuthorize = (autoApprove, checkedScopes, uncheckedScopes) => {
  return OAuth2Api.authorize(
      queryParams.responseType,
      queryParams.clientId,
      queryParams.redirectUri,
      queryParams.state,
      autoApprove,
      checkedScopes,
      uncheckedScopes
  )
}

/** 格式化 scope 文本 */
const formatScope = (scope) => {
  // 格式化 scope 授权范围，方便用户理解。
  // 这里仅仅是一个 demo，可以考虑录入到字典数据中，例如说字典类型 "system_oauth2_scope"，它的每个 scope 都是一条字典数据。
  switch (scope) {
    case 'user.read':
      return '访问你的个人信息'
    case 'user.write':
      return '修改你的个人信息'
    default:
      return scope
  }
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
