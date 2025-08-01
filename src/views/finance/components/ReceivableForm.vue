<template>
  <div class="receivable-form">
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
            <el-form-item label="应收款编号" prop="receivableNo">
              <el-input 
                v-model="form.receivableNo" 
                placeholder="系统自动生成"
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="关联订单" prop="orderNo">
              <el-select 
                v-model="form.orderNo" 
                placeholder="请选择关联订单"
                filterable
                style="width: 100%"
                @change="handleOrderChange"
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
            <el-form-item label="客户名称" prop="customerName">
              <el-input 
                v-model="form.customerName" 
                placeholder="请输入客户名称"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="应收金额" prop="totalAmount">
              <el-input-number 
                v-model="form.totalAmount" 
                :precision="2"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="应收金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="已收金额" prop="receivedAmount">
              <el-input-number 
                v-model="form.receivedAmount" 
                :precision="2"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="已收金额"
                @change="calculatePendingAmount"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="待收金额" prop="pendingAmount">
              <el-input-number 
                v-model="form.pendingAmount" 
                :precision="2"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="待收金额"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="应收日期" prop="receivableDate">
              <el-date-picker
                v-model="form.receivableDate"
                type="date"
                placeholder="选择应收日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="到期日期" prop="dueDate">
              <el-date-picker
                v-model="form.dueDate"
                type="date"
                placeholder="选择到期日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="收款状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择状态"
                style="width: 100%"
              >
                <el-option label="待收款" value="pending" />
                <el-option label="部分收款" value="partial" />
                <el-option label="已收款" value="received" />
                <el-option label="逾期" value="overdue" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 收款记录 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>收款记录</span>
            <el-button type="primary" size="small" @click="addPaymentRecord">
              <el-icon><Plus /></el-icon>
              添加收款记录
            </el-button>
          </div>
        </template>
        
        <div class="payment-records">
          <div 
            v-for="(record, index) in form.paymentRecords" 
            :key="index" 
            class="payment-record"
          >
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item :label="`收款日期${index + 1}`" :prop="`paymentRecords.${index}.paymentDate`">
                  <el-date-picker
                    v-model="record.paymentDate"
                    type="date"
                    placeholder="选择收款日期"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="收款金额" :prop="`paymentRecords.${index}.amount`">
                  <el-input-number 
                    v-model="record.amount" 
                    :precision="2"
                    :min="0"
                    controls-position="right"
                    style="width: 100%"
                    placeholder="收款金额"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item label="收款方式" :prop="`paymentRecords.${index}.paymentMethod`">
                  <el-select 
                    v-model="record.paymentMethod" 
                    placeholder="收款方式"
                    style="width: 100%"
                  >
                    <el-option label="银行转账" value="bank" />
                    <el-option label="现金" value="cash" />
                    <el-option label="支票" value="check" />
                    <el-option label="支付宝" value="alipay" />
                    <el-option label="微信" value="wechat" />
                    <el-option label="其他" value="other" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="收款账户" :prop="`paymentRecords.${index}.account`">
                  <el-input 
                    v-model="record.account" 
                    placeholder="收款账户"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-form-item label="操作">
                  <el-button 
                    type="danger" 
                    size="small" 
                    @click="removePaymentRecord(index)"
                    :disabled="form.paymentRecords.length === 1"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 收款统计 -->
        <div class="payment-summary">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="summary-item">
                <span class="label">应收总额：</span>
                <span class="value">¥{{ form.totalAmount.toLocaleString() }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="summary-item">
                <span class="label">已收总额：</span>
                <span class="value received">¥{{ totalReceivedAmount.toLocaleString() }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="summary-item">
                <span class="label">待收金额：</span>
                <span class="value pending">¥{{ form.pendingAmount.toLocaleString() }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="summary-item">
                <span class="label">收款进度：</span>
                <span class="value">{{ paymentProgress }}%</span>
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
            <el-form-item label="联系人" prop="contactPerson">
              <el-input 
                v-model="form.contactPerson" 
                placeholder="请输入联系人"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input 
                v-model="form.contactPhone" 
                placeholder="请输入联系电话"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="付款条件" prop="paymentTerms">
              <el-select 
                v-model="form.paymentTerms" 
                placeholder="请选择付款条件"
                style="width: 100%"
              >
                <el-option label="货到付款" value="cod" />
                <el-option label="30天付款" value="30days" />
                <el-option label="60天付款" value="60days" />
                <el-option label="90天付款" value="90days" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="催收状态" prop="collectionStatus">
              <el-select 
                v-model="form.collectionStatus" 
                placeholder="请选择催收状态"
                style="width: 100%"
              >
                <el-option label="未催收" value="none" />
                <el-option label="已提醒" value="reminded" />
                <el-option label="已催收" value="collected" />
                <el-option label="已逾期" value="overdue" />
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
        <el-button v-if="!isEdit" type="success" @click="handleSubmitAndReceive">
          <el-icon><Money /></el-icon>
          保存并收款
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  receivable: {
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
  receivableNo: '',
  orderNo: '',
  customerName: '',
  totalAmount: 0,
  receivedAmount: 0,
  pendingAmount: 0,
  receivableDate: '',
  dueDate: '',
  status: 'pending',
  paymentRecords: [
    {
      paymentDate: '',
      amount: 0,
      paymentMethod: '',
      account: ''
    }
  ],
  contactPerson: '',
  contactPhone: '',
  paymentTerms: '',
  collectionStatus: 'none',
  remarks: ''
})

// 表单验证规则
const rules = {
  orderNo: [
    { required: true, message: '请选择关联订单', trigger: 'change' }
  ],
  customerName: [
    { required: true, message: '请输入客户名称', trigger: 'blur' }
  ],
  totalAmount: [
    { required: true, message: '请输入应收金额', trigger: 'blur' },
    { type: 'number', min: 0, message: '应收金额不能为负数', trigger: 'blur' }
  ],
  receivableDate: [
    { required: true, message: '请选择应收日期', trigger: 'change' }
  ],
  dueDate: [
    { required: true, message: '请选择到期日期', trigger: 'change' }
  ],
  'paymentRecords.0.paymentDate': [
    { required: true, message: '请选择收款日期', trigger: 'change' }
  ],
  'paymentRecords.0.amount': [
    { required: true, message: '请输入收款金额', trigger: 'blur' },
    { type: 'number', min: 0, message: '收款金额不能为负数', trigger: 'blur' }
  ]
}

// 模拟数据
const orderList = ref([
  { orderNo: 'SO202412001', customerName: 'ABC Trading Co.', amount: 164000 },
  { orderNo: 'SO202412002', customerName: 'XYZ Import Ltd', amount: 137500 },
  { orderNo: 'SO202412003', customerName: 'Global Solutions', amount: 42500 },
  { orderNo: 'SO202412004', customerName: 'European Trade', amount: 89000 },
  { orderNo: 'SO202412005', customerName: 'Asian Market Co.', amount: 156000 }
])

// 计算属性
const totalReceivedAmount = computed(() => {
  return form.paymentRecords.reduce((sum, record) => sum + (record.amount || 0), 0)
})

const paymentProgress = computed(() => {
  if (form.totalAmount === 0) return 0
  return Math.round((totalReceivedAmount.value / form.totalAmount) * 100)
})

// 初始化表单
const initForm = () => {
  if (props.receivable) {
    Object.keys(form).forEach(key => {
      if (props.receivable[key] !== undefined) {
        form[key] = props.receivable[key]
      }
    })
  } else {
    // 新增时的默认值
    form.status = 'pending'
    form.collectionStatus = 'none'
    form.receivableDate = new Date().toISOString().split('T')[0]
    form.paymentRecords = [
      {
        paymentDate: '',
        amount: 0,
        paymentMethod: '',
        account: ''
      }
    ]
  }
}

// 订单选择变化
const handleOrderChange = (orderNo) => {
  const order = orderList.value.find(o => o.orderNo === orderNo)
  if (order) {
    form.customerName = order.customerName
    form.totalAmount = order.amount
    calculatePendingAmount()
  }
}

// 计算待收金额
const calculatePendingAmount = () => {
  form.pendingAmount = form.totalAmount - totalReceivedAmount.value
}

// 添加收款记录
const addPaymentRecord = () => {
  form.paymentRecords.push({
    paymentDate: '',
    amount: 0,
    paymentMethod: '',
    account: ''
  })
}

// 移除收款记录
const removePaymentRecord = (index) => {
  if (form.paymentRecords.length > 1) {
    form.paymentRecords.splice(index, 1)
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 验证收款记录
    if (form.paymentRecords.length === 0) {
      ElMessage.warning('请至少添加一条收款记录')
      return
    }
    
    const hasEmptyRecord = form.paymentRecords.some(record => 
      !record.paymentDate || !record.amount || !record.paymentMethod
    )
    if (hasEmptyRecord) {
      ElMessage.warning('请完善所有收款记录信息')
      return
    }
    
    // 验证收款金额
    if (totalReceivedAmount.value > form.totalAmount) {
      ElMessage.warning('已收金额不能大于应收金额')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(props.isEdit ? '应收款信息更新成功' : '应收款信息添加成功')
    emit('success')
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

// 保存并收款
const handleSubmitAndReceive = async () => {
  try {
    await formRef.value.validate()
    
    // 验证收款记录
    if (form.paymentRecords.length === 0) {
      ElMessage.warning('请至少添加一条收款记录')
      return
    }
    
    const hasEmptyRecord = form.paymentRecords.some(record => 
      !record.paymentDate || !record.amount || !record.paymentMethod
    )
    if (hasEmptyRecord) {
      ElMessage.warning('请完善所有收款记录信息')
      return
    }
    
    // 验证收款金额
    if (totalReceivedAmount.value > form.totalAmount) {
      ElMessage.warning('已收金额不能大于应收金额')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    if (totalReceivedAmount.value >= form.totalAmount) {
      form.status = 'received'
    } else {
      form.status = 'partial'
    }
    
    ElMessage.success('应收款信息已保存并完成收款')
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

// 删除应收款
const handleDelete = () => {
  ElMessageBox.confirm(
    `确定要删除应收款记录 "${form.receivableNo}" 吗？此操作不可恢复！`,
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
      
      ElMessage.success('应收款记录删除成功')
      emit('success')
    } catch (error) {
      ElMessage.error('删除失败，请重试')
    } finally {
      submitting.value = false
    }
  })
}

// 监听应收款数据变化
watch(() => props.receivable, () => {
  initForm()
}, { immediate: true })

// 监听收款记录变化
watch(() => totalReceivedAmount.value, () => {
  calculatePendingAmount()
})

onMounted(() => {
  initForm()
})
</script>

<style lang="scss" scoped>
.receivable-form {
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
  
  .payment-records {
    .payment-record {
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
  
  .payment-summary {
    margin-top: 20px;
    padding: 15px;
    background-color: #f5f7fa;
    border-radius: 4px;
    
    .summary-item {
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
        
        &.received {
          color: #67C23A;
        }
        
        &.pending {
          color: #E6A23C;
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