package com.hongsite.jisu.controller;

import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.common.Result;
import com.hongsite.jisu.entity.Quotation;
import com.hongsite.jisu.service.QuotationService;
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
 * 报价单管理控制器
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/quotations")
@Tag(name = "报价单管理", description = "报价单信息的增删改查操作")
@Validated
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @GetMapping("/page")
    @Operation(summary = "分页查询报价单列表", description = "支持多条件搜索和分页")
    public Result<PageResult<Quotation>> getQuotationPage(
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "报价单号") @RequestParam(required = false) String quotationNo,
            @Parameter(description = "客户名称") @RequestParam(required = false) String customerName,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "货币") @RequestParam(required = false) String currency,
            @Parameter(description = "客户ID") @RequestParam(required = false) Long customerId) {
        
        try {
            PageResult<Quotation> result = quotationService.getQuotationPage(current, size, quotationNo, customerName,
                    status, currency, customerId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("分页查询报价单列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取报价单详情", description = "根据报价单ID获取详细信息，包含明细")
    public Result<Quotation> getQuotation(
            @Parameter(description = "报价单ID", required = true) @PathVariable Long id) {
        
        try {
            Quotation quotation = quotationService.getQuotationById(id);
            if (quotation == null) {
                return Result.error(404, "报价单不存在");
            }
            return Result.success(quotation);
        } catch (Exception e) {
            log.error("获取报价单详情失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "新增报价单", description = "创建新的报价单信息")
    public Result<String> saveQuotation(@Valid @RequestBody Quotation quotation) {
        
        try {
            Boolean success = quotationService.saveQuotation(quotation);
            if (success) {
                return Result.success("报价单创建成功");
            } else {
                return Result.error("报价单创建失败");
            }
        } catch (Exception e) {
            log.error("新增报价单失败", e);
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新报价单", description = "更新报价单信息")
    public Result<String> updateQuotation(
            @Parameter(description = "报价单ID", required = true) @PathVariable Long id,
            @Valid @RequestBody Quotation quotation) {
        
        try {
            quotation.setId(id);
            Boolean success = quotationService.updateQuotation(quotation);
            if (success) {
                return Result.success("报价单更新成功");
            } else {
                return Result.error("报价单更新失败");
            }
        } catch (Exception e) {
            log.error("更新报价单失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除报价单", description = "逻辑删除报价单信息")
    public Result<String> deleteQuotation(
            @Parameter(description = "报价单ID", required = true) @PathVariable Long id) {
        
        try {
            Boolean success = quotationService.deleteQuotation(id);
            if (success) {
                return Result.success("报价单删除成功");
            } else {
                return Result.error("报价单删除失败");
            }
        } catch (Exception e) {
            log.error("删除报价单失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除报价单", description = "批量逻辑删除报价单信息")
    public Result<String> batchDeleteQuotations(@RequestBody List<Long> ids) {
        
        try {
            Boolean success = quotationService.batchDeleteQuotations(ids);
            if (success) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除报价单失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/check-no")
    @Operation(summary = "检查报价单号", description = "检查报价单号是否已存在")
    public Result<Boolean> checkQuotationNo(
            @Parameter(description = "报价单号", required = true) @RequestParam String quotationNo,
            @Parameter(description = "排除的报价单ID") @RequestParam(required = false) Long excludeId) {
        
        try {
            Boolean exists = quotationService.checkQuotationNoExists(quotationNo, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查报价单号失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "获取报价单统计", description = "获取报价单相关统计信息")
    public Result<Map<String, Object>> getQuotationStats() {
        
        try {
            Map<String, Object> stats = quotationService.getQuotationStats();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取报价单统计失败", e);
            return Result.error("获取统计失败：" + e.getMessage());
        }
    }

    @GetMapping("/by-customer/{customerId}")
    @Operation(summary = "根据客户获取报价单", description = "根据客户ID获取该客户的报价单列表")
    public Result<List<Map<String, Object>>> getQuotationsByCustomerId(
            @Parameter(description = "客户ID", required = true) @PathVariable Long customerId) {
        
        try {
            List<Map<String, Object>> quotations = quotationService.getQuotationsByCustomerId(customerId);
            return Result.success(quotations);
        } catch (Exception e) {
            log.error("根据客户获取报价单失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新报价单状态", description = "更新报价单的状态")
    public Result<String> updateQuotationStatus(
            @Parameter(description = "报价单ID", required = true) @PathVariable Long id,
            @Parameter(description = "状态", required = true) @RequestParam String status) {
        
        try {
            Boolean success = quotationService.updateQuotationStatus(id, status);
            if (success) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新报价单状态失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    @PostMapping("/{id}/copy")
    @Operation(summary = "复制报价单", description = "复制现有报价单创建新的报价单")
    public Result<String> copyQuotation(
            @Parameter(description = "报价单ID", required = true) @PathVariable Long id) {
        
        try {
            Boolean success = quotationService.copyQuotation(id);
            if (success) {
                return Result.success("报价单复制成功");
            } else {
                return Result.error("报价单复制失败");
            }
        } catch (Exception e) {
            log.error("复制报价单失败", e);
            return Result.error("复制失败：" + e.getMessage());
        }
    }

    @PostMapping("/{id}/send")
    @Operation(summary = "发送报价单", description = "发送报价单给客户（状态更新为已发送）")
    public Result<String> sendQuotation(
            @Parameter(description = "报价单ID", required = true) @PathVariable Long id) {
        
        try {
            Boolean success = quotationService.sendQuotation(id);
            if (success) {
                return Result.success("报价单发送成功");
            } else {
                return Result.error("报价单发送失败");
            }
        } catch (Exception e) {
            log.error("发送报价单失败", e);
            return Result.error("发送失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}/pdf")
    @Operation(summary = "生成报价单PDF", description = "生成报价单的PDF文件")
    public Result<String> generateQuotationPdf(
            @Parameter(description = "报价单ID", required = true) @PathVariable Long id) {
        
        try {
            byte[] pdfBytes = quotationService.generateQuotationPdf(id);
            // 这里应该返回PDF文件或下载链接
            // 由于是简化版本，暂时返回成功消息
            return Result.success("PDF生成成功");
        } catch (Exception e) {
            log.error("生成报价单PDF失败", e);
            return Result.error("生成失败：" + e.getMessage());
        }
    }
}