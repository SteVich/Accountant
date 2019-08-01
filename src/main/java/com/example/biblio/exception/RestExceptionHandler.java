package com.example.biblio.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Log4j2
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity userNotFound(UserNotFoundException ex, WebRequest request) {
        log.debug("handling UserNotFoundException...");
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {InvalidJwtAuthenticationException.class})
    public ResponseEntity invalidJwtAuthentication(InvalidJwtAuthenticationException ex, WebRequest request) {
        log.debug("handling InvalidJwtAuthenticationException...");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity InvalidTokenException(InvalidTokenException ex, WebRequest request) {
        log.debug("handling InvalidTokenException...");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}