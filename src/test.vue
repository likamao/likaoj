<template>
  <div id="test"></div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'

onMounted(() => {
  console.log('mounted')
})
</script>

<style scoped>
#test {
}
</style>
