package com.jisu.modules.product;

import com.jisu.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String sku;

    @NotNull
    private BigDecimal price;

    private String unit;

    private Integer stock;
}