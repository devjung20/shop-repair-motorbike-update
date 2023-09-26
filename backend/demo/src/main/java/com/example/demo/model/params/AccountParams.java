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
public class AccountParams extends BaseParams {
    private String username;
    private String email;
    private String role;
    private String employeeName;

    public AccountParams(Map<String, String> map) {
        super(map);
        this.username = map.get("userName");
        this.email = map.get("email");
        this.role = map.get("type");
        this.employeeName = map.get(employeeName);
    }
}
