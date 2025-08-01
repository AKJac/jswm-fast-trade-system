# 极速外贸管理系统 - 后端API

鸿思特科技有限公司开发的外贸管理系统后端API服务，基于Node.js + Express + MySQL构建。

## 项目信息

- **公司名称**: 鸿思特科技有限公司
- **联系邮箱**: hst_co@163.com
- **联系人**: 陈志成
- **联系电话**: 17600198256

## 技术栈

- **后端框架**: Node.js + Express.js
- **数据库**: MySQL 8.0+
- **ORM**: Sequelize
- **身份认证**: JWT
- **文件上传**: Multer
- **PDF生成**: Puppeteer
- **Excel处理**: XLSX
- **日志**: Morgan
- **安全**: Helmet + CORS

## 功能模块

### 已实现功能

1. **客户管理模块**
   - 客户列表查询（支持分页、搜索、筛选）
   - 客户详情查看
   - 客户信息增删改查
   - 客户名称查重
   - 客户统计信息

2. **产品管理模块**
   - 产品列表查询（支持分页、搜索、筛选）
   - 产品详情查看
   - 产品信息增删改查
   - 产品统计信息
   - 产品选项接口（用于下拉选择）

3. **报价管理模块**
   - 报价单列表查询
   - 报价单详情查看
   - 报价单创建和编辑
   - 报价单明细管理
   - 金额自动计算
   - 报价单统计信息

### 待实现功能

4. **订单管理模块**
5. **供应商管理模块**
6. **库存管理模块**
7. **财务管理模块**
8. **系统管理模块**
9. **统计分析模块**
10. **文件上传功能**

## 项目结构

```
backend/
├── app.js                  # 应用入口文件
├── package.json           # 项目配置文件
├── .env.example          # 环境变量示例
├── config/
│   └── database.js       # 数据库配置
├── models/               # 数据模型
│   ├── index.js         # 模型索引
│   ├── Customer.js      # 客户模型
│   ├── Product.js       # 产品模型
│   ├── Supplier.js      # 供应商模型
│   ├── Quotation.js     # 报价单模型
│   └── QuotationItem.js # 报价单明细模型
├── controllers/          # 控制器
│   ├── customerController.js
│   ├── productController.js
│   └── quotationController.js
├── routes/              # 路由
│   ├── index.js        # 主路由
│   ├── customers.js    # 客户路由
│   ├── products.js     # 产品路由
│   └── quotations.js   # 报价单路由
├── middleware/          # 中间件
│   └── errorHandler.js # 错误处理
├── utils/              # 工具函数
│   └── initDatabase.js # 数据库初始化
└── uploads/            # 文件上传目录
```

## 快速开始

### 环境要求

- Node.js 16.0+
- MySQL 8.0+
- npm 或 yarn

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd backend
```

2. **安装依赖**
```bash
npm install
```

3. **配置环境变量**
```bash
cp .env.example .env
# 编辑 .env 文件，配置数据库连接等信息
```

4. **创建数据库**
```sql
CREATE DATABASE jisu_foreign_trade CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

5. **启动开发服务器**
```bash
npm run dev
```

6. **访问API**
```
http://localhost:3000/api
```

### 生产环境部署

1. **构建项目**
```bash
npm install --production
```

2. **启动服务**
```bash
npm start
```

## API接口文档

### 基础信息

- **Base URL**: `http://localhost:3000/api`
- **Content-Type**: `application/json`
- **响应格式**: 统一JSON格式

### 响应格式

#### 成功响应
```json
{
  "success": true,
  "data": {},
  "message": "操作成功"
}
```

#### 错误响应
```json
{
  "success": false,
  "message": "错误信息",
  "errors": []
}
```

### 客户管理API

#### 获取客户列表
```
GET /api/customers
```

**查询参数**:
- `page`: 页码（默认：1）
- `limit`: 每页数量（默认：10）
- `customerName`: 客户名称（模糊搜索）
- `country`: 国家/地区
- `level`: 客户等级
- `status`: 客户状态

#### 获取客户详情
```
GET /api/customers/:id
```

#### 创建客户
```
POST /api/customers
```

#### 更新客户
```
PUT /api/customers/:id
```

#### 删除客户
```
DELETE /api/customers/:id
```

### 产品管理API

#### 获取产品列表
```
GET /api/products
```

#### 获取产品详情
```
GET /api/products/:id
```

#### 创建产品
```
POST /api/products
```

#### 更新产品
```
PUT /api/products/:id
```

#### 删除产品
```
DELETE /api/products/:id
```

### 报价管理API

#### 获取报价单列表
```
GET /api/quotations
```

#### 获取报价单详情
```
GET /api/quotations/:id
```

#### 创建报价单
```
POST /api/quotations
```

#### 更新报价单
```
PUT /api/quotations/:id
```

#### 删除报价单
```
DELETE /api/quotations/:id
```

## 数据库设计

### 主要数据表

1. **customers** - 客户表
2. **products** - 产品表
3. **suppliers** - 供应商表
4. **quotations** - 报价单表
5. **quotation_items** - 报价单明细表

### 编号规则

- **客户编号**: CYYYYMMDDXXX（C + 年月日 + 3位随机数）
- **产品货号**: PYYYYMMDDXXX（P + 年月日 + 3位随机数）
- **供应商编号**: SYYYYMMDDXXX（S + 年月日 + 3位随机数）
- **报价单号**: QYYYYMMDDXXX（Q + 年月日 + 3位随机数）

## 开发规范

### 代码规范

1. 使用ES6+语法
2. 统一使用async/await处理异步
3. 错误处理使用try-catch
4. 数据验证使用Joi
5. 数据库事务处理复杂操作

### API设计规范

1. RESTful API设计
2. 统一响应格式
3. 合理的HTTP状态码
4. 详细的错误信息
5. 分页查询支持

### 安全规范

1. 输入验证和过滤
2. SQL注入防护
3. XSS攻击防护
4. CSRF攻击防护
5. 请求频率限制

## 错误处理

系统提供完整的错误处理机制：

- **400**: 请求参数错误
- **401**: 未授权访问
- **403**: 权限不足
- **404**: 资源不存在
- **409**: 数据冲突
- **500**: 服务器内部错误

## 日志系统

- 开发环境：详细日志输出
- 生产环境：错误日志记录
- 访问日志：Morgan中间件记录

## 性能优化

1. 数据库连接池
2. 查询优化和索引
3. 响应压缩
4. 静态资源缓存
5. 分页查询

## 监控和维护

1. 健康检查接口：`GET /health`
2. 数据库连接监控
3. API响应时间监控
4. 错误日志监控

## 版本历史

### v1.0.0 (当前版本)
- 基础框架搭建
- 客户管理模块
- 产品管理模块
- 报价管理模块
- 数据库设计和初始化

## 联系我们

如有问题或建议，请联系：

- **邮箱**: hst_co@163.com
- **电话**: 17600198256
- **联系人**: 陈志成

---

© 2024 鸿思特科技有限公司 版权所有