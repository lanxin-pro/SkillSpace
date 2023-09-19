<template>
  <Dialog v-model="dialogVisible" title="文件预览" width="800">
      <Dplayer
          :video="dplayerObj.video"
          :danmaku="dplayerObj.danmaku"
          :contextmenu="dplayerObj.contextmenu"
          :highlight="dplayerObj.highlight"
      />
  </Dialog>

</template>

<script setup>
import { ref,reactive,unref } from 'vue'
import Dialog from '@/components/Dialog/index.vue'
import ELComponent from '@/plugins/modal.js'
import { getFilePage } from "@/api/infra/file/index.js"
import { getAccessToken } from '@/utils/auth'
import Hls from 'hls.js';
import Dplayer from '@/components/Dplayer/index.vue'



// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中
const formLoading = ref(false)
const url = import.meta.env.VITE_UPLOAD_URL
// 上传 Header 头
const uploadHeaders = ref()
// 文件列表
const fileList = ref([])
const data = ref({ path: '' })
const uploadRef = ref()

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })

const type = ref()

const dplayerObj = reactive({
  video: {
    // 视频地址
    url: 'http://127.0.0.1:9011/server/admin-api/infra/file/4/get/WeChat_20230919160349.mp4',
    // mp4不要走
    type: 'mp4',
    // 封面地址
    pic: "http://127.0.0.1:9011/server/admin-api/infra/file/4/get/Snipaste_2023-04-24_14-35-14(1).png",
    // 自定义类型 u3u8的处理方式
    customType: {
      customHls: function (video, player) {
        //实例化Hls  用于解析m3u8
        const hls = new Hls();
        hls.loadSource(video.src);
        hls.attachMedia(video);
      }
    }
  },
  danmaku: {
    id: '9E2E3368B56CDBB4',
    api: 'http://localhost:9011/server/admin-api/video/danmu/',
    token: 'tokendemo',
    maximum: 1000,
    addition: ['https://s-sh-17-dplayercdn.oss.dogecdn.com/1678963.json'],
    user: 'DIYgod',
    bottom: '15%',
    unlimited: true,
    speedRate: 0.5,
  },
  contextmenu: [
    {
      text: 'custom1',
      link: 'https://github.com/DIYgod/DPlayer',
    },
    {
      text: 'custom2',
      click: (player) => {
        console.log(player);
      },
    },
  ],
  highlight: [
    {
      time: 20,
      text: '这是第 20 秒',
    },
    {
      time: 120,
      text: '这是 2 分钟',
    },
  ],
})
</script>

<style scoped>

</style>
