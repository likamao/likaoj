<template>
  <div id="addQuestionView">
    <a-form :model="form" :layout="layout" label-align="left">
      <h2>创建题目</h2>
      <a-form-item field="title">
        <a-input v-model="form.title" placeholder="请输入题目标题" />
      </a-form-item>
      <a-form-item field="title">
        <a-input-tag v-model="form.tags" placeholder="请输入标签" />
      </a-form-item>
      <a-form-item field="answer" label="答案">
        <MdEditor :value="form.answer" :handle-change="handleAnswerValueChange" />
      </a-form-item>
      <a-form-item field="content" label="题目内容">
        <MdEditor :value="form.content" :handle-change="handleCodeEditorChange" />
      </a-form-item>

      <a-form-item label="判题配置" :content-flex="false" :merge-props="false">
        <a-space direction="vertical" style="min-width: 480px">
          <a-form-item field="judgeConfig.timeLimit" label="时间限制">
            <a-input-number
              v-model="form.judgeConfig.timeLimit"
              placeholder="请输入时间限制（毫秒）"
              mode="button"
              :default-value="1000"
              min="0"
              size="large"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.memoryLimit" label="内存限制">
            <a-input-number
              v-model="form.judgeConfig.memoryLimit"
              placeholder="请输入内存限制（KB）"
              mode="button"
              :default-value="1000"
              min="0"
              size="large"
            />
          </a-form-item>
          <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
            <a-input-number
              v-model="form.judgeConfig.stackLimit"
              placeholder="请输入堆栈限制"
              mode="button"
              :default-value="1000"
              min="0"
              size="large"
            />
          </a-form-item>
        </a-space>
      </a-form-item>

      <a-form-item label="判题配置" :content-flex="false" :merge-props="false">
        <a-form-item v-for="(judgeCaseItem, index) of form.judgeCase" :key="index">
          <a-space direction="vertical" style="min-width: 800px">
            <a-form-item
              :field="`form.judgeCase[${index}].input`"
              :label="`输入用例-${index}`"
              :key="index"
            >
              <a-input v-model="judgeCaseItem.input" placeholder="请输入测试输入用例" />
            </a-form-item>
            <a-form-item
              :field="`form.judgeCase[${index}].output`"
              :label="`输出用例-${index}`"
              :key="index"
            >
              <a-input v-model="judgeCaseItem.output" placeholder="请输入测试输出用例" />
            </a-form-item>
            <a-button status="danger" @click="handleDelete(index)">删除</a-button>
          </a-space>
        </a-form-item>
        <a-form-item>
          <a-button @click="handleAdd" type="outline" status="success">新增测试用例</a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" size="large" @click="doSubmit">提交</a-button>
        </a-form-item>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import MdEditor from '@/components/MdEditor.vue'
import { QuestionControllerService } from '../../../generated'
import { useRoute } from 'vue-router'
import message from '@arco-design/web-vue/es/message'

const route = useRoute()

// 当前页面add or update
const currentUpdatePage = route.path.indexOf('update') !== -1

const loadData = () => {
  const id = route.query.id
  if (!id) {
    return
  }
  QuestionControllerService.getQuestionByIdUsingGet(id as any)
    .then((res) => {
      if (res.code === 0) {
        form.value = res.data as any
        if (!form.value.judgeConfig) {
          form.value.judgeConfig = [
            {
              memoryLimit: 1000,
              stackLimit: 1000,
              timeLimit: 1000,
            },
          ]
        } else {
          form.value.judgeConfig = JSON.parse(form.value.judgeConfig as any)
        }

        if (!form.value.judgeCase) {
          form.value.judgeCase = [
            {
              input: '',
              output: '',
            },
          ]
        } else {
          form.value.judgeCase = JSON.parse(form.value.judgeCase as any)
        }
        if (!form.value.tags) {
          form.value.tags = []
        } else {
          form.value.tags = JSON.parse(form.value.tags as any)
        }
      } else {
        message.error('获取题目失败')
      }
    })
    .catch((err) => {
      console.log(err)
      message.error('获取题目失败')
    })
}

onMounted(() => {
  loadData()
})

const form = ref({
  title: '',
  tags: [],
  answer: '',
  content: '',
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  judgeCase: [
    {
      input: '',
      output: '',
    },
  ],
})

/**
 * 处理答案输入框值变化
 * @param val
 */
const handleAnswerValueChange = (val: string) => {
  form.value.answer = val
}

/**
 * 处理代码编辑器值变化
 * @param val
 */
const handleCodeEditorChange = (val: string) => {
  form.value.content = val
}
/**
 * 提交题目
 */
const doSubmit = () => {
  if (currentUpdatePage) {
    updateQuestion()
  } else {
    createQuestion()
  }
}

/**
 * 更新题目
 */
const updateQuestion = () => {
  QuestionControllerService.updateQuestionUsingPost(form.value).then((res) => {
    if (res.code === 0) {
      message.success('更新成功')
    } else {
      message.error('更新失败：' + res.message)
    }
  })
}
/**
 * 创建题目
 */
const createQuestion = () => {
  QuestionControllerService.addQuestionUsingPost(form.value).then((res) => {
    if (res.code === 0) {
      message.success('创建成功')
    } else {
      message.error('创建失败：' + res.message)
    }
  })
}

/**
 * 新增判题用例
 */
const handleAdd = () => {
  form.value.judgeCase.push({
    input: '',
    output: '',
  })
}
/**
 * 删除测试用例
 * @param index
 */
const handleDelete = (index: number) => {
  form.value.judgeCase.splice(index, 1)
}
</script>

<style scoped>
#addQuestionView h2 {
  text-align: center;
}
</style>
