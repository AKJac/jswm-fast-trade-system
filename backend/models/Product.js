const { DataTypes } = require('sequelize');
const { sequelize } = require('../config/database');

const Product = sequelize.define('Product', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  product_code: {
    type: DataTypes.STRING(50),
    unique: true,
    allowNull: false,
    comment: '产品货号，格式：PYYYYMMDDXXX'
  },
  category: {
    type: DataTypes.ENUM('knife', 'tool', 'accessory', 'packaging', 'other'),
    allowNull: false,
    defaultValue: 'knife',
    comment: '产品分类'
  },
  product_name: {
    type: DataTypes.STRING(200),
    allowNull: false,
    comment: '产品名称(中文)'
  },
  product_name_en: {
    type: DataTypes.STRING(200),
    comment: '产品名称(英文)'
  },
  unit: {
    type: DataTypes.ENUM('piece', 'set', 'box', 'carton', 'meter', 'kg', 'ton', 'other'),
    allowNull: false,
    defaultValue: 'piece',
    comment: '单位'
  },
  status: {
    type: DataTypes.ENUM('order', 'bulk', 'discontinued', 'developing'),
    allowNull: false,
    defaultValue: 'bulk',
    comment: '产品状态'
  },
  brand: {
    type: DataTypes.STRING(100),
    comment: '品牌'
  },
  supplier_item_no: {
    type: DataTypes.STRING(100),
    comment: '供应商货号'
  },
  blade_color: {
    type: DataTypes.ENUM('silver', 'black', 'gold', 'blue', 'red', 'other'),
    comment: '刃色'
  },
  specification: {
    type: DataTypes.STRING(200),
    comment: '规格'
  },
  handle_color: {
    type: DataTypes.ENUM('black', 'brown', 'red', 'blue', 'green', 'other'),
    comment: '柄色'
  },
  custom_field: {
    type: DataTypes.STRING(200),
    comment: '自定义参数'
  },
  dimensions: {
    type: DataTypes.STRING(100),
    comment: '尺寸'
  },
  weight: {
    type: DataTypes.DECIMAL(10, 3),
    comment: '重量(kg)'
  },
  material: {
    type: DataTypes.STRING(100),
    comment: '材质'
  },
  packaging: {
    type: DataTypes.STRING(200),
    comment: '包装'
  },
  description_cn: {
    type: DataTypes.TEXT,
    comment: '产品中文描述'
  },
  description_en: {
    type: DataTypes.TEXT,
    comment: '产品英文描述'
  },
  sale_price: {
    type: DataTypes.DECIMAL(10, 2),
    comment: '销售价格'
  },
  purchase_price: {
    type: DataTypes.DECIMAL(10, 2),
    comment: '采购价格'
  },
  cost_price: {
    type: DataTypes.DECIMAL(10, 2),
    comment: '成本价格'
  },
  currency: {
    type: DataTypes.ENUM('CNY', 'USD', 'EUR', 'GBP', 'JPY'),
    defaultValue: 'CNY',
    comment: '货币'
  },
  current_stock: {
    type: DataTypes.INTEGER,
    defaultValue: 0,
    comment: '当前库存'
  },
  safety_stock: {
    type: DataTypes.INTEGER,
    defaultValue: 0,
    comment: '安全库存'
  },
  stock_location: {
    type: DataTypes.STRING(100),
    comment: '库存位置'
  },
  images: {
    type: DataTypes.JSON,
    comment: '产品图片URL数组'
  },
  is_active: {
    type: DataTypes.BOOLEAN,
    defaultValue: true,
    comment: '是否启用'
  }
}, {
  tableName: 'products',
  comment: '产品表',
  indexes: [
    {
      fields: ['product_code']
    },
    {
      fields: ['product_name']
    },
    {
      fields: ['category']
    },
    {
      fields: ['status']
    },
    {
      fields: ['brand']
    }
  ]
});

module.exports = Product;