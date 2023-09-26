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
public class CustomerParams extends BaseParams {
    private String code;
    private String name;
    private String phone;
    private String email;

    public CustomerParams(Map<String, String> params) {
        super(params);
        this.code = params.get("code");
        this.name = params.get("name");
        this.phone = params.get("phone");
        this.email = params.get("email");
    }
}
