<template>
  <div class="contain">
    <div
      v-for="(selectsection, indexS) in SelectSection"
      :key="indexS"
      class="holder"
    >
      <!-- 每块的标题 -->
      <div class="sub-title">{{ selectsection.SectionName }}</div>
      <!-- 每块的选项框 -->
      <div class="DisAttriInf">
        <b-select
          :placeholder="selectoption.placeholder"
          v-for="(selectoption, index) in selectsection.SelectChoose"
          :key="index"
          :icon="selectsection.icon"
          v-model="selectoption.datamodel"
          size="is-small"
          class="nav"
          @input="updateOptions(indexS, index)"
        >
          <!-- 每各选项框的选项 -->
          <option
            :value="option.value"
            v-for="(option, index) in selectoption.options"
            :key="index"
          >
            {{ option.label }}
          </option>
        </b-select>

        <!-- 灾情属性信息下的时间选择器 -->
        <b-datetimepicker
          v-model="selected"
          v-if="selectsection.SectionName == '灾情属性信息'"
          placeholder="请选择灾情发生时间"
          icon="calendar-today"
          :icon-right="selected ? 'close-circle' : ''"
          icon-right-clickable
          @icon-right-click="clearDateTime"
          :locale="locale"
          :first-day-of-week="firstDayOfWeek"
          :datepicker="{ showWeekNumber }"
          :timepicker="{ enableSeconds, hourFormat }"
          horizontal-time-picker
          size="is-small"
        >
        </b-datetimepicker>

        <!-- 灾情载体信息下的上传文件 -->
        <b-field
          class="file is-primary"
          :class="{ 'has-name': !!file }"
          v-if="
            selectsection.SectionName == '灾情载体信息' &&
            selectsection.SelectChoose[0].datamodel != '0' &&
            selectsection.SelectChoose[0].datamodel != null
          "
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
              <span class="file-label">{{
                file.name || '点击上传多媒体文件'
              }}</span>
            </span>
          </b-upload>
        </b-field>
      </div>
    </div>
    <div>
      <div>描述</div>
      <b-input
        maxlength="200"
        type="textarea"
        v-model="description"
        :disabled="disable"
      ></b-input>
    </div>

    <div class="button-bottom">
      <b-button
        type="is-dark"
        style="margin-left: 20px"
        @click="submit"
        >提交</b-button
      >
      <b-button
        style="background-color: #ffc966; color: #996300"
        @click="clearInfo"
        >清空</b-button
      >
    </div>
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
      selected: new Date(),
      showWeekNumber: false,
      enableSeconds: false,
      hourFormat: undefined,
      locale: undefined,
      firstDayOfWeek: undefined,
      description: '',
      addressInfo: [],
      file: {},
      FileIsOk: 0,
      Update_state: null,
      formdata: null,
      disable: false,
      SelectSection: [
        {
          SectionName: '灾情属性信息',
          icon: 'earth',
          SelectChoose: [
            {
              placeholder: '省、直辖市、自治区行政区划代码',
              datamodel: null,
              options: [
                {
                  value: '110000000000',
                  label: '北京市',
                },
                {
                  value: '120000000000',
                  label: '天津市',
                },
                {
                  value: '130000000000',
                  label: '河北省',
                },
                {
                  value: '140000000000',
                  label: '山西省',
                },
                {
                  value: '150000000000',
                  label: '内蒙古自治区',
                },
                {
                  value: '210000000000',
                  label: '辽宁省',
                },
                {
                  value: '220000000000',
                  label: '吉林省',
                },
                {
                  value: '230000000000',
                  label: '黑龙江省',
                },
                {
                  value: '310000000000',
                  label: '上海市',
                },
                {
                  value: '320000000000',
                  label: '江苏省',
                },
                {
                  value: '330000000000',
                  label: '浙江省',
                },
                {
                  value: '340000000000',
                  label: '安徽省',
                },
                {
                  value: '350000000000',
                  label: '福建省',
                },
                {
                  value: '360000000000',
                  label: '江西省',
                },
                {
                  value: '370000000000',
                  label: '山东省',
                },
                {
                  value: '410000000000',
                  label: '河南省',
                },
                {
                  value: '420000000000',
                  label: '湖北省',
                },
                {
                  value: '430000000000',
                  label: '湖南省',
                },
                {
                  value: '440000000000',
                  label: '广东省',
                },
                {
                  value: '450000000000',
                  label: '广西壮族自治区',
                },
                {
                  value: '460000000000',
                  label: '海南省',
                },
                {
                  value: '500000000000',
                  label: '重庆市',
                },
                {
                  value: '510000000000',
                  label: '四川省',
                },
                {
                  value: '520000000000',
                  label: '贵州省',
                },
                {
                  value: '530000000000',
                  label: '云南省',
                },
                {
                  value: '540000000000',
                  label: '西藏自治区',
                },
                {
                  value: '610000000000',
                  label: '陕西省',
                },
                {
                  value: '620000000000',
                  label: '甘肃省',
                },
                {
                  value: '630000000000',
                  label: '青海省',
                },
                {
                  value: '640000000000',
                  label: '宁夏回族自治区',
                },
                {
                  value: '650000000000',
                  label: '新疆维吾尔自治区',
                },
              ],
            },
            {
              placeholder: '地市行政区划代码',
              datamodel: null,
              options: [
                {
                  value: '',
                  label: '',
                },
              ],
            },
            {
              placeholder: '县区行政区划',
              datamodel: null,
              options: [
                {
                  value: '',
                  label: '',
                },
              ],
            },
            {
              placeholder: '乡镇或街道代码',
              datamodel: null,
              options: [
                {
                  value: '',
                  label: '',
                },
              ],
            },
            {
              placeholder: '行政村或社区代码',
              datamodel: null,
              options: [
                {
                  value: '',
                  label: '',
                },
              ],
            },
          ],
        },
        {
          SectionName: '灾情报送单位',
          SelectChoose: [
            {
              placeholder: '大类代码',
              datamodel: null,
              options: [
                {
                  value: '1',
                  label: '业务报送数据',
                },
                {
                  value: '2',
                  label: '泛在感知数据',
                },
                {
                  value: '3',
                  label: '其他数据',
                },
              ],
            },
            {
              placeholder: '子类代码',
              datamodel: null,
              options: [
                {
                  value: '',
                  label: '',
                },
              ],
            },
          ],
        },
        {
          SectionName: '灾情载体信息',
          SelectChoose: [
            {
              placeholder: '载体码',
              datamodel: null,
              options: [
                {
                  value: '0',
                  label: '文字',
                },
                {
                  value: '1',
                  label: '图像',
                },
                {
                  value: '2',
                  label: '音频',
                },
                {
                  value: '3',
                  label: '视频',
                },
                {
                  value: '4',
                  label: '其他',
                },
              ],
            },
          ],
        },
        {
          SectionName: '数据具体信息',
          SelectChoose: [
            {
              placeholder: '灾害大类',
              datamodel: null,
              options: [
                {
                  value: '1',
                  label: '震情',
                },
                {
                  value: '2',
                  label: '人员伤亡及失踪',
                },
                {
                  value: '3',
                  label: '房屋破坏',
                },
                {
                  value: '4',
                  label: '生命线工程灾情',
                },
                {
                  value: '5',
                  label: '此生灾害',
                },
              ],
            },
            {
              placeholder: '灾害子类',
              datamodel: null,
              options: [
                {
                  value: '',
                  label: '',
                },
              ],
            },
            {
              placeholder: '灾情指标',
              datamodel: null,
              options: [
                {
                  value: '',
                  label: '',
                },
              ],
            },
          ],
        },
      ],
      //selection的options
      originSmallClassOptions: [
        [],
        //大类1对应的子类
        [
          {
            value: '00',
            label: '前方地震应急指挥部',
          },
          {
            value: '01',
            label: '后方地震应急指挥部',
          },
          {
            value: '20',
            label: '应急指挥技术系统',
          },
          {
            value: '21',
            label: '社会服务工程应急救援系统',
          },
          {
            value: '40',
            label: '危险区预评估工作组',
          },
          {
            value: '41',
            label: '地震应急指挥技术协调组',
          },
          {
            value: '42',
            label: '震后政府信息支持工作项目组',
          },
          {
            value: '80',
            label: '灾情快速上报接收处理系统',
          },
          {
            value: '81',
            label: '地方地震局应急信息服务相关技术系统',
          },
          {
            value: '99',
            label: '其他',
          },
        ],
        //大类2对应的子类
        [
          {
            value: '00',
            label: '互联网感知',
          },
          {
            value: '01',
            label: '通信网感知',
          },
          {
            value: '02',
            label: '舆情网感知',
          },
          {
            value: '03',
            label: '电力系统感知',
          },
          {
            value: '04',
            label: '交通系统感知',
          },
          {
            value: '05',
            label: '其他',
          },
        ],
        //其他子类 要做特殊处理
        [
          {
            value: '00',
            label: '其他',
          },
        ],
      ],
      //灾情信息子类
      disasterSmallClassOptions: [
        [],
        //大类1对应的子类
        [
          {
            value: '01',
            label: '震情信息',
          },
        ],
        //大类2对应的子类
        [
          {
            value: '01',
            label: '死亡',
          },
          {
            value: '02',
            label: '受伤',
          },
          {
            value: '03',
            label: '失踪',
          },
        ],
        //大类3对应的子类
        [
          {
            value: '01',
            label: '土木',
          },
          {
            value: '02',
            label: '砖木',
          },
          {
            value: '03',
            label: '砖混',
          },
          {
            value: '04',
            label: '框架',
          },
          {
            value: '05',
            label: '其他',
          },
        ],
        //大类4对应的子类
        [
          {
            value: '01',
            label: '交通',
          },
          {
            value: '02',
            label: '供水',
          },
          {
            value: '03',
            label: '输油',
          },
          {
            value: '04',
            label: '燃气',
          },
          {
            value: '05',
            label: '电力',
          },
          {
            value: '06',
            label: '通信',
          },
          {
            value: '07',
            label: '水利',
          },
        ],
        //大类5对应的子类
        [
          {
            value: '01',
            label: '崩塌',
          },
          {
            value: '02',
            label: '滑坡',
          },
          {
            value: '03',
            label: '泥石流',
          },
          {
            value: '04',
            label: '岩溶塌陷',
          },
          {
            value: '05',
            label: '地裂缝',
          },
          {
            value: '06',
            label: '地面沉降',
          },
          {
            value: '07',
            label: '其他',
          },
        ],
      ],
      //灾情指标
      disasterStandardOptions: [
        [
          {
            value: '',
            label: '',
          },
        ],
        //大类1对应的灾情指标
        [
          {
            value: '001',
            label: '地理位置',
          },
          {
            value: '002',
            label: '时间',
          },
          {
            value: '003',
            label: '震级',
          },
          {
            value: '004',
            label: '震源深度',
          },
          {
            value: '005',
            label: '烈度',
          },
        ],

        //大类2对应的灾情指标
        [
          {
            value: '001',
            label: '受灾人数',
          },
          {
            value: '002',
            label: '受灾程度',
          },
        ],
        //大类3对应的灾情 指标
        [
          {
            value: '001',
            label: '一般损坏面积',
          },
          {
            value: '002',
            label: '严重损坏面积',
          },
          {
            value: '003',
            label: '受灾程度',
          },
        ],
        //大类4对应的灾情指标
        [
          {
            value: '001',
            label: '受灾设施数',
          },
          {
            value: '002',
            label: '受灾范围',
          },
          {
            value: '003',
            label: '受灾程度',
          },
        ],
        //大类5对应的灾情指标
        [
          {
            value: '001',
            label: '灾害损失',
          },
          {
            value: '002',
            label: '灾害范围',
          },
          {
            value: '003',
            label: '受灾程度',
          },
        ],
      ],
      selectionOptions: {},
      selectionData: [],
    }
  },
  methods: {
    //选择文件函数
    inputDMT() {
      // console.log(this.file.type)
      switch (this.SelectSection[2].SelectChoose[0].datamodel) {
        case '1': {
          console.log('文件为图像')
          if (this.file.type == 'image/jpeg') {
            this.$buefy.toast.open({
              message: '选择成功',
              type: 'is-success',
            })
            this.mutiplfileOK = true
            this.description = this.file.name
          } else {
            this.$buefy.toast.open({
              message: '请选择正确文件类型呢',
              type: 'is-danger',
            })
            this.file = {}
          }
          break
        }
        case '2': {
          console.log('文件为音频')
          if (this.file.type == 'audio/mpeg') {
            this.$buefy.toast.open({
              message: '选择成功',
              type: 'is-success',
            })
            this.mutiplfileOK = true
            this.description = this.file.name
          } else {
            this.$buefy.toast.open({
              message: '请选择正确文件类型呢',
              type: 'is-danger',
            })
            this.file = {}
          }
          break
        }
        case '3': {
          console.log('文件为视频')
          if (this.file.type == 'video/mp4') {
            this.$buefy.toast.open({
              message: '选择成功',
              type: 'is-success',
            })
            this.mutiplfileOK = true
            this.description = this.file.name
          } else {
            this.$buefy.toast.open({
              message: '请选择正确文件类型呢',
              type: 'is-danger',
            })
            this.file = {}
          }
          break
        }
        ///////////其他类型咋处理
        case '4': {
          console.log('文件为其他')
          this.$buefy.toast.open({
            message: '选择成功',
            type: 'is-success',
          })
          this.mutiplfileOK = true
          this.description = this.file.name
          break
        }
        default: {
          break
        }
      }
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
          // console.log(res)
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

      const finFileName = this.resData1.fileName
      const tempUrl = this.resData1.tempUrl
      const disposition = this.resData1.disposition
      const headers = {}
      headers['Content-Type'] = contentType
      headers['Content-Disposition'] = disposition
      // headers['timeout'] = 3 * 60 * 1000
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
    clearDateTime() {
      this.selected = null
    },
    updateOptions(indexS, index) {
      //第一行行政区域
      if (indexS == 0) {
        if (index == 0) {
          this.SelectSection[indexS].SelectChoose[1].options = this.getOptions(
            this.SelectSection[indexS].SelectChoose[0].datamodel
          )
        }
        if (index == 1) {
          this.SelectSection[indexS].SelectChoose[2].options = this.getOptions(
            this.SelectSection[indexS].SelectChoose[1].datamodel
          )
        }
        if (index == 2) {
          this.SelectSection[indexS].SelectChoose[3].options = this.getOptions(
            this.SelectSection[indexS].SelectChoose[2].datamodel
          )
        }
        if (index == 3) {
          this.SelectSection[indexS].SelectChoose[4].options = this.getOptions(
            this.SelectSection[indexS].SelectChoose[3].datamodel
          )
        }
      }

      if (indexS == 1) {
        // this.SelectSection[indexS].SelectChoose[1].datamodel = 'ok'
        this.SelectSection[indexS].SelectChoose[1].options =
          this.originSmallClassOptions[
            Number(this.SelectSection[indexS].SelectChoose[0].datamodel)
          ]
      }

      if (indexS == 3) {
        this.SelectSection[indexS].SelectChoose[1].options =
          this.disasterSmallClassOptions[
            Number(this.SelectSection[indexS].SelectChoose[0].datamodel)
          ]
        this.SelectSection[indexS].SelectChoose[2].options =
          this.disasterStandardOptions[
            Number(this.SelectSection[indexS].SelectChoose[0].datamodel)
          ]
      }
      if (this.SelectSection[2].SelectChoose[0].datamodel != '0') {
        this.disable = true
      } else if (this.SelectSection[2].SelectChoose[0].datamodel == '0') {
        this.disable = false
      }
    },
    //获取地址信息
    getAddressInfo() {
      let xhr = new XMLHttpRequest()
      xhr.open(
        'GET',
        'https://bistu-disaster.oss-cn-beijing.aliyuncs.com/area-number-2020.txt',
        false
      ) // public文件夹下的绝对路径
      xhr.overrideMimeType('text/html;charset=utf-8')
      xhr.send(null)
      //console.log(xhr.responseText[0]) // xhr.responseText为文本中的内容
      this.addressInfo = xhr.responseText.split('\n')
      //   console.log(this.addressInfo)
    },

    //找到匹配的地址列表
    findAddressList(findCode) {
      let selectResult = []
      findCode = findCode + '\r'
      //   console.log(findCode)
      this.addressInfo.forEach((line) => {
        if (line.lastIndexOf(findCode) != -1) {
          selectResult.push(line)
        }
      })
      return selectResult
    },

    //构建选项
    constructOptions(selectResult) {
      let options = []
      selectResult.forEach((item) => {
        let itemSplit = item.split('\t')
        const option = {
          value: itemSplit[1],
          label: itemSplit[2],
        }
        options.push(option)
      })
      return options
    },

    //进一步封装 获取选项列表
    getOptions(addressCode) {
      let selectResult = this.findAddressList(addressCode)
      return this.constructOptions(selectResult)
    },
    //上传
    submit() {
      let result = this.SelectSection
      let date = this.selected
      let mouth = date.getMonth() + 1
      let d = date.getDate()
      let h = date.getHours()
      let m = date.getMinutes()
      let s = date.getSeconds()
      let dateCode =
        String(date.getFullYear()) +
        (mouth < 10 ? '0' + mouth : String(mouth)) +
        (d < 10 ? '0' + d : String(d)) +
        (h < 10 ? '0' + h : String(h)) +
        (m < 10 ? '0' + m : String(m)) +
        (s < 10 ? '0' + s : String(s))
      this.dataCode =
        result[0].SelectChoose[4].datamodel +
        dateCode +
        result[1].SelectChoose[0].datamodel +
        result[1].SelectChoose[1].datamodel +
        result[2].SelectChoose[0].datamodel +
        result[3].SelectChoose[0].datamodel +
        result[3].SelectChoose[1].datamodel +
        result[3].SelectChoose[2].datamodel
      const data = {
        code: this.dataCode,
        description: this.description,
      }
      if (data.code.length == 36 && data.description != '') {
        if (this.SelectSection[2].SelectChoose[0].datamodel != 0) {
          this.uploadFile(this.file)
        } else {
          this.$api.datalist
            .decodeOneInsert(data)
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
              //console.log(res)
            })
            .catch((error) => {
              this.$buefy.toast.open({
                message: '哎呀，出错了~',
                type: 'is-danger',
              })
              console.log(error)
            })
        }
      } else {
        this.$buefy.toast.open({
          message: '请将信息填写完整！',
          type: 'is-warning',
        })
      }
    },
    clearInfo() {
      this.SelectSection.forEach((selection) => {
        selection.SelectChoose.forEach((item) => {
          item.datamodel = null
        })
      })
      this.description = ''
      this.dataCode = ''
      this.mutiplfileOK = false
      this.file = {}
    },
  },
  created() {
    //获取初始地址代码信息
    this.getAddressInfo()
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
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgb(158, 154, 154);
}

.sub-title {
  font-weight: 500;
}

.DisAttriInf {
  display: flex;
  flex-direction: row;
}

.nav {
  margin-right: 0.625rem;
}

/deep/.select select:focus {
  border: 1px solid orange;
  box-shadow: 0 0 1px #ffe4b3;
}

/deep/.select:not(.is-multiple):not(.is-loading)::after {
  border-color: black !important;
}

/deep/.input.is-small:focus {
  border: 1px solid orange;
  box-shadow: 0 0 1px #ffe4b3;
}

/deep/.file.is-primary .file-cta {
  background-color: black;
  height: 30px;
}

/deep/.file.is-primary .file-cta:hover {
  background-color: #ffe4b3;
  color: #cc8400;
  height: 30px;
}

/deep/.textarea:focus {
  border-color: orange;
  box-shadow: 0 0 1 #ffe4b3;
}

.button-bottom {
  display: flex;
  flex-direction: row-reverse;
}

/deep/textarea {
  resize: none;
}
/deep/article {
  display: none;
}
</style>
