package com.herokuapp.flights.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<String> handleConnversion(RuntimeException ex) {

        return Mono.just(ex.getMessage() +":: "+ HttpStatus.BAD_REQUEST);
    }
}
