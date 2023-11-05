import Vue from 'vue/dist/vue.esm.js'
import datalist from './datalist'
import login from './login'
import uploadfile from './upload'
const api = {
  datalist,
  login,
  uploadfile,
}

Vue.prototype.$api = api
export default api
