package com.example.demo.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResponseAll {
    private Long id;
    private String code;
    private String name;
    private String phone;
    private String type;
    private Double salary;
    private Boolean status;
    private Boolean isActive;
}
