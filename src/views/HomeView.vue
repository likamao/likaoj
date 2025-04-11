<template>
  <main>
    <MdEditor :value="value" :handle-change="handleChange" />
    <CodeEditor :value="codeEditorValue" :handle-change="handleCodeEditorChange" />
  </main>
</template>

<script setup lang="ts">
import MdEditor from '@/components/MdEditor.vue'
import { ref } from 'vue'
import CodeEditor from '@/components/CodeEditor.vue'

const value = ref('')
const codeEditorValue = ref<any>(null)
const handleChange = (val: string) => {
  value.value = val
}
const handleCodeEditorChange = (val: any) => {
  codeEditorValue.value = val
}
</script>
