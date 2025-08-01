<template>
  <div class="page-container">
    <div class="page-header">
      <h1>报价管理</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>报价管理</el-breadcrumb-item>
        <el-breadcrumb-item>报价单</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索栏 -->
    <el-card class="custom-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="报价单号">
          <el-input 
            v-model="searchForm.quotationNo" 
            placeholder="请输入报价单号" 
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
        <el-form-item label="报价日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="报价状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable
            style="width: 150px"
          >
            <el-option label="草稿" value="draft" />
            <el-option label="已发送" value="sent" />
            <el-option label="已确认" value="confirmed" />
            <el-option label="已拒绝" value="rejected" />
            <el-option label="已过期" value="expired" />
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

    <!-- 操作栏 -->
    <el-card class="custom-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新建报价
          </el-button>
          <el-button type="success" @click="handleBatchSend" :disabled="selectedQuotations.length === 0">
            <el-icon><Message /></el-icon>
            批量发送 ({{ selectedQuotations.length }})
          </el-button>
          <el-button type="warning" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出报价
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 报价列表 -->
    <el-card class="custom-card">
      <el-table 
        :data="quotationList" 
        stripe 
        class="custom-table"
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="quotationNo" label="报价单号" width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">
              {{ row.quotationNo }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="customerName" label="客户名称" min-width="180" />
        <el-table-column prop="contact" label="联系人" width="100" />
        <el-table-column label="报价产品" min-width="200">
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
        <el-table-column prop="totalAmount" label="报价金额" width="120">
          <template #default="{ row }">
            <span class="amount-text">{{ row.currency }} {{ row.totalAmount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="quotationDate" label="报价日期" width="120" />
        <el-table-column prop="validUntil" label="有效期至" width="120">
          <template #default="{ row }">
            <span :class="getValidityClass(row.validUntil)">
              {{ row.validUntil }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="salesperson" label="业务员" width="100" />
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
                  <el-dropdown-item command="send" :disabled="row.status === 'sent'">
                    <el-icon><Message /></el-icon>
                    发送客户
                  </el-dropdown-item>
                  <el-dropdown-item command="copy">
                    <el-icon><CopyDocument /></el-icon>
                    复制报价
                  </el-dropdown-item>
                  <el-dropdown-item command="order" :disabled="row.status !== 'confirmed'">
                    <el-icon><Document /></el-icon>
                    转为订单
                  </el-dropdown-item>
                  <el-dropdown-item command="print">
                    <el-icon><Printer /></el-icon>
                    打印报价
                  </el-dropdown-item>
                  <el-dropdown-item command="delete" divided>
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

    <!-- 报价详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="1200px"
      @close="handleDialogClose"
    >
      <quotation-form
        v-if="dialogVisible"
        :quotation="currentQuotation"
        :is-edit="isEdit"
        @success="handleFormSuccess"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import QuotationForm from './components/QuotationForm.vue'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentQuotation = ref(null)

const searchForm = reactive({
  quotationNo: '',
  customerName: '',
  dateRange: null,
  status: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const quotationList = ref([])
const selectedQuotations = ref([])

// 计算属性
const dialogTitle = computed(() => {
  return isEdit.value ? '编辑报价单' : '新建报价单'
})

// 模拟报价数据
const mockQuotations = [
  {
    id: 1,
    quotationNo: 'Q202412001',
    customerName: 'ABC Trading Co.',
    contact: 'John Smith',
    products: [
      { name: '智能蓝牙耳机', quantity: 100, price: 299, amount: 29900 },
      { name: '无线充电器', quantity: 50, price: 129, amount: 6450 }
    ],
    totalAmount: 36350,
    currency: '¥',
    quotationDate: '2024-12-15',
    validUntil: '2025-01-15',
    status: 'sent',
    salesperson: '张三',
    remarks: '首次合作客户，价格优惠5%'
  },
  {
    id: 2,
    quotationNo: 'Q202412002',
    customerName: 'XYZ Import Ltd',
    contact: 'Sarah Johnson',
    products: [
      { name: '便携式音箱', quantity: 200, price: 199, amount: 39800 },
      { name: '智能手环', quantity: 80, price: 399, amount: 31920 },
      { name: 'USB数据线', quantity: 500, price: 29, amount: 14500 }
    ],
    totalAmount: 86220,
    currency: '¥',
    quotationDate: '2024-12-18',
    validUntil: '2025-01-18',
    status: 'confirmed',
    salesperson: '李四',
    remarks: '老客户，已确认订单'
  },
  {
    id: 3,
    quotationNo: 'Q202412003',
    customerName: 'Global Solutions',
    contact: 'Michael Brown',
    products: [
      { name: '平板电脑保护套', quantity: 300, price: 89, amount: 26700 }
    ],
    totalAmount: 26700,
    currency: '¥',
    quotationDate: '2024-12-10',
    validUntil: '2024-12-25',
    status: 'expired',
    salesperson: '王五',
    remarks: '客户未及时回复，报价已过期'
  },
  {
    id: 4,
    quotationNo: 'Q202412004',
    customerName: 'Eastern Trading',
    contact: 'Yuki Tanaka',
    products: [
      { name: '智能蓝牙耳机', quantity: 500, price: 280, amount: 140000 },
      { name: '便携式音箱', quantity: 300, price: 185, amount: 55500 }
    ],
    totalAmount: 195500,
    currency: '¥',
    quotationDate: '2024-12-20',
    validUntil: '2025-02-20',
    status: 'draft',
    salesperson: '赵六',
    remarks: '大批量订单，正在准备发送'
  }
]

// 方法
const getStatusType = (status) => {
  const typeMap = {
    'draft': 'info',
    'sent': 'warning',
    'confirmed': 'success',
    'rejected': 'danger',
    'expired': 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    'draft': '草稿',
    'sent': '已发送',
    'confirmed': '已确认',
    'rejected': '已拒绝',
    'expired': '已过期'
  }
  return textMap[status] || status
}

const getValidityClass = (validUntil) => {
  const today = new Date()
  const validDate = new Date(validUntil)
  const diffDays = Math.ceil((validDate - today) / (1000 * 60 * 60 * 24))
  
  if (diffDays < 0) return 'validity-expired'
  if (diffDays <= 7) return 'validity-warning'
  return 'validity-normal'
}

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loadQuotations()
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
  currentQuotation.value = null
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (quotation) => {
  currentQuotation.value = { ...quotation }
  isEdit.value = true
  dialogVisible.value = true
}

const handleView = (quotation) => {
  ElMessage.info(`查看报价单：${quotation.quotationNo}`)
}

const handleDelete = (quotation) => {
  ElMessageBox.confirm(
    `确定要删除报价单 "${quotation.quotationNo}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = quotationList.value.findIndex(item => item.id === quotation.id)
    if (index > -1) {
      quotationList.value.splice(index, 1)
      pagination.total--
      ElMessage.success('删除成功')
    }
  })
}

const handleCommand = (command, quotation) => {
  switch (command) {
    case 'send':
      ElMessage.success(`报价单 ${quotation.quotationNo} 已发送给客户`)
      quotation.status = 'sent'
      break
    case 'copy':
      ElMessage.info(`复制报价单：${quotation.quotationNo}`)
      break
    case 'order':
      ElMessage.success(`报价单 ${quotation.quotationNo} 已转为订单`)
      break
    case 'print':
      ElMessage.info(`打印报价单：${quotation.quotationNo}`)
      break
    case 'delete':
      handleDelete(quotation)
      break
  }
}

const handleBatchSend = () => {
  if (selectedQuotations.value.length === 0) {
    ElMessage.warning('请先选择要发送的报价单')
    return
  }
  
  ElMessageBox.confirm(
    `确定要发送选中的 ${selectedQuotations.value.length} 个报价单吗？`,
    '确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    selectedQuotations.value.forEach(quotation => {
      if (quotation.status === 'draft') {
        quotation.status = 'sent'
      }
    })
    ElMessage.success(`成功发送 ${selectedQuotations.value.length} 个报价单`)
    selectedQuotations.value = []
  })
}

const handleExport = () => {
  ElMessage.info('正在导出报价数据...')
}

const handleSelectionChange = (selection) => {
  selectedQuotations.value = selection
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadQuotations()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadQuotations()
}

const handleDialogClose = () => {
  currentQuotation.value = null
}

const handleFormSuccess = () => {
  dialogVisible.value = false
  loadQuotations()
  ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
}

const loadQuotations = () => {
  quotationList.value = [...mockQuotations]
  pagination.total = mockQuotations.length
}

onMounted(() => {
  loadQuotations()
})
</script>

<style lang="scss" scoped>
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

.validity-normal {
  color: #67C23A;
}

.validity-warning {
  color: #E6A23C;
  font-weight: bold;
}

.validity-expired {
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