<template>
  <div>
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }" @click="navBill(0)">
        <chart-card :loading="loading" title="本月总应收账单（元）" :total="totalFee | NumFormat">
          <template slot="footer">
            <span>总已收款<span> ￥{{ receive | NumFormat }} </span></span>
            <span style="float:right;">总未收款<span> ￥{{ unReceive | NumFormat }} </span></span>
          </template>
        </chart-card>
      </a-col>

      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }" @click="navBill(0)">
        <chart-card :loading="loading" title="物业管理费应收账单（元）" :total="billManagementData.totalFee | NumFormat">
          <template slot="footer">
            <span>已收款<span> ￥{{ billManagementData.receive | NumFormat }} </span></span>
            <span style="float:right;">未收款<span> ￥{{ billManagementData.unReceive | NumFormat }} </span></span>
          </template>
        </chart-card>
      </a-col>

      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }" @click="navBill(0)">
        <chart-card :loading="loading" title="租金应收账单（元）" :total="billRentData.totalFee | NumFormat">
          <template slot="footer">
            <span>已收款<span> ￥{{ billRentData.receive | NumFormat }} </span></span>
            <span style="float:right;">未收款<span> ￥{{ billRentData.unReceive | NumFormat }} </span></span>
          </template>
        </chart-card>
      </a-col>

      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }" @click="navBill(0)">
        <chart-card :loading="loading" title="水电费应收账单（元）" :total="billPwData.totalFee | NumFormat">
          <template slot="footer">
            <span>已收款<span> ￥{{ billPwData.receive | NumFormat }} </span></span>
            <span style="float:right;">未收款<span> ￥{{ billPwData.unReceive | NumFormat }} </span></span>
          </template>
        </chart-card>
      </a-col>

      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }" @click="navBill(3)">
        <chart-card :loading="loading" title="销售总额（元）" :total="saleData.total | NumFormat">
          <template slot="footer">
            <span>总已支付<span> ￥{{ saleData.payTotal | NumFormat }} </span></span>
            <span style="float:right;">总未支付<span> ￥{{ saleData.noPayTotal | NumFormat }} </span></span>
          </template>
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <div class="extra-item">
              <a-radio-group :defaultValue="value1" button-style="solid" @change="timeChange">
                <a-radio-button value="1">
                  本月
                </a-radio-button>
                <a-radio-button value="0">
                  本年
                </a-radio-button>
              </a-radio-group>
            </div>
          </div>
          <a-tab-pane loading="true" tab="应收款" key="1">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar :data="barData" :scale="barDatascale"/>
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="企业应收账单TOP10排行榜" :list="rankList"/>
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="已收款" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar :data="barData2" :scale="barData2scale" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <rank-list title="企业应收帐单排行榜" :list="rankList"/>
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="未收款" key="3">
            <a-row>
              <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
                <bar :data="barData3" :scale="barData3scale" />
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <div class="antd-pro-pages-dashboard-analysis-twoColLayout" :class="!isMobile && 'desktop'" hidden="true">
      <a-row :gutter="24" type="flex" :style="{ marginTop: '24px' }">
        <a-col :xl="12" :lg="24" :md="24" :sm="24" :xs="24">
          <a-card :loading="loading" :bordered="false" title="线上热门搜索" :style="{ height: '100%' }">
            <a-dropdown :trigger="['click']" placement="bottomLeft" slot="extra">
              <a class="ant-dropdown-link" href="#">
                <a-icon type="ellipsis" />
              </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a href="javascript:;">操作一</a>
                </a-menu-item>
                <a-menu-item>
                  <a href="javascript:;">操作二</a>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
            <a-row :gutter="68">
              <a-col :xs="24" :sm="12" :style="{ marginBottom: ' 24px'}">
                <number-info :total="12321" :sub-total="17.1">
                  <span slot="subtitle">
                    <span>搜索用户数</span>
                    <a-tooltip title="指标说明" slot="action">
                      <a-icon type="info-circle-o" :style="{ marginLeft: '8px' }" />
                    </a-tooltip>
                  </span>
                </number-info>
                <!-- miniChart -->
                <div>
                  <mini-smooth-area :style="{ height: '45px' }" :dataSource="searchUserData" :scale="searchUserScale" />
                </div>
              </a-col>
              <a-col :xs="24" :sm="12" :style="{ marginBottom: ' 24px'}">
                <number-info :total="2.7" :sub-total="26.2" status="down">
                  <span slot="subtitle">
                    <span>人均搜索次数</span>
                    <a-tooltip title="指标说明" slot="action">
                      <a-icon type="info-circle-o" :style="{ marginLeft: '8px' }" />
                    </a-tooltip>
                  </span>
                </number-info>
                <!-- miniChart -->
                <div>
                  <mini-smooth-area :style="{ height: '45px' }" :dataSource="searchUserData" :scale="searchUserScale" />
                </div>
              </a-col>
            </a-row>
            <div class="ant-table-wrapper">
              <a-table
                row-key="index"
                size="small"
                :columns="searchTableColumns"
                :dataSource="searchData"
                :pagination="{ pageSize: 5 }"
              >
                <span slot="range" slot-scope="text, record">
                  <trend :flag="record.status === 0 ? 'up' : 'down'">
                    {{ text }}%
                  </trend>
                </span>
              </a-table>
            </div>
          </a-card>
        </a-col>
        <a-col :xl="12" :lg="24" :md="24" :sm="24" :xs="24">
          <a-card class="antd-pro-pages-dashboard-analysis-salesCard" :loading="loading" :bordered="false" title="销售额类别占比" :style="{ height: '100%' }">
            <div slot="extra" style="height: inherit;">
              <span class="dashboard-analysis-iconGroup">
                <a-dropdown :trigger="['click']" placement="bottomLeft">
                  <a-icon type="ellipsis" class="ant-dropdown-link" />
                  <a-menu slot="overlay">
                    <a-menu-item>
                      <a href="javascript:;">操作一</a>
                    </a-menu-item>
                    <a-menu-item>
                      <a href="javascript:;">操作二</a>
                    </a-menu-item>
                  </a-menu>
                </a-dropdown>
              </span>
              <div class="analysis-salesTypeRadio">
                <a-radio-group defaultValue="a">
                  <a-radio-button value="a">全部渠道</a-radio-button>
                  <a-radio-button value="b">线上</a-radio-button>
                  <a-radio-button value="c">门店</a-radio-button>
                </a-radio-group>
              </div>

            </div>
            <h4>销售额</h4>
            <div>
              <!-- style="width: calc(100% - 240px);" -->
              <div>
                <v-chart :force-fit="true" :height="405" :data="pieData" :scale="pieScale">
                  <v-tooltip :showTitle="false" dataKey="item*percent" />
                  <v-axis />
                  <!-- position="right" :offsetX="-140" -->
                  <v-legend dataKey="item"/>
                  <v-pie position="percent" color="item" :vStyle="pieStyle" />
                  <v-coord type="theta" :radius="0.75" :innerRadius="0.6" />
                </v-chart>
              </div>

            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import {
  getBillData,
  getBillYearData,
  getRankList,
  getBillRentData,
  getBillManagementData,
  getBillPwData,
  getCountSaleData
} from '@/api/dashboard/analysis'
import {
  ChartCard,
  MiniArea,
  MiniBar,
  MiniProgress,
  RankList,
  Bar,
  Trend,
  NumberInfo,
  MiniSmoothArea
} from '@/components'
import { baseMixin } from '@/store/app-mixin'
const searchUserData = []
for (let i = 0; i < 7; i++) {
  searchUserData.push({
    x: moment().add(i, 'days').format('YYYY-MM-DD'),
    y: Math.ceil(Math.random() * 10)
  })
}
const searchUserScale = [
  {
    dataKey: 'x',
    alias: '时间'
  },
  {
    dataKey: 'y',
    alias: '用户数',
    min: 0,
    max: 10
  }]

const searchTableColumns = [
  {
    dataIndex: 'index',
    title: '排名',
    width: 90
  },
  {
    dataIndex: 'keyword',
    title: '搜索关键词'
  },
  {
    dataIndex: 'count',
    title: '用户数'
  },
  {
    dataIndex: 'range',
    title: '周涨幅',
    align: 'right',
    sorter: (a, b) => a.range - b.range,
    scopedSlots: { customRender: 'range' }
  }
]
const searchData = []
for (let i = 0; i < 50; i += 1) {
  searchData.push({
    index: i + 1,
    keyword: `搜索关键词-${i}`,
    count: Math.floor(Math.random() * 1000),
    range: Math.floor(Math.random() * 100),
    status: Math.floor((Math.random() * 10) % 2)
  })
}

const DataSet = require('@antv/data-set')

const sourceData = [
  { item: '家用电器', count: 32.2 },
  { item: '食用酒水', count: 21 },
  { item: '个护健康', count: 17 },
  { item: '服饰箱包', count: 13 },
  { item: '母婴产品', count: 9 },
  { item: '其他', count: 7.8 }
]

const pieScale = [{
  dataKey: 'percent',
  min: 0,
  formatter: '.0%'
}]

const dv = new DataSet.View().source(sourceData)
dv.transform({
  type: 'percent',
  field: 'count',
  dimension: 'item',
  as: 'percent'
})
const pieData = dv.rows

export default {
  Date: '',
  name: 'Analysis',
  mixins: [baseMixin],
  components: {
    ChartCard,
    MiniArea,
    MiniBar,
    MiniProgress,
    RankList,
    Bar,
    Trend,
    NumberInfo,
    MiniSmoothArea
  },
  data () {
    return {
      doHandleDate: '',
      value1: '1',
      countRentArea: {},
      billsScale: [{
        dataKey: 'x',
        alias: '时间'
      },
      {
        dataKey: 'y',
        alias: '逾期数量'
      }],
      barDatascale: [{
        dataKey: 'x',
        alias: '时间'
      },
      {
        dataKey: 'y',
        alias: '应收'
      }],
      barData2scale: [{
        dataKey: 'x',
        alias: '时间'
      },
      {
        dataKey: 'y',
        alias: '已收'
      }],
      barData3scale: [{
        dataKey: 'x',
        alias: '时间'
      },
      {
        dataKey: 'y',
        alias: '未收'
      }],
      scale: [{
        dataKey: 'x',
        alias: '时间'
      },
      {
        dataKey: 'y',
        alias: '待审批数量'
      }],
      loading: true,
      rankList: [],

      // 搜索用户数
      searchUserData,
      searchUserScale,
      searchTableColumns,
      searchData,

      barData: [],
      barData2: [],
      barData3: [],

      //
      pieScale,
      pieData,
      sourceData,
      pieStyle: {
        stroke: '#fff',
        lineWidth: 1
      }
    }
  },
  filters: {
    NumFormat: function (value) {
      if (!value) return '0.00'
      value = value.toFixed(2)
      var intPart = Math.trunc(value)// 获取整数部分
      var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,') // 将整数部分逢三一断
      var floatPart = '.00' // 预定义小数部分
      var value2Array = value.split('.')
      // =2表示数据有小数位
      if (value2Array.length === 2) {
        floatPart = value2Array[1].toString() // 拿到小数部分
        if (floatPart.length === 1) { // 补0,实际上用不着
          return intPartFormat + '.' + floatPart + '0'
        } else {
          return intPartFormat + '.' + floatPart
        }
      } else {
        return intPartFormat + floatPart
      }
    }
  },
  created () {
    this.Date = this.Date()
    getBillRentData().then(res => { if (res.code === 0) this.billRentData = res })
    getBillManagementData().then(res => { if (res.code === 0) this.billManagementData = res })
    getBillPwData().then(res => { if (res.code === 0) this.billPwData = res })
    getCountSaleData().then(res => { if (res.code === 0) this.saleData = res })
    getBillData().then(res => {
      if (res.code === 0) {
        this.receive = res.receive
        this.totalFee = res.totalFee
        this.unReceive = res.unReceive
      }
    })
    getBillYearData().then(res => {
      if (res.code === 0) {
        this.barData = res.totalList
        this.barData2 = res.receiveList
        this.barData3 = res.unReceiveList
      }
    })
    this.getlist()
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
  },
  methods: {
    Date () {
      var myDate = new Date()
      var tYear = myDate.getFullYear()
      var tMonth = myDate.getMonth()
      var m = tMonth + 1
      if (m.toString().length === 1) {
        m = '0' + m
      }
      return tYear + '-' + m
    },
    timeChange (e) {
      this.loadingChange = true
      this.value1 = e.target.value
      this.getlist(e.target.value)
    },
    getlist (dateType) {
      getRankList({ dateType }).then(res => {
        if (res.code === 0) {
          this.rankList = res.data
        }
      })
    },
    navBill (e) {
      if (e === 0) {
        this.$router.push({ name: 'bill', query: { Date: this.Date } })
      } else if (e === 2) {
        this.$router.push({ name: 'bill', query: { isOverdue: '1' } })
      } else if (e === 3) {
        this.$router.push({ name: 'orderManage' })
      }
    }
  }
}
</script>

<style lang="less" scoped>
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
      display: inline-block;
      margin-right: 24px;

      a {
        margin-left: 24px;
      }
    }
  }

  .antd-pro-pages-dashboard-analysis-twoColLayout {
    position: relative;
    display: flex;
    display: block;
    flex-flow: row wrap;
  }

  .antd-pro-pages-dashboard-analysis-salesCard {
    height: calc(100% - 24px);
    /deep/ .ant-card-head {
      position: relative;
    }
  }

  .dashboard-analysis-iconGroup {
    i {
      margin-left: 16px;
      color: rgba(0,0,0,.45);
      cursor: pointer;
      transition: color .32s;
      color: black;
    }
  }
  .analysis-salesTypeRadio {
    position: absolute;
    right: 54px;
    bottom: 12px;
  }
</style>
