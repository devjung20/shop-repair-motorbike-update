package com.example.demo.model.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticEmployees {
    private Long id;
    private String code;
    private String name;
    private Double salary;
    private String type;
    private Double incomeService;
    private Double total;
}
