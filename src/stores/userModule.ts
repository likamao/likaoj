import ACCESS_ENUM from '@/access/accessEnum.ts'
import { UserControllerService } from '../../generated'
import message from '@arco-design/web-vue/es/message'

const userStore = {
  state: () => ({
    loginUser: {
      userName: '未登录',
    },
  }),
  actions: {
    async getLoginUser({ commit, state }, payload) {
      const res = await UserControllerService.getLoginUserUsingGet()
      if (res.code === 0) {
        commit('updateUser', res.data);
      } else {
        commit('updateUser', {
          ...state.loginUser,
          userRole: ACCESS_ENUM.NO_LOGIN,
        });
      }
      return res.code;
    },
  },
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload
    },
  },
  getters: {},
}
export default userStore
