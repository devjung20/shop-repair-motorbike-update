package com.example.demo.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank(message = "Tên khách hàng không được để trống!")
    private String name;
    @Size(min = 10, max = 12, message = "Số điện thoại phải có ít nhất 10 chữ số và tối đa 12 chữ số!")
    private String phone;
    private String address;
    @Email(message = "Email không hợp lệ!", regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;
}
