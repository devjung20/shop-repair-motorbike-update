package com.example.demo.model.response.orderReponse;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InfoProduct {
    private Long id;
    private String name;
    private Double price;
    private String unit;
    private Integer quantity;
    private Integer storageQuantity;
}
