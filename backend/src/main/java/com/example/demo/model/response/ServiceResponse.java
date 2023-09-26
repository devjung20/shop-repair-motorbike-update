package com.example.demo.model.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ServiceResponse {
    private Long id;
    private String code;
    private String name;
    private Double price;
    private Double salaryDispatcher;
    private Double salaryRepairer;
    private String description;
    private Boolean status;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    private Boolean isActive;
    private AccountCreateAndUpdate accountCreate;
    private AccountCreateAndUpdate accountModify;

}
