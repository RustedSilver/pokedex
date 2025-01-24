package com.example.pokedex.web.dto;

import org.springframework.http.HttpStatus;

public class HttpRuntimeException extends RuntimeException {
    private final HttpStatus statusCode;

    public HttpRuntimeException(HttpStatus statusCode, String message, Throwable tx) {
        super(message, tx);
        this.statusCode = statusCode;
    }

    public HttpRuntimeException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
