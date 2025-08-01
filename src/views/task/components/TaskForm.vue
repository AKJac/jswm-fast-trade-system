<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑任务' : '新建任务'"
    width="800px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="task-form"
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
            <el-form-item label="任务编号" prop="taskNo">
              <el-input 
                v-model="form.taskNo" 
                placeholder="系统自动生成"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务标题" prop="title">
              <el-input 
                v-model="form.title" 
                placeholder="请输入任务标题"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="任务类型" prop="type">
              <el-select 
                v-model="form.type" 
                placeholder="请选择任务类型"
                style="width: 100%"
              >
                <el-option label="客户跟进" value="customer_follow" />
                <el-option label="订单处理" value="order_process" />
                <el-option label="报价制作" value="quotation_make" />
                <el-option label="采购任务" value="purchase_task" />
                <el-option label="库存管理" value="inventory_manage" />
                <el-option label="财务处理" value="finance_process" />
                <el-option label="物流安排" value="logistics_arrange" />
                <el-option label="其他任务" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select 
                v-model="form.priority" 
                placeholder="请选择优先级"
                style="width: 100%"
              >
                <el-option label="低" value="low" />
                <el-option label="中" value="medium" />
                <el-option label="高" value="high" />
                <el-option label="紧急" value="urgent" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="assignee">
              <el-select 
                v-model="form.assignee" 
                placeholder="请选择负责人"
                style="width: 100%"
              >
                <el-option 
                  v-for="user in userList" 
                  :key="user.id" 
                  :label="user.name" 
                  :value="user.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务状态" prop="status">
              <el-select 
                v-model="form.status" 
                placeholder="请选择状态"
                style="width: 100%"
              >
                <el-option label="待处理" value="pending" />
                <el-option label="进行中" value="in_progress" />
                <el-option label="已完成" value="completed" />
                <el-option label="已暂停" value="paused" />
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
            <el-form-item label="开始时间" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="datetime"
                placeholder="请选择开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="截止时间" prop="dueDate">
              <el-date-picker
                v-model="form.dueDate"
                type="datetime"
                placeholder="请选择截止时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预计工时" prop="estimatedHours">
              <el-input-number
                v-model="form.estimatedHours"
                :min="0"
                :max="1000"
                placeholder="请输入预计工时"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际工时" prop="actualHours">
              <el-input-number
                v-model="form.actualHours"
                :min="0"
                :max="1000"
                placeholder="请输入实际工时"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <!-- 任务详情 -->
      <el-card class="form-section">
        <template #header>
          <div class="card-header">
            <span>任务详情</span>
          </div>
        </template>
        
        <el-form-item label="任务描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入任务描述"
          />
        </el-form-item>

        <el-form-item label="相关客户" prop="relatedCustomer">
          <el-select 
            v-model="form.relatedCustomer" 
            placeholder="请选择相关客户"
            clearable
            style="width: 100%"
          >
            <el-option 
              v-for="customer in customerList" 
              :key="customer.id" 
              :label="customer.customerName" 
              :value="customer.id" 
            />
          </el-select>
        </el-form-item>

        <el-form-item label="相关订单" prop="relatedOrder">
          <el-select 
            v-model="form.relatedOrder" 
            placeholder="请选择相关订单"
            clearable
            style="width: 100%"
          >
            <el-option 
              v-for="order in orderList" 
              :key="order.id" 
              :label="order.orderNo" 
              :value="order.id" 
            />
          </el-select>
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
  task: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const isEdit = computed(() => !!props.task)

// 表单数据
const form = reactive({
  taskNo: '',
  title: '',
  type: '',
  priority: 'medium',
  assignee: '',
  status: 'pending',
  startDate: '',
  dueDate: '',
  estimatedHours: 0,
  actualHours: 0,
  description: '',
  relatedCustomer: '',
  relatedOrder: '',
  remark: ''
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入任务标题', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择任务类型', trigger: 'change' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ],
  assignee: [
    { required: true, message: '请选择负责人', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择任务状态', trigger: 'change' }
  ],
  startDate: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  dueDate: [
    { required: true, message: '请选择截止时间', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入任务描述', trigger: 'blur' }
  ]
}

// 模拟数据
const userList = ref([
  { id: 1, name: '张三' },
  { id: 2, name: '李四' },
  { id: 3, name: '王五' },
  { id: 4, name: '赵六' }
])

const customerList = ref([
  { id: 1, customerName: 'ABC公司' },
  { id: 2, customerName: 'XYZ贸易' },
  { id: 3, customerName: 'DEF集团' }
])

const orderList = ref([
  { id: 1, orderNo: 'O20241201001' },
  { id: 2, orderNo: 'O20241201002' },
  { id: 3, orderNo: 'O20241201003' }
])

// 生成任务编号
const generateTaskNo = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0')
  return `T${year}${month}${day}${random}`
}

// 初始化表单
const initForm = () => {
  if (props.task) {
    // 编辑模式
    Object.assign(form, props.task)
  } else {
    // 新增模式
    Object.assign(form, {
      taskNo: generateTaskNo(),
      title: '',
      type: '',
      priority: 'medium',
      assignee: '',
      status: 'pending',
      startDate: new Date(),
      dueDate: '',
      estimatedHours: 0,
      actualHours: 0,
      description: '',
      relatedCustomer: '',
      relatedOrder: '',
      remark: ''
    })
  }
}

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false)
}

// 删除任务
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个任务吗？删除后无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟删除操作
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('任务删除成功')
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
    
    ElMessage.success(isEdit.value ? '任务更新成功' : '任务创建成功')
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
.task-form {
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