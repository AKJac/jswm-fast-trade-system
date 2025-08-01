const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const Quotation = sequelize.define('Quotation', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  quotation_no: {
    type: DataTypes.STRING(50),
    unique: true,
    allowNull: false,
    comment: '报价单号'
  },
  customer_id: {
    type: DataTypes.INTEGER,
    allowNull: false,
    comment: '客户ID'
  },
  customer_name: {
    type: DataTypes.STRING(200),
    comment: '客户名称'
  },
  contact: {
    type: DataTypes.STRING(100),
    comment: '联系人'
  },
  email: {
    type: DataTypes.STRING(200),
    comment: '邮箱'
  },
  phone: {
    type: DataTypes.STRING(50),
    comment: '电话'
  },
  quotation_date: {
    type: DataTypes.DATE,
    allowNull: false,
    defaultValue: DataTypes.NOW,
    comment: '报价日期'
  },
  valid_until: {
    type: DataTypes.DATE,
    comment: '有效期至'
  },
  currency: {
    type: DataTypes.ENUM('CNY', 'USD', 'EUR', 'GBP', 'JPY'),
    defaultValue: 'USD',
    comment: '货币'
  },
  subtotal: {
    type: DataTypes.DECIMAL(15, 2),
    defaultValue: 0,
    comment: '小计金额'
  },
  discount_rate: {
    type: DataTypes.DECIMAL(5, 2),
    defaultValue: 0,
    comment: '折扣率(%)'
  },
  discount_amount: {
    type: DataTypes.DECIMAL(15, 2),
    defaultValue: 0,
    comment: '折扣金额'
  },
  total_amount: {
    type: DataTypes.DECIMAL(15, 2),
    defaultValue: 0,
    comment: '总金额'
  },
  payment_terms: {
    type: DataTypes.STRING(200),
    comment: '付款条件'
  },
  delivery_terms: {
    type: DataTypes.STRING(200),
    comment: '交货条件'
  },
  special_terms: {
    type: DataTypes.TEXT,
    comment: '特殊条款'
  },
  notes: {
    type: DataTypes.TEXT,
    comment: '备注'
  },
  status: {
    type: DataTypes.ENUM('draft', 'sent', 'accepted', 'rejected', 'expired'),
    defaultValue: 'draft',
    comment: '状态'
  },
  created_by: {
    type: DataTypes.INTEGER,
    comment: '创建者ID'
  },
  is_active: {
    type: DataTypes.BOOLEAN,
    defaultValue: true,
    comment: '是否启用'
  }
}, {
  tableName: 'quotations',
  comment: '报价单表'
});

module.exports = Quotation;