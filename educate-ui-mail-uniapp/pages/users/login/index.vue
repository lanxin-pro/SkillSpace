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
              <input type="password" class="texts" placeholder="填写登录密码" v-model="password" required />
            </view>
          </view>
          <view class="loginbtn">
            <!--    base.css里面有专属loading    -->
            <text class="loading" v-if="loading" @click="open">登录</text>
            <text class="loading" :class="!loading ? 'loadingImg' : '' " v-else>登录中...</text>
          </view>
          <view class="login-explain">
            <checkbox :value="agreement" :checked="false">
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
            </checkbox>
          </view>

          <!--    登录弹出层      -->
          <uni-popup ref="popup" type="dialog">
            <uni-popup-dialog
                type="warn"
                mode="base"
                title="用户协议与隐私条款"
                :duration="2000"
                :before-close="true"
                content='欢迎使用 uni-popup!'
                @close="close"
                @confirm="confirm"
				    >
              <slot>
                <view class="tip-interests">
                  为了保障您的合法权益，请您先阅读并同意
                  <navigator url="/pages/users/agreement/index" hover-class="navigator-hover" class="display-inline-block">
                    用户协议
                  </navigator>
                  、
                  <navigator url="/pages/users/privacy/index" hover-class="navigator-hover" class="display-inline-block">
                    隐私政策
                  </navigator>
                </view>
              </slot>
            </uni-popup-dialog>
          </uni-popup>

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
          遇到问题?<navigator url="/pages/users/agreement/index" hover-class="navigator-hover" class="display-inline-block">查看帮助</navigator>
        </view>
      </view>
    </view>
	</view>
</template>

<script>
	import CountryPicker from '@/components/country-picker/CountryPicker.vue'
	export default {
		data() {
			return {
        // 1：登录；0：注册
        loginOrRegister: 1,
        loading: true,
        agreement: false
			}
		},
		methods: {
      onCountryChange(selectedCountry) {
          console.log(selectedCountry);
      },
      open() {
        this.$refs.popup.open()
      },
      /**
       * 点击取消按钮触发
       * @param {Object} done
       */
      close() {
        // TODO 做一些其他的事情，before-close 为true的情况下，手动执行 close 才会关闭对话框
        // ...
        // 每次打开的时候关闭协议
        this.agreement = false
        this.$refs.popup.close()
      },
      /**
       * 点击确认按钮触发
       * @param {Object} done
       * @param {Object} value
       */
      confirm() {
        this.agreement = true
        // TODO 做一些其他的事情，手动执行 close 才会关闭对话框
        // ...
        this.$refs.popup.close()
      }

		},
    onLoad() {
      // 每次打开的时候关闭协议
      this.agreement = false
    },
		components: {
			CountryPicker
		}
	}
</script>

<style lang="scss">
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
            input {
              margin-left: 20rpx;
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
          .loadingImg {
            background: url(https://static.hdslb.com/passport/img/mini-login/loading_b.gif) center no-repeat;
          }
        }
        .login-explain {
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
