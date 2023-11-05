<template>
  <div class="data-statistic">
    <div class="up-chart">
      <div class="chart1" ref="chart1"></div>
      <div class="chart2" ref="chart2"></div>
    </div>
    <div class="down-chart">
      <div class="chart3" ref="chart3"></div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      //图表1 折线图数据
      chart1Data: {
        //x轴
        xAxis: [],
        //数据
        yData: [],
      },
      //图表2  饼图数据
      chart2Data: [],
      //图表3  柱状图数据
      chart3Data: {
        //x轴
        xAxis: [],
        //数据
        yData: [],
      },
    }
  },
  methods: {
    getCart1Data() {
      console.log('data')
      console.log(this.chart1Data)
      console.log(this.chart2Data)
      console.log(this.chart3Data)
      const chart1 = this.$refs.chart1
      if (chart1) {
        const myChart = this.$echarts.init(chart1)
        const option = {
          title: {
            text: '近6个月发生灾情次数',
            left: 'center',
          },
          xAxis: {
            type: 'category',
            data: this.chart1Data.xAxis,
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: this.chart1Data.yData,
              type: 'line',
            },
          ],
        }
        myChart.setOption(option)
      }
    },
    getCart2Data() {
      const chart2 = this.$refs.chart2
      if (chart2) {
        const myChart = this.$echarts.init(chart2)
        const option = {
          title: {
            text: '灾情信息载体类型比例',
            left: 'center',
          },
          tooltip: {
            trigger: 'item',
          },
          legend: {
            orient: 'vertical',
            left: 'left',
          },
          series: [
            {
              name: 'Access From',
              type: 'pie',
              radius: '50%',
              data: this.chart2Data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        }
        myChart.setOption(option)
      }
    },

    getCart3Data() {
      const chart3 = this.$refs.chart3
      if (chart3) {
        const myChart = this.$echarts.init(chart3)
        const option = {
          title: {
            text: '部分省市灾情发生次数',

            left: 'center',
          },
          xAxis: {
            type: 'category',
            data: this.chart3Data.xAxis,
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: this.chart3Data.yData,
              type: 'bar',
            },
          ],
        }
        myChart.setOption(option)
      }
    },

    getStatistics() {
      this.$api.datalist
        .getStatistics()
        .then((res) => {
          console.log(res)
          //从返回数据中获取三个图表的数据
          const chart1Data = res.data.monthlyTimes
          const chart2Data = res.data.codeTypeTimes
          const chart3Data = res.data.provinceTimes
          const month = [
            '',
            '一月',
            '二月',
            '三月',
            '四月',
            '五月',
            '六月',
            '七月',
            '八月',
            '九月',
            '十月',
            '十一月',
            '十二月',
          ]
          let data2 = []
          //对三个图表的数据进行处理
          chart1Data.forEach((element) => {
            this.chart1Data.xAxis.push(month[element.month])
            this.chart1Data.yData.push(element.times)
          })
          this.chart1Data.xAxis = this.chart1Data.xAxis.reverse()
          this.chart1Data.yData = this.chart1Data.yData.reverse()
          chart2Data.forEach((element) => {
            const data = {
              value: element.times,
              name: element.codeType,
            }
            this.chart2Data.push(data)
          })

          chart3Data.forEach((element) => {
            this.chart3Data.xAxis.push(element.province)
            this.chart3Data.yData.push(element.times)
          })
          this.getCart1Data()
          this.getCart2Data()
          this.getCart3Data()
        })
        .catch((error) => {
          console.log(error)
        })
    },
  },
  created() {
    this.getStatistics()
  },
}
</script>

<style lang="less" scoped>
.data-statistic {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  .up-chart {
    width: 100%;
    height: 48%;
    display: flex;
    flex-direction: row;
    margin-top: 1%;
    .chart1 {
      width: 48%;
      height: 100%;
    }
    .chart2 {
      margin-left: 1%;
      width: 48%;
      height: 100%;
    }
  }
  .down-chart {
    height: 48%;
    width: 100%;
    .chart3 {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
