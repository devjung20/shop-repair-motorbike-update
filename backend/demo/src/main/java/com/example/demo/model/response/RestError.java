package com.example.demo.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestError {
    private String statusCode;
    private String message;
}
