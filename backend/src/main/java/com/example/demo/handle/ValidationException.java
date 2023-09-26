package com.example.demo.handle;

import com.example.demo.exception.BaseException;
import com.example.demo.exception.CreateAccountException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.BaseResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        final String[] errorMessages = { "Bad request" };
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
            errorMessages[0] = error.getDefaultMessage();
        });
        return BaseResponse.builder()
                .message(errorMessages[0])
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .data(errors)
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse handleLoginFailed(Exception exception) {
        log.error(exception.getMessage());
        return BaseResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message("Đăng nhập không thành công. Vui lòng kiểm tra email và mật khẩu của bạn.")
                .data(null)
                .build();
    }

    @ExceptionHandler(CreateAccountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse createAccountFailedByEmail(Exception exception) {
        log.error(exception.getMessage());
        return BaseResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse handleEntityNotfound(EntityNotFoundException exception) {
        log.error(exception.getMessage());
        return BaseResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse handleBaseException(Exception exception) {
        log.error(exception.getMessage());
        return BaseResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse handleSendEmailFailed(Exception exception) {
        log.error(exception.getMessage());
        return BaseResponse.builder()
                .message("Gửi email không thành công.")
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }
}
