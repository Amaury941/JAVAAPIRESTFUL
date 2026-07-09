package com.example.demo.exception;

/**
 * EmailAlreadyExistsException
 */
public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(String message) {
        super(message);
    }

}
