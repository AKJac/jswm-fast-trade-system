import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/Layout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { hidden: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '控制台', icon: 'Odometer' }
      }
    ]
  },
  {
    path: '/task',
    component: Layout,
    redirect: '/task/work',
    meta: { title: '工作任务', icon: 'List' },
    children: [
      {
        path: 'work',
        name: 'WorkTask',
        component: () => import('@/views/task/WorkTask.vue'),
        meta: { title: '工作任务' }
      }
    ]
  },
  {
    path: '/customer',
    component: Layout,
    redirect: '/customer/list',
    meta: { title: '客户管理', icon: 'User' },
    children: [
      {
        path: 'list',
        name: 'CustomerList',
        component: () => import('@/views/customer/CustomerList.vue'),
        meta: { title: '客户列表' }
      },
      {
        path: 'detail/:id',
        name: 'CustomerDetail',
        component: () => import('@/views/customer/CustomerDetail.vue'),
        meta: { title: '客户详情' }
      },
      {
        path: 'pool',
        name: 'CustomerPool',
        component: () => import('@/views/customer/CustomerPool.vue'),
        meta: { title: '客户公海' }
      }
    ]
  },
  {
    path: '/product',
    component: Layout,
    redirect: '/product/list',
    meta: { title: '产品管理', icon: 'Goods' },
    children: [
      {
        path: 'list',
        name: 'ProductList',
        component: () => import('@/views/product/ProductList.vue'),
        meta: { title: '产品列表' }
      },
      {
        path: 'detail/:id',
        name: 'ProductDetail',
        component: () => import('@/views/product/ProductDetail.vue'),
        meta: { title: '产品详情' }
      }
    ]
  },
  {
    path: '/quotation',
    component: Layout,
    redirect: '/quotation/list',
    meta: { title: '报价管理', icon: 'Tickets' },
    children: [
      {
        path: 'list',
        name: 'QuotationList',
        component: () => import('@/views/quotation/QuotationList.vue'),
        meta: { title: '报价单' }
      },
      {
        path: 'detail/:id',
        name: 'QuotationDetail',
        component: () => import('@/views/quotation/QuotationDetail.vue'),
        meta: { title: '报价详情' }
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/list',
    meta: { title: '订单管理', icon: 'Document' },
    children: [
      {
        path: 'list',
        name: 'OrderList',
        component: () => import('@/views/order/OrderList.vue'),
        meta: { title: '销售订单' }
      }
    ]
  },
  {
    path: '/purchase',
    component: Layout,
    redirect: '/purchase/list',
    meta: { title: '采购管理', icon: 'ShoppingCart' },
    children: [
      {
        path: 'list',
        name: 'PurchaseList',
        component: () => import('@/views/purchase/PurchaseList.vue'),
        meta: { title: '采购订单' }
      }
    ]
  },
  {
    path: '/inventory',
    component: Layout,
    redirect: '/inventory/list',
    meta: { title: '库存管理', icon: 'Box' },
    children: [
      {
        path: 'list',
        name: 'InventoryList',
        component: () => import('@/views/inventory/InventoryList.vue'),
        meta: { title: '库存查询' }
      }
    ]
  },
  {
    path: '/shipping',
    component: Layout,
    redirect: '/shipping/list',
    meta: { title: '出运管理', icon: 'Van' },
    children: [
      {
        path: 'list',
        name: 'ShippingList',
        component: () => import('@/views/shipping/ShippingList.vue'),
        meta: { title: '出运订单' }
      }
    ]
  },
  {
    path: '/finance',
    component: Layout,
    redirect: '/finance/overview',
    meta: { title: '财务管理', icon: 'Money' },
    children: [
      {
        path: 'overview',
        name: 'FinanceOverview',
        component: () => import('@/views/finance/FinanceOverview.vue'),
        meta: { title: '财务概览' }
      }
    ]
  },
  {
    path: '/statistics',
    component: Layout,
    redirect: '/statistics/overview',
    meta: { title: '统计分析', icon: 'DataAnalysis' },
    children: [
      {
        path: 'overview',
        name: 'StatisticsOverview',
        component: () => import('@/views/statistics/Overview.vue'),
        meta: { title: '数据概览' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/user',
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        name: 'UserManagement',
        component: () => import('@/views/system/UserManagement.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'role',
        name: 'RoleManagement',
        component: () => import('@/views/system/RoleManagement.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: 'settings',
        name: 'SystemSettings',
        component: () => import('@/views/system/SystemSettings.vue'),
        meta: { title: '系统设置' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/system/Profile.vue'),
        meta: { title: '个人中心' }
      },
              {
          path: 'help',
          name: 'Help',
          component: () => import('@/views/system/Help.vue'),
          meta: { title: '帮助中心' }
        },
        {
          path: 'import',
          name: 'DataImport',
          component: () => import('@/views/system/DataImport.vue'),
          meta: { title: '数据导入导出' }
        }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn')
  
  console.log('路由守卫 - 目标路径:', to.path, '是否已登录:', isLoggedIn)
  
  if (to.path === '/login') {
    if (isLoggedIn) {
      console.log('已登录，重定向到首页')
      next('/')
    } else {
      console.log('未登录，允许访问登录页')
      next()
    }
  } else {
    if (isLoggedIn) {
      console.log('已登录，允许访问:', to.path)
      next()
    } else {
      console.log('未登录，重定向到登录页')
      next('/login')
    }
  }
})

export default router 