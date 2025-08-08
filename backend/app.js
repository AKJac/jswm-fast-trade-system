const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const compression = require('compression');
const morgan = require('morgan');
const rateLimit = require('express-rate-limit');
const path = require('path');
require('dotenv').config();

const { sequelize } = require('./config/database');
const routes = require('./routes');
const errorHandler = require('./middleware/errorHandler');
const { initializeDatabase } = require('./utils/initDatabase');

const app = express();
const PORT = process.env.PORT || 3000;

// 安全中间件
app.use(helmet());

// 限流中间件
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15分钟
  max: 1000, // 限制每个IP 15分钟内最多1000次请求
  message: '请求过于频繁，请稍后再试'
});
app.use(limiter);

// 压缩中间件
app.use(compression());

// 日志中间件
if (process.env.NODE_ENV === 'development') {
  app.use(morgan('dev'));
}

// CORS配置
app.use(cors({
  origin: process.env.NODE_ENV === 'production' 
    ? ['http://localhost:5173', 'http://localhost:4173'] 
    : true,
  credentials: true
}));

// 解析中间件
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));

// 静态文件服务
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// 健康检查
app.get('/health', (req, res) => {
  res.json({ 
    status: 'OK', 
    timestamp: new Date().toISOString(),
    environment: process.env.NODE_ENV 
  });
});

// API路由
app.use('/api', routes);

// 404处理
app.use('*', (req, res) => {
  res.status(404).json({
    success: false,
    message: '接口不存在',
    path: req.originalUrl
  });
});

// 错误处理中间件
app.use(errorHandler);

// 启动服务器
const startServer = async () => {
  try {
    // 测试数据库连接
    await sequelize.authenticate();
    console.log('数据库连接成功');
    
    // 初始化数据库
    await initializeDatabase();
    
    // 启动服务器
    app.listen(PORT, () => {
      console.log(`服务器运行在端口 ${PORT}`);
      console.log(`环境: ${process.env.NODE_ENV}`);
      console.log(`健康检查: http://localhost:${PORT}/health`);
    });
  } catch (error) {
    console.error('启动服务器失败:', error);
    process.exit(1);
  }
};

// 优雅关闭
process.on('SIGTERM', async () => {
  console.log('收到SIGTERM信号，开始优雅关闭...');
  await sequelize.close();
  process.exit(0);
});

process.on('SIGINT', async () => {
  console.log('收到SIGINT信号，开始优雅关闭...');
  await sequelize.close();
  process.exit(0);
});

startServer();

module.exports = app;