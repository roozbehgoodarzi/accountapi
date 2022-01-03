package com.codefactory.accountapi.service.exception;

public class TransferValidationException extends Exception{

    private final String message;

    public TransferValidationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
