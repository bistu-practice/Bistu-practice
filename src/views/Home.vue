<template>
  <div id="home">
    <!-- 头部标题栏 -->
    <div id="home_header">
      <div id="home_header_left">DISASTER</div>
      <div id="home_header_right">
        <!-- 面包屑 -->
        <div id="home_header_breadcrumb">
          <b-breadcrumb>
            <b-breadcrumb-item id="bread1">{{ this.routers[0].firstText }}</b-breadcrumb-item>
            <b-breadcrumb-item tag="router-link" :to="this.routers[0].link" id="bread2">{{ this.routers[0].secondText }} </b-breadcrumb-item>
          </b-breadcrumb>
        </div>
      </div>
    </div>

    <div id="home_main">
      <div id="home_left">
        <b-menu>
          <b-menu-list label="Menu">
            <!-- 账户管理 -->
            <b-menu-item icon="account-hard-hat">
              <template #label="account">
                账户管理
                <b-icon class="is-pulled-right" :icon="account.expanded ? 'menu-up' : 'menu-down'"></b-icon>
              </template>

              <b-menu-item icon="account-hard-hat-outline" label="个人中心" tag="router-link" to="/personPage"> </b-menu-item>
            </b-menu-item>

            <!-- 灾情报送 -->
            <b-menu-item icon="send">
              <template #label="send">
                灾情报送
                <b-icon class="is-pulled-right" :icon="send.expanded ? 'menu-up' : 'menu-down'"></b-icon>
              </template>
              <!-- 手动输入 -->

              <b-menu-item icon="note-edit" label="手动输入" tag="router-link" to="/handupdate"> </b-menu-item>
              <!-- 文件导入 -->
              <b-menu-item icon="microsoft-excel" label="文件导入" type="is-primary" @click="isComponentModalActive = true"> </b-menu-item>
            </b-menu-item>
          </b-menu-list>

          <b-menu-list label="Statistics">
            <!-- 数据查询 -->
            <b-menu-item icon="clipboard-search" label="数据查询"> </b-menu-item>
            <!-- 数据分析及统计 -->
            <b-menu-item icon="chart-box">
              <template #label="dataAnalysis">
                数据分析及统计
                <b-icon class="is-pulled-right" :icon="dataAnalysis.expanded ? 'menu-up' : 'menu-down'"></b-icon>
              </template>
              <!-- 数据列表 -->
              <b-menu-item icon="table" label="数据列表" tag="router-link" to="/dataList"> </b-menu-item>
              <!-- 可视化地图 -->
              <b-menu-item icon="map" label="可视化地图" tag="router-link" to="/map"> </b-menu-item>
              <!-- 数据看板 -->
              <b-menu-item
                icon="finance"
                label="数据统计"
                tag="router-link"
                to="/datastatistic"
              >
              </b-menu-item>
            </b-menu-item>
          </b-menu-list>
        </b-menu>
      </div>
      <div id="home_right">
        <router-view></router-view>
      </div>
    </div>

    <div id="home_footer"></div>

    <!-- Excel导入模态框 -->
    <b-modal v-model="isComponentModalActive" has-modal-card trap-focus :destroy-on-hide="false" aria-role="dialog" aria-label="Example Modal" close-button-aria-label="Close" aria-modal @after-leave="leave">
      <template #default="props">
        <modal-form v-model="formProps" @close="props.close" ref="child"></modal-form>
      </template>
    </b-modal>
  </div>
</template>

<script>
const ModalForm = {
  props: ['file'],
  data() {
    return {
      file2: null,
      FileIsOk: 0,
      Update_state: null,
      formdata: null
    }
  },
  template: `
        <form action="">
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">Excel文件导入</p>
                </header>

                <section class="modal-card-body">
                  <b-field class="file is-primary"  :class="{'has-name': !!file2}">
                    <b-upload v-model="file2" class="file-label" accept=".xlsx" @input="input" id="Excelfile" name="file" rounded>
                      <span class="file-cta">
                        <b-icon class="file-icon" icon="upload"></b-icon>
                        <span class="file-label">提交文件(.xlsx)</span>
                      </span>
                      <span class="file-name" v-if="file2">
                        {{ file2.name }}
                      </span>
                    </b-upload>
                  </b-field> 
                </section>

                <footer class="modal-card-foot">
                    <b-button
                        label="取消"
                        @click="close" />
                    <b-button
                        label="确认上传"
                        style="background-color:#ffc966; color:#996300;"
                        @click="upgrateExcel" />
                </footer>
            </div>
        </form>
    `,
  methods: {
    input(file) {
      let formData = new window.FormData()
      formData.set('file', file)
      this.formdata = formData
      // console.log(formData.get("file"));
      let getfile = formData.get('file')
      if (getfile.type == 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        if (getfile.size < 104857600) {
          this.FileIsOk = 1

          this.$buefy.toast.open({
            message: '成功读取文件！',
            type: 'is-success'
          })
        } else {
          this.FileIsOk = 2
          this.$buefy.toast.open({
            message: '提交的文件大小不超过100MB！',
            type: 'is-warning'
          })
        }
      } else {
        this.FileIsOk = 3
        this.$buefy.toast.open({
          message: '提交的文件类型错误！',
          type: 'is-danger'
        })
      }
    },

    //上传文件
    upgrateExcel() {
      if (this.FileIsOk == 1) {
        this.$api.datalist
          .postExcel(this.formdata)
          .then((res) => {
            // 上传成功
            this.Update_state = res.data
            const params = {
              pageIndex: 1,
              pageSize: 9
            }
            // 总线触发
            // console.log(this.$bus)
            this.$bus.$emit('getDataList')
          })
          .catch((error) => {
            console.log(error)
            this.$buefy.toast.open({
              message: '上传失败，请检查上传文件内容！',
              type: 'is-danger'
            })
          })
        this.FileIsOk = 0
        this.file2 = null
        this.$emit('close')
      } else if (this.FileIsOk == 2) {
        this.$buefy.toast.open({
          message: '提交的文件大小不超过100MB！请重新选择',
          type: 'is-warning'
        })
      } else if (this.FileIsOk == 0) {
        this.$buefy.toast.open({
          message: '请先选择文件！',
          type: 'is-warning'
        })
      }
    },
    close() {
      this.file2 = null
      this.$emit('close')
    }
  }
}
export default {
  components: {
    ModalForm
  },
  data() {
    return {
      isComponentModalActive: false,
      formProps: {
        file: null
      },

      routers: [
        {
          link: '/datalist',
          firstText: '数据分析及统计',
          secondText: '数据列表'
        }
      ]
    }
  },
  methods: {
    leave() {
      this.$refs.child.file2 = null
    }
  },
  watch: {
    $route(to, from) {
      this.routers[0].link = to.path
      this.routers[0].firstText = to.meta.ftitle
      this.routers[0].secondText = to.meta.stitle
      // console.log(this.routers[0].firstText);
      // console.log(to.path);
      // console.log(to.meta.stitle)
      // console.log(to.meta.ftitle)
    }
  }
}
</script>

<style lang="less" scoped>
/* 页面全局设置 */
#home {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-content: center;
}

/* 头部 */
#home_header {
  height: 50px;
  width: 100vw;
  background-color: black;
  display: flex;
  align-content: center;
  flex: 0 0 auto;
}

#home_header_left {
  width: 220px;
  height: 50px;
  background-color: orange;
  color: black;
  line-height: 50px;
  text-align: center;
  font-size: 30px;
  font: 600;
}

#home_header_right {
  height: 50px;
  line-height: 50px;
}

#home_header_breadcrumb {
  margin-left: 10px;
}

/deep/.breadcrumb {
  a {
    color: orange;
    font: 400;
    font-size: 10px;
  }

  li::before {
    font-size: 10px;
  }
}

/* 中部 */
#home_main {
  width: 100%;
  display: flex;
  flex-direction: row;
  flex: 1 0 auto;
}

#home_left {
  min-width: 220px;
  height: 100%;
  border-right: 0.0625rem rgba(201, 197, 197, 0.82) solid;
}

/deep/.menu-list {
  a.is-active {
    background-color: orange;
    color: black;
  }
}

#home_right {
  width: 100%;
  height: 100%;
  min-width: 49.375rem;
  // background-color: green;
}

/* 底部 */
#home_footer {
  height: 50px;
  width: 100%;
  background-color: rgb(0, 0, 0);
  flex: 0 0 auto;
}

/deep/.modal-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 400px;

  .has-addons {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .modal-card-head {
    width: 400px;
  }

  .modal-card-body {
    .field {
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
      width: 400px;
    }

    padding-left: 0 !important;
    padding-right: 0 !important;
  }

  .modal-card-foot {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    width: 400px;
  }

  /deep/.file .is-primary .file-cta {
    background-color: black;
  }
}
</style>
