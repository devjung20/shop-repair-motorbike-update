package com.example.demo.model.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryDetail {
    private Long productId;
    private Integer difference;
}
