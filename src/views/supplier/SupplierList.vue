<template>
  <div class="page-container">
    <div class="page-header">
      <h1>供应商管理</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>供应商管理</el-breadcrumb-item>
        <el-breadcrumb-item>供应商列表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索栏 -->
    <el-card class="custom-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="供应商编号">
          <el-input 
            v-model="searchForm.supplierNo" 
            placeholder="请输入供应商编号" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="供应商名称">
          <el-input 
            v-model="searchForm.supplierName" 
            placeholder="请输入供应商名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="公司类型">
          <el-select 
            v-model="searchForm.companyType" 
            placeholder="请选择公司类型" 
            clearable
            style="width: 150px"
          >
            <el-option label="生产商" value="manufacturer" />
            <el-option label="贸易商" value="trader" />
            <el-option label="代理商" value="agent" />
            <el-option label="服务商" value="service" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="合作状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable
            style="width: 150px"
          >
            <el-option label="合作中" value="active" />
            <el-option label="暂停合作" value="paused" />
            <el-option label="终止合作" value="terminated" />
            <el-option label="待评估" value="evaluating" />
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
            新增供应商
          </el-button>
          <el-button type="success" @click="handleImport">
            <el-icon><Upload /></el-icon>
            批量导入
          </el-button>
          <el-button type="warning" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出数据
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-radio-group v-model="viewMode" size="small">
            <el-radio-button label="table">表格视图</el-radio-button>
            <el-radio-button label="card">卡片视图</el-radio-button>
          </el-radio-group>
        </div>
      </div>
    </el-card>

    <!-- 供应商列表 -->
    <el-card class="custom-card">
      <!-- 表格视图 -->
      <el-table 
        v-if="viewMode === 'table'"
        :data="supplierList" 
        stripe 
        class="custom-table"
        v-loading="loading"
      >
        <el-table-column prop="supplierNo" label="供应商编号" width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">
              {{ row.supplierNo }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="supplierName" label="供应商名称" min-width="200" />
        <el-table-column prop="companyType" label="公司类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getCompanyTypeTag(row.companyType)">
              {{ getCompanyTypeLabel(row.companyType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contact" label="联系人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="status" label="合作状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 卡片视图 -->
      <div v-else class="card-view">
        <el-row :gutter="20">
          <el-col 
            v-for="item in supplierList" 
            :key="item.id" 
            :xs="24" 
            :sm="12" 
            :md="8" 
            :lg="6" 
            :xl="6"
          >
            <el-card class="supplier-card" shadow="hover">
              <div class="card-header">
                <div class="supplier-info">
                  <h3 class="supplier-name">{{ item.supplierName }}</h3>
                  <p class="supplier-no">{{ item.supplierNo }}</p>
                </div>
                <div class="supplier-status">
                  <el-tag :type="getStatusTag(item.status)" size="small">
                    {{ getStatusLabel(item.status) }}
                  </el-tag>
                </div>
              </div>
              
              <div class="card-content">
                <div class="info-item">
                  <span class="label">公司类型:</span>
                  <el-tag :type="getCompanyTypeTag(item.companyType)" size="small">
                    {{ getCompanyTypeLabel(item.companyType) }}
                  </el-tag>
                </div>
                <div class="info-item">
                  <span class="label">联系人:</span>
                  <span class="value">{{ item.contact }}</span>
                </div>
                <div class="info-item">
                  <span class="label">电话:</span>
                  <span class="value">{{ item.phone }}</span>
                </div>
                <div class="info-item">
                  <span class="label">邮箱:</span>
                  <span class="value">{{ item.email }}</span>
                </div>
              </div>
              
              <div class="card-actions">
                <el-button size="small" @click="handleView(item)">查看</el-button>
                <el-button size="small" type="primary" @click="handleEdit(item)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDelete(item)">删除</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 供应商表单对话框 -->
    <SupplierForm
      v-model:visible="formVisible"
      :supplier="currentSupplier"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import SupplierForm from './components/SupplierForm.vue'

// 搜索表单
const searchForm = reactive({
  supplierNo: '',
  supplierName: '',
  companyType: '',
  status: ''
})

// 视图模式
const viewMode = ref('table')

// 加载状态
const loading = ref(false)

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 表单对话框
const formVisible = ref(false)
const currentSupplier = ref(null)

// 模拟供应商数据
const supplierList = ref([
  {
    id: 1,
    supplierNo: 'S20241201001',
    supplierName: 'ABC制造有限公司',
    companyType: 'manufacturer',
    contact: '张经理',
    phone: '+86-138-0012-3456',
    email: 'zhang@abc.com',
    status: 'active',
    createTime: '2024-12-01 10:00:00'
  },
  {
    id: 2,
    supplierNo: 'S20241201002',
    supplierName: 'XYZ贸易公司',
    companyType: 'trader',
    contact: '李总',
    phone: '+86-139-0012-3457',
    email: 'li@xyz.com',
    status: 'active',
    createTime: '2024-12-01 11:00:00'
  },
  {
    id: 3,
    supplierNo: 'S20241201003',
    supplierName: 'DEF代理商',
    companyType: 'agent',
    contact: '王经理',
    phone: '+86-137-0012-3458',
    email: 'wang@def.com',
    status: 'paused',
    createTime: '2024-12-01 12:00:00'
  }
])

// 获取公司类型标签
const getCompanyTypeLabel = (type) => {
  const typeMap = {
    manufacturer: '生产商',
    trader: '贸易商',
    agent: '代理商',
    service: '服务商',
    other: '其他'
  }
  return typeMap[type] || '未知'
}

// 获取公司类型标签颜色
const getCompanyTypeTag = (type) => {
  const tagMap = {
    manufacturer: 'success',
    trader: 'primary',
    agent: 'warning',
    service: 'info',
    other: 'default'
  }
  return tagMap[type] || 'default'
}

// 获取状态标签
const getStatusLabel = (status) => {
  const statusMap = {
    active: '合作中',
    paused: '暂停合作',
    terminated: '终止合作',
    evaluating: '待评估'
  }
  return statusMap[status] || '未知'
}

// 获取状态标签颜色
const getStatusTag = (status) => {
  const tagMap = {
    active: 'success',
    paused: 'warning',
    terminated: 'danger',
    evaluating: 'info'
  }
  return tagMap[status] || 'default'
}

// 搜索
const handleSearch = () => {
  loading.value = true
  // 模拟搜索
  setTimeout(() => {
    loading.value = false
    ElMessage.success('搜索完成')
  }, 500)
}

// 重置搜索
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  handleSearch()
}

// 新增供应商
const handleAdd = () => {
  currentSupplier.value = null
  formVisible.value = true
}

// 编辑供应商
const handleEdit = (row) => {
  currentSupplier.value = row
  formVisible.value = true
}

// 查看供应商
const handleView = (row) => {
  ElMessage.info(`查看供应商: ${row.supplierName}`)
}

// 删除供应商
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除供应商 "${row.supplierName}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟删除
    await new Promise(resolve => setTimeout(resolve, 500))
    ElMessage.success('删除成功')
  } catch (error) {
    // 用户取消删除
  }
}

// 批量导入
const handleImport = () => {
  ElMessage.info('批量导入功能')
}

// 导出数据
const handleExport = () => {
  ElMessage.success('导出成功')
}

// 表单成功回调
const handleFormSuccess = () => {
  ElMessage.success('操作成功')
  // 刷新列表
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.pageSize = size
  // 重新加载数据
}

// 当前页改变
const handleCurrentChange = (page) => {
  pagination.currentPage = page
  // 重新加载数据
}

// 初始化
onMounted(() => {
  pagination.total = supplierList.value.length
})
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0 0 10px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.breadcrumb {
  font-size: 14px;
}

.custom-card {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.toolbar-left {
  display: flex;
  gap: 10px;
}

.custom-table {
  width: 100%;
}

.card-view {
  margin-top: 20px;
}

.supplier-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.supplier-card:hover {
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.supplier-info {
  flex: 1;
}

.supplier-name {
  margin: 0 0 5px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.supplier-no {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.card-content {
  margin-bottom: 15px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
}

.info-item .label {
  color: #606266;
  font-weight: 500;
}

.info-item .value {
  color: #303133;
  word-break: break-all;
}

.card-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .toolbar {
    flex-direction: column;
    gap: 10px;
  }
  
  .toolbar-left {
    width: 100%;
    justify-content: center;
  }
  
  .toolbar-right {
    width: 100%;
    display: flex;
    justify-content: center;
  }
}
</style> 