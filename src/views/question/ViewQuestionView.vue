<template>
  <div id="viewQuestionView">
    <a-row :gutter="[24, 24]">
      <a-col :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目">
            <a-card :title="question.title" v-if="question">
              <a-descriptions title="判题限制" :column="{ xs: 1, md: 2, lg: 3 }">
                <a-descriptions-item label="时间限制">
                  {{ question.judgeConfig.timeLimit ?? 0 }} ms
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  {{ question.judgeConfig.memoryLimit ?? 0 }} kb
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig.stackLimit ?? 0 }}
                </a-descriptions-item>
              </a-descriptions>
              <MdView :value="question.content as any" />
              <template #extra>
                <a-space>
                  <a-tag v-for="(tag, index) of question.tags" :key="index" color="orange"
                    >{{ tag }}
                  </a-tag>
                </a-space>
              </template>
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="comment" title="评论" disabled></a-tab-pane>
          <a-tab-pane key="answer" title="答案">
            {{ (question && question.answer) || '暂无答案' }}
          </a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col :md="12" :xs="24">
        <a-form :model="form" :label-width="80" layout="inline">
          <a-form-item label="语言:" field="title" style="min-width: 240px">
            <a-select v-model="form.language" :style="{ width: '320px' }" placeholder="请选择语言">
              <a-option>java</a-option>
              <a-option>cpp</a-option>
              <a-option>go</a-option>
              <a-option disabled>Disabled</a-option>
            </a-select>
          </a-form-item>
        </a-form>

        <CodeEditor
          :value="form.code as string"
          :language="form.language as string"
          :handle-change="changeCode"
        />
        <a-button type="primary" @click="doSubmit" style="margin-top: 20px; min-width: 100px"
          >提交
        </a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import {
  QuestionControllerService,
  type QuestionSubmitAddRequest,
  type QuestionVO,
} from '../../../generated'
import message from '@arco-design/web-vue/es/message'
import CodeEditor from '@/components/CodeEditor.vue'
import MdView from '@/components/MdView.vue'

/**
 * 返回数据
 */
const data = ref()
const question = ref<QuestionVO>()

interface props {
  id: string
}

const { id } = withDefaults(defineProps<props>(), {
  id: () => '1',
})

const form = ref<QuestionSubmitAddRequest>({
  language: 'java',
  code: '',
})

const loadData = () => {
  QuestionControllerService.getQuestionVoByIdUsingGet(Number(id))
    .then((res) => {
      if (res.code === 0) {
        question.value = res.data as QuestionVO
      } else {
        throw new Error(res.message)
      }
    })
    .catch((err) => {
      message.error(err.message)
      console.error(err)
    })
}

const doSubmit = () => {
  if (!form.value || !question.value?.id ) {
    message.error('题目不存在')
    return
  }

  QuestionControllerService.dosSubmitQuestionUsingPost({
    ...form.value,
    questionId: question.value.id,
  })
    .then((res) => {
      if (res?.code === 0) {
        message.success('提交成功')
      } else {
        throw new Error(res.message)
      }
    })
    .catch((err) => {
      message.error(err.message)
      console.error(err)
    })
}

onMounted(() => {
  loadData()
})

const changeCode = (value: string) => {
  // form.value = {
  //   ...form.value,
  //   code: value,
  // }
  form.value.code = value
}
</script>

<style scoped>
#viewQuestionView {
  max-width: 1400px;
  margin: 0 auto;
}
</style>
