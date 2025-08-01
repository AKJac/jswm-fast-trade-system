<template>
  <div class="page-container">
    <div class="page-header">
      <h1>客户公海</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>客户管理</el-breadcrumb-item>
        <el-breadcrumb-item>客户公海</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 说明卡片 -->
    <el-alert
      title="客户公海说明"
      type="info"
      :closable="false"
      show-icon
      class="pool-alert"
    >
      <template #default>
        <p>客户公海是存放未分配或已释放客户资源的共享池。所有业务员都可以从公海中领取客户进行跟进。</p>
        <p><strong>自动进入规则：</strong>超过30天未跟进的客户将自动进入公海；业务员主动释放的客户。</p>
      </template>
    </el-alert>

    <!-- 搜索和操作栏 -->
    <el-card class="custom-card">
      <el-row :gutter="20" class="search-row">
        <el-col :span="18">
          <el-form :model="searchForm" :inline="true">
            <el-form-item label="客户名称">
              <el-input 
                v-model="searchForm.customerName" 
                placeholder="请输入客户名称" 
                clearable
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="国家">
              <el-select 
                v-model="searchForm.country" 
                placeholder="请选择国家" 
                clearable
                style="width: 150px"
              >
                <el-option label="美国" value="美国" />
                <el-option label="英国" value="英国" />
                <el-option label="德国" value="德国" />
                <el-option label="日本" value="日本" />
              </el-select>
            </el-form-item>
            <el-form-item label="客户等级">
              <el-select 
                v-model="searchForm.level" 
                placeholder="请选择等级" 
                clearable
                style="width: 150px"
              >
                <el-option label="VIP客户" value="VIP客户" />
                <el-option label="重要客户" value="重要客户" />
                <el-option label="普通客户" value="普通客户" />
                <el-option label="潜在客户" value="潜在客户" />
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
        </el-col>
        <el-col :span="6" class="text-right">
          <el-button 
            type="success" 
            @click="handleBatchClaim"
            :disabled="selectedCustomers.length === 0"
          >
            <el-icon><UserFilled /></el-icon>
            批量领取 ({{ selectedCustomers.length }})
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 客户公海列表 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>公海客户列表 ({{ pagination.total }})</span>
          <div class="header-actions">
            <el-tooltip content="客户在公海中停留超过90天将被自动清理" placement="top">
              <el-icon class="info-icon"><InfoFilled /></el-icon>
            </el-tooltip>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="poolCustomers" 
        stripe 
        class="custom-table"
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="customerNo" label="客户编号" width="120" />
        <el-table-column prop="customerName" label="客户名称" min-width="150">
          <template #default="{ row }">
            <div class="customer-info">
              <el-link type="primary" @click="handleView(row)">
                {{ row.customerName }}
              </el-link>
              <el-tag 
                v-if="row.daysInPool > 60" 
                type="danger" 
                size="small" 
                class="pool-tag"
              >
                即将清理
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="contact" label="联系人" width="100" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="country" label="国家" width="100" />
        <el-table-column prop="level" label="客户等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)" size="small">
              {{ row.level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastContact" label="最后跟进" width="120" />
        <el-table-column prop="lastSalesperson" label="原业务员" width="100" />
        <el-table-column label="公海天数" width="100">
          <template #default="{ row }">
            <span :class="getDaysClass(row.daysInPool)">
              {{ row.daysInPool }} 天
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="poolReason" label="进入原因" width="120">
          <template #default="{ row }">
            <el-tag :type="getReasonType(row.poolReason)" size="small" effect="plain">
              {{ row.poolReason }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="poolTime" label="进入时间" width="120" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="success" size="small" @click="handleClaim(row)">
              <el-icon><UserFilled /></el-icon>
              领取
            </el-button>
            <el-dropdown trigger="click" @command="(command) => handleCommand(command, row)">
              <el-button size="small">
                更多
                <el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="history">
                    <el-icon><Clock /></el-icon>
                    跟进历史
                  </el-dropdown-item>
                  <el-dropdown-item command="clean" divided>
                    <el-icon><Delete /></el-icon>
                    清理客户
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

    <!-- 领取客户对话框 -->
    <el-dialog
      v-model="claimDialogVisible"
      title="领取客户"
      width="500px"
    >
      <div class="claim-dialog">
        <el-alert
          title="确认领取"
          type="warning"
          :closable="false"
          show-icon
        >
          <template #default>
            <p>您确定要领取以下客户吗？</p>
            <ul>
              <li v-for="customer in selectedCustomers" :key="customer.id">
                {{ customer.customerName }} ({{ customer.contact }})
              </li>
            </ul>
            <p>领取后，这些客户将从公海中移除并分配给您负责跟进。</p>
          </template>
        </el-alert>
      </div>
      <template #footer>
        <el-button @click="claimDialogVisible = false">取消</el-button>
        <el-button type="success" @click="confirmClaim" :loading="claiming">
          确认领取
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const loading = ref(false)
const claiming = ref(false)
const claimDialogVisible = ref(false)

const searchForm = reactive({
  customerName: '',
  country: '',
  level: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const poolCustomers = ref([])
const selectedCustomers = ref([])

// 模拟公海客户数据
const mockPoolCustomers = [
  {
    id: 1,
    customerNo: 'C202410001',
    customerName: 'Atlantic Trading Inc.',
    contact: 'Robert Wilson',
    email: 'robert@atlantic.com',
    country: '美国',
    level: 'VIP客户',
    lastContact: '2024-10-15',
    lastSalesperson: '张三',
    daysInPool: 45,
    poolReason: '超期未跟进',
    poolTime: '2024-11-20'
  },
  {
    id: 2,
    customerNo: 'C202409002',
    customerName: 'Nordic Supply Co.',
    contact: 'Anna Larsson',
    email: 'anna@nordic.com',
    country: '德国',
    level: '重要客户',
    lastContact: '2024-09-30',
    lastSalesperson: '李四',
    daysInPool: 67,
    poolReason: '业务员释放',
    poolTime: '2024-11-05'
  },
  {
    id: 3,
    customerNo: 'C202411003',
    customerName: 'Asia Pacific Ltd',
    contact: 'David Kim',
    email: 'david@asiapac.com',
    country: '日本',
    level: '普通客户',
    lastContact: '2024-11-01',
    lastSalesperson: '王五',
    daysInPool: 25,
    poolReason: '超期未跟进',
    poolTime: '2024-12-01'
  },
  {
    id: 4,
    customerNo: 'C202408004',
    customerName: 'European Imports',
    contact: 'Marie Dubois',
    email: 'marie@euro-imports.com',
    country: '法国',
    level: '潜在客户',
    lastContact: '2024-08-20',
    lastSalesperson: '赵六',
    daysInPool: 85,
    poolReason: '客户暂停合作',
    poolTime: '2024-10-15'
  }
]

// 方法
const getLevelType = (level) => {
  const typeMap = {
    'VIP客户': 'danger',
    '重要客户': 'warning',
    '普通客户': 'primary',
    '潜在客户': 'info'
  }
  return typeMap[level] || 'info'
}

const getDaysClass = (days) => {
  if (days > 60) return 'days-danger'
  if (days > 30) return 'days-warning'
  return 'days-normal'
}

const getReasonType = (reason) => {
  const typeMap = {
    '超期未跟进': 'warning',
    '业务员释放': 'info',
    '客户暂停合作': 'danger'
  }
  return typeMap[reason] || 'info'
}

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loadPoolCustomers()
    loading.value = false
  }, 500)
}

const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  handleSearch()
}

const handleView = (customer) => {
  ElMessage.info(`查看客户详情：${customer.customerName}`)
}

const handleClaim = (customer) => {
  selectedCustomers.value = [customer]
  claimDialogVisible.value = true
}

const handleBatchClaim = () => {
  if (selectedCustomers.value.length === 0) {
    ElMessage.warning('请先选择要领取的客户')
    return
  }
  claimDialogVisible.value = true
}

const confirmClaim = () => {
  claiming.value = true
  
  // 模拟领取操作
  setTimeout(() => {
    claiming.value = false
    claimDialogVisible.value = false
    
    // 从公海中移除已领取的客户
    selectedCustomers.value.forEach(customer => {
      const index = poolCustomers.value.findIndex(item => item.id === customer.id)
      if (index > -1) {
        poolCustomers.value.splice(index, 1)
        pagination.total--
      }
    })
    
    ElMessage.success(`成功领取 ${selectedCustomers.value.length} 个客户`)
    selectedCustomers.value = []
  }, 1000)
}

const handleCommand = (command, customer) => {
  switch (command) {
    case 'history':
      ElMessage.info(`查看跟进历史：${customer.customerName}`)
      break
    case 'clean':
      handleCleanCustomer(customer)
      break
  }
}

const handleCleanCustomer = (customer) => {
  ElMessageBox.confirm(
    `确定要清理客户 "${customer.customerName}" 吗？清理后该客户将被永久删除。`,
    '警告',
    {
      confirmButtonText: '确定清理',
      cancelButtonText: '取消',
      type: 'warning',
      dangerouslyUseHTMLString: true
    }
  ).then(() => {
    // 模拟清理操作
    const index = poolCustomers.value.findIndex(item => item.id === customer.id)
    if (index > -1) {
      poolCustomers.value.splice(index, 1)
      pagination.total--
      ElMessage.success('客户清理成功')
    }
  })
}

const handleSelectionChange = (selection) => {
  selectedCustomers.value = selection
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadPoolCustomers()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadPoolCustomers()
}

const loadPoolCustomers = () => {
  poolCustomers.value = [...mockPoolCustomers]
  pagination.total = mockPoolCustomers.length
}

onMounted(() => {
  loadPoolCustomers()
})
</script>

<style lang="scss" scoped>
.pool-alert {
  margin-bottom: 20px;
  
  p {
    margin: 5px 0;
  }
}

.search-row {
  align-items: center;
}

.text-right {
  text-align: right;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .header-actions {
    .info-icon {
      color: #909399;
      cursor: help;
    }
  }
}

.customer-info {
  .pool-tag {
    margin-left: 8px;
  }
}

.days-normal {
  color: #67C23A;
}

.days-warning {
  color: #E6A23C;
  font-weight: bold;
}

.days-danger {
  color: #F56C6C;
  font-weight: bold;
}

.claim-dialog {
  ul {
    margin: 10px 0;
    padding-left: 20px;
    
    li {
      margin: 5px 0;
    }
  }
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

// 响应式设计
@media (max-width: 768px) {
  .search-row {
    flex-direction: column;
    gap: 15px;
    
    .el-col {
      width: 100%;
    }
    
    .text-right {
      text-align: center;
    }
  }
}
</style> 