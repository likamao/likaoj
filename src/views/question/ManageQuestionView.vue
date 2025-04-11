<template>
  <div id="manageQuestionView">
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
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doUpdate(record)">修改</a-button>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from 'vue'
import { type Question, QuestionControllerService } from '../../../generated'
import message from '@arco-design/web-vue/es/message'
import { useRouter } from 'vue-router'

const total = ref(0)
const dataList = ref([])
const searchParams = ref({
  current: 1,
  pageSize: 10,
})
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionByPageUsingPost(searchParams.value)
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
const doUpdate = (record: Question) => {
  router.push({ path: '/update/question', query: { id: record.id } })
}
/**
 * 删除题目
 * @param record
 */
const doDelete = async (record: Question) => {
  const res = await QuestionControllerService.deleteQuestionUsingPost({ id: record.id })
  if (res.code === 0) {
    message.success('删除成功')
    await loadData()
  } else {
    message.error('删除失败' + res.message)
  }
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
    title: '内容',
    dataIndex: 'content',
  },
  {
    title: '标签',
    dataIndex: 'tags',
  },
  {
    title: '答案',
    dataIndex: 'answer',
  },
  {
    title: '提交数',
    dataIndex: 'submitNum',
  },
  {
    title: '通过数',
    dataIndex: 'acceptedNum',
  },
  {
    title: '判题配置',
    dataIndex: 'judgeConfig',
  },
  {
    title: '判题用例',
    dataIndex: 'judgeCase',
  },
  {
    title: '判题配置',
    dataIndex: 'judgeConfig',
  },
  {
    title: '用户ID',
    dataIndex: 'userId',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: 'Optional',
    slotName: 'optional',
  },
]
</script>

<style scoped>
#manageQuestionView {
}
</style>
