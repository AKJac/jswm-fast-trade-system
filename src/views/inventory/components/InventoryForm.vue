<template>
  <div class="inventory-form">
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
            <el-form-item label="产品货号" prop="productCode">
              <el-input 
                v-model="form.productCode" 
                placeholder="请输入产品货号"
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品名称" prop="productName">
              <el-select 
                v-model="form.productName" 
                placeholder="请选择产品"
                filterable
                style="width: 100%"
                @change="handleProductChange"
              >
                <el-option 
                  v-for="product in productList" 
                  :key="product.id"
                  :label="product.productName"
                  :value="product.productName"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="规格型号" prop="specifications">
              <el-input 
                v-model="form.specifications" 
                placeholder="请输入规格型号"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="仓库" prop="warehouse">
              <el-select 
                v-model="form.warehouse" 
                placeholder="请选择仓库"
                style="width: 100%"
              >
                <el-option label="主仓库" value="main" />
                <el-option label="次仓库" value="secondary" />
                <el-option label="临时仓库" value="temporary" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="计量单位" prop="unit">
              <el-select 
                v-model="form.unit" 
                placeholder="请选择单位"
                style="width: 100%"
              >
                <el-option label="个" value="个" />
                <el-option label="件" value="件" />
                <el-option label="套" value="套" />
                <el-option label="箱" value="箱" />
                <el-option label="米" value="米" />
                <el-option label="千克" value="千克" />
                <el-option label="升" value="升" />
                <el-option label="根" value="根" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="库存状态" prop="stockStatus">
              <el-select 
                v-model="form.stockStatus" 
                placeholder="请选择状态"
                style="width: 100%"
              >
                <el-option label="充足" value="sufficient" />
                <el-option label="低库存" value="low" />
                <el-option label="缺货" value="outofstock" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 库存数量 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>库存数量</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="当前库存" prop="stockQuantity">
              <el-input-number 
                v-model="form.stockQuantity" 
                :precision="0"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="当前库存"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="可用数量" prop="availableQuantity">
              <el-input-number 
                v-model="form.availableQuantity" 
                :precision="0"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="可用数量"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="锁定数量" prop="lockedQuantity">
              <el-input-number 
                v-model="form.lockedQuantity" 
                :precision="0"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="锁定数量"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="最低库存" prop="minStock">
              <el-input-number 
                v-model="form.minStock" 
                :precision="0"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="最低库存"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="采购价格" prop="purchasePrice">
              <el-input-number 
                v-model="form.purchasePrice" 
                :precision="2"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="采购价格"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="库存价值" prop="stockValue">
              <el-input-number 
                v-model="form.stockValue" 
                :precision="2"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="库存价值"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="安全库存" prop="safeStock">
              <el-input-number 
                v-model="form.safeStock" 
                :precision="0"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="安全库存"
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
                style="width: 100%"
              >
                <el-option label="ABC制造有限公司" value="ABC Manufacturing" />
                <el-option label="XYZ贸易公司" value="XYZ Trading" />
                <el-option label="DEF代理商" value="DEF Agent" />
                <el-option label="GHI工业集团" value="GHI Industrial" />
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
                <el-option label="超期未付" value="overdue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="付款日期" prop="paymentDate">
              <el-date-picker
                v-model="form.paymentDate"
                type="date"
                placeholder="选择付款日期"
                style="width: 100%"
                :disabled="form.paymentStatus === 'unpaid'"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 库存位置 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>库存位置</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="库位编号" prop="locationCode">
              <el-input 
                v-model="form.locationCode" 
                placeholder="请输入库位编号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="货架号" prop="shelfNumber">
              <el-input 
                v-model="form.shelfNumber" 
                placeholder="请输入货架号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="区域" prop="area">
              <el-input 
                v-model="form.area" 
                placeholder="请输入区域"
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
        <el-button v-if="!isEdit" type="success" @click="handleStockIn">
          <el-icon><Plus /></el-icon>
          保存并入库
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  inventory: {
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
  productCode: '',
  productName: '',
  specifications: '',
  warehouse: '',
  unit: '',
  stockStatus: 'sufficient',
  stockQuantity: 0,
  availableQuantity: 0,
  lockedQuantity: 0,
  minStock: 0,
  purchasePrice: 0,
  stockValue: 0,
  safeStock: 0,
  supplierName: '',
  paymentStatus: 'unpaid',
  paymentDate: null,
  locationCode: '',
  shelfNumber: '',
  area: '',
  remarks: ''
})

// 表单验证规则
const rules = {
  productCode: [
    { required: true, message: '请输入产品货号', trigger: 'blur' }
  ],
  productName: [
    { required: true, message: '请选择产品名称', trigger: 'change' }
  ],
  warehouse: [
    { required: true, message: '请选择仓库', trigger: 'change' }
  ],
  unit: [
    { required: true, message: '请选择计量单位', trigger: 'change' }
  ],
  stockQuantity: [
    { required: true, message: '请输入当前库存', trigger: 'blur' },
    { type: 'number', min: 0, message: '库存不能为负数', trigger: 'blur' }
  ],
  availableQuantity: [
    { required: true, message: '请输入可用数量', trigger: 'blur' },
    { type: 'number', min: 0, message: '可用数量不能为负数', trigger: 'blur' }
  ],
  minStock: [
    { required: true, message: '请输入最低库存', trigger: 'blur' },
    { type: 'number', min: 0, message: '最低库存不能为负数', trigger: 'blur' }
  ],
  purchasePrice: [
    { required: true, message: '请输入采购价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '采购价格不能为负数', trigger: 'blur' }
  ]
}

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
const calculatedStockValue = computed(() => {
  return form.stockQuantity * form.purchasePrice
})

// 初始化表单
const initForm = () => {
  if (props.inventory) {
    Object.keys(form).forEach(key => {
      if (props.inventory[key] !== undefined) {
        form[key] = props.inventory[key]
      }
    })
  } else {
    // 新增时的默认值
    form.stockStatus = 'sufficient'
    form.stockQuantity = 0
    form.availableQuantity = 0
    form.lockedQuantity = 0
    form.minStock = 0
    form.purchasePrice = 0
    form.stockValue = 0
    form.safeStock = 0
  }
}

// 产品选择变化
const handleProductChange = (productName) => {
  const product = productList.value.find(p => p.productName === productName)
  if (product) {
    form.specifications = product.specifications
    form.purchasePrice = product.price
    calculateStockValue()
  }
}

// 计算库存价值
const calculateStockValue = () => {
  form.stockValue = calculatedStockValue.value
}

// 监听库存数量变化
watch(() => form.stockQuantity, () => {
  calculateStockValue()
})

watch(() => form.purchasePrice, () => {
  calculateStockValue()
})

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    // 验证库存逻辑
    if (form.availableQuantity > form.stockQuantity) {
      ElMessage.warning('可用数量不能大于当前库存')
      return
    }
    
    if (form.lockedQuantity > form.stockQuantity) {
      ElMessage.warning('锁定数量不能大于当前库存')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(props.isEdit ? '库存信息更新成功' : '库存信息添加成功')
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

// 删除库存
const handleDelete = () => {
  ElMessageBox.confirm(
    `确定要删除库存记录 "${form.productName}" 吗？此操作不可恢复！`,
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
      
      ElMessage.success('库存记录删除成功')
      emit('success')
    } catch (error) {
      ElMessage.error('删除失败，请重试')
    } finally {
      submitting.value = false
    }
  })
}

// 保存并入库
const handleStockIn = async () => {
  try {
    await formRef.value.validate()
    
    if (form.availableQuantity > form.stockQuantity) {
      ElMessage.warning('可用数量不能大于当前库存')
      return
    }
    
    if (form.lockedQuantity > form.stockQuantity) {
      ElMessage.warning('锁定数量不能大于当前库存')
      return
    }
    
    submitting.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('库存信息已保存并入库')
    emit('success')
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

// 监听库存数据变化
watch(() => props.inventory, () => {
  initForm()
}, { immediate: true })

onMounted(() => {
  initForm()
})
</script>

<style lang="scss" scoped>
.inventory-form {
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