package com.example.demo.service.impl;

import com.example.demo.entity.CustomerEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.params.CustomerParams;
import com.example.demo.model.request.CustomerRequest;
import com.example.demo.model.response.AccountCreateAndUpdate;
import com.example.demo.model.response.CustomerResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerImplService implements CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public CustomerImplService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public BaseResponse getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CustomerEntity> customerPage = customerRepository.getAllCustomersActive(pageable);
        List<CustomerResponse> customerResponses = customerPage.getContent().stream().map(customer -> {
            return this.entityToResponse(customer);
        }).collect(Collectors.toList());
        Page<CustomerResponse> customerResponsePage = new PageImpl<>(customerResponses, pageable,
                customerPage.getTotalElements());
        return BaseResponse.builder()
                .message("Nhận tất cả danh sách khách hàng thành công!")
                .statusCode(HttpStatus.OK.value())
                .data(customerResponsePage)
                .build();
    }

    @Override
    public BaseResponse create(CustomerRequest customerRequest) {
        try {
            boolean phoneExists = customerRepository.countByPhone(customerRequest.getPhone()) > 0;
            boolean emailExists = customerRepository.countByEmail(customerRequest.getEmail()) > 0;

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

            CustomerEntity customer = CustomerEntity.builder()
                    .name(customerRequest.getName())
                    .phone(customerRequest.getPhone())
                    .address(customerRequest.getAddress())
                    .email(customerRequest.getEmail())
                    .build();
            customer.setIsActive(true);
            CustomerEntity customerSave = customerRepository.save(customer);
            return BaseResponse.builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Tạo khách hàng thành công.")
                    .data(this.entityToResponse(customerSave))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Tạo khách hàng không thành công!")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseResponse update(CustomerRequest customerRequest, Long customerId) {
        try {
            CustomerEntity existingCustomer = customerRepository.findByIdAndIsActive(customerId, true)
                    .orElseThrow(() -> new EntityNotFoundException("Customer", "Id", customerId.toString()));
            if (customerRequest.getPhone() != null && !customerRequest.getPhone().isEmpty()) {
                boolean phoneExists = customerRepository.countByPhone(customerRequest.getPhone()) > 0;
                if (!customerRequest.getPhone().equals(existingCustomer.getPhone()) && phoneExists) {
                    return BaseResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Số điện thoại đã tồn tại trong hệ thống.")
                            .data(null)
                            .build();
                }
            }

            if (customerRequest.getEmail() != null && !customerRequest.getEmail().isEmpty()) {
                boolean emailExists = customerRepository.countByEmail(customerRequest.getEmail()) > 0;
                if (!customerRequest.getEmail().equals(existingCustomer.getEmail()) && emailExists) {
                    return BaseResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Email đã tồn tại trong hệ thống.")
                            .data(null)
                            .build();
                }
            }

            existingCustomer.setName(customerRequest.getName());
            existingCustomer.setPhone(customerRequest.getPhone());
            existingCustomer.setAddress(customerRequest.getAddress());
            existingCustomer.setEmail(customerRequest.getEmail());
            CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);

            return BaseResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Cập nhật khách hàng thành công.")
                    .data(this.entityToResponse(updatedCustomer))
                    .build();
        } catch (EntityNotFoundException e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Cập nhật khách hàng không thành công!")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseResponse deleteById(Long aLong) {
        CustomerEntity customerEntity = customerRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Customer", "Id", aLong.toString());
        });
        customerEntity.setIsActive(false);
        customerRepository.save(customerEntity);
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa khách hàng thành công.")
                .build();
    }

    @Override
    public BaseResponse getById(Long aLong) {
        CustomerEntity customerEntity = customerRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Customer", "Id", aLong.toString());
        });
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Nhận thông tin chi tiết khách hàng thành công.")
                .data(this.entityToResponse(customerEntity))
                .build();
    }

    private CustomerResponse entityToResponse(CustomerEntity customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .code(customer.getCode())
                .name(customer.getName())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .email(customer.getEmail())
                .isActive(customer.getIsActive())
                .createAt(customer.getCreateDate())
                .modifyAt(customer.getModifyDate())
                .build();
    }

    private AccountCreateAndUpdate changeTypeAccount(Long id) {
        Object[] customerFind = accountRepository.findAccountById(id).get(0);
        return AccountCreateAndUpdate.builder()
                .id((Long) customerFind[0])
                .name((String) customerFind[1])
                .build();
    }

    @Override
    public BaseResponse searchByNameAndPhoneAndEmail(int pageNumber, int pageSize, String name, String phone,
            String email, String orderBy) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<CustomerEntity> customerPage = customerRepository.findByNameAndPhoneAndEmail(name, phone, email,
                    orderBy, pageable);
            List<CustomerResponse> customerResponses = customerPage.getContent().stream().map(customer -> {
                return this.entityToResponse(customer);
            }).collect(Collectors.toList());

            Page<CustomerResponse> customerResponsePage = new PageImpl<>(customerResponses, pageable,
                    customerPage.getTotalElements());

            return BaseResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Tìm kiếm và lọc khách hàng thành công!")
                    .data(customerResponsePage)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Lỗi tìm kiếm và lọc khách hàng!")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseResponse getByParams(Map<String, String> params) {
        CustomerParams customerParams = new CustomerParams(params);

        Sort sort = Sort.by(customerParams.getSortOrder(), customerParams.getSortBy());

        Pageable pageable = PageRequest.of(
                customerParams.getPageNumber() - 1,
                customerParams.getPageSize(), sort);

        Page<CustomerEntity> customers = customerRepository.getAllByParams(
                customerParams.getCode(),
                customerParams.getName(),
                customerParams.getPhone(),
                customerParams.getEmail(),
                pageable);

        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Lấy được khách hàng thành công")
                .data(customers)
                .build();
    }
}
