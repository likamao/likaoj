import { createPinia } from 'pinia'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './stores/index'
import '@/access/index.ts'
import ArcoVue from '@arco-design/web-vue'
import '@arco-design/web-vue/dist/arco.css'
import 'bytemd/dist/index.css'


const app = createApp(App)
app.use(ArcoVue).use(createPinia()).use(router).use(store).mount('#app')
