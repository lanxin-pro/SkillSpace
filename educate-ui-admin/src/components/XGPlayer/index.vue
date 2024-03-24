<template>
  <div class="contain">
    <div id="mse" class="xigua"></div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import Player from 'xgplayer'
import 'xgplayer/dist/index.min.css'

// 初始化加载西瓜视频播放器
const initPlayer = () => {
  new Player({
    id: 'mse',
    autoplay: true,
    volume: 0.3,
    url: 'https://outin-fef10c9cc4bc11eeacf600163e31af8a.oss-cn-shanghai.aliyuncs.com/sv/2aa07b01-18d7dca1fa4/2aa07b01-18d7dca1fa4.mp4?Expires=1711295307&OSSAccessKeyId=LTAIxSaOfEzCnBOj&Signature=kgWnQGjBBtS4OgqD87W3IJL%2By6M%3D',
    poster: "//lf3-static.bytednsdoc.com/obj/eden-cn/nupenuvpxnuvo/xgplayer_doc/poster.jpg",
    playsinline: true,
    thumbnail: {
      pic_num: 44,
      width: 160,
      height: 90,
      col: 10,
      row: 10,
      urls: ['//lf3-static.bytednsdoc.com/obj/eden-cn/nupenuvpxnuvo/xgplayer_doc/xgplayer-demo-thumbnail.jpg'],
    },
    danmu: {
      comments: [
        {
          duration: 1500,
          id: '1',
          start: 3000,
          txt: '长弹幕长弹幕长弹幕长弹幕长弹幕',
          style: { // 弹幕自定义样式
            color: '#ff9500',
            fontSize: '20px',
            border: 'solid 1px #ff9500',
            borderRadius: '50px',
            padding: '5px 11px',
            backgroundColor: 'rgba(255, 255, 255, 0.1)'
          }
        }
      ],
      area: {
        start: 0,
        end: 1
      }
    },
    mp4plugin: {
      maxBufferLength: 50,
      minBufferLength: 10,
    },
    // 键盘快捷键
    keyShortcut: true
  });
}
onMounted(() => {
  initPlayer()
})
</script>

<style scoped>

</style>
