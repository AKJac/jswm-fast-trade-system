const { Quotation, QuotationItem, Customer, Product } = require('../models');
const { Op } = require('sequelize');
const { sequelize } = require('../config/database');

// 生成报价单号
const generateQuotationNo = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0');
  return `Q${year}${month}${day}${random}`;
};

// 获取报价单列表
const getQuotations = async (req, res, next) => {
  try {
    const {
      page = 1,
      limit = 10,
      quotationNo,
      customerName,
      status,
      startDate,
      endDate
    } = req.query;

    const offset = (page - 1) * limit;
    const where = { is_active: true };

    // 构建查询条件
    if (quotationNo) {
      where.quotation_no = { [Op.like]: `%${quotationNo}%` };
    }
    if (customerName) {
      where.customer_name = { [Op.like]: `%${customerName}%` };
    }
    if (status) {
      where.status = status;
    }
    if (startDate && endDate) {
      where.quotation_date = {
        [Op.between]: [new Date(startDate), new Date(endDate)]
      };
    }

    const { count, rows } = await Quotation.findAndCountAll({
      where,
      include: [
        {
          model: Customer,
          as: 'customer',
          attributes: ['id', 'customer_name', 'contact', 'email']
        }
      ],
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

// 获取报价单详情
const getQuotation = async (req, res, next) => {
  try {
    const { id } = req.params;
    
    const quotation = await Quotation.findOne({
      where: { id, is_active: true },
      include: [
        {
          model: Customer,
          as: 'customer',
          attributes: ['id', 'customer_name', 'contact', 'email', 'phone']
        },
        {
          model: QuotationItem,
          as: 'items',
          include: [
            {
              model: Product,
              as: 'product',
              attributes: ['id', 'product_name', 'product_name_en', 'specification']
            }
          ]
        }
      ]
    });

    if (!quotation) {
      return res.status(404).json({
        success: false,
        message: '报价单不存在'
      });
    }

    res.json({
      success: true,
      data: quotation
    });
  } catch (error) {
    next(error);
  }
};

// 创建报价单
const createQuotation = async (req, res, next) => {
  const transaction = await sequelize.transaction();
  
  try {
    const { items, ...quotationData } = req.body;

    // 获取客户信息
    const customer = await Customer.findByPk(quotationData.customer_id);
    if (!customer) {
      return res.status(400).json({
        success: false,
        message: '客户不存在'
      });
    }

    // 创建报价单
    const quotation = await Quotation.create({
      ...quotationData,
      quotation_no: generateQuotationNo(),
      customer_name: customer.customer_name,
      contact: customer.contact,
      email: customer.email,
      phone: customer.phone
    }, { transaction });

    // 创建报价单明细
    if (items && items.length > 0) {
      const quotationItems = items.map(item => ({
        ...item,
        quotation_id: quotation.id,
        total_price: item.quantity * item.unit_price
      }));

      await QuotationItem.bulkCreate(quotationItems, { transaction });

      // 计算总金额
      const subtotal = quotationItems.reduce((sum, item) => sum + parseFloat(item.total_price), 0);
      const discountAmount = subtotal * (quotation.discount_rate / 100);
      const totalAmount = subtotal - discountAmount;

      await quotation.update({
        subtotal,
        discount_amount: discountAmount,
        total_amount: totalAmount
      }, { transaction });
    }

    await transaction.commit();

    // 返回完整的报价单信息
    const newQuotation = await Quotation.findByPk(quotation.id, {
      include: [
        {
          model: Customer,
          as: 'customer'
        },
        {
          model: QuotationItem,
          as: 'items',
          include: [
            {
              model: Product,
              as: 'product'
            }
          ]
        }
      ]
    });

    res.status(201).json({
      success: true,
      message: '报价单创建成功',
      data: newQuotation
    });
  } catch (error) {
    await transaction.rollback();
    next(error);
  }
};

// 更新报价单
const updateQuotation = async (req, res, next) => {
  const transaction = await sequelize.transaction();
  
  try {
    const { id } = req.params;
    const { items, ...quotationData } = req.body;

    const quotation = await Quotation.findOne({
      where: { id, is_active: true }
    });

    if (!quotation) {
      return res.status(404).json({
        success: false,
        message: '报价单不存在'
      });
    }

    // 更新报价单基本信息
    await quotation.update(quotationData, { transaction });

    // 更新报价单明细
    if (items) {
      // 删除原有明细
      await QuotationItem.destroy({
        where: { quotation_id: id },
        transaction
      });

      // 创建新明细
      if (items.length > 0) {
        const quotationItems = items.map(item => ({
          ...item,
          quotation_id: id,
          total_price: item.quantity * item.unit_price
        }));

        await QuotationItem.bulkCreate(quotationItems, { transaction });

        // 重新计算总金额
        const subtotal = quotationItems.reduce((sum, item) => sum + parseFloat(item.total_price), 0);
        const discountAmount = subtotal * (quotation.discount_rate / 100);
        const totalAmount = subtotal - discountAmount;

        await quotation.update({
          subtotal,
          discount_amount: discountAmount,
          total_amount: totalAmount
        }, { transaction });
      }
    }

    await transaction.commit();

    // 返回更新后的报价单信息
    const updatedQuotation = await Quotation.findByPk(id, {
      include: [
        {
          model: Customer,
          as: 'customer'
        },
        {
          model: QuotationItem,
          as: 'items',
          include: [
            {
              model: Product,
              as: 'product'
            }
          ]
        }
      ]
    });

    res.json({
      success: true,
      message: '报价单更新成功',
      data: updatedQuotation
    });
  } catch (error) {
    await transaction.rollback();
    next(error);
  }
};

// 删除报价单
const deleteQuotation = async (req, res, next) => {
  try {
    const { id } = req.params;

    const quotation = await Quotation.findOne({
      where: { id, is_active: true }
    });

    if (!quotation) {
      return res.status(404).json({
        success: false,
        message: '报价单不存在'
      });
    }

    await quotation.update({ is_active: false });

    res.json({
      success: true,
      message: '报价单删除成功'
    });
  } catch (error) {
    next(error);
  }
};

// 获取报价单统计信息
const getQuotationStats = async (req, res, next) => {
  try {
    const totalQuotations = await Quotation.count({
      where: { is_active: true }
    });

    const pendingQuotations = await Quotation.count({
      where: { status: 'draft', is_active: true }
    });

    const acceptedQuotations = await Quotation.count({
      where: { status: 'accepted', is_active: true }
    });

    const statusStats = await Quotation.findAll({
      where: { is_active: true },
      attributes: [
        'status',
        [Quotation.sequelize.fn('COUNT', Quotation.sequelize.col('id')), 'count'],
        [Quotation.sequelize.fn('SUM', Quotation.sequelize.col('total_amount')), 'total_amount']
      ],
      group: ['status']
    });

    res.json({
      success: true,
      data: {
        totalQuotations,
        pendingQuotations,
        acceptedQuotations,
        statusStats
      }
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getQuotations,
  getQuotation,
  createQuotation,
  updateQuotation,
  deleteQuotation,
  getQuotationStats
};