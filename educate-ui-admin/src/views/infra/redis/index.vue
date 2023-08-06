<template>
  <div class="app-container">
    <!-- 基本信息 -->
    <el-row>
      <el-col :span="24" class="card-box" shadow="hover">
        <el-card>
          <el-descriptions title="基本信息" :column="6" border>
            <el-descriptions-item label="Redis版本 :">
              {{ cache?.info?.redis_version }}
            </el-descriptions-item>
            <el-descriptions-item label="运行模式 :">
              {{ cache?.info?.redis_mode == 'standalone' ? '单机' : '集群' }}
            </el-descriptions-item>
            <el-descriptions-item label="端口 :">
              {{ cache?.info?.tcp_port }}
            </el-descriptions-item>
            <el-descriptions-item label="客户端数 :">
              {{ cache?.info?.connected_clients }}
            </el-descriptions-item>
            <el-descriptions-item label="运行时间(天) :">
              {{ cache?.info?.uptime_in_days }}
            </el-descriptions-item>
            <el-descriptions-item label="使用内存 :">
              {{ cache?.info?.used_memory_human }}
            </el-descriptions-item>
            <el-descriptions-item label="使用CPU :">
              {{ cache?.info ? parseFloat(cache?.info?.used_cpu_user_children).toFixed(2) : '' }}
            </el-descriptions-item>
            <el-descriptions-item label="内存配置 :">
              {{ cache?.info?.maxmemory_human }}
            </el-descriptions-item>
            <el-descriptions-item label="AOF是否开启 :">
              {{ cache?.info?.aof_enabled == '0' ? '否' : '是' }}
            </el-descriptions-item>
            <el-descriptions-item label="RDB是否成功 :">
              {{ cache?.info?.rdb_last_bgsave_status }}
            </el-descriptions-item>
            <el-descriptions-item label="Key数量 :">
              {{ cache?.dbSize }}
            </el-descriptions-item>
            <el-descriptions-item label="网络入口/出口 :">
              {{ cache?.info?.instantaneous_input_kbps }}kps/
              {{ cache?.info?.instantaneous_output_kbps }}kps
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <!-- 命令统计 -->
      <el-col :span="12" class="mt-3">
        <el-card :gutter="12" shadow="hover">
          <Echart :options="commandStatsRefChika" :height="420" />
        </el-card>
      </el-col>
      <!-- 内存使用量统计 -->
      <el-col :span="12" class="mt-3">
        <el-card class="ml-3" :gutter="12" shadow="hover">
          <Echart :options="usedmemoryEchartChika" :height="420" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import Echart from '@/components/Echart/index.vue'
import { ref,reactive,onMounted } from 'vue'
import { getCache } from '@/api/infra/redis'
const cache = ref()

/** 指令使用情况 */
const commandStatsRefChika = reactive({
  title: {
    text: '命令统计',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    type: 'scroll',
    orient: 'vertical',
    right: 30,
    top: 10,
    bottom: 20,
    data: [],
    textStyle: {
      color: '#a1a1a1'
    }
  },
  series: [
    {
      name: '命令',
      type: 'pie',
      radius: [20, 120],
      center: ['40%', '60%'],
      data: [],
      roseType: 'radius',
      label: {
        show: true
      },
      emphasis: {
        label: {
          show: true
        },
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
})

/** 内存使用情况 */
const usedmemoryEchartChika = reactive({
  title: {
    // 仪表盘标题。
    text: '内存使用情况',
    left: 'center',
    // 是否显示标题,默认 true。
    show: true,
    //相对于仪表盘中心的偏移位置，数组第一项是水平方向的偏移，第二项是垂直方向的偏移。可以是绝对的数值，也可以是相对于仪表盘半径的百分比。
    offsetCenter: [0, '20%'],
    // 文字的颜色,默认 #333。
    color: 'yellow',
    // 文字的字体大小,默认 15。
    fontSize: 20
  },
  toolbox: {
    show: false,
    feature: {
      restore: { show: true },
      saveAsImage: { show: true }
    }
  },
  series: [
    {
      name: '峰值',
      type: 'gauge',
      min: 0,
      max: 50,
      splitNumber: 10,
      // 这是指针的颜色
      color: '#F5C74E',
      radius: '85%',
      center: ['50%', '50%'],
      startAngle: 225,
      endAngle: -45,
      axisLine: {
        // 坐标轴线
        lineStyle: {
          // 属性lineStyle控制线条样式
          color: [
            [0.2, '#7FFF00'],
            [0.8, '#00FFFF'],
            [1, '#FF0000']
          ],
          //width: 6 外框的大小（环的宽度）
          width: 10
        }
      },
      axisTick: {
        // 坐标轴小标记
        //里面的线长是5（短线）
        length: 5, // 属性length控制线长
        lineStyle: {
          // 属性lineStyle控制线条样式
          color: '#76D9D7'
        }
      },
      splitLine: {
        // 分隔线
        length: 20, // 属性length控制线长
        lineStyle: {
          // 属性lineStyle（详见lineStyle）控制线条样式
          color: '#76D9D7'
        }
      },
      axisLabel: {
        color: '#76D9D7',
        distance: 15,
        fontSize: 15
      },
      pointer: {
        // 指针的大小
        width: 7,
        show: true
      },
      detail: {
        textStyle: {
          fontWeight: 'normal',
          // 里面文字下的数值大小（50）
          fontSize: 15,
          color: '#FFFFFF'
        },
        valueAnimation: true
      },
      progress: {
        show: true
      }
    }
  ]
})

/** 加载数据 */
const getSummary = () => {
  // 初始化命令图表
  initCommandStatsChart()
  usedMemoryInstance()
}
/** 命令使用情况 */
const initCommandStatsChart = async () => {
  usedmemoryEchartChika.series[0].data = []
  // 发起请求
  try {
    const response = await getCache()
    cache.value = response.data
    // 处理数据
    const commandStats = []
    const nameList = []
    response.data.commandStats.forEach((row) => {
      commandStats.push({
        name: row.command,
        value: row.calls
      })
      nameList.push(row.command)
    })
    commandStatsRefChika.legend.data = nameList
    commandStatsRefChika.series[0].data = commandStats
  } catch {}
}
const usedMemoryInstance = async () => {
  try {
    const response = await getCache()
    cache.value = response.data
    // 仪表盘详情，用于显示数据。
    usedmemoryEchartChika.series[0].detail = {
      // 是否显示详情,默认 true。
      show: true,
      // 相对于仪表盘中心的偏移位置，数组第一项是水平方向的偏移，第二项是垂直方向的偏移。可以是绝对的数值，也可以是相对于仪表盘半径的百分比。
      offsetCenter: [0, '50%'],
      // 文字的颜色,默认 auto。
      color: 'auto',
      // 文字的字体大小,默认 15。
      fontSize: 30,
      // 格式化函数或者字符串
      formatter: cache.value.info.used_memory_human
  }

    usedmemoryEchartChika.series[0].data[0] = {
      value: cache.value.info.used_memory_human,
      name: '内存消耗'
  }
    usedmemoryEchartChika.tooltip = {
      formatter: '{b} <br/>{a} : ' + cache.value.info.used_memory_human
  }
  } catch {}
}

// 基本信息
const readRedisInfo = async () => {
  const response = await getCache()
  cache.value = response.data
}

/** 初始化 **/
onMounted(() => {
  // 读取 redis 信息
  readRedisInfo()
  // 加载数据
  getSummary()
})
</script>

<style scoped>

</style>
