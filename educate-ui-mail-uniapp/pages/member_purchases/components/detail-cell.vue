<template>
  <view class="content">
    <view class="list-wrap" @click="onClick">
      <view class="logo"></view>
      <view class="tag" v-for="(item, index) of this.list" :class="index === 0 ? 'none-tag-icon' : ''">
        {{ item.title }}
      </view>
      <view class="tag tag-bottom" v-if="nonsupport">
        不支持7天无理由退货
      </view>
    </view>

    <view class="right-icon">
      <text style="font-size: 26rpx" class="iconfont icon-xiangyou"></text>
    </view>

    <SuPopup
      :show="show"
      type="bottom"
      :showClose="true"
      round="5"
      @close="show = false"
    >
      <view class="attention-details">
        <view class="about-freight">
          <view class="title">关于运费</view>
          <text class="details display-inline-block">
            单笔订单中合并发货的商品实付总金额（含定金+尾款）满足包邮门槛即可包邮，定金预售商品的运费将在补款发货时产生。
            具体物流规则以商品页面展示为准。
          </text>

        </view>
        <view class="about-freight">
          <view class="title">不支持7天无理由</view>
          <text class="details">特殊商品及类目不支持7天无理由退货。如，手办模玩类均不支持7天无理由（手办为手工制品，难免会有瑕疵点，敬请谅解）</text>
        </view>

        <view class="about-freight">
          <view class="title">保价保单</view>
          <text class="details">保证补款时全款价格不会高于预售价格。预售商品在补款后保证发货（供应商不供货、取消制作除外，出现这类情况无条件返还定金）。</text>
        </view>

      </view>

    </SuPopup>

  </view>
</template>

<script>
import SuPopup from "@/components/ui/su-popup/su-popup.vue"

export default {
  data() {
    return {
      show: false,
      showClose: false
    }
  },
  components: {
    SuPopup
  },
  props: {
    list: {
      type: Array,
      default: function () {
        return [{title: '专业包装'},
          {title: '48h内发货'},
          {title: '延期发货'},
          {title: '低价保单'},
          {title: '满129包邮'},
        ]
      }
    },
    nonsupport: {
      type: Boolean,
      default: true,
    },
    value: {
      type: String,
      default: '',
    }
  },
  methods: {
    onClick() {
      this.show = true
    }
  }
}
</script>

<style lang="scss" scoped>
.content {
  display: flex;
  align-items: center;
  .list-wrap {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    .logo {
      width: 196rpx;
      height: 28rpx;
      background: #fff url(http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=5LiL6L29LnBuZw==&version_id=null) no-repeat 0;
      background-size: 196rpx auto;
      padding: 8rpx 40rpx 8rpx 0;
      position: relative;
      z-index: 2;
      margin-right: 46rpx;
    }
    .tag {
      margin-right: 34rpx;
      padding: 8rpx 0;
      font-size: 22rpx;
      color: #212121;
      line-height: 28rpx;
      position: relative;
    }
    .tag-bottom {
      margin-right: 34rpx;
      padding: 8rpx 0;
      font-size: 22rpx;
      color: #979797;
      line-height: 28rpx;
    }
    .tag-bottom:before {
      content: "";
      display: inline-block;
      width: 24rpx;
      height: 24rpx;
      background: url(http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=5LiL6L29ICgxKS5wbmc=&version_id=null) no-repeat 50%;
      background-size: 24rpx 24rpx;
      margin-right: 6rpx;
    }
    .tag:after {
      content: "";
      display: block;
      width: 4rpx;
      height: 4rpx;
      background: #d3d3d3;
      position: absolute;
      left: -20rpx;
      top: 50%;
      margin-top: -2rpx;
      z-index: 1;
      border-radius: 50%;
    }
    .none-tag-icon:after {
      display: none;
    }
  }
  .right-icon {
    color: #cbcbcb;
    font-weight: 600;
  }
}

.attention-details {
  padding: 0 10rpx;
  .about-freight {
    margin-bottom: 28rpx;
    .title {
      position: relative;
      font-weight: 600;
      font-size: 26rpx;
      margin-bottom: 7rpx;
      padding-left: 40rpx;
    }
    .title:before {
      position: absolute;
      left: 0;
      top: 3px;
      display: block;
      content: "";
      height: 12px;
      width: 12px;
      background: url(http://alanxin.cn:55555/api/v1/buckets/educate-mall/objects/download?preview=true&prefix=ZW5zdXJlLjgzMzViZWQuc3Zn&version_id=null) no-repeat;
      background-size: 12px 12px;
    }
    .details {
      font-size: 24rpx;
    }
  }
  .about-freight:last-child {
    margin-bottom: 0;
  }
}
</style>
