package com.example.demo.service.impl;

import com.example.demo.entity.AccountEntity;
import com.example.demo.entity.EmployeeEntity;
import com.example.demo.entity.enums.Gender;
import com.example.demo.entity.enums.TypeEmployee;
import com.example.demo.exception.CreateAccountException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.params.EmployeeParams;
import com.example.demo.model.request.EmployeeRequest;
import com.example.demo.model.response.AccountCreateAndUpdate;
import com.example.demo.model.response.BaseEmployee;
import com.example.demo.model.response.EmployeeResponse;
import com.example.demo.model.response.EmployeeResponseAll;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import com.example.demo.security.CustomUserDetail;
import com.example.demo.utils.DateFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeImplService implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;

    public EmployeeImplService(EmployeeRepository employeeRepository, AccountRepository accountRepository) {
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public BaseResponse getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Object[]> employeeEntities = this.employeeRepository.getAllEmployee(pageable);
        List<EmployeeResponseAll> employeeResponseAlls = employeeEntities.getContent().stream().map(employee -> {
            return this.entityToResponseAll(employee);
        }).collect(Collectors.toList());

        Page<EmployeeResponseAll> employeeResponseAllPage = new PageImpl<>(employeeResponseAlls, pageable,
                employeeEntities.getTotalElements());
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Nhận tất cả nhân viên thành công.")
                .data(employeeResponseAllPage)
                .build();
    }

    @Override
    public BaseResponse create(EmployeeRequest employeeRequest) {
        try {
            boolean phoneExists = employeeRepository.existsByPhoneAndIsActive(employeeRequest.getPhone());
            boolean emailExists = employeeRepository.existsByEmailAndIsActive(employeeRequest.getEmail());

            if (phoneExists) {
                return BaseResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("Số điện thoại đã tồn tại trong hệ thống.")
                        .data(null)
                        .build();
            }
            if (emailExists) {
                return BaseResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("Email đã tồn tại trong hệ thống.")
                        .data(null)
                        .build();
            }

            EmployeeEntity employee = EmployeeEntity.builder()
                    .name(employeeRequest.getName())
                    .phone(employeeRequest.getPhone())
                    .email(employeeRequest.getEmail())
                    .address(employeeRequest.getAddress())
                    .birthday(DateFormat.stringToDate(employeeRequest.getBirthday()))
                    .salary(employeeRequest.getSalary())
                    .gender(Gender.valueOf(employeeRequest.getGender()))
                    .type(TypeEmployee.valueOf(employeeRequest.getType()))
//                    .isWorking(employeeRequest.getType().equalsIgnoreCase(TypeEmployee.REPAIRER.toString()) ? false : null)
                    .status(true)
                    .build();
            employee.setIsActive(true);
            AccountEntity accountCreate = ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal()).getAccount();
            employee.setCreateBy(accountCreate);
            EmployeeEntity employee1 = employeeRepository.save(employee);
            AccountCreateAndUpdate accountResponseCreate = this.changeTypeAccount(accountCreate);
            return BaseResponse.builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Tạo nhân viên thành công.")
                    .data(this.entityToResponse(employee1, null, accountResponseCreate))
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Tạo nhân viên không thành công!")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseResponse update(EmployeeRequest employeeRequest, Long aLong) {
        try {
            EmployeeEntity employee = employeeRepository.findById(aLong).orElseThrow(() -> {
                throw new EntityNotFoundException("Employee", "Id", aLong.toString());
            });
            if (employeeRequest.getPhone() != null && !employeeRequest.getPhone().isEmpty()) {
                boolean phoneExists = employeeRepository.existsByPhoneAndIsActive(employeeRequest.getPhone());
                if (!employeeRequest.getPhone().equals(employee.getPhone()) && phoneExists) {
                    return BaseResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Số điện thoại đã tồn tại trong hệ thống.")
                            .data(null)
                            .build();
                }
            }

            if (employeeRequest.getEmail() != null && !employeeRequest.getEmail().isEmpty()) {
                boolean emailExists = employeeRepository.existsByEmailAndIsActive(employeeRequest.getEmail());
                if (!employeeRequest.getEmail().equals(employee.getEmail()) && emailExists) {
                    return BaseResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Email đã tồn tại trong hệ thống.")
                            .data(null)
                            .build();
                }
            }

            employee.setName(employeeRequest.getName());
            employee.setBirthday(DateFormat.stringToDate(employeeRequest.getBirthday()));
            employee.setPhone(employeeRequest.getPhone());
            employee.setEmail(employeeRequest.getEmail());
            employee.setSalary(employeeRequest.getSalary());
            employee.setAddress(employeeRequest.getAddress());
            employee.setGender(Gender.valueOf(employeeRequest.getGender()));
            employee.setType(TypeEmployee.valueOf(employeeRequest.getType()));
            employee.setStatus(employeeRequest.getStatus() == null ? employee.getStatus() : employeeRequest.getStatus());
            employee.setIsActive(employee.getIsActive());
            employee.setModifyBy(((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getAccount());
            employeeRepository.save(employee);
            return BaseResponse.builder()
                    .message("Cập nhật nhân viên thành công.")
                    .statusCode(HttpStatus.OK.value())
                    .data(null)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Cập nhật nhân viên không thành công!")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseResponse deleteById(Long aLong) {
        EmployeeEntity employee = employeeRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Employee", "Id", aLong.toString());
        });
        employee.setIsActive(false);
        employeeRepository.save(employee);
        return BaseResponse.builder()
                .data(null)
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("Xóa nhân viên thành công.")
                .build();
    }

    @Override
    public BaseResponse getById(Long aLong) {
        EmployeeEntity employee = employeeRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Employee", "Id", aLong.toString());
        });
        AccountEntity accountCreate = employee.getCreateBy();
        AccountEntity accountModify = employee.getModifyBy();

        AccountCreateAndUpdate accountCreateInResponse = accountCreate != null ? this.changeTypeAccount(accountCreate)
                : null;
        AccountCreateAndUpdate accountUpdate = accountModify != null ? this.changeTypeAccount(accountModify) : null;
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Nhận nhân viên thành công.")
                .data(this.entityToResponse(employee, accountUpdate, accountCreateInResponse))
                .build();
    }

    @Override
    public BaseResponse getAllActive(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Object[]> employeeEntities = this.employeeRepository.getAllEmployeeActive(pageable);
        List<EmployeeResponseAll> employeeResponseAlls = employeeEntities.getContent().stream()
                .map(employee -> {
                    return this.entityToResponseAll(employee);
                }).collect(Collectors.toList());

        Page<EmployeeResponseAll> employeeResponseAllPage = new PageImpl<>(employeeResponseAlls, pageable,
                employeeEntities.getTotalElements());
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("tất cả nhân viên hoạt động thành công.")
                .data(employeeResponseAllPage)
                .build();
    }

    @Override
    public BaseResponse getEmployeeNoAccount() {
        List<Object[]> employeeObjs = employeeRepository.getAllByNoAccount();
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Nhận nhân viên không có tài khoản thành công.")
                .data(employeeObjs.stream().map(employeeObj -> {
                    return BaseEmployee.builder()
                            .id((Long) employeeObj[0])
                            .name((String) employeeObj[1])
                            .code((String) employeeObj[2])
                            .type((String) employeeObj[3])
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }

    private EmployeeResponse entityToResponse(EmployeeEntity employee, AccountCreateAndUpdate accountResponseModify,
                                              AccountCreateAndUpdate accountResponseCreate) {

        return EmployeeResponse.builder()
                .id(employee.getId())
                .code(employee.getCode())
                .name(employee.getName())
                .birthday(employee.getBirthday())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .salary(employee.getSalary())
                .address(employee.getAddress())
                .gender(employee.getGender().toString())
                .type(employee.getType().toString())
                .status(employee.getStatus())
                .isActive(employee.getIsActive())
                .createAt(employee.getCreateDate())
                .modifyBy(accountResponseModify)
                .modifyAt(employee.getModifyDate())
                .createBy(accountResponseCreate)
                .build();
    }

    private EmployeeResponseAll entityToResponseAll(Object[] employee) {
        return EmployeeResponseAll.builder()
                .code((String) employee[1])
                .id((Long) employee[0])
                .name((String) employee[2])
                .phone((String) employee[3])
                .type((String) employee[4])
                .salary((Double) employee[5])
                .status((Boolean) employee[6])
                .isActive((Boolean) employee[7])
                .build();
    }

    private AccountCreateAndUpdate changeTypeAccount(AccountEntity account) {
        return AccountCreateAndUpdate.builder()
                .id(account.getId())
                .name(account.getUserName())
                .build();
    }

    @Override
    public BaseResponse getByParams(Map<String, String> params) {

        EmployeeParams employeeParams = new EmployeeParams(params);

        Sort sort = Sort.by(
                employeeParams.getSortOrder(),
                employeeParams.getSortBy());

        Pageable pageable = PageRequest.of(
                employeeParams.getPageNumber() - 1,
                employeeParams.getPageSize(),
                sort);

        Page<EmployeeEntity> employees = employeeRepository.getAllByParams(
                employeeParams.getName(),
                employeeParams.getCode(),
                employeeParams.getPhone(),
                employeeParams.getEmail(),
                employeeParams.getType(),
                employeeParams.getStatus(),
//                employeeParams.getIsWorking(),
                pageable);

        System.out.println(employeeParams);

        List<EmployeeResponse> employeeResponsePage = employees.getContent().stream().map(employee -> {
            AccountCreateAndUpdate createBy = this.changeTypeAccount(employee.getCreateBy());
            AccountCreateAndUpdate modifyBy = employee.getModifyBy() == null ? null
                    : this.changeTypeAccount(employee.getModifyBy());
            return this.entityToResponse(employee, null, null);
        }).collect(Collectors.toList());
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Nhận nhân viên thành công.")
                .data(new PageImpl<>(employeeResponsePage, pageable, employees.getTotalElements()))
                .build();
    }
}