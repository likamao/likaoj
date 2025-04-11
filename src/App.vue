<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
     <router-view />
    </template>

    <template v-else>
      <BaseLayout />
    </template>
  </div>
</template>

<script setup lang="ts">
import BaseLayout from './layout/BaseLayout.vue'
import { useRoute } from 'vue-router'

const route = useRoute()

</script>

<style scoped>
#app {
}
</style>
