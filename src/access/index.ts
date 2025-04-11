// 全局路由权限拦截
import router from '@/router'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import store from '@/stores'
import checkAccess from '@/access/checkAccess.ts'

router.beforeEach(async (to, from, next) => {
  let loginUser = store.state.user.loginUser

  // 如果没有登录过，自动登录
  if (!loginUser || !loginUser.userRole) {
    await store.dispatch('getLoginUser', {})
    loginUser = store.state.user.loginUser
  }

  const needAccess = (to.meta?.access as string) ?? ACCESS_ENUM.NO_LOGIN
  // 如果不需要登录，直接放行
  if (needAccess !== ACCESS_ENUM.NO_LOGIN) {
    if (!loginUser || !loginUser.userRole || loginUser.userRole === ACCESS_ENUM.NO_LOGIN) {
      next({ path: '/user/login', query: { redirect: to.fullPath } })
    }
    if (!checkAccess(loginUser, needAccess)) {
      return next({ path: '/noAuth' })
    }
  }

  next()
  return
})
