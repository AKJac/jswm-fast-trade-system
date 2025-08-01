<template>
  <div class="page-container">
    <div class="page-header">
      <h1>个人中心</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>个人中心</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-row :gutter="20">
      <!-- 左侧个人信息 -->
      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <el-card class="profile-card">
          <div class="profile-header">
            <el-avatar :size="80" :src="userInfo.avatar" />
            <div class="profile-info">
              <h3>{{ userInfo.name }}</h3>
              <p class="user-role">{{ userInfo.role }}</p>
              <p class="user-department">{{ userInfo.department }}</p>
            </div>
          </div>
          
          <el-divider />
          
          <div class="profile-details">
            <div class="detail-item">
              <span class="label">工号：</span>
              <span class="value">{{ userInfo.employeeId }}</span>
            </div>
            <div class="detail-item">
              <span class="label">邮箱：</span>
              <span class="value">{{ userInfo.email }}</span>
            </div>
            <div class="detail-item">
              <span class="label">手机：</span>
              <span class="value">{{ userInfo.phone }}</span>
            </div>
            <div class="detail-item">
              <span class="label">入职时间：</span>
              <span class="value">{{ userInfo.joinDate }}</span>
            </div>
            <div class="detail-item">
              <span class="label">最后登录：</span>
              <span class="value">{{ userInfo.lastLogin }}</span>
            </div>
          </div>
          
          <el-divider />
          
          <div class="profile-actions">
            <el-button type="primary" @click="editProfile">
              <el-icon><Edit /></el-icon>
              编辑资料
            </el-button>
            <el-button @click="uploadAvatar">
              <el-icon><Upload /></el-icon>
              更换头像
            </el-button>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧设置内容 -->
      <el-col :xs="24" :sm="16" :md="16" :lg="16" :xl="16">
        <el-card class="settings-card">
          <el-tabs v-model="activeTab" type="card">
            <!-- 基本信息 -->
            <el-tab-pane label="基本信息" name="basic">
              <el-form :model="basicForm" label-width="100px">
                <el-form-item label="姓名">
                  <el-input v-model="basicForm.name" placeholder="请输入姓名" />
                </el-form-item>
                <el-form-item label="邮箱">
                  <el-input v-model="basicForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item label="手机号">
                  <el-input v-model="basicForm.phone" placeholder="请输入手机号" />
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="basicForm.gender">
                    <el-radio label="male">男</el-radio>
                    <el-radio label="female">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="生日">
                  <el-date-picker v-model="basicForm.birthday" type="date" placeholder="请选择生日" />
                </el-form-item>
                <el-form-item label="个人简介">
                  <el-input v-model="basicForm.bio" type="textarea" :rows="3" placeholder="请输入个人简介" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveBasicInfo">保存</el-button>
                  <el-button @click="resetBasicInfo">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 修改密码 -->
            <el-tab-pane label="修改密码" name="password">
              <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
                <el-form-item label="当前密码" prop="currentPassword">
                  <el-input v-model="passwordForm.currentPassword" type="password" placeholder="请输入当前密码" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="changePassword">修改密码</el-button>
                  <el-button @click="resetPasswordForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 偏好设置 -->
            <el-tab-pane label="偏好设置" name="preferences">
              <el-form :model="preferencesForm" label-width="120px">
                <el-form-item label="界面主题">
                  <el-select v-model="preferencesForm.theme" placeholder="请选择主题">
                    <el-option label="默认主题" value="default" />
                    <el-option label="深色主题" value="dark" />
                    <el-option label="浅色主题" value="light" />
                  </el-select>
                </el-form-item>
                <el-form-item label="语言设置">
                  <el-select v-model="preferencesForm.language" placeholder="请选择语言">
                    <el-option label="简体中文" value="zh-CN" />
                    <el-option label="English" value="en-US" />
                  </el-select>
                </el-form-item>
                <el-form-item label="时区设置">
                  <el-select v-model="preferencesForm.timezone" placeholder="请选择时区">
                    <el-option label="中国标准时间 (UTC+8)" value="Asia/Shanghai" />
                    <el-option label="美国东部时间 (UTC-5)" value="America/New_York" />
                    <el-option label="欧洲中部时间 (UTC+1)" value="Europe/Berlin" />
                  </el-select>
                </el-form-item>
                <el-form-item label="日期格式">
                  <el-select v-model="preferencesForm.dateFormat" placeholder="请选择日期格式">
                    <el-option label="YYYY-MM-DD" value="YYYY-MM-DD" />
                    <el-option label="MM/DD/YYYY" value="MM/DD/YYYY" />
                    <el-option label="DD/MM/YYYY" value="DD/MM/YYYY" />
                  </el-select>
                </el-form-item>
                <el-form-item label="邮件通知">
                  <el-switch v-model="preferencesForm.emailNotification" />
                </el-form-item>
                <el-form-item label="短信通知">
                  <el-switch v-model="preferencesForm.smsNotification" />
                </el-form-item>
                <el-form-item label="系统通知">
                  <el-switch v-model="preferencesForm.systemNotification" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="savePreferences">保存</el-button>
                  <el-button @click="resetPreferences">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 安全设置 -->
            <el-tab-pane label="安全设置" name="security">
              <el-form :model="securityForm" label-width="120px">
                <el-form-item label="双因素认证">
                  <el-switch v-model="securityForm.twoFactorAuth" />
                  <span class="form-tip">启用后需要额外的验证码才能登录</span>
                </el-form-item>
                <el-form-item label="登录设备管理">
                  <el-button @click="manageDevices">管理登录设备</el-button>
                  <span class="form-tip">查看和管理已登录的设备</span>
                </el-form-item>
                <el-form-item label="登录历史">
                  <el-button @click="viewLoginHistory">查看登录历史</el-button>
                  <span class="form-tip">查看最近的登录记录</span>
                </el-form-item>
                <el-form-item label="账户锁定">
                  <el-button type="danger" @click="lockAccount">锁定账户</el-button>
                  <span class="form-tip">临时锁定账户，防止未授权访问</span>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="saveSecuritySettings">保存设置</el-button>
                  <el-button @click="resetSecuritySettings">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    <!-- 编辑资料对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑个人资料" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input v-model="editForm.bio" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEditForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const activeTab = ref('basic')
const editDialogVisible = ref(false)

// 用户信息
const userInfo = reactive({
  name: '陈志成',
  role: '系统管理员',
  department: '技术部',
  employeeId: 'EMP001',
  email: 'chenzhicheng@hongsitech.com',
  phone: '17600198256',
  joinDate: '2023-01-15',
  lastLogin: '2024-12-20 14:30:25',
  avatar: ''
})

// 基本信息表单
const basicForm = reactive({
  name: '陈志成',
  email: 'chenzhicheng@hongsitech.com',
  phone: '17600198256',
  gender: 'male',
  birthday: new Date('1990-01-01'),
  bio: '专注于外贸管理软件开发，拥有丰富的项目管理经验。'
})

// 密码表单
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 密码验证规则
const passwordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 偏好设置表单
const preferencesForm = reactive({
  theme: 'default',
  language: 'zh-CN',
  timezone: 'Asia/Shanghai',
  dateFormat: 'YYYY-MM-DD',
  emailNotification: true,
  smsNotification: false,
  systemNotification: true
})

// 安全设置表单
const securityForm = reactive({
  twoFactorAuth: false
})

// 编辑表单
const editForm = reactive({
  name: '',
  email: '',
  phone: '',
  bio: ''
})

// 方法
const editProfile = () => {
  editForm.name = userInfo.name
  editForm.email = userInfo.email
  editForm.phone = userInfo.phone
  editForm.bio = basicForm.bio
  editDialogVisible.value = true
}

const saveEditForm = () => {
  userInfo.name = editForm.name
  userInfo.email = editForm.email
  userInfo.phone = editForm.phone
  basicForm.bio = editForm.bio
  editDialogVisible.value = false
  ElMessage.success('个人资料已更新')
}

const uploadAvatar = () => {
  ElMessage.info('头像上传功能开发中...')
}

const saveBasicInfo = () => {
  ElMessage.success('基本信息已保存')
}

const resetBasicInfo = () => {
  ElMessageBox.confirm('确定要重置基本信息吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('基本信息已重置')
  })
}

const changePassword = () => {
  ElMessage.success('密码修改成功')
  passwordForm.currentPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

const resetPasswordForm = () => {
  passwordForm.currentPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

const savePreferences = () => {
  ElMessage.success('偏好设置已保存')
}

const resetPreferences = () => {
  ElMessageBox.confirm('确定要重置偏好设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('偏好设置已重置')
  })
}

const saveSecuritySettings = () => {
  ElMessage.success('安全设置已保存')
}

const resetSecuritySettings = () => {
  ElMessageBox.confirm('确定要重置安全设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('安全设置已重置')
  })
}

const manageDevices = () => {
  ElMessage.info('设备管理功能开发中...')
}

const viewLoginHistory = () => {
  ElMessage.info('登录历史功能开发中...')
}

const lockAccount = () => {
  ElMessageBox.confirm('确定要锁定账户吗？锁定后需要联系管理员解锁。', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('账户已锁定')
  })
}
</script>

<style lang="scss" scoped>
.profile-card {
  .profile-header {
    text-align: center;
    padding: 20px 0;
    
    .profile-info {
      margin-top: 15px;
      
      h3 {
        margin: 0 0 5px 0;
        color: #303133;
        font-size: 18px;
      }
      
      .user-role {
        margin: 5px 0;
        color: #409eff;
        font-size: 14px;
      }
      
      .user-department {
        margin: 5px 0;
        color: #909399;
        font-size: 12px;
      }
    }
  }
  
  .profile-details {
    .detail-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
      
      .label {
        color: #606266;
        font-weight: 500;
      }
      
      .value {
        color: #303133;
      }
    }
  }
  
  .profile-actions {
    display: flex;
    gap: 10px;
    justify-content: center;
  }
}

.settings-card {
  .form-tip {
    margin-left: 10px;
    color: #909399;
    font-size: 12px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .profile-card {
    margin-bottom: 20px;
  }
}
</style> 