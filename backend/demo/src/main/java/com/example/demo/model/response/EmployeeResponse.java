package com.example.demo.model.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeResponse {
    private Long id;
    private String code;
    private String name;
    private Date birthday;
    private String phone;
    private String email;
    private Double salary;
    private String address;
    private String gender;
    private String type;
    private Boolean status;
    private Boolean isActive;
    private LocalDateTime createAt;
    private AccountCreateAndUpdate createBy;
    private LocalDateTime modifyAt;
    private AccountCreateAndUpdate modifyBy;
}
