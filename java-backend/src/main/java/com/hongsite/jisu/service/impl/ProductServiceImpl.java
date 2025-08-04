package com.hongsite.jisu.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongsite.jisu.common.PageResult;
import com.hongsite.jisu.entity.Product;
import com.hongsite.jisu.mapper.ProductMapper;
import com.hongsite.jisu.service.ProductService;
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
 * 产品服务层实现类
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public PageResult<Product> getProductPage(Long current, Long size, String productName, String productCode,
                                            String category, String status, String brand, String supplier) {
        // 构建查询条件
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getIsActive, true)
                   .and(StrUtil.isNotBlank(productName), qw -> qw
                       .like(Product::getProductName, productName)
                       .or()
                       .like(Product::getProductNameEn, productName))
                   .like(StrUtil.isNotBlank(productCode), Product::getProductCode, productCode)
                   .eq(StrUtil.isNotBlank(category), Product::getCategory, category)
                   .eq(StrUtil.isNotBlank(status), Product::getStatus, status)
                   .like(StrUtil.isNotBlank(brand), Product::getBrand, brand)
                   .orderByDesc(Product::getCreatedAt);

        // 分页查询
        Page<Product> page = new Page<>(current, size);
        IPage<Product> result = this.page(page, queryWrapper);

        // 处理图片列表
        result.getRecords().forEach(this::processProductImages);

        return PageResult.build(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public Product getProductById(Long id) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getId, id)
                   .eq(Product::getIsActive, true);
        
        Product product = this.getOne(queryWrapper);
        if (product != null) {
            processProductImages(product);
        }
        return product;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveProduct(Product product) {
        // 生成产品货号
        product.setProductCode(generateProductCode());
        
        // 设置默认值
        if (product.getIsActive() == null) {
            product.setIsActive(true);
        }
        if (StrUtil.isBlank(product.getCategory())) {
            product.setCategory("knife");
        }
        if (StrUtil.isBlank(product.getUnit())) {
            product.setUnit("piece");
        }
        if (StrUtil.isBlank(product.getStatus())) {
            product.setStatus("bulk");
        }
        if (StrUtil.isBlank(product.getCurrency())) {
            product.setCurrency("USD");
        }
        if (product.getCurrentStock() == null) {
            product.setCurrentStock(0);
        }
        if (product.getSafetyStock() == null) {
            product.setSafetyStock(0);
        }

        // 处理图片列表
        processProductImagesForSave(product);
        
        return this.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProduct(Product product) {
        // 检查产品是否存在
        Product existProduct = getProductById(product.getId());
        if (existProduct == null) {
            throw new RuntimeException("产品不存在");
        }

        // 处理图片列表
        processProductImagesForSave(product);
        
        return this.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProduct(Long id) {
        // 检查产品是否存在
        Product product = getProductById(id);
        if (product == null) {
            throw new RuntimeException("产品不存在");
        }
        
        // 逻辑删除（设置is_active为false）
        product.setIsActive(false);
        return this.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchDeleteProducts(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("请选择要删除的产品");
        }
        
        List<Product> products = this.listByIds(ids);
        for (Product product : products) {
            product.setIsActive(false);
        }
        
        return this.updateBatchById(products);
    }

    @Override
    public Boolean checkProductCodeExists(String productCode, Long excludeId) {
        if (StrUtil.isBlank(productCode)) {
            return false;
        }
        
        Integer count = baseMapper.checkProductCodeExists(productCode, excludeId);
        return count > 0;
    }

    @Override
    public Boolean checkProductNameExists(String productName, Long excludeId) {
        if (StrUtil.isBlank(productName)) {
            return false;
        }
        
        Integer count = baseMapper.checkProductNameExists(productName, excludeId);
        return count > 0;
    }

    @Override
    public Map<String, Object> getProductStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总产品数
        Long totalProducts = this.lambdaQuery()
                .eq(Product::getIsActive, true)
                .count();
        stats.put("totalProducts", totalProducts);
        
        // 在售产品数
        Long activeProducts = this.lambdaQuery()
                .eq(Product::getIsActive, true)
                .eq(Product::getStatus, "bulk")
                .count();
        stats.put("activeProducts", activeProducts);
        
        // 低库存产品数
        Long lowStockProducts = baseMapper.getLowStockProductCount();
        stats.put("lowStockProducts", lowStockProducts);
        
        // 停产产品数
        Long discontinuedProducts = this.lambdaQuery()
                .eq(Product::getIsActive, true)
                .eq(Product::getStatus, "discontinued")
                .count();
        stats.put("discontinuedProducts", discontinuedProducts);
        
        // 产品分类统计
        List<Map<String, Object>> categoryStats = baseMapper.getProductCategoryStats();
        stats.put("categoryStats", categoryStats);
        
        // 产品状态统计
        List<Map<String, Object>> statusStats = baseMapper.getProductStatusStats();
        stats.put("statusStats", statusStats);
        
        // 品牌统计
        List<Map<String, Object>> brandStats = baseMapper.getProductBrandStats();
        stats.put("brandStats", brandStats);
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getProductOptions() {
        return baseMapper.getProductOptions();
    }

    @Override
    public String generateProductCode() {
        // 格式：P + YYYYMMDD + 3位随机数
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%03d", new Random().nextInt(1000));
        return "P" + dateStr + randomStr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProductStock(Long productId, Integer quantity, String operation) {
        Product product = getProductById(productId);
        if (product == null) {
            throw new RuntimeException("产品不存在");
        }
        
        Integer currentStock = product.getCurrentStock();
        if (currentStock == null) {
            currentStock = 0;
        }
        
        // 根据操作类型更新库存
        switch (operation.toLowerCase()) {
            case "add":
                product.setCurrentStock(currentStock + quantity);
                break;
            case "subtract":
                if (currentStock < quantity) {
                    throw new RuntimeException("库存不足");
                }
                product.setCurrentStock(currentStock - quantity);
                break;
            case "set":
                product.setCurrentStock(quantity);
                break;
            default:
                throw new RuntimeException("不支持的操作类型：" + operation);
        }
        
        return this.updateById(product);
    }

    /**
     * 处理产品图片（从JSON字符串转换为List）
     */
    private void processProductImages(Product product) {
        if (StrUtil.isNotBlank(product.getImages())) {
            try {
                List<String> imageList = JSONUtil.toList(product.getImages(), String.class);
                product.setImageList(imageList);
            } catch (Exception e) {
                log.warn("解析产品图片JSON失败: {}", e.getMessage());
            }
        }
    }

    /**
     * 处理产品图片（从List转换为JSON字符串保存）
     */
    private void processProductImagesForSave(Product product) {
        if (product.getImageList() != null && !product.getImageList().isEmpty()) {
            product.setImages(JSONUtil.toJsonStr(product.getImageList()));
        }
    }
}