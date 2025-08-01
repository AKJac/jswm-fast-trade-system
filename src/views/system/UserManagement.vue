<template>
  <div class="page-container">
    <div class="page-header">
      <h1>用户管理</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 搜索栏 -->
    <el-card class="custom-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="用户名">
          <el-input 
            v-model="searchForm.username" 
            placeholder="请输入用户名" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input 
            v-model="searchForm.realName" 
            placeholder="请输入姓名" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="部门">
          <el-select 
            v-model="searchForm.department" 
            placeholder="请选择部门" 
            clearable
            style="width: 150px"
          >
            <el-option label="销售部" value="sales" />
            <el-option label="采购部" value="purchase" />
            <el-option label="财务部" value="finance" />
            <el-option label="行政部" value="admin" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select 
            v-model="searchForm.status" 
            placeholder="请选择状态" 
            clearable
            style="width: 120px"
          >
            <el-option label="启用" value="active" />
            <el-option label="禁用" value="inactive" />
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
            新增用户
          </el-button>
          <el-button type="warning" @click="handleExport">
            <el-icon><Download /></el-icon>
            导出用户
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 用户列表 -->
    <el-card class="custom-card">
      <el-table 
        :data="userList" 
        stripe 
        class="custom-table"
        v-loading="loading"
      >
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="department" label="部门" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ getDepartmentText(row.department) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="getRoleType(row.role)" size="small">
              {{ getRoleText(row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              active-value="active"
              inactive-value="inactive"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="lastLogin" label="最后登录" width="150" />
        <el-table-column prop="createTime" label="创建时间" width="150" />
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
                  <el-dropdown-item command="resetPassword">
                    <el-icon><Key /></el-icon>
                    重置密码
                  </el-dropdown-item>
                  <el-dropdown-item command="permissions">
                    <el-icon><Lock /></el-icon>
                    权限设置
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
  username: '',
  realName: '',
  department: '',
  status: ''
})

const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

const userList = ref([])

// 模拟用户数据
const mockUsers = [
  {
    id: 1,
    username: 'chenzc',
    realName: '陈志成',
    email: 'hst_co@163.com',
    phone: '17600198256',
    department: 'admin',
    role: 'admin',
    status: 'active',
    lastLogin: '2024-12-27 14:30:00',
    createTime: '2024-01-01 09:00:00'
  },
  {
    id: 2,
    username: 'zhangsan',
    realName: '张三',
    email: 'zhangsan@company.com',
    phone: '13800138001',
    department: 'sales',
    role: 'salesperson',
    status: 'active',
    lastLogin: '2024-12-27 10:15:00',
    createTime: '2024-02-15 10:30:00'
  },
  {
    id: 3,
    username: 'lisi',
    realName: '李四',
    email: 'lisi@company.com',
    phone: '13800138002',
    department: 'sales',
    role: 'supervisor',
    status: 'active',
    lastLogin: '2024-12-26 16:45:00',
    createTime: '2024-03-01 11:00:00'
  },
  {
    id: 4,
    username: 'wangwu',
    realName: '王五',
    email: 'wangwu@company.com',
    phone: '13800138003',
    department: 'finance',
    role: 'finance',
    status: 'inactive',
    lastLogin: '2024-12-20 09:20:00',
    createTime: '2024-04-10 14:20:00'
  }
]

// 方法
const getDepartmentText = (department) => {
  const textMap = {
    'sales': '销售部',
    'purchase': '采购部',
    'finance': '财务部',
    'admin': '行政部'
  }
  return textMap[department] || department
}

const getRoleType = (role) => {
  const typeMap = {
    'admin': 'danger',
    'supervisor': 'warning',
    'salesperson': 'primary',
    'finance': 'success'
  }
  return typeMap[role] || 'info'
}

const getRoleText = (role) => {
  const textMap = {
    'admin': '系统管理员',
    'supervisor': '主管',
    'salesperson': '业务员',
    'finance': '财务'
  }
  return textMap[role] || role
}

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loadUsers()
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
  ElMessage.info('新增用户功能开发中...')
}

const handleEdit = (user) => {
  ElMessage.info(`编辑用户：${user.realName}`)
}

const handleStatusChange = (user) => {
  const statusText = user.status === 'active' ? '启用' : '禁用'
  ElMessage.success(`用户 ${user.realName} 已${statusText}`)
}

const handleCommand = (command, user) => {
  switch (command) {
    case 'resetPassword':
      ElMessageBox.confirm(
        `确定要重置用户 "${user.realName}" 的密码吗？`,
        '确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        ElMessage.success(`用户 ${user.realName} 密码已重置`)
      })
      break
    case 'permissions':
      ElMessage.info(`设置用户权限：${user.realName}`)
      break
    case 'delete':
      handleDelete(user)
      break
  }
}

const handleDelete = (user) => {
  ElMessageBox.confirm(
    `确定要删除用户 "${user.realName}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = userList.value.findIndex(item => item.id === user.id)
    if (index > -1) {
      userList.value.splice(index, 1)
      pagination.total--
      ElMessage.success('删除成功')
    }
  })
}

const handleExport = () => {
  ElMessage.info('正在导出用户数据...')
}

const handleSizeChange = (size) => {
  pagination.size = size
  loadUsers()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadUsers()
}

const loadUsers = () => {
  userList.value = [...mockUsers]
  pagination.total = mockUsers.length
}

onMounted(() => {
  loadUsers()
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