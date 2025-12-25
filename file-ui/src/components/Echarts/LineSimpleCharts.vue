<template>
  <div :class="className" :style="{ height, width }" ref="chartRef"></div>
</template>

<script>
// 引入 ECharts 核心模块
import * as echarts from 'echarts'
// 如果您需要主题，可以取消注释
// import 'echarts/theme/macarons';

export default {
  name: 'LineSimpleCharts',
  props: {
    className: {type: String, default: 'chart'},
    width: {type: String, default: '100%'},
    height: {type: String, default: '100%'},
    autoResize: {type: Boolean, default: true},

    // 核心图表数据，包含 names (x轴) 和 values (y轴)
    chartData: {
      type: Object,
      // 默认数据，用于在父组件未传入数据时显示
      default: () => ({
        names: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
        values: [75, 68, 82, 85, 90, 78, 72]
      })
    },
    chartTitle: {type: String, default: '简单折线图'},
    chartName: {type: String, default: '数据值'}
  },

  data() {
    return {
      chart: null // ECharts 实例
    }
  },

  watch: {
    // 深度侦听 chartData 对象的变化
    chartData: {
      deep: true,
      immediate: true,
      handler(val) {
        // 当数据变化时，重新设置 ECharts 配置项
        this.setOptions(val)
      }
    },
    // 侦听容器宽高的变化，用于响应式调整
    width() {
      this.$nextTick(() => this.resizeChart())
    },
    height() {
      this.$nextTick(() => this.resizeChart())
    }
  },

  mounted() {
    // 1. 初始化图表
    this.initChart()

    // 2. 如果开启了 autoResize，添加窗口缩放监听
    if (this.autoResize) {
      window.addEventListener('resize', this.resizeChart)
    }
  },

  beforeDestroy() {
    // 1. 销毁 ECharts 实例，释放资源
    if (this.chart) {
      this.chart.dispose()
      this.chart = null
    }

    // 2. 移除窗口缩放监听
    if (this.autoResize) {
      window.removeEventListener('resize', this.resizeChart)
    }
  },

  methods: {
    /**
     * 初始化图表实例
     */
    initChart() {
      // 使用 $nextTick 确保 DOM 已经渲染
      this.$nextTick(() => {
        // 检查 ref 是否存在
        if (!this.$refs.chartRef) return

        // 销毁可能存在的旧实例
        if (this.chart) {
          this.chart.dispose()
          this.chart = null
        }

        // 初始化 ECharts 实例
        // 可以在这里传入主题，例如：echarts.init(this.$refs.chartRef, 'macarons')
        this.chart = echarts.init(this.$refs.chartRef)

        // 设置初始配置项
        this.setOptions(this.chartData)
      })
    },

    /**
     * 设置 ECharts 配置项
     * @param {Object} data - 从 this.chartData 传入的数据
     */
    setOptions(data) {
      if (!this.chart) return
      if (!data || !data.names || !data.values) {
        console.warn('ECharts: 传入的 chartData 无效。')
        return
      }

      // 从传入的 data 中解构出 names 和 values
      const {names, values} = data

      const option = {
        title: {
          text: this.chartTitle,
          textStyle: {
            color: '#ccc'
          },
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          backgroundColor: 'rgba(6,37,68,0.9)',
          borderWidth: 0,
          textStyle: {
            color: '#FFF'
          },
          formatter: function (params) {
            let res = params[0].name + '<br/>'
            params.forEach(function (item) {
              // 在数值后面加上 '%'
              res += item.marker + item.seriesName + ': ' + item.value + '%' + '<br/>'
            })
            return res
          }
        },
        grid: {
          left: '5%',
          right: '5%',
          bottom: '12%',
          top: '18%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: names,       // 动态数据：X轴类目
          boundaryGap: false,
          axisTick: {show: false},
          axisLine: {lineStyle: {color: '#ccc'}},
          axisLabel: {color: '#ccc'},
          splitLine: {show: false}
        },
        yAxis: {
          type: 'value',
          min: 0,
          splitLine: {
            show: true,
            lineStyle: {
              color: '#4A4A4A',
              type: 'solid'
            }
          },
          axisTick: {show: false},
          axisLine: {lineStyle: {color: '#ccc'}},
          axisLabel: {color: '#ccc'}
        },
        dataZoom: [
          {
            type: 'slider', // 滑块型 dataZoom (拖动条)
            show: true,
            xAxisIndex: [0],
            start: 0,        // 初始显示范围起始
            end: 100,      // 初始显示范围结束
            height: 20,
            bottom: '2%',    // 放置在底部
            textStyle: {
              color: '#ccc'
            }
          },
          {
            type: 'inside', // 内置型 dataZoom (鼠标滚轮/触摸板操作)
            xAxisIndex: [0],
            start: 0,
            end: 100
          }
        ],
        series: [
          {
            name: this.chartName, // 系列名称
            type: 'line',
            data: values,       // 动态数据：Y轴数值
            smooth: true,
            lineStyle: {
              color: '#38F0B4',
              width: 3
            },
            showSymbol: true,
            symbol: 'circle',
            symbolSize: 8,
            itemStyle: {
              color: '#38F0B4',
              borderColor: '#fff',
              borderWidth: 2
            },
            areaStyle: {
              opacity: 0.8,
              // 注意：在方法中必须使用导入的 echarts 变量来创建渐变
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {offset: 0, color: '#00314F'},
                {offset: 0.8, color: '#01676C'}
              ])
            }
          }
        ]
      }

      // 应用配置项
      this.chart.setOption(option, true) // true 表示不合并，完全替换
    },

    /**
     * 响应式重绘图表
     */
    resizeChart() {
      if (this.chart) {
        this.chart.resize()
      }
    }
  }
}
</script>

<style scoped>
/* 确保容器有尺寸 */
.chart {
  overflow: hidden;
}
</style>
