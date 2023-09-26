package com.example.demo.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class InsertOrder {
    @Positive(message = "id phải lớn hơn 0.")
    @NotNull(message = "Nhân viên điều phối không được trống.")
    private Long dispatcherId;

    @Positive(message = "id phải lớn hơn 0.")
    @NotNull(message = "Khách hàng không được trống.")
    private Long customerId;

    @Positive(message = "id phải lớn hơn 0.")
    @NotNull(message = "Nhân viên sửa chữa không được trống.")
    private Long repairerId;

    @NotBlank(message = "Biển số xe là bắt buộc.")
    private String motorbikeCode;

    @NotBlank(message = "Loại xe là bắt buộc.")
    private String motorbikeName;

    private String type;

    private String note;
    private List<ServiceOrders> services;
    private List<ProductOrders> products;
}
