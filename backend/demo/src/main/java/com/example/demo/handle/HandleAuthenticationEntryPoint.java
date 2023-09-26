package com.example.demo.handle;

import com.example.demo.model.response.RestError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class HandleAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("authentication failed");
        RestError restError = new RestError();
        if(request.getAttribute("tokenError") != null || request.getAttribute("userNotFoundByEmail") != null) {
            restError.setStatusCode(HttpStatus.UNAUTHORIZED.toString());
            restError.setMessage(request.getAttribute("tokenError") != null ? request.getAttribute("tokenError").toString() :request.getAttribute("userNotFoundByEmail").toString());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else if (request.getAttribute("TokenIsNull") != null) {
            restError.setStatusCode(HttpStatus.UNAUTHORIZED.toString());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            restError.setMessage("Phải xác thực bằng mã thông báo!");
        }
//        else if (request.getRequestURI().equals("/error")) {
//            restError.setStatusCode(HttpStatus.NOT_FOUND.toString());
//            response.setStatus(HttpStatus.NOT_FOUND.value());
//            restError.setMessage("Request không hỗ trợ .");
//        }
        else {
            restError.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            restError.setStatusCode("INTERNAL_SERVER_ERROR");
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream,restError);
        outputStream.flush();
    }
}
