package com.example.demo.model.request;

import com.example.demo.validate.Author;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountRequest {

    @NotBlank(message = "tên người dùng là bắt buộc.")
    private String username;

    @Email(message = "Email không hợp lệ", regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank(message = "Email là bắt buộc.")
    private String email;

    // @NotBlank(message = "password is mandatory.")
    @Size(min = 4, max = 10, message = "độ dài mật khẩu lớn hơn 4 và nhỏ hơn 10.")
    private String password;

    @NotBlank(message = "vai trò là bắt buộc.")
    @Author
    private String role;

    @Positive(message = "Id nhân viên không thể nhỏ hơn 0")
    private Long employeeId;

    private Boolean isActive;
}
