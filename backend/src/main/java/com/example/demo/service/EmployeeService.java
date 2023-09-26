package com.example.demo.service;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.EmployeeRequest;

public interface EmployeeService extends BaseService<EmployeeRequest,Long>{

    BaseResponse getAllActive(int pageNumber, int pageSize);

    BaseResponse getEmployeeNoAccount();
}
