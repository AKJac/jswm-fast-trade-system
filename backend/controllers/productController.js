const { Product } = require('../models');
const { Op } = require('sequelize');

// 生成产品编号
const generateProductCode = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0');
  return `P${year}${month}${day}${random}`;
};

// 获取产品列表
const getProducts = async (req, res, next) => {
  try {
    const {
      page = 1,
      limit = 10,
      productName,
      productCode,
      category,
      status,
      brand,
      supplier
    } = req.query;

    const offset = (page - 1) * limit;
    const where = { is_active: true };

    // 构建查询条件
    if (productName) {
      where[Op.or] = [
        { product_name: { [Op.like]: `%${productName}%` } },
        { product_name_en: { [Op.like]: `%${productName}%` } }
      ];
    }
    if (productCode) {
      where.product_code = { [Op.like]: `%${productCode}%` };
    }
    if (category) {
      where.category = category;
    }
    if (status) {
      where.status = status;
    }
    if (brand) {
      where.brand = { [Op.like]: `%${brand}%` };
    }

    const { count, rows } = await Product.findAndCountAll({
      where,
      limit: parseInt(limit),
      offset: parseInt(offset),
      order: [['created_at', 'DESC']]
    });

    res.json({
      success: true,
      data: {
        list: rows,
        total: count,
        page: parseInt(page),
        limit: parseInt(limit),
        totalPages: Math.ceil(count / limit)
      }
    });
  } catch (error) {
    next(error);
  }
};

// 获取产品详情
const getProduct = async (req, res, next) => {
  try {
    const { id } = req.params;
    
    const product = await Product.findOne({
      where: { id, is_active: true }
    });

    if (!product) {
      return res.status(404).json({
        success: false,
        message: '产品不存在'
      });
    }

    res.json({
      success: true,
      data: product
    });
  } catch (error) {
    next(error);
  }
};

// 创建产品
const createProduct = async (req, res, next) => {
  try {
    const productData = {
      ...req.body,
      product_code: generateProductCode()
    };

    const product = await Product.create(productData);

    res.status(201).json({
      success: true,
      message: '产品创建成功',
      data: product
    });
  } catch (error) {
    next(error);
  }
};

// 更新产品
const updateProduct = async (req, res, next) => {
  try {
    const { id } = req.params;
    const updateData = req.body;

    const product = await Product.findOne({
      where: { id, is_active: true }
    });

    if (!product) {
      return res.status(404).json({
        success: false,
        message: '产品不存在'
      });
    }

    await product.update(updateData);

    res.json({
      success: true,
      message: '产品更新成功',
      data: product
    });
  } catch (error) {
    next(error);
  }
};

// 删除产品（软删除）
const deleteProduct = async (req, res, next) => {
  try {
    const { id } = req.params;

    const product = await Product.findOne({
      where: { id, is_active: true }
    });

    if (!product) {
      return res.status(404).json({
        success: false,
        message: '产品不存在'
      });
    }

    await product.update({ is_active: false });

    res.json({
      success: true,
      message: '产品删除成功'
    });
  } catch (error) {
    next(error);
  }
};

// 批量删除产品
const batchDeleteProducts = async (req, res, next) => {
  try {
    const { ids } = req.body;

    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({
        success: false,
        message: '请提供要删除的产品ID列表'
      });
    }

    await Product.update(
      { is_active: false },
      { where: { id: { [Op.in]: ids }, is_active: true } }
    );

    res.json({
      success: true,
      message: `成功删除 ${ids.length} 个产品`
    });
  } catch (error) {
    next(error);
  }
};

// 获取产品统计信息
const getProductStats = async (req, res, next) => {
  try {
    const totalProducts = await Product.count({
      where: { is_active: true }
    });

    const activeProducts = await Product.count({
      where: { status: 'bulk', is_active: true }
    });

    const lowStockProducts = await Product.count({
      where: {
        current_stock: { [Op.lte]: Product.sequelize.col('safety_stock') },
        is_active: true
      }
    });

    const categoryStats = await Product.findAll({
      where: { is_active: true },
      attributes: [
        'category',
        [Product.sequelize.fn('COUNT', Product.sequelize.col('id')), 'count']
      ],
      group: ['category']
    });

    const statusStats = await Product.findAll({
      where: { is_active: true },
      attributes: [
        'status',
        [Product.sequelize.fn('COUNT', Product.sequelize.col('id')), 'count']
      ],
      group: ['status']
    });

    res.json({
      success: true,
      data: {
        totalProducts,
        activeProducts,
        lowStockProducts,
        categoryStats,
        statusStats
      }
    });
  } catch (error) {
    next(error);
  }
};

// 获取产品选项（用于下拉选择）
const getProductOptions = async (req, res, next) => {
  try {
    const products = await Product.findAll({
      where: { is_active: true, status: { [Op.in]: ['bulk', 'order'] } },
      attributes: ['id', 'product_code', 'product_name', 'product_name_en', 'unit', 'sale_price'],
      order: [['product_name', 'ASC']]
    });

    res.json({
      success: true,
      data: products
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getProducts,
  getProduct,
  createProduct,
  updateProduct,
  deleteProduct,
  batchDeleteProducts,
  getProductStats,
  getProductOptions
};