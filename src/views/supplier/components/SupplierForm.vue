<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑供应商' : '新建供应商'"
    width="900px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="supplier-form"
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
            <el-form-item label="供应商编号" prop="supplierNo">
              <el-input 
                v-model="form.supplierNo" 
                placeholder="系统自动生成"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="supplierName">
              <el-input 
                v-model="form.supplierName" 
                placeholder="请输入供应商名称"
                @blur="handleSupplierNameBlur"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司类型" prop="companyType">
              <el-select 
                v-model="form.companyType" 
                placeholder="请选择公司类型"
                style="width: 100%"
              >
                <el-option label="生产商" value="manufacturer" />
                <el-option label="贸易商" value="trader" />
                <el-option label="代理商" value="agent" />
                <el-option label="服务商" value="service" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合作状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择合作状态"
                style="width: 100%"
              >
                <el-option label="合作中" value="active" />
                <el-option label="暂停合作" value="paused" />
                <el-option label="终止合作" value="terminated" />
                <el-option label="待评估" value="evaluating" />
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
            <el-form-item label="联系电话" prop="phone">
              <el-input 
                v-model="form.phone" 
                placeholder="请输入联系电话"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input 
                v-model="form.email" 
                placeholder="请输入邮箱"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="传真" prop="fax">
              <el-input 
                v-model="form.fax" 
                placeholder="请输入传真号码"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网站" prop="website">
              <el-input 
                v-model="form.website" 
                placeholder="请输入网站地址"
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
            <el-form-item label="省份/州" prop="province">
              <el-input 
                v-model="form.province" 
                placeholder="请输入省份/州"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-input 
                v-model="form.city" 
                placeholder="请输入城市"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮编" prop="postcode">
              <el-input 
                v-model="form.postcode" 
                placeholder="请输入邮编"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="详细地址" prop="address">
          <el-input
            v-model="form.address"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
          />
        </el-form-item>
      </el-card>

      <!-- 业务信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>业务信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主要产品" prop="mainProducts">
              <el-input 
                v-model="form.mainProducts" 
                placeholder="请输入主要产品"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年营业额" prop="annualRevenue">
              <el-input-number
                v-model="form.annualRevenue"
                :min="0"
                :precision="2"
                placeholder="请输入年营业额"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="员工数量" prop="employeeCount">
              <el-input-number
                v-model="form.employeeCount"
                :min="0"
                placeholder="请输入员工数量"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成立时间" prop="establishDate">
              <el-date-picker
                v-model="form.establishDate"
                type="date"
                placeholder="请选择成立时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="信用等级" prop="creditRating">
              <el-select 
                v-model="form.creditRating" 
                placeholder="请选择信用等级"
                style="width: 100%"
              >
                <el-option label="AAA" value="AAA" />
                <el-option label="AA" value="AA" />
                <el-option label="A" value="A" />
                <el-option label="BBB" value="BBB" />
                <el-option label="BB" value="BB" />
                <el-option label="B" value="B" />
                <el-option label="CCC" value="CCC" />
                <el-option label="CC" value="CC" />
                <el-option label="C" value="C" />
                <el-option label="D" value="D" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款条件" prop="paymentTerms">
              <el-select 
                v-model="form.paymentTerms" 
                placeholder="请选择付款条件"
                style="width: 100%"
              >
                <el-option label="预付款" value="advance" />
                <el-option label="货到付款" value="cod" />
                <el-option label="30天付款" value="30days" />
                <el-option label="60天付款" value="60days" />
                <el-option label="90天付款" value="90days" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合作开始时间" prop="cooperationStartDate">
              <el-date-picker
                v-model="form.cooperationStartDate"
                type="date"
                placeholder="请选择合作开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同到期时间" prop="contractEndDate">
              <el-date-picker
                v-model="form.contractEndDate"
                type="date"
                placeholder="请选择合同到期时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 对接信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>对接信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="对接报价单" prop="quotationIntegration">
              <el-switch
                v-model="form.quotationIntegration"
                active-text="已对接"
                inactive-text="未对接"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对接销售单" prop="salesOrderIntegration">
              <el-switch
                v-model="form.salesOrderIntegration"
                active-text="已对接"
                inactive-text="未对接"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="对接采购单" prop="purchaseOrderIntegration">
              <el-switch
                v-model="form.purchaseOrderIntegration"
                active-text="已对接"
                inactive-text="未对接"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对接库存" prop="inventoryIntegration">
              <el-switch
                v-model="form.inventoryIntegration"
                active-text="已对接"
                inactive-text="未对接"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="API接口地址" prop="apiUrl">
          <el-input 
            v-model="form.apiUrl" 
            placeholder="请输入API接口地址"
          />
        </el-form-item>

        <el-form-item label="对接备注" prop="integrationRemark">
          <el-input
            v-model="form.integrationRemark"
            type="textarea"
            :rows="3"
            placeholder="请输入对接相关备注"
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
  supplier: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const isEdit = computed(() => !!props.supplier)

// 表单数据
const form = reactive({
  supplierNo: '',
  supplierName: '',
  companyType: '',
  status: 'evaluating',
  contact: '',
  position: '',
  phone: '',
  email: '',
  fax: '',
  website: '',
  country: '',
  province: '',
  city: '',
  postcode: '',
  address: '',
  mainProducts: '',
  annualRevenue: 0,
  employeeCount: 0,
  establishDate: '',
  creditRating: '',
  paymentTerms: '',
  cooperationStartDate: '',
  contractEndDate: '',
  quotationIntegration: false,
  salesOrderIntegration: false,
  purchaseOrderIntegration: false,
  inventoryIntegration: false,
  apiUrl: '',
  integrationRemark: '',
  remark: ''
})

// 表单验证规则
const rules = {
  supplierName: [
    { required: true, message: '请输入供应商名称', trigger: 'blur' }
  ],
  companyType: [
    { required: true, message: '请选择公司类型', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择合作状态', trigger: 'change' }
  ],
  contact: [
    { required: true, message: '请输入联系人', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  country: [
    { required: true, message: '请选择国家/地区', trigger: 'change' }
  ]
}

// 模拟供应商数据用于查重
const existingSuppliers = ref([
  { id: 1, supplierName: 'ABC制造有限公司', companyType: 'manufacturer' },
  { id: 2, supplierName: 'XYZ贸易公司', companyType: 'trader' },
  { id: 3, supplierName: 'DEF代理商', companyType: 'agent' }
])

// 生成供应商编号
const generateSupplierNo = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0')
  return `S${year}${month}${day}${random}`
}

// 供应商名称查重
const handleSupplierNameBlur = () => {
  if (!form.supplierName) return
  
  const duplicate = existingSuppliers.value.find(supplier => 
    supplier.supplierName.toLowerCase() === form.supplierName.toLowerCase() &&
    supplier.id !== (props.supplier?.id || 0)
  )
  
  if (duplicate) {
    ElMessage.warning('该供应商名称已存在，请检查是否重复')
  }
}

// 初始化表单
const initForm = () => {
  if (props.supplier) {
    // 编辑模式
    Object.assign(form, props.supplier)
  } else {
    // 新增模式
    Object.assign(form, {
      supplierNo: generateSupplierNo(),
      supplierName: '',
      companyType: '',
      status: 'evaluating',
      contact: '',
      position: '',
      phone: '',
      email: '',
      fax: '',
      website: '',
      country: '',
      province: '',
      city: '',
      postcode: '',
      address: '',
      mainProducts: '',
      annualRevenue: 0,
      employeeCount: 0,
      establishDate: '',
      creditRating: '',
      paymentTerms: '',
      cooperationStartDate: '',
      contractEndDate: '',
      quotationIntegration: false,
      salesOrderIntegration: false,
      purchaseOrderIntegration: false,
      inventoryIntegration: false,
      apiUrl: '',
      integrationRemark: '',
      remark: ''
    })
  }
}

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false)
}

// 删除供应商
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个供应商吗？删除后无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟删除操作
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('供应商删除成功')
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
    
    ElMessage.success(isEdit.value ? '供应商更新成功' : '供应商创建成功')
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
.supplier-form {
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