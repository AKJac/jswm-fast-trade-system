<template>
  <div class="page-container">
    <div class="page-header">
      <h1>控制台</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>控制台</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ dashboardData.customerCount }}</div>
            <div class="stat-label">总客户数</div>
            <div class="stat-trend">
              <el-icon color="#67C23A"><TrendCharts /></el-icon>
              <span class="trend-text">+12%</span>
            </div>
          </div>
          <div class="stat-icon customer-icon">
            <el-icon><User /></el-icon>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ dashboardData.orderCount }}</div>
            <div class="stat-label">本月订单</div>
            <div class="stat-trend">
              <el-icon color="#67C23A"><TrendCharts /></el-icon>
              <span class="trend-text">+8%</span>
            </div>
          </div>
          <div class="stat-icon order-icon">
            <el-icon><Document /></el-icon>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">¥{{ dashboardData.salesAmount.toLocaleString() }}</div>
            <div class="stat-label">本月销售额</div>
            <div class="stat-trend">
              <el-icon color="#67C23A"><TrendCharts /></el-icon>
              <span class="trend-text">+15%</span>
            </div>
          </div>
          <div class="stat-icon sales-icon">
            <el-icon><Money /></el-icon>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ dashboardData.quotationCount }}</div>
            <div class="stat-label">待处理报价</div>
            <div class="stat-trend">
              <el-icon color="#F56C6C"><Warning /></el-icon>
              <span class="trend-text urgent">需关注</span>
            </div>
          </div>
          <div class="stat-icon quotation-icon">
            <el-icon><Tickets /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表和列表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 销售趋势图 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="16" :xl="16">
        <el-card class="custom-card">
          <template #header>
            <div class="card-header">
              <span>销售趋势</span>
              <el-button-group size="small">
                <el-button @click="setSalesDateRange('week')" :type="salesDateRange === 'week' ? 'primary' : ''">本周</el-button>
                <el-button @click="setSalesDateRange('month')" :type="salesDateRange === 'month' ? 'primary' : ''">本月</el-button>
                <el-button @click="setSalesDateRange('year')" :type="salesDateRange === 'year' ? 'primary' : ''">本年</el-button>
              </el-button-group>
            </div>
          </template>
          <div ref="salesChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      
      <!-- 快捷操作 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-card class="custom-card">
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="quick-actions">
            <div class="action-item" @click="quickAction('customer')">
              <el-icon class="action-icon"><UserFilled /></el-icon>
              <span>新增客户</span>
            </div>
            <div class="action-item" @click="quickAction('product')">
              <el-icon class="action-icon"><Box /></el-icon>
              <span>新增产品</span>
            </div>
            <div class="action-item" @click="quickAction('quotation')">
              <el-icon class="action-icon"><Money /></el-icon>
              <span>创建报价</span>
            </div>
            <div class="action-item" @click="quickAction('order')">
              <el-icon class="action-icon"><Document /></el-icon>
              <span>新增订单</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近动态和待办事项 -->
    <el-row :gutter="20" class="bottom-row">
      <!-- 最近订单 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="custom-card">
          <template #header>
            <div class="card-header">
              <span>最近订单</span>
              <el-button type="text" @click="$router.push('/order/sales')">查看更多</el-button>
            </div>
          </template>
          <el-table 
            :data="recentOrders" 
            stripe 
            style="width: 100%"
            :show-header="false"
            size="small"
          >
            <el-table-column prop="orderNo" label="订单号" width="120">
              <template #default="{ row }">
                <el-link type="primary">{{ row.orderNo }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="customerName" label="客户" />
            <el-table-column prop="amount" label="金额" width="100">
              <template #default="{ row }">
                <span class="amount">¥{{ row.amount.toLocaleString() }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)" size="small">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <!-- 待办任务 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="custom-card">
          <template #header>
            <div class="card-header">
              <span>待办任务</span>
              <el-badge :value="todoTasks.length" class="task-badge">
                <el-button type="text">全部</el-button>
              </el-badge>
            </div>
          </template>
          <div class="todo-list">
            <div 
              v-for="task in todoTasks" 
              :key="task.id" 
              class="todo-item"
              :class="{ 'urgent': task.urgent }"
            >
              <div class="task-content">
                <div class="task-title">{{ task.title }}</div>
                <div class="task-time">{{ task.dueDate }}</div>
              </div>
              <div class="task-priority">
                <el-tag 
                  :type="task.urgent ? 'danger' : 'info'" 
                  size="small"
                  effect="plain"
                >
                  {{ task.urgent ? '紧急' : '普通' }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'

const router = useRouter()

// 响应式数据
const salesChart = ref(null)
const salesDateRange = ref('month')

const dashboardData = reactive({
  customerCount: 1286,
  orderCount: 156,
  salesAmount: 2850000,
  quotationCount: 23
})

const recentOrders = ref([
  { orderNo: 'SO202412001', customerName: 'ABC Trading Co.', amount: 125000, status: '已确认' },
  { orderNo: 'SO202412002', customerName: 'XYZ Import Ltd', amount: 89000, status: '待发货' },
  { orderNo: 'SO202412003', customerName: 'Global Solutions', amount: 167000, status: '已发货' },
  { orderNo: 'SO202412004', customerName: 'Eastern Trading', amount: 95000, status: '待确认' },
  { orderNo: 'SO202412005', customerName: 'Pacific Imports', amount: 210000, status: '已完成' }
])

const todoTasks = ref([
  { id: 1, title: '完成ABC公司报价单', dueDate: '今日 15:00', urgent: true },
  { id: 2, title: '跟进XYZ公司付款事宜', dueDate: '明日 10:00', urgent: false },
  { id: 3, title: '安排样品寄送', dueDate: '12-28 14:00', urgent: true },
  { id: 4, title: '库存盘点', dueDate: '12-30 09:00', urgent: false },
  { id: 5, title: '月度销售总结', dueDate: '12-31 17:00', urgent: false }
])

// 方法
const setSalesDateRange = (range) => {
  salesDateRange.value = range
  updateSalesChart()
}

const getStatusType = (status) => {
  const statusMap = {
    '已确认': 'success',
    '待发货': 'warning',
    '已发货': 'primary',
    '待确认': 'info',
    '已完成': 'success'
  }
  return statusMap[status] || 'info'
}

const quickAction = (type) => {
  const routes = {
    customer: '/customer/list',
    product: '/product/list',
    quotation: '/quotation/list',
    order: '/order/sales'
  }
  router.push(routes[type])
}

const initSalesChart = () => {
  if (!salesChart.value) return
  
  const chart = echarts.init(salesChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    legend: {
      data: ['销售额', '订单数']
    },
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        boundaryGap: false,
        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
      }
    ],
    yAxis: [
      {
        type: 'value',
        name: '销售额(万元)',
        position: 'left',
        axisLabel: {
          formatter: '{value}'
        }
      },
      {
        type: 'value',
        name: '订单数',
        position: 'right',
        axisLabel: {
          formatter: '{value}'
        }
      }
    ],
    series: [
      {
        name: '销售额',
        type: 'line',
        smooth: true,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.8)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
          ])
        },
        data: [120, 132, 101, 134, 90, 230, 210, 182, 191, 234, 290, 330]
      },
      {
        name: '订单数',
        type: 'line',
        smooth: true,
        yAxisIndex: 1,
        itemStyle: {
          color: '#67C23A'
        },
        data: [45, 52, 38, 47, 35, 68, 62, 58, 61, 72, 85, 92]
      }
    ]
  }
  
  chart.setOption(option)
  
  // 响应式调整
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

const updateSalesChart = () => {
  // 根据时间范围更新图表数据
  // 这里可以调用API获取不同时间范围的数据
  nextTick(() => {
    initSalesChart()
  })
}

onMounted(() => {
  nextTick(() => {
    initSalesChart()
  })
})
</script>

<style lang="scss" scoped>
.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  position: relative;
  overflow: hidden;
  
  :deep(.el-card__body) {
    padding: 20px;
  }
  
  .stat-content {
    position: relative;
    z-index: 2;
    
    .stat-number {
      font-size: 32px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 8px;
    }
    
    .stat-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 8px;
    }
    
    .stat-trend {
      display: flex;
      align-items: center;
      gap: 4px;
      
      .trend-text {
        font-size: 12px;
        color: #67C23A;
        
        &.urgent {
          color: #F56C6C;
        }
      }
    }
  }
  
  .stat-icon {
    position: absolute;
    right: 20px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 48px;
    opacity: 0.1;
    z-index: 1;
    
    &.customer-icon {
      color: #409EFF;
    }
    
    &.order-icon {
      color: #67C23A;
    }
    
    &.sales-icon {
      color: #E6A23C;
    }
    
    &.quotation-icon {
      color: #F56C6C;
    }
  }
}

.charts-row {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
  
  .action-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    border: 1px dashed #dcdfe6;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover {
      border-color: #409eff;
      background-color: #f0f9ff;
      
      .action-icon {
        color: #409eff;
      }
    }
    
    .action-icon {
      font-size: 24px;
      color: #909399;
      margin-bottom: 8px;
      transition: color 0.3s;
    }
    
    span {
      font-size: 14px;
      color: #606266;
    }
  }
}

.bottom-row {
  .amount {
    font-weight: 600;
    color: #67C23A;
  }
}

.todo-list {
  .todo-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    &.urgent {
      .task-title {
        color: #F56C6C;
      }
    }
    
    .task-content {
      flex: 1;
      
      .task-title {
        font-size: 14px;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .task-time {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.task-badge {
  :deep(.el-badge__content) {
    background-color: #409eff;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .stats-row {
    .stat-card {
      margin-bottom: 15px;
      
      .stat-content {
        .stat-number {
          font-size: 24px;
        }
      }
      
      .stat-icon {
        font-size: 36px;
      }
    }
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
    
    .action-item {
      padding: 15px;
    }
  }
}
</style> 