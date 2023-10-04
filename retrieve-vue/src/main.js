import {createApp} from 'vue'
// collapse
import ElementPlus, {ElCollapseTransition} from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from "@/routers/router";
import axios from "axios";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import store from "@/store";
import * as echarts from 'echarts'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import 'dayjs/locale/zh-cn'


const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.config.globalProperties.$echarts = echarts
axios.defaults.baseURL = 'http://127.0.0.1:8081'
// axios.defaults.baseURL = 'http://localhost:8081'
axios.defaults.headers.post['Content-Type'] = 'application/json';
app.use(ElementPlus, {
  locale: zhCn,
}).use(router).use(store)
app.component(ElCollapseTransition.name, ElCollapseTransition)
app.mount('#app')
