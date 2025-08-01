<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-left">
        <el-button @click="$router.go(-1)" text>
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h1>客户详情</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleEdit">
          <el-icon><Edit /></el-icon>
          编辑客户
        </el-button>
        <el-button type="success" @click="handleCreateQuotation">
          <el-icon><Plus /></el-icon>
          新建报价
        </el-button>
        <el-button type="warning" @click="handleCreateOrder">
          <el-icon><Document /></el-icon>
          新建订单
        </el-button>
      </div>
    </div>

    <!-- 客户基本信息 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
          <el-tag :type="getLevelType(customerInfo.level)" size="small">
            {{ getLevelText(customerInfo.level) }}
          </el-tag>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>客户名称：</label>
            <span>{{ customerInfo.name }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>英文名称：</label>
            <span>{{ customerInfo.englishName }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>国家/地区：</label>
            <span>{{ customerInfo.country }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>客户类型：</label>
            <span>{{ customerInfo.type }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>联系人：</label>
            <span>{{ customerInfo.contactPerson }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>联系电话：</label>
            <span>{{ customerInfo.phone }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>邮箱地址：</label>
            <span>{{ customerInfo.email }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>跟进进度：</label>
            <el-tag :type="getProgressType(customerInfo.progress)" size="small">
              {{ getProgressText(customerInfo.progress) }}
            </el-tag>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>创建时间：</label>
            <span>{{ customerInfo.createTime }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>最后跟进：</label>
            <span>{{ customerInfo.lastFollowTime }}</span>
          </div>
        </el-col>
      </el-row>
      
      <div class="info-section">
        <h4>公司地址</h4>
        <p>{{ customerInfo.address }}</p>
      </div>
      
      <div class="info-section">
        <h4>备注信息</h4>
        <p>{{ customerInfo.remarks || '暂无备注' }}</p>
      </div>
    </el-card>

    <!-- 统计信息 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon order">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ customerStats.orderCount }}</div>
              <div class="stat-label">订单总数</div>
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
              <div class="stat-value">{{ customerStats.quotationCount }}</div>
              <div class="stat-label">报价总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon amount">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">¥{{ customerStats.totalAmount.toLocaleString() }}</div>
              <div class="stat-label">交易总额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon follow">
              <el-icon><ChatDotRound /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ customerStats.followCount }}</div>
              <div class="stat-label">跟进次数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细信息标签页 -->
    <el-card class="custom-card">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 联系记录 -->
        <el-tab-pane label="联系记录" name="contacts">
          <div class="tab-header">
            <el-button type="primary" size="small" @click="handleAddContact">
              <el-icon><Plus /></el-icon>
              添加记录
            </el-button>
          </div>
          
          <el-timeline>
            <el-timeline-item
              v-for="contact in contactRecords"
              :key="contact.id"
              :timestamp="contact.time"
              :type="getContactType(contact.type)"
            >
              <h4>{{ contact.title }}</h4>
              <p>{{ contact.content }}</p>
              <div class="contact-footer">
                <span class="contact-person">跟进人：{{ contact.person }}</span>
                <span class="contact-result">结果：{{ contact.result }}</span>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>

        <!-- 订单历史 -->
        <el-tab-pane label="订单历史" name="orders">
          <el-table :data="orderHistory" stripe>
            <el-table-column prop="orderNo" label="订单号" width="150">
              <template #default="{ row }">
                <el-link type="primary" @click="handleViewOrder(row)">
                  {{ row.orderNo }}
                </el-link>
              </template>
            </el-table-column>
            <el-table-column prop="orderDate" label="订单日期" width="120" />
            <el-table-column prop="productCount" label="产品数量" width="100" />
            <el-table-column prop="totalAmount" label="订单金额" width="120">
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
            <el-table-column prop="paymentStatus" label="付款状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getPaymentStatusType(row.paymentStatus)" size="small">
                  {{ getPaymentStatusText(row.paymentStatus) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="handleViewOrder(row)">
                  查看
                </el-button>
                <el-button type="success" size="small" @click="handlePrintOrder(row)">
                  打印
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 报价历史 -->
        <el-tab-pane label="报价历史" name="quotations">
          <el-table :data="quotationHistory" stripe>
            <el-table-column prop="quotationNo" label="报价单号" width="150">
              <template #default="{ row }">
                <el-link type="primary" @click="handleViewQuotation(row)">
                  {{ row.quotationNo }}
                </el-link>
              </template>
            </el-table-column>
            <el-table-column prop="quotationDate" label="报价日期" width="120" />
            <el-table-column prop="productCount" label="产品数量" width="100" />
            <el-table-column prop="totalAmount" label="报价金额" width="120">
              <template #default="{ row }">
                ¥{{ row.totalAmount.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="validUntil" label="有效期至" width="120" />
            <el-table-column prop="status" label="报价状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getQuotationStatusType(row.status)" size="small">
                  {{ getQuotationStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="handleViewQuotation(row)">
                  查看
                </el-button>
                <el-button type="success" size="small" @click="handleSendQuotation(row)">
                  发送
                </el-button>
                <el-button type="warning" size="small" @click="handlePrintQuotation(row)">
                  打印
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 文档资料 -->
        <el-tab-pane label="文档资料" name="documents">
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
const activeTab = ref('contacts')

// 客户基本信息
const customerInfo = reactive({
  id: 1,
  name: 'ABC贸易公司',
  englishName: 'ABC Trading Co., Ltd.',
  country: '美国',
  type: '贸易公司',
  level: 'VIP',
  contactPerson: 'John Smith',
  phone: '+1-555-123-4567',
  email: 'john.smith@abc.com',
  address: '123 Business Street, New York, NY 10001, USA',
  progress: 'Deal',
  remarks: '重要客户，年采购额超过100万美元，主要采购电子产品。',
  createTime: '2024-01-15',
  lastFollowTime: '2024-12-20'
})

// 客户统计
const customerStats = reactive({
  orderCount: 15,
  quotationCount: 28,
  totalAmount: 1250000,
  followCount: 45
})

// 联系记录
const contactRecords = ref([
  {
    id: 1,
    time: '2024-12-20 14:30',
    type: 'phone',
    title: '电话沟通',
    content: '与客户John Smith电话沟通，确认新订单需求，客户对产品质量非常满意。',
    person: '张三',
    result: '客户确认下单'
  },
  {
    id: 2,
    time: '2024-12-18 10:00',
    type: 'email',
    title: '邮件跟进',
    content: '发送产品目录和最新报价单，客户回复表示感兴趣。',
    person: '李四',
    result: '客户要求详细报价'
  },
  {
    id: 3,
    time: '2024-12-15 16:00',
    type: 'meeting',
    title: '视频会议',
    content: '与客户进行视频会议，展示新产品功能，客户表示满意。',
    person: '王五',
    result: '达成初步合作意向'
  }
])

// 订单历史
const orderHistory = ref([
  {
    id: 1,
    orderNo: 'SO202412001',
    orderDate: '2024-12-20',
    productCount: 5,
    totalAmount: 85000,
    status: 'processing',
    paymentStatus: 'paid'
  },
  {
    id: 2,
    orderNo: 'SO202411015',
    orderDate: '2024-11-15',
    productCount: 3,
    totalAmount: 45000,
    status: 'completed',
    paymentStatus: 'paid'
  }
])

// 报价历史
const quotationHistory = ref([
  {
    id: 1,
    quotationNo: 'QT202412025',
    quotationDate: '2024-12-25',
    productCount: 8,
    totalAmount: 120000,
    validUntil: '2025-01-25',
    status: 'sent'
  },
  {
    id: 2,
    quotationNo: 'QT202412020',
    quotationDate: '2024-12-20',
    productCount: 6,
    totalAmount: 95000,
    validUntil: '2025-01-20',
    status: 'confirmed'
  }
])

// 文档资料
const documents = ref([
  {
    id: 1,
    name: '公司营业执照.pdf',
    description: '客户公司营业执照扫描件',
    uploadTime: '2024-12-15',
    size: '2.5MB'
  },
  {
    id: 2,
    name: '产品需求清单.xlsx',
    description: '客户产品需求详细清单',
    uploadTime: '2024-12-18',
    size: '1.8MB'
  }
])

// 方法
const getLevelType = (level) => {
  const typeMap = {
    'VIP': 'danger',
    'Important': 'warning',
    'Normal': 'info',
    'Potential': 'success'
  }
  return typeMap[level] || 'info'
}

const getLevelText = (level) => {
  const textMap = {
    'VIP': 'VIP客户',
    'Important': '重要客户',
    'Normal': '普通客户',
    'Potential': '潜在客户'
  }
  return textMap[level] || level
}

const getProgressType = (progress) => {
  const typeMap = {
    'Initial': 'info',
    'Interested': 'warning',
    'Quoting': 'primary',
    'Deal': 'success'
  }
  return typeMap[progress] || 'info'
}

const getProgressText = (progress) => {
  const textMap = {
    'Initial': '初步接触',
    'Interested': '意向客户',
    'Quoting': '报价中',
    'Deal': '已成交'
  }
  return textMap[progress] || progress
}

const getContactType = (type) => {
  const typeMap = {
    'phone': 'primary',
    'email': 'success',
    'meeting': 'warning',
    'visit': 'danger'
  }
  return typeMap[type] || 'info'
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

const getPaymentStatusType = (status) => {
  const typeMap = {
    'unpaid': 'danger',
    'partial': 'warning',
    'paid': 'success'
  }
  return typeMap[status] || 'info'
}

const getPaymentStatusText = (status) => {
  const textMap = {
    'unpaid': '未付款',
    'partial': '部分付款',
    'paid': '已付款'
  }
  return textMap[status] || status
}

const getQuotationStatusType = (status) => {
  const typeMap = {
    'draft': 'info',
    'sent': 'warning',
    'confirmed': 'success',
    'rejected': 'danger',
    'expired': 'danger'
  }
  return typeMap[status] || 'info'
}

const getQuotationStatusText = (status) => {
  const textMap = {
    'draft': '草稿',
    'sent': '已发送',
    'confirmed': '已确认',
    'rejected': '已拒绝',
    'expired': '已过期'
  }
  return textMap[status] || status
}

const handleEdit = () => {
  ElMessage.info('编辑客户功能开发中...')
}

const handleCreateQuotation = () => {
  ElMessage.info('新建报价功能开发中...')
}

const handleCreateOrder = () => {
  ElMessage.info('新建订单功能开发中...')
}

const handleAddContact = () => {
  ElMessage.info('添加联系记录功能开发中...')
}

const handleViewOrder = (order) => {
  ElMessage.info(`查看订单：${order.orderNo}`)
}

const handlePrintOrder = (order) => {
  ElMessage.info(`打印订单：${order.orderNo}`)
}

const handleViewQuotation = (quotation) => {
  ElMessage.info(`查看报价：${quotation.quotationNo}`)
}

const handleSendQuotation = (quotation) => {
  ElMessage.info(`发送报价：${quotation.quotationNo}`)
}

const handlePrintQuotation = (quotation) => {
  ElMessage.info(`打印报价：${quotation.quotationNo}`)
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
  // 从路由参数获取客户ID
  const customerId = route.params.id
  console.log('客户ID:', customerId)
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
        
        &.order {
          background: linear-gradient(135deg, #409EFF 0%, #2D8CF0 100%);
        }
        
        &.quotation {
          background: linear-gradient(135deg, #67C23A 0%, #59B329 100%);
        }
        
        &.amount {
          background: linear-gradient(135deg, #E6A23C 0%, #D48806 100%);
        }
        
        &.follow {
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

.contact-footer {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
  
  .contact-person {
    margin-right: 15px;
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