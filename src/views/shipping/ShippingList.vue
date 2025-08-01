<template>
  <div class="page-container">
    <div class="page-header">
      <h1>出运管理</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>出运管理</el-breadcrumb-item>
        <el-breadcrumb-item>出运订单</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索栏 -->
    <el-card class="custom-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="出运编号">
          <el-input 
            v-model="searchForm.shippingNo" 
            placeholder="请输入出运编号" 
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
        <el-form-item label="运输方式">
          <el-select 
            v-model="searchForm.transportMethod" 
            placeholder="请选择运输方式" 
            clearable
            style="width: 150px"
          >
            <el-option label="海运" value="sea" />
            <el-option label="空运" value="air" />
            <el-option label="陆运" value="land" />
            <el-option label="快递" value="express" />
          </el-select>
        </el-form-item>
        <el-form-item label="出运状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable
            style="width: 150px"
          >
            <el-option label="待出运" value="pending" />
            <el-option label="已出运" value="shipped" />
            <el-option label="运输中" value="transit" />
            <el-option label="已到达" value="arrived" />
            <el-option label="已完成" value="completed" />
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
            新建出运
          </el-button>
          <el-button type="warning" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出数据
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 出运列表 -->
    <el-card class="custom-card">
      <el-table 
        :data="shippingList" 
        stripe 
        class="custom-table"
        v-loading="loading"
      >
        <el-table-column prop="shippingNo" label="出运编号" width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">
              {{ row.shippingNo }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单编号" width="150" />
        <el-table-column prop="customerName" label="客户名称" min-width="180" />
        <el-table-column prop="transportMethod" label="运输方式" width="100">
          <template #default="{ row }">
            <el-tag :type="getTransportType(row.transportMethod)" size="small">
              {{ getTransportText(row.transportMethod) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="destination" label="目的地" width="120" />
        <el-table-column prop="shippingDate" label="出运日期" width="120" />
        <el-table-column prop="estimatedArrival" label="预计到达" width="120" />
        <el-table-column prop="trackingNo" label="跟踪号码" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
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
                  <el-dropdown-item command="track">
                    <el-icon><View /></el-icon>
                    物流跟踪
                  </el-dropdown-item>
                  <el-dropdown-item command="document">
                    <el-icon><Document /></el-icon>
                    出运单据
                  </el-dropdown-item>
                  <el-dropdown-item command="complete" :disabled="row.status === 'completed'">
                    <el-icon><Check /></el-icon>
                    完成出运
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const loading = ref(false)

const searchForm = reactive({
  shippingNo: '',
  customerName: '',
  transportMethod: '',
  status: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const shippingList = ref([])

// 模拟出运数据
const mockShippings = [
  {
    id: 1,
    shippingNo: 'SH202412001',
    orderNo: 'SO202412001',
    customerName: 'ABC Trading Co.',
    transportMethod: 'sea',
    destination: '纽约港',
    shippingDate: '2024-12-20',
    estimatedArrival: '2025-01-15',
    trackingNo: 'COSCO2024120001',
    status: 'transit'
  },
  {
    id: 2,
    shippingNo: 'SH202412002',
    orderNo: 'SO202412002',
    customerName: 'XYZ Import Ltd',
    transportMethod: 'air',
    destination: '伦敦希思罗',
    shippingDate: '2024-12-22',
    estimatedArrival: '2024-12-24',
    trackingNo: 'CA9981234567890',
    status: 'completed'
  }
]

// 方法
const getTransportType = (method) => {
  const typeMap = {
    'sea': 'primary',
    'air': 'success',
    'land': 'warning',
    'express': 'info'
  }
  return typeMap[method] || 'info'
}

const getTransportText = (method) => {
  const textMap = {
    'sea': '海运',
    'air': '空运',
    'land': '陆运',
    'express': '快递'
  }
  return textMap[method] || method
}

const getStatusType = (status) => {
  const typeMap = {
    'pending': 'info',
    'shipped': 'warning',
    'transit': 'primary',
    'arrived': 'success',
    'completed': 'success'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    'pending': '待出运',
    'shipped': '已出运',
    'transit': '运输中',
    'arrived': '已到达',
    'completed': '已完成'
  }
  return textMap[status] || status
}

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loadShippings()
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
  ElMessage.info('新建出运功能开发中...')
}

const handleEdit = (shipping) => {
  ElMessage.info(`编辑出运：${shipping.shippingNo}`)
}

const handleView = (shipping) => {
  ElMessage.info(`查看出运详情：${shipping.shippingNo}`)
}

const handleCommand = (command, shipping) => {
  switch (command) {
    case 'track':
      ElMessage.info(`查看物流跟踪：${shipping.trackingNo}`)
      break
    case 'document':
      ElMessage.info(`查看出运单据：${shipping.shippingNo}`)
      break
    case 'complete':
      ElMessage.success(`出运 ${shipping.shippingNo} 已标记为完成`)
      shipping.status = 'completed'
      break
    case 'delete':
      handleDelete(shipping)
      break
  }
}

const handleDelete = (shipping) => {
  ElMessageBox.confirm(
    `确定要删除出运记录 "${shipping.shippingNo}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = shippingList.value.findIndex(item => item.id === shipping.id)
    if (index > -1) {
      shippingList.value.splice(index, 1)
      pagination.total--
      ElMessage.success('删除成功')
    }
  })
}

const handleExport = () => {
  ElMessage.info('正在导出出运数据...')
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadShippings()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadShippings()
}

const loadShippings = () => {
  shippingList.value = [...mockShippings]
  pagination.total = mockShippings.length
}

onMounted(() => {
  loadShippings()
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
      
      .el-input, .el-select {
        width: 100% !important;
      }
    }
  }
}
</style> 