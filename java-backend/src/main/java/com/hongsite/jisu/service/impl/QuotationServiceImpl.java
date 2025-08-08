package com.hongsite.jisu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.entity.Customer;
import com.hongsite.jisu.entity.Quotation;
import com.hongsite.jisu.entity.QuotationItem;
import com.hongsite.jisu.mapper.QuotationMapper;
import com.hongsite.jisu.service.CustomerService;
import com.hongsite.jisu.service.QuotationItemService;
import com.hongsite.jisu.service.QuotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 报价单服务层实现类
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@Service
public class QuotationServiceImpl extends ServiceImpl<QuotationMapper, Quotation> implements QuotationService {

    @Autowired
    private QuotationItemService quotationItemService;

    @Autowired
    private CustomerService customerService;

    @Override
    public PageResult<Quotation> getQuotationPage(Long current, Long size, String quotationNo, String customerName,
                                                 String status, String currency, Long customerId) {
        // 构建查询条件
        LambdaQueryWrapper<Quotation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Quotation::getIsActive, true)
                   .like(StrUtil.isNotBlank(quotationNo), Quotation::getQuotationNo, quotationNo)
                   .like(StrUtil.isNotBlank(customerName), Quotation::getCustomerName, customerName)
                   .eq(StrUtil.isNotBlank(status), Quotation::getStatus, status)
                   .eq(StrUtil.isNotBlank(currency), Quotation::getCurrency, currency)
                   .eq(customerId != null, Quotation::getCustomerId, customerId)
                   .orderByDesc(Quotation::getCreatedAt);

        // 分页查询
        Page<Quotation> page = new Page<>(current, size);
        IPage<Quotation> result = this.page(page, queryWrapper);

        return PageResult.build(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Quotation getQuotationById(Long id) {
        LambdaQueryWrapper<Quotation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Quotation::getId, id)
                   .eq(Quotation::getIsActive, true);
        
        Quotation quotation = this.getOne(queryWrapper);
        if (quotation != null) {
            // 加载报价单明细
            List<QuotationItem> items = quotationItemService.getItemsByQuotationId(id);
            quotation.setItems(items);
            
            // 加载客户信息
            Customer customer = customerService.getCustomerById(quotation.getCustomerId());
            quotation.setCustomer(customer);
        }
        return quotation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveQuotation(Quotation quotation) {
        // 生成报价单号
        quotation.setQuotationNo(generateQuotationNo());
        
        // 设置默认值
        if (quotation.getIsActive() == null) {
            quotation.setIsActive(true);
        }
        if (StrUtil.isBlank(quotation.getStatus())) {
            quotation.setStatus("draft");
        }
        if (StrUtil.isBlank(quotation.getCurrency())) {
            quotation.setCurrency("USD");
        }
        if (quotation.getQuotationDate() == null) {
            quotation.setQuotationDate(LocalDateTime.now());
        }
        if (quotation.getValidUntil() == null) {
            // 默认有效期30天
            quotation.setValidUntil(LocalDateTime.now().plusDays(30));
        }
        
        // 从客户信息中填充联系信息
        if (quotation.getCustomerId() != null) {
            Customer customer = customerService.getCustomerById(quotation.getCustomerId());
            if (customer != null) {
                quotation.setCustomerName(customer.getCustomerName());
                if (StrUtil.isBlank(quotation.getContact())) {
                    quotation.setContact(customer.getContact());
                }
                if (StrUtil.isBlank(quotation.getEmail())) {
                    quotation.setEmail(customer.getEmail());
                }
                if (StrUtil.isBlank(quotation.getPhone())) {
                    quotation.setPhone(customer.getPhone());
                }
            }
        }
        
        // 保存报价单
        boolean success = this.save(quotation);
        
        // 保存报价单明细
        if (success && quotation.getItems() != null && !quotation.getItems().isEmpty()) {
            for (QuotationItem item : quotation.getItems()) {
                item.setQuotationId(quotation.getId());
            }
            quotationItemService.saveBatch(quotation.getItems());
            
            // 计算总金额
            calculateQuotationAmount(quotation);
            this.updateById(quotation);
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateQuotation(Quotation quotation) {
        // 检查报价单是否存在
        Quotation existQuotation = getQuotationById(quotation.getId());
        if (existQuotation == null) {
            throw new RuntimeException("报价单不存在");
        }
        
        // 更新报价单明细
        if (quotation.getItems() != null) {
            // 删除原有明细
            quotationItemService.deleteByQuotationId(quotation.getId());
            
            // 保存新明细
            for (QuotationItem item : quotation.getItems()) {
                item.setQuotationId(quotation.getId());
            }
            quotationItemService.saveBatch(quotation.getItems());
            
            // 重新计算总金额
            calculateQuotationAmount(quotation);
        }
        
        return this.updateById(quotation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteQuotation(Long id) {
        // 检查报价单是否存在
        Quotation quotation = getQuotationById(id);
        if (quotation == null) {
            throw new RuntimeException("报价单不存在");
        }
        
        // 逻辑删除（设置is_active为false）
        quotation.setIsActive(false);
        return this.updateById(quotation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchDeleteQuotations(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("请选择要删除的报价单");
        }
        
        List<Quotation> quotations = this.listByIds(ids);
        for (Quotation quotation : quotations) {
            quotation.setIsActive(false);
        }
        
        return this.updateBatchById(quotations);
    }

    @Override
    public Boolean checkQuotationNoExists(String quotationNo, Long excludeId) {
        if (StrUtil.isBlank(quotationNo)) {
            return false;
        }
        
        Integer count = baseMapper.checkQuotationNoExists(quotationNo, excludeId);
        return count > 0;
    }

    @Override
    public Map<String, Object> getQuotationStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总报价单数
        Long totalQuotations = this.lambdaQuery()
                .eq(Quotation::getIsActive, true)
                .count();
        stats.put("totalQuotations", totalQuotations);
        
        // 草稿报价单数
        Long draftQuotations = this.lambdaQuery()
                .eq(Quotation::getIsActive, true)
                .eq(Quotation::getStatus, "draft")
                .count();
        stats.put("draftQuotations", draftQuotations);
        
        // 已发送报价单数
        Long sentQuotations = this.lambdaQuery()
                .eq(Quotation::getIsActive, true)
                .eq(Quotation::getStatus, "sent")
                .count();
        stats.put("sentQuotations", sentQuotations);
        
        // 已接受报价单数
        Long acceptedQuotations = this.lambdaQuery()
                .eq(Quotation::getIsActive, true)
                .eq(Quotation::getStatus, "accepted")
                .count();
        stats.put("acceptedQuotations", acceptedQuotations);
        
        // 即将过期的报价单数
        Long expiringQuotations = baseMapper.getExpiringQuotationCount();
        stats.put("expiringQuotations", expiringQuotations);
        
        // 报价单状态统计
        List<Map<String, Object>> statusStats = baseMapper.getQuotationStatusStats();
        stats.put("statusStats", statusStats);
        
        // 报价单货币统计
        List<Map<String, Object>> currencyStats = baseMapper.getQuotationCurrencyStats();
        stats.put("currencyStats", currencyStats);
        
        // 客户报价单统计
        List<Map<String, Object>> customerStats = baseMapper.getCustomerQuotationStats();
        stats.put("customerStats", customerStats);
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getQuotationsByCustomerId(Long customerId) {
        return baseMapper.getQuotationsByCustomerId(customerId);
    }

    @Override
    public String generateQuotationNo() {
        // 格式：Q + YYYYMMDD + 3位随机数
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%03d", new Random().nextInt(1000));
        return "Q" + dateStr + randomStr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateQuotationStatus(Long quotationId, String status) {
        Quotation quotation = getQuotationById(quotationId);
        if (quotation == null) {
            throw new RuntimeException("报价单不存在");
        }
        
        quotation.setStatus(status);
        return this.updateById(quotation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean copyQuotation(Long quotationId) {
        Quotation originalQuotation = getQuotationById(quotationId);
        if (originalQuotation == null) {
            throw new RuntimeException("原报价单不存在");
        }
        
        // 创建新报价单
        Quotation newQuotation = new Quotation();
        newQuotation.setCustomerId(originalQuotation.getCustomerId());
        newQuotation.setCustomerName(originalQuotation.getCustomerName());
        newQuotation.setContact(originalQuotation.getContact());
        newQuotation.setEmail(originalQuotation.getEmail());
        newQuotation.setPhone(originalQuotation.getPhone());
        newQuotation.setCurrency(originalQuotation.getCurrency());
        newQuotation.setPaymentTerms(originalQuotation.getPaymentTerms());
        newQuotation.setDeliveryTerms(originalQuotation.getDeliveryTerms());
        newQuotation.setSpecialTerms(originalQuotation.getSpecialTerms());
        newQuotation.setItems(originalQuotation.getItems());
        
        return saveQuotation(newQuotation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean sendQuotation(Long quotationId) {
        return updateQuotationStatus(quotationId, "sent");
    }

    @Override
    public byte[] generateQuotationPdf(Long quotationId) {
        // 这里应该实现PDF生成逻辑
        // 由于是简化版本，暂时返回空数组
        // 实际项目中可以使用iText或其他PDF库生成报价单PDF
        log.info("生成报价单PDF: {}", quotationId);
        return new byte[0];
    }

    /**
     * 计算报价单金额
     */
    private void calculateQuotationAmount(Quotation quotation) {
        if (quotation.getItems() == null || quotation.getItems().isEmpty()) {
            return;
        }
        
        BigDecimal subtotal = BigDecimal.ZERO;
        for (QuotationItem item : quotation.getItems()) {
            if (item.getTotalPrice() != null) {
                subtotal = subtotal.add(item.getTotalPrice());
            }
        }
        
        quotation.setSubtotal(subtotal);
        
        // 计算折扣金额
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (quotation.getDiscountRate() != null && quotation.getDiscountRate().compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = subtotal.multiply(quotation.getDiscountRate()).divide(new BigDecimal("100"));
        }
        quotation.setDiscountAmount(discountAmount);
        
        // 计算总金额
        BigDecimal totalAmount = subtotal.subtract(discountAmount);
        quotation.setTotalAmount(totalAmount);
    }
}