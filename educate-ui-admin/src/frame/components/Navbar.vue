<template>
  <div class="navbar">


    <Hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container"
               @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav"/>
    <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav"/>




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
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              <el-dropdown-item @click.native="setting = true">
                <span>布局设置</span>
              </el-dropdown-item>
              <el-dropdown-item divided @click.native="logout">
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-tooltip>

</div>
  </div>
</template>

<script setup>
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
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'

const { wsCache } = useCache()
const wsCacheUser = wsCache.get(CACHE_KEY.USER)

const { appContext } = getCurrentInstance()

const sidebar = computed(()=>{
  return store.getters['app/getSidebar']
})
const device = computed(()=>{
  return store.getters['app/getDevice']
})
const avatar = computed(()=>{
  return wsCacheUser.user.avatar
})
const nickname = computed(()=>{
  return wsCacheUser.user.nickname
})



const logout = ()=>{
  console.log()
   appContext.config.globalProperties.$modal.confirm('确定注销并退出系统吗？', '提示').then(() => {
    store.dispatch('LogOut').then(() => {
      location.href = getPath('/index')
    })
  }).catch(() => {});
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
