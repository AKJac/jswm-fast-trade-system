package com.hongsite.jisu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 供应商实体类
 * 
 * @author 鸿思特科技有限公司
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("suppliers")
@Schema(description = "供应商信息")
public class Supplier {

    @Schema(description = "供应商ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "供应商编号", example = "S20241201001")
    @TableField("supplier_no")
    @NotBlank(message = "供应商编号不能为空")
    @Size(max = 50, message = "供应商编号长度不能超过50个字符")
    private String supplierNo;

    @Schema(description = "供应商名称", example = "深圳精工制造有限公司")
    @TableField("supplier_name")
    @NotBlank(message = "供应商名称不能为空")
    @Size(max = 200, message = "供应商名称长度不能超过200个字符")
    private String supplierName;

    @Schema(description = "公司类型", example = "manufacturer",
            allowableValues = {"manufacturer", "trader", "agent", "service", "other"})
    @TableField("company_type")
    private String companyType;

    @Schema(description = "联系人", example = "李经理")
    @TableField("contact")
    @Size(max = 100, message = "联系人长度不能超过100个字符")
    private String contact;

    @Schema(description = "邮箱", example = "li@jinggong.com")
    @TableField("email")
    @Email(message = "邮箱格式不正确")
    @Size(max = 200, message = "邮箱长度不能超过200个字符")
    private String email;

    @Schema(description = "电话", example = "0755-12345678")
    @TableField("phone")
    @Size(max = 50, message = "电话长度不能超过50个字符")
    private String phone;

    @Schema(description = "地址")
    @TableField("address")
    private String address;

    @Schema(description = "主要产品")
    @TableField("main_products")
    private String mainProducts;

    @Schema(description = "信用等级", allowableValues = {"AAA", "AA", "A", "BBB", "BB", "B", "CCC", "CC", "C", "D"})
    @TableField("credit_rating")
    private String creditRating;

    @Schema(description = "付款条件", example = "30天付款")
    @TableField("payment_terms")
    @Size(max = 100, message = "付款条件长度不能超过100个字符")
    private String paymentTerms;

    @Schema(description = "对接报价单开关")
    @TableField("enable_quotation_api")
    private Boolean enableQuotationApi;

    @Schema(description = "对接销售单开关")
    @TableField("enable_sales_api")
    private Boolean enableSalesApi;

    @Schema(description = "API接口地址")
    @TableField("api_endpoint")
    @Size(max = 500, message = "API接口地址长度不能超过500个字符")
    private String apiEndpoint;

    @Schema(description = "备注")
    @TableField("notes")
    private String notes;

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
}