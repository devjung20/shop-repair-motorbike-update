package com.example.demo.exception;

public class CreateAccountException extends RuntimeException{

    public CreateAccountException(String message) {
        super(message);
    }
}
