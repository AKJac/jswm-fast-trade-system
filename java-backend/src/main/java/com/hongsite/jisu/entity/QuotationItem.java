package com.hongsite.jisu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报价单明细实体类
 * 
 * @author 鸿思特科技有限公司
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quotation_items")
@Schema(description = "报价单明细信息")
public class QuotationItem {

    @Schema(description = "明细ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "报价单ID")
    @TableField("quotation_id")
    @NotNull(message = "报价单ID不能为空")
    private Long quotationId;

    @Schema(description = "产品ID")
    @TableField("product_id")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "产品货号")
    @TableField("product_code")
    @Size(max = 50, message = "产品货号长度不能超过50个字符")
    private String productCode;

    @Schema(description = "产品名称")
    @TableField("product_name")
    @Size(max = 200, message = "产品名称长度不能超过200个字符")
    private String productName;

    @Schema(description = "产品英文名称")
    @TableField("product_name_en")
    @Size(max = 200, message = "产品英文名称长度不能超过200个字符")
    private String productNameEn;

    @Schema(description = "规格")
    @TableField("specification")
    @Size(max = 200, message = "规格长度不能超过200个字符")
    private String specification;

    @Schema(description = "单位")
    @TableField("unit")
    @Size(max = 20, message = "单位长度不能超过20个字符")
    private String unit;

    @Schema(description = "数量")
    @TableField("quantity")
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    @Schema(description = "单价")
    @TableField("unit_price")
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;

    @Schema(description = "总价")
    @TableField("total_price")
    @NotNull(message = "总价不能为空")
    private BigDecimal totalPrice;

    @Schema(description = "备注")
    @TableField("notes")
    private String notes;

    @Schema(description = "创建时间")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // 关联字段（不映射到数据库）
    @TableField(exist = false)
    @Schema(description = "产品信息")
    private Product product;
}