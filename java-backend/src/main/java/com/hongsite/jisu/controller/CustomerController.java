package com.hongsite.jisu.controller;

import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.common.Result;
import com.hongsite.jisu.entity.Customer;
import com.hongsite.jisu.service.CustomerService;
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
 * 客户管理控制器
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/customers")
@Tag(name = "客户管理", description = "客户信息的增删改查操作")
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/page")
    @Operation(summary = "分页查询客户列表", description = "支持多条件搜索和分页")
    public Result<PageResult<Customer>> getCustomerPage(
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "客户名称") @RequestParam(required = false) String customerName,
            @Parameter(description = "国家/地区") @RequestParam(required = false) String country,
            @Parameter(description = "客户等级") @RequestParam(required = false) String level,
            @Parameter(description = "客户状态") @RequestParam(required = false) String status,
            @Parameter(description = "跟进进度") @RequestParam(required = false) String progress,
            @Parameter(description = "客户类型") @RequestParam(required = false) String customerType,
            @Parameter(description = "客户来源") @RequestParam(required = false) String customerSource) {
        
        try {
            PageResult<Customer> result = customerService.getCustomerPage(current, size, customerName, country, 
                    level, status, progress, customerType, customerSource);
            return Result.success(result);
        } catch (Exception e) {
            log.error("分页查询客户列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取客户详情", description = "根据客户ID获取详细信息")
    public Result<Customer> getCustomer(
            @Parameter(description = "客户ID", required = true) @PathVariable Long id) {
        
        try {
            Customer customer = customerService.getCustomerById(id);
            if (customer == null) {
                return Result.error(404, "客户不存在");
            }
            return Result.success(customer);
        } catch (Exception e) {
            log.error("获取客户详情失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "新增客户", description = "创建新的客户信息")
    public Result<String> saveCustomer(@Valid @RequestBody Customer customer) {
        
        try {
            // 检查客户名称是否重复
            if (customerService.checkCustomerNameExists(customer.getCustomerName(), null)) {
                return Result.error("客户名称已存在");
            }
            
            Boolean success = customerService.saveCustomer(customer);
            if (success) {
                return Result.success("客户创建成功");
            } else {
                return Result.error("客户创建失败");
            }
        } catch (Exception e) {
            log.error("新增客户失败", e);
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新客户", description = "更新客户信息")
    public Result<String> updateCustomer(
            @Parameter(description = "客户ID", required = true) @PathVariable Long id,
            @Valid @RequestBody Customer customer) {
        
        try {
            customer.setId(id);
            
            // 检查客户名称是否重复（排除当前客户）
            if (customerService.checkCustomerNameExists(customer.getCustomerName(), id)) {
                return Result.error("客户名称已存在");
            }
            
            Boolean success = customerService.updateCustomer(customer);
            if (success) {
                return Result.success("客户更新成功");
            } else {
                return Result.error("客户更新失败");
            }
        } catch (Exception e) {
            log.error("更新客户失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户", description = "逻辑删除客户信息")
    public Result<String> deleteCustomer(
            @Parameter(description = "客户ID", required = true) @PathVariable Long id) {
        
        try {
            Boolean success = customerService.deleteCustomer(id);
            if (success) {
                return Result.success("客户删除成功");
            } else {
                return Result.error("客户删除失败");
            }
        } catch (Exception e) {
            log.error("删除客户失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除客户", description = "批量逻辑删除客户信息")
    public Result<String> batchDeleteCustomers(@RequestBody List<Long> ids) {
        
        try {
            Boolean success = customerService.batchDeleteCustomers(ids);
            if (success) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除客户失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/check-name")
    @Operation(summary = "检查客户名称", description = "检查客户名称是否已存在")
    public Result<Boolean> checkCustomerName(
            @Parameter(description = "客户名称", required = true) @RequestParam String customerName,
            @Parameter(description = "排除的客户ID") @RequestParam(required = false) Long excludeId) {
        
        try {
            Boolean exists = customerService.checkCustomerNameExists(customerName, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查客户名称失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "获取客户统计", description = "获取客户相关统计信息")
    public Result<Map<String, Object>> getCustomerStats() {
        
        try {
            Map<String, Object> stats = customerService.getCustomerStats();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取客户统计失败", e);
            return Result.error("获取统计失败：" + e.getMessage());
        }
    }
}