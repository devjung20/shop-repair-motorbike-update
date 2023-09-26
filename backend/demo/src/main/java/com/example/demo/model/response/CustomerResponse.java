package com.example.demo.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {
    private Long id;
    private String code;
    private String name;
    private String phone;
    private String address;
    private String email;
    private Boolean isActive;
    private LocalDateTime createAt;
    private AccountCreateAndUpdate createBy;
    private LocalDateTime modifyAt;
    private AccountCreateAndUpdate modifyBy;
}
