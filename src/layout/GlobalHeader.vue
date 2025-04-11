<template>
  <a-row id="globalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu mode="horizontal" :selected-keys="selectedKeys" @menu-item-click="handleClick">
        <a-menu-item key="0" :style="{ padding: 0, marginRight: '38px' }" disabled>
          <div class="title-bar">
            <a-avatar class="avatar">
              <img class="logo" alt="avatar" src="../assets/logo.png" />
            </a-avatar>
            <div class="title">LIKA-OJ</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">{{ item.name }}</a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px" class="loginUser">
      <div>{{ store.state.user?.loginUser?.userName || '未登录' }}</div>
    </a-col>
  </a-row>
</template>

<style scoped>
# globalHeader {
}

.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: #444;
  margin-left: 16px;
}

.logo {
  height: 48px;
}
</style>

<script setup lang="ts">
import { routes } from '@/router/routes'
import { useRouter } from 'vue-router'
import { computed, ref } from 'vue'
import { useStore } from 'vuex'
import checkAccess from '@/access/checkAccess.ts'

const router = useRouter()

const selectedKeys = ref(['/'])
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path]
})
// 点击菜单项
const handleClick = (key: string) => {
  router.push({
    path: key,
  })
}

const store = useStore()
// 过滤出可见的路由
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false
    }
    return checkAccess(store.state.user?.loginUser, item.meta?.access as string)
  })
})
</script>
