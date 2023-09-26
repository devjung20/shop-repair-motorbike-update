package com.example.demo.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {

        private Long imageId;
        @NotBlank(message = "Tên sản phẩm không được để trống!")
        private String name;
        @NotNull(message = "Giá đầu vào không được để trống!")
        @Min(value = 0, message = "Giá đầu vào tối thiểu là 0")
        private Double priceIn;
        @NotNull(message = "Giá bán không được để trống!")
        @Min(value = 0, message = "Giá bán tối thiểu là 0")
        private Double priceOut;
        @NotBlank(message = "Thương hiệu sản phẩm không được để trống!")
        private String brand;
        private String description;
        @NotNull(message = "Số lượng lưu trữ sản phẩm không được để trống!")
        @Min(value = 0, message = "Số lượng lưu trữ phải lớn hơn hoặc bằng 0")
        private Integer storageQuantity;

        @Min(value = 1, message = "Số lượng sản phẩm cảnh cáo khi sắp hết hàng lớn hơn hoặc bằng 1")
        private Integer quantityWarning;

        private String unit;
}
