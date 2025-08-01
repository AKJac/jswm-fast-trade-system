<template>
  <div class="page-container">
    <div class="page-header">
      <h1>采购管理</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>采购管理</el-breadcrumb-item>
        <el-breadcrumb-item>采购订单</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索栏 -->
    <el-card class="custom-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="采购单号">
          <el-input 
            v-model="searchForm.purchaseNo" 
            placeholder="请输入采购单号" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="供应商">
          <el-input 
            v-model="searchForm.supplierName" 
            placeholder="请输入供应商名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="采购日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="采购状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable
            style="width: 150px"
          >
            <el-option label="待审核" value="pending" />
            <el-option label="已审核" value="approved" />
            <el-option label="采购中" value="purchasing" />
            <el-option label="已入库" value="received" />
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
            <div class="stat-value">{{ purchaseStats.total }}</div>
            <div class="stat-label">采购单总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value pending">{{ purchaseStats.pending }}</div>
            <div class="stat-label">待审核</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value purchasing">{{ purchaseStats.purchasing }}</div>
            <div class="stat-label">采购中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value amount">¥{{ purchaseStats.totalAmount.toLocaleString() }}</div>
            <div class="stat-label">采购总额</div>
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
            新建采购单
          </el-button>
          <el-button type="success" @click="handleBatchApprove" :disabled="selectedPurchases.length === 0">
            <el-icon><Check /></el-icon>
            批量审核 ({{ selectedPurchases.length }})
          </el-button>
          <el-button type="warning" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出采购单
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 采购单列表 -->
    <el-card class="custom-card">
      <el-table 
        :data="purchaseList" 
        stripe 
        class="custom-table"
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="purchaseNo" label="采购单号" width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">
              {{ row.purchaseNo }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="relatedOrderNo" label="关联订单" width="150">
          <template #default="{ row }">
            <el-link type="info" v-if="row.relatedOrderNo">
              {{ row.relatedOrderNo }}
            </el-link>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="supplierName" label="供应商" min-width="180" />
        <el-table-column label="采购产品" min-width="200">
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
        <el-table-column prop="totalAmount" label="采购金额" width="120">
          <template #default="{ row }">
            <span class="amount-text">¥{{ row.totalAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="purchaseDate" label="采购日期" width="120" />
        <el-table-column prop="expectedDate" label="预计到货" width="120">
          <template #default="{ row }">
            <span :class="getExpectedDateClass(row.expectedDate, row.status)">
              {{ row.expectedDate }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="采购状态" width="100">
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
        <el-table-column label="操作" width="200" fixed="right">
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
                  <el-dropdown-item command="receive" :disabled="row.status !== 'purchasing'">
                    <el-icon><Finished /></el-icon>
                    确认收货
                  </el-dropdown-item>
                  <el-dropdown-item command="pay" :disabled="row.paymentStatus === 'paid'">
                    <el-icon><Money /></el-icon>
                    付款
                  </el-dropdown-item>
                  <el-dropdown-item command="print">
                    <el-icon><Printer /></el-icon>
                    打印采购单
                  </el-dropdown-item>
                  <el-dropdown-item command="cancel" divided :disabled="['received', 'completed', 'cancelled'].includes(row.status)">
                    <el-icon><Close /></el-icon>
                    取消采购
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const loading = ref(false)

const searchForm = reactive({
  purchaseNo: '',
  supplierName: '',
  dateRange: null,
  status: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const purchaseList = ref([])
const selectedPurchases = ref([])

const purchaseStats = reactive({
  total: 35,
  pending: 5,
  purchasing: 8,
  totalAmount: 1560000
})

// 模拟采购数据
const mockPurchases = [
  {
    id: 1,
    purchaseNo: 'PO202412001',
    relatedOrderNo: 'SO202412001',
    supplierName: '深圳科技有限公司',
    products: [
      { name: '智能蓝牙耳机', quantity: 500, price: 200, amount: 100000 },
      { name: '无线充电器', quantity: 200, price: 80, amount: 16000 }
    ],
    totalAmount: 116000,
    purchaseDate: '2024-12-16',
    expectedDate: '2024-12-30',
    status: 'purchasing',
    paymentStatus: 'partial',
    purchaser: '赵六',
    remarks: '订单SO202412001的采购需求'
  },
  {
    id: 2,
    purchaseNo: 'PO202412002',
    relatedOrderNo: 'SO202412002',
    supplierName: '东莞电子制造厂',
    products: [
      { name: '便携式音箱', quantity: 300, price: 120, amount: 36000 },
      { name: '智能手环', quantity: 150, price: 280, amount: 42000 },
      { name: 'USB数据线', quantity: 1000, price: 15, amount: 15000 }
    ],
    totalAmount: 93000,
    purchaseDate: '2024-12-19',
    expectedDate: '2025-01-05',
    status: 'pending',
    paymentStatus: 'unpaid',
    purchaser: '周七',
    remarks: '新订单采购'
  },
  {
    id: 3,
    purchaseNo: 'PO202412003',
    relatedOrderNo: null,
    supplierName: '广州配件供应商',
    products: [
      { name: '充电线', quantity: 2000, price: 12, amount: 24000 },
      { name: '包装盒', quantity: 1000, price: 5, amount: 5000 }
    ],
    totalAmount: 29000,
    purchaseDate: '2024-12-10',
    expectedDate: '2024-12-20',
    status: 'received',
    paymentStatus: 'paid',
    purchaser: '吴八',
    remarks: '库存补充'
  }
]

// 方法
const getStatusType = (status) => {
  const typeMap = {
    'pending': 'warning',
    'approved': 'primary',
    'purchasing': 'primary',
    'received': 'success',
    'completed': 'success',
    'cancelled': 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    'pending': '待审核',
    'approved': '已审核',
    'purchasing': '采购中',
    'received': '已入库',
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

const getExpectedDateClass = (expectedDate, status) => {
  if (['received', 'completed', 'cancelled'].includes(status)) return 'date-normal'
  
  const today = new Date()
  const expected = new Date(expectedDate)
  const diffDays = Math.ceil((expected - today) / (1000 * 60 * 60 * 24))
  
  if (diffDays < 0) return 'date-overdue'
  if (diffDays <= 3) return 'date-urgent'
  return 'date-normal'
}

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loadPurchases()
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
  ElMessage.info('新建采购单功能开发中...')
}

const handleEdit = (purchase) => {
  ElMessage.info(`编辑采购单：${purchase.purchaseNo}`)
}

const handleView = (purchase) => {
  ElMessage.info(`查看采购单详情：${purchase.purchaseNo}`)
}

const handleSelectionChange = (selection) => {
  selectedPurchases.value = selection
}

const handleBatchApprove = () => {
  if (selectedPurchases.value.length === 0) {
    ElMessage.warning('请先选择要审核的采购单')
    return
  }
  
  const pendingCount = selectedPurchases.value.filter(p => p.status === 'pending').length
  if (pendingCount === 0) {
    ElMessage.warning('所选采购单中没有待审核的单据')
    return
  }
  
  ElMessageBox.confirm(
    `确定要审核通过选中的 ${pendingCount} 个采购单吗？`,
    '批量审核',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    selectedPurchases.value.forEach(purchase => {
      if (purchase.status === 'pending') {
        purchase.status = 'approved'
      }
    })
    ElMessage.success(`成功审核 ${pendingCount} 个采购单`)
    selectedPurchases.value = []
  })
}

const handleExport = () => {
  ElMessage.info('正在导出采购数据...')
}

const handleCommand = (command, purchase) => {
  switch (command) {
    case 'approve':
      ElMessage.success(`采购单 ${purchase.purchaseNo} 审核通过`)
      purchase.status = 'approved'
      break
    case 'receive':
      ElMessage.success(`采购单 ${purchase.purchaseNo} 已确认收货`)
      purchase.status = 'received'
      break
    case 'pay':
      ElMessage.info(`处理付款：${purchase.purchaseNo}`)
      break
    case 'print':
      ElMessage.info(`打印采购单：${purchase.purchaseNo}`)
      break
    case 'cancel':
      handleCancel(purchase)
      break
  }
}

const handleCancel = (purchase) => {
  ElMessageBox.confirm(
    `确定要取消采购单 "${purchase.purchaseNo}" 吗？`,
    '确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    purchase.status = 'cancelled'
    ElMessage.success('采购单已取消')
  })
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadPurchases()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadPurchases()
}

const loadPurchases = () => {
  purchaseList.value = [...mockPurchases]
  pagination.total = mockPurchases.length
}

onMounted(() => {
  loadPurchases()
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
        
        &.purchasing {
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

.date-normal {
  color: #606266;
}

.date-urgent {
  color: #E6A23C;
  font-weight: bold;
}

.date-overdue {
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