package com.example.ebankify.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {

        super(message);
    }
}
