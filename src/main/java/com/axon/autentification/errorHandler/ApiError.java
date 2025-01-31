package com.axon.autentification.errorHandler;

public class ApiError {
    private String message;
    private String field;

    public ApiError(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }
}