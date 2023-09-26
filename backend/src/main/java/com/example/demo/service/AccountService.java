package com.example.demo.service;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.AccountRequest;
import com.example.demo.model.request.LoginRequest;
import com.example.demo.model.response.BaseAccount;

public interface AccountService extends BaseService<AccountRequest, Long> {

    BaseResponse login(LoginRequest loginRequest);

    BaseResponse searchByManyConditions(BaseAccount account, int pageNumber, int pageSize);
}
