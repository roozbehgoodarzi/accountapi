package com.codefactory.accountapi.service.exception;

public class WithdrawValidationException extends Exception{
    private final String message;

    public WithdrawValidationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
