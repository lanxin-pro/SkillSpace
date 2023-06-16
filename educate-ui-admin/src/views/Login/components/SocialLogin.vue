<template>
  <div v-show="socialVisible" class="form-cont" style="width: 360px">
    <LoginFormTitle style="width: 100%" />
    <div class="form-cont">
      <el-tabs class="form" v-model="socialLoginForm.loginType" style=" float:none;">
        <el-tab-pane label="绑定账号" name="uname">
        </el-tab-pane>
      </el-tabs>
      <div>
        <el-form
            ref="formLogin"
            :model="socialLoginForm"
            :rules="loginRules"
            class="login-form">
          <!-- 账号密码登录 -->
          <el-form-item prop="username">
            <el-input
                v-model="socialLoginForm.username"
                type="text"
                auto-complete="off"
                placeholder="账号"
                prefix-icon="Avatar"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                v-model="socialLoginForm.password"
                type="text"
                auto-complete="off"
                placeholder="密码"
                prefix-icon="Lock"
                @keyup.enter.native="getCode"
            />
          </el-form-item>
          <el-checkbox v-model="socialLoginForm.rememberMe" style="margin:0 0 25px 0;">记住密码</el-checkbox>
          <!-- 下方的登录按钮 -->
          <el-form-item style="width:100%;">
            <el-button :loading="loading" size="medium" type="primary" style="width:100%;"
                       @click.native.prevent="getCode">
              <span v-if="!socialLoginLoading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <!-- 图形验证码 -->
    <Verify ref="verify" :captcha-type="captchaType" :img-size="{width:'400px',height:'200px'}"
            @success="handleLogin" />
  </div>
</template>

<script setup>
import { Verify } from '@/components/Verifition/index.js'
import store from '@/store'
import { useStore } from '@/piniastore/modules/user.js'
import { ref,reactive,computed,unref,watch,onMounted } from 'vue'
import { LoginStateEnum, useLoginState } from './useLogin.js'
import { LoginFormTitle } from './index.js'
import router from '@/router'
import { useRouter } from 'vue-router'
import { ElLoading } from "element-plus"
import ElComponents from '@/plugins/modal.js'
import {
  getPassword,
  getRememberMe, getTenantName,
  getUsername,
  removePassword, removeRememberMe, removeTenantName,
  removeUsername,
  setPassword, setRememberMe, setTenantId, setTenantName,
  setUsername
} from "@/utils/auth";

let pinia = useStore()
const captchaEnable = import.meta.env.VITE_APP_CAPTCHA_ENABLE
const captcha = import.meta.env.VITE_CAPTCHATYPE
const captchaType = ref(captcha)
const verify = ref()
const formLogin = ref()
const socialLoginLoading = ref(false)
const { currentRoute } = useRouter()
const { setLoginState,getLoginState } = useLoginState()
const socialLoginForm = reactive({
      loginType: "uname",
      username: "",
      password: "",
      rememberMe: false,
      captchaVerification: "",
      redirect: undefined,
      type: undefined,
      code: undefined,
      state: undefined
})

const socialVisible = computed(()=>{
  return unref(getLoginState) === LoginStateEnum.SOCIAL_ACCREDIT
})
onMounted(async ()=>{
  if(getLoginState.value && getLoginState.value === "SOCIAL_ACCREDIT"){
    if(pinia.getSocialLogin() === '' || pinia.getSocialLogin() === undefined){
      ElComponents.msgError("页面参数异常")
      setLoginState(LoginStateEnum.LOGIN)
      router.push({ path: socialLoginForm.redirect || "/" })
      return
    }
    socialLoginForm.type = pinia.getSocialLogin()
    socialLoginForm.code = currentRoute.value.query.code
    socialLoginForm.state = currentRoute.value.query.state
    // 登录
    socialLoginLoading.value = true
    pinia.SocialLogin({
      type: socialLoginForm.type,
      code: socialLoginForm.code,
      state: socialLoginForm.state,
    }).then(()=>{
      router.push({ path: socialLoginForm.redirect || "/" })
    }).catch((error)=>{
      socialLoginLoading.value = false
      ElComponents.msgError(error)
    })
  }

})
/**
 * 获取验证码
 * @returns {*}
 */
const getCode = ()=>{
  // 情况一，未开启：则直接登录
  if (!captchaEnable) {
    formLogin.value.validate(async valid => {
      if(valid){
        await handleLogin()
      }
    })
  }else{
    // 情况二，已开启：则展示验证码；只有完成验证码的情况，才进行登录
    // 弹出验证码
    formLogin.value.validate(valid => {
      if(valid){
        verify.value.show()
      }
    })
  }
}

/**
 * 登录
 */
const handleLogin = async (captchaParams)=>{
  socialLoginLoading.value = true
  formLogin.value.validate(valid =>{
    if(valid){
      // 系统加载的遮罩层
      ElLoading.service({
        lock: true,
        text: '正在加载系统中...',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      if(socialLoginForm.rememberMe){
        setUsername(socialLoginForm.username)
        setPassword(socialLoginForm.password)
        setRememberMe(socialLoginForm.rememberMe)
      }else{
        removeUsername()
        removePassword()
        removeRememberMe()
      }
      socialLoginForm.captchaVerification = captchaParams.captchaVerification
      // 登录
      store.dispatch('user/Login',{
        socialCode: socialLoginForm.code,
        socialState: socialLoginForm.state,
        socialType: socialLoginForm.type,
        // 账号密码登录
        username: socialLoginForm.username,
        password: socialLoginForm.password,
        captchaVerification: captchaParams.captchaVerification
      }).then(()=>{
        router.push({ path: socialLoginForm.redirect || "/" }).catch(()=>{})
      }).catch((error)=>{
        ElComponents.msgError("[系统错误]请联系管理员message：" + error)
        socialLoginLoading.value = false
      }).finally(()=>{
        setTimeout(() => {
          const loadingInstance = ElLoading.service()
          loadingInstance.close()
        }, 400)
      })
    }
  })
}

/**
 * 这段代码的作用是从当前网页的URL中获取指定参数的值。
 * 它首先创建了一个新的 URL 对象(url)，并使用当前网页的URL作为参数。
 * 然后使用 searchParams 属性获取URL的查询参数对象，
 * 接着使用 get(key) 方法获取指定参数的值 （key代表参数名）。
 * 该函数最终返回获取到的参数值（如果存在），如果参数不存在则返回null。
 * 需要注意的是，该代码使用了decodeURIComponent方法对location.href进行了解码，防止出现特殊字符对URL进行干扰。
 * @param key
 * @returns {string}
 */
const getUrlValue = (key)=>{
  const url = new URL(decodeURIComponent(location.href))
  return url.searchParams.get(key)
}
/**
 * 校验
 * @type {{password: [{trigger: string, message: string, required: boolean}], username: [{trigger: string, message: string, required: boolean}]}}
 */
const loginRules = {
  username: [{ required: true, message: "用户名不能为空", trigger: "change" }],
  password: [{ required: true, message: "密码不能为空", trigger: "change" }]
}

/**
 * 监听当前路由为 SSOAccreditLogin 时，进行数据的初始化
 */
watch(
    () => currentRoute.value,
    (route) => {
      if (route.name === 'socialLogin') {
        setLoginState(LoginStateEnum.SOCIAL_ACCREDIT)
      }
    },
    { immediate: true }
)
</script>

<style scoped>

</style>
