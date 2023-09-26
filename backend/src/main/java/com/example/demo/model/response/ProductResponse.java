package com.example.demo.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private Double priceIn;
    private Double priceOut;
    private String brand;
    private String description;
    private Integer storageQuantity;
    private String imageUrl;
    private String unit;
    private Boolean isActive;
    private Integer quantityWarning;
    private String status;
    private LocalDateTime createAt;
    private AccountCreateAndUpdate createBy;
    private LocalDateTime modifyAt;
    private AccountCreateAndUpdate modifyBy;
}
