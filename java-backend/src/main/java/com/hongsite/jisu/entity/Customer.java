package com.hongsite.jisu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 客户实体类
 * 
 * @author 鸿思特科技有限公司
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("customers")
@Schema(description = "客户信息")
public class Customer {

    @Schema(description = "客户ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "客户编号", example = "C20241201001")
    @TableField("customer_no")
    @NotBlank(message = "客户编号不能为空")
    @Size(max = 50, message = "客户编号长度不能超过50个字符")
    private String customerNo;

    @Schema(description = "客户名称", example = "ABC Trading Company")
    @TableField("customer_name")
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 200, message = "客户名称长度不能超过200个字符")
    private String customerName;

    @Schema(description = "客户类型", example = "foreign_trade", 
            allowableValues = {"foreign_trade", "wholesale", "retail", "agent", "other"})
    @TableField("customer_type")
    private String customerType;

    @Schema(description = "客户来源", example = "website",
            allowableValues = {"website", "exhibition", "referral", "search_engine", "social_media", "other"})
    @TableField("customer_source")
    private String customerSource;

    @Schema(description = "联系人", example = "John Smith")
    @TableField("contact")
    @Size(max = 100, message = "联系人长度不能超过100个字符")
    private String contact;

    @Schema(description = "职位", example = "Purchasing Manager")
    @TableField("position")
    @Size(max = 100, message = "职位长度不能超过100个字符")
    private String position;

    @Schema(description = "邮箱", example = "john@abctrading.com")
    @TableField("email")
    @Email(message = "邮箱格式不正确")
    @Size(max = 200, message = "邮箱长度不能超过200个字符")
    private String email;

    @Schema(description = "电话", example = "+1-555-0123")
    @TableField("phone")
    @Size(max = 50, message = "电话长度不能超过50个字符")
    private String phone;

    @Schema(description = "国家/地区", example = "US")
    @TableField("country")
    @Size(max = 10, message = "国家/地区代码长度不能超过10个字符")
    private String country;

    @Schema(description = "客户等级", example = "A", allowableValues = {"A", "B", "C", "D"})
    @TableField("level")
    private String level;

    @Schema(description = "客户状态", example = "active",
            allowableValues = {"potential", "active", "inactive", "lost"})
    @TableField("status")
    private String status;

    @Schema(description = "跟进进度", example = "deal",
            allowableValues = {"initial", "understanding", "proposal", "quotation", "negotiation", "deal", "service"})
    @TableField("progress")
    private String progress;

    @Schema(description = "公司地址")
    @TableField("company_address")
    private String companyAddress;

    @Schema(description = "公司网站", example = "https://www.abctrading.com")
    @TableField("website")
    @Size(max = 200, message = "网站地址长度不能超过200个字符")
    private String website;

    @Schema(description = "主要产品")
    @TableField("main_products")
    private String mainProducts;

    @Schema(description = "年营业额")
    @TableField("annual_revenue")
    private BigDecimal annualRevenue;

    @Schema(description = "员工数量")
    @TableField("employee_count")
    private Integer employeeCount;

    @Schema(description = "成立时间")
    @TableField("established_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime establishedDate;

    @Schema(description = "合作开始时间")
    @TableField("cooperation_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cooperationStartDate;

    @Schema(description = "信用等级", allowableValues = {"AAA", "AA", "A", "BBB", "BB", "B", "CCC", "CC", "C", "D"})
    @TableField("credit_rating")
    private String creditRating;

    @Schema(description = "付款条件", example = "30天付款")
    @TableField("payment_terms")
    @Size(max = 100, message = "付款条件长度不能超过100个字符")
    private String paymentTerms;

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