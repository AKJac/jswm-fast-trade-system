package com.hongsite.jisu.controller;

import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.common.Result;
import com.hongsite.jisu.entity.Supplier;
import com.hongsite.jisu.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 供应商管理控制器
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/suppliers")
@Tag(name = "供应商管理", description = "供应商信息的增删改查操作")
@Validated
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/page")
    @Operation(summary = "分页查询供应商列表", description = "支持多条件搜索和分页")
    public Result<PageResult<Supplier>> getSupplierPage(
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "供应商名称") @RequestParam(required = false) String supplierName,
            @Parameter(description = "公司类型") @RequestParam(required = false) String companyType,
            @Parameter(description = "信用等级") @RequestParam(required = false) String creditRating,
            @Parameter(description = "联系人") @RequestParam(required = false) String contact,
            @Parameter(description = "主要产品") @RequestParam(required = false) String mainProducts) {
        
        try {
            PageResult<Supplier> result = supplierService.getSupplierPage(current, size, supplierName, companyType,
                    creditRating, contact, mainProducts);
            return Result.success(result);
        } catch (Exception e) {
            log.error("分页查询供应商列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取供应商详情", description = "根据供应商ID获取详细信息")
    public Result<Supplier> getSupplier(
            @Parameter(description = "供应商ID", required = true) @PathVariable Long id) {
        
        try {
            Supplier supplier = supplierService.getSupplierById(id);
            if (supplier == null) {
                return Result.error(404, "供应商不存在");
            }
            return Result.success(supplier);
        } catch (Exception e) {
            log.error("获取供应商详情失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "新增供应商", description = "创建新的供应商信息")
    public Result<String> saveSupplier(@Valid @RequestBody Supplier supplier) {
        
        try {
            // 检查供应商名称是否重复
            if (supplierService.checkSupplierNameExists(supplier.getSupplierName(), null)) {
                return Result.error("供应商名称已存在");
            }
            
            Boolean success = supplierService.saveSupplier(supplier);
            if (success) {
                return Result.success("供应商创建成功");
            } else {
                return Result.error("供应商创建失败");
            }
        } catch (Exception e) {
            log.error("新增供应商失败", e);
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新供应商", description = "更新供应商信息")
    public Result<String> updateSupplier(
            @Parameter(description = "供应商ID", required = true) @PathVariable Long id,
            @Valid @RequestBody Supplier supplier) {
        
        try {
            supplier.setId(id);
            
            // 检查供应商名称是否重复（排除当前供应商）
            if (supplierService.checkSupplierNameExists(supplier.getSupplierName(), id)) {
                return Result.error("供应商名称已存在");
            }
            
            Boolean success = supplierService.updateSupplier(supplier);
            if (success) {
                return Result.success("供应商更新成功");
            } else {
                return Result.error("供应商更新失败");
            }
        } catch (Exception e) {
            log.error("更新供应商失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除供应商", description = "逻辑删除供应商信息")
    public Result<String> deleteSupplier(
            @Parameter(description = "供应商ID", required = true) @PathVariable Long id) {
        
        try {
            Boolean success = supplierService.deleteSupplier(id);
            if (success) {
                return Result.success("供应商删除成功");
            } else {
                return Result.error("供应商删除失败");
            }
        } catch (Exception e) {
            log.error("删除供应商失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除供应商", description = "批量逻辑删除供应商信息")
    public Result<String> batchDeleteSuppliers(@RequestBody List<Long> ids) {
        
        try {
            Boolean success = supplierService.batchDeleteSuppliers(ids);
            if (success) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除供应商失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/check-no")
    @Operation(summary = "检查供应商编号", description = "检查供应商编号是否已存在")
    public Result<Boolean> checkSupplierNo(
            @Parameter(description = "供应商编号", required = true) @RequestParam String supplierNo,
            @Parameter(description = "排除的供应商ID") @RequestParam(required = false) Long excludeId) {
        
        try {
            Boolean exists = supplierService.checkSupplierNoExists(supplierNo, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查供应商编号失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    @GetMapping("/check-name")
    @Operation(summary = "检查供应商名称", description = "检查供应商名称是否已存在")
    public Result<Boolean> checkSupplierName(
            @Parameter(description = "供应商名称", required = true) @RequestParam String supplierName,
            @Parameter(description = "排除的供应商ID") @RequestParam(required = false) Long excludeId) {
        
        try {
            Boolean exists = supplierService.checkSupplierNameExists(supplierName, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查供应商名称失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "获取供应商统计", description = "获取供应商相关统计信息")
    public Result<Map<String, Object>> getSupplierStats() {
        
        try {
            Map<String, Object> stats = supplierService.getSupplierStats();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取供应商统计失败", e);
            return Result.error("获取统计失败：" + e.getMessage());
        }
    }

    @GetMapping("/options")
    @Operation(summary = "获取供应商选项", description = "获取供应商选项列表，用于下拉选择")
    public Result<List<Map<String, Object>>> getSupplierOptions() {
        
        try {
            List<Map<String, Object>> options = supplierService.getSupplierOptions();
            return Result.success(options);
        } catch (Exception e) {
            log.error("获取供应商选项失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    @GetMapping("/search-by-product")
    @Operation(summary = "根据产品搜索供应商", description = "根据主要产品关键词搜索相关供应商")
    public Result<List<Map<String, Object>>> searchSuppliersByProduct(
            @Parameter(description = "产品关键词", required = true) @RequestParam String product) {
        
        try {
            List<Map<String, Object>> suppliers = supplierService.searchSuppliersByProduct(product);
            return Result.success(suppliers);
        } catch (Exception e) {
            log.error("根据产品搜索供应商失败", e);
            return Result.error("搜索失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}/api-config")
    @Operation(summary = "更新供应商API配置", description = "更新供应商的API接口配置")
    public Result<String> updateSupplierApiConfig(
            @Parameter(description = "供应商ID", required = true) @PathVariable Long id,
            @Parameter(description = "启用报价单API") @RequestParam(required = false) Boolean enableQuotationApi,
            @Parameter(description = "启用销售单API") @RequestParam(required = false) Boolean enableSalesApi,
            @Parameter(description = "API接口地址") @RequestParam(required = false) String apiEndpoint) {
        
        try {
            Boolean success = supplierService.updateSupplierApiConfig(id, enableQuotationApi, enableSalesApi, apiEndpoint);
            if (success) {
                return Result.success("API配置更新成功");
            } else {
                return Result.error("API配置更新失败");
            }
        } catch (Exception e) {
            log.error("更新供应商API配置失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }
}