package com.example.demo.model.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class StatisticMonthlyUsage {
    private String year;
    private String month;
    private Double expense;
}
