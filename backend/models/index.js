const Customer = require('./Customer');
const Product = require('./Product');
const Supplier = require('./Supplier');
const Quotation = require('./Quotation');
const QuotationItem = require('./QuotationItem');

// 定义模型关联关系
// 报价单与客户的关联
Quotation.belongsTo(Customer, {
  foreignKey: 'customer_id',
  as: 'customer'
});
Customer.hasMany(Quotation, {
  foreignKey: 'customer_id',
  as: 'quotations'
});

// 报价单与报价单明细的关联
Quotation.hasMany(QuotationItem, {
  foreignKey: 'quotation_id',
  as: 'items'
});
QuotationItem.belongsTo(Quotation, {
  foreignKey: 'quotation_id',
  as: 'quotation'
});

// 报价单明细与产品的关联
QuotationItem.belongsTo(Product, {
  foreignKey: 'product_id',
  as: 'product'
});
Product.hasMany(QuotationItem, {
  foreignKey: 'product_id',
  as: 'quotationItems'
});

module.exports = {
  Customer,
  Product,
  Supplier,
  Quotation,
  QuotationItem
};