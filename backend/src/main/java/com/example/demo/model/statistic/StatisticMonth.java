package com.example.demo.model.statistic;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class StatisticMonth {
    private String year;
    private String month;
    private Double expense;
}
