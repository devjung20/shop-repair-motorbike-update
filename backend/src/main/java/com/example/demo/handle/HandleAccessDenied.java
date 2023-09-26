package com.example.demo.handle;

import com.example.demo.model.response.RestError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

@Component
@Slf4j
public class HandleAccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("forbidden.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RestError restError = new RestError();
        restError.setStatusCode(HttpStatus.FORBIDDEN.toString());
        restError.setMessage("Bạn có Role là: " + authentication.getAuthorities() + " không có quyền truy cập vào: "
                + request.getRequestURL());

        ObjectMapper objectMapper = new ObjectMapper();
        String errorJson = objectMapper.writeValueAsString(restError);

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        PrintWriter writer = response.getWriter();
        writer.write(errorJson);
        writer.flush();
    }
}
