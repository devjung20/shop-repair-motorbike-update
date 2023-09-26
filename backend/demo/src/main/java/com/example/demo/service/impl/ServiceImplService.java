package com.example.demo.service.impl;

import com.example.demo.entity.AccountEntity;
import com.example.demo.entity.ServiceEntity;
import com.example.demo.exception.BaseException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.params.ServiceParams;
import com.example.demo.model.request.ServiceRequest;
import com.example.demo.model.response.AccountCreateAndUpdate;
import com.example.demo.model.response.BaseService;
import com.example.demo.model.response.ServiceResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ServiceRepository;
import com.example.demo.security.CustomUserDetail;
import com.example.demo.service.ServiceService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceImplService implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final AccountRepository accountRepository;

    public ServiceImplService(ServiceRepository serviceRepository, AccountRepository accountRepository) {
        this.serviceRepository = serviceRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public BaseResponse getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Object[]> objects = serviceRepository.getAll(pageable);
        List<BaseService> baseServices = objects.getContent().stream().map(objects1 -> {
            return this.objectToBase(objects1);
        }).collect(Collectors.toList());
        Page<BaseService> baseServicePage = new PageImpl<>(baseServices, pageable, objects.getTotalElements());
        return BaseResponse.builder()
                .message("nhận được tất cả các dịch vụ thành công.")
                .data(baseServicePage)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @Override
    public BaseResponse create(ServiceRequest serviceRequest) {
//        if (serviceRequest.getSalaryRepairer() + serviceRequest.getSalaryDispatcher() > serviceRequest.getPrice()) {
//            throw new BaseException(
//                    "Tổng lương nhân viên điều độ và nhân viên sửa chữa không lớn hơn giá dịch vụ.");
//        }
        ServiceEntity service = ServiceEntity.builder()
                .name(serviceRequest.getName())
                .price(serviceRequest.getPrice())
//                .salaryDispatcher(serviceRequest.getSalaryDispatcher())
//                .salaryRepairer(serviceRequest.getSalaryRepairer())
                .description(serviceRequest.getDescription())
                .build();
        service.setIsActive(true);
        service.setCreateBy(((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getAccount());
        ServiceEntity serviceSaved = serviceRepository.save(service);
        return BaseResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Tạo dịch vụ thành công.")
                .data(this.entityToResponse(serviceSaved, this.changeTypeAccount(serviceSaved.getCreateBy()), null))
                .build();
    }

    @Override
    public BaseResponse update(ServiceRequest serviceRequest, Long aLong) {
        ServiceEntity service = serviceRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Service", "Id", aLong.toString());
        });
        service.setName(serviceRequest.getName());
        service.setPrice(serviceRequest.getPrice());
//        service.setSalaryDispatcher(serviceRequest.getSalaryDispatcher());
//        service.setSalaryRepairer(serviceRequest.getSalaryRepairer());
        service.setDescription(serviceRequest.getDescription());
        // service.setStatus(serviceRequest.getStatus());
        service.setModifyBy(((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getAccount());
        serviceRepository.save(service);
        return BaseResponse.builder()
                .message("Cập nhật dịch vụ thành công.")
                .statusCode(HttpStatus.OK.value())
                .data(null)
                .build();
    }

    @Override
    public BaseResponse deleteById(Long aLong) {
        ServiceEntity service = serviceRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Service", "Id", aLong.toString());
        });
        service.setIsActive(false);
        serviceRepository.save(service);
        return BaseResponse.builder()
                .data(null)
                .message("xóa dịch vụ thành công theo id = " + aLong)
                .build();
    }

    @Override
    public BaseResponse getById(Long aLong) {
        ServiceEntity service = serviceRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Service", "Id", aLong.toString());
        });
        return BaseResponse.builder()
                .data(this.entityToResponse(
                        service,
                        this.changeTypeAccount(service.getCreateBy()),
                        service.getModifyBy() == null ? null : this.changeTypeAccount(service.getModifyBy())))
                .statusCode(HttpStatus.OK.value())
                .message("nhận dịch vụ thành công bằng id = " + aLong)
                .build();
    }

    @Override
    public BaseResponse getByParams(Map<String, String> params) {
        ServiceParams serviceParams = new ServiceParams(params);
        Sort sort = Sort.by(serviceParams.getSortOrder(), serviceParams.getSortBy());
        Pageable pageable = PageRequest.of(serviceParams.getPageNumber() - 1, serviceParams.getPageSize(), sort);
        BaseService baseService = BaseService.builder()
                .name(serviceParams.getName())
                .code(serviceParams.getCode())
                .price(serviceParams.getPrice())
//                .salaryDispatcher(serviceParams.getSalaryDispatcher())
//                .salaryRepairer(serviceParams.getSalaryRepairer())
                .build();
        Page<Object[]> objects = this.serviceRepository.getByParamsInService(baseService, pageable);
        List<BaseService> baseServices = objects.getContent().stream().map(objects1 -> {
            return this.objectToBase(objects1);
        }).collect(Collectors.toList());
        Page<BaseService> baseServicePage = new PageImpl<>(baseServices, pageable, objects.getTotalElements());
        return BaseResponse.builder()
                .message("nhận được tất cả các dịch vụ thành công.")
                .data(baseServicePage)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    private ServiceResponse entityToResponse(ServiceEntity service, AccountCreateAndUpdate accountCreate,
            AccountCreateAndUpdate accountUpdate) {
        return ServiceResponse.builder()
                .id(service.getId())
                .code(service.getCode())
                .name(service.getName())
                .price(service.getPrice())
//                .salaryDispatcher(service.getSalaryDispatcher())
//                .salaryRepairer(service.getSalaryRepairer())
                .createAt(service.getCreateDate())
                .modifyAt(service.getModifyDate())
                .accountCreate(accountCreate)
                .accountModify(accountUpdate)
                .isActive(service.getIsActive())
                .description(service.getDescription())
                .build();
    }

    private AccountCreateAndUpdate changeTypeAccount(AccountEntity account) {
        return AccountCreateAndUpdate.builder()
                .id(account.getId())
                .name(account.getUserName())
                .build();
    }

    private BaseService objectToBase(Object[] objects) {
        return BaseService.builder()
                .id((Long) objects[0])
                .code((String) objects[3])
                .price((Double) objects[2])
                .name((String) objects[1])
//                .salaryDispatcher((Double) objects[4])
//                .salaryRepairer((Double) objects[5])
                .description((String) objects[4])
                .build();
    }
}
