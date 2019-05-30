package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RestController
public class CustomExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, WebRequest request) {
        UsernameAlreadyExistsResponse exceptionResponse = new UsernameAlreadyExistsResponse((ex.getMessage()));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
