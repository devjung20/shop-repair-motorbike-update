package com.example.demo.service.impl;

import com.example.demo.constant.CharConstant;
import com.example.demo.entity.AccountEntity;
import com.example.demo.entity.ForgotPasswordEntity;
import com.example.demo.event.PublisherEvent;
import com.example.demo.exception.BaseException;
import com.example.demo.jwt.JwtService;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.ConfirmCode;
import com.example.demo.model.request.ForgotPassword;
import com.example.demo.model.response.PasswordResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ForgotPasswordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class ForgotPasswordService {
    private final PublisherEvent publisherEvent;

    private final AccountRepository accountRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ForgotPasswordService(PublisherEvent publisherEvent, AccountRepository accountRepository,
            ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.publisherEvent = publisherEvent;
        this.accountRepository = accountRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public BaseResponse generateCode(ForgotPassword forgotPassword) {

        AccountEntity account = accountRepository.getByEmailAndIsActive(forgotPassword.getEmail(), true)
                .orElseThrow(() -> {
                    throw new BaseException("Tài khoản " + forgotPassword.getEmail() + " không tồn tại.");
                });
        List<ForgotPasswordEntity> forgotPasswordEntityList = forgotPasswordRepository
                .getAllByAccountCreate(account.getId());
        if (forgotPasswordEntityList.size() > 0) {
            forgotPasswordRepository.deleteAll(forgotPasswordEntityList);
        }
        String chars = CharConstant.CHARS;
        StringBuilder stringBuilderRandom = new StringBuilder();
        int length = chars.length();
        Random random = new Random();
        for (int i = 1; i <= 6; i++) {
            int number = random.nextInt(1, length);
            stringBuilderRandom.append(chars.charAt(number));
        }
        stringBuilderRandom.append(account.getId());
        ForgotPasswordEntity forgotPasswordEntity = ForgotPasswordEntity.builder()
                .code(stringBuilderRandom.toString())
                .accountCreate(account)
                .createAt(LocalDateTime.now())
                .build();
        publisherEvent.sendEmail(forgotPasswordEntity);
        forgotPasswordRepository.save(forgotPasswordEntity);
        return BaseResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Tạo mã thành công.")
                .build();
    }

    public BaseResponse confirmCode(ConfirmCode code) {
        ForgotPasswordEntity forgotPasswordEntity = forgotPasswordRepository.getByCode(code.getCode())
                .orElseThrow(() -> {
                    throw new BaseException("Mã không hợp lệ!");
                });
        Duration duration = Duration.between(forgotPasswordEntity.getCreateAt(), LocalDateTime.now());
        if (duration.getSeconds() > 5 * 60) {
            throw new BaseException("Mã đã hết hạn.");
        }
        // String token = this.jwtService.generateToken(new
        // CustomUserDetail(forgotPasswordEntity.getAccountCreate()));
        AccountEntity account = forgotPasswordEntity.getAccountCreate();
        String chars = CharConstant.CHARS;
        Random random = new Random();
        int length = chars.length();
        StringBuilder stringBuilderRandom = new StringBuilder();
        for(int i = 1 ; i <= 6 ;i++) {
            int number = random.nextInt(1 , length);
            stringBuilderRandom.append(chars.charAt(number));
        }
        account.setPassword(passwordEncoder.encode(stringBuilderRandom.toString()));
        accountRepository.save(account);

        publisherEvent.sendPassWordToEmail(PasswordResponse.builder().password(stringBuilderRandom.toString()).email(account.getEmail()).build());

        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Mật khẩu mới của bạn được xác nhận ở email.")
                .data(null)
                .build();
    }
}
