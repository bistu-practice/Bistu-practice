<template>
  <div class="personContain">
    <div class="personList">
      <div class="avatarBox">
        <img class="avatarImg" :src="avatarUrl" alt="图加载错误" />
      </div>
      <b-field label="用户账号" :label-position="labelPosition">
        <b-input v-model="userAccount" size="is-medium"></b-input>
      </b-field>
      <b-field label="用户名" :label-position="labelPosition">
        <b-input v-model="userName" size="is-medium"></b-input>
      </b-field>
      <b-field label="用户职位" :label-position="labelPosition">
        <b-input v-model="userJob" size="is-medium"></b-input>
      </b-field>
      <!-- 两个按钮 -->
      <b-button v-if="updateOrSave" type="is-primary" @click="toUpdate()" style="width: 100px; background-color: orange">修改</b-button>
      <b-button v-if="!updateOrSave" type="is-primary" @click="toSave()" style="width: 100px background-color: orange;">保存</b-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 组件库中设置input左上角字体位置
      labelPosition: 'on-border',
      // 修改或者保存按钮
      updateOrSave: true,
      // 用户id
      userId: '',
      // 用户账号
      userAccount: '',
      // 用户名
      userName: '',
      // 用户职位
      userJob: '暂无',
      // 用户头像
      avatarUrl: ''
    }
  },
  mounted() {
    // 获取登录用户
    this.$api.login.currentUser().then((res) => {
      console.log(res.data)
      this.userAccount = res.data.userAccount
      this.userName = res.data.userName
      this.avatarUrl = res.data.avatarUrl
      this.userId = res.data.id
      if (res.data.userJob == null) {
        this.userJob = '暂无'
      } else {
        this.userJob = res.data.userJob
      }
    })

    // 通过类名获取组件,并且禁用input使用
    let input = document.getElementsByClassName('input is-medium')
    for (let i = 0; i < input.length; i++) {
      input[i].disabled = true
    }
  },
  methods: {
    // 用户可修改
    toUpdate() {
      this.updateOrSave = false
      // 通过类名获取组件,并且禁用input使用
      let input = document.getElementsByClassName('input is-medium')
      for (let i = 1; i < input.length; i++) {
        input[i].disabled = false
      }
    },
    // 用户保存修改
    toSave() {
      this.$api.login
        .updateuser({
          id: this.userId,
          userName: this.userName,
          avatarUrl: this.avatarUrl,
          userJob: this.userJob
        })
        .then((res) => {
          if (res.code == 0) {
            this.$buefy.toast.open({
              message: '修改成功',
              type: 'is-success'
            })
            this.updateOrSave = true
            let input = document.getElementsByClassName('input is-medium')
            for (let i = 0; i < input.length; i++) {
              input[i].disabled = true
            }
          } else {
            this.$buefy.toast.open({
              duration: 5000,
              message: `<b>修改失败</b>`,
              type: 'is-danger'
            })
          }
        })
    }
  }
}
</script>

<style lang="less" scoped>
.personContain {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.personList {
  width: 30%;
  height: 70%;
  display: flex;
  justify-content: space-around;
  flex-direction: column;
}

/* 头像盒子 */
.avatarBox {
  display: flex;
  justify-content: center;
}

/* 头像 */
.avatarImg {
    float: left;
    width: 100px;
    height: 100px;
    object-fit: cover;/*图片完全填充*/
    /*object-position: center;*//*设置头像选取照片的哪个区域object-position: x y*/
    border-radius: 50%;/*头像框圆形设置*/
}

/deep/ .input.is-medium:focus {
  box-shadow: 0 0 0.125rem orange;
}
</style>
