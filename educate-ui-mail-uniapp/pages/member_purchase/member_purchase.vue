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
				<text class='iconfont icon-gouwuche6 icon-top-right mr20' />
				<text class='iconfont icon-yonghu3 icon-top-right' />
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
			<view class="ml10">分类</view>
		</view>

		<view class="category">
			<view class="category-swiper">
				<view class="category-swiper-item">
					<view class="quarantine flex">
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-001.png" />
							<text>手办雕像</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-002.png" />
							<text>盲盒</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-003.png" />
							<text>漫展</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-004.png" />
							<text>新人福利</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-005.png" />
							<text>景品</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-006.png" />
							<text>谷子</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-007.png" />
							<text>Q版手办</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-008.png" />
							<text>漫画</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-009.png" />
							<text>画集</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-010.png" />
							<text>毛绒玩偶</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-011.png" />
							<text>品牌店</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-012.png" />
							<text>轻小说</text>
						</view>
						<view class="category-item-picture-text">
							<image class="uploade-img" src="../../static/images/category-swiper/1-013.png" />
							<text>个性装扮</text>
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
	</view>
</template>

<script>
	export default {
	    data() {
	        return {

	        }
	    },
	    methods: {

	    }
	}

</script>

<style lang="scss">
.continer {
	padding-top: 15px;
	margin: 0 5px;
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
			width: 83rpx;
			height: 50rpx;
		}
	}
	.input {
		display: flex;
		align-items: center;
		width: 526rpx;
		height: 54rpx;
		line-height: 54rpx;
		padding: 0 0 0 20rpx;
		background: rgba(247, 247, 247, 1);
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
				.uploade-img {
					width: 75rpx;
					height: 75rpx;
					display: inline-block;
					margin-right: 47rpx;
				}
				.uploade-img:last-child {
					background: red;
				}
				text {
					font-size: 22rpx;
					width: 75rpx;
					text-align: center;
					font-family: cursive;
				}
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
</style>
