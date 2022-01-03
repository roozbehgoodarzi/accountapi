package com.codefactory.accountapi.service.exception;

public class AccountLockException extends Exception {

    private final String message;

    public AccountLockException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
