package com.example.demo.model.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreRequest {
    private String phone;
    private String name;
    private String address;
    private String email;
    private Double vat;
}
