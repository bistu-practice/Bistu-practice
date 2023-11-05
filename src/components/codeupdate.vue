<template>
  <div class="contain">
    <div>
      <div style="margin-bottom: 1.25rem">灾情编码</div>
      <div class="holder">
        <!-- 灾情编码 -->
        <b-field
          :label="oneinput.placeholder"
          v-for="(oneinput, index) in InputType"
          :key="index"
          label-position="on-border"
        >
          <!-- <b-numberinput :controls=false size="is-small" :placeholder="oneinput.placeholder">
                    </b-numberinput> -->
          <b-input
            :placeholder="oneinput.placeholder"
            size="is-small"
            v-model="oneinput.code"
            type="number"
          >
          </b-input>
        </b-field>
      </div>
    </div>
    <div>
      <div>描述</div>
      <b-input
        maxlength="200"
        type="textarea"
        v-model="desription"
      ></b-input>
    </div>
    <!-- 上传文件 -->
    <div class="button-bottom">
      <b-button
        type="is-dark"
        style="margin-left: 1.25rem; flex: 0 0 auto"
        @click="submit"
        >提交</b-button
      >
      <b-button
        style="background-color: #ffc966; color: #996300; flex: 0 0 auto"
        @click="clearInfo"
        >清空</b-button
      >
      <b-field
        class="file is-primary"
        :class="{ 'has-name': !!file }"
        style="flex: 8 0 auto"
      >
        <b-upload
          v-model="file"
          class="file-label"
          rounded
          @input="inputDMT"
        >
          <span class="file-cta">
            <b-icon
              class="file-icon"
              icon="upload"
            ></b-icon>
            <span class="file-label">{{ file.name || '点击多媒体文件' }}</span>
          </span>
        </b-upload>
      </b-field>
    </div>
    、
    <b-notification
      ref="element"
      :closable="false"
    >
    </b-notification>
  </div>
</template>
<script>
export default {
  data() {
    return {
      resData1: {},
      mutiplfileOK: false,
      dataCode: '',
      file: {},
      FileIsOk: 0,
      Update_state: null,
      formdata: null,
      InputType: [
        {
          placeholder: '省、直辖市、自治区行政区划代码',
          code: '',
        },
        {
          placeholder: '地市行政区划代码',
          code: '',
        },
        {
          placeholder: '县区行政区划',
          code: '',
        },
        {
          placeholder: '乡镇或街道代码',
          code: '',
        },
        {
          placeholder: '行政村或社区代码',
          code: '',
        },
        {
          placeholder: '年.月日.时.分.秒',
          code: '',
        },
        {
          placeholder: '来源码',
          code: '',
        },
        {
          placeholder: '载体码',
          code: '',
        },
        {
          placeholder: '灾情码',
          code: '',
        },
      ],
      desription: '',
    }
  },
  methods: {
    inputDMT() {
      this.$buefy.toast.open({
        message: '选择成功',
        type: 'is-success',
      })
      this.desription = this.file.name
    },
    //提交文件函数
    async uploadFile(file) {
      const loadingComponent = this.$buefy.loading.open({
        container: null,
      })
      // 获得文件信息
      const fileName = file.name
      const contentType = file.type
      const code = this.dataCode
      // 请求临时地址
      const tempUrlParams = {
        code,
        fileName,
        contentType,
      }
      // console.log(tempUrlParams)
      await this.$api.uploadfile
        .getTemAddress(tempUrlParams)
        .then((res) => {
          this.$buefy.toast.open({
            message: '获取临时地址成功',
            type: 'is-success',
          })
          console.log(res)
          this.resData1 = res.data
          // console.log(this.resData1)
        })
        .catch((error) => {
          console.log(error)
          this.$buefy.toast.open({
            message: '获得临时地址错误',
            type: 'is-danger',
          })
        })
      console.log(this.resData1)
      const finFileName = this.resData1.fileName
      const tempUrl = this.resData1.tempUrl
      const disposition = this.resData1.disposition
      const headers = {}
      headers['Content-Type'] = contentType
      headers['Content-Disposition'] = disposition
      // 调用阿里云上传文件接口
      // console.log(headers)
      await this.$api.uploadfile
        .tryTempUrl(tempUrl, file, {
          headers,
          withCredentials: false,
          timeout: 3000000,
        })
        .then((res) => {
          this.$buefy.toast.open({
            message: '阿里云上传文件成功',
            type: 'is-success',
          })
        })
        .catch((err) => {
          console.log(err)
          this.$buefy.toast.open({
            message: '阿里云上传文件错误',
            type: 'is-danger',
          })
        })
      // 向服务器正式发送插入请求
      const insertParam = {
        code: this.dataCode,
        fileName: finFileName,
      }
      // console.log(insertParam)
      await this.$api.uploadfile
        .insertRes(insertParam)
        .then((res) => {
          if (res.code == 0) {
            this.clearInfo()
            this.$buefy.toast.open({
              message: '提交成功',
              type: 'is-success',
            })
          } else {
            this.$buefy.toast.open({
              message: res.description,
              type: 'is-danger',
            })
          }
        })
        .catch((err) => {
          this.$buefy.toast.open({
            message: '插入数据错误',
            type: 'is-danger',
          })
        })
      loadingComponent.close()
    },
    submit() {
      let dataCode = ''
      this.InputType.forEach((input) => {
        dataCode += input.code
      })
      this.dataCode = dataCode
      console.log(dataCode)

      const data = {
        code: dataCode,
        description: this.desription,
      }
      if (data.code.length == 36 && data.description != '') {
        switch (this.InputType[7].code) {
          case '0': {
            this.$api.datalist
              .decodeOneInsert(data)
              .then((res) => {
                if (res.code == 0) {
                  this.clearInfo()
                  this.$buefy.toast.open({
                    message: '提交成功',
                    type: 'is-success',
                  })
                  this.desription = ''
                } else {
                  this.$buefy.toast.open({
                    message: res.description,
                    type: 'is-danger',
                  })
                  this.desription = ''
                }
                console.log(res)
              })
              .catch((error) => {
                this.$buefy.toast.open({
                  message: '哎呀，出错了~',
                  type: 'is-danger',
                })
                // console.log(error)
              })
            break
          }
          case '1': {
            if (this.file.type == 'image/jpeg') this.uploadFile(this.file)
            else {
              this.$buefy.toast.open({
                message: '请选择正确文件类型呢',
                type: 'is-danger',
              })
              this.file = {}
            }
            break
          }
          case '2': {
            if (this.file.type == 'audio/mpeg') this.uploadFile(this.file)
            else {
              this.$buefy.toast.open({
                message: '请选择正确文件类型呢',
                type: 'is-danger',
              })
              this.file = {}
            }
            break
          }
          case '3': {
            if (this.file.type == 'video/mp4') this.uploadFile(this.file)
            else {
              this.$buefy.toast.open({
                message: '请选择正确文件类型呢',
                type: 'is-danger',
              })
              this.file = {}
            }
            break
          }
          case '4': {
            this.uploadFile(this.file)
            break
          }
        }
      } else {
        this.$buefy.toast.open({
          message: '请将信息填写完整！',
          type: 'is-warning',
        })
      }
    },
    clearInfo() {
      this.InputType.forEach((input) => {
        input.code = null
      })

      this.desription = ''
      this.dataCode = ''
      this.mutiplfileOK = false
      this.file = {}
    },
  },
}
</script>
<style lang="less" scoped>
.contain {
  width: 100%;
  margin: 1.25rem;
  padding: 1.25rem;
  border: 2px solid transparent;
  border-radius: 16px;
  background-clip: padding-box, border-box;
  background-origin: padding-box, border-box;
  background-image: linear-gradient(to right, white, white),
    linear-gradient(90deg, grey, black);
  // background-color: white;
}

.holder {
  display: flex;
  flex-direction: row;
  margin-bottom: 1.25rem;
  padding-bottom: 1.25rem;
  border-bottom: 1px solid rgb(158, 154, 154);
}

/deep/.file.is-primary .file-cta {
  background-color: black;
  // height: 30px;
}

/deep/.file.is-primary .file-cta:hover {
  background-color: #ffe4b3;
  color: #cc8400;
  // height: 30px;
}

/deep/.textarea:focus {
  border-color: orange;
  box-shadow: 0 0 1 #ffe4b3;
}

/deep/.is-small.input:focus {
  border-color: orange;
  box-shadow: 0 0 1 #ffe4b3;
}

.button-bottom {
  display: flex;
  flex-direction: row-reverse;
}

/deep/textarea {
  resize: none;
  height: 18.75rem;
}
/deep/article {
  display: none;
}
</style>
