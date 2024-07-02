<template>
  <view class="app-container">
    <view class="header-print-discussion">
      <view class="left-region">
        <view class="title">
          晒图/讨论
        </view>
        <view class="comment-number">
          93条
        </view>
      </view>
      <view class="right-region">
        <view class="title">
          发精评
        </view>
        <text class="iconfont icon-jiahao2" />
      </view>
    </view>
    <view class="comment-content">

      <view class="comment-new">
        <view class="comment-header">
          <view class="user-info">
            <img src="http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=5b6u5L+h5Zu+54mHXzIwMjMxMjIxMjM0OTI4LmpwZw==&version_id=null" class="avatar">
            <div class="user-name">j-sentinel</div>
            <div
                style="background-image: url('http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=bGV2ZWwtNS5zdmc=&version_id=null')"
                class="user-level">
            </div>
          </view>
          <view class="comment-info time">
            1月前
          </view>
        </view>
        <view class="comment-container">
          <view class="comment-content">
            猪猪可爱捏
          </view>
        </view>
      </view>

      <view class="comment-new">
        <view class="comment-header">
          <view class="user-info">
            <img src="http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=5b6u5L+h5Zu+54mHXzIwMjMxMjIxMjM0OTI4LmpwZw==&version_id=null" class="avatar">
            <div class="user-name">j-sentinel</div>
            <div
                style="background-image: url('http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=bGV2ZWwtNS5zdmc=&version_id=null')"
                class="user-level">
            </div>
          </view>
          <view class="comment-info time">
            1月前
          </view>
        </view>
        <view class="comment-container">
          <view class="comment-content">
            猪猪可爱捏
          </view>
        </view>
      </view>

      <view class="view-all-discussion">
        <view class="all-button">
          查看全部讨论
        </view>
      </view>

    </view>
  </view>
</template>

<script setup>

</script>

<style scoped lang="scss">
.app-container {
  padding: 20rpx;
  background: #ffffff;
  border-radius: 25rpx;
  .header-print-discussion {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20rpx;
    .left-region {
      display: flex;
      align-items: center;
      .title {
        font-weight: 700;
        font-size: 26rpx;
      }
      .comment-number {
        font-size: 22rpx;
        margin-left: 12rpx;
        color: #919191;
      }
    }
    .right-region {
      display: flex;
      align-items: center;
      .title {
        font-size: 22rpx;
        color: #d9a336;
        margin-right: 8rpx;
      }
      text {
        font-size: 18rpx;
      }
    }
  }
  .comment-content {
    display: flex;
    flex-direction: column;
    .comment-new {
      padding: 10rpx 6rpx;
      .comment-header {
        display: flex;
        justify-content: space-between;
        .user-info {
          display: flex;
          align-items: center;
          .avatar {
            height: 48rpx;
            width: 48rpx;
            border-radius: 200%;
          }
          .user-name {
            font-size: 24rpx;
            margin-left: 20rpx;
            line-height: 48rpx;
            white-space: nowrap;
            color: #999;
          }
          .user-level {
            width: 44rpx;
            height: 44rpx;
            margin-top: 2rpx;
            margin-left: 12rpx;
            background-position: 50%;
            background-size: 36rpx 18rpx;
            background-repeat: no-repeat;
            box-sizing: border-box;
          }
        }
        .comment-info {
          color: #929292;
          font-size: 22rpx;
        }
      }
      .comment-container {
        box-sizing: border-box;
        padding-left: 64rpx;
        margin-top: 20rpx;
        .comment-content {
          margin-top: 12rpx;
          font-size: 26rpx;
          color: #212121;
          line-height: 24rpx;
          word-break: break-all;
          word-wrap: break-word;
          white-space: pre-line;
        }
      }
    }
    .view-all-discussion {
      margin: 20rpx auto 0;
      .all-button {
        font-size: 26rpx;
        color: #606060;
        border-radius: 30rpx;
        border: 1rpx solid #e3e3e3;
        width: 200rpx;
        height: 50rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        font-family: math;
      }
    }
  }
}
</style>
