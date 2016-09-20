package com.management.domain.exception;

public class InvalidValueException extends Exception{

    public InvalidValueException(){
        super();
    }

    public InvalidValueException(String message){
        super(message);
    }
}
