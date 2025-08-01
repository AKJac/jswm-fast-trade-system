<template>
  <div class="page-container">
    <div class="page-header">
      <h1>客户列表</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>客户管理</el-breadcrumb-item>
        <el-breadcrumb-item>客户列表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索栏 -->
    <el-card class="custom-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
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
            <el-option label="美国" value="USA" />
            <el-option label="英国" value="UK" />
            <el-option label="德国" value="Germany" />
            <el-option label="日本" value="Japan" />
            <el-option label="澳大利亚" value="Australia" />
          </el-select>
        </el-form-item>
        <el-form-item label="客户等级">
          <el-select 
            v-model="searchForm.level" 
            placeholder="请选择等级" 
            clearable
            style="width: 150px"
          >
            <el-option label="VIP客户" value="VIP" />
            <el-option label="重要客户" value="Important" />
            <el-option label="普通客户" value="Normal" />
            <el-option label="潜在客户" value="Potential" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进进度">
          <el-select 
            v-model="searchForm.progress" 
            placeholder="请选择进度" 
            clearable
            style="width: 150px"
          >
            <el-option label="初步接触" value="Initial" />
            <el-option label="意向客户" value="Interested" />
            <el-option label="报价中" value="Quoting" />
            <el-option label="已成交" value="Deal" />
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
            新增客户
          </el-button>
          <el-button type="success" @click="handleBatchImport">
            <el-icon><Upload /></el-icon>
            批量导入
          </el-button>
          <el-button type="warning" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出数据
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button-group>
            <el-button :type="viewMode === 'table' ? 'primary' : ''" @click="viewMode = 'table'">
              <el-icon><List /></el-icon>
            </el-button>
            <el-button :type="viewMode === 'card' ? 'primary' : ''" @click="viewMode = 'card'">
              <el-icon><Grid /></el-icon>
            </el-button>
          </el-button-group>
        </div>
      </div>
    </el-card>

    <!-- 客户列表 - 表格视图 -->
    <el-card v-if="viewMode === 'table'" class="custom-card">
      <el-table 
        :data="customerList" 
        stripe 
        class="custom-table"
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="customerNo" label="客户编号" width="120" />
        <el-table-column prop="customerName" label="客户名称" min-width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">
              {{ row.customerName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="contact" label="联系人" width="100" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="phone" label="电话" width="130" />
        <el-table-column prop="country" label="国家" width="100" />
        <el-table-column prop="level" label="客户等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)" size="small">
              {{ row.level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="跟进进度" width="100">
          <template #default="{ row }">
            <el-tag :type="getProgressType(row.progress)" size="small" effect="plain">
              {{ row.progress }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="source" label="客户来源" width="100" />
        <el-table-column prop="lastContact" label="最近联系" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="120" />
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
                  <el-dropdown-item command="contact">
                    <el-icon><Phone /></el-icon>
                    联系记录
                  </el-dropdown-item>
                  <el-dropdown-item command="quotation">
                    <el-icon><Money /></el-icon>
                    报价记录
                  </el-dropdown-item>
                  <el-dropdown-item command="order">
                    <el-icon><Document /></el-icon>
                    订单记录
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

    <!-- 客户列表 - 卡片视图 -->
    <div v-else class="card-view">
      <el-row :gutter="20">
        <el-col 
          v-for="customer in customerList" 
          :key="customer.id" 
          :xs="24" :sm="12" :md="8" :lg="6" :xl="6"
          class="customer-card-col"
        >
          <el-card class="customer-card" @click="handleView(customer)">
            <div class="customer-header">
              <div class="customer-avatar">
                <el-avatar :size="50">{{ customer.customerName.charAt(0) }}</el-avatar>
              </div>
              <div class="customer-info">
                <h3 class="customer-name">{{ customer.customerName }}</h3>
                <p class="customer-contact">{{ customer.contact }}</p>
              </div>
              <div class="customer-level">
                <el-tag :type="getLevelType(customer.level)" size="small">
                  {{ customer.level }}
                </el-tag>
              </div>
            </div>
            <div class="customer-details">
              <div class="detail-item">
                <el-icon><Location /></el-icon>
                <span>{{ customer.country }}</span>
              </div>
              <div class="detail-item">
                <el-icon><Message /></el-icon>
                <span>{{ customer.email }}</span>
              </div>
              <div class="detail-item">
                <el-icon><Phone /></el-icon>
                <span>{{ customer.phone }}</span>
              </div>
            </div>
            <div class="customer-progress">
              <span class="progress-label">跟进进度:</span>
              <el-tag :type="getProgressType(customer.progress)" size="small" effect="plain">
                {{ customer.progress }}
              </el-tag>
            </div>
            <div class="customer-actions">
              <el-button size="small" type="primary" @click.stop="handleEdit(customer)">
                编辑
              </el-button>
              <el-button size="small" @click.stop="handleContact(customer)">
                联系
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

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

    <!-- 客户详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose"
    >
      <customer-form
        v-if="dialogVisible"
        :customer="currentCustomer"
        :is-edit="isEdit"
        @success="handleFormSuccess"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import CustomerForm from './components/CustomerForm.vue'

// 响应式数据
const loading = ref(false)
const viewMode = ref('table')
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentCustomer = ref(null)

const searchForm = reactive({
  customerName: '',
  country: '',
  level: '',
  progress: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const customerList = ref([])
const selectedCustomers = ref([])

// 计算属性
const dialogTitle = computed(() => {
  return isEdit.value ? '编辑客户' : '新增客户'
})

// 模拟数据
const mockCustomers = [
  {
    id: 1,
    customerNo: 'C202412001',
    customerName: 'ABC Trading Co.',
    contact: 'John Smith',
    email: 'john@abctrading.com',
    phone: '+1-555-1234',
    country: '美国',
    level: 'VIP客户',
    progress: '已成交',
    source: '展会',
    lastContact: '2024-12-20',
    createTime: '2024-01-15'
  },
  {
    id: 2,
    customerNo: 'C202412002',
    customerName: 'XYZ Import Ltd',
    contact: 'Sarah Johnson',
    email: 'sarah@xyzimport.com',
    phone: '+44-20-1234',
    country: '英国',
    level: '重要客户',
    progress: '报价中',
    source: '网络',
    lastContact: '2024-12-18',
    createTime: '2024-02-20'
  },
  {
    id: 3,
    customerNo: 'C202412003',
    customerName: 'Global Solutions',
    contact: 'Michael Brown',
    email: 'michael@globalsol.com',
    phone: '+49-30-5678',
    country: '德国',
    level: '普通客户',
    progress: '意向客户',
    source: '介绍',
    lastContact: '2024-12-15',
    createTime: '2024-03-10'
  },
  {
    id: 4,
    customerNo: 'C202412004',
    customerName: 'Eastern Trading',
    contact: 'Yuki Tanaka',
    email: 'yuki@eastern.co.jp',
    phone: '+81-3-9876',
    country: '日本',
    level: '潜在客户',
    progress: '初步接触',
    source: '展会',
    lastContact: '2024-12-10',
    createTime: '2024-04-05'
  },
  {
    id: 5,
    customerNo: 'C202412005',
    customerName: 'Pacific Imports',
    contact: 'Emma Wilson',
    email: 'emma@pacific.com.au',
    phone: '+61-2-5432',
    country: '澳大利亚',
    level: 'VIP客户',
    progress: '已成交',
    source: '老客户',
    lastContact: '2024-12-22',
    createTime: '2024-05-12'
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

const getProgressType = (progress) => {
  const typeMap = {
    '初步接触': 'info',
    '意向客户': 'primary',
    '报价中': 'warning',
    '已成交': 'success'
  }
  return typeMap[progress] || 'info'
}

const handleSearch = () => {
  loading.value = true
  // 模拟搜索
  setTimeout(() => {
    loadCustomers()
    loading.value = false
  }, 500)
}

const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  handleSearch()
}

const handleAdd = () => {
  currentCustomer.value = null
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (customer) => {
  currentCustomer.value = { ...customer }
  isEdit.value = true
  dialogVisible.value = true
}

const handleView = (customer) => {
  ElMessage.info(`查看客户：${customer.customerName}`)
}

const handleDelete = (customer) => {
  ElMessageBox.confirm(
    `确定要删除客户 "${customer.customerName}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 模拟删除
    const index = customerList.value.findIndex(item => item.id === customer.id)
    if (index > -1) {
      customerList.value.splice(index, 1)
      pagination.total--
      ElMessage.success('删除成功')
    }
  })
}

const handleCommand = (command, customer) => {
  switch (command) {
    case 'contact':
      ElMessage.info(`查看联系记录：${customer.customerName}`)
      break
    case 'quotation':
      ElMessage.info(`查看报价记录：${customer.customerName}`)
      break
    case 'order':
      ElMessage.info(`查看订单记录：${customer.customerName}`)
      break
    case 'delete':
      handleDelete(customer)
      break
  }
}

const handleContact = (customer) => {
  ElMessage.info(`联系客户：${customer.customerName}`)
}

const handleBatchImport = () => {
  ElMessage.info('批量导入功能开发中...')
}

const handleExport = () => {
  ElMessage.info('正在导出数据...')
}

const handleSelectionChange = (selection) => {
  selectedCustomers.value = selection
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadCustomers()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadCustomers()
}

const handleDialogClose = () => {
  currentCustomer.value = null
}

const handleFormSuccess = () => {
  dialogVisible.value = false
  loadCustomers()
  ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
}

const loadCustomers = () => {
  // 模拟加载数据
  customerList.value = [...mockCustomers]
  pagination.total = mockCustomers.length
}

onMounted(() => {
  loadCustomers()
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

.card-view {
  .customer-card-col {
    margin-bottom: 20px;
  }
  
  .customer-card {
    cursor: pointer;
    transition: all 0.3s;
    height: 100%;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }
    
    .customer-header {
      display: flex;
      align-items: center;
      margin-bottom: 15px;
      
      .customer-avatar {
        margin-right: 15px;
      }
      
      .customer-info {
        flex: 1;
        
        .customer-name {
          margin: 0 0 5px 0;
          font-size: 16px;
          color: #303133;
        }
        
        .customer-contact {
          margin: 0;
          font-size: 14px;
          color: #909399;
        }
      }
    }
    
    .customer-details {
      margin-bottom: 15px;
      
      .detail-item {
        display: flex;
        align-items: center;
        margin-bottom: 8px;
        font-size: 14px;
        color: #606266;
        
        .el-icon {
          margin-right: 8px;
          color: #909399;
        }
        
        span {
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
    
    .customer-progress {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 15px;
      
      .progress-label {
        font-size: 14px;
        color: #606266;
      }
    }
    
    .customer-actions {
      display: flex;
      justify-content: space-between;
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
  .toolbar {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
    
    .toolbar-left {
      justify-content: center;
    }
    
    .toolbar-right {
      display: flex;
      justify-content: center;
    }
  }
  
  .search-form {
    .el-form-item {
      margin-bottom: 15px;
      display: block;
      
      .el-input, .el-select {
        width: 100% !important;
      }
    }
  }
}
</style> 