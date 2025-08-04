package com.hongsite.jisu.controller;

import com.hongsite.jisu.common.Result;
import com.hongsite.jisu.service.CustomerService;
import com.hongsite.jisu.service.ProductService;
import com.hongsite.jisu.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计分析控制器
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/statistics")
@Tag(name = "统计分析", description = "系统数据统计分析")
public class StatisticsController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/dashboard")
    @Operation(summary = "获取仪表盘统计", description = "获取首页仪表盘显示的统计数据")
    public Result<Map<String, Object>> getDashboardStats() {
        
        try {
            Map<String, Object> dashboard = new HashMap<>();
            
            // 获取各模块统计数据
            Map<String, Object> customerStats = customerService.getCustomerStats();
            Map<String, Object> productStats = productService.getProductStats();
            Map<String, Object> supplierStats = supplierService.getSupplierStats();
            
            // 汇总核心指标
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCustomers", customerStats.get("totalCustomers"));
            summary.put("activeCustomers", customerStats.get("activeCustomers"));
            summary.put("totalProducts", productStats.get("totalProducts"));
            summary.put("activeProducts", productStats.get("activeProducts"));
            summary.put("lowStockProducts", productStats.get("lowStockProducts"));
            summary.put("totalSuppliers", supplierStats.get("totalSuppliers"));
            summary.put("manufacturerCount", supplierStats.get("manufacturerCount"));
            summary.put("apiEnabledCount", supplierStats.get("apiEnabledCount"));
            
            dashboard.put("summary", summary);
            dashboard.put("customerStats", customerStats);
            dashboard.put("productStats", productStats);
            dashboard.put("supplierStats", supplierStats);
            dashboard.put("updateTime", LocalDateTime.now());
            
            return Result.success("仪表盘数据获取成功", dashboard);
        } catch (Exception e) {
            log.error("获取仪表盘统计失败", e);
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    @GetMapping("/customer")
    @Operation(summary = "获取客户统计", description = "获取客户相关的详细统计信息")
    public Result<Map<String, Object>> getCustomerStats() {
        
        try {
            Map<String, Object> stats = customerService.getCustomerStats();
            return Result.success("客户统计数据获取成功", stats);
        } catch (Exception e) {
            log.error("获取客户统计失败", e);
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    @GetMapping("/product")
    @Operation(summary = "获取产品统计", description = "获取产品相关的详细统计信息")
    public Result<Map<String, Object>> getProductStats() {
        
        try {
            Map<String, Object> stats = productService.getProductStats();
            return Result.success("产品统计数据获取成功", stats);
        } catch (Exception e) {
            log.error("获取产品统计失败", e);
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    @GetMapping("/supplier")
    @Operation(summary = "获取供应商统计", description = "获取供应商相关的详细统计信息")
    public Result<Map<String, Object>> getSupplierStats() {
        
        try {
            Map<String, Object> stats = supplierService.getSupplierStats();
            return Result.success("供应商统计数据获取成功", stats);
        } catch (Exception e) {
            log.error("获取供应商统计失败", e);
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    @GetMapping("/overview")
    @Operation(summary = "获取系统概览", description = "获取系统整体运行概览数据")
    public Result<Map<String, Object>> getSystemOverview() {
        
        try {
            Map<String, Object> overview = new HashMap<>();
            
            // 系统基本信息
            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("systemName", "极速外贸管理系统");
            systemInfo.put("version", "1.0.0");
            systemInfo.put("company", "鸿思特科技有限公司");
            systemInfo.put("email", "hst_co@163.com");
            systemInfo.put("phone", "17600198256");
            systemInfo.put("startTime", LocalDateTime.now().minusHours(1)); // 模拟启动时间
            
            // 数据总览
            Map<String, Object> dataOverview = new HashMap<>();
            dataOverview.put("totalCustomers", customerService.count());
            dataOverview.put("totalProducts", productService.count());
            dataOverview.put("totalSuppliers", supplierService.count());
            
            // 今日活动（模拟数据）
            Map<String, Object> todayActivity = new HashMap<>();
            todayActivity.put("newCustomers", 5);
            todayActivity.put("newProducts", 12);
            todayActivity.put("newQuotations", 8);
            todayActivity.put("activeUsers", 15);
            
            overview.put("systemInfo", systemInfo);
            overview.put("dataOverview", dataOverview);
            overview.put("todayActivity", todayActivity);
            overview.put("updateTime", LocalDateTime.now());
            
            return Result.success("系统概览数据获取成功", overview);
        } catch (Exception e) {
            log.error("获取系统概览失败", e);
            return Result.error("获取概览数据失败：" + e.getMessage());
        }
    }
}