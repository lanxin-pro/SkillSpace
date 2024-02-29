<template>
  <el-row :gutter="16">
    <el-col v-for="(item, index) in infoList" :key="index" class="ivu-mb" v-bind="grid">
      <el-card :padding="0" shadow="never">
        <p slot="header">
          <span style="color: #4d4d4f" v-text="item.title"></span>
          <el-tag style="float: right" type="primary">{{ item.date }}</el-tag>
        </p>
        <div>
          <div class="number">{{ item.today }}</div>
          <div class="ivu-pt-8" style="height: 42px;color: #4d4d4f">
            <span>昨日 {{ item.yesterday }}</span>
            <span class="ivu-mr">
              日环比 {{ Number(item.today_ratio) }}%
            <el-icon :class="Number(item.today_ratio) >= 0 ? ' ' : 'on'" class="iconColor">
              <component :is="Number(item.today_ratio) >= 0 ? 'caret-top' : 'caret-bottom'" />
            </el-icon>

              <i
                  :class="[
                  Number(item.today_ratio) >= 0 ? ' ' : 'on',
                  Number(item.today_ratio) >= 0 ? 'el-icon-caret-top' : 'el-icon-caret-bottom\n',
                ]"
                  class="iconColor"
              />
            </span>
          </div>
          <el-divider />
          <div class="total">
            <el-row>
              <el-col :span="12">{{ item.total_name }}</el-col>
              <el-col :span="12" class="ivu-text-right">{{ item.total }}</el-col>
            </el-row>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>
<script>

export default {
  data() {
    return {
      infoList: [{
        title: '销售额',
        date: '今日',
        yesterday: 0,
        today: 0,
        today_ratio: 20,
        total_name: '本月销售额',
        total: '401516.02元'
      },
        {
          title: '用户访问量',
          date: '今日',
          yesterday: 0,
          today: 0,
          today_ratio: -74.42,
          total_name: '本月访问量',
          total: '6349Pv'
        },
        {
          title: '订单量',
          date: '今日',
          yesterday: 0,
          today: 0,
          today_ratio: -100,
          total_name: '本月订单量',
          total: '58单'
        },
        {
          title: '新增用户',
          date: '今日',
          yesterday: 0,
          today: 0,
          today_ratio: -62.74,
          total_name: '本月新增用户',
          total: '747人'
        },
      ],
      grid: {
        xl: 6,
        lg: 6,
        md: 12,
        sm: 12,
        xs: 24,
      },
      excessStyle: {
        color: '#f56a00',
        backgroundColor: '#fde3cf',
      },
      avatarList: [],
    };
  },
  methods: {
    // 统计
    getStatistics() {

    },
  },
  mounted() {
    this.getStatistics();
  },
};
</script>
<style lang="scss" scoped>
.ivu-mb{
  margin-bottom: 14px;
}
.number {
  margin-bottom: 10px;
  font-size: 32px;
  font-weight: 400;
  color: #333333;
  line-height: 40px;
}

.iconColor {
  color: #F53F3F;
}

.iconColor.on {
  color: #0FC6C2;
}

.ivu-mr {
  display: inline-block;
  margin-left: 16px !important;
}

.ivu-text-right {
  text-align: right;
}
::v-deep .el-card__header {
  border-bottom: none !important;
  padding-bottom: 0;
}
.el-divider--horizontal {
  margin: 0 0 12px 0;
}
.total {
  font-size: 14px;
  font-weight: 400;
  color: #999999;
  line-height: 22px;
}
</style>
