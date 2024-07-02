<template>
  <div ref="videoRef"></div>
</template>

<script setup>
import DPlayer from 'dplayer'
import Hls from 'hls.js'
import { ref, reactive, onBeforeUnmount, onMounted } from 'vue'

const videoRef = ref()
const state = reactive({
  instance: null
})

const props = defineProps({
  // 是否自动播放
  autoplay: {
    type: Boolean,
    default: false
  },
  // 主题色
  theme: {
    type: String,
    default: '#0093ff'
  },
  // 视频是否循环播放
  loop: {
    type: Boolean,
    default: true
  },
  // 语言(可选值: 'en', 'zh-cn', 'zh-tw')
  lang: {
    type: String,
    default: 'zh-cn'
  },
  // 是否开启截图(如果开启，视频和视频封面需要允许跨域)
  screenshot: {
    type: Boolean,
    default: false
  },
  // 是否开启热键
  hotkey: {
    type: Boolean,
    default: true
  },
  // 视频是否预加载(可选值: 'none', 'metadata', 'auto')
  preload: {
    type: String,
    default: 'auto'
  },
  // 默认音量
  volume: {
    type: Number,
    default: 0.7
  },
  // 可选的播放速率，可以设置成自定义的数组
  playbackSpeed: {
    type: Array,
    default: [0.5, 0.75, 1, 1.25, 1.5, 2]
  },
  // 在左上角展示一个 logo，你可以通过 CSS 调整它的大小和位置
  logo: {
    type: String,
    default: 'http://127.0.0.1:9011/server/admin-api/infra/file/4/get/preview.gif'
  },
  // 视频信息
  video: {
    type: Object,
    default: {
      //视频地址
      url: 'http://localhost:9011/server/admin-api/infra/file/5/get/20230824-190210.mp4',
      type: 'customHls',
      pic: "http://127.0.0.1:9011/server/admin-api/infra/file/4/get/Snipaste_2023-04-24_14-35-14(1).png",
      customType: {
        customHls: function (video, player) {
          const hls = new Hls(); //实例化Hls  用于解析m3u8
          hls.loadSource(video.src);
          hls.attachMedia(video);
        }
      }
    },
  },
  // 外挂字幕
  subtitle: {
    type: Object,
    default: {}
  },
  // 显示弹幕
  danmaku: {
    type: Object,
    default: {}
  },
  // 自定义右键菜单
  contextmenu: {
    type: Array,
    default: []
  },
  // 自定义进度条提示点
  highlight: {
    type: Array,
    default: []
  },
  // 阻止多个播放器同时播放，当前播放器播放时暂停其他播放器
  mutex: {
    type: Boolean,
    default: true
  }
})
onMounted(() => {
  let player = {
    container: videoRef.value,
    autoplay: props.autoplay,
    theme: props.theme,
    loop: props.loop,
    lang: props.lang,
    screenshot: props.screenshot,
    hotkey: props.hotkey,
    preload: props.preload,
    volume: props.volume,
    playbackSpeed: props.playbackSpeed,
    logo: props.logo,
    video: props.video,
    contextmenu: props.contextmenu,
    highlight: props.highlight,
    mutex: props.mutex,
  }
  if (props.subtitle.url) {
    player.subtitle = props.subtitle
  }
  if (props.danmaku) {
    console.log("aaaaaaaaaaaa",props.danmaku)
    player.danmaku = props.danmaku
  }
  console.log("player",player);

  state.instance = new DPlayer(player)
  console.log("state.instance",state.instance)
})
// 销毁
onBeforeUnmount(() => {
  state.instance.destroy()
})


</script>

<style lang='scss'>
// TODO j-sentinel 并没有解决核心问题，依旧是animation-name不会动态显示，甚至没有
.dplayer-danmaku .dplayer-danmaku-right.dplayer-danmaku-move {
  will-change: transform;
  // TODO j-sentinel 2024/1/20 修改成danmaku貌似解决了问题，应该是版本的问题，播放器的作者进行了修复
  animation-name: danmaku;
  animation-timing-function: linear;
  animation-play-state: paused;
}
@keyframes slide-in-right {
  0% {
    transform: translateX(100%);
  }
  100% {
    transform: translateX(-100%);
  }
}
</style>
