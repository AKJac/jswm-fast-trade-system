const { Customer } = require('../models');
const { Op } = require('sequelize');
const moment = require('moment');

// 生成客户编号
const generateCustomerNo = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0');
  return `C${year}${month}${day}${random}`;
};

// 获取客户列表
const getCustomers = async (req, res, next) => {
  try {
    const {
      page = 1,
      limit = 10,
      customerName,
      country,
      level,
      status,
      progress,
      customerType,
      customerSource
    } = req.query;

    const offset = (page - 1) * limit;
    const where = { is_active: true };

    // 构建查询条件
    if (customerName) {
      where.customer_name = { [Op.like]: `%${customerName}%` };
    }
    if (country) {
      where.country = country;
    }
    if (level) {
      where.level = level;
    }
    if (status) {
      where.status = status;
    }
    if (progress) {
      where.progress = progress;
    }
    if (customerType) {
      where.customer_type = customerType;
    }
    if (customerSource) {
      where.customer_source = customerSource;
    }

    const { count, rows } = await Customer.findAndCountAll({
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

// 获取客户详情
const getCustomer = async (req, res, next) => {
  try {
    const { id } = req.params;
    
    const customer = await Customer.findOne({
      where: { id, is_active: true }
    });

    if (!customer) {
      return res.status(404).json({
        success: false,
        message: '客户不存在'
      });
    }

    res.json({
      success: true,
      data: customer
    });
  } catch (error) {
    next(error);
  }
};

// 创建客户
const createCustomer = async (req, res, next) => {
  try {
    const customerData = {
      ...req.body,
      customer_no: generateCustomerNo()
    };

    const customer = await Customer.create(customerData);

    res.status(201).json({
      success: true,
      message: '客户创建成功',
      data: customer
    });
  } catch (error) {
    next(error);
  }
};

// 更新客户
const updateCustomer = async (req, res, next) => {
  try {
    const { id } = req.params;
    const updateData = req.body;

    const customer = await Customer.findOne({
      where: { id, is_active: true }
    });

    if (!customer) {
      return res.status(404).json({
        success: false,
        message: '客户不存在'
      });
    }

    await customer.update(updateData);

    res.json({
      success: true,
      message: '客户更新成功',
      data: customer
    });
  } catch (error) {
    next(error);
  }
};

// 删除客户（软删除）
const deleteCustomer = async (req, res, next) => {
  try {
    const { id } = req.params;

    const customer = await Customer.findOne({
      where: { id, is_active: true }
    });

    if (!customer) {
      return res.status(404).json({
        success: false,
        message: '客户不存在'
      });
    }

    await customer.update({ is_active: false });

    res.json({
      success: true,
      message: '客户删除成功'
    });
  } catch (error) {
    next(error);
  }
};

// 批量删除客户
const batchDeleteCustomers = async (req, res, next) => {
  try {
    const { ids } = req.body;

    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({
        success: false,
        message: '请提供要删除的客户ID列表'
      });
    }

    await Customer.update(
      { is_active: false },
      { where: { id: { [Op.in]: ids }, is_active: true } }
    );

    res.json({
      success: true,
      message: `成功删除 ${ids.length} 个客户`
    });
  } catch (error) {
    next(error);
  }
};

// 检查客户名称重复
const checkCustomerName = async (req, res, next) => {
  try {
    const { customerName, excludeId } = req.query;

    if (!customerName) {
      return res.status(400).json({
        success: false,
        message: '请提供客户名称'
      });
    }

    const where = {
      customer_name: customerName,
      is_active: true
    };

    // 排除指定ID（用于编辑时检查）
    if (excludeId) {
      where.id = { [Op.ne]: excludeId };
    }

    const existingCustomer = await Customer.findOne({ where });

    res.json({
      success: true,
      data: {
        exists: !!existingCustomer,
        customer: existingCustomer
      }
    });
  } catch (error) {
    next(error);
  }
};

// 获取客户统计信息
const getCustomerStats = async (req, res, next) => {
  try {
    const totalCustomers = await Customer.count({
      where: { is_active: true }
    });

    const activeCustomers = await Customer.count({
      where: { status: 'active', is_active: true }
    });

    const potentialCustomers = await Customer.count({
      where: { status: 'potential', is_active: true }
    });

    const levelStats = await Customer.findAll({
      where: { is_active: true },
      attributes: [
        'level',
        [Customer.sequelize.fn('COUNT', Customer.sequelize.col('id')), 'count']
      ],
      group: ['level']
    });

    const typeStats = await Customer.findAll({
      where: { is_active: true },
      attributes: [
        'customer_type',
        [Customer.sequelize.fn('COUNT', Customer.sequelize.col('id')), 'count']
      ],
      group: ['customer_type']
    });

    res.json({
      success: true,
      data: {
        totalCustomers,
        activeCustomers,
        potentialCustomers,
        levelStats,
        typeStats
      }
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getCustomers,
  getCustomer,
  createCustomer,
  updateCustomer,
  deleteCustomer,
  batchDeleteCustomers,
  checkCustomerName,
  getCustomerStats
};