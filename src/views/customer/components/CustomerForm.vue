<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑客户' : '新建客户'"
    width="800px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="customer-form"
    >
      <!-- 基本信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>基本信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户编号" prop="customerNo">
              <el-input 
                v-model="form.customerNo" 
                placeholder="系统自动生成"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input 
                v-model="form.customerName" 
                placeholder="请输入客户名称"
                @blur="handleCustomerNameBlur"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户类型" prop="customerType">
              <el-select 
                v-model="form.customerType" 
                placeholder="请选择客户类型"
                style="width: 100%"
              >
                <el-option label="外贸客户" value="foreign_trade" />
                <el-option label="批发客户" value="wholesale" />
                <el-option label="零售客户" value="retail" />
                <el-option label="代理商" value="agent" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户来源" prop="customerSource">
              <el-select 
                v-model="form.customerSource" 
                placeholder="请选择客户来源"
                style="width: 100%"
              >
                <el-option label="网站推广" value="website" />
                <el-option label="展会获取" value="exhibition" />
                <el-option label="老客户推荐" value="referral" />
                <el-option label="搜索引擎" value="search_engine" />
                <el-option label="社交媒体" value="social_media" />
                <el-option label="其他渠道" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contact">
              <el-input 
                v-model="form.contact" 
                placeholder="请输入联系人"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职位" prop="position">
              <el-input 
                v-model="form.position" 
                placeholder="请输入职位"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input 
                v-model="form.email" 
                placeholder="请输入邮箱"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="phone">
              <el-input 
                v-model="form.phone" 
                placeholder="请输入电话"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="国家/地区" prop="country">
              <el-select 
                v-model="form.country" 
                placeholder="请选择国家/地区"
                style="width: 100%"
              >
                <el-option label="中国" value="CN" />
                <el-option label="美国" value="US" />
                <el-option label="欧盟" value="EU" />
                <el-option label="日本" value="JP" />
                <el-option label="韩国" value="KR" />
                <el-option label="东南亚" value="SEA" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户等级" prop="level">
              <el-select 
                v-model="form.level" 
                placeholder="请选择客户等级"
                style="width: 100%"
              >
                <el-option label="A级" value="A" />
                <el-option label="B级" value="B" />
                <el-option label="C级" value="C" />
                <el-option label="D级" value="D" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="跟进进度" prop="progress">
              <el-select 
                v-model="form.progress" 
                placeholder="请选择跟进进度"
                style="width: 100%"
              >
                <el-option label="初步接触" value="initial_contact" />
                <el-option label="需求了解" value="requirement_analysis" />
                <el-option label="方案制定" value="solution_design" />
                <el-option label="报价阶段" value="quotation" />
                <el-option label="谈判阶段" value="negotiation" />
                <el-option label="成交阶段" value="deal_closed" />
                <el-option label="售后服务" value="after_sales" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择客户状态"
                style="width: 100%"
              >
                <el-option label="潜在客户" value="potential" />
                <el-option label="活跃客户" value="active" />
                <el-option label="休眠客户" value="dormant" />
                <el-option label="流失客户" value="lost" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 地址信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>地址信息</span>
          </div>
        </template>
        
        <el-form-item label="详细地址" prop="address">
          <el-input
            v-model="form.address"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
          />
        </el-form-item>

        <el-form-item label="网站" prop="website">
          <el-input 
            v-model="form.website" 
            placeholder="请输入网站地址"
          />
        </el-form-item>
      </el-card>

      <!-- 备注信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>备注信息</span>
          </div>
        </template>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-card>
    </el-form>

    <template #footer>
      <div class="form-actions">
        <el-button @click="handleClose">取消</el-button>
        <el-button 
          v-if="isEdit" 
          type="danger" 
          @click="handleDelete"
        >
          删除
        </el-button>
        <el-button type="primary" @click="handleSubmit">
          {{ isEdit ? '保存' : '创建' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  customer: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const isEdit = computed(() => !!props.customer)

// 表单数据
const form = reactive({
  customerNo: '',
  customerName: '',
  customerType: '',
  customerSource: '',
  contact: '',
  position: '',
  email: '',
  phone: '',
  country: '',
  level: '',
  progress: '',
  status: 'potential',
  address: '',
  website: '',
  remark: ''
})

// 表单验证规则
const rules = {
  customerName: [
    { required: true, message: '请输入客户名称', trigger: 'blur' }
  ],
  customerType: [
    { required: true, message: '请选择客户类型', trigger: 'change' }
  ],
  customerSource: [
    { required: true, message: '请选择客户来源', trigger: 'change' }
  ],
  contact: [
    { required: true, message: '请输入联系人', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' }
  ],
  country: [
    { required: true, message: '请选择国家/地区', trigger: 'change' }
  ],
  level: [
    { required: true, message: '请选择客户等级', trigger: 'change' }
  ],
  progress: [
    { required: true, message: '请选择跟进进度', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择客户状态', trigger: 'change' }
  ]
}

// 模拟客户数据用于查重
const existingCustomers = ref([
  { id: 1, customerName: 'ABC公司', customerType: 'foreign_trade', customerSource: 'website' },
  { id: 2, customerName: 'XYZ贸易', customerType: 'wholesale', customerSource: 'exhibition' },
  { id: 3, customerName: 'DEF集团', customerType: 'retail', customerSource: 'referral' }
])

// 生成客户编号
const generateCustomerNo = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0')
  return `C${year}${month}${day}${random}`
}

// 客户名称查重
const handleCustomerNameBlur = () => {
  if (!form.customerName) return
  
  const duplicate = existingCustomers.value.find(customer => 
    customer.customerName.toLowerCase() === form.customerName.toLowerCase() &&
    customer.id !== (props.customer?.id || 0)
  )
  
  if (duplicate) {
    ElMessage.warning('该客户名称已存在，请检查是否重复')
  }
}

// 初始化表单
const initForm = () => {
  if (props.customer) {
    // 编辑模式
    Object.assign(form, props.customer)
  } else {
    // 新增模式
    Object.assign(form, {
      customerNo: generateCustomerNo(),
      customerName: '',
      customerType: '',
      customerSource: '',
      contact: '',
      position: '',
      email: '',
      phone: '',
      country: '',
      level: '',
      progress: '',
      status: 'potential',
      address: '',
      website: '',
      remark: ''
    })
  }
}

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false)
}

// 删除客户
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个客户吗？删除后无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟删除操作
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('客户删除成功')
    emit('success')
    handleClose()
  } catch (error) {
    // 用户取消删除
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 模拟保存操作
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success(isEdit.value ? '客户更新成功' : '客户创建成功')
    emit('success')
    handleClose()
  } catch (error) {
    ElMessage.error('请检查表单信息')
  }
}

// 监听visible变化
onMounted(() => {
  if (props.visible) {
    initForm()
  }
})
</script>

<style scoped>
.customer-form {
  max-height: 70vh;
  overflow-y: auto;
}

.form-section {
  margin-bottom: 20px;
}

.form-section:last-child {
  margin-bottom: 0;
}

.card-header {
  font-weight: 600;
  color: #303133;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-card__header) {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
  background-color: #fafafa;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style> 