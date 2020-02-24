package com.herokuapp.flights.Exceptions;

public class FlightsException extends RuntimeException{

    public FlightsException(String message, Throwable throwable){
        super(message, throwable);
    }
}
