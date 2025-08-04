package com.hongsite.jisu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.entity.Supplier;

import java.util.List;
import java.util.Map;

/**
 * 供应商服务层接口
 * 
 * @author 鸿思特科技有限公司
 */
public interface SupplierService extends IService<Supplier> {

    /**
     * 分页查询供应商列表
     */
    PageResult<Supplier> getSupplierPage(Long current, Long size, String supplierName, String companyType,
                                       String creditRating, String contact, String mainProducts);

    /**
     * 根据ID获取供应商详情
     */
    Supplier getSupplierById(Long id);

    /**
     * 新增供应商
     */
    Boolean saveSupplier(Supplier supplier);

    /**
     * 更新供应商
     */
    Boolean updateSupplier(Supplier supplier);

    /**
     * 删除供应商（逻辑删除）
     */
    Boolean deleteSupplier(Long id);

    /**
     * 批量删除供应商
     */
    Boolean batchDeleteSuppliers(List<Long> ids);

    /**
     * 检查供应商编号是否存在
     */
    Boolean checkSupplierNoExists(String supplierNo, Long excludeId);

    /**
     * 检查供应商名称是否存在
     */
    Boolean checkSupplierNameExists(String supplierName, Long excludeId);

    /**
     * 获取供应商统计信息
     */
    Map<String, Object> getSupplierStats();

    /**
     * 获取供应商选项列表（用于下拉选择）
     */
    List<Map<String, Object>> getSupplierOptions();

    /**
     * 根据主要产品搜索供应商
     */
    List<Map<String, Object>> searchSuppliersByProduct(String product);

    /**
     * 生成供应商编号
     */
    String generateSupplierNo();

    /**
     * 更新供应商API配置
     */
    Boolean updateSupplierApiConfig(Long supplierId, Boolean enableQuotationApi, Boolean enableSalesApi, String apiEndpoint);
}