package com.example.demo.model.request;

import com.example.demo.validate.Age;
import com.example.demo.validate.EmployeeType;
import com.example.demo.validate.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeRequest {

    @NotBlank(message = "Tên là bắt buộc.")
    private String name;

    @NotBlank(message = "điện thoại là bắt buộc.")
    @Size(min = 10, max = 10, message = "số điện thoại phải có 10 chữ số.")
    private String phone;

    @Email(message = "Email không hợp lệ", regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank(message = "email là bắt buộc.")
    private String email;

    @Age
    private String birthday;

    @DecimalMin(value = "0.0", message = "Mức lương phải lớn hơn 0")
    @NotNull(message = "Mức lương là bắt buộc.")
    private Double salary;

    @Gender
    private String gender;

    @EmployeeType
    private String type;

    @NotBlank(message = "địa chỉ là bắt buộc.")
    private String address;

    private Boolean status;

    private Boolean isActive;

}
