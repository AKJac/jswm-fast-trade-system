package com.hongsite.jisu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品实体类
 * 
 * @author 鸿思特科技有限公司
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("products")
@Schema(description = "产品信息")
public class Product {

    @Schema(description = "产品ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "产品货号", example = "P20241201001")
    @TableField("product_code")
    @NotBlank(message = "产品货号不能为空")
    @Size(max = 50, message = "产品货号长度不能超过50个字符")
    private String productCode;

    @Schema(description = "产品分类", example = "knife",
            allowableValues = {"knife", "tool", "accessory", "packaging", "other"})
    @TableField("category")
    private String category;

    @Schema(description = "产品名称(中文)", example = "多功能厨房刀")
    @TableField("product_name")
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 200, message = "产品名称长度不能超过200个字符")
    private String productName;

    @Schema(description = "产品名称(英文)", example = "Multi-function Kitchen Knife")
    @TableField("product_name_en")
    @Size(max = 200, message = "产品英文名称长度不能超过200个字符")
    private String productNameEn;

    @Schema(description = "单位", example = "piece",
            allowableValues = {"piece", "set", "box", "carton", "meter", "kg", "ton", "other"})
    @TableField("unit")
    private String unit;

    @Schema(description = "产品状态", example = "bulk",
            allowableValues = {"order", "bulk", "discontinued", "developing"})
    @TableField("status")
    private String status;

    @Schema(description = "品牌", example = "鸿思特")
    @TableField("brand")
    @Size(max = 100, message = "品牌长度不能超过100个字符")
    private String brand;

    @Schema(description = "供应商货号", example = "HST-001")
    @TableField("supplier_item_no")
    @Size(max = 100, message = "供应商货号长度不能超过100个字符")
    private String supplierItemNo;

    @Schema(description = "刃色", example = "silver",
            allowableValues = {"silver", "black", "gold", "blue", "red", "other"})
    @TableField("blade_color")
    private String bladeColor;

    @Schema(description = "规格", example = "20cm")
    @TableField("specification")
    @Size(max = 200, message = "规格长度不能超过200个字符")
    private String specification;

    @Schema(description = "柄色", example = "black",
            allowableValues = {"black", "brown", "red", "blue", "green", "other"})
    @TableField("handle_color")
    private String handleColor;

    @Schema(description = "自定义参数")
    @TableField("custom_field")
    @Size(max = 200, message = "自定义参数长度不能超过200个字符")
    private String customField;

    @Schema(description = "尺寸", example = "20×5×2cm")
    @TableField("dimensions")
    @Size(max = 100, message = "尺寸长度不能超过100个字符")
    private String dimensions;

    @Schema(description = "重量(kg)", example = "0.5")
    @TableField("weight")
    private BigDecimal weight;

    @Schema(description = "材质", example = "不锈钢")
    @TableField("material")
    @Size(max = 100, message = "材质长度不能超过100个字符")
    private String material;

    @Schema(description = "包装", example = "彩盒包装")
    @TableField("packaging")
    @Size(max = 200, message = "包装长度不能超过200个字符")
    private String packaging;

    @Schema(description = "产品中文描述")
    @TableField("description_cn")
    private String descriptionCn;

    @Schema(description = "产品英文描述")
    @TableField("description_en")
    private String descriptionEn;

    @Schema(description = "销售价格")
    @TableField("sale_price")
    private BigDecimal salePrice;

    @Schema(description = "采购价格")
    @TableField("purchase_price")
    private BigDecimal purchasePrice;

    @Schema(description = "成本价格")
    @TableField("cost_price")
    private BigDecimal costPrice;

    @Schema(description = "货币", example = "USD", allowableValues = {"CNY", "USD", "EUR", "GBP", "JPY"})
    @TableField("currency")
    private String currency;

    @Schema(description = "当前库存")
    @TableField("current_stock")
    private Integer currentStock;

    @Schema(description = "安全库存")
    @TableField("safety_stock")
    private Integer safetyStock;

    @Schema(description = "库存位置", example = "A区-01-01")
    @TableField("stock_location")
    @Size(max = 100, message = "库存位置长度不能超过100个字符")
    private String stockLocation;

    @Schema(description = "产品图片URL数组")
    @TableField("images")
    private String images;

    @Schema(description = "是否启用")
    @TableField("is_active")
    private Boolean isActive;

    @Schema(description = "逻辑删除标志")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @Schema(description = "创建时间")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // 辅助字段：图片列表（不映射到数据库）
    @TableField(exist = false)
    @Schema(description = "图片列表")
    private List<String> imageList;
}