package com.docapp.api.exception;

public class BadRequestException extends Exception {
	private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
