import Vue from 'vue/dist/vue.esm.js'
import App from './App.vue'
import Buefy from 'buefy'
import router from './router/index'
import 'buefy/dist/buefy.css'
import '@/api'
import * as echarts from 'echarts'
Vue.config.productionTip = false
Vue.prototype.$echarts = echarts

Vue.use(Buefy)
new Vue({
  router,
  render: (h) => h(App),
  beforeCreate() {
    Vue.prototype.$bus = this
  },
}).$mount('#app')
