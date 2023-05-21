<template>
  <el-form
      v-show="getShow"
      ref="formSmsLogin"
      :model="loginData.loginForm"
      :rules="rules"
      class="login-form"
      label-position="top"
      label-width="120px"
      size="large"
  >
    <el-row style="margin-left: -10px; margin-right: -10px">
      <!-- 租户名 -->
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
      <!-- 手机号 -->
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item prop="mobileNumber">
          <el-input
              v-model="loginData.loginForm.mobileNumber"
              placeholder="请输入手机号"
              prefix-icon="Cellphone"
          />
        </el-form-item>
      </el-col>
      <!-- 验证码 -->
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item prop="code">
          <el-row :gutter="5" justify="space-between" style="width: 100%">
            <el-col :span="24">
              <el-input
                  v-model="loginData.loginForm.code"
                  placeholder="请输入验证码"
                  prefix-icon="CircleCheck"
              >
                <!-- <el-button class="w-[100%]"> -->
                <template #append>
                  <!--     style="cursor: pointer"   将鼠标光标的形状改为小手图标       -->
                  <span
                      v-if="mobileCodeTimer <= 0"
                      class="getMobileCode"
                      style="cursor: pointer"
                      @click="getCode"
                  >
                    获取验证码
                  </span>
                  <span v-if="mobileCodeTimer > 0" class="getMobileCode" style="cursor: pointer">
                    {{ mobileCodeTimer }}秒后可重新获取
                  </span>
                </template>
              </el-input>
              <!-- </el-button> -->
            </el-col>
          </el-row>
        </el-form-item>
      </el-col>
      <!-- 登录按钮 / 返回按钮 -->
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item>
          <XButton
              :loading="loginLoading"
              title="登录"
              class="w-[100%]"
              type="primary"
              @click="handleLogin()"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-left: 10px; padding-right: 10px">
        <el-form-item>
          <XButton
              :loading="loginLoading"
              title="返回"
              class="w-[100%]"
              @click="handleBackLogin()"
          />
        </el-form-item>
      </el-col>
      <Verify
          ref="verify"
          :captchaType="captchaType"
          :imgSize="{ width: '400px', height: '200px' }"
          mode="pop"
          @success="getSmsCode"
      />
    </el-row>
  </el-form>
</template>

<script setup>
import { sendSmsCode } from '@/api/login'
import store from '@/store'
import router from '@/router'
import { computed, reactive, ref, unref} from 'vue'
import { XButton } from '@/components/XButton/index.js'
import { Verify } from '@/components/Verifition/index.js'
import { LoginFormTitle } from '../components'
import { ElLoading,ElMessage } from 'element-plus'
import { LoginStateEnum, useLoginState } from './useLogin.js'
const { handleBackLogin,getLoginState } = useLoginState()
const getShow = computed(() => unref(getLoginState) === LoginStateEnum.MOBILE)

const formSmsLogin = ref()
// 验证码的ref
const verify = ref()
// 获取环境变量
const captcha = import.meta.env.VITE_CAPTCHATYPE
const captchaType = ref(captcha)
const loginLoading = ref(false)
const mobileCodeTimer = ref(0)
const loginData = reactive({
  captchaEnable: import.meta.env.VITE_APP_CAPTCHA_ENABLE,
  codeImg: '',
  tenantEnable: import.meta.env.VITE_APP_TENANT_ENABLE,
  token: '',
  loading: {
    signIn: false
  },
  loginForm: {
    uuid: '',
    tenantName: '字节跳动',
    mobileNumber: '',
    code: '',
    scene: 21
  }
})
// 校验
const rules = {
  tenantName: [
      { required: true, trigger: "change", message: "租户名称不能为空"}
  ],
  mobileNumber: [
      { required: true, trigger: "blur", message: "手机号不能为空"},
      {
        validator: function (rule, value, callback) {
          if (/^(?:(?:\+|00)86)?1(?:3[\d]|4[5-79]|5[0-35-9]|6[5-7]|7[0-8]|8[\d]|9[189])\d{8}$/.test(value) === false) {
            callback(new Error("手机号格式错误"));
          } else {
            callback();
          }
        }, trigger: "blur"
      }
  ],
}
const getCode = async () => {
  // 情况一，未开启：则直接登录
  if (loginData.captchaEnable === 'false') {
    await getSmsCode({})
  } else {
    // 情况二，已开启：则展示验证码；只有完成验证码的情况，才进行登录
    // 弹出验证码
    formSmsLogin.value.validate( async (valid) => {
      if(valid) {
        verify.value.show()
      }
    })
  }
}

const handleLogin = (()=>{
  formSmsLogin.value.validate( async (valid) => {
    d
  })
})

// 获取验证码
const getSmsCode = (()=>{
  // 防止越过上面的代码来刷新
  if (mobileCodeTimer.value > 0) return;
  formSmsLogin.value.validate( async (valid) => {
    if(valid){
      try {
        await sendSmsCode(loginData.loginForm.mobileNumber,loginData.loginForm.scene,loginData.loginForm.uuid,loginData.loginForm.code)
        ElMessage({
          type: 'success',
          duration: 4 * 1000,
          message: "获取验证码成功"
        })
        mobileCodeTimer.value = 120
        let msgTimer = setInterval(() => {
          mobileCodeTimer.value = mobileCodeTimer.value - 1;
          if (mobileCodeTimer.value <= 0) {
            clearInterval(msgTimer);
          }
        }, 1000);
      }catch (error){
        ElMessage({
          type: 'error',
          duration: 0,
          message: error
        })
      }
    }
  })
})
</script>

<style lang="scss" scoped>
:deep(.anticon) {
  &:hover {
    color: var(--el-color-primary) !important;
  }
}

.smsbtn {
  margin-top: 33px;
}
</style>
