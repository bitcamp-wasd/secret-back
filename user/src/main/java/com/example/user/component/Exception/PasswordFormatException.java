package com.example.user.component.Exception;

public class PasswordFormatException extends RuntimeException{
    public PasswordFormatException(String messsage) {
        super(messsage);
    }
}
