<template>
  <div
      class="sidebar-logo-container"
      :class="{'collapse': collapse}"
      :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">

    <transition name="sidebarLogoFade">

      <!--   collapse === !sidebar.value.opened 关闭的样式 -->
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/index">
        <!--   这里就会与windcss冲突     -->
        <img v-if="logo" :src="logo" class="sidebar-logo" style="display: inline-block" />
        <h1
            v-else
            class="sidebar-title"
            :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">
          {{ title }}
        </h1>
      </router-link>

<!--  展开的样式    -->
      <router-link v-else key="expand" class="sidebar-logo-link" to="/index">
<!--   这里就会与windcss冲突     -->
        <img v-if="logo" :src="logo" style="display: inline-block" class="sidebar-logo" />
        <h1
            class="sidebar-title"
            :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">

          {{ title }}
        </h1>
      </router-link>
    </transition>
  </div>
</template>

<script setup>
import logoImg from '@/assets/logo/logo3.png'
import { computed,ref } from 'vue'
import store from '@/store'

const viteApp = import.meta.env.VITE_APP_TITLE
const title = ref(viteApp)
const logo = logoImg
defineProps({
  collapse: {
    type: Boolean,
    required: true
  }
})
const sideTheme = computed(()=>{
  return store.getters['settings/getSideTheme']
})


</script>
<script>
import variables from '@/assets/styles/variables.module.scss'

export default {
  computed: {
    variables() {
      return variables;
    },
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      width: 32px;
      height: 32px;
      vertical-align: middle;
      margin-right: 12px;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
</style>
