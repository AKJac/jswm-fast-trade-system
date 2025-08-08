const express = require('express');
const router = express.Router();
const quotationController = require('../controllers/quotationController');

// 报价单列表
router.get('/', quotationController.getQuotations);

// 报价单统计
router.get('/stats', quotationController.getQuotationStats);

// 报价单详情
router.get('/:id', quotationController.getQuotation);

// 创建报价单
router.post('/', quotationController.createQuotation);

// 更新报价单
router.put('/:id', quotationController.updateQuotation);

// 删除报价单
router.delete('/:id', quotationController.deleteQuotation);

module.exports = router;