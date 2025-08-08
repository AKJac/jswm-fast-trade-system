package com.hongsite.jisu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongsite.jisu.entity.QuotationItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 报价单明细数据访问层
 * 
 * @author 鸿思特科技有限公司
 */
@Mapper
public interface QuotationItemMapper extends BaseMapper<QuotationItem> {

    /**
     * 根据报价单ID获取明细列表
     */
    @Select("SELECT * FROM quotation_items WHERE quotation_id = #{quotationId} ORDER BY id")
    List<QuotationItem> getItemsByQuotationId(@Param("quotationId") Long quotationId);

    /**
     * 根据报价单ID删除所有明细
     */
    @Select("DELETE FROM quotation_items WHERE quotation_id = #{quotationId}")
    Integer deleteByQuotationId(@Param("quotationId") Long quotationId);

    /**
     * 获取产品报价统计
     */
    @Select("SELECT qi.product_code, qi.product_name, COUNT(*) as quote_count, " +
            "AVG(qi.unit_price) as avg_price, MAX(qi.unit_price) as max_price, MIN(qi.unit_price) as min_price " +
            "FROM quotation_items qi " +
            "INNER JOIN quotations q ON qi.quotation_id = q.id " +
            "WHERE q.deleted = 0 AND q.is_active = 1 " +
            "GROUP BY qi.product_id, qi.product_code, qi.product_name " +
            "ORDER BY quote_count DESC LIMIT 20")
    List<Map<String, Object>> getProductQuoteStats();

    /**
     * 获取热门报价产品
     */
    @Select("SELECT qi.product_code, qi.product_name, SUM(qi.quantity) as total_quantity, " +
            "COUNT(DISTINCT qi.quotation_id) as quotation_count " +
            "FROM quotation_items qi " +
            "INNER JOIN quotations q ON qi.quotation_id = q.id " +
            "WHERE q.deleted = 0 AND q.is_active = 1 AND q.quotation_date >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
            "GROUP BY qi.product_id, qi.product_code, qi.product_name " +
            "ORDER BY total_quantity DESC LIMIT 10")
    List<Map<String, Object>> getHotQuoteProducts();
}