package com.hongsite.jisu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongsite.jisu.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 产品数据访问层
 * 
 * @author 鸿思特科技有限公司
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 分页查询产品列表（带条件）
     */
    IPage<Product> selectProductPage(Page<Product> page, @Param("params") Map<String, Object> params);

    /**
     * 检查产品货号是否存在
     */
    @Select("SELECT COUNT(*) FROM products WHERE product_code = #{productCode} AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    Integer checkProductCodeExists(@Param("productCode") String productCode, @Param("excludeId") Long excludeId);

    /**
     * 检查产品名称是否存在
     */
    @Select("SELECT COUNT(*) FROM products WHERE product_name = #{productName} AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    Integer checkProductNameExists(@Param("productName") String productName, @Param("excludeId") Long excludeId);

    /**
     * 获取产品统计信息
     */
    List<Map<String, Object>> getProductStats();

    /**
     * 根据产品分类统计
     */
    @Select("SELECT category, COUNT(*) as count FROM products WHERE deleted = 0 AND is_active = 1 GROUP BY category")
    List<Map<String, Object>> getProductCategoryStats();

    /**
     * 根据产品状态统计
     */
    @Select("SELECT status, COUNT(*) as count FROM products WHERE deleted = 0 AND is_active = 1 GROUP BY status")
    List<Map<String, Object>> getProductStatusStats();

    /**
     * 根据品牌统计
     */
    @Select("SELECT brand, COUNT(*) as count FROM products WHERE deleted = 0 AND is_active = 1 AND brand IS NOT NULL GROUP BY brand")
    List<Map<String, Object>> getProductBrandStats();

    /**
     * 获取低库存产品数量
     */
    @Select("SELECT COUNT(*) FROM products WHERE deleted = 0 AND is_active = 1 AND current_stock <= safety_stock")
    Long getLowStockProductCount();

    /**
     * 获取产品选项列表（用于下拉选择）
     */
    @Select("SELECT id, product_code, product_name, product_name_en, unit, sale_price, currency " +
            "FROM products WHERE deleted = 0 AND is_active = 1 AND status IN ('bulk', 'order') " +
            "ORDER BY product_name")
    List<Map<String, Object>> getProductOptions();
}