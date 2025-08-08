package com.hongsite.jisu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongsite.jisu.entity.QuotationItem;
import com.hongsite.jisu.mapper.QuotationItemMapper;
import com.hongsite.jisu.service.QuotationItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 报价单明细服务层实现类
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@Service
public class QuotationItemServiceImpl extends ServiceImpl<QuotationItemMapper, QuotationItem> implements QuotationItemService {

    @Override
    public List<QuotationItem> getItemsByQuotationId(Long quotationId) {
        return baseMapper.getItemsByQuotationId(quotationId);
    }

    @Override
    public Boolean deleteByQuotationId(Long quotationId) {
        Integer count = baseMapper.deleteByQuotationId(quotationId);
        return count > 0;
    }

    @Override
    public List<Map<String, Object>> getProductQuoteStats() {
        return baseMapper.getProductQuoteStats();
    }

    @Override
    public List<Map<String, Object>> getHotQuoteProducts() {
        return baseMapper.getHotQuoteProducts();
    }
}