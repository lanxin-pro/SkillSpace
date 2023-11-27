<template>
	<view>
		<view class="user-header-info">
			<div class="user-status">
        <div class="left-info">
          <image class="user-avatar" src="../../static/images/user-info/noface.jpg" />
          <div class="title" v-if="!isLogin" @tap="openAuto">点击登录</div>
        </div>
        <div class="right-info">
          <text class='iconfont icon-arrow' />
        </div>
			</div>
      <div class="user-simple-info">
        <div class="user-info">
          <hr>
          <div class="font">动态</div>
        </div>
          <div class="vertical" />
        <div class="user-info">
          <hr>
          <div class="font">关注</div>
        </div>
          <div class="vertical" />
        <div class="user-info">
          <hr>
          <div class="font">粉丝</div>
        </div>
      </div>
      <div class="promotion">
        <div class="membership-interest">
          成为大会员
        </div>
      </div>

		</view>
	</view>
</template>

<script>
  import { BACK_URL } from '@/config/cache.js'
  import Cache from '@/utils/cache.js'
  import { toLogin } from '@/libs/login.js'
  import { mapGetters } from "vuex"

	export default {
    computed: mapGetters(['isLogin']),
		data() {
			return {

			}
		},
		methods: {
      // 打开授权
      openAuto() {
        // 设置缓存，回到的地址是空
        Cache.set(BACK_URL, '')
        // 登录store
        toLogin()
      }
		}
	}
</script>

<style lang="scss">
.user-header-info {
  padding: 60rpx 20rpx 20rpx 15rpx;
  .user-status {
    display: flex;
    justify-content: space-between;

    align-items: center;
    .left-info {
      display: flex;
      justify-content: center;
      align-items: center;
      .title {
        margin-left: 30rpx;
        font-size: 28rpx;
        font-weight: 600;
      }
      .user-avatar {
        cursor: pointer;
        width: 55px;
        height: 55px;
        border-radius: 50%;
      }
    }
    .right-info {

    }
  }
  .user-simple-info {
    margin: 60rpx 0 20rpx;
    display: flex;
    justify-content: center;
    gap: 100rpx;
    .user-info {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      gap: 15rpx;

/*      .font {
        color: rgb(128, 129, 131);
      }*/
    }
    .link-right {
      width: 50px;
      height: 20%;
      border-right: solid #ACC0D8 1px;
    }
  }

  .promotion {
    .membership-interest {

    }
  }


}
.vertical {
  border-left: 1px solid black;
  margin: 10rpx;
}
</style>
