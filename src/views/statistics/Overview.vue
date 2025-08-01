<template>
  <div class="page-container">
    <div class="page-header">
      <h1>数据统计</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>数据统计</el-breadcrumb-item>
        <el-breadcrumb-item>统计概览</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 时间筛选 -->
    <el-card class="filter-card">
      <div class="filter-row">
        <div class="filter-item">
          <span class="filter-label">统计时间：</span>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :shortcuts="dateShortcuts"
            @change="handleDateChange"
          />
        </div>
        <div class="filter-item">
          <span class="filter-label">统计维度：</span>
          <el-select v-model="statDimension" @change="handleDimensionChange">
            <el-option label="按日统计" value="day" />
            <el-option label="按月统计" value="month" />
            <el-option label="按年统计" value="year" />
          </el-select>
        </div>
        <el-button type="primary" @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
      </div>
    </el-card>

    <!-- 核心指标 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon sales">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatCurrency(overviewData.totalSales) }}</div>
              <div class="stat-label">总销售额</div>
              <div class="stat-trend" :class="overviewData.salesTrend >= 0 ? 'up' : 'down'">
                <el-icon><CaretTop v-if="overviewData.salesTrend >= 0" /><CaretBottom v-else /></el-icon>
                {{ Math.abs(overviewData.salesTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon orders">
              <el-icon><ShoppingCart /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ overviewData.totalOrders }}</div>
              <div class="stat-label">订单数量</div>
              <div class="stat-trend" :class="overviewData.ordersTrend >= 0 ? 'up' : 'down'">
                <el-icon><CaretTop v-if="overviewData.ordersTrend >= 0" /><CaretBottom v-else /></el-icon>
                {{ Math.abs(overviewData.ordersTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon customers">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ overviewData.totalCustomers }}</div>
              <div class="stat-label">客户数量</div>
              <div class="stat-trend" :class="overviewData.customersTrend >= 0 ? 'up' : 'down'">
                <el-icon><CaretTop v-if="overviewData.customersTrend >= 0" /><CaretBottom v-else /></el-icon>
                {{ Math.abs(overviewData.customersTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon profit">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatCurrency(overviewData.totalProfit) }}</div>
              <div class="stat-label">总利润</div>
              <div class="stat-trend" :class="overviewData.profitTrend >= 0 ? 'up' : 'down'">
                <el-icon><CaretTop v-if="overviewData.profitTrend >= 0" /><CaretBottom v-else /></el-icon>
                {{ Math.abs(overviewData.profitTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 销售趋势图 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>销售趋势</span>
              <el-radio-group v-model="salesChartType" size="small">
                <el-radio-button label="line">折线图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-container">
            <div ref="salesChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>

      <!-- 产品销量排行 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>产品销量排行</span>
              <el-select v-model="productRankType" size="small" style="width: 120px">
                <el-option label="按销量" value="quantity" />
                <el-option label="按金额" value="amount" />
              </el-select>
            </div>
          </template>
          <div class="chart-container">
            <div ref="productChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 客户分析 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 客户地域分布 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>客户地域分布</span>
            </div>
          </template>
          <div class="chart-container">
            <div ref="regionChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>

      <!-- 客户类型分析 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>客户类型分析</span>
            </div>
          </template>
          <div class="chart-container">
            <div ref="customerTypeChart" class="chart"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细数据表格 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>详细数据</span>
          <div class="header-actions">
            <el-button type="primary" size="small" @click="exportData">
              <el-icon><Download /></el-icon>
              导出数据
            </el-button>
          </div>
        </div>
      </template>

      <el-tabs v-model="activeTab" type="card">
        <el-tab-pane label="销售明细" name="sales">
          <el-table :data="salesData" stripe>
            <el-table-column prop="date" label="日期" width="120" />
            <el-table-column prop="orderCount" label="订单数" width="100" />
            <el-table-column prop="salesAmount" label="销售额" width="120">
              <template #default="{ row }">
                {{ formatCurrency(row.salesAmount) }}
              </template>
            </el-table-column>
            <el-table-column prop="profit" label="利润" width="120">
              <template #default="{ row }">
                {{ formatCurrency(row.profit) }}
              </template>
            </el-table-column>
            <el-table-column prop="customerCount" label="客户数" width="100" />
            <el-table-column prop="avgOrderValue" label="平均订单金额" width="150">
              <template #default="{ row }">
                {{ formatCurrency(row.avgOrderValue) }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="产品分析" name="products">
          <el-table :data="productData" stripe>
            <el-table-column prop="productName" label="产品名称" min-width="200" />
            <el-table-column prop="category" label="分类" width="120" />
            <el-table-column prop="salesQuantity" label="销量" width="100" />
            <el-table-column prop="salesAmount" label="销售额" width="120">
              <template #default="{ row }">
                {{ formatCurrency(row.salesAmount) }}
              </template>
            </el-table-column>
            <el-table-column prop="profit" label="利润" width="120">
              <template #default="{ row }">
                {{ formatCurrency(row.profit) }}
              </template>
            </el-table-column>
            <el-table-column prop="profitRate" label="利润率" width="100">
              <template #default="{ row }">
                {{ row.profitRate }}%
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="客户分析" name="customers">
          <el-table :data="customerData" stripe>
            <el-table-column prop="customerName" label="客户名称" min-width="200" />
            <el-table-column prop="region" label="地区" width="120" />
            <el-table-column prop="customerType" label="客户类型" width="120" />
            <el-table-column prop="orderCount" label="订单数" width="100" />
            <el-table-column prop="totalAmount" label="总金额" width="120">
              <template #default="{ row }">
                {{ formatCurrency(row.totalAmount) }}
              </template>
            </el-table-column>
            <el-table-column prop="lastOrderDate" label="最近订单" width="120" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

// 响应式数据
const dateRange = ref([])
const statDimension = ref('month')
const salesChartType = ref('line')
const productRankType = ref('quantity')
const activeTab = ref('sales')

// 日期快捷选项
const dateShortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    }
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    }
  }
]

// 概览数据
const overviewData = reactive({
  totalSales: 1250000,
  salesTrend: 12.5,
  totalOrders: 156,
  ordersTrend: 8.3,
  totalCustomers: 89,
  customersTrend: 15.2,
  totalProfit: 280000,
  profitTrend: 18.7
})

// 图表实例
let salesChart = null
let productChart = null
let regionChart = null
let customerTypeChart = null

// 模拟数据
const salesData = ref([
  {
    date: '2024-12-01',
    orderCount: 12,
    salesAmount: 85000,
    profit: 18000,
    customerCount: 8,
    avgOrderValue: 7083
  },
  {
    date: '2024-12-02',
    orderCount: 15,
    salesAmount: 92000,
    profit: 19500,
    customerCount: 10,
    avgOrderValue: 6133
  },
  {
    date: '2024-12-03',
    orderCount: 18,
    salesAmount: 110000,
    profit: 23500,
    customerCount: 12,
    avgOrderValue: 6111
  }
])

const productData = ref([
  {
    productName: 'LED显示屏',
    category: '电子产品',
    salesQuantity: 45,
    salesAmount: 225000,
    profit: 67500,
    profitRate: 30
  },
  {
    productName: '无线充电器',
    category: '配件',
    salesQuantity: 120,
    salesAmount: 180000,
    profit: 54000,
    profitRate: 30
  },
  {
    productName: '蓝牙耳机',
    category: '音频设备',
    salesQuantity: 89,
    salesAmount: 178000,
    profit: 53400,
    profitRate: 30
  }
])

const customerData = ref([
  {
    customerName: 'ABC贸易公司',
    region: '华东',
    customerType: '批发商',
    orderCount: 15,
    totalAmount: 280000,
    lastOrderDate: '2024-12-20'
  },
  {
    customerName: 'XYZ进出口',
    region: '华南',
    customerType: '贸易商',
    orderCount: 12,
    totalAmount: 220000,
    lastOrderDate: '2024-12-18'
  },
  {
    customerName: 'DEF电子',
    region: '华北',
    customerType: '制造商',
    orderCount: 8,
    totalAmount: 150000,
    lastOrderDate: '2024-12-15'
  }
])

// 方法
const formatCurrency = (amount) => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  }).format(amount)
}

const handleDateChange = () => {
  refreshData()
}

const handleDimensionChange = () => {
  refreshData()
}

const refreshData = () => {
  ElMessage.success('数据已刷新')
  initCharts()
}

const exportData = () => {
  ElMessage.success('数据导出功能开发中...')
}

// 初始化图表
const initCharts = async () => {
  await nextTick()
  
  // 销售趋势图
  if (salesChart) {
    salesChart.dispose()
  }
  salesChart = echarts.init(document.querySelector('.chart'))
  
  const salesOption = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['销售额', '订单数']
    },
    xAxis: {
      type: 'category',
      data: ['12-01', '12-02', '12-03', '12-04', '12-05', '12-06', '12-07']
    },
    yAxis: [
      {
        type: 'value',
        name: '销售额',
        position: 'left'
      },
      {
        type: 'value',
        name: '订单数',
        position: 'right'
      }
    ],
    series: [
      {
        name: '销售额',
        type: salesChartType.value,
        data: [85000, 92000, 110000, 95000, 120000, 135000, 140000],
        yAxisIndex: 0
      },
      {
        name: '订单数',
        type: salesChartType.value,
        data: [12, 15, 18, 14, 20, 22, 25],
        yAxisIndex: 1
      }
    ]
  }
  
  salesChart.setOption(salesOption)
  
  // 产品销量排行
  if (productChart) {
    productChart.dispose()
  }
  productChart = echarts.init(document.querySelectorAll('.chart')[1])
  
  const productOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'value'
    },
    yAxis: {
      type: 'category',
      data: ['LED显示屏', '无线充电器', '蓝牙耳机', '智能手表', '平板电脑']
    },
    series: [
      {
        name: productRankType.value === 'quantity' ? '销量' : '销售额',
        type: 'bar',
        data: [45, 120, 89, 67, 56]
      }
    ]
  }
  
  productChart.setOption(productOption)
  
  // 客户地域分布
  if (regionChart) {
    regionChart.dispose()
  }
  regionChart = echarts.init(document.querySelectorAll('.chart')[2])
  
  const regionOption = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '客户分布',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 35, name: '华东' },
          { value: 25, name: '华南' },
          { value: 20, name: '华北' },
          { value: 15, name: '华中' },
          { value: 5, name: '其他' }
        ]
      }
    ]
  }
  
  regionChart.setOption(regionOption)
  
  // 客户类型分析
  if (customerTypeChart) {
    customerTypeChart.dispose()
  }
  customerTypeChart = echarts.init(document.querySelectorAll('.chart')[3])
  
  const customerTypeOption = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '客户类型',
        type: 'doughnut',
        radius: ['40%', '70%'],
        data: [
          { value: 40, name: '批发商' },
          { value: 30, name: '贸易商' },
          { value: 20, name: '制造商' },
          { value: 10, name: '零售商' }
        ]
      }
    ]
  }
  
  customerTypeChart.setOption(customerTypeOption)
}

// 监听图表类型变化
const watchChartType = () => {
  if (salesChart) {
    initCharts()
  }
}

// 监听窗口大小变化
const handleResize = () => {
  if (salesChart) salesChart.resize()
  if (productChart) productChart.resize()
  if (regionChart) regionChart.resize()
  if (customerTypeChart) customerTypeChart.resize()
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
})
</script>

<style lang="scss" scoped>
.filter-card {
  margin-bottom: 20px;
  
  .filter-row {
    display: flex;
    gap: 20px;
    align-items: center;
    flex-wrap: wrap;
    
    .filter-item {
      display: flex;
      align-items: center;
      gap: 10px;
      
      .filter-label {
        font-weight: 500;
        color: #606266;
      }
    }
  }
}

.stats-row {
  margin-bottom: 20px;
  
  .stat-card {
    :deep(.el-card__body) {
      padding: 20px;
    }
    
    .stat-item {
      display: flex;
      align-items: center;
      
      .stat-icon {
        width: 50px;
        height: 50px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
        color: white;
        margin-right: 15px;
        
        &.sales {
          background: linear-gradient(135deg, #409EFF 0%, #2D8CF0 100%);
        }
        
        &.orders {
          background: linear-gradient(135deg, #67C23A 0%, #59B329 100%);
        }
        
        &.customers {
          background: linear-gradient(135deg, #E6A23C 0%, #D48806 100%);
        }
        
        &.profit {
          background: linear-gradient(135deg, #F56C6C 0%, #E74C3C 100%);
        }
      }
      
      .stat-content {
        flex: 1;
        
        .stat-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          line-height: 1.2;
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 5px;
        }
        
        .stat-trend {
          font-size: 12px;
          margin-top: 5px;
          display: flex;
          align-items: center;
          gap: 2px;
          
          &.up {
            color: #67C23A;
          }
          
          &.down {
            color: #F56C6C;
          }
        }
      }
    }
  }
}

.charts-row {
  margin-bottom: 20px;
  
  .chart-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .chart-container {
      height: 300px;
      
      .chart {
        width: 100%;
        height: 100%;
      }
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .header-actions {
    display: flex;
    gap: 10px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .filter-row {
    flex-direction: column;
    align-items: stretch;
    
    .filter-item {
      justify-content: space-between;
    }
  }
  
  .chart-container {
    height: 250px !important;
  }
}
</style> 