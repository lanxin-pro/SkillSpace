<template>
  <div class="app-container">
    <VueVideo
        ref="aplayVideo"
        v-bind="options"
        @play="onPlay"
    >
    </VueVideo>

    <VueDanmaku
        :autoplay="false"
        ref="danmakuRef"
        v-model:danmus="danmus"
        style="height:100px; width:300px;"
    />

  </div>
</template>

<script setup>
import { ref,reactive } from 'vue'
import VueVideo from "vue3-video-play"
import 'vue3-video-play/dist/style.css'
import VueDanmaku from "vue3-danmaku"


const danmus = ref(['danmu1', 'danmu2', 'danmu3'])

const danmakuRef = ref()
//事件示例
const onPlay = (ev) => {
  danmakuRef.value.play()
}
const options = reactive({
  // type: "auto", // 势必报错 视频类型
  width: '70%', // 播放器高度
  height: '70%', // 播放器高度
  color: "#409eff", // 主题色
  title: "测试", // 视频名称
  webFullScreen:false,// 网页全屏
  speed:true,// 是否支持快进快退
  currentTime:0,// 跳转到固定播放时间(s)
  muted: false,// 静音
  volume: 0.4,// 默认音量0-1
  autoPlay: false, // 注意：这里如果自动播放就会没有声音 自动播放
  loop:false,// 循环播放
  mirror:false,// 镜像画面
  control: true, // 是否显示控制器
  ligthOff:false,// 关灯模式
  src: 'http://localhost:9011/server/admin-api/infra/file/5/get/20230824-190210.mp4', //视频源
  poster: 'http://127.0.0.1:9011/server/admin-api/infra/file/4/get/Kaguya_S2_1400_2.jpg', //封面
  speedRate: [1.0,1.25,1.5,2.0], // 可选的播放速度
  controlBtns: [
    "audioTrack",// 音轨切换按钮
    "quality",// 视频质量切换按钮
    "speedRate",// 速率切换按钮
    "volume",// 音量
    "setting",// 设置
    "pip",// 画中画按钮
    "pageFullScreen",// 网页全屏按钮
    "fullScreen",// 全屏按钮
  ], // 显示所有按钮,
})

</script>

<style scoped>

</style>
