const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const Supplier = sequelize.define('Supplier', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  supplier_no: {
    type: DataTypes.STRING(50),
    unique: true,
    allowNull: false,
    comment: '供应商编号'
  },
  supplier_name: {
    type: DataTypes.STRING(200),
    allowNull: false,
    comment: '供应商名称'
  },
  company_type: {
    type: DataTypes.ENUM('manufacturer', 'trader', 'agent', 'service', 'other'),
    allowNull: false,
    defaultValue: 'manufacturer',
    comment: '公司类型'
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
  address: {
    type: DataTypes.TEXT,
    comment: '地址'
  },
  main_products: {
    type: DataTypes.TEXT,
    comment: '主要产品'
  },
  credit_rating: {
    type: DataTypes.ENUM('AAA', 'AA', 'A', 'BBB', 'BB', 'B', 'CCC', 'CC', 'C', 'D'),
    comment: '信用等级'
  },
  payment_terms: {
    type: DataTypes.STRING(100),
    comment: '付款条件'
  },
  enable_quotation_api: {
    type: DataTypes.BOOLEAN,
    defaultValue: false,
    comment: '对接报价单开关'
  },
  enable_sales_api: {
    type: DataTypes.BOOLEAN,
    defaultValue: false,
    comment: '对接销售单开关'
  },
  api_endpoint: {
    type: DataTypes.STRING(500),
    comment: 'API接口地址'
  },
  notes: {
    type: DataTypes.TEXT,
    comment: '备注'
  },
  is_active: {
    type: DataTypes.BOOLEAN,
    defaultValue: true,
    comment: '是否启用'
  }
}, {
  tableName: 'suppliers',
  comment: '供应商表'
});

module.exports = Supplier;