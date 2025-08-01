<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑产品' : '新建产品'"
    width="900px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="product-form"
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
            <el-form-item label="货号" prop="productCode">
              <el-input 
                v-model="form.productCode" 
                placeholder="系统自动生成"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品分类" prop="category">
              <el-select 
                v-model="form.category" 
                placeholder="请选择产品分类"
                style="width: 100%"
              >
                <el-option label="刀具类" value="knife" />
                <el-option label="工具类" value="tool" />
                <el-option label="配件类" value="accessory" />
                <el-option label="包装类" value="packaging" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="产品名称(中文)" prop="productName">
              <el-input 
                v-model="form.productName" 
                placeholder="请输入产品中文名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品名称(英文)" prop="productNameEn">
              <el-input 
                v-model="form.productNameEn" 
                placeholder="请输入产品英文名称"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-select 
                v-model="form.unit" 
                placeholder="请选择单位"
                style="width: 100%"
              >
                <el-option label="个" value="piece" />
                <el-option label="套" value="set" />
                <el-option label="盒" value="box" />
                <el-option label="箱" value="carton" />
                <el-option label="米" value="meter" />
                <el-option label="千克" value="kg" />
                <el-option label="吨" value="ton" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择产品状态"
                style="width: 100%"
              >
                <el-option label="订单产品" value="order" />
                <el-option label="散货产品" value="bulk" />
                <el-option label="停产" value="discontinued" />
                <el-option label="开发中" value="developing" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-input 
                v-model="form.brand" 
                placeholder="请输入品牌"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商货号" prop="supplierItemNo">
              <el-input 
                v-model="form.supplierItemNo" 
                placeholder="请输入供应商货号"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 规格参数 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>规格参数</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="刃色" prop="bladeColor">
              <el-select 
                v-model="form.bladeColor" 
                placeholder="请选择刃色"
                style="width: 100%"
              >
                <el-option label="银色" value="silver" />
                <el-option label="黑色" value="black" />
                <el-option label="金色" value="gold" />
                <el-option label="蓝色" value="blue" />
                <el-option label="红色" value="red" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格" prop="specification">
              <el-input 
                v-model="form.specification" 
                placeholder="请输入规格"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="柄色" prop="handleColor">
              <el-select 
                v-model="form.handleColor" 
                placeholder="请选择柄色"
                style="width: 100%"
              >
                <el-option label="黑色" value="black" />
                <el-option label="棕色" value="brown" />
                <el-option label="红色" value="red" />
                <el-option label="蓝色" value="blue" />
                <el-option label="绿色" value="green" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="自定义" prop="custom">
              <el-input 
                v-model="form.custom" 
                placeholder="请输入自定义参数"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="尺寸" prop="dimensions">
              <el-input 
                v-model="form.dimensions" 
                placeholder="请输入尺寸"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重量" prop="weight">
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
            <el-form-item label="材质" prop="material">
              <el-input 
                v-model="form.material" 
                placeholder="请输入材质"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="包装" prop="packaging">
              <el-input 
                v-model="form.packaging" 
                placeholder="请输入包装信息"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 价格信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>价格信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="销售价格" prop="salePrice">
              <el-input-number
                v-model="form.salePrice"
                :min="0"
                :precision="2"
                placeholder="请输入销售价格"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购价格" prop="purchasePrice">
              <el-input-number
                v-model="form.purchasePrice"
                :min="0"
                :precision="2"
                placeholder="请输入采购价格"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="成本价格" prop="costPrice">
              <el-input-number
                v-model="form.costPrice"
                :min="0"
                :precision="2"
                placeholder="请输入成本价格"
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

      <!-- 库存信息 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>库存信息</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="当前库存" prop="stock">
              <el-input-number
                v-model="form.stock"
                :min="0"
                placeholder="请输入当前库存"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安全库存" prop="safeStock">
              <el-input-number
                v-model="form.safeStock"
                :min="0"
                placeholder="请输入安全库存"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplier">
              <el-select 
                v-model="form.supplier" 
                placeholder="请选择供应商"
                style="width: 100%"
              >
                <el-option label="ABC制造有限公司" value="ABC" />
                <el-option label="XYZ贸易公司" value="XYZ" />
                <el-option label="DEF代理商" value="DEF" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库存位置" prop="location">
              <el-input 
                v-model="form.location" 
                placeholder="请输入库存位置"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 产品图片 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>产品图片</span>
          </div>
        </template>
        
        <el-form-item label="产品图片" prop="image">
          <el-upload
            class="product-upload"
            action="#"
            :auto-upload="false"
            :on-change="handleImageChange"
            :before-upload="beforeImageUpload"
            list-type="picture-card"
            :file-list="imageList"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">
            支持 JPG、PNG 格式，文件大小不超过 2MB
          </div>
        </el-form-item>
      </el-card>

      <!-- 产品描述 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>产品描述</span>
          </div>
        </template>
        
        <el-form-item label="中文描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入产品中文描述"
          />
        </el-form-item>

        <el-form-item label="英文描述" prop="descriptionEn">
          <el-input
            v-model="form.descriptionEn"
            type="textarea"
            :rows="4"
            placeholder="请输入产品英文描述"
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
import { Plus } from '@element-plus/icons-vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  product: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const isEdit = computed(() => !!props.product)

// 图片列表
const imageList = ref([])

// 表单数据
const form = reactive({
  productCode: '',
  category: '',
  productName: '',
  productNameEn: '',
  unit: '',
  status: 'order',
  brand: '',
  supplierItemNo: '',
  bladeColor: '',
  specification: '',
  handleColor: '',
  custom: '',
  dimensions: '',
  weight: 0,
  material: '',
  packaging: '',
  salePrice: 0,
  purchasePrice: 0,
  costPrice: 0,
  currency: 'USD',
  stock: 0,
  safeStock: 0,
  supplier: '',
  location: '',
  image: '',
  description: '',
  descriptionEn: '',
  remark: ''
})

// 表单验证规则
const rules = {
  category: [
    { required: true, message: '请选择产品分类', trigger: 'change' }
  ],
  productName: [
    { required: true, message: '请输入产品中文名称', trigger: 'blur' }
  ],
  productNameEn: [
    { required: true, message: '请输入产品英文名称', trigger: 'blur' }
  ],
  unit: [
    { required: true, message: '请选择单位', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择产品状态', trigger: 'change' }
  ],
  salePrice: [
    { required: true, message: '请输入销售价格', trigger: 'blur' }
  ],
  purchasePrice: [
    { required: true, message: '请输入采购价格', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入当前库存', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入产品中文描述', trigger: 'blur' }
  ],
  descriptionEn: [
    { required: true, message: '请输入产品英文描述', trigger: 'blur' }
  ]
}

// 生成产品编号
const generateProductCode = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0')
  return `P${year}${month}${day}${random}`
}

// 图片上传处理
const handleImageChange = (file, fileList) => {
  imageList.value = fileList
  form.image = fileList.map(f => f.url || f.response?.url).join(',')
}

// 图片上传前验证
const beforeImageUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isPNG = file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG && !isPNG) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 初始化表单
const initForm = () => {
  if (props.product) {
    // 编辑模式
    Object.assign(form, props.product)
    // 处理图片列表
    if (props.product.image) {
      imageList.value = props.product.image.split(',').map(url => ({
        url: url,
        name: 'product-image'
      }))
    }
  } else {
    // 新增模式
    Object.assign(form, {
      productCode: generateProductCode(),
      category: '',
      productName: '',
      productNameEn: '',
      unit: '',
      status: 'order',
      brand: '',
      supplierItemNo: '',
      bladeColor: '',
      specification: '',
      handleColor: '',
      custom: '',
      dimensions: '',
      weight: 0,
      material: '',
      packaging: '',
      salePrice: 0,
      purchasePrice: 0,
      costPrice: 0,
      currency: 'USD',
      stock: 0,
      safeStock: 0,
      supplier: '',
      location: '',
      image: '',
      description: '',
      descriptionEn: '',
      remark: ''
    })
    imageList.value = []
  }
}

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false)
}

// 删除产品
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个产品吗？删除后无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟删除操作
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('产品删除成功')
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
    
    ElMessage.success(isEdit.value ? '产品更新成功' : '产品创建成功')
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
.product-form {
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

.product-upload {
  width: 100%;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
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

:deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
  line-height: 100px;
}
</style> 