package com.example.demo.model.statistic;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatisticTop {
    private Long id;
    private String name;
    private Double price;
    private String unit;
    private Long quantity;
}
