package com.hongsite.jisu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.entity.Quotation;

import java.util.List;
import java.util.Map;

/**
 * 报价单服务层接口
 * 
 * @author 鸿思特科技有限公司
 */
public interface QuotationService extends IService<Quotation> {

    /**
     * 分页查询报价单列表
     */
    PageResult<Quotation> getQuotationPage(Long current, Long size, String quotationNo, String customerName,
                                          String status, String currency, Long customerId);

    /**
     * 根据ID获取报价单详情（包含明细）
     */
    Quotation getQuotationById(Long id);

    /**
     * 新增报价单
     */
    Boolean saveQuotation(Quotation quotation);

    /**
     * 更新报价单
     */
    Boolean updateQuotation(Quotation quotation);

    /**
     * 删除报价单（逻辑删除）
     */
    Boolean deleteQuotation(Long id);

    /**
     * 批量删除报价单
     */
    Boolean batchDeleteQuotations(List<Long> ids);

    /**
     * 检查报价单号是否存在
     */
    Boolean checkQuotationNoExists(String quotationNo, Long excludeId);

    /**
     * 获取报价单统计信息
     */
    Map<String, Object> getQuotationStats();

    /**
     * 根据客户ID获取报价单列表
     */
    List<Map<String, Object>> getQuotationsByCustomerId(Long customerId);

    /**
     * 生成报价单号
     */
    String generateQuotationNo();

    /**
     * 更新报价单状态
     */
    Boolean updateQuotationStatus(Long quotationId, String status);

    /**
     * 复制报价单
     */
    Boolean copyQuotation(Long quotationId);

    /**
     * 发送报价单（更新状态为已发送）
     */
    Boolean sendQuotation(Long quotationId);

    /**
     * 生成报价单PDF
     */
    byte[] generateQuotationPdf(Long quotationId);
}