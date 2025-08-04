package com.hongsite.jisu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报价单实体类
 * 
 * @author 鸿思特科技有限公司
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quotations")
@Schema(description = "报价单信息")
public class Quotation {

    @Schema(description = "报价单ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "报价单号", example = "Q20241201001")
    @TableField("quotation_no")
    @NotBlank(message = "报价单号不能为空")
    @Size(max = 50, message = "报价单号长度不能超过50个字符")
    private String quotationNo;

    @Schema(description = "客户ID")
    @TableField("customer_id")
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @Schema(description = "客户名称")
    @TableField("customer_name")
    @Size(max = 200, message = "客户名称长度不能超过200个字符")
    private String customerName;

    @Schema(description = "联系人")
    @TableField("contact")
    @Size(max = 100, message = "联系人长度不能超过100个字符")
    private String contact;

    @Schema(description = "邮箱")
    @TableField("email")
    @Size(max = 200, message = "邮箱长度不能超过200个字符")
    private String email;

    @Schema(description = "电话")
    @TableField("phone")
    @Size(max = 50, message = "电话长度不能超过50个字符")
    private String phone;

    @Schema(description = "报价日期")
    @TableField("quotation_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime quotationDate;

    @Schema(description = "有效期至")
    @TableField("valid_until")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validUntil;

    @Schema(description = "货币", example = "USD", allowableValues = {"CNY", "USD", "EUR", "GBP", "JPY"})
    @TableField("currency")
    private String currency;

    @Schema(description = "小计金额")
    @TableField("subtotal")
    private BigDecimal subtotal;

    @Schema(description = "折扣率(%)")
    @TableField("discount_rate")
    private BigDecimal discountRate;

    @Schema(description = "折扣金额")
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    @Schema(description = "总金额")
    @TableField("total_amount")
    private BigDecimal totalAmount;

    @Schema(description = "付款条件")
    @TableField("payment_terms")
    @Size(max = 200, message = "付款条件长度不能超过200个字符")
    private String paymentTerms;

    @Schema(description = "交货条件")
    @TableField("delivery_terms")
    @Size(max = 200, message = "交货条件长度不能超过200个字符")
    private String deliveryTerms;

    @Schema(description = "特殊条款")
    @TableField("special_terms")
    private String specialTerms;

    @Schema(description = "备注")
    @TableField("notes")
    private String notes;

    @Schema(description = "状态", allowableValues = {"draft", "sent", "accepted", "rejected", "expired"})
    @TableField("status")
    private String status;

    @Schema(description = "创建者ID")
    @TableField("created_by")
    private Long createdBy;

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

    // 关联字段（不映射到数据库）
    @TableField(exist = false)
    @Schema(description = "客户信息")
    private Customer customer;

    @TableField(exist = false)
    @Schema(description = "报价单明细列表")
    private List<QuotationItem> items;
}