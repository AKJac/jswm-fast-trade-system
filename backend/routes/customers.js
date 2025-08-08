const express = require('express');
const router = express.Router();
const customerController = require('../controllers/customerController');

// 客户列表
router.get('/', customerController.getCustomers);

// 客户统计
router.get('/stats', customerController.getCustomerStats);

// 客户名称查重
router.get('/check-name', customerController.checkCustomerName);

// 客户详情
router.get('/:id', customerController.getCustomer);

// 创建客户
router.post('/', customerController.createCustomer);

// 更新客户
router.put('/:id', customerController.updateCustomer);

// 删除客户
router.delete('/:id', customerController.deleteCustomer);

// 批量删除客户
router.post('/batch-delete', customerController.batchDeleteCustomers);

module.exports = router;