<template>
  <div class="home">

    <!-- 数据块-->
    <el-row :gutter="15" class="home-card-one">
      <el-col
          :xs="24"
          :sm="12"
          :md="12"
          :lg="6"
          :xl="6"
          v-for="(v, k) in recommendList"
          :key="k"
          :class="{ 'home-media home-media-lg': k > 1, 'home-media-sm': k === 1 }"
      >
        <div class="home-card-item flex">
          <div class="flex-margin flex w100" :class="` home-one-animation${k}`">
            <div class="flex-auto">
              <span class="font30">{{ v.num1 }}</span>
              <span class="ml5 font16" :style="{ color: v.color1 }">{{ v.num2 }}%</span>
              <div class="mt10">{{ v.num3 }}</div>
            </div>
            <div class="home-card-item-icon flex" :style="{ 'background-color': v.color2}">
              <i class="flex-margin font32" :class="v.num4" :style="{ color: v.color3 }"></i>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- charts -->
    <el-row :gutter="15" class="mt15">
      <el-col :md="24" :lg="8" :xl="8" class="mb15">
        <el-card shadow="hover">
          <div slot="header">
            <span>库存作业</span>
          </div>
          <div class="charts">
            <div class="charts-right">
              <div ref="homeStockRef" class="h100"></div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :md="24" :lg="16" :xl="16" class="mb15">
        <el-card shadow="hover">
          <div slot="header">
            <span>履约情况</span>
          </div>
          <div class="charts">
            <div class="charts-left mr7">
              <div ref="homeLaboratoryRef" class="h100"></div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 履约超时预警 -->
    <el-row :gutter="15">
      <el-col :md="24" :lg="16" :xl="16" class="home-lg">
        <el-card shadow="hover">
          <div slot="header">
            <span>缺货监控</span>
          </div>
          <div class="charts">
            <div class="charts-left mr7">
              <div ref="homeOvertimeRef" class="h100"></div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :md="24" :lg="8" :xl="8">
        <el-card shadow="hover">
          <div slot="header">
            <span>履约超时预警</span>
          </div>
          <div class="home-charts">
            <div class="home-charts-item" v-for="(v, k) in chartsRightList" :key="k">
              <div class="home-charts-item-left">
                <div class="home-charts-item-title">{{ v.title }}</div>
                <div class="home-charts-item-num" :style="{ color: v.color }" :id="`titleNum${k + 1}`"></div>
              </div>
              <div class="home-charts-item-right">
                <i :class="v.icon" :style="{ 'background-color': v.iconBg, color: v.color }"></i>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import Scroll from 'vue-seamless-scroll';
import {CountUp} from 'countup.js';
import {Session} from '@/utils/storage';
import {formatAxis, formatDate} from '@/utils/style/formatTime';
import {recommendList, chartsRightList, newsInfoList} from './mock';

export default {
  name: 'home',
  components: {Scroll},
  data() {
    return {
      recommendList,
      chartsRightList,
      newsInfoList,
      userInfo: {},
      charts: {
        theme: '',
        bgColor: '',
      },
      global: {
        homeChartOne: null,
        homeChartTwo: null,
        homeCharThree: null,
        dispose: [null, '', undefined],
      },
    };
  },
  created() {
    this.initUserInfo();
  },
  computed: {
    currentTime() {
      return formatAxis(new Date());
    },
    optionSingleHeight() {
      return {
        singleHeight: 28,
        limitMoveNum: 8,
        waitTime: 2000,
      };
    }
  },
  mounted() {
    this.initHomeStock();
    this.initHomeLaboratory();
    this.initHomeOvertime();
    this.initNumCountUp();
  },
  methods: {
    // 初始化数字滚动
    initNumCountUp() {
      this.$nextTick(() => {
        new CountUp('titleNum1', Math.random() * 100000).start();
        new CountUp('titleNum2', Math.random() * 100000).start();
        new CountUp('titleNum3', Math.random() * 100000).start();
      });
    },
    // 库存作业
    initHomeStock() {
      if (!this.global.dispose.some((b) => b === this.global.homeChartOne)) this.global.homeChartOne.dispose();
      this.global.homeChartOne = echarts.init(this.$refs.homeStockRef, this.charts.theme);
      const option = {
        backgroundColor: this.charts.bgColor,
        grid: {
          top: 50,
          right: 20,
          bottom: 30,
          left: 30,
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          left: 'center',
        },
        series: [
          {
            name: '库存作业',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
            },
            data: [
              {value: 580, name: '邮件营销'},
              {value: 300, name: '视频广告'},
              {value: 230, name: '联盟广告'},
            ],
            top: 30,
          },
        ],
      };
      this.global.homeChartOne.setOption(option);
      window.addEventListener('resize', () => {
        this.global.homeChartOne.resize();
      });
    },
    // 履约情况
    initHomeLaboratory() {
      if (!this.global.dispose.some((b) => b === this.global.homeChartTwo)) this.global.homeChartTwo.dispose();
      this.global.homeChartTwo = echarts.init(this.$refs.homeLaboratoryRef, this.charts.theme);
      const option = {
        backgroundColor: this.charts.bgColor,
        grid: {
          top: 50,
          right: 20,
          bottom: 30,
          left: 30,
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['预购队列', '最新成交价'],
          right: 13,
        },
        xAxis: {
          data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子'],
        },
        yAxis: [
          {
            type: 'value',
            name: '价格',
          },
        ],
        series: [
          {
            name: '预购队列',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20],
          },
          {
            name: '最新成交价',
            type: 'line',
            data: [15, 20, 16, 20, 30, 8],
          },
        ],
      };
      this.global.homeChartTwo.setOption(option);
      window.addEventListener('resize', () => {
        this.global.homeChartTwo.resize();
      });
    },
    // 缺货监控
    initHomeOvertime() {
      if (!this.global.dispose.some((b) => b === this.global.homeCharThree)) this.global.homeCharThree.dispose();
      this.global.homeCharThree = echarts.init(this.$refs.homeOvertimeRef, this.charts.theme);
      const option = {
        backgroundColor: this.charts.bgColor,
        grid: {
          top: 50,
          right: 20,
          bottom: 30,
          left: 30,
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['订单数量', '超时数量', '在线数量', '预警数量'],
          right: 13,
        },
        xAxis: {
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
        },
        yAxis: [
          {
            type: 'value',
            name: '数量',
          },
        ],
        series: [
          {
            name: '订单数量',
            type: 'bar',
            data: [5, 20, 36, 10, 10, 20, 11, 13, 10, 9, 17, 19],
          },
          {
            name: '超时数量',
            type: 'bar',
            data: [15, 12, 26, 15, 11, 16, 31, 13, 5, 16, 13, 15],
          },
          {
            name: '在线数量',
            type: 'line',
            data: [15, 20, 16, 20, 30, 8, 16, 19, 12, 18, 19, 14],
          },
          {
            name: '预警数量',
            type: 'line',
            data: [10, 10, 13, 12, 15, 18, 19, 10, 12, 15, 11, 17],
          },
        ],
      };
      this.global.homeCharThree.setOption(option);
      window.addEventListener('resize', () => {
        this.global.homeCharThree.resize();
      });
    },
    // 初始化登录信息
    initUserInfo() {
      if (!Session.get('userInfo')) return false;
      this.userInfo = Session.get('userInfo');
      this.userInfo.time = formatDate(new Date(this.userInfo.time), 'YYYY-mm-dd HH:MM:SS');
    },
    // 消息通知当前项点击
    onNewsInfoListClick(v) {
      window.open(v.link);
    },
    // 跳转到 gitee
    onOpenGitee() {
      window.open('https://gitee.com/lyt-top/vue-next-admin');
    },
  },
  watch: {
    // 监听 vuex 数据变化
    '$store.state.themeConfig.themeConfig.isIsDark': {
      handler(isIsDark) {
        this.$nextTick(() => {
          this.charts.theme = isIsDark ? 'dark' : '';
          this.charts.bgColor = isIsDark ? 'transparent' : '';
          setTimeout(() => {
            this.initHomeStock();
          }, 500);
          setTimeout(() => {
            this.initHomeLaboratory();
          }, 700);
          setTimeout(() => {
            this.initHomeOvertime();
          }, 1000);
        });
      },
      deep: true,
      immediate: true,
    },
  },
};
</script>

<style scoped lang="scss">
@import './index.scss';
</style>
