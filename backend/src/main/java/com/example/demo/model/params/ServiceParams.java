package com.example.demo.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceParams extends BaseParams {
    private String name;
    private Double price;
    private Double salaryDispatcher;
    private Double salaryRepairer;
    private String code;

    public ServiceParams(Map<String, String> map) {
        super(map);
        this.code = map.get("code");
        this.name = map.get("name");
        this.price = map.get("price") != null ? Double.valueOf(map.get("price")) : null;
        this.salaryDispatcher = map.get("price") != null ? Double.valueOf(map.get("salaryDispatcher")) : null;
        this.salaryRepairer = map.get("price") != null ? Double.valueOf(map.get("salaryRepairer")) : null;
    }
}
