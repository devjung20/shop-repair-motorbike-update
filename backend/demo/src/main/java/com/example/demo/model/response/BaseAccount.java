package com.example.demo.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BaseAccount {
    private Long id;
    private String userName;
    private String type;
    private String email;
    private String employeeName;
    private Boolean isActive;
}
