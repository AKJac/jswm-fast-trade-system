<template>
  <div class="user-form">
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="form-container"
    >
      <!-- 基本信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>基本信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="用户名" prop="username">
              <el-input 
                v-model="form.username" 
                placeholder="请输入用户名"
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="真实姓名" prop="realName">
              <el-input 
                v-model="form.realName" 
                placeholder="请输入真实姓名"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="邮箱" prop="email">
              <el-input 
                v-model="form.email" 
                placeholder="请输入邮箱地址"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="手机号" prop="phone">
              <el-input 
                v-model="form.phone" 
                placeholder="请输入手机号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="部门" prop="department">
              <el-select 
                v-model="form.department" 
                placeholder="请选择部门"
                style="width: 100%"
              >
                <el-option label="销售部" value="sales" />
                <el-option label="采购部" value="purchase" />
                <el-option label="财务部" value="finance" />
                <el-option label="行政部" value="admin" />
                <el-option label="技术部" value="tech" />
                <el-option label="人事部" value="hr" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="职位" prop="position">
              <el-input 
                v-model="form.position" 
                placeholder="请输入职位"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="角色" prop="role">
              <el-select 
                v-model="form.role" 
                placeholder="请选择角色"
                style="width: 100%"
              >
                <el-option label="系统管理员" value="admin" />
                <el-option label="主管" value="supervisor" />
                <el-option label="业务员" value="salesperson" />
                <el-option label="财务" value="finance" />
                <el-option label="采购员" value="purchaser" />
                <el-option label="普通用户" value="user" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择状态"
                style="width: 100%"
              >
                <el-option label="启用" value="active" />
                <el-option label="禁用" value="inactive" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="工号" prop="employeeNo">
              <el-input 
                v-model="form.employeeNo" 
                placeholder="请输入工号"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 密码设置 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>密码设置</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item 
              label="密码" 
              prop="password"
              :rules="isEdit ? [] : rules.password"
            >
              <el-input 
                v-model="form.password" 
                type="password"
                placeholder="请输入密码"
                show-password
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item 
              label="确认密码" 
              prop="confirmPassword"
              :rules="isEdit ? [] : rules.confirmPassword"
            >
              <el-input 
                v-model="form.confirmPassword" 
                type="password"
                placeholder="请再次输入密码"
                show-password
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="密码有效期" prop="passwordExpireDays">
              <el-input-number 
                v-model="form.passwordExpireDays" 
                :min="0"
                :max="365"
                controls-position="right"
                style="width: 100%"
                placeholder="密码有效期(天)"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="强制修改密码">
              <el-switch 
                v-model="form.forceChangePassword"
                active-text="是"
                inactive-text="否"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="允许登录">
              <el-switch 
                v-model="form.allowLogin"
                active-text="是"
                inactive-text="否"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="登录失败锁定">
              <el-switch 
                v-model="form.loginLock"
                active-text="是"
                inactive-text="否"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 权限设置 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>权限设置</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据权限">
              <el-checkbox-group v-model="form.dataPermissions">
                <el-checkbox label="all" border>全部数据</el-checkbox>
                <el-checkbox label="department" border>部门数据</el-checkbox>
                <el-checkbox label="own" border>个人数据</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="功能权限">
              <el-checkbox-group v-model="form.functionPermissions">
                <el-checkbox label="customer" border>客户管理</el-checkbox>
                <el-checkbox label="product" border>产品管理</el-checkbox>
                <el-checkbox label="order" border>订单管理</el-checkbox>
                <el-checkbox label="finance" border>财务管理</el-checkbox>
                <el-checkbox label="system" border>系统管理</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="操作权限">
              <el-checkbox-group v-model="form.operationPermissions">
                <el-checkbox label="view" border>查看</el-checkbox>
                <el-checkbox label="add" border>新增</el-checkbox>
                <el-checkbox label="edit" border>编辑</el-checkbox>
                <el-checkbox label="delete" border>删除</el-checkbox>
                <el-checkbox label="export" border>导出</el-checkbox>
                <el-checkbox label="import" border>导入</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="特殊权限">
              <el-checkbox-group v-model="form.specialPermissions">
                <el-checkbox label="approve" border>审批</el-checkbox>
                <el-checkbox label="audit" border>审计</el-checkbox>
                <el-checkbox label="admin" border>管理</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 其他信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>其他信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="入职日期" prop="hireDate">
              <el-date-picker
                v-model="form.hireDate"
                type="date"
                placeholder="选择入职日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="直属上级" prop="supervisor">
              <el-select 
                v-model="form.supervisor" 
                placeholder="请选择直属上级"
                filterable
                style="width: 100%"
              >
                <el-option 
                  v-for="user in supervisorList" 
                  :key="user.id"
                  :label="user.realName"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="办公地点" prop="officeLocation">
              <el-input 
                v-model="form.officeLocation" 
                placeholder="请输入办公地点"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="紧急联系人" prop="emergencyContact">
              <el-input 
                v-model="form.emergencyContact" 
                placeholder="请输入紧急联系人"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急联系电话" prop="emergencyPhone">
              <el-input 
                v-model="form.emergencyPhone" 
                placeholder="请输入紧急联系电话"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remarks">
          <el-input 
            v-model="form.remarks" 
            type="textarea" 
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-card>

      <!-- 操作按钮 -->
      <div class="form-actions">
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          {{ isEdit ? '更新' : '保存' }}
        </el-button>
        <el-button @click="handleCancel">取消</el-button>
        <el-button v-if="isEdit" type="danger" @click="handleDelete">删除</el-button>
        <el-button v-if="isEdit" type="warning" @click="handleResetPassword">
          <el-icon><Key /></el-icon>
          重置密码
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  user: {
    type: Object,
    default: null
  },
  isEdit: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['success', 'cancel'])

const formRef = ref()
const submitting = ref(false)

// 表单数据
const form = reactive({
  username: '',
  realName: '',
  email: '',
  phone: '',
  department: '',
  position: '',
  role: '',
  status: 'active',
  employeeNo: '',
  password: '',
  confirmPassword: '',
  passwordExpireDays: 90,
  forceChangePassword: false,
  allowLogin: true,
  loginLock: true,
  dataPermissions: ['own'],
  functionPermissions: ['customer', 'product'],
  operationPermissions: ['view', 'add', 'edit'],
  specialPermissions: [],
  hireDate: '',
  supervisor: '',
  officeLocation: '',
  emergencyContact: '',
  emergencyPhone: '',
  remarks: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 模拟数据
const supervisorList = ref([
  { id: 1, realName: '陈志成', department: 'admin' },
  { id: 2, realName: '张三', department: 'sales' },
  { id: 3, realName: '李四', department: 'sales' },
  { id: 4, realName: '王五', department: 'finance' },
  { id: 5, realName: '赵六', department: 'purchase' }
])

// 初始化表单
const initForm = () => {
  if (props.user) {
    Object.keys(form).forEach(key => {
      if (props.user[key] !== undefined) {
        form[key] = props.user[key]
      }
    })
  } else {
    // 新增时的默认值
    form.status = 'active'
    form.passwordExpireDays = 90
    form.forceChangePassword = false
    form.allowLogin = true
    form.loginLock = true
    form.dataPermissions = ['own']
    form.functionPermissions = ['customer', 'product']
    form.operationPermissions = ['view', 'add', 'edit']
    form.specialPermissions = []
    form.hireDate = new Date().toISOString().split('T')[0]
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(props.isEdit ? '用户信息更新成功' : '用户创建成功')
    emit('success')
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

// 取消操作
const handleCancel = () => {
  emit('cancel')
}

// 删除用户
const handleDelete = () => {
  ElMessageBox.confirm(
    `确定要删除用户 "${form.realName}" 吗？此操作不可恢复！`,
    '警告',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      submitting.value = true
      // 模拟删除API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      ElMessage.success('用户删除成功')
      emit('success')
    } catch (error) {
      ElMessage.error('删除失败，请重试')
    } finally {
      submitting.value = false
    }
  })
}

// 重置密码
const handleResetPassword = () => {
  ElMessageBox.confirm(
    `确定要重置用户 "${form.realName}" 的密码吗？`,
    '确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      submitting.value = true
      // 模拟重置密码API调用
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      ElMessage.success('密码重置成功，新密码已发送到用户邮箱')
    } catch (error) {
      ElMessage.error('密码重置失败，请重试')
    } finally {
      submitting.value = false
    }
  })
}

// 监听用户数据变化
watch(() => props.user, () => {
  initForm()
}, { immediate: true })

onMounted(() => {
  initForm()
})
</script>

<style lang="scss" scoped>
.user-form {
  .form-container {
    padding: 20px;
  }
  
  .form-section {
    margin-bottom: 20px;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
  
  .form-actions {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #e4e7ed;
  }
}
</style> 