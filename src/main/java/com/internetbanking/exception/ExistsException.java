package com.internetbanking.exception;

public class ExistsException extends RuntimeException {
    public ExistsException(String message) {
        super(message);
    }
    public ExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public ExistsException(Throwable cause) {
        super(cause);
    }
    public ExistsException() {
    }
}
