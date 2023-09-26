package com.example.demo.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BaseService {
    private Long id;
    private String code;
    private Double price;
    private String name;
    private Double salaryDispatcher;
    private Double salaryRepairer;
    private String description;
    private String isActive;
}
