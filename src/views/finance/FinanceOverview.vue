<template>
  <div class="page-container">
    <div class="page-header">
      <h1>财务概览</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>财务管理</el-breadcrumb-item>
        <el-breadcrumb-item>财务概览</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 财务统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card receivable">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ financeStats.totalReceivable.toLocaleString() }}</div>
              <div class="stat-label">应收款总额</div>
              <div class="stat-detail">
                <span class="stat-item">待收: {{ financeStats.pendingReceivable }}笔</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card payable">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Wallet /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ financeStats.totalPayable.toLocaleString() }}</div>
              <div class="stat-label">应付款总额</div>
              <div class="stat-detail">
                <span class="stat-item">待付: {{ financeStats.pendingPayable }}笔</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card profit">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ financeStats.totalProfit.toLocaleString() }}</div>
              <div class="stat-label">本月利润</div>
              <div class="stat-detail">
                <span class="stat-item positive">+15.2%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card cash">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><CreditCard /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ financeStats.cashFlow.toLocaleString() }}</div>
              <div class="stat-label">现金流</div>
              <div class="stat-detail">
                <span class="stat-item">本月净流入</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 应收款列表 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>应收款项</span>
          <el-button type="primary" size="small" @click="viewAllReceivables">
            查看全部
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
      </template>
      <el-table :data="receivableList" stripe>
        <el-table-column prop="orderNo" label="订单编号" width="150">
          <template #default="{ row }">
            <el-link type="primary">{{ row.orderNo }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户名称" min-width="180" />
        <el-table-column prop="orderAmount" label="订单金额" width="120">
          <template #default="{ row }">
            <span>¥{{ row.orderAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="receivedAmount" label="已收金额" width="120">
          <template #default="{ row }">
            <span class="received">¥{{ row.receivedAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pendingAmount" label="待收金额" width="120">
          <template #default="{ row }">
            <span class="pending">¥{{ row.pendingAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="到期日期" width="120">
          <template #default="{ row }">
            <span :class="getDueDateClass(row.dueDate)">{{ row.dueDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleReceive(row)">
              收款
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 应付款列表 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>应付款项</span>
          <el-button type="primary" size="small" @click="viewAllPayables">
            查看全部
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
      </template>
      <el-table :data="payableList" stripe>
        <el-table-column prop="purchaseNo" label="采购单号" width="150">
          <template #default="{ row }">
            <el-link type="primary">{{ row.purchaseNo }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="supplierName" label="供应商名称" min-width="180" />
        <el-table-column prop="purchaseAmount" label="采购金额" width="120">
          <template #default="{ row }">
            <span>¥{{ row.purchaseAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已付金额" width="120">
          <template #default="{ row }">
            <span class="paid">¥{{ row.paidAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="pendingAmount" label="待付金额" width="120">
          <template #default="{ row }">
            <span class="pending">¥{{ row.pendingAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="到期日期" width="120">
          <template #default="{ row }">
            <span :class="getDueDateClass(row.dueDate)">{{ row.dueDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="warning" size="small" @click="handlePay(row)">
              付款
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 财务图表 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="custom-card">
          <template #header>
            <span>月度收支趋势</span>
          </template>
          <div ref="incomeExpenseChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="custom-card">
          <template #header>
            <span>订单利润分析</span>
          </template>
          <div ref="profitChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

// 响应式数据
const financeStats = reactive({
  totalReceivable: 1280000,
  pendingReceivable: 15,
  totalPayable: 860000,
  pendingPayable: 12,
  totalProfit: 420000,
  cashFlow: 320000
})

// 应收款列表
const receivableList = ref([
  {
    id: 1,
    orderNo: 'SO202412001',
    customerName: 'ABC Trading Co.',
    orderAmount: 164000,
    receivedAmount: 80000,
    pendingAmount: 84000,
    dueDate: '2024-12-30'
  },
  {
    id: 2,
    orderNo: 'SO202412002',
    customerName: 'XYZ Import Ltd',
    orderAmount: 137500,
    receivedAmount: 0,
    pendingAmount: 137500,
    dueDate: '2025-01-10'
  },
  {
    id: 3,
    orderNo: 'SO202412003',
    customerName: 'Global Solutions',
    orderAmount: 42500,
    receivedAmount: 42500,
    pendingAmount: 0,
    dueDate: '2024-12-15'
  }
])

// 应付款列表
const payableList = ref([
  {
    id: 1,
    purchaseNo: 'PO202412001',
    supplierName: '深圳科技有限公司',
    purchaseAmount: 120000,
    paidAmount: 50000,
    pendingAmount: 70000,
    dueDate: '2024-12-28'
  },
  {
    id: 2,
    purchaseNo: 'PO202412002',
    supplierName: '东莞电子制造厂',
    purchaseAmount: 85000,
    paidAmount: 0,
    pendingAmount: 85000,
    dueDate: '2025-01-05'
  }
])

// 图表引用
const incomeExpenseChart = ref(null)
const profitChart = ref(null)

// 方法
const getDueDateClass = (dueDate) => {
  const today = new Date()
  const due = new Date(dueDate)
  const diffDays = Math.ceil((due - today) / (1000 * 60 * 60 * 24))
  
  if (diffDays < 0) return 'overdue'
  if (diffDays <= 7) return 'urgent'
  return 'normal'
}

const viewAllReceivables = () => {
  ElMessage.info('跳转到应收款管理页面')
}

const viewAllPayables = () => {
  ElMessage.info('跳转到应付款管理页面')
}

const handleReceive = (row) => {
  ElMessage.info(`处理收款：${row.orderNo}`)
}

const handlePay = (row) => {
  ElMessage.info(`处理付款：${row.purchaseNo}`)
}

// 初始化收支趋势图
const initIncomeExpenseChart = () => {
  if (!incomeExpenseChart.value) return
  
  const chart = echarts.init(incomeExpenseChart.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['收入', '支出', '净利润']
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    },
    yAxis: {
      type: 'value',
      name: '金额(万元)'
    },
    series: [
      {
        name: '收入',
        type: 'bar',
        data: [120, 150, 180, 160, 200, 220, 250, 280, 300, 320, 350, 380],
        itemStyle: { color: '#67C23A' }
      },
      {
        name: '支出',
        type: 'bar',
        data: [80, 90, 110, 100, 130, 140, 160, 170, 190, 200, 220, 240],
        itemStyle: { color: '#F56C6C' }
      },
      {
        name: '净利润',
        type: 'line',
        data: [40, 60, 70, 60, 70, 80, 90, 110, 110, 120, 130, 140],
        itemStyle: { color: '#409EFF' }
      }
    ]
  }
  
  chart.setOption(option)
}

// 初始化利润分析图
const initProfitChart = () => {
  if (!profitChart.value) return
  
  const chart = echarts.init(profitChart.value)
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '利润构成',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 35, name: '电子产品' },
          { value: 25, name: '配件类' },
          { value: 20, name: '耗材类' },
          { value: 15, name: '其他产品' },
          { value: 5, name: '服务费' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
}

onMounted(() => {
  nextTick(() => {
    initIncomeExpenseChart()
    initProfitChart()
  })
})
</script>

<style lang="scss" scoped>
.stats-row {
  margin-bottom: 20px;
  
  .stat-card {
    :deep(.el-card__body) {
      padding: 20px;
    }
    
    &.receivable {
      .stat-icon {
        background: linear-gradient(135deg, #67C23A 0%, #59B329 100%);
      }
    }
    
    &.payable {
      .stat-icon {
        background: linear-gradient(135deg, #F56C6C 0%, #E85555 100%);
      }
    }
    
    &.profit {
      .stat-icon {
        background: linear-gradient(135deg, #409EFF 0%, #2D8CF0 100%);
      }
    }
    
    &.cash {
      .stat-icon {
        background: linear-gradient(135deg, #E6A23C 0%, #D48806 100%);
      }
    }
    
    .stat-content {
      display: flex;
      align-items: center;
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
        color: white;
        margin-right: 15px;
      }
      
      .stat-info {
        flex: 1;
        
        .stat-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 5px;
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 5px;
        }
        
        .stat-detail {
          font-size: 12px;
          color: #909399;
          
          .stat-item {
            &.positive {
              color: #67C23A;
            }
            
            &.negative {
              color: #F56C6C;
            }
          }
        }
      }
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.received, .paid {
  color: #67C23A;
  font-weight: 600;
}

.pending {
  color: #E6A23C;
  font-weight: 600;
}

.normal {
  color: #606266;
}

.urgent {
  color: #E6A23C;
  font-weight: bold;
}

.overdue {
  color: #F56C6C;
  font-weight: bold;
}

.charts-row {
  margin-top: 20px;
}

// 响应式设计
@media (max-width: 768px) {
  .stats-row {
    .stat-card {
      margin-bottom: 10px;
      
      .stat-content {
        .stat-icon {
          width: 50px;
          height: 50px;
          font-size: 20px;
        }
        
        .stat-info .stat-value {
          font-size: 20px;
        }
      }
    }
  }
}
</style> 