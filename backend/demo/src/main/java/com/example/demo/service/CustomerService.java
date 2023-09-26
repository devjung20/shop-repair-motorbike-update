package com.example.demo.service;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.CustomerRequest;

public interface CustomerService extends BaseService<CustomerRequest, Long> {
    BaseResponse searchByNameAndPhoneAndEmail(int pageNumber, int pageSize
            , String name, String phone, String email, String orderBy);
}
