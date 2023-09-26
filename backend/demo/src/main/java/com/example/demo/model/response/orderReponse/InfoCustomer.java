package com.example.demo.model.response.orderReponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoCustomer {
    private Long id;
    private String code;
    private String name;
    private String phone;
    private String motorbikeName;
    private String motorbikeCode;
    private String email;
    private String address;
}
