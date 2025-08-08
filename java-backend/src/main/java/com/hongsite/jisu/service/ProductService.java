package com.hongsite.jisu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * 产品服务层接口
 * 
 * @author 鸿思特科技有限公司
 */
public interface ProductService extends IService<Product> {

    /**
     * 分页查询产品列表
     */
    PageResult<Product> getProductPage(Long current, Long size, String productName, String productCode,
                                     String category, String status, String brand, String supplier);

    /**
     * 根据ID获取产品详情
     */
    Product getProductById(Long id);

    /**
     * 新增产品
     */
    Boolean saveProduct(Product product);

    /**
     * 更新产品
     */
    Boolean updateProduct(Product product);

    /**
     * 删除产品（逻辑删除）
     */
    Boolean deleteProduct(Long id);

    /**
     * 批量删除产品
     */
    Boolean batchDeleteProducts(List<Long> ids);

    /**
     * 检查产品货号是否存在
     */
    Boolean checkProductCodeExists(String productCode, Long excludeId);

    /**
     * 检查产品名称是否存在
     */
    Boolean checkProductNameExists(String productName, Long excludeId);

    /**
     * 获取产品统计信息
     */
    Map<String, Object> getProductStats();

    /**
     * 获取产品选项列表（用于下拉选择）
     */
    List<Map<String, Object>> getProductOptions();

    /**
     * 生成产品货号
     */
    String generateProductCode();

    /**
     * 更新产品库存
     */
    Boolean updateProductStock(Long productId, Integer quantity, String operation);
}