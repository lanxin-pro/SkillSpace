<template>
  <!--
  class属性中，"h-[100%]"用于将该元素高度设置为100%；
  "relative"表示将该元素设置为相对定位；
  "<xl:bg-v-dark"是一个Windi CSS的类名，用于在extra large屏幕上将该元素背景色设置为暗色；
  "<sm:px-10px"是一个Windi CSS的类名，用于在small屏幕上将该元素水平内边距设置为10像素；
  "<xl:px-10px"是一个Windi CSS的类名，用于在extra large屏幕上将该元素水平内边距设置为10像素；
  "<md:px-10px"是一个Windi CSS的类名，用于在medium屏幕上将该元素水平内边距设置为10像素。
  flex-1 表示将该元素设置为 Flex 布局中的一个 Flex 项目，并将 flex-grow 属性设置为 1，让该元素随着其他项目的大小而自适应地延展；
  p-30px 表示为该元素添加 30 像素的内边距(padding)，使内容与边框之间有一段空隙；
  dark:bg-v-dark 表示在暗色主题下，将该元素的背景颜色设置为暗色，这里使用的是一个 Windi CSS 扩展属性；
   -->
  <div
      :class="prefixCls"
      class="h-[100%] relative <xl:bg-v-dark <sm:px-10px <xl:px-10px <md:px-10px">
    <div class="relative h-full flex mx-auto">
      <!--   左边界   -->
      <div :class="`${prefixCls}__left flex-1 bg-gray-500 bg-opacity-20 relative p-30px <xl:hidden`">
        <!-- 左上角的 logo + 系统标题 -->
        <div class="flex items-center relative text-white">
          <img alt="" class="w-48px h-48px mr-10px" src="@/assets/imgs/logo.png" />
          <span class="text-20px font-bold">{{ vue_title }}</span>
        </div>
        <!-- 左边的背景图 + 欢迎语 -->
        <div class="flex justify-center items-center h-[calc(100%-60px)]">
          <TransitionGroup
              appear
              enter-active-class="animate__animated animate__bounceInLeft"
              tag="div"
          >
            <img key="1" alt="" class="w-350px" src="@/assets/svgs/login-box-bg.svg" />
            <div key="2" class="text-3xl text-white">欢迎使用本系统</div>
            <div key="3" class="mt-5 font-normal text-white text-14px">
              开箱即用的中后台管理系统
            </div>
          </TransitionGroup>
        </div>
      </div>
      <!--   右边界   -->
      <div class="flex-1 p-30px <sm:p-10px dark:bg-v-dark relative">
        <!-- 右上角的主题、语言选择 -->
<!--        <div class="flex justify-between items-center text-white @2xl:justify-end @xl:justify-end">
          <div class="flex items-center">
            <img alt="" class="w-48px h-48px mr-10px" src="@/assets/imgs/logo.png" />
            <span class="text-20px font-bold">{{ vue_title }}</span>
          </div>
          <div class="flex justify-end items-center space-x-10px">

          </div>
        </div>-->
        <!-- 右边的登录界面 -->
          <div
              class="h-full flex items-center m-auto w-[100%] @2xl:max-w-500px @xl:max-w-500px @md:max-w-500px @lg:max-w-500px"
          >

            <Transition appear enter-active-class="animate__animated animate__bounceInRight">
              <!-- 账号登录 -->
              <LoginForm class="p-20px h-auto m-auto <xl:(rounded-3xl light:bg-white)" />
            </Transition>

            <!-- 手机登录 -->
            <Transition appear enter-active-class="animate__animated animate__bounceInRight">
              <MobileForm class="p-20px h-auto m-auto <xl:(rounded-3xl light:bg-white)" />
            </Transition>
          </div>
      </div>
    </div>

  </div>

</template>

<script setup>
import { LoginForm, MobileForm } from './components'
const prefixCls = "v-login"
const vue_title = import.meta.env.VITE_APP_TITLE

</script>

<style scoped lang="scss">

.v-login {
  &__left {
    &::before {
      position: absolute;
      top: 0;
      left: 0;
      z-index: -1;
      width: 100%;
      height: 100%;
      // 左边黑色的svg背景图
      background-image: url('@/assets/svgs/login-bg.svg');
      background-position: center;
      background-repeat: no-repeat;
      content: '';
    }
  }
}


</style>
