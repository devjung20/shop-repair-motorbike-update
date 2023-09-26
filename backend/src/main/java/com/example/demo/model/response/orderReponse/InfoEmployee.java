package com.example.demo.model.response.orderReponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoEmployee {
    private Long id;
    private String code;
    private String name;
    private String phone;
    private String email;
    private Boolean status;
}
