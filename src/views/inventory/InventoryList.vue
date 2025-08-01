<template>
  <div class="page-container">
    <div class="page-header">
      <h1>库存管理</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>库存管理</el-breadcrumb-item>
        <el-breadcrumb-item>库存查询</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索栏 -->
    <el-card class="custom-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="产品名称">
          <el-input 
            v-model="searchForm.productName" 
            placeholder="请输入产品名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="产品货号">
          <el-input 
            v-model="searchForm.productCode" 
            placeholder="请输入产品货号" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="仓库">
          <el-select 
            v-model="searchForm.warehouse" 
            placeholder="请选择仓库" 
            clearable
            style="width: 150px"
          >
            <el-option label="主仓库" value="main" />
            <el-option label="次仓库" value="secondary" />
            <el-option label="临时仓库" value="temporary" />
          </el-select>
        </el-form-item>
        <el-form-item label="库存状态">
          <el-select 
            v-model="searchForm.stockStatus" 
            placeholder="请选择状态" 
            clearable
            style="width: 150px"
          >
            <el-option label="充足" value="sufficient" />
            <el-option label="低库存" value="low" />
            <el-option label="缺货" value="outofstock" />
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

    <!-- 库存统计 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon total">
              <el-icon><Box /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ inventoryStats.totalItems }}</div>
              <div class="stat-label">库存品种</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon quantity">
              <el-icon><Goods /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ inventoryStats.totalQuantity.toLocaleString() }}</div>
              <div class="stat-label">总库存量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon value">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">¥{{ inventoryStats.totalValue.toLocaleString() }}</div>
              <div class="stat-label">库存总值</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon warning">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value warning-text">{{ inventoryStats.lowStockItems }}</div>
              <div class="stat-label">低库存预警</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 操作栏 -->
    <el-card class="custom-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="handleStockIn">
            <el-icon><Plus /></el-icon>
            入库
          </el-button>
          <el-button type="warning" @click="handleStockOut">
            <el-icon><Minus /></el-icon>
            出库
          </el-button>
          <el-button type="success" @click="handleStockTake">
            <el-icon><Document /></el-icon>
            库存盘点
          </el-button>
          <el-button @click="handleExport">
            <el-icon><Download /></el-icon>
            导出库存
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button-group>
            <el-button 
              :type="viewType === 'table' ? 'primary' : 'default'"
              @click="viewType = 'table'"
            >
              <el-icon><List /></el-icon>
              列表视图
            </el-button>
            <el-button 
              :type="viewType === 'card' ? 'primary' : 'default'"
              @click="viewType = 'card'"
            >
              <el-icon><Grid /></el-icon>
              卡片视图
            </el-button>
          </el-button-group>
        </div>
      </div>
    </el-card>

    <!-- 库存列表 -->
    <el-card class="custom-card" v-if="viewType === 'table'">
      <el-table 
        :data="inventoryList" 
        stripe 
        class="custom-table"
        v-loading="loading"
      >
        <el-table-column prop="productCode" label="产品货号" width="120" />
        <el-table-column label="产品图片" width="80">
          <template #default="{ row }">
            <el-image 
              :src="row.productImage" 
              :preview-src-list="[row.productImage]"
              style="width: 50px; height: 50px;"
              fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="产品名称" min-width="200">
          <template #default="{ row }">
            <div>
              <div>{{ row.productName }}</div>
              <div class="product-specs">{{ row.specifications }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="warehouse" label="仓库" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ getWarehouseText(row.warehouse) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="stockQuantity" label="库存数量" width="120">
          <template #default="{ row }">
            <span :class="getStockClass(row.stockQuantity, row.minStock)">
              {{ row.stockQuantity }} {{ row.unit }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="availableQuantity" label="可用数量" width="120">
          <template #default="{ row }">
            <span class="available">{{ row.availableQuantity }} {{ row.unit }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lockedQuantity" label="锁定数量" width="120">
          <template #default="{ row }">
            <span class="locked" v-if="row.lockedQuantity > 0">
              {{ row.lockedQuantity }} {{ row.unit }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" label="最低库存" width="120">
          <template #default="{ row }">
            {{ row.minStock }} {{ row.unit }}
          </template>
        </el-table-column>
        <el-table-column prop="stockValue" label="库存价值" width="120">
          <template #default="{ row }">
            <span class="amount">¥{{ row.stockValue.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleDetail(row)">
              <el-icon><View /></el-icon>
              明细
            </el-button>
            <el-button type="warning" size="small" @click="handleAdjust(row)">
              <el-icon><Edit /></el-icon>
              调整
            </el-button>
            <el-button type="info" size="small" @click="handleHistory(row)">
              <el-icon><Clock /></el-icon>
              记录
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 卡片视图 -->
    <div class="card-view" v-if="viewType === 'card'">
      <el-row :gutter="20">
        <el-col 
          v-for="item in inventoryList" 
          :key="item.id"
          :xs="24" :sm="12" :md="8" :lg="6" :xl="6"
        >
          <el-card class="inventory-card">
            <div class="card-header">
              <el-image 
                :src="item.productImage" 
                :preview-src-list="[item.productImage]"
                style="width: 100%; height: 150px;"
                fit="cover"
              />
              <el-tag 
                class="stock-tag" 
                :type="getStockTagType(item.stockQuantity, item.minStock)"
                size="small"
              >
                {{ getStockStatus(item.stockQuantity, item.minStock) }}
              </el-tag>
            </div>
            <div class="card-body">
              <h4>{{ item.productName }}</h4>
              <p class="product-code">货号：{{ item.productCode }}</p>
              <p class="product-specs">{{ item.specifications }}</p>
              
              <div class="stock-info">
                <div class="info-item">
                  <span class="label">库存：</span>
                  <span :class="getStockClass(item.stockQuantity, item.minStock)">
                    {{ item.stockQuantity }} {{ item.unit }}
                  </span>
                </div>
                <div class="info-item">
                  <span class="label">可用：</span>
                  <span class="available">{{ item.availableQuantity }} {{ item.unit }}</span>
                </div>
                <div class="info-item">
                  <span class="label">仓库：</span>
                  <span>{{ getWarehouseText(item.warehouse) }}</span>
                </div>
                <div class="info-item">
                  <span class="label">价值：</span>
                  <span class="amount">¥{{ item.stockValue.toLocaleString() }}</span>
                </div>
              </div>
              
              <div class="card-actions">
                <el-button type="primary" size="small" @click="handleDetail(item)">
                  查看明细
                </el-button>
                <el-button type="warning" size="small" @click="handleAdjust(item)">
                  库存调整
                </el-button>
              </div>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 响应式数据
const loading = ref(false)
const viewType = ref('table')

const searchForm = reactive({
  productName: '',
  productCode: '',
  warehouse: '',
  stockStatus: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const inventoryStats = reactive({
  totalItems: 156,
  totalQuantity: 28500,
  totalValue: 3850000,
  lowStockItems: 12
})

const inventoryList = ref([])

// 模拟库存数据
const mockInventory = [
  {
    id: 1,
    productCode: 'PRD001',
    productName: '智能蓝牙耳机',
    productImage: 'https://via.placeholder.com/200x200?text=Product1',
    specifications: 'TWS无线/降噪/续航24小时',
    warehouse: 'main',
    stockQuantity: 850,
    availableQuantity: 750,
    lockedQuantity: 100,
    minStock: 200,
    unit: '个',
    stockValue: 254150
  },
  {
    id: 2,
    productCode: 'PRD002',
    productName: '无线充电器',
    productImage: 'https://via.placeholder.com/200x200?text=Product2',
    specifications: '15W快充/多设备兼容',
    warehouse: 'main',
    stockQuantity: 150,
    availableQuantity: 150,
    lockedQuantity: 0,
    minStock: 300,
    unit: '个',
    stockValue: 19350
  },
  {
    id: 3,
    productCode: 'PRD003',
    productName: '便携式音箱',
    productImage: 'https://via.placeholder.com/200x200?text=Product3',
    specifications: '防水IPX7/蓝牙5.0/360°环绕音',
    warehouse: 'secondary',
    stockQuantity: 420,
    availableQuantity: 380,
    lockedQuantity: 40,
    minStock: 150,
    unit: '个',
    stockValue: 83580
  },
  {
    id: 4,
    productCode: 'PRD004',
    productName: '智能手环',
    productImage: 'https://via.placeholder.com/200x200?text=Product4',
    specifications: '心率监测/睡眠分析/IP68防水',
    warehouse: 'main',
    stockQuantity: 0,
    availableQuantity: 0,
    lockedQuantity: 0,
    minStock: 100,
    unit: '个',
    stockValue: 0
  }
]

// 方法
const getWarehouseText = (warehouse) => {
  const warehouseMap = {
    'main': '主仓库',
    'secondary': '次仓库',
    'temporary': '临时仓库'
  }
  return warehouseMap[warehouse] || warehouse
}

const getStockClass = (quantity, minStock) => {
  if (quantity === 0) return 'stock-out'
  if (quantity < minStock) return 'stock-low'
  return 'stock-normal'
}

const getStockStatus = (quantity, minStock) => {
  if (quantity === 0) return '缺货'
  if (quantity < minStock) return '低库存'
  return '充足'
}

const getStockTagType = (quantity, minStock) => {
  if (quantity === 0) return 'danger'
  if (quantity < minStock) return 'warning'
  return 'success'
}

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loadInventory()
    loading.value = false
  }, 500)
}

const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  handleSearch()
}

const handleStockIn = () => {
  ElMessage.info('入库功能开发中...')
}

const handleStockOut = () => {
  ElMessage.info('出库功能开发中...')
}

const handleStockTake = () => {
  ElMessage.info('库存盘点功能开发中...')
}

const handleExport = () => {
  ElMessage.info('正在导出库存数据...')
}

const handleDetail = (item) => {
  ElMessage.info(`查看库存明细：${item.productName}`)
}

const handleAdjust = (item) => {
  ElMessage.info(`调整库存：${item.productName}`)
}

const handleHistory = (item) => {
  ElMessage.info(`查看出入库记录：${item.productName}`)
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadInventory()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadInventory()
}

const loadInventory = () => {
  inventoryList.value = [...mockInventory]
  pagination.total = mockInventory.length
}

onMounted(() => {
  loadInventory()
})
</script>

<style lang="scss" scoped>
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
        
        &.total {
          background: linear-gradient(135deg, #409EFF 0%, #2D8CF0 100%);
        }
        
        &.quantity {
          background: linear-gradient(135deg, #67C23A 0%, #59B329 100%);
        }
        
        &.value {
          background: linear-gradient(135deg, #E6A23C 0%, #D48806 100%);
        }
        
        &.warning {
          background: linear-gradient(135deg, #F56C6C 0%, #E85555 100%);
        }
      }
      
      .stat-content {
        flex: 1;
        
        .stat-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          line-height: 1.2;
          
          &.warning-text {
            color: #F56C6C;
          }
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 5px;
        }
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
  
  .toolbar-left, .toolbar-right {
    display: flex;
    gap: 10px;
  }
}

.product-specs {
  font-size: 12px;
  color: #909399;
  margin-top: 3px;
}

.stock-normal {
  color: #67C23A;
  font-weight: 600;
}

.stock-low {
  color: #E6A23C;
  font-weight: 600;
}

.stock-out {
  color: #F56C6C;
  font-weight: 600;
}

.available {
  color: #409EFF;
}

.locked {
  color: #909399;
}

.amount {
  color: #E6A23C;
  font-weight: 600;
}

// 卡片视图样式
.card-view {
  .inventory-card {
    margin-bottom: 20px;
    transition: transform 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }
    
    .card-header {
      position: relative;
      margin: -20px -20px 15px;
      
      .stock-tag {
        position: absolute;
        top: 10px;
        right: 10px;
      }
    }
    
    .card-body {
      h4 {
        margin: 0 0 10px 0;
        font-size: 16px;
        color: #303133;
      }
      
      .product-code {
        font-size: 13px;
        color: #909399;
        margin: 5px 0;
      }
      
      .product-specs {
        font-size: 12px;
        color: #909399;
        margin-bottom: 15px;
      }
      
      .stock-info {
        margin-bottom: 15px;
        
        .info-item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;
          font-size: 14px;
          
          .label {
            color: #909399;
          }
        }
      }
      
      .card-actions {
        display: flex;
        gap: 10px;
        
        .el-button {
          flex: 1;
        }
      }
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
  .stats-row {
    .stat-card {
      margin-bottom: 10px;
      
      .stat-item {
        .stat-icon {
          width: 40px;
          height: 40px;
          font-size: 20px;
        }
        
        .stat-content .stat-value {
          font-size: 20px;
        }
      }
    }
  }
  
  .toolbar {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
    
    .toolbar-left, .toolbar-right {
      justify-content: center;
      flex-wrap: wrap;
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