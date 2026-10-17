package com.cricket.exception;

public class CricketApiException extends RuntimeException {
    public CricketApiException(String message) {
        super(message);
    }

    public CricketApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
