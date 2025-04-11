<template>
  <div id="code-editor" ref="codeEditorRef" :style="{ height: '400px' }"></div>
</template>

<script setup lang="ts">
import * as monaco from 'monaco-editor'
import { onMounted, ref, toRaw, watch } from 'vue'

const codeEditorRef = ref()
const codeEditor = ref()

interface Props {
  value: string
  language: string
  handleChange: (value: string) => void
}

const props = withDefaults(defineProps<Props>(), {
  value: () => 'hello world',
  language: () => 'java',
  handleChange: (value: string) => {},
})

watch(
  () => props.language,
  () => {
    if (codeEditor.value) {
      monaco.editor.setModelLanguage(toRaw(codeEditor.value).getModel(), props.language)
    }
  },
)

onMounted(() => {
  if (props.value == null) {
    props.handleChange('hello world')
  }
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value,
    language: props.language,
    automaticLayout: true,
    lineNumbers: 'on',
    disableLayerHinting: true,
    colorDecorators: true,
    minimap: {
      enabled: true,
      scale: 30,
      size: 'proportional',
    },
    roundedSelection: false,
    scrollBeyondLastLine: false,
    readOnly: false,
    theme: 'vs-dark',
  })

  codeEditor.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditor.value).getValue())
  })
})
</script>

<style>
#code-editor {
  min-height: 600px;
}
</style>
