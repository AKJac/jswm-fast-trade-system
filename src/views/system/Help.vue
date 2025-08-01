<template>
  <div class="page-container">
    <div class="page-header">
      <h1>帮助中心</h1>
      <el-breadcrumb class="breadcrumb">
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>帮助中心</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-row :gutter="20">
      <!-- 左侧导航 -->
      <el-col :xs="24" :sm="8" :md="6" :lg="6" :xl="6">
        <el-card class="help-nav-card">
          <el-menu
            :default-active="activeHelp"
            @select="handleHelpSelect"
            class="help-menu"
          >
            <el-menu-item index="guide">
              <el-icon><Document /></el-icon>
              <span>使用指南</span>
            </el-menu-item>
            <el-menu-item index="faq">
              <el-icon><QuestionFilled /></el-icon>
              <span>常见问题</span>
            </el-menu-item>
            <el-menu-item index="video">
              <el-icon><VideoPlay /></el-icon>
              <span>视频教程</span>
            </el-menu-item>
            <el-menu-item index="contact">
              <el-icon><Phone /></el-icon>
              <span>联系支持</span>
            </el-menu-item>
            <el-menu-item index="feedback">
              <el-icon><ChatDotRound /></el-icon>
              <span>意见反馈</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-col>

      <!-- 右侧内容 -->
      <el-col :xs="24" :sm="16" :md="18" :lg="18" :xl="18">
        <el-card class="help-content-card">
          <!-- 使用指南 -->
          <div v-if="activeHelp === 'guide'" class="help-section">
            <h3>系统使用指南</h3>
            
            <el-collapse v-model="activeGuide" accordion>
              <el-collapse-item title="快速开始" name="quickstart">
                <div class="guide-content">
                  <h4>1. 登录系统</h4>
                  <p>使用您的用户名和密码登录系统，首次登录建议修改默认密码。</p>
                  
                  <h4>2. 熟悉界面</h4>
                  <p>系统主要分为以下几个模块：</p>
                  <ul>
                    <li><strong>控制台：</strong>查看系统概览和重要数据</li>
                    <li><strong>客户管理：</strong>管理客户信息和跟进记录</li>
                    <li><strong>产品管理：</strong>维护产品目录和价格信息</li>
                    <li><strong>订单管理：</strong>处理销售订单和跟踪状态</li>
                    <li><strong>财务管理：</strong>管理收付款和财务报表</li>
                  </ul>
                  
                  <h4>3. 基本操作</h4>
                  <p>学会使用系统的增删改查功能，掌握数据导入导出操作。</p>
                </div>
              </el-collapse-item>
              
              <el-collapse-item title="客户管理" name="customer">
                <div class="guide-content">
                  <h4>客户信息录入</h4>
                  <p>在客户管理模块中，您可以：</p>
                  <ul>
                    <li>添加新客户信息</li>
                    <li>记录客户联系历史</li>
                    <li>设置客户分类和标签</li>
                    <li>管理客户跟进计划</li>
                  </ul>
                  
                  <h4>客户公海管理</h4>
                  <p>客户公海功能允许团队成员共享客户资源，提高客户转化率。</p>
                </div>
              </el-collapse-item>
              
              <el-collapse-item title="订单处理" name="order">
                <div class="guide-content">
                  <h4>创建订单</h4>
                  <p>订单处理流程：</p>
                  <ol>
                    <li>选择客户</li>
                    <li>添加产品</li>
                    <li>设置价格和数量</li>
                    <li>确认订单信息</li>
                    <li>生成订单</li>
                  </ol>
                  
                  <h4>订单跟踪</h4>
                  <p>系统会自动跟踪订单状态，包括：待确认、已确认、生产中、已发货、已完成等。</p>
                </div>
              </el-collapse-item>
              
              <el-collapse-item title="财务管理" name="finance">
                <div class="guide-content">
                  <h4>收付款管理</h4>
                  <p>在财务管理模块中，您可以：</p>
                  <ul>
                    <li>记录客户付款</li>
                    <li>管理供应商付款</li>
                    <li>生成财务报表</li>
                    <li>跟踪应收账款</li>
                  </ul>
                  
                  <h4>财务报表</h4>
                  <p>系统提供多种财务报表，帮助您了解业务状况。</p>
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>

          <!-- 常见问题 -->
          <div v-if="activeHelp === 'faq'" class="help-section">
            <h3>常见问题</h3>
            
            <el-collapse v-model="activeFaq" accordion>
              <el-collapse-item title="如何修改密码？" name="password">
                <p>进入个人中心 → 修改密码，输入当前密码和新密码即可修改。</p>
              </el-collapse-item>
              
              <el-collapse-item title="忘记密码怎么办？" name="forgot">
                <p>请联系系统管理员重置密码，或使用邮箱验证码重置。</p>
              </el-collapse-item>
              
              <el-collapse-item title="如何导出数据？" name="export">
                <p>在列表页面点击"导出"按钮，选择导出格式（Excel、PDF等）即可。</p>
              </el-collapse-item>
              
              <el-collapse-item title="系统支持哪些浏览器？" name="browser">
                <p>系统支持Chrome、Firefox、Safari、Edge等主流浏览器，建议使用最新版本。</p>
              </el-collapse-item>
              
              <el-collapse-item title="如何备份数据？" name="backup">
                <p>系统会自动备份数据，您也可以在系统设置中手动备份。</p>
              </el-collapse-item>
              
              <el-collapse-item title="如何添加新用户？" name="user">
                <p>管理员可以在用户管理模块中添加新用户，设置相应的角色和权限。</p>
              </el-collapse-item>
            </el-collapse>
          </div>

          <!-- 视频教程 -->
          <div v-if="activeHelp === 'video'" class="help-section">
            <h3>视频教程</h3>
            
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" v-for="video in videoList" :key="video.id">
                <el-card class="video-card" shadow="hover">
                  <div class="video-thumbnail">
                    <el-image :src="video.thumbnail" fit="cover" />
                    <div class="video-duration">{{ video.duration }}</div>
                  </div>
                  <div class="video-info">
                    <h4>{{ video.title }}</h4>
                    <p>{{ video.description }}</p>
                    <el-button type="primary" size="small" @click="playVideo(video)">
                      观看视频
                    </el-button>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 联系支持 -->
          <div v-if="activeHelp === 'contact'" class="help-section">
            <h3>联系技术支持</h3>
            
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
                <el-card class="contact-card">
                  <h4>公司信息</h4>
                  <div class="contact-info">
                    <p><strong>公司名称：</strong>鸿思特科技有限公司</p>
                    <p><strong>联系电话：</strong>17600198256</p>
                    <p><strong>联系邮箱：</strong>hst_co@163.com</p>
                    <p><strong>服务时间：</strong>周一至周五 9:00-18:00</p>
                  </div>
                </el-card>
              </el-col>
              
              <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
                <el-card class="contact-card">
                  <h4>在线支持</h4>
                  <div class="contact-actions">
                    <el-button type="primary" @click="openChat">
                      <el-icon><ChatDotRound /></el-icon>
                      在线客服
                    </el-button>
                    <el-button @click="sendEmail">
                      <el-icon><Message /></el-icon>
                      发送邮件
                    </el-button>
                    <el-button @click="callSupport">
                      <el-icon><Phone /></el-icon>
                      电话咨询
                    </el-button>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            
            <el-card class="contact-form-card">
              <h4>提交问题</h4>
              <el-form :model="contactForm" label-width="100px">
                <el-form-item label="问题类型">
                  <el-select v-model="contactForm.type" placeholder="请选择问题类型">
                    <el-option label="功能问题" value="function" />
                    <el-option label="系统错误" value="error" />
                    <el-option label="使用咨询" value="consultation" />
                    <el-option label="其他" value="other" />
                  </el-select>
                </el-form-item>
                <el-form-item label="问题标题">
                  <el-input v-model="contactForm.title" placeholder="请简要描述问题" />
                </el-form-item>
                <el-form-item label="问题描述">
                  <el-input v-model="contactForm.description" type="textarea" :rows="4" placeholder="请详细描述问题" />
                </el-form-item>
                <el-form-item label="联系方式">
                  <el-input v-model="contactForm.contact" placeholder="请输入您的联系方式" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitContact">提交问题</el-button>
                  <el-button @click="resetContactForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </div>

          <!-- 意见反馈 -->
          <div v-if="activeHelp === 'feedback'" class="help-section">
            <h3>意见反馈</h3>
            
            <el-form :model="feedbackForm" label-width="100px">
              <el-form-item label="反馈类型">
                <el-radio-group v-model="feedbackForm.type">
                  <el-radio label="suggestion">功能建议</el-radio>
                  <el-radio label="bug">问题反馈</el-radio>
                  <el-radio label="praise">表扬</el-radio>
                  <el-radio label="complaint">投诉</el-radio>
                </el-radio-group>
              </el-form-item>
              
              <el-form-item label="反馈标题">
                <el-input v-model="feedbackForm.title" placeholder="请简要描述您的反馈" />
              </el-form-item>
              
              <el-form-item label="详细描述">
                <el-input v-model="feedbackForm.description" type="textarea" :rows="6" placeholder="请详细描述您的反馈内容" />
              </el-form-item>
              
              <el-form-item label="期望改进">
                <el-input v-model="feedbackForm.expectation" type="textarea" :rows="3" placeholder="请描述您期望的改进方向" />
              </el-form-item>
              
              <el-form-item label="联系方式">
                <el-input v-model="feedbackForm.contact" placeholder="请输入您的联系方式（可选）" />
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
                <el-button @click="resetFeedbackForm">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

// 响应式数据
const activeHelp = ref('guide')
const activeGuide = ref('quickstart')
const activeFaq = ref('password')

// 视频列表
const videoList = ref([
  {
    id: 1,
    title: '系统入门教程',
    description: '介绍系统基本功能和操作流程',
    duration: '15:30',
    thumbnail: 'https://via.placeholder.com/300x200'
  },
  {
    id: 2,
    title: '客户管理详解',
    description: '详细讲解客户管理模块的使用方法',
    duration: '20:15',
    thumbnail: 'https://via.placeholder.com/300x200'
  },
  {
    id: 3,
    title: '订单处理流程',
    description: '演示完整的订单处理流程',
    duration: '18:45',
    thumbnail: 'https://via.placeholder.com/300x200'
  },
  {
    id: 4,
    title: '财务管理指南',
    description: '介绍财务管理模块的各项功能',
    duration: '25:20',
    thumbnail: 'https://via.placeholder.com/300x200'
  },
  {
    id: 5,
    title: '数据统计分析',
    description: '如何使用系统的数据统计功能',
    duration: '22:10',
    thumbnail: 'https://via.placeholder.com/300x200'
  },
  {
    id: 6,
    title: '系统设置配置',
    description: '系统各项设置的配置方法',
    duration: '16:35',
    thumbnail: 'https://via.placeholder.com/300x200'
  }
])

// 联系表单
const contactForm = reactive({
  type: '',
  title: '',
  description: '',
  contact: ''
})

// 反馈表单
const feedbackForm = reactive({
  type: 'suggestion',
  title: '',
  description: '',
  expectation: '',
  contact: ''
})

// 方法
const handleHelpSelect = (key) => {
  activeHelp.value = key
}

const playVideo = (video) => {
  ElMessage.info(`播放视频：${video.title}`)
}

const openChat = () => {
  ElMessage.info('在线客服功能开发中...')
}

const sendEmail = () => {
  window.open('mailto:hst_co@163.com?subject=技术支持咨询')
}

const callSupport = () => {
  ElMessage.info('正在拨打技术支持电话：17600198256')
}

const submitContact = () => {
  ElMessage.success('问题已提交，我们会尽快回复您')
  contactForm.type = ''
  contactForm.title = ''
  contactForm.description = ''
  contactForm.contact = ''
}

const resetContactForm = () => {
  contactForm.type = ''
  contactForm.title = ''
  contactForm.description = ''
  contactForm.contact = ''
}

const submitFeedback = () => {
  ElMessage.success('反馈已提交，感谢您的宝贵意见')
  feedbackForm.type = 'suggestion'
  feedbackForm.title = ''
  feedbackForm.description = ''
  feedbackForm.expectation = ''
  feedbackForm.contact = ''
}

const resetFeedbackForm = () => {
  feedbackForm.type = 'suggestion'
  feedbackForm.title = ''
  feedbackForm.description = ''
  feedbackForm.expectation = ''
  feedbackForm.contact = ''
}
</script>

<style lang="scss" scoped>
.help-nav-card {
  .help-menu {
    border-right: none;
    
    :deep(.el-menu-item) {
      height: 50px;
      line-height: 50px;
      
      &.is-active {
        background-color: #409eff;
        color: white;
        
        .el-icon {
          color: white;
        }
      }
      
      &:hover {
        background-color: #ecf5ff;
        color: #409eff;
      }
    }
  }
}

.help-content-card {
  .help-section {
    h3 {
      margin: 0 0 20px 0;
      color: #303133;
      font-size: 18px;
      font-weight: 600;
    }
    
    .guide-content {
      h4 {
        color: #409eff;
        margin: 15px 0 10px 0;
      }
      
      p {
        margin: 10px 0;
        line-height: 1.6;
      }
      
      ul, ol {
        margin: 10px 0;
        padding-left: 20px;
        
        li {
          margin: 5px 0;
          line-height: 1.6;
        }
      }
    }
  }
}

.video-card {
  margin-bottom: 20px;
  
  .video-thumbnail {
    position: relative;
    margin-bottom: 15px;
    
    .el-image {
      width: 100%;
      height: 150px;
      border-radius: 8px;
    }
    
    .video-duration {
      position: absolute;
      bottom: 8px;
      right: 8px;
      background: rgba(0, 0, 0, 0.7);
      color: white;
      padding: 2px 6px;
      border-radius: 4px;
      font-size: 12px;
    }
  }
  
  .video-info {
    h4 {
      margin: 0 0 8px 0;
      font-size: 16px;
      color: #303133;
    }
    
    p {
      margin: 0 0 15px 0;
      color: #606266;
      font-size: 14px;
      line-height: 1.4;
    }
  }
}

.contact-card {
  margin-bottom: 20px;
  
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 16px;
  }
  
  .contact-info {
    p {
      margin: 8px 0;
      line-height: 1.6;
    }
  }
  
  .contact-actions {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
}

.contact-form-card {
  margin-top: 20px;
  
  h4 {
    margin: 0 0 20px 0;
    color: #303133;
    font-size: 16px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .help-nav-card {
    margin-bottom: 20px;
  }
  
  .video-card {
    .video-thumbnail {
      .el-image {
        height: 120px;
      }
    }
  }
  
  .contact-actions {
    flex-direction: column;
  }
}
</style> 