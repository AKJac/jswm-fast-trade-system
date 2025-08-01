<template>
  <div class="layout-container">
    <!-- 头部 -->
    <el-header class="layout-header">
      <div class="header-left">
        <el-button
          :icon="isCollapse ? 'Expand' : 'Fold'"
          @click="toggleCollapse"
          text
          size="large"
        />
        <h2 class="system-title">极速外贸管理系统</h2>
      </div>
      <div class="header-right">
        <span class="company-info">鸿思特科技有限公司</span>
        <el-dropdown trigger="click">
          <span class="user-info">
            <el-avatar :size="32" :src="userAvatar" />
            <span class="username">{{ currentUser.name }}</span>
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item><el-icon><User /></el-icon>个人中心</el-dropdown-item>
              <el-dropdown-item><el-icon><Setting /></el-icon>系统设置</el-dropdown-item>
              <el-dropdown-item divided @click="logout">
                <el-icon><SwitchButton /></el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container class="layout-body">
      <!-- 侧边栏 -->
      <el-aside :width="sidebarWidth" class="layout-sidebar">
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :unique-opened="false"
          router
          class="sidebar-menu"
        >
          <template v-for="route in menuRoutes" :key="route.path">
            <el-sub-menu
              v-if="route.children && route.children.length > 1"
              :index="route.path"
            >
              <template #title>
                <el-icon><component :is="route.meta?.icon || 'Menu'" /></el-icon>
                <span>{{ route.meta?.title }}</span>
              </template>
              <el-menu-item
                v-for="child in route.children"
                :key="child.path"
                :index="route.path + '/' + child.path"
              >
                <el-icon><component :is="child.meta?.icon || 'CircleRight'" /></el-icon>
                <span>{{ child.meta?.title }}</span>
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item
              v-else-if="route.children && route.children.length === 1"
              :index="route.path + '/' + route.children[0].path"
            >
              <el-icon><component :is="route.meta?.icon || 'Menu'" /></el-icon>
              <span>{{ route.meta?.title }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 响应式数据
const isCollapse = ref(false)
const currentUser = ref({
  name: '陈志成',
  role: '系统管理员',
  avatar: ''
})

// 计算属性
const sidebarWidth = computed(() => isCollapse.value ? '64px' : '200px')
const activeMenu = computed(() => route.path)
const userAvatar = computed(() => currentUser.value.avatar || '/src/assets/avatar.png')

// 菜单路由（过滤掉不需要显示的路由）
const menuRoutes = computed(() => {
  return router.options.routes.filter(route => 
    route.path !== '/' && route.path !== '/login' && route.component && route.meta?.title
  )
})

// 方法
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 清除登录信息
    localStorage.removeItem('isLoggedIn')
    localStorage.removeItem('username')
    ElMessage.success('已退出登录')
    router.push('/login')
  })
}

onMounted(() => {
  // 组件挂载后的初始化操作
})
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  
  .header-left {
    display: flex;
    align-items: center;
    
    .system-title {
      margin: 0 0 0 15px;
      font-size: 20px;
      font-weight: 600;
    }
  }
  
  .header-right {
    display: flex;
    align-items: center;
    gap: 20px;
    
    .company-info {
      font-size: 14px;
      opacity: 0.9;
    }
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      padding: 8px;
      border-radius: 4px;
      transition: background-color 0.3s;
      
      &:hover {
        background-color: rgba(255, 255, 255, 0.1);
      }
      
      .username {
        font-size: 14px;
      }
    }
  }
}

.layout-body {
  flex: 1;
  height: calc(100vh - 60px);
}

.layout-sidebar {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
  transition: width 0.3s;
  overflow: hidden;
  
  .sidebar-menu {
    height: 100%;
    border-right: none;
    background-color: transparent;
    
    :deep(.el-menu-item) {
      height: 48px;
      line-height: 48px;
      
      &.is-active {
        background-color: #409eff;
        color: white;
        
        .el-icon {
          color: white;
        }
      }
      
      &:hover {
        background-color: #ecf5ff;
        color: #409eff;
      }
    }
    
    :deep(.el-sub-menu__title) {
      height: 48px;
      line-height: 48px;
      
      &:hover {
        background-color: #ecf5ff;
        color: #409eff;
      }
    }
  }
}

.layout-main {
  background-color: #f0f2f5;
  padding: 0;
  overflow-y: auto;
}

// 响应式设计
@media (max-width: 768px) {
  .layout-header {
    padding: 0 10px;
    
    .system-title {
      font-size: 16px;
    }
    
    .company-info {
      display: none;
    }
  }
  
  .layout-sidebar {
    position: fixed;
    left: 0;
    top: 60px;
    height: calc(100vh - 60px);
    z-index: 1000;
    transform: translateX(-100%);
    transition: transform 0.3s;
    
    &.mobile-show {
      transform: translateX(0);
    }
  }
  
  .layout-main {
    margin-left: 0 !important;
  }
}
</style> 