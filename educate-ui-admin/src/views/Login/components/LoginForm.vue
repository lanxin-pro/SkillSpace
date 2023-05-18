<template>
  <el-form
      v-show="getShow"
      ref="formLogin"
      :model="loginData.loginForm"
      :rules="LoginRules"
      class="login-form"
      label-position="top"
      label-width="120px"
      size="large"
  >
    <el-row style="maring-left: -10px; maring-right: -10px">
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item>
          <LoginFormTitle style="width: 100%" />
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item v-if="loginData.tenantEnable === 'true'" prop="tenantName">
          <el-input
              v-model="loginData.loginForm.tenantName"
              placeholder="请输入租户名称"
              prefix-icon="House"
              type="primary"
              link
          />
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item prop="username">
          <el-input
              v-model="loginData.loginForm.username"
              placeholder="请输入用户名名称"
              prefix-icon="Avatar"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item prop="password">
          <el-input
              v-model="loginData.loginForm.password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
              type="password"
              @keyup.enter="getCode()"
          />
        </el-form-item>
      </el-col>
      <el-col
          :span="24"
          style="padding-left: 10px; padding-right: 10px; margin-top: -20px; margin-bottom: -20px"
      >
        <el-form-item>
          <el-row justify="space-between" style="width: 100%">
            <el-col :span="6">
              <el-checkbox v-model="loginData.loginForm.rememberMe">
                记住我
              </el-checkbox>
            </el-col>
            <el-col :offset="6" :span="12">
              <el-link style="float: right" type="primary">忘记密码</el-link>
            </el-col>
          </el-row>
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item>
          <XButton
              :loading="loginLoading"
              title="登录"
              class="w-[100%]"
              type="primary"
              @click="getCode()"
          />
        </el-form-item>
      </el-col>
      <Verify
          ref="verify"
          :captchaType="captchaType"
          :imgSize="{ width: '400px', height: '200px' }"
          mode="pop"
          @success="handleLogin"
      />
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item>
          <el-row :gutter="5" justify="space-between" style="width: 100%">
            <el-col :span="8">
              <XButton
                  title="手机登录"
                  class="w-[100%]"
                  @click="setLoginState(LoginStateEnum.MOBILE)"
              />
            </el-col>
            <el-col :span="8">
              <XButton
                  title="二维码登录"
                  class="w-[100%]"
                  @click="setLoginState(LoginStateEnum.QR_CODE)"
              />
            </el-col>
            <el-col :span="8">
              <XButton
                  title="注册"
                  class="w-[100%]"
                  @click="setLoginState(LoginStateEnum.REGISTER)"
              />
            </el-col>
          </el-row>
        </el-form-item>
      </el-col>
      <el-divider content-position="center">其他登录方式</el-divider>
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item>
          <div class="flex justify-between w-[100%]">
            <template v-for="(item, key) in socialList">
              <svg class="alibabaiconfont" aria-hidden="true">
                <use :xlink:href="item.icon"></use>
              </svg>
            </template>

<!--            <Icon

                :key="key"
                :icon="item.icon"
                :size="30"
                class=""
                color="#999"
                @click="doSocialLogin(item.type)"
            />-->
          </div>
        </el-form-item>
      </el-col>
      <el-divider content-position="center">萌新必读</el-divider>
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item>
          <div class="flex justify-between w-[100%]">
            <el-link href="https://doc.iocoder.cn/" target="_blank">📚开发指南</el-link>
            <el-link href="https://doc.iocoder.cn/video/" target="_blank">🔥视频教程</el-link>
            <el-link href="https://www.iocoder.cn/Interview/good-collection/" target="_blank">
              ⚡面试手册
            </el-link>
            <el-link href="http://static.yudao.iocoder.cn/mp/Aix9975.jpeg" target="_blank">
              🤝外包咨询


            </el-link>
          </div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup>
import '@/assets/icons/login/iconfont.css' // 阿里图标
import '@/assets/icons/login/iconfont.js' // 阿里图标
import store from '@/store'
import router from '@/router'
import {computed, reactive, ref, unref} from 'vue'
import { XButton } from '@/components/XButton/index.js'
import { Verify } from '@/components/Verifition/index.js'
import { LoginFormTitle } from '../components'
import { ElLoading } from 'element-plus'
import { LoginStateEnum, useLoginState } from './useLogin.js'
const { setLoginState,getLoginState } = useLoginState()

import {
  getPassword,
  getRememberMe, getTenantName,
  getUsername,
  removePassword, removeRememberMe, removeTenantName,
  removeUsername,
  setPassword, setRememberMe, setTenantId, setTenantName,
  setUsername
} from "@/utils/auth";


const formLogin = ref()

const loginLoading = ref(false)
const verify = ref()
const captcha = import.meta.env.VITE_CAPTCHATYPE
const captchaType = ref(captcha)
const redirect = ref()
// 校验
const LoginRules = {
  tenantName: [{ required: true, message: "租户名称不能为空", trigger: "change" }],
  username: [{ required: true, message: "用户名不能为空", trigger: "change" }],
  password: [{ required: true, message: "密码不能为空", trigger: "change" }]
}
// 表单提交数据
const loginData = reactive({
  isShowPassword: false,
  captchaEnable: import.meta.env.VITE_APP_CAPTCHA_ENABLE,
  tenantEnable: import.meta.env.VITE_APP_TENANT_ENABLE,
  loginForm: {
    tenantName: '字节跳动',
    username: 'j-sentinel',
    password: '123456',
    captchaVerification: '',
    rememberMe: false,
    loginType: "uname",
  }
})
// 其他登录方式
const socialList = [
  { icon: '#icon-github', type: 0 },
  { icon: '#icon-weixin', type: 30 },
  { icon: '#icon-zhifubao', type: 0 },
  { icon: '#icon-dingding', type: 20 }
]

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN)


// 获取验证码
const getCode = async () => {
  // 情况一，未开启：则直接登录
  if (loginData.captchaEnable === 'false') {
    await handleLogin({})
  } else {
    // 情况二，已开启：则展示验证码；只有完成验证码的情况，才进行登录
    // 弹出验证码
    verify.value.show()
  }
}

// 登录
const handleLogin = async (captchaParams)=>{
  loginLoading.value = true
  formLogin.value.validate(valid => {
    if(valid){
      // 系统加载的遮罩层
      ElLoading.service({
        lock: true,
        text: '正在加载系统中...',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      if(loginData.loginForm.rememberMe){
        setUsername(loginData.loginForm.username)
        setPassword(loginData.loginForm.password)
        setRememberMe(loginData.loginForm.rememberMe)
        setTenantName(loginData.loginForm.tenantName)
      }else{
        removeUsername()
        removePassword()
        removeRememberMe()
        removeTenantName()
      }
      loginData.loginForm.captchaVerification = captchaParams.captchaVerification
      store.dispatch(loginData.loginForm.loginType === "sms" ? "user/SmsLogin" : "user/Login",loginData.loginForm)
          .then(()=>{
          router.push({path: redirect.value || "/"}).catch(()=>{
            console.log("路由error")
          })
      }).catch(()=>{
        loginLoading.value = false
      }).finally(()=>{
        setTimeout(() => {
          const loadingInstance = ElLoading.service()
          loadingInstance.close()
        }, 400)
      })
    }
  })

}
</script>

<style scoped>

.alibabaiconfont{
  cursor: pointer;
  width: 6em;
  height: 2em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
.alibabaiconfont:last-child{
  width: 6em;
  height: 2.3em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
  position: relative;
  top: -2px;
}
</style>
