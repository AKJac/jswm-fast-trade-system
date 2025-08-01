<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo-placeholder">
          <el-icon size="48" color="#409EFF"><OfficeBuilding /></el-icon>
        </div>
        <h1>极速外贸管理系统</h1>
        <p>鸿思特科技有限公司</p>
      </div>
      
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="loginRules"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <div class="remember-forget">
            <el-checkbox v-model="loginForm.remember">记住密码</el-checkbox>
            <el-link type="primary" :underline="false" @click="handleForgetPassword">
              忘记密码？
            </el-link>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large"
            :loading="loading"
            @click="handleLogin"
            class="login-button"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>© 2024 鸿思特科技有限公司 版权所有</p>
        <p>联系电话：17600198256 | 邮箱：hst_co@163.com</p>
      </div>
    </div>
    
    <div class="login-background">
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    // 模拟登录
    setTimeout(() => {
      loading.value = false
      
      // 模拟验证
      if (loginForm.username === 'admin' && loginForm.password === '123456') {
        ElMessage.success('登录成功！')
        
        // 存储登录信息
        localStorage.setItem('isLoggedIn', 'true')
        localStorage.setItem('username', loginForm.username)
        
        if (loginForm.remember) {
          localStorage.setItem('rememberUsername', loginForm.username)
        } else {
          localStorage.removeItem('rememberUsername')
        }
        
        // 跳转到首页
        console.log('准备跳转到首页...')
        router.push('/').then(() => {
          console.log('跳转成功')
        }).catch((error) => {
          console.error('跳转失败:', error)
        })
      } else {
        ElMessage.error('用户名或密码错误！')
      }
    }, 1000)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const handleForgetPassword = () => {
  ElMessage.info('请联系管理员重置密码')
}

// 检查记住的用户名
const rememberUsername = localStorage.getItem('rememberUsername')
if (rememberUsername) {
  loginForm.username = rememberUsername
  loginForm.remember = true
}
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  padding: 40px;
  position: relative;
  z-index: 10;
  backdrop-filter: blur(10px);
  
  .login-header {
    text-align: center;
    margin-bottom: 40px;
    
    .logo-placeholder {
      width: 80px;
      height: 80px;
      margin: 0 auto 20px auto;
      border-radius: 50%;
      background: linear-gradient(135deg, #409EFF 0%, #2D8CF0 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 8px 20px rgba(64, 158, 255, 0.3);
    }
    
    h1 {
      font-size: 28px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 10px 0;
    }
    
    p {
      font-size: 14px;
      color: #909399;
    }
  }
  
  .login-form {
    .remember-forget {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
    }
    
    .login-button {
      width: 100%;
      font-weight: 500;
      letter-spacing: 1px;
    }
    
    :deep(.el-input__wrapper) {
      border-radius: 8px;
    }
  }
  
  .login-footer {
    text-align: center;
    margin-top: 30px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
    
    p {
      font-size: 12px;
      color: #909399;
      margin: 5px 0;
    }
  }
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: 1;
  
  .bg-shape {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
    animation: float 20s infinite ease-in-out;
    
    &.bg-shape-1 {
      width: 400px;
      height: 400px;
      top: -200px;
      left: -200px;
      animation-delay: 0s;
    }
    
    &.bg-shape-2 {
      width: 300px;
      height: 300px;
      bottom: -150px;
      right: -150px;
      animation-delay: 5s;
    }
    
    &.bg-shape-3 {
      width: 200px;
      height: 200px;
      top: 50%;
      right: 10%;
      animation-delay: 10s;
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  33% {
    transform: translate(30px, -30px) rotate(120deg);
  }
  66% {
    transform: translate(-20px, 20px) rotate(240deg);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .login-card {
    width: 90%;
    max-width: 400px;
    padding: 30px 20px;
    
    .login-header {
      h1 {
        font-size: 24px;
      }
    }
  }
}
</style> 