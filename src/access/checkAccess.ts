import ACCESS_ENUM from '@/access/accessEnum.ts'

/**
 * 用户someUser是否有访问权限
 * @param loginUser
 * @param needAccess
 * @returns boolean
 */
const checkAccess = (loginUser: any, needAccess = ACCESS_ENUM.NO_LOGIN) => {
  const loginAccess = loginUser.userRole || ACCESS_ENUM.NO_LOGIN

  // 不需要用户登录即可访问
  if (needAccess === ACCESS_ENUM.NO_LOGIN) {
    return true
  }
  // 需要用户登录才能访问
  if (needAccess === ACCESS_ENUM.USER) {
    const isUserLogin = loginAccess === ACCESS_ENUM.USER
    if (!isUserLogin) {
      return loginAccess === ACCESS_ENUM.ADMIN
    }
    return isUserLogin
  }

  if (needAccess === ACCESS_ENUM.ADMIN) {
    return loginAccess === ACCESS_ENUM.ADMIN
  }
  return true
}
export default checkAccess
