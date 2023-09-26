package com.example.demo.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String entity , String fieldName, String value) {
        super(String.format("%s không được tìm thấy bởi %s với giá trị %s" , entity,fieldName,value));
    }
}
