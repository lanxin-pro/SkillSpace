<template>
  <view class="content">
    <view class="tab-nav">
      <view :class="loginOrRegister === 1 ? 'active animated tada' : ''" class="t" for="login"
            @click="loginOrRegister = 1">登录</view>
      <view :class="loginOrRegister === 0 ? 'active animated tada' : ''" class="t" for="register"
            @click="loginOrRegister = 0">注册</view>
    </view>
    <view class="panel">
      <view class="login-normal">
        <!-- 输入账号密码 -->
        <view class="login-container" v-if="loginOrRegister === 1">
          <view class="flex-end">
            <a class="a-text">短信登录&gt;</a>
          </view>
          <view class="item">
            <view class="acea-row row-middle">
              <image src="/static/images/login/phone_1.png" />
              <input type="text" class="texts" placeholder="输入手机号码" v-model="account" required/>
            </view>
          </view>
          <view class="item">
            <view class="acea-row row-middle">
              <image src="/static/images/login/code_2.png"></image>
              <input type="password" class="texts" placeholder="填写验证码" v-model="captcha" required />
              <button class="code" :disabled="disabled" :class="disabled === true ? 'on' : ''" @tap="sendCode()">
                {{ text }}
              </button>
            </view>
          </view>
          <view class="loginbtn">
            <!--    base.css里面有专属loading    -->
            <text class="loading" v-if="loading" @click="open">登录</text>
            <text class="loading" :class="!loading ? 'loadingImg' : '' " v-else>登录中...</text>
          </view>
          <view class="login-explain">
            <uv-checkbox-group  v-model="agreement">
              <uv-checkbox size="14" :name="true" activeColor="#f55b91" label="全选">
                <view>
                  登录即代表你同意
                  <view class="user-agreement">
                    <navigator url="/pages/users/agreement/index" hover-class="navigator-hover">
                      用户协议
                    </navigator>
                  </view>
                  和
                  <view class="privacy-policy">
                    <navigator url="/pages/users/privacy/index" hover-class="navigator-hover">
                      隐私政策
                    </navigator>
                  </view>
                </view>
              </uv-checkbox>
            </uv-checkbox-group>
          </view>

          <!--    登录弹出层      -->
          <su-popup z-index="998" class="popup" :showClose="true" round="6" :show="isShow"  @close="isShow = false" type="center">
            <view class="popup-container">
              <view class="popup-title">
                用户协议与隐私条款
              </view>
              <view class="popup-tip-interests">
                为了保障您的合法权益，请您先阅读并同意
                <navigator url="/pages/users/agreement/index" hover-class="navigator-hover" class="display-inline navigator-color">
                  用户协议
                </navigator>
                、
                <navigator url="/pages/users/privacy/index" hover-class="navigator-hover" class="display-inline navigator-color">
                  隐私政策
                </navigator>
                ，未注册绑定手机号验证成功后将自动注册。
                <button @tap="loginMobile" class="agree-and-sign-in">同意并登录</button>
                <button @tap="handleDisagree" class="disagree">不同意</button>
              </view>
            </view>
          </su-popup>

        </view>
        <view v-else>
          <!-- 注册页 -->
          <view class="register">
            <view class="item">
              <view class="num">
                <view class="loginForm">
                  <CountryPicker @change="onCountryChange"></CountryPicker>
                  <view class="acea-row row-middle">
                    <image src="/static/images/login/phone_1.png" />
                    <input type="text" class="texts" placeholder="输入手机号码" v-model="account" required/>
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>
        <view class="issue">
          遇到问题?<navigator url="/pages/users/agreement/index" hover-class="navigator-hover" class="display-inline navigator-color">查看帮助</navigator>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import SuPopup from "../../../sheep/ui/su-popup/su-popup"
import { ref, reactive } from 'vue'
import sheep from '@/sheep'
import AuthApi from '@/sheep/api/member/auth.js'

// 1：登录；0：注册
const loginOrRegister = ref(1)
const loading = ref(true)
const agreement = ref(false)
// 手机号
const account = ref("")
// 验证码
const captcha = ref("")
// 验证码状态
const disabled = ref(false)
// 验证码文字
const text = ref("获取验证码")

const isShow = ref(false)

const open = () => {
  if(agreement.value[0] === true) {
    loginMobile()
  } else {
    isShow.value = true
  }
}
const sendCode = async () => {
  if (!account.value) {
    return sheep.$helper.toast('请填写手机号码')
  }
  if (!/^1(3|4|5|7|8|9|6)\d{9}$/i.test(account.value)) {
    return sheep.$helper.toast('请输入正确的手机号码')
  }
  await AuthApi.sendSmsCode(account.value, 23)
      .then(res => {
        console.log('看看你的结果', res)
        sheep.$helper.toast('验证码发送成功！')
        // 发送验证码
        sendCodeNumber()
      }).catch(err => {
       console.log('代码发生错误了！！！', err)
      });
}
const sendCodeNumber = () => {
  if (disabled.value) {
    return
  }
  // 禁用
  disabled.value = true
  let n = 60
  text.value = "剩余 " + n + "s"
  const run = setInterval(() => {
    n = n - 1
    if (n < 0) {
      clearInterval(run)
    }
    text.value = "剩余 " + n + "s"
    // 时间到了在开启
    if (text.value < "剩余 " + 0 + "s") {
      disabled.value = false
      text.value = "重新获取"
    }
  }, 1000)
}
const loginMobile = async () => {
  if (!account.value) {
    isShow.value = false
    return sheep.$helper.toast('请填写手机号码')
  }
  if (!/^1(3|4|5|7|8|9|6)\d{9}$/i.test(account.value)) {
    isShow.value = false
    return sheep.$helper.toast('请输入正确的手机号码')
  }
  if (!captcha.value) {
    isShow.value = false
    return sheep.$helper.toast('请填写验证码')
  }
  if (!/^[\w\d]+$/i.test(captcha.value)) {
    isShow.value = false
    return sheep.$helper.toast('请输入正确的验证码')
  }
  // 提交数据
  const { code } = await AuthApi.smsLogin({
    mobile: account.value,
    code: captcha.value
  })
  if (code === 0) {
    // 后退
   /* uni.navigateBack({
      delta: 1
    })*/
  }
}
const handleDisagree = () => {
  isShow.value = false
  agreement.value = false
}
</script>

<style scoped lang="scss">
button {
  padding: 0;
  margin: 0;
  line-height: normal;
  background-color: #fff
}
button::after {
  border: 0
}
/* 弹出层 */
.tip-interests {
  font-size: 24rpx;
  line-height: 34rpx;
}
.content {
  background: url(https://static.hdslb.com/passport/img/mini-login/banner.jpg) left top no-repeat #fff;
  border-radius: 8px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.1);
  width: 330px;
  padding: 25px 20px 40px 35px;
  position: absolute;
  top: 45%;
  left: 0;
  right: 0;
  margin: -200px auto auto;
  .tab-nav {
    font-size: 18px;
    line-height: 2;
    margin-bottom: 25px;
    .t {
      border-bottom: 5px solid #eee;
      cursor: pointer;
      float: left;
      text-align: center;
      width: 64px;
    }
    .t.active {
      border-color: #00a1d6;
      color: #00a1d6;
    }
  }
  .panel {
    .login-normal {
      margin-top: 100rpx;
      .register {
        .item {
          margin-top: 160rpx;
          .num {
            .loginForm {
              display: flex;
              align-items: center;
              gap: 30rpx;
            }
            .acea-row.row-middle {
              border-bottom: 1px solid #F0F0F0;
              background: #fff;
              input {
                margin-left: 10rpx;
              }
            }
            image {
              width: 34rpx;
              height: 34rpx;
            }
            .texts{
              flex: 1;
              font-size: 28rpx;
              height: 80rpx;
              line-height: 80rpx;
              display: flex;
              justify-content: center;
              align-items: center;

            }

          }

        }
      }
      .flex-end {
        display: flex;
        justify-content: flex-end;
        align-items: center;
      }
      .login-container {
        .a-text {
          color: #00a1d6;
        }
        .item {
          margin-top: 20rpx;
          border-bottom: 1px solid #F0F0F0;
          background: #fff;
          .acea-row.row-middle {
            position: relative;
            input {
              margin-left: 20rpx;
            }
            .code {
              position: absolute;
              right: 16rpx;
              top: 50%;
              color: rgb(251, 114, 153);
              font-size: 26rpx;
              transform: translateY(-50%);
              /* 这是一个属性选择器，用于选择带有 disabled 属性的元素 */
              &[disabled] {
                padding: 2rpx 10rpx;
                background: rgba(0, 0, 0, 0.05);
                color: #999;
              }
            }
          }
          image {
            width: 34rpx;
            height: 34rpx;
          }
          .texts{
            flex: 1;
            font-size: 28rpx;
            height: 80rpx;
            line-height: 80rpx;
            display: flex;
            justify-content: center;
            align-items: center;
          }
        }
        .loginbtn {
          margin-top: 60rpx;
          background: #00a1d6;
          border-radius: 8rpx;
          cursor: pointer;
          text-align: center;
          transition: background .2s ease;
          color: #fff;
          box-sizing: border-box;
          .loading {
            width: 100%;
            height: 80rpx;
            line-height: 80rpx;
            align-items: center;
            justify-content: center;
            position: relative;
            text-align: center
          }
          .loadingImg {
            background: url(https://static.hdslb.com/passport/img/mini-login/loading_b.gif) center no-repeat;
          }
        }
        .login-explain {
          display: flex;
          align-items: center;
          margin-top: 40rpx;
          width: 100%;
          font-family: PingFangSC-Regular;
          font-size: 12px;
          color: #99A2AA;
          line-height: 16px;
          .privacy-policy {
            display: inline-block;
            color: #00a1d6;
            text-decoration: none;
            padding: 5rpx;
          }
          .user-agreement {
            display: inline-block;
            color: #00a1d6;
            text-decoration: none;
            padding: 5rpx;
          }

        }

        .popup-container {
          max-width: 510rpx;
          padding: 0 20rpx 20rpx;
          .popup-title {
            margin: 16rpx;
            font-weight: 600;
            display: flex;
            justify-content: center;
          }
          .popup-tip-interests {
            font-size: 22rpx;
            line-height: 36rpx;
            .agree-and-sign-in {
              margin-top: 20rpx;
              background: linear-gradient(to right, #fc7296, #f86194, #f1568d, #e54c72);
              color: #ffffff;
              border-radius: 30rpx;
              padding: 14rpx 0;
              font-size: 30rpx;
              font-weight: 600;
            }
            .disagree {
              margin-top: 20rpx;
              font-size: 30rpx;
              color: #6f6f6f;
            }
          }
        }
      }
      .issue {
        margin-top: 60rpx;
        text-align: center;
        font-size: 23rpx;
        display: flex;
        justify-content: center;
      }
    }
  }

}
</style>
