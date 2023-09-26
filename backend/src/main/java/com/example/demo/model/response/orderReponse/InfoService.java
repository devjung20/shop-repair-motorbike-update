package com.example.demo.model.response.orderReponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoService {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
}
