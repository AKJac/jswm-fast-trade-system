const express = require('express');
const router = express.Router();
const productController = require('../controllers/productController');

// 产品列表
router.get('/', productController.getProducts);

// 产品统计
router.get('/stats', productController.getProductStats);

// 产品选项（用于下拉选择）
router.get('/options', productController.getProductOptions);

// 产品详情
router.get('/:id', productController.getProduct);

// 创建产品
router.post('/', productController.createProduct);

// 更新产品
router.put('/:id', productController.updateProduct);

// 删除产品
router.delete('/:id', productController.deleteProduct);

// 批量删除产品
router.post('/batch-delete', productController.batchDeleteProducts);

module.exports = router;