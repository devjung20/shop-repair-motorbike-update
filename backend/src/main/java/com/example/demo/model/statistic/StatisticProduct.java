package com.example.demo.model.statistic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticProduct {
    private Long id;
    private String code;
    private String name;
    private Double priceIn;
    private Double priceOut;
    private Long quantityExport;
    private Long quantityImport;
    private Double income;
}
