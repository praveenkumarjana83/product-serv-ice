package com.jpk.productservice.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
}
