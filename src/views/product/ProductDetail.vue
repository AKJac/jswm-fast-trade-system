<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-left">
        <el-button @click="$router.go(-1)" text>
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h1>产品详情</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleEdit">
          <el-icon><Edit /></el-icon>
          编辑产品
        </el-button>
        <el-button type="success" @click="handleCreateQuotation">
          <el-icon><Plus /></el-icon>
          创建报价
        </el-button>
        <el-button type="warning" @click="handlePrint">
          <el-icon><Printer /></el-icon>
          打印详情
        </el-button>
      </div>
    </div>

    <!-- 产品基本信息 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
          <el-tag :type="getStatusType(productInfo.status)" size="small">
            {{ getStatusText(productInfo.status) }}
          </el-tag>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>产品名称：</label>
            <span>{{ productInfo.name }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>产品货号：</label>
            <span>{{ productInfo.code }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>产品分类：</label>
            <span>{{ productInfo.category }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>供应商：</label>
            <span>{{ productInfo.supplier }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>品牌：</label>
            <span>{{ productInfo.brand }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>型号：</label>
            <span>{{ productInfo.model }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>材质：</label>
            <span>{{ productInfo.material }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>颜色：</label>
            <span>{{ productInfo.color }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>尺寸：</label>
            <span>{{ productInfo.size }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>重量：</label>
            <span>{{ productInfo.weight }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>包装：</label>
            <span>{{ productInfo.package }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>创建时间：</label>
            <span>{{ productInfo.createTime }}</span>
          </div>
        </el-col>
      </el-row>
      
      <div class="info-section">
        <h4>产品描述</h4>
        <p>{{ productInfo.description }}</p>
      </div>
      
      <div class="info-section">
        <h4>产品特点</h4>
        <ul>
          <li v-for="feature in productInfo.features" :key="feature">{{ feature }}</li>
        </ul>
      </div>
    </el-card>

    <!-- 统计信息 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon stock">
              <el-icon><Box /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ productStats.stockQuantity }}</div>
              <div class="stat-label">当前库存</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon price">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">¥{{ productStats.currentPrice.toLocaleString() }}</div>
              <div class="stat-label">当前价格</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon sales">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ productStats.salesCount }}</div>
              <div class="stat-label">销售数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon quotation">
              <el-icon><Tickets /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ productStats.quotationCount }}</div>
              <div class="stat-label">报价次数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细信息标签页 -->
    <el-card class="custom-card">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 规格参数 -->
        <el-tab-pane label="规格参数" name="specs">
          <el-table :data="specifications" stripe>
            <el-table-column prop="name" label="参数名称" width="200" />
            <el-table-column prop="value" label="参数值" />
            <el-table-column prop="unit" label="单位" width="100" />
            <el-table-column prop="description" label="说明" />
          </el-table>
        </el-tab-pane>

        <!-- 库存信息 -->
        <el-tab-pane label="库存信息" name="inventory">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
              <div class="inventory-card">
                <div class="inventory-header">
                  <h4>总库存</h4>
                  <el-tag type="primary">{{ inventoryInfo.totalStock }}</el-tag>
                </div>
                <div class="inventory-details">
                  <div class="detail-item">
                    <span>可用库存：</span>
                    <span class="value">{{ inventoryInfo.availableStock }}</span>
                  </div>
                  <div class="detail-item">
                    <span>预留库存：</span>
                    <span class="value">{{ inventoryInfo.reservedStock }}</span>
                  </div>
                  <div class="detail-item">
                    <span>在途库存：</span>
                    <span class="value">{{ inventoryInfo.inTransitStock }}</span>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
              <div class="inventory-card">
                <div class="inventory-header">
                  <h4>库存预警</h4>
                  <el-tag :type="inventoryInfo.stockWarning ? 'danger' : 'success'">
                    {{ inventoryInfo.stockWarning ? '库存不足' : '库存充足' }}
                  </el-tag>
                </div>
                <div class="inventory-details">
                  <div class="detail-item">
                    <span>安全库存：</span>
                    <span class="value">{{ inventoryInfo.safetyStock }}</span>
                  </div>
                  <div class="detail-item">
                    <span>最低库存：</span>
                    <span class="value">{{ inventoryInfo.minStock }}</span>
                  </div>
                  <div class="detail-item">
                    <span>最高库存：</span>
                    <span class="value">{{ inventoryInfo.maxStock }}</span>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
              <div class="inventory-card">
                <div class="inventory-header">
                  <h4>库存成本</h4>
                  <el-tag type="warning">¥{{ inventoryInfo.stockValue.toLocaleString() }}</el-tag>
                </div>
                <div class="inventory-details">
                  <div class="detail-item">
                    <span>平均成本：</span>
                    <span class="value">¥{{ inventoryInfo.avgCost.toLocaleString() }}</span>
                  </div>
                  <div class="detail-item">
                    <span>最新成本：</span>
                    <span class="value">¥{{ inventoryInfo.latestCost.toLocaleString() }}</span>
                  </div>
                  <div class="detail-item">
                    <span>成本变化：</span>
                    <span class="value" :class="inventoryInfo.costChange >= 0 ? 'positive' : 'negative'">
                      {{ inventoryInfo.costChange >= 0 ? '+' : '' }}{{ inventoryInfo.costChange }}%
                    </span>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
              <div class="inventory-card">
                <div class="inventory-header">
                  <h4>库存周转</h4>
                  <el-tag type="info">{{ inventoryInfo.turnoverDays }}天</el-tag>
                </div>
                <div class="inventory-details">
                  <div class="detail-item">
                    <span>周转率：</span>
                    <span class="value">{{ inventoryInfo.turnoverRate }}次/年</span>
                  </div>
                  <div class="detail-item">
                    <span>平均周转：</span>
                    <span class="value">{{ inventoryInfo.avgTurnover }}天</span>
                  </div>
                  <div class="detail-item">
                    <span>周转状态：</span>
                    <span class="value" :class="inventoryInfo.turnoverStatus === 'good' ? 'positive' : 'negative'">
                      {{ inventoryInfo.turnoverStatus === 'good' ? '良好' : '需关注' }}
                    </span>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!-- 价格历史 -->
        <el-tab-pane label="价格历史" name="prices">
          <div class="tab-header">
            <el-button-group>
              <el-button :type="pricePeriod === 'month' ? 'primary' : ''" @click="pricePeriod = 'month'">
                月度
              </el-button>
              <el-button :type="pricePeriod === 'quarter' ? 'primary' : ''" @click="pricePeriod = 'quarter'">
                季度
              </el-button>
              <el-button :type="pricePeriod === 'year' ? 'primary' : ''" @click="pricePeriod = 'year'">
                年度
              </el-button>
            </el-button-group>
          </div>
          
          <el-table :data="priceHistory" stripe>
            <el-table-column prop="date" label="日期" width="120" />
            <el-table-column prop="price" label="价格" width="120">
              <template #default="{ row }">
                ¥{{ row.price.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="change" label="变化" width="100">
              <template #default="{ row }">
                <span :class="row.change >= 0 ? 'positive' : 'negative'">
                  {{ row.change >= 0 ? '+' : '' }}{{ row.change }}%
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="reason" label="调整原因" />
            <el-table-column prop="operator" label="操作人" width="100" />
          </el-table>
        </el-tab-pane>

        <!-- 销售记录 -->
        <el-tab-pane label="销售记录" name="sales">
          <el-table :data="salesRecords" stripe>
            <el-table-column prop="orderNo" label="订单号" width="150">
              <template #default="{ row }">
                <el-link type="primary" @click="handleViewOrder(row)">
                  {{ row.orderNo }}
                </el-link>
              </template>
            </el-table-column>
            <el-table-column prop="orderDate" label="订单日期" width="120" />
            <el-table-column prop="customerName" label="客户名称" width="150" />
            <el-table-column prop="quantity" label="数量" width="80" />
            <el-table-column prop="unitPrice" label="单价" width="120">
              <template #default="{ row }">
                ¥{{ row.unitPrice.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="totalAmount" label="总金额" width="120">
              <template #default="{ row }">
                ¥{{ row.totalAmount.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="订单状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getOrderStatusType(row.status)" size="small">
                  {{ getOrderStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 相关文档 -->
        <el-tab-pane label="相关文档" name="documents">
          <div class="tab-header">
            <el-button type="primary" size="small" @click="handleUploadDocument">
              <el-icon><Upload /></el-icon>
              上传文档
            </el-button>
          </div>
          
          <el-row :gutter="20">
            <el-col 
              v-for="doc in documents" 
              :key="doc.id"
              :xs="24" :sm="12" :md="8" :lg="6" :xl="6"
            >
              <div class="document-item">
                <div class="document-icon">
                  <el-icon size="32" color="#409EFF"><Document /></el-icon>
                </div>
                <div class="document-info">
                  <h4>{{ doc.name }}</h4>
                  <p>{{ doc.description }}</p>
                  <div class="document-meta">
                    <span>{{ doc.uploadTime }}</span>
                    <span>{{ doc.size }}</span>
                  </div>
                </div>
                <div class="document-actions">
                  <el-button type="primary" size="small" @click="handleDownloadDocument(doc)">
                    下载
                  </el-button>
                  <el-button type="danger" size="small" @click="handleDeleteDocument(doc)">
                    删除
                  </el-button>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 响应式数据
const activeTab = ref('specs')
const pricePeriod = ref('month')

// 产品基本信息
const productInfo = reactive({
  id: 1,
  name: '智能手表 Pro Max',
  code: 'SW-2024-001',
  category: '电子产品',
  supplier: '深圳科技有限公司',
  brand: 'TechBrand',
  model: 'TB-Watch-Pro-Max',
  material: '铝合金 + 蓝宝石玻璃',
  color: '深空灰',
  size: '44mm',
  weight: '45g',
  package: '精美礼盒包装',
  status: 'active',
  description: '新一代智能手表，采用最新处理器，支持心率监测、GPS定位、运动追踪等功能。续航时间长达7天，支持无线充电。',
  features: [
    '7天超长续航',
    '心率血氧监测',
    'GPS精准定位',
    '50米防水',
    '无线充电',
    '运动模式丰富'
  ],
  createTime: '2024-01-15'
})

// 产品统计
const productStats = reactive({
  stockQuantity: 1250,
  currentPrice: 2999,
  salesCount: 856,
  quotationCount: 234
})

// 规格参数
const specifications = ref([
  { name: '屏幕尺寸', value: '1.78英寸', unit: '英寸', description: 'AMOLED高清显示屏' },
  { name: '分辨率', value: '448x368', unit: '像素', description: '视网膜级显示效果' },
  { name: '处理器', value: '双核1.4GHz', unit: 'GHz', description: '低功耗高性能' },
  { name: '内存', value: '2GB', unit: 'GB', description: 'LPDDR4高速内存' },
  { name: '存储', value: '32GB', unit: 'GB', description: 'eMMC5.1闪存' },
  { name: '电池容量', value: '450mAh', unit: 'mAh', description: '锂聚合物电池' },
  { name: '充电时间', value: '2小时', unit: '小时', description: '快速充电技术' },
  { name: '防水等级', value: 'IP68', unit: '', description: '50米防水' },
  { name: '重量', value: '45g', unit: 'g', description: '轻量化设计' },
  { name: '表带材质', value: '氟橡胶', unit: '', description: '舒适透气' }
])

// 库存信息
const inventoryInfo = reactive({
  totalStock: 1250,
  availableStock: 980,
  reservedStock: 120,
  inTransitStock: 150,
  stockWarning: false,
  safetyStock: 200,
  minStock: 100,
  maxStock: 2000,
  stockValue: 3748750,
  avgCost: 2999,
  latestCost: 2850,
  costChange: -4.97,
  turnoverDays: 45,
  turnoverRate: 8.1,
  avgTurnover: 42,
  turnoverStatus: 'good'
})

// 价格历史
const priceHistory = ref([
  { date: '2024-12-01', price: 2999, change: 0, reason: '价格调整', operator: '张三' },
  { date: '2024-11-01', price: 2899, change: -3.33, reason: '促销活动', operator: '李四' },
  { date: '2024-10-01', price: 2999, change: 3.45, reason: '成本上涨', operator: '王五' },
  { date: '2024-09-01', price: 2899, change: -3.33, reason: '促销活动', operator: '赵六' },
  { date: '2024-08-01', price: 2999, change: 3.45, reason: '价格调整', operator: '周七' }
])

// 销售记录
const salesRecords = ref([
  {
    id: 1,
    orderNo: 'SO202412001',
    orderDate: '2024-12-20',
    customerName: 'ABC贸易公司',
    quantity: 50,
    unitPrice: 2999,
    totalAmount: 149950,
    status: 'completed'
  },
  {
    id: 2,
    orderNo: 'SO202411015',
    orderDate: '2024-11-15',
    customerName: 'XYZ进出口公司',
    quantity: 30,
    unitPrice: 2899,
    totalAmount: 86970,
    status: 'shipped'
  }
])

// 文档资料
const documents = ref([
  {
    id: 1,
    name: '产品说明书.pdf',
    description: '详细的产品使用说明和技术参数',
    uploadTime: '2024-12-15',
    size: '2.8MB'
  },
  {
    id: 2,
    name: '产品图片.zip',
    description: '高清产品图片和宣传素材',
    uploadTime: '2024-12-18',
    size: '15.6MB'
  },
  {
    id: 3,
    name: '质量检测报告.pdf',
    description: '第三方质量检测认证报告',
    uploadTime: '2024-12-20',
    size: '1.2MB'
  }
])

// 方法
const getStatusType = (status) => {
  const typeMap = {
    'active': 'success',
    'inactive': 'info',
    'discontinued': 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    'active': '在售',
    'inactive': '停售',
    'discontinued': '已下架'
  }
  return textMap[status] || status
}

const getOrderStatusType = (status) => {
  const typeMap = {
    'draft': 'info',
    'processing': 'warning',
    'shipped': 'primary',
    'completed': 'success',
    'cancelled': 'danger'
  }
  return typeMap[status] || 'info'
}

const getOrderStatusText = (status) => {
  const textMap = {
    'draft': '草稿',
    'processing': '处理中',
    'shipped': '已发货',
    'completed': '已完成',
    'cancelled': '已取消'
  }
  return textMap[status] || status
}

const handleEdit = () => {
  ElMessage.info('编辑产品功能开发中...')
}

const handleCreateQuotation = () => {
  ElMessage.info('创建报价功能开发中...')
}

const handlePrint = () => {
  ElMessage.info('打印产品详情功能开发中...')
}

const handleViewOrder = (order) => {
  ElMessage.info(`查看订单：${order.orderNo}`)
}

const handleUploadDocument = () => {
  ElMessage.info('上传文档功能开发中...')
}

const handleDownloadDocument = (doc) => {
  ElMessage.info(`下载文档：${doc.name}`)
}

const handleDeleteDocument = (doc) => {
  ElMessageBox.confirm(
    `确定要删除文档 "${doc.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('文档已删除')
  })
}

onMounted(() => {
  // 从路由参数获取产品ID
  const productId = route.params.id
  console.log('产品ID:', productId)
})
</script>

<style lang="scss" scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .header-left {
    display: flex;
    align-items: center;
    gap: 15px;
    
    h1 {
      margin: 0;
    }
  }
  
  .header-actions {
    display: flex;
    gap: 10px;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item {
  margin-bottom: 15px;
  
  label {
    font-weight: 600;
    color: #606266;
    margin-right: 8px;
  }
  
  span {
    color: #303133;
  }
}

.info-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  
  h4 {
    margin: 0 0 10px 0;
    color: #606266;
    font-size: 14px;
  }
  
  p {
    margin: 0;
    color: #303133;
    line-height: 1.6;
  }
  
  ul {
    margin: 0;
    padding-left: 20px;
    
    li {
      margin-bottom: 5px;
      color: #303133;
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
        
        &.stock {
          background: linear-gradient(135deg, #409EFF 0%, #2D8CF0 100%);
        }
        
        &.price {
          background: linear-gradient(135deg, #67C23A 0%, #59B329 100%);
        }
        
        &.sales {
          background: linear-gradient(135deg, #E6A23C 0%, #D48806 100%);
        }
        
        &.quotation {
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
      }
    }
  }
}

.tab-header {
  margin-bottom: 20px;
}

.inventory-card {
  background: #f8f9ff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  
  .inventory-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    
    h4 {
      margin: 0;
      font-size: 16px;
      color: #303133;
    }
  }
  
  .inventory-details {
    .detail-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: 8px;
      font-size: 14px;
      
      .value {
        font-weight: 600;
        color: #303133;
        
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

.document-item {
  background: #f8f9ff;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  
  .document-icon {
    flex-shrink: 0;
  }
  
  .document-info {
    flex: 1;
    
    h4 {
      margin: 0 0 5px 0;
      font-size: 16px;
      color: #303133;
    }
    
    p {
      margin: 0 0 8px 0;
      font-size: 14px;
      color: #606266;
    }
    
    .document-meta {
      font-size: 12px;
      color: #909399;
      
      span {
        margin-right: 15px;
      }
    }
  }
  
  .document-actions {
    flex-shrink: 0;
    display: flex;
    gap: 8px;
  }
}

.positive {
  color: #67C23A;
}

.negative {
  color: #F56C6C;
}

// 响应式设计
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
    
    .header-actions {
      justify-content: center;
    }
  }
  
  .document-item {
    flex-direction: column;
    text-align: center;
    
    .document-actions {
      margin-top: 10px;
    }
  }
}
</style> 