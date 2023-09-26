package com.example.demo.model.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticService {
    private Long id;
    private String code;
    private String name;
    private Double price;
    private Long quantityService;
    private Double profit;
}
