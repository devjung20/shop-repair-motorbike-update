package com.example.demo.model.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AccountResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime modifyDate;
    private LocalDateTime createDate;
    private AccountCreateAndUpdate modifyBy;
    private AccountCreateAndUpdate createBy;
    private BaseEmployee baseEmployee;
}
