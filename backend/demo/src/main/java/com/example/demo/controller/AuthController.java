package com.example.demo.controller;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.AccountRequest;
import com.example.demo.model.request.ConfirmCode;
import com.example.demo.model.request.ForgotPassword;
import com.example.demo.model.request.LoginRequest;
import com.example.demo.service.AccountService;
import com.example.demo.service.impl.ForgotPasswordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AccountService accountService;
    private final ForgotPasswordService forgotPasswordService;

    public AuthController(AccountService accountService, ForgotPasswordService forgotPasswordService) {
        this.accountService = accountService;
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        BaseResponse response = accountService.create(accountRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        BaseResponse response = accountService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponse> forgotPassword(@Valid @RequestBody ForgotPassword forgotPassword) {
        BaseResponse response = forgotPasswordService.generateCode(forgotPassword);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/forgot-password-confirm")
    public ResponseEntity<BaseResponse> confirmForgotPassword(@RequestBody ConfirmCode code) {
        BaseResponse response = forgotPasswordService.confirmCode(code);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
