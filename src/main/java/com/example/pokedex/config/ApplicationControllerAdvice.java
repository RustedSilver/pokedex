package com.example.pokedex.config;

import com.example.pokedex.web.dto.ErrorMessage;
import com.example.pokedex.web.dto.HttpRuntimeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler({HttpRuntimeException.class})
    public ResponseEntity<ErrorMessage> handlerHttpRuntimeExceptions(HttpRuntimeException ex) {
        final ErrorMessage e = new ErrorMessage(ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(e);
    }

    @ExceptionHandler({HttpServerErrorException.class})
    public ResponseEntity<ErrorMessage> handlerHttpServerErrorException(HttpServerErrorException ex) {
        final ErrorMessage e = new ErrorMessage(ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode().value()).body(e);
    }
}
