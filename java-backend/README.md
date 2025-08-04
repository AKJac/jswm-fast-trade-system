# 极速外贸管理系统 - Java后端API

鸿思特科技有限公司开发的外贸管理系统后端API服务，基于Spring Boot + MySQL构建。

## 项目信息

- **公司名称**: 鸿思特科技有限公司
- **联系邮箱**: hst_co@163.com
- **联系人**: 陈志成
- **联系电话**: 17600198256

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0+
- **ORM框架**: MyBatis-Plus 3.5.4
- **连接池**: Druid 1.2.20
- **安全框架**: Spring Security + JWT
- **API文档**: Knife4j 4.3.0
- **工具库**: Hutool 5.8.22
- **Excel处理**: Apache POI 5.2.4
- **PDF处理**: iText 7.2.5

## 功能模块

### 已实现功能

1. **客户管理模块**
   - 客户列表查询（支持分页、搜索、筛选）
   - 客户详情查看
   - 客户信息增删改查
   - 客户名称查重
   - 客户统计信息
   - 自动生成客户编号

2. **产品管理模块**
   - 产品列表查询（支持分页、搜索、筛选）
   - 产品详情查看
   - 产品信息增删改查
   - 产品货号和名称查重
   - 产品统计信息
   - 产品选项接口（用于下拉选择）
   - 库存管理功能
   - 自动生成产品货号

3. **供应商管理模块**
   - 供应商列表查询（支持分页、搜索、筛选）
   - 供应商详情查看
   - 供应商信息增删改查
   - 供应商编号和名称查重
   - 供应商统计信息
   - 供应商选项接口（用于下拉选择）
   - 根据产品搜索供应商
   - API接口配置管理
   - 自动生成供应商编号

4. **文件上传模块**
   - 图片文件上传（产品图片、头像等）
   - Excel文件上传（批量导入数据）
   - 文档文件上传（PDF、Word等）
   - 文件类型和大小验证
   - 文件存储路径管理

5. **统计分析模块**
   - 仪表盘统计数据
   - 客户统计分析
   - 产品统计分析
   - 供应商统计分析
   - 系统运行概览

### 待实现功能

6. **报价管理模块**（部分完成 - 实体类已创建）
7. **订单管理模块**
8. **库存管理模块**
9. **财务管理模块**
10. **系统管理模块**（用户、角色、权限管理）

## 项目结构

```
java-backend/
├── src/main/java/com/hongsite/jisu/
│   ├── JisuForeignTradeApplication.java    # 主启动类
│   ├── common/                             # 通用类
│   │   ├── Result.java                     # 统一响应结果
│   │   └── PageResult.java                 # 分页响应结果
│   ├── entity/                             # 实体类
│   │   ├── Customer.java                   # 客户实体
│   │   ├── Product.java                    # 产品实体
│   │   ├── Supplier.java                   # 供应商实体
│   │   ├── Quotation.java                  # 报价单实体
│   │   └── QuotationItem.java              # 报价单明细实体
│   ├── mapper/                             # 数据访问层
│   │   ├── CustomerMapper.java             # 客户Mapper
│   │   ├── ProductMapper.java              # 产品Mapper
│   │   ├── SupplierMapper.java             # 供应商Mapper
│   │   ├── QuotationMapper.java            # 报价单Mapper
│   │   └── QuotationItemMapper.java        # 报价单明细Mapper
│   ├── service/                            # 服务层
│   │   ├── CustomerService.java            # 客户服务接口
│   │   ├── ProductService.java             # 产品服务接口
│   │   ├── SupplierService.java            # 供应商服务接口
│   │   └── impl/
│   │       ├── CustomerServiceImpl.java    # 客户服务实现
│   │       ├── ProductServiceImpl.java     # 产品服务实现
│   │       └── SupplierServiceImpl.java    # 供应商服务实现
│   ├── controller/                         # 控制层
│   │   ├── CustomerController.java         # 客户控制器
│   │   ├── ProductController.java          # 产品控制器
│   │   ├── SupplierController.java         # 供应商控制器
│   │   ├── FileUploadController.java       # 文件上传控制器
│   │   ├── StatisticsController.java       # 统计分析控制器
│   │   └── HealthController.java           # 健康检查控制器
│   └── config/                             # 配置类
│       ├── MybatisPlusConfig.java          # MyBatis-Plus配置
│       ├── CorsConfig.java                 # 跨域配置
│       └── GlobalExceptionHandler.java     # 全局异常处理
├── src/main/resources/
│   ├── application.yml                     # 配置文件
│   ├── sql/
│   │   └── init.sql                        # 数据库初始化脚本
│   └── mapper/                             # MyBatis映射文件
├── pom.xml                                 # Maven配置
└── README.md                               # 项目说明
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+ (可选)

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd java-backend
```

2. **创建数据库**
```sql
-- 执行 src/main/resources/sql/init.sql 脚本
mysql -u root -p < src/main/resources/sql/init.sql
```

3. **配置数据库连接**
```yaml
# 编辑 src/main/resources/application.yml
spring:
  datasource:
    druid:
      url: jdbc:mysql://localhost:3306/jisu_foreign_trade
      username: root
      password: your_password
```

4. **编译项目**
```bash
mvn clean compile
```

5. **启动应用**
```bash
# 使用Maven启动
mvn spring-boot:run

# 或者使用启动脚本
./start.sh
```

6. **访问API文档**
```
http://localhost:8080/api/doc.html
```

7. **健康检查**
```
http://localhost:8080/api/health
```

### 生产环境部署

1. **打包应用**
```bash
mvn clean package -Dmaven.test.skip=true
```

2. **运行JAR包**
```bash
java -jar target/jisu-foreign-trade-1.0.0.jar
```

## API接口文档

### 基础信息

- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **响应格式**: 统一JSON格式

### 响应格式

#### 成功响应
```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1701234567890
}
```

#### 错误响应
```json
{
  "success": false,
  "code": 500,
  "message": "错误信息",
  "data": null,
  "timestamp": 1701234567890
}
```

#### 分页响应
```json
{
  "success": true,
  "code": 200,
  "message": "操作成功",
  "data": {
    "list": [],
    "total": 100,
    "current": 1,
    "size": 10,
    "pages": 10
  },
  "timestamp": 1701234567890
}
```

### 客户管理API

#### 获取客户列表
```
GET /api/customers/page
```

**查询参数**:
- `current`: 页码（默认：1）
- `size`: 每页数量（默认：10）
- `customerName`: 客户名称（模糊搜索）
- `country`: 国家/地区
- `level`: 客户等级
- `status`: 客户状态
- `progress`: 跟进进度
- `customerType`: 客户类型
- `customerSource`: 客户来源

#### 获取客户详情
```
GET /api/customers/{id}
```

#### 创建客户
```
POST /api/customers
```

**请求体**:
```json
{
  "customerName": "ABC Trading Company",
  "customerType": "foreign_trade",
  "customerSource": "website",
  "contact": "John Smith",
  "position": "Purchasing Manager",
  "email": "john@abctrading.com",
  "phone": "+1-555-0123",
  "country": "US",
  "level": "A"
}
```

#### 更新客户
```
PUT /api/customers/{id}
```

#### 删除客户
```
DELETE /api/customers/{id}
```

#### 批量删除客户
```
POST /api/customers/batch-delete
```

**请求体**:
```json
[1, 2, 3]
```

#### 检查客户名称
```
GET /api/customers/check-name?customerName=ABC&excludeId=1
```

#### 获取客户统计
```
GET /api/customers/stats
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

### 数据库特性

- 使用UTF8MB4字符集，支持emoji等特殊字符
- 所有表支持逻辑删除
- 自动维护创建时间和更新时间
- 合理的索引设计，优化查询性能

## 开发规范

### 代码规范

1. 使用Java 17特性
2. 遵循阿里巴巴Java开发手册
3. 统一使用Lombok简化代码
4. 完整的参数验证和异常处理
5. 详细的接口文档注解

### API设计规范

1. RESTful API设计
2. 统一响应格式
3. 合理的HTTP状态码
4. 详细的错误信息
5. 分页查询支持

### 数据库规范

1. 表名使用复数形式
2. 字段名使用下划线命名
3. 必要的索引和约束
4. 完整的注释说明

## 配置说明

### 应用配置

```yaml
# 服务器配置
server:
  port: 8080
  servlet:
    context-path: /api

# 数据库配置
spring:
  datasource:
    druid:
      url: jdbc:mysql://localhost:3306/jisu_foreign_trade
      username: root
      password: 123456

# MyBatis-Plus配置
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

### 环境变量

- `DB_PASSWORD`: 数据库密码
- `JWT_SECRET`: JWT密钥
- `REDIS_PASSWORD`: Redis密码
- `MAIL_USERNAME`: 邮箱用户名
- `MAIL_PASSWORD`: 邮箱密码

## 监控和维护

### 监控端点

- **应用健康检查**: `GET /actuator/health`
- **数据库监控**: `http://localhost:8080/api/druid/`
- **API文档**: `http://localhost:8080/api/doc.html`

### 日志配置

- 开发环境：控制台输出详细日志
- 生产环境：文件输出，按日期滚动
- 错误日志单独记录

## 测试

### 单元测试

```bash
mvn test
```

### 集成测试

```bash
mvn verify
```

### API测试

使用Knife4j文档页面进行在线测试，或使用Postman等工具。

## 部署

### Docker部署

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/jisu-foreign-trade-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 传统部署

1. 上传JAR包到服务器
2. 配置环境变量
3. 启动应用：`java -jar jisu-foreign-trade-1.0.0.jar`

## 版本历史

### v1.0.0 (当前版本)
- 基础框架搭建
- 客户管理模块
- 数据库设计和初始化
- API文档集成
- 基础安全配置

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License

## 联系我们

如有问题或建议，请联系：

- **邮箱**: hst_co@163.com
- **电话**: 17600198256
- **联系人**: 陈志成

---

© 2024 鸿思特科技有限公司 版权所有