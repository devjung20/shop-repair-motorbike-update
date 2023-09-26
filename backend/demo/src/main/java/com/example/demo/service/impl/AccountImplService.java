package com.example.demo.service.impl;

import com.example.demo.entity.AccountEntity;
import com.example.demo.entity.EmployeeEntity;
import com.example.demo.entity.enums.Role;
import com.example.demo.exception.BaseException;
import com.example.demo.exception.CreateAccountException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.jwt.JwtService;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.params.AccountParams;
import com.example.demo.model.request.AccountRequest;
import com.example.demo.model.request.LoginRequest;
import com.example.demo.model.response.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.security.CustomUserDetail;
import com.example.demo.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountImplService implements AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;

    public AccountImplService(PasswordEncoder passwordEncoder, AccountRepository accountRepository,
            AuthenticationManager authenticationManager, EmployeeRepository employeeRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.jwtService = jwtService;
    }

    @Override
    public BaseResponse getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<AccountEntity> accountEntityPage = accountRepository.findAll(pageable);
        List<BaseAccount> accountResponses = accountEntityPage.getContent().stream().map(account -> {
            return this.entityToBase(account);
        }).collect(Collectors.toList());
        Page<BaseAccount> accountResponsePage = new PageImpl<>(accountResponses, pageable,
                accountEntityPage.getTotalElements());
        return BaseResponse.builder()
                .message("Nhận tất cả tài khoản thành công!")
                .statusCode(HttpStatus.OK.value())
                .data(accountResponsePage)
                .build();
    }

    @Override
    public BaseResponse create(AccountRequest accountRequest) {
        Boolean existByEmail = accountRepository.existsByEmailAndIsActive(accountRequest.getEmail());
        if (existByEmail) {
            throw new CreateAccountException("Email đã được sử dụng");
        }
        AccountEntity account = AccountEntity.builder()
                .email(accountRequest.getEmail())
                .userName(accountRequest.getUsername())
                .password(passwordEncoder.encode(accountRequest.getPassword()))
                .role(Role.valueOf(accountRequest.getRole()))
                .build();
        account.setIsActive(true);
        accountRepository.save(account);
        return BaseResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Đã tạo tài khoản thành công.")
                .build();
    }

    @Override
    public BaseResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        String token = this.jwtService.generateToken(authentication.getPrincipal());
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Đăng nhập thành công.")
                .data(new LoginSuccessResponse(token))
                .build();
    }

    @Override
    public BaseResponse searchByManyConditions(BaseAccount account, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<AccountEntity> accountEntityPage = this.accountRepository.searchByManyConditions(account, pageable);
        List<BaseAccount> accounts = accountEntityPage.getContent().stream().map(account1 -> {
            return this.entityToBase(account1);
        }).collect(Collectors.toList());
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Nhận tài khoản theo nhiều điều kiện thành công.")
                .data(new PageImpl<>(accounts, pageable, accountEntityPage.getTotalElements()))
                .build();
    }

    @Override
    public BaseResponse update(AccountRequest accountRequest, Long aLong) {

        AccountEntity accountEntity = accountRepository.findById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Account", "Id", aLong.toString());
        });
        if (accountRequest.getEmail() != null && !accountRequest.getEmail().isEmpty()) {
            boolean emailExists = accountRepository.existsByEmailAndIsActive(accountRequest.getEmail());
            if (!accountRequest.getEmail().equals(accountEntity.getEmail()) && emailExists) {
                throw new CreateAccountException("Email đã được sử dụng");
            }
        }
//        Optional<AccountEntity> accountFindByEmail = accountRepository
//                .getAccountEntityByEmail(accountRequest.getEmail());
//        if (accountFindByEmail.isPresent() && accountFindByEmail.get().getId() != accountEntity.getId()) {
//            throw new CreateAccountException("Email đã được sử dụng");
//        }
        accountEntity.setRole(Role.valueOf(accountRequest.getRole()));
        accountEntity.setEmail(accountRequest.getEmail());
        accountEntity.setUserName(accountRequest.getUsername());
        if (accountRequest.getPassword() != null) {
            accountEntity.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        }
        if (accountRequest.getIsActive() != null) {
            accountEntity.setIsActive(accountRequest.getIsActive());
        }
//        if (accountRequest.getEmployeeId() != null) {
//            EmployeeEntity employee = employeeRepository.findById(accountRequest.getEmployeeId()).orElseThrow(() -> {
//                throw new EntityNotFoundException("Employee", "Id", accountRequest.getEmployeeId().toString());
//            });
//            if (accountRepository.existsByEmployeeAndIsActive(employee,true)) {
//                if (accountEntity.getEmployee() == null) {
//                    throw new CreateAccountException("nhân viên đã có tài khoản.");
//                } else if (accountEntity.getEmployee().getId() != employee.getId()) {
//                    throw new CreateAccountException("nhân viên đã có tài khoản.");
//                }
//            }
//            accountEntity.setEmployee(employee);
//
//        }

        accountEntity
                .setModifyBy(((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getAccount());
        accountRepository.save(accountEntity);
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật tài khoản thành công.")
                .data(null)
                .build();
    }

    @Override
    public BaseResponse deleteById(Long aLong) {
        AccountEntity accountEntity = accountRepository.getAccountById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Account", "Id", aLong.toString());
        });
        accountEntity.setIsActive(false);
        accountRepository.save(accountEntity);
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa tài khoản thành công.")
                .build();
    }

    @Override
    public BaseResponse getById(Long aLong) {
        AccountEntity accountEntity = accountRepository.getAccountById(aLong).orElseThrow(() -> {
            throw new EntityNotFoundException("Account", "Id", aLong.toString());
        });

        return BaseResponse.builder()
                .data(this.entityToResponse(accountEntity))
                .message("Nhận tài khoản theo id thành công.")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    @Override
    public BaseResponse getByParams(Map<String, String> params) {
        AccountParams accountParams = new AccountParams(params);
        Sort sort = Sort.by(accountParams.getSortOrder(), accountParams.getSortBy());
        Pageable pageable = PageRequest.of(accountParams.getPageNumber() - 1, accountParams.getPageSize(), sort);
        BaseAccount baseAccount = BaseAccount.builder()
                .userName(accountParams.getUsername())
                .email(accountParams.getEmail())
                .type(accountParams.getRole())
                .employeeName(accountParams.getEmployeeName())
                .build();
        Page<AccountEntity> accountEntityPage = accountRepository.searchByManyConditions(baseAccount, pageable);
        List<BaseAccount> accountResponses = accountEntityPage.getContent().stream().map(account -> {
            return this.entityToBase(account);
        }).collect(Collectors.toList());
        Page<BaseAccount> accountResponsePage = new PageImpl<>(accountResponses, pageable,
                accountEntityPage.getTotalElements());
        return BaseResponse.builder()
                .message("Nhận tất cả tài khoản thành công!")
                .statusCode(HttpStatus.OK.value())
                .data(accountResponsePage)
                .build();
    }

    private AccountResponse entityToResponse(AccountEntity account) {
        return AccountResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .username(account.getUserName())
                .role(account.getRole().toString())
                .createDate(account.getCreateDate())
                .modifyDate(account.getModifyDate())
                .createBy(account.getCreateBy() == null ? null
                        : new AccountCreateAndUpdate(account.getCreateBy().getId(),
                                account.getCreateBy().getUserName()))
                .modifyBy(account.getModifyBy() == null ? null
                        : new AccountCreateAndUpdate(account.getModifyBy().getId(),
                                account.getModifyBy().getUserName()))
//                .baseEmployee(account.getEmployee() == null ? null
//                        : new BaseEmployee(account.getEmployee().getId(), account.getEmployee().getCode(),
//                                account.getEmployee().getName(), account.getEmployee().getType().toString()))
                .build();
    }

    private BaseAccount entityToBase(AccountEntity account) {
        return BaseAccount.builder()
                .isActive(account.getIsActive())
                .id(account.getId())
                .email(account.getEmail())
                .userName(account.getUserName())
//                .employeeName(account.getEmployee() == null ? null : account.getEmployee().getName())
                .type(account.getRole().toString())
                .build();
    }

}
