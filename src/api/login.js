import request  from "./request";
export default {
    // 登录
    loginUser(user) {
        return request.post('/user/login', user)
    },
    // 注册
    registerUser(user) {
        return request.post('/user/register', user)
    },
    // 获取当前登录用户
    currentUser() {
        return request.get('/user/currentUser')
    },
    // 更新用户信息
    updateuser(user) {
        return request.put('/user/update', user)
    }
}