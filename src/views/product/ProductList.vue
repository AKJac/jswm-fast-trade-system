<template>
  <div class="page-container">
    <div class="page-header">
      <h1>产品列表</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>产品管理</el-breadcrumb-item>
        <el-breadcrumb-item>产品列表</el-breadcrumb-item>
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
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="产品分类">
          <el-select 
            v-model="searchForm.category" 
            placeholder="请选择分类" 
            clearable
            style="width: 150px"
          >
            <el-option label="电子产品" value="电子产品" />
            <el-option label="机械设备" value="机械设备" />
            <el-option label="纺织服装" value="纺织服装" />
            <el-option label="建材家具" value="建材家具" />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商">
          <el-select 
            v-model="searchForm.supplier" 
            placeholder="请选择供应商" 
            clearable
            style="width: 150px"
          >
            <el-option label="深圳科技有限公司" value="supplier1" />
            <el-option label="广州制造集团" value="supplier2" />
            <el-option label="上海工业股份" value="supplier3" />
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
            新增产品
          </el-button>
          <el-button type="success" @click="handleBatchImport">
            <el-icon><Upload /></el-icon>
            批量导入
          </el-button>
          <el-button type="warning" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出数据
          </el-button>
          <el-button 
            type="danger" 
            @click="handleBatchDelete"
            :disabled="selectedProducts.length === 0"
          >
            <el-icon><Delete /></el-icon>
            批量删除
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

    <!-- 产品列表 - 表格视图 -->
    <el-card v-if="viewMode === 'table'" class="custom-card">
      <el-table 
        :data="productList" 
        stripe 
        class="custom-table"
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="productCode" label="产品货号" width="120" />
        <el-table-column label="产品图片" width="80">
          <template #default="{ row }">
            <el-image
              :src="row.image"
              :preview-src-list="[row.image]"
              fit="cover"
              style="width: 50px; height: 50px; border-radius: 4px;"
              :z-index="9999"
            />
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="产品名称" min-width="200">
          <template #default="{ row }">
            <div class="product-info">
              <div class="product-name-zh">{{ row.productName }}</div>
              <div class="product-name-en">{{ row.productNameEn }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="产品分类" width="120" />
        <el-table-column prop="specifications" label="规格型号" width="150" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column label="价格信息" width="200">
          <template #default="{ row }">
            <div class="price-info">
              <div class="price-item">
                <span class="price-label">销售价:</span>
                <span class="price-value sale">¥{{ row.salePrice.toLocaleString() }}</span>
              </div>
              <div class="price-item">
                <span class="price-label">采购价:</span>
                <span class="price-value purchase">¥{{ row.purchasePrice.toLocaleString() }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存数量" width="100">
          <template #default="{ row }">
            <span :class="getStockClass(row.stock)">{{ row.stock }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="supplier" label="供应商" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
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
                  <el-dropdown-item command="copy">
                    <el-icon><CopyDocument /></el-icon>
                    复制产品
                  </el-dropdown-item>
                  <el-dropdown-item command="price">
                    <el-icon><Money /></el-icon>
                    价格历史
                  </el-dropdown-item>
                  <el-dropdown-item command="stock">
                    <el-icon><Box /></el-icon>
                    库存记录
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

    <!-- 产品列表 - 卡片视图 -->
    <div v-else class="card-view">
      <el-row :gutter="20">
        <el-col 
          v-for="product in productList" 
          :key="product.id" 
          :xs="24" :sm="12" :md="8" :lg="6" :xl="4"
          class="product-card-col"
        >
          <el-card class="product-card" @click="handleView(product)">
            <div class="product-image">
              <el-image
                :src="product.image"
                fit="cover"
                style="width: 100%; height: 150px;"
              >
                <template #error>
                  <div class="image-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="product-status">
                <el-tag :type="getStatusType(product.status)" size="small">
                  {{ product.status }}
                </el-tag>
              </div>
            </div>
            <div class="product-content">
              <h3 class="product-title">{{ product.productName }}</h3>
              <p class="product-title-en">{{ product.productNameEn }}</p>
              <div class="product-code">货号: {{ product.productCode }}</div>
              <div class="product-specs">规格: {{ product.specifications }}</div>
              <div class="product-price">
                <div class="price-row">
                  <span class="price-label">销售价:</span>
                  <span class="price-value sale">¥{{ product.salePrice.toLocaleString() }}</span>
                </div>
                <div class="price-row">
                  <span class="price-label">库存:</span>
                  <span :class="getStockClass(product.stock)">{{ product.stock }}</span>
                </div>
              </div>
              <div class="product-supplier">{{ product.supplier }}</div>
            </div>
            <div class="product-actions">
              <el-button size="small" type="primary" @click.stop="handleEdit(product)">
                编辑
              </el-button>
              <el-button size="small" @click.stop="handleQuote(product)">
                报价
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
        :page-sizes="[12, 24, 48, 96]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 产品详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
      @close="handleDialogClose"
    >
      <product-form
        v-if="dialogVisible"
        :product="currentProduct"
        :is-edit="isEdit"
        @success="handleFormSuccess"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import ProductForm from './components/ProductForm.vue'

// 响应式数据
const loading = ref(false)
const viewMode = ref('table')
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentProduct = ref(null)

const searchForm = reactive({
  productName: '',
  productCode: '',
  category: '',
  supplier: ''
})

const pagination = reactive({
  current: 1,
  size: 24,
  total: 0
})

const productList = ref([])
const selectedProducts = ref([])

// 计算属性
const dialogTitle = computed(() => {
  return isEdit.value ? '编辑产品' : '新增产品'
})

// 模拟产品数据
const mockProducts = [
  {
    id: 1,
    productCode: 'P202412001',
    productName: '智能蓝牙耳机',
    productNameEn: 'Smart Bluetooth Headphones',
    category: '电子产品',
    specifications: 'TWS无线 / 降噪 / 长续航',
    unit: '个',
    salePrice: 299,
    purchasePrice: 180,
    costPrice: 150,
    stock: 1250,
    supplier: '深圳科技有限公司',
    status: '在售',
    image: 'https://via.placeholder.com/200x200/409EFF/ffffff?text=耳机',
    createTime: '2024-12-01'
  },
  {
    id: 2,
    productCode: 'P202412002',
    productName: '无线充电器',
    productNameEn: 'Wireless Charger',
    category: '电子产品',
    specifications: '15W快充 / 兼容多设备',
    unit: '个',
    salePrice: 129,
    purchasePrice: 80,
    costPrice: 65,
    stock: 890,
    supplier: '深圳科技有限公司',
    status: '在售',
    image: 'https://via.placeholder.com/200x200/67C23A/ffffff?text=充电器',
    createTime: '2024-12-03'
  },
  {
    id: 3,
    productCode: 'P202412003',
    productName: '便携式音箱',
    productNameEn: 'Portable Speaker',
    category: '电子产品',
    specifications: '防水 / 蓝牙5.0 / 12小时续航',
    unit: '个',
    salePrice: 199,
    purchasePrice: 120,
    costPrice: 95,
    stock: 45,
    supplier: '广州制造集团',
    status: '在售',
    image: 'https://via.placeholder.com/200x200/E6A23C/ffffff?text=音箱',
    createTime: '2024-12-05'
  },
  {
    id: 4,
    productCode: 'P202412004',
    productName: '智能手环',
    productNameEn: 'Smart Bracelet',
    category: '电子产品',
    specifications: '心率监测 / 运动追踪',
    unit: '个',
    salePrice: 399,
    purchasePrice: 250,
    costPrice: 200,
    stock: 15,
    supplier: '上海工业股份',
    status: '缺货',
    image: 'https://via.placeholder.com/200x200/F56C6C/ffffff?text=手环',
    createTime: '2024-12-08'
  },
  {
    id: 5,
    productCode: 'P202412005',
    productName: 'USB数据线',
    productNameEn: 'USB Cable',
    category: '电子产品',
    specifications: 'Type-C / 3米 / 快充支持',
    unit: '根',
    salePrice: 29,
    purchasePrice: 15,
    costPrice: 12,
    stock: 2850,
    supplier: '深圳科技有限公司',
    status: '在售',
    image: 'https://via.placeholder.com/200x200/909399/ffffff?text=数据线',
    createTime: '2024-12-10'
  },
  {
    id: 6,
    productCode: 'P202412006',
    productName: '平板电脑保护套',
    productNameEn: 'Tablet Case',
    category: '电子产品',
    specifications: '皮质 / 支架功能 / 多角度',
    unit: '个',
    salePrice: 89,
    purchasePrice: 45,
    costPrice: 35,
    stock: 0,
    supplier: '广州制造集团',
    status: '停产',
    image: 'https://via.placeholder.com/200x200/C0C4CC/ffffff?text=保护套',
    createTime: '2024-12-12'
  }
]

// 方法
const getStatusType = (status) => {
  const typeMap = {
    '在售': 'success',
    '缺货': 'warning',
    '停产': 'danger',
    '新品': 'primary'
  }
  return typeMap[status] || 'info'
}

const getStockClass = (stock) => {
  if (stock === 0) return 'stock-empty'
  if (stock < 50) return 'stock-low'
  if (stock < 200) return 'stock-medium'
  return 'stock-high'
}

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loadProducts()
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
  currentProduct.value = null
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (product) => {
  currentProduct.value = { ...product }
  isEdit.value = true
  dialogVisible.value = true
}

const handleView = (product) => {
  ElMessage.info(`查看产品：${product.productName}`)
}

const handleQuote = (product) => {
  ElMessage.info(`为产品 "${product.productName}" 创建报价单`)
}

const handleDelete = (product) => {
  ElMessageBox.confirm(
    `确定要删除产品 "${product.productName}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = productList.value.findIndex(item => item.id === product.id)
    if (index > -1) {
      productList.value.splice(index, 1)
      pagination.total--
      ElMessage.success('删除成功')
    }
  })
}

const handleCommand = (command, product) => {
  switch (command) {
    case 'copy':
      ElMessage.info(`复制产品：${product.productName}`)
      break
    case 'price':
      ElMessage.info(`查看价格历史：${product.productName}`)
      break
    case 'stock':
      ElMessage.info(`查看库存记录：${product.productName}`)
      break
    case 'delete':
      handleDelete(product)
      break
  }
}

const handleBatchImport = () => {
  ElMessage.info('批量导入功能开发中...')
}

const handleExport = () => {
  ElMessage.info('正在导出数据...')
}

const handleBatchDelete = () => {
  if (selectedProducts.value.length === 0) {
    ElMessage.warning('请先选择要删除的产品')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedProducts.value.length} 个产品吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    selectedProducts.value.forEach(product => {
      const index = productList.value.findIndex(item => item.id === product.id)
      if (index > -1) {
        productList.value.splice(index, 1)
        pagination.total--
      }
    })
    selectedProducts.value = []
    ElMessage.success('批量删除成功')
  })
}

const handleSelectionChange = (selection) => {
  selectedProducts.value = selection
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadProducts()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadProducts()
}

const handleDialogClose = () => {
  currentProduct.value = null
}

const handleFormSuccess = () => {
  dialogVisible.value = false
  loadProducts()
  ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
}

const loadProducts = () => {
  productList.value = [...mockProducts]
  pagination.total = mockProducts.length
}

onMounted(() => {
  loadProducts()
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

.product-info {
  .product-name-zh {
    font-weight: 600;
    color: #303133;
    margin-bottom: 2px;
  }
  
  .product-name-en {
    font-size: 12px;
    color: #909399;
  }
}

.price-info {
  .price-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 4px;
    
    .price-label {
      font-size: 12px;
      color: #909399;
    }
    
    .price-value {
      font-weight: 600;
      font-size: 13px;
      
      &.sale {
        color: #67C23A;
      }
      
      &.purchase {
        color: #E6A23C;
      }
    }
  }
}

.stock-empty {
  color: #F56C6C;
  font-weight: bold;
}

.stock-low {
  color: #E6A23C;
  font-weight: bold;
}

.stock-medium {
  color: #409EFF;
}

.stock-high {
  color: #67C23A;
}

.card-view {
  .product-card-col {
    margin-bottom: 20px;
  }
  
  .product-card {
    cursor: pointer;
    transition: all 0.3s;
    height: 100%;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }
    
    :deep(.el-card__body) {
      padding: 0;
    }
    
    .product-image {
      position: relative;
      
      .product-status {
        position: absolute;
        top: 8px;
        right: 8px;
      }
      
      .image-slot {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 150px;
        background: #f5f7fa;
        color: #909399;
        font-size: 30px;
      }
    }
    
    .product-content {
      padding: 15px;
      
      .product-title {
        margin: 0 0 5px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        line-height: 1.4;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .product-title-en {
        margin: 0 0 10px 0;
        font-size: 12px;
        color: #909399;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .product-code,
      .product-specs {
        font-size: 12px;
        color: #606266;
        margin-bottom: 5px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .product-price {
        margin: 10px 0;
        
        .price-row {
          display: flex;
          justify-content: space-between;
          margin-bottom: 3px;
          
          .price-label {
            font-size: 12px;
            color: #909399;
          }
          
          .price-value {
            font-weight: 600;
            font-size: 13px;
            
            &.sale {
              color: #67C23A;
            }
          }
        }
      }
      
      .product-supplier {
        font-size: 12px;
        color: #909399;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
    
    .product-actions {
      padding: 0 15px 15px 15px;
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
      flex-wrap: wrap;
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