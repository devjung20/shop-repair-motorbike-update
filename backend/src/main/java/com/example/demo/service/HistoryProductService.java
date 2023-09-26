package com.example.demo.service;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.HistoryProductRequest;

import java.util.Map;

public interface HistoryProductService {

    BaseResponse getAllHistoryProduct(int pageNumber, int pageSize);

    BaseResponse searchByNameAndAction(int pageNumber, int pageSize, String productName, String action, String orderBy);

    BaseResponse createHistory(HistoryProductRequest historyProductRequest , Boolean check,Boolean checkImorEx);

    BaseResponse getHistoryProductByConditons(Map<String, String> map);
}
