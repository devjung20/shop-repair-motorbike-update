package com.example.demo.model.response;

import lombok.Data;

@Data
public class LoginSuccessResponse {
    private String token;

    public LoginSuccessResponse(String token) {
        this.token = token;
    }
}
