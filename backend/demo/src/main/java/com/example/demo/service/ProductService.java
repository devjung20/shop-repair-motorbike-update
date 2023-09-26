package com.example.demo.service;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService extends BaseService<ProductRequest, Long> {
    BaseResponse uploadImage(MultipartFile image) throws IOException;

    void addToInventory(Long productId, int difference, String note);

    void removeFromInventory(Long productId, int difference, String note);

    BaseResponse updateImage(MultipartFile file, long imageId);
}
