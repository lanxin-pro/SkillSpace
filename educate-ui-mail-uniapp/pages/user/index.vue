<template>
	<view class="app-container">
		<view class="user-header-info">
			<view class="user-status">
        <view class="left-info">
          <image class="user-avatar" src="../../static/images/user-info/noface.jpg" />
          <view class="title" v-if="!isLogin" @tap="openAuto">点击登录</view>
        </view>
        <view class="right-info">
          <text class='iconfont icon-xiangyou' />
        </view>
			</view>
      <view class="user-simple-info">
        <view class="user-info">
          <hr>
          <view class="font">动态</view>
        </view>
          <view class="vertical" />
        <view class="user-info">
          <hr>
          <view class="font">关注</view>
        </view>
          <view class="vertical" />
        <view class="user-info">
          <hr>
          <view class="font">粉丝</view>
        </view>
      </view>
      <view class="promotion">
        <view class="membership-interest">
          <view class="membership-interest-flex">
            <svg width="30" style="color: rgb(255, 87, 144)" height="30" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg" class="right-entry-icon"><path fill-rule="evenodd" clip-rule="evenodd" d="M10 1C5.02955 1 1 5.02955 1 10C1 14.9705 5.02955 19 10 19C14.9705 19 19 14.9705 19 10C19 5.02955 14.9705 1 10 1ZM10.0006 2.63614C14.0612 2.63614 17.3642 5.93996 17.3642 9.99977C17.3642 14.0604 14.0612 17.3634 10.0006 17.3634C5.93996 17.3634 2.63696 14.0604 2.63696 9.99977C2.63696 5.93996 5.93996 2.63614 10.0006 2.63614Z" fill="currentColor"></path><path d="M13.1381 8.05573V8.05331H10.7706C10.7859 7.8643 10.7948 7.67286 10.7948 7.47981C10.7948 7.26414 10.7843 7.05008 10.7649 6.83926C10.7658 6.82552 10.7674 6.81179 10.7674 6.79725V6.79483C10.7674 6.35541 10.4111 6 9.97254 6C9.53312 6 9.17771 6.35622 9.17771 6.79483V6.79725C9.17771 6.85137 9.18336 6.90468 9.19386 6.95557L9.18255 6.95719C9.19871 7.12924 9.20759 7.30291 9.20759 7.479C9.20759 7.67286 9.19709 7.8643 9.17771 8.0525H6.74313V8.05573C6.32876 8.08239 6 8.42649 6 8.84814V8.85057C6 9.28998 6.33683 9.64216 6.77544 9.64216C6.80937 9.64216 6.8441 9.64378 6.89903 9.64297L8.7601 9.63893C8.28837 10.7294 7.47011 11.6341 6.44507 12.2149C6.44023 12.2173 6.43619 12.2197 6.43134 12.2229C6.42003 12.2294 6.40953 12.2359 6.39822 12.2423L6.39903 12.2431C6.17528 12.3837 6.02585 12.6325 6.02585 12.916V12.9184C6.02585 13.3578 6.38207 13.7132 6.82068 13.7132C6.99111 13.7132 7.14782 13.6591 7.27706 13.5687C8.7706 12.706 9.9168 11.3094 10.4556 9.64055H13.0105C13.0517 9.64136 13.1131 9.63893 13.1131 9.63893C13.5905 9.62924 13.9039 9.2916 13.9039 8.85299V8.85057C13.9047 8.42003 13.5638 8.07108 13.1381 8.05573Z" fill="currentColor"></path><path d="M13.7731 12.5388C13.7715 12.5356 13.7691 12.5331 13.7674 12.5307C13.74 12.4814 13.7077 12.4362 13.6713 12.3942C13.1584 11.6672 12.513 11.0412 11.7674 10.5541L11.7666 10.555C11.6366 10.4613 11.4766 10.4055 11.3046 10.4055C10.8652 10.4055 10.5098 10.7617 10.5098 11.2003V11.2028C10.5098 11.5033 10.677 11.765 10.9233 11.8999C11.5615 12.3215 12.0825 12.8045 12.4944 13.4499L12.5372 13.5041C12.6786 13.6333 12.866 13.7133 13.0728 13.7133C13.5122 13.7133 13.8676 13.3571 13.8676 12.9184V12.916C13.8668 12.7795 13.8329 12.6511 13.7731 12.5388Z" fill="currentColor"></path></svg>
            <view class="left-member">
              <view class="discounts">
                大会员双12特惠
              </view>
              <view class="annual-card">
                年卡108元，仅需0.3元/天
                <text class='iconfont icon-xiangyou' />
              </view>
            </view>
          </view>
          <view class="open-channels">
            立即开通
            <text class='ml20 iconfont icon-xiangyou' />
          </view>

        </view>
      </view>
		</view>
    <view class="member-formal-content">
      <view class="category">
        <view class="category-item">
          <view class="item">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg" class="download-client-trigger__icon"><path d="M7.23181 8.65895V1.75796C7.23181 1.33935 7.57582 1 8.00018 1C8.42453 1 8.76854 1.33935 8.76854 1.75796V8.67097L9.98589 7.47009C10.286 7.17409 10.7725 7.17409 11.0725 7.47009C11.3726 7.7661 11.3726 8.24601 11.0725 8.54201L8.54958 11.0308C8.24952 11.3268 7.76302 11.3268 7.46295 11.0308L4.94002 8.54201C4.63995 8.24601 4.63995 7.7661 4.94002 7.47009C5.24008 7.17409 5.72658 7.17409 6.02665 7.47009L7.23181 8.65895Z" fill="currentColor"></path><path d="M3.48023 4.29936C2.40686 4.29936 1.53672 5.15772 1.53672 6.21656V11.5669C1.53672 12.6257 2.40686 13.4841 3.48023 13.4841H12.5198C13.5931 13.4841 14.4633 12.6257 14.4633 11.5669V6.21656C14.4633 5.15772 13.5931 4.29936 12.5198 4.29936H11.6158C11.1915 4.29936 10.8475 3.96001 10.8475 3.5414C10.8475 3.12279 11.1915 2.78344 11.6158 2.78344H12.5198C14.4418 2.78344 16 4.3205 16 6.21656V11.5669C16 13.4629 14.4418 15 12.5198 15H3.48023C1.55815 15 0 13.4629 0 11.5669V6.21656C0 4.3205 1.55815 2.78344 3.48023 2.78344H4.38418C4.80853 2.78344 5.15254 3.12279 5.15254 3.5414C5.15254 3.96001 4.80853 4.29936 4.38418 4.29936H3.48023Z" fill="currentColor"></path></svg>
            <view class="title">
              离线缓存
            </view>
          </view>
          <view class="item">
            <svg width="20" height="21" viewBox="0 0 20 21" fill="none" xmlns="http://www.w3.org/2000/svg" class="right-entry-icon"><path fill-rule="evenodd" clip-rule="evenodd" d="M10 1.74286C5.02955 1.74286 1 5.7724 1 10.7429C1 15.7133 5.02955 19.7429 10 19.7429C14.9705 19.7429 19 15.7133 19 10.7429C19 5.7724 14.9705 1.74286 10 1.74286ZM10.0006 3.379C14.0612 3.379 17.3642 6.68282 17.3642 10.7426C17.3642 14.8033 14.0612 18.1063 10.0006 18.1063C5.93996 18.1063 2.63696 14.8033 2.63696 10.7426C2.63696 6.68282 5.93996 3.379 10.0006 3.379Z" fill="currentColor"></path><path d="M9.99985 6.6521V10.743" stroke="currentColor" stroke-width="1.7" stroke-linecap="round"></path><path d="M12.4545 10.7427H10" stroke="currentColor" stroke-width="1.7" stroke-linecap="round"></path></svg>
            <view class="title">
              历史记录
            </view>
          </view>
          <view class="item">
            <svg width="20" height="21" viewBox="0 0 20 21" fill="none" xmlns="http://www.w3.org/2000/svg" class="right-entry-icon"><path fill-rule="evenodd" clip-rule="evenodd" d="M11.0505 3.16759L12.7915 6.69573C12.954 7.02647 13.2702 7.25612 13.6349 7.30949L17.5294 7.87474C18.448 8.00817 18.8159 9.13785 18.1504 9.78639L15.3331 12.5334C15.0686 12.7905 14.9481 13.1609 15.0104 13.5256L15.6759 17.4031C15.8328 18.3184 14.8721 19.0171 14.0497 18.5845L10.5661 16.7537C10.2402 16.5823 9.85042 16.5823 9.52373 16.7537L6.04087 18.5845C5.21848 19.0171 4.2578 18.3184 4.41468 17.4031L5.07939 13.5256C5.14166 13.1609 5.02198 12.7905 4.75755 12.5334L1.9394 9.78639C1.27469 9.13785 1.64182 8.00817 2.56126 7.87474L6.4549 7.30949C6.82041 7.25612 7.13578 7.02647 7.29832 6.69573L9.04015 3.16759C9.45095 2.33468 10.6389 2.33468 11.0505 3.16759Z" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"></path><path d="M11.603 11.8739C11.413 12.5556 10.7871 13.0554 10.0447 13.0554C9.29592 13.0554 8.66679 12.5467 8.48242 11.8569" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"></path></svg>
            <view class="title">
              我的收藏
            </view>
          </view>
          <view class="item">
            <svg width="20" height="21" viewBox="0 0 20 21" fill="none" xmlns="http://www.w3.org/2000/svg" class="right-entry-icon"><path fill-rule="evenodd" clip-rule="evenodd" d="M10 1.74286C5.02955 1.74286 1 5.7724 1 10.7429C1 15.7133 5.02955 19.7429 10 19.7429C14.9705 19.7429 19 15.7133 19 10.7429C19 5.7724 14.9705 1.74286 10 1.74286ZM10.0006 3.379C14.0612 3.379 17.3642 6.68282 17.3642 10.7426C17.3642 14.8033 14.0612 18.1063 10.0006 18.1063C5.93996 18.1063 2.63696 14.8033 2.63696 10.7426C2.63696 6.68282 5.93996 3.379 10.0006 3.379Z" fill="currentColor"></path><path d="M9.99985 6.6521V10.743" stroke="currentColor" stroke-width="1.7" stroke-linecap="round"></path><path d="M12.4545 10.7427H10" stroke="currentColor" stroke-width="1.7" stroke-linecap="round"></path></svg>
            <view class="title">
              稍后再看
            </view>
          </view>
        </view>
      </view>
      <view class="creative-center">
        <view class="title">创作中心</view>
      </view>
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
.app-container {
  padding: 60rpx 20rpx 20rpx 15rpx;
  .user-header-info {
    /* 定位元素位置 */
    position: fixed;
    top: 60rpx;
    right: 20rpx;
    bottom: 20rpx;
    left: 15rpx;
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
        text {
          color: rgb(149, 149, 149);
        }
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
      margin-top: 30rpx;
      background: rgb(255, 236, 240);
      padding: 25rpx 25rpx;
      border-radius: 20rpx;
      .membership-interest {
        display: flex;
        justify-content: space-between;
        align-items: center;
        line-height: 30rpx;
        .membership-interest-flex {
          display: flex;
          .left-member {
            /* 通用颜色 */
            color: rgb(255, 102, 153);
            margin-left: 15rpx;
            .discounts {
              font-size: 26rpx;
              margin-bottom: 6rpx;
            }
            .annual-card {
              font-size: 22rpx;
              text {
                margin-left: 5rpx;
                font-size: 23rpx
              }
            }
          }
        }
        .open-channels {
          font-size: 24rpx;
          color: rgb(255, 102, 153);
          line-height: 30rpx;
          text {
            font-size: 25rpx
          }
        }
      }
    }
  }
  .member-formal-content {
    /* 定位元素位置 */
    margin-top: 400rpx;
    .category {
      .category-item {
        padding: 0 25rpx;
        display: flex;
        justify-content: space-between;
        .item {
          display: flex;
          flex-direction: column;
          align-items: center;
        }
        .title {
          margin-top: 10rpx;
          font-size: 22rpx;
        }
      }
    }
    .creative-center {
      margin-top: 10rpx;
      font-weight: bold;
      font-size: 24rpx;
    }
  }
}

.vertical {
  border-left: 1px solid black;
  margin: 10rpx;
}
</style>
