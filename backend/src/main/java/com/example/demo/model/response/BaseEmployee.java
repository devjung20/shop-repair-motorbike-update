package com.example.demo.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BaseEmployee {
    private Long id;
    private String code;
    private String name;
    private String type;

}
