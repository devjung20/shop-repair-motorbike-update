package com.example.demo.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreResponse {
    private String phone;
    private String name;
    private String address;
    private String email;
    private Double vat;
}
