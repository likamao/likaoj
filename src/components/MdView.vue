<template>
  <Viewer :value="value" :plugins="plugins" />
</template>

<script setup lang="ts">
import gfm from '@bytemd/plugin-gfm'
import highlight from '@bytemd/plugin-highlight'
import {Viewer} from '@bytemd/vue-next'

interface Props {
  value: any
}

const props = withDefaults(defineProps<Props>(), {
  value: '',
})

const plugins = [
  gfm(),
  highlight(),
  // Add more plugins here
]
</script>

<style></style>
