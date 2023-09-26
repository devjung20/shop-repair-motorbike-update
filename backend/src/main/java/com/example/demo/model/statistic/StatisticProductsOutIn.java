package com.example.demo.model.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticProductsOutIn {
    private Long id;
    private String code;
    private String name;
    private String unit;
    private Double priceOut;
    private Double priceIn;
    private Long quantity;
    private Double income;
}
