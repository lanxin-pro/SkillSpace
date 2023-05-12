<template>
  <div class="app-wrapper" >
    <div class="drawer-bg" @click="handleClickOutside"/>
    <sidebar class="sidebar-container" />
    <div class="main-container">
      <div>
        <navbar/>
        <tags-view/>
      </div>
      <app-main/>
      <right-panel>
        <settings/>
      </right-panel>
    </div>
  </div>
</template>

<script setup>
import RightPanel from '@/components/RightPanel/index.vue'
import { AppMain, Navbar, Settings, Sidebar, TagsView } from './components'
import variables from '@/assets/styles/variables.scss'

const handleClickOutside = ()=>{

}
</script>

<style lang="scss" scoped>


</style>
