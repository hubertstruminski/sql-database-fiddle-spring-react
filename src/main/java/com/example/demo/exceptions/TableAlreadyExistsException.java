package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TableAlreadyExistsException extends RuntimeException {

    public TableAlreadyExistsException(String message) {
        super(message);
    }
}
