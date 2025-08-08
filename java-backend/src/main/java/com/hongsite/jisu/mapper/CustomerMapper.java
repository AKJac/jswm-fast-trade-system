package com.hongsite.jisu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongsite.jisu.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 客户数据访问层
 * 
 * @author 鸿思特科技有限公司
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 分页查询客户列表（带条件）
     */
    IPage<Customer> selectCustomerPage(Page<Customer> page, @Param("params") Map<String, Object> params);

    /**
     * 检查客户名称是否存在
     */
    @Select("SELECT COUNT(*) FROM customers WHERE customer_name = #{customerName} AND deleted = 0 " +
            "AND (#{excludeId} IS NULL OR id != #{excludeId})")
    Integer checkCustomerNameExists(@Param("customerName") String customerName, @Param("excludeId") Long excludeId);

    /**
     * 获取客户统计信息
     */
    List<Map<String, Object>> getCustomerStats();

    /**
     * 根据客户等级统计
     */
    @Select("SELECT level, COUNT(*) as count FROM customers WHERE deleted = 0 AND is_active = 1 GROUP BY level")
    List<Map<String, Object>> getCustomerLevelStats();

    /**
     * 根据客户类型统计
     */
    @Select("SELECT customer_type, COUNT(*) as count FROM customers WHERE deleted = 0 AND is_active = 1 GROUP BY customer_type")
    List<Map<String, Object>> getCustomerTypeStats();

    /**
     * 根据客户状态统计
     */
    @Select("SELECT status, COUNT(*) as count FROM customers WHERE deleted = 0 AND is_active = 1 GROUP BY status")
    List<Map<String, Object>> getCustomerStatusStats();
}