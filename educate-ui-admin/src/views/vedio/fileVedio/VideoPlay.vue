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
  resetForm()
}
// 提供 open 方法，用于打开弹窗
defineExpose({ open })


const dplayerObj = reactive({
  video: {
    // 视频地址
    url: 'https://api.dogecloud.com/player/get.m3u8?vcode=5ac682e6f8231991&userId=17&ext=.m3u8',
    type: 'customHls',
    // 封面地址
    pic: "http://127.0.0.1:9011/server/admin-api/infra/file/4/get/Snipaste_2023-04-24_14-35-14(1).png",
    // 自定义类型
    customType: {
      customHls: function (video, player) {
        const hls = new Hls(); //实例化Hls  用于解析m3u8
        hls.loadSource(video.src);
        hls.attachMedia(video);
      }
    }
  },
  danmaku: {
    id: '9E2E3368B56CDBB4',
    api: 'https://api.prprpr.me/dplayer/',
    token: 'tokendemo',
    maximum: 1000,
    addition: ['https://api.prprpr.me/dplayer/v3/bilibili?aid=4157142'],
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
