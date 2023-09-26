package com.example.demo.service;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.InsertOrder;

public interface OrderService extends BaseService<InsertOrder , Long>{
    BaseResponse toInvoice(long orderId);
}
