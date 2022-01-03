package com.codefactory.accountapi.service.exception;

public class InvalidAccountTypeException extends Exception{
    private final String message;

    public InvalidAccountTypeException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
