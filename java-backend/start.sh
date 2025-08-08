#!/bin/bash

# 极速外贸管理系统启动脚本
# 鸿思特科技有限公司

echo "============================================="
echo "极速外贸管理系统启动脚本"
echo "鸿思特科技有限公司"
echo "联系邮箱：hst_co@163.com"
echo "联系电话：17600198256"
echo "============================================="

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "错误：未找到Java环境，请先安装JDK 17+"
    exit 1
fi

# 检查Maven环境
if ! command -v mvn &> /dev/null; then
    echo "错误：未找到Maven环境，请先安装Maven 3.6+"
    exit 1
fi

# 检查MySQL连接
echo "检查数据库连接..."
mysql -h localhost -P 3306 -u root -p -e "SELECT 1;" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "警告：无法连接到MySQL数据库，请确保数据库已启动并配置正确"
fi

# 编译项目
echo "编译项目..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "错误：项目编译失败"
    exit 1
fi

echo "编译完成！"

# 启动应用
echo "启动应用..."
mvn spring-boot:run

echo "应用已启动！"
echo "API文档地址：http://localhost:8080/api/doc.html"
echo "健康检查：http://localhost:8080/api/health"