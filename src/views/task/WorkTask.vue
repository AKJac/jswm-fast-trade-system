<template>
  <div class="page-container">
    <div class="page-header">
      <h1>工作任务</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>工作任务</el-breadcrumb-item>
        <el-breadcrumb-item>任务管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 快速统计 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon total">
              <el-icon><List /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ taskStats.total }}</div>
              <div class="stat-label">任务总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon pending">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value pending-text">{{ taskStats.pending }}</div>
              <div class="stat-label">待处理</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon progress">
              <el-icon><Loading /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value progress-text">{{ taskStats.inProgress }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon completed">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value completed-text">{{ taskStats.completed }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 任务看板 -->
    <el-card class="custom-card">
      <template #header>
        <div class="card-header">
          <span>任务看板</span>
          <div class="header-actions">
            <el-button type="primary" size="small" @click="handleAddTask">
              <el-icon><Plus /></el-icon>
              新建任务
            </el-button>
            <el-segmented v-model="viewMode" :options="viewOptions" />
          </div>
        </div>
      </template>

      <!-- 看板视图 -->
      <div v-if="viewMode === 'board'" class="kanban-board">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
            <div class="kanban-column">
              <div class="column-header pending">
                <span class="column-title">待处理</span>
                <el-badge :value="pendingTasks.length" />
              </div>
              <div class="column-body">
                <draggable 
                  v-model="pendingTasks" 
                  group="tasks"
                  item-key="id"
                  @change="handleDragChange"
                  class="task-list"
                >
                  <template #item="{ element }">
                    <div class="task-card" @click="handleTaskClick(element)">
                      <div class="task-priority">
                        <el-tag :type="getPriorityType(element.priority)" size="small">
                          {{ getPriorityText(element.priority) }}
                        </el-tag>
                      </div>
                      <h4>{{ element.title }}</h4>
                      <p class="task-description">{{ element.description }}</p>
                      <div class="task-footer">
                        <div class="task-assignee">
                          <el-avatar size="small">{{ element.assignee[0] }}</el-avatar>
                          <span>{{ element.assignee }}</span>
                        </div>
                        <div class="task-due" :class="getDueDateClass(element.dueDate)">
                          <el-icon><Calendar /></el-icon>
                          {{ element.dueDate }}
                        </div>
                      </div>
                    </div>
                  </template>
                </draggable>
              </div>
            </div>
          </el-col>

          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
            <div class="kanban-column">
              <div class="column-header progress">
                <span class="column-title">进行中</span>
                <el-badge :value="inProgressTasks.length" />
              </div>
              <div class="column-body">
                <draggable 
                  v-model="inProgressTasks" 
                  group="tasks"
                  item-key="id"
                  @change="handleDragChange"
                  class="task-list"
                >
                  <template #item="{ element }">
                    <div class="task-card" @click="handleTaskClick(element)">
                      <div class="task-priority">
                        <el-tag :type="getPriorityType(element.priority)" size="small">
                          {{ getPriorityText(element.priority) }}
                        </el-tag>
                      </div>
                      <h4>{{ element.title }}</h4>
                      <p class="task-description">{{ element.description }}</p>
                      <div class="task-footer">
                        <div class="task-assignee">
                          <el-avatar size="small">{{ element.assignee[0] }}</el-avatar>
                          <span>{{ element.assignee }}</span>
                        </div>
                        <div class="task-due" :class="getDueDateClass(element.dueDate)">
                          <el-icon><Calendar /></el-icon>
                          {{ element.dueDate }}
                        </div>
                      </div>
                    </div>
                  </template>
                </draggable>
              </div>
            </div>
          </el-col>

          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
            <div class="kanban-column">
              <div class="column-header review">
                <span class="column-title">待审核</span>
                <el-badge :value="reviewTasks.length" />
              </div>
              <div class="column-body">
                <draggable 
                  v-model="reviewTasks" 
                  group="tasks"
                  item-key="id"
                  @change="handleDragChange"
                  class="task-list"
                >
                  <template #item="{ element }">
                    <div class="task-card" @click="handleTaskClick(element)">
                      <div class="task-priority">
                        <el-tag :type="getPriorityType(element.priority)" size="small">
                          {{ getPriorityText(element.priority) }}
                        </el-tag>
                      </div>
                      <h4>{{ element.title }}</h4>
                      <p class="task-description">{{ element.description }}</p>
                      <div class="task-footer">
                        <div class="task-assignee">
                          <el-avatar size="small">{{ element.assignee[0] }}</el-avatar>
                          <span>{{ element.assignee }}</span>
                        </div>
                        <div class="task-due" :class="getDueDateClass(element.dueDate)">
                          <el-icon><Calendar /></el-icon>
                          {{ element.dueDate }}
                        </div>
                      </div>
                    </div>
                  </template>
                </draggable>
              </div>
            </div>
          </el-col>

          <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6">
            <div class="kanban-column">
              <div class="column-header completed">
                <span class="column-title">已完成</span>
                <el-badge :value="completedTasks.length" />
              </div>
              <div class="column-body">
                <draggable 
                  v-model="completedTasks" 
                  group="tasks"
                  item-key="id"
                  @change="handleDragChange"
                  class="task-list"
                >
                  <template #item="{ element }">
                    <div class="task-card completed-card" @click="handleTaskClick(element)">
                      <div class="task-priority">
                        <el-tag :type="getPriorityType(element.priority)" size="small">
                          {{ getPriorityText(element.priority) }}
                        </el-tag>
                      </div>
                      <h4>{{ element.title }}</h4>
                      <p class="task-description">{{ element.description }}</p>
                      <div class="task-footer">
                        <div class="task-assignee">
                          <el-avatar size="small">{{ element.assignee[0] }}</el-avatar>
                          <span>{{ element.assignee }}</span>
                        </div>
                        <div class="task-due">
                          <el-icon><Calendar /></el-icon>
                          {{ element.dueDate }}
                        </div>
                      </div>
                    </div>
                  </template>
                </draggable>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 列表视图 -->
      <div v-if="viewMode === 'list'" class="list-view">
        <el-table :data="allTasks" stripe>
          <el-table-column prop="title" label="任务标题" min-width="200">
            <template #default="{ row }">
              <el-link type="primary" @click="handleTaskClick(row)">
                {{ row.title }}
              </el-link>
            </template>
          </el-table-column>
          <el-table-column prop="priority" label="优先级" width="100">
            <template #default="{ row }">
              <el-tag :type="getPriorityType(row.priority)" size="small">
                {{ getPriorityText(row.priority) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)" size="small">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="assignee" label="负责人" width="120" />
          <el-table-column prop="dueDate" label="截止日期" width="120">
            <template #default="{ row }">
              <span :class="getDueDateClass(row.dueDate)">{{ row.dueDate }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="150" />
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleTaskClick(row)">
                查看
              </el-button>
              <el-button type="danger" size="small" @click="handleDeleteTask(row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 常用笔记 -->
    <el-card class="custom-card notes-card">
      <template #header>
        <div class="card-header">
          <span>常用笔记</span>
          <el-button type="primary" size="small" @click="handleAddNote">
            <el-icon><Plus /></el-icon>
            新建笔记
          </el-button>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col 
          v-for="note in notesList" 
          :key="note.id"
          :xs="24" :sm="12" :md="8" :lg="6" :xl="6"
        >
          <div class="note-item">
            <div class="note-header">
              <h4>{{ note.title }}</h4>
              <el-dropdown trigger="click">
                <el-icon class="note-actions"><MoreFilled /></el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleEditNote(note)">
                      <el-icon><Edit /></el-icon>
                      编辑
                    </el-dropdown-item>
                    <el-dropdown-item @click="handleDeleteNote(note)" divided>
                      <el-icon><Delete /></el-icon>
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <p class="note-content">{{ note.content }}</p>
            <div class="note-footer">
              <span class="note-author">{{ note.author }}</span>
              <span class="note-date">{{ note.createTime }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import draggable from 'vuedraggable'

// 响应式数据
const viewMode = ref('board')
const viewOptions = [
  { label: '看板视图', value: 'board' },
  { label: '列表视图', value: 'list' }
]

const taskStats = reactive({
  total: 12,
  pending: 4,
  inProgress: 3,
  completed: 5
})

// 任务数据
const pendingTasks = ref([
  {
    id: 1,
    title: '联系新客户ABC公司',
    description: '发送产品目录和报价单，跟进客户需求',
    priority: 'high',
    assignee: '张三',
    dueDate: '2024-12-28',
    status: 'pending'
  },
  {
    id: 2,
    title: '准备年终总结报告',
    description: '整理2024年销售数据和客户分析',
    priority: 'medium',
    assignee: '李四',
    dueDate: '2024-12-30',
    status: 'pending'
  }
])

const inProgressTasks = ref([
  {
    id: 3,
    title: '处理订单SO202412001',
    description: '跟进生产进度，确保按时交货',
    priority: 'high',
    assignee: '王五',
    dueDate: '2024-12-25',
    status: 'progress'
  }
])

const reviewTasks = ref([
  {
    id: 4,
    title: '审核新供应商资质',
    description: '评估供应商生产能力和产品质量',
    priority: 'medium',
    assignee: '赵六',
    dueDate: '2024-12-26',
    status: 'review'
  }
])

const completedTasks = ref([
  {
    id: 5,
    title: '完成产品拍摄',
    description: '新产品系列照片拍摄和后期处理',
    priority: 'low',
    assignee: '周七',
    dueDate: '2024-12-20',
    status: 'completed'
  }
])

// 笔记数据
const notesList = ref([
  {
    id: 1,
    title: '客户沟通要点',
    content: '1. 始终保持专业和礼貌\n2. 及时回复客户邮件\n3. 准确记录客户需求\n4. 定期跟进订单状态',
    author: '管理员',
    createTime: '2024-12-15'
  },
  {
    id: 2,
    title: '报价注意事项',
    content: '1. 核实产品库存\n2. 确认运费和税费\n3. 注明付款条款\n4. 标注有效期',
    author: '张三',
    createTime: '2024-12-18'
  },
  {
    id: 3,
    title: '常用邮件模板',
    content: 'Dear Customer,\nThank you for your inquiry...',
    author: '李四',
    createTime: '2024-12-20'
  }
])

// 计算属性
const allTasks = computed(() => {
  return [
    ...pendingTasks.value,
    ...inProgressTasks.value,
    ...reviewTasks.value,
    ...completedTasks.value
  ]
})

// 方法
const getPriorityType = (priority) => {
  const typeMap = {
    'high': 'danger',
    'medium': 'warning',
    'low': 'info'
  }
  return typeMap[priority] || 'info'
}

const getPriorityText = (priority) => {
  const textMap = {
    'high': '高',
    'medium': '中',
    'low': '低'
  }
  return textMap[priority] || priority
}

const getStatusType = (status) => {
  const typeMap = {
    'pending': 'warning',
    'progress': 'primary',
    'review': 'info',
    'completed': 'success'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    'pending': '待处理',
    'progress': '进行中',
    'review': '待审核',
    'completed': '已完成'
  }
  return textMap[status] || status
}

const getDueDateClass = (dueDate) => {
  const today = new Date()
  const due = new Date(dueDate)
  const diffDays = Math.ceil((due - today) / (1000 * 60 * 60 * 24))
  
  if (diffDays < 0) return 'overdue'
  if (diffDays <= 3) return 'urgent'
  return 'normal'
}

const handleAddTask = () => {
  ElMessage.info('新建任务功能开发中...')
}

const handleTaskClick = (task) => {
  ElMessage.info(`查看任务：${task.title}`)
}

const handleDeleteTask = (task) => {
  ElMessageBox.confirm(
    `确定要删除任务 "${task.title}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('任务已删除')
  })
}

const handleDragChange = () => {
  ElMessage.success('任务状态已更新')
}

const handleAddNote = () => {
  ElMessage.info('新建笔记功能开发中...')
}

const handleEditNote = (note) => {
  ElMessage.info(`编辑笔记：${note.title}`)
}

const handleDeleteNote = (note) => {
  ElMessageBox.confirm(
    `确定要删除笔记 "${note.title}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = notesList.value.findIndex(item => item.id === note.id)
    if (index > -1) {
      notesList.value.splice(index, 1)
      ElMessage.success('笔记已删除')
    }
  })
}
</script>

<style lang="scss" scoped>
.stats-row {
  margin-bottom: 20px;
  
  .stat-card {
    :deep(.el-card__body) {
      padding: 20px;
    }
    
    .stat-item {
      display: flex;
      align-items: center;
      
      .stat-icon {
        width: 50px;
        height: 50px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 24px;
        color: white;
        margin-right: 15px;
        
        &.total {
          background: linear-gradient(135deg, #409EFF 0%, #2D8CF0 100%);
        }
        
        &.pending {
          background: linear-gradient(135deg, #E6A23C 0%, #D48806 100%);
        }
        
        &.progress {
          background: linear-gradient(135deg, #67C23A 0%, #59B329 100%);
        }
        
        &.completed {
          background: linear-gradient(135deg, #909399 0%, #73767A 100%);
        }
      }
      
      .stat-content {
        flex: 1;
        
        .stat-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          line-height: 1.2;
          
          &.pending-text { color: #E6A23C; }
          &.progress-text { color: #67C23A; }
          &.completed-text { color: #909399; }
        }
        
        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 5px;
        }
      }
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  .header-actions {
    display: flex;
    gap: 15px;
    align-items: center;
  }
}

// 看板视图样式
.kanban-board {
  .kanban-column {
    background: #f5f7fa;
    border-radius: 8px;
    height: 600px;
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
    
    .column-header {
      padding: 15px;
      font-weight: 600;
      font-size: 16px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-radius: 8px 8px 0 0;
      
      &.pending { background: #fdf6ec; color: #E6A23C; }
      &.progress { background: #f0f9ff; color: #409EFF; }
      &.review { background: #f4f4f5; color: #909399; }
      &.completed { background: #f0f9ff; color: #67C23A; }
    }
    
    .column-body {
      flex: 1;
      overflow-y: auto;
      padding: 15px;
      
      .task-list {
        min-height: 100%;
      }
      
      .task-card {
        background: white;
        border-radius: 8px;
        padding: 15px;
        margin-bottom: 10px;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        
        &.completed-card {
          opacity: 0.7;
          
          h4 {
            text-decoration: line-through;
          }
        }
        
        .task-priority {
          margin-bottom: 10px;
        }
        
        h4 {
          margin: 0 0 8px 0;
          font-size: 15px;
          color: #303133;
        }
        
        .task-description {
          font-size: 13px;
          color: #909399;
          margin-bottom: 12px;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
        
        .task-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;
          
          .task-assignee {
            display: flex;
            align-items: center;
            gap: 5px;
            font-size: 12px;
            color: #606266;
          }
          
          .task-due {
            font-size: 12px;
            display: flex;
            align-items: center;
            gap: 3px;
            
            &.normal { color: #606266; }
            &.urgent { color: #E6A23C; }
            &.overdue { color: #F56C6C; }
          }
        }
      }
    }
  }
}

// 笔记卡片样式
.notes-card {
  margin-top: 20px;
  
  .note-item {
    background: #f8f9ff;
    border-radius: 8px;
    padding: 15px;
    height: 180px;
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
    
    .note-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
      
      h4 {
        margin: 0;
        font-size: 16px;
        color: #303133;
      }
      
      .note-actions {
        cursor: pointer;
        color: #909399;
        
        &:hover {
          color: #606266;
        }
      }
    }
    
    .note-content {
      flex: 1;
      font-size: 14px;
      color: #606266;
      margin-bottom: 10px;
      overflow: hidden;
      white-space: pre-wrap;
      display: -webkit-box;
      -webkit-line-clamp: 4;
      -webkit-box-orient: vertical;
    }
    
    .note-footer {
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      color: #909399;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .kanban-column {
    height: 400px !important;
  }
  
  .card-header {
    flex-direction: column;
    gap: 10px;
    align-items: stretch;
    
    .header-actions {
      justify-content: center;
    }
  }
}
</style> 