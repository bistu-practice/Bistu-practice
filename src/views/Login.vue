<template>
  <div class="loginContainer">
    <div class="waterContainer">
      <div class="drop" style="--clr: #ff0f5b">
        <div class="content">
          <!-- 登录 -->
          <div class="loginBox" v-if="loginOrRegister">
            <form autocomplete="off" class="loginForm">
              <h2>登录</h2>
              <div class="inputBox">
                <input type="text" required="required" v-model="userLoginAccout" />
                <span>账号</span>
                <i></i>
              </div>
              <div class="inputBox">
                <input type="password" required="required" v-model="userLoginPassword" />
                <span>密码</span>
                <i></i>
              </div>
              <div class="links">
                <a href="#">忘记密码 ?</a>
                <a href="#" @click="changeToRegister()">注册</a>
              </div>
              <input type="button" value="登录" @click.stop="Login()" />
            </form>
          </div>
          <!-- 注册 -->
          <div class="registerBox" v-if="!loginOrRegister">
            <form autocomplete="off" class="loginForm">
              <h2>注册</h2>
              <div class="inputBox">
                <input type="text" required="required" v-model="userRegisterAccount" />
                <span>账号</span>
                <i></i>
              </div>
              <div class="inputBox">
                <input type="password" required="required" v-model="userRegisterPassword" />
                <span>密码</span>
                <i></i>
              </div>
              <div class="inputBox">
                <input type="text" required="required" v-model="userRegisterName" />
                <span>用户名</span>
                <i></i>
              </div>
              <div class="links">
                <a href="#" @click="changeToLogin()" ref="backLoginBox">返回登录</a>
              </div>
              <input type="button" value="注册" @click.stop="Register()" />
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 切换登录注册盒子
      loginOrRegister: true,
      loginFlag: false,
      // 登录账号密码
      userLoginAccout: '',
      userLoginPassword: '',
      // 注册用户账号密码用户名
      userRegisterAccount: '',
      userRegisterPassword: '',
      userRegisterName: ''
    }
  },
  methods: {
    // 切换注册
    changeToRegister() {
      this.userLoginAccout = '',
      this.userLoginPassword = '',
      this.loginOrRegister = !this.loginOrRegister
    },
    // 切换登录
    changeToLogin() {
      this.userRegisterAccount = '',
      this.userRegisterPassword = '',
      this.userRegisterName = '',
      this.loginOrRegister = !this.loginOrRegister
    },
    Login() {
      this.$api.login
        .loginUser({
          userAccount: this.userLoginAccout,
          userPassword: this.userLoginPassword
        })
        .then((res) => {
          if (res.code == 0) {
            this.$buefy.toast.open({
              message: '登陆成功',
              type: 'is-success'
            })
            // 设置token
            localStorage.removeItem('token')
            localStorage.setItem('token', res.data)
            this.$router.push({ name: 'home' })
          } else {
            this.$buefy.toast.open({
              duration: 5000,
              message: `账号或密码<b>错误</b>`,
              type: 'is-danger'
            })
          }
        })
    },
    Register() {
      this.$api.login
        .registerUser({
          userAccount: this.userRegisterAccount,
          userPassword: this.userRegisterPassword,
          userName: this.userRegisterName
        })
        .then((res) => {
          // console.log(res)
          // 注册失败
          if (res.code == 40000) {
            this.$buefy.toast.open({
              duration: 5000,
              message: `<b>` + res.description + `</b>`,
              type: 'is-danger'
            })
          } else {
            this.$buefy.toast.open({
              message: '注册成功',
              type: 'is-success'
            })

            // 回到登录盒子
            this.userLoginAccout = this.userRegisterAccount
            this.userLoginPassword = this.userRegisterPassword
            this.userRegisterAccount = '',
            this.userRegisterPassword = '',
            this.userRegisterName = '',
            this.loginOrRegister = true
          }
        })
    }
  }
}
</script>

<style scoped>
/* 登录外层最大盒子 */
.loginContainer {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  flex-direction: column;
  background-image: url('https://bistu-disaster.oss-cn-beijing.aliyuncs.com/background2.jpeg');
  background-repeat: no-repeat;
  background-position: center center;
  background-repeat: no-repeat;
  /* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */
  background-attachment: fixed;
  /* 让背景图基于容器大小伸缩 */
  background-size: cover;
  /* 设置背景颜色，背景图加载过程中会显示背景色 */
  background-color: #464646;
}

/* 水滴UI界面最大盒子 */
.waterContainer {
  position: relative;
  justify-content: center;
  align-items: center;
  display: flex;
  flex-wrap: wrap;
  padding: 50px 0;
  gap: 40px 60px;
}

/* 模拟阴影水滴的盒子(现在还是正方形) */
.waterContainer .drop {
  position: relative;
  width: 550px;
  height: 550px;
  box-shadow: inset 20px 20px 20px rgba(0, 0, 0, 0.05), 25px 35px 20px rgba(0, 0, 0, 0.05), 25px 30px 30px rgba(0, 0, 0, 0.05), inset -20px -20px 25px rgba(255, 255, 255, 0.9);
  transition: 0.5s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 设置每个盒子圆角,显得像水滴 */
.waterContainer .drop:nth-child(1) {
  border-radius: 53% 48% 48% 53% / 48% 48% 53% 53%;
}

.waterContainer .drop:hover {
  border-radius: 50%;
}

/* 水滴左上角的小高光 */
.waterContainer .drop::before {
  content: '';
  position: absolute;
  top: 80px;
  left: 100px;
  width: 35px;
  height: 35px;
  background-color: #fff;
  border-radius: 50%;
  opacity: 0.9;
}

/* 左上角第二个高光 */
.waterContainer .drop::after {
  content: '';
  position: absolute;
  top: 120px;
  left: 130px;
  width: 15px;
  height: 15px;
  background-color: #fff;
  border-radius: 50%;
  opacity: 0.9;
}

/* 水滴的文本框 */
.waterContainer .drop .content {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  text-align: center;
  padding: 40px;
  gap: 15px;
}

/* 登陆盒子 */
.loginBox {
  position: relative;
  width: 380px;
  height: 420px;
  background: transparent;
  border-radius: 8px;
  overflow: hidden;
}

.registerBox {
  position: relative;
  width: 380px;
  height: 460px;
  background: transparent;
  border-radius: 8px;
  overflow: hidden;
}

/* 不要修改这个z-index小于1,上面两个盒子需要被这个表单挡住,只留下一点点光弧 */
.loginForm {
  position: absolute;
  inset: 2px;
  background: transparent;
  padding: 50px 40px;
  border-radius: 8px;
  z-index: 2;
  display: flex;
  flex-direction: column;
}

.loginForm h2 {
  color: #fff;
  font-size: 200%;
  font-weight: 700;
  text-align: center;
  letter-spacing: 0.1em;
}

/* 输入框盒子 */
.inputBox {
  position: relative;
  width: 300px;
  margin-top: 35px;
}

/* 输入框 */
.inputBox input {
  position: relative;
  width: 100%;
  padding: 20px 10px 10px;
  /* 去掉input框的基本设置 */
  background: transparent;
  outline: none;
  box-shadow: none;
  border: none;
  color: #23242a;
  font-size: 1em;
  letter-spacing: 0.05em;
  transition: 0.5s;
  z-index: 10;
}

/* 输入框盒子的span */
.inputBox span {
  position: absolute;
  left: 0;
  padding: 20px 0px 10px;
  pointer-events: none;
  font-size: 1em;
  color: #fff;
  letter-spacing: 0.05em;
  transition: 0.5s;
}

/* 输入框获取焦点和失去焦点时的动画 */
.inputBox input:valid ~ span,
.inputBox input:focus ~ span {
  color: white;
  transform: translateX(0px) translateY(-34px);
  font-size: 0.75em;
}

/* 重新绘制输入框底部的边框,注意:这是一个i标签 */
.inputBox i {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 2px;
  /* background: #68fc24; */
  background: white;
  border-radius: 4px;
  overflow: hidden;
  transition: 0.5s;
  pointer-events: none;
  z-index: 9;
}

/* 输入框下i标签在失去和获得焦点时的添加的样式 
这主要是让i显得像一个输入框,但其实透明的无样式的输入框在i标签上面*/
.inputBox input:valid ~ i,
.inputBox input:focus ~ i {
  height: 44px;
}

/* 忘记密码和注册的样式 */
.links {
  display: flex;
  justify-content: space-between;
}

.links a {
  margin: 10px 0;
  font-size: 0.75em;
  color: #fff;
  text-decoration: beige;
}

.links a:hover,
.links a:nth-child(2) {
  color: #000;
}

input[type='button'] {
  color: #faffdf;
  border: none;
  outline: none;
  padding: 11px 25px;
  background: #3b261f;
  cursor: pointer;
  border-radius: 4px;
  font-weight: 600;
  width: 100px;
  margin-top: 10px;
}
input[type='button']:active {
  opacity: 0.8;
}
</style>
