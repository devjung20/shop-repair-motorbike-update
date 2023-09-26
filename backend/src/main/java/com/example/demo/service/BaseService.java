package com.example.demo.service;

import com.example.demo.model.BaseResponse;

import java.util.Map;

public interface BaseService<T,K> {
    BaseResponse getAll(int pageNumber , int pageSize);
    BaseResponse create(T t);
    BaseResponse update(T t , K k);
    BaseResponse deleteById(K k);
    BaseResponse getById(K k);
    BaseResponse getByParams(Map<String, String> params);

}
