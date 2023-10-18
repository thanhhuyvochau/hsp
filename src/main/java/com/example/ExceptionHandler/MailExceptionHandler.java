package com.example.ExceptionHandler;

public class MailExceptionHandler extends RuntimeException {
    public MailExceptionHandler(String message) {
        super(message);
    }
}
