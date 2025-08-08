package com.hongsite.jisu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.entity.Customer;

import java.util.List;
import java.util.Map;

/**
 * 客户服务层接口
 * 
 * @author 鸿思特科技有限公司
 */
public interface CustomerService extends IService<Customer> {

    /**
     * 分页查询客户列表
     */
    PageResult<Customer> getCustomerPage(Long current, Long size, String customerName, String country, 
                                       String level, String status, String progress, String customerType, 
                                       String customerSource);

    /**
     * 根据ID获取客户详情
     */
    Customer getCustomerById(Long id);

    /**
     * 新增客户
     */
    Boolean saveCustomer(Customer customer);

    /**
     * 更新客户
     */
    Boolean updateCustomer(Customer customer);

    /**
     * 删除客户（逻辑删除）
     */
    Boolean deleteCustomer(Long id);

    /**
     * 批量删除客户
     */
    Boolean batchDeleteCustomers(List<Long> ids);

    /**
     * 检查客户名称是否存在
     */
    Boolean checkCustomerNameExists(String customerName, Long excludeId);

    /**
     * 获取客户统计信息
     */
    Map<String, Object> getCustomerStats();

    /**
     * 生成客户编号
     */
    String generateCustomerNo();
}