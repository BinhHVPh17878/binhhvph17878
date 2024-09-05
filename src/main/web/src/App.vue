<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Nunito:ital,wght@0,200..1000;1,200..1000&display=swap');
#app {
  font-family: "Nunito", sans-serif;
  word-break: break-word !important;
}
.el-table .cell {
  word-break: break-word !important;
}
</style>
