import { createStore } from 'vuex'
import userModule from './userModule'

export default createStore({
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    user: userModule,
  },
})
