<template>
  <div class="page-container">
    <div class="page-header">
      <h1>数据导入导出</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>数据导入导出</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 导入功能 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>数据导入</span>
          <el-button type="primary" size="small" @click="downloadTemplate">
            <el-icon><Download /></el-icon>
            下载模板
          </el-button>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="import-section">
            <h4>客户数据导入</h4>
            <el-upload
              class="upload-demo"
              drag
              :action="uploadUrl"
              :headers="uploadHeaders"
              :data="{ type: 'customer' }"
              :on-success="handleCustomerImportSuccess"
              :on-error="handleImportError"
              :before-upload="beforeUpload"
              accept=".xlsx,.xls"
            >
              <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
              <div class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  只能上传 xlsx/xls 文件，且不超过 10MB
                </div>
              </template>
            </el-upload>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="import-section">
            <h4>产品数据导入</h4>
            <el-upload
              class="upload-demo"
              drag
              :action="uploadUrl"
              :headers="uploadHeaders"
              :data="{ type: 'product' }"
              :on-success="handleProductImportSuccess"
              :on-error="handleImportError"
              :before-upload="beforeUpload"
              accept=".xlsx,.xls"
            >
              <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
              <div class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  只能上传 xlsx/xls 文件，且不超过 10MB
                </div>
              </template>
            </el-upload>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="import-section">
            <h4>订单数据导入</h4>
            <el-upload
              class="upload-demo"
              drag
              :action="uploadUrl"
              :headers="uploadHeaders"
              :data="{ type: 'order' }"
              :on-success="handleOrderImportSuccess"
              :on-error="handleImportError"
              :before-upload="beforeUpload"
              accept=".xlsx,.xls"
            >
              <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
              <div class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  只能上传 xlsx/xls 文件，且不超过 10MB
                </div>
              </template>
            </el-upload>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="import-section">
            <h4>报价数据导入</h4>
            <el-upload
              class="upload-demo"
              drag
              :action="uploadUrl"
              :headers="uploadHeaders"
              :data="{ type: 'quotation' }"
              :on-success="handleQuotationImportSuccess"
              :on-error="handleImportError"
              :before-upload="beforeUpload"
              accept=".xlsx,.xls"
            >
              <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
              <div class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  只能上传 xlsx/xls 文件，且不超过 10MB
                </div>
              </template>
            </el-upload>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 导出功能 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>数据导出</span>
          <el-button type="success" size="small" @click="handleBatchExport">
            <el-icon><Download /></el-icon>
            批量导出
          </el-button>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="export-section">
            <h4>客户数据导出</h4>
            <div class="export-options">
              <el-checkbox v-model="exportOptions.customer.includeAll" label="包含所有字段" />
              <el-checkbox v-model="exportOptions.customer.includeHistory" label="包含历史记录" />
              <el-date-picker
                v-model="exportOptions.customer.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 100%; margin-top: 10px;"
              />
              <el-button type="primary" @click="handleExportCustomer" style="width: 100%; margin-top: 10px;">
                导出客户数据
              </el-button>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="export-section">
            <h4>产品数据导出</h4>
            <div class="export-options">
              <el-checkbox v-model="exportOptions.product.includeAll" label="包含所有字段" />
              <el-checkbox v-model="exportOptions.product.includeInventory" label="包含库存信息" />
              <el-checkbox v-model="exportOptions.product.includePrice" label="包含价格信息" />
              <el-button type="primary" @click="handleExportProduct" style="width: 100%; margin-top: 10px;">
                导出产品数据
              </el-button>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="export-section">
            <h4>订单数据导出</h4>
            <div class="export-options">
              <el-checkbox v-model="exportOptions.order.includeAll" label="包含所有字段" />
              <el-checkbox v-model="exportOptions.order.includeItems" label="包含订单明细" />
              <el-date-picker
                v-model="exportOptions.order.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 100%; margin-top: 10px;"
              />
              <el-button type="primary" @click="handleExportOrder" style="width: 100%; margin-top: 10px;">
                导出订单数据
              </el-button>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="6">
          <div class="export-section">
            <h4>报价数据导出</h4>
            <div class="export-options">
              <el-checkbox v-model="exportOptions.quotation.includeAll" label="包含所有字段" />
              <el-checkbox v-model="exportOptions.quotation.includeProducts" label="包含产品清单" />
              <el-date-picker
                v-model="exportOptions.quotation.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 100%; margin-top: 10px;"
              />
              <el-button type="primary" @click="handleExportQuotation" style="width: 100%; margin-top: 10px;">
                导出报价数据
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 导入历史 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>导入历史</span>
          <el-button type="primary" size="small" @click="refreshImportHistory">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      
      <el-table :data="importHistory" stripe>
        <el-table-column prop="time" label="导入时间" width="180" />
        <el-table-column prop="type" label="数据类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getImportTypeTag(row.type)" size="small">
              {{ getImportTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileName" label="文件名" min-width="200" />
        <el-table-column prop="totalCount" label="总记录数" width="100" />
        <el-table-column prop="successCount" label="成功数" width="100">
          <template #default="{ row }">
            <span class="success-count">{{ row.successCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="failCount" label="失败数" width="100">
          <template #default="{ row }">
            <span class="fail-count">{{ row.failCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getImportStatusType(row.status)" size="small">
              {{ getImportStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="120" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleViewImportDetail(row)">
              查看详情
            </el-button>
            <el-button type="danger" size="small" @click="handleDeleteImport(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 导入详情对话框 -->
    <el-dialog
      v-model="importDetailVisible"
      title="导入详情"
      width="800px"
    >
      <div v-if="importDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="导入时间">{{ importDetail.time }}</el-descriptions-item>
          <el-descriptions-item label="数据类型">{{ getImportTypeText(importDetail.type) }}</el-descriptions-item>
          <el-descriptions-item label="文件名">{{ importDetail.fileName }}</el-descriptions-item>
          <el-descriptions-item label="操作人">{{ importDetail.operator }}</el-descriptions-item>
          <el-descriptions-item label="总记录数">{{ importDetail.totalCount }}</el-descriptions-item>
          <el-descriptions-item label="成功数">{{ importDetail.successCount }}</el-descriptions-item>
          <el-descriptions-item label="失败数">{{ importDetail.failCount }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getImportStatusType(importDetail.status)" size="small">
              {{ getImportStatusText(importDetail.status) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        
        <div v-if="importDetail.errorDetails && importDetail.errorDetails.length > 0" style="margin-top: 20px;">
          <h4>错误详情</h4>
          <el-table :data="importDetail.errorDetails" stripe>
            <el-table-column prop="row" label="行号" width="80" />
            <el-table-column prop="field" label="字段" width="120" />
            <el-table-column prop="value" label="值" width="150" />
            <el-table-column prop="error" label="错误信息" />
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const uploadUrl = '/api/import'
const uploadHeaders = {
  'Authorization': 'Bearer ' + localStorage.getItem('token')
}

const exportOptions = reactive({
  customer: {
    includeAll: true,
    includeHistory: false,
    dateRange: null
  },
  product: {
    includeAll: true,
    includeInventory: true,
    includePrice: true
  },
  order: {
    includeAll: true,
    includeItems: true,
    dateRange: null
  },
  quotation: {
    includeAll: true,
    includeProducts: true,
    dateRange: null
  }
})

const importHistory = ref([
  {
    id: 1,
    time: '2024-12-25 14:30',
    type: 'customer',
    fileName: '客户数据导入.xlsx',
    totalCount: 150,
    successCount: 148,
    failCount: 2,
    status: 'completed',
    operator: '张三'
  },
  {
    id: 2,
    time: '2024-12-24 16:20',
    type: 'product',
    fileName: '产品数据导入.xlsx',
    totalCount: 80,
    successCount: 80,
    failCount: 0,
    status: 'completed',
    operator: '李四'
  },
  {
    id: 3,
    time: '2024-12-23 10:15',
    type: 'order',
    fileName: '订单数据导入.xlsx',
    totalCount: 200,
    successCount: 195,
    failCount: 5,
    status: 'completed',
    operator: '王五'
  }
])

const importDetailVisible = ref(false)
const importDetail = ref(null)

// 方法
const downloadTemplate = () => {
  ElMessage.info('下载模板功能开发中...')
}

const beforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                  file.type === 'application/vnd.ms-excel'
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  return true
}

const handleCustomerImportSuccess = (response, file) => {
  ElMessage.success('客户数据导入成功！')
  refreshImportHistory()
}

const handleProductImportSuccess = (response, file) => {
  ElMessage.success('产品数据导入成功！')
  refreshImportHistory()
}

const handleOrderImportSuccess = (response, file) => {
  ElMessage.success('订单数据导入成功！')
  refreshImportHistory()
}

const handleQuotationImportSuccess = (response, file) => {
  ElMessage.success('报价数据导入成功！')
  refreshImportHistory()
}

const handleImportError = (error, file) => {
  ElMessage.error('文件上传失败，请重试！')
}

const handleExportCustomer = () => {
  ElMessage.info('导出客户数据功能开发中...')
}

const handleExportProduct = () => {
  ElMessage.info('导出产品数据功能开发中...')
}

const handleExportOrder = () => {
  ElMessage.info('导出订单数据功能开发中...')
}

const handleExportQuotation = () => {
  ElMessage.info('导出报价数据功能开发中...')
}

const handleBatchExport = () => {
  ElMessage.info('批量导出功能开发中...')
}

const refreshImportHistory = () => {
  ElMessage.info('刷新导入历史功能开发中...')
}

const getImportTypeTag = (type) => {
  const tagMap = {
    'customer': 'primary',
    'product': 'success',
    'order': 'warning',
    'quotation': 'info'
  }
  return tagMap[type] || 'info'
}

const getImportTypeText = (type) => {
  const textMap = {
    'customer': '客户数据',
    'product': '产品数据',
    'order': '订单数据',
    'quotation': '报价数据'
  }
  return textMap[type] || type
}

const getImportStatusType = (status) => {
  const typeMap = {
    'processing': 'warning',
    'completed': 'success',
    'failed': 'danger'
  }
  return typeMap[status] || 'info'
}

const getImportStatusText = (status) => {
  const textMap = {
    'processing': '处理中',
    'completed': '已完成',
    'failed': '失败'
  }
  return textMap[status] || status
}

const handleViewImportDetail = (row) => {
  importDetail.value = {
    ...row,
    errorDetails: [
      { row: 5, field: '邮箱', value: 'invalid-email', error: '邮箱格式不正确' },
      { row: 12, field: '电话', value: '', error: '电话号码不能为空' }
    ]
  }
  importDetailVisible.value = true
}

const handleDeleteImport = (row) => {
  ElMessageBox.confirm(
    `确定要删除导入记录 "${row.fileName}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('导入记录已删除')
  })
}
</script>

<style lang="scss" scoped>
.import-section, .export-section {
  margin-bottom: 20px;
  
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 16px;
  }
}

.export-options {
  .el-checkbox {
    display: block;
    margin-bottom: 8px;
  }
}

.success-count {
  color: #67C23A;
  font-weight: bold;
}

.fail-count {
  color: #F56C6C;
  font-weight: bold;
}

:deep(.el-upload-dragger) {
  width: 100%;
  height: 120px;
}

:deep(.el-upload__tip) {
  margin-top: 10px;
}

// 响应式设计
@media (max-width: 768px) {
  .import-section, .export-section {
    margin-bottom: 30px;
  }
}
</style> 