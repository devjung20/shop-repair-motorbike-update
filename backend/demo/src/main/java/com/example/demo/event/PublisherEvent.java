package com.example.demo.event;

import com.example.demo.entity.ForgotPasswordEntity;
import com.example.demo.model.response.PasswordResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherEvent {
    private final ApplicationEventPublisher eventPublisher;

    public PublisherEvent(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void sendEmail(ForgotPasswordEntity forgotPassword) {
        eventPublisher.publishEvent(new Event(forgotPassword));
    }

    public void sendPassWordToEmail(PasswordResponse passwordResponse) {
        eventPublisher.publishEvent(new PasswordEvent(passwordResponse));
    }
}
