<template>
    <router-view></router-view>
</template>

<script setup>

</script>

<style lang="scss">
.size {
  width: 100%;
  height: 100%;
}
html,
body {
  padding: 0 !important;
  margin: 0;
  overflow: hidden;
  @extend .size;

  #app {
    @extend .size;
  }
}

</style>
