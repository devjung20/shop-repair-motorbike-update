package com.example.demo.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HistoryProductResponse {
    private Long id;
    private int difference;
    private String note;
    private int quantityLeft;
    private String unit;
    private String action;
    private String productName;
    private Double priceIn;
    private Double priceOut;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}