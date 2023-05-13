package com.odisee.bibliotheek.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookContentNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(BookContentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String bookContentNotFoundHandler(BookContentNotFoundException ex) {
        return ex.getMessage();
    }
}
