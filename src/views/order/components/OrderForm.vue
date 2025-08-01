<template>
  <div class="order-form">
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
          <el-col :span="6">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input 
                v-model="form.orderNo" 
                placeholder="系统自动生成"
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="订单类型" prop="orderType">
              <el-select 
                v-model="form.orderType" 
                placeholder="请选择订单类型"
                style="width: 100%"
                @change="handleOrderTypeChange"
              >
                <el-option label="外贸订单" value="export" />
                <el-option label="批发订单" value="wholesale" />
                <el-option label="退货订单" value="return" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
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
          <el-col :span="6">
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
            <el-form-item label="订单日期" prop="orderDate">
              <el-date-picker
                v-model="form.orderDate"
                type="date"
                placeholder="选择订单日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="交货日期" prop="deliveryDate">
              <el-date-picker
                v-model="form.deliveryDate"
                type="date"
                placeholder="选择交货日期"
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
            <el-form-item label="订单状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择状态"
                style="width: 100%"
              >
                <el-option label="待审核" value="pending" />
                <el-option label="生产中" value="producing" />
                <el-option label="待发货" value="ready" />
                <el-option label="已发货" value="shipped" />
                <el-option label="已完成" value="completed" />
                <el-option label="已取消" value="cancelled" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="付款状态" prop="paymentStatus">
              <el-select 
                v-model="form.paymentStatus" 
                placeholder="请选择付款状态"
                style="width: 100%"
              >
                <el-option label="未付款" value="unpaid" />
                <el-option label="部分付款" value="partial" />
                <el-option label="已付款" value="paid" />
              </el-select>
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
            <span class="total-label">运费:</span>
            <el-input-number 
              v-model="form.shippingFee" 
              :precision="2"
              :min="0"
              controls-position="right"
              style="width: 120px"
              @change="calculateTotal"
            />
          </div>
          <div class="total-row">
            <span class="total-label">折扣金额:</span>
            <el-input-number 
              v-model="form.discount" 
              :precision="2"
              :min="0"
              controls-position="right"
              style="width: 120px"
              @change="calculateTotal"
            />
          </div>
          <div class="total-row total-final">
            <span class="total-label">订单总额:</span>
            <span class="total-value final">{{ form.currency }} {{ totalAmount.toLocaleString() }}</span>
          </div>
        </div>
      </el-card>

      <!-- 退货信息 -->
      <el-card v-if="form.orderType === 'return'" class="form-section">
        <template #header>
          <div class="card-header">
            <span>退货信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="退货原因" prop="returnReason">
              <el-select 
                v-model="form.returnReason" 
                placeholder="请选择退货原因"
                style="width: 100%"
              >
                <el-option label="质量问题" value="quality" />
                <el-option label="数量错误" value="quantity" />
                <el-option label="规格不符" value="specification" />
                <el-option label="包装破损" value="packaging" />
                <el-option label="客户原因" value="customer" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="退款金额" prop="returnAmount">
              <el-input-number
                v-model="form.returnAmount"
                :min="0"
                :precision="2"
                placeholder="请输入退款金额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="退货详细说明" prop="returnDescription">
          <el-input 
            v-model="form.returnDescription" 
            type="textarea" 
            :rows="3"
            placeholder="请详细描述退货原因"
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
        
        <el-form-item label="订单备注" prop="remarks">
          <el-input 
            v-model="form.remarks" 
            type="textarea" 
            :rows="4"
            placeholder="请输入订单备注信息"
          />
        </el-form-item>

        <el-form-item label="特殊要求" prop="specialRequirements">
          <el-input 
            v-model="form.specialRequirements" 
            type="textarea" 
            :rows="3"
            placeholder="请输入特殊要求"
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
        <el-button v-if="!isEdit" type="success" @click="handleApprove">
          <el-icon><Check /></el-icon>
          保存并审核
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  order: {
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
  orderNo: '',
  orderType: 'export',
  customerName: '',
  contact: '',
  orderDate: new Date(),
  deliveryDate: null,
  salesperson: '',
  currency: '¥',
  status: 'pending',
  paymentStatus: 'unpaid',
  products: [],
  shippingFee: 0,
  discount: 0,
  remarks: '',
  specialRequirements: '',
  // 退货订单专用字段
  returnReason: '',
  returnAmount: 0,
  returnDescription: ''
})

// 表单验证规则
const rules = {
  customerName: [
    { required: true, message: '请选择客户', trigger: 'change' }
  ],
  contact: [
    { required: true, message: '请输入联系人', trigger: 'blur' }
  ],
  orderDate: [
    { required: true, message: '请选择订单日期', trigger: 'change' }
  ],
  deliveryDate: [
    { required: true, message: '请选择交货日期', trigger: 'change' }
  ],
  salesperson: [
    { required: true, message: '请选择业务员', trigger: 'change' }
  ],
  currency: [
    { required: true, message: '请选择货币单位', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择订单状态', trigger: 'change' }
  ],
  paymentStatus: [
    { required: true, message: '请选择付款状态', trigger: 'change' }
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

const totalAmount = computed(() => {
  return Math.max(0, subtotal.value + form.shippingFee - form.discount)
})

// 生成订单编号
const generateOrderNo = (orderType = 'export') => {
  const date = new Date()
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
  
  // 根据订单类型生成不同前缀
  const prefixMap = {
    export: 'SO',      // 外贸订单 Sales Order
    wholesale: 'WO',   // 批发订单 Wholesale Order
    return: 'RO'       // 退货订单 Return Order
  }
  
  const prefix = prefixMap[orderType] || 'SO'
  return `${prefix}${year}${month}${day}${random}`
}

// 初始化表单
const initForm = () => {
  if (props.order) {
    Object.keys(form).forEach(key => {
      if (props.order[key] !== undefined) {
        form[key] = props.order[key]
      }
    })
    // 处理日期格式
    if (form.orderDate) {
      form.orderDate = new Date(form.orderDate)
    }
    if (form.deliveryDate) {
      form.deliveryDate = new Date(form.deliveryDate)
    }
  } else {
    // 新增时生成订单编号
    form.orderNo = generateOrderNo(form.orderType)
    form.orderDate = new Date()
    // 设置默认交货日期为30天后
    const deliveryDate = new Date()
    deliveryDate.setDate(deliveryDate.getDate() + 30)
    form.deliveryDate = deliveryDate
  }
}

// 客户选择变化
const handleCustomerChange = (customerName) => {
  const customer = customerList.value.find(c => c.customerName === customerName)
  if (customer) {
    form.contact = customer.contact
  }
}

// 订单类型变化
const handleOrderTypeChange = (orderType) => {
  // 根据订单类型重新生成订单编号
  if (!props.isEdit) {
    form.orderNo = generateOrderNo(orderType)
  }
  
  // 清空退货相关字段
  if (orderType !== 'return') {
    form.returnReason = ''
    form.returnAmount = 0
    form.returnDescription = ''
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

// 计算总额
const calculateTotal = () => {
  // 总额会自动通过计算属性更新
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
    
    ElMessage.success(props.isEdit ? '订单更新成功' : '订单创建成功')
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

// 删除订单
const handleDelete = () => {
  ElMessageBox.confirm(
    `确定要删除订单 "${form.orderNo}" 吗？此操作不可恢复！`,
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
      
      ElMessage.success('订单删除成功')
      emit('success')
    } catch (error) {
      ElMessage.error('删除失败，请重试')
    } finally {
      submitting.value = false
    }
  })
}

// 保存并审核
const handleApprove = async () => {
  try {
    await formRef.value.validate()
    
    if (form.products.length === 0) {
      ElMessage.warning('请至少添加一个产品')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    form.status = 'producing'
    ElMessage.success('订单已保存并审核通过')
    emit('success')
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

// 监听订单数据变化
watch(() => props.order, () => {
  initForm()
}, { immediate: true })

onMounted(() => {
  initForm()
})
</script>

<style lang="scss" scoped>
.order-form {
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
      align-items: center;
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