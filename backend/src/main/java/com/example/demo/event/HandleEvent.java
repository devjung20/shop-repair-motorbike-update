package com.example.demo.event;

import com.example.demo.email.EmailService;
import com.example.demo.entity.ForgotPasswordEntity;
import com.example.demo.model.response.PasswordResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableAsync
public class HandleEvent {
    private final EmailService emailService;

    public HandleEvent(EmailService emailService) {
        this.emailService = emailService;
    }
    @EventListener
    public void handleEventSendEmail(Event event) throws MessagingException {
        ForgotPasswordEntity forgotPassword = ((ForgotPasswordEntity)event.getSource());
        log.info(forgotPassword.getAccountCreate().getEmail());
        emailService.send( forgotPassword.getAccountCreate().getEmail(), "Mã xác nhận quên mật khẩu." ,forgotPassword.getCode());
    }

    @EventListener
    @Async
    public void handleSendPassword(PasswordEvent event) throws MessagingException {
        PasswordResponse passwordResponse = ((PasswordResponse) event.getSource());
        log.info("send password to " + passwordResponse.getEmail());
        emailService.send(passwordResponse.getEmail(), "Password của bạn được đặt lại là :", passwordResponse.getPassword());
    }
}
