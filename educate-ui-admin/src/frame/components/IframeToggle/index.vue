<template>
  <transition-group name="fade-transform" mode="out-in">
    <inner-link
        v-for="(item, index) in iframeViews"
        :key="item.path"
        :iframeId="'iframe' + index"
        v-show="$route.path === item.path"
        :src="item.meta.link"
    ></inner-link>
  </transition-group>
</template>

<script setup>
import InnerLink from "../InnerLink/index.vue"
import { computed } from 'vue'
import store from '@/store'

const iframeViews = computed(()=>{
  return store.state.tagsView.iframeViews
})
</script>

<style scoped>

</style>
