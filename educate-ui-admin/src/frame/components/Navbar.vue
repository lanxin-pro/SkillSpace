<template>
  <div class="navbar">
    <el-row style="height: 100%">

      <el-col :span="9">
        <Hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container"
                 @toggleClick="toggleSideBar" />
        <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav"/>
        <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav"/>
      </el-col>

      <el-col :span="4">
        <div style="display: flex;justify-content: center;position: relative;">
          <div style="position: absolute;top: 10px;">
            <el-dropdown v-loading="timeLoading" >
              <el-tag
                  size="large"
                  class="time-tag"
                  type="info">
                当前服务器时间为：<span v-show="formatTime"> {{ gmt }} {{ formatterTime(formatTime) }}</span>
              </el-tag>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :disabled="true" v-for="(it, i) in timeList" :key="i">
                    {{ lang === 'zh' ? it.country + '时间：' : it.countryEn + ' time ' }}<span>{{ it.gmt }}</span>
                    <span style="margin-left: 5px"> {{ formatterTime(it.time) }}</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-col>

      <el-col :span="11">
        <div class="right-menu">
          <template v-if="device!=='mobile'">
            <el-tooltip content="快速搜索" effect="dark" placement="bottom">
              <Search id="header-search" class="right-menu-item" />
            </el-tooltip>
            <!-- 站内信 -->
            <el-tooltip content="站内信" effect="dark" placement="bottom">
              <NotifyMessage class="right-menu-item hover-effect" />
            </el-tooltip>

            <el-tooltip content="源码地址" effect="dark" placement="bottom">
              <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
            </el-tooltip>

            <el-tooltip content="文档地址" effect="dark" placement="bottom">
              <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
            </el-tooltip>

            <el-tooltip content="屏幕缩放" effect="dark" placement="bottom">
              <Screenfull id="screenfull" class="right-menu-item hover-effect" />
            </el-tooltip>

            <el-tooltip content="布局大小" effect="dark" placement="bottom">
              <SizeSelect id="size-select" class="right-menu-item hover-effect" />
            </el-tooltip>

          </template>

          <el-tooltip content="用户信息" effect="dark" placement="bottom">
            <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
              <div class="avatar-wrapper">
                <img :src="avatar" class="user-avatar">
                <span v-if="nickname" class="user-nickname">{{ nickname }}</span>
                <font-awesome-icon class="el-icon-caret-bottom"  icon="fa-solid fa-caret-down" />
              </div>
              <template #dropdown>

                <el-dropdown-menu slot="dropdown">
                  <router-link to="/user/profile">
                    <el-dropdown-item>
                      <div>
                        <font-awesome-icon icon="fa-solid fa-gear" style="color: #3772DA;" />
                        个人中心
                      </div>
                    </el-dropdown-item>
                  </router-link>
                  <el-dropdown-item @click.native="setting = true">
                    <span>
                      <div>
                        <font-awesome-icon icon="fa-brands fa-canadian-maple-leaf" style="color: #fab1ce" />
                        布局设置
                      </div>
                    </span>
                  </el-dropdown-item>
                  <el-dropdown-item divided @click.native="logout">
                    <span>
                      <div>
                        <font-awesome-icon icon="fa-regular fa-circle-xmark" style="color: #F64C2F" />
                        退出登录
                      </div>
                    </span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-tooltip>
        </div>
      </el-col>

    </el-row>
  </div>
</template>

<script setup>
import { ref,onMounted } from 'vue'
import Breadcrumb from '@/components/Breadcrumb/index.vue'
import TopNav from '@/components/TopNav/index.vue'
import Hamburger from '@/components/Hamburger/index.vue'
import Screenfull from '@/components/Screenfull/index.vue'
import SizeSelect from '@/components/SizeSelect/index.vue'
import Search from '@/components/HeaderSearch/index.vue'
import RuoYiGit from '@/components/RuoYi/Git/index.vue'
import RuoYiDoc from '@/components/RuoYi/Doc/index.vue'
import NotifyMessage from '@/frame/components/Message/index.vue'
import { getCurrentInstance,computed } from 'vue'
import { getPath } from '@/utils/ruoyi'
import store from '@/store'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/piniastore/modules/user.js'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'
import ElComponent from '@/plugins/modal.js'
import Dayjs from 'dayjs'
import { getCountryWithTimeZoneList } from '@/api/system/region.js'
import { useTimeStore } from '@/piniastore/modules/timeZone.js'
import { getServerTime } from '@/api/public.js'
import { handleZoneToTimeUtils } from '@/utils/formatTime.js'


const { wsCache } = useCache()
const { push, replace } = useRouter()
const userStore = useUserStore()
const wsCacheUser = wsCache.get(CACHE_KEY.USER)
// 时间
const timeList = ref([])
const formatTime = ref()
const gmt = ref()
const lang = ref('zh')
const serverTime = ref(null)
const timer = ref(null)
const delay = ref(1000)
const timeLoading = ref(true)

const { appContext } = getCurrentInstance()

const timeStore = useTimeStore()

onMounted(()=>{
  getServerTimeFn()
  getTime()
})
const getTime = async () => {
  const response = await getCountryWithTimeZoneList()
  timeStore.addZoneList(response.data)
  handleZoneToTime()
}
const getServerTimeFn = async ()=>{
  const response = await getServerTime()
  serverTime.value = response.data.time
  gmt.value = getUTC(new Date(serverTime.value))
}
const getUTC = (time) => {
  return `UTC +${0 - time.getTimezoneOffset() / 60}`
}
const handleZoneToTime = (cnTime)=>{
  timeLoading.value = true
  timeList.value = timeStore.getZoneList.map((it) => {
    return {
      time: new Date(handleZoneToTimeUtils(it.timeZone, cnTime, null)).getTime(),
      gmt: 'UTC ' + it.timeZone.slice(it.timeZone.indexOf('+')),
      country: it.country,
      countryEn: it.id === 1 ? 'China'
          : it.id === 2 ? 'India'
              : it.id === 3 ? 'Taiwan'
                  : it.id === 4 ? 'Thailand'
                      : it.id === 5 ? 'Vietnam'
                          : it.id === 6 ? 'Korea'
                              : it.id === 8 ? 'Malaysia' : 'UK'
    }
  })

  timer.value = setInterval(() => {
    if (timer.value === null){
      clearInterval(timer.value)
    }

    serverTime.value += delay.value
    formatTime.value = Dayjs(serverTime.value).format('YYYY-MM-DD HH:mm:ss')
    timeList.value.map((it) => {
      it.time += delay.value
      return {
        ...it
      }
    })
    timeLoading.value = false
  }, delay.value)
}
const sidebar = computed(()=>{
  return store.getters['app/getSidebar']
})
const device = computed(()=>{
  return store.getters['app/getDevice']
})
const avatar = computed(()=>{
  // 这个执行时机有点快
  // return wsCacheUser.user.avatar
  return wsCacheUser.data.user.avatar
})
const nickname = computed(()=>{
  // 这个执行时机有点快
  // return wsCacheUser.user.nickname
  return wsCacheUser.data.user.nickname
})
const formatterTime = (time)=> {
  return Dayjs(time).format('YYYY-MM-DD HH:mm:ss').replace(/\-/g, '.')
}


const logout = ()=>{
  ElComponent.confirm('确定注销并退出系统吗？', '提示').then(async() => {
    await userStore.loginOut()
    replace('/login?redirect=/index')
    ElComponent.msgSuccess("退出成功")
  }).catch(() => {})
}

const toggleSideBar = ()=>{
  store.dispatch('app/toggleSideBar')
}
</script>
<style>
.el-dropdown{
  line-height: inherit;
}
</style>
<style lang="scss" scoped>

.time-tag {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}


.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }


  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        display: flex;
        justify-content: center;
        align-items: center;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 35px;
          height: 35px;
          border-radius: 50%;
        }
        .user-nickname{
          margin-left: 5px;
          font-size: 14px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 18px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
