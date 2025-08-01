const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const Customer = sequelize.define('Customer', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  customer_no: {
    type: DataTypes.STRING(50),
    unique: true,
    allowNull: false,
    comment: '客户编号，格式：CYYYYMMDDXXX'
  },
  customer_name: {
    type: DataTypes.STRING(200),
    allowNull: false,
    comment: '客户名称'
  },
  customer_type: {
    type: DataTypes.ENUM('foreign_trade', 'wholesale', 'retail', 'agent', 'other'),
    allowNull: false,
    defaultValue: 'foreign_trade',
    comment: '客户类型'
  },
  customer_source: {
    type: DataTypes.ENUM('website', 'exhibition', 'referral', 'search_engine', 'social_media', 'other'),
    allowNull: false,
    defaultValue: 'website',
    comment: '客户来源'
  },
  contact: {
    type: DataTypes.STRING(100),
    comment: '联系人'
  },
  position: {
    type: DataTypes.STRING(100),
    comment: '职位'
  },
  email: {
    type: DataTypes.STRING(200),
    validate: {
      isEmail: true
    },
    comment: '邮箱'
  },
  phone: {
    type: DataTypes.STRING(50),
    comment: '电话'
  },
  country: {
    type: DataTypes.STRING(10),
    comment: '国家/地区代码'
  },
  level: {
    type: DataTypes.ENUM('A', 'B', 'C', 'D'),
    defaultValue: 'C',
    comment: '客户等级'
  },
  status: {
    type: DataTypes.ENUM('potential', 'active', 'inactive', 'lost'),
    defaultValue: 'potential',
    comment: '客户状态'
  },
  progress: {
    type: DataTypes.ENUM('initial', 'understanding', 'proposal', 'quotation', 'negotiation', 'deal', 'service'),
    defaultValue: 'initial',
    comment: '跟进进度'
  },
  company_address: {
    type: DataTypes.TEXT,
    comment: '公司地址'
  },
  website: {
    type: DataTypes.STRING(200),
    comment: '公司网站'
  },
  main_products: {
    type: DataTypes.TEXT,
    comment: '主要产品'
  },
  annual_revenue: {
    type: DataTypes.DECIMAL(15, 2),
    comment: '年营业额'
  },
  employee_count: {
    type: DataTypes.INTEGER,
    comment: '员工数量'
  },
  established_date: {
    type: DataTypes.DATE,
    comment: '成立时间'
  },
  cooperation_start_date: {
    type: DataTypes.DATE,
    comment: '合作开始时间'
  },
  credit_rating: {
    type: DataTypes.ENUM('AAA', 'AA', 'A', 'BBB', 'BB', 'B', 'CCC', 'CC', 'C', 'D'),
    comment: '信用等级'
  },
  payment_terms: {
    type: DataTypes.STRING(100),
    comment: '付款条件'
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
  tableName: 'customers',
  comment: '客户表',
  indexes: [
    {
      fields: ['customer_no']
    },
    {
      fields: ['customer_name']
    },
    {
      fields: ['customer_type']
    },
    {
      fields: ['level']
    },
    {
      fields: ['status']
    }
  ]
});

module.exports = Customer;