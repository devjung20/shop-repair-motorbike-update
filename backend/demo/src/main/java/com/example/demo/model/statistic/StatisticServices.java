package com.example.demo.model.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticServices {
    private Long id;
    private String code;
    private Long quantity;
    private String name;
    private Double price;
//    private Double salaryDispatcher;
//    private Double salaryRepairer;
//    private Double profit;
    private Double income;
}
