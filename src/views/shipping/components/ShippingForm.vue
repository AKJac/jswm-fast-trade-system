<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑出运' : '新建出运'"
    width="900px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="shipping-form"
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
            <el-form-item label="出运编号" prop="shippingNo">
              <el-input 
                v-model="form.shippingNo" 
                placeholder="系统自动生成"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-select 
                v-model="form.customerName" 
                placeholder="请选择客户"
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
            <el-form-item label="联系电话" prop="phone">
              <el-input 
                v-model="form.phone" 
                placeholder="请输入联系电话"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="运输方式" prop="transportMethod">
              <el-select 
                v-model="form.transportMethod" 
                placeholder="请选择运输方式"
                style="width: 100%"
              >
                <el-option label="海运" value="sea" />
                <el-option label="空运" value="air" />
                <el-option label="陆运" value="land" />
                <el-option label="快递" value="express" />
                <el-option label="铁路" value="railway" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出运状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择状态"
                style="width: 100%"
              >
                <el-option label="待出运" value="pending" />
                <el-option label="已出运" value="shipped" />
                <el-option label="运输中" value="transit" />
                <el-option label="已到达" value="arrived" />
                <el-option label="已完成" value="completed" />
                <el-option label="已取消" value="cancelled" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 时间安排 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>时间安排</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出运日期" prop="shippingDate">
              <el-date-picker
                v-model="form.shippingDate"
                type="date"
                placeholder="请选择出运日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计到达" prop="expectedArrival">
              <el-date-picker
                v-model="form.expectedArrival"
                type="date"
                placeholder="请选择预计到达日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="实际到达" prop="actualArrival">
              <el-date-picker
                v-model="form.actualArrival"
                type="date"
                placeholder="请选择实际到达日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="完成日期" prop="completionDate">
              <el-date-picker
                v-model="form.completionDate"
                type="date"
                placeholder="请选择完成日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 货物信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>货物信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="货物名称" prop="cargoName">
              <el-input 
                v-model="form.cargoName" 
                placeholder="请输入货物名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="货物数量" prop="cargoQuantity">
              <el-input-number
                v-model="form.cargoQuantity"
                :min="1"
                placeholder="请输入货物数量"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="包装方式" prop="packaging">
              <el-select 
                v-model="form.packaging" 
                placeholder="请选择包装方式"
                style="width: 100%"
              >
                <el-option label="纸箱" value="carton" />
                <el-option label="木箱" value="wooden_box" />
                <el-option label="托盘" value="pallet" />
                <el-option label="集装箱" value="container" />
                <el-option label="散装" value="bulk" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重量(kg)" prop="weight">
              <el-input-number
                v-model="form.weight"
                :min="0"
                :precision="2"
                placeholder="请输入重量"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体积(m³)" prop="volume">
              <el-input-number
                v-model="form.volume"
                :min="0"
                :precision="2"
                placeholder="请输入体积"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="集装箱号" prop="containerNo">
              <el-input 
                v-model="form.containerNo" 
                placeholder="请输入集装箱号"
              />
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
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发货地址" prop="shippingAddress">
              <el-input 
                v-model="form.shippingAddress" 
                placeholder="请输入发货地址"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收货地址" prop="deliveryAddress">
              <el-input 
                v-model="form.deliveryAddress" 
                placeholder="请输入收货地址"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发货港口" prop="shippingPort">
              <el-input 
                v-model="form.shippingPort" 
                placeholder="请输入发货港口"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目的港口" prop="destinationPort">
              <el-input 
                v-model="form.destinationPort" 
                placeholder="请输入目的港口"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 费用信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>费用信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="运费" prop="freightCost">
              <el-input-number
                v-model="form.freightCost"
                :min="0"
                :precision="2"
                placeholder="请输入运费"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保险费" prop="insuranceCost">
              <el-input-number
                v-model="form.insuranceCost"
                :min="0"
                :precision="2"
                placeholder="请输入保险费"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="关税" prop="customsDuty">
              <el-input-number
                v-model="form.customsDuty"
                :min="0"
                :precision="2"
                placeholder="请输入关税"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其他费用" prop="otherCosts">
              <el-input-number
                v-model="form.otherCosts"
                :min="0"
                :precision="2"
                placeholder="请输入其他费用"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="总费用">
              <el-input 
                :value="totalCost" 
                disabled
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="货币" prop="currency">
              <el-select 
                v-model="form.currency" 
                placeholder="请选择货币"
                style="width: 100%"
              >
                <el-option label="人民币" value="CNY" />
                <el-option label="美元" value="USD" />
                <el-option label="欧元" value="EUR" />
                <el-option label="日元" value="JPY" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 备注信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>备注信息</span>
          </div>
        </template>
        
        <el-form-item label="特殊要求" prop="specialRequirements">
          <el-input
            v-model="form.specialRequirements"
            type="textarea"
            :rows="3"
            placeholder="请输入特殊要求"
          />
        </el-form-item>

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
  shipping: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const isEdit = computed(() => !!props.shipping)

// 表单数据
const form = reactive({
  shippingNo: '',
  customerName: '',
  contact: '',
  phone: '',
  transportMethod: '',
  status: 'pending',
  shippingDate: '',
  expectedArrival: '',
  actualArrival: '',
  completionDate: '',
  cargoName: '',
  cargoQuantity: 1,
  packaging: '',
  weight: 0,
  volume: 0,
  containerNo: '',
  shippingAddress: '',
  deliveryAddress: '',
  shippingPort: '',
  destinationPort: '',
  freightCost: 0,
  insuranceCost: 0,
  customsDuty: 0,
  otherCosts: 0,
  currency: 'USD',
  specialRequirements: '',
  remark: ''
})

// 表单验证规则
const rules = {
  customerName: [
    { required: true, message: '请选择客户名称', trigger: 'change' }
  ],
  contact: [
    { required: true, message: '请输入联系人', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' }
  ],
  transportMethod: [
    { required: true, message: '请选择运输方式', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择出运状态', trigger: 'change' }
  ],
  shippingDate: [
    { required: true, message: '请选择出运日期', trigger: 'change' }
  ],
  cargoName: [
    { required: true, message: '请输入货物名称', trigger: 'blur' }
  ],
  cargoQuantity: [
    { required: true, message: '请输入货物数量', trigger: 'blur' }
  ],
  weight: [
    { required: true, message: '请输入重量', trigger: 'blur' }
  ]
}

// 模拟数据
const customerList = ref([
  { id: 1, customerName: 'ABC公司', contact: 'John Smith', phone: '+1-555-0123' },
  { id: 2, customerName: 'XYZ贸易', contact: 'Mary Johnson', phone: '+1-555-0456' },
  { id: 3, customerName: 'DEF集团', contact: 'David Brown', phone: '+1-555-0789' }
])

// 计算总费用
const totalCost = computed(() => {
  return (form.freightCost + form.insuranceCost + form.customsDuty + form.otherCosts).toFixed(2)
})

// 生成出运编号
const generateShippingNo = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0')
  return `S${year}${month}${day}${random}`
}

// 客户选择变化处理
const handleCustomerChange = (customerName) => {
  const customer = customerList.value.find(c => c.customerName === customerName)
  if (customer) {
    form.contact = customer.contact
    form.phone = customer.phone
  }
}

// 初始化表单
const initForm = () => {
  if (props.shipping) {
    // 编辑模式
    Object.assign(form, props.shipping)
  } else {
    // 新增模式
    Object.assign(form, {
      shippingNo: generateShippingNo(),
      customerName: '',
      contact: '',
      phone: '',
      transportMethod: '',
      status: 'pending',
      shippingDate: new Date(),
      expectedArrival: '',
      actualArrival: '',
      completionDate: '',
      cargoName: '',
      cargoQuantity: 1,
      packaging: '',
      weight: 0,
      volume: 0,
      containerNo: '',
      shippingAddress: '',
      deliveryAddress: '',
      shippingPort: '',
      destinationPort: '',
      freightCost: 0,
      insuranceCost: 0,
      customsDuty: 0,
      otherCosts: 0,
      currency: 'USD',
      specialRequirements: '',
      remark: ''
    })
  }
}

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false)
}

// 删除出运
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个出运记录吗？删除后无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟删除操作
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('出运记录删除成功')
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
    
    ElMessage.success(isEdit.value ? '出运记录更新成功' : '出运记录创建成功')
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
.shipping-form {
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