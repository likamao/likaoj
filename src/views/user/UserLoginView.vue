<template>
  <div id="userLoginView">
    <div class="title">用户登录</div>
    <a-form
      label-align="left"
      auto-label-width
      style="max-width: 480px; margin: 0 auto"
      :model="form"
      :style="{ width: '600px' }"
      @submit="handleSubmit"
    >
      <a-form-item field="userAccount" label="用户账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="userPassword" tooltip="密码长度至少8位" label="用户密码">
        <a-input-password v-model="form.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <a-form-item style="justify-content: right">
        <a-button type="primary" style="width: 100px" html-type="submit">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<style scoped>
.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 50px;
  text-align: center;
}
</style>

<script setup lang="ts">
import { reactive } from 'vue'
import { UserControllerService, type UserLoginRequest } from '../../../generated'
import message from '@arco-design/web-vue/es/message'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

const form = reactive({
  userAccount: '',
  userPassword: '',
}) as UserLoginRequest
const router = useRouter()
const store = useStore()

const handleSubmit = async (data: any) => {
  const res = await UserControllerService.userLoginUsingPost(data.values)
  if (res.code === 0) {
    let code = await store.dispatch('getLoginUser', {})
    if (code === 0) {
      message.success('登录成功')
      await router.push('/')
    }
    return
  } else {
    message.error('登录失败', res?.message)
  }
}
</script>
