<template>
  <div class="page-container">
    <div class="page-header">
      <h1>系统设置</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>系统设置</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-row :gutter="20">
      <!-- 左侧设置菜单 -->
      <el-col :xs="24" :sm="8" :md="6" :lg="6" :xl="6">
        <el-card class="settings-menu-card">
          <el-menu
            :default-active="activeSetting"
            @select="handleSettingSelect"
            class="settings-menu"
          >
            <el-menu-item index="basic">
              <el-icon><Setting /></el-icon>
              <span>基本设置</span>
            </el-menu-item>
            <el-menu-item index="security">
              <el-icon><Lock /></el-icon>
              <span>安全设置</span>
            </el-menu-item>
            <el-menu-item index="notification">
              <el-icon><Bell /></el-icon>
              <span>通知设置</span>
            </el-menu-item>
            <el-menu-item index="backup">
              <el-icon><Download /></el-icon>
              <span>备份设置</span>
            </el-menu-item>
            <el-menu-item index="company">
              <el-icon><OfficeBuilding /></el-icon>
              <span>公司信息</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-col>

      <!-- 右侧设置内容 -->
      <el-col :xs="24" :sm="16" :md="18" :lg="18" :xl="18">
        <el-card class="settings-content-card">
          <!-- 基本设置 -->
          <div v-if="activeSetting === 'basic'" class="setting-section">
            <h3>基本设置</h3>
            <el-form :model="basicSettings" label-width="120px">
              <el-form-item label="系统名称">
                <el-input v-model="basicSettings.systemName" placeholder="请输入系统名称" />
              </el-form-item>
              <el-form-item label="系统语言">
                <el-select v-model="basicSettings.language" placeholder="请选择语言">
                  <el-option label="简体中文" value="zh-CN" />
                  <el-option label="English" value="en-US" />
                </el-select>
              </el-form-item>
              <el-form-item label="时区设置">
                <el-select v-model="basicSettings.timezone" placeholder="请选择时区">
                  <el-option label="中国标准时间 (UTC+8)" value="Asia/Shanghai" />
                  <el-option label="美国东部时间 (UTC-5)" value="America/New_York" />
                  <el-option label="欧洲中部时间 (UTC+1)" value="Europe/Berlin" />
                </el-select>
              </el-form-item>
              <el-form-item label="日期格式">
                <el-select v-model="basicSettings.dateFormat" placeholder="请选择日期格式">
                  <el-option label="YYYY-MM-DD" value="YYYY-MM-DD" />
                  <el-option label="MM/DD/YYYY" value="MM/DD/YYYY" />
                  <el-option label="DD/MM/YYYY" value="DD/MM/YYYY" />
                </el-select>
              </el-form-item>
              <el-form-item label="货币符号">
                <el-input v-model="basicSettings.currency" placeholder="请输入货币符号" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveBasicSettings">保存设置</el-button>
                <el-button @click="resetBasicSettings">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 安全设置 -->
          <div v-if="activeSetting === 'security'" class="setting-section">
            <h3>安全设置</h3>
            <el-form :model="securitySettings" label-width="120px">
              <el-form-item label="密码策略">
                <el-checkbox-group v-model="securitySettings.passwordPolicy">
                  <el-checkbox label="requireUppercase">必须包含大写字母</el-checkbox>
                  <el-checkbox label="requireLowercase">必须包含小写字母</el-checkbox>
                  <el-checkbox label="requireNumbers">必须包含数字</el-checkbox>
                  <el-checkbox label="requireSpecialChars">必须包含特殊字符</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item label="最小密码长度">
                <el-input-number v-model="securitySettings.minPasswordLength" :min="6" :max="20" />
              </el-form-item>
              <el-form-item label="密码有效期">
                <el-input-number v-model="securitySettings.passwordExpiryDays" :min="0" :max="365" />
                <span class="form-tip">天（0表示永不过期）</span>
              </el-form-item>
              <el-form-item label="登录失败锁定">
                <el-input-number v-model="securitySettings.maxLoginAttempts" :min="3" :max="10" />
                <span class="form-tip">次后锁定账户</span>
              </el-form-item>
              <el-form-item label="会话超时">
                <el-input-number v-model="securitySettings.sessionTimeout" :min="10" :max="480" />
                <span class="form-tip">分钟</span>
              </el-form-item>
              <el-form-item label="双因素认证">
                <el-switch v-model="securitySettings.twoFactorAuth" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveSecuritySettings">保存设置</el-button>
                <el-button @click="resetSecuritySettings">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 通知设置 -->
          <div v-if="activeSetting === 'notification'" class="setting-section">
            <h3>通知设置</h3>
            <el-form :model="notificationSettings" label-width="120px">
              <el-form-item label="邮件通知">
                <el-switch v-model="notificationSettings.emailNotification" />
              </el-form-item>
              <el-form-item label="SMTP服务器">
                <el-input v-model="notificationSettings.smtpServer" placeholder="请输入SMTP服务器地址" />
              </el-form-item>
              <el-form-item label="SMTP端口">
                <el-input-number v-model="notificationSettings.smtpPort" :min="1" :max="65535" />
              </el-form-item>
              <el-form-item label="发件人邮箱">
                <el-input v-model="notificationSettings.senderEmail" placeholder="请输入发件人邮箱" />
              </el-form-item>
              <el-form-item label="邮箱密码">
                <el-input v-model="notificationSettings.emailPassword" type="password" placeholder="请输入邮箱密码" />
              </el-form-item>
              <el-form-item label="通知类型">
                <el-checkbox-group v-model="notificationSettings.notificationTypes">
                  <el-checkbox label="orderCreated">新订单通知</el-checkbox>
                  <el-checkbox label="orderStatusChanged">订单状态变更</el-checkbox>
                  <el-checkbox label="paymentReceived">收到付款通知</el-checkbox>
                  <el-checkbox label="inventoryLow">库存不足警告</el-checkbox>
                  <el-checkbox label="systemAlert">系统告警</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveNotificationSettings">保存设置</el-button>
                <el-button @click="testEmailSettings">测试邮件</el-button>
                <el-button @click="resetNotificationSettings">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 备份设置 -->
          <div v-if="activeSetting === 'backup'" class="setting-section">
            <h3>备份设置</h3>
            <el-form :model="backupSettings" label-width="120px">
              <el-form-item label="自动备份">
                <el-switch v-model="backupSettings.autoBackup" />
              </el-form-item>
              <el-form-item label="备份频率">
                <el-select v-model="backupSettings.backupFrequency" :disabled="!backupSettings.autoBackup">
                  <el-option label="每日" value="daily" />
                  <el-option label="每周" value="weekly" />
                  <el-option label="每月" value="monthly" />
                </el-select>
              </el-form-item>
              <el-form-item label="备份时间">
                <el-time-picker v-model="backupSettings.backupTime" format="HH:mm" :disabled="!backupSettings.autoBackup" />
              </el-form-item>
              <el-form-item label="保留备份数">
                <el-input-number v-model="backupSettings.retentionCount" :min="1" :max="100" />
                <span class="form-tip">个</span>
              </el-form-item>
              <el-form-item label="备份路径">
                <el-input v-model="backupSettings.backupPath" placeholder="请输入备份路径" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveBackupSettings">保存设置</el-button>
                <el-button @click="manualBackup">立即备份</el-button>
                <el-button @click="resetBackupSettings">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 公司信息 -->
          <div v-if="activeSetting === 'company'" class="setting-section">
            <h3>公司信息</h3>
            <el-form :model="companySettings" label-width="120px">
              <el-form-item label="公司名称">
                <el-input v-model="companySettings.companyName" placeholder="请输入公司名称" />
              </el-form-item>
              <el-form-item label="公司地址">
                <el-input v-model="companySettings.companyAddress" type="textarea" :rows="3" placeholder="请输入公司地址" />
              </el-form-item>
              <el-form-item label="联系电话">
                <el-input v-model="companySettings.phone" placeholder="请输入联系电话" />
              </el-form-item>
              <el-form-item label="联系邮箱">
                <el-input v-model="companySettings.email" placeholder="请输入联系邮箱" />
              </el-form-item>
              <el-form-item label="公司网站">
                <el-input v-model="companySettings.website" placeholder="请输入公司网站" />
              </el-form-item>
              <el-form-item label="营业执照号">
                <el-input v-model="companySettings.businessLicense" placeholder="请输入营业执照号" />
              </el-form-item>
              <el-form-item label="公司简介">
                <el-input v-model="companySettings.description" type="textarea" :rows="4" placeholder="请输入公司简介" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveCompanySettings">保存设置</el-button>
                <el-button @click="resetCompanySettings">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const activeSetting = ref('basic')

// 基本设置
const basicSettings = reactive({
  systemName: '极速外贸管理系统',
  language: 'zh-CN',
  timezone: 'Asia/Shanghai',
  dateFormat: 'YYYY-MM-DD',
  currency: '¥'
})

// 安全设置
const securitySettings = reactive({
  passwordPolicy: ['requireUppercase', 'requireNumbers'],
  minPasswordLength: 8,
  passwordExpiryDays: 90,
  maxLoginAttempts: 5,
  sessionTimeout: 30,
  twoFactorAuth: false
})

// 通知设置
const notificationSettings = reactive({
  emailNotification: true,
  smtpServer: 'smtp.163.com',
  smtpPort: 465,
  senderEmail: 'hst_co@163.com',
  emailPassword: '',
  notificationTypes: ['orderCreated', 'orderStatusChanged', 'paymentReceived']
})

// 备份设置
const backupSettings = reactive({
  autoBackup: true,
  backupFrequency: 'daily',
  backupTime: new Date(2024, 0, 1, 2, 0),
  retentionCount: 30,
  backupPath: '/backup'
})

// 公司信息
const companySettings = reactive({
  companyName: '鸿思特科技有限公司',
  companyAddress: '北京市朝阳区xxx街道xxx号',
  phone: '17600198256',
  email: 'hst_co@163.com',
  website: 'https://www.hongsitech.com',
  businessLicense: '91110105MA00XXX123',
  description: '鸿思特科技有限公司是一家专注于外贸管理软件开发的科技公司，致力于为客户提供专业的外贸管理解决方案。'
})

// 方法
const handleSettingSelect = (key) => {
  activeSetting.value = key
}

const saveBasicSettings = () => {
  ElMessage.success('基本设置已保存')
}

const resetBasicSettings = () => {
  ElMessageBox.confirm('确定要重置基本设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('基本设置已重置')
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

const saveNotificationSettings = () => {
  ElMessage.success('通知设置已保存')
}

const testEmailSettings = () => {
  ElMessage.success('测试邮件发送功能开发中...')
}

const resetNotificationSettings = () => {
  ElMessageBox.confirm('确定要重置通知设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('通知设置已重置')
  })
}

const saveBackupSettings = () => {
  ElMessage.success('备份设置已保存')
}

const manualBackup = () => {
  ElMessage.success('手动备份功能开发中...')
}

const resetBackupSettings = () => {
  ElMessageBox.confirm('确定要重置备份设置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('备份设置已重置')
  })
}

const saveCompanySettings = () => {
  ElMessage.success('公司信息已保存')
}

const resetCompanySettings = () => {
  ElMessageBox.confirm('确定要重置公司信息吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('公司信息已重置')
  })
}
</script>

<style lang="scss" scoped>
.settings-menu-card {
  .settings-menu {
    border-right: none;
    
    :deep(.el-menu-item) {
      height: 50px;
      line-height: 50px;
      
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
  }
}

.settings-content-card {
  .setting-section {
    h3 {
      margin: 0 0 20px 0;
      color: #303133;
      font-size: 18px;
      font-weight: 600;
    }
    
    .form-tip {
      margin-left: 10px;
      color: #909399;
      font-size: 12px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .settings-menu-card {
    margin-bottom: 20px;
  }
}
</style> 