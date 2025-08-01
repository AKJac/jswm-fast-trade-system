<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-left">
        <el-button @click="$router.go(-1)" text>
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <h1>报价详情</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleEdit">
          <el-icon><Edit /></el-icon>
          编辑报价
        </el-button>
        <el-button type="success" @click="handleSend">
          <el-icon><Message /></el-icon>
          发送报价
        </el-button>
        <el-button type="warning" @click="handlePrint">
          <el-icon><Printer /></el-icon>
          打印报价
        </el-button>
        <el-button type="danger" @click="handleDelete">
          <el-icon><Delete /></el-icon>
          删除报价
        </el-button>
      </div>
    </div>

    <!-- 报价基本信息 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
          <el-tag :type="getStatusType(quotationInfo.status)" size="small">
            {{ getStatusText(quotationInfo.status) }}
          </el-tag>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>报价单号：</label>
            <span>{{ quotationInfo.quotationNo }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>客户名称：</label>
            <span>{{ quotationInfo.customerName }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>联系人：</label>
            <span>{{ quotationInfo.contactPerson }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>联系电话：</label>
            <span>{{ quotationInfo.contactPhone }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>报价日期：</label>
            <span>{{ quotationInfo.quotationDate }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>有效期至：</label>
            <span>{{ quotationInfo.validUntil }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>报价人：</label>
            <span>{{ quotationInfo.quoter }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>审核人：</label>
            <span>{{ quotationInfo.approver || '待审核' }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>付款条件：</label>
            <span>{{ quotationInfo.paymentTerms }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>交货期：</label>
            <span>{{ quotationInfo.deliveryTime }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>运输方式：</label>
            <span>{{ quotationInfo.shippingMethod }}</span>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="info-item">
            <label>币种：</label>
            <span>{{ quotationInfo.currency }}</span>
          </div>
        </el-col>
      </el-row>
      
      <div class="info-section">
        <h4>备注说明</h4>
        <p>{{ quotationInfo.remarks || '无特殊说明' }}</p>
      </div>
    </el-card>

    <!-- 价格汇总 -->
    <el-row :gutter="20" class="summary-row">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        <el-card class="summary-card">
          <div class="summary-item">
            <div class="summary-icon subtotal">
              <el-icon><Money /></el-icon>
            </div>
            <div class="summary-content">
              <div class="summary-value">¥{{ priceSummary.subtotal.toLocaleString() }}</div>
              <div class="summary-label">商品小计</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        <el-card class="summary-card">
          <div class="summary-item">
            <div class="summary-icon discount">
              <el-icon><Discount /></el-icon>
            </div>
            <div class="summary-content">
              <div class="summary-value">-¥{{ priceSummary.discount.toLocaleString() }}</div>
              <div class="summary-label">折扣金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        <el-card class="summary-card">
          <div class="summary-item">
            <div class="summary-icon shipping">
              <el-icon><Van /></el-icon>
            </div>
            <div class="summary-content">
              <div class="summary-value">¥{{ priceSummary.shipping.toLocaleString() }}</div>
              <div class="summary-label">运费</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
        <el-card class="summary-card">
          <div class="summary-item">
            <div class="summary-icon total">
              <el-icon><Wallet /></el-icon>
            </div>
            <div class="summary-content">
              <div class="summary-value">¥{{ priceSummary.total.toLocaleString() }}</div>
              <div class="summary-label">报价总额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细信息标签页 -->
    <el-card class="custom-card">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 产品清单 -->
        <el-tab-pane label="产品清单" name="products">
          <el-table :data="productList" stripe>
            <el-table-column prop="productCode" label="产品货号" width="150" />
            <el-table-column prop="productName" label="产品名称" min-width="200" />
            <el-table-column prop="specification" label="规格参数" width="150" />
            <el-table-column prop="unit" label="单位" width="80" />
            <el-table-column prop="quantity" label="数量" width="100" />
            <el-table-column prop="unitPrice" label="单价" width="120">
              <template #default="{ row }">
                ¥{{ row.unitPrice.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="amount" label="金额" width="120">
              <template #default="{ row }">
                ¥{{ row.amount.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
          </el-table>
        </el-tab-pane>

        <!-- 价格明细 -->
        <el-tab-pane label="价格明细" name="pricing">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="商品小计">
              ¥{{ priceDetails.subtotal.toLocaleString() }}
            </el-descriptions-item>
            <el-descriptions-item label="折扣率">
              {{ priceDetails.discountRate }}%
            </el-descriptions-item>
            <el-descriptions-item label="折扣金额">
              -¥{{ priceDetails.discountAmount.toLocaleString() }}
            </el-descriptions-item>
            <el-descriptions-item label="折扣后金额">
              ¥{{ priceDetails.discountedAmount.toLocaleString() }}
            </el-descriptions-item>
            <el-descriptions-item label="运费">
              ¥{{ priceDetails.shipping.toLocaleString() }}
            </el-descriptions-item>
            <el-descriptions-item label="保险费">
              ¥{{ priceDetails.insurance.toLocaleString() }}
            </el-descriptions-item>
            <el-descriptions-item label="税费">
              ¥{{ priceDetails.tax.toLocaleString() }}
            </el-descriptions-item>
            <el-descriptions-item label="其他费用">
              ¥{{ priceDetails.other.toLocaleString() }}
            </el-descriptions-item>
            <el-descriptions-item label="报价总额" :span="2">
              <span class="total-amount">¥{{ priceDetails.total.toLocaleString() }}</span>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 审批流程 -->
        <el-tab-pane label="审批流程" name="approval">
          <el-timeline>
            <el-timeline-item
              v-for="step in approvalSteps"
              :key="step.id"
              :timestamp="step.time"
              :type="getStepType(step.status)"
            >
              <h4>{{ step.title }}</h4>
              <p>{{ step.description }}</p>
              <div class="step-footer">
                <span class="step-person">处理人：{{ step.person }}</span>
                <span class="step-result">结果：{{ step.result }}</span>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-tab-pane>

        <!-- 操作记录 -->
        <el-tab-pane label="操作记录" name="logs">
          <el-table :data="operationLogs" stripe>
            <el-table-column prop="time" label="操作时间" width="180" />
            <el-table-column prop="operator" label="操作人" width="120" />
            <el-table-column prop="action" label="操作类型" width="120" />
            <el-table-column prop="description" label="操作描述" />
            <el-table-column prop="ip" label="IP地址" width="120" />
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
const activeTab = ref('products')

// 报价基本信息
const quotationInfo = reactive({
  id: 1,
  quotationNo: 'QT202412025',
  customerName: 'ABC贸易公司',
  contactPerson: 'John Smith',
  contactPhone: '+1-555-123-4567',
  quotationDate: '2024-12-25',
  validUntil: '2025-01-25',
  quoter: '张三',
  approver: '李四',
  paymentTerms: 'T/T 30天',
  deliveryTime: '收到订单后30天内',
  shippingMethod: '海运',
  currency: 'USD',
  status: 'sent',
  remarks: '此报价单有效期为30天，如有疑问请及时联系。'
})

// 价格汇总
const priceSummary = reactive({
  subtotal: 120000,
  discount: 6000,
  shipping: 3000,
  total: 117000
})

// 产品清单
const productList = ref([
  {
    id: 1,
    productCode: 'SW-2024-001',
    productName: '智能手表 Pro Max',
    specification: '44mm 深空灰',
    unit: '个',
    quantity: 100,
    unitPrice: 2999,
    amount: 299900,
    remark: '含无线充电器'
  },
  {
    id: 2,
    productCode: 'PH-2024-002',
    productName: '无线充电器',
    specification: '15W 快充',
    unit: '个',
    quantity: 100,
    unitPrice: 199,
    amount: 19900,
    remark: '兼容多种设备'
  }
])

// 价格明细
const priceDetails = reactive({
  subtotal: 120000,
  discountRate: 5,
  discountAmount: 6000,
  discountedAmount: 114000,
  shipping: 3000,
  insurance: 500,
  tax: 0,
  other: 0,
  total: 117000
})

// 审批流程
const approvalSteps = ref([
  {
    id: 1,
    time: '2024-12-25 10:30',
    title: '创建报价单',
    description: '销售员张三创建了报价单QT202412025',
    person: '张三',
    result: '已创建',
    status: 'success'
  },
  {
    id: 2,
    time: '2024-12-25 14:20',
    title: '部门审核',
    description: '销售经理李四审核通过报价单',
    person: '李四',
    result: '已通过',
    status: 'success'
  },
  {
    id: 3,
    time: '2024-12-25 16:00',
    title: '发送报价',
    description: '报价单已发送给客户ABC贸易公司',
    person: '张三',
    result: '已发送',
    status: 'success'
  }
])

// 操作记录
const operationLogs = ref([
  {
    id: 1,
    time: '2024-12-25 16:00',
    operator: '张三',
    action: '发送报价',
    description: '通过邮件发送报价单给客户',
    ip: '192.168.1.100'
  },
  {
    id: 2,
    time: '2024-12-25 14:20',
    operator: '李四',
    action: '审核通过',
    description: '部门经理审核通过报价单',
    ip: '192.168.1.101'
  },
  {
    id: 3,
    time: '2024-12-25 10:30',
    operator: '张三',
    action: '创建报价',
    description: '创建新的报价单',
    ip: '192.168.1.100'
  }
])

// 文档资料
const documents = ref([
  {
    id: 1,
    name: '报价单.pdf',
    description: '正式报价单文档',
    uploadTime: '2024-12-25',
    size: '1.2MB'
  },
  {
    id: 2,
    name: '产品目录.pdf',
    description: '详细产品目录和参数',
    uploadTime: '2024-12-25',
    size: '5.8MB'
  }
])

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

const getStepType = (status) => {
  const typeMap = {
    'success': 'success',
    'warning': 'warning',
    'info': 'info'
  }
  return typeMap[status] || 'info'
}

const handleEdit = () => {
  ElMessage.info('编辑报价功能开发中...')
}

const handleSend = () => {
  ElMessageBox.confirm(
    '确定要发送此报价单吗？',
    '确认发送',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('报价单已发送')
  })
}

const handlePrint = () => {
  ElMessage.info('打印报价单功能开发中...')
}

const handleDelete = () => {
  ElMessageBox.confirm(
    '确定要删除此报价单吗？删除后无法恢复！',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('报价单已删除')
    router.go(-1)
  })
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
  // 从路由参数获取报价ID
  const quotationId = route.params.id
  console.log('报价ID:', quotationId)
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

.summary-row {
  margin-bottom: 20px;
  
  .summary-card {
    :deep(.el-card__body) {
      padding: 20px;
    }
    
    .summary-item {
      display: flex;
      align-items: center;
      
      .summary-icon {
        width: 50px;
        height: 50px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
        color: white;
        margin-right: 15px;
        
        &.subtotal {
          background: linear-gradient(135deg, #409EFF 0%, #2D8CF0 100%);
        }
        
        &.discount {
          background: linear-gradient(135deg, #67C23A 0%, #59B329 100%);
        }
        
        &.shipping {
          background: linear-gradient(135deg, #E6A23C 0%, #D48806 100%);
        }
        
        &.total {
          background: linear-gradient(135deg, #F56C6C 0%, #E74C3C 100%);
        }
      }
      
      .summary-content {
        flex: 1;
        
        .summary-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          line-height: 1.2;
        }
        
        .summary-label {
          font-size: 14px;
          color: #909399;
          margin-top: 5px;
        }
      }
    }
  }
}

.total-amount {
  font-size: 18px;
  font-weight: bold;
  color: #F56C6C;
}

.tab-header {
  margin-bottom: 20px;
}

.step-footer {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
  
  .step-person {
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