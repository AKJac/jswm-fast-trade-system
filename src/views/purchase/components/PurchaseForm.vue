<template>
  <div class="purchase-form">
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
            <el-form-item label="采购单号" prop="purchaseNo">
              <el-input 
                v-model="form.purchaseNo" 
                placeholder="系统自动生成"
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="关联订单" prop="relatedOrderNo">
              <el-select 
                v-model="form.relatedOrderNo" 
                placeholder="请选择关联订单"
                filterable
                clearable
                style="width: 100%"
              >
                <el-option 
                  v-for="order in orderList" 
                  :key="order.orderNo"
                  :label="`${order.orderNo} - ${order.customerName}`"
                  :value="order.orderNo"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="采购日期" prop="purchaseDate">
              <el-date-picker
                v-model="form.purchaseDate"
                type="date"
                placeholder="选择采购日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="供应商" prop="supplierName">
              <el-select 
                v-model="form.supplierName" 
                placeholder="请选择供应商"
                filterable
                style="width: 100%"
                @change="handleSupplierChange"
              >
                <el-option 
                  v-for="supplier in supplierList" 
                  :key="supplier.id"
                  :label="supplier.name"
                  :value="supplier.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="采购员" prop="purchaser">
              <el-select 
                v-model="form.purchaser" 
                placeholder="请选择采购员"
                style="width: 100%"
              >
                <el-option label="赵六" value="赵六" />
                <el-option label="周七" value="周七" />
                <el-option label="吴八" value="吴八" />
                <el-option label="郑九" value="郑九" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预计到货" prop="expectedDate">
              <el-date-picker
                v-model="form.expectedDate"
                type="date"
                placeholder="选择预计到货日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="采购状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择状态"
                style="width: 100%"
              >
                <el-option label="待审核" value="pending" />
                <el-option label="已审核" value="approved" />
                <el-option label="采购中" value="purchasing" />
                <el-option label="已入库" value="received" />
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
          <el-col :span="8">
            <el-form-item label="付款方式" prop="paymentMethod">
              <el-select 
                v-model="form.paymentMethod" 
                placeholder="请选择付款方式"
                style="width: 100%"
              >
                <el-option label="银行转账" value="bank" />
                <el-option label="现金支付" value="cash" />
                <el-option label="支票" value="check" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 采购产品 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>采购产品</span>
            <el-button type="primary" size="small" @click="addProduct">
              <el-icon><Plus /></el-icon>
              添加产品
            </el-button>
          </div>
        </template>
        
        <div class="product-list">
          <div 
            v-for="(product, index) in form.products" 
            :key="index" 
            class="product-item"
          >
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item :label="`产品${index + 1}`" :prop="`products.${index}.name`">
                  <el-select 
                    v-model="product.name" 
                    placeholder="请选择产品"
                    filterable
                    style="width: 100%"
                    @change="(value) => handleProductChange(value, index)"
                  >
                    <el-option 
                      v-for="item in productOptions" 
                      :key="item.id"
                      :label="item.name"
                      :value="item.name"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="规格" :prop="`products.${index}.specifications`">
                  <el-input 
                    v-model="product.specifications" 
                    placeholder="规格型号"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-form-item label="数量" :prop="`products.${index}.quantity`">
                  <el-input-number 
                    v-model="product.quantity" 
                    :min="1"
                    :precision="0"
                    controls-position="right"
                    style="width: 100%"
                    @change="calculateProductAmount(index)"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-form-item label="单价" :prop="`products.${index}.price`">
                  <el-input-number 
                    v-model="product.price" 
                    :min="0"
                    :precision="2"
                    controls-position="right"
                    style="width: 100%"
                    @change="calculateProductAmount(index)"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-form-item label="金额">
                  <el-input-number 
                    v-model="product.amount" 
                    :precision="2"
                    controls-position="right"
                    style="width: 100%"
                    :disabled="true"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-form-item label="单位" :prop="`products.${index}.unit`">
                  <el-select 
                    v-model="product.unit" 
                    placeholder="单位"
                    style="width: 100%"
                  >
                    <el-option label="个" value="个" />
                    <el-option label="件" value="件" />
                    <el-option label="套" value="套" />
                    <el-option label="箱" value="箱" />
                    <el-option label="米" value="米" />
                    <el-option label="千克" value="千克" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="2">
                <el-form-item label="操作">
                  <el-button 
                    type="danger" 
                    size="small" 
                    @click="removeProduct(index)"
                    :disabled="form.products.length === 1"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 合计信息 -->
        <div class="total-info">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="total-item">
                <span class="label">产品总数：</span>
                <span class="value">{{ totalQuantity }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="total-item">
                <span class="label">采购总额：</span>
                <span class="value amount">¥{{ totalAmount.toLocaleString() }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="total-item">
                <span class="label">已付金额：</span>
                <span class="value">¥{{ paidAmount.toLocaleString() }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="total-item">
                <span class="label">未付金额：</span>
                <span class="value unpaid">¥{{ unpaidAmount.toLocaleString() }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>

      <!-- 其他信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>其他信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交货地址" prop="deliveryAddress">
              <el-input 
                v-model="form.deliveryAddress" 
                placeholder="请输入交货地址"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input 
                v-model="form.contactPerson" 
                placeholder="请输入联系人"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input 
                v-model="form.contactPhone" 
                placeholder="请输入联系电话"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="运输方式" prop="transportMethod">
              <el-select 
                v-model="form.transportMethod" 
                placeholder="请选择运输方式"
                style="width: 100%"
              >
                <el-option label="快递" value="express" />
                <el-option label="物流" value="logistics" />
                <el-option label="自提" value="self" />
                <el-option label="其他" value="other" />
              </el-select>
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
        <el-button v-if="!isEdit" type="success" @click="handleSubmitAndApprove">
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
  purchase: {
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
  purchaseNo: '',
  relatedOrderNo: '',
  purchaseDate: '',
  supplierName: '',
  purchaser: '',
  expectedDate: '',
  status: 'pending',
  paymentStatus: 'unpaid',
  paymentMethod: '',
  products: [
    {
      name: '',
      specifications: '',
      quantity: 1,
      price: 0,
      amount: 0,
      unit: '个'
    }
  ],
  deliveryAddress: '',
  contactPerson: '',
  contactPhone: '',
  transportMethod: '',
  remarks: ''
})

// 表单验证规则
const rules = {
  supplierName: [
    { required: true, message: '请选择供应商', trigger: 'change' }
  ],
  purchaser: [
    { required: true, message: '请选择采购员', trigger: 'change' }
  ],
  purchaseDate: [
    { required: true, message: '请选择采购日期', trigger: 'change' }
  ],
  expectedDate: [
    { required: true, message: '请选择预计到货日期', trigger: 'change' }
  ],
  'products.0.name': [
    { required: true, message: '请选择产品', trigger: 'change' }
  ],
  'products.0.quantity': [
    { required: true, message: '请输入数量', trigger: 'blur' },
    { type: 'number', min: 1, message: '数量必须大于0', trigger: 'blur' }
  ],
  'products.0.price': [
    { required: true, message: '请输入单价', trigger: 'blur' },
    { type: 'number', min: 0, message: '单价不能为负数', trigger: 'blur' }
  ]
}

// 模拟数据
const supplierList = ref([
  { id: 1, name: '深圳科技有限公司', contact: '张经理', phone: '0755-12345678' },
  { id: 2, name: '东莞电子制造厂', contact: '李经理', phone: '0769-87654321' },
  { id: 3, name: '广州配件供应商', contact: '王经理', phone: '020-11223344' },
  { id: 4, name: '上海精密仪器厂', contact: '陈经理', phone: '021-55667788' },
  { id: 5, name: '北京电子科技公司', contact: '刘经理', phone: '010-99887766' }
])

const orderList = ref([
  { orderNo: 'SO202412001', customerName: '美国ABC公司', amount: 50000 },
  { orderNo: 'SO202412002', customerName: '德国XYZ贸易', amount: 80000 },
  { orderNo: 'SO202412003', customerName: '法国DEF进口', amount: 120000 },
  { orderNo: 'SO202412004', customerName: '意大利GHI商贸', amount: 65000 },
  { orderNo: 'SO202412005', customerName: '西班牙JKL贸易', amount: 95000 }
])

const productOptions = ref([
  { id: 1, name: '智能蓝牙耳机', specifications: 'TWS无线 / 降噪', price: 200 },
  { id: 2, name: '无线充电器', specifications: '15W快充 / 兼容多设备', price: 80 },
  { id: 3, name: '便携式音箱', specifications: '防水 / 蓝牙5.0 / 12小时续航', price: 120 },
  { id: 4, name: '智能手环', specifications: '心率监测 / 运动追踪', price: 280 },
  { id: 5, name: 'USB数据线', specifications: 'Type-C / 3米 / 快充支持', price: 15 },
  { id: 6, name: '平板电脑保护套', specifications: '皮质 / 支架功能 / 多角度', price: 45 },
  { id: 7, name: '充电线', specifications: '快充 / 1米 / 多接口', price: 12 },
  { id: 8, name: '包装盒', specifications: '环保材质 / 定制印刷', price: 5 }
])

// 计算属性
const totalQuantity = computed(() => {
  return form.products.reduce((sum, product) => sum + (product.quantity || 0), 0)
})

const totalAmount = computed(() => {
  return form.products.reduce((sum, product) => sum + (product.amount || 0), 0)
})

const paidAmount = computed(() => {
  if (form.paymentStatus === 'paid') return totalAmount.value
  if (form.paymentStatus === 'partial') return totalAmount.value * 0.5
  return 0
})

const unpaidAmount = computed(() => {
  return totalAmount.value - paidAmount.value
})

// 初始化表单
const initForm = () => {
  if (props.purchase) {
    Object.keys(form).forEach(key => {
      if (props.purchase[key] !== undefined) {
        form[key] = props.purchase[key]
      }
    })
  } else {
    // 新增时的默认值
    form.status = 'pending'
    form.paymentStatus = 'unpaid'
    form.purchaseDate = new Date().toISOString().split('T')[0]
    form.products = [
      {
        name: '',
        specifications: '',
        quantity: 1,
        price: 0,
        amount: 0,
        unit: '个'
      }
    ]
  }
}

// 供应商选择变化
const handleSupplierChange = (supplierName) => {
  const supplier = supplierList.value.find(s => s.name === supplierName)
  if (supplier) {
    form.contactPerson = supplier.contact
    form.contactPhone = supplier.phone
  }
}

// 产品选择变化
const handleProductChange = (productName, index) => {
  const product = productOptions.value.find(p => p.name === productName)
  if (product) {
    form.products[index].specifications = product.specifications
    form.products[index].price = product.price
    calculateProductAmount(index)
  }
}

// 计算产品金额
const calculateProductAmount = (index) => {
  const product = form.products[index]
  product.amount = (product.quantity || 0) * (product.price || 0)
}

// 添加产品
const addProduct = () => {
  form.products.push({
    name: '',
    specifications: '',
    quantity: 1,
    price: 0,
    amount: 0,
    unit: '个'
  })
}

// 移除产品
const removeProduct = (index) => {
  if (form.products.length > 1) {
    form.products.splice(index, 1)
  }
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
    
    const hasEmptyProduct = form.products.some(product => !product.name || !product.quantity || !product.price)
    if (hasEmptyProduct) {
      ElMessage.warning('请完善所有产品信息')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(props.isEdit ? '采购单更新成功' : '采购单创建成功')
    emit('success')
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

// 保存并审核
const handleSubmitAndApprove = async () => {
  try {
    await formRef.value.validate()
    
    // 验证产品信息
    if (form.products.length === 0) {
      ElMessage.warning('请至少添加一个产品')
      return
    }
    
    const hasEmptyProduct = form.products.some(product => !product.name || !product.quantity || !product.price)
    if (hasEmptyProduct) {
      ElMessage.warning('请完善所有产品信息')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    form.status = 'approved'
    ElMessage.success('采购单已创建并审核通过')
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

// 删除采购单
const handleDelete = () => {
  ElMessageBox.confirm(
    `确定要删除采购单 "${form.purchaseNo}" 吗？此操作不可恢复！`,
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
      
      ElMessage.success('采购单删除成功')
      emit('success')
    } catch (error) {
      ElMessage.error('删除失败，请重试')
    } finally {
      submitting.value = false
    }
  })
}

// 监听采购数据变化
watch(() => props.purchase, () => {
  initForm()
}, { immediate: true })

onMounted(() => {
  initForm()
})
</script>

<style lang="scss" scoped>
.purchase-form {
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
  
  .product-list {
    .product-item {
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      padding: 15px;
      margin-bottom: 15px;
      background-color: #fafafa;
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
  
  .total-info {
    margin-top: 20px;
    padding: 15px;
    background-color: #f5f7fa;
    border-radius: 4px;
    
    .total-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .label {
        font-weight: 500;
        color: #606266;
      }
      
      .value {
        font-weight: bold;
        color: #303133;
        
        &.amount {
          color: #67C23A;
        }
        
        &.unpaid {
          color: #F56C6C;
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
}
</style> 