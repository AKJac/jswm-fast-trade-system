package com.hongsite.jisu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongsite.jisu.entity.QuotationItem;

import java.util.List;
import java.util.Map;

/**
 * 报价单明细服务层接口
 * 
 * @author 鸿思特科技有限公司
 */
public interface QuotationItemService extends IService<QuotationItem> {

    /**
     * 根据报价单ID获取明细列表
     */
    List<QuotationItem> getItemsByQuotationId(Long quotationId);

    /**
     * 根据报价单ID删除所有明细
     */
    Boolean deleteByQuotationId(Long quotationId);

    /**
     * 获取产品报价统计
     */
    List<Map<String, Object>> getProductQuoteStats();

    /**
     * 获取热门报价产品
     */
    List<Map<String, Object>> getHotQuoteProducts();
}