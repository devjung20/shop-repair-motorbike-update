package com.example.demo.event;

import org.springframework.context.ApplicationEvent;

public class PasswordEvent extends ApplicationEvent {
    public PasswordEvent(Object source) {
        super(source);
    }
}
