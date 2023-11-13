<template>
  <div id="dataList">
    <!-- 搜索框 -->
    <div id="dataList_header">
      <b-input
        placeholder="请输入搜索内容"
        type="is-warning"
        rounded
      ></b-input>
      <b-button id="search_button">搜索</b-button>
    </div>
    <!-- 表格 -->
    <div class="mid">
      <b-table
        :data="data"
        height="0"
        checkable
        detailed
        detail-key="id"
        @details-open="(row) => $buefy.toast.open(`展开ID为：${row.id}的信息`)"
        :default-sort-direction="'ASC'"
        :sort-icon="'arrow-up'"
        :sort-icon-size="'is-small'"
        default-sort="id"
      >
        <!-- ID -->
        <b-table-column
          field="id"
          label="ID"
          width="40"
          sortable
          numeric
          v-slot="props"
          @click="console.log(props)"
        >
          {{ props.row.id }}
        </b-table-column>
        <!-- 地区 -->
        <b-table-column
          field="location"
          label="地区"
          sortable
          v-slot="props"
        >
          {{
            `${props.row.location.province} ${props.row.location.city}
                    ${props.row.location.county} ${props.row.location.town} ${props.row.location.village}`
          }}
        </b-table-column>
        <!-- 发生时间 -->
        <b-table-column
          field="occurTime"
          label="发生时间"
          sortable
          v-slot="props"
        >
          {{ props.row.occurTime }}
        </b-table-column>
        <!-- 来源 -->
        <b-table-column
          field="sourceMain"
          label="来源"
          sortable
          v-slot="props"
        >
          {{ `${props.row.sourceSub} (${props.row.sourceMain})` }}
        </b-table-column>
        <!-- 类型 -->
        <b-table-column
          field="codeType"
          label="数据类型"
          v-slot="props"
        >
          <span
            :class="[
              'tag',
              { 'is-default': props.row.codeType == '文字' },
              { 'is-danger': props.row.codeType == '图像' },
              { 'is-success': props.row.codeType == '音频' },
              { 'is-info': props.row.codeType == '视频' },
              { 'is-warning': props.row.codeType == '其他' },
            ]"
          >
            {{ props.row.codeType }}
          </span>
        </b-table-column>
        <!-- 灾情分类 -->
        <b-table-column
          field="disaster_Type"
          label="灾情类型"
          v-slot="props"
        >
          {{
            `${props.row.disasterMain} > ${props.row.disasterSub} > ${props.row.disasterPoint}`
          }}
        </b-table-column>
        <!-- 操作 -->
        <b-table-column
          label="操作"
          v-slot="props"
        >
          <div style="display: flex; align-items: center; height: 2.5rem">
            <b-button
              id="edit"
              class="tag is-info"
              >编辑</b-button
            >
            <b-button
              id="delete"
              class="tag is-danger"
              @click="deleteLine(props.row.id)"
              >删除</b-button
            >
          </div>
        </b-table-column>
        <!-- 详细描述---------------如果可以，看看能不能解决打开一个就把上一个展开的详情收起 -->
        <template #detail="props">
          <article class="media">
            <img
              src="../../public/information.png"
              style="width: 40px; margin-right: 10px"
            />
            <div class="media-content">
              <div class="content">
                <p style="font-size: 1rem; margin: 0">
                  {{ props.row.description }}
                </p>
                <b-button
                  type="is-dark"
                  style="height: 22px; width: 40px"
                  v-if="props.row.codeType != '文字'"
                  @click="downloadfile(props.row.description)"
                  >下载</b-button
                >
              </div>
            </div>
          </article>
        </template>
      </b-table>
    </div>
    <!-- 分页页码 -->
    <div>
      <b-pagination
        :total="pageInfo.total"
        v-model="pageInfo.pageIndex"
        :range-before="'1'"
        :range-after="'1'"
        :order="'is-centered'"
        :size="'small'"
        :rounded="true"
        :per-page="pageInfo.pageSize"
        :icon-prev="'chevron-left'"
        :icon-next="'chevron-right'"
        aria-next-label="Next page"
        aria-previous-label="Previous page"
        aria-page-label="Page"
        aria-current-label="Current page"
        @change="getList"
      >
      </b-pagination>
    </div>
  </div>
</template>

<script>
export default {
  //测试数据
  data() {
    return {
      data: [],
      pageInfo: {
        pageIndex: 1,
        pageSize: 9,
        total: 14,
      },
    }
  },
  methods: {
    downloadfile(filename) {
      const FileName = {
        fileName: filename,
      }
      // console.log(FileName)
      this.$api.datalist
        .getDownloadUrl(FileName)
        .then((res) => {
          let url = res.data.tempUrl
          // console.log(url)
          const el = document.createElement('a')
          el.style.display = 'none'
          el.setAttribute('target', '_blank')
          filename && el.setAttribute('download', filename)
          el.href = url
          // console.log(el)
          document.body.appendChild(el)
          el.click()
          document.body.removeChild(el)
        })
        .catch((err) => {
          this.$buefy.toast.open({
            message: '下载失败',
            type: 'is-danger',
          })
        })
    },
    getList() {
      const params = {
        pageIndex: this.pageInfo.pageIndex,
        pageSize: this.pageInfo.pageSize,
      }
      this.$api.datalist
        .getInfo(params)
        .then((res) => {
          //console.log(res.data)
          this.data = res.data.content
          this.pageInfo.total = res.data.total
        })
        .catch((error) => {
          console.log(error)
        })
    },

    deleteLine(id) {
      //console.log(id)
      this.$api.datalist
        .deleteLine(id)
        .then((res) => {
          if (res.code == 0) {
            this.$buefy.toast.open({
              message: '删除成功',
              type: 'is-success',
            })
            this.getList()
          }
          //console.log(res)
        })
        .catch((error) => {
          this.$buefy.toast.open({
            message: '删除失败',
            type: 'is-warning',
          })
          console.log(error)
        })
    },
  },
  created() {
    this.getList()
    //console.log('bind', this.$bus)
    this.$bus.$on('getDataList', () => {
      this.getList()
    })
  },
  beforeDestroy() {
    this.$bus.$off('getDataList')
  },
}
</script>

<style lang="less" scoped>
#dataList {
  height: 100%;
  display: flex;
  flex-direction: column;
  // justify-content: center;
  align-content: center;
}

/deep/.b-table {
  height: 100%;
  overflow: auto;

  .table-wrapper {
    //font-size: 0.625rem;
    font-size: 10px;
  }

  .table {
    .checkbox-cell {
      vertical-align: middle;
    }
  }

  tr {
    height: 2.5rem;
  }

  td {
    height: 2.5rem;
    line-height: 2.5rem;
  }
}

#edit {
  margin-right: 0.1875rem;
}

/deep/.level {
  display: none;
}

.mid {
  flex: auto;
  border-top: 1px grey solid;
  overflow: hidden;

  ::-webkit-scrollbar {
    display: none;
  }
}

#dataList_header {
  display: flex;
  flex-direction: row;
  align-items: center;
  height: 1.875rem;
  width: 21.875rem;
  margin-left: 0.625rem;
  margin-top: 0.1875rem;
  margin-bottom: 0.1875rem;
}

/deep/.input.is-rounded {
  height: 1.875rem;
  width: 18.75rem;
  padding-right: 3.4375rem;
  border: 0.0625rem black solid;
}

/deep/.input.is-rounded:focus {
  box-shadow: 0 0 0.125rem orange;
}

/deep/#search_button.button {
  height: 1.8125rem;
  width: 3.125rem;
  background-color: #ffc966;
  color: #996300;
  transform: translateX(-3.125rem);
  border-radius: 0 0.9063rem 0.9063rem 0;
}

/deep/.pagination-link.is-current {
  background-color: rgba(255, 189, 67, 0.879);
  color: black;
  border-color: antiquewhite;
}

/deep/.b-table .table .chevron-cell > a {
  color: black !important;
}
/deep/.media {
  align-items: center !important;
}
.content {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
</style>
