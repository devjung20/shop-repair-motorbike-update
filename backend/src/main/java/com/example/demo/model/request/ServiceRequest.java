package com.example.demo.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServiceRequest {
    @NotBlank(message = "Tên dịch vụ là bắt buộc.")
    private String name;

    @Min(value = 0, message = "Giá dịch vụ không thể âm")
    @NotNull(message = "Giá dịch vụ là bắt buộc.")
    private Double price;

//    @NotNull(message = "Tiền công của điều phối viên là bắt buộc.")
//    @Min(value = 0, message = "Tiền công của điều phối viên không thể âm")
//    private Double salaryDispatcher;
//
//    @NotNull(message = "Tiền công của thợ sửa chữa là bắt buộc")
//    @Min(value = 0, message = "Tiền công của thợ sửa chữa không được âm")
//    private Double salaryRepairer;

    private String description;

    private Boolean status;
}
