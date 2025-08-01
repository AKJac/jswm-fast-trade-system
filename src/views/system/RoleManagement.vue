<template>
  <div class="page-container">
    <div class="page-header">
      <h1>角色管理</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>角色管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 操作栏 -->
    <el-card class="custom-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增角色
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 角色列表 -->
    <el-card class="custom-card">
      <el-table 
        :data="roleList" 
        stripe 
        class="custom-table"
        v-loading="loading"
      >
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="description" label="角色描述" min-width="200" />
        <el-table-column prop="userCount" label="用户数量" width="100">
          <template #default="{ row }">
            <el-link type="primary" @click="handleViewUsers(row)">
              {{ row.userCount }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              active-value="active"
              inactive-value="inactive"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="success" size="small" @click="handlePermissions(row)">
              <el-icon><Lock /></el-icon>
              权限
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(row)"
              :disabled="row.userCount > 0"
            >
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 权限设置对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="权限设置"
      width="600px"
    >
      <div class="permission-tree">
        <el-tree
          ref="permissionTree"
          :data="permissionData"
          :default-checked-keys="checkedPermissions"
          :props="{ children: 'children', label: 'name' }"
          show-checkbox
          node-key="id"
          :check-strictly="false"
        />
      </div>
      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePermissions">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const loading = ref(false)
const permissionDialogVisible = ref(false)
const permissionTree = ref(null)
const currentRole = ref(null)
const checkedPermissions = ref([])

const roleList = ref([])

// 权限数据
const permissionData = ref([
  {
    id: 1,
    name: '控制台',
    children: [
      { id: 11, name: '查看控制台' },
      { id: 12, name: '数据统计' }
    ]
  },
  {
    id: 2,
    name: '客户管理',
    children: [
      { id: 21, name: '查看客户' },
      { id: 22, name: '新增客户' },
      { id: 23, name: '编辑客户' },
      { id: 24, name: '删除客户' },
      { id: 25, name: '客户公海' }
    ]
  },
  {
    id: 3,
    name: '产品管理',
    children: [
      { id: 31, name: '查看产品' },
      { id: 32, name: '新增产品' },
      { id: 33, name: '编辑产品' },
      { id: 34, name: '删除产品' },
      { id: 35, name: '供应商管理' }
    ]
  },
  {
    id: 4,
    name: '报价管理',
    children: [
      { id: 41, name: '查看报价' },
      { id: 42, name: '新增报价' },
      { id: 43, name: '编辑报价' },
      { id: 44, name: '删除报价' },
      { id: 45, name: '发送报价' }
    ]
  },
  {
    id: 5,
    name: '订单管理',
    children: [
      { id: 51, name: '查看订单' },
      { id: 52, name: '新增订单' },
      { id: 53, name: '编辑订单' },
      { id: 54, name: '删除订单' }
    ]
  },
  {
    id: 6,
    name: '财务管理',
    children: [
      { id: 61, name: '应收款管理' },
      { id: 62, name: '应付款管理' },
      { id: 63, name: '财务统计' }
    ]
  },
  {
    id: 7,
    name: '系统管理',
    children: [
      { id: 71, name: '用户管理' },
      { id: 72, name: '角色管理' },
      { id: 73, name: '权限管理' },
      { id: 74, name: '系统设置' }
    ]
  }
])

// 模拟角色数据
const mockRoles = [
  {
    id: 1,
    roleName: '系统管理员',
    roleCode: 'admin',
    description: '拥有系统所有权限，可以管理用户、角色和系统设置',
    userCount: 1,
    status: 'active',
    createTime: '2024-01-01 09:00:00',
    permissions: [11, 12, 21, 22, 23, 24, 25, 31, 32, 33, 34, 35, 41, 42, 43, 44, 45, 51, 52, 53, 54, 61, 62, 63, 71, 72, 73, 74]
  },
  {
    id: 2,
    roleName: '销售主管',
    roleCode: 'supervisor',
    description: '负责销售团队管理，可以查看和管理客户、产品、报价、订单',
    userCount: 3,
    status: 'active',
    createTime: '2024-02-01 10:00:00',
    permissions: [11, 12, 21, 22, 23, 24, 25, 31, 32, 33, 35, 41, 42, 43, 44, 45, 51, 52, 53, 54]
  },
  {
    id: 3,
    roleName: '业务员',
    roleCode: 'salesperson',
    description: '负责客户跟进和订单处理，可以管理自己的客户和订单',
    userCount: 8,
    status: 'active',
    createTime: '2024-02-15 14:00:00',
    permissions: [11, 21, 22, 23, 31, 41, 42, 43, 45, 51, 52, 53]
  },
  {
    id: 4,
    roleName: '财务专员',
    roleCode: 'finance',
    description: '负责财务管理，可以查看订单和管理收付款',
    userCount: 2,
    status: 'active',
    createTime: '2024-03-01 11:00:00',
    permissions: [11, 21, 51, 61, 62, 63]
  }
]

// 方法
const handleAdd = () => {
  ElMessage.info('新增角色功能开发中...')
}

const handleEdit = (role) => {
  ElMessage.info(`编辑角色：${role.roleName}`)
}

const handleDelete = (role) => {
  if (role.userCount > 0) {
    ElMessage.warning('该角色下还有用户，无法删除')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除角色 "${role.roleName}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = roleList.value.findIndex(item => item.id === role.id)
    if (index > -1) {
      roleList.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  })
}

const handleStatusChange = (role) => {
  const statusText = role.status === 'active' ? '启用' : '禁用'
  ElMessage.success(`角色 ${role.roleName} 已${statusText}`)
}

const handleViewUsers = (role) => {
  ElMessage.info(`查看角色 "${role.roleName}" 下的用户列表`)
}

const handlePermissions = (role) => {
  currentRole.value = role
  checkedPermissions.value = role.permissions || []
  permissionDialogVisible.value = true
}

const handleSavePermissions = () => {
  if (!permissionTree.value) return
  
  // 获取选中的权限节点
  const checkedKeys = permissionTree.value.getCheckedKeys()
  const halfCheckedKeys = permissionTree.value.getHalfCheckedKeys()
  const allCheckedKeys = [...checkedKeys, ...halfCheckedKeys]
  
  // 更新角色权限
  if (currentRole.value) {
    currentRole.value.permissions = allCheckedKeys
    ElMessage.success(`角色 ${currentRole.value.roleName} 权限更新成功`)
  }
  
  permissionDialogVisible.value = false
}

const loadRoles = () => {
  roleList.value = [...mockRoles]
}

onMounted(() => {
  loadRoles()
})
</script>

<style lang="scss" scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .toolbar-left {
    display: flex;
    gap: 10px;
  }
}

.permission-tree {
  max-height: 400px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  padding: 15px;
  
  :deep(.el-tree) {
    background: transparent;
    
    .el-tree-node__content {
      height: 36px;
      
      &:hover {
        background-color: #f5f7fa;
      }
    }
    
    .el-tree-node__label {
      font-size: 14px;
    }
    
    .el-checkbox__input.is-checked .el-checkbox__inner {
      background-color: #409eff;
      border-color: #409eff;
    }
  }
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
}
</style> 