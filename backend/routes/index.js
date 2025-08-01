const express = require('express');
const router = express.Router();

// 导入各模块路由
const customerRoutes = require('./customers');
const productRoutes = require('./products');
const quotationRoutes = require('./quotations');

// 注册路由
router.use('/customers', customerRoutes);
router.use('/products', productRoutes);
router.use('/quotations', quotationRoutes);

// API根路径
router.get('/', (req, res) => {
  res.json({
    success: true,
    message: '极速外贸管理系统 API',
    version: '1.0.0',
    company: '鸿思特科技有限公司',
    contact: 'hst_co@163.com',
    timestamp: new Date().toISOString()
  });
});

module.exports = router;