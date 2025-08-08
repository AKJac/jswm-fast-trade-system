-- 极速外贸管理系统数据库初始化脚本
-- 鸿思特科技有限公司
-- 联系邮箱：hst_co@163.com
-- 联系电话：17600198256

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `jisu_foreign_trade` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `jisu_foreign_trade`;

-- 客户表
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `customer_no` varchar(50) NOT NULL COMMENT '客户编号',
  `customer_name` varchar(200) NOT NULL COMMENT '客户名称',
  `customer_type` enum('foreign_trade','wholesale','retail','agent','other') DEFAULT 'foreign_trade' COMMENT '客户类型',
  `customer_source` enum('website','exhibition','referral','search_engine','social_media','other') DEFAULT 'website' COMMENT '客户来源',
  `contact` varchar(100) DEFAULT NULL COMMENT '联系人',
  `position` varchar(100) DEFAULT NULL COMMENT '职位',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) DEFAULT NULL COMMENT '电话',
  `country` varchar(10) DEFAULT NULL COMMENT '国家/地区代码',
  `level` enum('A','B','C','D') DEFAULT 'C' COMMENT '客户等级',
  `status` enum('potential','active','inactive','lost') DEFAULT 'potential' COMMENT '客户状态',
  `progress` enum('initial','understanding','proposal','quotation','negotiation','deal','service') DEFAULT 'initial' COMMENT '跟进进度',
  `company_address` text COMMENT '公司地址',
  `website` varchar(200) DEFAULT NULL COMMENT '公司网站',
  `main_products` text COMMENT '主要产品',
  `annual_revenue` decimal(15,2) DEFAULT NULL COMMENT '年营业额',
  `employee_count` int DEFAULT NULL COMMENT '员工数量',
  `established_date` datetime DEFAULT NULL COMMENT '成立时间',
  `cooperation_start_date` datetime DEFAULT NULL COMMENT '合作开始时间',
  `credit_rating` enum('AAA','AA','A','BBB','BB','B','CCC','CC','C','D') DEFAULT NULL COMMENT '信用等级',
  `payment_terms` varchar(100) DEFAULT NULL COMMENT '付款条件',
  `notes` text COMMENT '备注',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标志',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_no` (`customer_no`),
  KEY `idx_customer_name` (`customer_name`),
  KEY `idx_customer_type` (`customer_type`),
  KEY `idx_level` (`level`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户表';

-- 产品表
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `product_code` varchar(50) NOT NULL COMMENT '产品货号',
  `category` enum('knife','tool','accessory','packaging','other') DEFAULT 'knife' COMMENT '产品分类',
  `product_name` varchar(200) NOT NULL COMMENT '产品名称(中文)',
  `product_name_en` varchar(200) DEFAULT NULL COMMENT '产品名称(英文)',
  `unit` enum('piece','set','box','carton','meter','kg','ton','other') DEFAULT 'piece' COMMENT '单位',
  `status` enum('order','bulk','discontinued','developing') DEFAULT 'bulk' COMMENT '产品状态',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `supplier_item_no` varchar(100) DEFAULT NULL COMMENT '供应商货号',
  `blade_color` enum('silver','black','gold','blue','red','other') DEFAULT NULL COMMENT '刃色',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格',
  `handle_color` enum('black','brown','red','blue','green','other') DEFAULT NULL COMMENT '柄色',
  `custom_field` varchar(200) DEFAULT NULL COMMENT '自定义参数',
  `dimensions` varchar(100) DEFAULT NULL COMMENT '尺寸',
  `weight` decimal(10,3) DEFAULT NULL COMMENT '重量(kg)',
  `material` varchar(100) DEFAULT NULL COMMENT '材质',
  `packaging` varchar(200) DEFAULT NULL COMMENT '包装',
  `description_cn` text COMMENT '产品中文描述',
  `description_en` text COMMENT '产品英文描述',
  `sale_price` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `purchase_price` decimal(10,2) DEFAULT NULL COMMENT '采购价格',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价格',
  `currency` enum('CNY','USD','EUR','GBP','JPY') DEFAULT 'CNY' COMMENT '货币',
  `current_stock` int DEFAULT '0' COMMENT '当前库存',
  `safety_stock` int DEFAULT '0' COMMENT '安全库存',
  `stock_location` varchar(100) DEFAULT NULL COMMENT '库存位置',
  `images` json DEFAULT NULL COMMENT '产品图片URL数组',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标志',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code` (`product_code`),
  KEY `idx_product_name` (`product_name`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_brand` (`brand`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

-- 供应商表
DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `supplier_no` varchar(50) NOT NULL COMMENT '供应商编号',
  `supplier_name` varchar(200) NOT NULL COMMENT '供应商名称',
  `company_type` enum('manufacturer','trader','agent','service','other') DEFAULT 'manufacturer' COMMENT '公司类型',
  `contact` varchar(100) DEFAULT NULL COMMENT '联系人',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) DEFAULT NULL COMMENT '电话',
  `address` text COMMENT '地址',
  `main_products` text COMMENT '主要产品',
  `credit_rating` enum('AAA','AA','A','BBB','BB','B','CCC','CC','C','D') DEFAULT NULL COMMENT '信用等级',
  `payment_terms` varchar(100) DEFAULT NULL COMMENT '付款条件',
  `enable_quotation_api` tinyint(1) DEFAULT '0' COMMENT '对接报价单开关',
  `enable_sales_api` tinyint(1) DEFAULT '0' COMMENT '对接销售单开关',
  `api_endpoint` varchar(500) DEFAULT NULL COMMENT 'API接口地址',
  `notes` text COMMENT '备注',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标志',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_no` (`supplier_no`),
  KEY `idx_supplier_name` (`supplier_name`),
  KEY `idx_company_type` (`company_type`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商表';

-- 报价单表
DROP TABLE IF EXISTS `quotations`;
CREATE TABLE `quotations` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报价单ID',
  `quotation_no` varchar(50) NOT NULL COMMENT '报价单号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `customer_name` varchar(200) DEFAULT NULL COMMENT '客户名称',
  `contact` varchar(100) DEFAULT NULL COMMENT '联系人',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) DEFAULT NULL COMMENT '电话',
  `quotation_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '报价日期',
  `valid_until` datetime DEFAULT NULL COMMENT '有效期至',
  `currency` enum('CNY','USD','EUR','GBP','JPY') DEFAULT 'USD' COMMENT '货币',
  `subtotal` decimal(15,2) DEFAULT '0.00' COMMENT '小计金额',
  `discount_rate` decimal(5,2) DEFAULT '0.00' COMMENT '折扣率(%)',
  `discount_amount` decimal(15,2) DEFAULT '0.00' COMMENT '折扣金额',
  `total_amount` decimal(15,2) DEFAULT '0.00' COMMENT '总金额',
  `payment_terms` varchar(200) DEFAULT NULL COMMENT '付款条件',
  `delivery_terms` varchar(200) DEFAULT NULL COMMENT '交货条件',
  `special_terms` text COMMENT '特殊条款',
  `notes` text COMMENT '备注',
  `status` enum('draft','sent','accepted','rejected','expired') DEFAULT 'draft' COMMENT '状态',
  `created_by` bigint DEFAULT NULL COMMENT '创建者ID',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标志',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_quotation_no` (`quotation_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_status` (`status`),
  KEY `idx_quotation_date` (`quotation_date`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报价单表';

-- 报价单明细表
DROP TABLE IF EXISTS `quotation_items`;
CREATE TABLE `quotation_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `quotation_id` bigint NOT NULL COMMENT '报价单ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `product_code` varchar(50) DEFAULT NULL COMMENT '产品货号',
  `product_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
  `product_name_en` varchar(200) DEFAULT NULL COMMENT '产品英文名称',
  `specification` varchar(200) DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `quantity` int NOT NULL COMMENT '数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `total_price` decimal(15,2) NOT NULL COMMENT '总价',
  `notes` text COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_quotation_id` (`quotation_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报价单明细表';

-- 插入示例数据
INSERT INTO `customers` (`customer_no`, `customer_name`, `customer_type`, `customer_source`, `contact`, `position`, `email`, `phone`, `country`, `level`, `status`, `progress`) VALUES
('C20241201001', 'ABC Trading Company', 'foreign_trade', 'website', 'John Smith', 'Purchasing Manager', 'john@abctrading.com', '+1-555-0123', 'US', 'A', 'active', 'deal'),
('C20241201002', '德国工具有限公司', 'wholesale', 'exhibition', 'Hans Mueller', 'CEO', 'hans@germantools.de', '+49-30-12345678', 'EU', 'B', 'active', 'negotiation'),
('C20241201003', '日本贸易株式会社', 'foreign_trade', 'referral', '田中太郎', '部长', 'tanaka@japantrade.co.jp', '+81-3-1234-5678', 'JP', 'A', 'active', 'service');

INSERT INTO `products` (`product_code`, `category`, `product_name`, `product_name_en`, `unit`, `status`, `brand`, `blade_color`, `specification`, `handle_color`, `sale_price`, `purchase_price`, `cost_price`, `currency`, `current_stock`, `safety_stock`) VALUES
('P20241201001', 'knife', '多功能厨房刀', 'Multi-function Kitchen Knife', 'piece', 'bulk', '鸿思特', 'silver', '20cm', 'black', 25.00, 15.00, 12.00, 'USD', 1000, 100),
('P20241201002', 'tool', '精密螺丝刀套装', 'Precision Screwdriver Set', 'set', 'bulk', '鸿思特', NULL, '12件套', NULL, 35.00, 20.00, 16.00, 'USD', 500, 50),
('P20241201003', 'knife', '专业切片刀', 'Professional Slicing Knife', 'piece', 'bulk', '鸿思特', 'black', '25cm', 'brown', 45.00, 28.00, 22.00, 'USD', 300, 30);

INSERT INTO `suppliers` (`supplier_no`, `supplier_name`, `company_type`, `contact`, `email`, `phone`, `address`, `main_products`, `credit_rating`, `payment_terms`) VALUES
('S20241201001', '深圳精工制造有限公司', 'manufacturer', '李经理', 'li@jinggong.com', '0755-12345678', '深圳市宝安区工业园', '刀具、工具制造', 'AA', '30天付款'),
('S20241201002', '广州五金贸易公司', 'trader', '王总', 'wang@wujin.com', '020-87654321', '广州市白云区商贸城', '五金工具批发', 'A', '货到付款'),
('S20241201003', '东莞精密工具厂', 'manufacturer', '张厂长', 'zhang@precision.com', '0769-11111111', '东莞市长安镇工业区', '精密工具生产', 'AAA', '预付30%，余款60天');

-- 创建索引
CREATE INDEX idx_customers_deleted ON customers(deleted);
CREATE INDEX idx_products_deleted ON products(deleted);
CREATE INDEX idx_suppliers_deleted ON suppliers(deleted);
CREATE INDEX idx_quotations_deleted ON quotations(deleted);

COMMIT;