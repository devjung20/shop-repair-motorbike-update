package com.example.demo.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PasswordResponse {
    private String email;
    private String password;
}
