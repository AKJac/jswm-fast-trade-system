package com.hongsite.jisu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongsite.jisu.entity.Quotation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 报价单数据访问层
 * 
 * @author 鸿思特科技有限公司
 */
@Mapper
public interface QuotationMapper extends BaseMapper<Quotation> {

    /**
     * 分页查询报价单列表（带条件）
     */
    IPage<Quotation> selectQuotationPage(Page<Quotation> page, @Param("params") Map<String, Object> params);

    /**
     * 检查报价单号是否存在
     */
    @Select("SELECT COUNT(*) FROM quotations WHERE quotation_no = #{quotationNo} AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    Integer checkQuotationNoExists(@Param("quotationNo") String quotationNo, @Param("excludeId") Long excludeId);

    /**
     * 获取报价单统计信息
     */
    List<Map<String, Object>> getQuotationStats();

    /**
     * 根据状态统计报价单
     */
    @Select("SELECT status, COUNT(*) as count FROM quotations WHERE deleted = 0 AND is_active = 1 GROUP BY status")
    List<Map<String, Object>> getQuotationStatusStats();

    /**
     * 根据货币统计报价单
     */
    @Select("SELECT currency, COUNT(*) as count, SUM(total_amount) as total_amount " +
            "FROM quotations WHERE deleted = 0 AND is_active = 1 GROUP BY currency")
    List<Map<String, Object>> getQuotationCurrencyStats();

    /**
     * 获取即将过期的报价单数量
     */
    @Select("SELECT COUNT(*) FROM quotations WHERE deleted = 0 AND is_active = 1 " +
            "AND status IN ('draft', 'sent') AND valid_until <= DATE_ADD(NOW(), INTERVAL 7 DAY)")
    Long getExpiringQuotationCount();

    /**
     * 获取客户报价单统计
     */
    @Select("SELECT customer_name, COUNT(*) as count, SUM(total_amount) as total_amount " +
            "FROM quotations WHERE deleted = 0 AND is_active = 1 " +
            "GROUP BY customer_id, customer_name ORDER BY total_amount DESC LIMIT 10")
    List<Map<String, Object>> getCustomerQuotationStats();

    /**
     * 根据客户ID获取报价单列表
     */
    @Select("SELECT id, quotation_no, quotation_date, valid_until, currency, total_amount, status " +
            "FROM quotations WHERE customer_id = #{customerId} AND deleted = 0 AND is_active = 1 " +
            "ORDER BY quotation_date DESC")
    List<Map<String, Object>> getQuotationsByCustomerId(@Param("customerId") Long customerId);
}