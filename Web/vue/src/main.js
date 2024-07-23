import { createApp, h } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
import vue3TreeOrg from 'vue3-tree-org';
import "vue3-tree-org/lib/vue3-tree-org.css";
// import 'dayjs/locale/zh-cn'
// import locale from 'element-plus/lib/locale/lang/zh-cn'
import http from 'axios'
import * as echarts from 'echarts'
import '@/assets/css/global.css'
import { plugin } from 'echarts-for-vue';
const app = createApp(App)
    .use(store)
    .use(router)
    .use(vue3TreeOrg)
    .use(plugin,{echarts, h})
    .use(ElementPlus, {size: 'small' })
    .mount('#app')
app.echarts = echarts
