<template>
  <div class="quotation-form">
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
            <el-form-item label="报价单号" prop="quotationNo">
              <el-input 
                v-model="form.quotationNo" 
                placeholder="系统自动生成"
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户名称" prop="customerName">
              <el-select 
                v-model="form.customerName" 
                placeholder="请选择客户"
                filterable
                style="width: 100%"
                @change="handleCustomerChange"
              >
                <el-option 
                  v-for="customer in customerList" 
                  :key="customer.id"
                  :label="customer.customerName"
                  :value="customer.customerName"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系人" prop="contact">
              <el-input 
                v-model="form.contact" 
                placeholder="请输入联系人"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="报价日期" prop="quotationDate">
              <el-date-picker
                v-model="form.quotationDate"
                type="date"
                placeholder="选择报价日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="有效期至" prop="validUntil">
              <el-date-picker
                v-model="form.validUntil"
                type="date"
                placeholder="选择有效期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="业务员" prop="salesperson">
              <el-select 
                v-model="form.salesperson" 
                placeholder="请选择业务员"
                style="width: 100%"
              >
                <el-option label="张三" value="张三" />
                <el-option label="李四" value="李四" />
                <el-option label="王五" value="王五" />
                <el-option label="赵六" value="赵六" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="货币单位" prop="currency">
              <el-select 
                v-model="form.currency" 
                placeholder="请选择货币"
                style="width: 100%"
              >
                <el-option label="人民币(¥)" value="¥" />
                <el-option label="美元($)" value="$" />
                <el-option label="欧元(€)" value="€" />
                <el-option label="英镑(£)" value="£" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="报价状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择状态"
                style="width: 100%"
              >
                <el-option label="草稿" value="draft" />
                <el-option label="已发送" value="sent" />
                <el-option label="已确认" value="confirmed" />
                <el-option label="已拒绝" value="rejected" />
                <el-option label="已过期" value="expired" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="折扣率(%)" prop="discount">
              <el-input-number 
                v-model="form.discount" 
                :precision="2"
                :step="0.5"
                :min="0"
                :max="100"
                controls-position="right"
                style="width: 100%"
                placeholder="折扣率"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 产品信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>产品信息</span>
            <el-button type="primary" size="small" @click="handleAddProduct">
              <el-icon><Plus /></el-icon>
              添加产品
            </el-button>
          </div>
        </template>

        <el-table 
          :data="form.products" 
          border 
          class="product-table"
        >
          <el-table-column label="序号" type="index" width="60" />
          <el-table-column label="产品名称" min-width="200">
            <template #default="{ row, $index }">
              <el-select 
                v-model="row.productId" 
                placeholder="请选择产品"
                filterable
                style="width: 100%"
                @change="(value) => handleProductChange(value, $index)"
              >
                <el-option 
                  v-for="product in productList" 
                  :key="product.id"
                  :label="product.productName"
                  :value="product.id"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="规格型号" width="150">
            <template #default="{ row }">
              <span>{{ row.specifications || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="数量" width="120">
            <template #default="{ row, $index }">
              <el-input-number 
                v-model="row.quantity" 
                :min="1"
                :precision="0"
                controls-position="right"
                style="width: 100%"
                @change="() => calculateRowAmount($index)"
              />
            </template>
          </el-table-column>
          <el-table-column label="单价" width="120">
            <template #default="{ row, $index }">
              <el-input-number 
                v-model="row.price" 
                :precision="2"
                :min="0"
                controls-position="right"
                style="width: 100%"
                @change="() => calculateRowAmount($index)"
              />
            </template>
          </el-table-column>
          <el-table-column label="小计" width="120">
            <template #default="{ row }">
              <span class="amount-text">{{ form.currency }} {{ row.amount.toLocaleString() }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ $index }">
              <el-button 
                type="danger" 
                size="small" 
                @click="handleRemoveProduct($index)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 合计信息 -->
        <div class="total-section">
          <div class="total-row">
            <span class="total-label">产品小计:</span>
            <span class="total-value">{{ form.currency }} {{ subtotal.toLocaleString() }}</span>
          </div>
          <div class="total-row">
            <span class="total-label">折扣金额:</span>
            <span class="total-value discount">{{ form.currency }} {{ discountAmount.toLocaleString() }}</span>
          </div>
          <div class="total-row total-final">
            <span class="total-label">报价总额:</span>
            <span class="total-value final">{{ form.currency }} {{ totalAmount.toLocaleString() }}</span>
          </div>
        </div>
      </el-card>

      <!-- 备注信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>备注信息</span>
          </div>
        </template>
        
        <el-form-item label="报价备注" prop="remarks">
          <el-input 
            v-model="form.remarks" 
            type="textarea" 
            :rows="4"
            placeholder="请输入报价备注信息"
          />
        </el-form-item>

        <el-form-item label="特殊条款" prop="specialTerms">
          <el-input 
            v-model="form.specialTerms" 
            type="textarea" 
            :rows="3"
            placeholder="请输入特殊条款"
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
        <el-button v-if="!isEdit" type="success" @click="handleSend">
          <el-icon><Message /></el-icon>
          保存并发送
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  quotation: {
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
  quotationNo: '',
  customerName: '',
  contact: '',
  quotationDate: new Date(),
  validUntil: null,
  salesperson: '',
  currency: '¥',
  status: 'draft',
  discount: 0,
  products: [],
  remarks: '',
  specialTerms: ''
})

// 表单验证规则
const rules = {
  customerName: [
    { required: true, message: '请选择客户', trigger: 'change' }
  ],
  contact: [
    { required: true, message: '请输入联系人', trigger: 'blur' }
  ],
  quotationDate: [
    { required: true, message: '请选择报价日期', trigger: 'change' }
  ],
  validUntil: [
    { required: true, message: '请选择有效期', trigger: 'change' }
  ],
  salesperson: [
    { required: true, message: '请选择业务员', trigger: 'change' }
  ],
  currency: [
    { required: true, message: '请选择货币单位', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择报价状态', trigger: 'change' }
  ]
}

// 模拟客户数据
const customerList = ref([
  { id: 1, customerName: 'ABC Trading Co.', contact: 'John Smith' },
  { id: 2, customerName: 'XYZ Import Ltd', contact: 'Sarah Johnson' },
  { id: 3, customerName: 'Global Solutions', contact: 'Michael Brown' },
  { id: 4, customerName: 'Eastern Trading', contact: 'Yuki Tanaka' },
  { id: 5, customerName: 'Pacific Imports', contact: 'Emma Wilson' }
])

// 模拟产品数据
const productList = ref([
  { id: 1, productName: '智能蓝牙耳机', specifications: 'TWS无线 / 降噪', price: 299 },
  { id: 2, productName: '无线充电器', specifications: '15W快充 / 兼容多设备', price: 129 },
  { id: 3, productName: '便携式音箱', specifications: '防水 / 蓝牙5.0 / 12小时续航', price: 199 },
  { id: 4, productName: '智能手环', specifications: '心率监测 / 运动追踪', price: 399 },
  { id: 5, productName: 'USB数据线', specifications: 'Type-C / 3米 / 快充支持', price: 29 },
  { id: 6, productName: '平板电脑保护套', specifications: '皮质 / 支架功能 / 多角度', price: 89 }
])

// 计算属性
const subtotal = computed(() => {
  return form.products.reduce((sum, product) => sum + product.amount, 0)
})

const discountAmount = computed(() => {
  return (subtotal.value * form.discount / 100)
})

const totalAmount = computed(() => {
  return subtotal.value - discountAmount.value
})

// 生成报价单号
const generateQuotationNo = () => {
  const date = new Date()
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
  return `Q${year}${month}${day}${random}`
}

// 初始化表单
const initForm = () => {
  if (props.quotation) {
    Object.keys(form).forEach(key => {
      if (props.quotation[key] !== undefined) {
        form[key] = props.quotation[key]
      }
    })
    // 处理日期格式
    if (form.quotationDate) {
      form.quotationDate = new Date(form.quotationDate)
    }
    if (form.validUntil) {
      form.validUntil = new Date(form.validUntil)
    }
  } else {
    // 新增时生成报价单号
    form.quotationNo = generateQuotationNo()
    form.quotationDate = new Date()
    // 设置默认有效期为30天后
    const validDate = new Date()
    validDate.setDate(validDate.getDate() + 30)
    form.validUntil = validDate
  }
}

// 客户选择变化
const handleCustomerChange = (customerName) => {
  const customer = customerList.value.find(c => c.customerName === customerName)
  if (customer) {
    form.contact = customer.contact
  }
}

// 添加产品
const handleAddProduct = () => {
  form.products.push({
    productId: '',
    productName: '',
    specifications: '',
    quantity: 1,
    price: 0,
    amount: 0
  })
}

// 移除产品
const handleRemoveProduct = (index) => {
  form.products.splice(index, 1)
}

// 产品选择变化
const handleProductChange = (productId, index) => {
  const product = productList.value.find(p => p.id === productId)
  if (product) {
    form.products[index].productName = product.productName
    form.products[index].specifications = product.specifications
    form.products[index].price = product.price
    calculateRowAmount(index)
  }
}

// 计算行金额
const calculateRowAmount = (index) => {
  const product = form.products[index]
  product.amount = product.quantity * product.price
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 验证产品信息
    if (form.products.length === 0) {
      ElMessage.warning('请至少添加一个产品')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(props.isEdit ? '报价单更新成功' : '报价单创建成功')
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

// 删除报价单
const handleDelete = () => {
  ElMessageBox.confirm(
    `确定要删除报价单 "${form.quotationNo}" 吗？此操作不可恢复！`,
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
      
      ElMessage.success('报价单删除成功')
      emit('success')
    } catch (error) {
      ElMessage.error('删除失败，请重试')
    } finally {
      submitting.value = false
    }
  })
}

// 保存并发送
const handleSend = async () => {
  try {
    await formRef.value.validate()
    
    if (form.products.length === 0) {
      ElMessage.warning('请至少添加一个产品')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    form.status = 'sent'
    ElMessage.success('报价单已保存并发送给客户')
    emit('success')
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

// 监听报价单数据变化
watch(() => props.quotation, () => {
  initForm()
}, { immediate: true })

onMounted(() => {
  initForm()
})
</script>

<style lang="scss" scoped>
.quotation-form {
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
  
  .product-table {
    margin-bottom: 20px;
  }
  
  .total-section {
    background: #f8f9fa;
    padding: 15px;
    border-radius: 4px;
    
    .total-row {
      display: flex;
      justify-content: space-between;
      margin-bottom: 8px;
      
      &:last-child {
        margin-bottom: 0;
        border-top: 1px solid #e4e7ed;
        padding-top: 8px;
      }
      
      .total-label {
        font-weight: 600;
        color: #606266;
      }
      
      .total-value {
        font-weight: 600;
        
        &.discount {
          color: #E6A23C;
        }
        
        &.final {
          color: #67C23A;
          font-size: 16px;
        }
      }
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
  
  .amount-text {
    font-weight: 600;
    color: #67C23A;
  }
}
</style> 