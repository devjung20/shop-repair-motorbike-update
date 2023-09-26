package com.example.demo.service.impl;

import com.example.demo.entity.StoreEntity;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.StoreRequest;
import com.example.demo.model.response.StoreResponse;
import com.example.demo.repository.StoreRepository;
import com.example.demo.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StoreImplService implements StoreService {

    private final StoreRepository storeRepository;

    public StoreImplService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public BaseResponse getAll(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public BaseResponse create(StoreRequest storeRequest) {
        StoreEntity store = new StoreEntity();
        if (storeRepository.findAll().size() != 0) {
            store = storeRepository.findById(1L).get();
        }
        store.setName(storeRequest.getName());
        store.setPhone(storeRequest.getPhone());
        store.setAddress(storeRequest.getAddress());
        store.setEmail(storeRequest.getEmail());
        store.setVat(storeRequest.getVat());
        store.setIsActive(true);
        storeRepository.save(store);
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tạo hoặc cập nhật cửa hàng thành công.")
                .build();
    }

    @Override
    public BaseResponse update(StoreRequest storeRequest, Long aLong) {
        return null;
    }

    @Override
    public BaseResponse deleteById(Long aLong) {
        return null;
    }

    @Override
    public BaseResponse getById(Long aLong) {
        return null;
    }

    @Override
    public BaseResponse getByParams(Map<String, String> params) {
        StoreEntity storeEntity = storeRepository.findById(1L).get();
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tạo hoặc cập nhật cửa hàng thành công.")
                .data(StoreResponse.builder()
                        .phone(storeEntity.getPhone())
                        .name(storeEntity.getName())
                        .address(storeEntity.getAddress())
                        .email(storeEntity.getEmail())
                        .vat(storeEntity.getVat())
                        .build())
                .build();
    }
}
