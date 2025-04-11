<template>
  <div id="questionSubmitView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="questionId" label="题号" style="min-width: 240px">
        <a-input v-model="searchParams.questionId" placeholder="请输入题号..." />
      </a-form-item>
      <a-form-item label="语言:" field="title" style="min-width: 240px">
        <a-select
          v-model="searchParams.language"
          :style="{ width: '320px' }"
          placeholder="请选择语言"
        >
          <a-option>java</a-option>
          <a-option>cpp</a-option>
          <a-option>go</a-option>
          <a-option disabled>Disabled</a-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">搜索</a-button>
      </a-form-item>
    </a-form>

    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
        showTotal: true,
      }"
      @page-change="handlePageChange"
    >
      <template #judgeInfo="{ record }">
        {{ `判题信息：${record.judgeInfo.message} / ${record.judgeInfo.time} / ${record.judgeInfo.memory}` }}
      </template>
      <template #createTime="{ record }">
        {{ moment().format('YYYY-MM-DD') }}
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from 'vue'
import {
  type Question,
  QuestionControllerService,
  type QuestionSubmitQueryRequest,
} from '../../../generated'
import message from '@arco-design/web-vue/es/message'
import { useRouter } from 'vue-router'
import moment from 'moment'
import { useStore } from 'vuex'

const store = useStore()
const total = ref(0)
const dataList = ref([])
const searchParams = ref<QuestionSubmitQueryRequest>({
  questionId: undefined,
  language: undefined,
  userId: store.state.user.id,
  current: 1,
  pageSize: 10,
  sortField: 'createTime',
  sortOrder: 'desc',
})
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionSubmitByPageUsingPost(searchParams.value)
  if (res.code === 0) {
    dataList.value = res.data.records
    total.value = res.data.total
  } else {
    message.error('获取题目提交列表错误' + res.message)
  }
}

/**
 * 监听函数内变量变化，重新调用数据
 */
watchEffect(() => {
  loadData()
})

/**
 * 页面加载时加载数据
 */
onMounted(() => {
  loadData()
})

const router = useRouter()


/**
 * 分页变化时加载数据
 * @param page
 */
const handlePageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  }
}
const doSubmit = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  }
  loadData()
}

const columns = [
  {
    title: '提交号',
    dataIndex: 'id',
  },
  {
    title: '编程语言',
    dataIndex: 'language',
  },
  {
    title: '判题信息',
    slotName: 'judgeInfo',
  },
  {
    title: '判题状态',
    dataIndex: 'status',
  },
  {
    title: '题目ID',
    dataIndex: 'questionId',
  },
  {
    title: '提交用户ID',
    dataIndex: 'userId',
  },
  {
    title: '创建时间',
    slotName: 'createTime',
  },
]
</script>

<style scoped>
#questionSubmitView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
