<template>
  <div id="questionsView">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="title" label="名称" style="min-width: 240px">
        <a-input v-model="searchParams.title" placeholder="请输入名称..." />
      </a-form-item>
      <a-form-item field="tags" label="标签" style="min-width: 240px">
        <a-input v-model="searchParams.tags" placeholder="请输入标签..." />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">提交</a-button>
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
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="orange" >{{ tag }}</a-tag>
        </a-space>
      </template>
      <template #acceptRate="{ record }">
        {{ `${record.submitNum ? (record.acceptNum / record.submitNum * 100).toFixed(2)  : '0' }%(${record.submitNum} / ${record.acceptNum})` }}
      </template>
      <template #createTime="{ record }">
          {{moment(record.createTime).format('YYYY-MM-DD')}}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="questionView(record)">做题</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from 'vue'
import {
  type Question,
  QuestionControllerService,
  type QuestionQueryRequest,
} from '../../../generated'
import message from '@arco-design/web-vue/es/message'
import { useRouter } from 'vue-router'
import moment from 'moment'

const total = ref(0)
const dataList = ref([])
const searchParams = ref<QuestionQueryRequest>({
  title: '',
  tags: [],
  current: 1,
  pageSize: 10,
})
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(searchParams.value)
  if (res.code === 0) {
    dataList.value = res.data.records
    total.value = res.data.total
  } else {
    message.error('获取题目列表错误' + res.message)
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
 * 跳转到新增题目页面
 * @param record
 */
const questionView = (record: Question) => {
  router.push({ path: `/view/question/${record.id}` })
}

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
    title: 'ID',
    dataIndex: 'id',
  },
  {
    title: '标题',
    dataIndex: 'title',
  },
  {
    title: '标签',
    slotName: 'tags'
  },
  {
    title: '通过率',
    slotName: 'acceptRate'
  },
  {
    title: '创建时间',
    slotName: 'createTime'
  },
  {
    slotName: 'optional',
  },
]
</script>

<style scoped>
#questionsView {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
