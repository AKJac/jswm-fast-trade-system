<template>
  <div class="page-container">
    <div class="page-header">
      <h1>订单管理</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>订单管理</el-breadcrumb-item>
        <el-breadcrumb-item>销售订单</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索栏 -->
    <el-card class="custom-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="订单编号">
          <el-input 
            v-model="searchForm.orderNo" 
            placeholder="请输入订单编号" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input 
            v-model="searchForm.customerName" 
            placeholder="请输入客户名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="订单日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable
            style="width: 150px"
          >
            <el-option label="待审核" value="pending" />
            <el-option label="生产中" value="producing" />
            <el-option label="待发货" value="ready" />
            <el-option label="已发货" value="shipped" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计信息 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value">{{ orderStats.total }}</div>
            <div class="stat-label">订单总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value pending">{{ orderStats.pending }}</div>
            <div class="stat-label">待处理</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value producing">{{ orderStats.producing }}</div>
            <div class="stat-label">生产中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value amount">¥{{ orderStats.totalAmount.toLocaleString() }}</div>
            <div class="stat-label">订单总额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 操作栏 -->
    <el-card class="custom-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新建订单
          </el-button>
          <el-button type="success" @click="handleImport">
            <el-icon><Upload /></el-icon>
            批量导入
          </el-button>
          <el-button type="warning" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出订单
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 订单列表 -->
    <el-card class="custom-card">
      <el-table 
        :data="orderList" 
        stripe 
        class="custom-table"
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderNo" label="订单编号" width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">
              {{ row.orderNo }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户名称" min-width="180" />
        <el-table-column prop="contact" label="联系人" width="100" />
        <el-table-column label="订单产品" min-width="200">
          <template #default="{ row }">
            <div class="product-list">
              <div v-for="(product, index) in row.products.slice(0, 2)" :key="index" class="product-item">
                {{ product.name }} x{{ product.quantity }}
              </div>
              <div v-if="row.products.length > 2" class="more-products">
                +{{ row.products.length - 2 }}个产品
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template #default="{ row }">
            <span class="amount-text">{{ row.currency }} {{ row.totalAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderDate" label="订单日期" width="120" />
        <el-table-column prop="deliveryDate" label="交货日期" width="120">
          <template #default="{ row }">
            <span :class="getDeliveryClass(row.deliveryDate, row.status)">
              {{ row.deliveryDate }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="付款状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getPaymentType(row.paymentStatus)" size="small">
              {{ getPaymentText(row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-dropdown trigger="click" @command="(command) => handleCommand(command, row)">
              <el-button size="small">
                更多
                <el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="approve" :disabled="row.status !== 'pending'">
                    <el-icon><Check /></el-icon>
                    审核通过
                  </el-dropdown-item>
                  <el-dropdown-item command="purchase">
                    <el-icon><ShoppingCart /></el-icon>
                    生成采购单
                  </el-dropdown-item>
                  <el-dropdown-item command="ship" :disabled="row.status !== 'ready'">
                    <el-icon><Van /></el-icon>
                    安排出运
                  </el-dropdown-item>
                  <el-dropdown-item command="print">
                    <el-icon><Printer /></el-icon>
                    打印订单
                  </el-dropdown-item>
                  <el-dropdown-item command="cancel" divided :disabled="['shipped', 'completed', 'cancelled'].includes(row.status)">
                    <el-icon><Close /></el-icon>
                    取消订单
                  </el-dropdown-item>
                  <el-dropdown-item command="delete" :disabled="row.status !== 'cancelled'">
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const loading = ref(false)

const searchForm = reactive({
  orderNo: '',
  customerName: '',
  dateRange: null,
  status: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const orderList = ref([])
const selectedOrders = ref([])

const orderStats = reactive({
  total: 45,
  pending: 8,
  producing: 12,
  totalAmount: 2890000
})

// 模拟订单数据
const mockOrders = [
  {
    id: 1,
    orderNo: 'SO202412001',
    customerName: 'ABC Trading Co.',
    contact: 'John Smith',
    products: [
      { name: '智能蓝牙耳机', quantity: 500, price: 280, amount: 140000 },
      { name: '无线充电器', quantity: 200, price: 120, amount: 24000 }
    ],
    totalAmount: 164000,
    currency: '¥',
    orderDate: '2024-12-15',
    deliveryDate: '2025-01-15',
    status: 'producing',
    paymentStatus: 'partial',
    salesperson: '张三',
    remarks: '客户要求1月15日前交货'
  },
  {
    id: 2,
    orderNo: 'SO202412002',
    customerName: 'XYZ Import Ltd',
    contact: 'Sarah Johnson',
    products: [
      { name: '便携式音箱', quantity: 300, price: 185, amount: 55500 },
      { name: '智能手环', quantity: 150, price: 380, amount: 57000 },
      { name: 'USB数据线', quantity: 1000, price: 25, amount: 25000 }
    ],
    totalAmount: 137500,
    currency: '¥',
    orderDate: '2024-12-18',
    deliveryDate: '2025-01-20',
    status: 'pending',
    paymentStatus: 'unpaid',
    salesperson: '李四',
    remarks: '新客户首单，需要审核'
  },
  {
    id: 3,
    orderNo: 'SO202412003',
    customerName: 'Global Solutions',
    contact: 'Michael Brown',
    products: [
      { name: '平板电脑保护套', quantity: 500, price: 85, amount: 42500 }
    ],
    totalAmount: 42500,
    currency: '¥',
    orderDate: '2024-12-10',
    deliveryDate: '2024-12-25',
    status: 'shipped',
    paymentStatus: 'paid',
    salesperson: '王五',
    remarks: '已发货，正在运输中'
  },
  {
    id: 4,
    orderNo: 'SO202412004',
    customerName: 'Eastern Trading',
    contact: 'Yuki Tanaka',
    products: [
      { name: '智能蓝牙耳机', quantity: 1000, price: 275, amount: 275000 },
      { name: '便携式音箱', quantity: 500, price: 180, amount: 90000 }
    ],
    totalAmount: 365000,
    currency: '¥',
    orderDate: '2024-12-08',
    deliveryDate: '2024-12-22',
    status: 'completed',
    paymentStatus: 'paid',
    salesperson: '赵六',
    remarks: '老客户，已完成交易'
  }
]

// 方法
const getStatusType = (status) => {
  const typeMap = {
    'pending': 'warning',
    'producing': 'primary',
    'ready': 'success',
    'shipped': 'info',
    'completed': 'success',
    'cancelled': 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    'pending': '待审核',
    'producing': '生产中',
    'ready': '待发货',
    'shipped': '已发货',
    'completed': '已完成',
    'cancelled': '已取消'
  }
  return textMap[status] || status
}

const getPaymentType = (status) => {
  const typeMap = {
    'unpaid': 'danger',
    'partial': 'warning',
    'paid': 'success'
  }
  return typeMap[status] || 'info'
}

const getPaymentText = (status) => {
  const textMap = {
    'unpaid': '未付款',
    'partial': '部分付款',
    'paid': '已付款'
  }
  return textMap[status] || status
}

const getDeliveryClass = (deliveryDate, status) => {
  if (['completed', 'cancelled'].includes(status)) return 'delivery-normal'
  
  const today = new Date()
  const delivery = new Date(deliveryDate)
  const diffDays = Math.ceil((delivery - today) / (1000 * 60 * 60 * 24))
  
  if (diffDays < 0) return 'delivery-overdue'
  if (diffDays <= 7) return 'delivery-urgent'
  return 'delivery-normal'
}

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loadOrders()
    loading.value = false
  }, 500)
}

const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = key === 'dateRange' ? null : ''
  })
  handleSearch()
}

const handleAdd = () => {
  ElMessage.info('新建订单功能开发中...')
}

const handleImport = () => {
  ElMessage.info('批量导入功能开发中...')
}

const handleExport = () => {
  ElMessage.info('正在导出订单数据...')
}

const handleEdit = (order) => {
  ElMessage.info(`编辑订单：${order.orderNo}`)
}

const handleView = (order) => {
  ElMessage.info(`查看订单详情：${order.orderNo}`)
}

const handleSelectionChange = (selection) => {
  selectedOrders.value = selection
}

const handleCommand = (command, order) => {
  switch (command) {
    case 'approve':
      ElMessage.success(`订单 ${order.orderNo} 审核通过`)
      order.status = 'producing'
      break
    case 'purchase':
      ElMessage.info(`生成采购单：${order.orderNo}`)
      break
    case 'ship':
      ElMessage.success(`订单 ${order.orderNo} 已安排出运`)
      order.status = 'shipped'
      break
    case 'print':
      ElMessage.info(`打印订单：${order.orderNo}`)
      break
    case 'cancel':
      handleCancel(order)
      break
    case 'delete':
      handleDelete(order)
      break
  }
}

const handleCancel = (order) => {
  ElMessageBox.confirm(
    `确定要取消订单 "${order.orderNo}" 吗？`,
    '确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    order.status = 'cancelled'
    ElMessage.success('订单已取消')
  })
}

const handleDelete = (order) => {
  ElMessageBox.confirm(
    `确定要删除订单 "${order.orderNo}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = orderList.value.findIndex(item => item.id === order.id)
    if (index > -1) {
      orderList.value.splice(index, 1)
      pagination.total--
      ElMessage.success('删除成功')
    }
  })
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadOrders()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadOrders()
}

const loadOrders = () => {
  orderList.value = [...mockOrders]
  pagination.total = mockOrders.length
}

onMounted(() => {
  loadOrders()
})
</script>

<style lang="scss" scoped>
.stats-row {
  margin-bottom: 20px;
  
  .stat-card {
    :deep(.el-card__body) {
      padding: 16px;
    }
    
    .stat-item {
      text-align: center;
      
      .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 5px;
        
        &.pending {
          color: #E6A23C;
        }
        
        &.producing {
          color: #409EFF;
        }
        
        &.amount {
          color: #67C23A;
        }
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.search-form {
  .el-form-item {
    margin-bottom: 0;
  }
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .toolbar-left {
    display: flex;
    gap: 10px;
  }
}

.product-list {
  .product-item {
    font-size: 13px;
    color: #606266;
    margin-bottom: 2px;
  }
  
  .more-products {
    font-size: 12px;
    color: #909399;
    font-style: italic;
  }
}

.amount-text {
  font-weight: 600;
  color: #67C23A;
}

.delivery-normal {
  color: #606266;
}

.delivery-urgent {
  color: #E6A23C;
  font-weight: bold;
}

.delivery-overdue {
  color: #F56C6C;
  font-weight: bold;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

// 响应式设计
@media (max-width: 768px) {
  .stats-row {
    .stat-card {
      margin-bottom: 10px;
      
      .stat-item .stat-value {
        font-size: 20px;
      }
    }
  }
  
  .toolbar {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
    
    .toolbar-left {
      justify-content: center;
      flex-wrap: wrap;
    }
  }
  
  .search-form {
    .el-form-item {
      margin-bottom: 15px;
      display: block;
      
      .el-input, .el-select, .el-date-picker {
        width: 100% !important;
      }
    }
  }
}
</style> 