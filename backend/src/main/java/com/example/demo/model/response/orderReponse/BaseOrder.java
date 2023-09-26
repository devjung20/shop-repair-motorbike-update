package com.example.demo.model.response.orderReponse;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class BaseOrder {
    private Long id;
    private String code;
    private String customerName;
    private String customerCode;
    private String dispatcherName;
    private String repairerName;
    private String motorbikeCode;
    private LocalDateTime createDate;
    private String type;
}
