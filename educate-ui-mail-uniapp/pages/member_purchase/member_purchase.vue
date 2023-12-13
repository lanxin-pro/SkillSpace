<template>
	<view class="continer">
		<view class="header-top">
			<view class="header-top-left">
				<view class="title">
					<text class="animated fadeIn">会员购</text>
				</view>
				<view class="uni-margin-wrap">
					<swiper class="swiper" vertical="true" autoplay="true" disable-touch="true">
						<swiper-item>
							<view class="header-swiper-item">内部群现时开放 > </view>
						</swiper-item>
						<swiper-item>
							<view class="header-swiper-item">内部群现时开放 > </view>
						</swiper-item>
						<swiper-item>
							<view class="header-swiper-item-guan">官方直营 ~ 正品直售</view>
						</swiper-item>
					</swiper>
				</view>
			</view>
			<view class="header-top-right">
        <navigator url="/pages/member_purchases/shopping-trolley/index" class="display-inline" style="color: rgb(95, 98, 105);">
          <text class='iconfont icon-gouwuche6 icon-top-right mr20' />
        </navigator>
        <navigator url="/pages/member_purchases/user/index" class="display-inline" style="color: rgb(95, 98, 105);">
          <text class='iconfont icon-yonghu3 icon-top-right' />
        </navigator>
			</view>
		</view>

		<view class="search-wrap flex">
			<view class="signIn">
				<image src="../../static/images/sign-in/1a02a0dbcfd5440db747adde568e5ca57e6c1a8f.gif" />
			</view>
			<view class="serch-wrapper flex">

				<navigator url="/pages/goods_search/index" class="input" hover-class="none">
					<text class="iconfont icon-xiazai5" /> 搜索商品 ！！
				</navigator>
			</view>
			<view class="category ml10">
        <text class="category-alphabet">ALL</text>
        <text class="category-title">分类</text>
      </view>
		</view>

		<view class="category">
			<view class="category-swiper">
				<view class="category-swiper-item">
					<view class="quarantine flex">
						<view class="category-item-picture-text">
              <view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-001.png" />
              </view>
              <view class="category-name">
                <text>手办雕像</text>
              </view>
						</view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-002.png" />
              </view>
              <view class="category-name">
                <text>盲盒</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-003.png" />
              </view>
              <view class="category-name">
                <text>漫展</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-004.png" />
              </view>
              <view class="category-name">
                <text>新人福利</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-005.png" />
              </view>
              <view class="category-name">
                <text>景品</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-006.png" />
              </view>
              <view class="category-name">
                <text>谷子</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-007.png" />
              </view>
              <view class="category-name">
                <text>Q版手办</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-008.png" />
              </view>
              <view class="category-name">
                <text>漫画</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-009.png" />
              </view>
              <view class="category-name">
                <text>画集</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-010.png" />
              </view>
              <view class="category-name">
                <text>毛绒玩偶</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-011.png" />
              </view>
              <view class="category-name">
                <text>品牌店</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-012.png" />
              </view>
              <view class="category-name">
                <text>轻小说</text>
						  </view>
            </view>
						<view class="category-item-picture-text">
							<view class="category-image">
                <image class="uploade-img" src="../../static/images/category-swiper/1-013.png" />
              </view>
              <view class="category-name">
                <text>个性装扮</text>
						  </view>
            </view>
					</view>
				</view>
			</view>
		</view>

		<view class="hot-category">
			<view class="background-category">
        <view class="background">
          <view class="left-title">原神</view>
          <view class="right-picture">
            <view class="cover-image">
              <image src="../../static/images/hot-prcture/rollRight-001.png" />
            </view>
          </view>
          <image src="../../static/images/hot-category/rollBottom.png" />
        </view>
        <view class="background">
          <view class="left-title">崩坏系列</view>
          <view class="right-picture">
            <view class="cover-image">
              <image src="../../static/images/hot-prcture/rollRight-002.png" />
            </view>
          </view>
          <image src="../../static/images/hot-category/rollBottom.png" />
        </view>
        <view class="background">
          <view class="left-title">更衣人坠入</view>
          <view class="right-picture">
            <view class="cover-image">
              <image src="../../static/images/hot-prcture/rollRight-003.png" />
            </view>
          </view>
          <image src="../../static/images/hot-category/rollBottom.png" />
        </view>
			</view>
		</view>
		<view class="content">

      <view class="content-left">
        <view class="carousel-advertising">
          <swiper
              class="swiper"
              circular
              indicator-dots="true"
              autoplay="true"
              interval="2000"
              duration="500"
              indicator-color="#FFFFFF"
              indicator-active-color="rgb(254, 103, 154)"
          >
            <swiper-item>
              <view class="swiper-item uni-bg-red">
                <image src="../../static/images/carousel/vane1b6310f7edad4a65b9ed485e85838af0.png"></image>
              </view>
            </swiper-item>
            <swiper-item>
              <view class="swiper-item uni-bg-green">
                <image src="../../static/images/carousel/vane2071bcfe706f4ea18d5de8fb9bd7bb77.png"></image>
              </view>
            </swiper-item>
            <swiper-item>
              <view class="swiper-item uni-bg-green">
                <image src="../../static/images/carousel/vane7e151cf86ad34629a59d5aba97b1fbae.png"></image>
              </view>
            </swiper-item>
            <swiper-item>
              <view class="swiper-item uni-bg-blue">
                <image src="../../static/images/carousel/vane19b50b1dc9b647c8aafcfe0a985d094b.jpg"></image>
              </view>
            </swiper-item>
            <swiper-item>
              <view class="swiper-item uni-bg-blue">
                <image src="../../static/images/carousel/vane933a2a38c83645cb8565b635ff3cc847.png"></image>
              </view>
            </swiper-item>
            <swiper-item>
              <view class="swiper-item uni-bg-blue">
                <image src="../../static/images/carousel/vane2071bcfe706f4ea18d5de8fb9bd7bb77.png"></image>
              </view>
            </swiper-item>
          </swiper>
        </view>
        <!-- 使用 v-for 进行列表渲染，:key 提供唯一键 -->
        <block v-for="(item, index) in list" :key="item.id">
          <!-- 使用 v-if 进行条件渲染 -->
          <view v-if="index % 2 === 0" class="content-item">
            <!--     这个属性可能是用来指定图片的缩放或适应模式。
            在这个上下文中，widthFix 可能意味着图片的宽度会被固定或调整以适应其容器，
            而高度可能会相应地改变以保持图片的比例。不过，这取决于你使用的具体框架或库，
            因为 mode 并非标准的HTML属性。       -->
            <image :src="item.url" class="content-item-img" mode="widthFix" />
            <view class="content-item-box">
              <view class="content-item-title">{{ item.title }}</view>
              <view class="content-item-ranking-list">
                <view class="text ranking-text" v-if="item.ranking">
                  原创排行榜No.{{ item.ranking }}
                </view>
                <view class="text first-week-only-couples" v-if="item.onlyCouples">
                  新人首周专享
                </view>
              </view>
              <view class="content-item-price">
                <view class="price-row">
                  <view class="roll-text text-color">新人劵后</view>
                  <view class="text-color-price">￥</view>
                  <view class="now-price text-color-price">{{ item.price }}</view>

                  <view class="original-price">
                    <text style="margin-right: -4rpx">￥</text>
                    426
                  </view>
                </view>
                <view class="collect">
                  <view class='iconfont icon-shoucang' />
                  <view class="collect-number">121</view>
                </view>
              </view>
            </view>
          </view>
        </block>
      </view>
      <view class="content-right">
        <block v-for="(item, index) in list" :key="item.id">
          <view v-if="index % 2 === 1" class="content-item">
            <!-- 同上，复制左侧的内容 -->
            <!-- 数据绑定使用 v-bind 或简写 : -->
            <image :src="item.url" class="content-item-img" mode="widthFix" />
            <view class="content-item-box">
              <view class="content-item-title">{{ item.title }}</view>
              <view class="content-item-ranking-list">
                <view class="text ranking-text" v-if="item.ranking">
                  原创排行榜No.{{ item.ranking }}
                </view>
                <view class="text first-week-only-couples" v-if="item.onlyCouples">
                  新人首周专享
                </view>
              </view>
              <view class="content-item-price">
                <view class="price-row">
                  <view class="roll-text text-color">新人劵后</view>
                  <view class="text-color-price">￥</view>
                  <view class="now-price text-color-price">{{ item.price }}</view>

                  <view class="original-price">
                    <text style="margin-right: -4rpx">￥</text>
                    300
                  </view>
                </view>
                <view class="collect">
                  <view class='iconfont icon-shoucang' />
                  <view class="collect-number">121</view>
                </view>
              </view>
            </view>
          </view>
        </block>
      </view>


		</view>
    <view class='loadingicon acea-row row-center-wrapper' v-if="goodScroll">
      <!--   加载的时候显示   -->
      <text class='loading iconfont icon-jiazai' :hidden='!loading' />
    </view>
    <view class="mores-txt" v-if="!goodScroll">
      <text>我是有底线的</text>
    </view>
	</view>
</template>

<script>
import { silenceBindingSpread } from '@/utils/index.js'
import { mapGetters } from "vuex"

	export default {
      computed: mapGetters(['isLogin']),
	    data() {
	        return {
            // 会员购页面的 title
            site_name: '会员购 ~',
            // 商品
            goodScroll: true,
            loading: false,
            list: [
              {
                id: 1,
                price: 389,
                num: '10',
                title: '虞美人有大长腿',
                url: 'https://img1.baidu.com/it/u=610726533,1686239110&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=750',
                avatar: 'https://img1.baidu.com/it/u=610726533,1686239110&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=750',
                ranking: '3',
              },
              {
                id: 2,
                name: '苏苏6',
                num: '8',
                title: '苏苏哇哈哈-苏苏的bug-苏苏小苏苏',
                url: 'https://img0.baidu.com/it/u=1555018837,2431012166&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500',
                avatar: 'https://img0.baidu.com/it/u=1555018837,2431012166&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500',
                ranking: '1',
                onlyCouples: '1',
                advertising: '1'
              },
              {
                id: 3,
                name: '苏苏2',
                num: '12',
                title: '你好旧时光',
                url: 'https://img2.baidu.com/it/u=2656232546,4221523240&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=777',
                avatar: 'https://img2.baidu.com/it/u=2656232546,4221523240&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=777'
              },
              {
                id: 4,
                name: '苏苏3',
                num: '99+',
                title: '黑暗荣耀',
                url: 'https://img1.baidu.com/it/u=2602506082,2229028687&fm=253&fmt=auto&app=138&f=JPEG?w=796&h=500',
                avatar: 'https://img1.baidu.com/it/u=2602506082,2229028687&fm=253&fmt=auto&app=138&f=JPEG?w=796&h=500'
              },
              {
                id: 5,
                name: '苏苏4',
                num: '66',
                title: '归路',
                url: 'https://img1.baidu.com/it/u=3462460603,3220043498&fm=253&fmt=auto&app=120&f=JPEG?w=690&h=458',
                avatar: 'https://img1.baidu.com/it/u=3462460603,3220043498&fm=253&fmt=auto&app=120&f=JPEG?w=690&h=458'
              },
              {
                id: 6,
                name: '苏苏5',
                num: '8',
                title: '花琉璃',
                url: 'https://img0.baidu.com/it/u=3184989322,3773788620&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=667',
                avatar: 'https://img0.baidu.com/it/u=3184989322,3773788620&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=667'
              },
            ]
	        }
	    },
      onLoad() {
        let that = this
        uni.getLocation({
          type: 'wgs84',
          altitude: true,
          geocode: true,
          // gcj02 需要权限 App 和 H5 需配置定位 SDK 信息才可支持 gcj02
          // https://blog.csdn.net/m0_65878076/article/details/129474617
          // type: 'gcj02',
          success: function(res) {
              // 地理数据的缓存
              // https://uniapp.dcloud.net.cn/api/storage/storage.html#setstorage
              uni.setStorageSync('user_latitude', res.latitude)
              uni.setStorageSync('user_longitude', res.longitude)
          }
        })
        // 是登录状态 才会走 静默推广
        this.isLogin && silenceBindingSpread()
        this.getIndexConfig()
      },
      onShow() {
        uni.setNavigationBarTitle({
          title: this.site_name
        })
      },
      onReachBottom : function() {
        this.loading = true
      },
      methods: {
        getIndexConfig: function () {
          // TODO j-sentinel 页面店铺装修？
        }
	    }
	}

</script>

<style lang="scss">
.continer {
	padding: 20rpx 15rpx;
  background: linear-gradient(rgb(255, 255, 255),rgb(241, 242, 244),rgb(241, 242, 244),rgb(241, 242, 244));

  .mores-txt {
    width: 100%;
    align-items: center;
    justify-content: center;
    height: 40rpx;
    color: #999;
    font-size: 24rpx;
    display: flex;
  }
}
.header-top {
	display: flex;
	justify-content: space-between;
	line-height: 30rpx;
	.header-top-left {
		width: 100%;
		display: flex;
		line-height: 37rpx;
		.title {
			font-size: 30rpx;
			font-weight: bold;
			width: 130rpx;
		}
		.uni-margin-wrap {
			width: 100%;
			.swiper {
				height: 37rpx;
			}
			.header-swiper-item {
				font-size: 24rpx;
				color: rgb(255, 91, 163);
			}
			.header-swiper-item-guan {
				font-size: 24rpx;
				color: rgb(153, 153, 153);
			}

		}
	}
	.icon-top-right {
		font-size: 40rpx;
	}
}
.search-wrap {
	margin-top: 30rpx;
	align-items: center;
	.signIn {
		margin-right: 15rpx;
		image {
			width: 90rpx;
			height: 55rpx;
		}
	}
  .category {
    width: 80rpx;
    color: rgb(100, 99, 105);
    line-height: 25rpx;
    text-align: center;
    font-size: 24rpx;
    font-weight: 600;
    font-family: 华文仿宋;
    .category-title {
      display: inline-block;
    }
  }
	.input {
		display: flex;
		align-items: center;
		width: 526rpx;
		height: 54rpx;
		line-height: 54rpx;
		padding: 0 0 0 20rpx;
		background: rgb(255, 255, 255);
		border: 1rpx solid rgb(255, 91, 163);
		border-radius: 29rpx;
		color: #000000;
		font-size: 24rpx;
		font-weight: 600;
		.iconfont {
			margin-right: 20rpx;
			font-size: 26rpx;
			font-weight: 700;
			color: rgb(255, 91, 163);
		}
	}
}
.category-swiper {
	margin-left: 5rpx;
	.category-swiper-item {
		margin-top: 20rpx;
		// 目前这个滚动的宽度变量是写死的
		white-space: nowrap;
		overflow: scroll;
		.quarantine {
			.category-item-picture-text {
				display: flex;
        flex-direction: column;
        align-items: center;
        padding: 0 18rpx;
        .category-image {
          width: 75rpx;
          height: 75rpx;
          .uploade-img {
            width: 75rpx;
            height: 75rpx;
            display: inline-block;
            margin-right: 45rpx;
          }
          .category-name {

          }
        }
				text {
					font-size: 24rpx;
					width: 75rpx;
					text-align: center;
					font-family: 黑体;
				}
			}
      .category-item-picture-text:first-child {
        margin-left: -10rpx;
      }
		}
	}
}
/* 隐藏滚动条样式 */
::-webkit-scrollbar {
	display: none;
}

.hot-category {
  margin-top: 30rpx;
	.background-category {
    display: flex;
    justify-content: space-between;
    .background {
	    position: relative;
      .left-title {
        position: absolute;
        z-index: 10;
        left: 10rpx;
        top: 5rpx;
        font-size: 24rpx;
        font-weight: 600;
      }
      .right-picture {
        position: absolute;
        z-index: 10;
        top: -20rpx;
        right: 0;
        .cover-image {
          width: 145rpx;
          /* 这里会多出10rpx，是为了视觉效果更好 */
          height: 135rpx;
          overflow: hidden;
          image {
            width: 100%; /* Makes the image width equal to the container width */
            height: 100%; /* Makes the image height equal to the container height */
            object-fit: contain; /* This will make sure the image is scaled properly; use 'cover' to fill the height and width completely */
          }
        }
      }
      image {
        width: 235rpx;
        height: 105rpx;
      }
    }

	}
}

.content {
  margin-top: 20rpx;
  .carousel-advertising {
    height: 280rpx;
    width: 350rpx;
    margin-bottom: 20rpx;
    margin-left: 5rpx;
    .swiper {
      height: 280rpx;
    }
    .swiper-item {
      display: block;
      height: 280rpx;
      line-height: 280rpx;
      text-align: center;
      image {
        border-radius: 20rpx;
        max-width: 100%;
        max-height: 100%;
        object-fit: contain;
      }
    }
  }
}


.content {
  display: flex;
  justify-content: space-between;
  .content-left, .content-right {
    width: 49%;
    .content-item {
      margin: 0rpx auto 20rpx;
      background: #fff;
      width: 98%;
      border-radius: 20rpx;
      overflow: hidden;
      .content-item-img {
        width: 100%;
        position: relative;
      }
      .content-item-box {
        font-size: 24rpx;
        color: #333;
        padding: 5rpx 12rpx;
        box-sizing: border-box;
        .content-item-title {
          /* 一行省略 */
          /*text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 1;
          overflow: hidden;*/
          font-size: 24rpx;
          margin-left: 5rpx;
        }
        .content-item-ranking-list {
          margin-top: 10rpx;
          display: flex;
          gap: 10rpx;
          font-size: 21rpx;
          font-family: cursive;
          // 换行
          flex-wrap: wrap;
          .text{
            border-radius: 10rpx;
            padding: 5rpx 7rpx;
          }
          /* 一行省略 */
          .ranking-text {
            background: rgb(254, 244, 235);
            color: rgb(242, 131, 50);
          }
          .first-week-only-couples {
            background: rgb(254, 242, 244);
            color: rgb(236, 109, 151);
          }
        }
        .content-item-price {
          align-items: center;
          margin-top: 12rpx;
          padding-bottom: 12rpx;
          font-size: 22rpx;
          margin-left: 5rpx;

          display: flex;
          justify-content: space-between;
          .price-row {
            display: flex;
            height: 30rpx;
            line-height: 30rpx;
            .text-color {
              color: rgb(223, 118, 153);
            }
            .text-color-price {
              color: rgb(255, 103, 154);
            }
            .roll-text {
              font-size: 19rpx;
            }
            .now-price {
              font-size: 28rpx;
            }
            .original-price {
              margin-left: 6rpx;
              color: rgb(142, 146, 150);
              font-size: 19rpx;
            }
          }
          .collect {
            display: flex;
            justify-content: center;
            align-items: center;
            .icon-shoucang {
              font-size: 28rpx;
              color: rgb(178, 178, 178);
              margin-right: 5rpx;
            }
            .collect-number {
              color: rgb(148, 153, 159);
            }
          }
        }
      }
    }
  }
}
</style>
