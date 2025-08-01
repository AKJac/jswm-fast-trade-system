const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const QuotationItem = sequelize.define('QuotationItem', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  quotation_id: {
    type: DataTypes.INTEGER,
    allowNull: false,
    comment: '报价单ID'
  },
  product_id: {
    type: DataTypes.INTEGER,
    allowNull: false,
    comment: '产品ID'
  },
  product_code: {
    type: DataTypes.STRING(50),
    comment: '产品货号'
  },
  product_name: {
    type: DataTypes.STRING(200),
    comment: '产品名称'
  },
  product_name_en: {
    type: DataTypes.STRING(200),
    comment: '产品英文名称'
  },
  specification: {
    type: DataTypes.STRING(200),
    comment: '规格'
  },
  unit: {
    type: DataTypes.STRING(20),
    comment: '单位'
  },
  quantity: {
    type: DataTypes.INTEGER,
    allowNull: false,
    comment: '数量'
  },
  unit_price: {
    type: DataTypes.DECIMAL(10, 2),
    allowNull: false,
    comment: '单价'
  },
  total_price: {
    type: DataTypes.DECIMAL(15, 2),
    allowNull: false,
    comment: '总价'
  },
  notes: {
    type: DataTypes.TEXT,
    comment: '备注'
  }
}, {
  tableName: 'quotation_items',
  comment: '报价单明细表'
});

module.exports = QuotationItem;