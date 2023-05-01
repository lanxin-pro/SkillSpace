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
              :placeholder="租户"
              :prefix-icon="iconHouse"
              type="primary"
              link
          />
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item prop="username">
          <el-input
              v-model="loginData.loginForm.username"
              :placeholder="用户名"
              :prefix-icon="iconAvatar"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item prop="password">
          <el-input
              v-model="loginData.loginForm.password"
              :placeholder="密码"
              :prefix-icon="iconLock"
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
            <Icon
                v-for="(item, key) in socialList"
                :key="key"
                :icon="item.icon"
                :size="30"
                class="cursor-pointer anticon"
                color="#999"
                @click="doSocialLogin(item.type)"
            />
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
import { reactive,ref } from 'vue'
import { XButton } from '@/components/XButton/index.js'
import { LoginFormTitle } from '../components'
import { ElLoading } from 'element-plus'


const getShow = ref(true)
const loginLoading = ref(false)
const verify = ref()
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
    rememberMe: false
  }
})
// 其他登录方式
const socialList = [
  { icon: 'ant-design:github-filled', type: 0 },
  { icon: 'ant-design:wechat-filled', type: 30 },
  { icon: 'ant-design:alipay-circle-filled', type: 0 },
  { icon: 'ant-design:dingtalk-circle-filled', type: 20 }
]









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
const handleLogin = async ()=>{

}
</script>

<style scoped>

</style>
