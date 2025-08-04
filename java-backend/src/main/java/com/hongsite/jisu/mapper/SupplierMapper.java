package com.hongsite.jisu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongsite.jisu.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 供应商数据访问层
 * 
 * @author 鸿思特科技有限公司
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {

    /**
     * 分页查询供应商列表（带条件）
     */
    IPage<Supplier> selectSupplierPage(Page<Supplier> page, @Param("params") Map<String, Object> params);

    /**
     * 检查供应商编号是否存在
     */
    @Select("SELECT COUNT(*) FROM suppliers WHERE supplier_no = #{supplierNo} AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    Integer checkSupplierNoExists(@Param("supplierNo") String supplierNo, @Param("excludeId") Long excludeId);

    /**
     * 检查供应商名称是否存在
     */
    @Select("SELECT COUNT(*) FROM suppliers WHERE supplier_name = #{supplierName} AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    Integer checkSupplierNameExists(@Param("supplierName") String supplierName, @Param("excludeId") Long excludeId);

    /**
     * 获取供应商统计信息
     */
    List<Map<String, Object>> getSupplierStats();

    /**
     * 根据公司类型统计
     */
    @Select("SELECT company_type, COUNT(*) as count FROM suppliers WHERE deleted = 0 AND is_active = 1 GROUP BY company_type")
    List<Map<String, Object>> getSupplierTypeStats();

    /**
     * 根据信用等级统计
     */
    @Select("SELECT credit_rating, COUNT(*) as count FROM suppliers WHERE deleted = 0 AND is_active = 1 AND credit_rating IS NOT NULL GROUP BY credit_rating")
    List<Map<String, Object>> getSupplierCreditStats();

    /**
     * 获取启用API接口的供应商数量
     */
    @Select("SELECT COUNT(*) FROM suppliers WHERE deleted = 0 AND is_active = 1 AND (enable_quotation_api = 1 OR enable_sales_api = 1)")
    Long getApiEnabledSupplierCount();

    /**
     * 获取供应商选项列表（用于下拉选择）
     */
    @Select("SELECT id, supplier_no, supplier_name, company_type, contact, email, phone " +
            "FROM suppliers WHERE deleted = 0 AND is_active = 1 " +
            "ORDER BY supplier_name")
    List<Map<String, Object>> getSupplierOptions();

    /**
     * 根据主要产品搜索供应商
     */
    @Select("SELECT id, supplier_no, supplier_name, company_type, contact, main_products " +
            "FROM suppliers WHERE deleted = 0 AND is_active = 1 " +
            "AND main_products LIKE CONCAT('%', #{product}, '%') " +
            "ORDER BY supplier_name")
    List<Map<String, Object>> searchSuppliersByProduct(@Param("product") String product);
}