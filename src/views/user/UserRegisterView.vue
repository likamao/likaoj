<template>
  <div id="userRegisterView">用户注册</div>
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
    <a-form-item field="checkPassword" tooltip="密码长度至少8位" label="用户密码">
      <a-input-password v-model="form.checkPassword" placeholder="请输入密码" />
    </a-form-item>
    <a-form-item style="justify-content: right">
      <a-button type="primary" style="width: 100px" html-type="submit">注册</a-button>
    </a-form-item>
  </a-form>
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

const form = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
}) as UserLoginRequest
const router = useRouter()
const handleSubmit = async (data: any) => {
  const res = await UserControllerService.userRegisterUsingPost(data.values)
  if (res.code === 0) {
    message.success('注册成功')
    await router.push('/login')
    return
  } else {
    message.error('注册失败', res.message)
  }
}
</script>
