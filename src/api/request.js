import axios from 'axios'
//创建新的axios实例，
const request = axios.create({
  // 基址
  baseURL: '/api',
  // 5s超时
  timeout: 8 * 1000,
})

//请求拦截器
request.interceptors.request.use(
  (config) => {
    // 如果有Token值,则获取这个Token并且使用
    // let userToken = store.state.user.token
    let userToken = localStorage.getItem('token')
    if (userToken) {
      config.headers['Authorization'] = userToken
    }
    return config
  },
  (error) => {
    Promise.reject(error)
  },
)

//响应拦截器
request.interceptors.response.use(
  (res) => {
    return res.data
  },
  (err) => {
    //响应错误
    console.log(err)
    return Promise.reject(err)
  },
)
//4.导入文件
export default request
