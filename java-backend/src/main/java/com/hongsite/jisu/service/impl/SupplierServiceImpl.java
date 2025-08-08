package com.hongsite.jisu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.entity.Supplier;
import com.hongsite.jisu.mapper.SupplierMapper;
import com.hongsite.jisu.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 供应商服务层实现类
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Override
    public PageResult<Supplier> getSupplierPage(Long current, Long size, String supplierName, String companyType,
                                              String creditRating, String contact, String mainProducts) {
        // 构建查询条件
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Supplier::getIsActive, true)
                   .like(StrUtil.isNotBlank(supplierName), Supplier::getSupplierName, supplierName)
                   .eq(StrUtil.isNotBlank(companyType), Supplier::getCompanyType, companyType)
                   .eq(StrUtil.isNotBlank(creditRating), Supplier::getCreditRating, creditRating)
                   .like(StrUtil.isNotBlank(contact), Supplier::getContact, contact)
                   .like(StrUtil.isNotBlank(mainProducts), Supplier::getMainProducts, mainProducts)
                   .orderByDesc(Supplier::getCreatedAt);

        // 分页查询
        Page<Supplier> page = new Page<>(current, size);
        IPage<Supplier> result = this.page(page, queryWrapper);

        return PageResult.build(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Supplier getSupplierById(Long id) {
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Supplier::getId, id)
                   .eq(Supplier::getIsActive, true);
        
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSupplier(Supplier supplier) {
        // 生成供应商编号
        supplier.setSupplierNo(generateSupplierNo());
        
        // 设置默认值
        if (supplier.getIsActive() == null) {
            supplier.setIsActive(true);
        }
        if (StrUtil.isBlank(supplier.getCompanyType())) {
            supplier.setCompanyType("manufacturer");
        }
        if (StrUtil.isBlank(supplier.getCreditRating())) {
            supplier.setCreditRating("A");
        }
        if (supplier.getEnableQuotationApi() == null) {
            supplier.setEnableQuotationApi(false);
        }
        if (supplier.getEnableSalesApi() == null) {
            supplier.setEnableSalesApi(false);
        }
        
        return this.save(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateSupplier(Supplier supplier) {
        // 检查供应商是否存在
        Supplier existSupplier = getSupplierById(supplier.getId());
        if (existSupplier == null) {
            throw new RuntimeException("供应商不存在");
        }
        
        return this.updateById(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteSupplier(Long id) {
        // 检查供应商是否存在
        Supplier supplier = getSupplierById(id);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }
        
        // 逻辑删除（设置is_active为false）
        supplier.setIsActive(false);
        return this.updateById(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchDeleteSuppliers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("请选择要删除的供应商");
        }
        
        List<Supplier> suppliers = this.listByIds(ids);
        for (Supplier supplier : suppliers) {
            supplier.setIsActive(false);
        }
        
        return this.updateBatchById(suppliers);
    }

    @Override
    public Boolean checkSupplierNoExists(String supplierNo, Long excludeId) {
        if (StrUtil.isBlank(supplierNo)) {
            return false;
        }
        
        Integer count = baseMapper.checkSupplierNoExists(supplierNo, excludeId);
        return count > 0;
    }

    @Override
    public Boolean checkSupplierNameExists(String supplierName, Long excludeId) {
        if (StrUtil.isBlank(supplierName)) {
            return false;
        }
        
        Integer count = baseMapper.checkSupplierNameExists(supplierName, excludeId);
        return count > 0;
    }

    @Override
    public Map<String, Object> getSupplierStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总供应商数
        Long totalSuppliers = this.lambdaQuery()
                .eq(Supplier::getIsActive, true)
                .count();
        stats.put("totalSuppliers", totalSuppliers);
        
        // 制造商数量
        Long manufacturerCount = this.lambdaQuery()
                .eq(Supplier::getIsActive, true)
                .eq(Supplier::getCompanyType, "manufacturer")
                .count();
        stats.put("manufacturerCount", manufacturerCount);
        
        // 贸易商数量
        Long traderCount = this.lambdaQuery()
                .eq(Supplier::getIsActive, true)
                .eq(Supplier::getCompanyType, "trader")
                .count();
        stats.put("traderCount", traderCount);
        
        // 启用API接口的供应商数量
        Long apiEnabledCount = baseMapper.getApiEnabledSupplierCount();
        stats.put("apiEnabledCount", apiEnabledCount);
        
        // 公司类型统计
        List<Map<String, Object>> typeStats = baseMapper.getSupplierTypeStats();
        stats.put("typeStats", typeStats);
        
        // 信用等级统计
        List<Map<String, Object>> creditStats = baseMapper.getSupplierCreditStats();
        stats.put("creditStats", creditStats);
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getSupplierOptions() {
        return baseMapper.getSupplierOptions();
    }

    @Override
    public List<Map<String, Object>> searchSuppliersByProduct(String product) {
        if (StrUtil.isBlank(product)) {
            return getSupplierOptions();
        }
        return baseMapper.searchSuppliersByProduct(product);
    }

    @Override
    public String generateSupplierNo() {
        // 格式：S + YYYYMMDD + 3位随机数
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%03d", new Random().nextInt(1000));
        return "S" + dateStr + randomStr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateSupplierApiConfig(Long supplierId, Boolean enableQuotationApi, Boolean enableSalesApi, String apiEndpoint) {
        Supplier supplier = getSupplierById(supplierId);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }
        
        supplier.setEnableQuotationApi(enableQuotationApi);
        supplier.setEnableSalesApi(enableSalesApi);
        supplier.setApiEndpoint(apiEndpoint);
        
        return this.updateById(supplier);
    }
}