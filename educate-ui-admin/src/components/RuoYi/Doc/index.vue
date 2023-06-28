<template>
  <div>
    <font-awesome-icon
        @click="goto()"
        icon="fa-solid fa-question"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const envUrl = import.meta.env.VITE_APP_GITHUB_URL
const url = ref(envUrl)

const goto = ()=>{
  window.open(url.value)
}
</script>

<style scoped>

</style>
