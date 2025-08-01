const { sequelize } = require('../config/database');
const models = require('../models');

const initializeDatabase = async () => {
  try {
    console.log('开始初始化数据库...');
    
    // 同步数据库表结构
    await sequelize.sync({ alter: true });
    console.log('数据库表结构同步完成');
    
    // 检查是否需要初始化数据
    const customerCount = await models.Customer.count();
    if (customerCount === 0) {
      await initSampleData();
    }
    
    console.log('数据库初始化完成');
  } catch (error) {
    console.error('数据库初始化失败:', error);
    throw error;
  }
};

const initSampleData = async () => {
  try {
    console.log('开始初始化示例数据...');
    
    // 创建示例客户
    const customers = [
      {
        customer_no: generateCustomerNo(),
        customer_name: 'ABC Trading Company',
        customer_type: 'foreign_trade',
        customer_source: 'website',
        contact: 'John Smith',
        position: 'Purchasing Manager',
        email: 'john@abctrading.com',
        phone: '+1-555-0123',
        country: 'US',
        level: 'A',
        status: 'active',
        progress: 'deal'
      },
      {
        customer_no: generateCustomerNo(),
        customer_name: '德国工具有限公司',
        customer_type: 'wholesale',
        customer_source: 'exhibition',
        contact: 'Hans Mueller',
        position: 'CEO',
        email: 'hans@germantools.de',
        phone: '+49-30-12345678',
        country: 'EU',
        level: 'B',
        status: 'active',
        progress: 'negotiation'
      }
    ];
    
    await models.Customer.bulkCreate(customers);
    
    // 创建示例产品
    const products = [
      {
        product_code: generateProductCode(),
        category: 'knife',
        product_name: '多功能厨房刀',
        product_name_en: 'Multi-function Kitchen Knife',
        unit: 'piece',
        status: 'bulk',
        brand: '鸿思特',
        blade_color: 'silver',
        specification: '20cm',
        handle_color: 'black',
        sale_price: 25.00,
        purchase_price: 15.00,
        cost_price: 12.00,
        currency: 'USD',
        current_stock: 1000,
        safety_stock: 100
      },
      {
        product_code: generateProductCode(),
        category: 'tool',
        product_name: '精密螺丝刀套装',
        product_name_en: 'Precision Screwdriver Set',
        unit: 'set',
        status: 'bulk',
        brand: '鸿思特',
        specification: '12件套',
        sale_price: 35.00,
        purchase_price: 20.00,
        cost_price: 16.00,
        currency: 'USD',
        current_stock: 500,
        safety_stock: 50
      }
    ];
    
    await models.Product.bulkCreate(products);
    
    // 创建示例供应商
    const suppliers = [
      {
        supplier_no: generateSupplierNo(),
        supplier_name: '深圳精工制造有限公司',
        company_type: 'manufacturer',
        contact: '李经理',
        email: 'li@jinggong.com',
        phone: '0755-12345678',
        address: '深圳市宝安区工业园',
        main_products: '刀具、工具制造',
        credit_rating: 'AA',
        payment_terms: '30天付款'
      }
    ];
    
    await models.Supplier.bulkCreate(suppliers);
    
    console.log('示例数据初始化完成');
  } catch (error) {
    console.error('示例数据初始化失败:', error);
    throw error;
  }
};

// 生成客户编号
const generateCustomerNo = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0');
  return `C${year}${month}${day}${random}`;
};

// 生成产品编号
const generateProductCode = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0');
  return `P${year}${month}${day}${random}`;
};

// 生成供应商编号
const generateSupplierNo = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const random = String(Math.floor(Math.random() * 1000)).padStart(3, '0');
  return `S${year}${month}${day}${random}`;
};

module.exports = { initializeDatabase };