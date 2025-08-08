package com.hongsite.jisu.controller;

import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.common.Result;
import com.hongsite.jisu.entity.Product;
import com.hongsite.jisu.service.ProductService;
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
 * 产品管理控制器
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/products")
@Tag(name = "产品管理", description = "产品信息的增删改查操作")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/page")
    @Operation(summary = "分页查询产品列表", description = "支持多条件搜索和分页")
    public Result<PageResult<Product>> getProductPage(
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "产品名称") @RequestParam(required = false) String productName,
            @Parameter(description = "产品货号") @RequestParam(required = false) String productCode,
            @Parameter(description = "产品分类") @RequestParam(required = false) String category,
            @Parameter(description = "产品状态") @RequestParam(required = false) String status,
            @Parameter(description = "品牌") @RequestParam(required = false) String brand,
            @Parameter(description = "供应商") @RequestParam(required = false) String supplier) {
        
        try {
            PageResult<Product> result = productService.getProductPage(current, size, productName, productCode,
                    category, status, brand, supplier);
            return Result.success(result);
        } catch (Exception e) {
            log.error("分页查询产品列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取产品详情", description = "根据产品ID获取详细信息")
    public Result<Product> getProduct(
            @Parameter(description = "产品ID", required = true) @PathVariable Long id) {
        
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return Result.error(404, "产品不存在");
            }
            return Result.success(product);
        } catch (Exception e) {
            log.error("获取产品详情失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "新增产品", description = "创建新的产品信息")
    public Result<String> saveProduct(@Valid @RequestBody Product product) {
        
        try {
            // 检查产品名称是否重复
            if (productService.checkProductNameExists(product.getProductName(), null)) {
                return Result.error("产品名称已存在");
            }
            
            Boolean success = productService.saveProduct(product);
            if (success) {
                return Result.success("产品创建成功");
            } else {
                return Result.error("产品创建失败");
            }
        } catch (Exception e) {
            log.error("新增产品失败", e);
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新产品", description = "更新产品信息")
    public Result<String> updateProduct(
            @Parameter(description = "产品ID", required = true) @PathVariable Long id,
            @Valid @RequestBody Product product) {
        
        try {
            product.setId(id);
            
            // 检查产品名称是否重复（排除当前产品）
            if (productService.checkProductNameExists(product.getProductName(), id)) {
                return Result.error("产品名称已存在");
            }
            
            Boolean success = productService.updateProduct(product);
            if (success) {
                return Result.success("产品更新成功");
            } else {
                return Result.error("产品更新失败");
            }
        } catch (Exception e) {
            log.error("更新产品失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除产品", description = "逻辑删除产品信息")
    public Result<String> deleteProduct(
            @Parameter(description = "产品ID", required = true) @PathVariable Long id) {
        
        try {
            Boolean success = productService.deleteProduct(id);
            if (success) {
                return Result.success("产品删除成功");
            } else {
                return Result.error("产品删除失败");
            }
        } catch (Exception e) {
            log.error("删除产品失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除产品", description = "批量逻辑删除产品信息")
    public Result<String> batchDeleteProducts(@RequestBody List<Long> ids) {
        
        try {
            Boolean success = productService.batchDeleteProducts(ids);
            if (success) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除产品失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/check-code")
    @Operation(summary = "检查产品货号", description = "检查产品货号是否已存在")
    public Result<Boolean> checkProductCode(
            @Parameter(description = "产品货号", required = true) @RequestParam String productCode,
            @Parameter(description = "排除的产品ID") @RequestParam(required = false) Long excludeId) {
        
        try {
            Boolean exists = productService.checkProductCodeExists(productCode, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查产品货号失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    @GetMapping("/check-name")
    @Operation(summary = "检查产品名称", description = "检查产品名称是否已存在")
    public Result<Boolean> checkProductName(
            @Parameter(description = "产品名称", required = true) @RequestParam String productName,
            @Parameter(description = "排除的产品ID") @RequestParam(required = false) Long excludeId) {
        
        try {
            Boolean exists = productService.checkProductNameExists(productName, excludeId);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("检查产品名称失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "获取产品统计", description = "获取产品相关统计信息")
    public Result<Map<String, Object>> getProductStats() {
        
        try {
            Map<String, Object> stats = productService.getProductStats();
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取产品统计失败", e);
            return Result.error("获取统计失败：" + e.getMessage());
        }
    }

    @GetMapping("/options")
    @Operation(summary = "获取产品选项", description = "获取产品选项列表，用于下拉选择")
    public Result<List<Map<String, Object>>> getProductOptions() {
        
        try {
            List<Map<String, Object>> options = productService.getProductOptions();
            return Result.success(options);
        } catch (Exception e) {
            log.error("获取产品选项失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "更新产品库存", description = "更新产品库存数量")
    public Result<String> updateProductStock(
            @Parameter(description = "产品ID", required = true) @PathVariable Long id,
            @Parameter(description = "数量", required = true) @RequestParam Integer quantity,
            @Parameter(description = "操作类型", required = true) @RequestParam String operation) {
        
        try {
            Boolean success = productService.updateProductStock(id, quantity, operation);
            if (success) {
                return Result.success("库存更新成功");
            } else {
                return Result.error("库存更新失败");
            }
        } catch (Exception e) {
            log.error("更新产品库存失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }
}