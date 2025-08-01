package com.hongsite.jisu.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.entity.Customer;
import com.hongsite.jisu.mapper.CustomerMapper;
import com.hongsite.jisu.service.CustomerService;
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
 * 客户服务层实现类
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Override
    public PageResult<Customer> getCustomerPage(Long current, Long size, String customerName, String country, 
                                              String level, String status, String progress, String customerType, 
                                              String customerSource) {
        // 构建查询条件
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getIsActive, true)
                   .like(StrUtil.isNotBlank(customerName), Customer::getCustomerName, customerName)
                   .eq(StrUtil.isNotBlank(country), Customer::getCountry, country)
                   .eq(StrUtil.isNotBlank(level), Customer::getLevel, level)
                   .eq(StrUtil.isNotBlank(status), Customer::getStatus, status)
                   .eq(StrUtil.isNotBlank(progress), Customer::getProgress, progress)
                   .eq(StrUtil.isNotBlank(customerType), Customer::getCustomerType, customerType)
                   .eq(StrUtil.isNotBlank(customerSource), Customer::getCustomerSource, customerSource)
                   .orderByDesc(Customer::getCreatedAt);

        // 分页查询
        Page<Customer> page = new Page<>(current, size);
        IPage<Customer> result = this.page(page, queryWrapper);

        return PageResult.build(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Customer getCustomerById(Long id) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getId, id)
                   .eq(Customer::getIsActive, true);
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveCustomer(Customer customer) {
        // 生成客户编号
        customer.setCustomerNo(generateCustomerNo());
        
        // 设置默认值
        if (customer.getIsActive() == null) {
            customer.setIsActive(true);
        }
        if (StrUtil.isBlank(customer.getLevel())) {
            customer.setLevel("C");
        }
        if (StrUtil.isBlank(customer.getStatus())) {
            customer.setStatus("potential");
        }
        if (StrUtil.isBlank(customer.getProgress())) {
            customer.setProgress("initial");
        }
        
        return this.save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCustomer(Customer customer) {
        // 检查客户是否存在
        Customer existCustomer = getCustomerById(customer.getId());
        if (existCustomer == null) {
            throw new RuntimeException("客户不存在");
        }
        
        return this.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCustomer(Long id) {
        // 检查客户是否存在
        Customer customer = getCustomerById(id);
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }
        
        // 逻辑删除（设置is_active为false）
        customer.setIsActive(false);
        return this.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchDeleteCustomers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("请选择要删除的客户");
        }
        
        List<Customer> customers = this.listByIds(ids);
        for (Customer customer : customers) {
            customer.setIsActive(false);
        }
        
        return this.updateBatchById(customers);
    }

    @Override
    public Boolean checkCustomerNameExists(String customerName, Long excludeId) {
        if (StrUtil.isBlank(customerName)) {
            return false;
        }
        
        Integer count = baseMapper.checkCustomerNameExists(customerName, excludeId);
        return count > 0;
    }

    @Override
    public Map<String, Object> getCustomerStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总客户数
        Long totalCustomers = this.lambdaQuery()
                .eq(Customer::getIsActive, true)
                .count();
        stats.put("totalCustomers", totalCustomers);
        
        // 活跃客户数
        Long activeCustomers = this.lambdaQuery()
                .eq(Customer::getIsActive, true)
                .eq(Customer::getStatus, "active")
                .count();
        stats.put("activeCustomers", activeCustomers);
        
        // 潜在客户数
        Long potentialCustomers = this.lambdaQuery()
                .eq(Customer::getIsActive, true)
                .eq(Customer::getStatus, "potential")
                .count();
        stats.put("potentialCustomers", potentialCustomers);
        
        // 客户等级统计
        List<Map<String, Object>> levelStats = baseMapper.getCustomerLevelStats();
        stats.put("levelStats", levelStats);
        
        // 客户类型统计
        List<Map<String, Object>> typeStats = baseMapper.getCustomerTypeStats();
        stats.put("typeStats", typeStats);
        
        // 客户状态统计
        List<Map<String, Object>> statusStats = baseMapper.getCustomerStatusStats();
        stats.put("statusStats", statusStats);
        
        return stats;
    }

    @Override
    public String generateCustomerNo() {
        // 格式：C + YYYYMMDD + 3位随机数
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%03d", new Random().nextInt(1000));
        return "C" + dateStr + randomStr;
    }
}